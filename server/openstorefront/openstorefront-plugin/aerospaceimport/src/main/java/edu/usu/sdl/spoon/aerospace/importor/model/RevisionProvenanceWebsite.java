/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.spoon.aerospace.importor.model;

import org.simpleframework.xml.Element;

/**
 *
 * @author dshurtleff
 */
public class RevisionProvenanceWebsite
{

	@Element
	private String url;

	@Element(name = "snapshot_url")
	private String snapshotUrl;

	@Element(name = "snapshot_id")
	private String snapshotId;

	@Element
	private String description;

	@Element
	private String title;

	@Element(name = "last_visited")
	private String lastVisited;

	public RevisionProvenanceWebsite()
	{
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getSnapshotUrl()
	{
		return snapshotUrl;
	}

	public void setSnapshotUrl(String snapshotUrl)
	{
		this.snapshotUrl = snapshotUrl;
	}

	public String getSnapshotId()
	{
		return snapshotId;
	}

	public void setSnapshotId(String snapshotId)
	{
		this.snapshotId = snapshotId;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getLastVisited()
	{
		return lastVisited;
	}

	public void setLastVisited(String lastVisited)
	{
		this.lastVisited = lastVisited;
	}

}
