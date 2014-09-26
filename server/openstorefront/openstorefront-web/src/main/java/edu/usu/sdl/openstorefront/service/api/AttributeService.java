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
import edu.usu.sdl.openstorefront.service.transfermodel.Architecture;
import edu.usu.sdl.openstorefront.storage.model.ArticleTracking;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeCodePk;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttribute;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
public interface AttributeService
{

	/**
	 * Finds all Required Attributes
	 *
	 * @return
	 */
	public List<AttributeType> getRequiredAttributes();

	/**
	 * Gets the active code for a type
	 *
	 * @param type
	 * @return if type doesn't exist it return empty list
	 */
	public List<AttributeCode> findCodesForType(String type);

	/**
	 * This will lookup code in an efficient matter
	 *
	 * @param pk
	 * @return code or null if not found
	 */
	public AttributeCode findCodeForType(AttributeCodePk pk);

	/**
	 * This will lookup type in an efficient matter
	 *
	 * @param type
	 * @return
	 */
	public AttributeType findType(String type);

	/**
	 * Saves type
	 *
	 * @param attributeType
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveAttributeType(AttributeType attributeType);

	/**
	 * Saves code
	 *
	 * @param attributeCode
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveAttributeCode(AttributeCode attributeCode);

	/**
	 *
	 * @param componentId
	 * @return
	 */
	public List<ComponentAttribute> getAttributesByComponentId(String componentId);

	/**
	 * Grabs the article for a give code or type
	 *
	 * @param attributeCodePk
	 * @return article data or null for no article.
	 */
	public String getArticle(AttributeCodePk attributeCodePk);

	/**
	 * Saves a new article (This will scub the article data prior to save)
	 *
	 * @param attributeCodePk
	 * @param article
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveArticle(AttributeCodePk attributeCodePk, String article);

	/**
	 * Deletes a article. WARNING: This will remove the link and delete any
	 * internal file.
	 *
	 * @param attributeCodePk
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void deleteArticle(AttributeCodePk attributeCodePk);

	/**
	 * Remove Type
	 *
	 * @param type
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void removeAttributeType(String type);

	/**
	 * Remove Code
	 *
	 * @param attributeCodePk
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void removeAttributeCode(AttributeCodePk attributeCodePk);

	/**
	 * Sync the db with the attribute code Map
	 *
	 * @param attributeMap
	 */
	public void syncAttribute(Map<AttributeType, List<AttributeCode>> attributeMap);

	/**
	 * Refreshes the cache from the DB
	 */
	public void refreshCache();

	/**
	 * Find the recently posted (created date) articles
	 *
	 * @param maxResults
	 * @return
	 */
	public List<AttributeCode> findRecentlyAddedArticles(int maxResults);

	/**
	 * Builds and Architecture given a attribute type NOTE: AttributeType must
	 * an architecture with codes in the following format: 1.1.1
	 *
	 * @param attributeType
	 * @return
	 */
	public Architecture generateArchitecture(String attributeType);

	/**
	 * Saves a new article Tracking event
	 *
	 * @param articleTracking
	 */
	public void addArticleTrackEvent(ArticleTracking articleTracking);

}
