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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.FileFormat;
import edu.usu.sdl.openstorefront.core.entity.FileHistory;
import edu.usu.sdl.openstorefront.core.entity.FileType;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class FileHistoryView
		extends FileHistory
{

	private String fileFormatDescription;
	private String fileTypeDescription;

	public FileHistoryView()
	{
	}

	public static FileHistoryView toView(FileHistory fileHistory)
	{
		FileHistoryView view = new FileHistoryView();
		try {
			BeanUtils.copyProperties(view, fileHistory);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}

		FileFormat fileFormat = ServiceProxyFactory.getServiceProxy().getLookupService().getLookupEnity(FileFormat.class, fileHistory.getFileFormat());
		view.setFileFormatDescription(fileFormat.getDescription());
		view.setFileTypeDescription(TranslateUtil.translate(FileType.class, fileFormat.getFileType()));

		return view;
	}

	public static List<FileHistoryView> toView(List<FileHistory> fileHistories)
	{
		List<FileHistoryView> views = new ArrayList<>();
		fileHistories.forEach(fileHistory -> {
			views.add(toView(fileHistory));
		});
		return views;
	}

	public String getFileFormatDescription()
	{
		return fileFormatDescription;
	}

	public void setFileFormatDescription(String fileFormatDescription)
	{
		this.fileFormatDescription = fileFormatDescription;
	}

	public String getFileTypeDescription()
	{
		return fileTypeDescription;
	}

	public void setFileTypeDescription(String fileTypeDescription)
	{
		this.fileTypeDescription = fileTypeDescription;
	}

}
