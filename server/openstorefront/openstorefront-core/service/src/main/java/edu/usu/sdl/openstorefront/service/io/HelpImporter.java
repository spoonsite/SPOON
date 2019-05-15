/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.usu.sdl.openstorefront.service.io;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.manager.Initializable;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.core.entity.ApplicationProperty;
import edu.usu.sdl.openstorefront.core.entity.HelpSection;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pegdown.PegDownProcessor;

/**
 * Handles reading in markdown help files
 *
 * @author dshurtleff
 */
public class HelpImporter
		implements Initializable
{

	private static final Logger LOG = Logger.getLogger(HelpImporter.class.getName());

	private static final long PROCESSING_TIMEOUT = 100000L;
	private static final String IMAGE_URL_REPLACE = "../webapp/";

	@Override
	public void initialize()
	{
		try {

			ServiceProxy serviceProxy = new ServiceProxy();

			//Check for file changes
			MessageDigest md = MessageDigest.getInstance("MD5");
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			try (InputStream is = HelpImporter.class.getResourceAsStream("/userhelp.md"); DigestInputStream dis = new DigestInputStream(is, md);) {
				FileSystemManager.getInstance().copy(dis, bout);
			}
			byte[] digest = md.digest();
			String checkSum = serviceProxy.getSystemService().getPropertyValue(ApplicationProperty.HELP_SYNC);

			boolean process = true;
			String newCheckSum = new String(digest);
			if (StringUtils.isNotBlank(checkSum) && checkSum.equals(newCheckSum)) {
				process = false;
			}

			if (process) {
				LOG.log(Level.INFO, "Loading Help");
				List<HelpSection> helpSections = processHelp(new ByteArrayInputStream(bout.toByteArray()));
				serviceProxy.getSystemService().loadNewHelpSections(helpSections);

				serviceProxy.getSystemService().saveProperty(ApplicationProperty.HELP_SYNC, newCheckSum);
				LOG.log(Level.INFO, "Done Loading Help");
			}

		} catch (NoSuchAlgorithmException | IOException ex) {
			throw new OpenStorefrontRuntimeException("Unable to load help.", "Check system and resource userhelp.md", ex);
		}
	}

	@Override
	public void shutdown()
	{
		//do nothing
	}

	@Override
	public boolean isStarted()
	{
		return true;
	}

	/**
	 * Accept a stream pointed to markdown
	 *
	 * @param in
	 * @return
	 */
	public List<HelpSection> processHelp(InputStream in)
	{
		List<HelpSection> helpSections = new ArrayList<>();

		String data = "";
		try (BufferedReader bin = new BufferedReader(new InputStreamReader(in))) {
			data = bin.lines().collect(Collectors.joining("\n"));
		} catch (IOException e) {
			LOG.log(Level.WARNING, "Unable to read help file", e);
		}

		PegDownProcessor pegDownProcessor = new PegDownProcessor(PROCESSING_TIMEOUT);
		String html = pegDownProcessor.markdownToHtml(data);
		Document doc = Jsoup.parse(html);
		Elements elements = doc.getAllElements();

		Set<String> headerTags = new HashSet<>();
		headerTags.add("h1");
		headerTags.add("h2");
		headerTags.add("h3");
		headerTags.add("h4");
		headerTags.add("h5");
		headerTags.add("h6");

		boolean capture = false;
		HelpSection helpSection = null;
		for (Element element : elements) {
			if (!headerTags.contains(element.tagName().toLowerCase())
					&& capture
					&& helpSection != null) {

				if (!helpSection.getContent().contains(element.outerHtml())) {
					helpSection.setContent(helpSection.getContent() + element.outerHtml());
				}
			}

			if (headerTags.contains(element.tagName().toLowerCase())) {
				String title = element.html();

				if (helpSection != null) {
					//save old section
					addHelpSection(helpSections, helpSection);
				}

				String titleSplit[] = title.split(" ");

				helpSection = new HelpSection();
				helpSection.setTitle(title);
				helpSection.setHeaderLevel(Convert.toInteger(element.tagName().toLowerCase().replace("h", "")));
				helpSection.setSectionNumber(titleSplit[0]);
				helpSection.setContent("");

				if (title.contains("@")) {
					String permissionRaw[] = title.split("@");
					helpSection.setPermission(permissionRaw[1]);
					helpSection.setTitle(permissionRaw[0].trim());
				}
				helpSection.setAdminSection(false);

				capture = true;
			}
		}
		//Add last section
		if (helpSection != null) {
			addHelpSection(helpSections, helpSection);
		}
		return helpSections;
	}

	private void addHelpSection(List<HelpSection> helpSections, HelpSection helpSection)
	{
		helpSection.setContent(helpSection.getContent().replace(IMAGE_URL_REPLACE, ""));
		helpSections.add(helpSection);
	}

}
