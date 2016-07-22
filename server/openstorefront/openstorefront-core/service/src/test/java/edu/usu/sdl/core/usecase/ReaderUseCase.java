/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.core.usecase;

import edu.usu.sdl.openstorefront.service.io.mapper.MapModel;
import edu.usu.sdl.openstorefront.service.io.reader.XMLMapReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class ReaderUseCase
{
	@Test
	public void testXMLMapReader() throws FileNotFoundException, Exception{
		
		try (XMLMapReader mapReader = new XMLMapReader(new FileInputStream("T:\\DI2E\\spoon\\ADACS_Equipment_Actuators_CMGs.xml"))) {
			MapModel mapModel = mapReader.findFields(new FileInputStream("T:\\DI2E\\spoon\\ADACS_Equipment_Actuators_CMGs.xml"));
		//	String fileData = mapModel.getArrayFields().get(1).getArrayFields().get(0).getMapFields().get(0).getValue();
		//	Base64.decodeToFile(fileData, "/temp/check.pdf");
		
			mapModel.getUniqueFields().forEach(f->System.out.println(f));
			
//			ComponentMapper componentMapper = new ComponentMapper(DataTemplateEntity<ComponentAll>{
//				
//			}, new FileHistoryAll());
			
			
		}
		
	}

}
