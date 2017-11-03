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

import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.entity.BaseEntity;
import javax.persistence.CascadeType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author kbair
 */
public class TestParent
		extends BaseEntity<TestParent>
{
	@PK
	@NotNull
	private String parentId;

	@NotNull
	@OneToOne(cascade = {CascadeType.ALL}, optional = false, orphanRemoval = true)
	private TestChild child;

	public String getParentId()
	{
		return parentId;
	}

	public void setParentId(String parentId)
	{
		this.parentId = parentId;
	}

	public TestChild getChild()
	{
		return child;
	}

	public void setChild(TestChild child)
	{
		this.child = child;
	}
}
