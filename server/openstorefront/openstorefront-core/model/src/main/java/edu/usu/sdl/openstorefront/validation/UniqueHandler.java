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
package edu.usu.sdl.openstorefront.validation;

import java.lang.reflect.Field;

/**
 * Used to determine Uniqueness
 *
 * @author dshurtleff
 * @param <T> Entity that the unique handler can handle
 */
public interface UniqueHandler<T>
{

	public boolean isUnique(Field field, Object value, T fullDataObject);

	/**
	 * Retrieves the error message when uniqueness test fails
	 *
	 * @return Error message
	 */
	public String getMessage();

}
