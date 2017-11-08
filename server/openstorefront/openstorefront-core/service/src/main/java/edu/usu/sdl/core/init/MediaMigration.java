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
package edu.usu.sdl.core.init;

import edu.usu.sdl.core.CoreSystem;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.api.PersistenceService;
import edu.usu.sdl.openstorefront.core.entity.ComponentMedia;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.ContentSectionMedia;
import edu.usu.sdl.openstorefront.core.entity.GeneralMedia;
import edu.usu.sdl.openstorefront.core.entity.MediaFile;
import edu.usu.sdl.openstorefront.core.entity.MediaModel;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.util.MediaFileType;
import edu.usu.sdl.openstorefront.service.manager.JobManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author cyearsley
 */
public class MediaMigration extends ApplyOnceInit
{
	private static final Logger LOG = Logger.getLogger(MediaMigration.class.getName());
	protected PersistenceService persistenceService = service.getNewPersistenceService();
	
	public MediaMigration()
	{
		super("MEDIA-MIGRATION-2.5");
	}
	
	private static int noFileCount = 0;
	private static int failedDeleteCount = 0;
	private static int failedFileReadCount = 0;
	
	@Override
	protected String internalApply()
	{
		StringBuilder results = new StringBuilder();
		CoreSystem.standby("Running media data migration");
		JobManager.pauseScheduler();
		
		//	Loop through all of the following data models, and transfer mimeType, origFilename to MediaFile
		//		MediaFile will have it's own filename (newly generated).
		//		Don't forget to rename the old filenames (in the file system), to the newly generated MediaFile filename
		
		/*************************************/
		//	Migrate data from General Media
		/*************************************/
		List<GeneralMedia> generalMedias = new GeneralMedia().findByExampleProxy();
		for (GeneralMedia generalMedia : generalMedias) {
			
			if (generalMedia.getFileName() != null) {
				Path originalFilePath = Paths.get(MediaFileType.GENERAL.getPath() + "\\" + generalMedia.getFileName());

				GeneralMedia newGeneralMedia = new GeneralMedia();
				
				newGeneralMedia.setCreateDts(generalMedia.getCreateDts());
				newGeneralMedia.setCreateUser(generalMedia.getCreateUser());
				newGeneralMedia.setDataSensitivity(generalMedia.getDataSensitivity());
				newGeneralMedia.setName(generalMedia.getName());
				newGeneralMedia.setSecurityMarkingType(generalMedia.getSecurityMarkingType());
				
				newGeneralMedia.setFileName(null);
				newGeneralMedia.setMimeType(null);
				newGeneralMedia.setOriginalFileName(null);
				newGeneralMedia.populateBaseUpdateFields();
				
				MediaFile newMediaFile = saveMediaFile(null, originalFilePath, generalMedia.getMimeType(), generalMedia.getFileName(), MediaFileType.GENERAL, newGeneralMedia);
				newGeneralMedia.setFile(newMediaFile);
				persistenceService.delete(generalMedia);
				persistenceService.persist(newGeneralMedia);
			}
		}
		
		/***************************************/
		//	Migrate data from Component Media
		/***************************************/
		List<ComponentMedia> componentMedias = new ComponentMedia().findByExampleProxy();
		for (ComponentMedia componentMedia : componentMedias) {
			
			if (componentMedia.getFileName() != null) {
				Path originalFilePath = Paths.get(MediaFileType.MEDIA.getPath() + "\\" + componentMedia.getFileName());
				MediaFile newMediaFile = saveMediaFile(null, originalFilePath, componentMedia.getMimeType(), componentMedia.getFileName(), MediaFileType.MEDIA, componentMedia);
				componentMedia.setFile(newMediaFile);

				componentMedia.setFileName(null);
				componentMedia.setMimeType(null);
				componentMedia.setOriginalName(null);
				persistenceService.persist(componentMedia);
			}
		}
		
		/***************************************/
		//	Migrate data from Component Resource
		/***************************************/
		List<ComponentResource> componentResources = new ComponentResource().findByExampleProxy();
		for (ComponentResource componentResource : componentResources) {
			
			if (componentResource.getFileName() != null) {
				Path originalFilePath = Paths.get(MediaFileType.RESOURCE.getPath() + "\\" + componentResource.getFileName());
				MediaFile newMediaFile = saveMediaFile(null, originalFilePath, componentResource.getMimeType(), componentResource.getFileName(), MediaFileType.RESOURCE, componentResource);
				componentResource.setFile(newMediaFile);

				componentResource.setFileName(null);
				componentResource.setMimeType(null);
				componentResource.setOriginalName(null);
				persistenceService.persist(componentResource);
			}
		}
		
		/******************************************/
		//	Migrate data from Content Section Media
		/******************************************/
		List<ContentSectionMedia> contentSectionMedias = new ContentSectionMedia().findByExampleProxy();
		for (ContentSectionMedia sectionMedia : contentSectionMedias) {
			
			if (sectionMedia.getFileName() != null) {
				Path originalFilePath = Paths.get(MediaFileType.MEDIA.getPath() + "\\" + sectionMedia.getFileName());
				MediaFile newMediaFile = saveMediaFile(null, originalFilePath, sectionMedia.getMimeType(), sectionMedia.getFileName(), MediaFileType.MEDIA, sectionMedia);
				sectionMedia.setFile(newMediaFile);

				sectionMedia.setFileName(null);
				sectionMedia.setMimeType(null);
				sectionMedia.setOriginalName(null);
				persistenceService.persist(sectionMedia);
			}
		}
		
		JobManager.resumeScheduler();
		CoreSystem.resume("Completed media data migration");
		
		if (noFileCount > 0) {
			results.append("Number of files that couldn't be located: ").append(noFileCount);
		}
		if (failedDeleteCount > 0) {
			results.append("<br />Number of files that failed to delete: ").append(failedDeleteCount);
		}
		if (failedFileReadCount > 0) {
			results.append("<br />Number of files that failed to read or saved: ").append(failedFileReadCount);
		}
		
		return results.toString();
	}
	
	@Override
	public int getPriority()
	{
		return 9999;
	}
	
	public <T extends StandardEntity & MediaModel> MediaFile saveMediaFile(MediaFile media, Path sourcePath, String mimeType, String originalFileName, MediaFileType type, T mediaToPersist)
	{
		Objects.requireNonNull(sourcePath);
		if (media == null) {
			media = new MediaFile();
		}
		if (StringUtils.isBlank(media.getMediaFileId())) {
			media.setMediaFileId(persistenceService.generateId());
		}
		media.setFileName(persistenceService.generateId() + OpenStorefrontConstant.getFileExtensionForMime(mimeType));
		media.setMimeType(mimeType);
		media.setOriginalName(originalFileName);
		media.setFileType(type);

		Path targetPath = Paths.get(type.getPath() + "/" + media.getFileName());
		
		mediaToPersist.setFile(media);
		
		try {
			Files.move(sourcePath, targetPath);
			persistenceService.persist(mediaToPersist);
		} catch (IOException ex) {
			// Mention class name, source and target paths
			LOG.log(Level.SEVERE, null, ex);
		}
		
		return media;
	}
}
