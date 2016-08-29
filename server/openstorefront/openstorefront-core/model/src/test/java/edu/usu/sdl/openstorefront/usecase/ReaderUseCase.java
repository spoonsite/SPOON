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
package edu.usu.sdl.openstorefront.usecase;

import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.model.AttributeAll;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.model.FileHistoryAll;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.AttributeMapper;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.DataMapper;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.MapModel;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.StringTransforms;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.XMLMapReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class ReaderUseCase
{
	@Test
	public void testXMLMapReader() throws FileNotFoundException, Exception{
		
		try (XMLMapReader mapReader = new XMLMapReader(new FileInputStream("C:\\temp\\spoon\\ADACS_Equipment_Actuators_CMGs.xml"))) {
			MapModel mapModel = mapReader.findFields(new FileInputStream("C:\\temp\\spoon\\ADACS_Equipment_Actuators_CMGs.xml"));
		//	String fileData = mapModel.getArrayFields().get(1).getArrayFields().get(0).getMapFields().get(0).getValue();
			//Base64.decodeToFile(fileData, "/temp/check.pdf");
		
			mapModel.getUniqueFields().forEach(f->System.out.println(f));
			
			FileHistoryAll fileHistory = new FileHistoryAll();
			
			Map<String, DataMapper> dataMappers = new HashMap<>();
			
			DataMapper dataMapper = new DataMapper();
			dataMapper.setRootField(true);
			dataMapper.setField("dataroot.Module_ADACS_Equipment_Actuators_CMGs");
			dataMapper.setEntityClass(ComponentAll.class);			
			dataMappers.put("dataroot.Module_ADACS_Equipment_Actuators_CMGs", dataMapper);
			
			dataMapper = new DataMapper();
			dataMapper.setRootField(false);
			dataMapper.setField("dataroot.Module_ADACS_Equipment_Actuators_CMGs.Model");
			dataMapper.setEntityClass(Component.class);
			dataMapper.setEntityField("name");
			dataMapper.getTransforms().add(StringTransforms.UPPERCASE);
			dataMappers.put("dataroot.Module_ADACS_Equipment_Actuators_CMGs.Model", dataMapper);			
			
//			ComponentMapper componentMapper = new ComponentMapper(() -> {
//				ComponentAll componentAll = new ComponentAll();
//				componentAll.setComponent(new Component());
//				return componentAll;
//			}, new FileHistoryAll(), dataMappers, null);
//			
//
//			
//			List<ComponentAll> componentAlls  = componentMapper.multiMapData(mapModel);			
//			System.out.println("componentAlls = " + componentAlls.get(0).getComponent().getName());
			
		}
		
	}
	
	@Test
	public void testAttributeMapReader() throws FileNotFoundException, Exception
	{
		try (XMLMapReader mapReader = new XMLMapReader(new FileInputStream("C:\\temp\\spoon\\ADACS_Properties_GPSFrequencies.xml"))) {
			MapModel mapModel = mapReader.findFields(new FileInputStream("C:\\temp\\spoon\\ADACS_Properties_GPSFrequencies.xml"));
			
			mapModel.getUniqueFields().forEach(f->System.out.println(f));
			
			FileHistoryAll fileHistory = new FileHistoryAll();
			
			Map<String, DataMapper> dataMappers = new HashMap<>();
			
			DataMapper dataMapper = new DataMapper();
			dataMapper.setRootField(true);
			dataMapper.setField("dataroot.Module_ADACS_Properties_GPSFrequencies");
			dataMapper.setEntityClass(AttributeType.class);
			dataMapper.getTransforms().add(StringTransforms.UPPERCASE);
			dataMapper.getTransforms().add(StringTransforms.SPLITLASTUNDERSCORE);
			dataMappers.put("dataroot.Module_ADACS_Properties_GPSFrequencies", dataMapper);
			
			dataMapper = new DataMapper();
			dataMapper.setRootField(false);
			dataMapper.setField("dataroot.Module_ADACS_Properties_GPSFrequencies.ID");
			dataMapper.setEntityClass(AttributeCodePk.class);
			dataMapper.setEntityField("code");
			dataMapper.getTransforms().add(StringTransforms.UPPERCASE);
			dataMappers.put("dataroot.Module_ADACS_Properties_GPSFrequencies.ID", dataMapper);
			
			AttributeMapper attributeMapper = new AttributeMapper(() -> {
				AttributeAll attributeAll = new AttributeAll();
				attributeAll.setAttributeType(new AttributeType());
				return attributeAll;
			}, new FileHistoryAll(), dataMappers, null);
			
			List<AttributeAll> attributeAlls  = attributeMapper.multiMapData(mapModel);			
			System.out.println("attributeAlls = " + attributeAlls.get(0).getAttributeType().getAttributeType());
			
			
		}
		
	}
	
}
