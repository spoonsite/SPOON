/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.describe.parser;

import edu.usu.sdl.describe.model.Assertion;
import edu.usu.sdl.describe.model.HandlingAssertion;
import edu.usu.sdl.describe.model.TrustedDataObject;
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
public class TrustedDataObjectConverter
	implements Converter<TrustedDataObject>	
{

	@Override
	public TrustedDataObject read(InputNode node) throws Exception
	{
		TrustedDataObject trustedDataObject = new TrustedDataObject();
		trustedDataObject.setVersion(node.getAttribute("version").getValue());
				
		Strategy strategy = new AnnotationStrategy();
		Serializer serializer = new Persister(strategy);		
		
		InputNode child;
		while( ( child = node.getNext() ) != null )
		{		

			switch(child.getName())
			{
				case "HandlingAssertion":			
					HandlingAssertion handlingAssertion = serializer.read(HandlingAssertion.class, child);
					trustedDataObject.setHandlingAssertion(handlingAssertion);					
					break;
				case "Assertion":
					Assertion assertion = serializer.read(Assertion.class, child);
					trustedDataObject.getAssertions().add(assertion);						
					break;
				case "ReferenceValuePayload":							
					trustedDataObject.setPayloadURI(child.getAttribute("uri").getValue());					
					break;
				default:
					log.log(Level.WARNING, MessageFormat.format("Unknown Element found: {0}", child));
			}			
		}
		return trustedDataObject;
	}

	@Override
	public void write(OutputNode node, TrustedDataObject value) throws Exception
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
}
