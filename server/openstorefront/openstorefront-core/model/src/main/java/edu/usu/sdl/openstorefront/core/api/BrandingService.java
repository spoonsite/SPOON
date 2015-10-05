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

import edu.usu.sdl.openstorefront.core.entity.TopicSearchItem;
import edu.usu.sdl.openstorefront.core.view.BrandingView;
import java.util.List;

/**
 *
 * @author jlaw
 */
public interface BrandingService 
		extends AsyncService
{
	/**
	 * High-speed component name lookup
	 *
	 * @param brandingId
	 * @return Name or null if not found
	 */
	public BrandingView getBrandingView(String brandingId);

	/**
	 * High-speed component name lookup
	 *
	 * @return Name or null if not found
	 */
	public List<TopicSearchItem> getTopicSearchItems();

	/**
	 * High-speed component name lookup
	 *
	 * @param brandingId
	 * @return Name or null if not found
	 */
	public List<TopicSearchItem> getTopicSearchItems(String brandingId);

	/**
	 * High-speed component name lookup
	 *
	 * @param brandingId
	 * @return Name or null if not found
	 */
	public TopicSearchItem getTopicSearchItem(String entityId);

	/**
	 * High-speed component name lookup
	 *
	 * @param item
	 */
	public TopicSearchItem addTopicSearchItem(TopicSearchItem item);

	/**
	 * High-speed component name lookup
	 *
	 * @param item
	 */
	public void deleteTopicSearchItem(String entityId);

	/**
	 * High-speed component name lookup
	 *
	 */
	public void deleteTopicSearchItems(String brandingId);

}
