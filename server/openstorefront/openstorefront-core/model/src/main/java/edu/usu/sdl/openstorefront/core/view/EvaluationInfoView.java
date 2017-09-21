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
package edu.usu.sdl.openstorefront.core.view;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author dshurtleff
 */
public class EvaluationInfoView
{

	private String evaluationId;
	private Date lastChangeDate;
	private BigDecimal progessPercent;

	public EvaluationInfoView()
	{
	}

	public String getEvaluationId()
	{
		return evaluationId;
	}

	public void setEvaluationId(String evaluationId)
	{
		this.evaluationId = evaluationId;
	}

	public Date getLastChangeDate()
	{
		return lastChangeDate;
	}

	public void setLastChangeDate(Date lastChangeDate)
	{
		this.lastChangeDate = lastChangeDate;
	}

	public BigDecimal getProgessPercent()
	{
		return progessPercent;
	}

	public void setProgessPercent(BigDecimal progessPercent)
	{
		this.progessPercent = progessPercent;
	}

}
