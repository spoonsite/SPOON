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
package edu.usu.sdl.openstorefront.core.api.query;

import edu.usu.sdl.openstorefront.core.entity.BaseEntity;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kbair
 * @param <T>
 */
public class WhereClauseGroup<T extends BaseEntity> implements WhereClause
{

	private final List<SpecialOperatorModel<T>> extraWhereClause = new ArrayList<>();
	private GenerateStatementOption option = new GenerateStatementOptionBuilder().build();

	public List<SpecialOperatorModel<T>> getExtraWhereClause()
	{
		return extraWhereClause;
	}

	public GenerateStatementOption getStatementOption()
	{
		return option;
	}

	public void setStatementOption(GenerateStatementOption option)
	{
		this.option = option;
	}
}
