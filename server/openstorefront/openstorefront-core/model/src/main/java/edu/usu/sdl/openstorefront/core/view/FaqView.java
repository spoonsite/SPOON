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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.Faq;
import edu.usu.sdl.openstorefront.core.entity.FaqCategoryType;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author cyearsley
 */
public class FaqView
		extends Faq
{

	private String faqCategoryTypeDescription;
	private int categorySortOrder;

	public static FaqView toView(Faq faq)
	{
		FaqView view = new FaqView();
		try {
			BeanUtils.copyProperties(view, faq);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		view.setFaqCategoryTypeDescription(TranslateUtil.translate(FaqCategoryType.class, faq.getCategory()));

		FaqCategoryType categoryType = ServiceProxyFactory.getServiceProxy().getLookupService().getLookupEnity(FaqCategoryType.class, faq.getCategory());
		int categorySortOrder = categoryType.getSortOrder() != null ? categoryType.getSortOrder() : Integer.MAX_VALUE;

		view.setCategorySortOrder(categorySortOrder);

		return view;
	}

	public static List<FaqView> toView(List<Faq> faqs)
	{
		List<FaqView> views = new ArrayList<>();
		faqs.forEach(faq -> {
			views.add(toView(faq));
		});
		return views;
	}

	public String getFaqCategoryTypeDescription()
	{
		return faqCategoryTypeDescription;
	}

	public void setFaqCategoryTypeDescription(String faqCategoryTypeDescription)
	{
		this.faqCategoryTypeDescription = faqCategoryTypeDescription;
	}

	public int getCategorySortOrder()
	{
		return categorySortOrder;
	}

	public void setCategorySortOrder(int categorySortOrder)
	{
		this.categorySortOrder = categorySortOrder;
	}
}
