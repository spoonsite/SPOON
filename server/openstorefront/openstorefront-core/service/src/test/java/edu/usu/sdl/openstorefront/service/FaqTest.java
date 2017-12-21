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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.core.api.FaqService;
import edu.usu.sdl.openstorefront.core.api.PersistenceService;
import edu.usu.sdl.openstorefront.core.entity.Faq;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author cyearsley
 */
public class FaqTest
{
	
	/**
	 * Test FAQ Activate
	 */
	@Test
	public void createFaq() {
		
		PersistenceService persistenceService = Mockito.mock(PersistenceService.class);
		
		Faq faq = new Faq();
		Mockito.when(persistenceService.persist(Mockito.any(Faq.class))).thenReturn(faq);
		
		ServiceProxy.Test.setPersistenceServiceToMock(persistenceService);
		
		FaqService faqService = new FaqServiceImpl();
		faqService.createFaq(faq);

		Mockito.verify(persistenceService, Mockito.times(1)).persist(Mockito.any(Faq.class));
	}
	
	@After
	public void cleanup() {
		ServiceProxy.Test.clearPersistenceMock();
	}
}
