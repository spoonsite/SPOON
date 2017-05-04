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
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.model.FieldChangeModel;
import edu.usu.sdl.openstorefront.validation.BasicHTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Metadata that is not linked to filters; Free keyed attributes")
public class ComponentMetadata
		extends BaseComponent<ComponentMetadata>
		implements LoggableModel<ComponentMetadata>
{

	public static final String FIELD_LABEL = "label";

	@PK(generated = true)
	@NotNull
	private String metadataId;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	private String label;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(BasicHTMLSanitizer.class)
	private String value;

	public ComponentMetadata()
	{
	}

	@Override
	public String uniqueKey()
	{
		return getLabel() + OpenStorefrontConstant.GENERAL_KEY_SEPARATOR + getValue();
	}

	@Override
	protected void customKeyClear()
	{
		setMetadataId(null);
	}

	@Override
	public void updateFields(StandardEntity entity)
	{
		ComponentMetadata metadata = (ComponentMetadata) entity;
		ServiceProxyFactory.getServiceProxy().getChangeLogService().findUpdateChanges(this, metadata);
		super.updateFields(entity);

		this.setLabel(metadata.getLabel());
		this.setValue(metadata.getValue());
	}

	@Override
	public List<FieldChangeModel> findChanges(ComponentMetadata updated)
	{
		Set<String> excludeFields = excludedChangeFields();
		excludeFields.add("metadataId");
		List<FieldChangeModel> changes = FieldChangeModel.allChangedFields(excludeFields, this, updated);
		return changes;
	}

	@Override
	public String addRemoveComment()
	{
		return getLabel() + " - " + getValue();
	}

	public String getMetadataId()
	{
		return metadataId;
	}

	public void setMetadataId(String metadataId)
	{
		this.metadataId = metadataId;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

}
