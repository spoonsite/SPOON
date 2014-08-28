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
import edu.usu.sdl.openstorefront.web.rest.model.ComponentView;
import java.util.List;

/**
 * Services that handle all component classes
 *
 * @author dshurtleff
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
	 * Return the whole list of components. (the short view)
	 *
	 * @return
	 */
	public List<ComponentView> getComponents();

	/**
	 * Return the whole list of components. (the short view)
	 *
	 * @param componentId
	 * @return
	 */
	public ComponentView getComponent(String componentId);

	/**
	 * Return the details object of the component attached to the given id. (the
	 * full view)
	 *
	 * @param componentId
	 * @return
	 */
	public ComponentDetailView getComponentDetails(String componentId);

	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentAttribute(ComponentAttribute attribute);
	
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentContact(ComponentContact contact);
	
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentEvaluationSection(ComponentEvaluationSection section);
	
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentEvaluationSchedule(ComponentEvaluationSchedule schedule);

	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentMedia(ComponentMedia media);

	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentMetadata(ComponentMetadata metadata);
	
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentQuestion(ComponentQuestion question);
	
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentQuestionResponse(ComponentQuestionResponse response);
	
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentResource(ComponentResource resource);
	
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentReview(ComponentReview review);
	
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentReviewCon(ComponentReviewCon con);

	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentReviewPro(ComponentReviewPro pro);
	
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentTag(ComponentTag tag);
	
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentTracking(ComponentTracking tracking);

}
