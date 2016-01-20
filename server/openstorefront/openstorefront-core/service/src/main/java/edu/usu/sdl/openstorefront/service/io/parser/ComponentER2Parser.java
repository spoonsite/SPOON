/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.io.parser;

import edu.usu.sdl.er2.model.Asset;
import edu.usu.sdl.er2.model.Document;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentExternalDependency;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.ResourceType;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.service.io.reader.GenericReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 *
 * @author dshurtleff
 */
public class ComponentER2Parser
		extends BaseComponentParser
{

	private static final Logger log = Logger.getLogger(ComponentER2Parser.class.getName());

	@Override
	public String checkFormat(String mimeType, InputStream input)
	{
		String errorMessage = null;
		Serializer serializer = new Persister();
		try {
			serializer.read(Asset.class, input);
		} catch (Exception ex) {
			log.log(Level.FINEST, "Unable to read file", ex);
			errorMessage = "Unable to read format.  The file must be an XML Asset file.";
		}
		return errorMessage;
	}

	@Override
	protected GenericReader getReader(InputStream in)
	{
		return new GenericReader<Asset>(in)
		{

			private List<Asset> assets = new ArrayList<>();

			@Override
			public void preProcess()
			{
				Serializer serializer = new Persister();
				try {
					Asset asset = serializer.read(Asset.class, in);
					assets.add(asset);
					totalRecords = 1;
				} catch (Exception ex) {
					throw new OpenStorefrontRuntimeException("Unable to read xml record.", "Check data", ex);
				}
			}

			@Override
			public Asset nextRecord()
			{
				if (assets.size() > 0) {
					currentRecordNumber++;
					return assets.remove(0);
				} else {
					return null;
				}
			}

		};
	}

	@Override
	protected <T> Object parseRecord(T record)
	{
		Asset asset = (Asset) record;
		ComponentAll componentAll = defaultComponentAll();

		Component component = componentAll.getComponent();
		component.setName(asset.mandatoryData.name);
		String description = asset.mandatoryData.description;
		if (StringUtils.isBlank(description)) {
			description = OpenStorefrontConstant.NOT_AVAILABLE;
		}
		component.setDescription(description);
		component.setExternalId(Integer.toString(asset.id));
		component.setGuid(asset.mandatoryData.uuid);
		component.setVersion(asset.mandatoryData.version);
		component.setSubmittedDts(Convert.toDate(asset.loadDate));

		if (asset.customData != null) {
			if (asset.customData.producingAgency != null) {
				component.setOrganization(asset.customData.producingAgency);
			} else if (asset.customData.agency != null) {
				component.setOrganization(asset.customData.agency);
			}
		}

		//Admin (Contacts, Approval)
		if (asset.adminData != null
				&& asset.adminData.accepted != null) {
			component.setApprovalState(ApprovalStatus.APPROVED);
			component.setApprovedUser(asset.adminData.accepted.user.username);
			component.setApprovedDts(Convert.toDate(asset.adminData.accepted.date));
		}

//		if (asset.adminData != null &&
//			asset.adminData.submitted != null)
//		{
//			ComponentContact componentContact = new ComponentContact();
//
//		}
		//Attributes (xrefs)
		//custom data (Metadata)
		//dependanices
		if (asset.customData != null && asset.customData.dependencies != null) {
			ComponentExternalDependency dependency = new ComponentExternalDependency();
			dependency.setDependencyName(OpenStorefrontConstant.NOT_AVAILABLE);
			dependency.setComment(StringUtils.left(asset.customData.dependencies, OpenStorefrontConstant.FIELD_SIZE_DETAILED_DESCRIPTION));
			dependency.setVersion(OpenStorefrontConstant.NOT_AVAILABLE);
			componentAll.getExternalDependencies().add(dependency);
		}

		//resources
		if (asset.customData != null
				&& asset.customData.documentList != null) {
			List<Document> docs = asset.customData.documentList;
			for (Document document : docs) {
				if (StringUtils.isNotBlank(document.documentURL)) {
					if (Convert.toBoolean(document.documentApproved)) {
						ComponentResource componentResource = new ComponentResource();
						componentResource.setResourceType(ResourceType.DOCUMENT);
						componentResource.setDescription(StringUtils.left(document.documentName, OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT));
						componentResource.setLink(document.documentURL);
						componentAll.getResources().add(componentResource);
					}
				}
			}
		}

		return componentAll;
	}

}
