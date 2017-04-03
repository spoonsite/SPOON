/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.rest.resource;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.ChangeLog;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.view.ChangeLogView;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/changelogs")
@APIDescription("Change log endpoints for see history on entities that support it.")
public class ChangeLogResource
		extends BaseResource
{

	@GET
	@APIDescription("Gets changes log for an entity (also pulls children changes as an option)")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ChangeLogView.class)
	@Path("/{entity}/{entityId}")
	public Response getChangeLogForEntity(
			@PathParam("entity") String entity,
			@PathParam("entityId") String entityId,
			@QueryParam("includeChildren") boolean includeChildren
	)
	{
		ChangeLog changeLogExample = new ChangeLog();
		changeLogExample.setActiveStatus(ChangeLog.ACTIVE_STATUS);
		changeLogExample.setEntity(entity);
		changeLogExample.setEntityId(entityId);

		List<ChangeLog> changeLogs = changeLogExample.findByExample();

		if (includeChildren) {
			changeLogExample = new ChangeLog();
			changeLogExample.setActiveStatus(ChangeLog.ACTIVE_STATUS);
			changeLogExample.setParentEntity(entity);
			changeLogExample.setParentEntityId(entityId);

			List<ChangeLog> childrenChanges = changeLogExample.findByExample();
			changeLogs.addAll(childrenChanges);
		}

		List<ChangeLogView> views = ChangeLogView.toView(changeLogs);
		views.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_DESCENDING, StandardEntity.FIELD_CREATE_DTS));

		GenericEntity<List<ChangeLogView>> returnEntity = new GenericEntity<List<ChangeLogView>>(views)
		{
		};
		return sendSingleEntityResponse(returnEntity);
	}

}
