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
package edu.usu.sdl.openstorefront.core.api.query;

import java.util.ArrayList;
import java.util.List;

/**
 * Used for query generation
 *
 * @author dshurtleff
 */
public class GenerateStatementOption
{

	public static final String CONDITION_AND = " AND ";
	public static final String CONDITION_OR = " OR";
	public static final String CONDITION_COMMA = " , ";

	public static final String OPERATION_EQUALS = "=";
	public static final String OPERATION_NOT_EQUALS = " <> ";
	public static final String OPERATION_LIKE = "LIKE";
	public static final String OPERATION_NOT_NULL = " IS NOT NULL";
	public static final String OPERATION_NULL = " IS NULL";
	public static final String OPERATION_LESS_THAN_EQUAL = "<=";
	public static final String OPERATION_LESS_THAN = "<";
	public static final String OPERATION_GREATER_THAN_EQUAL = " >=";
	public static final String OPERATION_GREATER_THAN = ">";
	public static final String OPERATION_IN = " IN";
	public static final String OPERATION_NOT_IN = " NOT IN";

	public static final String PARAMETER_SUFFIX_DEFAULT = "Param";
	public static final String PARAMETER_SUFFIX_SET = "SetParam";
	public static final String PARAMETER_SUFFIX_END_RANGE = "EndRangeParam";

	public static final String METHOD_LOWER_CASE = ".toLowerCase()";
	public static final String METHOD_UPPER_CASE = ".toUpperCase()";

	private String operation = OPERATION_EQUALS;
	private String condition = CONDITION_AND;
	private String parameterSuffix = PARAMETER_SUFFIX_DEFAULT;
	private String method = "";
	private List<String> values = new ArrayList<>();

	public GenerateStatementOption()
	{
	}

	public GenerateStatementOption(String operation, String condition, String parameterSuffix, String method)
	{
		this.operation = operation;
		this.condition = condition;
		this.parameterSuffix = parameterSuffix;
		this.method = method;
	}

	public String getOperation()
	{
		return operation;
	}

	public void setOperation(String operation)
	{
		this.operation = operation;
	}

	public String getCondition()
	{
		return condition;
	}

	public void setCondition(String condition)
	{
		this.condition = condition;
	}

	public String getParameterSuffix()
	{
		return parameterSuffix;
	}

	public void setParameterSuffix(String parameterSuffix)
	{
		this.parameterSuffix = parameterSuffix;
	}

	public String getMethod()
	{
		return method;
	}

	public void setMethod(String method)
	{
		this.method = method;
	}

	public void setParameterValues(List<String> values)
	{
		this.values = values;
	}

	public List<String> getParameterValues()
	{
		return values;
	}

}
