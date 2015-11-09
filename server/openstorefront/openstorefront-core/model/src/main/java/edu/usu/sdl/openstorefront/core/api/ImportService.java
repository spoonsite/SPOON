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

import edu.usu.sdl.openstorefront.core.entity.FileFormat;
import edu.usu.sdl.openstorefront.core.entity.FileHistory;
import edu.usu.sdl.openstorefront.core.model.FileHistoryAll;
import edu.usu.sdl.openstorefront.core.model.ImportContext;
import java.util.List;

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
	 * Saves a file history record; Note: it's expected the record has the
	 * complete set of errors. As this does a simple replace of the errors.
	 *
	 * @param fileHistoryAll
	 * @return FileHistory saved
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public FileHistory saveFileHistory(FileHistoryAll fileHistoryAll);

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
	 * Removes records older than the clean up property is set to
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void cleanupOldFileHistory();

	@ServiceInterceptor(TransactionInterceptor.class)
	public void rollback(String fileHistoryId);

}
