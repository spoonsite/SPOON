package edu.usu.sdl.openstorefront.service;


import edu.usu.sdl.openstorefront.core.api.FaqService;
import edu.usu.sdl.openstorefront.core.entity.Faq;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import java.util.List;
import java.util.logging.Logger;

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

/**
 *
 * @author cyearsley
 */
public class FaqServiceImpl
		extends ServiceProxy
		implements FaqService
{
	
	private static final Logger LOG = Logger.getLogger(FaqServiceImpl.class.getName());
	
	@Override
	public List<Faq> getFaqs(boolean isAdmin)
	{
		Faq faqExample = new Faq();
		
		if(!isAdmin) {
			faqExample.setActiveStatus(StandardEntity.ACTIVE_STATUS);
		}
		
		List<Faq> faqs = faqExample.findByExample();
		
		return faqs;
	}

	@Override
	public Faq getFaq(String faqId, boolean isAdmin)
	{
		Faq faqExample = new Faq();
		faqExample.setFaqId(faqId);
		
		if(!isAdmin) {
			faqExample.setActiveStatus(StandardEntity.ACTIVE_STATUS);
		}
		
		Faq faq = faqExample.find();
		
		return faq;
	}

	@Override
	public Faq createFaq(Faq faq)
	{
		return persistenceService.persist(faq);
	}

	@Override
	public Faq updateFaq(String faqId, Faq newFaq)
	{
		Faq faqExample = new Faq();
		faqExample.setFaqId(faqId);
		Faq faq = faqExample.findProxy();
		
		if (faq != null) {
			faq.updateFields(newFaq);
			
			faq = persistenceService.persist(faq);
		}
		
		return faq;
	}
	
	@Override
	public boolean deleteFaq(String faqId)
	{
		Faq faqExample = new Faq();
		faqExample.setFaqId(faqId);
		Faq faq = faqExample.findProxy();
		
		if (faq != null) {
			return faq.delete();
		}
		else {
			return false;
		}
	}
	
}
