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
package edu.usu.sdl.openstorefront.service.io.export;

import edu.usu.sdl.describe.model.Address;
import edu.usu.sdl.describe.model.Assertion;
import edu.usu.sdl.describe.model.Conformance;
import edu.usu.sdl.describe.model.Edh;
import edu.usu.sdl.describe.model.GeneralInfo;
import edu.usu.sdl.describe.model.HandlingAssertion;
import edu.usu.sdl.describe.model.HandlingStatement;
import edu.usu.sdl.describe.model.Organization;
import edu.usu.sdl.describe.model.Person;
import edu.usu.sdl.describe.model.PointOfContact;
import edu.usu.sdl.describe.model.RelatedResource;
import edu.usu.sdl.describe.model.ResponsibleEntity;
import edu.usu.sdl.describe.model.SearchInterface;
import edu.usu.sdl.describe.model.SearchProvider;
import edu.usu.sdl.describe.model.Security;
import edu.usu.sdl.describe.model.Service;
import edu.usu.sdl.describe.model.ServiceType;
import edu.usu.sdl.describe.model.StructuredStatement;
import edu.usu.sdl.describe.model.TrustedDataCollection;
import edu.usu.sdl.describe.model.TrustedDataObject;
import edu.usu.sdl.describe.parser.DescribeParser;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentContact;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.ResourceType;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.view.ComponentResourceView;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author dshurtleff
 */
