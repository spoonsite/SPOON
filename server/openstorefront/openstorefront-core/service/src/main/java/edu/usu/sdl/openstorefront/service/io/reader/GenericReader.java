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
package edu.usu.sdl.openstorefront.service.io.reader;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides a common interface for reading data
 *
 * @author dshurtleff
 */
public abstract class GenericReader<T>
		implements AutoCloseable
{

	private static final Logger log = Logger.getLogger(GenericReader.class.getName());

	protected int currentRecordNumber;
	protected InputStream in;

	//This may be null if it's not known
	protected Integer totalRecords;

	public GenericReader(InputStream in)
	{
		this.in = in;
	}

	public void preProcess()
	{
	}

	/**
	 * Reader records one at a time
	 *
	 * @param in
	 * @return record or null if no more records
	 */
	public abstract T nextRecord();

	@Override
	public void close() throws Exception
	{
		if (in != null) {
			try {
				in.close();
			} catch (Exception e) {
				log.log(Level.WARNING, "Unable to close input. (Continuing)", e);
			}
		}
	}

	public int getCurrentRecordNumber()
	{
		return currentRecordNumber;
	}

	public Integer getTotalRecords()
	{
		return totalRecords;
	}

}
