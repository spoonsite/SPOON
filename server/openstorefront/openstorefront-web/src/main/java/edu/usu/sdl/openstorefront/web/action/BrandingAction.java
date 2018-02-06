/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.action;

import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.core.entity.Branding;
import edu.usu.sdl.openstorefront.core.entity.GeneralMedia;
import static edu.usu.sdl.openstorefront.web.action.MediaAction.MISSING_IMAGE;
import static edu.usu.sdl.openstorefront.web.action.MediaAction.MISSING_MEDIA_IMAGE_SIZE;
import edu.usu.sdl.openstorefront.web.action.resolution.RangeResolutionBuilder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.validation.Validate;
import org.apache.commons.lang3.StringUtils;

/**
 * Handles Branding (dynamic pages)
 *
 * @author dshurtleff
 */
public class BrandingAction
		extends BaseAction
{

	private static final Logger log = Logger.getLogger(BrandingAction.class.getName());

	@Validate(required = true, on = "CSS")
	private String template;

	private Branding branding;

	@Validate(required = true, on = "GeneralMedia")
	private String name;

	@HandlesEvent("CSS")
	public Resolution cssPage()
	{
		branding = loadBranding();
		if (branding != null) {
			return new ForwardResolution("/WEB-INF/securepages/css/" + template);
		} else {
			return new ErrorResolution(404);
		}
	}

	@HandlesEvent("Override")
	public Resolution brandingCssOverride()
	{
		branding = loadBranding();
		if (branding != null) {
			String overrideCss = "";
			if (StringUtils.isNotBlank(branding.getOverrideCSS())) {
				overrideCss = branding.getOverrideCSS();
			}
			return new StreamingResolution("text/css", overrideCss);
		} else {
			return new ErrorResolution(404);
		}
	}

	@HandlesEvent("Preview")
	public Resolution previewBranding()
	{
		return new ForwardResolution("/WEB-INF/securepages/admin/application/brandingPreview.jsp");
	}

	@HandlesEvent("GeneralMedia")
	public Resolution generalMedia() throws FileNotFoundException
	{
		GeneralMedia generalMediaExample = new GeneralMedia();
		generalMediaExample.setName(name);
		GeneralMedia generalMedia = service.getPersistenceService().queryOneByExample(generalMediaExample);

		//restrict to media part of the branding
		if (!Convert.toBoolean(generalMedia.getAllowInBranding())) {
			generalMedia = null;
			log.log(Level.FINE, MessageFormat.format("General Media with name: {0} is restricted.", name));
		}

		if (generalMedia == null) {
			log.log(Level.FINE, MessageFormat.format("General Media with name: {0} is not found or was restricted.", name));
			return new StreamingResolution("image/png")
			{

				@Override
				protected void stream(HttpServletResponse response) throws Exception
				{
					try (InputStream in = new BrandingAction().getClass().getResourceAsStream(MISSING_IMAGE)) {
						FileSystemManager.getInstance().copy(in, response.getOutputStream());
					}
				}

			}.setFilename("MediaNotFound.png");
		}

		InputStream in;
		long length;
		Path path = generalMedia.pathToMedia();
		if (path != null && path.toFile().exists()) {
			in = new FileInputStream(path.toFile());
			length = path.toFile().length();
		} else {
			log.log(Level.WARNING, MessageFormat.format("Media not on disk: {0} Check general media record: {1} ", new Object[]{generalMedia.pathToMedia(), generalMedia.getName()}));
			in = new BrandingAction().getClass().getResourceAsStream(MISSING_IMAGE);
			length = MISSING_MEDIA_IMAGE_SIZE;
		}

		return new RangeResolutionBuilder()
				.setContentType(generalMedia.getFile().getMimeType())
				.setInputStream(in)
				.setTotalLength(length)
				.setRequest(getContext().getRequest())
				.setFilename(generalMedia.getFile().getOriginalName())
				.createRangeResolution();

	}

	public String getTemplate()
	{
		return template;
	}

	public void setTemplate(String template)
	{
		this.template = template;
	}

	public Branding getBranding()
	{
		return branding;
	}

	public void setBranding(Branding branding)
	{
		this.branding = branding;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

}
