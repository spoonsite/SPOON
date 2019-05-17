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
package edu.usu.sdl.openstorefront.service.io.archive.export;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.ContentSection;
import edu.usu.sdl.openstorefront.core.entity.ContentSectionMedia;
import edu.usu.sdl.openstorefront.core.entity.ContentSectionTemplate;
import edu.usu.sdl.openstorefront.core.model.ContentSectionAll;
import edu.usu.sdl.openstorefront.service.io.archive.BaseExporter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileInputStream;
import net.java.truevfs.access.TFileOutputStream;

/**
 *
 * @author dshurtleff
 */
public class SectionTemplateExporter
		extends BaseExporter
{

	private static final Logger LOG = Logger.getLogger(SectionTemplateExporter.class.getName());
	private static final String DATA_DIR = "/sectiontemplates/";
	private static final String DATA_SECTION_DIR = "/sectiontemplates/sections/";
	private static final String DATA_SECTION_MEDIA_DIR = "/sectiontemplates/sections/media/";

	@Override
	public int getPriority()
	{
		return 9;
	}

	@Override
	public String getExporterSupportEntity()
	{
		return ContentSectionTemplate.class.getSimpleName();
	}

	@Override
	public List<BaseExporter> getAllRequiredExports()
	{
		List<BaseExporter> exporters = new ArrayList<>();
		exporters.add(this);
		return exporters;
	}

	@Override
	public void exportRecords()
	{
		ContentSectionTemplate sectionTemplate = new ContentSectionTemplate();
		sectionTemplate.setActiveStatus(ContentSectionTemplate.ACTIVE_STATUS);
		List<ContentSectionTemplate> templates = sectionTemplate.findByExample();

		archive.setStatusDetails("Exporting section templates...");
		archive.save();

		File dataFile = new TFile(archiveBasePath + DATA_DIR + "templates.json");

		try (OutputStream out = new TFileOutputStream(dataFile)) {
			StringProcessor.defaultObjectMapper().writeValue(out, templates);
		} catch (IOException ex) {
			LOG.log(Level.WARNING, MessageFormat.format("Unable to export section templates.", ex));
			addError("Unable to export section templates");
		}

		//save sections and section media
		List<ContentSectionAll> sections = new ArrayList<>();
		for (ContentSectionTemplate template : templates) {
			ContentSection contentSection = new ContentSection();
			contentSection.setEntity(ContentSectionTemplate.class.getSimpleName());
			contentSection.setEntityId(template.getTemplateId());

			contentSection = contentSection.find();

			ContentSectionAll sectionAll = service.getContentSectionService().getContentSectionAll(contentSection.getContentSectionId(), false);
			sections.add(sectionAll);
		}

		archive.setStatusDetails("Exporting template sections...");
		archive.save();

		dataFile = new TFile(archiveBasePath + DATA_SECTION_DIR + "sections.json");
		try (OutputStream out = new TFileOutputStream(dataFile)) {
			StringProcessor.defaultObjectMapper().writeValue(out, sections);
		} catch (IOException ex) {
			LOG.log(Level.WARNING, MessageFormat.format("Unable to export section templates.", ex));
			addError("Unable to export section templates");
		}

		archive.setStatusDetails("Exporting template sections media...");
		archive.save();

		List<ContentSectionMedia> allMediaRecord = new ArrayList<>();
		for (ContentSectionAll section : sections) {
			ContentSectionMedia mediaExample = new ContentSectionMedia();
			mediaExample.setContentSectionId(section.getSection().getContentSectionId());

			List<ContentSectionMedia> sectionMedia = new ArrayList<>();
			allMediaRecord.addAll(sectionMedia);

			for (ContentSectionMedia media : sectionMedia) {
				Path sourceMedia = media.pathToMedia();
				if (media.getFile() != null) {
					File mediaFile = new TFile(archiveBasePath + DATA_SECTION_MEDIA_DIR + media.getFile().getFileName());
					try (OutputStream out = new TFileOutputStream(mediaFile)) {
						Files.copy(sourceMedia, out);
					} catch (IOException ex) {
						LOG.log(Level.WARNING, "Unable to copy media file: " + media.getFile().getFileName(), ex);
						addError("Unable to copy media file: " + media.getFile().getFileName());
					}
				}
			}
		}
		//save media records
		dataFile = new TFile(archiveBasePath + DATA_SECTION_DIR + "sectionmedia.json");
		try (OutputStream out = new TFileOutputStream(dataFile)) {
			StringProcessor.defaultObjectMapper().writeValue(out, allMediaRecord);
		} catch (IOException ex) {
			LOG.log(Level.WARNING, MessageFormat.format("Unable to export section media.", ex));
			addError("Unable to export section media");
		}

		archive.setRecordsProcessed(archive.getRecordsProcessed() + templates.size());
		archive.setStatusDetails("Exported " + templates.size() + " section templates");
		archive.save();
	}

	@Override
	public void importRecords()
	{
		//restore templates
		boolean continueProcessing = true;
		int recordCount = 0;
		File dataFile = new TFile(archiveBasePath + DATA_DIR + "templates.json");
		try (InputStream in = new TFileInputStream(dataFile)) {
			archive.setStatusDetails("Importing section templates: " + dataFile.getName());
			archive.save();

			List<ContentSectionTemplate> sectionTemplates = StringProcessor.defaultObjectMapper().readValue(in, new TypeReference<List<ContentSectionTemplate>>()
			{
			});
			recordCount = sectionTemplates.size();
			for (ContentSectionTemplate sectionTemplate : sectionTemplates) {
				sectionTemplate.save();
			}
		} catch (Exception ex) {
			LOG.log(Level.WARNING, "Failed to Load templates", ex);
			addError("Unable to load section templates: " + dataFile.getName());
			continueProcessing = false;
		}

		if (continueProcessing) {
			dataFile = new TFile(archiveBasePath + DATA_SECTION_DIR + "sections.json");
			try (InputStream in = new TFileInputStream(dataFile)) {
				archive.setStatusDetails("Importing sections for templates: " + dataFile.getName());
				archive.save();

				List<ContentSectionAll> sections = StringProcessor.defaultObjectMapper().readValue(in, new TypeReference<List<ContentSectionAll>>()
				{
				});
				for (ContentSectionAll section : sections) {
					service.getContentSectionService().saveAll(section);
				}
			} catch (Exception ex) {
				LOG.log(Level.FINE, "Failed to Load sections", ex);
				addError("Unable to load sections: " + dataFile.getName());
				continueProcessing = false;
			}
		}

		if (continueProcessing) {
			dataFile = new TFile(archiveBasePath + DATA_SECTION_DIR + "sectionmedia.json");
			try (InputStream in = new TFileInputStream(dataFile)) {
				archive.setStatusDetails("Importing section Media: " + dataFile.getName());
				archive.save();

				List<ContentSectionMedia> sectionMedia = StringProcessor.defaultObjectMapper().readValue(in, new TypeReference<List<ContentSectionMedia>>()
				{
				});
				for (ContentSectionMedia media : sectionMedia) {
					media.save();
					service.getRepoFactory().getMediaFileRepo().handleMediaFileSave(service.getPersistenceService(), media.getFile());
				}

				TFile mediaDir = new TFile(archiveBasePath + DATA_SECTION_MEDIA_DIR);
				TFile media[] = mediaDir.listFiles();
				if (media != null) {
					for (TFile mediaFile : media) {
						try {
							Files.copy(mediaFile.toPath(), FileSystemManager.getInstance().getDir(FileSystemManager.MEDIA_DIR).toPath().resolve(mediaFile.getName()), StandardCopyOption.REPLACE_EXISTING);

						} catch (IOException ex) {
							LOG.log(Level.WARNING, MessageFormat.format("Failed to copy media to path file: {0}", mediaFile.getName()), ex);
							addError(MessageFormat.format("Failed to copy media to path file: {0}", mediaFile.getName()));
						}
					}
				}
			} catch (Exception ex) {
				LOG.log(Level.WARNING, "Failed to Load sections media", ex);
				addError("Unable to load sections media: " + dataFile.getName());
			}

			archive.setRecordsProcessed(archive.getRecordsProcessed() + recordCount);
			archive.setStatusDetails("Imported " + recordCount + " section templates");
			archive.save();
		}

	}

	@Override
	public long getTotalRecords()
	{
		ContentSectionTemplate sectionTemplate = new ContentSectionTemplate();
		sectionTemplate.setActiveStatus(ContentSectionTemplate.ACTIVE_STATUS);
		return service.getPersistenceService().countByExample(sectionTemplate);
	}

}
