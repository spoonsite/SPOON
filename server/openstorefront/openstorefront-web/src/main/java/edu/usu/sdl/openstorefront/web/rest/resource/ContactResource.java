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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.ComponentContact;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/contacts")
@APIDescription("Provide access to contacts")
public class ContactResource
		extends BaseResource
{

	@GET
	@APIDescription("Gets distinct contacts")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentContact.class)
	@Path("/filtered")
	public List<ComponentContact> getAllContacts()
	{
		ComponentContact contactExample = new ComponentContact();
		contactExample.setActiveStatus(ComponentContact.ACTIVE_STATUS);
		List<ComponentContact> contacts = contactExample.findByExample();
		List<ComponentContact> filtered = new ArrayList<>();
		Set<String> uniqueSet = new HashSet<>();
		for (ComponentContact contact : contacts) {
			String key = contact.uniqueKey();
			if (StringUtils.isNotBlank(key)) {
				if (uniqueSet.contains(key) == false) {
					filtered.add(contact);
					uniqueSet.add(key);
				}
			}
		}
		filtered.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_DESCENDING, "firstName"));

		return filtered;
	}

}
