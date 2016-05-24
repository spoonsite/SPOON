/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.describe.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 *
 * @author dshurtleff
 */
@Root(strict = false)
public class Security
{
	
	@Attribute(required = false)
	private String compliesWith;
	
	@Attribute(required = false)
	private String createDate;
	
	@Attribute(required = false)
	private String classification;
	
	@Attribute(required = false)
	private String ownerProducer;

	public Security()
	{
	}

	public String getCompliesWith()
	{
		return compliesWith;
	}

	public void setCompliesWith(String compliesWith)
	{
		this.compliesWith = compliesWith;
	}

	public String getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(String createDate)
	{
		this.createDate = createDate;
	}

	public String getClassification()
	{
		return classification;
	}

	public void setClassification(String classification)
	{
		this.classification = classification;
	}

	public String getOwnerProducer()
	{
		return ownerProducer;
	}

	public void setOwnerProducer(String ownerProducer)
	{
		this.ownerProducer = ownerProducer;
	}
	
}
