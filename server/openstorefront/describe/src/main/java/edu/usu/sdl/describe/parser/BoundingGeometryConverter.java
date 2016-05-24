/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.describe.parser;

import edu.usu.sdl.describe.model.BoundingGeometry;
import static edu.usu.sdl.describe.parser.TrustedDataConverter.log;
import java.text.MessageFormat;
import java.util.logging.Level;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

/**
 *
 * @author dshurtleff
 */
public class BoundingGeometryConverter
	implements Converter<BoundingGeometry>
{

	@Override
	public BoundingGeometry read(InputNode node) throws Exception
	{
		BoundingGeometry boundingGeometry = new BoundingGeometry();
		
		InputNode child;
		while( ( child = node.getNext() ) != null )
		{		

			switch(child.getName())
			{
				case "Point":
					InputNode pos = child.getNext("pos");
					if (pos != null) {
						boundingGeometry.getPoints().add(pos.getValue());
					}
					break;
				default:
					log.log(Level.WARNING, MessageFormat.format("Unknown Element found: {0}", child));
			}			
		}		
		
		return boundingGeometry;		
	}

	@Override
	public void write(OutputNode node, BoundingGeometry value) throws Exception
	{
		node.setName("boundingGeometry");
				
		for (String point : value.getPoints()) {
			node.getChild("Point").getChild("pos").setValue(point);
		}
		
	}
	
}
