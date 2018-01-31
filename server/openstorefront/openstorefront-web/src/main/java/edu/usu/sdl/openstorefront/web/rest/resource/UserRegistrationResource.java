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

import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.UserRegistration;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.UserRegistrationView;
import edu.usu.sdl.openstorefront.core.view.UserRegistrationWrapper;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.validation.RuleResult;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.sourceforge.stripes.util.bean.BeanUtil;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/userregistrations")
@APIDescription("Handles user registration")
public class UserRegistrationResource
		extends BaseResource
{

	@Context
	HttpServletRequest request;

	@Context
	HttpServletResponse response;

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_USER_MANAGEMENT)
	@APIDescription("Gets user registeration records.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserRegistrationWrapper.class)
	public Response getUserRegistration(
			@BeanParam FilterQueryParams filterQueryParams
	)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		UserRegistration registrationExample = new UserRegistration();
		registrationExample.setActiveStatus(filterQueryParams.getStatus());

		UserRegistration registrationStartExample = new UserRegistration();
		registrationStartExample.setUpdateDts(filterQueryParams.getStart());

		UserRegistration registrationEndExample = new UserRegistration();
		registrationEndExample.setUpdateDts(filterQueryParams.getEnd());

		QueryByExample queryByExample = new QueryByExample(registrationExample);

		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(registrationStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(registrationEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		queryByExample.setMaxResults(filterQueryParams.getMax());
		queryByExample.setFirstResult(filterQueryParams.getOffset());
		queryByExample.setSortDirection(filterQueryParams.getSortOrder());

		UserRegistration registrationSortExample = new UserRegistration();
		Field sortField = ReflectionUtil.getField(registrationSortExample, filterQueryParams.getSortField());
		if (sortField != null) {
			BeanUtil.setPropertyValue(sortField.getName(), registrationSortExample, QueryByExample.getFlagForType(sortField.getType()));
			queryByExample.setOrderBy(registrationSortExample);
		}

		List<UserRegistration> userRegistrations = service.getPersistenceService().queryByExample(queryByExample);

		UserRegistrationWrapper userRegistrationWrapper = new UserRegistrationWrapper();
		userRegistrationWrapper.getData().addAll(UserRegistrationView.toView(userRegistrations));
		userRegistrationWrapper.setTotalNumber(service.getPersistenceService().countByExample(queryByExample));

		return sendSingleEntityResponse(userRegistrationWrapper);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_USER_MANAGEMENT)
	@APIDescription("Gets a user registeration record.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserRegistrationView.class)
	@Path("/{registrationId}")
	public Response getUserRegistration(
			@PathParam("registrationId") String registrationId
	)
	{
		UserRegistration registration = new UserRegistration();
		registration.setRegistrationId(registrationId);
		registration = registration.find();
		return sendSingleEntityResponse(UserRegistrationView.toView(registration));
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_USER_MANAGEMENT)
	@APIDescription("Creates a user registration by administrator")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/admin")
	public Response adminCreateUserRegistration(
			UserRegistration userRegistration
	)
	{
		ValidationResult validationResult = userRegistration.validate();
		if (validationResult.valid()) {
			validationResult.merge(service.getSecurityService().processNewRegistration(userRegistration, false));
		}
		if (validationResult.valid()) {
			validationResult.merge(service.getSecurityService().processNewUser(userRegistration));
		}

		if (validationResult.valid()) {
			UserRegistration savedRegistration = new UserRegistration();
			savedRegistration.setRegistrationId(userRegistration.getRegistrationId());
			savedRegistration = savedRegistration.find();

			return Response.created(URI.create("v1/resource/userregistrations/" + savedRegistration.getRegistrationId())).entity(savedRegistration).build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}

	}

	@POST
	@APIDescription("Creates a user registration")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public Response createUserRegistration(
			UserRegistration userRegistration
	)
	{
		if (SecurityUtil.isLoggedIn()) {
			SecurityUtil.logout(request, response);
		}

		ValidationResult validationResult = userRegistration.validate();
		if (validationResult.valid()) {
			validationResult.merge(service.getSecurityService().processNewRegistration(userRegistration, true));
		}

		if (validationResult.valid()) {
			UserRegistration savedRegistration = new UserRegistration();
			savedRegistration.setRegistrationId(userRegistration.getRegistrationId());
			savedRegistration = savedRegistration.find();

			return Response.created(URI.create("v1/resource/userregistrations/" + savedRegistration.getRegistrationId())).entity(savedRegistration).build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@PUT
	@APIDescription("Creates a user from an existing Registration")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public Response createUser(
			UserRegistration userRegistration
	)
	{
		if (SecurityUtil.isLoggedIn()) {
			SecurityUtil.logout(request, response);
		}

		ValidationResult validationResult = userRegistration.validate();
		validationResult.merge(validateCode(userRegistration));
		if (validationResult.valid()) {
			validationResult.merge(service.getSecurityService().processNewUser(userRegistration));
		}

		if (validationResult.valid()) {

			UserRegistration savedRegistration = new UserRegistration();
			savedRegistration.setRegistrationId(userRegistration.getRegistrationId());
			savedRegistration = savedRegistration.find();

			return Response.created(URI.create("v1/resource/userregistrations/" + userRegistration.getRegistrationId())).entity(savedRegistration).build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_USER_MANAGEMENT)
	@APIDescription("Deletes a user registeration record only")
	@Path("/{registrationId}")
	public void deleteUserRegistration(
			@PathParam("registrationId") String registrationId
	)
	{
		service.getSecurityService().deleteUserRegistration(registrationId);
	}

	private ValidationResult validateCode(UserRegistration userRegistration)

	{
		ValidationResult result = new ValidationResult();

		UserRegistration savedRegistration = new UserRegistration();
		savedRegistration.setRegistrationId(userRegistration.getRegistrationId());
		savedRegistration = savedRegistration.find();
		if (savedRegistration == null
				|| (!userRegistration.getEmail().equals(savedRegistration.getEmail()))
				|| (!userRegistration.getUsername().equals(savedRegistration.getUsername()))
				|| (!userRegistration.getVerificationCode().equals(savedRegistration.getVerificationCode()))) {
			RuleResult ruleResult = new RuleResult();
			ruleResult.setEntityClassName(UserRegistration.class.getSimpleName());
			ruleResult.setFieldName(UserRegistration.VERIFICATION_CODE_FIELD);
			ruleResult.setMessage("Invalid or Expired Verification Code");
			result.getRuleResults().add(ruleResult);
		}

		return result;
	}
}
