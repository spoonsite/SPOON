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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;

/**
 *
 * @author jlaw
 */
@APIDescription("Resource Type: Document, Homepage, Releases, etc.")
public class ResourceType
		extends LookupEntity<ResourceType>
{

	public static final String DOCUMENT = "DOC";
	public static final String INSTALL = "INSTALL";
	public static final String DI2E_EVAL_REPORT = "DI2EEVAL";
	public static final String HOME_PAGE = "HOME";
	public static final String CODE = "CODE";
	public static final String BINARY = "BINARIES";
	public static final String SERVICE = "SERVICE";

}
