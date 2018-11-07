/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.openstorefront.dataupdater;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.ComponentMedia;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchWrapper;
import edu.usu.sdl.openstorefront.core.view.SearchResultAttribute;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class UpdateComUseCase
{

	@Test
	public void testUpdatingData()
	{

		//load search data
		ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();

		try {
			ComponentSearchWrapper search = objectMapper.readValue(new File("/test/spoon/com.json"), new TypeReference<ComponentSearchWrapper>()
			{
			});

			Set<String> alreadyUpdated = objectMapper.readValue(new File("/test/spoon/updated.json"), new TypeReference<HashSet<String>>()
			{
			});

			//find entries with lat/long
			Map<String, ComponentSearchView> componentMap = new HashMap<>();
			for (ComponentSearchView view : search.getData()) {
				boolean hasLat = false;
				for (SearchResultAttribute attribute : view.getAttributes()) {
					if (attribute.getTypeLabel().toLowerCase().contains("latitude")) {
						hasLat = true;
					}
				}
				if (hasLat) {
					componentMap.put(view.getComponentId(), view);
				}
			}
			System.out.println("To be updated: " + componentMap.keySet().size());

			System.out.println("Connecting to API...");
			ClientAPI clientAPI = new ClientAPI(objectMapper);
			clientAPI.connect("", "", "http://spoonsite.usurf.usu.edu/openstorefront"); //"https://spoonsite.com/openstorefront");  //Cert issue
			System.out.println("Connected");

			for (String componentId : componentMap.keySet()) {
				if (alreadyUpdated.contains(componentId) == false) {
					ComponentSearchView view = componentMap.get(componentId);
					System.out.println("Working on: " + view.getName());

					//get lat long
					String lat = null;
					String longt = null;

					for (SearchResultAttribute attribute : view.getAttributes()) {
						if (attribute.getTypeLabel().toLowerCase().contains("latitude")) {
							lat = attribute.getLabel().trim();
						}
						if (attribute.getTypeLabel().toLowerCase().contains("longitude")) {
							longt = attribute.getLabel().trim();
						}
					}

					if (lat != null && longt != null) {
						//add media
						ComponentMedia componentMedia = new ComponentMedia();
						componentMedia.setMediaTypeCode("IMG");
						componentMedia.setCaption("Location View");
						componentMedia.setComponentId(componentId);

						System.out.println("Adding media to: " + view.getName());
						//clientAPI.httpPost("/api/v1/resource/components/" + componentId + "/media", componentMedia, null);
						downloadImageAndPost(componentMedia, clientAPI, lat, longt);

						//add resource
						ComponentResource componentResource = new ComponentResource();
						componentResource.setResourceType("MAPLOC");
						componentResource.setLink("https://www.google.com/maps?q=" + lat + "," + longt);
						componentResource.setDescription("Map point to location");

						System.out.println("Adding Resource to: " + view.getName());
						clientAPI.httpPost("/api/v1/resource/components/" + componentId + "/resources", componentResource, null);
					}
					alreadyUpdated.add(componentId);
					objectMapper.writeValue(new File("/test/spoon/updated.json"), alreadyUpdated);
				}
			}

			try {

			} finally {
				clientAPI.disconnect();
			}

		} catch (IOException ex) {
			Logger.getLogger(UpdateComUseCase.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	private void downloadImageAndPost(ComponentMedia componentMedia, ClientAPI clientAPI, String lat, String longt)
	{
//		try {
//			String latLong = lat + "," + longt;
//
//			System.out.println("Getting image from goolge");
//			BasicCookieStore cookieStore = new BasicCookieStore();
//			CloseableHttpClient httpclient = HttpClients.custom()
//					.setDefaultCookieStore(cookieStore)
//					.build();
//
//			RequestConfig defaultRequestConfig = RequestConfig.custom()
//					.setCircularRedirectsAllowed(true).build();
//
//			RequestBuilder builder = RequestBuilder.get()
//					.setUri(new URI("https://maps.googleapis.com/maps/api/staticmap?center=" + latLong + "&zoom=17&maptype=hybrid&size=640x640&markers=color:yellow%7Clabel:S%7C" + latLong))
//					.setConfig(defaultRequestConfig);
//
//			HttpUriRequest request = builder.build();
//
//			try (CloseableHttpResponse httpResponse = httpclient.execute(request)) {
//				Files.copy(httpResponse.getEntity().getContent(), Paths.get("/test/spoon/map.png"), StandardCopyOption.REPLACE_EXISTING);
//			}
//
//			//upload
//			System.out.println("Uploading image to spoon...");
//			File file = new File("/test/spoon/map.png");
//			HttpPost post = new HttpPost("http://spoonsite.usurf.usu.edu/openstorefront/Media.action?UploadMedia");
//			FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY, "map.png");
//			StringBody componentIdBody = new StringBody(componentMedia.getComponentId(), ContentType.MULTIPART_FORM_DATA);
//			StringBody mediaTypeCodeBody = new StringBody(componentMedia.getMediaTypeCode(), ContentType.MULTIPART_FORM_DATA);
//			StringBody captionBody = new StringBody(componentMedia.getCaption(), ContentType.MULTIPART_FORM_DATA);
//
//			MultipartEntityBuilder multiBuilder = MultipartEntityBuilder.create();
//			multiBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//			multiBuilder.addPart("file", fileBody);
//			multiBuilder.addPart("componentMedia.componentId", componentIdBody);
//			multiBuilder.addPart("componentMedia.mediaTypeCode", mediaTypeCodeBody);
//			multiBuilder.addPart("componentMedia.caption", captionBody);
//			HttpEntity entity = multiBuilder.build();
//			//
//			post.setEntity(entity);
//			try (CloseableHttpResponse response = clientAPI.getHttpclient().execute(post)) {
//				post.releaseConnection();
//				System.out.println("Upload status: " + response.getStatusLine().getStatusCode());
//			}
//
//		} catch (IOException | URISyntaxException ex) {
//			throw new ConnectionException("Unable to Connect.", ex);
//		}

	}

}
