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
package edu.usu.sdl.openstorefront.service.testModels;

import edu.usu.sdl.openstorefront.core.entity.BaseEntity;

/**
 *
 * @author kbair
 */
public class TestClassWithEnum
		extends BaseEntity<TestClassWithEnum>
{
	private String id;

	private TestEnum myEnum;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}
	
	public TestEnum getMyEnum()
	{
		return myEnum;
	}

	public void setMyEnum(TestEnum myEnum)
	{
		this.myEnum = myEnum;
	}
}
