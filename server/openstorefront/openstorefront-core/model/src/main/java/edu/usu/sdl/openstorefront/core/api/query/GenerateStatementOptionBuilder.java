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
package edu.usu.sdl.openstorefront.core.api.query;

import static edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption.CONDITION_AND;
import static edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption.OPERATION_EQUALS;
import static edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption.PARAMETER_SUFFIX_DEFAULT;

public class GenerateStatementOptionBuilder
{

	private String operation = OPERATION_EQUALS;
	private String condition = CONDITION_AND;
	private String parameterSuffix = PARAMETER_SUFFIX_DEFAULT;
	private String method = "";

	public GenerateStatementOptionBuilder()
	{
	}

	public GenerateStatementOptionBuilder setOperation(String operation)
	{
		this.operation = operation;
		return this;
	}

	public GenerateStatementOptionBuilder setCondition(String condition)
	{
		this.condition = condition;
		return this;
	}

	public GenerateStatementOptionBuilder setParameterSuffix(String parameterSuffix)
	{
		this.parameterSuffix = parameterSuffix;
		return this;
	}

	public GenerateStatementOptionBuilder setMethod(String method)
	{
		this.method = method;
		return this;
	}

	public GenerateStatementOption build()
	{
		return new GenerateStatementOption(operation, condition, parameterSuffix, method);
	}

}
