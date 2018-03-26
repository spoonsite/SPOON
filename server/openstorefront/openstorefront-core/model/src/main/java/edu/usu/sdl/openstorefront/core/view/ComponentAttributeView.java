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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentTypeRestriction;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ComponentAttributeView
		extends StandardEntityView
{

	public static final String TYPE_DESCRIPTION_FIELD = "typeDescription";

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
	private boolean allowUserCodes;
	private boolean hideOnSubmission;
	private boolean codeHasAttachment;

	private String defaultAttributeCode;
	private Date updateDts;
	private Integer sortOrder;
	private String groupCode;
	private boolean orphan;
	private String activeStatus;
	private String badgeUrl;
	private String highlightStyle;
	private List<ComponentTypeRestriction> requiredRestrictions;
	private boolean privateFlag;
	private String comment;

	public ComponentAttributeView()
	{
	}

	public static ComponentAttributeView toView(ComponentAttribute attribute)
	{
		Service service = ServiceProxyFactory.getServiceProxy();

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

		view.setPrivateFlag(Convert.toBoolean(attribute.getPrivateFlag()));
		view.setComment(attribute.getComment());

		if (code != null && type != null) {
			view.setRequiredRestrictions(type.getRequiredRestrictions());
			view.setExternalLink(code.getDetailUrl());
			view.setCodeDescription(code.getLabel());
			view.setCodeLongDescription(code.getDescription());
			view.setTypeDescription(type.getDescription());
			view.setTypeLongDescription(type.getDetailedDescription());
			view.setType(type.getAttributeType());
			view.setCode(code.getAttributeCodePk().getAttributeCode());
			view.setBadgeUrl(code.getBadgeUrl());
			view.setImportantFlg(Convert.toBoolean(type.getImportantFlg()));
			view.setRequiredFlg(Convert.toBoolean(type.getRequiredFlg()));
			view.setAllowUserCodes(Convert.toBoolean(type.getAllowUserGeneratedCodes()));
			view.setAllowMultipleFlg(Convert.toBoolean(type.getAllowMultipleFlg()));
			view.setHideOnSubmission(Convert.toBoolean(type.getHideOnSubmission()));
			view.setDefaultAttributeCode(type.getDefaultAttributeCode());
			view.setArchitectureFlg(Convert.toBoolean(type.getArchitectureFlg()));
			view.setVisibleFlg(Convert.toBoolean(type.getVisibleFlg()));
			view.setUpdateDts(attribute.getUpdateDts());
			view.setSortOrder(code.getSortOrder());
			view.setGroupCode(code.getGroupCode());
			view.setHighlightStyle(code.getHighlightStyle());
			view.setActiveStatus(attribute.getActiveStatus());
			view.toStandardView(code, type);

			if (code.getAttachmentFileName() != null && !code.getAttachmentFileName().isEmpty()) {
				view.setCodeHasAttachment(true);
			} else {
				view.setCodeHasAttachment(false);
			}

		} else {
			//For snapshots which are frozen in time; attributes may not exist any more
			//create stub
			view.setCodeDescription("Missing Code: " + attribute.getComponentAttributePk().getAttributeCode());
			view.setTypeDescription("Missing Type: " + attribute.getComponentAttributePk().getAttributeType());
			view.setType(attribute.getComponentAttributePk().getAttributeType());
			view.setCode(attribute.getComponentAttributePk().getAttributeCode());
		}

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
		Service service = ServiceProxyFactory.getServiceProxy();
		ComponentAttributeView view = new ComponentAttributeView();
		AttributeCodePk pk = attribute.getAttributeCodePk();
		AttributeCode code = service.getAttributeService().findCodeForType(pk);
		AttributeType type = service.getAttributeService().findType(attribute.getAttributeCodePk().getAttributeType());

		if (code != null && type != null) {
			view.setExternalLink(code.getDetailUrl());
			view.setCodeDescription(code.getLabel());
			view.setCodeLongDescription(code.getDescription());
			view.setTypeDescription(type.getDescription());
			view.setTypeLongDescription(type.getDetailedDescription());
			view.setType(type.getAttributeType());
			view.setCode(code.getAttributeCodePk().getAttributeCode());
			view.setBadgeUrl(code.getBadgeUrl());
			view.setImportantFlg(Convert.toBoolean(type.getImportantFlg()));
			view.setRequiredFlg(Convert.toBoolean(type.getRequiredFlg()));
			view.setAllowUserCodes(Convert.toBoolean(type.getAllowUserGeneratedCodes()));
			view.setAllowMultipleFlg(Convert.toBoolean(type.getAllowMultipleFlg()));
			view.setArchitectureFlg(Convert.toBoolean(type.getArchitectureFlg()));
			view.setHideOnSubmission(Convert.toBoolean(type.getHideOnSubmission()));
			view.setDefaultAttributeCode(type.getDefaultAttributeCode());
			view.setVisibleFlg(Convert.toBoolean(type.getVisibleFlg()));
			view.setUpdateDts(attribute.getUpdateDts());
			view.setSortOrder(code.getSortOrder());
			view.setGroupCode(code.getGroupCode());
			view.setActiveStatus(attribute.getActiveStatus());
			view.toStandardView(code, type);
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

	public Date getUpdateDts()
	{
		return updateDts;
	}

	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	public boolean isVisibleFlg()
	{
		return visibleFlg;
	}

	public void setVisibleFlg(boolean visibleFlg)
	{
		this.visibleFlg = visibleFlg;
	}

	public boolean isRequiredFlg()
	{
		return requiredFlg;
	}

	public void setRequiredFlg(boolean requiredFlg)
	{
		this.requiredFlg = requiredFlg;
	}

	public boolean isAllowMultipleFlg()
	{
		return allowMultipleFlg;
	}

	public void setAllowMultipleFlg(boolean allowMultipleFlg)
	{
		this.allowMultipleFlg = allowMultipleFlg;
	}

	public boolean isArchitectureFlg()
	{
		return architectureFlg;
	}

	public void setArchitectureFlg(boolean architectureFlg)
	{
		this.architectureFlg = architectureFlg;
	}

	public String getTypeLongDescription()
	{
		return typeLongDescription;
	}

	public void setTypeLongDescription(String typeLongDescription)
	{
		this.typeLongDescription = typeLongDescription;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getExternalLink()
	{
		return externalLink;
	}

	public void setExternalLink(String externalLink)
	{
		this.externalLink = externalLink;
	}

	public Integer getSortOrder()
	{
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	public String getGroupCode()
	{
		return groupCode;
	}

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

	public String getBadgeUrl()
	{
		return badgeUrl;
	}

	public void setBadgeUrl(String badgeUrl)
	{
		this.badgeUrl = badgeUrl;
	}

	public String getHighlightStyle()
	{
		return highlightStyle;
	}

	public void setHighlightStyle(String highlightStyle)
	{
		this.highlightStyle = highlightStyle;
	}

	public boolean getHideOnSubmission()
	{
		return hideOnSubmission;
	}

	public void setHideOnSubmission(boolean hideOnSubmission)
	{
		this.hideOnSubmission = hideOnSubmission;
	}

	public String getDefaultAttributeCode()
	{
		return defaultAttributeCode;
	}

	public void setDefaultAttributeCode(String defaultAttributeCode)
	{
		this.defaultAttributeCode = defaultAttributeCode;
	}

	public List<ComponentTypeRestriction> getRequiredRestrictions()
	{
		return requiredRestrictions;
	}

	public void setRequiredRestrictions(List<ComponentTypeRestriction> requiredRestrictions)
	{
		this.requiredRestrictions = requiredRestrictions;
	}

	public boolean getCodeHasAttachment()
	{
		return codeHasAttachment;
	}

	public void setCodeHasAttachment(boolean codeHasAttachment)
	{
		this.codeHasAttachment = codeHasAttachment;
	}

	public boolean getAllowUserCodes()
	{
		return allowUserCodes;
	}

	public void setAllowUserCodes(boolean allowUserCodes)
	{
		this.allowUserCodes = allowUserCodes;
	}

	public boolean getPrivateFlag()
	{
		return privateFlag;
	}

	public void setPrivateFlag(boolean privateFlag)
	{
		this.privateFlag = privateFlag;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

}
