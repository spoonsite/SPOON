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
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.Alert;
import edu.usu.sdl.openstorefront.core.entity.Highlight;
import edu.usu.sdl.openstorefront.service.io.archive.BaseExporter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
public class AlertsExporter
		extends BaseExporter
{

	private static final Logger LOG = Logger.getLogger(AlertsExporter.class.getName());
	private static final String DATA_DIR = "/alerts/";

	@Override
	public int getPriority()
	{
		return 15;
	}

	@Override
	public String getExporterSupportEntity()
	{
		return Alert.class.getSimpleName();
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
		Alert alertExample = new Alert();
		alertExample.setActiveStatus(Highlight.ACTIVE_STATUS);
		return service.getPersistenceService().countByExample(alertExample);
	}

	@Override
	public void exportRecords()
	{
		Alert alertExample = new Alert();
		alertExample.setActiveStatus(Alert.ACTIVE_STATUS);
		List<Alert> alerts = alertExample.findByExample();

		File alertsFile = new TFile(archiveBasePath + DATA_DIR + "alerts.json");

		try (OutputStream out = new TFileOutputStream(alertsFile)) {
			StringProcessor.defaultObjectMapper().writeValue(out, alerts);
		} catch (IOException ex) {
			LOG.log(Level.WARNING, MessageFormat.format("Unable to export alerts.{0}", ex));
			addError("Unable to export alerts");
		}

		archive.setRecordsProcessed(archive.getRecordsProcessed() + alerts.size());
		archive.setStatusDetails("Exported " + alerts.size() + " alerts");
		archive.save();
	}

	@Override
	public void importRecords()
	{
		File dataDir = new TFile(archiveBasePath + DATA_DIR);
		File files[] = dataDir.listFiles();
		if (files != null) {
			for (File dataFile : files) {
				try (InputStream in = new TFileInputStream(dataFile)) {
					archive.setStatusDetails("Importing: " + dataFile.getName());
					archive.save();

					List<Alert> alerts = StringProcessor.defaultObjectMapper().readValue(in, new TypeReference<List<Alert>>()
					{
					});
					for (Alert alert : alerts) {
						service.getAlertService().saveAlert(alert);
					}

					archive.setRecordsProcessed(archive.getRecordsProcessed() + alerts.size());
					archive.save();

				} catch (Exception ex) {
					LOG.log(Level.WARNING, "Failed to Load alerts", ex);
					addError("Unable to load alerts: " + dataFile.getName());
				}
			}
		} else {
			LOG.log(Level.FINE, "No alerts to load.");
		}
	}

}
