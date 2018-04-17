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
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.FileDataMap;
import edu.usu.sdl.openstorefront.core.entity.FileFormat;
import edu.usu.sdl.openstorefront.core.entity.FileHistory;
import edu.usu.sdl.openstorefront.core.entity.FileType;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
	private String fileMappingApplied;
	private long warningsCount;
	private long errorsCount;

	public static FileHistoryView toView(FileHistory fileHistory, Map<String, List<FileDataMap>> dataMaps)
	{
		FileHistoryView view = new FileHistoryView();
		try {
			BeanUtils.copyProperties(view, fileHistory);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}

		FileFormat fileFormat = ServiceProxyFactory.getServiceProxy().getImportService().findFileFormat(fileHistory.getFileFormat());
		if (fileFormat != null) {
			view.setFileFormatDescription(fileFormat.getDescription());
			view.setFileTypeDescription(TranslateUtil.translate(FileType.class, fileFormat.getFileType()));
		} else {
			view.setFileFormatDescription(fileHistory.getFileFormat());
			view.setFileTypeDescription(OpenStorefrontConstant.NOT_AVAILABLE);
		}

		if (fileHistory.getFileDataMapId() != null) {
			if (dataMaps.containsKey(fileHistory.getFileDataMapId())) {
				FileDataMap fileDataMap = dataMaps.get(fileHistory.getFileDataMapId()).get(0);
				view.setFileMappingApplied(fileDataMap.getName());
			}
		}

		return view;
	}

	public static List<FileHistoryView> toView(List<FileHistory> fileHistories)
	{
		List<FileHistoryView> views = new ArrayList<>();

		FileDataMap fileDataMap = new FileDataMap();
		List<FileDataMap> fileDataMaps = fileDataMap.findByExample();
		Map<String, List<FileDataMap>> dataMaps = fileDataMaps.stream().collect(Collectors.groupingBy(FileDataMap::getFileDataMapId));

		fileHistories.forEach(fileHistory -> {
			views.add(toView(fileHistory, dataMaps));
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

	public long getErrorsCount()
	{
		return errorsCount;
	}

	public void setErrorsCount(long errorsCount)
	{
		this.errorsCount = errorsCount;
	}

	public long getWarningsCount()
	{
		return warningsCount;
	}

	public void setWarningsCount(long warningsCount)
	{
		this.warningsCount = warningsCount;
	}

	public String getFileMappingApplied()
	{
		return fileMappingApplied;
	}

	public void setFileMappingApplied(String fileMappingApplied)
	{
		this.fileMappingApplied = fileMappingApplied;
	}

}
