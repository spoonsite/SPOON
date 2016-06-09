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
public class MetacardInfo
{
	@Attribute(name="value", required = false)
	@Path("identifier")		
	private String identifierValue;
	
	@Element(name="name", required = false)
	@Path("publisher/unknown")		
	private String publisher;

	@Attribute(name="created", required = false)
	@Path("dates")			
	private String createDate;

	public MetacardInfo()
	{
	}

	public String getIdentifierValue()
	{
		return identifierValue;
	}

	public void setIdentifierValue(String identifierValue)
	{
		this.identifierValue = identifierValue;
	}

	public String getPublisher()
	{
		return publisher;
	}

	public void setPublisher(String publisher)
	{
		this.publisher = publisher;
	}

	public String getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(String createDate)
	{
		this.createDate = createDate;
	}
	
	
	
}
