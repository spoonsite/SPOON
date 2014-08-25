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

package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.api.ComponentService;
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.storage.model.BaseEntity;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.TestEntity;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentAttribute;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentContact;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentDetail;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentEvaluation;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentEvaluationSchedule;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentExternalDependency;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentMedia;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentMetadata;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentQuestion;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentResource;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentReview;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentTag;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class ComponentServiceImpl
	extends ServiceProxy
	implements ComponentService
{
	private static final Logger log = Logger.getLogger(ComponentServiceImpl.class.getName());

	public ComponentServiceImpl()
	{
	}
	
	public <T> List<T> findClassInstances(Class<T> classInstance, String methodName, String id)
	{
		try
		{
			T temp = classInstance.newInstance();
			Method method = null;
			try 
			{
				method = temp.getClass().getMethod(methodName, String.class);
			} catch (SecurityException | NoSuchMethodException e) {
				// ...
			}
			try 
			{
				method.invoke(temp, id);
			}
			catch (InvocationTargetException ex) {
				Logger.getLogger(ComponentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
			}
			return persistenceService.queryByExample(classInstance, new QueryByExample((BaseEntity) temp));
		} catch(InstantiationException | IllegalAccessException ex)
		{
			throw new OpenStorefrontRuntimeException(ex);
		}
	}
	
	@Override
	public List<Component> getComponents()
	{
		Component temp;
		temp = new Component();
		temp.setActiveStatus(TestEntity.ACTIVE_STATUS);
		return persistenceService.queryByExample(Component.class, new QueryByExample(temp));
	}

	@Override
	public Component getComponentSingle(String componentId)
	{
		if (!componentId.isEmpty()) 
		{
			Component temp;
			temp = new Component();
			temp.setComponentId(componentId);
			List<Component> result = persistenceService.queryByExample(Component.class, new QueryByExample(temp));
			if (!result.isEmpty())
			{
				return result.get(0);
			} 
			else
			{
				//TODO: What do we return when there is no component with that id?
				return new Component();
			}
		}
		else 
		{
			throw new OpenStorefrontRuntimeException("There was an error getting the specified component");
		}
	}

	@Override
	public ComponentDetail getComponentDetails(String componentId)
	{

		// TODO: Decide on whether we'll be using the storange models here
		// or if we'll be using the view models... Currently they're the view models
		ComponentDetail result = new ComponentDetail();
		
		// TODO: Make the ComponentDetail extend the storage Component so we can handle
		// all of that stuff there...
		
		result.setComponentId(Long.MIN_VALUE /*change the id to a string?*/);
		result.setTags(findClassInstances(ComponentTag.class, "setComponentId", componentId));
		result.setReviews(findClassInstances(ComponentReview.class, "setComponentId", componentId));
		result.setResources(findClassInstances(ComponentResource.class, "setComponentId", componentId));
		result.setQuestions(findClassInstances(ComponentQuestion.class, "setComponentId", componentId));
		result.setMetadata(findClassInstances(ComponentMetadata.class, "setComponentId", componentId));
		result.setComponentMedia(findClassInstances(ComponentMedia.class, "setComponentId", componentId));
		result.setAttributes(findClassInstances(ComponentAttribute.class, "setComponentId", componentId));
		result.setDependencies(findClassInstances(ComponentExternalDependency.class, "setComponentId", componentId));
		result.setContacts(findClassInstances(ComponentContact.class, "setComponentId", componentId));
	    result.setComponentViews(Integer.MIN_VALUE /*figure out a way to get component views*/);
	
		List<ComponentEvaluation> tempEvaluation = findClassInstances(ComponentEvaluation.class, "setComponentId", componentId);
		if (!tempEvaluation.isEmpty())
		{
			result.setEvaluation(tempEvaluation.get(0));
		}
		
		return result;
	}
	
}
