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
package edu.usu.sdl.openstorefront.core.api;

import edu.usu.sdl.openstorefront.core.entity.Branding;
import edu.usu.sdl.openstorefront.core.entity.TopicSearchItem;
import edu.usu.sdl.openstorefront.core.model.BrandingModel;
import edu.usu.sdl.openstorefront.core.view.BrandingView;
import java.util.List;

/**
 * Handles Branding
 *
 * @author jlaw
 */
public interface BrandingService
		extends AsyncService
{

	/**
	 * Finds the currently active branding or returns a default one if none
	 * exists
	 *
	 * @return BrandingView
	 */
	public BrandingView getCurrentBrandingView();

	/**
	 * Set the branding if found to active and set the other brandings to
	 * inactive
	 *
	 * @param brandingId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void setBrandingAsCurrent(String brandingId);

	/**
	 * Save a branding full branding entity
	 *
	 * @param brandingModel
	 * @return newly saved branding
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public BrandingModel saveFullBanding(BrandingModel brandingModel);

	/**
	 * Save a branding entity
	 *
	 * @param branding
	 * @return newly saved branding
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public Branding saveBanding(Branding branding);

	/**
	 * Removes existing topic search items and saves this list
	 *
	 * @param items
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveTopicSearchItems(String brandingId, List<TopicSearchItem> items);

	/**
	 * Delete branding entity and all related data
	 *
	 * @param brandingId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void deleteBranding(String brandingId);

}
