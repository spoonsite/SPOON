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
package edu.usu.sdl.openstorefront.sort;

import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.util.StringProcessor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class AttributeCodeArchComparator<T extends AttributeCode>
		implements Comparator<T>, Serializable
{

	@Override
	public int compare(T o1, T o2)
	{
		//Codes should be numbers (Note the description includes the code so they should be sort as well)
		BigDecimal codeKey1;
		BigDecimal codeKey2;

		if (o1.getSortOrder() != null && o2.getSortOrder() != null) {
			return o1.getSortOrder().compareTo(o2.getSortOrder());
		} else {
			if (StringUtils.isNotBlank(o1.getArchitectureCode())) {
				codeKey1 = StringProcessor.archtecureCodeToDecimal(o1.getArchitectureCode());
			} else {
				codeKey1 = StringProcessor.archtecureCodeToDecimal(o1.getAttributeCodePk().getAttributeCode());
			}

			if (StringUtils.isNotBlank(o2.getArchitectureCode())) {
				codeKey2 = StringProcessor.archtecureCodeToDecimal(o2.getArchitectureCode());
			} else {
				codeKey2 = StringProcessor.archtecureCodeToDecimal(o2.getAttributeCodePk().getAttributeCode());
			}

			int results = codeKey1.compareTo(codeKey2);
			if (results != 0) {
				return results;
			} else {
				return o1.getAttributeCodePk().getAttributeCode().compareTo(o2.getAttributeCodePk().getAttributeCode());
			}
		}
	}

}
