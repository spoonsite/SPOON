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
package edu.usu.sdl.openstorefront.web.rest.model;

import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dshurtleff
 */
public class AttributeTypeView
{

	@NotNull
	private String type;

	@NotNull
	private String description;

	@NotNull
	private boolean visibleFlg;

	@NotNull
	private boolean requiredFlg;

	@NotNull
	private boolean architectureFlg;

	@NotNull
	private boolean importantFlg;

	@NotNull
	private boolean allowMutlipleFlg;

	@DataType(AttributeCodeView.class)
	private List<AttributeCodeView> codes = new ArrayList<>();

	public AttributeTypeView()
	{
	}

	public static AttributeTypeView toView(AttributeType attributeType)
	{
		AttributeTypeView attributeTypeView = new AttributeTypeView();
		attributeTypeView.setType(attributeType.getAttributeType());
		attributeTypeView.setAllowMutlipleFlg(attributeType.getAllowMutlipleFlg());
		attributeTypeView.setArchitectureFlg(attributeType.getArchitectureFlg());
		attributeTypeView.setDescription(attributeType.getDescription());
		attributeTypeView.setImportantFlg(attributeType.getImportantFlg());
		attributeTypeView.setRequiredFlg(attributeType.getRequiredFlg());
		attributeTypeView.setVisibleFlg(attributeType.getVisibleFlg());

		return attributeTypeView;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public List<AttributeCodeView> getCodes()
	{
		return codes;
	}

	public void setCodes(List<AttributeCodeView> codes)
	{
		this.codes = codes;
	}

	public boolean getVisibleFlg()
	{
		return visibleFlg;
	}

	public void setVisibleFlg(boolean visibleFlg)
	{
		this.visibleFlg = visibleFlg;
	}

	public boolean getRequiredFlg()
	{
		return requiredFlg;
	}

	public void setRequiredFlg(boolean requiredFlg)
	{
		this.requiredFlg = requiredFlg;
	}

	public boolean getArchitectureFlg()
	{
		return architectureFlg;
	}

	public void setArchitectureFlg(boolean architectureFlg)
	{
		this.architectureFlg = architectureFlg;
	}

	public boolean getImportantFlg()
	{
		return importantFlg;
	}

	public void setImportantFlg(boolean importantFlg)
	{
		this.importantFlg = importantFlg;
	}

	public boolean isAllowMutlipleFlg()
	{
		return allowMutlipleFlg;
	}

	public void setAllowMutlipleFlg(boolean allowMutlipleFlg)
	{
		this.allowMutlipleFlg = allowMutlipleFlg;
	}

}
