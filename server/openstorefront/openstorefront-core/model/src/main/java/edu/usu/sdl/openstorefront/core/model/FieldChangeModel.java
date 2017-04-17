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
package edu.usu.sdl.openstorefront.core.model;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.entity.LoggableModel;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class FieldChangeModel
{
	private String field;
	private String oldValue;
	private String newValue;

	public FieldChangeModel()
	{
	}

	public FieldChangeModel(String field, String oldValue, String newValue)
	{
		this.field = field;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public static <T> FieldChangeModel changedField(T original, T updated, String fieldname)
	{
		FieldChangeModel fieldChangeModel = null;
		
		try {
			Method originalMethod = original.getClass().getMethod("get" + StringUtils.capitalize(fieldname), (Class<?>[]) null);
			Method updatedMethod = updated.getClass().getMethod("get" + StringUtils.capitalize(fieldname), (Class<?>[]) null);
			
			Object resultOriginal = originalMethod.invoke(original, (Object[]) null);
			Object resultUpdated = updatedMethod.invoke(updated, (Object[]) null);
			
			if (ReflectionUtil.isFieldsDifferent(resultOriginal, resultUpdated)) 
			{
				fieldChangeModel = new FieldChangeModel(fieldname, 
						resultOriginal != null ? resultOriginal.toString() : null, 
						resultUpdated != null ? resultUpdated.toString() : null
				);			
			}			
			
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new OpenStorefrontRuntimeException(e);
		}
		
		return fieldChangeModel;
	}
	
	public static <T extends LoggableModel> List<FieldChangeModel> allChangedFields(Set<String> excludeFields, T original, T updated) 
	{
		Objects.requireNonNull(updated);
		
		List<FieldChangeModel> changes = new ArrayList<>();
	
		List<Field> fields = ReflectionUtil.getAllFields(original.getClass());
		for (Field field : fields) {
			String fieldname = field.getName();
			if (excludeFields.contains(fieldname ) == false) {
				FieldChangeModel change = FieldChangeModel.changedField(original, updated, fieldname);
				if (change != null) {
					changes.add(change);
				}
			}
		}
		
		return changes;
	}
	
	public String getField()
	{
		return field;
	}

	public void setField(String field)
	{
		this.field = field;
	}

	public String getOldValue()
	{
		return oldValue;
	}

	public void setOldValue(String oldValue)
	{
		this.oldValue = oldValue;
	}

	public String getNewValue()
	{
		return newValue;
	}

	public void setNewValue(String newValue)
	{
		this.newValue = newValue;
	}

}
