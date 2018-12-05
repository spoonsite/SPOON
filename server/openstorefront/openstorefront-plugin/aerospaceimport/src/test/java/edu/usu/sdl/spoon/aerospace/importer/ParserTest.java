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

import edu.usu.sdl.spoon.aerospace.importer.model.FloatFeature;
import edu.usu.sdl.spoon.aerospace.importer.model.Product;
import edu.usu.sdl.spoon.aerospace.importer.model.Services;
import edu.usu.sdl.spoon.aerospace.importer.model.Shape;
import edu.usu.sdl.spoon.aerospace.importer.model.Specs;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author rfrazier
 */
public class ParserTest {

    @Test
    public void testParseXML() {
        //--- ARRANGE ---

        //  Test XML generated with Python script
        //  with open("AerospaceTest.xml", 'r') as fin:
        //     for line in fin:
        //         print('+ "' + line.strip('\n\r').replace('"','\\"') + '"')
        String testString
                = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<services>"
                + "    <product>"
                + "        <key>525</key>"
                + "        <short_name>1.5U CubeSat EPS v10</short_name>"
                + "        <long_name>1.5U CubeSat EPS 10 Whr Integrated Battery</long_name>"
                + "        <product_source>Unknown</product_source>"
                + "        <model_number>CS-1U5EPS2-10</model_number>"
                + "        <description>"
                + "            High Efficiency Solar Array Interface "
                + "        </description>"
                + "        <product_revision>"
                + "            <from_date>2014-04-14</from_date>"
                + "            <thru_date></thru_date>"
                + "            <specs>"
                + "                <float_feature>"
                + "                    <name>Battery Power Capacity</name>"
                + "                    <description>Battery power capacity (W-hr)</description>"
                + "                    <value_description></value_description>"
                + "                    <type>function</type>"
                + "                    <data_type>float</data_type>"
                + "                    <value>10.0</value>"
                + "                    <unit>Watt-hour</unit>"
                + "                    <unit_abbr>W-hr</unit_abbr>"
                + "                </float_feature>"
                + "            </specs>"
                + "            <shape>"
                + "                <float_feature>"
                + "                    <name>Mass</name>"
                + "                    <description>Product mass</description>"
                + "                    <value_description></value_description>"
                + "                    <type>form</type>"
                + "                    <data_type>float</data_type>"
                + "                    <value>0.165</value>"
                + "                    <unit>Kilogram</unit>"
                + "                    <unit_abbr>kg</unit_abbr>"
                + "                </float_feature>"
                + "            </shape>"
                + "            <product_type>"
                + "                <classification>"
                + "                    <category_name>Subsystem</category_name>"
                + "                </classification>"
                + "            </product_type>"
                + "            <product_family>"
                + "                <classification>"
                + "                    <category_name>Electrical Power Subsystem (EPS)</category_name>"
                + "                </classification>"
                + "            </product_family>"
                + "        </product_revision>"
                + "        <organizations></organizations>"
                + "    </product>"
                + "</services>";

        byte[] data = testString.getBytes();
        InputStream input = new ByteArrayInputStream(data);
        AerospaceXMLParser parser = new AerospaceXMLParser();

        //--- ACT ---
        Services services = parser.parseXML(input);
        List<Product> products = services.getProducts();
        Product product = products.get(0);

        // get the nested data from the models
        Specs productRevisionSpecs = product.getProductRevision().getSpecs();
        FloatFeature floatFeature = productRevisionSpecs.getFloatFeatures().get(0);
        Shape productRevisionShape = product.getProductRevision().getShape();
        FloatFeature floatFeature2 = productRevisionShape.getFloatFeatures().get(0);

        //--- ASSERT ---
        assertEquals(product.getKey(), 525);
        assertEquals(product.getShortName(), "1.5U CubeSat EPS v10");
        assertEquals(product.getProductSource(), "Unknown");
        assertEquals(floatFeature.getValue(), (Double) 10.0);
        assertEquals(floatFeature2.getValue(), (Double) 0.165);
    }

}
