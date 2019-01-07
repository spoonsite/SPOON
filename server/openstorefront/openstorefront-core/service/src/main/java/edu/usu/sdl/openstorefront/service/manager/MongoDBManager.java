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
package edu.usu.sdl.openstorefront.service.manager;

import edu.usu.sdl.openstorefront.common.manager.Initializable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class MongoDBManager
		implements Initializable
{

	private static final Logger LOG = Logger.getLogger(MongoDBManager.class.getName());

	private AtomicBoolean started = new AtomicBoolean(false);

	@Override
	public void initialize()
	{
		//connect and create client

		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void shutdown()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean isStarted()
	{
		return started.get();
	}

}
