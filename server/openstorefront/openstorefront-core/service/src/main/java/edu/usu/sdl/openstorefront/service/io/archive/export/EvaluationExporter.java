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
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ContentSectionMedia;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.SystemArchiveOption;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.model.ContentSectionAll;
import edu.usu.sdl.openstorefront.core.model.EvaluationAll;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileInputStream;
import net.java.truevfs.access.TFileOutputStream;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class EvaluationExporter
		extends BaseExporter
{

	private static final Logger LOG = Logger.getLogger(EvaluationExporter.class.getName());
	private static final String DATA_DIR = "/evaluations/";
	private static final String DATA_SECTION_DIR = "/evaluations/sections/";
	private static final String DATA_SECTION_MEDIA_DIR = "/evaluations/sections/media/";

	@Override
	public int getPriority()
	{
		return 11;
	}

	@Override
	public String getExporterSupportEntity()
	{
		return Evaluation.class.getSimpleName();
	}

	@Override
	public List<BaseExporter> getAllRequiredExports()
	{
		List<BaseExporter> exporters = new ArrayList<>();

		exporters.add(new UserLookupTypeExporter());
		exporters.add(new GeneralMediaExporter());
		exporters.add(new EntryTypeExporter());
		exporters.add(new EntryTemplateExporter());
		exporters.add(new AttributeExporter());
		exporters.add(new ComponentExporter());

		exporters.add(new ChecklistQuestionExporter());
		exporters.add(new ChecklistTemplateExporter());
		exporters.add(new SectionTemplateExporter());

		exporters.add(new EvaluationTemplateExporter());
		exporters.add(this);

		return exporters;
	}

	@Override
	public void exportRecords()
	{
		Evaluation evaluationExample = new Evaluation();
		evaluationExample.setActiveStatus(Evaluation.ACTIVE_STATUS);
		List<Evaluation> evaluations = evaluationExample.findByExample();

		Set<String> componentIdSet = new HashSet<>();
		for (SystemArchiveOption option : archive.getArchiveOptions()) {
			if (Component.class.getSimpleName().equals(option.getPrimaryEntity())) {
				componentIdSet.add(option.getEntityId());
			}
		}

		ComponentExporter componentExporter = new ComponentExporter();
		componentExporter.init(archive, archiveBasePath);

		for (Evaluation evaluation : evaluations) {
			if (componentIdSet.contains(evaluation.getOriginComponentId())) {
				File dataFile = new TFile(archiveBasePath + DATA_DIR + "eval_" + evaluation.getEvaluationId() + ".json");

				EvaluationAll evaluationAll = service.getEvaluationService().getEvaluation(evaluation.getEvaluationId());

				try (OutputStream out = new TFileOutputStream(dataFile)) {
					StringProcessor.defaultObjectMapper().writeValue(out, evaluationAll);
				} catch (IOException ex) {
					LOG.log(Level.WARNING, MessageFormat.format("Unable to export eval: {0}", evaluation.getEvaluationId()), ex);
					addError("Unable to export eval");
				}

				//change request
				if (StringUtils.isNotBlank(evaluationAll.getEvaluation().getComponentId())) {
					ComponentAll componentAll = service.getComponentService().getFullComponent(evaluationAll.getEvaluation().getComponentId());
					if (componentAll != null) {
						componentExporter.exportComponent(componentAll);
					}
				}

				List<ContentSectionMedia> allMediaRecords = new ArrayList<>();
				for (ContentSectionAll section : evaluationAll.getContentSections()) {
					ContentSectionMedia mediaExample = new ContentSectionMedia();
					mediaExample.setContentSectionId(section.getSection().getContentSectionId());

					List<ContentSectionMedia> sectionMedia = mediaExample.findByExample();
					allMediaRecords.addAll(sectionMedia);
					for (ContentSectionMedia media : sectionMedia) {
						Path sourceMedia = media.pathToMedia();
						File mediaFile = new TFile(archiveBasePath + DATA_SECTION_MEDIA_DIR + media.getFileName());
						try (OutputStream out = new TFileOutputStream(mediaFile)) {
							Files.copy(sourceMedia, out);
						} catch (IOException ex) {
							LOG.log(Level.WARNING, "Unable to copy media file: " + media.getFileName(), ex);
							addError("Unable to copy media file: " + media.getFileName());
						}
					}
				}

				//save secion media records
				dataFile = new TFile(archiveBasePath + DATA_SECTION_DIR + "sectionmedia_" + evaluation.getEvaluationId() + ".json");
				try (OutputStream out = new TFileOutputStream(dataFile)) {
					StringProcessor.defaultObjectMapper().writeValue(out, allMediaRecords);
				} catch (IOException ex) {
					LOG.log(Level.WARNING, MessageFormat.format("Unable to export section media.", ex));
					addError("Unable to export section media");
				}

				archive.setRecordsProcessed(archive.getRecordsProcessed() + 1);
				archive.setStatusDetails("Exported eval " + evaluation.getEvaluationId());
				archive.save();
			}
		}
	}

	@Override
	public void importRecords()
	{
		//load all evals
		TFile dataDir = new TFile(archiveBasePath + DATA_DIR);
		TFile files[] = dataDir.listFiles();
		if (files != null) {
			int importedCount = 0;
			for (TFile dataFile : files) {
				if (dataFile.isFile()) {
					try (InputStream in = new TFileInputStream(dataFile)) {
						archive.setStatusDetails("Importing: " + dataFile.getName());
						archive.save();

						EvaluationAll evaluationAll = StringProcessor.defaultObjectMapper().readValue(in, EvaluationAll.class);
						service.getEvaluationService().saveEvaluationAll(evaluationAll);
						importedCount++;
					} catch (Exception ex) {
						LOG.log(Level.WARNING, "Failed to Load evaluation", ex);
						addError("Unable to load evaluation: " + dataFile.getName());
					}
				}
			}

			//load all eval media records
			dataDir = new TFile(archiveBasePath + DATA_SECTION_DIR);
			files = dataDir.listFiles();
			if (files != null) {
				for (TFile dataFile : files) {
					if (dataFile.isFile()) {
						try (InputStream in = new TFileInputStream(dataFile)) {
							archive.setStatusDetails("Importing: " + dataFile.getName());
							archive.save();

							List<ContentSectionMedia> allMediaRecords = StringProcessor.defaultObjectMapper().readValue(in, new TypeReference<List<ContentSectionMedia>>()
							{
							});
							for (ContentSectionMedia media : allMediaRecords) {
								media.save();
							}

						} catch (Exception ex) {
							LOG.log(Level.WARNING, "Failed to Load evaluation", ex);
							addError("Unable to load evaluation: " + dataFile.getName());
						}
					}
				}
				TFile mediaDir = new TFile(archiveBasePath + DATA_SECTION_MEDIA_DIR);
				TFile media[] = mediaDir.listFiles();
				if (media != null) {
					for (TFile mediaFile : media) {
						try {
							Files.copy(mediaFile.toPath(), FileSystemManager.getDir(FileSystemManager.MEDIA_DIR).toPath().resolve(mediaFile.getName()), StandardCopyOption.REPLACE_EXISTING);

						} catch (IOException ex) {
							LOG.log(Level.WARNING, MessageFormat.format("Failed to copy media to path file: {0}", mediaFile.getName()), ex);
							addError(MessageFormat.format("Failed to copy media to path file: {0}", mediaFile.getName()));
						}
					}
				}
			}

			archive.setRecordsProcessed(archive.getRecordsProcessed() + importedCount);
			archive.save();
		} else {
			LOG.log(Level.FINE, "No evaluations to load.");
		}
	}

	@Override
	public long getTotalRecords()
	{
		long count = 0;
		Evaluation evaluationExample = new Evaluation();
		evaluationExample.setActiveStatus(Evaluation.ACTIVE_STATUS);
		List<Evaluation> evaluations = evaluationExample.findByExample();

		Set<String> componentIdSet = new HashSet<>();
		for (SystemArchiveOption option : archive.getArchiveOptions()) {
			if (Component.class.getSimpleName().equals(option.getPrimaryEntity())) {
				componentIdSet.add(option.getEntityId());
			}
		}

		for (Evaluation evaluation : evaluations) {
			if (componentIdSet.contains(evaluation.getOriginComponentId())) {
				count++;
			}
		}
		return count;
	}

}
