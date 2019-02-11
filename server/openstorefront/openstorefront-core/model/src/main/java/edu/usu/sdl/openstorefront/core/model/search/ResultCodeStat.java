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

/**
 *
 * @author rfrazier
 */
public class ResultCodeStat
{
	private String codeLabel;
	private int count = 1;

	public String getCodeLabel()
	{
		return codeLabel;
	}

	public void setCodeLabel(String codeLabel)
	{
		this.codeLabel = codeLabel;
	}
	
	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}
		
	public void incrementCount()
	{
		this.count += 1;
	}
}
