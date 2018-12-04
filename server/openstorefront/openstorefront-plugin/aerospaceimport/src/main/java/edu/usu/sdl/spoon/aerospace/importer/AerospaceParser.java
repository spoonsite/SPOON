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
import edu.usu.sdl.openstorefront.core.spi.parser.BaseComponentParser;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.GenericReader;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.XMLMapReader;
import edu.usu.sdl.spoon.aerospace.importer.model.Product;
import java.io.InputStream;

/**
 *
 * @author rfrazier
 */
public class AerospaceParser
        extends BaseComponentParser {

    public static final String FORMAT_CODE = "AEROSPACECMP";

    @Override
    public String checkFormat(String mimeType, InputStream input) {
        if (mimeType.contains("text")) {
            return "";
        } else {
            return "Invalid format. Please upload a TXT file.";
        }
    }

    @Override
    protected GenericReader getReader(InputStream in) {
        //return a product
//          return new XMLMapReader(in);
        return new CopyReader(in);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected <T> Object parseRecord(T record) {
//      PROCEED NO FURTHER, YOU HAVE BEEN WARNED!!!!
        Product product = (Product) record;

        ComponentAll componentAll = defaultComponentAll();

        return componentAll;
    }

    @Override
    protected void finishProcessing() {
        super.finishProcessing(); //To change body of generated methods, choose Tools | Templates.
    }

}
