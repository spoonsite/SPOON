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
import edu.usu.sdl.openstorefront.core.entity.ComponentMetadata;
import edu.usu.sdl.openstorefront.core.filter.FilterEngine;
import edu.usu.sdl.openstorefront.core.view.ComponentMetadataView;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1/resource/componentmetadata")
@APIDescription("Metadata belonging to a component")
public class ComponentMetadataResource
		extends BaseResource
{

	@GET
	@APIDescription("Gets all active metadata")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentMetadataView.class)
	public Response getMetadata()
	{
		ComponentMetadata componentMetadata = new ComponentMetadata();
		componentMetadata.setActiveStatus(ComponentMetadata.ACTIVE_STATUS);

		List<ComponentMetadata> metadata = componentMetadata.findByExample();
		metadata = FilterEngine.filter(metadata, true);
		List<ComponentMetadataView> views = ComponentMetadataView.toViewList(metadata);

		views = views.stream().collect(Collectors.toList());

		GenericEntity<List<ComponentMetadataView>> entity = new GenericEntity<List<ComponentMetadataView>>(views)
		{
		};

		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Get a list of active metadata labels")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(LookupModel.class)
	@Path("/lookup")
	public Response getMetadataLabelLookupList()
	{
		ComponentMetadata componentMetadata = new ComponentMetadata();
		componentMetadata.setActiveStatus(ComponentMetadata.ACTIVE_STATUS);

		List<ComponentMetadata> metadata = componentMetadata.findByExample();
		metadata = FilterEngine.filter(metadata, true);
		List<LookupModel> lookupModels = new ArrayList<>();

		Set<String> labels = new HashSet<>();
		for (ComponentMetadata md : metadata) {
			labels.add(md.getLabel());
		}

		for (String label : labels) {
			LookupModel lookupModel = new LookupModel();
			lookupModel.setCode(label);
			lookupModel.setDescription(label);
			lookupModels.add(lookupModel);
		}

		GenericEntity<List<LookupModel>> entity = new GenericEntity<List<LookupModel>>(lookupModels)
		{
		};
		return sendSingleEntityResponse(entity);

	}

	@GET
	@APIDescription("Get a list of active metadata values associated with a label")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(LookupModel.class)
	@Path("/lookup/values")
	public Response getMetadataValuesLookupList(@RequiredParam @QueryParam("label") String label)
	{

		TextSanitizer sanitizer = new TextSanitizer();
		label = (String) sanitizer.santize(label);

		ComponentMetadata componentMetadata = new ComponentMetadata();
		componentMetadata.setActiveStatus(ComponentMetadata.ACTIVE_STATUS);
		componentMetadata.setLabel(label);

		List<ComponentMetadata> metadata = componentMetadata.findByExample();
		metadata = FilterEngine.filter(metadata, true);

		Set<String> values = new HashSet<>();
		for (ComponentMetadata md : metadata) {
			values.add(md.getValue());
		}

		List<LookupModel> lookupModels = new ArrayList<>();
		for (String value : values) {
			LookupModel lookupModel = new LookupModel();
			lookupModel.setCode(value);
			lookupModel.setDescription(value);
			lookupModels.add(lookupModel);
		}

		GenericEntity<List<LookupModel>> entity = new GenericEntity<List<LookupModel>>(lookupModels)
		{
		};
		return sendSingleEntityResponse(entity);

	}

}
