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
package edu.usu.sdl.openstorefront.core.spi.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryError;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryErrorType;
import edu.usu.sdl.openstorefront.core.model.FileHistoryAll;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.GenericReader;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.MappableReader;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.TextReader;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 * @param <T>
 */
public abstract class AbstractParser<T>
{

	private static final Logger LOG = Logger.getLogger(AbstractParser.class.getName());

	protected FileHistoryAll fileHistoryAll;
	protected int currentRecordNumber;
	protected Service service = ServiceProxyFactory.getServiceProxy();

	public AbstractParser()
	{
	}

	/**
	 * Check the data to confirm that they uploaded a valid file.
	 *
	 * @param mimeType
	 * @param input
	 * @return null if valid or a message if not.
	 */
	public abstract String checkFormat(String mimeType, InputStream input);

	@SuppressWarnings("unchecked")
	public void processData(FileHistoryAll fileHistoryAll)
	{
		this.fileHistoryAll = fileHistoryAll;

		LOG.log(Level.INFO, MessageFormat.format("Processing: {0}", fileHistoryAll.getFileHistory().getOriginalFilename()));

		//getReader
		LOG.log(Level.FINEST, "Get Reader");
		Path path = fileHistoryAll.getFileHistory().pathToFileName();
		if (path != null) {
			try (GenericReader reader = getReader(new FileInputStream(path.toFile()))) {
				reader.preProcess();

				LOG.log(Level.FINEST, "Read Records");
				if (reader.getTotalRecords() != null) {
					fileHistoryAll.getFileHistory().setNumberRecords(reader.getTotalRecords());
				}

				Object record = reader.nextRecord();
				while (record != null) {
					currentRecordNumber = reader.getCurrentRecordNumber();
					try {

						Object parsed = parseRecord(record);

						if (parsed != null) {
							if (validateRecord(parsed)) {
								addRecordToStorage((T) parsed);
							}
						}
					} catch (Exception e) {
						StringWriter stringWriter = new StringWriter();
						PrintWriter printWriter = new PrintWriter(stringWriter);
						e.printStackTrace(printWriter);
						fileHistoryAll.addError(FileHistoryErrorType.PARSE, "Unable to parse record.  Trace: <br> " + stringWriter.toString(), currentRecordNumber);
					}
					fileHistoryAll.getFileHistory().setRecordsProcessed(currentRecordNumber);
					updateFileHistoryStats();

					record = reader.nextRecord();
				}
				//flush any remaining records
				flushRecordsToStorage();

				LOG.log(Level.FINEST, "Finish Up");
				finishProcessing();
			} catch (Exception e) {
				StringWriter stringWriter = new StringWriter();
				PrintWriter printWriter = new PrintWriter(stringWriter);
				e.printStackTrace(printWriter);

				fileHistoryAll.addError(FileHistoryErrorType.SYSTEM, "Unable to process all of the records;  Failed reading the data. <br> Error Trace: <br>" + stringWriter.toString());
			}

			LOG.log(Level.INFO, MessageFormat.format("Results for processing: {0}", fileHistoryAll.getFileHistory().getOriginalFilename()));
			LOG.log(Level.INFO, MessageFormat.format("Records Total processed: {0}", currentRecordNumber));
			LOG.log(Level.INFO, MessageFormat.format("Records Total Failed: {0}", fileHistoryAll.getErrors().size()));
			LOG.log(Level.FINE, "Error Message(s):");
			for (FileHistoryError error : fileHistoryAll.getErrors()) {
				LOG.log(Level.FINE, MessageFormat.format("{0} -  Message: {1}", new Object[]{error.getFileHistoryErrorType(), error.getErrorMessage().replace("<br>", "\n")}));
			}
			LOG.log(Level.FINE, "----------------->");
			fileHistoryAll.getFileHistory().setNumberRecords(currentRecordNumber);

		} else {
			throw new OpenStorefrontRuntimeException("Unable to get path to file.", "The filename is likely missing from file history record.  The record may be corrupt. ID: " + fileHistoryAll.getFileHistory().getFileHistoryId());
		}
	}

	protected void addMultipleRecords(List<T> records)
	{
		for (T record : records) {
			if (validateRecord(record)) {
				addRecordToStorage(record);
			}
		}
	}

	public String previewProcessedData(FileHistoryAll fileHistoryAll, InputStream input)
	{
		this.fileHistoryAll = fileHistoryAll;

		StringBuilder output = new StringBuilder();
		try (GenericReader reader = getReader(input)) {
			reader.preProcess();

			Object record = reader.nextRecord();
			if (record != null) {
				Object parsed = parseRecord(record);
				output.append(handlePreviewOfRecord(parsed));
			}
		} catch (Exception e) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);

			output.append("Unable to process all of the records;  Failed reading the data. <br> Error Trace: <br>")
					.append(stringWriter.toString());
		}
		return output.toString();
	}

	protected String handlePreviewOfRecord(Object data)
	{
		ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();

		String dataInJSON = "No record parsed";
		if (data != null) {
			try {
				dataInJSON = objectMapper.writeValueAsString(data);
			} catch (JsonProcessingException ex) {
				LOG.log(Level.WARNING, "Unable to create preview of data.", ex);
				dataInJSON = "Unable to create preview of data.  See logs for details.";
			}
		}
		return dataInJSON;
	}

	protected void updateFileHistoryStats()
	{
		service.getImportService().updateImportProgress(fileHistoryAll);
	}

	protected GenericReader getReader(InputStream in)
	{
		return new TextReader(in);
	}

	/**
	 * Get the mappable reader for field extraction
	 *
	 * @param in
	 * @return reader or null if not mappable
	 */
	public MappableReader getMappableReader(InputStream in)
	{
		GenericReader reader = getReader(in);
		if (reader instanceof MappableReader) {
			return (MappableReader) reader;
		}
		return null;
	}

	/**
	 *
	 * @param <T>
	 * @param record
	 * @return new Entity record to be stored or Null to skip
	 */
	protected abstract <T> Object parseRecord(T record);

	protected <T> boolean validateRecord(T record)
	{

		ValidationModel validationModel = new ValidationModel(record);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid() == false) {
			fileHistoryAll.addError(FileHistoryErrorType.VALIDATION, validationResult.toHtmlString(), currentRecordNumber);
		}
		return validationResult.valid();
	}

	protected void addRecordToStorage(T record)
	{
		getStorageBucket().add(record);

		if (getStorageBucket().size() > getMaxBucketSize()) {
			flushRecordsToStorage();
			getStorageBucket().clear();
		}
	}

	protected abstract List<T> getStorageBucket();

	protected abstract int getMaxBucketSize();

	protected void flushRecordsToStorage()
	{
		if (getStorageBucket().isEmpty() == false) {
			performStorage();
			if (fileHistoryAll.getFileHistory().getRecordsStored() == null) {
				fileHistoryAll.getFileHistory().setRecordsStored(0);
			}
			fileHistoryAll.getFileHistory().setRecordsStored(fileHistoryAll.getFileHistory().getRecordsStored() + getStorageBucket().size());
			updateFileHistoryStats();
		}
	}

	protected abstract void performStorage();

	protected void finishProcessing()
	{
	}

}
