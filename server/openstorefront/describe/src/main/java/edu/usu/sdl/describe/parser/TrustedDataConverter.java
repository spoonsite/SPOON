/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.describe.parser;

import edu.usu.sdl.describe.model.Assertion;
import edu.usu.sdl.describe.model.HandlingAssertion;
import edu.usu.sdl.describe.model.TrustedDataCollection;
import edu.usu.sdl.describe.model.TrustedDataObject;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class TrustedDataConverter
	implements Converter<TrustedDataCollection>	
{
	public static final Logger log = Logger.getLogger(TrustedDataConverter.class.getSimpleName());

	@Override
	public TrustedDataCollection read(InputNode node) throws Exception
	{
		TrustedDataCollection trustedDataCollection = new TrustedDataCollection();
		trustedDataCollection.setVersion(node.getAttribute("version").getValue());
	
		Strategy strategy = new AnnotationStrategy();
		Serializer serializer = new Persister(strategy);		
		
		InputNode child;
		while( ( child = node.getNext() ) != null )
		{		

			switch(child.getName())
			{
				case "HandlingAssertion":			
					HandlingAssertion handlingAssertion = serializer.read(HandlingAssertion.class, child);
					trustedDataCollection.setHandlingAssertion(handlingAssertion);					
					break;
				case "Assertion":
					Assertion assertion = serializer.read(Assertion.class, child);
					trustedDataCollection.setAssertion(assertion);						
					break;
				case "TrustedDataObject":					
					TrustedDataObject tdo = serializer.read(TrustedDataObject.class, child);
					trustedDataCollection.getTrustedDataObjects().add(tdo);
					break;
				default:
					log.log(Level.WARNING, MessageFormat.format("Unknown Element found: {0}", child));
			}			
		}
		return trustedDataCollection;
	}

	@Override
	public void write(OutputNode node, TrustedDataCollection value) throws Exception
	{
		node.setName("TrustedDataCollection");
		node.setAttribute("version", value.getVersion());
				
				
		Strategy strategy = new AnnotationStrategy();
		Serializer serializer = new Persister(strategy);	
		
		serializer.write(value.getHandlingAssertion(), node);
		serializer.write(value.getAssertion(), node);
	
		for (TrustedDataObject trustedDataObject : value.getTrustedDataObjects()) {
			serializer.write(trustedDataObject, node);
		}
		
	}
	
}
