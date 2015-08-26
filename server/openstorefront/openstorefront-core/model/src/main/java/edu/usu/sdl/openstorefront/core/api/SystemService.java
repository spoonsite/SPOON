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
package edu.usu.sdl.openstorefront.core.api;

import edu.usu.sdl.openstorefront.core.api.model.TaskFuture;
import edu.usu.sdl.openstorefront.core.entity.ApplicationProperty;
import edu.usu.sdl.openstorefront.core.entity.DBLogRecord;
import edu.usu.sdl.openstorefront.core.entity.GeneralMedia;
import edu.usu.sdl.openstorefront.core.entity.HelpSection;
import edu.usu.sdl.openstorefront.core.entity.Highlight;
import edu.usu.sdl.openstorefront.core.model.ErrorInfo;
import edu.usu.sdl.openstorefront.core.model.HelpSectionAll;
import edu.usu.sdl.openstorefront.core.view.GlobalIntegrationModel;
import edu.usu.sdl.openstorefront.core.view.SystemErrorModel;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public interface SystemService
		extends AsyncService
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
	 * Delete Hightlight
	 *
	 * @param hightlightId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void deleteHighlight(String hightlightId);

	/**
	 * Activate Hightlight
	 *
	 * @param hightlightId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void activateHighlight(String hightlightId);

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

	/**
	 * Gets the Global integration properties
	 *
	 * @return
	 */
	public GlobalIntegrationModel getGlobalIntegrationConfig();

	/**
	 * Save the Global Config properties
	 *
	 * @param globalIntegrationModel
	 */
	public void saveGlobalIntegrationConfig(GlobalIntegrationModel globalIntegrationModel);

	/**
	 * Saves a general media file
	 *
	 * @param generalMedia
	 * @param fileInput (optional on update)
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveGeneralMedia(GeneralMedia generalMedia, InputStream fileInput);

	/**
	 * Delete the general media
	 *
	 * @param mediaName
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void removeGeneralMedia(String mediaName);

	/**
	 * Saves a task so it can be persisted across bounces This is meant for use
	 * with completed tasks. Note: this will override existing task of the same
	 * id
	 *
	 * @param taskFuture
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveAsyncTask(TaskFuture taskFuture);

	/**
	 * Removes a persist task
	 *
	 * @param taskId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void removeAsyncTask(String taskId);

	/**
	 * Inserts a new log record.
	 *
	 * @param logRecord
	 */
	public void addLogRecord(DBLogRecord logRecord);

	/**
	 * This enforces max log records kept in the DB.
	 */
	public void cleanUpOldLogRecords();

	/**
	 * Warning: This will clear all database log records Keep in mind the
	 * purpose of DB logs is for troubleshooting and it works in conjunction
	 * with run-time log Level switching. Server log are the primary long term
	 * login method.
	 */
	public void clearAllLogRecord();

	/**
	 * Loads a new set of help sections...it will remove the old and save the
	 * new Full Refresh of the help data
	 *
	 * @param helpSections
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void loadNewHelpSections(List<HelpSection> helpSections);

	/**
	 * Pull all help and organize it
	 *
	 * @param includeAdmin
	 * @return root help section and all children
	 */
	public HelpSectionAll getAllHelp(Boolean includeAdmin);

}
