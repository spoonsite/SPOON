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

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 *
 * @author afine
 */
public class LinkRepository
{

//	private final MongoCollection<Document> links;
//
//	public LinkRepository(MongoCollection<Document> links)
//	{
//		this.links = links;
//	}
//
//	public Link findById(String id)
//	{
//		Document doc = links.find(eq("_id", new ObjectId(id))).first();
//		return link(doc);
//	}
//
//	public List<Link> getAllLinks(LinkFilter filter, int skip, int first)
//	{
//		Optional<Bson> mongoFilter = Optional.ofNullable(filter).map(this::buildFilter);
//
//		List<Link> allLinks = new ArrayList<>();
//		FindIterable<Document> documents = mongoFilter.map(links::find).orElseGet(links::find);
//		for (Document doc : documents.skip(skip).limit(first)) {
//			allLinks.add(link(doc));
//		}
//		return allLinks;
//	}
//
//	//builds a Bson from a LinkFilter
//	private Bson buildFilter(LinkFilter filter)
//	{
//		String descriptionPattern = filter.getDescriptionContains();
//		String urlPattern = filter.getUrlContains();
//		Bson descriptionCondition = null;
//		Bson urlCondition = null;
//		if (descriptionPattern != null && !descriptionPattern.isEmpty()) {
//			descriptionCondition = regex("description", ".*" + descriptionPattern + ".*", "i");
//		}
//		if (urlPattern != null && !urlPattern.isEmpty()) {
//			urlCondition = regex("url", ".*" + urlPattern + ".*", "i");
//		}
//		if (descriptionCondition != null && urlCondition != null) {
//			return and(descriptionCondition, urlCondition);
//		}
//		return descriptionCondition != null ? descriptionCondition : urlCondition;
//	}
//
//	public void saveLink(Link link)
//	{
//		Document doc = new Document();
//		doc.append("url", link.getUrl());
//		doc.append("description", link.getDescription());
//		links.insertOne(doc);
//	}
//
//	private Link link(Document doc)
//	{
//		return new Link(
//				doc.get("_id").toString(),
//				doc.getString("url"),
//				doc.getString("description"));
//	}
	private final List<Link> links;

	public LinkRepository()
	{
		links = new ArrayList<>();
		//add some links to start off with
		links.add(new Link("first", "http://howtographql.com", "Your favorite GraphQL page"));
		links.add(new Link("second", "http://graphql.org/learn/", "The official docks"));
	}

	public List<Link> getAllLinks()
	{
		return links;
	}

	public void saveLink(Link link)
	{
		links.add(link);
	}
}
