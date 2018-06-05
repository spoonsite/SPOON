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

import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormTemplate;
import edu.usu.sdl.openstorefront.core.entity.SubmissionTemplateStatus;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubmissionFormTemplateView
{

	private String submissionTemplateId;
	private String name;
	private String description;
	private String templateStatus;
	private String templateStatusLabel;
	private Boolean defaultTemplate;
	private String activeStatus;
	private String createUser;
	private Date createDts;
	private String updateUser;
	private Date updateDts;

	@DataType(SubmissionFormSectionView.class)
	private List<SubmissionFormSectionView> sections = new ArrayList<>();

	@SuppressWarnings({"squid:S1186"})
	public SubmissionFormTemplateView()
	{
	}

	public static SubmissionFormTemplateView toView(SubmissionFormTemplate template)
	{
		SubmissionFormTemplateView view = new SubmissionFormTemplateView();
		view.setSubmissionTemplateId(template.getSubmissionTemplateId());
		view.setName(template.getName());
		view.setDescription(template.getDescription());
		view.setTemplateStatus(template.getTemplateStatus());
		view.setTemplateStatusLabel(TranslateUtil.translate(SubmissionTemplateStatus.class, template.getTemplateStatus()));
		view.setActiveStatus(template.getActiveStatus());
		view.setCreateUser(template.getCreateUser());
		view.setCreateDts(template.getCreateDts());
		view.setUpdateUser(template.getUpdateUser());
		view.setUpdateDts(template.getUpdateDts());
		view.setDefaultTemplate(template.getDefaultTemplate());

		if (template.getSections() != null) {
			template.getSections().sort((a, b) -> {
				return a.getSectionOrder().compareTo(b.getSectionOrder());
			});

			template.getSections().forEach(section -> {
				view.getSections().add(SubmissionFormSectionView.toView(section));
			});

		}

		return view;
	}

	public static List<SubmissionFormTemplateView> toView(List<SubmissionFormTemplate> templates)
	{
		List<SubmissionFormTemplateView> views = new ArrayList<>();
		templates.forEach(template -> {
			views.add(toView(template));
		});
		return views;
	}

	public List<SubmissionFormSectionView> getSections()
	{
		return sections;
	}

	public void setSections(List<SubmissionFormSectionView> sections)
	{
		this.sections = sections;
	}

	public String getSubmissionTemplateId()
	{
		return submissionTemplateId;
	}

	public void setSubmissionTemplateId(String submissionTemplateId)
	{
		this.submissionTemplateId = submissionTemplateId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getTemplateStatus()
	{
		return templateStatus;
	}

	public void setTemplateStatus(String templateStatus)
	{
		this.templateStatus = templateStatus;
	}

	public String getTemplateStatusLabel()
	{
		return templateStatusLabel;
	}

	public void setTemplateStatusLabel(String templateStatusLabel)
	{
		this.templateStatusLabel = templateStatusLabel;
	}

	public String getActiveStatus()
	{
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus)
	{
		this.activeStatus = activeStatus;
	}

	public String getCreateUser()
	{
		return createUser;
	}

	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}

	public Date getCreateDts()
	{
		return createDts;
	}

	public void setCreateDts(Date createDts)
	{
		this.createDts = createDts;
	}

	public String getUpdateUser()
	{
		return updateUser;
	}

	public void setUpdateUser(String updateUser)
	{
		this.updateUser = updateUser;
	}

	public Date getUpdateDts()
	{
		return updateDts;
	}

	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	public Boolean getDefaultTemplate()
	{
		return defaultTemplate;
	}

	public void setDefaultTemplate(Boolean defaultTemplate)
	{
		this.defaultTemplate = defaultTemplate;
	}

}
