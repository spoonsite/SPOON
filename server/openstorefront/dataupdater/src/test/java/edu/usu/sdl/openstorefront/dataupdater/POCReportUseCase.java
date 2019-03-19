/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.openstorefront.dataupdater;

import au.com.bytecode.opencsv.CSVWriter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.apiclient.APIResponse;
import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.Organization;
import edu.usu.sdl.openstorefront.core.view.ComponentContactView;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.OrganizationWrapper;
import edu.usu.sdl.openstorefront.core.view.SearchResultAttribute;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class POCReportUseCase
{

	@Test
	public void generatePOCReport()
	{
		ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();

		System.out.println("Connecting to API...");
		ClientAPI clientAPI = new ClientAPI(objectMapper);
		clientAPI.connect("", "", "http://spoonsite.usurf.usu.edu/openstorefront"); //"https://spoonsite.com/openstorefront");  //Cert issue
		System.out.println("Connected");

		//pull components
		APIResponse response = clientAPI.httpGet("/api/v1/resource/components", null);
		List<ComponentSearchView> components = response.getList(new TypeReference<List<ComponentSearchView>>()
		{
		});
		System.out.println("Components: " + components.size());
		Map<String, List<ComponentSearchView>> componentMap = components.stream()
				.collect(Collectors.groupingBy(ComponentSearchView::getComponentId));

		//pull orgs
		response = clientAPI.httpGet("/api/v1/resource/organizations", null);
		OrganizationWrapper orgsWrapper = response.getList(new TypeReference<OrganizationWrapper>()
		{
		});
		System.out.println("Organization: " + orgsWrapper.getData().size());
		Map<String, List<Organization>> orgMap = orgsWrapper.getData().stream()
				.collect(Collectors.groupingBy(Organization::getName));

		//pull contacts
		Map<String, List<ComponentContactView>> contactMap = new HashMap<>();
		for (String componentId : componentMap.keySet()) {
			response = clientAPI.httpGet("/api/v1/resource/components/" + componentId + "/contacts/view", null);
			List<ComponentContactView> contacts = response.getList(new TypeReference<List<ComponentContactView>>()
			{
			});
			System.out.println("Pulling contact for: " + componentId);
			contactMap.put(componentId, contacts);
		}

		Map<String, List<ComponentSearchView>> orgComponents = components.stream()
				.collect(Collectors.groupingBy(ComponentSearchView::getOrganization));

		System.out.println("Writing Report...");
		try (Writer writer = new FileWriter("/test/spoon/pocreport.csv")) {
			//Organize Report
			//generate

			CSVWriter csvWriter = new CSVWriter(writer);

			for (String organization : orgComponents.keySet()) {
				csvWriter.writeNext(new String[]{
					organization
				});
				csvWriter.writeNext(new String[]{
					""
				});
				csvWriter.writeNext(new String[]{
					"Contacts"
				});
				csvWriter.writeNext(new String[]{
					"Name",
					"Email",
					"Phone",
					"Organization Contact"
				});

				List<Organization> organizationAll = orgMap.get(organization);
				if (organizationAll != null) {
					Organization organizationFull = organizationAll.get(0);
					if (StringUtils.isNotBlank(organizationFull.getContactEmail())
							|| StringUtils.isNotBlank(organizationFull.getContactPhone())) {
						csvWriter.writeNext(new String[]{
							organizationFull.getContactName(),
							organizationFull.getContactEmail(),
							organizationFull.getContactPhone(),
							"T"
						});
					}
				}

				List<ComponentSearchView> orgParts = orgComponents.get(organization);
				for (ComponentSearchView part : orgParts) {

					List<ComponentContactView> contacts = contactMap.get(part.getComponentId());
					if (contacts != null) {
						contacts.forEach(c -> {
							csvWriter.writeNext(new String[]{
								c.getFirstName() + " " + c.getLastName(),
								c.getEmail(),
								c.getPhone(),
								""
							});
						});
					}
				}

				csvWriter.writeNext(new String[]{
					""
				});
				csvWriter.writeNext(new String[]{
					"Components: (SPOON Entries)"
				});

				csvWriter.writeNext(new String[]{
					"Name",
					"Entry POC(s)"
				});

				for (ComponentSearchView part : orgParts) {

					List<ComponentContactView> contacts = contactMap.get(part.getComponentId());
					StringBuilder allContacts = new StringBuilder();
					if (contacts != null) {
						contacts.forEach(c -> {
							allContacts.append(c.getFirstName() + " " + c.getLastName())
									.append(" (")
									.append(c.getEmail())
									.append(" ")
									.append(c.getPhone())
									.append(" ")
									.append(c.getPositionDescription())
									.append("), ");
						});
					}

					csvWriter.writeNext(new String[]{
						part.getName(),
						allContacts.toString()
					});
				}
				csvWriter.writeNext(new String[]{
					""
				});
				csvWriter.writeNext(new String[]{
					"-------------------------------"
				});
			}
			System.out.println("Done");

			csvWriter.flush();
			csvWriter.close();
		} catch (IOException ex) {
			Logger.getLogger(POCReportUseCase.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	@Test
	public void generateCheckDataReport()
	{
		ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();

		System.out.println("Connecting to API...");
		ClientAPI clientAPI = new ClientAPI(objectMapper);
		clientAPI.connect("", "", "http://spoonsite.usurf.usu.edu/openstorefront"); //"https://spoonsite.com/openstorefront");  //Cert issue
		System.out.println("Connected");

		//pull components
		APIResponse response = clientAPI.httpGet("/api/v1/resource/components", null);
		List<ComponentSearchView> components = response.getList(new TypeReference<List<ComponentSearchView>>()
		{
		});

		//pull attributes for components
		List<ComponentSearchView> checkComponents = components.stream()
				.filter(c -> {
					return checkComponent(c);
				})
				.collect(Collectors.toList());

		//check conditions
		System.out.println("Keep: ");
		checkComponents.forEach(c -> {
			System.out.println(c.getName());
		});

		//report
		System.out.println("Writing Report...");
		try (Writer writer = new FileWriter("/test/spoon/trlreport.csv")) {

			CSVWriter csvWriter = new CSVWriter(writer);

			csvWriter.writeNext(new String[]{
				"Name",
				"Heritage",
				"TRL Level"
			});

			for (ComponentSearchView component : checkComponents) {

				csvWriter.writeNext(new String[]{
					component.getName(),
					getHeritage(component),
					getTRL(component)
				});

			}

			System.out.println("Done");

			csvWriter.flush();
			csvWriter.close();
		} catch (IOException ex) {
			Logger.getLogger(POCReportUseCase.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private boolean checkComponent(ComponentSearchView view)
	{
		boolean keep = false;

		if (view.getAttributes() != null) {
			boolean hasHertiage = false;
			for (SearchResultAttribute attribute : view.getAttributes()) {
				if ("HERITAGE".equals(attribute.getType())) {
					hasHertiage = true;
				}
			}

			if (hasHertiage) {
				for (SearchResultAttribute attribute : view.getAttributes()) {
					if ("TRL".equals(attribute.getType())) {
						if (!"TRL9".equals(attribute.getCode())) {
							keep = true;
						}
					}
				}
			}
		}

		return keep;
	}

	private String getHeritage(ComponentSearchView view)
	{
		String heritage = "";

		for (SearchResultAttribute attribute : view.getAttributes()) {
			if ("HERITAGE".equals(attribute.getType())) {
				heritage = attribute.getLabel();
			}
		}

		return heritage;
	}

	private String getTRL(ComponentSearchView view)
	{
		String trl = "";

		for (SearchResultAttribute attribute : view.getAttributes()) {
			if ("TRL".equals(attribute.getType())) {
				trl = attribute.getLabel();
			}
		}
		return trl;
	}

}
