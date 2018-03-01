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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.validation.BasicHTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Comment are use internal management and communication with owners")
public class ComponentComment
		extends BaseComponent<ComponentComment>
{

	@PK(generated = true)
	private String commentId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = ComponentCommentType.class)
	@FK(ComponentCommentType.class)
	private String commentType;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	@Sanitize(BasicHTMLSanitizer.class)
	private String comment;

	@ConsumeField
	private String parentCommentId;

	@ConsumeField
	@APIDescription("Private comments are not shared with owners")
	private Boolean privateComment;

	@SuppressWarnings({"squid:S1186"})
	public ComponentComment()
	{
	}

	@Override
	public String uniqueKey()
	{
		return getCommentId();
	}

	@Override
	protected void customKeyClear()
	{
		setCommentId(null);
	}

	public String getCommentId()
	{
		return commentId;
	}

	public void setCommentId(String commentId)
	{
		this.commentId = commentId;
	}

	public String getCommentType()
	{
		return commentType;
	}

	public void setCommentType(String commentType)
	{
		this.commentType = commentType;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public Boolean getPrivateComment()
	{
		return privateComment;
	}

	public void setPrivateComment(Boolean privateComment)
	{
		this.privateComment = privateComment;
	}

	public String getParentCommentId()
	{
		return parentCommentId;
	}

	public void setParentCommentId(String parentCommentId)
	{
		this.parentCommentId = parentCommentId;
	}

}
