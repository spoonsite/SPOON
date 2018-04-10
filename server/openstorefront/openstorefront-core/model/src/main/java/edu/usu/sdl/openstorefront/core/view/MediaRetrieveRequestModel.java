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
package edu.usu.sdl.openstorefront.core.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MediaRetrieveRequestModel
		implements Serializable
{

	@ConsumeField
	@NotNull
	@Size(min = 1, max = 2000)
	private String URL;

	@Override
	public String toString()
	{
		String result = "URL: " + URL;
		try {
			result = StringProcessor.defaultObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		return result;
	}

	public String getURL()
	{
		return URL;
	}

	public void setURL(String URL)
	{
		this.URL = URL;
	}

}
