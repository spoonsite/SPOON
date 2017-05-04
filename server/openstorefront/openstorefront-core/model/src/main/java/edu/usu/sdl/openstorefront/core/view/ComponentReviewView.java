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

import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewCon;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewConPk;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewPro;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewProPk;
import edu.usu.sdl.openstorefront.core.entity.ExperienceTimeType;
import edu.usu.sdl.openstorefront.core.entity.UserTypeCode;
import edu.usu.sdl.openstorefront.core.model.ReviewAll;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * Composite View of a Component Review
 *
 * @author dshurtleff
 */
public class ComponentReviewView
		extends StandardEntityView
{

	private String username;

	@NotNull
	@ConsumeField
	private String userTypeCode;

	@NotNull
	@ConsumeField
	private String comment;

	@NotNull
	@ConsumeField
	private int rating;

	@NotNull
	@ConsumeField
	private String title;

	@NotNull
	@ConsumeField
	private String userTimeCode;
	private String userTimeDescription;
	
	@NotNull
	@ConsumeField
	private Date lastUsed;
	private Date updateDate;

	@NotNull
	@ConsumeField
	private String organization;

	@ConsumeField
	private boolean recommend;
	private String componentId;
	private String reviewId;
	private String activeStatus;

	@NotNull
	@ConsumeField
	private String name;
	private Component component;

	@ConsumeField
	@DataType(ComponentReviewProCon.class)
	private List<ComponentReviewProCon> pros = new ArrayList<>();

	@ConsumeField
	@DataType(ComponentReviewProCon.class)
	private List<ComponentReviewProCon> cons = new ArrayList<>();

	public ComponentReviewView()
	{
	}

	public static ComponentReviewView toView(ComponentReview review)
	{
		return toView(review, null, null);
	}

	public static ComponentReviewView toView(ComponentReview review, List<ComponentReviewPro> pros, List<ComponentReviewCon> cons)
	{
		Service service = ServiceProxyFactory.getServiceProxy();
		ComponentReviewView view = new ComponentReviewView();
		view.setUsername(review.getCreateUser());
		view.setUserTypeCode(TranslateUtil.translate(UserTypeCode.class, review.getUserTypeCode()));
		view.setComment(review.getComment());
		view.setRating(review.getRating());
		view.setTitle(review.getTitle());
		view.setComponentId(review.getComponentId());
		view.setReviewId(review.getComponentReviewId());
		view.setName(service.getComponentService().getComponentName(review.getComponentId()));
		view.setUserTimeCode(review.getUserTimeCode());
		view.setUserTimeDescription(TranslateUtil.translate(ExperienceTimeType.class, review.getUserTimeCode()));

		if (pros == null) {
			ComponentReviewPro reviewProExample = new ComponentReviewPro();
			reviewProExample.setActiveStatus(ComponentReviewPro.ACTIVE_STATUS);
			ComponentReviewProPk reviewProPk = new ComponentReviewProPk();
			reviewProPk.setComponentReviewId(review.getComponentReviewId());
			reviewProExample.setComponentReviewProPk(reviewProPk);
			List<ComponentReviewPro> componentReviewPros = service.getPersistenceService().queryByExample(reviewProExample);
			for (ComponentReviewPro pro : componentReviewPros) {
				view.toStandardView(pro);
			}
			view.setPros(ComponentReviewProCon.toViewListPro(componentReviewPros));
		} else {
			view.setPros(ComponentReviewProCon.toViewListPro(pros));
		}

		if (cons == null) {
			ComponentReviewCon reviewConExample = new ComponentReviewCon();
			reviewConExample.setActiveStatus(ComponentReviewCon.ACTIVE_STATUS);
			ComponentReviewConPk reviewConPk = new ComponentReviewConPk();
			reviewConPk.setComponentReviewId(review.getComponentReviewId());
			reviewConExample.setComponentReviewConPk(reviewConPk);
			List<ComponentReviewCon> componentReviewCons = service.getPersistenceService().queryByExample(reviewConExample);
			for (ComponentReviewCon con : componentReviewCons) {
				view.toStandardView(con);
			}
			view.setCons(ComponentReviewProCon.toViewListCon(componentReviewCons));
		} else {
			view.setPros(ComponentReviewProCon.toViewListCon(cons));
		}

		view.setActiveStatus(review.getActiveStatus());
		view.setLastUsed(review.getLastUsed());
		view.setUpdateDate(review.getUpdateDts());
		view.setOrganization(review.getOrganization());
		view.setRecommend(review.getRecommend());
		view.toStandardView(review);

		return view;
	}

	public static List<ComponentReviewView> toViewList(List<ComponentReview> reviews)
	{
		List<ComponentReviewView> componentReviewViews = new ArrayList<>();
		reviews.forEach(review -> {
			componentReviewViews.add(toView(review));
		});
		return componentReviewViews;
	}

	public static List<ComponentReviewView> toViewListAll(List<ReviewAll> reviews)
	{
		List<ComponentReviewView> componentReviewViews = new ArrayList<>();
		reviews.forEach(review -> {
			componentReviewViews.add(toView(review.getComponentReview(), review.getPros(), review.getCons()));
		});
		return componentReviewViews;
	}

	public List<ComponentReviewProCon> getPros()
	{
		return pros;
	}

	public void setPros(List<ComponentReviewProCon> pros)
	{
		this.pros = pros;
	}

	public List<ComponentReviewProCon> getCons()
	{
		return cons;
	}

	public void setCons(List<ComponentReviewProCon> cons)
	{
		this.cons = cons;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getUserTypeCode()
	{
		return userTypeCode;
	}

	public void setUserTypeCode(String userTypeCode)
	{
		this.userTypeCode = userTypeCode;
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

	public String getUserTimeCode()
	{
		return userTimeCode;
	}

	public void setUserTimeCode(String usedTimeCode)
	{
		this.userTimeCode = usedTimeCode;
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

	public Component getComponent()
	{
		return component;
	}

	public void setComponent(Component component)
	{
		this.component = component;
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getReviewId()
	{
		return reviewId;
	}

	public void setReviewId(String reviewId)
	{
		this.reviewId = reviewId;
	}

	public String getActiveStatus()
	{
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus)
	{
		this.activeStatus = activeStatus;
	}

	public String getUserTimeDescription()
	{
		return userTimeDescription;
	}

	public void setUserTimeDescription(String userTimeDescription)
	{
		this.userTimeDescription = userTimeDescription;
	}

}
