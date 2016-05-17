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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.util.EntityUtil;
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Used to represent a set of data")
public class ContentCollection
	extends StandardEntity<ContentCollection>		
{
	@PK(generated = true)
	@NotNull
	private String collectionId;
	
	@FK(ComponentResource.class)
	@NotNull
	private String resourceId;
	
	@ConsumeField
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)		
	private String name;
	
	@ConsumeField
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_16K)
	@Sanitize(HTMLSanitizer.class)	
	private String description;
	
	@ConsumeField
	private Date dataStartDate;
	
	@ConsumeField
	private Date dataEndDate;
	
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)		
	private String geospatialRegion;
	
	@ConsumeField
	@Min(0)
	@Max(Long.MAX_VALUE)
	private Long numberOfRecords;
	
	@ConsumeField
	@DataType(CollectionAttribute.class)
	@OneToMany(orphanRemoval = true)
	private List<CollectionAttribute> attributes;

	public ContentCollection()
	{
	}
	
	@Override
	public int compareTo(ContentCollection o)
	{		
		int value = super.compareTo(o);
		if (value == 0) {
			value = ReflectionUtil.compareObjects(getResourceId(), o.getResourceId());
		}
		if (value == 0) {
			value = EntityUtil.compareConsumeFields(this, o);
		}
		if (value == 0) {
			if (getAttributes() != null && o.getAttributes() == null) {
				value = -1;
			} else if (getAttributes() == null && o.getAttributes() == null) {
				value = 1;				
			} else {			
				Set<String> originAttributesSet = new HashSet<>();
				for (CollectionAttribute collectionAttribute : getAttributes()) {
					originAttributesSet.add(collectionAttribute.getAttributeType() + ReflectionUtil.COMPOSITE_KEY_SEPERATOR + collectionAttribute.getAttributeCode());							
				}
				for (CollectionAttribute collectionAttribute : o.getAttributes()) {
					String key = collectionAttribute.getAttributeType() + ReflectionUtil.COMPOSITE_KEY_SEPERATOR + collectionAttribute.getAttributeCode();
					if (originAttributesSet.contains(key) == false) {
						value = 1;
						break;
					}
				}
			}			
		}
		return value;
	}	

	public String getCollectionId()
	{
		return collectionId;
	}

	public void setCollectionId(String collectionId)
	{
		this.collectionId = collectionId;
	}

	public String getResourceId()
	{
		return resourceId;
	}

	public void setResourceId(String resourceId)
	{
		this.resourceId = resourceId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Date getDataStartDate()
	{
		return dataStartDate;
	}

	public void setDataStartDate(Date dataStartDate)
	{
		this.dataStartDate = dataStartDate;
	}

	public Date getDataEndDate()
	{
		return dataEndDate;
	}

	public void setDataEndDate(Date dataEndDate)
	{
		this.dataEndDate = dataEndDate;
	}

	public String getGeospatialRegion()
	{
		return geospatialRegion;
	}

	public void setGeospatialRegion(String geospatialRegion)
	{
		this.geospatialRegion = geospatialRegion;
	}

	public Long getNumberOfRecords()
	{
		return numberOfRecords;
	}

	public void setNumberOfRecords(Long numberOfRecords)
	{
		this.numberOfRecords = numberOfRecords;
	}

	public List<CollectionAttribute> getAttributes()
	{
		return attributes;
	}

	public void setAttributes(List<CollectionAttribute> attributes)
	{
		this.attributes = attributes;
	}
	
	
	
	
	
}
