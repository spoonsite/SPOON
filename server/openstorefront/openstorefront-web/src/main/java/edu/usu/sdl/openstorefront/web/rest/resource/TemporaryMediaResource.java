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

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.TemporaryMedia;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import edu.usu.sdl.openstorefront.core.view.TemporaryMediaView;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1/resource/temporarymedia")
@APIDescription("Temporary Media is media that a client wishes to store to later be assigned to an entry")
public class TemporaryMediaResource
		extends BaseResource
{

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_TEMPMEDIA_MANAGEMENT_READ)
	@APIDescription("Gets all temporary media records.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(TemporaryMediaView.class)
	public Response getTemporaryMedia(@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		TemporaryMedia temporaryMediaExample = new TemporaryMedia();
		temporaryMediaExample.setActiveStatus(filterQueryParams.getStatus());
		List<TemporaryMedia> temporaryMedia = service.getPersistenceService().queryByExample(temporaryMediaExample);
		temporaryMedia = filterQueryParams.filter(temporaryMedia);
		List<TemporaryMediaView> temporaryMediaViews = TemporaryMediaView.toViewList(temporaryMedia);

		GenericEntity<List<TemporaryMediaView>> entity = new GenericEntity<List<TemporaryMediaView>>(temporaryMediaViews)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_TEMPMEDIA_MANAGEMENT_READ)
	@APIDescription("Gets all temporary media records for a lookup list")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(LookupModel.class)
	@Path("/lookup")
	public Response getTemporaryMediaLookup()
	{
		TemporaryMedia temporaryMediaExample = new TemporaryMedia();
		temporaryMediaExample.setActiveStatus(TemporaryMedia.ACTIVE_STATUS);
		List<TemporaryMedia> temporaryMedia = service.getPersistenceService().queryByExample(temporaryMediaExample);
		List<TemporaryMediaView> temporaryMediaViews = TemporaryMediaView.toViewList(temporaryMedia);

		List<LookupModel> lookups = new ArrayList<>();
		temporaryMediaViews.forEach(media -> {
			LookupModel lookupModel = new LookupModel();
			lookupModel.setCode(media.getMediaLink());
			lookupModel.setDescription(media.getName());
		});

		GenericEntity<List<LookupModel>> entity = new GenericEntity<List<LookupModel>>(lookups)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_TEMPMEDIA_MANAGEMENT_READ)
	@APIDescription("Gets a temporary media record. See Media.action?TemporaryMedia&name={name} to get the actual resource")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(TemporaryMediaView.class)
	@Path("/{name}")
	public Response getTemporaryMedia(
			@PathParam("name") String name)
	{
		TemporaryMedia temporaryMediaExample = new TemporaryMedia();
		temporaryMediaExample.setName(name);
		TemporaryMedia temporaryMedia = service.getPersistenceService().queryOneByExample(temporaryMediaExample);
		return sendSingleEntityResponse(temporaryMedia);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_TEMPMEDIA_MANAGEMENT_DELETE)
	@APIDescription("Deletes a temporary media record.")
	@Path("/{id}")
	public void deleteTemporaryMedia(
			@PathParam("id") String id)
	{
		service.getSystemService().removeTemporaryMedia(id);
	}

}
