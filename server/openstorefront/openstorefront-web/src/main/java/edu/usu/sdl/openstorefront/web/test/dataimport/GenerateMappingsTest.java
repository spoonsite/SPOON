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
package edu.usu.sdl.openstorefront.web.test.dataimport;

import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentMetadata;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.DataMapTransform;
import edu.usu.sdl.openstorefront.core.entity.FileAttributeCodeXrefMap;
import edu.usu.sdl.openstorefront.core.entity.FileAttributeMap;
import edu.usu.sdl.openstorefront.core.entity.FileAttributeTypeXrefMap;
import edu.usu.sdl.openstorefront.core.entity.FileDataMap;
import edu.usu.sdl.openstorefront.core.entity.FileDataMapField;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.model.DataMapModel;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.FieldDefinition;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.MapModel;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.StringTransforms;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.XMLMapReader;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class GenerateMappingsTest
	extends BaseTestCase
{

	public GenerateMappingsTest()
	{
		this.description = "Generate Mappings";
	}
	
	@Override
	protected void runInternalTest()
	{
		//read file
		String fileNameWithOutExt = "ADACS_Equipment_Actuators_CMGs";
		try (XMLMapReader mapReader = new XMLMapReader(new FileInputStream("C:\\temp\\spoon\\ADACS_Equipment_Actuators_CMGs.xml"))) {
			mapReader.preProcess();
			MapModel mapModel = mapReader.nextRecord();
			
			List<FieldDefinition> fields = mapModel.getUniqueFields();

			Set<String> unMappedFieldSet = new HashSet<>();
			for (FieldDefinition fieldDefinition : fields) {
				unMappedFieldSet.add(fieldDefinition.getField());
			}
						
			List<DataMapTransform> standardPathName = new ArrayList<>();			
			for (String transform : Arrays.asList("TRANSLATEESCAPES", "REMOVEUNDERSCORE", "SPLITLASTDOT"))
			{
				DataMapTransform dataMapTransform = new DataMapTransform();
				dataMapTransform.setTransform(transform);
				standardPathName.add(dataMapTransform);
			}
			
			List<DataMapTransform> htmlEscapes = new ArrayList<>();			
			for (String transform : Arrays.asList("TRANSLATEHTMLESCAPES"))
			{
				DataMapTransform dataMapTransform = new DataMapTransform();
				dataMapTransform.setTransform(transform);
				htmlEscapes.add(dataMapTransform);
			}			
			
			
			DataMapModel dataMapModel = new DataMapModel();
			FileDataMap fileDataMap = new FileDataMap();
			FileAttributeMap fileAttributeMap = new FileAttributeMap();
			dataMapModel.setFileAttributeMap(fileAttributeMap);
			dataMapModel.setFileDataMap(fileDataMap);
			
			fileDataMap.setFileFormat("SPOONCMP");
			fileDataMap.setName(fileNameWithOutExt);
			fileDataMap.setDataMapFields(new ArrayList<>());
									
			//set maincomp
			FileDataMapField fileDataMapField = new FileDataMapField();
			fileDataMapField.setEntityClass(ComponentAll.class.getName());
			fileDataMapField.setField("dataroot.Module_" + fileNameWithOutExt);
			fileDataMapField.setEntityField("component");
			fileDataMap.getDataMapFields().add(fileDataMapField);
			unMappedFieldSet.remove(fileDataMapField.getField());					
			
			fileDataMapField = new FileDataMapField();
			fileDataMapField.setEntityClass(Component.class.getName());
			fileDataMapField.setField("dataroot.Module_" + fileNameWithOutExt + ".Model");
			fileDataMapField.setEntityField("name");
			fileDataMap.getDataMapFields().add(fileDataMapField);
			unMappedFieldSet.remove(fileDataMapField.getField());

			fileDataMapField = new FileDataMapField();
			fileDataMapField.setEntityClass(Component.class.getName());
			fileDataMapField.setField("dataroot.Module_" + fileNameWithOutExt + ".ID");
			fileDataMapField.setEntityField("guid");
			fileDataMap.getDataMapFields().add(fileDataMapField);
			unMappedFieldSet.remove(fileDataMapField.getField());
			
			fileDataMapField = new FileDataMapField();
			fileDataMapField.setEntityClass(Component.class.getName());
			fileDataMapField.setField("dataroot.Module_" + fileNameWithOutExt + ".Other_x0020_Comments");
			fileDataMapField.setEntityField("description");
			fileDataMap.getDataMapFields().add(fileDataMapField);
			unMappedFieldSet.remove(fileDataMapField.getField());			
			
			//Map what is known
			fileDataMapField = new FileDataMapField();
			fileDataMapField.setEntityClass(ComponentAttributePk.class.getName());
			fileDataMapField.setField("dataroot.Module_" + fileNameWithOutExt + ".Data_x0020_Sensitivity");
			fileDataMapField.setEntityField("attributeCode");
			fileDataMapField.setPathToEnityField("attributeType");
			fileDataMapField.setPathTransforms(standardPathName);	
			fileDataMap.getDataMapFields().add(fileDataMapField);
			unMappedFieldSet.remove(fileDataMapField.getField());

			fileDataMapField = new FileDataMapField();
			fileDataMapField.setEntityClass(ComponentAttributePk.class.getName());
			fileDataMapField.setField("dataroot.Module_" + fileNameWithOutExt + ".Geometry");
			fileDataMapField.setEntityField("attributeCode");
			fileDataMapField.setPathToEnityField("attributeType");
			fileDataMapField.setPathTransforms(standardPathName);
			fileDataMap.getDataMapFields().add(fileDataMapField);
			unMappedFieldSet.remove(fileDataMapField.getField());			
			
			fileDataMapField = new FileDataMapField();
			fileDataMapField.setEntityClass(ComponentAttributePk.class.getName());
			fileDataMapField.setField("dataroot.Module_" + fileNameWithOutExt + ".Production_x0020_Status");
			fileDataMapField.setEntityField("attributeCode");
			fileDataMapField.setPathToEnityField("attributeType");
			fileDataMapField.setPathTransforms(standardPathName);
			fileDataMap.getDataMapFields().add(fileDataMapField);
			unMappedFieldSet.remove(fileDataMapField.getField());				
			
			
			fileDataMapField = new FileDataMapField();
			fileDataMapField.setEntityClass(ComponentResource.class.getName());
			fileDataMapField.setField("dataroot.Module_" + fileNameWithOutExt + ".Data_x0020_Origin");
			fileDataMapField.setEntityField("link");
			fileDataMapField.setTransforms(htmlEscapes);			
			fileDataMapField.setPathToEnityField("description");
			fileDataMapField.setPathTransforms(standardPathName);
			fileDataMap.getDataMapFields().add(fileDataMapField);
			unMappedFieldSet.remove(fileDataMapField.getField());			
								
			//Attachments
			fileDataMapField = new FileDataMapField();
			fileDataMapField.setEntityClass(ComponentResource.class.getName());
			fileDataMapField.setField("dataroot.Module_" + fileNameWithOutExt + ".Attachments.FileName");
			fileDataMapField.setEntityField("originalName");
			fileDataMap.getDataMapFields().add(fileDataMapField);
			unMappedFieldSet.remove(fileDataMapField.getField());	
			
			fileDataMapField = new FileDataMapField();
			fileDataMapField.setEntityClass(ComponentResource.class.getName());
			fileDataMapField.setField("dataroot.Module_" + fileNameWithOutExt + ".Attachments.FileType");
			fileDataMapField.setEntityField("mimeType");
			fileDataMap.getDataMapFields().add(fileDataMapField);
			unMappedFieldSet.remove(fileDataMapField.getField());

			fileDataMapField = new FileDataMapField();
			fileDataMapField.setEntityClass(ComponentResource.class.getName());
			fileDataMapField.setField("dataroot.Module_" + fileNameWithOutExt + ".Attachments.FileData");
			fileDataMapField.setEntityField("fileName");
			fileDataMap.getDataMapFields().add(fileDataMapField);
			unMappedFieldSet.remove(fileDataMapField.getField());
			
			fileDataMapField = new FileDataMapField();
			fileDataMapField.setEntityClass(ComponentResource.class.getName());
			fileDataMapField.setField("dataroot.Module_" + fileNameWithOutExt + ".Attachments");
			fileDataMapField.setEntityField("mimeType");
			fileDataMap.getDataMapFields().add(fileDataMapField);
			unMappedFieldSet.remove(fileDataMapField.getField());			
			
			
			//Add every thing else as metadata
			//Skip Item_x0020_Type, Path, URL_x0020_Path
			Set<String> skipPaths = new HashSet<>(Arrays.asList("Item_x0020_Type", "Path", "URL_x0020_Path"));
			for (String path : unMappedFieldSet) {
				String pathEnd = StringTransforms.SPLITLASTDOT.transform(path);
				if (skipPaths.contains(pathEnd) == false) {				
					fileDataMapField = new FileDataMapField();
					fileDataMapField.setEntityClass(ComponentMetadata.class.getName());
					fileDataMapField.setField(path);
					fileDataMapField.setEntityField("value");
					fileDataMapField.setPathToEnityField("label");
					fileDataMapField.setPathTransforms(standardPathName);
					fileDataMap.getDataMapFields().add(fileDataMapField);
				}
			}	
			
			fileAttributeMap.setAddMissingAttributeTypeFlg(Boolean.TRUE);
			fileAttributeMap.setAttributeTypeXrefMap(new ArrayList<>());			
			for (String type : Arrays.asList("DATASENSITIVITY", "GEOMETRY", "PRODSTATUS")) {
				FileAttributeTypeXrefMap fileAttributeTypeXrefMap = new FileAttributeTypeXrefMap();
				fileAttributeTypeXrefMap.setAttributeType(type);
				fileAttributeTypeXrefMap.setAddMissingCode(Boolean.TRUE);
				fileAttributeTypeXrefMap.setAttributeCodeXrefMap(new ArrayList<>());
								
				if ("DATASENSITIVITY".equals(type)) {				
					fileAttributeTypeXrefMap.setExternalType("Data Sensitivity");
					
					FileAttributeCodeXrefMap fileAttributeCodeXrefMap = new FileAttributeCodeXrefMap();
					fileAttributeCodeXrefMap.setAttributeCode("ITAR");
					fileAttributeCodeXrefMap.setExternalCode("ITAR Restricted");
					fileAttributeTypeXrefMap.getAttributeCodeXrefMap().add(fileAttributeCodeXrefMap);
					
					fileAttributeCodeXrefMap = new FileAttributeCodeXrefMap();
					fileAttributeCodeXrefMap.setAttributeCode("PUBLIC");
					fileAttributeCodeXrefMap.setExternalCode("Public");
					fileAttributeTypeXrefMap.getAttributeCodeXrefMap().add(fileAttributeCodeXrefMap);					
					
				} else if ("GEOMETRY".equals(type)) {	
					fileAttributeTypeXrefMap.setExternalType("Geometry");
					
					FileAttributeCodeXrefMap fileAttributeCodeXrefMap = new FileAttributeCodeXrefMap();
					fileAttributeCodeXrefMap.setAttributeCode("CYL");
					fileAttributeCodeXrefMap.setExternalCode("Geometry");
					fileAttributeTypeXrefMap.getAttributeCodeXrefMap().add(fileAttributeCodeXrefMap);
					
					fileAttributeCodeXrefMap = new FileAttributeCodeXrefMap();
					fileAttributeCodeXrefMap.setAttributeCode("RECT");
					fileAttributeCodeXrefMap.setExternalCode("Rectangular");
					fileAttributeTypeXrefMap.getAttributeCodeXrefMap().add(fileAttributeCodeXrefMap);							
					
				} else if ("PRODSTATUS".equals(type)) {	
					fileAttributeTypeXrefMap.setExternalType("Production Status");
				
					FileAttributeCodeXrefMap fileAttributeCodeXrefMap = new FileAttributeCodeXrefMap();
					fileAttributeCodeXrefMap.setAttributeCode("INDEVELOPMENT");
					fileAttributeCodeXrefMap.setExternalCode("Geometry");
					fileAttributeTypeXrefMap.getAttributeCodeXrefMap().add(fileAttributeCodeXrefMap);
					
					fileAttributeCodeXrefMap = new FileAttributeCodeXrefMap();
					fileAttributeCodeXrefMap.setAttributeCode("RECT");
					fileAttributeCodeXrefMap.setExternalCode("Rectangular");
					fileAttributeTypeXrefMap.getAttributeCodeXrefMap().add(fileAttributeCodeXrefMap);						
					
				}
				fileAttributeMap.getAttributeTypeXrefMap().add(fileAttributeTypeXrefMap);
			}	
						
			//write out mapping			
			String outFileName = "C:\\temp\\spoon\\" + fileNameWithOutExt + ".json";
			StringProcessor.defaultObjectMapper().writeValue(new File(outFileName), dataMapModel);
			results.append("Wrote file: " + outFileName);
						
		} catch (Exception ex) {
			Logger.getLogger(GenerateMappingsTest.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		
		
	}
	
}
