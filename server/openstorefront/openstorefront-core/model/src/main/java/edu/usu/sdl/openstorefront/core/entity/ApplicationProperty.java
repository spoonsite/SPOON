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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jlaw
 */
@APIDescription("This is used for system managed properties")
public class ApplicationProperty
		extends StandardEntity<ApplicationProperty>
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
	public static final String GLOBAL_INTEGRATION_REFRESH = "GLOBALINTREF";
	public static final String RECENT_CHANGE_EMAIL_LAST_DTS = "REMAILLASTDTS";
	public static final String HELP_SYNC = "HELPSYNC";
	public static final String PLUGIN_LAST_LOAD_MAP = "PLUGINLASTDTS";
	public static final String APPLICATION_CRYPT_KEY = "APPCRYPTKEY";

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public ApplicationProperty()
	{
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 23 * hash + Objects.hashCode(this.key);
		hash = 23 * hash + Objects.hashCode(this.value);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ApplicationProperty other = (ApplicationProperty) obj;
		if (!Objects.equals(this.key, other.key)) {
			return false;
		}
		if (!Objects.equals(this.value, other.value)) {
			return false;
		}
		return true;
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
