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
package edu.usu.sdl.spoon.importer;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryErrorType;
import edu.usu.sdl.openstorefront.core.entity.ResourceType;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.spi.parser.BaseComponentParser;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.ComponentMapper;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.MapModel;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.GenericReader;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.XMLMapReader;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ComponentSpoonParser
		extends BaseComponentParser
{

	public static final String FORMAT_CODE = "SPOONCMP";
	private static final String EQUIPMENT_TYPE = "EQUIPTYPE";

	private List<ResourceAttachment> attachments = new ArrayList<>();
	private List<ComponentAll> componentAlls;

	@Override
	public String checkFormat(String mimeType, InputStream input)
	{
		if (mimeType.contains("xml")) {
			return "";
		} else {
			return "Invalid format. Please upload a XML file.";
		}
	}

	@Override
	protected GenericReader getReader(InputStream in)
	{
		return new XMLMapReader(in);
	}

	@Override
	protected String handlePreviewOfRecord(Object data)
	{
		String output = "";
		if (componentAlls != null
				&& !componentAlls.isEmpty()) {
			try {
				output = service.getSystemService().toJson(componentAlls.get(0));
			} catch (Exception ex) {
				output = "Unable preview attributes.  <br>Trace:<br>" + StringProcessor.parseStackTraceHtml(ex);
			}
		}
		return output;
	}

	@Override
	protected <T> Object parseRecord(T record)
	{
		MapModel mapModel = (MapModel) record;

		ComponentMapper componentMapper = new ComponentMapper(() -> {
			ComponentAll componentAll = defaultComponentAll();
			componentAll.getComponent().setDescription(null);
			return componentAll;
		}, fileHistoryAll);

		componentAlls = componentMapper.multiMapData(mapModel);

		int realRecordNumber = 0;
		for (ComponentAll componentAll : componentAlls) {
			realRecordNumber++;

			//add missing information
			Component component = componentAll.getComponent();
			if (StringProcessor.stringIsNotBlank(component.getName())
					&& StringProcessor.stringIsNotBlank(component.getOrganization()) == false) {
				String nameSplit[] = component.getName().split(" ");

				component.setOrganization(nameSplit[0]);
			}

			if (StringProcessor.stringIsNotBlank(component.getDescription()) == false) {
				component.setDescription(component.getName());
			}

			String fileNameSplit[] = fileHistoryAll.getFileHistory().getOriginalFilename().split("_");

			String entryTypeLabel = fileNameSplit[0] + " " + fileNameSplit[1];
			component.setComponentType(getEntryType(entryTypeLabel));

			StringBuilder equipmentLabel = new StringBuilder();
			for (int i = 2; i < fileNameSplit.length; i++) {
				equipmentLabel.append(fileNameSplit[i]).append(" ");
			}
			String equipLabel = equipmentLabel.toString().replace(".xml", "");

			AttributeCode equipmentTypeCode = getAttributeCode(EQUIPMENT_TYPE, equipLabel.trim());

			if (equipmentTypeCode != null) {
				ComponentAttribute componentAttribute = new ComponentAttribute();
				ComponentAttributePk componentAttributePk = new ComponentAttributePk();
				componentAttributePk.setAttributeCode(equipmentTypeCode.getAttributeCodePk().getAttributeCode());
				componentAttributePk.setAttributeType(equipmentTypeCode.getAttributeCodePk().getAttributeType());
				componentAttribute.setComponentAttributePk(componentAttributePk);

				componentAll.getAttributes().add(componentAttribute);
			}

			for (ComponentResource componentResource : componentAll.getResources()) {
				componentResource.setResourceType(getLookup(ResourceType.class, ResourceType.DOCUMENT));
			}

			ValidationResult validationResult = componentAll.validate();
			if (validationResult.valid()) {

				for (ComponentResource componentResource : componentAll.getResources()) {
					if (StringProcessor.stringIsNotBlank(componentResource.getFileName())) {
						ResourceAttachment attachment = new ResourceAttachment();
						attachment.setComponentName(component.getName());
						attachment.setFileData(componentResource.getFileName());
						attachment.setResourceOriginalName(componentResource.getOriginalName());

						componentResource.setFileName(null);

						attachments.add(attachment);
					}
				}

				addRecordToStorage(componentAll);
			} else {
				fileHistoryAll.addError(FileHistoryErrorType.VALIDATION, validationResult.toHtmlString(), realRecordNumber);
			}
		}

		return null;
	}

	@Override
	protected void finishProcessing()
	{
		for (ResourceAttachment attachment : attachments) {

			Component component = new Component();
			component.setName(attachment.getComponentName());
			component = component.find();

			ComponentResource componentResource = new ComponentResource();
			componentResource.setComponentId(component.getComponentId());
			componentResource.setOriginalName(attachment.getResourceOriginalName());

			//There may be more than one; pick one and remove others.
			List<ComponentResource> componentResources = componentResource.findByExample();
			ComponentResource pickedResource = null;
			for (ComponentResource componentResourceFound : componentResources) {
				if (pickedResource == null) {
					pickedResource = componentResourceFound;
					pickedResource.setActiveStatus(ComponentResource.ACTIVE_STATUS);
				} else {
					service.getComponentService().deleteBaseComponent(ComponentResource.class, componentResourceFound.getResourceId());
				}
			}
			pickedResource.setOriginalName(attachment.getResourceOriginalName());
			pickedResource.setMimeType(
					OpenStorefrontConstant.getMimeForFileExtension(
							StringProcessor.getFileExtension(attachment.getResourceOriginalName())
					)
			);

			byte[] fileData = Base64.getMimeDecoder().decode(attachment.getFileData());

			//FIX if this importer is needed
			//service.getComponentService().saveResourceFile(pickedResource, new ByteArrayInputStream(fileData));
		}
	}

}
