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
import edu.usu.sdl.openstorefront.core.util.MediaFileType;
import edu.usu.sdl.openstorefront.service.manager.JobManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
	private static final Logger LOG = Logger.getLogger(DBDataMigration.class.getName());
	protected PersistenceService persistenceService = service.getNewPersistenceService();
	
	public MediaMigration()
	{
		super("MEDIA-MIGRATION-2.5");
	}
	
	private static int noFileCount = 0;
	private static int failedDeleteCount = 0;
	private static int failedFileReadCount = 0;
	private static List<Path> filesToDelete = new ArrayList<>();
	
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
				filesToDelete.add(originalFilePath);

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
				
				try (InputStream inputStream = new FileInputStream(originalFilePath.toString())) {
					
					MediaFile newMediaFile = saveMediaFile(null, inputStream, generalMedia.getMimeType(), generalMedia.getFileName(), MediaFileType.GENERAL);
					newMediaFile = persistenceService.persist(newMediaFile);
					newGeneralMedia.setFile(newMediaFile);
					persistenceService.delete(generalMedia);
					persistenceService.persist(newGeneralMedia);
					
				} catch (FileNotFoundException ex) {
//					Logger.getLogger(MediaMigration.class.getName()).log(Level.SEVERE, null, ex);
					System.out.println("General Media, cant find file!");
					noFileCount += 1;
				} catch (IOException ex) {
					Logger.getLogger(MediaMigration.class.getName()).log(Level.SEVERE, null, ex);
					failedFileReadCount += 1;
				}
			}
		}
		
		/***************************************/
		//	Migrate data from Component Media
		/***************************************/
		List<ComponentMedia> componentMedias = new ComponentMedia().findByExampleProxy();
		for (ComponentMedia componentMedia : componentMedias) {
			
			if (componentMedia.getFileName() != null) {
				Path originalFilePath = Paths.get(MediaFileType.MEDIA.getPath() + "\\" + componentMedia.getFileName());
				filesToDelete.add(originalFilePath);

				ComponentMedia newCompMedia = new ComponentMedia();
				
				newCompMedia.setCreateDts(componentMedia.getCreateDts());
				newCompMedia.setCreateUser(componentMedia.getCreateUser());
				newCompMedia.setMediaTypeCode(componentMedia.getMediaTypeCode());
				newCompMedia.setDataSensitivity(componentMedia.getDataSensitivity());
				newCompMedia.setSecurityMarkingType(componentMedia.getSecurityMarkingType());
				newCompMedia.setComponentId(componentMedia.getComponentId());
				
				newCompMedia.populateBaseUpdateFields();
				
				System.out.println("Comp Media path: " + originalFilePath.toString());
				try (InputStream inputStream = new FileInputStream(originalFilePath.toString())) {
					
					MediaFile newMediaFile = saveMediaFile(null, inputStream, componentMedia.getMimeType(), componentMedia.getFileName(), MediaFileType.MEDIA);
					newMediaFile = persistenceService.persist(newMediaFile);
					newCompMedia.setFile(newMediaFile);
					persistenceService.delete(componentMedia);
					
					newCompMedia.setFileName(null);
					newCompMedia.setMimeType(null);
					newCompMedia.setOriginalName(null);
					persistenceService.persist(newCompMedia);
					
				} catch (FileNotFoundException ex) {
					System.out.println("CompMedia, cant find file!" + ex.toString());
//					Logger.getLogger(MediaMigration.class.getName()).log(Level.SEVERE, null, ex);
					noFileCount += 1;
				} catch (IOException ex) {
					Logger.getLogger(MediaMigration.class.getName()).log(Level.SEVERE, null, ex);
					failedFileReadCount += 1;
				}
			}
		}
		
		/***************************************/
		//	Migrate data from Component Resource
		/***************************************/
		List<ComponentResource> componentResources = new ComponentResource().findByExampleProxy();
		for (ComponentResource componentResource : componentResources) {
			
			if (componentResource.getFileName() != null) {
				Path originalFilePath = Paths.get(MediaFileType.RESOURCE.getPath() + "\\" + componentResource.getFileName());
				filesToDelete.add(originalFilePath);

				ComponentResource newCompResource = new ComponentResource();
				
				newCompResource.setCreateDts(componentResource.getCreateDts());
				newCompResource.setCreateUser(componentResource.getCreateUser());
				newCompResource.setResourceType(componentResource.getResourceType());
				newCompResource.setDataSensitivity(componentResource.getDataSensitivity());
				newCompResource.setSecurityMarkingType(componentResource.getSecurityMarkingType());
				newCompResource.setComponentId(componentResource.getComponentId());
				
				newCompResource.populateBaseUpdateFields();
				
				try (InputStream inputStream = new FileInputStream(originalFilePath.toString())) {
					
					MediaFile newMediaFile = saveMediaFile(null, inputStream, componentResource.getMimeType(), componentResource.getFileName(), MediaFileType.RESOURCE);
					newMediaFile = persistenceService.persist(newMediaFile);
					newCompResource.setFile(newMediaFile);
					persistenceService.delete(componentResource);
					
					newCompResource.setFileName(null);
					newCompResource.setMimeType(null);
					newCompResource.setOriginalName(null);
					persistenceService.persist(newCompResource);
					
				} catch (FileNotFoundException ex) {
//					Logger.getLogger(MediaMigration.class.getName()).log(Level.SEVERE, null, ex);
					System.out.println("comp resource, cant find file!");
					noFileCount += 1;
				} catch (IOException ex) {
					Logger.getLogger(MediaMigration.class.getName()).log(Level.SEVERE, null, ex);
					failedFileReadCount += 1;
				}
			}
		}
		
		/******************************************/
		//	Migrate data from Content Section Media
		/******************************************/
		List<ContentSectionMedia> contentSectionMedias = new ContentSectionMedia().findByExampleProxy();
		for (ContentSectionMedia sectionMedia : contentSectionMedias) {
			
			if (sectionMedia.getFileName() != null) {
				Path originalFilePath = Paths.get(MediaFileType.MEDIA.getPath() + "\\" + sectionMedia.getFileName());
				filesToDelete.add(originalFilePath);

				ContentSectionMedia newSectionMedia = new ContentSectionMedia();
				
				newSectionMedia.setCreateDts(sectionMedia.getCreateDts());
				newSectionMedia.setCreateUser(sectionMedia.getCreateUser());
				newSectionMedia.setMediaTypeCode(sectionMedia.getMediaTypeCode());
				newSectionMedia.setDataSensitivity(sectionMedia.getDataSensitivity());
				newSectionMedia.setSecurityMarkingType(sectionMedia.getSecurityMarkingType());
				newSectionMedia.setPrivateMedia(sectionMedia.getPrivateMedia());
				newSectionMedia.setContentSectionId(sectionMedia.getContentSectionId());
				
				newSectionMedia.populateBaseUpdateFields();
				
				try (InputStream inputStream = new FileInputStream(originalFilePath.toString())) {
					
					MediaFile newMediaFile = saveMediaFile(null, inputStream, sectionMedia.getMimeType(), sectionMedia.getFileName(), MediaFileType.MEDIA);
					newMediaFile = persistenceService.persist(newMediaFile);
					newSectionMedia.setFile(newMediaFile);
					persistenceService.delete(sectionMedia);
					
					newSectionMedia.setFileName(null);
					newSectionMedia.setMimeType(null);
					newSectionMedia.setOriginalName(null);
					persistenceService.persist(newSectionMedia);
					
				} catch (FileNotFoundException ex) {
//					Logger.getLogger(MediaMigration.class.getName()).log(Level.SEVERE, null, ex);
					System.out.println("section media, cant find file!");
					noFileCount += 1;
				} catch (IOException ex) {
					Logger.getLogger(MediaMigration.class.getName()).log(Level.SEVERE, null, ex);
					failedFileReadCount += 1;
				}
			}
		}
		
		// Delete files
		for (Path fileToDelete : filesToDelete) {
			try {
				deleteOriginalFile(fileToDelete);
			} catch (IOException ex) {
				failedDeleteCount += 1;
				Logger.getLogger(MediaMigration.class.getName()).log(Level.SEVERE, null, ex);
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
	
	private void deleteOriginalFile(Path filePath) throws IOException
	{
		boolean deletedFile = Files.deleteIfExists(filePath);
	}
	
	private MediaFile saveMediaFile(MediaFile media, InputStream fileInput, String mimeType, String originalFileName, MediaFileType type) throws IOException
	{
		Objects.requireNonNull(fileInput);
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

		Path path = Paths.get(type.getPath() + "/" + media.getFileName());
		Files.copy(fileInput, path, StandardCopyOption.REPLACE_EXISTING);
		
//		results.append("Successfully saved the file: ").append(path.toString());
		return media;
	}
}
