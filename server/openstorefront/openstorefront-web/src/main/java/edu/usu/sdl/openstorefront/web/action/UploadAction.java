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
package edu.usu.sdl.openstorefront.web.action;

import au.com.bytecode.opencsv.CSVReader;
import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.manager.DBManager;
import edu.usu.sdl.openstorefront.storage.model.LookupEntity;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

/**
 *
 * @author dshurtleff
 */
public class UploadAction
		extends BaseAction
{

	private static final Logger log = Logger.getLogger(UploadAction.class.getName());

	@Validate(required = true, on = {"UploadLookup"})
	private FileBean uploadFile;

	@Validate(required = true, on = "UploadLookup")
	private String entityName;

	@HandlesEvent("UploadLookup")
	public Resolution uploadLookup()
	{
		Map<String, String> errors = new HashMap<>();
		if (SecurityUtil.isAdminUser()) {
			//log
			log.log(Level.INFO, MessageFormat.format("(Admin) Uploading lookup: {0}", uploadFile));

			//check content type
			Set<String> allowTypes = new HashSet<>();
			allowTypes.add("text/csv");
			allowTypes.add("application/vnd.ms-excel");
			allowTypes.add("application/vnd.oasis.opendocument.spreadsheet");

			if (allowTypes.contains(uploadFile.getContentType()) == false) {
				errors.put("uploadFile", "Format not supported.  Requires a csv text file.");
			}

			if (errors.isEmpty()) {
				//parse
				List<LookupEntity> lookupEntities = new ArrayList<>();

				Class lookupClass = null;
				StringBuilder errorsMessages = new StringBuilder();
				try (CSVReader reader = new CSVReader(new InputStreamReader(uploadFile.getInputStream()))) {

					lookupClass = Class.forName(DBManager.ENTITY_MODEL_PACKAGE + "." + entityName);
					List<String[]> allData = reader.readAll();
					for (String data[] : allData) {
						try {
							LookupEntity lookupEntity = (LookupEntity) lookupClass.newInstance();
							lookupEntity.importData(data);
							lookupEntities.add(lookupEntity);
						} catch (Exception e) {
							errorsMessages.append(MessageFormat.format(e.toString() + " -  Unable Process line: {0}", new Object[]{Arrays.toString(data)}));
						}
					}
					if (errorsMessages.length() > 0) {
						errors.put("uploadFile", errorsMessages.toString());
					}
				} catch (IOException ex) {
					throw new OpenStorefrontRuntimeException("Unable to read file: " + uploadFile.getFileName(), ex);
				} catch (ClassNotFoundException ex) {
					errors.put("entityName", "Unable to find Lookup Class:  " + entityName);
				} finally {
					try {
						uploadFile.delete();
					} catch (IOException ex) {
						throw new OpenStorefrontRuntimeException(ex);
					}
				}

				if (errors.isEmpty()) {
					if (lookupClass != null) {
						service.getLookupService().syncLookupImport(lookupClass, lookupEntities);
					}
				}
			}
			return streamUploadResponse(errors);
		} else {
			return new ErrorResolution(HttpServletResponse.SC_FORBIDDEN, "Access denied");
		}
	}

	public FileBean getUploadFile()
	{
		return uploadFile;
	}

	public void setUploadFile(FileBean uploadFile)
	{
		this.uploadFile = uploadFile;
	}

	public String getEntityName()
	{
		return entityName;
	}

	public void setEntityName(String entityName)
	{
		this.entityName = entityName;
	}

}
