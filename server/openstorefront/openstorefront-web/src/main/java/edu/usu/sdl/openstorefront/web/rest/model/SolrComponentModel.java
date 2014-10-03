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

import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.storage.model.ComponentTag;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Field("title")
    private String name;

//    @Field
//    private String title;
    @Field("content_text")
    private String description;

//    @Field
//    private String content_text;
    @Field
    private String queryString;

    @Field
    private String guid;

    @Field("tags_s_ims")
    @DataType(ComponentTag.class)
    private List<ComponentTag> tags = new ArrayList<>();

    @Field("attributes_s_ims")
    // @DataType(ComponentTag.class)
    //private List<ComponentTag> tags = new ArrayList<>();
    private List<SolrAttributeCodeTypeModel> attributes = new ArrayList<>();

    @Field
    private String organization;

    @Field
    private Date releaseDate;

    @Field
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
}
