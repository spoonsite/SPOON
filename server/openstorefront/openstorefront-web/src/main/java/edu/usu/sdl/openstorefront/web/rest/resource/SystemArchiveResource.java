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

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.IODirectionType;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.SystemArchive;
import edu.usu.sdl.openstorefront.core.entity.SystemArchiveError;
import edu.usu.sdl.openstorefront.core.view.SystemArchiveView;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/systemarchives")
@APIDescription("Handles security roles")
public class SystemArchiveResource
		extends BaseResource
{

	private static final int MAX_FILENAME = 80;

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_SYSTEM_MANAGEMENT)
	@APIDescription("Gets system archives.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(SystemArchiveView.class)
	public List<SystemArchiveView> getArchives()
	{
		SystemArchive archiveExample = new SystemArchive();
		archiveExample.setActiveStatus(SystemArchive.ACTIVE_STATUS);

		List<SystemArchive> archives = archiveExample.findByExample();
		List<SystemArchiveView> views = SystemArchiveView.toView(archives);
		return views;
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_SYSTEM_MANAGEMENT)
	@APIDescription("Gets system archives")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(SystemArchive.class)
	@Path("/{archiveId}")
	public Response getArchive(
			@PathParam("archiveId") String archiveId
	)
	{
		SystemArchive systemArchive = new SystemArchive();
		systemArchive.setArchiveId(archiveId);
		return sendSingleEntityResponse(systemArchive.find());
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_SYSTEM_MANAGEMENT)
	@APIDescription("Gets system archives errors.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(SystemArchiveError.class)
	@Path("/{archiveId}/errors")
	public List<SystemArchiveError> getArchiveErrors(
			@PathParam("archiveId") String archiveId
	)
	{
		SystemArchiveError systemArchiveError = new SystemArchiveError();
		systemArchiveError.setActiveStatus(SystemArchive.ACTIVE_STATUS);
		systemArchiveError.setArchiveId(archiveId);
		return systemArchiveError.findByExample();
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_SYSTEM_MANAGEMENT)
	@APIDescription("Downloads the archive")
	@Produces({MediaType.WILDCARD})
	@Path("/{archiveId}/download")
	public Response getArchiveDownload(
			@PathParam("archiveId") String archiveId
	)
	{
		SystemArchive systemArchive = new SystemArchive();
		systemArchive.setArchiveId(archiveId);
		systemArchive = systemArchive.find();
		if (systemArchive != null) {
			java.nio.file.Path path = systemArchive.pathToArchive();
			if (path != null) {
				String archiveName = path.toString();
				Response.ResponseBuilder response = Response.ok((StreamingOutput) (OutputStream output) -> {
					Files.copy(Paths.get(archiveName), output);
				});

				String filename = StringUtils.left(systemArchive.getName().replace(" ", "_"), MAX_FILENAME);

				response.header("Content-Type", "application/zip");
				response.header("Content-Disposition", "attachment; filename=\"" + filename + ".zip\"");
				return response.build();
			} else {
				return sendSingleEntityResponse(null);
			}
		}
		return sendSingleEntityResponse(null);
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_SYSTEM_MANAGEMENT)
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@APIDescription("Create a new Archive")
	@DataType(SystemArchive.class)
	public Response createArchive(
			SystemArchive archive
	)
	{
		archive.setIoDirectionType(IODirectionType.EXPORT);
		ValidationResult validationResult = archive.validate();
		if (validationResult.valid()) {
			String archiveId = service.getSystemArchiveService().queueArchiveRequest(archive);
			return Response.created(URI.create("v1/resource/systemarchives/" + archiveId)).entity(archive).build();
		} else {
			return sendSingleEntityResponse(validationResult.toRestError());
		}
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_SYSTEM_MANAGEMENT)
	@APIDescription("Delete a system archive")
	@Path("/{archiveId}")
	public Response deleteArchive(
			@PathParam("archiveId") String archiveId
	)
	{
		SystemArchive systemArchive = new SystemArchive();
		systemArchive.setArchiveId(archiveId);
		systemArchive = systemArchive.find();
		if (systemArchive != null) {
			service.getSystemArchiveService().deleteArchive(archiveId);
		}
		return Response.status(Response.Status.NO_CONTENT).build();
	}

}
