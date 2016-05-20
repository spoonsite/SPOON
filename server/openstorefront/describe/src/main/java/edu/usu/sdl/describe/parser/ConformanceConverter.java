/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.describe.parser;

import edu.usu.sdl.describe.model.Conformance;
import edu.usu.sdl.describe.model.RelatedResource;
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
public class ConformanceConverter
	implements Converter<Conformance>	
{

	@Override
	public Conformance read(InputNode node) throws Exception
	{
		Conformance conformance = new Conformance();
		conformance.setId(node.getAttribute("id").getValue());
				
		Strategy strategy = new AnnotationStrategy();
		Serializer serializer = new Persister(strategy);		
		
		InputNode child;
		while( ( child = node.getNext() ) != null )
		{		

			switch(child.getName())
			{
				case "name":			
					conformance.setName(child.getValue());					
					break;
				case "relatedResource":
					RelatedResource relatedResource = serializer.read(RelatedResource.class, child);
					conformance.getRelatedResources().add(relatedResource);						
					break;
				default:
					log.log(Level.WARNING, MessageFormat.format("Unknown Element found: {0}", child));
			}			
		}
		return conformance;		
	}

	@Override
	public void write(OutputNode node, Conformance value) throws Exception
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
}
