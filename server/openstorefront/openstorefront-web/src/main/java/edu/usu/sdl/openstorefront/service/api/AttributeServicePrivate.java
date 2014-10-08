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

import edu.usu.sdl.openstorefront.service.ServiceInterceptor;
import edu.usu.sdl.openstorefront.service.TransactionInterceptor;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeCodePk;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;

/**
 *
 * @author jlaw
 */
public interface AttributeServicePrivate
{
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveArticle(AttributeCodePk attributeCodePk, String article, boolean test);
	
	
	/**
	 * Saves type
	 *
	 * @param attributeType
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveAttributeType(AttributeType attributeType, boolean test);

	/**
	 * Saves code
	 *
	 * @param attributeCode
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveAttributeCode(AttributeCode attributeCode, boolean test);
	
}
