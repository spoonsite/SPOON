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
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.validation.CleanKeySanitizer;
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Allows for dynamic changing of the application look and feel")
public class Branding
		extends StandardEntity<Branding>
{

	@PK(generated = true)
	@NotNull
	private String brandingId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_COMPONENT_NAME)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String name;

	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String primaryLogoUrl;

	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String secondaryLogoUrl;

	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String loginWarning;

	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String applicationName;

	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String landingPageTitle;

	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private String landingPageBanner;

	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_64K)
	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private String landingPageFooter;

	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@Sanitize(CleanKeySanitizer.class)
	@ConsumeField
	private String archtectureSearchType;

	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String archtectureSearchLabel;

	@ConsumeField
	private Boolean allowSecurityMarkingsFlg;

	@ConsumeField
	private Boolean allowJiraFeedbackFlg;

	@ConsumeField
	private Boolean showComponentTypeSearchFlg;

	public Branding()
	{
	}

	@Override
	public void updateFields(StandardEntity entity)
	{
		super.updateFields(entity);

		Branding branding = (Branding) entity;

		setAllowJiraFeedbackFlg(branding.getAllowJiraFeedbackFlg());
		setAllowSecurityMarkingsFlg(branding.getAllowSecurityMarkingsFlg());
		setApplicationName(branding.getApplicationName());
		setArchtectureSearchLabel(branding.getArchtectureSearchLabel());
		setArchtectureSearchType(branding.getArchtectureSearchType());
		setLandingPageBanner(branding.getLandingPageBanner());
		setLandingPageFooter(branding.getLandingPageFooter());
		setLandingPageTitle(branding.getLandingPageTitle());
		setLoginWarning(branding.getLoginWarning());
		setName(branding.getName());
		setPrimaryLogoUrl(branding.getPrimaryLogoUrl());
		setSecondaryLogoUrl(branding.getSecondaryLogoUrl());
		setShowComponentTypeSearchFlg(branding.getShowComponentTypeSearchFlg());

	}

	public String getBrandingId()
	{
		return brandingId;
	}

	public void setBrandingId(String brandingId)
	{
		this.brandingId = brandingId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPrimaryLogoUrl()
	{
		return primaryLogoUrl;
	}

	public void setPrimaryLogoUrl(String primaryLogoUrl)
	{
		this.primaryLogoUrl = primaryLogoUrl;
	}

	public String getSecondaryLogoUrl()
	{
		return secondaryLogoUrl;
	}

	public void setSecondaryLogoUrl(String secondaryLogoUrl)
	{
		this.secondaryLogoUrl = secondaryLogoUrl;
	}

	public String getLoginWarning()
	{
		return loginWarning;
	}

	public void setLoginWarning(String loginWarning)
	{
		this.loginWarning = loginWarning;
	}

	public String getApplicationName()
	{
		return applicationName;
	}

	public void setApplicationName(String applicationName)
	{
		this.applicationName = applicationName;
	}

	public String getLandingPageTitle()
	{
		return landingPageTitle;
	}

	public void setLandingPageTitle(String landingPageTitle)
	{
		this.landingPageTitle = landingPageTitle;
	}

	public String getLandingPageBanner()
	{
		return landingPageBanner;
	}

	public void setLandingPageBanner(String landingPageBanner)
	{
		this.landingPageBanner = landingPageBanner;
	}

	public String getLandingPageFooter()
	{
		return landingPageFooter;
	}

	public void setLandingPageFooter(String landingPageFooter)
	{
		this.landingPageFooter = landingPageFooter;
	}

	public String getArchtectureSearchType()
	{
		return archtectureSearchType;
	}

	public void setArchtectureSearchType(String archtectureSearchType)
	{
		this.archtectureSearchType = archtectureSearchType;
	}

	public String getArchtectureSearchLabel()
	{
		return archtectureSearchLabel;
	}

	public void setArchtectureSearchLabel(String archtectureSearchLabel)
	{
		this.archtectureSearchLabel = archtectureSearchLabel;
	}

	public Boolean getShowComponentTypeSearchFlg()
	{
		return showComponentTypeSearchFlg;
	}

	public void setShowComponentTypeSearchFlg(Boolean showComponentTypeSearchFlg)
	{
		this.showComponentTypeSearchFlg = showComponentTypeSearchFlg;
	}

	public Boolean getAllowSecurityMarkingsFlg()
	{
		return allowSecurityMarkingsFlg;
	}

	public void setAllowSecurityMarkingsFlg(Boolean allowSecurityMarkingsFlg)
	{
		this.allowSecurityMarkingsFlg = allowSecurityMarkingsFlg;
	}

	public Boolean getAllowJiraFeedbackFlg()
	{
		return allowJiraFeedbackFlg;
	}

	public void setAllowJiraFeedbackFlg(Boolean allowJiraFeedbackFlg)
	{
		this.allowJiraFeedbackFlg = allowJiraFeedbackFlg;
	}

}
