/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.describe.model;

import edu.usu.sdl.describe.parser.GeospatialCoverageConverter;
import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

/**
 *
 * @author dshurtleff
 */
@Root(strict = false)
@Convert(GeospatialCoverageConverter.class)
public class GeospatialCoverage
{
	private List<BoundingGeometry> boundingGeometries = new ArrayList<>();

	public GeospatialCoverage()
	{
	}

	public List<BoundingGeometry> getBoundingGeometries()
	{
		return boundingGeometries;
	}

	public void setBoundingGeometries(List<BoundingGeometry> boundingGeometries)
	{
		this.boundingGeometries = boundingGeometries;
	}

}
