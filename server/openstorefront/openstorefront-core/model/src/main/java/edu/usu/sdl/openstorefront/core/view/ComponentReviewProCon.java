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
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewCon;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewPro;
import edu.usu.sdl.openstorefront.core.entity.ReviewCon;
import edu.usu.sdl.openstorefront.core.entity.ReviewPro;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jlaw
 */
public class ComponentReviewProCon
		extends StandardEntityView
{

	@NotNull
	@ConsumeField
	private String text;
	private String code;
	private Date updateDts;
	private String componentId;
	private String reviewId;

	public static ComponentReviewProCon toView(ComponentReviewPro pro)
	{
		ComponentReviewProCon view = new ComponentReviewProCon();
		view.setComponentId(pro.getComponentId());
		view.setUpdateDts(pro.getUpdateDts());
		view.setText(TranslateUtil.translate(ReviewPro.class, pro.getComponentReviewProPk().getReviewPro()));
		view.setCode(pro.getComponentReviewProPk().getReviewPro());
		view.setReviewId(pro.getComponentReviewProPk().getComponentReviewId());
		view.toStandardView(pro);

		return view;
	}

	public static ComponentReviewProCon toView(ComponentReviewCon con)
	{
		ComponentReviewProCon view = new ComponentReviewProCon();
		view.setComponentId(con.getComponentId());
		view.setUpdateDts(con.getUpdateDts());
		view.setText(TranslateUtil.translate(ReviewCon.class, con.getComponentReviewConPk().getReviewCon()));
		view.setCode(con.getComponentReviewConPk().getReviewCon());
		view.setReviewId(con.getComponentReviewConPk().getComponentReviewId());
		view.toStandardView(con);
		return view;
	}

	public static List<ComponentReviewProCon> toViewListPro(List<ComponentReviewPro> pros)
	{
		List<ComponentReviewProCon> views = new ArrayList<>();
		pros.stream().forEach((pro) -> {
			views.add(ComponentReviewProCon.toView(pro));
		});
		return views;
	}

	public static List<ComponentReviewProCon> toViewListCon(List<ComponentReviewCon> cons)
	{
		List<ComponentReviewProCon> views = new ArrayList<>();
		cons.stream().forEach((con) -> {
			views.add(ComponentReviewProCon.toView(con));
		});
		return views;
	}

	/**
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text)
	{
		this.text = text;
	}

	/**
	 * @return the updateDts
	 */
	public Date getUpdateDts()
	{
		return updateDts;
	}

	/**
	 * @param updateDts the updateDts to set
	 */
	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	/**
	 * @return the componentId
	 */
	public String getComponentId()
	{
		return componentId;
	}

	/**
	 * @param componentId the componentId to set
	 */
	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	/**
	 * @return the reviewId
	 */
	public String getReviewId()
	{
		return reviewId;
	}

	/**
	 * @param reviewId the reviewId to set
	 */
	public void setReviewId(String reviewId)
	{
		this.reviewId = reviewId;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}
}
