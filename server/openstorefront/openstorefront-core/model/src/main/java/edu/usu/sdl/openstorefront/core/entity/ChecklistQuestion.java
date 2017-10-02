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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Represents and evaluation checklist question")
public class ChecklistQuestion
		extends StandardEntity<ChecklistQuestion>
{

	public static final String FIELD_QID = "qid";

	@PK(generated = true)
	@NotNull
	private String questionId;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_60)
	private String qid;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	@Sanitize(HTMLSanitizer.class)
	private String question;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_16K)
	@Sanitize(HTMLSanitizer.class)
	private String narrative;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_16K)
	@Sanitize(HTMLSanitizer.class)
	private String objective;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	@Sanitize(HTMLSanitizer.class)
	private String scoreCriteria;

	@NotNull
	@ConsumeField
	@ValidValueType(value = {}, lookupClass = EvaluationSection.class)
	@FK(EvaluationSection.class)
	private String evaluationSection;

	@DataType(Tag.class)
	@ConsumeField
	@Embedded
	@OneToMany(orphanRemoval = true)
	private List<Tag> tags;

	public ChecklistQuestion()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		ChecklistQuestion checklistQuestion = (ChecklistQuestion) entity;
		setQid(checklistQuestion.getQid());
		setEvaluationSection(checklistQuestion.getEvaluationSection());
		setNarrative(checklistQuestion.getNarrative());
		setObjective(checklistQuestion.getObjective());
		setQuestion(checklistQuestion.getQuestion());
		setScoreCriteria(checklistQuestion.getScoreCriteria());
		setTags(checklistQuestion.getTags());

	}

	public String getQuestionId()
	{
		return questionId;
	}

	public void setQuestionId(String questionId)
	{
		this.questionId = questionId;
	}

	public String getQid()
	{
		return qid;
	}

	public void setQid(String qid)
	{
		this.qid = qid;
	}

	public String getQuestion()
	{
		return question;
	}

	public void setQuestion(String question)
	{
		this.question = question;
	}

	public String getNarrative()
	{
		return narrative;
	}

	public void setNarrative(String narrative)
	{
		this.narrative = narrative;
	}

	public String getObjective()
	{
		return objective;
	}

	public void setObjective(String objective)
	{
		this.objective = objective;
	}

	public String getScoreCriteria()
	{
		return scoreCriteria;
	}

	public void setScoreCriteria(String scoreCriteria)
	{
		this.scoreCriteria = scoreCriteria;
	}

	public String getEvaluationSection()
	{
		return evaluationSection;
	}

	public void setEvaluationSection(String evaluationSection)
	{
		this.evaluationSection = evaluationSection;
	}

	public void setChecklistQuestion(String questionId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public List<Tag> getTags()
	{
		return tags;
	}

	public void setTags(List<Tag> tags)
	{
		this.tags = tags;
	}

}
