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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.ChangeLog;
import edu.usu.sdl.openstorefront.core.entity.ChangeType;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class ChangeLogView
		extends ChangeLog
{

	private String changeTypeDescription;

	public static ChangeLogView toView(ChangeLog changeLog)
	{
		ChangeLogView view = new ChangeLogView();
		try {
			BeanUtils.copyProperties(view, changeLog);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		view.setChangeTypeDescription(TranslateUtil.translate(ChangeType.class, changeLog.getChangeType()));
		return view;
	}

	public static List<ChangeLogView> toView(List<ChangeLog> changeLogs)
	{
		List<ChangeLogView> views = new ArrayList<>();

		changeLogs.forEach(change -> {
			views.add(toView(change));
		});

		return views;
	}

	public String getChangeTypeDescription()
	{
		return changeTypeDescription;
	}

	public void setChangeTypeDescription(String changeTypeDescription)
	{
		this.changeTypeDescription = changeTypeDescription;
	}

}
