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
package edu.usu.sdl.openstorefront.service.api;

import edu.usu.sdl.openstorefront.service.ServiceInterceptor;
import edu.usu.sdl.openstorefront.service.TransactionInterceptor;
import edu.usu.sdl.openstorefront.service.transfermodel.AlertContext;
import edu.usu.sdl.openstorefront.storage.model.Alert;

/**
 * Handles Alerts
 *
 * @author dshurtleff
 */
public interface AlertService
		extends AsyncService
{

	/**
	 * Saves an Alert
	 *
	 * @param alert
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public Alert saveAlert(Alert alert);

	/**
	 * This is hard delete of an Alert
	 *
	 * @param alertId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void deleteAlert(String alertId);

	/**
	 * This handles processing the alerts into UserMessage
	 *
	 * @param alertContext
	 */
	public void checkAlert(AlertContext alertContext);

}
