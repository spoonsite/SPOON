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
public class EvaluationChecklistRecommendationView
		extends EvaluationChecklistRecommendation
{

	private String recommendationTypeDescription;

	public EvaluationChecklistRecommendationView()
	{
	}

	public static EvaluationChecklistRecommendationView toView(EvaluationChecklistRecommendation recommendation)
	{
		EvaluationChecklistRecommendationView view = new EvaluationChecklistRecommendationView();
		try {
			BeanUtils.copyProperties(view, recommendation);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		view.setRecommendationTypeDescription(TranslateUtil.translate(RecommendationType.class, view.getRecommendationType()));

		return view;
	}

	public static List<EvaluationChecklistRecommendationView> toView(List<EvaluationChecklistRecommendation> recommendations)
	{
		List<EvaluationChecklistRecommendationView> views = new ArrayList<>();

		for (EvaluationChecklistRecommendation recommendation : recommendations) {
			views.add(toView(recommendation));
		}
		return views;
	}

	public EvaluationChecklistRecommendation toRecommendation()
	{
		EvaluationChecklistRecommendation recommendation = new EvaluationChecklistRecommendation();
		try {
			BeanUtils.copyProperties(recommendation, this);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		return recommendation;
	}

	public String getRecommendationTypeDescription()
	{
		return recommendationTypeDescription;
	}

	public void setRecommendationTypeDescription(String recommendationTypeDescription)
	{
		this.recommendationTypeDescription = recommendationTypeDescription;
	}

}
