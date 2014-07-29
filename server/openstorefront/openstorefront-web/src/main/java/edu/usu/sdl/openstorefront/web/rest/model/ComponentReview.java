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

package edu.usu.sdl.openstorefront.web.rest.model;

import edu.usu.sdl.openstorefront.doc.DataType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ComponentReview
{
	private String username;
	private String userType;
	private String comment;
	private int rating;
	private String title;
	private String usedTimeCode;
	private Date lastUsed;	
	private Date updateDate;
	private String organization;
	private boolean recommend;
	
	@DataType(ComponentTag.class)
	private List<ComponentTag> pros = new ArrayList<>();
	
	@DataType(ComponentTag.class)
	private List<ComponentTag> cons = new ArrayList<>();

	public ComponentReview()
	{
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public int getRating()
	{
		return rating;
	}

	public void setRating(int rating)
	{
		this.rating = rating;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getUsedTimeCode()
	{
		return usedTimeCode;
	}

	public void setUsedTimeCode(String usedTimeCode)
	{
		this.usedTimeCode = usedTimeCode;
	}

	public Date getLastUsed()
	{
		return lastUsed;
	}

	public void setLastUsed(Date lastUsed)
	{
		this.lastUsed = lastUsed;
	}

	public Date getUpdateDate()
	{
		return updateDate;
	}

	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
	}

	public String getOrganization()
	{
		return organization;
	}

	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	public boolean isRecommend()
	{
		return recommend;
	}

	public void setRecommend(boolean recommend)
	{
		this.recommend = recommend;
	}

	public String getUserType()
	{
		return userType;
	}

	public void setUserType(String userType)
	{
		this.userType = userType;
	}

	public List<ComponentTag> getPros()
	{
		return pros;
	}

	public void setPros(List<ComponentTag> pros)
	{
		this.pros = pros;
	}

	public List<ComponentTag> getCons()
	{
		return cons;
	}

	public void setCons(List<ComponentTag> cons)
	{
		this.cons = cons;
	}
	
}
