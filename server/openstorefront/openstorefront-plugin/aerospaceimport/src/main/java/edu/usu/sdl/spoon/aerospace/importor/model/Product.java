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
package edu.usu.sdl.spoon.aerospace.importor.model;

import org.simpleframework.xml.Element;

/**
 *
 * @author dshurtleff
 */
public class Product
{

	@Element
	private int key;

	@Element(name = "short_name")
	private String shortName;

	@Element(name = "long_name")
	private String longName;

	@Element(name = "product_source")
	private String productSource;

	@Element(name = "model_number")
	private String modelNumber;

	@Element
	private String description;

	@Element(name = "product_revision")
	private ProductRevision productRevision;

	@Element
	private Organizations organizations;

	public Product()
	{
	}

	public int getKey()
	{
		return key;
	}

	public void setKey(int key)
	{
		this.key = key;
	}

	public String getShortName()
	{
		return shortName;
	}

	public void setShortName(String shortName)
	{
		this.shortName = shortName;
	}

	public String getLongName()
	{
		return longName;
	}

	public void setLongName(String longName)
	{
		this.longName = longName;
	}

	public String getProductSource()
	{
		return productSource;
	}

	public void setProductSource(String productSource)
	{
		this.productSource = productSource;
	}

	public String getModelNumber()
	{
		return modelNumber;
	}

	public void setModelNumber(String modelNumber)
	{
		this.modelNumber = modelNumber;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public ProductRevision getProductRevision()
	{
		return productRevision;
	}

	public void setProductRevision(ProductRevision productRevision)
	{
		this.productRevision = productRevision;
	}

	public Organizations getOrganizations()
	{
		return organizations;
	}

	public void setOrganizations(Organizations organizations)
	{
		this.organizations = organizations;
	}

}
