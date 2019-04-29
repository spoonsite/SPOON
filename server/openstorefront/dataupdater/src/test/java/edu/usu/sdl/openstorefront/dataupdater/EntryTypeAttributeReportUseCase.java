package edu.usu.sdl.openstorefront.dataupdater;

import au.com.bytecode.opencsv.CSVWriter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.apiclient.APIResponse;
import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.view.ComponentAdminView;
import edu.usu.sdl.openstorefront.core.view.ComponentAdminWrapper;
import edu.usu.sdl.openstorefront.core.view.ComponentAttributeView;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

public class EntryTypeAttributeReportUseCase
{

	@Test
	public void createEntryTypeAttributeReport()
	{
		ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();

		System.out.println("Connecting to API...");
		ClientAPI clientAPI = new ClientAPI(objectMapper);
		// fill in your username and password to connect
		clientAPI.connect("USERNAME", "PASSWORD", "http://spoonsite.usurf.usu.edu/openstorefront"); //"https://spoonsite.com/openstorefront"  //Cert issue
		System.out.println("Connected");

		System.out.println("Pulling all components..");
		APIResponse response = clientAPI.httpGet("/api/v1/resource/components/filterable", null);
		ComponentAdminWrapper componentWrapper = response.getList(new TypeReference<ComponentAdminWrapper>()
		{
		});
		System.out.println("Found: " + componentWrapper.getTotalNumber());

		Map<String, Map<String, ComponentAttributeView>> entryTypeMap = new HashMap<>();
		Map<String, String> entryTypeDescMap = new HashMap<>();

		for (ComponentAdminView component : componentWrapper.getComponents()) {

			entryTypeDescMap.put(component.getComponent().getComponentType(), component.getComponent().getComponentTypeLabel());

			System.out.println("Pulling Attributes for: " + component.getComponent().getName());
			response = clientAPI.httpGet("/api/v1/resource/components/" + component.getComponent().getComponentId() + "/attributes/view", null);
			//pull component attributes view
			List<ComponentAttributeView> attributes = response.getList(new TypeReference<List<ComponentAttributeView>>()
			{
			});
			Map<String, ComponentAttributeView> attributeMap = entryTypeMap.get(component.getComponent().getComponentType());

			if (attributeMap == null) {
				attributeMap = new HashMap<>();
			}

			//group by entry-type (list unique attributes per type)
			for (ComponentAttributeView attributeView : attributes) {
				attributeMap.put(attributeView.getType(), attributeView);
			}

			entryTypeMap.put(component.getComponent().getComponentType(), attributeMap);
		}

		System.out.println("Writing Report...");
		try (Writer writer = new FileWriter("/test/spoon/entryTypeReport.csv")) {
			CSVWriter csvWriter = new CSVWriter(writer);

			csvWriter.writeNext(new String[]{
				"Entry Type",
				"Attribute",
				"Unit"
			});

			for (String entryType : entryTypeMap.keySet()) {

				csvWriter.writeNext(new String[]{
					entryTypeDescMap.get(entryType)
				});

				Map<String, ComponentAttributeView> attributeMap = entryTypeMap.get(entryType);
				if (attributeMap != null) {
					for (String attributeKey : attributeMap.keySet()) {
						ComponentAttributeView attributeView = attributeMap.get(attributeKey);
						csvWriter.writeNext(new String[]{
							"",
							attributeView.getTypeDescription(),
							attributeView.getUnit()
						});
					}
				} else {
					csvWriter.writeNext(new String[]{
						"",
						"No Attributes Found",
						""
					});
				}
				csvWriter.writeNext(new String[]{
					""
				});
			}
			System.out.println("Done");

			csvWriter.flush();
			csvWriter.close();
		} catch (IOException ex) {
			Logger.getLogger(POCReportUseCase.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

}
