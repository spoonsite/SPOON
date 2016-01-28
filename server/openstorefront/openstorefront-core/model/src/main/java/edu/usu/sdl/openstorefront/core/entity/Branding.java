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

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String primaryLogoUrl;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String secondaryLogoUrl;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_16K)
	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private String loginWarning;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String applicationName;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private String landingPageTitle;
	
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private String landingStatsText;	

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private String landingPageBanner;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_64K)
	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private String landingPageFooter;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@Sanitize(CleanKeySanitizer.class)
	@ConsumeField
	private String archtectureSearchType;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String archtectureSearchLabel;

	@ConsumeField
	private Boolean allowSecurityMarkingsFlg;

	@ConsumeField
	private Boolean allowJiraFeedbackFlg;

	@ConsumeField
	private Boolean showComponentTypeSearchFlg;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookTextColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookSiteBackgroundColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookLandingInfoSectionColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookHomeFooterColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookButtonDefaultColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookButtonPrimaryColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookButtonInfoColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookButtonSucessColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookButtonWarningColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookButtonDangerColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookNavbarColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookNavbarTextColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookLogoTextColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookAccentColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookBannerColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookFocusColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookTabColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookTabTextColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookTabInactiveColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookTabInactiveTextColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookEntryNameColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookEntryToolColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookEntrySectionColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookEntryEvalutionColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String lookRatingColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_1MB)
	private String lookCustomOverrides;

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
		setLandingStatsText(branding.getLandingStatsText());		
		setLoginWarning(branding.getLoginWarning());
		setName(branding.getName());
		setPrimaryLogoUrl(branding.getPrimaryLogoUrl());
		setSecondaryLogoUrl(branding.getSecondaryLogoUrl());
		setShowComponentTypeSearchFlg(branding.getShowComponentTypeSearchFlg());

		setLookAccentColor(branding.getLookAccentColor());
		setLookBannerColor(branding.getLookBannerColor());
		setLookButtonDangerColor(branding.getLookButtonDangerColor());
		setLookButtonDefaultColor(branding.getLookButtonDefaultColor());
		setLookButtonInfoColor(branding.getLookButtonInfoColor());
		setLookButtonPrimaryColor(branding.getLookButtonPrimaryColor());
		setLookButtonWarningColor(branding.getLookButtonWarningColor());
		setLookButtonSucessColor(branding.getLookButtonSucessColor());
		setLookCustomOverrides(branding.getLookCustomOverrides());
		setLookEntryEvalutionColor(branding.getLookEntryEvalutionColor());
		setLookEntryNameColor(branding.getLookEntryNameColor());
		setLookEntrySectionColor(branding.getLookEntrySectionColor());
		setLookEntryToolColor(branding.getLookEntryToolColor());
		setLookHomeFooterColor(branding.getLookHomeFooterColor());
		setLookLandingInfoSectionColor(branding.getLookLandingInfoSectionColor());
		setLookLogoTextColor(branding.getLookLogoTextColor());
		setLookNavbarColor(branding.getLookNavbarColor());
		setLookNavbarTextColor(branding.getLookNavbarTextColor());
		setLookRatingColor(branding.getLookRatingColor());
		setLookSiteBackgroundColor(branding.getLookSiteBackgroundColor());
		setLookTabColor(branding.getLookTabColor());
		setLookTabInactiveColor(branding.getLookTabInactiveColor());
		setLookTabInactiveTextColor(branding.getLookTabInactiveTextColor());
		setLookTabTextColor(branding.getLookTabTextColor());
		setLookTextColor(branding.getLookTextColor());

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

	public String getLookTextColor()
	{
		return lookTextColor;
	}

	public void setLookTextColor(String lookTextColor)
	{
		this.lookTextColor = lookTextColor;
	}

	public String getLookSiteBackgroundColor()
	{
		return lookSiteBackgroundColor;
	}

	public void setLookSiteBackgroundColor(String lookSiteBackgroundColor)
	{
		this.lookSiteBackgroundColor = lookSiteBackgroundColor;
	}

	public String getLookLandingInfoSectionColor()
	{
		return lookLandingInfoSectionColor;
	}

	public void setLookLandingInfoSectionColor(String lookLandingInfoSectionColor)
	{
		this.lookLandingInfoSectionColor = lookLandingInfoSectionColor;
	}

	public String getLookHomeFooterColor()
	{
		return lookHomeFooterColor;
	}

	public void setLookHomeFooterColor(String lookHomeFooterColor)
	{
		this.lookHomeFooterColor = lookHomeFooterColor;
	}

	public String getLookButtonDefaultColor()
	{
		return lookButtonDefaultColor;
	}

	public void setLookButtonDefaultColor(String lookButtonDefaultColor)
	{
		this.lookButtonDefaultColor = lookButtonDefaultColor;
	}

	public String getLookButtonPrimaryColor()
	{
		return lookButtonPrimaryColor;
	}

	public void setLookButtonPrimaryColor(String lookButtonPrimaryColor)
	{
		this.lookButtonPrimaryColor = lookButtonPrimaryColor;
	}

	public String getLookButtonInfoColor()
	{
		return lookButtonInfoColor;
	}

	public void setLookButtonInfoColor(String lookButtonInfoColor)
	{
		this.lookButtonInfoColor = lookButtonInfoColor;
	}

	public String getLookButtonWarningColor()
	{
		return lookButtonWarningColor;
	}

	public void setLookButtonWarningColor(String lookButtonWarningColor)
	{
		this.lookButtonWarningColor = lookButtonWarningColor;
	}

	public String getLookButtonDangerColor()
	{
		return lookButtonDangerColor;
	}

	public void setLookButtonDangerColor(String lookButtonDangerColor)
	{
		this.lookButtonDangerColor = lookButtonDangerColor;
	}

	public String getLookNavbarColor()
	{
		return lookNavbarColor;
	}

	public void setLookNavbarColor(String lookNavbarColor)
	{
		this.lookNavbarColor = lookNavbarColor;
	}

	public String getLookNavbarTextColor()
	{
		return lookNavbarTextColor;
	}

	public void setLookNavbarTextColor(String lookNavbarTextColor)
	{
		this.lookNavbarTextColor = lookNavbarTextColor;
	}

	public String getLookLogoTextColor()
	{
		return lookLogoTextColor;
	}

	public void setLookLogoTextColor(String lookLogoTextColor)
	{
		this.lookLogoTextColor = lookLogoTextColor;
	}

	public String getLookAccentColor()
	{
		return lookAccentColor;
	}

	public void setLookAccentColor(String lookAccentColor)
	{
		this.lookAccentColor = lookAccentColor;
	}

	public String getLookBannerColor()
	{
		return lookBannerColor;
	}

	public void setLookBannerColor(String lookBannerColor)
	{
		this.lookBannerColor = lookBannerColor;
	}

	public String getLookFocusColor()
	{
		return lookFocusColor;
	}

	public void setLookFocusColor(String lookFocusColor)
	{
		this.lookFocusColor = lookFocusColor;
	}

	public String getLookTabColor()
	{
		return lookTabColor;
	}

	public void setLookTabColor(String lookTabColor)
	{
		this.lookTabColor = lookTabColor;
	}

	public String getLookTabTextColor()
	{
		return lookTabTextColor;
	}

	public void setLookTabTextColor(String lookTabTextColor)
	{
		this.lookTabTextColor = lookTabTextColor;
	}

	public String getLookTabInactiveColor()
	{
		return lookTabInactiveColor;
	}

	public void setLookTabInactiveColor(String lookTabInactiveColor)
	{
		this.lookTabInactiveColor = lookTabInactiveColor;
	}

	public String getLookTabInactiveTextColor()
	{
		return lookTabInactiveTextColor;
	}

	public void setLookTabInactiveTextColor(String lookTabInactiveTextColor)
	{
		this.lookTabInactiveTextColor = lookTabInactiveTextColor;
	}

	public String getLookEntryNameColor()
	{
		return lookEntryNameColor;
	}

	public void setLookEntryNameColor(String lookEntryNameColor)
	{
		this.lookEntryNameColor = lookEntryNameColor;
	}

	public String getLookEntryToolColor()
	{
		return lookEntryToolColor;
	}

	public void setLookEntryToolColor(String lookEntryToolColor)
	{
		this.lookEntryToolColor = lookEntryToolColor;
	}

	public String getLookEntrySectionColor()
	{
		return lookEntrySectionColor;
	}

	public void setLookEntrySectionColor(String lookEntrySectionColor)
	{
		this.lookEntrySectionColor = lookEntrySectionColor;
	}

	public String getLookEntryEvalutionColor()
	{
		return lookEntryEvalutionColor;
	}

	public void setLookEntryEvalutionColor(String lookEntryEvalutionColor)
	{
		this.lookEntryEvalutionColor = lookEntryEvalutionColor;
	}

	public String getLookRatingColor()
	{
		return lookRatingColor;
	}

	public void setLookRatingColor(String lookRatingColor)
	{
		this.lookRatingColor = lookRatingColor;
	}

	public String getLookCustomOverrides()
	{
		return lookCustomOverrides;
	}

	public void setLookCustomOverrides(String lookCustomOverrides)
	{
		this.lookCustomOverrides = lookCustomOverrides;
	}

	public String getLookButtonSucessColor()
	{
		return lookButtonSucessColor;
	}

	public void setLookButtonSucessColor(String lookButtonSucessColor)
	{
		this.lookButtonSucessColor = lookButtonSucessColor;
	}

	public String getLandingStatsText()
	{
		return landingStatsText;
	}

	public void setLandingStatsText(String landingStatsText)
	{
		this.landingStatsText = landingStatsText;
	}

}
