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
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeCodePk;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import edu.usu.sdl.openstorefront.storage.model.BaseComponent;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttribute;
import edu.usu.sdl.openstorefront.storage.model.ComponentContact;
import edu.usu.sdl.openstorefront.storage.model.ComponentExternalDependency;
import edu.usu.sdl.openstorefront.storage.model.ComponentMedia;
import edu.usu.sdl.openstorefront.storage.model.ComponentMetadata;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestion;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.storage.model.ComponentResource;
import edu.usu.sdl.openstorefront.storage.model.ComponentReview;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewCon;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewPro;
import edu.usu.sdl.openstorefront.storage.model.ComponentTag;
import edu.usu.sdl.openstorefront.storage.model.TestEntity;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentAttributeView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentContactView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentDetailView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentEvaluationView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentExternalDependencyView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentQuestionView;
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
	public ComponentDetailView getComponentDetails(String componentId)
	{

		// TODO: Decide on whether we'll be using the storange models here
		// or if we'll be using the view models... Currently they're the view models
		ComponentDetailView result = new ComponentDetailView();
		
		// TODO: Make the ComponentDetailView extend the storage Component so we can handle
		// all of that stuff there...
		
		result.setComponentId(Long.MIN_VALUE /*change the id to a string?*/);
		result.setTags(findClassInstances(ComponentTag.class, componentId, Boolean.TRUE));
		result.setResources(findClassInstances(ComponentResource.class, componentId, Boolean.TRUE));
		result.setMetadata(findClassInstances(ComponentMetadata.class, componentId, Boolean.TRUE));
		result.setComponentMedia(findClassInstances(ComponentMedia.class, componentId, Boolean.TRUE));
		result.setDependencies(findClassInstances(ComponentExternalDependency.class, componentId, Boolean.TRUE));
		result.setContacts(findClassInstances(ComponentContact.class, componentId, Boolean.TRUE));
	    result.setComponentViews(Integer.MIN_VALUE /*figure out a way to get component views*/);

		
		// This may need to change to create a list of the reviews as view objects instead of the storage models
		// Here we grab the pros and cons for the reviews.
		List<ComponentReviewView> reviews = (List<ComponentReviewView>)(List<?>)findClassInstances(ComponentReview.class, componentId, Boolean.TRUE);
		reviews.stream().forEach((review) -> {
			ComponentReviewPro tempPro = new ComponentReviewPro();
			// TODO: Set the composite key here so we can grab the right pros.
			ComponentReviewCon tempCon = new ComponentReviewCon();
			// TODO: Set the composite key here so we can grab the right cons.
			review.setPros(persistenceService.queryByExample(ComponentReviewPro.class, new QueryByExample(tempPro)));
			review.setCons(persistenceService.queryByExample(ComponentReviewCon.class, new QueryByExample(tempPro)));
		});
		result.setReviews(reviews);
		
		// This may also need to change to create a list of component question views out of the component question results
		// Here we grab the responses to each question
		List<ComponentQuestionView> questions = (List<ComponentQuestionView>)(List<?>)findClassInstances(ComponentQuestion.class, componentId, Boolean.TRUE);
		questions.stream().forEach((question) -> {
			ComponentQuestionResponse tempResponse = new ComponentQuestionResponse();
			tempResponse.setQuestionId(question.getQuestionId());
			question.setResponses(persistenceService.queryByExample(ComponentQuestionResponse.class, new QueryByExample(tempResponse)));
		});
		result.setQuestions(questions);

		// This might change also.
		// Here we grab the descriptions for each type and code per attribute
		List<ComponentAttributeView> attributes = (List<ComponentAttributeView>)(List<?>)findClassInstances(ComponentAttribute.class, componentId, Boolean.TRUE);
		attributes.stream().forEach((attribute) -> {
			AttributeType tempType = new AttributeType();
			AttributeCode tempCode = new AttributeCode();
			tempType.setAttributeType(attribute.getComponentAttributePk().getAttributeType());
			AttributeCodePk codePk = new AttributeCodePk();
			codePk.setAttributeCode(attribute.getComponentAttributePk().getAttributeCode());
			codePk.setAttributeType(attribute.getComponentAttributePk().getAttributeType());
			tempCode.setAttributeCodePk(codePk);
			
			tempType = persistenceService.queryByExample(AttributeType.class, new QueryByExample(tempType)).get(0);
			tempCode = persistenceService.queryByExample(AttributeCode.class, new QueryByExample(tempCode)).get(0);
			attribute.setCodeDescription(tempCode.getDescription());
			attribute.setCodeLongDescription(tempCode.getFullDescription());
			attribute.setTypeDescription(tempType.getDescription());
		});
		result.setAttributes(attributes);
				
		//List<ComponentEvaluationView> tempEvaluation = findClassInstances(ComponentEvaluationView.class, "setComponentId", componentId);
		//if (!tempEvaluation.isEmpty())
		//{
		//	result.setEvaluation(tempEvaluation.get(0));
		//}
		
		return result;
	}
	
}
