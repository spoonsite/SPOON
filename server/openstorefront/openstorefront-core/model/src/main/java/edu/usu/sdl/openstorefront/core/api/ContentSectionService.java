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
package edu.usu.sdl.openstorefront.core.api;

import edu.usu.sdl.openstorefront.core.entity.ContentSectionAttribute;
import edu.usu.sdl.openstorefront.core.entity.ContentSectionMedia;
import edu.usu.sdl.openstorefront.core.model.ContentSectionAll;
import java.io.InputStream;

/**
 * Handles Content Sections
 *
 * @author dshurtleff
 */
public interface ContentSectionService
		extends AsyncService
{

	/**
	 * Saves content Section Attribute
	 *
	 * @param contentSectionAttribute
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public ContentSectionAttribute saveAttribute(ContentSectionAttribute contentSectionAttribute);

	/**
	 * Save media for a section This doesn't support transactions
	 *
	 * @param contentSectionMedia
	 * @param in
	 * @return
	 */
	public ContentSectionMedia saveMedia(ContentSectionMedia contentSectionMedia, InputStream in);

	/**
	 * Hard deletes the media and removes existing file.
	 *
	 * @param contentSectionMediaId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void deleteMedia(String contentSectionMediaId);

	/**
	 * Save everything except media that has to be handled separately. Deletes
	 * existing attributes and save whats in the section Deletes existing
	 * sub-section and save whats in the section
	 *
	 * @param contentSectionAll
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public String saveAll(ContentSectionAll contentSectionAll);

	/**
	 * Get the complete model for a section
	 *
	 * @param contentSectionId
	 * @return
	 */
	public ContentSectionAll getContentSectionAll(String contentSectionId);

}
