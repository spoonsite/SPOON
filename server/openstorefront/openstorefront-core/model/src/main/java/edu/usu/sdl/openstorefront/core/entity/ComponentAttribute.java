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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.model.FieldChangeModel;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import java.util.List;
import java.util.Set;
import javax.persistence.Embedded;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Referenced Metadata Attribute")
public class ComponentAttribute
		extends BaseComponent<ComponentAttribute>
		implements LoggableModel<ComponentAttribute>
{

	@PK
	@ConsumeField
	@Embedded
	@OneToOne(orphanRemoval = true)
	private ComponentAttributePk componentAttributePk;

	@ConsumeField
	@APIDescription("Denotes whether this is publicly viewable.")
	private Boolean privateFlag;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	@Sanitize(HTMLSanitizer.class)
	private String comment;
	
	@ConsumeField
	private String preferredUnit;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public ComponentAttribute()
	{
	}

	@Override
	public String uniqueKey()
	{
		return getComponentAttributePk().getAttributeType()
				+ OpenStorefrontConstant.GENERAL_KEY_SEPARATOR
				+ getComponentAttributePk().getAttributeCode();
	}

	@Override
	protected void customKeyClear()
	{
		//Leave the other key items
		if (getComponentAttributePk() != null) {
			getComponentAttributePk().setComponentId(null);
		}
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		ComponentAttribute attribute = (ComponentAttribute) entity;
		this.setPrivateFlag(attribute.getPrivateFlag());
		this.setComment(attribute.getComment());

	}

	@Override
	public List<FieldChangeModel> findChanges(ComponentAttribute updated)
	{
		Set<String> excludeFields = excludedChangeFields();
		excludeFields.add("componentAttributePk");

		List<FieldChangeModel> changes = FieldChangeModel.allChangedFields(excludeFields, this, updated);
		return changes;
	}

	@Override
	public String addRemoveComment()
	{
		Service service = ServiceProxyFactory.getServiceProxy();
		AttributeCodePk pk = new AttributeCodePk();
		pk.setAttributeCode(getComponentAttributePk().getAttributeCode());
		pk.setAttributeType(getComponentAttributePk().getAttributeType());
		AttributeCode code = service.getAttributeService().findCodeForType(pk);
		AttributeType type = service.getAttributeService().findType(getComponentAttributePk().getAttributeType());
		if (code != null && type != null) {
			return type.getDescription() + ": " + code.getLabel();
		} else {
			return "Missing Attribute code or Type";
		}
	}

	public ComponentAttributePk getComponentAttributePk()
	{
		return componentAttributePk;
	}

	public void setComponentAttributePk(ComponentAttributePk componentAttributePk)
	{
		this.componentAttributePk = componentAttributePk;
	}

	public Boolean getPrivateFlag()
	{
		return privateFlag;
	}

	public void setPrivateFlag(Boolean privateFlag)
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

	public String getPreferredUnit()
	{
		return preferredUnit;
	}

	public void setPreferredUnit(String preferredUnit)
	{
		this.preferredUnit = preferredUnit;
	}

}
