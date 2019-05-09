/*
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */
package edu.usu.sdl.openstorefront.web.rest.resource;

import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;

/**
 *
 * @author rfrazier
 */
public class UnitView
{
	@ConsumeField
	private String unit;
	private String dimension;
	private String standardUnit;

	public String getUnit()
	{
		return unit;
	}

	public void setUnit(String unit)
	{
		this.unit = unit;
	}

	public String getDimension()
	{
		return dimension;
	}

	public void setDimension(String dimension)
	{
		this.dimension = dimension;
	}

	public String getStandardUnit()
	{
		return standardUnit;
	}

	public void setStandardUnit(String standardUnit)
	{
		this.standardUnit = standardUnit;
	}
	
}
