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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.SystemTable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Defines a field type")
@SystemTable
public class CustomFieldType
		extends LookupEntity<CustomFieldType>
{

	public static final String TEXT_FIELD = "TEXT";
	public static final String RICH_TEXT_FIELD = "RICHTEXT";
	public static final String COMBO_FIELD = "COMBO";
	public static final String COMBO_EDIT_FIELD = "COMBOEDIT";
	public static final String CHECKBOX_FIELD = "CHECKBOX";

	public CustomFieldType()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();

		codeMap.put(TEXT_FIELD, newLookup(CustomFieldType.class, TEXT_FIELD, "Text"));
		codeMap.put(RICH_TEXT_FIELD, newLookup(CustomFieldType.class, RICH_TEXT_FIELD, "Rich Text"));
		codeMap.put(COMBO_FIELD, newLookup(CustomFieldType.class, COMBO_FIELD, "Combobox"));
		codeMap.put(COMBO_EDIT_FIELD, newLookup(CustomFieldType.class, COMBO_EDIT_FIELD, "Editable Combobox"));
		codeMap.put(CHECKBOX_FIELD, newLookup(CustomFieldType.class, CHECKBOX_FIELD, "Checkbox"));

		return codeMap;
	}

}
