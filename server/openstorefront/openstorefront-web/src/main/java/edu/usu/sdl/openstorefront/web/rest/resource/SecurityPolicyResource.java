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
import edu.usu.sdl.openstorefront.core.entity.SecurityPolicy;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/securitypolicy")
@APIDescription("Provides access to the security policy")
public class SecurityPolicyResource
	extends BaseResource
{
	
	@GET 
	@APIDescription("Gets current security policy.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(SecurityPolicy.class)
	public Response getPolicy()
	{
		SecurityPolicy securityPolicy = service.getSecurityService().getSecurityPolicy();
		return sendSingleEntityResponse(securityPolicy);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_SECURITY_POLICY)
	@APIDescription("Updates security policy.")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(SecurityPolicy.class)	
	@Path("/{policyId}")
	public Response updatePolicy(
			@PathParam("policyId") String policyId,
			SecurityPolicy securityPolicy
	)
	{	
		SecurityPolicy existing = new SecurityPolicy();
		existing.setPolicyId(policyId);
		if (existing != null) {
		
			securityPolicy.setPolicyId(policyId);			
			securityPolicy = service.getSecurityService().updateSecurityPolicy(securityPolicy);
			return Response.ok(securityPolicy).build();
		} 
		return sendSingleEntityResponse(existing);
	}
	
}
