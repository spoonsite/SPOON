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
package edu.usu.sdl.core;

import com.google.common.io.Files;
import edu.usu.sdl.openstorefront.common.util.Convert;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class UseCase
{

	@Test
	public void testGroup() throws IOException
	{
		List<String> lines = Files.readLines(new File("c:/temp/store.tab"), Charset.defaultCharset());
		Map<String, List<DataModel>> modelList = new HashMap<>();
		lines.remove(0);
		for (String line : lines) {

			String data[] = line.split("\t");

			DataModel dataModel = new DataModel();
			dataModel.setOpens(Convert.toInteger(data[3]));
			dataModel.setWsi2Gov(Convert.toInteger(data[1]));
			dataModel.setWso2Ent(Convert.toInteger(data[2]));

			if (modelList.containsKey(data[0])) {
				modelList.get(data[0]).add(dataModel);
			} else {
				List<DataModel> dataModels = new ArrayList<>();
				dataModels.add(dataModel);
				modelList.put(data[0], dataModels);
			}
		}

		for (String key : modelList.keySet()) {
			List<DataModel> dataModels = modelList.get(key);
			System.out.println(key + "\t"
					+ dataModels.stream().mapToInt(DataModel::getWsi2Gov).average().getAsDouble()
					+ "\t"
					+ dataModels.stream().mapToInt(DataModel::getWso2Ent).average().getAsDouble()
					+ "\t"
					+ dataModels.stream().mapToInt(DataModel::getOpens).average().getAsDouble()
			);
		}

	}

	private class DataModel
	{

		private int wso2Ent;
		private int wsi2Gov;
		private int opens;

		public DataModel()
		{
		}

		public int getWso2Ent()
		{
			return wso2Ent;
		}

		public void setWso2Ent(int wso2Ent)
		{
			this.wso2Ent = wso2Ent;
		}

		public int getWsi2Gov()
		{
			return wsi2Gov;
		}

		public void setWsi2Gov(int wsi2Gov)
		{
			this.wsi2Gov = wsi2Gov;
		}

		public int getOpens()
		{
			return opens;
		}

		public void setOpens(int opens)
		{
			this.opens = opens;
		}

	}

}
