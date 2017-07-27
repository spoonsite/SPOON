/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.common.util;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * Global Constants used in the Application
 *
 * @author dshurtleff
 */
public class OpenStorefrontConstant
{

	public static final String SORT_ASCENDING = "ASC";
	public static final String SORT_DESCENDING = "DESC";
	public static final String MAX_RECORDS = Integer.toString(Integer.MAX_VALUE);
	public static final String SYSTEM_USER = "SYSTEM";
	public static final String SYSTEM_ADMIN_USER = "SYSTEM-ADMIN";
	public static final String ANONYMOUS_USER = "ANONYMOUS";
	public static final String NOT_AVAILABLE = "NA";
	public static final String ERROR_LOGGER = "edu.usu.sdl.openstorefront.SYSTEM-ERROR-LOGGER";
	public static final String HEADER_USER_AGENT = "User-Agent";
	public static final String STATUS_VIEW_ALL = "ALL";
	public static final String DEFAULT_FROM_ADDRESS = "noreply@storefront.net";
	public static final String GENERAL_KEY_SEPARATOR = ":";

	//Field Max Sizes
	public static final int FIELD_SIZE_CRON = 250;
	public static final int FIELD_SIZE_USERNAME = 80;
	public static final int FIELD_SIZE_FIRSTNAME = 80;
	public static final int FIELD_SIZE_LASTNAME = 80;
	public static final int FIELD_SIZE_EMAIL = 250;
	public static final int FIELD_SIZE_EMAIL_LIST_SIZE = 32000;
	public static final int FIELD_SIZE_ORGANIZATION = 120;
	public static final int FIELD_SIZE_CODE = 20;
	public static final int FIELD_SIZE_PHONE = 80;
	public static final int FIELD_SIZE_DESCRIPTION = 255;
	public static final int FIELD_SIZE_COMPONENT_NAME = 255;
	public static final int FIELD_SIZE_COMPONENT_DESCRIPTION = 32000;
	public static final int FIELD_SIZE_GUID = 40;
	public static final int FIELD_SIZE_GENERAL_TEXT = 255;
	public static final int FIELD_SIZE_URL = 1024;
	public static final int FIELD_SIZE_QUESTION = 1024;
	public static final int FIELD_SIZE_RESPONSE = 4096;
	public static final int FIELD_SIZE_REVIEW_COMMENT = 4096;
	public static final int FIELD_SIZE_DETAILED_DESCRIPTION = 4096;
	public static final int FIELD_SIZE_ADMIN_MESSAGE = 32000;
	public static final int FIELD_SIZE_TAG = 60;
	public static final int FIELD_SIZE_ARTICLE_SIZE = 10485760;
	public static final int FIELD_SIZE_80 = 80;
	public static final int FIELD_SIZE_60 = 60;
	public static final int FIELD_SIZE_255 = 255;
	public static final int FIELD_SIZE_510 = 510;
	public static final int FIELD_SIZE_1K = 1024;
	public static final int FIELD_SIZE_4K = 4096;
	public static final int FIELD_SIZE_16K = 16384;
	public static final int FIELD_SIZE_32K = 32768;
	public static final int FIELD_SIZE_64K = 65536;
	public static final int FIELD_SIZE_1MB = 1048576;

	//This should only be used for warning; Not for storage validation.
	//This came from http://emailregex.com/  Previous one didn't cover all cases.
	public static final String EMAIL_PATTERN = "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$";

	public static final String ERRORS_MAX_COUNT_DEFAULT = "5000";
	public static final String DEFAULT_RECENT_CHANGE_EMAIL_INTERVAL = "28";

	public static final String ERROR_CODE_SYSTEM = "SYS";
	public static final String ERROR_CODE_REST_API = "API";
	public static final String ERROR_CODE_INTEGRATION = "INT";
	public static final String ERROR_CODE_REPORT = "RPT";

	private static final Map<String, String> mimeXref = loadMimeXref();

	private static Map<String, String> loadMimeXref()
	{
		Map<String, String> mimeXrefLocal = new HashMap<>();
		mimeXrefLocal.put("text/html", ".html");
		mimeXrefLocal.put("text/plain", ".txt");
		mimeXrefLocal.put("text/json", ".json");
		mimeXrefLocal.put("text/csv", ".csv");
		mimeXrefLocal.put("text/tsv", ".tsv");
		mimeXrefLocal.put("application/json", ".json");
		mimeXrefLocal.put("text/xml", ".xml");
		mimeXrefLocal.put("application/xml", ".xml");
		mimeXrefLocal.put("image/jpeg", ".jpg");
		mimeXrefLocal.put("image/jpg", ".jpg");
		mimeXrefLocal.put("image/png", ".png");
		mimeXrefLocal.put("image/gif", ".gif");
		mimeXrefLocal.put("image/bmp", ".bmp");
		mimeXrefLocal.put("video/mpg", ".mpg");
		mimeXrefLocal.put("video/mpeg", ".mpg");
		mimeXrefLocal.put("video/mp4", ".mp4");
		mimeXrefLocal.put("video/avi", ".avi");
		mimeXrefLocal.put("audio/mpg", ".mpg");
		mimeXrefLocal.put("audio/mpeg", ".mpg");
		mimeXrefLocal.put("audio/ogg", ".ogg");
		mimeXrefLocal.put("audio/mp4", ".mp4");
		mimeXrefLocal.put("application/doc", ".doc");
		mimeXrefLocal.put("application/docx", ".docx");
		mimeXrefLocal.put("application/xls", ".xls");
		mimeXrefLocal.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx");
		mimeXrefLocal.put("application/vnd.ms-excel", ".xls");
		mimeXrefLocal.put("application/pdf", ".pdf");
		mimeXrefLocal.put("application/zip", ".zip");
		mimeXrefLocal.put("application/gzip", ".gzip");
		return mimeXrefLocal;
	}

	public static String getFileExtensionForMime(String mimeType)
	{
		String ext = "NA";
		if (mimeType != null) {
			String found = mimeXref.get(mimeType);
			if (found != null) {
				ext = found;
			}
		}
		return ext;
	}

	public static String getMimeForFileExtension(String fileExtension)
	{
		String mime = "application/octet-stream";
		if (StringUtils.isNotBlank(fileExtension)) {
			String extensionToCheck = fileExtension;
			if (extensionToCheck.startsWith(".") == false) {
				extensionToCheck = "." + fileExtension;
			}
			if (mimeXref.containsValue(extensionToCheck.toLowerCase())) {
				//get first match
				for (String extCheckKey : mimeXref.keySet()) {
					String value = mimeXref.get(extCheckKey);
					if (value.equalsIgnoreCase(fileExtension)) {
						mime = value;
						break;
					}
				}
			}
		}
		return mime;
	}

	public static enum ListingType
	{

		ARTICLE("Article"),
		COMPONENT("Component");

		private final String description;

		private ListingType(String description)
		{
			this.description = description;
		}

		public String getDescription()
		{
			return description;
		}

	}

	public static enum TaskStatus
	{

		QUEUED,
		WORKING,
		DONE,
		CANCELLED,
		FAILED

	}

}
