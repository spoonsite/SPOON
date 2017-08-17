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

import edu.usu.sdl.openstorefront.core.view.ChecklistResponseView;
import java.util.Comparator;

/**
 *
 * @author dshurtleff
 */
public class ChecklistResponseViewComparator<T extends ChecklistResponseView>
		implements Comparator<T>
{

	public ChecklistResponseViewComparator()
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
		} else {
			Integer sort1 = o1.getSortOrder();
			Integer sort2 = o2.getSortOrder();
			if (sort1 == null
					&& sort2 == null) {
				return 0;
			} else if (sort1 != null
					&& sort2 == null) {
				return 1;
			} else if (sort1 == null
					&& sort2 != null) {
				return -1;
			} else {
				return sort1.compareTo(sort2);
			}
		}
	}

}
