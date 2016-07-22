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
package edu.usu.sdl.openstorefront.service.io.mapper;

import edu.usu.sdl.openstorefront.core.model.AttributeAll;
import edu.usu.sdl.openstorefront.core.model.FileHistoryAll;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class AttributeMapper
	extends BaseMapper<AttributeAll>
{

	public AttributeMapper(DataTemplateEntity<AttributeAll> templateFactory, FileHistoryAll fileHistoryAll)
	{
		super(templateFactory, fileHistoryAll);
	}
	
	@Override
	public List<AttributeAll> mapData(MapModel input, Map<String, DataMapper> dataMappers)
	{
		List<AttributeAll> mappedAttributes = new ArrayList<>();
		
		
		return mappedAttributes;
	}
	
	private void doMapping(List<AttributeAll> mappedAttributes, MapModel root, Map<String, DataMapper> dataMappers, String fieldPath)
	{
		if (StringUtils.isNotBlank(fieldPath)) {
			fieldPath = fieldPath + ".";
		}
		
		for (MapField field : root.getMapFields()) {			
			String pathToField = fieldPath + root.getName() + "." +  field.getName();
			
			
			
		}
		
		for (MapModel child : root.getArrayFields()) {
			String newParent = root.getName();
			if (StringUtils.isNotBlank(fieldPath)) {				
				newParent = fieldPath  + root.getName();
			}
			doMapping(mappedAttributes, child, dataMappers, newParent);
		}
	}
	
}
