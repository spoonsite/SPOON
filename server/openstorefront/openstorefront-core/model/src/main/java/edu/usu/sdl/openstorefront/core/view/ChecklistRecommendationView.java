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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklistRecommendation;
import edu.usu.sdl.openstorefront.core.entity.RecommendationType;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class ChecklistRecommendationView
		extends EvaluationChecklistRecommendation
{

	private String typeDescription;

	public static ChecklistRecommendationView toView(EvaluationChecklistRecommendation recommendation)
	{
		ChecklistRecommendationView view = new ChecklistRecommendationView();
		try {
			BeanUtils.copyProperties(view, recommendation);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		view.setTypeDescription(TranslateUtil.translate(RecommendationType.class, recommendation.getRecommendationType()));
		return view;
	}

	public static List<ChecklistRecommendationView> toView(List<EvaluationChecklistRecommendation> recommendations)
	{
		List<ChecklistRecommendationView> views = new ArrayList<>();

		for (EvaluationChecklistRecommendation recommendation : recommendations) {
			views.add(toView(recommendation));
		}

		return views;
	}

	public String getTypeDescription()
	{
		return typeDescription;
	}

	public void setTypeDescription(String typeDescription)
	{
		this.typeDescription = typeDescription;
	}

}
