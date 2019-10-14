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
package edu.usu.sdl.openstorefront.validation;

/**
 *
 * @author dshurtleff
 */
public class ValidationModel
{

	private Object dataObject;
	private boolean acceptNull = false;
	private boolean consumeFieldsOnly = false;
	private boolean sanitize = true;
	private boolean applyDefaults = true;

	public ValidationModel(Object dataObject)
	{
		this.dataObject = dataObject;
	}

	public static ValidationModel copy(ValidationModel original, Object newDataObject)
	{
		ValidationModel validateModel = new ValidationModel(newDataObject);
		validateModel.setAcceptNull(original.isAcceptNull());
		validateModel.setConsumeFieldsOnly(original.isConsumeFieldsOnly());
		validateModel.setSanitize(original.getSanitize());
		validateModel.setApplyDefaults(original.applyDefaults);
		return validateModel;
	}

	public Object getDataObject()
	{
		return dataObject;
	}

	public void setDataObject(Object dataObject)
	{
		this.dataObject = dataObject;
	}

	public boolean isAcceptNull()
	{
		return acceptNull;
	}

	public void setAcceptNull(boolean acceptNull)
	{
		this.acceptNull = acceptNull;
	}

	public boolean isConsumeFieldsOnly()
	{
		return consumeFieldsOnly;
	}

	public void setConsumeFieldsOnly(boolean consumeFieldsOnly)
	{
		this.consumeFieldsOnly = consumeFieldsOnly;
	}

	public boolean getSanitize()
	{
		return sanitize;
	}

	public void setSanitize(boolean sanitize)
	{
		this.sanitize = sanitize;
	}

	public boolean getApplyDefaults()
	{
		return applyDefaults;
	}

	public void setApplyDefaults(boolean applyDefaults)
	{
		this.applyDefaults = applyDefaults;
	}

}
