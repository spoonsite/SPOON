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

package edu.usu.sdl.openstorefront.service.manager;

import java.io.File;

/**
 * Handles storage and retrieval of files
 * @author dshurtleff
 */
public class FileSystemManager
{
	private static final String MAIN_DIR = "/var/openstorefront";
	private static final String MAIN_PERM_DIR = "/var/openstorefront/perm";
	private static final String MAIN_TEMP_DIR = "/var/openstorefront/temp";
	private static final String SYSTEM_TEMP_DIR = System.getProperty("java.io.tmpdir");
	private static final String CONFIG_DIR = "/var/openstorefront/config";
	private static final String IMPORT_DIR = "/var/openstorefront/import";
	
	public static File getImportDir()
	{
		File importDir = new File(IMPORT_DIR);
		importDir.mkdirs();
		return importDir;
	}
	
	public static File getMediaDir()
	{	
		File mediaDir = new File(MAIN_PERM_DIR + "/media");
		mediaDir.mkdirs();
		return mediaDir;		
	}
	
}
