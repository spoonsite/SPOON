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
package edu.usu.sdl.describe.model;

import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 *
 * @author dshurtleff
 */
@Root(strict = false)
public class ContentCollection
{
	@Attribute
	private String classification;
	
	@Attribute
	private String updated;
	
	@Element(name = "metrics")
	private Metrics metrics;
	
	@Element(name="originator")
	private String originator;
	
	@ElementList(name = "mimeTypes")
	private List<MimeType> mimeTypes = new ArrayList<>();

	public ContentCollection()
	{
	}

	public String getClassification()
	{
		return classification;
	}

	public void setClassification(String classification)
	{
		this.classification = classification;
	}

	public String getUpdated()
	{
		return updated;
	}

	public void setUpdated(String updated)
	{
		this.updated = updated;
	}

	public Metrics getMetrics()
	{
		return metrics;
	}

	public void setMetrics(Metrics metrics)
	{
		this.metrics = metrics;
	}

	public String getOriginator()
	{
		return originator;
	}

	public void setOriginator(String originator)
	{
		this.originator = originator;
	}

	public List<MimeType> getMimeTypes()
	{
		return mimeTypes;
	}

	public void setMimeTypes(List<MimeType> mimeTypes)
	{
		this.mimeTypes = mimeTypes;
	}
	
	
	
}
