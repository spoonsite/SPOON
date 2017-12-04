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
package edu.usu.sdl.openstorefront.service.api;

import edu.usu.sdl.openstorefront.core.api.ServiceInterceptor;
import edu.usu.sdl.openstorefront.core.api.TransactionInterceptor;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.validation.ValidationResult;

/**
 * Used to jump into a transactions when needed.
 *
 * @author jlaw
 */
public interface AttributeServicePrivate
{

	@ServiceInterceptor(TransactionInterceptor.class)
	public void performSaveAttributeType(AttributeType attributeType);

	@ServiceInterceptor(TransactionInterceptor.class)
	public ValidationResult performSaveAttributeCode(AttributeCode attributeCode);

	/**
	 * This will pre-populate caches in a efficient matter if not already warm
	 */
	public void warmAttributeCaches();

}
