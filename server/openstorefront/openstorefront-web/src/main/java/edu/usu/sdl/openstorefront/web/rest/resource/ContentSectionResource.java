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
package edu.usu.sdl.openstorefront.web.rest.resource;

/**
 *
 * @author dshurtleff
 */
//@APIDescription("Provides access to content sections.")
//@Path("v1/resource/contentsections")
public class ContentSectionResource

{

	//get sections (option for entity, entityId)
	
	
	//Hold on this for now ....keep in mind this may allowing pull sections
	//from unapproved entry and/or unpublished  evaluations.
	//We need to think about the security and the need for this.
	
	
//	@GET
//	@RequireSecurity
//	@Produces({MediaType.APPLICATION_JSON})
//	@DataType(ContentSection.class)
//	@APIDescription("Gets top-level sections")
//	public Response getSections(
//			@QueryParam("entity") String entity,
//			@QueryParam("entityId") String entityId,
//			@QueryParam("workflowStatus") String workflowStatus
//	)
//	{
//		ContentSection contentSection = new ContentSection();
//		contentSection.setEntity(StringProcessor.nullIfBlank(entity));
//		contentSection.setEntityId(StringProcessor.nullIfBlank(entityId));
//		contentSection.setWorkflowStatus(StringProcessor.nullIfBlank(workflowStatus));
//		contentSection.setActiveStatus(ContentSection.ACTIVE_STATUS);
//
//		List<ContentSection> sections = contentSection.findByExample();
//
//		GenericEntity<List<ContentSection>> sectionEntity = new GenericEntity<List<ContentSection>>(sections)
//		{
//		};
//		return sendSingleEntityResponse(sectionEntity);
//	}
	
	//get attributes 
	
	//add attributes
	
	//delete attributes
	
	
	//get a section
	//create content section
	//update content section
	//delete

}
