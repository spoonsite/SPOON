/*
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.core.model.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author rfrazier
 */
public class ResultAttributeStatsAdapter
		extends XmlAdapter<String, HashMap<String, ResultAttributeStat>>
{

	@Override
	public HashMap<String, ResultAttributeStat> unmarshal(String v) throws Exception
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String marshal(HashMap<String, ResultAttributeStat> map) throws Exception
	{
		ObjectMapper mapper = new ObjectMapper();
		String jsonResult = mapper.writerWithDefaultPrettyPrinter()
			.writeValueAsString(map);
		return jsonResult;
	}


}
