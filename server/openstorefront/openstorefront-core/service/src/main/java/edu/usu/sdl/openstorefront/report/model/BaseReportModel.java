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
package edu.usu.sdl.openstorefront.report.model;

import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import java.util.Date;
import java.util.List;

/**
 * All Report report models should extend this.
 *
 * @author dshurtleff
 */
public abstract class BaseReportModel
{

	private String title;
	private Date createTime = TimeUtil.currentDate();
	private Date dataStartDate;
	private Date dataEndDate;

	public BaseReportModel()
	{
	}

	public abstract <T> List<T> getData();

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getDataStartDate()
	{
		return dataStartDate;
	}

	public void setDataStartDate(Date dataStartDate)
	{
		this.dataStartDate = dataStartDate;
	}

	public Date getDataEndDate()
	{
		return dataEndDate;
	}

	public void setDataEndDate(Date dataEndDate)
	{
		this.dataEndDate = dataEndDate;
	}

}
