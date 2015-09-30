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
package edu.usu.sdl.openstorefront.core.model;

import java.io.InputStream;

/**
 * Hold the context of an upload into the system
 *
 * @author dshurtleff
 */
public class ImportContext
{

	private InputStream input;
	private FileHistoryAll fileHistoryAll = new FileHistoryAll();

	public ImportContext()
	{
	}

	public InputStream getInput()
	{
		return input;
	}

	public void setInput(InputStream input)
	{
		this.input = input;
	}

	public FileHistoryAll getFileHistoryAll()
	{
		return fileHistoryAll;
	}

	public void setFileHistoryAll(FileHistoryAll fileHistoryAll)
	{
		this.fileHistoryAll = fileHistoryAll;
	}

}
