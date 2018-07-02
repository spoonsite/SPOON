/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.action;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import edu.usu.sdl.openstorefront.doc.EntityProcessor;
import edu.usu.sdl.openstorefront.doc.JaxrsProcessor;
import edu.usu.sdl.openstorefront.doc.model.APIResourceModel;
import edu.usu.sdl.openstorefront.doc.model.EntityDocModel;
import edu.usu.sdl.openstorefront.doc.model.ServiceClassModel;
import edu.usu.sdl.openstorefront.doc.sort.ApiResourceComparator;
import edu.usu.sdl.openstorefront.web.rest.RestConfiguration;
import edu.usu.sdl.openstorefront.web.rest.resource.BaseResource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.util.ResolverUtil;
import net.sourceforge.stripes.validation.Validate;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class APIAction
		extends BaseAction
{

	@Validate(required = true, on = "API")
	private String resourceClass;

	@Validate(required = true, on = "Page")
	private String page;

	private APIResourceModel resourceModel;

	private String classPath = "resource";
	private String classPathDescription = "Resource";

	private List<APIResourceModel> allResources = new ArrayList<>();

	private List<LookupModel> resourceClasses = new ArrayList<>();
	private List<LookupModel> serviceClasses = new ArrayList<>();

	private List<EntityDocModel> entityDocModels = new ArrayList<>();

	@DefaultHandler
	public Resolution mainPage()
	{
		initServiceDescription();
		return new ForwardResolution("/WEB-INF/securepages/api/apidocmain.jsp");
	}

	@HandlesEvent("Services")
	public Resolution getServices()
	{
		initServiceDescription();
		ServiceClassModel serviceClassModel = new ServiceClassModel();
		serviceClassModel.setResourceClasses(resourceClasses);
		serviceClassModel.setServiceClasses(serviceClasses);

		return streamResults(serviceClassModel);
	}

	@SuppressWarnings({"unchecked", "squid:S1872"})
	private void initServiceDescription()
	{
		ResolverUtil resolverUtil = new ResolverUtil();
		resolverUtil.find(new ResolverUtil.IsA(BaseResource.class), "edu.usu.sdl.openstorefront.web.rest.resource");

		List<Class> classList = new ArrayList<>();
		classList.addAll(resolverUtil.getClasses());
		for (Class apiResourceClass : classList) {
			if (BaseResource.class.getName().equals(apiResourceClass.getName()) == false) {

				//skip extensions classes
				if (!apiResourceClass.getSimpleName().endsWith("Ext")) {
					LookupModel lookupModel = new LookupModel();
					lookupModel.setCode(apiResourceClass.getSimpleName());
					lookupModel.setDescription(String.join(" ", StringUtils.splitByCharacterTypeCamelCase(apiResourceClass.getSimpleName())).replace("Resource", "").replace("REST", ""));
					resourceClasses.add(lookupModel);
				}
			}
		}

		resolverUtil = new ResolverUtil();
		resolverUtil.find(new ResolverUtil.IsA(BaseResource.class), "edu.usu.sdl.openstorefront.web.rest.service");

		classList = new ArrayList<>();
		classList.addAll(resolverUtil.getClasses());
		for (Class apiResourceClass : classList) {
			LookupModel lookupModel = new LookupModel();
			lookupModel.setCode(apiResourceClass.getSimpleName());
			lookupModel.setDescription(String.join(" ", StringUtils.splitByCharacterTypeCamelCase(apiResourceClass.getSimpleName())).replace("Service", ""));
			serviceClasses.add(lookupModel);
		}
		resourceClasses.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, LookupModel.DESCRIPTION_FIELD));
		serviceClasses.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, LookupModel.DESCRIPTION_FIELD));
	}

	@HandlesEvent("API")
	public Resolution apiDetails()
	{
		try {
			classPathDescription = StringUtils.capitalize(classPath);
			Class resource = Class.forName("edu.usu.sdl.openstorefront.web.rest." + classPath + "." + resourceClass);
			resourceModel = JaxrsProcessor.processRestClass(resource, RestConfiguration.APPLICATION_BASE_PATH);
		} catch (ClassNotFoundException ex) {
			return new ErrorResolution(404, "resource not found");
		}
		return streamResults(resourceModel);
	}

	@HandlesEvent("Page")
	public Resolution apiPage()
	{
		page = page.replace("../", "");
		if (page.equalsIgnoreCase("apidetails.jsp")) {
			page = "404";
		}
		return new ForwardResolution("/WEB-INF/securepages/api/" + page + ".jsp");
	}

	@HandlesEvent("PrintView")
	@SuppressWarnings({"unchecked", "squid:S1872"})
	public Resolution printView()
	{
		ResolverUtil resolverUtil = new ResolverUtil();
		resolverUtil.find(new ResolverUtil.IsA(BaseResource.class), "edu.usu.sdl.openstorefront.web.rest.resource");

		List<Class> classList = new ArrayList<>();
		classList.addAll(resolverUtil.getClasses());
		for (Class apiResourceClass : classList) {
			if (BaseResource.class.getName().equals(apiResourceClass.getName()) == false) {
				APIResourceModel result = JaxrsProcessor.processRestClass(apiResourceClass, RestConfiguration.APPLICATION_BASE_PATH);
				allResources.add(result);
			}
		}

		resolverUtil = new ResolverUtil();
		resolverUtil.find(new ResolverUtil.IsA(BaseResource.class), "edu.usu.sdl.openstorefront.web.rest.service");

		classList = new ArrayList<>();
		classList.addAll(resolverUtil.getClasses());
		for (Class apiResourceClass : classList) {
			APIResourceModel result = JaxrsProcessor.processRestClass(apiResourceClass, RestConfiguration.APPLICATION_BASE_PATH);
			allResources.add(result);
		}
		allResources.sort(new ApiResourceComparator<>());

		return new ForwardResolution("/WEB-INF/securepages/api/printapi.jsp");
	}

	@HandlesEvent("ViewEntities")
	@SuppressWarnings({"unchecked", "squid:S1872"})
	public Resolution viewEntities()
	{
		ResolverUtil resolverUtil = new ResolverUtil();
		resolverUtil.find(new ResolverUtil.IsA(Serializable.class), "edu.usu.sdl.openstorefront.core.entity");
		List<Class> classList = new ArrayList<>();
		classList.addAll(resolverUtil.getClasses());

		entityDocModels = EntityProcessor.processEntites(classList);

		return new ForwardResolution("/WEB-INF/securepages/api/entity.jsp");
	}

	public String getResourceClass()
	{
		return resourceClass;
	}

	public void setResourceClass(String resourceClass)
	{
		this.resourceClass = resourceClass;
	}

	public APIResourceModel getResourceModel()
	{
		return resourceModel;
	}

	public void setResourceModel(APIResourceModel resourceModel)
	{
		this.resourceModel = resourceModel;
	}

	public String getPage()
	{
		return page;
	}

	public void setPage(String page)
	{
		this.page = page;
	}

	public String getClassPath()
	{
		return classPath;
	}

	public void setClassPath(String classPath)
	{
		this.classPath = classPath;
	}

	public String getClassPathDescription()
	{
		return classPathDescription;
	}

	public void setClassPathDescription(String classPathDescription)
	{
		this.classPathDescription = classPathDescription;
	}

	public List<APIResourceModel> getAllResources()
	{
		return allResources;
	}

	public void setAllResources(List<APIResourceModel> allResources)
	{
		this.allResources = allResources;
	}

	public List<LookupModel> getResourceClasses()
	{
		return resourceClasses;
	}

	public void setResourceClasses(List<LookupModel> resourceClasses)
	{
		this.resourceClasses = resourceClasses;
	}

	public List<LookupModel> getServiceClasses()
	{
		return serviceClasses;
	}

	public void setServiceClasses(List<LookupModel> serviceClasses)
	{
		this.serviceClasses = serviceClasses;
	}

	public List<EntityDocModel> getEntityDocModels()
	{
		return entityDocModels;
	}

	public void setEntityDocModels(List<EntityDocModel> entityDocModels)
	{
		this.entityDocModels = entityDocModels;
	}

}
