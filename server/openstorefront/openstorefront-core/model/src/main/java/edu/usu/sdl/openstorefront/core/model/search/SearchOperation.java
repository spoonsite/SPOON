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
package edu.usu.sdl.openstorefront.core.model.search;

import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.validation.EnumLookup;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dshurtleff
 */
public class SearchOperation
{

	public enum MergeCondition
			implements EnumLookup
	{

		AND,
		OR,
		NOT;

		public List<String> apply(List<String> setA, List<String> setB)
		{
			List<String> results = new ArrayList();

			Set<String> masterSet = new HashSet<>();
			masterSet.addAll(setA);

			if (name().equals(AND.name())) {
				for (String item : setB) {
					if (masterSet.contains(item)) {
						results.add(item);
					}
				}
			} else if (name().equals(OR.name())) {
				masterSet.addAll(setB);
				results.addAll(masterSet);
			} else {
				for (String item : setB) {
					if (masterSet.contains(item) == false) {
						results.add(item);
					}
				}
			}

			return results;
		}

		@Override
		public boolean vaildValue(String value)
		{
			boolean vaild = false;
			for (MergeCondition type : MergeCondition.values()) {
				if (type.name().equals(value)) {
					vaild = true;
					break;
				}
			}
			return vaild;
		}
	}

	public enum NumberOperation
			implements EnumLookup
	{

		EQUALS,
		GREATERTHAN,
		GREATERTHANEQUALS,
		LESSTHAN,
		LESSTHANEQUALS;

		public String toQueryOperation()
		{
			String operation = "";
			switch (this) {
				case EQUALS:
					operation = GenerateStatementOption.OPERATION_EQUALS;
					break;
				case GREATERTHAN:
					operation = GenerateStatementOption.OPERATION_GREATER_THAN;
					break;
				case GREATERTHANEQUALS:
					operation = GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL;
					break;
				case LESSTHAN:
					operation = GenerateStatementOption.OPERATION_LESS_THAN;
					break;
				case LESSTHANEQUALS:
					operation = GenerateStatementOption.OPERATION_LESS_THAN_EQUAL;
					break;
			}
			return operation;
		}

		public boolean pass(int value, int checkValue)
		{
			boolean pass = false;

			switch (this) {
				case EQUALS:
					pass = value == checkValue;
					break;
				case GREATERTHAN:
					pass = value > checkValue;
					break;
				case GREATERTHANEQUALS:
					pass = value >= checkValue;
					break;
				case LESSTHAN:
					pass = value < checkValue;
					break;
				case LESSTHANEQUALS:
					pass = value <= checkValue;
					break;
			}

			return pass;
		}

		public boolean pass(BigDecimal value, BigDecimal checkValue)
		{
			boolean pass = false;

			if (value != null && checkValue != null) {			
				switch (this) {
					case EQUALS:
						pass = value.compareTo(checkValue) == 0;
						break;
					case GREATERTHAN:
						pass = value.compareTo(checkValue) > 0;
						break;
					case GREATERTHANEQUALS:
						pass = value.compareTo(checkValue) >= 0;
						break;
					case LESSTHAN:
						pass = value.compareTo(checkValue) < 0;
						break;
					case LESSTHANEQUALS:
						pass = value.compareTo(checkValue) <= 0;
						break;
				}
			}

			return pass;
		}		
		
		@Override
		public boolean vaildValue(String value)
		{
			boolean vaild = false;
			for (NumberOperation type : NumberOperation.values()) {
				if (type.name().equals(value)) {
					vaild = true;
					break;
				}
			}
			return vaild;
		}
	}

	public enum StringOperation
			implements EnumLookup
	{

		EQUALS,
		STARTS_LIKE,
		ENDS_LIKE,
		CONTAINS;

		public String toQueryString(String input)
		{
			switch (this) {
				case EQUALS:
					//Ignore
					break;
				case STARTS_LIKE:
					input = input + QueryByExample.LIKE_SYMBOL;
					break;
				case ENDS_LIKE:
					input = QueryByExample.LIKE_SYMBOL + input;
					break;
				case CONTAINS:
					input = QueryByExample.LIKE_SYMBOL + input + QueryByExample.LIKE_SYMBOL;
					break;
			}
			return input;
		}

		@Override
		public boolean vaildValue(String value)
		{
			boolean vaild = false;
			for (StringOperation type : StringOperation.values()) {
				if (type.name().equals(value)) {
					vaild = true;
					break;
				}
			}
			return vaild;
		}
	}

	public enum SearchType
			implements EnumLookup
	{

		COMPONENT,
		ENTRYTYPE,
		ATTRIBUTE,
		ATTRIBUTESET,
		ARCHITECTURE,
		INDEX,
		TAG,
		METADATA,
		USER_RATING,
		CONTACT,
		REVIEWPRO,
		REVIEWCON,
		QUESTION,
		QUESTION_RESPONSE,
		EVALUTATION_SCORE,
		REVIEW;

		@Override
		public boolean vaildValue(String value)
		{
			boolean vaild = false;
			for (SearchType type : SearchType.values()) {
				if (type.name().equals(value)) {
					vaild = true;
					break;
				}
			}
			return vaild;
		}
	}

}
