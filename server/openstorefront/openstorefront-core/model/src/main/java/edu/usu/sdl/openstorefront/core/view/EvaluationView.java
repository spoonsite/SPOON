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
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class EvaluationView
		extends Evaluation
{

	private String componentName;

	public EvaluationView()
	{
	}

	public static EvaluationView toView(Evaluation evaluation)
	{
		EvaluationView evaluationView = new EvaluationView();
		try {
			BeanUtils.copyProperties(evaluationView, evaluation);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		evaluationView.setComponentName(ServiceProxyFactory.getServiceProxy().getComponentService().getComponentName(evaluation.getEvaluationId()));

		return evaluationView;
	}

	public static List<EvaluationView> toView(List<Evaluation> evaluations)
	{
		List<EvaluationView> views = new ArrayList<>();

		for (Evaluation evaluation : evaluations) {
			views.add(toView(evaluation));
		}
		return views;
	}

	public String getComponentName()
	{
		return componentName;
	}

	public void setComponentName(String componentName)
	{
		this.componentName = componentName;
	}

}
