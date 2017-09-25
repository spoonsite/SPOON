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
package edu.usu.sdl.openstorefront.core.sort;

import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.core.entity.ChecklistQuestion;
import java.util.Comparator;
import org.apache.commons.lang3.StringUtils;

/**
 * order by QID (numbers first)
 *
 * @author dshurtleff
 * @param <T>
 */
public class ChecklistQuestionComparator<T extends ChecklistQuestion>
		implements Comparator<T>
{

	public ChecklistQuestionComparator()
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
			String quid1 = o1.getQid();
			String quid2 = o2.getQid();

			if (StringUtils.isNumeric(quid1)
					&& StringUtils.isNumeric(quid2)) {
				return Convert.toInteger(quid1).compareTo(Convert.toInteger(quid2));
			} else {
				return o1.getQid().compareTo(o2.getQid());
			}
		}
	}

}
