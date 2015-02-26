/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.storage.model;

import au.com.bytecode.opencsv.CSVWriter;
import edu.usu.sdl.openstorefront.doc.APIDescription;
import edu.usu.sdl.openstorefront.doc.ConsumeField;
import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.io.ExportImport;
import edu.usu.sdl.openstorefront.util.Convert;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.PK;
import edu.usu.sdl.openstorefront.validation.BasicHTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
public abstract class LookupEntity
		extends BaseEntity
		implements ExportImport
{

	@PK
	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@APIDescription("Internal System code")
	protected String code;

	@NotNull
	@ConsumeField
	@Sanitize(TextSanitizer.class)
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_DESCRIPTION)
	@APIDescription("A label")
	protected String description;

	@ConsumeField
	@Sanitize(BasicHTMLSanitizer.class)
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_DETAILED_DESCRIPTION)
	@APIDescription("This is a long description")
	private String detailedDecription;

	@APIDescription("Used to force order")
	@ConsumeField
	@Min(0)
	@Max(Integer.MAX_VALUE)
	private Integer sortOrder;

	public LookupEntity()
	{
	}

	/**
	 * Override to add codes
	 *
	 * @return
	 */
	protected Map<String, LookupEntity> systemCodeMap()
	{
		return new HashMap<>();
	}

	public <T extends LookupEntity> List<T> systemValues()
	{
		List<T> lookups = new ArrayList<>();
		for (String key : systemCodeMap().keySet()) {
			T lookup = (T) systemCodeMap().get(key);
			lookups.add(lookup);
		}

		return lookups;
	}

	public <T extends LookupEntity> T systemValue(String code)
	{
		return (T) systemCodeMap().get(code);
	}

	public static <T extends LookupEntity> T newLookup(Class<T> lookupClass, String code, String description)
	{
		T lookup = null;
		try {
			lookup = lookupClass.newInstance();
			lookup.setActiveStatus(LookupEntity.ACTIVE_STATUS);
			lookup.setCode(code);
			lookup.setDescription(description);
		} catch (InstantiationException | IllegalAccessException ex) {
			throw new OpenStorefrontRuntimeException("Unable to create lookup", ex);
		}
		return lookup;
	}

	@Override
	public String export()
	{
		StringWriter stringWriter = new StringWriter();
		CSVWriter writer = new CSVWriter(stringWriter);
		writer.writeNext(new String[]{getCode(),
			getDescription(),
			getDetailedDecription(),
			getSortOrder() != null ? sortOrder.toString() : ""
		});
		return stringWriter.toString();
	}

	@Override
	public void importData(String[] data)
	{
		int CODE = 0;
		int DESCRIPTION = 1;
		int DETAILED_DESCRIPTION = 2;
		int SORT_ORDER = 3;

		if (data.length > DESCRIPTION) {

			setCode(data[CODE].trim().toUpperCase());
			setDescription(data[DESCRIPTION].trim());

			if (data.length > DETAILED_DESCRIPTION) {
				setDetailedDecription(data[DETAILED_DESCRIPTION].trim());
			}
			if (data.length > SORT_ORDER) {
				setSortOrder(Convert.toInteger(data[SORT_ORDER].trim()));
			}
		} else {
			throw new OpenStorefrontRuntimeException("Missing Required Fields: (Code, Description) Unable import the data.");
		}
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getDetailedDecription()
	{
		return detailedDecription;
	}

	public void setDetailedDecription(String detailedDecription)
	{
		this.detailedDecription = detailedDecription;
	}

	public Integer getSortOrder()
	{
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	@Override
	public int hashCode()
	{
		int hash = 0;
		hash += (code != null ? code.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object)
	{
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof LookupEntity)) {
			return false;
		}
		LookupEntity other = (LookupEntity) object;
		if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return this.getClass().getName() + "[ code=" + code + " ]";
	}

}
