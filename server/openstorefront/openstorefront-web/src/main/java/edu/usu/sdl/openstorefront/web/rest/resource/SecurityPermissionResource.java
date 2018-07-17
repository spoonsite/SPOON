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
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.SecurityPolicy;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author bmichaelis
 */
@Path("v1/resource/securitypermission")
@APIDescription("Provides access to security permission information")
public class SecurityPermissionResource
	extends BaseResource
{
	@GET
	@APIDescription("Gets some info ")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(SecurityPolicy.class)
	public Response getPermission()
	{
		Service service = ServiceProxyFactory.getServiceProxy();
		List<SecurityPermission> securityPermissionList = service.getLookupService().findLookup(SecurityPermission.class);
		GenericEntity<List<SecurityPermission>> entity = new GenericEntity<List<SecurityPermission>>(securityPermissionList)
		{
		};
		return sendSingleEntityResponse(entity);
	}

}

//		Service service = ServiceProxyFactory.getServiceProxy();
//		AttributeCodePk pk = new AttributeCodePk();
//		pk.setAttributeCode(getComponentAttributePk().getAttributeCode());
//		pk.setAttributeType(getComponentAttributePk().getAttributeType());
//		AttributeCode code = service.getAttributeService().findCodeForType(pk);
//		service.getLookupService().findLookup(lookTableClass);