public class DescribeExport
		extends Exporter
{

	private static final String ATTRIBUTE_TYPE_CONFORMANCE = "DESCCOMF";
	private static final String ATTRIBUTE_TYPE_NETWORK = "DESCNETW";

	@Override
	protected void writeRecord(OutputStream out, ComponentAll record)
	{
		TrustedDataCollection dataCollection = new TrustedDataCollection();
		TrustedDataObject dataObject = new TrustedDataObject();

		dataObject.setVersion(PropertiesManager.getInstance().getApplicationVersion());
		dataObject.setPayloadURI(OpenStorefrontConstant.NOT_AVAILABLE);

		HandlingAssertion handlingAssertion = new HandlingAssertion();
		handlingAssertion.setScope(OpenStorefrontConstant.NOT_AVAILABLE);
		HandlingStatement handlingStatement = new HandlingStatement();
		Edh edh = new Edh();
		edh.setCreateDts(TimeUtil.dateToString(record.getComponent().getCreateDts()));
		edh.setIdentifier(OpenStorefrontConstant.NOT_AVAILABLE);
		ResponsibleEntity entity = new ResponsibleEntity();
		entity.setCountry("USA");
		entity.setOrganization("DI2E");
		edh.setResponsibleEntity(entity);
		Security security = new Security();
		security.setClassification("U");
		security.setCompliesWith(OpenStorefrontConstant.NOT_AVAILABLE);
		security.setCreateDate(TimeUtil.dateToString(TimeUtil.currentDate()));
		security.setOwnerProducer(OpenStorefrontConstant.NOT_AVAILABLE);
		edh.setSecurity(security);

		handlingStatement.setEdh(edh);
		handlingAssertion.setHandlingStatement(handlingStatement);

		dataObject.setHandlingAssertion(handlingAssertion);

		dataCollection.setHandlingAssertion(handlingAssertion);
		dataCollection.setVersion(PropertiesManager.getInstance().getApplicationVersion());
		dataCollection.getTrustedDataObjects().add(dataObject);

		Assertion collectionAssertion = new Assertion();
		collectionAssertion.setStatement(OpenStorefrontConstant.NOT_AVAILABLE);
		dataCollection.setAssertion(collectionAssertion);

		Assertion assertion = new Assertion();
		StructuredStatement structuredStatement = new StructuredStatement();
		assertion.setStructuredStatement(structuredStatement);

		dataObject.getAssertions().add(assertion);

		SearchProvider searchProvider = new SearchProvider();
		structuredStatement.setSearchProvider(searchProvider);

		GeneralInfo generalInfo = new GeneralInfo();
		searchProvider.setGeneralInfo(generalInfo);

		generalInfo.setName(record.getComponent().getName());
		generalInfo.setDescription(record.getComponent().getDescription());
		generalInfo.setDescriptionClassification(StringProcessor.blankIfNull(record.getComponent().getSecurityMarkingType()));
		generalInfo.setGuid(StringProcessor.blankIfNull(record.getComponent().getGuid()));
		generalInfo.setNameClassification("");
		generalInfo.setNetwork("");

		Map<String, List<ComponentAttribute>> attributeTypeMap = record.getAttributes().stream().collect(Collectors.groupingBy(c -> c.getComponentAttributePk().getAttributeType()));

		if (attributeTypeMap.containsKey(ATTRIBUTE_TYPE_NETWORK)) {
			List<ComponentAttribute> attributes = attributeTypeMap.get(ATTRIBUTE_TYPE_NETWORK);
			StringBuilder sb = new StringBuilder();
			int count = 0;
			for (ComponentAttribute attribute : attributes) {
				sb.append(attribute.getComponentAttributePk().getAttributeCode());
				if (count > 1) {
					sb.append(" ");
				}

				count++;
			}
			generalInfo.setNetwork(sb.toString());
		}

		PointOfContact pointOfContact = new PointOfContact();
		Organization organization = new Organization();
		organization.setName(record.getComponent().getOrganization());

		edu.usu.sdl.openstorefront.core.entity.Organization sfOrg = new edu.usu.sdl.openstorefront.core.entity.Organization();
		sfOrg.setName(record.getComponent().getOrganization());
		sfOrg = sfOrg.find();

		if (sfOrg != null) {
			organization.setEmail(sfOrg.getContactEmail());
			organization.setPhone(sfOrg.getContactPhone());
			organization.setSubOrganization(sfOrg.getAgency());
		}
		pointOfContact.setOrganization(organization);
		generalInfo.getContacts().add(pointOfContact);

		for (ComponentContact contact : record.getContacts()) {
			pointOfContact = new PointOfContact();

			Person person = new Person();
			person.setName(contact.getFirstName());
			person.setSurname(contact.getLastName());
			person.setEmail(contact.getEmail());
			person.setPhone(contact.getPhone());
			person.setAffiliation(contact.getOrganization());
			pointOfContact.setPerson(person);
			generalInfo.getContacts().add(pointOfContact);
		}

		List<ComponentResource> serviceResources = new ArrayList<>();
		for (ComponentResource resource : record.getResources()) {
			if (ResourceType.SERVICE.equals(resource.getResourceType())) {
				serviceResources.add(resource);
			} else {
				RelatedResource relatedResource = new RelatedResource();
				relatedResource.setDescription(resource.getDescription());

				ComponentResourceView view = ComponentResourceView.toView(resource);
				relatedResource.setLink(view.getLink());

				relatedResource.setRelationship(resource.getResourceType());
				searchProvider.getRelatedResources().add(relatedResource);
			}
		}

		if (!serviceResources.isEmpty()) {

			for (ComponentResource resource : serviceResources) {
				SearchInterface searchInterface = new SearchInterface();
				Service serviceResource = new Service();
				searchInterface.setService(serviceResource);

				serviceResource.setName(resource.getDescription());
				if (resource.getRestricted() != null) {
					ServiceType serviceType = new ServiceType();
					serviceType.setSecure(Boolean.toString(resource.getRestricted()));
					serviceResource.setServiceType(serviceType);
				}

				Address address = new Address();
				ComponentResourceView view = ComponentResourceView.toView(resource);
				address.setText(view.getLink());
				serviceResource.getAddresses().add(address);

				if (attributeTypeMap.containsKey(ATTRIBUTE_TYPE_CONFORMANCE)) {
					List<ComponentAttribute> attributes = attributeTypeMap.get(ATTRIBUTE_TYPE_CONFORMANCE);

					for (ComponentAttribute attribute : attributes) {
						Conformance conformance = new Conformance();
						conformance.setId(attribute.getComponentAttributePk().getAttributeCode());

						AttributeCodePk attributeCodePk = new AttributeCodePk();
						attributeCodePk.setAttributeCode(attribute.getComponentAttributePk().getAttributeCode());
						attributeCodePk.setAttributeType(attribute.getComponentAttributePk().getAttributeType());

						AttributeCode attributeCode = service.getAttributeService().findCodeForType(attributeCodePk);
						if (attributeCode != null) {
							conformance.setName(attributeCode.getLabel());
						} else {
							conformance.setName(attribute.getComponentAttributePk().getAttributeCode());
						}

						serviceResource.getConformances().add(conformance);
					}
				}

				searchProvider.getSearchInterfaces().add(searchInterface);
			}
		}

		try {
			DescribeParser.write(out, dataCollection);
		} catch (Exception e) {
			throw new OpenStorefrontRuntimeException(e);
		}
	}

}
