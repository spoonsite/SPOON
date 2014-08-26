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
import edu.usu.sdl.openstorefront.storage.model.BaseComponent;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.ComponentReview;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewCon;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewPro;
import edu.usu.sdl.openstorefront.storage.model.ComponentTag;
import edu.usu.sdl.openstorefront.storage.model.TestEntity;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentAttribute;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentContact;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentDetail;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentEvaluation;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentExternalDependency;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentMedia;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentMetadata;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentQuestion;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentResource;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentReviewView;
import java.util.List;
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
	
	public <T extends BaseComponent> List<T> findClassInstances(Class<T> classInstance, String id, Boolean activeStatus)
	{
		try
		{
			T temp = classInstance.newInstance();
			temp.setLookupId(id);
			if (activeStatus)
			{
				temp.setActiveStatus(BaseComponent.ACTIVE_STATUS);
			}
			return persistenceService.queryByExample(classInstance, new QueryByExample(temp));
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
			Component temp = new Component();
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
		result.setTags(findClassInstances(ComponentTag.class, componentId, Boolean.TRUE));
		
		//This may need to change to create a list of the reviews as view objects instead of the storage models
		List<ComponentReviewView> reviews = (List<ComponentReviewView>)(List<?>)findClassInstances(ComponentReview.class, componentId, Boolean.TRUE);
		for(ComponentReviewView review: reviews)
		{
			ComponentReviewPro tempPro = new ComponentReviewPro();
			//TODO: Set the composite key here so we can grab the right pros.
			ComponentReviewCon tempCon = new ComponentReviewCon();
			//TODO: Set the composite key here so we can grab the right cons.
			review.setPros(persistenceService.queryByExample(ComponentReviewPro.class, new QueryByExample(tempPro)));
			review.setCons(persistenceService.queryByExample(ComponentReviewCon.class, new QueryByExample(tempPro)));
		}
		result.setReviews(reviews);
		
		
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
