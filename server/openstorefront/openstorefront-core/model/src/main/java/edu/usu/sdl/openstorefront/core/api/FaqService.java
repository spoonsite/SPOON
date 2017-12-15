/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.core.entity.Faq;
import java.util.List;

/**
 *
 * @author cyearsley
 */
public interface FaqService
		extends AsyncService
{
	
	/**
	 * Gets all FAQs
	 *
	 * @return List<Faq>
	 */
	public List<Faq> getFaqs(boolean isAdmin);
	
	/**
	 * Get a single Faq
	 *
	 * @param faqId
	 * @return Faq
	 */
	public Faq getFaq(String faqId, boolean isAdmin);
	
	/**
	 * Create an Faq
	 *
	 * @param faq
	 * @return
	 */
	public Faq createFaq(Faq faq);
	
	/**
	 * Updates a Faq
	 *
	 * @param fiqId
	 * @param faq
	 * @return
	 */
	public Faq updateFaq(String fiqId, Faq faq);
	
	/**
	 * Deletes an Faq
	 *
	 * @param fiqId
	 * @return
	 */
	public boolean deleteFaq(String fiqId);
}
