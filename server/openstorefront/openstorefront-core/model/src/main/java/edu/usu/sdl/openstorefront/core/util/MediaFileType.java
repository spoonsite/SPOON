/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.core.util;

import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;

/**
 * Types of MediaFiles
 *
 * @author kbair
 */
public enum MediaFileType
{
	GENERAL(FileSystemManager.GENERAL_MEDIA_DIR),
	SUPPORT(FileSystemManager.SUPPORT_MEDIA_DIR),
	RESOURCE(FileSystemManager.RESOURCE_DIR),
	MEDIA(FileSystemManager.MEDIA_DIR);

	private final String directory;

	private MediaFileType(String dir)
	{
		this.directory = dir;
	}

	public String getPath()
	{
		return FileSystemManager.getInstance().getDir(this.directory).getPath();
	}
}
