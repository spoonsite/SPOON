/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.spoon.aerospace.importer;

import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.spoon.aerospace.importer.model.Product;
import edu.usu.sdl.spoon.aerospace.importer.model.Services;
import java.io.InputStream;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 *
 * @author rfrazier
 */
public class Parser {
    
    /**
     * 
     * @param in an input XML stream
     * @return a the root of the document i.e. a parsed services object
     */
    public Services parseXML(InputStream in) {
        Serializer serializer = new Persister();
        Services services = null;

        try {
            services = serializer.read(Services.class, in);
        } catch (Exception e) {
            System.out.println("Unable to serialize: \n" + e.getMessage());
        }

        return services;
    }
    
    /**
     * 
     * @param product the parsed root of the XML document
     * @return a mapped component with all data mapped to storefront
     */
    public ComponentAll productToComponentAll(Product product) {
        ComponentAll result = new ComponentAll();
        
        // map the data in the product to attributes and data in the componentAll
        
        return result;
    }
}
