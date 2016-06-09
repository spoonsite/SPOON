/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.describe.model;

import edu.usu.sdl.describe.parser.BoundingGeometryConverter;
import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

/**
 *
 * @author dshurtleff
 */
@Root(strict = false)
@Convert(BoundingGeometryConverter.class)
public class BoundingGeometry
{
	private List<String> points = new ArrayList<>();

	public BoundingGeometry()
	{
	}

	public List<String> getPoints()
	{
		return points;
	}

	public void setPoints(List<String> points)
	{
		this.points = points;
	}
	
}
