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
package edu.usu.sdl.openstorefront.core.spi.parser.mapper;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class MapModel	
{	
	private String name;
	private List<MapField> mapFields = new ArrayList<>();
	private List<MapModel> arrayFields = new ArrayList<>();

	public MapModel()
	{		
	}

	public List<FieldDefinition> getUniqueFields() 
	{
		List<FieldDefinition> fieldDefinitions = new ArrayList<>();
		
		Set<String> fields = new HashSet<>();
		buildPaths(fields, this, "");
		
		for (String field : fields) {
			fieldDefinitions.add(new FieldDefinition(field));
		}
		
		Set<String> rootFields = new HashSet<>();
		
		//add roots
		for (String field : fields) {
			String parts[] = field.split("\\.");
			if (parts.length > 1) {
				Deque<String> deque = new ArrayDeque<>();				
				for (int i=0; i <  parts.length; i++) {
					deque.push(parts[i]);										
				}
				while (!deque.isEmpty()) {
					deque.pop();
					
					Iterator<String> reversed = deque.descendingIterator();
					StringBuilder sb = new StringBuilder();
					while(reversed.hasNext()) {
						sb.append(reversed.next()).append(".");
					}		
					if (sb.length() > 0) {
						sb = sb.deleteCharAt(sb.length()-1);
						rootFields.add(sb.toString());
					}
				}
			} else {
				rootFields.add(field);
			}
		}
		for (String field : rootFields) {
			fieldDefinitions.add(new FieldDefinition(field, true));
		}				
		
		return fieldDefinitions;
	}
	
	private void buildPaths(Set<String> fields,  MapModel root, String parent) 
	{
		if (StringUtils.isNotBlank(parent)) {
			parent = parent + ".";
		}
		
		for (MapField field : root.getMapFields()) {			
			fields.add(parent + root.getName() + "." +  field.getName());
		}
		for (MapModel child : root.getArrayFields()) {
			String newParent = root.getName();
			if (StringUtils.isNotBlank(parent)) {				
				newParent = parent  + root.getName();
			}
			buildPaths(fields, child, newParent);
		}	
	}
	
	public List<MapField> getMapFields()
	{
		return mapFields;
	}

	public void setMapFields(List<MapField> mapFields)
	{
		this.mapFields = mapFields;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<MapModel> getArrayFields()
	{
		return arrayFields;
	}

	public void setArrayFields(List<MapModel> arrayFields)
	{
		this.arrayFields = arrayFields;
	}
	
}
