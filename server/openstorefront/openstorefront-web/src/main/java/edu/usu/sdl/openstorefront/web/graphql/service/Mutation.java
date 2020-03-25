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

import io.leangen.graphql.annotations.GraphQLMutation;

/**
 * This file was derived from the tutorial 
 * found here: https://www.howtographql.com/graphql-java/1-getting-started/
 * @author afine
 */
public class Mutation
{
	    private final LinkRepository linkRepository;

    public Mutation(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }
    
	@GraphQLMutation
    public Link createLink(String url, String description) {
        Link newLink = new Link(url, description, "get username or id");
        linkRepository.saveLink(newLink);
        return newLink;
    }
}
