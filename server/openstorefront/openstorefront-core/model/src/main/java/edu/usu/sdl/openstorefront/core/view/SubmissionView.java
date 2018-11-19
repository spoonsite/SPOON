/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author bmichaelis
 */
public class SubmissionView
		extends ComponentView
{
	private boolean evaluationsAttached;

	public SubmissionView()
	{
	}
	
	public static SubmissionView toView(ComponentView componentView, Map<String, List<Evaluation>> evaluationMap)
	{
		SubmissionView submissionView = new SubmissionView();
		try {
			BeanUtils.copyProperties(submissionView, componentView);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		if(evaluationMap == null){
			Evaluation evaluationExample = new Evaluation();
			evaluationExample.setOriginComponentId(componentView.getComponentId());
			if(evaluationExample.find() != null){
				submissionView.setEvaluationsAttached(true);
			}				
					
		}else{
			if(evaluationMap.containsKey(componentView.getComponentId())){
				submissionView.setEvaluationsAttached(true);
			}
		}
		
		return submissionView;
	}
	
	public static List<SubmissionView> toView(List<ComponentView> componentViews, Map<String, List<Evaluation>> evaluationMap){
		
		List<SubmissionView> submissionViews = new ArrayList<>();
		componentViews.forEach(componentView -> {
			submissionViews.add(toView(componentView, evaluationMap));
		});
		return submissionViews;
		
	}
	
	public boolean getEvaluationsAttached()
	{
		return evaluationsAttached;
	}

	public void setEvaluationsAttached(boolean evaluationsAttached)
	{
		this.evaluationsAttached = evaluationsAttached;
	}
	
}
