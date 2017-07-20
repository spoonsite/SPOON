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
import edu.usu.sdl.openstorefront.core.entity.FeedbackTicket;
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
public class FeedbackExporter
		extends BaseExporter
{

	private static final Logger LOG = Logger.getLogger(FeedbackExporter.class.getName());
	private static final String DATA_DIR = "/feedback/";

	@Override
	public int getPriority()
	{
		return 17;
	}

	@Override
	public String getExporterSupportEntity()
	{
		return FeedbackTicket.class.getSimpleName();
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
		FeedbackTicket feedbackExample = new FeedbackTicket();
		feedbackExample.setActiveStatus(FeedbackTicket.ACTIVE_STATUS);
		return service.getPersistenceService().countByExample(feedbackExample);
	}

	@Override
	public void exportRecords()
	{
		FeedbackTicket feedbackExample = new FeedbackTicket();
		feedbackExample.setActiveStatus(FeedbackTicket.ACTIVE_STATUS);
		List<FeedbackTicket> feedbackTickets = feedbackExample.findByExample();

		File feedbackFile = new TFile(archiveBasePath + DATA_DIR + "feedbacktickets.json");

		try (OutputStream out = new TFileOutputStream(feedbackFile)) {
			StringProcessor.defaultObjectMapper().writeValue(out, feedbackTickets);
		} catch (IOException ex) {
			LOG.log(Level.WARNING, MessageFormat.format("Unable to export feedback tickets.{0}", ex));
			addError("Unable to export feedback tickets");
		}

		archive.setRecordsProcessed(archive.getRecordsProcessed() + feedbackTickets.size());
		archive.setStatusDetails("Exported " + feedbackTickets.size() + " feedbacktickets");
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

					List<FeedbackTicket> tickets = StringProcessor.defaultObjectMapper().readValue(in, new TypeReference<List<FeedbackTicket>>()
					{
					});
					//We don't want to re-sent these; just store locally
					for (FeedbackTicket ticket : tickets) {
						ticket.save();
					}

					archive.setRecordsProcessed(archive.getRecordsProcessed() + tickets.size());
					archive.save();

				} catch (Exception ex) {
					LOG.log(Level.WARNING, "Failed to Load feedback tickets", ex);
					addError("Unable to load feedback tickets: " + dataFile.getName());
				}
			}
		} else {
			LOG.log(Level.FINE, "No feedback tickets to load.");
		}
	}

}
