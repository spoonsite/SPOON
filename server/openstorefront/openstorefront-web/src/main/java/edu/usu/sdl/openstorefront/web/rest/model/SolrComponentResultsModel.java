/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.openstorefront.web.rest.model;

//import java.math.BigDecimal;
//import java.util.ArrayList;
import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.storage.model.ComponentTag;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.solr.client.solrj.beans.Field;

/**
 *
 * @author gbagley * This model is to be populated and consumed by Solr for
 * inserts/updates
 */
//public class SolrComponentResultsModel {
public class SolrComponentResultsModel {
// private variables

    @Field
    private String id;

    @Field
    private String componentId;

    @Field("title")
    private String name;

    @Field("content_text")
    private String description;

    @Field("content_tags")
    private String tagsText;
    
    @Field("content_raw")
    private String attributesText;

    @Field("isComponentSearch_b_is")
    private Boolean componentSearch;
       
    @Field("tags_s_ims")
    @DataType(ComponentTag.class)
    private List<ComponentTag> tags = new ArrayList<>();

    @Field("attributes_s_ims")
    @DataType(SolrAttributeCodeTypeModel.class)
    private List<SolrAttributeCodeTypeModel> attributes = new ArrayList<>();

    @Field
    private String queryString;

    @Field
    private String guid;

    @Field
    private String organization;

    @Field
    private Date releaseDate;

    @Field
    private Date updateDate_dt_is;

    @Field
    private String version;

    // constructor 
    public SolrComponentResultsModel() {
    }

    // public methods
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTagsText() {
        return tagsText;
    }

    public void setTagsText(String tagsText) {
        this.tagsText = tagsText;
    }

    public boolean isComponentSearch() {
        return componentSearch;
    }

    public void setComponentSearch(boolean componentSearch) {
        this.componentSearch = componentSearch;
    }  
    
    public String getComponentID() {
        return componentId;
    }

    public void setComponentID(String componentId) {
        this.componentId = componentId;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getUpdateDate() {
        return updateDate_dt_is;
    }

    public void setUpdateDate(Date updateDate_dt_is) {
        this.updateDate_dt_is = updateDate_dt_is;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    void setID(String toString) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<ComponentTag> getTags() {
        return tags;
    }

    public void setTags(List<ComponentTag> tags) {
        this.tags = tags;
    }


    public List<SolrAttributeCodeTypeModel> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<SolrAttributeCodeTypeModel> attributes) {
        this.attributes = attributes;
    }

    
//    public List<SearchResultAttribute> getAttributes() {
//        return attributes;
//    }
//
//    public void setAttributes(List<SearchResultAttribute> attributes) {
//        this.attributes = attributes;
//    }
//    
}
