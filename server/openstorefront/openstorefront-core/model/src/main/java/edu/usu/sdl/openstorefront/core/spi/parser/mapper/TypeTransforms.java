/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.core.spi.parser.mapper;

import edu.usu.sdl.openstorefront.common.util.Convert;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Converts String to various data types
 * 
 * @author dshurtleff
 */
public enum TypeTransforms
	implements DataTransform<Object>	
{
	CONVERTBOOLEAN("Convert to Boolean") {
		@Override
		public Boolean transform(Object input)
		{
			return Convert.toBoolean(input);
		}
		
	},
	
	CONVERTBIGDECIMALZERO("Convert to BigDecimal; Default Zero") {
		@Override
		public BigDecimal transform(Object input)
		{
			return Convert.toBigDecimal(input, BigDecimal.ZERO);
		}
		
	},

	CONVERTBIGDECIMAL("Convert to BigDecimal") {
		@Override
		public BigDecimal transform(Object input)
		{
			return Convert.toBigDecimal(input);
		}
		
	},

	CONVERTINTERGER("Convert to BigDecimal") {
		@Override
		public Integer transform(Object input)
		{
			return Convert.toInteger(input);
		}
		
	},

	CONVERTLONG("Convert to BigDecimal") {
		@Override
		public Long transform(Object input)
		{
			return Convert.toLong(input);
		}
		
	},
	
	CONVERTDATE("Convert to Date using standard formats") {
		@Override
		public Date transform(Object input)
		{
			if (input != null) {
				return Convert.toDate(input.toString());
			} else {
				return null;
			}
		}
		
	};	
	
	private String description;

	private TypeTransforms(String description)
	{
		this.description = description;
	}

	public String getDescription()
	{
		return description;
	}
	
}
