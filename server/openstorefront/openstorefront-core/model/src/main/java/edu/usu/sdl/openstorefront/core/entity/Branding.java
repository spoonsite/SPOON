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
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
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

	private static final long serialVersionUID = 1L;

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

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String homebackSplashUrl;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_16K)
	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private String loginFooter;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_16K)
	@ConsumeField
	private String loginContentBlock;



	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String loginOverviewVideoUrl;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String loginOverviewVideoPosterUrl;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String loginRegistrationVideoUrl;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String applicationName;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private String landingPageTitle;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private String landingPageBanner;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_64K)
	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private String landingPageFooter;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_16K)
	@ConsumeField
	private String analyticsTrackingCode;

	@ConsumeField
	private Boolean allowSecurityMarkingsFlg;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	@Sanitize(HTMLSanitizer.class)
	private String securityBannerText;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_60)
	@Sanitize(TextSanitizer.class)
	private String securityBannerBackgroundColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_60)
	@Sanitize(TextSanitizer.class)
	private String securityBannerTextColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	@Sanitize(HTMLSanitizer.class)
	private String userInputWarning;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	@Sanitize(HTMLSanitizer.class)
	private String submissionFormWarning;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	@Sanitize(HTMLSanitizer.class)
	private String changeRequestWarning;

	@ConsumeField
	@ValidValueType(value = {}, lookupClass = FeedbackHandleType.class)
	private String feedbackHandler;

	@ConsumeField
	private Boolean showSupportMedia;

	@ConsumeField
	private Boolean showFAQ;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_COLOR)
	private String primaryColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_COLOR)
	private String primaryTextColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_COLOR)
	private String secondaryColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_COLOR)
	private String accentColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_COLOR)
	private String quoteColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_COLOR)
	private String linkColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_COLOR)
	private String linkVisitedColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_COLOR)
	private String linkhoverColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_COLOR)
	private String panelHeaderColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_COLOR)
	private String panelHeaderTextColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_COLOR)
	private String vuePrimaryColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_COLOR)
	private String vueSecondaryColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_COLOR)
	private String vueAccentColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_COLOR)
	private String vueErrorColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_COLOR)
	private String vueInfoColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_COLOR)
	private String vueWarningColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_COLOR)
	private String vueSuccessColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_1MB)
	private String overrideCSS;

	@ConsumeField
	private Boolean showSupportMenuOnLogin;

	@ConsumeField
	private Boolean showLinkToMobile;
	
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private String disclaimerMessage;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private String bulkUploadMessage;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public Branding()
	{
	}

	@Override
	public void updateFields(StandardEntity entity)
	{
		super.updateFields(entity);

		Branding branding = (Branding) entity;

		setAllowSecurityMarkingsFlg(branding.getAllowSecurityMarkingsFlg());
		setApplicationName(branding.getApplicationName());
		setLandingPageBanner(branding.getLandingPageBanner());
		setLandingPageFooter(branding.getLandingPageFooter());
		setLandingPageTitle(branding.getLandingPageTitle());
		setName(branding.getName());
		setPrimaryLogoUrl(branding.getPrimaryLogoUrl());
		setSecondaryLogoUrl(branding.getSecondaryLogoUrl());
		setHomebackSplashUrl(branding.getHomebackSplashUrl());
		setAnalyticsTrackingCode(branding.getAnalyticsTrackingCode());

		setLoginContentBlock(branding.getLoginContentBlock());
		setLoginOverviewVideoUrl(branding.getLoginOverviewVideoUrl());
		setLoginOverviewVideoPosterUrl(branding.getLoginOverviewVideoPosterUrl());
		setLoginRegistrationVideoUrl(branding.getLoginRegistrationVideoUrl());
		setShowSupportMenuOnLogin(branding.getShowSupportMenuOnLogin());
		setLoginFooter(branding.getLoginFooter());

		setSecurityBannerBackgroundColor(branding.getSecurityBannerBackgroundColor());
		setSecurityBannerText(branding.getSecurityBannerText());
		setSecurityBannerTextColor(branding.getSecurityBannerTextColor());
		setUserInputWarning(branding.getUserInputWarning());
		setSubmissionFormWarning(branding.getSubmissionFormWarning());
		setChangeRequestWarning(branding.getChangeRequestWarning());
		setFeedbackHandler(branding.getFeedbackHandler());
		setShowSupportMedia(branding.getShowSupportMedia());
		setShowFAQ(branding.getShowFAQ());

		setPrimaryColor(branding.getPrimaryColor());
		setPrimaryTextColor(branding.getPrimaryTextColor());
		setSecondaryColor(branding.getSecondaryColor());
		setAccentColor(branding.getAccentColor());
		setQuoteColor(branding.getQuoteColor());
		setLinkColor(branding.getLinkColor());
		setLinkVisitedColor(branding.getLinkVisitedColor());
		setLinkhoverColor(branding.getLinkhoverColor());
		setPanelHeaderColor(branding.getPanelHeaderColor());
		setPanelHeaderTextColor(branding.getPanelHeaderTextColor());
		setVuePrimaryColor(branding.getVuePrimaryColor());
		setVueSecondaryColor(branding.getVueSecondaryColor());
		setVueAccentColor(branding.getVueAccentColor());
		setVueErrorColor(branding.getVueErrorColor());
		setVueInfoColor(branding.getVueInfoColor());
		setVueWarningColor(branding.getVueWarningColor());
		setVueSuccessColor(branding.getVueSuccessColor());
		setOverrideCSS(branding.getOverrideCSS());

		setShowLinkToMobile(branding.getShowLinkToMobile());
		setDisclaimerMessage(branding.getDisclaimerMessage());
		setBulkUploadMessage(branding.getBulkUploadMessage());
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

	public Boolean getAllowSecurityMarkingsFlg()
	{
		return allowSecurityMarkingsFlg;
	}

	public void setAllowSecurityMarkingsFlg(Boolean allowSecurityMarkingsFlg)
	{
		this.allowSecurityMarkingsFlg = allowSecurityMarkingsFlg;
	}

	public String getFeedbackHandler()
	{
		return feedbackHandler;
	}

	public void setFeedbackHandler(String feedbackHandler)
	{
		this.feedbackHandler = feedbackHandler;
	}

	public String getSecurityBannerText()
	{
		return securityBannerText;
	}

	public void setSecurityBannerText(String securityBannerText)
	{
		this.securityBannerText = securityBannerText;
	}

	public String getSecurityBannerBackgroundColor()
	{
		return securityBannerBackgroundColor;
	}

	public void setSecurityBannerBackgroundColor(String securityBannerBackgroundColor)
	{
		this.securityBannerBackgroundColor = securityBannerBackgroundColor;
	}

	public String getSecurityBannerTextColor()
	{
		return securityBannerTextColor;
	}

	public void setSecurityBannerTextColor(String securityBannerTextColor)
	{
		this.securityBannerTextColor = securityBannerTextColor;
	}

	public String getUserInputWarning()
	{
		return userInputWarning;
	}

	public void setUserInputWarning(String userInputWarning)
	{
		this.userInputWarning = userInputWarning;
	}

	public String getPrimaryColor()
	{
		return primaryColor;
	}

	public void setPrimaryColor(String primaryColor)
	{
		this.primaryColor = primaryColor;
	}

	public String getAccentColor()
	{
		return accentColor;
	}

	public void setAccentColor(String accentColor)
	{
		this.accentColor = accentColor;
	}

	public String getQuoteColor()
	{
		return quoteColor;
	}

	public void setQuoteColor(String quoteColor)
	{
		this.quoteColor = quoteColor;
	}

	public String getLinkColor()
	{
		return linkColor;
	}

	public void setLinkColor(String linkColor)
	{
		this.linkColor = linkColor;
	}

	public String getLinkhoverColor()
	{
		return linkhoverColor;
	}

	public void setLinkhoverColor(String linkhoverColor)
	{
		this.linkhoverColor = linkhoverColor;
	}

	public String getPanelHeaderColor()
	{
		return panelHeaderColor;
	}

	public void setPanelHeaderColor(String panelHeaderColor)
	{
		this.panelHeaderColor = panelHeaderColor;
	}

	public String getOverrideCSS()
	{
		return overrideCSS;
	}

	public void setOverrideCSS(String overrideCSS)
	{
		this.overrideCSS = overrideCSS;
	}

	public String getPrimaryTextColor()
	{
		return primaryTextColor;
	}

	public void setPrimaryTextColor(String primaryTextColor)
	{
		this.primaryTextColor = primaryTextColor;
	}

	public String getLinkVisitedColor()
	{
		return linkVisitedColor;
	}

	public void setLinkVisitedColor(String linkVisitedColor)
	{
		this.linkVisitedColor = linkVisitedColor;
	}

	public String getPanelHeaderTextColor()
	{
		return panelHeaderTextColor;
	}

	public void setPanelHeaderTextColor(String panelHeaderTextColor)
	{
		this.panelHeaderTextColor = panelHeaderTextColor;
	}

	public String getVuePrimaryColor() {
		return this.vuePrimaryColor;
	}

	public void setVuePrimaryColor(String vuePrimaryColor) {
		this.vuePrimaryColor = vuePrimaryColor;
	}

	public String getVueSecondaryColor() {
		return this.vueSecondaryColor;
	}

	public void setVueSecondaryColor(String vueSecondaryColor) {
		this.vueSecondaryColor = vueSecondaryColor;
	}

	public String getVueAccentColor() {
		return this.vueAccentColor;
	}

	public void setVueAccentColor(String vueAccentColor) {
		this.vueAccentColor = vueAccentColor;
	}

	public String getVueErrorColor() {
		return this.vueErrorColor;
	}

	public void setVueErrorColor(String vueErrorColor) {
		this.vueErrorColor = vueErrorColor;
	}

	public String getVueInfoColor() {
		return this.vueInfoColor;
	}

	public void setVueInfoColor(String vueInfoColor) {
		this.vueInfoColor = vueInfoColor;
	}

	public String getVueWarningColor() {
		return this.vueWarningColor;
	}

	public void setVueWarningColor(String vueWarningColor) {
		this.vueWarningColor = vueWarningColor;
	}

	public String getVueSuccessColor() {
		return this.vueSuccessColor;
	}

	public void setVueSuccessColor(String vueSuccessColor) {
		this.vueSuccessColor = vueSuccessColor;
	}

	public String getSubmissionFormWarning()
	{
		return submissionFormWarning;
	}

	public void setSubmissionFormWarning(String submissionFormWarning)
	{
		this.submissionFormWarning = submissionFormWarning;
	}

	public String getAnalyticsTrackingCode()
	{
		return analyticsTrackingCode;
	}

	public void setAnalyticsTrackingCode(String analyticsTrackingCode)
	{
		this.analyticsTrackingCode = analyticsTrackingCode;
	}

	public String getChangeRequestWarning()
	{
		return changeRequestWarning;
	}

	public void setChangeRequestWarning(String changeRequestWarning)
	{
		this.changeRequestWarning = changeRequestWarning;
	}

	public String getHomebackSplashUrl()
	{
		return homebackSplashUrl;
	}

	public void setHomebackSplashUrl(String homebackSplashUrl)
	{
		this.homebackSplashUrl = homebackSplashUrl;
	}

	public String getSecondaryColor()
	{
		return secondaryColor;
	}

	public void setSecondaryColor(String secondaryColor)
	{
		this.secondaryColor = secondaryColor;
	}

	public String getLoginContentBlock()
	{
		return loginContentBlock;
	}

	public void setLoginContentBlock(String loginContentBlock)
	{
		this.loginContentBlock = loginContentBlock;
	}

	public String getLoginFooter()
	{
		return loginFooter;
	}

	public void setLoginFooter(String loginFooter)
	{
		this.loginFooter = loginFooter;
	}

	public Boolean getShowSupportMedia()
	{
		return showSupportMedia;
	}

	public void setShowSupportMedia(Boolean showSupportMedia)
	{
		this.showSupportMedia = showSupportMedia;
	}

	public String getLoginOverviewVideoUrl()
	{
		return loginOverviewVideoUrl;
	}

	public void setLoginOverviewVideoUrl(String loginOverviewVideoUrl)
	{
		this.loginOverviewVideoUrl = loginOverviewVideoUrl;
	}

	public String getLoginRegistrationVideoUrl()
	{
		return loginRegistrationVideoUrl;
	}

	public void setLoginRegistrationVideoUrl(String loginRegistrationVideoUrl)
	{
		this.loginRegistrationVideoUrl = loginRegistrationVideoUrl;
	}

	public String getLoginOverviewVideoPosterUrl()
	{
		return loginOverviewVideoPosterUrl;
	}

	public void setLoginOverviewVideoPosterUrl(String loginOverviewVideoPosterUrl)
	{
		this.loginOverviewVideoPosterUrl = loginOverviewVideoPosterUrl;
	}

	public Boolean getShowSupportMenuOnLogin()
	{
		return showSupportMenuOnLogin;
	}

	public void setShowSupportMenuOnLogin(Boolean showSupportMenuOnLogin)
	{
		this.showSupportMenuOnLogin = showSupportMenuOnLogin;
	}

	public Boolean getShowFAQ()
	{
		return showFAQ;
	}

	public void setShowFAQ(Boolean showFAQ)
	{
		this.showFAQ = showFAQ;
	}

	public Boolean getShowLinkToMobile()
	{
		return showLinkToMobile;
	}

	public void setShowLinkToMobile(Boolean showLinkToMobile)
	{
		this.showLinkToMobile = showLinkToMobile;
	}
	
	public String getDisclaimerMessage()
	{
		return this.disclaimerMessage;
	}
	
	public void setDisclaimerMessage(String disclaimerMessage)
	{
		this.disclaimerMessage = disclaimerMessage;
	}

	public String getBulkUploadMessage() {
		return bulkUploadMessage;
	}

	public void setBulkUploadMessage(String bulkUploadMessage) {
		this.bulkUploadMessage = bulkUploadMessage;
	}
}