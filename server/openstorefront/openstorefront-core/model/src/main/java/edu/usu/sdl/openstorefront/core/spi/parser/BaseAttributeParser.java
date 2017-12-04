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
package edu.usu.sdl.openstorefront.core.spi.parser;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryOption;
import edu.usu.sdl.openstorefront.core.model.AttributeAll;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseAttributeParser
		extends AbstractParser<AttributeAll>
{

	protected static final int MAX_BUCKET_SIZE = 1000;
	protected List<AttributeAll> attributesAll = new ArrayList<>();

	protected AttributeAll defaultAttributeAll()
	{
		AttributeAll attributeAll = new AttributeAll();
		AttributeType attributeType = new AttributeType();

		attributeType.setDescription(OpenStorefrontConstant.NOT_AVAILABLE);
		attributeType.setVisibleFlg(Boolean.FALSE);
		attributeType.setRequiredFlg(Boolean.FALSE);
		attributeType.setArchitectureFlg(Boolean.FALSE);
		attributeType.setImportantFlg(Boolean.FALSE);
		attributeType.setAllowMultipleFlg(Boolean.FALSE);
		attributeType.setHideOnSubmission(Boolean.FALSE);
		attributeType.setAllowUserGeneratedCodes(Boolean.FALSE);

		attributeAll.setAttributeType(attributeType);
		return attributeAll;
	}

	@Override
	protected List<AttributeAll> getStorageBucket()
	{
		return attributesAll;
	}

	@Override
	protected int getMaxBucketSize()
	{
		return MAX_BUCKET_SIZE;
	}

	@Override
	protected void performStorage()
	{
		ValidationResult validationResult;
		if (fileHistoryAll.getFileHistory().getFileHistoryOption() == null) {
			validationResult = service.getAttributeService().importAttributes(attributesAll, new FileHistoryOption());
		} else {
			validationResult = service.getAttributeService().importAttributes(attributesAll, fileHistoryAll.getFileHistory().getFileHistoryOption());
		}
		if (validationResult.valid() == false) {
			throw new OpenStorefrontRuntimeException(validationResult.toString());
		}
	}
}
