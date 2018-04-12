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
package edu.usu.sdl.openstorefront.common.exception;

/**
 * Thrown with there are attached references to an entity that must be manually
 * removed
 *
 * @author dshurtleff
 */
public class AttachedReferencesException
		extends Exception
{

	private static final long serialVersionUID = 1L;

	public AttachedReferencesException()
	{
		this("There are attached references to this entity.");
	}

	public AttachedReferencesException(String message)
	{
		super(message);
	}

	public AttachedReferencesException(Throwable cause)
	{
		super(cause);
	}

	public AttachedReferencesException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
