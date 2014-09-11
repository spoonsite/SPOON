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
package edu.usu.sdl.openstorefront.service.api;

import edu.usu.sdl.openstorefront.service.ServiceInterceptor;
import edu.usu.sdl.openstorefront.service.TransactionInterceptor;
import edu.usu.sdl.openstorefront.storage.model.BaseComponent;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttribute;
import edu.usu.sdl.openstorefront.storage.model.ComponentContact;
import edu.usu.sdl.openstorefront.storage.model.ComponentEvaluationSchedule;
import edu.usu.sdl.openstorefront.storage.model.ComponentEvaluationSection;
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
import edu.usu.sdl.openstorefront.storage.model.ComponentTracking;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentDetailView;
import edu.usu.sdl.openstorefront.web.rest.model.RequiredForComponent;
import java.util.List;

/**
 * Services that handle all component classes
 *
 * @author dshurtleff
 * @author jlaw
 */
public interface ComponentService
{

	/**
	 * This only returns the active
	 *
	 *
	 * @param <T>
	 * @param subComponentClass
	 * @param componentId
	 * @return
	 */
	public <T extends BaseComponent> List<T> getBaseComponent(Class<T> subComponentClass, String componentId);

	/**
	 * This can be use to get parts of the component (Eg. ComponentReview)
	 *
	 * @param <T>
	 * @param subComponentClass
	 * @param componentId
	 * @param all (true to get inactive as well)
	 * @return
	 */
	public <T extends BaseComponent> List<T> getBaseComponent(Class<T> subComponentClass, String componentId, boolean all);

	/**
	 *
	 * @param <T>
	 * @param subComponentClass
	 * @param pk
	 * @return
	 */
	public <T extends BaseComponent> T deactivateBaseComponent(Class<T> subComponentClass, Object pk);

	/**
	 *
	 * @param <T>
	 * @param subComponentClass
	 * @param pk
	 * @param all
	 * @return
	 */
	public <T extends BaseComponent> T deactivateBaseComponent(Class<T> subComponentClass, Object pk, boolean all);

	/**
	 * 
	 * @param <T>
	 * @param subComponentClass
	 * @param componentId 
	 */
	public <T extends BaseComponent> void deleteBaseComponent(Class<T> subComponentClass, String componentId);

	/**
	 * 
	 * @param <T>
	 * @param subComponentClass
	 * @param componentId
	 * @param all 
	 */
	public <T extends BaseComponent> void deleteBaseComponent(Class<T> subComponentClass, String componentId, Boolean all);

	
	/**
	 * Return the whole list of components. (the short view)
	 *
	 * @return
	 */
	public List<Component> getComponents();

	/**
	 * Return the details object of the component attached to the given id. (the
	 * full view)
	 *
	 * @param componentId
	 * @return
	 */
	public ComponentDetailView getComponentDetails(String componentId);

	/**
	 *
	 * @param attribute
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentAttribute(ComponentAttribute attribute);

	/**
	 *
	 * @param attribute
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public Boolean checkComponentAttribute(ComponentAttribute attribute);

	/**
	 *
	 * @param contact
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentContact(ComponentContact contact);

	/**
	 *
	 * @param dependency
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentDependency(ComponentExternalDependency dependency);

	/**
	 *
	 * @param section
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentEvaluationSection(ComponentEvaluationSection section);

	/**
	 *
	 * @param schedule
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentEvaluationSchedule(ComponentEvaluationSchedule schedule);

	/**
	 *
	 * @param media
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentMedia(ComponentMedia media);

	/**
	 *
	 * @param metadata
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentMetadata(ComponentMetadata metadata);

	/**
	 *
	 * @param question
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentQuestion(ComponentQuestion question);

	/**
	 *
	 * @param response
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentQuestionResponse(ComponentQuestionResponse response);

	/**
	 *
	 * @param resource
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentResource(ComponentResource resource);

	/**
	 *
	 * @param review
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentReview(ComponentReview review);

	/**
	 *
	 * @param con
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentReviewCon(ComponentReviewCon con);

	/**
	 *
	 * @param pro
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentReviewPro(ComponentReviewPro pro);

	/**
	 *
	 * @param tag
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentTag(ComponentTag tag);

	/**
	 *
	 * @param tracking
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentTracking(ComponentTracking tracking);

	/**
	 *
	 * @param component
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public RequiredForComponent saveComponent(RequiredForComponent component);
	// Todo: Make an object that we can pass in to this function, or figure out which
	// combination we'll need...

}
