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

import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.ComponentEvaluationSection;
import edu.usu.sdl.openstorefront.core.entity.EvaluationSection;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ComponentEvaluationSectionView
		extends StandardEntityView
{

	public static final String NAME_FIELD = "name";

	private String name;
	private BigDecimal actualScore;
	private Date updateDts;
	private String activeStatus;
	private String evaluationSection;
	private Boolean notAvailable;
	private String sectionDescription;

	public static ComponentEvaluationSectionView toView(ComponentEvaluationSection section)
	{
		ComponentEvaluationSectionView view = new ComponentEvaluationSectionView();
		view.setName(TranslateUtil.translate(EvaluationSection.class, section.getComponentEvaluationSectionPk().getEvaluationSection()));

		if (section.getActualScore() == null) {
			view.setActualScore(Convert.toBigDecimal(section.getScore()));
		} else {
			view.setActualScore(section.getActualScore());
		}
		view.setUpdateDts(section.getUpdateDts());
		view.setActiveStatus(section.getActiveStatus());
		view.setNotAvailable(section.getNotAvailable());
		view.setEvaluationSection(section.getComponentEvaluationSectionPk().getEvaluationSection());
		EvaluationSection evaluationSection = ServiceProxyFactory.getServiceProxy().getLookupService().getLookupEnity(EvaluationSection.class, section.getComponentEvaluationSectionPk().getEvaluationSection());
		if (evaluationSection != null) {
			view.setSectionDescription(evaluationSection.getDetailedDescription());
		}
		view.toStandardView(section);

		return view;
	}

	public static List<ComponentEvaluationSectionView> toViewList(List<ComponentEvaluationSection> sections)
	{
		List<ComponentEvaluationSectionView> viewList = new ArrayList();
		sections.forEach(section -> {
			viewList.add(ComponentEvaluationSectionView.toView(section));
		});
		return viewList;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public BigDecimal getActualScore()
	{
		return actualScore;
	}

	public void setActualScore(BigDecimal actualScore)
	{
		this.actualScore = actualScore;
	}

	public Date getUpdateDts()
	{
		return updateDts;
	}

	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	public String getActiveStatus()
	{
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus)
	{
		this.activeStatus = activeStatus;
	}

	public String getEvaluationSection()
	{
		return evaluationSection;
	}

	public void setEvaluationSection(String evaluationSection)
	{
		this.evaluationSection = evaluationSection;
	}

	public Boolean getNotAvailable()
	{
		return notAvailable;
	}

	public void setNotAvailable(Boolean notAvailable)
	{
		this.notAvailable = notAvailable;
	}

	public String getSectionDescription()
	{
		return sectionDescription;
	}

	public void setSectionDescription(String sectionDescription)
	{
		this.sectionDescription = sectionDescription;
	}

}
