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
package edu.usu.sdl.core.init;

import java.util.logging.Logger;

/**
 * Remove after 2.4
 *
 * @author dshurtleff
 */
public class MetaDataMigrationInit
		extends ApplyOnceInit
{

	private static final Logger LOG = Logger.getLogger(MetaDataMigrationInit.class.getName());

	public MetaDataMigrationInit()
	{
		super("Migrate Metadata");
	}

	@Override
	protected String internalApply()
	{
		StringBuilder results = new StringBuilder();
//		try
//		{
//			service.getContactService().convertContacts();
//			results.append("Converted Contacts");
//		} catch (Exception e) {
//			LOG.log(Level.WARNING, "Failed Converted Contacts", e);
//		}

		return results.toString();

	}

}
