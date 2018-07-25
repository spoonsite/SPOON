/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.core.model;

import edu.usu.sdl.openstorefront.core.spi.EntityEventListener;

/**
 * Used to register a new listener
 *
 * @author dshurtleff
 */
public class EntityEventRegistrationModel
{

	private String registrationId;
	private String name;
	private EntityEventListener listener;

	public EntityEventRegistrationModel()
	{
	}

	public String getRegistrationId()
	{
		return registrationId;
	}

	public void setRegistrationId(String registrationId)
	{
		this.registrationId = registrationId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public EntityEventListener getListener()
	{
		return listener;
	}

	public void setListener(EntityEventListener listener)
	{
		this.listener = listener;
	}

}
