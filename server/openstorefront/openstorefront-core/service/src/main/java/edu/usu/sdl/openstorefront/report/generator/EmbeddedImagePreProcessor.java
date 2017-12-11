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
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.entity.ComponentMedia;
import edu.usu.sdl.openstorefront.core.entity.MediaFile;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.pdf.ITextFSImage;

/**
 *
 * @author dshurtleff
 */
public class EmbeddedImagePreProcessor
{

	private static final Logger LOG = Logger.getLogger(EmbeddedImagePreProcessor.class.getName());

	private static final int MAX_WIDTH_LETTER = 665;

	private Service service;

	public EmbeddedImagePreProcessor(Service service)
	{
		this.service = service;
	}

	public String processHtml(String originHtml)
	{
		String baseUri = PropertiesManager.getValueDefinedDefault(PropertiesManager.KEY_EXTERNAL_HOST_URL);
		Document document = Jsoup.parse(originHtml, baseUri);
		Elements images = document.select("img");
		updateImageNodes(images);
		return document.outerHtml();
	}

	private void updateImageNodes(Elements elements)
	{
		for (Element element : elements) {

			//see if it's local
			String source = element.attr("src");

			String query = source.substring(source.indexOf("?") + 1);
			Map<String, List<String>> queryMap = StringProcessor.splitURLQuery(query);

			List<String> mediaIdList = queryMap.get("mediaId");
			if (mediaIdList != null && !mediaIdList.isEmpty()) {

				boolean checkSizeNeeded = true;
				if (element.hasAttr("width")
						&& element.hasAttr("height")) {
					checkSizeNeeded = false;
				}

				if (checkSizeNeeded) {
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

								//force a max-size and scale
								BigDecimal percentage = BigDecimal.ONE;
								if (!element.hasAttr("width")) {

									int width = fsImage.getWidth();
									if (width > MAX_WIDTH_LETTER) {
										width = MAX_WIDTH_LETTER;

										BigDecimal originWidth = BigDecimal.valueOf(fsImage.getWidth());
										BigDecimal maxWidth = BigDecimal.valueOf(MAX_WIDTH_LETTER);
										percentage = BigDecimal.ONE.subtract(originWidth.subtract(maxWidth).divide(originWidth, 4, RoundingMode.HALF_UP));
									}

									element.attr("width", "" + width);
								}

								if (!element.hasAttr("height")) {

									BigDecimal originHeight = BigDecimal.valueOf(fsImage.getHeight());
									BigDecimal height = BigDecimal.valueOf(fsImage.getHeight());
									height = originHeight.multiply(percentage);
									element.attr("height", "" + height.intValue());
								}
							} catch (IOException | BadElementException ex) {
								LOG.log(Level.WARNING, "Fail pulling local media to deternine size.");
								LOG.log(Level.FINEST, null, ex);
							}
						}
					}
				}
			}

		}

	}

}
