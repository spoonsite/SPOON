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
package edu.usu.sdl.loadgenerator;

import au.com.bytecode.opencsv.CSVReader;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentContact;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import static edu.usu.sdl.openstorefront.core.entity.ComponentType.COMPONENT;
import edu.usu.sdl.openstorefront.core.entity.Contact;
import static edu.usu.sdl.openstorefront.core.entity.ContactType.SUBMITTER;
import static edu.usu.sdl.openstorefront.core.entity.ResourceType.*;
import edu.usu.sdl.openstorefront.core.model.AttributeAll;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author ccummings
 */
public class LoadGenerator
{

	public int generateRandomNumber(int lower_bound, int upper_bound)
	{
		Random random_num = new Random();
		int randomNumber = random_num.nextInt(upper_bound - lower_bound) + lower_bound;

		return randomNumber;

	}

	public String generateDescription()
	{
		BufferedReader reader
				= new BufferedReader(new InputStreamReader(FileSystemManager.getInstance().getApplicationResourceFile("/generate_description.txt")));

		String fileData = reader.lines().collect(Collectors.joining("<br>"));

		return fileData;
	}

	public List<String> readLinesFromFile(String filepath)
	{
		BufferedReader reader
				= new BufferedReader(new InputStreamReader(FileSystemManager.getInstance().getApplicationResourceFile(filepath), StandardCharsets.UTF_8));
		List<String> objects = new ArrayList();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				objects.add(line);
			}
		} catch (IOException ex) {
			Logger.getLogger(LoadGenerator.class.getName()).log(Level.SEVERE, null, ex);
		}
		return objects;
	}

	public List<String> getOrganizationList() throws IOException
	{
		List<String> organizations = new ArrayList();
		BufferedReader reader
				= new BufferedReader(new InputStreamReader(FileSystemManager.getInstance().getApplicationResourceFile("/organizations.txt")));
		String line;
		while ((line = reader.readLine()) != null) {
			organizations.add(line);
		}
		return organizations;
	}

	public List<String> getComponentNameList()
	{
		List<String> cities = readLinesFromFile("/cities.txt");
		return cities;
	}

	public void createContactCSV() throws IOException
	{
		List<String> names = readLinesFromFile("/names.txt");
		String[] fullNames = null;
		List<String> firstNames = new ArrayList();
		List<String> lastNames = new ArrayList();
		for (String name : names) {
			fullNames = name.split(" ");
			firstNames.add(fullNames[0]);
			lastNames.add(fullNames[1]);
		}
		File file
				= new File("/Users/ccummings/Documents/openstorefront/server/openstorefront/loadGenerator/src/main/resources/contacts.csv");
		FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
		BufferedWriter writer = new BufferedWriter(fileWriter);
		for (int i = 0; i < firstNames.size(); i++) {
			String first = firstNames.get(i);
			String last = lastNames.get(i);
			String email = first.toLowerCase() + "." + last.toLowerCase() + "@testgenerator.com";
			String contactInfo = first + "," + last + "," + email + "\n";
			writer.write(contactInfo);
		}
		writer.close();
	}

	public List<Contact> getContactList() throws IOException
	{
		List<String> organizations = getOrganizationList();
		List<Contact> contacts = new ArrayList();
		CSVReader reader
				= new CSVReader(new InputStreamReader(FileSystemManager.getInstance().getApplicationResourceFile("/contacts.csv")));

		String[] contactFile;
		while ((contactFile = reader.readNext()) != null) {

			Contact contact = new Contact();
			contact.setFirstName(contactFile[0]);
			contact.setLastName(contactFile[1]);
			contact.setEmail(contactFile[2]);
			int randNum = generateRandomNumber(0, 100);
			contact.setOrganization(organizations.get(randNum));

			contacts.add(contact);
		}
		return contacts;
	}

	public void createAttributeCSV() throws IOException
	{
		List<String> attributes = readLinesFromFile("/list_of_cars.txt");
		String[] attributeList = null;
		List<String> attrTypes = new ArrayList();
		List<String> attrCodes = new ArrayList();
		for (String attribute : attributes) {
			attributeList = attribute.split(" ");
			attrTypes.add(attributeList[0]);
			attrCodes.add(attributeList[1]);
		}
		File file
				= new File("/Users/ccummings/Documents/openstorefront/server/openstorefront/loadGenerator/src/main/resources/attributes.csv");
		FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
		BufferedWriter writer = new BufferedWriter(fileWriter);
		for (int i = 0; i < attrTypes.size(); i++) {
			String type = attrTypes.get(i);
			String code = attrCodes.get(i);
			String label = "Attribute label for " + type + "-" + code;
			String contactInfo = type + "," + code + "," + label + "\n";
			writer.write(contactInfo);
		}
		writer.close();
	}

	public List<AttributeAll> getAttributeList() throws IOException
	{
		List<AttributeAll> attributeAll = new ArrayList();
		CSVReader reader
				= new CSVReader(new InputStreamReader(FileSystemManager.getInstance().getApplicationResourceFile("/attributes.csv")));

		String[] attributeFile;
		while ((attributeFile = reader.readNext()) != null) {
			AttributeAll attribute = new AttributeAll();
			AttributeType type = new AttributeType();
			type.setAttributeType(attributeFile[0]);
			type.setDescription(attributeFile[0]);
			type.setVisibleFlg(FALSE);
			type.setAllowMultipleFlg(TRUE);
			type.setRequiredFlg(FALSE);
			type.setArchitectureFlg(FALSE);
			type.setImportantFlg(FALSE);
			type.setHideOnSubmission(FALSE);
			type.setAllowUserGeneratedCodes(FALSE);
			AttributeCodePk attributeCodePk = new AttributeCodePk();
			attributeCodePk.setAttributeType(attributeFile[0]);
			attributeCodePk.setAttributeCode(attributeFile[1]);
			AttributeCode code = new AttributeCode();
			code.setAttributeCodePk(attributeCodePk);
			code.setLabel(attributeFile[2]);
			List<AttributeCode> attrCodes = new ArrayList();
			attrCodes.add(code);
			attribute.setAttributeCodes(attrCodes);
			attribute.setAttributeType(type);

			attributeAll.add(attribute);
		}
		return attributeAll;
	}

	public List<AttributeCodePk> getAttributeCodePkList() throws IOException
	{
		List<AttributeAll> attributes = getAttributeList();
		List<AttributeCodePk> attributeCodePks = new ArrayList();

		for (AttributeAll attribute : attributes) {
			attributeCodePks.add(attribute.getAttributeCodes().get(0).getAttributeCodePk());
		}
		return attributeCodePks;
	}

	public List<String> createResourceLinkList() throws IOException
	{
		List<String> resourceList = new ArrayList();

		BufferedReader reader
				= new BufferedReader(new InputStreamReader(FileSystemManager.getInstance().getApplicationResourceFile("/list_of_links.txt")));
		String line;
		while ((line = reader.readLine()) != null) {
			resourceList.add(line);
		}

		return resourceList;
	}

	public List<ComponentResource> getResourceList() throws IOException
	{
		List<String> resourceLinks = createResourceLinkList();
		List<ComponentResource> resources = new ArrayList();

		for (String link : resourceLinks) {
			ComponentResource resource = new ComponentResource();
			resource.setLink(link);
			resource.setResourceType(DOCUMENT);
			resources.add(resource);
		}
		return resources;
	}

	public List<ComponentTag> getListTags() throws IOException
	{
		List<String> listTags = readLinesFromFile("/list_of_tags.txt");
		List<ComponentTag> tags = new ArrayList();
		int count = 0;
		while (count < listTags.size()) {
			ComponentTag tag = new ComponentTag();
			tag.setText(listTags.get(count));
			tags.add(tag);
			count++;
		}

		return tags;
	}

	public List<ComponentAll> getComponentList(int numberComponents, int startCountAt) throws IOException
	{
		String description = generateDescription();
		List<ComponentTag> tags = getListTags();
		List<AttributeCodePk> attributeCodePks = getAttributeCodePkList();
		List<ComponentResource> resources = getResourceList();
		List<String> organizations = getOrganizationList();
		List<Contact> contacts = getContactList();
		List<ComponentAll> componentAllList = new ArrayList();
		List<String> cities = getComponentNameList();
		int randomOrg = 0;
		int maxDescription = 0;
		int randAttrPk = 0;
		int randContact = 0;
		int randResource = 0;
		int count = startCountAt;
		int index = 0;
		int firstSet = (int) (numberComponents * .25);
		int secondSet = (int) (numberComponents * .75);

		while (index < numberComponents) {

			if (index < firstSet) {
				ComponentAll componentAll = new ComponentAll();
				Component component = new Component();
				component.setComponentType(COMPONENT);
				component.setName("Component " + count);
				randomOrg = generateRandomNumber(0, 100);
				component.setOrganization(organizations.get(randomOrg));
				maxDescription = generateRandomNumber(100, 1000);
				component.setDescription(description.substring(0, maxDescription));
				componentAll.setComponent(component);
				List<ComponentAttribute> randAttributes = new ArrayList();
				int numAttributes = generateRandomNumber(1, 5);
				for (int i = 0; i < numAttributes; i++) {
					randAttrPk = generateRandomNumber(0, 100);
					ComponentAttribute compAttribute = new ComponentAttribute();
					ComponentAttributePk attributePk = new ComponentAttributePk();
					attributePk.setAttributeCode(attributeCodePks.get(randAttrPk).getAttributeCode());
					attributePk.setAttributeType(attributeCodePks.get(randAttrPk).getAttributeType());
					compAttribute.setComponentAttributePk(attributePk);
					randAttributes.add(compAttribute);
				}
				componentAll.setAttributes(randAttributes);
				int numContacts = generateRandomNumber(1, 10);
				List<ComponentContact> compContacts = new ArrayList();
				for (int i = 0; i < numContacts; i++) {
					randContact = generateRandomNumber(0, 100);
					ComponentContact contact = new ComponentContact();
					contact.setFirstName(contacts.get(randContact).getFirstName());
					contact.setLastName(contacts.get(randContact).getLastName());
					contact.setEmail(contacts.get(randContact).getEmail());
					contact.setOrganization(contacts.get(randContact).getOrganization());
					contact.setContactType(SUBMITTER);
					compContacts.add(contact);
				}
				componentAll.setContacts(compContacts);

				int numResources = generateRandomNumber(0, 5);
				List<ComponentResource> compResources = new ArrayList();
				for (int i = 0; i < numResources; i++) {
					randResource = generateRandomNumber(0, 39);
					ComponentResource componentResource = new ComponentResource();
					componentResource.setLink(resources.get(randResource).getLink());
					componentResource.setResourceType(resources.get(randResource).getResourceType());
					compResources.add(componentResource);
				}
				componentAll.setResources(compResources);

				componentAllList.add(componentAll);

			} else if (index < secondSet) {
				ComponentAll componentAll = new ComponentAll();
				Component component = new Component();
				component.setComponentType(COMPONENT);
				component.setName("Component " + count);
				randomOrg = generateRandomNumber(0, 100);
				component.setOrganization(organizations.get(randomOrg));
				maxDescription = generateRandomNumber(1001, 4000);
				component.setDescription(description.substring(0, maxDescription));
				componentAll.setComponent(component);
				List<ComponentAttribute> randAttributes = new ArrayList();
				int numAttributes = generateRandomNumber(1, 5);
				for (int i = 0; i < numAttributes; i++) {
					randAttrPk = generateRandomNumber(0, 100);
					ComponentAttribute compAttribute = new ComponentAttribute();
					ComponentAttributePk attributePk = new ComponentAttributePk();
					attributePk.setAttributeCode(attributeCodePks.get(randAttrPk).getAttributeCode());
					attributePk.setAttributeType(attributeCodePks.get(randAttrPk).getAttributeType());
					compAttribute.setComponentAttributePk(attributePk);
					randAttributes.add(compAttribute);
				}
				componentAll.setAttributes(randAttributes);
				int numContacts = generateRandomNumber(1, 10);
				List<ComponentContact> compContacts = new ArrayList();
				for (int i = 0; i < numContacts; i++) {
					randContact = generateRandomNumber(0, 100);
					ComponentContact contact = new ComponentContact();
					contact.setFirstName(contacts.get(randContact).getFirstName());
					contact.setLastName(contacts.get(randContact).getLastName());
					contact.setEmail(contacts.get(randContact).getEmail());
					contact.setOrganization(contacts.get(randContact).getOrganization());
					contact.setContactType(SUBMITTER);
					compContacts.add(contact);
				}
				componentAll.setContacts(compContacts);

				int numResources = generateRandomNumber(0, 5);
				List<ComponentResource> compResources = new ArrayList();
				for (int i = 0; i < numResources; i++) {
					randResource = generateRandomNumber(0, 39);
					ComponentResource componentResource = new ComponentResource();
					componentResource.setLink(resources.get(randResource).getLink());
					componentResource.setResourceType(resources.get(randResource).getResourceType());
					compResources.add(componentResource);
				}
				componentAll.setResources(compResources);

				int numTags = generateRandomNumber(1, 10);
				List<ComponentTag> compTags = new ArrayList();
				for (int i = 0; i < numTags; i++) {
					int randIndex = generateRandomNumber(0, 39);
					ComponentTag componentTag = new ComponentTag();
					componentTag.setText(tags.get(randIndex).getText());
					compTags.add(componentTag);
				}
				componentAll.setTags(compTags);
				componentAllList.add(componentAll);

			} else {
				ComponentAll componentAll = new ComponentAll();
				Component component = new Component();
				component.setComponentType(COMPONENT);
				component.setName("Component " + count);
				randomOrg = generateRandomNumber(0, 100);
				component.setOrganization(organizations.get(randomOrg));
				maxDescription = generateRandomNumber(4001, 16000);
				component.setDescription(description.substring(0, maxDescription));
				componentAll.setComponent(component);

				List<ComponentAttribute> randAttributes = new ArrayList();
				int numAttributes = generateRandomNumber(1, 5);
				for (int i = 0; i < numAttributes; i++) {
					randAttrPk = generateRandomNumber(0, 100);
					ComponentAttribute compAttribute = new ComponentAttribute();
					ComponentAttributePk attributePk = new ComponentAttributePk();
					attributePk.setAttributeCode(attributeCodePks.get(randAttrPk).getAttributeCode());
					attributePk.setAttributeType(attributeCodePks.get(randAttrPk).getAttributeType());
					compAttribute.setComponentAttributePk(attributePk);
					randAttributes.add(compAttribute);
				}
				componentAll.setAttributes(randAttributes);

				int numContacts = generateRandomNumber(1, 10);
				List<ComponentContact> compContacts = new ArrayList();
				for (int i = 0; i < numContacts; i++) {
					randContact = generateRandomNumber(0, 100);
					ComponentContact contact = new ComponentContact();
					contact.setFirstName(contacts.get(randContact).getFirstName());
					contact.setLastName(contacts.get(randContact).getLastName());
					contact.setEmail(contacts.get(randContact).getEmail());
					contact.setOrganization(contacts.get(randContact).getOrganization());
					contact.setContactType(SUBMITTER);
					compContacts.add(contact);
				}
				componentAll.setContacts(compContacts);

				int numResources = generateRandomNumber(0, 5);
				List<ComponentResource> compResources = new ArrayList();
				for (int i = 0; i < numResources; i++) {
					randResource = generateRandomNumber(0, 39);
					ComponentResource componentResource = new ComponentResource();
					componentResource.setLink(resources.get(randResource).getLink());
					componentResource.setResourceType(resources.get(randResource).getResourceType());
					compResources.add(componentResource);
				}
				componentAll.setResources(compResources);

				componentAllList.add(componentAll);
			}
			count++;
			index++;
		}

		return componentAllList;
	}

	public List<ComponentAll> createTestSet(int startCountAt) throws IOException
	{
		List<ComponentAll> compAllTestSet = getComponentList(10, startCountAt);
		return compAllTestSet;
	}

	public List<ComponentAll> createSearchSet(int startCountAt) throws IOException
	{
		List<ComponentAll> compAllSearchSet = getComponentList(2000, startCountAt);
		return compAllSearchSet;
	}

	public List<ComponentAll> createSmallSet(int startCountAt) throws IOException
	{
		List<ComponentAll> compAllSmallSet = getComponentList(1000, startCountAt);
		return compAllSmallSet;
	}

	public List<ComponentAll> createMediumSet(int startCountAt) throws IOException
	{
		List<ComponentAll> compAllMediumSet = getComponentList(5000, startCountAt);
		return compAllMediumSet;
	}

	public List<ComponentAll> createLargeSet(int startCountAt) throws IOException
	{
		List<ComponentAll> compAllLargeSet = getComponentList(10000, startCountAt);
		return compAllLargeSet;
	}

	public List<ComponentAll> createXLargeSet(int startCountAt) throws IOException
	{
		List<ComponentAll> compAllXLargeSet = getComponentList(50000, startCountAt);
		return compAllXLargeSet;
	}

	public static void main(String[] args) throws IOException
	{
		LoadGenerator generator = new LoadGenerator();
//		System.out.print(generator.generateDescription(10000, 13000));
//		List<ComponentResource> component_resources = generator.getResourceList();
//		System.out.println(component_resources.get(0).getLink());
//		System.out.println(component_resources.get(1).getLink());
		List<AttributeAll> attributes = generator.getAttributeList();

		FileOutputStream fileOut
				= new FileOutputStream("C:\\Development\\process-data\\attributList.json");
		StringProcessor.defaultObjectMapper().writeValue(fileOut, attributes);

//		List<ComponentAll> searchSet = generator.createSearchSet(0);
//		List<ComponentAll> smallComponentSet = generator.createSmallSet(0);
//		List<ComponentAll> mediumComponentSet = generator.createMediumSet(0);
//		List<ComponentAll> largeComponentSet = generator.createLargeSet(0);
		List<ComponentAll> xlargeComponentSet1 = generator.createXLargeSet(0);
//		List<ComponentAll> xlargeComponentSet2 = generator.createXLargeSet(50001);

//		fileOut = new FileOutputStream("C:\\Development\\process-data\\smallComponentSet.json");
//		StringProcessor.defaultObjectMapper().writeValue(fileOut, smallComponentSet);
//		fileOut = new FileOutputStream("C:\\Development\\process-data\\mediumComponentSet.json");
//		StringProcessor.defaultObjectMapper().writeValue(fileOut, mediumComponentSet);
//		fileOut = new FileOutputStream("C:\\Development\\process-data\\largeComponentSet.json");
//		StringProcessor.defaultObjectMapper().writeValue(fileOut, largeComponentSet);
		fileOut = new FileOutputStream("C:\\Development\\process-data\\xlargeComponentSet1.json");
		StringProcessor.defaultObjectMapper().writeValue(fileOut, xlargeComponentSet1);
//		fileOut = new FileOutputStream("C:\\Development\\process-data\\xlargeComponentSet2.json");
//		StringProcessor.defaultObjectMapper().writeValue(fileOut, xlargeComponentSet2);

//		fileOut = new FileOutputStream("C:\\Development\\process-data\\searchSet.json");
//		StringProcessor.defaultObjectMapper().writeValue(fileOut, searchSet);
	}

}
