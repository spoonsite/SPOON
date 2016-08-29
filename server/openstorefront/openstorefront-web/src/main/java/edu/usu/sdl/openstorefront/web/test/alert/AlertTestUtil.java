/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.test.alert;

import edu.usu.sdl.openstorefront.core.entity.Alert;
import static edu.usu.sdl.openstorefront.core.entity.StandardEntity.ACTIVE_STATUS;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import java.util.List;

/**
 *
 * @author ccummings
 */
public class AlertTestUtil
{

	private AlertTestUtil()
	{

	}

	public static List<Alert> getActiveAlerts()
	{
		Alert activeAlert = new Alert();
		activeAlert.setActiveStatus(ACTIVE_STATUS);
		List<Alert> activeAlerts = activeAlert.findByExample();
		return activeAlerts;
	}

	public static void inactivateAlerts(List<Alert> activeAlerts)
	{
		ServiceProxy service = ServiceProxy.getProxy();
		for (Alert activeAlert : activeAlerts) {
			activeAlert = service.getPersistenceService().setStatusOnEntity(Alert.class, activeAlert.getAlertId(), Alert.INACTIVE_STATUS);
			service.getAlertService().saveAlert(activeAlert);
		}
	}

	public static void activateAlerts(List<Alert> inactiveAlerts)
	{
		ServiceProxy service = ServiceProxy.getProxy();
		for (Alert inactiveAlert : inactiveAlerts) {
			inactiveAlert = service.getPersistenceService().setStatusOnEntity(Alert.class, inactiveAlert.getAlertId(), Alert.ACTIVE_STATUS);
			service.getAlertService().saveAlert(inactiveAlert);
		}
	}
}
