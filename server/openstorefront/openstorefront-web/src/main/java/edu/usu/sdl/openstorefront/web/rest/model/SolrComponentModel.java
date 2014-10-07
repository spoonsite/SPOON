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
package edu.usu.sdl.openstorefront.web.rest.model;

import java.util.Date;
import org.apache.solr.client.solrj.beans.Field;

/**
 *
 * @author gbagley
 */
public class SolrComponentModel {
// private variables

    @Field
    private String id;

    @Field
    private String componentId;

    @Field("isComponentSearch_b_is")
    private Boolean isComponent;

    @Field("title")
    private String name;

    @Field("content_text")
    private String description;

    @Field("content_tags")
    private String tags;

    @Field("content_raw")
    private String attributes;

    @Field
    private String queryString;

    @Field
    private String guid;

    @Field
    private String organization;

    @Field
    private Date releaseDate;

    @Field("modified")
    private Date updateDate_dt_is;

    @Field
    private String version;

    @Field("idInt_i_is")
    private int idInt;

    @Field("name_s_is")
    private String nameString;

    // constructor 
    public SolrComponentModel() {
    }

    // public methods
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIdInt() {
        return idInt;
    }

    public void setIdInt(Integer idInt) {
        this.idInt = idInt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameString() {
        return nameString;
    }

    public void setNameString(String nameString) {
        this.nameString = nameString;
    }

//
//    
//    public String getName() {
//        return title;
//    }
//
//    public void setName(String title) {
//        this.title = title;
//    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    
//    public String getDescription() {
//        return content_text;
//    }
//
//    public void setDescription(String content_text) {
//        this.content_text = content_text;
//    }
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

    /**
     * @return the tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * @return the attributes
     */
    public String getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the isComponent
     */
    public Boolean getIsComponent() {
        return isComponent;
    }

    /**
     * @param isComponent the isComponent to set
     */
    public void setIsComponent(Boolean isComponent) {
        this.isComponent = isComponent;
    }

}
