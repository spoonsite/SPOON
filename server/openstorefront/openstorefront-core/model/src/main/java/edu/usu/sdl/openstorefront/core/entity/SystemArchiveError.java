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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Holds error information for a system archive")
public class SystemArchiveError
		extends StandardEntity<SystemArchiveError>
{

	@NotNull
	@PK(generated = true)
	private String archiveErrorId;

	@NotNull
	@FK(SystemArchive.class)
	private String archiveId;

	@NotNull
	private String message;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public SystemArchiveError()
	{
	}

	public String getArchiveErrorId()
	{
		return archiveErrorId;
	}

	public void setArchiveErrorId(String archiveErrorId)
	{
		this.archiveErrorId = archiveErrorId;
	}

	public String getArchiveId()
	{
		return archiveId;
	}

	public void setArchiveId(String archiveId)
	{
		this.archiveId = archiveId;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

}
