/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.describe.parser;

import edu.usu.sdl.describe.model.SubjectCoverage;
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
public class SubjectCoverageConverter
	implements Converter<SubjectCoverage>
{

	@Override
	public SubjectCoverage read(InputNode node) throws Exception
	{
		SubjectCoverage subjectCoverage = new SubjectCoverage();
		
		InputNode child;
		while( ( child = node.getNext() ) != null )
		{		

			switch(child.getName())
			{
				case "keyword":			
					subjectCoverage.getKeywords().add(child.getAttribute("value").getValue());					
					break;
				default:
					log.log(Level.WARNING, MessageFormat.format("Unknown Element found: {0}", child));
			}			
		}
		
		return subjectCoverage;
	}

	@Override
	public void write(OutputNode node, SubjectCoverage value) throws Exception
	{
		node.setName("subjectCoverage");
				
		for (String keyword : value.getKeywords()) {
			node.getChild("keyword").setAttribute("value", Util.blankIfNull(keyword));
		}
	}
	
}
