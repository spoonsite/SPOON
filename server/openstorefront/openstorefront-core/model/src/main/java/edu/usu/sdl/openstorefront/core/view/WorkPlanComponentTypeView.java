/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanComponentType;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class WorkPlanComponentTypeView
		extends WorkPlanComponentType
{

	private static final long serialVersionUID = 1L;

	private String componentTypeDescription;
	private String componentTypeFullDescription;

	public WorkPlanComponentTypeView()
	{
	}

	public static WorkPlanComponentTypeView toView(WorkPlanComponentType componentType)
	{
		WorkPlanComponentTypeView view = new WorkPlanComponentTypeView();

		try {
			BeanUtils.copyProperties(view, componentType);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		Service service = ServiceProxyFactory.getServiceProxy();
		view.setComponentTypeDescription(TranslateUtil.translateComponentType(view.getComponentType()));
		view.setComponentTypeFullDescription(service.getComponentService().getComponentTypeParentsString(view.getComponentType(), false));
		return view;
	}

	public static List<WorkPlanComponentTypeView> toView(List<WorkPlanComponentType> types)
	{
		List<WorkPlanComponentTypeView> views = new ArrayList<>();

		types.forEach(type -> {
			views.add(toView(type));
		});

		return views;
	}

	public String getComponentTypeDescription()
	{
		return componentTypeDescription;
	}

	public void setComponentTypeDescription(String componentTypeDescription)
	{
		this.componentTypeDescription = componentTypeDescription;
	}

	public String getComponentTypeFullDescription()
	{
		return componentTypeFullDescription;
	}

	public void setComponentTypeFullDescription(String componentTypeFullDescription)
	{
		this.componentTypeFullDescription = componentTypeFullDescription;
	}

}
