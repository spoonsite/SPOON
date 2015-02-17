/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.sort;

import edu.usu.sdl.openstorefront.service.transfermodel.Architecture;
import edu.usu.sdl.openstorefront.util.StringProcessor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import org.apache.commons.lang3.StringUtils;

/**
 * Compare Architecture based on codes
 *
 * @author dshurtleff
 * @param <T>
 */
public class ArchitectureComparator<T extends Architecture>
		implements Comparator<T>, Serializable
{

	@Override
	public int compare(T o1, T o2)
	{
		//Codes should be numbers
		BigDecimal codeKey1;
		BigDecimal codeKey2;

		if (o1.getSortOrder() != null && o2.getSortOrder() != null) {
			return o1.getSortOrder().compareTo(o2.getSortOrder());
		} else {

			if (StringUtils.isNotBlank(o1.getArchitectureCode())) {
				codeKey1 = StringProcessor.archtecureCodeToDecimal(o1.getArchitectureCode());
			} else {
				codeKey1 = StringProcessor.archtecureCodeToDecimal(o1.getAttributeCode());
			}

			if (StringUtils.isNotBlank(o2.getArchitectureCode())) {
				codeKey2 = StringProcessor.archtecureCodeToDecimal(o2.getArchitectureCode());
			} else {
				codeKey2 = StringProcessor.archtecureCodeToDecimal(o2.getAttributeCode());
			}

			return codeKey1.compareTo(codeKey2);
		}
	}

}
