/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import edu.usu.sdl.openstorefront.service.transfermodel.ErrorInfo;
import edu.usu.sdl.openstorefront.storage.model.ApplicationProperty;
import edu.usu.sdl.openstorefront.storage.model.Highlight;
import edu.usu.sdl.openstorefront.web.viewmodel.SystemErrorModel;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public interface SystemService
{

	/**
	 * Gets Full application Property
	 *
	 * @param key
	 * @return null if property is not found
	 */
	public ApplicationProperty getProperty(String key);

	/**
	 * Gets property Value
	 *
	 * @param key
	 * @return null if property is not found
	 */
	public String getPropertyValue(String key);

	/**
	 * Save a property to Application Properties. Application properties are
	 * used to store application specific state. If the property should be admin
	 * configurable it should be store in the properties file
	 *
	 * @param key
	 * @param value
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveProperty(String key, String value);

	/**
	 * Saves Highlights
	 *
	 * @param highlights
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveHightlight(List<Highlight> highlights);

	/**
	 * Saves Highlights (generates Id on create)
	 *
	 * @param highlight
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveHightlight(Highlight highlight);

	/**
	 * Inactive Hightlight
	 *
	 * @param hightlightId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void removeHighlight(String hightlightId);

	/**
	 * Delete all old and save new highlights This assume to be called by the
	 * importer and is meant to sync the DB with the file
	 *
	 * @param highlights
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void syncHighlights(List<Highlight> highlights);

	/**
	 * This handle the error and generate an error ticket
	 *
	 * @param errorInfo
	 * @return SystemErrorModel
	 */
	public SystemErrorModel generateErrorTicket(ErrorInfo errorInfo);

	/**
	 * Retrieves the error ticket from the file system
	 *
	 * @param errorTicketId
	 * @return
	 */
	public String errorTicketInfo(String errorTicketId);

	/**
	 * Removes excess errors beyond max....deleting oldest first
	 */
	public void cleanupOldErrors();

}
