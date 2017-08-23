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

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.SecurityRole;
import edu.usu.sdl.openstorefront.core.entity.UserRole;
import edu.usu.sdl.openstorefront.core.entity.UserSecurity;
import edu.usu.sdl.openstorefront.core.view.UserCredential;
import edu.usu.sdl.openstorefront.core.view.UserFilterParams;
import edu.usu.sdl.openstorefront.core.view.UserSecurityWrapper;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.validation.RuleResult;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/users")
@APIDescription("Handles security users")
public class UserResource
		extends BaseResource
{

	private static final Logger LOG = Logger.getLogger(UserResource.class.getName());

	@GET
	@APIDescription("Get a list of user for the built in security")
	@RequireSecurity(SecurityPermission.ADMIN_USER_MANAGEMENT)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserSecurityWrapper.class)
	public Response getUsers(
			@BeanParam UserFilterParams filterQueryParams,
			@QueryParam("query") String query
	)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		if (StringUtils.isNotBlank(query)) {
			filterQueryParams.setSearchField(UserSecurity.FIELD_USERNAME);
			filterQueryParams.setSearchValue(query);
		}

		UserSecurityWrapper userSecurityWrapper = service.getSecurityService().getUserViews(filterQueryParams);
		return sendSingleEntityResponse(userSecurityWrapper);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_USER_MANAGEMENT)
	@APIDescription("Gets security roles that user is in (Not default group is not included).")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(SecurityRole.class)
	@Path("/{username}/roles")
	public Response getUserRoles(
			@PathParam("username") String username
	)
	{
		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUsername(username.toLowerCase());
		userSecurity = userSecurity.find();
		if (userSecurity != null) {
			UserRole userRole = new UserRole();
			userRole.setActiveStatus(UserRole.ACTIVE_STATUS);
			userRole.setUsername(username.toLowerCase());

			List<UserRole> userRoles = userRole.findByExample();

			List<SecurityRole> securityRoles = new ArrayList<>();
			for (UserRole role : userRoles) {
				SecurityRole securityRole = new SecurityRole();
				securityRole.setRoleName(role.getRole());
				securityRole = securityRole.find();
				if (securityRole != null) {
					securityRoles.add(securityRole);
				}
			}

			GenericEntity<List<SecurityRole>> entity = new GenericEntity<List<SecurityRole>>(securityRoles)
			{
			};
			return sendSingleEntityResponse(entity);
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@PUT
	@APIDescription("Disable a user and prevents login")
	@RequireSecurity(SecurityPermission.ADMIN_USER_MANAGEMENT)
	@Path("/{username}/disable")
	public Response disableUser(
			@PathParam("username") String username
	)
	{
		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUsername(username.toLowerCase());
		userSecurity = userSecurity.find();
		if (userSecurity != null) {
			service.getSecurityService().disableUser(username.toLowerCase());
			return Response.ok().build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_USER_MANAGEMENT)
	@APIDescription("Approves user")
	@Path("/{username}/approve")
	public Response approveRegistration(
			@PathParam("username") String username
	)
	{
		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUsername(username.toLowerCase());
		userSecurity = userSecurity.find();

		if (userSecurity != null) {
			service.getSecurityService().approveRegistration(username.toLowerCase());
			return Response.ok().build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@PUT
	@APIDescription("Unlocks a user")
	@RequireSecurity(SecurityPermission.ADMIN_USER_MANAGEMENT)
	@Path("/{username}/unlock")
	public Response unlockUser(
			@PathParam("username") String username
	)
	{
		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUsername(username.toLowerCase());
		userSecurity = userSecurity.find();
		if (userSecurity != null) {
			service.getSecurityService().unlockUser(username.toLowerCase());
			return Response.ok().build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@PUT
	@APIDescription("Resets the failed login attempts")
	@RequireSecurity(SecurityPermission.ADMIN_USER_MANAGEMENT)
	@Path("/{username}/resetfailedlogins")
	public Response resetfailedlogins(
			@PathParam("username") String username
	)
	{
		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUsername(username.toLowerCase());
		userSecurity = userSecurity.find();
		if (userSecurity != null) {
			service.getSecurityService().resetFailedAttempts(username);
			return Response.ok().build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@PUT
	@APIDescription("Reset a user password (Admin Reset)")
	@RequireSecurity(SecurityPermission.ADMIN_USER_MANAGEMENT)
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{username}/resetpassword")
	public Response resetUserPassword(
			@PathParam("username") String username,
			UserCredential userCredential
	)
	{
		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUsername(username.toLowerCase());
		userSecurity = userSecurity.find();
		if (userSecurity != null) {

			if (SecurityUtil.getCurrentUserName().equals(username.toLowerCase())
					|| SecurityUtil.hasPermission(SecurityPermission.ADMIN_USER_MANAGEMENT)) {

				service.getSecurityService().adminResetPassword(username.toLowerCase(), userCredential.getPassword().toCharArray());
				return Response.ok().build();
			} else {
				return Response.status(Response.Status.FORBIDDEN).build();
			}
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@PUT
	@APIDescription("Reset the current user's password (Requires Existing password)")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/currentuser/resetpassword")
	public Response resetCurrentUserPassword(
			UserCredential userCredential
	)
	{
		ValidationModel validationModel = new ValidationModel(userCredential);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (userCredential.getExistingPassword() == null) {
			userCredential.setExistingPassword("");
		}
		validationResult.merge(service.getSecurityService().validatePassword(userCredential.getPassword().toCharArray()));

		if (validationResult.valid()) {

			UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken();
			usernamePasswordToken.setUsername(SecurityUtil.getCurrentUserName());
			usernamePasswordToken.setPassword(userCredential.getExistingPassword().toCharArray());

			try {
				SecurityUtils.getSecurityManager().authenticate(usernamePasswordToken);

				service.getSecurityService().adminResetPassword(SecurityUtil.getCurrentUserName(), userCredential.getPassword().toCharArray());
				return Response.ok().build();

			} catch (AuthenticationException authException) {
				LOG.log(Level.FINE, "Current User Password - Failed to auth existing password.", authException);

				RuleResult result = new RuleResult();
				result.setFieldName(UserCredential.FIELD_EXISTING_PASSWORD);
				result.setMessage("Existing password is invalid");
				validationResult.getRuleResults().add(result);
			}
		}

		return Response.ok(validationResult.toRestError()).build();
	}

	@DELETE
	@APIDescription("Delete a user, user registration and profile.")
	@RequireSecurity(SecurityPermission.ADMIN_USER_MANAGEMENT)
	@Path("/{username}")
	public Response deleteUser(
			@PathParam("username") String username
	)
	{
		service.getSecurityService().deleteUser(username.toLowerCase());
		return Response.noContent().build();
	}

}
