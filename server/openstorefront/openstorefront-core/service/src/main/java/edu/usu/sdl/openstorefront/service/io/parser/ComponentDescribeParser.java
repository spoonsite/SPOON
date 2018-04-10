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
package edu.usu.sdl.openstorefront.service.io.parser;

import edu.usu.sdl.describe.model.Address;
import edu.usu.sdl.describe.model.Assertion;
import edu.usu.sdl.describe.model.BoundingGeometry;
import edu.usu.sdl.describe.model.Conformance;
import edu.usu.sdl.describe.model.MimeType;
import edu.usu.sdl.describe.model.PointOfContact;
import edu.usu.sdl.describe.model.RelatedResource;
import edu.usu.sdl.describe.model.SearchInterface;
import edu.usu.sdl.describe.model.SearchProvider;
import edu.usu.sdl.describe.model.Service;
import edu.usu.sdl.describe.model.StructuredStatement;
import edu.usu.sdl.describe.model.TrustedDataCollection;
import edu.usu.sdl.describe.model.TrustedDataObject;
import edu.usu.sdl.describe.parser.DescribeParser;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentContact;
import edu.usu.sdl.openstorefront.core.entity.ComponentMetadata;
import edu.usu.sdl.openstorefront.core.entity.ComponentRelationship;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.ContactType;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryErrorType;
import edu.usu.sdl.openstorefront.core.entity.RelationshipType;
import edu.usu.sdl.openstorefront.core.entity.ResourceType;
import edu.usu.sdl.openstorefront.core.entity.SecurityMarkingType;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.spi.parser.BaseComponentParser;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.GenericReader;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.io.InputStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * Parses a describe record
 *
 * @author dshurtleff
 */
