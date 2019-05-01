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
package edu.usu.sdl.core.init;

import edu.usu.sdl.core.CoreSystem;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class OrientToMongoMigration
		extends PostInitApplyOnce
{

	private static final Logger LOG = Logger.getLogger(OrientToMongoMigration.class.getName());

	public OrientToMongoMigration()
	{
		super("DB-MIGRATION-Mongo");
	}

	@Override
	protected String internalApply()
	{
		//1 mintue test
		StringBuilder details = new StringBuilder();

		LOG.log(Level.INFO, "Staring DB Migration");

		CoreSystem.standby("Working on DB Migration");
		for (int i = 0; i < 10; i++) {
			details.append("Moving Table: ").append(i).append("...");
			LOG.log(Level.INFO, "Moving Table: " + i);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException ex) {
				LOG.log(Level.WARNING, null, ex);
			}
			details.append(" Done<br>");

			CoreSystem.setDetailedStatus(details.toString());
		}
		CoreSystem.resume("Done with DB Migration");
		LOG.log(Level.INFO, "Done with DB Migration");
		return "testing";
	}

	@Override
	public int getPriority()
	{
		return 1;
	}

}
