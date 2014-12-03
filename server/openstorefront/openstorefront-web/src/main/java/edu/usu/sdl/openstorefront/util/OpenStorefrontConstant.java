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
package edu.usu.sdl.openstorefront.util;

import java.util.HashMap;
import java.util.Map;

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

	//Field Max Sizes
	public static final int FIELD_SIZE_CRON = 250;
	public static final int FIELD_SIZE_USERNAME = 80;
	public static final int FIELD_SIZE_FIRSTNAME = 80;
	public static final int FIELD_SIZE_LASTNAME = 80;
	public static final int FIELD_SIZE_EMAIL = 250;
	public static final int FIELD_SIZE_EMAIL_LIST_SIZE = 32000;
	public static final int FIELD_SIZE_ORGANIZATION = 120;
	public static final int FIELD_SIZE_CODE = 20;
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

	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static final String ERRORS_MAX_COUNT_DEFAULT = "5000";

	private static final Map<String, String> mimeXref = loadMimeXref();

	private static Map<String, String> loadMimeXref()
	{
		Map<String, String> mimeXref = new HashMap<>();
		mimeXref.put("text/html", ".html");
		mimeXref.put("text/plain", ".txt");
		mimeXref.put("text/json", ".json");
		mimeXref.put("text/csv", ".csv");
		mimeXref.put("text/tsv", ".tsv");
		mimeXref.put("application/json", ".json");
		mimeXref.put("text/xml", ".xml");
		mimeXref.put("application/xml", ".xml");
		mimeXref.put("image/jpeg", ".jpg");
		mimeXref.put("image/jpg", ".jpg");
		mimeXref.put("image/png", ".png");
		mimeXref.put("image/gif", ".gif");
		mimeXref.put("image/bmp", ".bmp");
		mimeXref.put("video/mpg", ".mpg");
		mimeXref.put("video/mpeg", ".mpg");
		mimeXref.put("video/mp4", ".mp4");
		mimeXref.put("video/avi", ".avi");
		mimeXref.put("audio/mpg", ".mpg");
		mimeXref.put("audio/mpeg", ".mpg");
		mimeXref.put("audio/ogg", ".ogg");
		mimeXref.put("audio/mp4", ".mp4");
		mimeXref.put("application/doc", ".doc");
		mimeXref.put("application/docx", ".docx");
		mimeXref.put("application/xls", ".xls");
		mimeXref.put("application/xlsx", ".xlsx");
		mimeXref.put("application/pdf", ".pdf");
		mimeXref.put("application/zip", ".zip");
		mimeXref.put("application/gzip", ".gzip");
		return mimeXref;
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

}
