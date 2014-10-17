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

package edu.usu.sdl.openstorefront.storage.model;

import edu.usu.sdl.openstorefront.util.PK;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jlaw
 */
public class CrossReference
{
	@PK
	@NotNull
	private String id;
	private String attributeType;
	private String localCode;
	private String externalCode;
	
	public CrossReference(){
		
	}

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @return the attributeType
	 */
	public String getAttributeType()
	{
		return attributeType;
	}

	/**
	 * @param attributeType the attributeType to set
	 */
	public void setAttributeType(String attributeType)
	{
		this.attributeType = attributeType;
	}

	/**
	 * @return the localCode
	 */
	public String getLocalCode()
	{
		return localCode;
	}

	/**
	 * @param localCode the localCode to set
	 */
	public void setLocalCode(String localCode)
	{
		this.localCode = localCode;
	}

	/**
	 * @return the externalCode
	 */
	public String getExternalCode()
	{
		return externalCode;
	}

	/**
	 * @param externalCode the externalCode to set
	 */
	public void setExternalCode(String externalCode)
	{
		this.externalCode = externalCode;
	}
	
}
