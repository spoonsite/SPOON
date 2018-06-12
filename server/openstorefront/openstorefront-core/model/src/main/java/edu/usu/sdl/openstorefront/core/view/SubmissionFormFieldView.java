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
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormField;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormFieldMappingType;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormFieldType;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class SubmissionFormFieldView
		extends SubmissionFormField
{

	private String mappingTypeDescription;
	private String fieldTypeDescription;

	@SuppressWarnings({"squid:S1186"})
	public SubmissionFormFieldView()
	{
	}

	public static SubmissionFormFieldView toView(SubmissionFormField formField)
	{
		SubmissionFormFieldView view = new SubmissionFormFieldView();
		try {
			BeanUtils.copyProperties(view, formField);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		view.setMappingTypeDescription(TranslateUtil.translate(SubmissionFormFieldMappingType.class, view.getMappingType()));
		view.setFieldTypeDescription(TranslateUtil.translate(SubmissionFormFieldType.class, view.getFieldType()));

		return view;
	}

	public static List<SubmissionFormFieldView> toView(List<SubmissionFormField> fields)
	{
		List<SubmissionFormFieldView> views = new ArrayList<>();
		fields.forEach(field -> {
			views.add(toView(field));
		});
		return views;
	}

	public String getMappingTypeDescription()
	{
		return mappingTypeDescription;
	}

	public void setMappingTypeDescription(String mappingTypeDescription)
	{
		this.mappingTypeDescription = mappingTypeDescription;
	}

	public String getFieldTypeDescription()
	{
		return fieldTypeDescription;
	}

	public void setFieldTypeDescription(String fieldTypeDescription)
	{
		this.fieldTypeDescription = fieldTypeDescription;
	}

}
