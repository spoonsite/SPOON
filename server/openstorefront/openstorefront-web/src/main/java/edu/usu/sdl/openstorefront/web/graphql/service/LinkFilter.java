/*
 * Copyright 2020 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */
package edu.usu.sdl.openstorefront.web.graphql.service;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This file was derived from the tutorial 
 * found here: https://www.howtographql.com/graphql-java/1-getting-started/
 * @author afine
 */
public class LinkFilter
{
	    private String descriptionContains;
    private String urlContains;

    @JsonProperty("description_contains") //the name must match the schema
    public String getDescriptionContains() {
        return descriptionContains;
    }

    public void setDescriptionContains(String descriptionContains) {
        this.descriptionContains = descriptionContains;
    }

    @JsonProperty("url_contains")
    public String getUrlContains() {
        return urlContains;
    }

    public void setUrlContains(String urlContains) {
        this.urlContains = urlContains;
    }
}
