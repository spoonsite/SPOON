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

import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeCodePk;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttribute;
import edu.usu.sdl.openstorefront.util.Convert;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ComponentAttributeView
{

	private String type;
	private String code;
	private String typeDescription;
	private String codeDescription;
	private String typeLongDescription;
	private String codeLongDescription;
	private String externalLink;
	private boolean visibleFlg;
	private boolean requiredFlg;
	private boolean allowMultipleFlg;
	private boolean architectureFlg;
	private boolean importantFlg;
	private Date updateDts;
	private Integer sortOrder;
	private String groupCode;
	private boolean orphan;
	private String activeStatus;

	public ComponentAttributeView()
	{
	}

	public static ComponentAttributeView toView(ArticleView article)
	{
		ComponentAttributeView view = new ComponentAttributeView();
		ServiceProxy service = new ServiceProxy();
		AttributeType type = service.getPersistenceService().findById(AttributeType.class, article.getAttributeType());
		AttributeCodePk pk = new AttributeCodePk();
		pk.setAttributeCode(article.getAttributeCode());
		pk.setAttributeType(article.getAttributeType());
		AttributeCode code = service.getPersistenceService().findById(AttributeCode.class, pk);
		view.setExternalLink(code.getDetailUrl());
		view.setCodeDescription(code.getLabel());
		view.setCodeLongDescription(code.getDescription());
		view.setTypeDescription(type.getDescription());
		view.setTypeLongDescription(type.getDescription());
		view.setType(type.getAttributeType());
		view.setCode(code.getAttributeCodePk().getAttributeCode());
		view.setImportantFlg(type.getImportantFlg());
		view.setRequiredFlg(type.getRequiredFlg());
		view.setAllowMultipleFlg(type.getAllowMultipleFlg());
		view.setArchitectureFlg(type.getArchitectureFlg());
		view.setVisibleFlg(type.getVisibleFlg());
		view.setUpdateDts(article.getUpdateDts());

		view.setSortOrder(0);
		view.setGroupCode("Article");

		return view;
	}

	public static ComponentAttributeView toView(ComponentAttribute attribute)
	{
		ServiceProxy service = new ServiceProxy();
		ComponentAttributeView view = new ComponentAttributeView();
		AttributeCodePk pk = new AttributeCodePk();
		pk.setAttributeCode(attribute.getComponentAttributePk().getAttributeCode());
		pk.setAttributeType(attribute.getComponentAttributePk().getAttributeType());
		AttributeCode code = service.getAttributeService().findCodeForType(pk);
		if (code == null) {
			view.setOrphan(true);
			code = service.getPersistenceService().findById(AttributeCode.class, pk);
		}
		AttributeType type = service.getAttributeService().findType(attribute.getComponentAttributePk().getAttributeType());
		if (type == null) {
			view.setOrphan(true);
			type = service.getPersistenceService().findById(AttributeType.class, attribute.getComponentAttributePk().getAttributeType());
		}

		view.setExternalLink(code.getDetailUrl());
		view.setCodeDescription(code.getLabel());
		view.setCodeLongDescription(code.getDescription());
		view.setTypeDescription(type.getDescription());
		view.setTypeLongDescription(type.getDescription());
		view.setType(type.getAttributeType());
		view.setCode(code.getAttributeCodePk().getAttributeCode());
		view.setImportantFlg(Convert.toBoolean(type.getImportantFlg()));
		view.setRequiredFlg(Convert.toBoolean(type.getRequiredFlg()));
		view.setAllowMultipleFlg(Convert.toBoolean(type.getAllowMultipleFlg()));
		view.setArchitectureFlg(Convert.toBoolean(type.getArchitectureFlg()));
		view.setVisibleFlg(Convert.toBoolean(type.getVisibleFlg()));
		view.setUpdateDts(attribute.getUpdateDts());
		view.setSortOrder(code.getSortOrder());
		view.setGroupCode(code.getGroupCode());
		view.setActiveStatus(attribute.getActiveStatus());

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

	public static ComponentAttributeView toView(AttributeCode attribute)
	{
		ServiceProxy service = new ServiceProxy();
		ComponentAttributeView view = new ComponentAttributeView();
		AttributeCodePk pk = attribute.getAttributeCodePk();
		AttributeCode code = service.getAttributeService().findCodeForType(pk);
		AttributeType type = service.getAttributeService().findType(attribute.getAttributeCodePk().getAttributeType());

		if (code != null && type != null) {
			view.setExternalLink(code.getDetailUrl());
			view.setCodeDescription(code.getLabel());
			view.setCodeLongDescription(code.getDescription());
			view.setTypeDescription(type.getDescription());
			view.setTypeLongDescription(type.getDescription());
			view.setType(type.getAttributeType());
			view.setCode(code.getAttributeCodePk().getAttributeCode());
			view.setImportantFlg(Convert.toBoolean(type.getImportantFlg()));
			view.setRequiredFlg(Convert.toBoolean(type.getRequiredFlg()));
			view.setAllowMultipleFlg(Convert.toBoolean(type.getAllowMultipleFlg()));
			view.setArchitectureFlg(Convert.toBoolean(type.getArchitectureFlg()));
			view.setVisibleFlg(Convert.toBoolean(type.getVisibleFlg()));
			view.setUpdateDts(attribute.getUpdateDts());
			view.setSortOrder(code.getSortOrder());
			view.setGroupCode(code.getGroupCode());
			view.setActiveStatus(attribute.getActiveStatus());
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find code and/or type.");
		}
		return view;
	}

	public static List<ComponentAttributeView> attributeCodeListToViewList(List<AttributeCode> attributes)
	{
		if (attributes != null && attributes.size() > 0) {

			List<ComponentAttributeView> views = new ArrayList<>();
			attributes.stream().forEach((attribute) -> {
				views.add(ComponentAttributeView.toView(attribute));
			});
			return views;
		} else {
			return new ArrayList<>();
		}

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

	/**
	 * @return the externalLink
	 */
	public String getExternalLink()
	{
		return externalLink;
	}

	/**
	 * @param externalLink the externalLink to set
	 */
	public void setExternalLink(String externalLink)
	{
		this.externalLink = externalLink;
	}

	/**
	 * @return the sortOrder
	 */
	public Integer getSortOrder()
	{
		return sortOrder;
	}

	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrder(Integer sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	/**
	 * @return the groupCode
	 */
	public String getGroupCode()
	{
		return groupCode;
	}

	/**
	 * @param groupCode the groupCode to set
	 */
	public void setGroupCode(String groupCode)
	{
		this.groupCode = groupCode;
	}

	public boolean getOrphan()
	{
		return orphan;
	}

	public void setOrphan(boolean orphan)
	{
		this.orphan = orphan;
	}

	public String getActiveStatus()
	{
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus)
	{
		this.activeStatus = activeStatus;
	}

}
