/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.openstorefront.dataupdater;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.apiclient.APIResponse;
import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.SearchResultAttribute;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class RemoveDuplicateAttributesUseCase
{

	@Test
	public void removeDuplicateAttributes()
	{

		//scan results for duplicates
		ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();

		System.out.println("Connecting to API...");
		ClientAPI clientAPI = new ClientAPI(objectMapper);
		clientAPI.connect("", "", "http://spoonsite.usurf.usu.edu/openstorefront"); //"https://spoonsite.com/openstorefront");  //Cert issue
		System.out.println("Connected");

		try {

			APIResponse response = clientAPI.httpGet("/api/v1/service/search/all", null);
			List<ComponentSearchView> results = response.getList(new TypeReference<List<ComponentSearchView>>()
			{
			});

			for (ComponentSearchView view : results) {
				Set<String> uniqueAttributes = new HashSet<>();
				for (SearchResultAttribute attribute : view.getAttributes()) {
					String attributeKey = attribute.getType() + "-" + attribute.getCode();
					if (uniqueAttributes.contains(attributeKey)) {

						//api/v1/resource/components/{id}/attributes/{attributeType}/{attributeCode}/force
						clientAPI.httpDelete("/api/v1/resource/components/"
								+ view.getComponentId()
								+ "/attributes/"
								+ attribute.getType()
								+ "/"
								+ attribute.getCode()
								+ "/force", null);

						System.out.println("Remove Duplicate:  " + attributeKey + " on " + view.getName());

					} else {
						uniqueAttributes.add(attributeKey);
					}
				}
			}

		} catch (Exception ex) {
			Logger.getLogger(UpdateLinkUseCase.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			clientAPI.disconnect();
		}

	}

}
