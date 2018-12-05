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
import edu.usu.sdl.spoon.aerospace.importer.model.FloatFeature;
import edu.usu.sdl.spoon.aerospace.importer.model.Product;
import edu.usu.sdl.spoon.aerospace.importer.model.Services;
import edu.usu.sdl.spoon.aerospace.importer.model.Shape;
import edu.usu.sdl.spoon.aerospace.importer.model.Specs;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author rfrazier
 */
public class ParserTest {
    
    public InputStream xmlFileStream;
    
    /**
     * 
     * @throws FileNotFoundException if the test XML cannot be found
     */
    @Before
    public void setup() throws FileNotFoundException {
        File xmlFile = new File(this.getClass().getResource("/AerospaceTest.xml").getFile());
        
        this.xmlFileStream = new FileInputStream(xmlFile);
    }

    @Test
    public void testParseXML() {
        //--- ARRANGE ---
        AerospaceXMLParser parser = new AerospaceXMLParser();

        //--- ACT ---
        Services services = parser.parseXML(this.xmlFileStream);
        List<Product> products = services.getProducts();
        Product product = products.get(0);

        // get the nested data from the models
        Specs productRevisionSpecs = product.getProductRevision().getSpecs();
        FloatFeature floatFeature = productRevisionSpecs.getFloatFeatures().get(0);
        Shape productRevisionShape = product.getProductRevision().getShape();
        FloatFeature floatFeature2 = productRevisionShape.getFloatFeatures().get(0);

//        String componentType
//                = product.getProductRevision()
//                        .getProductFamily()
//                        .getClassification()
//                        .get(0)
//                        .getCategoryName();

        //--- ASSERT ---
        assertEquals(product.getKey(), 525);
        assertEquals(product.getShortName(), "CubeSat");
        assertEquals(product.getProductSource(), "Unknown");
        assertEquals(floatFeature.getValue(), (Double) 10.0);
        assertEquals(floatFeature2.getValue(), (Double) 0.165);
    }

    // productToComponentAll not yet implemented
    // should fail
    @Test
    @Ignore
    public void testProductToComponentAll() {
        //--- ARRANGE ---
        AerospaceXMLParser parser = new AerospaceXMLParser();
        Services services = parser.parseXML(this.xmlFileStream);
        List<Product> products = services.getProducts();
        Product product = products.get(0);

        //--- ACT ---
        ComponentAll componentAll = parser.productToComponentAll(product);

        //--- ASSERT ---
        assertEquals(componentAll.getComponent().getDescription(), "High Efficiency Solar Array Interface");
        // assertEquals(componentAll.getComponent().getComponentType(), data);
    }
    
    @Test
    @Ignore
    public void testGetComponentTypeMap() throws IOException {
        AerospaceXMLParser parser = new AerospaceXMLParser();
        Map<String, String> map = parser.getComponentTypeMap();
        
        String result = map.get("Electrical Power Subsystem (EPS)");
        assertEquals(result, "POW-DIST");
    }
    
    @Test
    @Ignore
    public void testGetComponentType() {
        //--- ARRANGE ---
        AerospaceXMLParser parser = new AerospaceXMLParser();
        Services services = parser.parseXML(this.xmlFileStream);
        List<Product> products = services.getProducts();
        Product product = products.get(0);

        //--- ACT ---
        String componentType = parser.getComponentType(product);


        //--- ASSERT ---
        assertEquals(componentType, "POW-DIST" );
    }
    
}
