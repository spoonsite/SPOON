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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.UserRegistration;
import edu.usu.sdl.openstorefront.core.entity.UserTypeCode;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class UserRegistrationView
		extends UserRegistration
{

	private String userTypeDescription;

	public static UserRegistrationView toView(UserRegistration userRegistration)
	{
		UserRegistrationView view = new UserRegistrationView();
		try {
			BeanUtils.copyProperties(view, userRegistration);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		view.setUserTypeDescription(TranslateUtil.translate(UserTypeCode.class, userRegistration.getUserTypeCode()));
		return view;
	}

	public static List<UserRegistrationView> toView(List<UserRegistration> userRegistrations)
	{
		List<UserRegistrationView> views = new ArrayList<>();
		userRegistrations.forEach(userRegistration -> {
			views.add(toView(userRegistration));
		});
		return views;
	}

	public String getUserTypeDescription()
	{
		return userTypeDescription;
	}

	public void setUserTypeDescription(String userTypeDescription)
	{
		this.userTypeDescription = userTypeDescription;
	}

}
