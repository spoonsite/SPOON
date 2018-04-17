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
package edu.usu.sdl.openstorefront.core.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
public class LookupModel
		implements Serializable
{

	public static final String CODE_FIELD = "code";
	public static final String DESCRIPTION_FIELD = "description";

	@ConsumeField
	@NotNull
	@Size(min = 1, max = 255)
	private String code;

	@ConsumeField
	@Size(max = 1000)
	private String description;

	@Override
	public String toString()
	{
		String result = "Code: " + code + "Description: " + description;
		try {
			result = StringProcessor.defaultObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		return result;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

}
