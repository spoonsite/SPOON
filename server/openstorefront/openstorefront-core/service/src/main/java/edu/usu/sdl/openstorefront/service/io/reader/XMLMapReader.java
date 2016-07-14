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
package edu.usu.sdl.openstorefront.service.io.reader;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.io.mapper.MapModel;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.lang.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 *
 * @author dshurtleff
 */
public class XMLMapReader
	extends MappableReader	
{
	private static final Logger log = Logger.getLogger(XMLMapReader.class.getName());

	private XMLEventReader reader;
	
	public XMLMapReader(InputStream in)
	{
		super(in);
		initReader(in);
	}

	private void initReader(InputStream in) 
	{
		XMLInputFactory factory = XMLInputFactory.newInstance();
		try {			
			reader = factory.createXMLEventReader(in);
		} catch (XMLStreamException ex) {
			throw new OpenStorefrontRuntimeException("Unable to create Reader", ex);
		}
	}
	
	@Override
	public MapModel nextRecord()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public MapModel findFields(InputStream in)
	{	
		MapModel mapModel = new MapModel();
		
//		try {
//			initReader(in);
			
		//	Document doc = Jsoup.parse(in, "UTF-8", null, Parser.xmlParser());
//			doc.
			
//			Deque<MapModel> mapStack = new ArrayDeque<>();
//			mapStack.push(mapModel);
//			
//			boolean fieldTag = false;
//			StartElement currentElement = null;
//			while (reader.hasNext()) {
//				try {
//					XMLEvent event = reader.nextEvent();
//					
//					switch (event.getEventType()) {
//						case XMLEvent.START_ELEMENT:	
//						{
//							StartElement startElement = event.asStartElement();
//										
//							if ("Attachments".equals(startElement.getName().toString())) {
//								System.out.println("attachment");
//							}
//							
//							MapModel elementMapModel = new MapModel();
//							
//							MapModel currentMapModel = mapStack.peek();
//							currentMapModel.setName(startElement.getName().toString());							
//							currentMapModel.getArrayFields().add(elementMapModel);
//														
//							mapStack.push(elementMapModel);							
//							currentElement = event.asStartElement();
//						}
//							break;
//						case XMLEvent.CHARACTERS:							
//						{
//							if (currentElement != null) {
//															
//								if (event.asCharacters().isWhiteSpace() == false) {
//									String data = event.asCharacters().getData();	
//									MapField mapField = new MapField();
//									mapField.setName(currentElement.getName().toString());
//									mapField.setValue(data);	
//									
//									MapModel topValueMap = mapStack.pop();
//									MapModel elementMapModel = mapStack.pop();									
//									MapModel parentMapModel = mapStack.peek();
//									parentMapModel.getMapFields().add(mapField);
//									
//									elementMapModel.getArrayFields().clear();
//									mapStack.push(elementMapModel);
//									fieldTag = true;
//								}	
//							}
//						}	
//							break;
//						case XMLEvent.END_ELEMENT:
//						{
//							if (!fieldTag) {
//								EndElement endElement = event.asEndElement();
//
//								//MapModel bottom = mapStack.pop();	
//								//search
//								int popCount = 0;
//								for (MapModel mapModelOnStack : mapStack) {
//									popCount++;
//									if (endElement.getName().toString().equals(mapModelOnStack.getName())) {
//										break;
//									}									
//								}
//								for (int i = 0; i < popCount; i++) {
//									mapStack.pop();
//								}
//							} else {
//								fieldTag = false;
//							}
//						}
//							break;
//					}
//					
//				} catch (XMLStreamException ex) {
//					log.log(Level.SEVERE, null, ex);
//				}
//			}
//		}  finally {
//			if (in != null) {
//				try {
//					in.close();
//				} catch (IOException ex) {
//					log.log(Level.WARNING, "Failed to close input");
//				}
//			}
//		}
//		
//		//collapse stack
		
	
		 // the SAXBuilder is the easiest way to create the JDOM2 objects.
	        SAXBuilder jdomBuilder = new SAXBuilder();
  
		try (InputStream xmlIn = in){
			// jdomDocument is the JDOM2 Object
			Document jdomDocument = jdomBuilder.build(xmlIn);
			walkDom(jdomDocument.getRootElement(), mapModel);
				
			
		} catch (JDOMException ex) {
			Logger.getLogger(XMLMapReader.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(XMLMapReader.class.getName()).log(Level.SEVERE, null, ex);
		} 
			
  

		return mapModel;
	}
	
	private void walkDom(Element root, MapModel mapModel) {
		if (root == null) {
			return;
		}
		
		System.out.println(root.getName());
		if (StringUtils.isNotBlank(root.getText())) {
			System.out.println(root.getText());
		}
		for (Element child : root.getChildren()) {						
			walkDom(child, mapModel);			
		}
		
	}
	
}
