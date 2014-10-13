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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.api.AttributeService;
import edu.usu.sdl.openstorefront.service.api.AttributeServicePrivate;
import edu.usu.sdl.openstorefront.service.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.service.transfermodel.Architecture;
import edu.usu.sdl.openstorefront.sort.ArchitectureComparator;
import edu.usu.sdl.openstorefront.storage.model.ArticleTracking;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeCodePk;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import edu.usu.sdl.openstorefront.storage.model.LookupEntity;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import edu.usu.sdl.openstorefront.util.ServiceUtil;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import edu.usu.sdl.openstorefront.web.rest.model.Article;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentSearchView;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;

/**
 * Handles Attribute information
 *
 * @author dshurtleff
 */
public class AttributeServiceImpl
		extends ServiceProxy
		implements AttributeService, AttributeServicePrivate
{

	private static final Logger log = Logger.getLogger(AttributeServiceImpl.class.getName());

	@Override
	public List<AttributeType> getRequiredAttributes()
	{
		AttributeType example = new AttributeType();
		example.setActiveStatus(AttributeType.ACTIVE_STATUS);
		example.setRequiredFlg(Boolean.TRUE);
		List<AttributeType> required = persistenceService.queryByExample(AttributeType.class, new QueryByExample(example));
		return required;
	}

	@Override
	public List<AttributeCode> findCodesForType(String type)
	{
		Element element = OSFCacheManager.getAttributeCache().get(type);
		if (element != null) {
			return (List<AttributeCode>) element.getObjectValue();
		} else {

			AttributeCode attributeCodeExample = new AttributeCode();
			AttributeCodePk attributeCodePk = new AttributeCodePk();
			attributeCodePk.setAttributeType(type);
			attributeCodeExample.setAttributeCodePk(attributeCodePk);
			attributeCodeExample.setActiveStatus(AttributeCode.ACTIVE_STATUS);
			List<AttributeCode> attributeCodes = persistenceService.queryByExample(AttributeCode.class, new QueryByExample(attributeCodeExample));
			element = new Element(type, attributeCodes);
			OSFCacheManager.getAttributeCache().put(element);
			return attributeCodes;
		}
	}

	@Override
	public void saveAttributeType(AttributeType attributeType)
	{
		getAttributeServicePrivate().saveAttributeType(attributeType, true);
		//TODO: figure out where to update SOlr from this change.
	}

	@Override
	public void saveAttributeType(AttributeType attributeType, boolean test)
	{
		AttributeType existing = persistenceService.findById(AttributeType.class, attributeType.getAttributeType());
		if (existing != null) {
			//remove to inactivate
			existing.setActiveStatus(AttributeType.ACTIVE_STATUS);
			existing.setUpdateDts(TimeUtil.currentDate());
			existing.setUpdateUser(attributeType.getUpdateUser());
			existing.setAllowMutlipleFlg(attributeType.getAllowMutlipleFlg());
			existing.setArchitectureFlg(attributeType.getArchitectureFlg());
			existing.setDescription(attributeType.getDescription());
			existing.setImportantFlg(attributeType.getImportantFlg());
			existing.setRequiredFlg(attributeType.getRequiredFlg());
			existing.setVisibleFlg(attributeType.getVisibleFlg());
			persistenceService.persist(existing);
		} else {
			attributeType.setActiveStatus(AttributeType.ACTIVE_STATUS);
			attributeType.setUpdateDts(TimeUtil.currentDate());
			attributeType.setCreateDts(TimeUtil.currentDate());
			persistenceService.persist(attributeType);
		}
	}

	@Override
	public void saveAttributeCode(AttributeCode attributeCode)
	{
		getAttributeServicePrivate().saveAttributeCode(attributeCode, true);
		//TODO: figure out where to add changes to SOLR from this change.
	}

	@Override
	public void saveAttributeCode(AttributeCode attributeCode, boolean test)
	{
		AttributeCode existing = persistenceService.findById(AttributeCode.class, attributeCode.getAttributeCodePk());
		if (existing != null) {
			//remove to inactivate
			existing.setActiveStatus(AttributeCode.ACTIVE_STATUS);
			existing.setUpdateDts(TimeUtil.currentDate());
			existing.setUpdateUser(attributeCode.getUpdateUser());
			existing.setArticleFilename(attributeCode.getArticleFilename());
			existing.setDescription(attributeCode.getDescription());
			existing.setDetailUrl(attributeCode.getDetailUrl());
			existing.setLabel(attributeCode.getLabel());
			persistenceService.persist(existing);
		} else {
			attributeCode.setActiveStatus(AttributeCode.ACTIVE_STATUS);
			attributeCode.setUpdateDts(TimeUtil.currentDate());
			attributeCode.setCreateDts(TimeUtil.currentDate());
			persistenceService.persist(attributeCode);
		}
	}

	@Override
	public String getArticle(AttributeCodePk attributeCodePk)
	{
		String article = null;

		Objects.requireNonNull(attributeCodePk, "AttributeCodePk is required.");
		Objects.requireNonNull(attributeCodePk.getAttributeType(), "Type is required.");
		Objects.requireNonNull(attributeCodePk.getAttributeCode(), "Code is required.");

		AttributeCode attributeCode = persistenceService.findById(AttributeCode.class, attributeCodePk);
		if (attributeCode != null) {
			if (StringUtils.isNotBlank(attributeCode.getArticleFilename())) {
				File articleDir = FileSystemManager.getDir(FileSystemManager.ARTICLE_DIR);
				try {
					byte data[] = Files.readAllBytes(Paths.get(articleDir.getPath() + "/" + attributeCode.getArticleFilename()));
					article = new String(data);
				} catch (IOException e) {
					throw new OpenStorefrontRuntimeException("Unable to find article for type: " + attributeCodePk.getAttributeType() + " code: " + attributeCodePk.getAttributeCode(), "Contact system admin to confirm file exists and is not corrupt.", e);
				}
			}
		}
		return article;
	}

	@Override
	public void saveArticle(AttributeCodePk attributeCodePk, String article)
	{
		getAttributeServicePrivate().saveArticle(attributeCodePk, article, true);
		AttributeCode code = new AttributeCode();
		code.setAttributeCodePk(attributeCodePk);
		getSearchService().addArticle(Article.toView(code));
	}

	@Override
	public void saveArticle(AttributeCodePk attributeCodePk, String article, boolean test)
	{
		Objects.requireNonNull(attributeCodePk, "AttributeCodePk is required.");
		Objects.requireNonNull(attributeCodePk.getAttributeType(), "Type is required.");
		Objects.requireNonNull(attributeCodePk.getAttributeCode(), "Code is required.");
		Objects.requireNonNull(article, "Article is required.");

		AttributeCode attributeCode = persistenceService.findById(AttributeCode.class, attributeCodePk);
		if (attributeCode != null) {
			HTMLSanitizer sanitizer = new HTMLSanitizer();
			article = sanitizer.santize(article).toString();

			//save the article
			String filename = attributeCodePk.toKey() + ".htm";
			File articleDir = FileSystemManager.getDir(FileSystemManager.ARTICLE_DIR);
			try {
				Files.write(Paths.get(articleDir.getPath() + "/" + filename), article.getBytes());

				//save attribute
				attributeCode.setArticleFilename(filename);
				attributeCode.setUpdateDts(TimeUtil.currentDate());
				attributeCode.setUpdateUser(SecurityUtil.getCurrentUserName());
				persistenceService.persist(attributeCode);
			} catch (IOException e) {
				throw new OpenStorefrontRuntimeException("Unable to save article.", "Contact system admin.  Check permissions on the directory and make sure device has enough space.");
			}
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find attribute for type: " + attributeCodePk.getAttributeType() + " code: " + attributeCodePk.getAttributeCode(), "Add attribute first before posting article");
		}
	}

	@Override
	public void deleteArticle(AttributeCodePk attributeCodePk)
	{
		AttributeCode attributeCode = persistenceService.findById(AttributeCode.class, attributeCodePk);
		if (attributeCode != null) {
			attributeCode.setDetailUrl(null);
			if (attributeCode.getArticleFilename() != null) {
				File articleDir = FileSystemManager.getDir(FileSystemManager.ARTICLE_DIR);
				File ariticleFile = new File(articleDir.getPath() + "/" + attributeCode.getArticleFilename());
				if (ariticleFile.exists()) {
					ariticleFile.delete();
				}
				attributeCode.setArticleFilename(null);
			}
			attributeCode.setUpdateUser(SecurityUtil.getCurrentUserName());
			saveAttributeCode(attributeCode);
		}
	}

	@Override
	public void removeAttributeType(String type)
	{
		Objects.requireNonNull(type, "Type is required.");

		AttributeType attributeType = persistenceService.findById(AttributeType.class, type);
		if (attributeType != null) {
			attributeType.setActiveStatus(AttributeCode.INACTIVE_STATUS);
			attributeType.setUpdateDts(TimeUtil.currentDate());
			attributeType.setUpdateUser(SecurityUtil.getCurrentUserName());
			persistenceService.persist(attributeType);

			OSFCacheManager.getAttributeTypeCache().remove(type);
			OSFCacheManager.getAttributeCache().remove(type);
		}
	}

	@Override
	public void removeAttributeCode(AttributeCodePk attributeCodePk)
	{
		Objects.requireNonNull(attributeCodePk, "AttributeCodePk is required.");

		AttributeCode attributeCode = persistenceService.findById(AttributeCode.class, attributeCodePk);
		if (attributeCode != null) {
			attributeCode.setActiveStatus(AttributeCode.INACTIVE_STATUS);
			attributeCode.setUpdateDts(TimeUtil.currentDate());
			attributeCode.setUpdateUser(SecurityUtil.getCurrentUserName());
			persistenceService.persist(attributeCode);

			OSFCacheManager.getAttributeCache().remove(attributeCodePk.getAttributeType());
		}
	}

	@Override
	public void syncAttribute(Map<AttributeType, List<AttributeCode>> attributeMap)
	{
		AttributeType attributeTypeExample = new AttributeType();
		List<AttributeType> attributeTypes = persistenceService.queryByExample(AttributeType.class, new QueryByExample(attributeTypeExample));
		Map<String, AttributeType> existingAttributeMap = new HashMap<>();
		attributeTypes.stream().forEach((attributeType) -> {
			existingAttributeMap.put(attributeType.getAttributeType(), attributeType);
		});

		attributeMap.keySet().stream().forEach(attributeType -> {

			try {
				ValidationModel validationModel = new ValidationModel(attributeType);
				validationModel.setConsumeFieldsOnly(true);
				ValidationResult validationResult = ValidationUtil.validate(validationModel);
				if (validationResult.valid()) {
					attributeType.setAttributeType(attributeType.getAttributeType().replace(ServiceUtil.COMPOSITE_KEY_SEPERATOR, ServiceUtil.COMPOSITE_KEY_REPLACER));

					AttributeType existing = existingAttributeMap.get(attributeType.getAttributeType());
					if (existing != null) {
						existing.setDescription(attributeType.getDescription());
						existing.setAllowMutlipleFlg(attributeType.getAllowMutlipleFlg());
						existing.setArchitectureFlg(attributeType.getArchitectureFlg());
						existing.setImportantFlg(attributeType.getImportantFlg());
						existing.setRequiredFlg(attributeType.getRequiredFlg());
						existing.setVisibleFlg(attributeType.getVisibleFlg());
						existing.setActiveStatus(AttributeType.ACTIVE_STATUS);
						existing.setCreateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
						existing.setUpdateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
						saveAttributeType(existing);
					} else {
						attributeType.setActiveStatus(AttributeType.ACTIVE_STATUS);
						attributeType.setCreateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
						attributeType.setUpdateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
						saveAttributeType(attributeType);
					}

					List<AttributeCode> existingAttributeCodes = findCodesForType(attributeType.getAttributeType());
					Map<String, AttributeCode> existingCodeMap = new HashMap<>();
					for (AttributeCode attributeCode : existingAttributeCodes) {
						existingCodeMap.put(attributeCode.getAttributeCodePk().toKey(), attributeCode);
					}

					Set<String> newCodeSet = new HashSet<>();
					List<AttributeCode> attributeCodes = attributeMap.get(attributeType);
					for (AttributeCode attributeCode : attributeCodes) {
						try {
							ValidationModel validationModelCode = new ValidationModel(attributeCode);
							validationModelCode.setConsumeFieldsOnly(true);
							ValidationResult validationResultCode = ValidationUtil.validate(validationModelCode);
							if (validationResultCode.valid()) {
								attributeCode.getAttributeCodePk().setAttributeCode(attributeCode.getAttributeCodePk().getAttributeCode().replace(ServiceUtil.COMPOSITE_KEY_SEPERATOR, ServiceUtil.COMPOSITE_KEY_REPLACER));

								AttributeCode existingCode = existingCodeMap.get(attributeCode.getAttributeCodePk().toKey());
								if (existingCode != null) {
									if (ServiceUtil.isObjectsDifferent(existingCode, attributeCode, true)) {
										existingCode.setDescription(attributeCode.getDescription());
										existingCode.setDetailUrl(attributeCode.getDetailUrl());
										existingCode.setLabel(attributeCode.getLabel());
										existingCode.setActiveStatus(AttributeCode.ACTIVE_STATUS);
										existingCode.setCreateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
										existingCode.setUpdateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
										saveAttributeCode(existingCode);
									}
								} else {
									attributeCode.setActiveStatus(AttributeCode.ACTIVE_STATUS);
									attributeCode.setCreateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
									attributeCode.setUpdateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
									saveAttributeCode(attributeCode);
								}
								newCodeSet.add(attributeCode.getAttributeCodePk().toKey());
							} else {
								log.log(Level.WARNING, MessageFormat.format("(Data Sync) Unable to Add  Attribute Code:  {0} Validation Issues:\n{1}", new Object[]{attributeCode.getAttributeCodePk().toKey(), validationResult.toString()}));
							}
						} catch (Exception e) {
							log.log(Level.SEVERE, "Unable to save attribute code: " + attributeCode.getAttributeCodePk().toKey(), e);
						}
					}
					//inactive missing codes
					existingAttributeCodes.stream().forEach((attributeCode) -> {
						if (newCodeSet.contains(attributeCode.getAttributeCodePk().toKey()) == false) {
							attributeCode.setActiveStatus(LookupEntity.INACTIVE_STATUS);
							attributeCode.setUpdateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
							removeAttributeCode(attributeCode.getAttributeCodePk());
						}
					});
				} else {
					log.log(Level.WARNING, MessageFormat.format("(Data Sync) Unable to Add Type:  {0} Validation Issues:\n{1}", new Object[]{attributeType.getAttributeType(), validationResult.toString()}));
				}
			} catch (Exception e) {
				log.log(Level.SEVERE, "Unable to save attribute type:" + attributeType.getAttributeType(), e);
			}
		});

		//TODO: Figure out where to update Solr from this change.
	}

	@Override
	public AttributeCode findCodeForType(AttributeCodePk pk)
	{
		AttributeCode attributeCode = null;
		List<AttributeCode> attributeCodes = findCodesForType(pk.getAttributeType());
		for (AttributeCode attributeCodeCheck : attributeCodes) {
			if (attributeCodeCheck.getAttributeCodePk().getAttributeCode().equals(pk.getAttributeCode())) {
				attributeCode = attributeCodeCheck;
				break;
			}
		}
		return attributeCode;
	}

	@Override
	public AttributeType findType(String type)
	{
		AttributeType attributeType = null;

		Element element = OSFCacheManager.getAttributeTypeCache().get(type);
		if (element != null) {
			attributeType = (AttributeType) element.getObjectValue();
		} else {
			AttributeType attributeTypeExample = new AttributeType();
			attributeTypeExample.setActiveStatus(AttributeType.ACTIVE_STATUS);
			List<AttributeType> attributeTypes = persistenceService.queryByExample(AttributeType.class, new QueryByExample(attributeTypeExample));
			for (AttributeType attributeTypeCheck : attributeTypes) {
				if (attributeTypeCheck.getAttributeType().equals(type)) {
					attributeType = attributeTypeCheck;
				}
				element = new Element(attributeTypeCheck.getAttributeType(), attributeTypeCheck);
				OSFCacheManager.getAttributeTypeCache().put(element);
			}
		}

		return attributeType;
	}

	@Override
	public List<AttributeCode> findRecentlyAddedArticles(Integer maxResults)
	{
		return findRecentlyAddedArticles(maxResults, AttributeCode.ACTIVE_STATUS);
	}

	@Override
	public List<AttributeCode> findRecentlyAddedArticles(Integer maxResults, String activeStatus)
	{
		String query;
		if (maxResults != null) {
			query = "select from AttributeCode where activeStatus = :activeStatusParam "
					+ " and articleFilename is not null "
					+ " order by updateDts DESC LIMIT " + maxResults;
		} else {
			query = "select from AttributeCode where activeStatus = :activeStatusParam "
					+ " and articleFilename is not null "
					+ " order by updateDts DESC";
		}

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("activeStatusParam", activeStatus);

		return persistenceService.query(query, parameters);
	}

	@Override
	public Architecture generateArchitecture(String attributeType)
	{
		Architecture architecture = new Architecture();

		AttributeType attributeTypeFull = persistenceService.findById(AttributeType.class, attributeType);
		if (attributeTypeFull != null) {
			if (attributeTypeFull.getArchitectureFlg()) {
				architecture.setName(attributeTypeFull.getDescription());
				architecture.setAttributeType(attributeType);

				String rootCode = "0";
				List<AttributeCode> attributeCodes = findCodesForType(attributeType);
				for (AttributeCode attributeCode : attributeCodes) {
					if (rootCode.equals(attributeCode.getAttributeCodePk().getAttributeCode())) {
						architecture.setAttributeCode(attributeCode.getAttributeCodePk().getAttributeCode());
						architecture.setDescription(attributeCode.getDescription());
					} else {
						String codeTokens[] = attributeCode.getAttributeCodePk().getAttributeCode().split(Pattern.quote("."));
						Architecture rootArchtecture = architecture;
						StringBuilder codeKey = new StringBuilder();
						for (int i = 0; i < codeTokens.length - 1; i++) {
							codeKey.append(codeTokens[i]);

							//put in stubs as needed
							boolean found = false;
							for (Architecture child : rootArchtecture.getChildren()) {
								if (child.getAttributeCode().equals(codeKey.toString())) {
									found = true;
									rootArchtecture = child;
									break;
								}
							}
							if (!found) {
								Architecture newChild = new Architecture();
								newChild.setAttributeCode(codeKey.toString());
								newChild.setAttributeType(attributeType);
								rootArchtecture.getChildren().add(newChild);
								rootArchtecture = newChild;
							}
							codeKey.append(".");
						}
						//now find the correct postion and add/update
						boolean found = false;
						for (Architecture child : rootArchtecture.getChildren()) {
							if (child.getAttributeCode().equals(attributeCode.getAttributeCodePk().getAttributeCode())) {
								child.setName(attributeCode.getLabel());
								child.setDescription(attributeCode.getDescription());
								found = true;
							}
						}
						if (!found) {
							Architecture newChild = new Architecture();
							newChild.setAttributeCode(attributeCode.getAttributeCodePk().getAttributeCode());
							newChild.setAttributeType(attributeType);
							newChild.setName(attributeCode.getLabel());
							newChild.setDescription(attributeCode.getDescription());
							rootArchtecture.getChildren().add(newChild);
						}
					}
				}

			} else {
				throw new OpenStorefrontRuntimeException("Attribute Type is not an architecture: " + attributeType, "Make sure type is an architecture.");
			}
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find attribute type: " + attributeType, "Check type code.");
		}
		sortArchitecture(architecture.getChildren());
		return architecture;
	}

	private void sortArchitecture(List<Architecture> architectures)
	{
		if (architectures.isEmpty()) {
			return;
		}

		for (Architecture architecture : architectures) {
			sortArchitecture(architecture.getChildren());
		}
		architectures.sort(new ArchitectureComparator<>());
	}

	@Override
	public void addArticleTrackEvent(ArticleTracking articleTracking)
	{
		articleTracking.setArticleTrackingId(persistenceService.generateId());
		articleTracking.setActiveStatus(ArticleTracking.ACTIVE_STATUS);
		articleTracking.setCreateDts(TimeUtil.currentDate());
		articleTracking.setCreateUser(SecurityUtil.getCurrentUserName());
		articleTracking.setUpdateDts(TimeUtil.currentDate());
		articleTracking.setUpdateUser(SecurityUtil.getCurrentUserName());
		persistenceService.persist(articleTracking);
	}

	@Override
	public List<ComponentSearchView> getAllArticles()
	{
		List<ComponentSearchView> list = new ArrayList<>();
		List<AttributeCode> codes = this.getAttributeService().findRecentlyAddedArticles(null);
		codes.stream().forEach((code) -> {
			list.add(ComponentSearchView.toView(Article.toView(code)));
		});
		return list;
	}

	@Override
	public List<Article> getArticleLike(AttributeCodePk attributeCodePk)
	{
		List<Article> articles = new ArrayList<>();
		Objects.requireNonNull(attributeCodePk, "AttributeCodePk is required.");
		Objects.requireNonNull(attributeCodePk.getAttributeType(), "Type is required.");
		Objects.requireNonNull(attributeCodePk.getAttributeCode(), "Code is required.");

		//String query = "SELECT * FROM AttributeCodePk WHERE attributeType = :type AND attributeCode = :code ";
		// SELECT * FROM AttributeCodePk WHERE attributeType = 'DI2E-SVCV4-A' AND attributeCode = '1.2.1'
		String query = "SELECT * FROM AttributeCodePk WHERE attributeType = :type AND attributeCode LIKE ':code%'";
		Map<String, Object> params = new HashMap<>();
		params.put("type", attributeCodePk.getAttributeType());
		params.put("code", attributeCodePk.getAttributeCode());

//		AttributeCode attributeCodeExample = new AttributeCode();
//		attributeCodeExample.setAttributeCodePk(attributeCodePk);
//		List<AttributeCode> attributeCodes = persistenceService.queryByExample(AttributeCode.class, attributeCodeExample);
		List<AttributeCode> attributeCodes = persistenceService.query(query, params);

		//List<AttributeCodePk> pks = persistenceService.query(query, params);
		for (AttributeCode code : attributeCodes) {
			articles.add(Article.toView(code));
		}
		//return new ArrayList<Article>();
		return articles;
	}
}
