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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cyearsley
 */
public class MediaMigration extends ApplyOnceInit
{
	private static final Logger LOG = Logger.getLogger(MediaMigration.class.getName());
	protected PersistenceService persistenceService = service.getNewPersistenceService();
	private static final Map<String,String> movedFilesCache = new HashMap<>();
	private static int successfulMigrations = 0;
	private static int failedMigrations = 0;
	private static int skippedMigrations = 0;
	
	public MediaMigration()
	{
		super("MEDIA-MIGRATION-2.5");
	}
	
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
			
			if (generalMedia.getFileName() != null) { // this media needs to be migrated
				Path originalFilePath = Paths.get(MediaFileType.GENERAL.getPath() + "\\" + generalMedia.getFileName()); 

				GeneralMedia newGeneralMedia = new GeneralMedia();
				
				newGeneralMedia.setCreateDts(generalMedia.getCreateDts());
				newGeneralMedia.setCreateUser(generalMedia.getCreateUser());
				newGeneralMedia.setDataSensitivity(generalMedia.getDataSensitivity());
				newGeneralMedia.setName(generalMedia.getName());
				newGeneralMedia.setSecurityMarkingType(generalMedia.getSecurityMarkingType());
				
				newGeneralMedia.populateBaseUpdateFields();
				
				saveMediaFile(generalMedia, originalFilePath, generalMedia.getMimeType(), generalMedia.getFileName(), MediaFileType.GENERAL, newGeneralMedia);
				successfulMigrations += 1;
			}
			else { // this media has already been migrated
				if (generalMedia.getFile() != null) {
					LOG.log(Level.INFO, "Skipping duplicate migration for GeneralMedia file: " + generalMedia.getFile().getFileName());
					skippedMigrations += 1;
				}
			}
		}
		
		/***************************************/
		//	Migrate data from Component Media
		/***************************************/
		List<ComponentMedia> componentMedias = new ComponentMedia().findByExampleProxy();
		for (ComponentMedia componentMedia : componentMedias) {
			
			if (componentMedia.getFileName() != null) { // this media needs to be migrated
				Path originalFilePath = Paths.get(MediaFileType.MEDIA.getPath() + "\\" + componentMedia.getFileName());
				
				saveMediaFile(null, originalFilePath, componentMedia.getMimeType(), componentMedia.getFileName(), MediaFileType.MEDIA, componentMedia);
				successfulMigrations += 1;
			}
			else { // this media has already been migrated
				if (componentMedia.getFile() != null) {
					LOG.log(Level.INFO, "Skipping duplicate migration for ComponentMedia file: " + componentMedia.getFile().getFileName());
					skippedMigrations += 1;
				}
			}
		}
		
		/***************************************/
		//	Migrate data from Component Resource
		/***************************************/
		List<ComponentResource> componentResources = new ComponentResource().findByExampleProxy();
		for (ComponentResource componentResource : componentResources) {
			
			if (componentResource.getFileName() != null) { // this resource needs to be migrated
				Path originalFilePath = Paths.get(MediaFileType.RESOURCE.getPath() + "\\" + componentResource.getFileName());
				
				saveMediaFile(null, originalFilePath, componentResource.getMimeType(), componentResource.getFileName(), MediaFileType.RESOURCE, componentResource);
				successfulMigrations += 1;
			}
			else { // this resource has already been migrated
				if (componentResource.getFile() != null) {
					LOG.log(Level.INFO, "Skipping duplicate migration for ComponentResource file: " + componentResource.getFile().getFileName());
					skippedMigrations += 1;
				}
			}
		}
		
		/******************************************/
		//	Migrate data from Content Section Media
		/******************************************/
		List<ContentSectionMedia> contentSectionMedias = new ContentSectionMedia().findByExampleProxy();
		for (ContentSectionMedia sectionMedia : contentSectionMedias) {
			
			if (sectionMedia.getFileName() != null) { // this media needs to be migrated
				Path originalFilePath = Paths.get(MediaFileType.MEDIA.getPath() + "\\" + sectionMedia.getFileName());
				
				saveMediaFile(null, originalFilePath, sectionMedia.getMimeType(), sectionMedia.getFileName(), MediaFileType.MEDIA, sectionMedia);
				successfulMigrations += 1;
			}
			else { // this media has already been migrated
				if (sectionMedia.getFile() != null) {
					LOG.log(Level.INFO, "Skipping duplicate migration for ContentSectionMedia file: " + sectionMedia.getFile().getFileName());
					skippedMigrations += 1;
				}
			}
		}
		
		LOG.log(Level.INFO, "There were: " + successfulMigrations + " successful media/resource migrations after MediaMigration");
		LOG.log(Level.INFO, "There were: " + skippedMigrations + " skipped media/resource migrations after MediaMigration");
		LOG.log(Level.INFO, "There were: " + failedMigrations + " FAILED media/resource migrations after MediaMigration");
		
		JobManager.resumeScheduler();
		CoreSystem.resume("Completed media data migration");
		
		results.append("Successful Migrations: ").append(successfulMigrations);
		results.append("<br />Skipped Migrations: ").append(skippedMigrations);
		results.append("<br />Failed Migrations: ").append(failedMigrations);
		return results.toString();
	}
	
	@Override
	public int getPriority()
	{
		return 9999;
	}
	
	public <T extends StandardEntity & MediaModel> boolean saveMediaFile(GeneralMedia generalMedia, Path sourcePath, String mimeType, String originalFileName, MediaFileType type, T mediaToPersist)
	{
		Objects.requireNonNull(sourcePath);
		MediaFile media = new MediaFile();
		mediaToPersist.setFileName(null);
		mediaToPersist.setMimeType(null);
		mediaToPersist.setOriginalName(null);
		String newFileName = persistenceService.generateId() + OpenStorefrontConstant.getFileExtensionForMime(mimeType);
		Path targetPath = Paths.get(type.getPath() + "/" + newFileName);
		
		try {
			if (!movedFilesCache.containsKey(sourcePath.toString())) {
				media.setMediaFileId(persistenceService.generateId());
				media.setFileName(newFileName);
				media.setMimeType(mimeType);
				media.setOriginalName(originalFileName);
				media.setFileType(type);
				mediaToPersist.setFile(media);
				
				Files.move(sourcePath, targetPath);
				movedFilesCache.put(sourcePath.toString(), media.getMediaFileId());
//				LOG.log(Level.INFO, "Successfully moved file from \"" + originalFileName + "\" to \"" + targetPath.toString() + "\"");
			}
			else { // the file being referenced has already been migrated!
				String mediaFileId = movedFilesCache.get(sourcePath.toString());
				media.setMediaFileId(mediaFileId);
				media = media.find();
				mediaToPersist.setFile(media);
				LOG.log(Level.INFO, "The file \"" + originalFileName + "\" has already been renamed, referencing file via MediaFileId \"" + mediaFileId + "\"");
			}
			
			if (generalMedia != null) {
				persistenceService.delete(generalMedia);
			}
			persistenceService.persist(mediaToPersist);
		} catch (IOException ex) {
			String copyErrorInfo = "A file failed to be moved! Source: " + sourcePath.toString() + " | Target: " + targetPath.toString();
			LOG.log(Level.SEVERE, copyErrorInfo, ex);
			failedMigrations += 1;
			return false;
		}
		
		return true;
	}
}
