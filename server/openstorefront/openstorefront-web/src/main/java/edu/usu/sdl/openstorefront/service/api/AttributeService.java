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
import edu.usu.sdl.openstorefront.service.transfermodel.AttributeXrefModel;
import edu.usu.sdl.openstorefront.storage.model.ArticleTracking;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeCodePk;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import edu.usu.sdl.openstorefront.storage.model.AttributeXRefType;
import edu.usu.sdl.openstorefront.web.rest.model.Article;
import edu.usu.sdl.openstorefront.web.rest.model.AttributeXRefView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentSearchView;
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
	 * Saves type and Updates Indexes
	 *
	 * @param attributeType
	 */
	public void saveAttributeType(AttributeType attributeType);

	/**
	 * Saves type
	 *
	 * @param attributeType
	 * @param updateIndexes (For Searching)
	 */
	public void saveAttributeType(AttributeType attributeType, boolean updateIndexes);

	/**
	 * Saves code
	 *
	 * @param attributeCode
	 */
	public void saveAttributeCode(AttributeCode attributeCode);

	/**
	 * Saves code and Updates Indexes
	 *
	 * @param attributeCode
	 * @param updateIndexes (For Searching)
	 */
	public void saveAttributeCode(AttributeCode attributeCode, boolean updateIndexes);

	/**
	 * Grabs the article for a give code or type
	 *
	 * @param attributeCodePk
	 * @return article data or null for no article.
	 */
	public String getArticle(AttributeCodePk attributeCodePk);

	/**
	 * Grabs the article for a give code and type with the content
	 *
	 * @param attributeCodePk
	 * @return the full article
	 */
	public Article getArticleView(AttributeCodePk attributeCodePk);

	/**
	 * Grabs the article for a give code or type. Get the article codes only,
	 * not the article html content
	 *
	 * @param attributeCodePk
	 * @return article data or null for no article.
	 */
	public List<Article> getArticlesForCodeLike(AttributeCodePk attributeCodePk);

	/**
	 * Saves a new article (This will scub the article data prior to save)
	 *
	 * @param attributeCodePk
	 * @param article
	 */
	public void saveArticle(AttributeCodePk attributeCodePk, String article);

	/**
	 * Deletes a article. WARNING: This will remove the link and delete any
	 * internal file.
	 *
	 * @param attributeCodePk
	 */
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
	 * Sync the db with the attribute code Map Note this won't remove types
	 * because of the multiple file imports It will remove/inactivate codes.
	 *
	 * @param attributeMap
	 */
	public void syncAttribute(Map<AttributeType, List<AttributeCode>> attributeMap);

	/**
	 * Find the recently posted (created date) articles (Active Only)
	 *
	 * @param maxResults
	 * @return
	 */
	public List<AttributeCode> findRecentlyAddedArticles(Integer maxResults);

	/**
	 * Find the recently posted (created date) articles
	 *
	 * @param maxResults
	 * @param activeStatus
	 * @return Codes with articles according to parameters
	 */
	public List<AttributeCode> findRecentlyAddedArticles(Integer maxResults, String activeStatus);

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

	/**
	 * Get all articles as search results. Note: This doesn't pull the article
	 * content.
	 *
	 * @return
	 */
	public List<ComponentSearchView> getArticlesSearchView();

	/**
	 * Get all articles as search results with content
	 *
	 * @return
	 */
	public List<Article> getArticles();

	/**
	 * Gets the active xref types for an IntegrationType
	 *
	 * @param attributeXrefModel
	 * @return
	 */
	public List<AttributeXRefType> getAttributeXrefTypes(AttributeXrefModel attributeXrefModel);

	/**
	 * Gets the code mappings
	 *
	 * @return Attribute key, external code, our code
	 */
	public Map<String, Map<String, String>> getAttributeXrefMapFieldMap();

	/**
	 * Save Attribute Mapping
	 *
	 * @param attributeXRefView
	 */
	public void saveAttributeXrefMap(AttributeXRefView attributeXRefView);

}
