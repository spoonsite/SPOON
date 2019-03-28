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
package edu.usu.sdl.openstorefront.core.api;

import edu.usu.sdl.openstorefront.core.entity.FileDataMap;
import edu.usu.sdl.openstorefront.core.entity.FileFormat;
import edu.usu.sdl.openstorefront.core.entity.FileHistory;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryError;
import edu.usu.sdl.openstorefront.core.model.DataMapModel;
import edu.usu.sdl.openstorefront.core.model.FileFormatCheck;
import edu.usu.sdl.openstorefront.core.model.FileHistoryAll;
import edu.usu.sdl.openstorefront.core.model.ImportContext;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.FieldDefinition;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

/**
 * Handles importing of data; file history
 *
 * @author dshurtleff
 */
public interface ImportService
		extends AsyncService
{

	/**
	 * Import the data according to the importContext
	 *
	 * @param importContext
	 * @return File history
	 */
	public String importData(ImportContext importContext);

	/**
	 * Checked the file for the correct format. Note: this will close the
	 * inputstream when complete
	 *
	 * @param formatCheck
	 * @return Error message or null
	 */
	public String checkFormat(FileFormatCheck formatCheck);

	/**
	 * Re-queue file for processing
	 *
	 * @param fileHistoryId
	 */
	public void reprocessFile(String fileHistoryId);

	/**
	 * Saves a file history record; Note: it's expected the record has the
	 * complete set of errors. As this does a simple replace of the errors.
	 *
	 * @param fileHistoryAll
	 * @return FileHistory saved
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public FileHistory saveFileHistory(FileHistoryAll fileHistoryAll);

	@ServiceInterceptor(TransactionInterceptor.class)
	public void updateImportProgress(FileHistoryAll fileHistoryAll);
	
	/**
	 * Deletes a file history record and any associated data
	 *
	 * @param fileHistoryId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void removeFileHistory(String fileHistoryId);

	/**
	 * Finds all formats for a given file type
	 *
	 * @param fileType
	 * @return
	 */
	public List<FileFormat> findFileFormats(String fileType);

	/**
	 * Find format record. Note: some format could be transient due to plugins
	 * @param fileFormatCode
	 * @return Format or null if it doesn't exist
	 */
	public FileFormat findFileFormat(String fileFormatCode);
	
	/**
	 * Gets all formats that support mapping
	 * @return 
	 */
	public List<FileFormat> getFileFormatsMapping();
	
	/**
	 * Removes records older than the clean up property is set to
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void cleanupOldFileHistory();

	/**
	 * Rollback data (if possible) and remove file uploaded.
	 * @param fileHistoryId 
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void rollback(String fileHistoryId);

	/**
	 * Groups all Errors by fileHistoryId Tries to pull efficiently
	 *
	 * @return
	 */
	public Map<String, List<FileHistoryError>> fileHistoryErrorMap();
	
	/**
	 *  Add or updates a data mapper for a file format 
	 * @param dataMapModel (Contains both the data mapping and attribute mapping)
	 * @return  saved data map
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public FileDataMap saveFileDataMap(DataMapModel dataMapModel);
	
	/**
	 * Removes File Data map
	 * @param fileDataMapId 
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void removeFileDataMap(String fileDataMapId);
	
	/**
	 * Get the Complete DataMapModel
	 * 
	 * @param fileDataMapId
	 * @return 
	 */
	public DataMapModel getDataMap(String fileDataMapId);
	
	/**
	 * Adds a File Format to the import system
	 * 
	 * @param newFormat 
	 * @param parseClass 
	 */
	public void registerFormat(FileFormat newFormat, Class parseClass);
	
	/**
	 * Removes a File Format from the import system
	 * 
	 * @param fullClassPath 
	 */
	public void unregisterFormat(String fullClassPath);

	/**
	 * Finds the field path to use for data mapping
	 * 
	 * @param fileFormatCode
	 * @param in
	 * @return fieldDefinition found in the file
	 */
	public List<FieldDefinition> getMapField(String fileFormatCode, InputStream in);
	
	/**
	 * Processes a sample file and returns a preview to help with mapping
	 * 
	 * @param fileFormatCode
	 * @param fileDataMapId
	 * @param in
	 * @param filename
	 * @return preview output
	 */
	public String previewMapData(String fileFormatCode, String fileDataMapId, InputStream in, String filename);	

	/**
	 * Creates a copy of existing data map
	 * 
	 * @param fileDataMapId
	 * @return copy data map
	 */
	public FileDataMap copyDataMap(String fileDataMapId);


	/**
	 * Returns a map of componentTypeFrom -> componentTypeTo
	 * Needed for the Aerospace data import
	 * This method exposes the OpenCSV functionality to the plugin
	 * 
	 * @param in
	 * @return 
	 */
	public Map<String, String> getComponentTypeMapFromCSV(Reader in) throws IOException;
}
