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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Holds dashboard widget information for a user.")
public class DashboardWidget
		extends StandardEntity<DashboardWidget>
{

	public static final String FIELD_WIDGET_ORDER = "widgetOrder";

	@PK(generated = true)
	@NotNull
	private String widgetId;

	@FK(UserDashboard.class)
	@NotNull
	private String dashboardId;

	@ConsumeField
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String systemWidgetCode;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String widgetName;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_60)
	private String widgetColor;

	@ConsumeField
	private Integer widgetOrder;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_16K)
	private String widgetState;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_16K)
	private String optionalProperties;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public DashboardWidget()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		DashboardWidget dashboardWidget = (DashboardWidget) entity;
		this.setSystemWidgetCode(dashboardWidget.getSystemWidgetCode());
		this.setWidgetName(dashboardWidget.getWidgetName());
		this.setWidgetColor(dashboardWidget.getWidgetColor());
		this.setWidgetOrder(dashboardWidget.getWidgetOrder());
		this.setWidgetState(dashboardWidget.getWidgetState());
		this.setOptionalProperties(dashboardWidget.getOptionalProperties());

	}

	public String getWidgetId()
	{
		return widgetId;
	}

	public void setWidgetId(String widgetId)
	{
		this.widgetId = widgetId;
	}

	public String getSystemWidgetCode()
	{
		return systemWidgetCode;
	}

	public void setSystemWidgetCode(String systemWidgetCode)
	{
		this.systemWidgetCode = systemWidgetCode;
	}

	public String getWidgetName()
	{
		return widgetName;
	}

	public void setWidgetName(String widgetName)
	{
		this.widgetName = widgetName;
	}

	public String getWidgetColor()
	{
		return widgetColor;
	}

	public void setWidgetColor(String widgetColor)
	{
		this.widgetColor = widgetColor;
	}

	public String getDashboardId()
	{
		return dashboardId;
	}

	public void setDashboardId(String dashboardId)
	{
		this.dashboardId = dashboardId;
	}

	public String getWidgetState()
	{
		return widgetState;
	}

	public void setWidgetState(String widgetState)
	{
		this.widgetState = widgetState;
	}

	public String getOptionalProperties()
	{
		return optionalProperties;
	}

	public void setOptionalProperties(String optionalProperties)
	{
		this.optionalProperties = optionalProperties;
	}

	public Integer getWidgetOrder()
	{
		return widgetOrder;
	}

	public void setWidgetOrder(Integer widgetOrder)
	{
		this.widgetOrder = widgetOrder;
	}

}
