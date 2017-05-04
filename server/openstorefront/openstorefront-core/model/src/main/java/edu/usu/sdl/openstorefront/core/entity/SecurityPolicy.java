/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Hold security peferences for the application; note most of these only apply to the internal security.")
public class SecurityPolicy
	extends StandardEntity<SecurityPolicy>
{
	@PK(generated = true)
	@NotNull
	private String policyId;
	
	@NotNull
	@ConsumeField
	private Boolean autoApproveUsers;
	
	@NotNull	
	@ConsumeField
	@Min(8)
	@Max(OpenStorefrontConstant.FIELD_SIZE_80)
	private Integer minPasswordLength;
	
	@NotNull
	@ConsumeField	
	@Min(0)
	@Max(25)
	private Integer loginLockoutMaxAttempts;
	
	@NotNull
	@ConsumeField
	@Min(1)	
	@Max(1440)
	private Integer resetLockoutTimeMinutes;
	
	@NotNull
	@ConsumeField
	private Boolean requireAdminUnlock;

	@NotNull
	@ConsumeField
	private Boolean requiresProofOfCitizenship;
	
	@NotNull
	@ConsumeField
	private Boolean allowJSONPSupport;
	
	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	private String corsOrigins;
	
	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	private String corsMethods;
	
	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	private String corsHeaders;
	
	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	private String customHeaders;
	
	@NotNull
	@ConsumeField
	private Boolean csrfSupport;	
	
	@NotNull
	@ConsumeField
	private Boolean allowRegistration;
	
	@ConsumeField
	private Boolean disableUserInfoEdit;
	
	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(HTMLSanitizer.class)
	private String externalUserManagementText;
		
	public SecurityPolicy()
	{
	}

	
	public SecurityPolicy copy() 
	{
		SecurityPolicy newPolicy = new SecurityPolicy();
		newPolicy.setAllowJSONPSupport(getAllowJSONPSupport());
		newPolicy.setAllowRegistration(getAllowRegistration());
		newPolicy.setAutoApproveUsers(getAutoApproveUsers());
		newPolicy.setCorsHeaders(getCorsHeaders());
		newPolicy.setCorsMethods(getCorsMethods());		
		newPolicy.setCorsOrigins(getCorsOrigins());
		newPolicy.setCsrfSupport(getCsrfSupport());
		newPolicy.setCustomHeaders(getCustomHeaders());
		newPolicy.setLoginLockoutMaxAttempts(getLoginLockoutMaxAttempts());
		newPolicy.setMinPasswordLength(getMinPasswordLength());
		newPolicy.setPolicyId(getPolicyId());
		newPolicy.setRequireAdminUnlock(getRequireAdminUnlock());
		newPolicy.setRequiresProofOfCitizenship(getRequiresProofOfCitizenship());
		newPolicy.setResetLockoutTimeMinutes(getResetLockoutTimeMinutes());
		newPolicy.setDisableUserInfoEdit(getDisableUserInfoEdit());
		newPolicy.setExternalUserManagementText(getExternalUserManagementText());
				
		return newPolicy;
	}
	
	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity); 
		
		SecurityPolicy securityPolicy = (SecurityPolicy) entity;
		
		setAutoApproveUsers(Convert.toBoolean(securityPolicy.getAutoApproveUsers()));
		setMinPasswordLength(securityPolicy.getMinPasswordLength());
		setLoginLockoutMaxAttempts(securityPolicy.getLoginLockoutMaxAttempts());
		setResetLockoutTimeMinutes(securityPolicy.getResetLockoutTimeMinutes());
		setRequireAdminUnlock(Convert.toBoolean(securityPolicy.getRequireAdminUnlock()));
		setRequiresProofOfCitizenship(Convert.toBoolean(securityPolicy.getRequiresProofOfCitizenship()));
		setAllowJSONPSupport(Convert.toBoolean(securityPolicy.getAllowJSONPSupport()));
		setCorsOrigins(securityPolicy.getCorsOrigins());
		setCorsMethods(securityPolicy.getCorsMethods());
		setCorsHeaders(securityPolicy.getCorsHeaders());
		setCustomHeaders(securityPolicy.getCustomHeaders());
		setCsrfSupport(Convert.toBoolean(securityPolicy.getCsrfSupport()));
		setAllowRegistration(Convert.toBoolean(securityPolicy.getAllowRegistration()));
		setDisableUserInfoEdit(securityPolicy.getDisableUserInfoEdit());
		setExternalUserManagementText(securityPolicy.getExternalUserManagementText());
						
	}
	
	public String getPolicyId()
	{
		return policyId;
	}

	public void setPolicyId(String policyId)
	{
		this.policyId = policyId;
	}

	public Boolean getAutoApproveUsers()
	{
		return autoApproveUsers;
	}

	public void setAutoApproveUsers(Boolean autoApproveUsers)
	{
		this.autoApproveUsers = autoApproveUsers;
	}

	public Integer getMinPasswordLength()
	{
		return minPasswordLength;
	}

	public void setMinPasswordLength(Integer minPasswordLength)
	{
		this.minPasswordLength = minPasswordLength;
	}

	public Integer getLoginLockoutMaxAttempts()
	{
		return loginLockoutMaxAttempts;
	}

	public void setLoginLockoutMaxAttempts(Integer loginLockoutMaxAttempts)
	{
		this.loginLockoutMaxAttempts = loginLockoutMaxAttempts;
	}

	public Integer getResetLockoutTimeMinutes()
	{
		return resetLockoutTimeMinutes;
	}

	public void setResetLockoutTimeMinutes(Integer resetLockoutTimeMinutes)
	{
		this.resetLockoutTimeMinutes = resetLockoutTimeMinutes;
	}

	public Boolean getRequireAdminUnlock()
	{
		return requireAdminUnlock;
	}

	public void setRequireAdminUnlock(Boolean requireAdminUnlock)
	{
		this.requireAdminUnlock = requireAdminUnlock;
	}

	public Boolean getRequiresProofOfCitizenship()
	{
		return requiresProofOfCitizenship;
	}

	public void setRequiresProofOfCitizenship(Boolean requiresProofOfCitizenship)
	{
		this.requiresProofOfCitizenship = requiresProofOfCitizenship;
	}

	public Boolean getAllowJSONPSupport()
	{
		return allowJSONPSupport;
	}

	public void setAllowJSONPSupport(Boolean allowJSONPSupport)
	{
		this.allowJSONPSupport = allowJSONPSupport;
	}

	public String getCorsOrigins()
	{
		return corsOrigins;
	}

	public void setCorsOrigins(String corsOrigins)
	{
		this.corsOrigins = corsOrigins;
	}

	public String getCorsMethods()
	{
		return corsMethods;
	}

	public void setCorsMethods(String corsMethods)
	{
		this.corsMethods = corsMethods;
	}

	public String getCorsHeaders()
	{
		return corsHeaders;
	}

	public void setCorsHeaders(String corsHeaders)
	{
		this.corsHeaders = corsHeaders;
	}

	public String getCustomHeaders()
	{
		return customHeaders;
	}

	public void setCustomHeaders(String customHeaders)
	{
		this.customHeaders = customHeaders;
	}

	public Boolean getCsrfSupport()
	{
		return csrfSupport;
	}

	public void setCsrfSupport(Boolean csrfSupport)
	{
		this.csrfSupport = csrfSupport;
	}

	public Boolean getAllowRegistration()
	{
		return allowRegistration;
	}

	public void setAllowRegistration(Boolean allowRegistration)
	{
		this.allowRegistration = allowRegistration;
	}

	public Boolean getDisableUserInfoEdit()
	{
		return disableUserInfoEdit;
	}

	public void setDisableUserInfoEdit(Boolean disableUserInfoEdit)
	{
		this.disableUserInfoEdit = disableUserInfoEdit;
	}

	public String getExternalUserManagementText()
	{
		return externalUserManagementText;
	}

	public void setExternalUserManagementText(String externalUserManagementText)
	{
		this.externalUserManagementText = externalUserManagementText;
	}
	
}
