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
package edu.usu.sdl.openstorefront.web.rest.model;

import au.com.bytecode.opencsv.CSVWriter;
import edu.usu.sdl.openstorefront.service.io.ExportImport;
import edu.usu.sdl.openstorefront.storage.model.ComponentTracking;
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

	private String name;
	private ComponentTracking data;

	public ComponentTrackingCompleteWrapper()
	{
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the data
	 */
	public ComponentTracking getData()
	{
		return data;
	}

	/**
	 * @param data the data to set
	 */
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
			getData().getComponentId(),
			getData().getComponentResourceId(),
			getData().getComponentTrackingId(),
			df.format(getData().getCreateDts()),
			getData().getClientIp(),
			getData().getTrackEventTypeCode(),
			getData().getCreateUser()
		});
		return stringWriter.toString();
	}

	@Override
	public void importData(String[] data)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
