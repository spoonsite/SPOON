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

/**
 *
 * @author dshurtleff
 */
public class OpenStorefrontConstant
{

	public static final String SORT_ASCENDING = "ASC";
	public static final String SORT_DESCENDING = "DSC";
	public static final String MAX_RECORDS = Integer.toString(Integer.MAX_VALUE);
	public static final String SYSTEM_USER = "SYSTEM";
	public static final String SYSTEM_ADMIN_USER = "SYSTEM-ADMIN";

	//Field Max Sizes
	public static final int FIELD_SIZE_USERNAME = 80;
	public static final int FIELD_SIZE_FIRSTNAME = 80;
	public static final int FIELD_SIZE_LASTNAME = 80;
	public static final int FIELD_SIZE_EMAIL = 250;
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
	public static final int FIELD_SIZE_TAG = 60;

	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

}
