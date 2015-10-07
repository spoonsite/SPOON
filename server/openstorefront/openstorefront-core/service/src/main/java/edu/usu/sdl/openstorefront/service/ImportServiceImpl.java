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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.api.ImportService;
import edu.usu.sdl.openstorefront.core.entity.FileFormat;
import edu.usu.sdl.openstorefront.core.entity.FileHistory;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryError;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryErrorType;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryOption;
import edu.usu.sdl.openstorefront.core.entity.NotificationEvent;
import edu.usu.sdl.openstorefront.core.entity.NotificationEventType;
import edu.usu.sdl.openstorefront.core.model.FileHistoryAll;
import edu.usu.sdl.openstorefront.core.model.ImportContext;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.api.ImportServicePrivate;
import edu.usu.sdl.openstorefront.service.io.parser.AbstractParser;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ImportServiceImpl
		extends ServiceProxy
		implements ImportService, ImportServicePrivate
{

	private static final Logger log = Logger.getLogger(ImportServiceImpl.class.getName());

	@Override
	public String importData(ImportContext importContext)
	{
		Objects.requireNonNull(importContext.getFileHistoryAll().getFileHistory());

		//create history record
		FileHistory fileHistory = importContext.getFileHistoryAll().getFileHistory();
		fileHistory.setFileHistoryId(persistenceService.generateId());
		fileHistory.setFilename(fileHistory.getFileHistoryId());
		fileHistory.setRecordsStored(0);
		if (fileHistory.getFileHistoryOption() == null) {
			fileHistory.setFileHistoryOption(new FileHistoryOption());
		}

		try (InputStream in = importContext.getInput(); OutputStream out = new FileOutputStream(fileHistory.pathToFileName().toFile())) {
			FileSystemManager.copy(in, out);
		} catch (IOException e) {
			throw new OpenStorefrontRuntimeException("Unable to save upload.", "Check file system permissions and disk space.");
		}

		//save data (close stream)
		//queue upload so it can be batched
		FileHistory historySaved = saveFileHistory(importContext.getFileHistoryAll());

		return historySaved.getFileHistoryId();
	}

	@Override
	public void processImport(String fileHistoryId)
	{
		FileHistoryAll fileHistoryAll = new FileHistoryAll();
		FileHistory fileHistory = persistenceService.findById(FileHistory.class, fileHistoryId);
		if (fileHistory == null) {
			throw new OpenStorefrontRuntimeException("Unable to find file history.", "Check id: " + fileHistoryId + " it may have been deleted.");
		}
		fileHistoryAll.setFileHistory(fileHistory);

		fileHistory.setStartDts(TimeUtil.currentDate());
		fileHistory.populateBaseUpdateFields();
		fileHistory = persistenceService.persist(fileHistory);

		//Get Parser for format
		FileFormat fileFormat = getLookupService().getLookupEnity(FileFormat.class, fileHistory.getFileFormat());

		try {
			Class parserClass = this.getClass().getClassLoader().loadClass("edu.usu.sdl.openstorefront.service.io.parser." + fileFormat.getParserClass());
			AbstractParser abstractParser = (AbstractParser) parserClass.newInstance();
			abstractParser.processData(fileHistoryAll);

			if (OpenStorefrontConstant.ANONYMOUS_USER.equals(fileHistory.getCreateUser()) == false) {
				NotificationEvent notificationEvent = new NotificationEvent();
				notificationEvent.setEventType(NotificationEventType.IMPORT);
				notificationEvent.setUsername(fileHistory.getCreateUser());
				notificationEvent.setMessage("File: " + fileHistory.getOriginalFilename() + " has finished processing.");
				notificationEvent.setEntityName(FileHistory.class.getSimpleName());
				notificationEvent.setEntityId(fileHistoryId);
				getNotificationService().postEvent(notificationEvent);
			}

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			log.log(Level.SEVERE, "Unable to load parser class: " + fileFormat.getParserClass(), e);
			fileHistoryAll.addError(FileHistoryErrorType.SYSTEM, "Unable to load parser class: " + fileFormat.getParserClass());
		}

		//processData
		fileHistory.setCompleteDts(TimeUtil.currentDate());
		saveFileHistory(fileHistoryAll);
	}

	@Override
	public FileHistory saveFileHistory(FileHistoryAll fileHistoryAll)
	{
		Objects.requireNonNull(fileHistoryAll);
		Objects.requireNonNull(fileHistoryAll.getFileHistory());
		Objects.requireNonNull(fileHistoryAll.getErrors());

		FileHistory fileHistory = persistenceService.findById(FileHistory.class, fileHistoryAll.getFileHistory().getFileHistoryId());
		if (fileHistory != null) {
			fileHistory.updateFields(fileHistoryAll.getFileHistory());
			persistenceService.persist(fileHistory);
		} else {
			if (StringUtils.isBlank(fileHistoryAll.getFileHistory().getFileHistoryId())) {
				fileHistoryAll.getFileHistory().setFileHistoryId(persistenceService.generateId());
			}
			fileHistoryAll.getFileHistory().populateBaseCreateFields();
			fileHistory = persistenceService.persist(fileHistoryAll.getFileHistory());
		}

		//replace errors
		FileHistoryError fileHistoryError = new FileHistoryError();
		fileHistoryError.setFileHistoryId(fileHistory.getFileHistoryId());
		persistenceService.deleteByExample(fileHistoryError);

		for (FileHistoryError error : fileHistoryAll.getErrors()) {
			error.setFileHistoryErrorId(persistenceService.generateId());
			error.setFileHistoryId(fileHistory.getFileHistoryId());
			error.populateBaseCreateFields();
			persistenceService.persist(error);
		}

		return fileHistory;
	}

	@Override
	public void removeFileHistory(String fileHistoryId)
	{
		Objects.requireNonNull(fileHistoryId, "File History Id is required");

		FileHistory fileHistory = persistenceService.findById(FileHistory.class, fileHistoryId);

		FileHistoryError fileHistoryError = new FileHistoryError();
		fileHistoryError.setFileHistoryId(fileHistoryId);
		persistenceService.deleteByExample(fileHistoryError);

		if (fileHistory != null) {
			String filename = fileHistory.getFilename();
			String originalFilename = fileHistory.getOriginalFilename();

			//remove file
			Path path = fileHistory.pathToFileName();
			if (path != null) {
				path.toFile().delete();
			}
			persistenceService.delete(fileHistory);

			String removalUser = OpenStorefrontConstant.SYSTEM_USER;
			if (SecurityUtil.isLoggedIn()) {
				removalUser = SecurityUtil.getCurrentUserName();
			}
			log.log(Level.FINE, MessageFormat.format("File History removed by user: {0} filename: {1} Orginal name: {2}", new Object[]{removalUser, filename, originalFilename}));
		}
	}

	@Override
	public List<FileFormat> findFileFormats(String fileType)
	{
		List<FileFormat> fileFormats = getLookupService().findLookup(FileFormat.class);
		if (StringUtils.isNotBlank(fileType)) {
			fileFormats = fileFormats.stream().filter(f -> f.getFileType().equals(fileType)).collect(Collectors.toList());
		}
		return fileFormats;
	}

	@Override
	public void cleanupOldFileHistory()
	{
		int maxDays = Convert.toInteger(PropertiesManager.getValue(PropertiesManager.KEY_FILE_HISTORY_KEEP_DAYS, "30"));

		LocalDateTime archiveTime = LocalDateTime.now();
		archiveTime = archiveTime.minusDays(maxDays);
		archiveTime = archiveTime.truncatedTo(ChronoUnit.DAYS);
		String deleteQuery = "updateDts < :maxUpdateDts";

		ZonedDateTime zdt = archiveTime.atZone(ZoneId.systemDefault());
		Date archiveDts = Date.from(zdt.toInstant());

		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("maxUpdateDts", archiveDts);

		persistenceService.deleteByQuery(FileHistory.class, deleteQuery, queryParams);
	}

}
