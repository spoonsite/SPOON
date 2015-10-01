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
package edu.usu.sdl.openstorefront.service.io.parser;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryErrorType;
import edu.usu.sdl.openstorefront.core.model.FileHistoryAll;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.io.reader.GenericReader;
import edu.usu.sdl.openstorefront.service.io.reader.TextReader;
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
 */
public abstract class AbstractParser
{

	private static final Logger log = Logger.getLogger(AbstractParser.class.getName());

	protected FileHistoryAll fileHistoryAll;
	protected int currentRecordNumber;
	protected ServiceProxy service = ServiceProxy.getProxy();

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

	public void processData(FileHistoryAll fileHistoryAll)
	{
		this.fileHistoryAll = fileHistoryAll;

		log.log(Level.INFO, MessageFormat.format("Processing: {0}", fileHistoryAll.getFileHistory().getOriginalFilename()));

		//getReader
		log.log(Level.FINEST, "Get Reader");
		Path path = fileHistoryAll.getFileHistory().pathToFileName();
		if (path != null) {
			try (GenericReader reader = getReader(new FileInputStream(path.toFile()))) {
				reader.preProcess();

				log.log(Level.FINEST, "Read Records");
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
								addRecordToStorage(parsed);
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

				log.log(Level.FINEST, "Finish Up");
				finishProcessing();
			} catch (Exception e) {
				StringWriter stringWriter = new StringWriter();
				PrintWriter printWriter = new PrintWriter(stringWriter);
				e.printStackTrace(printWriter);

				fileHistoryAll.addError(FileHistoryErrorType.SYSTEM, "Unable to process all of the records the failed read the data. Error Trace: <br>" + stringWriter.toString());
			}

			log.log(Level.INFO, MessageFormat.format("Results for processing: {0}", fileHistoryAll.getFileHistory().getOriginalFilename()));
			log.log(Level.INFO, MessageFormat.format("Records Total processed: {0}", currentRecordNumber));
			log.log(Level.INFO, MessageFormat.format("Records Total Failed: {0}", fileHistoryAll.getErrors().size()));
			fileHistoryAll.getFileHistory().setNumberRecords(currentRecordNumber);

		} else {
			throw new OpenStorefrontRuntimeException("Unable to get path to file.", "The filename is likely missing from file history record.  The record may be corrupt. ID: " + fileHistoryAll.getFileHistory().getFileHistoryId());
		}
	}

	protected void updateFileHistoryStats()
	{

	}

	protected GenericReader getReader(InputStream in)
	{
		return new TextReader(in);
	}

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

	protected <T> void addRecordToStorage(T record)
	{
		getStorageBucket().add(record);

		if (getStorageBucket().size() > getMaxBucketSize()) {
			flushRecordsToStorage();
			getStorageBucket().clear();
		}
	}

	protected abstract <T> List<T> getStorageBucket();

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
