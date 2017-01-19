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
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Comment on a part of the evaluation")
public class EvaluationComment
		extends StandardEntity<EvaluationComment>
{

	@PK(generated = true)
	@NotNull
	private String commentId;

	@FK(Evaluation.class)
	@NotNull
	private String evaluationId;

	@ConsumeField
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	@Sanitize(HTMLSanitizer.class)
	private String comment;

	@NotNull
	@ConsumeField
	private Boolean acknowledge;

	@ConsumeField
	@FK(EvaluationComment.class)
	private String replyCommentId;

	@ConsumeField
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(TextSanitizer.class)
	private String entityId;

	@ConsumeField
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(TextSanitizer.class)
	private String entity;

	public EvaluationComment()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		EvaluationComment reviewNote = (EvaluationComment) entity;

		setEvaluationId(reviewNote.getEvaluationId());
		setComment(reviewNote.getComment());
		setAcknowledge(reviewNote.getAcknowledge());
		setReplyCommentId(reviewNote.getReplyCommentId());
		setEntityId(reviewNote.getEntityId());
		setEntity(reviewNote.getEntity());

	}

	public String getCommentId()
	{
		return commentId;
	}

	public void setCommentId(String commentId)
	{
		this.commentId = commentId;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public Boolean getAcknowledge()
	{
		return acknowledge;
	}

	public void setAcknowledge(Boolean acknowledge)
	{
		this.acknowledge = acknowledge;
	}

	public String getReplyCommentId()
	{
		return replyCommentId;
	}

	public void setReplyCommentId(String replyCommentId)
	{
		this.replyCommentId = replyCommentId;
	}

	public String getEntityId()
	{
		return entityId;
	}

	public void setEntityId(String entityId)
	{
		this.entityId = entityId;
	}

	public String getEntity()
	{
		return entity;
	}

	public void setEntity(String entity)
	{
		this.entity = entity;
	}

	public String getEvaluationId()
	{
		return evaluationId;
	}

	public void setEvaluationId(String evaluationId)
	{
		this.evaluationId = evaluationId;
	}

}
