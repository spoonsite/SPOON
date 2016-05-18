/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.describe.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 *
 * @author dshurtleff
 */
@Root(strict = false)
public class Edh
{
	@Element(name="Identifier")
	private String identifier;
	
	@Element(name="DataItemCreateDateTime")	
	private String createDts;
	
	@Element(name = "ResponsibleEntity")			
	private ResponsibleEntity responsibleEntity;
	
	@Element(name = "Security")
	private Security security;

	public Edh()
	{
	}

	public String getIdentifier()
	{
		return identifier;
	}

	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

	public String getCreateDts()
	{
		return createDts;
	}

	public void setCreateDts(String createDts)
	{
		this.createDts = createDts;
	}

	public ResponsibleEntity getResponsibleEntity()
	{
		return responsibleEntity;
	}

	public void setResponsibleEntity(ResponsibleEntity responsibleEntity)
	{
		this.responsibleEntity = responsibleEntity;
	}

	public Security getSecurity()
	{
		return security;
	}

	public void setSecurity(Security security)
	{
		this.security = security;
	}
	
}
