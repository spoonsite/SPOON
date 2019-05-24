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
package edu.usu.sdl.openstorefront.service.search;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOptionBuilder;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewCon;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewConPk;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewPro;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewProPk;
import edu.usu.sdl.openstorefront.core.model.search.SearchElement;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation.SearchType;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ReviewProConSearchHandler
		extends BaseSearchHandler
{

	public ReviewProConSearchHandler(SearchElement searchElement)
	{
		super(searchElement);
	}

	@Override
	protected ValidationResult internalValidate()
	{
		ValidationResult validationResult = new ValidationResult();

		if (StringUtils.isBlank(searchElement.getValue())) {
			validationResult.getRuleResults().add(getRuleResult("value", "Required"));
		}

		return validationResult;

	}

	@Override
	public List<String> processSearch()
	{

		if (SearchType.REVIEWPRO.equals(searchElement.getSearchType())) {
			try {
				ComponentReviewPro componentReviewPro = new ComponentReviewPro();
				ComponentReviewProPk componentReviewProPk = new ComponentReviewProPk();
				componentReviewPro.setComponentReviewProPk(componentReviewProPk);
				componentReviewPro.setActiveStatus(ComponentReviewPro.ACTIVE_STATUS);

				QueryByExample<ComponentReviewPro> queryByExample = new QueryByExample<>(componentReviewPro);

				String likeValue = null;
				switch (searchElement.getStringOperation()) {
					case EQUALS:
						String value = searchElement.getValue();
						if (searchElement.getCaseInsensitive()) {
							queryByExample.getFieldOptions().put(ComponentReviewProPk.FIELD_REVIEW_PRO,
									new GenerateStatementOptionBuilder().setMethod(GenerateStatementOption.METHOD_LOWER_CASE).build());

							value = value.toLowerCase();
						}
						componentReviewProPk.setReviewPro(value);
						break;
					default:
						likeValue = searchElement.getStringOperation().toQueryString(searchElement.getValue());
						break;
				}

				if (likeValue != null) {
					ComponentReviewPro componentReviewLike = new ComponentReviewPro();
					ComponentReviewProPk componentReviewProLikePk = new ComponentReviewProPk();
					componentReviewLike.setComponentReviewProPk(componentReviewProLikePk);
					if (searchElement.getCaseInsensitive()) {
						likeValue = likeValue.toLowerCase();
						componentReviewProLikePk.setReviewPro(likeValue);

						queryByExample.getLikeExampleOption().setMethod(GenerateStatementOption.METHOD_LOWER_CASE);
					}

					queryByExample.setLikeExample(componentReviewLike);
				}

				List<ComponentReviewPro> pros = serviceProxy.getPersistenceService().queryByExample(queryByExample);
				List<String> results = new ArrayList<>();
				for (ComponentReviewPro pro : pros) {
					results.add(pro.getComponentId());
				}
				return results;
			} catch (SecurityException | IllegalArgumentException | OpenStorefrontRuntimeException e) {
				throw new OpenStorefrontRuntimeException("Unable to handle search request", e);
			}
		} else if (SearchType.REVIEWCON.equals(searchElement.getSearchType())) {
			try {
				ComponentReviewCon componentReviewCon = new ComponentReviewCon();
				ComponentReviewConPk componentReviewConPk = new ComponentReviewConPk();
				componentReviewCon.setComponentReviewConPk(componentReviewConPk);
				componentReviewCon.setActiveStatus(ComponentReviewCon.ACTIVE_STATUS);

				QueryByExample<ComponentReviewCon> queryByExample = new QueryByExample<>(componentReviewCon);

				String likeValue = null;
				switch (searchElement.getStringOperation()) {
					case EQUALS:
						String value = searchElement.getValue();
						if (searchElement.getCaseInsensitive()) {
							queryByExample.getFieldOptions().put(ComponentReviewConPk.FIELD_REVIEW_CON,
									new GenerateStatementOptionBuilder().setMethod(GenerateStatementOption.METHOD_LOWER_CASE).build());

							value = value.toLowerCase();
						}
						componentReviewConPk.setReviewCon(value);
						break;
					default:
						likeValue = searchElement.getStringOperation().toQueryString(searchElement.getValue());
						break;
				}

				if (likeValue != null) {
					ComponentReviewCon componentReviewLike = new ComponentReviewCon();
					ComponentReviewConPk componentReviewConLikePk = new ComponentReviewConPk();
					componentReviewLike.setComponentReviewConPk(componentReviewConLikePk);
					if (searchElement.getCaseInsensitive()) {
						likeValue = likeValue.toLowerCase();
						componentReviewConLikePk.setReviewCon(likeValue);

						queryByExample.getLikeExampleOption().setMethod(GenerateStatementOption.METHOD_LOWER_CASE);
					}

					queryByExample.setLikeExample(componentReviewLike);
				}

				List<ComponentReviewCon> cons = serviceProxy.getPersistenceService().queryByExample(queryByExample);
				List<String> results = new ArrayList<>();
				for (ComponentReviewCon con : cons) {
					results.add(con.getComponentId());
				}
				return results;
			} catch (SecurityException | IllegalArgumentException | OpenStorefrontRuntimeException e) {
				throw new OpenStorefrontRuntimeException("Unable to handle search request", e);
			}
		}

		return new ArrayList<>();
	}

}
