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
package edu.usu.sdl.openstorefront.core.spi.parser.mapper;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.AttributeValueType;
import edu.usu.sdl.openstorefront.core.entity.ComponentTypeRestriction;
import edu.usu.sdl.openstorefront.core.entity.FileDataMapField;
import edu.usu.sdl.openstorefront.core.model.DataMapModel;
import edu.usu.sdl.openstorefront.core.model.FileHistoryAll;
import edu.usu.sdl.openstorefront.validation.CleanKeySanitizer;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 * @param <T>
 */
public abstract class BaseMapper<T>
{

	protected DataTemplateEntity<T> templateFactory;
	protected FileHistoryAll fileHistoryAll;
	protected Map<String, DataMapper> dataMappers;
	protected AttributeDataMapper attributeDataMapper;

	protected Service serviceProxy = ServiceProxyFactory.getServiceProxy();

	public BaseMapper(DataTemplateEntity<T> templateFactory, FileHistoryAll fileHistoryAll, Map<String, DataMapper> dataMappers, AttributeDataMapper attributeDataMapper)
	{
		this.templateFactory = templateFactory;
		this.fileHistoryAll = fileHistoryAll;
		this.dataMappers = dataMappers;
		this.attributeDataMapper = attributeDataMapper;
	}

	public BaseMapper(DataTemplateEntity<T> templateFactory, FileHistoryAll fileHistoryAll)
	{
		Objects.requireNonNull(fileHistoryAll.getDataMapModel(), "A data map is required for this format.");

		this.templateFactory = templateFactory;
		this.fileHistoryAll = fileHistoryAll;

		DataMapModel dataMapModel = fileHistoryAll.getDataMapModel();

		if (dataMapModel.getFileDataMap().getDataMapFields() == null) {
			dataMapModel.getFileDataMap().setDataMapFields(new ArrayList<>());
		}

		this.dataMappers = new HashMap<>();
		for (FileDataMapField fileDataMapField : dataMapModel.getFileDataMap().getDataMapFields()) {
			DataMapper dataMapper = DataMapper.toDataMapper(fileDataMapField);
			dataMappers.put(fileDataMapField.getField(), dataMapper);
		}

		if (dataMapModel.getFileAttributeMap() != null) {
			attributeDataMapper = AttributeDataMapper.toDataMapper(dataMapModel.getFileAttributeMap());
		}

	}

	public abstract List<T> multiMapData(MapModel input);

	public abstract T singleMapData(MapModel input);
	
	protected AttributeType createAttributeType(String attributeTypeCode, AttributeContext attributeContext)
	{
		AttributeType attributeType = new AttributeType();

		CleanKeySanitizer sanitizer = new CleanKeySanitizer();
		String key = sanitizer.santize(attributeTypeCode.toUpperCase()).toString();

		attributeType.setAttributeType(StringUtils.left(key, OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT));
		attributeType.setDescription(StringUtils.left(attributeTypeCode, OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT));
		attributeType.setVisibleFlg(Boolean.FALSE);
		attributeType.setRequiredFlg(Boolean.FALSE);
		attributeType.setArchitectureFlg(Boolean.FALSE);
		attributeType.setImportantFlg(Boolean.FALSE);
		attributeType.setAllowMultipleFlg(Boolean.TRUE);
		attributeType.setHideOnSubmission(Boolean.FALSE);
		attributeType.setAllowUserGeneratedCodes(Boolean.FALSE);
		attributeType.setCreateUser(fileHistoryAll.getFileHistory().getCreateUser());
		attributeType.setUpdateUser(fileHistoryAll.getFileHistory().getCreateUser());
		
		if(attributeContext != null) {
			attributeType.setDetailedDescription(attributeContext.getAttributeDescription());
			attributeType.setAttributeValueType(attributeContext.getAttributeValueType());
			if(StringUtils.isNotBlank(attributeContext.getComponentType())) {
				List<ComponentTypeRestriction> optionalAttributes = new ArrayList<>();
				ComponentTypeRestriction componentTypeRestriction = new ComponentTypeRestriction();
				componentTypeRestriction.setComponentType(attributeContext.getComponentType());
				optionalAttributes.add(componentTypeRestriction);
				attributeType.setOptionalRestrictions(optionalAttributes);
			}
		} else {
			attributeType.setAttributeType(AttributeValueType.TEXT);
		}

		serviceProxy.getAttributeService().saveAttributeType(attributeType);
		return attributeType;
	}

	protected AttributeCode createAttributeCode(String attributeTypeCode, String attributeCode)
	{
		AttributeCodePk attributeCodePk = new AttributeCodePk();
		attributeCodePk.setAttributeType(attributeTypeCode);
		attributeCodePk.setAttributeCode(attributeCode);

		CleanKeySanitizer sanitizer = new CleanKeySanitizer();
		String key = sanitizer.santize(attributeCode).toString();
		attributeCodePk.setAttributeCode(StringUtils.left(key, OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT));

		AttributeCode attributeCodeFound = new AttributeCode();
		attributeCodeFound.setAttributeCodePk(attributeCodePk);
		attributeCodeFound.setLabel(StringUtils.left(attributeCode, OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT));
		attributeCodeFound.setCreateUser(fileHistoryAll.getFileHistory().getCreateUser());
		attributeCodeFound.setUpdateUser(fileHistoryAll.getFileHistory().getCreateUser());

		ValidationResult validationResult = serviceProxy.getAttributeService().saveAttributeCode(attributeCodeFound, false);
		if (validationResult.valid() == false) {
			throw new OpenStorefrontRuntimeException(validationResult.toString());
		}
		return attributeCodeFound;
	}

}
