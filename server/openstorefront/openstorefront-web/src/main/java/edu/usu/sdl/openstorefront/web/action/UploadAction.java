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
package edu.usu.sdl.openstorefront.web.action;

import au.com.bytecode.opencsv.CSVReader;
import com.fasterxml.jackson.core.type.TypeReference;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.api.model.TaskRequest;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.FileHistory;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryOption;
import edu.usu.sdl.openstorefront.core.entity.IODirectionType;
import edu.usu.sdl.openstorefront.core.entity.LookupEntity;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.SystemArchive;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.model.DataMapModel;
import edu.usu.sdl.openstorefront.core.model.FileFormatCheck;
import edu.usu.sdl.openstorefront.core.model.ImportContext;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.FieldDefinition;
import edu.usu.sdl.openstorefront.core.view.JsonResponse;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.io.parser.MainAttributeParser;
import edu.usu.sdl.openstorefront.service.io.parser.OldBaseAttributeParser;
import edu.usu.sdl.openstorefront.service.manager.DBManager;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileInputStream;
import net.java.truevfs.access.TVFS;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class UploadAction
		extends BaseAction
{

	private static final Logger LOG = Logger.getLogger(UploadAction.class.getName());

	@Validate(required = true, on = {
		"UploadLookup",
		"UploadComponent",
		"UploadArticles",
		"UploadAttributes",
		"UploadSvcv4",
		"UploadPlugin",
		"ImportData",
		"DataMapFields",
		"PreviewMapping",
		"ImportMapping"
	})
	private FileBean uploadFile;

	@Validate(required = true, on = "UploadLookup")
	private String entityName;

	private FileHistoryOption componentUploadOptions = new FileHistoryOption();

	@Validate(required = true, on = "ImportData")
	private String fileType;

	@Validate(required = true, on = {"ImportData", "DataMapFields", "PreviewMapping"})
	private String fileFormat;

	@Validate(required = true, on = {"PreviewMapping"})
	private String dataMappingId;
	private String dataSource;

	@Validate(required = true, on = "AttributeCodeAttachment")
	private String attributeTypeName;
	private String attributeCodeName;
	private AttributeCode attributeCode;

	@Validate(required = true, on = {"ImportArchive"})
	private String importModeType;

	@RequireSecurity(SecurityPermission.ADMIN_LOOKUPS)
	@HandlesEvent("UploadLookup")
	@SuppressWarnings("UseSpecificCatch")
	public Resolution uploadLookup()
	{
		Map<String, String> errors = new HashMap<>();

		//log
		LOG.log(Level.INFO, MessageFormat.format("(Admin) Uploading lookup: {0}", uploadFile));

		//check content type
		Set<String> allowTypes = new HashSet<>();
		allowTypes.add("text/csv");
		allowTypes.add("application/vnd.ms-excel");
		allowTypes.add("application/vnd.oasis.opendocument.spreadsheet");

		if (allowTypes.contains(uploadFile.getContentType()) == false) {
			errors.put("uploadFile", "Format not supported.  Requires a csv text file.");
		}

		if (errors.isEmpty()) {
			//parse
			List<LookupEntity> lookupEntities = new ArrayList<>();

			Class lookupClass = null;
			StringBuilder errorsMessages = new StringBuilder();
			try (CSVReader reader = new CSVReader(new InputStreamReader(uploadFile.getInputStream()))) {

				lookupClass = Class.forName(DBManager.ENTITY_MODEL_PACKAGE + "." + entityName);
				List<String[]> allData = reader.readAll();
				for (String data[] : allData) {
					try {
						LookupEntity lookupEntity = (LookupEntity) lookupClass.newInstance();
						lookupEntity.importData(data);
						lookupEntities.add(lookupEntity);
					} catch (Exception e) {
						errorsMessages.append(MessageFormat.format(e.toString() + " -  Unable Process line: {0}", new Object[]{Arrays.toString(data)}));
					}
				}
				if (errorsMessages.length() > 0) {
					errors.put("uploadFile", errorsMessages.toString());
				}
			} catch (IOException ex) {
				throw new OpenStorefrontRuntimeException("Unable to read file: " + uploadFile.getFileName(), ex);
			} catch (ClassNotFoundException ex) {
				errors.put("entityName", "Unable to find Lookup Class:  " + entityName);
			} finally {
				try {
					uploadFile.delete();
				} catch (IOException ex) {
					LOG.log(Level.WARNING, "Unable delete temp file.", ex);
				}
			}

			if (errors.isEmpty()) {
				if (lookupClass != null) {
					service.getLookupService().syncLookupImport(lookupClass, lookupEntities);
				}
			}
		}
		return streamUploadResponse(errors);
	}

	@RequireSecurity(SecurityPermission.ADMIN_ATTRIBUTE_MANAGEMENT)
	@HandlesEvent("UploadAttributes")
	public Resolution uploadAttribute()
	{
		return handleAttributeUpload(new MainAttributeParser());
	}

	private Resolution handleAttributeUpload(OldBaseAttributeParser parser)
	{
		Map<String, String> errors = new HashMap<>();

		//log
		LOG.log(Level.INFO, MessageFormat.format("(Admin) Uploading attributes: {0}", uploadFile));

		Set<String> allowAttrbuteTypes = new HashSet<>();
		allowAttrbuteTypes.add("text/json");
		allowAttrbuteTypes.add("application/json");

		String bestGuessContentType = uploadFile.getContentType();
		if (allowAttrbuteTypes.contains(bestGuessContentType) == false) {

			bestGuessContentType = getContext().getServletContext().getMimeType(uploadFile.getFileName());
			if (allowAttrbuteTypes.contains(bestGuessContentType) == false) {
				errors.put("uploadFile", "Format not supported.  Requires a JSON text file.");
			}
		}

		if (errors.isEmpty()) {

			try (InputStream in = uploadFile.getInputStream()) {
				Map<AttributeType, List<AttributeCode>> attributeMap = parser.parse(in);

				TaskRequest taskRequest = new TaskRequest();
				taskRequest.setAllowMultiple(false);
				taskRequest.setName("Uploading Attribute(s)");
				taskRequest.setDetails("File name: " + uploadFile.getFileName());
				service.getAsyncProxy(service.getAttributeService(), taskRequest).syncAttribute(attributeMap);
			} catch (IOException ex) {
				throw new OpenStorefrontRuntimeException("Unable to read file: " + uploadFile.getFileName(), ex);
			} finally {
				try {
					uploadFile.delete();
				} catch (IOException ex) {
					LOG.log(Level.WARNING, "Unable delete temp file.", ex);
				}
			}
		}
		return streamUploadResponse(errors);
	}

	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
	@HandlesEvent("UploadComponent")
	public Resolution uploadComponent()
	{
		Map<String, String> errors = new HashMap<>();

		LOG.log(Level.INFO, SecurityUtil.adminAuditLogMessage(getContext().getRequest()));

		try {
			Set<String> allowTextTypes = new HashSet<>();
			Set<String> allowZipTypes = new HashSet<>();
			allowTextTypes.add("text/json");
			allowTextTypes.add("text");
			allowTextTypes.add("application/json");
			allowZipTypes.add("application/zip");
			allowZipTypes.add("application/x-zip-compressed");

			//This comes from the client
			String bestGuessContentType = uploadFile.getContentType();
			if (allowTextTypes.contains(bestGuessContentType) == false
					&& allowZipTypes.contains(bestGuessContentType) == false) {

				//check extentsion (this comes from the server config...based on the file extension)
				bestGuessContentType = getContext().getServletContext().getMimeType(uploadFile.getFileName());
				if (allowTextTypes.contains(bestGuessContentType) == false
						&& allowZipTypes.contains(bestGuessContentType) == false) {
					errors.put("uploadFile", "Format not supported.  Requires json text file or zip file");
				}
			}

			if (errors.isEmpty()) {
				List<ComponentAll> components = new ArrayList<>();
				if (allowTextTypes.contains(bestGuessContentType)) {
					try (InputStream in = uploadFile.getInputStream()) {
						components = StringProcessor.defaultObjectMapper().readValue(in, new TypeReference<List<ComponentAll>>()
						{
						});
					} catch (IOException ex) {
						throw ex;
					}
				} else {
					//zip handling
					File tempFile = new File(FileSystemManager.getDir(FileSystemManager.SYSTEM_TEMP_DIR) + "/" + System.currentTimeMillis() + "-Temp.zip");
					uploadFile.save(tempFile);
					TFile archive = new TFile(tempFile.getPath());
					TFile archiveFiles[] = archive.listFiles();
					if (archiveFiles != null) {
						for (TFile file : archiveFiles) {
							if (file.isFile()) {
								try (InputStream in = new TFileInputStream(file)) {
									components = StringProcessor.defaultObjectMapper().readValue(in, new TypeReference<List<ComponentAll>>()
									{
									});
								} catch (IOException ex) {
									throw ex;
								}
							} else if (file.isDirectory() && "media".equalsIgnoreCase(file.getName())) {
								TFile mediaFiles[] = file.listFiles();
								if (mediaFiles != null) {
									for (TFile mediaFile : mediaFiles) {
										Files.copy(mediaFile.toPath(), FileSystemManager.getDir(FileSystemManager.MEDIA_DIR).toPath().resolve(mediaFile.getName()), StandardCopyOption.REPLACE_EXISTING);
									}
								}
							} else if (file.isDirectory() && "resources".equalsIgnoreCase(file.getName())) {
								TFile resourcesFiles[] = file.listFiles();
								if (resourcesFiles != null) {
									for (TFile resourceFile : resourcesFiles) {
										Files.copy(resourceFile.toPath(), FileSystemManager.getDir(FileSystemManager.RESOURCE_DIR).toPath().resolve(resourceFile.getName()), StandardCopyOption.REPLACE_EXISTING);
									}
								}
							}
						}
					}

					//cleanup temp zip
					if (tempFile.delete() == false) {
						LOG.log(Level.WARNING, MessageFormat.format("Unable to remove temp upload file.  It can be safely removed from: {0}", tempFile.getPath()));
					}
				}

				TaskRequest taskRequest = new TaskRequest();
				taskRequest.setAllowMultiple(false);
				taskRequest.setName("Uploading Component(s)");
				taskRequest.setDetails("Component(s) Processing: " + components.size() + " from Filename: " + uploadFile.getFileName());
				service.getAsyncProxy(service.getComponentService(), taskRequest).importComponents(components, componentUploadOptions);
			}
		} catch (IOException ex) {
			LOG.log(Level.FINE, "Unable to read file: " + uploadFile.getFileName(), ex);
			errors.put("uploadFile", "Unable to read file: " + uploadFile.getFileName() + " Make sure the file in the proper format.");
		} finally {
			try {
				TVFS.umount();
			} catch (IOException ex) {
				LOG.log(Level.WARNING, "Unable to unmount tvfs", ex);
			}

			try {
				uploadFile.delete();
			} catch (IOException ex) {
				LOG.log(Level.WARNING, "Unable to remove temp upload file.", ex);
			}
		}
		return streamUploadResponse(errors);
	}

	@RequireSecurity(SecurityPermission.ADMIN_SYSTEM_MANAGEMENT)
	@HandlesEvent("UploadPlugin")
	public Resolution uploadPlugin()
	{
		Map<String, String> errors = new HashMap<>();
		LOG.log(Level.INFO, SecurityUtil.adminAuditLogMessage(getContext().getRequest()));
		try {

			// Store File Type
			String fileMimeType = uploadFile.getContentType();

			// Store File Name
			String fileName = uploadFile.getFileName();

			// Get Extension
			String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

			// Check MIME Type
			if ((fileMimeType.matches("application/(x-)?zip.*")
					|| MediaType.APPLICATION_OCTET_STREAM.equals(fileMimeType))
					&& (fileExtension.equals("war") || fileExtension.equals("jar"))) {

				//just copy plugin to  plugin directory...to avoid double pickup
				File pluginDir = FileSystemManager.getDir(FileSystemManager.PLUGIN_DIR);
				uploadFile.save(new File(pluginDir + "/" + StringProcessor.cleanFileName(uploadFile.getFileName())));
			} else {

				// Throw Invalid File Type Exception
				throw new InvalidFileTypeException("Invalid File Type");
			}
		} catch (IOException ex) {
			LOG.log(Level.FINE, "Unable to read file: " + uploadFile.getFileName(), ex);
			errors.put("uploadFile", "Unable to read file: " + uploadFile.getFileName() + " Make sure the file in the proper format.");
		} catch (InvalidFileTypeException ex) {
			LOG.log(Level.FINE, "Invalid file type: " + uploadFile.getFileName() + " ('" + uploadFile.getContentType() + "')", ex);
			errors.put("uploadFile", "Unable to save invalid file: " + uploadFile.getFileName() + ".");
			errors.put("uploadFileType", " Invalid file type: " + uploadFile.getContentType() + ". Make sure the file is the proper format.");
		} catch (Exception ex) {
			LOG.log(Level.FINE, "Unable to upload file: " + uploadFile.getFileName(), ex);
			errors.put("uploadFile", "Unable to upload file: " + uploadFile.getFileName() + ". An unexpected error occurred.");
		} finally {
			try {
				uploadFile.delete();
			} catch (IOException ex) {
				LOG.log(Level.WARNING, "Unable to remove temp upload file.", ex);
			}
		}
		return streamUploadResponse(errors);
	}

	@RequireSecurity(SecurityPermission.ADMIN_ATTRIBUTE_MANAGEMENT)
	@HandlesEvent("AttributeCodeAttachment")
	public Resolution uploadAttributeCodeAttachment()
	{
		Map<String, String> errors = new HashMap<>();
		LOG.log(Level.INFO, SecurityUtil.adminAuditLogMessage(getContext().getRequest()));

		AttributeCodePk attributeCodePk = new AttributeCodePk();
		attributeCodePk.setAttributeType(attributeTypeName);
		attributeCodePk.setAttributeCode(attributeCodeName);
		attributeCode = service.getPersistenceService().findById(AttributeCode.class, attributeCodePk);

		if (attributeCode != null) {
			attributeCode.setAttachmentOriginalFileName(StringProcessor.getJustFileName(uploadFile.getFileName()));
			attributeCode.setAttachmentMimeType(uploadFile.getContentType());

			ValidationModel validationModel = new ValidationModel(attributeCode);
			validationModel.setConsumeFieldsOnly(true);
			ValidationResult validationResult = ValidationUtil.validate(validationModel);
			if (validationResult.valid()) {
				try {
					service.getAttributeService().saveAttributeCodeAttachment(attributeCode, uploadFile.getInputStream());
				} catch (IOException ex) {
					throw new OpenStorefrontRuntimeException("Unable to able to save media.", "Contact System Admin. Check disk space and permissions.", ex);
				} finally {
					deleteUploadFile(uploadFile);
				}
			} else {
				errors.put("file", validationResult.toHtmlString());
			}
		} else {
			errors.put("attributeCode", "AttributeCode doesn't seem to exist.");
		}
		return streamUploadResponse(errors);
	}

	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
	@HandlesEvent("ImportData")
	public Resolution importData()
	{
		Map<String, String> errors = new HashMap<>();
		LOG.log(Level.INFO, SecurityUtil.adminAuditLogMessage(getContext().getRequest()));

		File tempFile = null;
		try {
			tempFile = File.createTempFile("import-", "tmp", FileSystemManager.getDir(FileSystemManager.MAIN_TEMP_DIR));
			uploadFile.save(tempFile);

			try (InputStream input = new FileInputStream(tempFile)) {
				FileFormatCheck fileFormatCheck = new FileFormatCheck();
				fileFormatCheck.setMimeType(uploadFile.getContentType());
				fileFormatCheck.setFileFormat(fileFormat);
				fileFormatCheck.setInput(input);

				String formatError = service.getImportService().checkFormat(fileFormatCheck);
				if (StringUtils.isNotBlank(formatError)) {
					errors.put("uploadFile", formatError);
				}
			} catch (IOException ioe) {
				throw ioe;
			}

			if (errors.isEmpty()) {
				ImportContext importContext = new ImportContext();
				importContext.setInput(new FileInputStream(tempFile));

				FileHistory fileHistory = new FileHistory();
				fileHistory.setMimeType(uploadFile.getContentType());
				if (StringUtils.isNotBlank(dataSource)) {
					fileHistory.setDataSource(dataSource);
				}
				fileHistory.setOriginalFilename(uploadFile.getFileName());
				fileHistory.setFileFormat(fileFormat);
				fileHistory.setFileHistoryOption(componentUploadOptions);
				fileHistory.setFileDataMapId(dataMappingId);
				importContext.getFileHistoryAll().setFileHistory(fileHistory);

				service.getImportService().importData(importContext);
			}

		} catch (IOException ex) {
			LOG.log(Level.FINE, "Unable to read file: " + uploadFile.getFileName(), ex);
			errors.put("uploadFile", "Unable to read file: " + uploadFile.getFileName() + " Make sure the file in the proper format.");
		} finally {
			try {
				if (uploadFile != null) {
					uploadFile.delete();
				}
				if (tempFile != null) {
					if (tempFile.delete()) {
						LOG.log(Level.WARNING, "Unable to remove temp upload file. OS will clean up.");
					}
				}
			} catch (IOException ex) {
				LOG.log(Level.WARNING, "Unable to remove temp upload file.", ex);
			}
		}

		return streamUploadResponse(errors);
	}

	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
	@HandlesEvent("DataMapFields")
	public Resolution mapFields()
	{
		Map<String, String> errors = new HashMap<>();
		LOG.log(Level.INFO, SecurityUtil.adminAuditLogMessage(getContext().getRequest()));

		List<FieldDefinition> fieldDefinitions = new ArrayList<>();

		try (InputStream in = uploadFile.getInputStream()) {
			fieldDefinitions = service.getImportService().getMapField(fileFormat, in);
		} catch (IOException ex) {
			errors.put("uploadFile", "Unable to read file: " + uploadFile.getFileName() + " Make sure the file in the proper format.");
		} finally {
			try {
				uploadFile.delete();
			} catch (IOException ex) {
				LOG.log(Level.WARNING, "Unable to remove temp upload file.", ex);
			}
		}

		return streamResults(fieldDefinitions, "text/html");
	}

	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
	@HandlesEvent("PreviewMapping")
	public Resolution previewMapping()
	{
		Map<String, String> errors = new HashMap<>();
		LOG.log(Level.INFO, SecurityUtil.adminAuditLogMessage(getContext().getRequest()));

		String output = "";
		try (InputStream in = uploadFile.getInputStream()) {
			output = service.getImportService().previewMapData(fileFormat, dataMappingId, in, uploadFile.getFileName());
		} catch (IOException ex) {
			output = "Unable to read file: " + uploadFile.getFileName() + " Make sure the file in the proper format.";
		} finally {
			try {
				uploadFile.delete();
			} catch (IOException ex) {
				LOG.log(Level.WARNING, "Unable to remove temp upload file.", ex);
			}
		}

		JsonResponse jsonResponse = new JsonResponse();
		jsonResponse.setErrors(errors);
		jsonResponse.setSuccess(true);
		jsonResponse.setMessage(output);

		return streamResults(jsonResponse);
	}

	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
	@HandlesEvent("ImportMapping")
	public Resolution importMapping()
	{
		Map<String, String> errors = new HashMap<>();
		LOG.log(Level.INFO, SecurityUtil.adminAuditLogMessage(getContext().getRequest()));

		try (InputStream in = uploadFile.getInputStream()) {

			DataMapModel dataMapModel = objectMapper.readValue(in, DataMapModel.class);
			ValidationResult validationResult = dataMapModel.validate();
			if (validationResult.valid()) {
				service.getImportService().saveFileDataMap(dataMapModel);
			} else {
				validationResult.addToErrors(errors);
			}

		} catch (IOException ex) {
			LOG.log(Level.FINEST, "Failed to read file.", ex);
			errors.put("uploadFile", "Unable to read file: " + uploadFile.getFileName() + " Make sure the file in the proper format.");
		} finally {
			try {
				uploadFile.delete();
			} catch (IOException ex) {
				LOG.log(Level.WARNING, "Unable to remove temp upload file.", ex);
			}
		}

		return streamErrorResponse(errors, true);
	}

	@RequireSecurity(SecurityPermission.ADMIN_SYSTEM_MANAGEMENT)
	@HandlesEvent("ImportArchive")
	public Resolution importArchive()
	{
		Map<String, String> errors = new HashMap<>();
		LOG.log(Level.INFO, SecurityUtil.adminAuditLogMessage(getContext().getRequest()));

		SystemArchive systemArchive = new SystemArchive();
		systemArchive.setName(StringProcessor.getJustFileName(uploadFile.getFileName()));
		systemArchive.setOriginalArchiveFilename(uploadFile.getFileName());
		systemArchive.setImportModeType(importModeType);
		systemArchive.setIoDirectionType(IODirectionType.IMPORT);

		systemArchive.setArchiveFilename("import-" + service.getPersistenceService().generateId() + ".zip");
		File archiveDir = FileSystemManager.getDir(FileSystemManager.ARCHIVE_DIR);
		Path path = Paths.get(archiveDir.getPath() + "/" + systemArchive.getArchiveFilename());
		File fullArchive = path.toFile();

		try {

			uploadFile.save(fullArchive);
			service.getSystemArchiveService().queueArchiveRequest(systemArchive);

		} catch (IOException ex) {
			LOG.log(Level.FINEST, "Failed to read file.", ex);
			errors.put("uploadFile", "Unable to read file: " + uploadFile.getFileName() + " Make sure the file in the proper format.");
		} finally {
			try {
				uploadFile.delete();
			} catch (IOException ex) {
				LOG.log(Level.WARNING, "Unable to remove temp upload file.", ex);
			}
		}

		return streamErrorResponse(errors, true);
	}

	public FileBean getUploadFile()
	{
		return uploadFile;
	}

	public void setUploadFile(FileBean uploadFile)
	{
		this.uploadFile = uploadFile;
	}

	public String getEntityName()
	{
		return entityName;
	}

	public void setEntityName(String entityName)
	{
		this.entityName = entityName;
	}

	public FileHistoryOption getComponentUploadOptions()
	{
		return componentUploadOptions;
	}

	public void setComponentUploadOptions(FileHistoryOption componentUploadOptions)
	{
		this.componentUploadOptions = componentUploadOptions;
	}

	public String getFileType()
	{
		return fileType;
	}

	public void setFileType(String fileType)
	{
		this.fileType = fileType;
	}

	public String getFileFormat()
	{
		return fileFormat;
	}

	public void setFileFormat(String fileFormat)
	{
		this.fileFormat = fileFormat;
	}

	public String getDataMappingId()
	{
		return dataMappingId;
	}

	public void setDataMappingId(String dataMappingId)
	{
		this.dataMappingId = dataMappingId;
	}

	public String getDataSource()
	{
		return dataSource;
	}

	public void setDataSource(String dataSource)
	{
		this.dataSource = dataSource;
	}

	public String getAttributeTypeName()
	{
		return attributeTypeName;
	}

	public void setAttributeTypeName(String attributeTypeName)
	{
		this.attributeTypeName = attributeTypeName;
	}

	public String getAttributeCodeName()
	{
		return attributeCodeName;
	}

	public void setAttributeCodeName(String attributeCodeName)
	{
		this.attributeCodeName = attributeCodeName;
	}

	private class InvalidFileTypeException extends Exception
	{

		public InvalidFileTypeException(String message)
		{

			super(message);
		}
	}

	public String getImportModeType()
	{
		return importModeType;
	}

	public void setImportModeType(String importModeType)
	{
		this.importModeType = importModeType;
	}

}
