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
package edu.usu.sdl.openstorefront.service.model;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.entity.BaseComponent;

/**
 *
 * @author bmichaelis
 */
@APIDescription("Model to facilitate sending emails to recipiants")
public class EmailCommentModel
		extends BaseComponent<EmailCommentModel>
{
	
	private String comment;
	
	private String author;
	
	private String entryName;
	
	private String currentStep;
	
	private String replyInstructions;
	
	private String entryType;
	
	private String changeRequestInformation;
	
	private String commentEntityId;
	
	private boolean privateComment;
	
	private boolean adminComment;

	@Override
	public String uniqueKey()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected void customKeyClear()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public String getEntryName()
	{
		return entryName;
	}

	public void setEntryName(String entryName)
	{
		this.entryName = entryName;
	}

	public String getCurrentStep()
	{
		return currentStep;
	}

	public void setCurrentStep(String currentStep)
	{
		this.currentStep = currentStep;
	}

	public String getReplyInstructions()
	{
		return replyInstructions;
	}

	public void setReplyInstructions(String replyInstructions)
	{
		this.replyInstructions = replyInstructions;
	}

	public String getEntryType()
	{
		return entryType;
	}

	public void setEntryType(String entryType)
	{
		this.entryType = entryType;
	}

	public String getChangeRequestInformation()
	{
		return changeRequestInformation;
	}

	public void setChangeRequestInformation(String changeRequestInformation)
	{
		this.changeRequestInformation = changeRequestInformation;
	}

	public boolean isPrivateComment()
	{
		return privateComment;
	}

	public void setPrivateComment(boolean privateComment)
	{
		this.privateComment = privateComment;
	}

	public boolean isAdminComment()
	{
		return adminComment;
	}

	public void setAdminComment(boolean adminComment)
	{
		this.adminComment = adminComment;
	}

	public String getCommentEntityId()
	{
		return commentEntityId;
	}

	public void setCommentEntityId(String commentEntityId)
	{
		this.commentEntityId = commentEntityId;
	}

}
