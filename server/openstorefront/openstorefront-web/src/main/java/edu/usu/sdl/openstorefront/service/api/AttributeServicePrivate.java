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
import edu.usu.sdl.openstorefront.web.rest.model.ArticleView;

/**
 * Used to jump into a transaction when needed.
 *
 * @author jlaw
 */
public interface AttributeServicePrivate
{

	@ServiceInterceptor(TransactionInterceptor.class)
	public void performSaveArticle(AttributeCode attributeCode, String articleContents);

	@ServiceInterceptor(TransactionInterceptor.class)
	public void performSaveArticle(ArticleView article);

	@ServiceInterceptor(TransactionInterceptor.class)
	public void performSaveAttributeType(AttributeType attributeType);

	@ServiceInterceptor(TransactionInterceptor.class)
	public void performSaveAttributeCode(AttributeCode attributeCode);

	@ServiceInterceptor(TransactionInterceptor.class)
	public void performDeleteArticle(AttributeCodePk attributeCodePk);

}
