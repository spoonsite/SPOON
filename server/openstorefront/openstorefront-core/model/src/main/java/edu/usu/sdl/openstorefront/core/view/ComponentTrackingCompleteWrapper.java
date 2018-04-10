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
package edu.usu.sdl.openstorefront.core.view;

import au.com.bytecode.opencsv.CSVWriter;
import edu.usu.sdl.openstorefront.core.entity.ComponentTracking;
import edu.usu.sdl.openstorefront.core.entity.TrackEventCode;
import edu.usu.sdl.openstorefront.core.util.ExportImport;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author jlaw
 */
public class ComponentTrackingCompleteWrapper
		implements ExportImport
{

	public static final String FIELD_NAME = "name";

	private String name;
	private ComponentTracking data;
	private String componentTypeLabel;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ComponentTracking getData()
	{
		return data;
	}

	public void setData(ComponentTracking data)
	{
		this.data = data;
	}

	@Override
	public String export()
	{
		StringWriter stringWriter = new StringWriter();
		CSVWriter writer = new CSVWriter(stringWriter);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

		writer.writeNext(new String[]{getName(),
			TranslateUtil.translateComponentType(getData().getComponentType()),
			getData().getComponentId(),
			getData().getComponentTrackingId(),
			df.format(getData().getEventDts()),
			getData().getClientIp(),
			TranslateUtil.translate(TrackEventCode.class, getData().getTrackEventTypeCode()),
			getData().getResourceLink(),
			getData().getResourceType(),
			getData().getRestrictedResouce() != null ? Boolean.toString(getData().getRestrictedResouce()) : "",
			getData().getCreateUser()
		});
		return stringWriter.toString();
	}

	@Override
	public void importData(String[] data)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public String getComponentTypeLabel()
	{
		return componentTypeLabel;
	}

	public void setComponentTypeLabel(String componentTypeLabel)
	{
		this.componentTypeLabel = componentTypeLabel;
	}

}
