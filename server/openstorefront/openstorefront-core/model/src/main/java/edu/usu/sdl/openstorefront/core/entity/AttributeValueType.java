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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.SystemTable;
import static edu.usu.sdl.openstorefront.core.entity.LookupEntity.newLookup;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
@SystemTable
@APIDescription("Hold attribute value type")
public class AttributeValueType
		extends LookupEntity<AttributeValueType>
{

	public static final String NUMBER = "NUMBER";
	public static final String TEXT = "TEXT";

	public AttributeValueType()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(NUMBER, newLookup(AttributeValueType.class, NUMBER, "Number"));
		codeMap.put(TEXT, newLookup(AttributeValueType.class, TEXT, "Text"));
		return codeMap;
	}

}
