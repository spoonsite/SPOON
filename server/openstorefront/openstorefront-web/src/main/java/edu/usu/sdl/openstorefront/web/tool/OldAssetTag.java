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

package edu.usu.sdl.openstorefront.web.tool;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author dshurtleff
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OldAssetTag
{
	private long id;
	private OldAssetUser createdBy;
	private OldAssetTagDetail oldAssetTagDetail;

	public OldAssetTag()
	{
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public OldAssetUser getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(OldAssetUser createdBy)
	{
		this.createdBy = createdBy;
	}

	public OldAssetTagDetail getOldAssetTagDetail()
	{
		return oldAssetTagDetail;
	}

	public void setOldAssetTagDetail(OldAssetTagDetail oldAssetTagDetail)
	{
		this.oldAssetTagDetail = oldAssetTagDetail;
	}
	
}
