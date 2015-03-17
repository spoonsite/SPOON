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
package edu.usu.sdl.openstorefront.web.rest.model;

import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.storage.model.Alert;
import edu.usu.sdl.openstorefront.storage.model.AlertType;
import edu.usu.sdl.openstorefront.util.TranslateUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class AlertView
		extends Alert
{

	private String alertTypeDesciption;

	public AlertView()
	{
	}

	public static AlertView toView(Alert alert)
	{
		AlertView alertView = new AlertView();
		try {
			BeanUtils.copyProperties(alertView, alert);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		alertView.setAlertTypeDesciption(TranslateUtil.translate(AlertType.class, alert.getAlertType()));
		return alertView;
	}

	public static List<AlertView> toView(List<Alert> alerts)
	{
		List<AlertView> views = new ArrayList<>();
		alerts.forEach(alert -> {
			views.add(toView(alert));
		});
		return views;
	}

	public String getAlertTypeDesciption()
	{
		return alertTypeDesciption;
	}

	public void setAlertTypeDesciption(String alertTypeDesciption)
	{
		this.alertTypeDesciption = alertTypeDesciption;
	}

}
