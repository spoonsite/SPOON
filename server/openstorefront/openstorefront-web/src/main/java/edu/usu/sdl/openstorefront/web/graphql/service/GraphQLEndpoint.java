/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.graphql.service;

import com.coxautodev.graphql.tools.SchemaParser;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import edu.usu.sdl.openstorefront.service.manager.MongoDBManager;
import io.leangen.graphql.GraphQLSchemaGenerator;
import edu.usu.sdl.openstorefront.web.rest.service.SearchV2;
import graphql.schema.GraphQLSchema;
import javax.servlet.annotation.WebServlet;
import graphql.servlet.SimpleGraphQLServlet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This file was derived from the tutorial 
 * found here: https://www.howtographql.com/graphql-java/1-getting-started/
 * @author afine
 */
@WebServlet(urlPatterns = "/graphql") 
// Access: http://localhost:8080/openstorefront/graphql?query=%7BallLinks%7Burl%7D%7D
//       : http://localhost:8080/openstorefront/graphql?query={allLinks{url}}
public class GraphQLEndpoint extends SimpleGraphQLServlet
{

	private static final Logger LOG = Logger.getLogger(SearchV2.class.getName());

	public GraphQLEndpoint()
	{
		super(buildSchema());
	}

	private static GraphQLSchema buildSchema()
	{
		LOG.log(Level.INFO, "GraphQL Endpoint called!");

		//create or inject the service beans
//		MongoDatabase mongo = MongoDBManager.getInstance().getConnection();
//		LinkRepository linkRepository = new LinkRepository(mongo.getCollection("links"));
//		Query query = new Query(linkRepository);
//		Mutation mutation = new Mutation(linkRepository);
//
//		return new GraphQLSchemaGenerator()
//				.withOperationsFromSingletons(query, linkRepository, mutation) //register the beans
//				.generate();
		String schema
				= "type Link {\n"
				+ "  url: String!\n"
				+ "  description: String!\n"
				+ "}\n"
				+ "type Query {\n"
				+ "  allLinks: [Link]\n"
				+ "}\n"
				+ "schema {\n"
				+ "  query: Query\n"
				+ "}";

		LinkRepository linkRepository = new LinkRepository();
		return SchemaParser.newParser()
				.schemaString(schema)
				.resolvers(new Query(linkRepository))
				.build()
				.makeExecutableSchema();
	}
}
