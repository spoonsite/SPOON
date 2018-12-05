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

import au.com.bytecode.opencsv.CSVReader;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.spoon.aerospace.importer.model.Classification;
import edu.usu.sdl.spoon.aerospace.importer.model.Product;
import edu.usu.sdl.spoon.aerospace.importer.model.Services;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 *
 * @author rfrazier
 */
public class AerospaceXMLParser {
    
    private static final Logger log = Logger.getLogger(Activator.class.getName());
    private Map<String, String> componentTypeMap;
    
    AerospaceXMLParser() {
        try {
            componentTypeMap = getComponentTypeMap();
        } catch (Exception e) {
            log.severe("Unable to initialize component type map from CSV: " + e.toString());
        }
    }

    
    /**
     * reads the component type mapping CSV in the class resources and
     * returns a map of Aerospace product type to Storefront component type
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public Map<String, String> getComponentTypeMap() throws FileNotFoundException, IOException {
        Map<String, String> componentTypeMap = new HashMap<>();

        Reader in = new FileReader(this.getClass().getResource("/componentTypeMapping.csv").getFile());
        CSVReader reader = new CSVReader(in);
        
        String[] record;
        while ((record = reader.readNext()) != null) {
            componentTypeMap.put(record[0], record[1]);
        }

        return componentTypeMap;
    }

    /**
     *
     * @param in an input XML stream
     * @return a the root of the document i.e. a parsed services object
     */
    public Services parseXML(InputStream in) {
        Serializer serializer = new Persister();
        Services services = null;

        try(InputStream xmlin = in) {
            services = serializer.read(Services.class, xmlin);
        } catch (Exception e) {
            System.out.println("Unable to serialize: \n" + e.getMessage());// USER THE LOGGER 
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

    public String getComponentType(Product product) {
        List<Classification> classifications = product.getProductRevision().getProductFamily().getClassification();
        String result = "";

        if (classifications.size() > 0) {
            String name = classifications.get(0).getCategoryName();
            // map Aerospace classification to storefront componentType
            result = componentTypeMap.get(name);
        }

        return result;
    }

    public List<ComponentAttribute> getComponentAttributes(Product product) {
        List<ComponentAttribute> result = new ArrayList();

        return result;
    }
}
