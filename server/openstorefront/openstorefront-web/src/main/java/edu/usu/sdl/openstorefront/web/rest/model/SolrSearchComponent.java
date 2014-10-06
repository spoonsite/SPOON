/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.openstorefront.web.rest.model;

//import edu.usu.sdl.solr.service.SolrService;
//import edu.usu.sdl.solr.service.model.SolrComponentResultsModel;
import edu.usu.sdl.openstorefront.service.query.SolrService;
import edu.usu.sdl.openstorefront.storage.model.ComponentTag;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

/**
 *
 * @author gbagley
 *
 * Query components based on search string supplied by user
 *
 */
public class SolrSearchComponent //extends BaseTestCase
{
    //private static final Logger log = Logger.getLogger(AddTestCase.class.getName());

    //@Override
    public String description() {
        return "Search Component";
    }

    public List<SolrComponentResultsModel> searchComponent(
            SearchQuery searchQuery,
            String searchString,
            String setFieldsIndex,
            String setFieldsName,
            String setFieldsDescription,
            String setFieldsTags,
            String setFieldsAttributes) throws MalformedURLException, SolrServerException {

        // initialize solr server
        SolrService.init();

        SolrQuery query = new SolrQuery();

        query.setQuery(searchString); // (default field?)

        // fields to be returned back from solr
        query.setFields(setFieldsIndex, setFieldsName, setFieldsDescription, setFieldsTags, setFieldsAttributes);

        // begin at nth solr index entry
        query.setStart(0);

        // Solr call
        QueryResponse response = SolrService.getExampleServer().query(query);

        SolrDocumentList results = response.getResults();

        // define array for holding solr response
        List<SolrComponentResultsModel> resultsList = new ArrayList<>();

        for (SolrDocument doc : results) {
            // place search results into results model
            SolrComponentResultsModel resultsModel = new SolrComponentResultsModel();

            resultsModel.setComponentID(doc.getFieldValue(setFieldsIndex).toString());
            resultsModel.setName(doc.getFieldValue(setFieldsName).toString());
            resultsModel.setDescription(doc.getFieldValue(setFieldsDescription).toString());

            //------------------------------------------------------------------------------
            // Process Tags
            List<String> solrTags = new ArrayList<String>();
            List<ComponentTag> tagsComponentTag = new ArrayList<>();

            solrTags.add(doc.getFieldValues(setFieldsTags).toString().replace("[", "").replace("]", ""));
            // Solr sends back comma delimited list of tags
            // iterate over array items in comma delimited string and copy into ComponentTag model
            for (String s : solrTags) {
                String[] temp;
                String delimiter = ",";
                temp = s.split(delimiter);
                String solrItem;

                for (int i = 0; i < temp.length; i++) {
                    ComponentTag tag = new ComponentTag();

                    solrItem = temp[i];

                    tag.setTagId(Integer.toString(i));
                    tag.setText(solrItem.trim());
                    tagsComponentTag.add(tag);
                }
                resultsModel.setTags(tagsComponentTag);
            }

            //------------------------------------------------------------------------------------------
            // Process Attributes
            List<String> solrAttributes = new ArrayList<String>();
            List<SolrAttributeCodeTypeModel> attributesComponent = new ArrayList<>();

            solrAttributes.add(doc.getFieldValues(setFieldsAttributes).toString().replace("[", "").replace("]", ""));

            // Solr sends back comma delimited list of attributes
            // iterate over array items in comma delimited string and copy into SolrAttributes model
            for (String s : solrAttributes) {
                String[] temp;
                String delimiter = ",";
                temp = s.split(delimiter);
                String solrItem;

                for (int i = 0; i < temp.length; i++) {
//                    "code": "3.5.1.1|DI2E-SVCV4-A Code Description|DI2E-SVCV4-A|DI2E SvcV-4 Alignment",
//                    "codeDescription": "3.5.1.1|DI2E-SVCV4-A Code Description|DI2E-SVCV4-A|DI2E SvcV-4 Alignment",
//                    "type": "3.5.1.1|DI2E-SVCV4-A Code Description|DI2E-SVCV4-A|DI2E SvcV-4 Alignment",
//                    "typeDescription": "3.5.1.1|DI2E-SVCV4-A Code Description|DI2E-SVCV4-A|DI2E SvcV-4 Alignment"
                    SolrAttributeCodeTypeModel attributes = new SolrAttributeCodeTypeModel();

                    solrItem = temp[i]; // "3.5.1.1|DI2E-SVCV4-A Code Description|DI2E-SVCV4-A|DI2E SvcV-4 Alignment",

                    String[] temp2;
                    String delimiter2 = "|";
                    temp2 = solrItem.split("\\Q" + delimiter2);

                    attributes.setCode(temp2[0]);
                    attributes.setCodeDescription(temp2[1]);
                    attributes.setType(temp2[2]);
                    attributes.setTypeDescription(temp2[3]);

                    attributesComponent.add(attributes);
                }
                resultsModel.setAttributes(attributesComponent);
            }

            // populate before writing out
            resultsList.add(resultsModel);
        }
        return resultsList;
    }
}
