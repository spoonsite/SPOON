/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.spoon.aerospace.importer.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 *
 * @author dshurtleff
 */
@Root(name = "float_feature")
public class FloatFeature
		extends BaseFeature
{

	@Element(name = "value", required = false)
	private Double value;

	@Element(name = "unit", required = false)
	private String unit;

	@Element(name = "unit_abbr", required = false)
	private String unitAbbr;

	public FloatFeature()
	{
	}

	public Double getValue()
	{
		return value;
	}

	public void setValue(Double value)
	{
		this.value = value;
	}

	public String getUnit()
	{
		return unit;
	}

	public void setUnit(String unit)
	{
		this.unit = unit;
	}

	public String getUnitAbbr()
	{
		return unitAbbr;
	}

	public void setUnitAbbr(String unitAbbr)
	{
		this.unitAbbr = unitAbbr;
	}

}
