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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.MapField;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.MapModel;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.MappableReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 *
 * @author dshurtleff
 */
public class CopyReader
	extends MappableReader	
{
	private static final Logger log = Logger.getLogger(CopyReader.class.getName());

	private MapModel rootModel;
	private List<MapModel> records = new ArrayList<>();
	private Iterator<MapModel> recordIterator;
	
	public CopyReader(InputStream in)
	{
		super(in);		
	}

	@Override
	public void preProcess()
	{
		rootModel = findFields(in);
		totalRecords = rootModel.getArrayFields().size();
		records.add(rootModel);
		recordIterator = records.iterator();
	}
	
	@Override
	public MapModel nextRecord()
	{		
		if (recordIterator.hasNext()) {
			MapModel mapModel = recordIterator.next();
			currentRecordNumber += mapModel.getArrayFields().size();
			return mapModel;
		} else {
			return null;
		}
	}

	@Override
	public MapModel findFields(InputStream in)
	{	
		MapModel mapModel = new MapModel();
	
		 // the SAXBuilder is the easiest way to create the JDOM2 objects.
	        SAXBuilder jdomBuilder = new SAXBuilder();
  
		try (InputStream xmlIn = in){
			// jdomDocument is the JDOM2 Object
			Document jdomDocument = jdomBuilder.build(xmlIn);
			parseDom(jdomDocument.getRootElement(), mapModel);
				
		} catch (JDOMException | IOException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		} 			

		return mapModel;
	}
	
	private void parseDom(Element root, MapModel mapModel) {
		if (root == null) {
			return;
		}
		
		mapModel.setName(root.getName());			
		for (Attribute attribute : root.getAttributes()) {
			MapField field = new MapField();
			field.setName(attribute.getName());
			field.setValue(attribute.getValue());
			mapModel.getMapFields().add(field);
		}
		
		if (StringProcessor.stringIsNotBlank(root.getText())) {
			MapField field = new MapField();
			field.setName(root.getName());
			field.setValue(root.getText());
			mapModel.getMapFields().add(field);
		}
		for (Element child : root.getChildren()) {				
			if (child.getChildren().isEmpty()) {
				MapField field = new MapField();
				field.setName(child.getName());
				field.setValue(child.getText());
				mapModel.getMapFields().add(field);								
			} else {
				MapModel childMap = new MapModel();
				mapModel.getArrayFields().add(childMap);
				parseDom(child, childMap);			
			}
		}
		
	}
	
}














