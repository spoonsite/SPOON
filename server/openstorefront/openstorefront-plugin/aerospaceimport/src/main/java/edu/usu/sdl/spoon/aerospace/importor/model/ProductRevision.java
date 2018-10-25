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
public class ProductRevision
{

	@Element(name = "from_date")
	private String fromDate;

	@Element(name = "thru_date")
	private String thruDate;

	@Element
	private Specs specs;

	@Element
	private Additional additional;

	@Element(name = "product_type")
	private ProductType productType;

	@Element(name = "product_family")
	private ProductFamily productFamily;

	@Element
	private Provenance provenance;

	@Element(name = "comments")
	private Comment comment;

	@Element(name = "organizations")
	private Organizations organizations;

	public ProductRevision()
	{
	}

	public String getFromDate()
	{
		return fromDate;
	}

	public void setFromDate(String fromDate)
	{
		this.fromDate = fromDate;
	}

	public String getThruDate()
	{
		return thruDate;
	}

	public void setThruDate(String thruDate)
	{
		this.thruDate = thruDate;
	}

	public Specs getSpecs()
	{
		return specs;
	}

	public void setSpecs(Specs specs)
	{
		this.specs = specs;
	}

	public Additional getAdditional()
	{
		return additional;
	}

	public void setAdditional(Additional additional)
	{
		this.additional = additional;
	}

	public ProductType getProductType()
	{
		return productType;
	}

	public void setProductType(ProductType productType)
	{
		this.productType = productType;
	}

	public ProductFamily getProductFamily()
	{
		return productFamily;
	}

	public void setProductFamily(ProductFamily productFamily)
	{
		this.productFamily = productFamily;
	}

	public Provenance getProvenance()
	{
		return provenance;
	}

	public void setProvenance(Provenance provenance)
	{
		this.provenance = provenance;
	}

	public Comment getComment()
	{
		return comment;
	}

	public void setComment(Comment comment)
	{
		this.comment = comment;
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
