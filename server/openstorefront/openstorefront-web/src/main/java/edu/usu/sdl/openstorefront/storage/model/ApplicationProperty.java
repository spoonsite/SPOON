/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.storage.model;

import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.PK;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jlaw
 */
public class ApplicationProperty
		extends BaseEntity
{

	@PK
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String key;

	@NotNull
	private String value;

	public static final String LOOKUP_IMPORTER_LAST_SYNC_DTS = "LOOKUPSYNCDTS";
	public static final String ATTRIBUTE_IMPORTER_LAST_SYNC_DTS = "ATTRIBUTSYNCDTS";
	public static final String ARTICLE_IMPORTER_LAST_SYNC_DTS = "ARTICLESYNCDTS";
	public static final String HIGHLIGHT_IMPORTER_LAST_SYNC_DTS = "HIGHLIGHTSYNCDTS";
	public static final String COMPONENT_IMPORTER_LAST_SYNC_DTS = "COMPONENTSYNCDTS";

	public ApplicationProperty()
	{
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

}
