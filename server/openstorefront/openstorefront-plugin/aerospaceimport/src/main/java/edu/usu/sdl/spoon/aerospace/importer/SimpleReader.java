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

import edu.usu.sdl.openstorefront.core.spi.parser.mapper.MapModel;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.GenericReader;
import java.io.InputStream;

/**
 *
 * @author bmichaelis
 */
public class SimpleReader 
        extends GenericReader<MapModel>	
{

    public SimpleReader(InputStream in) {

        super(in);
        System.out.println("I am in the constructor hahahahahah: " + in);
    }

    @Override
    public MapModel nextRecord() {
        System.out.println("I am in the next record succa!!!!!!!!!");
        throw new UnsupportedOperationException("Throwing next records is Not supported yet. I PITY THE FOOL!!!!!!!!!!!!"); //To change body of generated methods, choose Tools | Templates.
    }
    
}
