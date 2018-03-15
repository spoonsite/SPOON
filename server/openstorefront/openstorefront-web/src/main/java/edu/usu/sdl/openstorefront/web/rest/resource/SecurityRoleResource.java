/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.SecurityRole;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.UserRole;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/securityroles")
@APIDescription("Handles security roles")
public class SecurityRoleResource
		extends BaseResource
{

	private static final Logger LOG = Logger.getLogger(SecurityRoleResource.class.getName());

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_ROLE_MANAGEMENT)
	@APIDescription("Gets security roles.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(SecurityRole.class)
	public List<SecurityRole> getRoles()
	{
		SecurityRole securityRole = new SecurityRole();
		List<SecurityRole> securityRoles = securityRole.findByExample();
		return securityRoles;
	}

	@GET
	@APIDescription("Gets security roles for pick list.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(LookupModel.class)
	@Path("/lookup")
	public List<LookupModel> getRolesLookup()
	{
		List<LookupModel> roles = new ArrayList<>();

		SecurityRole securityRole = new SecurityRole();
		List<SecurityRole> securityRoles = securityRole.findByExample();
		securityRoles = securityRoles.stream()
				.filter(r -> !SecurityRole.DEFAULT_GROUP.equals(r.getRoleName()))
				.collect(Collectors.toList());

		for (SecurityRole role : securityRoles) {
			LookupModel lookup = new LookupModel();
			lookup.setCode(role.getRoleName());
			lookup.setDescription(role.getRoleName());
			roles.add(lookup);
		}
		roles.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, LookupModel.DESCRIPTION_FIELD));

		return roles;
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_ROLE_MANAGEMENT)
	@APIDescription("Gets a security role.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(SecurityRole.class)
	@Path("/{rolename}")
	public Response getRole(
			@PathParam("rolename") String rolename
	)
	{
		SecurityRole securityRole = new SecurityRole();
		securityRole.setRoleName(rolename);
		securityRole = securityRole.find();
		return sendSingleEntityResponse(securityRole);
	}

	@POST
	@APIDescription("Create a new security role.")
	@RequireSecurity(SecurityPermission.ADMIN_ROLE_MANAGEMENT)
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(UserRole.class)
	public Response createRole(
			SecurityRole securityRole
	) throws UnsupportedEncodingException
	{
		return handleSaveRole(securityRole, true);
	}

	@PUT
	@APIDescription("Update a security role.")
	@RequireSecurity(SecurityPermission.ADMIN_ROLE_MANAGEMENT)
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(UserRole.class)
	@Path("/{rolename}")
	public Response createRole(
			@PathParam("rolename") String rolename,
			SecurityRole securityRole
	) throws UnsupportedEncodingException
	{
		SecurityRole existing = new SecurityRole();
		existing.setRoleName(rolename);
		existing = existing.find();
		if (existing != null) {
			securityRole.setRoleName(rolename);
			return handleSaveRole(securityRole, false);
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	private Response handleSaveRole(SecurityRole securityRole, boolean post) throws UnsupportedEncodingException
	{
		ValidationResult validationResult = securityRole.validate();
		if (post) {
			validationResult.merge(service.getSecurityService().validateSecurityRoleName(securityRole.getRoleName()));
		}

		if (validationResult.valid()) {
			SecurityRole savedRole = service.getSecurityService().saveSecurityRole(securityRole);

			if (post) {
				return Response.created(URI.create("v1/resource/securityroles/" + URLEncoder.encode(savedRole.getRoleName(), StandardCharsets.UTF_8.name()))).entity(savedRole).build();
			} else {
				return Response.ok(savedRole).build();
			}
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_ROLE_MANAGEMENT)
	@APIDescription("Deletes a security role. Optional moves the users to a new role.")
	@Path("/{rolename}")
	public Response deleteRole(
			@PathParam("rolename") String rolename,
			@QueryParam("movetorole") String moveToRole
	)
	{
		SecurityRole securityRole = new SecurityRole();
		securityRole.setRoleName(rolename);
		securityRole = securityRole.find();
		if (securityRole != null) {
			service.getSecurityService().deleteSecurityRole(rolename, moveToRole);
			return Response.noContent().build();
		}
		return sendSingleEntityResponse(securityRole);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_ROLE_MANAGEMENT)
	@APIDescription("Get users associated with role. Keep in mind there maybe phantom records based on security realm used.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserRole.class)
	@Path("/{rolename}/users")
	public List<UserRole> getUserForRole(
			@PathParam("rolename") String rolename
	)
	{
		UserRole userRole = new UserRole();
		userRole.setRole(rolename);
		userRole.setActiveStatus(UserRole.ACTIVE_STATUS);
		List<UserRole> userRoles = userRole.findByExample();
		return userRoles;
	}

	@POST
	@APIDescription("Adds a user to a security role.")
	@RequireSecurity(SecurityPermission.ADMIN_ROLE_MANAGEMENT)
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(UserRole.class)
	@Path("/{rolename}/users")
	public Response addUserToRole(
			@PathParam("rolename") String rolename,
			UserRole userRole
	)
	{
		SecurityRole securityRole = new SecurityRole();
		securityRole.setRoleName(rolename);
		securityRole = securityRole.find();
		if (securityRole != null) {
			UserProfile userProfile = new UserProfile();
			userProfile.setUsername(userRole.getUsername());
			userProfile = userProfile.find();
			if (userProfile != null) {
				userRole.setRole(rolename);
				service.getSecurityService().addUserToRole(userRole.getUsername(), rolename);
				return Response.ok(userRole).build();
			} else {
				LOG.log(Level.FINE, MessageFormat.format("User was not found. User: {0}", userRole.getUsername()));
				return Response.status(Response.Status.NOT_FOUND).build();
			}
		}
		LOG.log(Level.FINE, MessageFormat.format("Role was not found. Role: {0}", rolename));
		return sendSingleEntityResponse(securityRole);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_ROLE_MANAGEMENT)
	@APIDescription("Removes an user from a security role.")
	@Path("/{rolename}/users/{username}")
	public Response removeUserFromRole(
			@PathParam("rolename") String rolename,
			@PathParam("username") String username
	)
	{
		SecurityRole securityRole = new SecurityRole();
		securityRole.setRoleName(rolename);
		securityRole = securityRole.find();
		if (securityRole != null) {
			UserProfile userProfile = new UserProfile();
			userProfile.setUsername(username);
			userProfile = userProfile.find();
			if (userProfile != null) {
				service.getSecurityService().removeRoleFromUser(username, rolename);
			} else {
				LOG.log(Level.FINE, MessageFormat.format("User was not found. User: {0}", username));
			}
		} else {
			LOG.log(Level.FINE, MessageFormat.format("Role was not found. Role: {0}", rolename));
		}
		return Response.noContent().build();
	}

}
