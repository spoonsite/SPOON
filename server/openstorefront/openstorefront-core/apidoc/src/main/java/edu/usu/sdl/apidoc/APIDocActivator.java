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
package edu.usu.sdl.apidoc;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class APIDocActivator
		implements BundleActivator
{

	private static final Logger log = Logger.getLogger(APIDocActivator.class.getName());

	@Override
	public void start(BundleContext context) throws Exception
	{
		log.log(Level.INFO, "API Doc bundle started.");
	}

	@Override
	public void stop(BundleContext context) throws Exception
	{
		log.log(Level.INFO, "API Doc bundle stopped.");
	}

}
