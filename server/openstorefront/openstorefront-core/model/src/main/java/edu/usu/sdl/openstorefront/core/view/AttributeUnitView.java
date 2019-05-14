/*
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import java.math.BigDecimal;

/**
 *
 * @author rfrazier
 */
public class AttributeUnitView
{

	private String unit;
	private BigDecimal conversionFactor;
	private BigDecimal convertedValue;

	public AttributeUnitView()
	{
	}

	public AttributeUnitView(String unit, BigDecimal conversionFactor)
	{
		this.unit = unit;
		this.conversionFactor = conversionFactor;
	}

	public String getUnit()
	{
		return unit;
	}

	public void setUnit(String unit)
	{
		this.unit = unit;
	}

	public BigDecimal getConversionFactor()
	{
		return conversionFactor;
	}

	public void setConversionFactor(BigDecimal conversionFactor)
	{
		this.conversionFactor = conversionFactor;
	}

	public BigDecimal getConvertedValue()
	{
		return convertedValue;
	}

	public void setConvertedValue(BigDecimal convertedValue)
	{
		this.convertedValue = convertedValue;
	}

}
