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
package edu.usu.sdl.openstorefront.core.sort;

import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import java.util.Comparator;

/**
 *
 * @author cyearsley
 * @param <T>
 * 
 */
public class RelevanceComparator<T extends ComponentSearchView>
		implements Comparator<T>
{
	public RelevanceComparator()
	{
	}
	
	@Override
	public int compare(T o1, T o2)
	{
		if (o1 == null && o2 == null) {
			return 0;
		} else if (o1 != null && o2 == null) {
			return 1;
		} else if (o1 == null && o2 != null) {
			return -1;
		} else if (o1.getSearchScore() == o2.getSearchScore()) {
			
			//	Ascending
			return o1.getName().compareTo(o2.getName());
		} else {
			
			//	Descending
			return o1.getSearchScore() > o2.getSearchScore() ? -1 : 1;
		}
	}
}
