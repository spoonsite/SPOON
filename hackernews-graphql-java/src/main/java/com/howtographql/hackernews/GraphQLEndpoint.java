/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.howtographql.hackernews;

import com.coxautodev.graphql.tools.*;
import graphql.schema.*;
import javax.servlet.annotation.WebServlet;
import graphql.servlet.SimpleGraphQLServlet;

/**
 * This is a test server to determine how GraphQL works. This file should tell Jetty to do something with incoming traffic. 
 * @author tsavage
 */
@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {
    
    
    
    public GraphQLEndpoint() {
//        super(SchemaParser.newParser()
//                .file("schema.graphqls") //parse the schema file created earlier
//                .build()
//                .makeExecutableSchema());
        super(buildSchema());
    }
    
    private static GraphQLSchema buildSchema() {
        LinkRepository linkRepository = new LinkRepository();
        return SchemaParser.newParser()
                .file("schema.graphqls")
                .resolvers(new Query(linkRepository))
                .build()
                .makeExecutableSchema();
    }
}
