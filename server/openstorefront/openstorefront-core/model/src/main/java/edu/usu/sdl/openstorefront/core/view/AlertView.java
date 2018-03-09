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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.Alert;
import edu.usu.sdl.openstorefront.core.entity.AlertType;
import edu.usu.sdl.openstorefront.core.entity.ComponentTypeAlertOption;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
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

	private String alertTypeDescription;

	private List<String> componentTypeAlertOptionLabels = new ArrayList<>();

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
		alertView.setAlertTypeDescription(TranslateUtil.translate(AlertType.class, alert.getAlertType()));
		//get labels from component codes
		if (alert.getComponentTypeAlertOptions() != null) {
			for (ComponentTypeAlertOption compType : alert.getComponentTypeAlertOptions()) {
				alertView.getComponentTypeAlertOptionLabels().add(TranslateUtil.translateComponentType(compType.getComponentType()));
			}
		}
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

	public String getAlertTypeDescription()
	{
		return alertTypeDescription;
	}

	public void setAlertTypeDescription(String alertTypeDescription)
	{
		this.alertTypeDescription = alertTypeDescription;
	}

	public List<String> getComponentTypeAlertOptionLabels()
	{
		return componentTypeAlertOptionLabels;
	}

	public void setComponentTypeAlertOptionLabels(List<String> componentTypeAlertOptionLabels)
	{
		this.componentTypeAlertOptionLabels = componentTypeAlertOptionLabels;
	}

}
