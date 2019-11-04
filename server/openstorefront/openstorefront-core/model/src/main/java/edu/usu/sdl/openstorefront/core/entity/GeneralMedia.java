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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.Unique;
import edu.usu.sdl.openstorefront.validation.GeneralMediaUniqueHandler;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.nio.file.Path;
import javax.persistence.CascadeType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 * @author kbair
 */
@APIDescription("General media used for articles, bagdes, etc.")
public class GeneralMedia
		extends StandardEntity<GeneralMedia>
		implements MediaModel
{

	@PK
	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@Unique(GeneralMediaUniqueHandler.class)
	private String name;

	@NotNull
	@OneToOne(cascade = {CascadeType.ALL}, optional = false, orphanRemoval = true)
	@APIDescription("Stored file information")
	private MediaFile file;

	@ConsumeField
	private Boolean allowInBranding;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public GeneralMedia()
	{
	}

	/**
	 * Get the path to the media on disk.
	 *
	 * @return Path or null if this doesn't represent a disk resource
	 */
	public Path pathToMedia()
	{
		//Note: this may be ran from a proxy so don't use variable directly
		return (this.getFile() == null) ? null : this.getFile().path();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public MediaFile getFile()
	{
		return file;
	}

	@Override
	public void setFile(MediaFile file)
	{
		this.file = file;
	}

	public Boolean getAllowInBranding()
	{
		return allowInBranding;
	}

	public void setAllowInBranding(Boolean allowInBranding)
	{
		this.allowInBranding = allowInBranding;
	}

}
