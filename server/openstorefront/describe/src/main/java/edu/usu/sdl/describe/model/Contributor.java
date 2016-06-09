/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.describe.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 *
 * @author dshurtleff
 */
@Root(strict = false)
public class Contributor
{
	@Attribute(name="acronym", required = false)
	@Path("organization")		
	private String organizationAcronym;
	
	@Element(name="name", required = false)
	@Path("organization")
	private String organizationName;

	public Contributor()
	{
	}

	public String getOrganizationAcronym()
	{
		return organizationAcronym;
	}

	public void setOrganizationAcronym(String organizationAcronym)
	{
		this.organizationAcronym = organizationAcronym;
	}

	public String getOrganizationName()
	{
		return organizationName;
	}

	public void setOrganizationName(String organizationName)
	{
		this.organizationName = organizationName;
	}

}
