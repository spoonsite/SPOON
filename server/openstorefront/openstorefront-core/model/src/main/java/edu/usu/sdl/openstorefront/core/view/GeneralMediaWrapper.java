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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.core.annotation.DataType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class GeneralMediaWrapper
		extends ListWrapper
{

	@DataType(GeneralMediaView.class)
	private List<GeneralMediaView> data = new ArrayList<>();

	public GeneralMediaWrapper()
	{
	}

	public GeneralMediaWrapper(List<GeneralMediaView> data, long total)
	{
		this.data = data;
		this.results = this.data.size();
		this.totalNumber = total;
	}

	public List<GeneralMediaView> getData()
	{
		return data;
	}

	public void setData(List<GeneralMediaView> data)
	{
		this.data = data;
	}

}
