/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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

package edu.usu.sdl.openstorefront.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.openstorefront.model.jpa.BaseEntity;
import edu.usu.sdl.openstorefront.util.ServiceUtil;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentAttribute;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentContact;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentDetail;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentMedia;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentResource;
import edu.usu.sdl.openstorefront.web.tool.OldAsset;
import edu.usu.sdl.openstorefront.web.tool.OldDataWrapper;
import java.io.File;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class DataConvertTest
{
	@Test
	public void testConvert() throws JsonProcessingException, IOException
	{
		
		ObjectMapper objectMapper = ServiceUtil.defaultObjectMapper();		
		OldDataWrapper oldDataWrapper = objectMapper.readValue(new File("C:\\development\\storefront\\source\\old\\old_data\\new-asset-data-all.json"), new TypeReference<OldDataWrapper>() {});
		
		List<OldAsset> assets = oldDataWrapper.getData();
		 
		List<ComponentDetail> newAssetMin = processData(assets, false);
		List<ComponentDetail> newAssetComplete =  processData(assets, true);
		
		System.out.println(objectMapper.writeValueAsString(newAssetMin));		
		objectMapper.writeValue(new File("c:/development/storefront/data/components-min.json"), newAssetMin);
		objectMapper.writeValue(new File("c:/development/storefront/data/components-full.json"), newAssetComplete);
	}
	
	private List<ComponentDetail> processData(List<OldAsset> assets, boolean generateData)
	{
		List<String> peopleNames = Arrays.asList("Cathy TEST", "Jack TEST", "Colby TEST", "Abby TEST", "Dawson TEST");
		
		Set<String> metaTypeToSkip = new HashSet<>();
		metaTypeToSkip.add("Government Point of Contact Email Address");
		metaTypeToSkip.add("Government Point of Contact Name");
		metaTypeToSkip.add("Government Point of Contact Phone");
		metaTypeToSkip.add("Code Location URL");
		metaTypeToSkip.add("Product Homepage");
		metaTypeToSkip.add("DI2E Framework Evaluation Report URL");
		
		SimpleDateFormat sdfDate = new SimpleDateFormat("MM/dd/yyyy");
		
		List<ComponentDetail> newAssets = new ArrayList<>();		
		assets.forEach(oldAsset -> {
			
			ComponentDetail componentDetail = new ComponentDetail();
			//defaults
			componentDetail.setActiveStatus(BaseEntity.ACTIVE_STATUS);
			
			//map form old			
			componentDetail.setComponentId(oldAsset.getId());
			componentDetail.setGuid(oldAsset.getUuid());
			componentDetail.setUpdateDts(oldAsset.getEditedDate());
			componentDetail.setDescription(oldAsset.getDescription());
			componentDetail.setApprovedDate(oldAsset.getApprovalDate());
			componentDetail.setCreateDts(oldAsset.getCreateDate());
			componentDetail.setVersion(oldAsset.getVersionName());
			
			if ("Approved".equalsIgnoreCase(oldAsset.getApprovalStatus()))
			{
				componentDetail.setApprovalState("A");
			}
			else
			{
				componentDetail.setApprovalState("P");
			}
			
			componentDetail.setCreateUser(oldAsset.getCreatedBy().getUsername());
			componentDetail.setUpdateUser(oldAsset.getEditedBy().getUsername());
			componentDetail.setOrganization(oldAsset.getOrganization());
			componentDetail.setName(oldAsset.getTitle());
			if (StringUtils.isNotBlank(oldAsset.getReleaseDate()))
			{
				componentDetail.setReleaseDate(sdfDate.parse(oldAsset.getReleaseDate(), new ParsePosition(0)));			
			}
			componentDetail.setLastActivityDate(oldAsset.getLastActivity().getActivitiyDate());
						
			oldAsset.getTechPocs().forEach(poc ->{
				//Need to generate 				
				Collections.shuffle(peopleNames);
				ComponentContact contact = new ComponentContact();
				contact.setPostionDescription("Technical POC");
				contact.setName(peopleNames.get(0));
				contact.setEmail("sample_email@test.com");
				contact.setPhone("555-555-5555");
				componentDetail.getContacts().add(contact);
			});
			
			//Government Contact
			oldAsset.getCustomFields().forEach(field ->{
				if ("Government Point of Contact Name".equalsIgnoreCase(field.getName()))
				{
					Collections.shuffle(peopleNames);
					ComponentContact contact = new ComponentContact();
					contact.setPostionDescription("Government POC");
					contact.setName(peopleNames.get(0));
					contact.setEmail("sample_email@test.com");
					contact.setPhone("555-555-5555");
					componentDetail.getContacts().add(contact);							
				}
			});
			
			
			//media
			oldAsset.getScreenShots().forEach(screen -> {				
				componentDetail.getComponentMedia().add(createMediaFromUrl(screen.getLargeImageUrl()));			
			});
			if (StringUtils.isNotBlank(oldAsset.getImageLargeUrl()))
			{
				componentDetail.getComponentMedia().add(createMediaFromUrl(oldAsset.getImageLargeUrl()));
			}
		
			//resources
			oldAsset.getDocUrls().forEach(doc -> {
				ComponentResource componentResource = new ComponentResource();
				componentResource.setName(doc.getName());
				componentResource.setLink(doc.getUrl());
				componentResource.setType("Document");
				componentDetail.getResources().add(componentResource);
			});
			
			oldAsset.getCustomFields().forEach(field ->{
				if ("Code Location URL".equals(field.getName())){
					ComponentResource componentResource = new ComponentResource();
					componentResource.setName(field.getName());
					componentResource.setLink(field.getValue());
					componentResource.setType("Code");
					componentDetail.getResources().add(componentResource);					
				} else 	if ("Product Homepage".equals(field.getName())){
					ComponentResource componentResource = new ComponentResource();
					componentResource.setName(field.getName());
					componentResource.setLink(field.getValue());
					componentResource.setType("Homepage");
					componentDetail.getResources().add(componentResource);						
				} else 	if ("DI2E Framework Evaluation Report URL".equals(field.getName())){
					ComponentResource componentResource = new ComponentResource();
					componentResource.setName(field.getName());
					componentResource.setLink(field.getValue());
					componentResource.setType("DI2E Framework Evaluation Report URL");
					componentDetail.getResources().add(componentResource);						
				}
			});
			
			//metadata/attributes
			ComponentAttribute attribute = new ComponentAttribute();
			attribute.setImportant(true);
			attribute.setTypeDescription("Type");
			attribute.setCodeDescription(oldAsset.getTypes().getTitle());
			componentDetail.getAttributes().add(attribute);
			
			attribute = new ComponentAttribute();
			attribute.setImportant(true);
			attribute.setTypeDescription("Evaluation Level");
			attribute.setCodeDescription(oldAsset.getState().getTitle());
			componentDetail.getAttributes().add(attribute);
			
			String level[] = oldAsset.getState().getTitle().split("-");
			componentDetail.getEvaluation().setCurrentLevelCode(level[0].trim().toUpperCase());			
			
			oldAsset.getCategories().forEach(category -> {
				ComponentAttribute catAttribute = new ComponentAttribute();				
				catAttribute.setTypeDescription("Category");
				catAttribute.setCodeDescription(category.getTitle());
				componentDetail.getAttributes().add(catAttribute);
			});
						
			
			oldAsset.getCustomFields().forEach(field ->{
				if (StringUtils.isNotBlank(field.getValue()) &&
					!metaTypeToSkip.contains(field.getName()))
				{
					ComponentAttribute metaAttribute = new ComponentAttribute();					
					metaAttribute.setTypeDescription(field.getName());
					metaAttribute.setCodeDescription(field.getValue());
					componentDetail.getAttributes().add(metaAttribute);		
				}			
			});
			
			
			if (generateData)	
			{
				//filling some details at random
			}				

			newAssets.add(componentDetail);
		});	
		return newAssets;
	}
	
	private ComponentMedia createMediaFromUrl(String url)
	{
		ComponentMedia media = new ComponentMedia();
		String baseImagePath = "images/oldsite/";
		String resource = ServiceUtil.getResourceNameFromUrl(url);
		media.setLink(baseImagePath + resource);								
		media.setContentType("image/" +  resource.substring(resource.lastIndexOf(".") + 1, resource.length()));							
		return media;
	}
	
}
