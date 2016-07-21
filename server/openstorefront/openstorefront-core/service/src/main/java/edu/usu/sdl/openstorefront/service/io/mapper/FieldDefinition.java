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

import java.util.Objects;

/**
 *
 * @author dshurtleff
 */
public class FieldDefinition
{
	private String field;
	private boolean rootField;

	public FieldDefinition()
	{
	}

	public FieldDefinition(String field)
	{
		this.field = field;
	}
	
	public FieldDefinition(String field, boolean rootField)
	{
		this.field = field;
		this.rootField = rootField;
	}

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 89 * hash + Objects.hashCode(this.field);
		hash = 89 * hash + (this.rootField ? 1 : 0);
		return hash;
	}

	@Override
	public String toString()
	{
		return "FieldDefinition{" + "field=" + field + ", rootField=" + rootField + '}';
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final FieldDefinition other = (FieldDefinition) obj;
		if (this.rootField != other.rootField) {
			return false;
		}
		if (!Objects.equals(this.field, other.field)) {
			return false;
		}
		return true;
	}
	
	public String getField()
	{
		return field;
	}

	public void setField(String field)
	{
		this.field = field;
	}

	public boolean getRootField()
	{
		return rootField;
	}

	public void setRootField(boolean rootField)
	{
		this.rootField = rootField;
	}
	
}
