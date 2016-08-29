/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.spoon.importer;

/**
 *
 * @author dshurtleff
 */
public class ResourceAttachment
{
	private String fileData;
	private String componentName;
	private String resourceOriginalName;

	public ResourceAttachment()
	{
	}

	public String getFileData()
	{
		return fileData;
	}

	public void setFileData(String fileData)
	{
		this.fileData = fileData;
	}

	public String getResourceOriginalName()
	{
		return resourceOriginalName;
	}

	public void setResourceOriginalName(String resourceOriginalName)
	{
		this.resourceOriginalName = resourceOriginalName;
	}

	public String getComponentName()
	{
		return componentName;
	}

	public void setComponentName(String componentName)
	{
		this.componentName = componentName;
	}
	
}