public class ComponentDescribeParser
		extends BaseComponentParser
{

	private static final Logger LOG = Logger.getLogger(ComponentDescribeParser.class.getName());

	private static final String ATTRIBUTE_TYPE_NETWORK = "DESCNETW";
	private static final String ATTRIBUTE_TYPE_NETWORK_DESC = "Describe Network";

	private static final String ATTRIBUTE_TYPE_CONFORMANCE = "DESCCOMF";
	private static final String ATTRIBUTE_TYPE_CONFORMANCE_DESC = "Describe Conformance";

	private static final String ATTRIBUTE_TYPE_MIMETYPE = "DESCMIME";
	private static final String ATTRIBUTE_TYPE_MIMETYPE_DESC = "Describe MIME Type";

	private static final String SERVICE_COMPONENT_TYPE = "DESCRIBE-S";
	private static final String CONTENT_COLLECTION_COMPONENT_TYPE = "DESCRIBE-CC";

	@Override
	public String checkFormat(String mimeType, InputStream input)
	{
		String errorMessage = null;
		try {
			TrustedDataCollection trustedDataCollection = DescribeParser.parse(input);
		} catch (Exception ex) {
			LOG.log(Level.FINE, "Unable to read file", ex);
			errorMessage = "Unable to read format.  The file must be an XML Describe Record.";
		}
		return errorMessage;
	}

	@Override
	protected GenericReader getReader(InputStream in)
	{
		return new GenericReader<TrustedDataCollection>(in)
		{
			private List<TrustedDataCollection> collections = new ArrayList<>();

			@Override
			public void preProcess()
			{
				Serializer serializer = new Persister();
				try {
					TrustedDataCollection trustedDataCollection = DescribeParser.parse(in);
					collections.add(trustedDataCollection);
					totalRecords = 1;
				} catch (Exception ex) {
					throw new OpenStorefrontRuntimeException("Unable to read xml record.", "Check data", ex);
				}
			}

			@Override
			public TrustedDataCollection nextRecord()
			{
				if (collections.size() > 0) {
					currentRecordNumber++;
					return collections.remove(0);
				} else {
					return null;
				}
			}

		};
	}

	@Override
	protected <T> Object parseRecord(T record)
	{
		TrustedDataCollection dataCollection = (TrustedDataCollection) record;

		List<ComponentAll> allData = new ArrayList<>();
		for (TrustedDataObject dataObject : dataCollection.getTrustedDataObjects()) {
			allData.addAll(processAssertions(dataObject.getAssertions()));
		}

		for (ComponentAll componentAll : allData) {
			ValidationResult validationResult = componentAll.validate();
			if (validationResult.valid() == false) {
				fileHistoryAll.addError(FileHistoryErrorType.VALIDATION, validationResult.toHtmlString(), currentRecordNumber);
			} else {
				addRecordToStorage(componentAll);
			}
		}

		return null;
	}

	private List<ComponentAll> processAssertions(List<Assertion> assertions)
	{
		List<ComponentAll> componentAlls = new ArrayList<>();

		StructuredStatement resourceCollection = new StructuredStatement();

		ComponentType serviceEntryType = new ComponentType();
		serviceEntryType.setComponentType(SERVICE_COMPONENT_TYPE);
		serviceEntryType = serviceEntryType.find();
		if (serviceEntryType == null) {

			serviceEntryType = new ComponentType();
			serviceEntryType.setComponentType(SERVICE_COMPONENT_TYPE);

			serviceEntryType.setLabel("Describe Service");
			serviceEntryType.setDescription("Describe service record");
			serviceEntryType.setDataEntryAttributes(Boolean.TRUE);
			serviceEntryType.setDataEntryContacts(Boolean.TRUE);
			serviceEntryType.setDataEntryMedia(Boolean.TRUE);
			serviceEntryType.setDataEntryMetadata(Boolean.TRUE);
			serviceEntryType.setDataEntryQuestions(Boolean.TRUE);
			serviceEntryType.setDataEntryRelationships(Boolean.TRUE);
			serviceEntryType.setDataEntryResources(Boolean.TRUE);
			serviceEntryType.setDataEntryReviews(Boolean.TRUE);
			serviceEntryType.setCreateUser(fileHistoryAll.getFileHistory().getCreateUser());
			serviceEntryType.setUpdateUser(fileHistoryAll.getFileHistory().getCreateUser());

			service.getComponentService().saveComponentType(serviceEntryType);
		}

		ComponentType contentCollectionType = new ComponentType();
		contentCollectionType.setComponentType(CONTENT_COLLECTION_COMPONENT_TYPE);
		contentCollectionType = contentCollectionType.find();
		if (contentCollectionType == null) {
			contentCollectionType = new ComponentType();
			contentCollectionType.setComponentType(CONTENT_COLLECTION_COMPONENT_TYPE);

			contentCollectionType.setLabel("Describe Content Collection");
			contentCollectionType.setDescription("Describe content collection record");
			contentCollectionType.setDataEntryAttributes(Boolean.TRUE);
			contentCollectionType.setDataEntryContacts(Boolean.TRUE);
			contentCollectionType.setDataEntryMedia(Boolean.TRUE);
			contentCollectionType.setDataEntryMetadata(Boolean.TRUE);
			contentCollectionType.setDataEntryQuestions(Boolean.TRUE);
			contentCollectionType.setDataEntryRelationships(Boolean.TRUE);
			contentCollectionType.setDataEntryResources(Boolean.TRUE);
			contentCollectionType.setDataEntryReviews(Boolean.TRUE);
			serviceEntryType.setCreateUser(fileHistoryAll.getFileHistory().getCreateUser());
			serviceEntryType.setUpdateUser(fileHistoryAll.getFileHistory().getCreateUser());

			service.getComponentService().saveComponentType(contentCollectionType);
		}

		for (Assertion assertion : assertions) {
			if (assertion.getStructuredStatement().getSearchProvider() != null) {
				ComponentAll componentAll = defaultComponentAll(SERVICE_COMPONENT_TYPE);
				Component component = componentAll.getComponent();

				SearchProvider searchProvider = assertion.getStructuredStatement().getSearchProvider();

				component.setName(searchProvider.getGeneralInfo().getName());
				component.setDescription(searchProvider.getGeneralInfo().getDescription());
				component.setGuid(searchProvider.getGeneralInfo().getGuid());
				component.setSecurityMarkingType(getLookup(SecurityMarkingType.class, searchProvider.getGeneralInfo().getDescriptionClassification()));
				component.setComponentType(serviceEntryType.getComponentType());

				for (PointOfContact contact : searchProvider.getGeneralInfo().getContacts()) {
					ComponentContact componentContact = new ComponentContact();
					if (contact.getOrganization() != null) {
						component.setOrganization(contact.getOrganization().getName());

						componentContact.setContactType(getLookup(ContactType.class, ContactType.GOVERNMENT));

						if (StringUtils.isNotBlank(contact.getOrganization().getSubOrganization())) {
							componentContact.setOrganization(contact.getOrganization().getSubOrganization());
						} else {
							componentContact.setOrganization(OpenStorefrontConstant.NOT_AVAILABLE);
						}
						componentContact.setEmail(contact.getOrganization().getEmail());
						componentContact.setPhone(contact.getOrganization().getPhone());
						componentContact.setFirstName(contact.getOrganization().getName());
						componentContact.setLastName(OpenStorefrontConstant.NOT_AVAILABLE);

					} else {
						componentContact.setContactType(getLookup(ContactType.class, ContactType.TECHINCAL));
						componentContact.setEmail(contact.getPerson().getEmail());
						componentContact.setPhone(contact.getPerson().getPhone());
						componentContact.setFirstName(contact.getPerson().getName());
						componentContact.setLastName(contact.getPerson().getSurname());
						componentContact.setOrganization(contact.getPerson().getAffiliation());
					}
					componentAll.getContacts().add(componentContact);
				}

				handleRelatedResources(searchProvider.getRelatedResources(), componentAll);

				for (SearchInterface searchInterface : searchProvider.getSearchInterfaces()) {
					handleRelatedResources(searchInterface.getRelatedResources(), componentAll);

					Service serviceResource = searchInterface.getService();

					for (Address address : serviceResource.getAddresses()) {
						ComponentResource componentResource = new ComponentResource();
						String description = serviceResource.getName();
						if (StringUtils.isNotBlank(address.getNetwork())) {
							description += " - Network: " + address.getNetwork();
						}

						componentResource.setDescription(description);
						componentResource.setRestricted(Convert.toBoolean(serviceResource.getServiceType().getSecure()));
						componentResource.setResourceType(getLookup(ResourceType.class, ResourceType.SERVICE));
						componentResource.setLink(address.getText());
						componentAll.getResources().add(componentResource);
					}

					for (Conformance conformance : serviceResource.getConformances()) {
						handleRelatedResources(conformance.getRelatedResources(), componentAll);

						//add attributes
						AttributeCode attributeCode = getAttributeCode(
								ATTRIBUTE_TYPE_CONFORMANCE,
								ATTRIBUTE_TYPE_CONFORMANCE_DESC,
								conformance.getId(),
								conformance.getName(),
								serviceEntryType.getComponentType()
						);

						if (attributeCode != null) {
							ComponentAttributePk componentAttributePk = new ComponentAttributePk();
							componentAttributePk.setAttributeCode(attributeCode.getAttributeCodePk().getAttributeCode());
							componentAttributePk.setAttributeType(attributeCode.getAttributeCodePk().getAttributeType());

							ComponentAttribute componentAttribute = new ComponentAttribute();
							componentAttribute.setComponentAttributePk(componentAttributePk);

							componentAll.getAttributes().add(componentAttribute);
						}
					}

				}

				//add attributes
				AttributeCode attributeCode = getAttributeCode(
						ATTRIBUTE_TYPE_NETWORK,
						ATTRIBUTE_TYPE_NETWORK_DESC,
						searchProvider.getGeneralInfo().getNetwork(),
						searchProvider.getGeneralInfo().getNetwork(),
						serviceEntryType.getComponentType()
				);

				if (attributeCode != null) {
					ComponentAttributePk componentAttributePk = new ComponentAttributePk();
					componentAttributePk.setAttributeCode(attributeCode.getAttributeCodePk().getAttributeCode());
					componentAttributePk.setAttributeType(attributeCode.getAttributeCodePk().getAttributeType());

					ComponentAttribute componentAttribute = new ComponentAttribute();
					componentAttribute.setComponentAttributePk(componentAttributePk);

					componentAll.getAttributes().add(componentAttribute);
				}

				componentAlls.add(componentAll);

			} else if (assertion.getStructuredStatement().getResource() != null) {
				resourceCollection.setResource(assertion.getStructuredStatement().getResource());
			} else if (assertion.getStructuredStatement().getContentCollection() != null) {
				resourceCollection.setContentCollection(assertion.getStructuredStatement().getContentCollection());
			}
		}

		if (resourceCollection.getResource() != null
				&& resourceCollection.getContentCollection() != null) {
			ComponentAll componentAll = defaultComponentAll(CONTENT_COLLECTION_COMPONENT_TYPE);
			Component component = componentAll.getComponent();

			//combine Resource and contentCollection into one record
			String name = resourceCollection.getResource().getMetacardInfo().getIdentifierValue().replace("_", " ");
			String description = resourceCollection.getResource().getTitle().getText();

			component.setName(name);
			component.setDescription(description);
			component.setSecurityMarkingType(getLookup(SecurityMarkingType.class, resourceCollection.getResource().getTitle().getClassification()));
			component.setOrganization(resourceCollection.getResource().getCreatorName());
			component.setComponentType(contentCollectionType.getComponentType());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			Date releaseDate = sdf.parse(resourceCollection.getResource().getCreateDate(), new ParsePosition(0));
			component.setReleaseDate(releaseDate);
			component.setGuid(resourceCollection.getResource().getGuid());

			ComponentMetadata metadata = new ComponentMetadata();
			metadata.setLabel("Document Type");
			metadata.setValue(resourceCollection.getResource().getType());
			componentAll.getMetadata().add(metadata);

			if (resourceCollection.getResource().getContributor() == null) {
				metadata = new ComponentMetadata();
				metadata.setLabel("Contributor");
				metadata.setValue(resourceCollection.getResource().getContributor().getOrganizationAcronym());
				componentAll.getMetadata().add(metadata);
			}

			if (resourceCollection.getResource().getSubjectCoverage() != null) {
				for (String keyword : resourceCollection.getResource().getSubjectCoverage().getKeywords()) {
					ComponentTag componentTag = new ComponentTag();
					componentTag.setText(keyword);
					componentAll.getTags().add(componentTag);
				}
			}

			if (resourceCollection.getResource().getGeospatialCoverage() != null) {
				for (BoundingGeometry boundingGeometry : resourceCollection.getResource().getGeospatialCoverage().getBoundingGeometries()) {
					for (String point : boundingGeometry.getPoints()) {
						metadata = new ComponentMetadata();
						metadata.setLabel("Geospatial Coverage");
						metadata.setValue(point);
						componentAll.getMetadata().add(metadata);
					}
				}
			}

			//Content collection
			if (resourceCollection.getContentCollection().getMetrics() != null) {
				metadata = new ComponentMetadata();
				metadata.setLabel("Record Count");
				metadata.setValue(resourceCollection.getContentCollection().getMetrics().getCount());
				componentAll.getMetadata().add(metadata);

				metadata = new ComponentMetadata();
				metadata.setLabel("Record Rate");
				Integer rate = Convert.toInteger(resourceCollection.getContentCollection().getMetrics().getRecordRate().getText());
				if (rate != null) {
					metadata.setValue(rate + " " + StringProcessor.puralize(rate, resourceCollection.getContentCollection().getMetrics().getRecordRate().getFrequency(), null));
					componentAll.getMetadata().add(metadata);
				}
			}

			metadata = new ComponentMetadata();
			metadata.setLabel("Originator");
			metadata.setValue(resourceCollection.getContentCollection().getOriginator());
			componentAll.getMetadata().add(metadata);

			metadata = new ComponentMetadata();
			metadata.setLabel("Collection Updated");
			metadata.setValue(resourceCollection.getContentCollection().getUpdated());
			componentAll.getMetadata().add(metadata);

			for (MimeType mimeType : resourceCollection.getContentCollection().getMimeTypes()) {
				AttributeCode attributeCode = getAttributeCode(
						ATTRIBUTE_TYPE_MIMETYPE,
						ATTRIBUTE_TYPE_MIMETYPE_DESC,
						mimeType.getText(),
						mimeType.getText(),
						serviceEntryType.getComponentType()
				);

				if (attributeCode != null) {
					ComponentAttributePk componentAttributePk = new ComponentAttributePk();
					componentAttributePk.setAttributeCode(attributeCode.getAttributeCodePk().getAttributeCode());
					componentAttributePk.setAttributeType(attributeCode.getAttributeCodePk().getAttributeType());

					ComponentAttribute componentAttribute = new ComponentAttribute();
					componentAttribute.setComponentAttributePk(componentAttributePk);
					componentAll.getAttributes().add(componentAttribute);
				}
			}

			componentAlls.add(componentAll);
		}

		return componentAlls;
	}

	private void handleRelatedResources(List<RelatedResource> relatedResources, ComponentAll componentAll)
	{
		for (RelatedResource relatedResource : relatedResources) {
			ComponentResource componentResource = new ComponentResource();
			componentResource.setDescription(relatedResource.getDescription());
			componentResource.setResourceType(getLookup(ResourceType.class, relatedResource.getRelationship()));
			componentResource.setLink(relatedResource.getLink());
			componentAll.getResources().add(componentResource);
		}
	}

	@Override
	protected void finishProcessing()
	{
		super.finishProcessing();

		//Put Relationships together
		Component componentExample = new Component();
		componentExample.setFileHistoryId(fileHistoryAll.getFileHistory().getFileHistoryId());

		List<Component> components = componentExample.findByExample();

		//Assume one service record (We only example...not sure how they packing multiple records)
		Component serviceComponent = null;
		List<Component> collections = new ArrayList<>();
		for (Component component : components) {
			if (SERVICE_COMPONENT_TYPE.equals(component.getComponentType())) {
				serviceComponent = component;
			} else {
				collections.add(component);
			}
		}

		if (serviceComponent != null) {
			for (Component contentCollection : collections) {
				ComponentRelationship componentRelationship = new ComponentRelationship();
				componentRelationship.setRelationshipType(getLookup(RelationshipType.class, "Provides"));
				componentRelationship.setComponentId(serviceComponent.getComponentId());
				componentRelationship.setRelatedComponentId(contentCollection.getComponentId());

				service.getComponentService().saveComponentRelationship(componentRelationship);
			}
		}
	}

}
