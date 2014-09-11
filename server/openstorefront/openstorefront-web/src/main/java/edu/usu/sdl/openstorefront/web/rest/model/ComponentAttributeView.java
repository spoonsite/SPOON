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

import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeCodePk;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttribute;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ComponentAttributeView
{

	private String typeDescription;
	private String type;
	private String code;
	private String codeDescription;
	private String typeLongDescription;
	private String codeLongDescription;
	private boolean visibleFlg;
	private boolean requiredFlg;
	private boolean allowMultipleFlg;
	private boolean architectureFlg;
	private boolean importantFlg;
	private Date updateDts;
	
	public ComponentAttributeView()
	{
	}

	public static ComponentAttributeView toView(ComponentAttribute attribute)
	{
		ServiceProxy service = new ServiceProxy();
		ComponentAttributeView view = new ComponentAttributeView();
		AttributeCodePk pk = new AttributeCodePk();
		pk.setAttributeCode(attribute.getComponentAttributePk().getAttributeCode());
		pk.setAttributeType(attribute.getComponentAttributePk().getAttributeType());
		AttributeCode code = service.getPersistenceService().findById(AttributeCode.class, pk);
		AttributeType type = service.getPersistenceService().findById(AttributeType.class, attribute.getComponentAttributePk().getAttributeType());
		
		view.setCodeDescription(code.getDescription());
		view.setCodeLongDescription(code.getDescription());
		view.setTypeDescription(type.getDescription());
		view.setType(type.getAttributeType());
		view.setCode(code.getAttributeCodePk().getAttributeCode());
		view.setImportantFlg(type.getImportantFlg());
		view.setRequiredFlg(type.getRequiredFlg());
		view.setAllowMultipleFlg(type.getAllowMutlipleFlg());
		view.setArchitectureFlg(type.getArchitectureFlg());
		view.setVisibleFlg(type.getVisibleFlg());
		view.setUpdateDts(attribute.getUpdateDts());
		
		return view;
	}

	public static List<ComponentAttributeView> toViewList(List<ComponentAttribute> attributes)
	{
		List<ComponentAttributeView> views = new ArrayList<>();
		attributes.stream().forEach((attribute) -> {
			views.add(ComponentAttributeView.toView(attribute));
		});
		return views;
	}
	
	public String getCodeDescription()
	{
		return codeDescription;
	}

	public void setCodeDescription(String codeDescription)
	{
		this.codeDescription = codeDescription;
	}

	public String getTypeDescription()
	{
		return typeDescription;
	}

	public void setTypeDescription(String typeDescription)
	{
		this.typeDescription = typeDescription;
	}

	public String getCodeLongDescription()
	{
		return codeLongDescription;
	}

	public void setCodeLongDescription(String codeLongDescription)
	{
		this.codeLongDescription = codeLongDescription;
	}

	public boolean isImportantFlg()
	{
		return importantFlg;
	}

	public void setImportantFlg(boolean importantFlg)
	{
		this.importantFlg = importantFlg;
	}

	/**
	 * @return the updateDts
	 */
	public Date getUpdateDts()
	{
		return updateDts;
	}

	/**
	 * @param updateDts the updateDts to set
	 */
	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	/**
	 * @return the visibleFlg
	 */
	public boolean isVisibleFlg()
	{
		return visibleFlg;
	}

	/**
	 * @param visibleFlg the visibleFlg to set
	 */
	public void setVisibleFlg(boolean visibleFlg)
	{
		this.visibleFlg = visibleFlg;
	}

	/**
	 * @return the requiredFlg
	 */
	public boolean isRequiredFlg()
	{
		return requiredFlg;
	}

	/**
	 * @param requiredFlg the requiredFlg to set
	 */
	public void setRequiredFlg(boolean requiredFlg)
	{
		this.requiredFlg = requiredFlg;
	}

	/**
	 * @return the allowMultipleFlg
	 */
	public boolean isAllowMultipleFlg()
	{
		return allowMultipleFlg;
	}

	/**
	 * @param allowMultipleFlg the allowMultipleFlg to set
	 */
	public void setAllowMultipleFlg(boolean allowMultipleFlg)
	{
		this.allowMultipleFlg = allowMultipleFlg;
	}

	/**
	 * @return the architectureFlg
	 */
	public boolean isArchitectureFlg()
	{
		return architectureFlg;
	}

	/**
	 * @param architectureFlg the architectureFlg to set
	 */
	public void setArchitectureFlg(boolean architectureFlg)
	{
		this.architectureFlg = architectureFlg;
	}

	/**
	 * @return the typeLongDescription
	 */
	public String getTypeLongDescription()
	{
		return typeLongDescription;
	}

	/**
	 * @param typeLongDescription the typeLongDescription to set
	 */
	public void setTypeLongDescription(String typeLongDescription)
	{
		this.typeLongDescription = typeLongDescription;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * @return the code
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code)
	{
		this.code = code;
	}

}
