/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.report.generator;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.entity.ComponentMedia;
import edu.usu.sdl.openstorefront.core.entity.MediaFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import org.apache.commons.lang.StringUtils;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.resource.CSSResource;
import org.xhtmlrenderer.resource.ImageResource;
import org.xhtmlrenderer.resource.XMLResource;

/**
 *
 * @author dshurtleff
 */
public class LocalMediaPDFHandler
		implements UserAgentCallback
{

	private static final Logger LOG = Logger.getLogger(LocalMediaPDFHandler.class.getName());

	private final UserAgentCallback superUserAgent;
	private Service service;

	public LocalMediaPDFHandler(UserAgentCallback superUserAgent, Service service)
	{
		this.superUserAgent = superUserAgent;
		this.service = service;
	}

	@Override
	public CSSResource getCSSResource(String uri)
	{
		return superUserAgent.getCSSResource(uri);
	}

	@Override
	public ImageResource getImageResource(String uri)
	{
		ImageResource imageResource = null;
		if (uri.contains("Media.action")) {

			String query = uri.substring(uri.indexOf("?") + 1);
			Map<String, List<String>> queryMap = splitQuery(query);

			List<String> mediaIdList = queryMap.get("mediaId");
			if (mediaIdList != null && !mediaIdList.isEmpty()) {

				String mediaId = mediaIdList.get(0);
				MediaFile mediaFile = service.getPersistenceService().findById(MediaFile.class, mediaId);
				if (mediaFile == null) {
					ComponentMedia componentMedia = service.getPersistenceService().findById(ComponentMedia.class, mediaId);

					//NOTE: Filtering doesn't apply as this is inline media and as such already filtered.
					mediaFile = componentMedia.getFile();
				}

				if (mediaFile != null) {

					Path mediaPath = mediaFile.path();
					if (mediaPath != null) {
						try {
							Image image = Image.getInstance(Files.readAllBytes(mediaPath));
							FSImage fsImage = new ITextFSImage(image);
							imageResource = new ImageResource(uri, fsImage);
						} catch (IOException | BadElementException ex) {
							LOG.log(Level.WARNING, "Fail pulling local media for output.");
							LOG.log(Level.FINEST, null, ex);
						}
					}
				}
			}
		}

		if (imageResource == null) {
			imageResource = superUserAgent.getImageResource(uri);
		}
		return imageResource;
	}

	private Map<String, List<String>> splitQuery(String query)
	{
		if (StringUtils.isBlank(query)) {
			return Collections.emptyMap();
		}
		return Arrays.stream(query.split("&"))
				.map(this::splitQueryParameter)
				.collect(Collectors.groupingBy(SimpleImmutableEntry::getKey, LinkedHashMap::new, mapping(Map.Entry::getValue, toList())));
	}

	private SimpleImmutableEntry<String, String> splitQueryParameter(String it)
	{
		final int idx = it.indexOf("=");
		final String key = idx > 0 ? it.substring(0, idx) : it;
		final String value = idx > 0 && it.length() > idx + 1 ? it.substring(idx + 1) : null;
		return new SimpleImmutableEntry<>(key, value);
	}

	@Override
	public XMLResource getXMLResource(String uri)
	{
		return superUserAgent.getXMLResource(uri);
	}

	@Override
	public byte[] getBinaryResource(String uri)
	{
		return superUserAgent.getBinaryResource(uri);
	}

	@Override
	public boolean isVisited(String uri)
	{
		return superUserAgent.isVisited(uri);
	}

	@Override
	public void setBaseURL(String url)
	{
		superUserAgent.setBaseURL(url);
	}

	@Override
	public String getBaseURL()
	{
		return superUserAgent.getBaseURL();
	}

	@Override
	public String resolveURI(String uri)
	{
		return superUserAgent.resolveURI(uri);
	}

}
