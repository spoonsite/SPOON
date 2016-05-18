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
package edu.usu.sdl.describe.model;

import edu.usu.sdl.describe.parser.GeneralInfoConverter;
import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

/**
 *
 * @author dshurtleff
 */
@Root(strict = false)
@Convert(GeneralInfoConverter.class)
public class GeneralInfo
{
	@Attribute(name="value", required = false)
	@Path("identifier")		
	private String guid;
	
	@Element(name = "name")
	private String name;	
	private String nameClassification;

	@Element(name = "description")
	private String description;	
	private String descriptionClassification;
	
	@Element(name = "network")
	private String network;	
	
	private List<PointOfContact> contacts = new ArrayList<>();	
	
	public GeneralInfo()
	{
	}

	public String getGuid()
	{
		return guid;
	}

	public void setGuid(String guid)
	{
		this.guid = guid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getNameClassification()
	{
		return nameClassification;
	}

	public void setNameClassification(String nameClassification)
	{
		this.nameClassification = nameClassification;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getDescriptionClassification()
	{
		return descriptionClassification;
	}

	public void setDescriptionClassification(String descriptionClassification)
	{
		this.descriptionClassification = descriptionClassification;
	}

	public String getNetwork()
	{
		return network;
	}

	public void setNetwork(String network)
	{
		this.network = network;
	}

	public List<PointOfContact> getContacts()
	{
		return contacts;
	}

	public void setContacts(List<PointOfContact> contacts)
	{
		this.contacts = contacts;
	}
	
}
