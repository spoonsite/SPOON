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
package edu.usu.sdl.openstorefront.exception;

import edu.usu.sdl.openstorefront.storage.model.ErrorTypeCode;
import org.apache.commons.lang.StringUtils;

/**
 * This the base class for runtime exception
 *
 * @author dshurtleff
 */
public class OpenStorefrontRuntimeException
		extends RuntimeException
{

	private String potentialResolution;
	private String errorTypeCode = ErrorTypeCode.SYSTEM;

	public OpenStorefrontRuntimeException()
	{
	}

	public OpenStorefrontRuntimeException(String message)
	{
		super(message);
	}

	public OpenStorefrontRuntimeException(String message, String potentialResolution)
	{
		super(message);
		this.potentialResolution = potentialResolution;
	}

	public OpenStorefrontRuntimeException(String message, String potentialResolution, String errorTypeCode)
	{
		super(message);
		this.potentialResolution = potentialResolution;
		this.errorTypeCode = errorTypeCode;
	}

	public OpenStorefrontRuntimeException(String message, Throwable thrwbl)
	{
		super(message, thrwbl);
	}

	public OpenStorefrontRuntimeException(String message, Throwable thrwbl, String errorTypeCode)
	{
		super(message, thrwbl);
		this.errorTypeCode = errorTypeCode;
	}

	public OpenStorefrontRuntimeException(String message, String potentialResolution, Throwable thrwbl)
	{
		super(message, thrwbl);
		this.potentialResolution = potentialResolution;
	}

	public OpenStorefrontRuntimeException(String message, String potentialResolution, Throwable thrwbl, String errorTypeCode)
	{
		super(message, thrwbl);
		this.potentialResolution = potentialResolution;
		this.errorTypeCode = errorTypeCode;
	}

	public OpenStorefrontRuntimeException(Throwable thrwbl)
	{
		super(thrwbl);
	}

	public String getPotentialResolution()
	{
		return potentialResolution;
	}

	public void setPotentialResolution(String potentialResolution)
	{
		this.potentialResolution = potentialResolution;
	}

	public String getErrorTypeCode()
	{
		return errorTypeCode;
	}

	public void setErrorTypeCode(String errorTypeCode)
	{
		this.errorTypeCode = errorTypeCode;
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
