/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import edu.usu.sdl.openstorefront.core.entity.Plugin;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.view.PluginView;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import java.io.OutputStream;
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

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/plugins")
@APIDescription("This provides plugin control access.  "
		+ "To upload a new plugin: form POST to /Upload.action?UploadPlugin with uploadFile as the file field. "
		+ "Requires Admin role")
public class PluginResource
		extends BaseResource
{

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_SYSTEM_MANAGEMENT)
	@APIDescription("Gets plugin records with runtime info.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(PluginView.class)
	public List<PluginView> getAllPlugins()
	{
		List<PluginView> views = service.getPluginService().findAllPlugins();
		return views;
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_SYSTEM_MANAGEMENT)
	@APIDescription("Get a plugin record with no runtime info.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Plugin.class)
	@Path("/{id}")
	public Response getPlugin(
			@PathParam("id") String pluginId
	)
	{
		Plugin plugin = new Plugin();
		plugin.setPluginId(pluginId);
		plugin = plugin.find();
		return sendSingleEntityResponse(plugin);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_SYSTEM_MANAGEMENT)
	@APIDescription("Downloads a plugin")
	@Produces({MediaType.WILDCARD})
	@DataType(Plugin.class)
	@Path("/{id}/download")
	public Response downloadPlugin(
			@PathParam("id") String pluginId
	)
	{

		Plugin plugin = new Plugin();
		plugin.setPluginId(pluginId);
		plugin = plugin.find();
		if (plugin != null) {

			java.nio.file.Path path = Paths.get(plugin.fullPath());

			if (path.toFile().exists()) {
				Response.ResponseBuilder response = Response.ok((StreamingOutput) (OutputStream output) -> {
					Files.copy(path, output);
				});
				response.header("Content-Type", "application/" + plugin.getPluginType());
				response.header("Content-Disposition", "attachment; filename=\"" + plugin.getOriginalFilename() + "\"");
				return response.build();
			}

		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_SYSTEM_MANAGEMENT)
	@APIDescription("Starts a plugin")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/start")
	public Response startPlugin(
			@PathParam("id") String pluginId
	)
	{
		Plugin plugin = new Plugin();
		plugin.setPluginId(pluginId);
		plugin = plugin.find();
		if (plugin != null) {
			service.getPluginService().activatePlugin(pluginId);
			return Response.ok(plugin.find()).build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_SYSTEM_MANAGEMENT)
	@APIDescription("Stops a plugin")
	@Path("/{id}/stop")
	public Response stopPlugin(
			@PathParam("id") String pluginId
	)
	{
		Plugin plugin = new Plugin();
		plugin.setPluginId(pluginId);
		plugin = plugin.find();
		if (plugin != null) {
			service.getPluginService().inactivatePlugin(pluginId);
			return Response.ok(plugin.find()).build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_SYSTEM_MANAGEMENT)
	@APIDescription("Uninstall a plugin")
	@Path("/{id}")
	public Response uninstallPlugin(
			@PathParam("id") String pluginId
	)
	{
		Plugin plugin = new Plugin();
		plugin.setPluginId(pluginId);
		plugin = plugin.find();
		if (plugin != null) {
			service.getPluginService().uninstallPlugin(pluginId);
			return Response.ok().build();
		}
		return Response.status(Response.Status.NO_CONTENT).build();
	}
}
