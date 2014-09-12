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
package edu.usu.sdl.openstorefront.service.transfermodel;

import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttribute;
import edu.usu.sdl.openstorefront.storage.model.ComponentContact;
import edu.usu.sdl.openstorefront.storage.model.ComponentEvaluationSchedule;
import edu.usu.sdl.openstorefront.storage.model.ComponentEvaluationSection;
import edu.usu.sdl.openstorefront.storage.model.ComponentExternalDependency;
import edu.usu.sdl.openstorefront.storage.model.ComponentMedia;
import edu.usu.sdl.openstorefront.storage.model.ComponentMetadata;
import edu.usu.sdl.openstorefront.storage.model.ComponentResource;
import edu.usu.sdl.openstorefront.storage.model.ComponentTag;
import java.util.ArrayList;
import java.util.List;

/**
 * This is full composite Model of the component
 *
 * @author dshurtleff
 */
public class ComponentAll
{

	private Component component;
	private List<ComponentAttribute> attributes = new ArrayList<>();
	private List<ComponentContact> contacts = new ArrayList<>();
	private List<ComponentEvaluationSchedule> evaluationSchedules = new ArrayList<>();
	private List<ComponentEvaluationSection> evaluationSections = new ArrayList<>();
	private List<ComponentExternalDependency> externalDependencies = new ArrayList<>();
	private List<ComponentMedia> media = new ArrayList<>();
	private List<ComponentMetadata> metadata = new ArrayList<>();
	private List<QuestionAll> questions = new ArrayList<>();
	private List<ComponentResource> resources = new ArrayList<>();
	private List<ReviewAll> reviews = new ArrayList<>();
	private List<ComponentTag> tags = new ArrayList<>();

	public ComponentAll()
	{
	}

	public Component getComponent()
	{
		return component;
	}

	public void setComponent(Component component)
	{
		this.component = component;
	}

	public List<ComponentAttribute> getAttributes()
	{
		return attributes;
	}

	public void setAttributes(List<ComponentAttribute> attributes)
	{
		this.attributes = attributes;
	}

	public List<ComponentContact> getContacts()
	{
		return contacts;
	}

	public void setContacts(List<ComponentContact> contacts)
	{
		this.contacts = contacts;
	}

	public List<ComponentEvaluationSchedule> getEvaluationSchedules()
	{
		return evaluationSchedules;
	}

	public void setEvaluationSchedules(List<ComponentEvaluationSchedule> evaluationSchedules)
	{
		this.evaluationSchedules = evaluationSchedules;
	}

	public List<ComponentEvaluationSection> getEvaluationSections()
	{
		return evaluationSections;
	}

	public void setEvaluationSections(List<ComponentEvaluationSection> evaluationSections)
	{
		this.evaluationSections = evaluationSections;
	}

	public List<ComponentExternalDependency> getExternalDependencies()
	{
		return externalDependencies;
	}

	public void setExternalDependencies(List<ComponentExternalDependency> externalDependencies)
	{
		this.externalDependencies = externalDependencies;
	}

	public List<ComponentMedia> getMedia()
	{
		return media;
	}

	public void setMedia(List<ComponentMedia> media)
	{
		this.media = media;
	}

	public List<ComponentMetadata> getMetadata()
	{
		return metadata;
	}

	public void setMetadata(List<ComponentMetadata> metadata)
	{
		this.metadata = metadata;
	}

	public List<QuestionAll> getQuestions()
	{
		return questions;
	}

	public void setQuestions(List<QuestionAll> questions)
	{
		this.questions = questions;
	}

	public List<ComponentResource> getResources()
	{
		return resources;
	}

	public void setResources(List<ComponentResource> resources)
	{
		this.resources = resources;
	}

	public List<ComponentTag> getTags()
	{
		return tags;
	}

	public void setTags(List<ComponentTag> tags)
	{
		this.tags = tags;
	}

	public List<ReviewAll> getReviews()
	{
		return reviews;
	}

	public void setReviews(List<ReviewAll> reviews)
	{
		this.reviews = reviews;
	}

}
