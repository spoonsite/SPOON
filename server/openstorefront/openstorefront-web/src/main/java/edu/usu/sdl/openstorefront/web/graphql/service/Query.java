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

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import io.leangen.graphql.annotations.GraphQLArgument;
import java.util.List;
import io.leangen.graphql.annotations.GraphQLQuery;

/**
 * This file was derived from the tutorial 
 * found here: https://www.howtographql.com/graphql-java/1-getting-started/
 * @author afine
 */
//public class Query
//{
//
//	private final LinkRepository linkRepository;
//
//	public Query(LinkRepository linkRepository)
//	{
//		this.linkRepository = linkRepository;
//	}
//	
//	@GraphQLQuery
//	public List<Link> allLinks(LinkFilter filter,
//                               @GraphQLArgument(name = "skip", defaultValue = "0") Number skip,
//                               @GraphQLArgument(name = "first", defaultValue = "0") Number first)
//	{
//		return linkRepository.getAllLinks(filter, skip.intValue(), first.intValue());
//	}
//}

public class Query implements GraphQLRootResolver {
    
    private final LinkRepository linkRepository;

    public Query(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public List<Link> allLinks() {
        return linkRepository.getAllLinks();
    }
}
