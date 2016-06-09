/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.describe.parser;

import edu.usu.sdl.describe.model.Address;
import edu.usu.sdl.describe.model.Conformance;
import edu.usu.sdl.describe.model.Service;
import edu.usu.sdl.describe.model.ServiceType;
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
public class ServiceConverter
	implements Converter<Service>			
{

	@Override
	public Service read(InputNode node) throws Exception
	{
		Service service = new Service();
				
		Strategy strategy = new AnnotationStrategy();
		Serializer serializer = new Persister(strategy);		
		
		InputNode child;
		while( ( child = node.getNext() ) != null )
		{
			switch(child.getName())
			{
				case "name":			
					service.setName(child.getValue());					
					break;
				case "type":
					ServiceType type = serializer.read(ServiceType.class, child);
					service.setServiceType(type);					
					break;
				case "address":							
					Address address = serializer.read(Address.class, child);
					service.getAddresses().add(address);															
					break;
				case "conformance":							
					Conformance conformance = serializer.read(Conformance.class, child);
					service.getConformances().add(conformance);															
					break;					
				default:
					log.log(Level.WARNING, MessageFormat.format("Unknown Element found: {0}", child));
			}			
		}
		return service;		
	}

	@Override
	public void write(OutputNode node, Service value) throws Exception
	{
		node.setName("service");
				
		Strategy strategy = new AnnotationStrategy();
		Serializer serializer = new Persister(strategy);	
		
		node.getChild("name").setValue(Util.blankIfNull(value.getName()));
				
		serializer.write(value.getServiceType(), node);
		
		for (Address address : value.getAddresses()) {
			serializer.write(address, node);
		}		
		
		for (Conformance conformance : value.getConformances()) {
			serializer.write(conformance, node);
		}
	}
	
}
