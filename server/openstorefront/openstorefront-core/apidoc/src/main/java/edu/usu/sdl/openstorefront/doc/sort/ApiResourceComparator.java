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
package edu.usu.sdl.openstorefront.doc.sort;

import edu.usu.sdl.openstorefront.doc.model.APIResourceModel;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Used to sort resources
 *
 * @author dshurtleff
 * @param <T>
 */
public class ApiResourceComparator<T extends APIResourceModel>
		implements Comparator<T>, Serializable
{

	@Override
	public int compare(T o1, T o2)
	{
		return o1.getResourceName().compareTo(o2.getResourceName());
	}

}
