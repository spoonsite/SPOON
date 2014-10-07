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

import edu.usu.sdl.openstorefront.service.api.SearchService;
import edu.usu.sdl.openstorefront.service.manager.SolrManager;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeCodePk;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttribute;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttributePk;
import edu.usu.sdl.openstorefront.storage.model.ComponentTag;
import edu.usu.sdl.openstorefront.web.rest.model.Article;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentSearchView;
import edu.usu.sdl.openstorefront.web.rest.model.SearchQuery;
import edu.usu.sdl.openstorefront.web.rest.model.SolrComponentModel;
import edu.usu.sdl.openstorefront.web.rest.model.SolrComponentResultsModel;
import edu.usu.sdl.openstorefront.web.rest.model.SolrSearchComponent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;

/**
 *
 * @author gbagley
 * @author dshurleff
 */
public class SearchServiceImpl
		extends ServiceProxy
		implements SearchService
{

	private static final Logger log = Logger.getLogger(SearchServiceImpl.class.getName());

	@Override
	public List<ComponentSearchView> getAll()
	{
		ServiceProxy service = new ServiceProxy();
		List<ComponentSearchView> list = new ArrayList<>();
		list.addAll(service.getComponentService().getComponents());
		list.addAll(service.getAttributeService().getAllArticles());
		return list;
	}

	@Override
	public List<SolrComponentResultsModel> getSearchItems(SearchQuery query)
	{

		// QUERY / SEARCH for a solr storefront component  
		SolrSearchComponent searchThis = new SolrSearchComponent();

		SolrComponentModel mySolrModel = new SolrComponentModel();

		String exactMatch = "";

		mySolrModel.setQueryString("" + exactMatch + query.getQuery() + exactMatch + "");

		// use for finding strings that are not equal / do not contain
		String myEqualNotEqual = "";
        // myEqualNotEqual = ""; // Equal
		// myEqualNotEqual = "-"; // NOT Equal

		// use for advanced search with And - Or combinations on separate fields
		String myAndOr = " OR ";
        //myAndOr = " OR ";
		//myAndOr = " AND ";  // 

		// Define search fields per solr schema
		String[] mySetFields = new String[6];
		mySetFields[0] = "id";
		mySetFields[1] = "title";
		mySetFields[2] = "content_text";
		mySetFields[3] = "content_tags";
		mySetFields[4] = "content_raw";
                mySetFields[5] = "isComponentSearch_b_is";

		String myQueryString = null;

		// If incoming query string is blank, default to solar *:* for the full query    
		if (mySolrModel.getQueryString() != null && !mySolrModel.getQueryString().trim().isEmpty()) {
			myQueryString = myEqualNotEqual + mySetFields[1] + ":" + mySolrModel.getQueryString() + myAndOr + myEqualNotEqual + mySetFields[2] + ":" + mySolrModel.getQueryString() + myAndOr + myEqualNotEqual + mySetFields[3] + ":" + mySolrModel.getQueryString() + myAndOr + myEqualNotEqual + mySetFields[4] + ":" + mySolrModel.getQueryString();
		}
		else {
			myQueryString = "*:*";
		};

		// execute the searchComponent method and bring back from solr a list array
		List<SolrComponentResultsModel> resultsList = null;

		try {
			resultsList = searchThis.searchComponent(query, myQueryString, mySetFields[0], mySetFields[1], mySetFields[2], mySetFields[3], mySetFields[4], mySetFields[5]);
			//   List<SolrComponentModel> resultsList = searchThis.searchComponent(myQueryString, mySetFields[0], mySetFields[1], mySetFields[2]);
		}
		catch (MalformedURLException | SolrServerException ex) {
			Logger.getLogger(SearchServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		return resultsList;
	}

	@Override
	public void addComponent(Component component)
	{

		// initialize solr server
		SolrServer solrService = SolrManager.getServer();

		//add document using the example schema
		SolrComponentModel solrDocModel = new SolrComponentModel();

		solrDocModel.setIsComponent(Boolean.TRUE);
		solrDocModel.setId(component.getComponentId());
		solrDocModel.setNameString(component.getName());
		solrDocModel.setName(component.getName());
		solrDocModel.setDescription(component.getDescription());
		solrDocModel.setUpdateDate(component.getUpdateDts());
		// solrDocModel.setComponentSearch(isComponent);

		List<ComponentTag> tags = this.getComponentService().getBaseComponent(ComponentTag.class, component.getComponentId());
		List<ComponentAttribute> attributes = this.getComponentService().getBaseComponent(ComponentAttribute.class, component.getComponentId());

		String tagList = "";
		String attributeList = "";

		for (ComponentTag tag : tags) {
			tagList = tagList + tag.getText() + ",";
		}
		for (ComponentAttribute attribute : attributes) {
			ComponentAttributePk pk = attribute.getComponentAttributePk();
			AttributeCodePk codePk = new AttributeCodePk();
			codePk.setAttributeCode(pk.getAttributeCode());
			codePk.setAttributeType(pk.getAttributeType());
			AttributeCode code = this.getPersistenceService().findById(AttributeCode.class, codePk);
			AttributeType type = this.getPersistenceService().findById(AttributeType.class, pk.getAttributeType());
			if (pk != null) {
				attributeList = attributeList + pk.getAttributeCode();
				attributeList = attributeList + pk.getAttributeType();
			}
			if (code != null && type != null) {
				attributeList = attributeList + code.getLabel() + ",";
				if (!code.getDescription().equals("")) {
					attributeList = attributeList + code.getDescription() + ",";
				}
				if (!type.getDescription().equals("")) {
					attributeList = attributeList + code.getDescription() + ",";
				}
			}
		}

		solrDocModel.setTags(tagList);
		solrDocModel.setAttributes(attributeList);

		try {
			solrService.addBean(solrDocModel);
			//results.append("Add: ").append(updateResponse.toString());
			solrService.commit();
		}
		catch (IOException | SolrServerException ex) {
			System.out.println("we have problems" + ex.toString());
			//log.log(Level.SEVERE, "Failed", ex);
		}
	}

	@Override
	public void addComponent(Article article)
	{

		// initialize solr server
		SolrServer solrService = SolrManager.getServer();

		//add document using the example schema
		SolrComponentModel solrDocModel = new SolrComponentModel();
		AttributeCodePk pk = new AttributeCodePk();
		pk.setAttributeCode(article.getAttributeCode());
		pk.setAttributeType(article.getAttributeType());
		AttributeCode code = this.getPersistenceService().findById(AttributeCode.class, pk);
		AttributeType type = this.getPersistenceService().findById(AttributeType.class, article.getAttributeType());

		solrDocModel.setIsComponent(Boolean.FALSE);
		solrDocModel.setId(type.getAttributeType() + code.getAttributeCodePk().getAttributeCode());
		solrDocModel.setNameString(type.getDescription() + " " + code.getLabel() + " Article");
		solrDocModel.setName(type.getDescription() + " " + code.getLabel() + " Article");
		solrDocModel.setDescription(type.getDescription() + " " + code.getLabel() + " Article" + code.getDescription());
		solrDocModel.setUpdateDate(article.getUpdateDts());
		// solrDocModel.setComponentSearch(isComponent);

		String tagList = "";
		String attributeList = "";
		if (type != null && code != null) {
			attributeList = type.getAttributeType() + "," + type.getDescription() + "," + code.getLabel() + "," + code.getDescription();
		}

		solrDocModel.setTags(tagList);
		solrDocModel.setAttributes(attributeList);

		try {
			solrService.addBean(solrDocModel);
			//results.append("Add: ").append(updateResponse.toString());
			solrService.commit();
		}
		catch (IOException | SolrServerException ex) {
			System.out.println("we have problems" + ex.toString());
			//log.log(Level.SEVERE, "Failed", ex);
		}
	}

}
