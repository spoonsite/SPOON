/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.common.exception;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import org.apache.commons.lang3.StringUtils;

/**
 * This is the base class for runtime exception
 *
 * @author dshurtleff
 */
public class OpenStorefrontRuntimeException
		extends RuntimeException
{

	private static final long serialVersionUID = 1L;

	private String potentialResolution;
	private String errorTypeCode;

	public OpenStorefrontRuntimeException(String message, String potentialResolution, Throwable thrwbl, String errorTypeCode)
	{
		super(message, thrwbl);
		this.potentialResolution = potentialResolution;
		this.errorTypeCode = errorTypeCode;
	}

	public OpenStorefrontRuntimeException(String message, String potentialResolution, String errorTypeCode)
	{
		super(message);
		this.potentialResolution = potentialResolution;
		this.errorTypeCode = errorTypeCode;
	}

	// <editor-fold desc="chained construtors" defaultstate="collapsed">
	public OpenStorefrontRuntimeException()
	{
		this((String) null);
	}

	public OpenStorefrontRuntimeException(String message)
	{
		this(message, (String) null);
	}

	public OpenStorefrontRuntimeException(String message, String potentialResolution)
	{
		this(message, potentialResolution, OpenStorefrontConstant.ERROR_CODE_SYSTEM);
	}

	public OpenStorefrontRuntimeException(String message, Throwable thrwbl)
	{
		this(message, thrwbl, OpenStorefrontConstant.ERROR_CODE_SYSTEM);
	}

	public OpenStorefrontRuntimeException(String message, Throwable thrwbl, String errorTypeCode)
	{
		this(message, null, thrwbl, errorTypeCode);
	}

	public OpenStorefrontRuntimeException(String message, String potentialResolution, Throwable thrwbl)
	{
		this(message, potentialResolution, thrwbl, OpenStorefrontConstant.ERROR_CODE_SYSTEM);
	}

	public OpenStorefrontRuntimeException(Throwable thrwbl)
	{
		this(null, thrwbl);
	}
	//</editor-fold>

	public String getPotentialResolution()
	{
		return potentialResolution;
	}

	public String getErrorTypeCode()
	{
		return errorTypeCode;
	}

	@Override
	public String getLocalizedMessage()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(super.getLocalizedMessage());

		if (StringUtils.isNotBlank(potentialResolution)) {
			sb.append("  Potential Resolution: ").append(potentialResolution);
		}

		if (StringUtils.isNotBlank(errorTypeCode)) {
			sb.append("  Error Type Code: ").append(errorTypeCode);
		}
		return sb.toString();
	}

}
