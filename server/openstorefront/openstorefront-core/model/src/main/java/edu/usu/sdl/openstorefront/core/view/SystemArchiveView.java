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
import edu.usu.sdl.openstorefront.core.entity.IODirectionType;
import edu.usu.sdl.openstorefront.core.entity.ImportModeType;
import edu.usu.sdl.openstorefront.core.entity.RunStatus;
import edu.usu.sdl.openstorefront.core.entity.SystemArchive;
import edu.usu.sdl.openstorefront.core.entity.SystemArchiveType;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class SystemArchiveView
		extends SystemArchive
{

	private String systemArchiveTypeDescription;
	private String runStatusDescription;
	private String ioDirectionTypeDescription;
	private String importModeTypeDescription;

	public static SystemArchiveView toView(SystemArchive archive)
	{
		SystemArchiveView view = new SystemArchiveView();
		try {
			BeanUtils.copyProperties(view, archive);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		view.setSystemArchiveTypeDescription(TranslateUtil.translate(SystemArchiveType.class, archive.getSystemArchiveType()));
		view.setRunStatusDescription(TranslateUtil.translate(RunStatus.class, archive.getRunStatus()));
		view.setIoDirectionTypeDescription(TranslateUtil.translate(IODirectionType.class, archive.getIoDirectionType()));
		view.setImportModeTypeDescription(TranslateUtil.translate(ImportModeType.class, archive.getImportModeType()));

		Path path = view.pathToArchive();
		if (path == null || !path.toFile().exists()) {
			view.setArchiveFilename(null);
		}

		return view;
	}

	public static List<SystemArchiveView> toView(List<SystemArchive> archives)
	{
		List<SystemArchiveView> views = new ArrayList<>();
		archives.forEach(archive -> {
			views.add(toView(archive));
		});
		return views;
	}

	public String getSystemArchiveTypeDescription()
	{
		return systemArchiveTypeDescription;
	}

	public void setSystemArchiveTypeDescription(String systemArchiveTypeDescription)
	{
		this.systemArchiveTypeDescription = systemArchiveTypeDescription;
	}

	public String getRunStatusDescription()
	{
		return runStatusDescription;
	}

	public void setRunStatusDescription(String runStatusDescription)
	{
		this.runStatusDescription = runStatusDescription;
	}

	public String getIoDirectionTypeDescription()
	{
		return ioDirectionTypeDescription;
	}

	public void setIoDirectionTypeDescription(String ioDirectionTypeDescription)
	{
		this.ioDirectionTypeDescription = ioDirectionTypeDescription;
	}

	public String getImportModeTypeDescription()
	{
		return importModeTypeDescription;
	}

	public void setImportModeTypeDescription(String importModeTypeDescription)
	{
		this.importModeTypeDescription = importModeTypeDescription;
	}

}
