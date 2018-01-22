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
package edu.usu.sdl.openstorefront.validation.exception;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.validation.ValidationResult;

/**
 *
 * @author kbair
 */
public class OpenStorefrontValidationException extends OpenStorefrontRuntimeException
{
	private ValidationResult validationResult;
	
	public OpenStorefrontValidationException(String message, ValidationResult validationResult, String potentialResolution, Throwable thrwbl, String errorTypeCode)
	{
		super(message, potentialResolution, thrwbl, errorTypeCode);
		this.validationResult = validationResult;
	}

	public OpenStorefrontValidationException(String message, ValidationResult validationResult, String potentialResolution, String errorTypeCode)
	{
		super(message, potentialResolution, errorTypeCode);
		this.validationResult = validationResult;
	}
	
	// <editor-fold desc="chained construtors" defaultstate="collapsed">

	public OpenStorefrontValidationException( ValidationResult validationResult)
	{
		this((String) null, validationResult);
	}

	public OpenStorefrontValidationException(String message, ValidationResult validationResult)
	{
		this(message, validationResult, "Fix Validation Error");
	}

	public OpenStorefrontValidationException(String message, ValidationResult validationResult, String potentialResolution)
	{
		this(message, validationResult, potentialResolution, OpenStorefrontConstant.ERROR_CODE_SYSTEM);
	}

	public OpenStorefrontValidationException(String message, ValidationResult validationResult, Throwable thrwbl)
	{
		this(message, validationResult, thrwbl, OpenStorefrontConstant.ERROR_CODE_SYSTEM);
	}

	public OpenStorefrontValidationException(String message, ValidationResult validationResult, Throwable thrwbl, String errorTypeCode)
	{
		this(message, validationResult, "Fix Validation Error", thrwbl, errorTypeCode);
	}

	public OpenStorefrontValidationException(String message, ValidationResult validationResult, String potentialResolution, Throwable thrwbl)
	{
		this(message, validationResult, potentialResolution, thrwbl, OpenStorefrontConstant.ERROR_CODE_SYSTEM);
	}

	public OpenStorefrontValidationException(ValidationResult validationResult, Throwable thrwbl)
	{
		this(null, validationResult, thrwbl);
	}
	//</editor-fold>

	// <editor-fold desc="overloaded super construtors" defaultstate="collapsed">
	
	public OpenStorefrontValidationException(String message, String potentialResolution, Throwable thrwbl, String errorTypeCode)
	{
		this(message, (ValidationResult)null, potentialResolution, thrwbl, errorTypeCode);
	}

	public OpenStorefrontValidationException(String message, String potentialResolution, String errorTypeCode)
	{
		this(message, (ValidationResult)null, potentialResolution, errorTypeCode);
	}
	
	public OpenStorefrontValidationException()
	{
		this((String) null);
	}

	public OpenStorefrontValidationException(String message)
	{
		this(message, "Fix Validation Error");
	}

	public OpenStorefrontValidationException(String message, String potentialResolution)
	{
		this(message, potentialResolution, OpenStorefrontConstant.ERROR_CODE_SYSTEM);
	}

	public OpenStorefrontValidationException(String message, Throwable thrwbl)
	{
		this(message, thrwbl, OpenStorefrontConstant.ERROR_CODE_SYSTEM);
	}

	public OpenStorefrontValidationException(String message, Throwable thrwbl, String errorTypeCode)
	{
		this(message, "Fix Validation Error", thrwbl, errorTypeCode);
	}

	public OpenStorefrontValidationException(String message, String potentialResolution, Throwable thrwbl)
	{
		this(message, potentialResolution, thrwbl, OpenStorefrontConstant.ERROR_CODE_SYSTEM);
	}

	public OpenStorefrontValidationException(Throwable thrwbl)
	{
		this((String)null, thrwbl);
	}
	//</editor-fold>
	
	public ValidationResult getValidationResult()
	{
		return validationResult;
	}
	
	@Override
	public String getLocalizedMessage()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(super.getLocalizedMessage());

		if (validationResult != null) {
			sb.append("  Validation Error: ").append(validationResult.toString());
		}
		return sb.toString();
	}
}
