/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.ComponentEvaluationSection;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ComponentEvaluationView
{

	private String reviewedVersion;

	@DataType(ComponentEvaluationSectionView.class)
	private List<ComponentEvaluationSectionView> evaluationSections = new ArrayList<>();

	public static ComponentEvaluationView toViewFromStorage(List<ComponentEvaluationSection> sections)
	{
		List<ComponentEvaluationSectionView> newSections = ComponentEvaluationSectionView.toViewList(sections);
		newSections.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, ComponentEvaluationSectionView.NAME_FIELD));
		return ComponentEvaluationView.toView(newSections);
	}

	public static ComponentEvaluationView toView(List<ComponentEvaluationSectionView> sections)
	{
		ComponentEvaluationView view = new ComponentEvaluationView();
		view.setEvaluationSections(sections);
		return view;
	}

	public String getReviewedVersion()
	{
		return reviewedVersion;
	}

	public void setReviewedVersion(String reviewedVersion)
	{
		this.reviewedVersion = reviewedVersion;
	}

	public List<ComponentEvaluationSectionView> getEvaluationSections()
	{
		return evaluationSections;
	}

	public void setEvaluationSections(List<ComponentEvaluationSectionView> evaluationSections)
	{
		this.evaluationSections = evaluationSections;
	}

}
