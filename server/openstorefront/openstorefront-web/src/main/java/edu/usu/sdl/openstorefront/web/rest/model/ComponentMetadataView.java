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
package edu.usu.sdl.openstorefront.web.rest.model;

import edu.usu.sdl.openstorefront.storage.model.ComponentMetadata;
import java.util.Date;

/**
 *
 * @author dshurtleff
 */
public class ComponentMetadataView
{

	private String label;
	private String value;
	private Date updateDts;

	public ComponentMetadataView()
	{
	}

	public static ComponentMetadataView toView(ComponentMetadata metadata)
	{
		ComponentMetadataView componentMetadataView = new ComponentMetadataView();
		componentMetadataView.setLabel(metadata.getLabel());
		componentMetadataView.setUpdateDts(metadata.getUpdateDts());
		componentMetadataView.setValue(metadata.getValue());
		return componentMetadataView;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public Date getUpdateDts()
	{
		return updateDts;
	}

	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

}
