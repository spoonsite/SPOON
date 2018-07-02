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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.Organization;
import edu.usu.sdl.openstorefront.core.entity.OrganizationType;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class OrganizationView
		extends Organization
{

	private String organizationTypeDescription;

	public static OrganizationView toView(Organization organization)
	{
		OrganizationView view = new OrganizationView();
		try {
			BeanUtils.copyProperties(view, organization);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		view.setOrganizationTypeDescription(TranslateUtil.translate(OrganizationType.class, organization.getOrganizationType()));
		return view;
	}

	public static List<OrganizationView> toView(List<Organization> organizations)
	{
		List<OrganizationView> views = new ArrayList<>();
		organizations.forEach(organization -> {
			views.add(toView(organization));
		});
		return views;
	}

	public String getOrganizationTypeDescription()
	{
		return organizationTypeDescription;
	}

	public void setOrganizationTypeDescription(String organizationTypeDescription)
	{
		this.organizationTypeDescription = organizationTypeDescription;
	}

}
