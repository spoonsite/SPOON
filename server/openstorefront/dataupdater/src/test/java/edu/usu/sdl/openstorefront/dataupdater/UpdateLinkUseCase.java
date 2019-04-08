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
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.view.ComponentResourceView;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class UpdateLinkUseCase
{

	@Test
	public void updateRestrictLinks()
	{
		//https://spoonsite.com/openstorefront/api/v1/resource/components/resources

		//restricted
		//share.nasa.gov
		//teams.share.nasa.gov
		//csbf.nasa.gov
		Set<String> containSet = new HashSet<>();
		containSet.add("share.nasa.gov");
		containSet.add("teams.share.nasa.gov");
		containSet.add("csbf.nasa.gov");

		ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();

		System.out.println("Connecting to API...");
		ClientAPI clientAPI = new ClientAPI(objectMapper);
		clientAPI.connect("", "", "http://spoonsite.usurf.usu.edu/openstorefront"); //"https://spoonsite.com/openstorefront");  //Cert issue
		System.out.println("Connected");

		try {

			APIResponse response = clientAPI.httpGet("/api/v1/resource/components/resources", null);
			List<ComponentResourceView> resources = response.getList(new TypeReference<List<ComponentResourceView>>()
			{
			});

			try (CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(new FileOutputStream("/test/spoon/compsToReview.csv")))) {

				for (ComponentResourceView resource : resources) {

					//find entries with resources that
					if (StringUtils.isNotBlank(resource.getOriginalFileName())) {
						//has attachment

						APIResponse compResponse = clientAPI.httpGet("/api/v1/resource/components/" + resource.getComponentId(), null);

						Component component = compResponse.getResponse(Component.class);
						if (component.getDescription().length() < 100) {
							csvWriter.writeNext(new String[]{
								component.getName(),
								resource.getOriginalFileName(),
								component.getDescription(),
								component.getLastActivityDts().toString()
							});
						}
					}

//				boolean restrict = false;
//				for (String key : containSet) {
//					if (StringUtils.isNotBlank(resource.getLink()) && resource.getLink().contains(key)) {
//						restrict = true;
//					}
//				}
//				if (restrict) {
//					System.out.println("Restrict: " + resource.getLink());
//
//					ComponentResource componentResource = new ComponentResource();
//					componentResource.setResourceType(resource.getResourceType());
//					componentResource.setRestricted(true);
//					componentResource.setDescription("NASA INTRANET");
//					componentResource.setLink(resource.getLink());
//
//					APIResponse updateResponse = clientAPI.httpPut("/api/v1/resource/components/"
//							+ resource.getComponentId()
//							+ "/resources/" + resource.getResourceId(),
//							componentResource, null);
//
//					System.out.println("Updated Restriction? " + updateResponse.getResponseCode());
//				}
				}

			} catch (IOException c) {
				Logger.getLogger(UpdateLinkUseCase.class.getName()).log(Level.SEVERE, null, c);
			}

		} catch (Exception ex) {
			Logger.getLogger(UpdateLinkUseCase.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			clientAPI.disconnect();
		}
	}

}
