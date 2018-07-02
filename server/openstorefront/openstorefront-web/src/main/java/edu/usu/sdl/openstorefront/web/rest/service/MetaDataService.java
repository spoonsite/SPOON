/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */
package edu.usu.sdl.openstorefront.web.rest.service;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.doc.EntityProcessor;
import edu.usu.sdl.openstorefront.doc.model.EntityDocModel;
import edu.usu.sdl.openstorefront.web.rest.resource.BaseResource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.sourceforge.stripes.util.ResolverUtil;

/**
 *
 * @author dshurtleff
 */
@Path("v1/service/metadata")
@APIDescription("Retrieve information about the meta model of the enties")
public class MetaDataService
		extends BaseResource
{

	@GET
	@APIDescription("Gets all enities")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(EntityDocModel.class)
	public List<EntityDocModel> getEntities()
	{
		return EntityProcessor.processEntites(availableEntities());
	}

	@GET
	@APIDescription("Gets all enities")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(EntityDocModel.class)
	@Path("/{entity}")
	public Response getEntity(
			@PathParam("entity") String entityName
	)
	{
		List<Class> classes = availableEntities();
		Map<String, Class> classMap = new HashMap<>();
		for (Class clazz : classes) {
			classMap.put(clazz.getSimpleName(), clazz);
		}

		EntityDocModel entityDocModel = null;
		if (classMap.containsKey(entityName)) {
			List<Class> processClass = new ArrayList<>();
			processClass.add(classMap.get(entityName));
			entityDocModel = EntityProcessor.processEntites(processClass).get(0);
		}

		return sendSingleEntityResponse(entityDocModel);
	}

	@SuppressWarnings("unchecked")
	private List<Class> availableEntities()
	{
		List<Class> classes = new ArrayList<>();
		ResolverUtil resolverUtil = new ResolverUtil();
		resolverUtil.find(new ResolverUtil.IsA(Serializable.class), "edu.usu.sdl.openstorefront.core.entity");
		classes.addAll(resolverUtil.getClasses());
		return classes;
	}

}
