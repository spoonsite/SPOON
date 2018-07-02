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

import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.ContentSection;
import edu.usu.sdl.openstorefront.core.entity.ContentSectionTemplate;
import edu.usu.sdl.openstorefront.core.entity.ContentSubSection;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ContentSectionTemplateView
{

	@ConsumeField
	private ContentSectionTemplate contentSectionTemplate;

	@ConsumeField
	private ContentSection contentSection;

	@ConsumeField
	@DataType(ContentSubSection.class)
	private List<ContentSubSection> subSections = new ArrayList<>();

	public ValidationResult validate()
	{
		ValidationResult validationResult = new ValidationResult();
		ValidationModel validationModel = new ValidationModel(contentSectionTemplate);
		validationModel.setConsumeFieldsOnly(true);
		validationModel.setAcceptNull(false);
		validationResult.merge(ValidationUtil.validate(validationModel));

		validationModel = new ValidationModel(contentSection);
		validationModel.setConsumeFieldsOnly(true);
		validationModel.setAcceptNull(false);
		validationResult.merge(ValidationUtil.validate(validationModel));

		for (ContentSubSection subSection : subSections) {
			validationResult.merge(subSection.validate());
		}
		return validationResult;
	}

	public static ContentSectionTemplateView toView(ContentSectionTemplate template)
	{
		ContentSectionTemplateView view = new ContentSectionTemplateView();
		view.setContentSectionTemplate(template);

		ContentSection contentSectionExample = new ContentSection();
		contentSectionExample.setEntity(ContentSectionTemplate.class.getSimpleName());
		contentSectionExample.setEntityId(template.getTemplateId());

		view.setContentSection(contentSectionExample.find());

		ContentSubSection contentSubSectionExample = new ContentSubSection();
		contentSubSectionExample.setActiveStatus(ContentSubSection.ACTIVE_STATUS);
		contentSubSectionExample.setContentSectionId(view.getContentSection().getContentSectionId());
		view.setSubSections(contentSubSectionExample.findByExample());

		return view;
	}

	public ContentSectionTemplate getContentSectionTemplate()
	{
		return contentSectionTemplate;
	}

	public void setContentSectionTemplate(ContentSectionTemplate contentSectionTemplate)
	{
		this.contentSectionTemplate = contentSectionTemplate;
	}

	public ContentSection getContentSection()
	{
		return contentSection;
	}

	public void setContentSection(ContentSection contentSection)
	{
		this.contentSection = contentSection;
	}

	public List<ContentSubSection> getSubSections()
	{
		return subSections;
	}

	public void setSubSections(List<ContentSubSection> subSections)
	{
		this.subSections = subSections;
	}

}
