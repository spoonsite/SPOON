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
import edu.usu.sdl.openstorefront.common.util.RetryUtil;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.api.ImportService;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentVersionHistory;
import edu.usu.sdl.openstorefront.core.entity.FileAttributeMap;
import edu.usu.sdl.openstorefront.core.entity.FileDataMap;
import edu.usu.sdl.openstorefront.core.entity.FileFormat;
import edu.usu.sdl.openstorefront.core.entity.FileHistory;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryError;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryErrorType;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryOption;
import edu.usu.sdl.openstorefront.core.entity.ModificationType;
import edu.usu.sdl.openstorefront.core.entity.NotificationEvent;
import edu.usu.sdl.openstorefront.core.entity.NotificationEventType;
import edu.usu.sdl.openstorefront.core.model.ComponentRestoreOptions;
import edu.usu.sdl.openstorefront.core.model.DataMapModel;
import edu.usu.sdl.openstorefront.core.model.ExternalFormat;
import edu.usu.sdl.openstorefront.core.model.FileFormatCheck;
import edu.usu.sdl.openstorefront.core.model.FileHistoryAll;
import edu.usu.sdl.openstorefront.core.model.ImportContext;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.sort.LookupComparator;
import edu.usu.sdl.openstorefront.core.spi.parser.AbstractParser;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.FieldDefinition;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.MapModel;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.MappableReader;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.api.ImportServicePrivate;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ImportServiceImpl
		extends ServiceProxy
		implements ImportService, ImportServicePrivate
{

	private static final Logger LOG = Logger.getLogger(ImportServiceImpl.class.getName());

	private static final String FORMATS_KEY = "ADDITIONAL-FILEFORMATS";

	@Override
	public String importData(ImportContext importContext)
	{
		Objects.requireNonNull(importContext.getFileHistoryAll().getFileHistory());

		//create history record
		FileHistory fileHistory = importContext.getFileHistoryAll().getFileHistory();
		fileHistory.setFileHistoryId(getPersistenceService().generateId());
		String extension = StringProcessor.getFileExtension(importContext.getFileHistoryAll().getFileHistory().getOriginalFilename());
		fileHistory.setFilename(fileHistory.getFileHistoryId() + "." + extension);
		fileHistory.setRecordsStored(0);
		if (fileHistory.getFileHistoryOption() == null) {
			fileHistory.setFileHistoryOption(new FileHistoryOption());
		}

		try (InputStream in = importContext.getInput(); OutputStream out = new FileOutputStream(fileHistory.pathToFileName().toFile())) {
			FileSystemManager.getInstance().copy(in, out);
		} catch (IOException e) {
			throw new OpenStorefrontRuntimeException("Unable to save upload.", "Check file system permissions and disk space.", e);
		}

		//save data (close stream)
		//queue upload so it can be batched
		FileHistory historySaved = saveFileHistory(importContext.getFileHistoryAll());

		return historySaved.getFileHistoryId();
	}

	@Override
	public String checkFormat(FileFormatCheck formatCheck)
	{
		Objects.requireNonNull(formatCheck);

		ExternalFormat externalFormat = handleFindFileFormat(formatCheck.getFileFormat());

		StringBuilder errors = new StringBuilder();
		try (InputStream in = formatCheck.getInput()) {
			Class parserClass = externalFormat.getParsingClass();
			if (parserClass == null) {
				parserClass = this.getClass().getClassLoader().loadClass(externalFormat.getFileFormat().getParserClass());
			}
			AbstractParser abstractParser = (AbstractParser) parserClass.newInstance();
			String message = abstractParser.checkFormat(formatCheck.getMimeType(), in);
			if (StringUtils.isNotBlank(message)) {
				errors.append(message);
			}
		} catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			LOG.log(Level.SEVERE, "Unable to load parser class: " + externalFormat.getFileFormat().getParserClass(), e);
			errors.append("(System Error)File Format not supported.");
		}
		return StringUtils.isNotBlank(errors.toString()) ? errors.toString() : null;
	}

	@Override
	public void reprocessFile(String fileHistoryId)
	{
		FileHistory fileHistory = getPersistenceService().findById(FileHistory.class, fileHistoryId);
		if (fileHistory == null) {
			throw new OpenStorefrontRuntimeException("Unable to find file history.", "Check id: " + fileHistoryId + " it may have been deleted.");
		}

		FileHistoryError fileHistoryError = new FileHistoryError();
		fileHistoryError.setFileHistoryId(fileHistoryId);
		getPersistenceService().deleteByExample(fileHistoryError);

		fileHistory.setStartDts(null);
		fileHistory.setCompleteDts(null);
		fileHistory.setNumberRecords(0);
		fileHistory.setRecordsProcessed(0);
		fileHistory.setRecordsStored(0);
		fileHistory.populateBaseUpdateFields();
		getPersistenceService().persist(fileHistory);
		LOG.log(Level.FINE, "Queued:  {0} to be reprocessed.", fileHistory.getOriginalFilename());
	}

	private ExternalFormat handleFindFileFormat(String fileFormatCode)
	{
		return handleFindFileFormat(fileFormatCode, true);
	}

	private ExternalFormat handleFindFileFormat(String fileFormatCode, boolean errorOnNotFound)
	{
		ExternalFormat externalFormat = new ExternalFormat();

		FileFormat fileFormat = getLookupService().getLookupEnity(FileFormat.class, fileFormatCode);

		if (fileFormat == null) {
			Element element = OSFCacheManager.getApplicationCache().get(FORMATS_KEY);
			if (element != null) {
				@SuppressWarnings("unchecked")
				List<ExternalFormat> extraFileFormats = (List<ExternalFormat>) element.getObjectValue();
				for (ExternalFormat pluginFormat : extraFileFormats) {
					if (fileFormatCode.equals(pluginFormat.getFileFormat().getCode())) {
						externalFormat = pluginFormat;
					}
				}
			}
		} else {
			externalFormat.setFileFormat(fileFormat);
		}
		if (errorOnNotFound && externalFormat.getFileFormat() == null) {
			throw new OpenStorefrontRuntimeException("Unable to find format.  File Format: " + fileFormatCode, "Make sure format is loaded (Maybe a loaded from a plugin)");
		}
		return externalFormat;
	}

	@Override
	public void processImport(String fileHistoryId)
	{
		FileHistoryAll fileHistoryAll = new FileHistoryAll();
		FileHistory fileHistory = getPersistenceService().findById(FileHistory.class, fileHistoryId);
		if (fileHistory == null) {
			throw new OpenStorefrontRuntimeException("Unable to find file history.", "Check id: " + fileHistoryId + " it may have been deleted.");
		}
		fileHistoryAll.setFileHistory(fileHistory);

		fileHistory.setStartDts(TimeUtil.currentDate());
		fileHistory.populateBaseUpdateFields();
		fileHistory = getPersistenceService().persist(fileHistory);

		//Get Parser for format
		ExternalFormat externalFormat = handleFindFileFormat(fileHistory.getFileFormat());

		if (fileHistory.getFileDataMapId() != null) {
			fileHistoryAll.setDataMapModel(getDataMap(fileHistory.getFileDataMapId()));
		}

		try {
			Class parserClass = externalFormat.getParsingClass();
			if (parserClass == null) {
				parserClass = this.getClass().getClassLoader().loadClass(externalFormat.getFileFormat().getParserClass());
			}
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
			LOG.log(Level.SEVERE, "Unable to load parser class: " + externalFormat.getFileFormat().getParserClass(), e);
			fileHistoryAll.addError(FileHistoryErrorType.SYSTEM, "Unable to load parser class: " + externalFormat.getFileFormat().getParserClass());
		}

		fileHistory.setCompleteDts(TimeUtil.currentDate());
		saveFileHistory(fileHistoryAll);
	}

	@Override
	public FileHistory saveFileHistory(FileHistoryAll fileHistoryAll)
	{
		Objects.requireNonNull(fileHistoryAll);
		Objects.requireNonNull(fileHistoryAll.getFileHistory());
		Objects.requireNonNull(fileHistoryAll.getErrors());

		FileHistory fileHistory = getPersistenceService().findById(FileHistory.class, fileHistoryAll.getFileHistory().getFileHistoryId());
		if (fileHistory != null) {
			RetryUtil.retryAction(2, () -> {
				FileHistory fileHistoryLocal = getPersistenceService().findById(FileHistory.class, fileHistoryAll.getFileHistory().getFileHistoryId());
				fileHistoryLocal.updateFields(fileHistoryAll.getFileHistory());
				getPersistenceService().persist(fileHistoryLocal);
			});
		} else {
			if (StringUtils.isBlank(fileHistoryAll.getFileHistory().getFileHistoryId())) {
				fileHistoryAll.getFileHistory().setFileHistoryId(getPersistenceService().generateId());
			}
			fileHistoryAll.getFileHistory().populateBaseCreateFields();
			fileHistory = getPersistenceService().persist(fileHistoryAll.getFileHistory());
		}

		//replace errors
		FileHistoryError fileHistoryError = new FileHistoryError();
		fileHistoryError.setFileHistoryId(fileHistory.getFileHistoryId());
		getPersistenceService().deleteByExample(fileHistoryError);

		for (FileHistoryError error : fileHistoryAll.getErrors()) {
			error.setFileHistoryErrorId(getPersistenceService().generateId());
			error.setFileHistoryId(fileHistory.getFileHistoryId());
			error.populateBaseCreateFields();
			getPersistenceService().persist(error);
		}

		return fileHistory;
	}

	@Override
	public void removeFileHistory(String fileHistoryId)
	{
		Objects.requireNonNull(fileHistoryId, "File History Id is required");

		FileHistory fileHistory = getPersistenceService().findById(FileHistory.class, fileHistoryId);

		FileHistoryError fileHistoryError = new FileHistoryError();
		fileHistoryError.setFileHistoryId(fileHistoryId);
		getPersistenceService().deleteByExample(fileHistoryError);

		if (fileHistory != null) {
			String filename = fileHistory.getFilename();
			String originalFilename = fileHistory.getOriginalFilename();

			//remove file
			Path path = fileHistory.pathToFileName();
			if (path != null) {
				if (!path.toFile().delete()) {
					LOG.log(Level.WARNING, MessageFormat.format("Unable to delete file history raw input. Path: {0}", path.toString()));
				}
			}
			getPersistenceService().delete(fileHistory);

			String removalUser = OpenStorefrontConstant.SYSTEM_USER;
			if (SecurityUtil.isLoggedIn()) {
				removalUser = SecurityUtil.getCurrentUserName();
			}
			LOG.log(Level.FINE, MessageFormat.format("File History removed by user: {0} filename: {1} Orginal name: {2}", new Object[]{removalUser, filename, originalFilename}));
		}
	}

	@Override
	public FileFormat findFileFormat(String fileFormatCode)
	{
		ExternalFormat externalFormat = handleFindFileFormat(fileFormatCode, false);
		return externalFormat.getFileFormat();
	}

	@Override
	public List<FileFormat> findFileFormats(String fileType)
	{
		List<FileFormat> fileFormats = getLookupService().findLookup(FileFormat.class);
		if (StringUtils.isNotBlank(fileType)) {
			fileFormats = fileFormats.stream().filter(f -> f.getFileType().equals(fileType)).collect(Collectors.toList());
		}
		//also pull in fileformat and translate Class to path
		Element element = OSFCacheManager.getApplicationCache().get(FORMATS_KEY);
		if (element != null) {
			@SuppressWarnings("unchecked")
			List<ExternalFormat> extraFileFormats = (List<ExternalFormat>) element.getObjectValue();
			for (ExternalFormat externalFormat : extraFileFormats) {
				if (externalFormat.getFileFormat().getFileType().equals(fileType)) {
					FileFormat translatedFileFormat = new FileFormat();
					translatedFileFormat.setCode(externalFormat.getFileFormat().getCode());
					translatedFileFormat.setDescription(externalFormat.getFileFormat().getDescription());
					translatedFileFormat.setSupportsDataMap(externalFormat.getFileFormat().getSupportsDataMap());
					translatedFileFormat.setFileType(externalFormat.getFileFormat().getFileType());
					translatedFileFormat.setFileRequirements(externalFormat.getFileFormat().getFileRequirements());

					fileFormats.add(translatedFileFormat);
				}
			}
		}
		fileFormats.sort(new LookupComparator<>());

		return fileFormats;
	}

	@Override
	public List<FileFormat> getFileFormatsMapping()
	{
		List<FileFormat> fileFormats = getLookupService().findLookup(FileFormat.class);

		//also pull in fileformat and translate Class to path
		Element element = OSFCacheManager.getApplicationCache().get(FORMATS_KEY);
		if (element != null) {
			@SuppressWarnings("unchecked")
			List<ExternalFormat> extraFileFormats = (List<ExternalFormat>) element.getObjectValue();
			for (ExternalFormat externalFormat : extraFileFormats) {
				FileFormat translatedFileFormat = new FileFormat();
				translatedFileFormat.setCode(externalFormat.getFileFormat().getCode());
				translatedFileFormat.setDescription(externalFormat.getFileFormat().getDescription());
				translatedFileFormat.setSupportsDataMap(externalFormat.getFileFormat().getSupportsDataMap());
				translatedFileFormat.setFileType(externalFormat.getFileFormat().getFileType());
				translatedFileFormat.setFileRequirements(externalFormat.getFileFormat().getFileRequirements());

				fileFormats.add(translatedFileFormat);
			}
		}
		fileFormats = fileFormats.stream()
				.filter(FileFormat::getSupportsDataMap)
				.collect(Collectors.toList());

		fileFormats.sort(new LookupComparator<>());

		return fileFormats;
	}

	@Override
	public void cleanupOldFileHistory()
	{
		int maxDays = Convert.toInteger(PropertiesManager.getInstance().getValueDefinedDefault(PropertiesManager.KEY_FILE_HISTORY_KEEP_DAYS));

		LocalDateTime archiveTime = LocalDateTime.now();
		archiveTime = archiveTime.minusDays(maxDays);
		archiveTime = archiveTime.truncatedTo(ChronoUnit.DAYS);

		ZonedDateTime zdt = archiveTime.atZone(ZoneId.systemDefault());
		Date archiveDts = Date.from(zdt.toInstant());

		FileHistory fileHistoryExample = new FileHistory();
		FileHistory fileHistoryDateExample = new FileHistory();
		fileHistoryDateExample.setUpdateDts(archiveDts);

		QueryByExample<FileHistory> query = new QueryByExample<>(fileHistoryExample);

		SpecialOperatorModel<FileHistory> specialOperatorModel = new SpecialOperatorModel<>();
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN);
		specialOperatorModel.setExample(fileHistoryDateExample);
		query.getExtraWhereCauses().add(specialOperatorModel);

		getPersistenceService().deleteByExample(query);
	}

	@Override
	public void rollback(String fileHistoryId)
	{
		Objects.requireNonNull(fileHistoryId, "File History Id is required");

		FileHistory fileHistory = getPersistenceService().findById(FileHistory.class, fileHistoryId);
		if (fileHistory != null) {

			LOG.log(Level.INFO, MessageFormat.format("(Undo) Rolling back of import: {0}", TimeUtil.dateToString(fileHistory.getUpdateDts())));

			//find records with batch id  (For now only components )
			Component componentExample = new Component();
			componentExample.setFileHistoryId(fileHistoryId);
			componentExample.setLastModificationType(ModificationType.IMPORT);

			List<Component> componentsToRollback = componentExample.findByExample();
			for (Component component : componentsToRollback) {
				//get versions/find previous to current file history
				ComponentVersionHistory componentVersionHistory = new ComponentVersionHistory();
				componentVersionHistory.setFileHistoryId(fileHistoryId);

				List<ComponentVersionHistory> versionHistories = getPersistenceService().queryByExample(componentVersionHistory);
				versionHistories.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, ComponentVersionHistory.FIELD_CREATE_DTS));

				ComponentVersionHistory batchVersion = null;
				ComponentVersionHistory previousVersion = null;
				boolean setPrevious = false;
				for (ComponentVersionHistory versionHistory : versionHistories) {
					if (fileHistoryId.equals(versionHistory.getFileHistoryId())) {
						batchVersion = versionHistory;
						setPrevious = true;
						continue;
					}

					if (setPrevious) {
						previousVersion = versionHistory;
						break;
					}
				}

				//restore or delete if there was no record
				if (batchVersion != null) {
					if (previousVersion != null) {
						ComponentRestoreOptions options = new ComponentRestoreOptions();
						getComponentService().restoreSnapshot(fileHistoryId, options);
					} else {
						getComponentService().cascadeDeleteOfComponent(component.getComponentId());
					}
				} else {
					LOG.log(Level.WARNING, MessageFormat.format("(Undo) Unable to undo import for component. No snapshot exists for component:   {0} FileHistory DTS: {1}", new Object[]{component.getName(), fileHistory.getCreateDts()}));
				}

			}

			removeFileHistory(fileHistoryId);
		} else {
			LOG.log(Level.WARNING, "File history doesn't exist unable to rollback.");
		}

	}

	@Override
	public Map<String, List<FileHistoryError>> fileHistoryErrorMap()
	{
		Map<String, List<FileHistoryError>> errorMap = new HashMap<>();

		FileHistoryError errorExample = new FileHistoryError();

		//REFACTOR: Note this isn't very efficient; Refactor to hold stats in the file record.
		List<FileHistoryError> results = errorExample.findByExample();
		for (FileHistoryError error : results) {

			if (errorMap.containsKey(error.getFileHistoryId())) {
				errorMap.get(error.getFileHistoryId()).add(error);
			} else {
				List<FileHistoryError> fileHistoryErrors = new ArrayList<>();
				fileHistoryErrors.add(error);
				errorMap.put(error.getFileHistoryId(), fileHistoryErrors);
			}
		}

		return errorMap;
	}

	@Override
	public FileDataMap saveFileDataMap(DataMapModel dataMapModel)
	{
		Objects.requireNonNull(dataMapModel.getFileDataMap());

		FileDataMap existingFileDataMap = getPersistenceService().findById(FileDataMap.class, dataMapModel.getFileDataMap().getFileDataMapId());
		if (existingFileDataMap != null) {
			existingFileDataMap.updateFields(dataMapModel.getFileDataMap());
			existingFileDataMap = getPersistenceService().persist(existingFileDataMap);
		} else {
			dataMapModel.getFileDataMap().setFileDataMapId(getPersistenceService().generateId());
			dataMapModel.getFileDataMap().populateBaseCreateFields();
			existingFileDataMap = getPersistenceService().persist(dataMapModel.getFileDataMap());
		}

		if (dataMapModel.getFileAttributeMap() != null) {
			//only allow one
			String fieldMapId = dataMapModel.getFileDataMap().getFileDataMapId();
			FileAttributeMap deleteFileAttributeMap = new FileAttributeMap();
			deleteFileAttributeMap.setFileDataMapId(fieldMapId);
			getPersistenceService().deleteByExample(deleteFileAttributeMap);

			dataMapModel.getFileAttributeMap().setFileAttributeMapId(getPersistenceService().generateId());
			dataMapModel.getFileAttributeMap().setFileDataMapId(fieldMapId);
			dataMapModel.getFileAttributeMap().populateBaseCreateFields();
			getPersistenceService().persist(dataMapModel.getFileAttributeMap());
		}

		return existingFileDataMap;
	}

	@Override
	public void removeFileDataMap(String fileDataMapId)
	{
		FileDataMap fileDataMap = getPersistenceService().findById(FileDataMap.class, fileDataMapId);
		if (fileDataMap != null) {

			//delete Attribute Maps if the exist
			FileAttributeMap fileAttributeMap = new FileAttributeMap();
			fileAttributeMap.setFileDataMapId(fileDataMapId);
			List<FileAttributeMap> attributeMaps = fileAttributeMap.findByExampleProxy();
			for (FileAttributeMap attributeMap : attributeMaps) {
				getPersistenceService().delete(attributeMap);
			}

			getPersistenceService().delete(fileDataMap);
		}
	}

	@Override
	public DataMapModel getDataMap(String fileDataMapId)
	{
		DataMapModel dataMapModel = null;
		FileDataMap fileDataMap = new FileDataMap();
		fileDataMap.setFileDataMapId(fileDataMapId);
		fileDataMap = fileDataMap.find();
		if (fileDataMap != null) {
			dataMapModel = new DataMapModel();
			dataMapModel.setFileDataMap(fileDataMap);

			FileAttributeMap fileAttributeMap = new FileAttributeMap();
			fileAttributeMap.setFileDataMapId(fileDataMapId);
			FileAttributeMap attributeMap = fileAttributeMap.find();
			dataMapModel.setFileAttributeMap(attributeMap);

		}
		return dataMapModel;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void registerFormat(FileFormat newFormat, Class parserClass)
	{
		Element element = OSFCacheManager.getApplicationCache().get(FORMATS_KEY);
		if (element == null) {
			List<ExternalFormat> fileFormats = new ArrayList<>();
			element = new Element(FORMATS_KEY, fileFormats);
			OSFCacheManager.getApplicationCache().put(element);
		}
		ExternalFormat externalFormat = new ExternalFormat();
		externalFormat.setFileFormat(newFormat);
		externalFormat.setParsingClass(parserClass);
		((List<ExternalFormat>) element.getObjectValue()).add(externalFormat);

		LOG.log(Level.FINE, MessageFormat.format("Registered new file format: {0}", newFormat.getDescription()));
	}

	@Override
	public void unregisterFormat(String fullClassPath)
	{
		Element element = OSFCacheManager.getApplicationCache().get(FORMATS_KEY);
		if (element != null) {
			@SuppressWarnings("unchecked")
			List<ExternalFormat> fileFormats = (List<ExternalFormat>) element.getObjectValue();
			for (int i = fileFormats.size() - 1; i >= 0; i--) {
				FileFormat fileFormat = fileFormats.get(i).getFileFormat();
				if (fileFormat.getParserClass().equals(fullClassPath)) {
					fileFormats.remove(i);
					LOG.log(Level.FINE, MessageFormat.format("Deregistered new file format: {0}", fileFormat.getDescription()));
				}
			}

			element = new Element(FORMATS_KEY, fileFormats);
			OSFCacheManager.getApplicationCache().put(element);
		} else {
			LOG.log(Level.WARNING, "Unable to find and internal format list. No format to unregister.");
		}
	}

	@Override
	public List<FieldDefinition> getMapField(String fileFormatCode, InputStream in)
	{
		List<FieldDefinition> fieldDefinitions = new ArrayList<>();

		ExternalFormat externalFormat = handleFindFileFormat(fileFormatCode);
		if (externalFormat.getFileFormat().getSupportsDataMap()) {
			try (InputStream processIn = in) {
				Class parserClass = externalFormat.getParsingClass();
				if (parserClass == null) {
					parserClass = this.getClass().getClassLoader().loadClass(externalFormat.getFileFormat().getParserClass());
				}
				AbstractParser abstractParser = (AbstractParser) parserClass.newInstance();
				MappableReader reader = abstractParser.getMappableReader(processIn);
				MapModel mapModel = reader.findFields(in);
				fieldDefinitions = mapModel.getUniqueFields();
			} catch (IOException ioe) {
				throw new OpenStorefrontRuntimeException("Unable to process file.  Data format not supported.", ioe);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				LOG.log(Level.SEVERE, "Unable to load parser: " + externalFormat.getFileFormat().getParserClass(), e);
			}
		} else {
			throw new OpenStorefrontRuntimeException("Format doesn't support data mapping.", "Check Format: " + fileFormatCode);
		}

		return fieldDefinitions;
	}

	@Override
	public void updateImportProgress(FileHistoryAll fileHistoryAll)
	{
		Objects.requireNonNull(fileHistoryAll);
		Objects.requireNonNull(fileHistoryAll.getFileHistory());

		FileHistory fileHistory = getPersistenceService().findById(FileHistory.class, fileHistoryAll.getFileHistory().getFileHistoryId());
		if (fileHistory != null) {
			fileHistory.updateFields(fileHistoryAll.getFileHistory());
			getPersistenceService().persist(fileHistory);
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find file history record to update.", "System error");
		}
	}

	@Override
	public String previewMapData(String fileFormatCode, String fileDataMapId, InputStream in, String filename)
	{
		String output;

		ExternalFormat externalFormat = handleFindFileFormat(fileFormatCode);
		if (externalFormat.getFileFormat().getSupportsDataMap()) {
			try (InputStream processIn = in) {
				Class parserClass = externalFormat.getParsingClass();
				if (parserClass == null) {
					parserClass = this.getClass().getClassLoader().loadClass(externalFormat.getFileFormat().getParserClass());
				}

				FileHistoryAll fileHistoryAll = new FileHistoryAll();
				fileHistoryAll.setDataMapModel(getDataMap(fileDataMapId));
				FileHistory history = new FileHistory();
				history.populateBaseCreateFields();
				history.setOriginalFilename(filename);
				fileHistoryAll.setFileHistory(history);

				AbstractParser abstractParser = (AbstractParser) parserClass.newInstance();
				output = abstractParser.previewProcessedData(fileHistoryAll, processIn);

			} catch (IOException ioe) {
				output = "Unable to process file.  Data format not supported. <br> <b>Trace: </b><br> " + StringProcessor.parseStackTraceHtml(ioe);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				output = "Unable to load parser: "
						+ externalFormat.getFileFormat().getParserClass()
						+ "<br> <b>Trace: </b><br>" + StringProcessor.parseStackTraceHtml(e);
			}
		} else {
			output = "Format doesn't support data mapping  Check Format: " + fileFormatCode;
		}

		return output;
	}

	@Override
	public FileDataMap copyDataMap(String fileDataMapId)
	{
		FileDataMap fileDataMap = null;

		DataMapModel dataMapModel = getDataMap(fileDataMapId);
		if (dataMapModel != null) {

			//Deattached copy
			FileDataMap copy = new FileDataMap();
			copy.setFileDataMapId(fileDataMapId);

			dataMapModel.setFileDataMap(copy.find());
			dataMapModel.getFileDataMap().setFileDataMapId(null);
			dataMapModel.getFileDataMap().setName(dataMapModel.getFileDataMap().getName() + " - Copy");
			if (dataMapModel.getFileAttributeMap() != null) {
				dataMapModel.getFileAttributeMap().setFileAttributeMapId(null);
				dataMapModel.getFileAttributeMap().setFileDataMapId(null);
			}
			fileDataMap = saveFileDataMap(dataMapModel);
		}
		return fileDataMap;
	}

}
