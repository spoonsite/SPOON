/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@APIDescription("Holds a highlight for the homepage")
public class Highlight
		extends StandardEntity<Highlight>
{

	@PK(generated = true)
	@NotNull
	private String highlightId;

	@NotNull
	@ConsumeField
	@ValidValueType(value = {Highlight.TYPE_ARTICLE, Highlight.TYPE_COMPONENT, Highlight.TYPE_EXTERNAL_LINK}, lookupClass = HighlightType.class)
	@FK(HighlightType.class)
	private String highlightType;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	private String title;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_64K)
	@Sanitize(HTMLSanitizer.class)
	private String description;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_URL)
	@APIDescription("External URL link")
	private String link;

	@NotNull
	@ConsumeField
	@APIDescription("Position when ordered")
	private Integer orderingPosition;

	public static final String FIELD_TITLE = "title";
	public static final String TYPE_COMPONENT = "C";
	public static final String TYPE_ARTICLE = "A";
	public static final String TYPE_EXTERNAL_LINK = "EL";

	public Highlight()
	{
	}

	public boolean hasChange(Highlight newHighlight)
	{
		boolean changed = false;

		if (getDescription().compareTo(newHighlight.getDescription()) != 0) {
			changed = true;
		} else if (getHighlightType().compareTo(newHighlight.getHighlightType()) != 0) {
			changed = true;
		} else if ((getLink() != null
				&& newHighlight.getLink() != null
				&& getLink().compareTo(newHighlight.getLink()) != 0)) {
			changed = true;
		} else if (getLink() != null && newHighlight.getLink() == null) {
			changed = true;
		} else if (getLink() == null && newHighlight.getLink() != null) {
			changed = true;
		} else if (getTitle().compareTo(newHighlight.getTitle()) != 0) {
			changed = true;
		}

		return changed;
	}

	@Override
	public void updateFields(StandardEntity entity)
	{
		super.updateFields(entity);

		Highlight highlight = (Highlight) entity;

		this.setDescription(highlight.getDescription());
		this.setHighlightType(highlight.getHighlightType());
		this.setLink(highlight.getLink());
		this.setTitle(highlight.getTitle());
		this.setOrderingPosition(highlight.getOrderingPosition());

	}

	public String getHighlightId()
	{
		return highlightId;
	}

	public void setHighlightId(String highlightId)
	{
		this.highlightId = highlightId;
	}

	public String getHighlightType()
	{
		return highlightType;
	}

	public void setHighlightType(String highlightType)
	{
		this.highlightType = highlightType;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public Integer getOrderingPosition()
	{
		return orderingPosition;
	}

	public void setOrderingPosition(Integer orderingPosition)
	{
		this.orderingPosition = orderingPosition;
	}

}
