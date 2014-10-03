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
import edu.usu.sdl.openstorefront.web.rest.model.SearchQuery;
import edu.usu.sdl.openstorefront.web.rest.model.SolrComponentModel;
import edu.usu.sdl.openstorefront.web.rest.model.SolrComponentResultsModel;
import edu.usu.sdl.openstorefront.web.rest.model.SolrSearchComponent;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.SolrServerException;

/**
 *
 * @author gbagley
 * 
 * Prepare the search fields for SOLR search and call method
 * 
 */
public class SearchServiceImpl
        extends ServiceProxy
        implements SearchService {

    private static final Logger log = Logger.getLogger(SearchServiceImpl.class.getName());

    @Override
    public List<SolrComponentResultsModel> getSearchItems(SearchQuery query) {

        // QUERY / SEARCH for a solr storefront component 
        SolrSearchComponent searchThis = new SolrSearchComponent();
     
        SolrComponentModel mySolrModel = new SolrComponentModel();

        String exactMatch = "";
        
        // did user request exact search?
        if (query.isExactSearch()){
            exactMatch = "\"";
        }

        // mySolrModel.setQueryString("\"Some Component\"");
//        // incoming search text
//        // can use ? * on either end
//        mySolrModel.setQueryString("*wall*");
//        mySolrModel.setQueryString("boeing*");
//        mySolrModel.setQueryString("*itus*");
//        mySolrModel.setQueryString("*2d* AND *api*");
//        mySolrModel.setQueryString("manipu*");
//        // mySolrModel.setQueryString("[2014-09-22T00:00:00.001Z TO 2014-09-22T23:59:59.999Z]");
//        mySolrModel.setQueryString("" + exactMatch + "used to be chosen dynamically by the user at runtime rather than being chosen by the developer" + exactMatch + "");
//        // mySolrModel.setQueryString("dynamically runtime developer");

      //  mySolrModel.setQueryString(query.getQuery());
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
        String[] mySetFields = new String[5];
        mySetFields[0] = "id";
        mySetFields[1] = "title";
        mySetFields[2] = "content_text";
        mySetFields[3] = "content_tags";
        mySetFields[4] = "content_raw";

        String myQueryString = null;

        // If incoming query string is blank, default to solar *:* for the full query    
        if (mySolrModel.getQueryString() != null && !mySolrModel.getQueryString().trim().isEmpty()) {
            myQueryString = myEqualNotEqual + mySetFields[1] + ":" + mySolrModel.getQueryString() + myAndOr + myEqualNotEqual + mySetFields[2] + ":" + mySolrModel.getQueryString() + myAndOr + myEqualNotEqual + mySetFields[3] + ":" + mySolrModel.getQueryString() + myAndOr + myEqualNotEqual + mySetFields[4] + ":" + mySolrModel.getQueryString();
        } else {
            myQueryString = "*:*";
        };

        // execute the searchComponent method and bring back from solr a list array
        List<SolrComponentResultsModel> resultsList = null;

        try {
            resultsList = searchThis.searchComponent(query, myQueryString, mySetFields[0], mySetFields[1], mySetFields[2], mySetFields[3], mySetFields[4]);
            //   List<SolrComponentModel> resultsList = searchThis.searchComponent(myQueryString, mySetFields[0], mySetFields[1], mySetFields[2]);
        } catch (MalformedURLException ex) {
            Logger.getLogger(SearchServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SolrServerException ex) {
            Logger.getLogger(SearchServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return resultsList;
    }
}
