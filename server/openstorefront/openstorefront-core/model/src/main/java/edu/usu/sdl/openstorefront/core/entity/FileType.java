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
@APIDescription("Defines file types (Data type)")
public class FileType
		extends LookupEntity<FileType>
{

	public static final String COMPONENT = "COMPONENT";
	public static final String ATTRIBUTE = "ATTRIBUTE";
	public static final String CHECKLISTQUESTIONS = "CHKQUESTIONS";
	public static final String SYSTEM = "SYSTEM";

	public FileType()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(COMPONENT, newLookup(FileType.class, COMPONENT, "Component"));
		codeMap.put(ATTRIBUTE, newLookup(FileType.class, ATTRIBUTE, "Attribute"));
		codeMap.put(CHECKLISTQUESTIONS, newLookup(FileType.class, CHECKLISTQUESTIONS, "Checklist Questions"));
		codeMap.put(SYSTEM, newLookup(FileType.class, SYSTEM, "System"));
		return codeMap;
	}

}
