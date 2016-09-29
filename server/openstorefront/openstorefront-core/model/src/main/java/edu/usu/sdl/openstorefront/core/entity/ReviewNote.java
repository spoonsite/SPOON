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
import edu.usu.sdl.openstorefront.validation.BasicHTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Comment on a piece of information")
public class ReviewNote
		extends StandardEntity<ReviewNote>
{

	@PK(generated = true)
	@NotNull
	private String reviewId;

	@ConsumeField
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	@Sanitize(BasicHTMLSanitizer.class)
	private String comment;

	@NotNull
	@ConsumeField
	private Boolean acknowlege;

	@ConsumeField
	@FK(ReviewNote.class)
	private String replyReviewId;

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

	public ReviewNote()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		ReviewNote reviewNote = (ReviewNote) entity;

		setComment(reviewNote.getComment());
		setAcknowlege(reviewNote.getAcknowlege());
		setReplyReviewId(reviewNote.getReplyReviewId());
		setEntityId(reviewNote.getEntityId());
		setEntity(reviewNote.getEntity());

	}

	public String getReviewId()
	{
		return reviewId;
	}

	public void setReviewId(String reviewId)
	{
		this.reviewId = reviewId;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public Boolean getAcknowlege()
	{
		return acknowlege;
	}

	public void setAcknowlege(Boolean acknowlege)
	{
		this.acknowlege = acknowlege;
	}

	public String getReplyReviewId()
	{
		return replyReviewId;
	}

	public void setReplyReviewId(String replyReviewId)
	{
		this.replyReviewId = replyReviewId;
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

}
