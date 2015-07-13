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
package edu.usu.sdl.openstorefront.web.action.resolution;

import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;

public class RangeResolutionBuilder
{

	private String contentType;
	private InputStream inputStream;
	private long totalLength;
	private HttpServletRequest request;
	private String filename = null;

	public RangeResolutionBuilder()
	{
	}

	public RangeResolutionBuilder setContentType(String contentType)
	{
		this.contentType = contentType;
		return this;
	}

	public RangeResolutionBuilder setInputStream(InputStream inputStream)
	{
		this.inputStream = inputStream;
		return this;
	}

	public RangeResolutionBuilder setTotalLength(long totalLength)
	{
		this.totalLength = totalLength;
		return this;
	}

	public RangeResolutionBuilder setRequest(HttpServletRequest request)
	{
		this.request = request;
		return this;
	}

	public RangeResolutionBuilder setFilename(String filename)
	{
		this.filename = filename;
		return this;
	}

	public RangeResolution createRangeResolution()
	{
		return new RangeResolution(contentType, inputStream, totalLength, request, filename);
	}

}
