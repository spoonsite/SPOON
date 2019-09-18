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
package edu.usu.sdl.openstorefront.core.entity;

import javax.ws.rs.QueryParam;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
/**
 * This base abstraction for attributes
 *
 * @author gfowler
 */
@APIDescription("This is base abstraction for attributes")
public class AttributeSearchType
{
	@ConsumeField
    @QueryParam("type")
    private String type;
    
    @ConsumeField
    @QueryParam("unit")
    private String unit;
    
    @ConsumeField
    @QueryParam("typelabel")
    private String typelabel;
    
	@ConsumeField
    @QueryParam("code")	
	private String code;

	public AttributeSearchType()
	{
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String gettypelabel() {
		return typelabel;
	}

	public void settypelabel(String typelabel) {
		this.typelabel = typelabel;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
