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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.UserSubmission;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class UserSubmissionView
{

	private String userSubmissionId;
	private String templateId;
	private String originalComponentId;
	private String componentType;
	private String componentTypeDescription;
	private String componentName;
	private String ownerUsername;
	private String createUser;
	private String updateUser;
	private Date createDate;
	private Date updateDate;

	public UserSubmissionView()
	{
		//ignore for serialization
	}

	public static UserSubmissionView toView(UserSubmission userSubmission)
	{
		UserSubmissionView view = new UserSubmissionView();
		try {
			BeanUtils.copyProperties(view, userSubmission);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		//Change to use nested parent view 2.5.4
		view.setComponentTypeDescription(TranslateUtil.translateComponentType(userSubmission.getComponentType()));

		if (StringUtils.isNotBlank(view.getOriginalComponentId())) {
			view.setComponentName(ServiceProxyFactory.getServiceProxy().getComponentService().getComponentName(view.getOriginalComponentId()));
		}
		return view;
	}

	public static List<UserSubmissionView> toView(List<UserSubmission> userSubmissions)
	{
		List<UserSubmissionView> views = new ArrayList<>();
		userSubmissions.forEach(userSubmission -> {
			views.add(toView(userSubmission));
		});
		return views;
	}

	public String getUserSubmissionId()
	{
		return userSubmissionId;
	}

	public void setUserSubmissionId(String userSubmissionId)
	{
		this.userSubmissionId = userSubmissionId;
	}

	public String getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(String templateId)
	{
		this.templateId = templateId;
	}

	public String getComponentType()
	{
		return componentType;
	}

	public void setComponentType(String componentType)
	{
		this.componentType = componentType;
	}

	public String getComponentTypeDescription()
	{
		return componentTypeDescription;
	}

	public void setComponentTypeDescription(String componentTypeDescription)
	{
		this.componentTypeDescription = componentTypeDescription;
	}

	public String getComponentName()
	{
		return componentName;
	}

	public void setComponentName(String componentName)
	{
		this.componentName = componentName;
	}

	public String getOwnerUsername()
	{
		return ownerUsername;
	}

	public void setOwnerUsername(String ownerUsername)
	{
		this.ownerUsername = ownerUsername;
	}

	public String getCreateUser()
	{
		return createUser;
	}

	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}

	public String getUpdateUser()
	{
		return updateUser;
	}

	public void setUpdateUser(String updateUser)
	{
		this.updateUser = updateUser;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	public Date getUpdateDate()
	{
		return updateDate;
	}

	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
	}

	public String getOriginalComponentId()
	{
		return originalComponentId;
	}

	public void setOriginalComponentId(String originalComponentId)
	{
		this.originalComponentId = originalComponentId;
	}

}
