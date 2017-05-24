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
import edu.usu.sdl.openstorefront.core.entity.Organization;
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
public class OrganizationExporter
		extends BaseExporter
{

	private static final Logger LOG = Logger.getLogger(EntryTypeExporter.class.getName());
	private static final String DATA_DIR = "/organizations/";
	private static final String MEDIA_DIR = "/organizations/media/";
	private static final String RECORD_FILENAME = "organizations.json";

	@Override
	public int getPriority()
	{
		return 4;
	}

	@Override
	public String getExporterSupportEntity()
	{
		return Organization.class.getSimpleName();
	}

	@Override
	public List<BaseExporter> getAllRequiredExports()
	{
		List<BaseExporter> exporters = new ArrayList<>();
		exporters.add(this);
		return exporters;
	}

	@Override
	public long getTotalRecords()
	{
		Organization organization = new Organization();
		organization.setActiveStatus(Organization.ACTIVE_STATUS);
		return service.getPersistenceService().countByExample(organization);
	}

	@Override
	public void exportRecords()
	{
		Organization organizationExample = new Organization();
		organizationExample.setActiveStatus(Organization.ACTIVE_STATUS);
		List<Organization> organizations = organizationExample.findByExample();

		File dataFile = new TFile(archiveBasePath + DATA_DIR + RECORD_FILENAME);

		try (OutputStream out = new TFileOutputStream(dataFile)) {
			StringProcessor.defaultObjectMapper().writeValue(out, organizations);
		} catch (IOException ex) {
			LOG.log(Level.WARNING, MessageFormat.format("Unable to export entry types.{0}", ex));
			addError("Unable to entry types");
		}

		for (Organization organization : organizations) {

			File mediaFile = new TFile(archiveBasePath + MEDIA_DIR + organization.getLogoFileName());
			Path path = organization.pathToLogo();
			try {
				Files.copy(path, mediaFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException ex) {
				LOG.log(Level.WARNING, "Unable to copy media file: " + mediaFile.getName(), ex);
				addError("Unable to copy media file: " + mediaFile.getName());
			}
		}

		archive.setRecordsProcessed(archive.getRecordsProcessed() + organizations.size());
		archive.setStatusDetails("Exported " + organizations.size() + " organization");
		archive.save();
	}

	@Override
	public void importRecords()
	{
		File dataFile = new TFile(archiveBasePath + DATA_DIR + RECORD_FILENAME);
		try (InputStream in = new TFileInputStream(dataFile)) {
			archive.setStatusDetails("Importing: " + dataFile.getName());
			archive.save();

			List<Organization> organizations = StringProcessor.defaultObjectMapper().readValue(in, new TypeReference<List<Organization>>()
			{
			});
			for (Organization organization : organizations) {
				service.getOrganizationService().saveOrganization(organization);
			}

			TFile mediaDir = new TFile(archiveBasePath + MEDIA_DIR);
			TFile media[] = mediaDir.listFiles();
			if (media != null) {
				for (TFile mediaFile : media) {
					try {
						Files.copy(mediaFile.toPath(), FileSystemManager.getDir(FileSystemManager.ORGANIZATION_DIR).toPath().resolve(mediaFile.getName()), StandardCopyOption.REPLACE_EXISTING);

					} catch (IOException ex) {
						LOG.log(Level.WARNING, MessageFormat.format("Failed to copy media to path file: {0}", mediaFile.getName()), ex);
						addError(MessageFormat.format("Failed to copy media to path file: {0}", mediaFile.getName()));
					}
				}
			}

			archive.setRecordsProcessed(archive.getRecordsProcessed() + organizations.size());
			archive.save();

		} catch (Exception ex) {
			LOG.log(Level.WARNING, "Failed to Load general media", ex);
			addError("Unable to load general media: " + dataFile.getName());
		}

	}

}
