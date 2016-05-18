/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.describe.parser;

import edu.usu.sdl.describe.model.GeneralInfo;
import edu.usu.sdl.describe.model.PointOfContact;
import static edu.usu.sdl.describe.parser.TrustedDataConverter.log;
import java.text.MessageFormat;
import java.util.logging.Level;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

/**
 *
 * @author dshurtleff
 */
public class GeneralInfoConverter
	implements Converter<GeneralInfo>
{

	@Override
	public GeneralInfo read(InputNode node) throws Exception
	{
		GeneralInfo generalInfo = new GeneralInfo();
						
		Strategy strategy = new AnnotationStrategy();
		Serializer serializer = new Persister(strategy);		
		
		InputNode child;
		while( ( child = node.getNext() ) != null )
		{		

			switch(child.getName())
			{
				case "identifier":			
					generalInfo.setGuid(child.getAttribute("value").getValue());
					break;
				case "name":
					generalInfo.setName(child.getValue());
					generalInfo.setNameClassification(child.getAttribute("classification").getValue());
					break;
				case "description":							
					generalInfo.setDescription(child.getValue());
					generalInfo.setDescriptionClassification(child.getAttribute("classification").getValue());					
					break;
				case "network":							
					generalInfo.setNetwork(child.getValue());					
					break;					
				case "pointOfContact":							
					PointOfContact contact = serializer.read(PointOfContact.class, child);
					generalInfo.getContacts().add(contact);															
					break;					
				default:
					log.log(Level.WARNING, MessageFormat.format("Unknown Element found: {0}", child));
			}			
		}
		return generalInfo;			
	}

	@Override
	public void write(OutputNode node, GeneralInfo value) throws Exception
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
}
