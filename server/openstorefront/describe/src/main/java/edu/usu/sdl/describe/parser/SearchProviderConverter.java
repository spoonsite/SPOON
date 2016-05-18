/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.describe.parser;

import edu.usu.sdl.describe.model.GeneralInfo;
import edu.usu.sdl.describe.model.RelatedResource;
import edu.usu.sdl.describe.model.SearchInterface;
import edu.usu.sdl.describe.model.SearchProvider;
import edu.usu.sdl.describe.model.SearchableField;
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
public class SearchProviderConverter
	implements Converter<SearchProvider>
{

	@Override
	public SearchProvider read(InputNode node) throws Exception
	{
		SearchProvider searchProvider = new SearchProvider();
		searchProvider.setClassification(node.getAttribute("classification").getValue());
				
		Strategy strategy = new AnnotationStrategy();
		Serializer serializer = new Persister(strategy);		
		
		InputNode child;
		while( ( child = node.getNext() ) != null )
		{		

			switch(child.getName())
			{
				case "generalInfo":			
					GeneralInfo generalInfo = serializer.read(GeneralInfo.class, child);
					searchProvider.setGeneralInfo(generalInfo);					
					break;
				case "relatedResource":
					RelatedResource relatedResource = serializer.read(RelatedResource.class, child);
					searchProvider.getRelatedResources().add(relatedResource);						
					break;
				case "searchInterface":							
					SearchInterface searchInterface = serializer.read(SearchInterface.class, child);
					searchProvider.getSearchInterfaces().add(searchInterface);															
					break;
				case "searchableFields":							
					SearchableField searchableField = serializer.read(SearchableField.class, child);
					searchProvider.getSearchableFields().add(searchableField);															
					break;					
				default:
					log.log(Level.WARNING, MessageFormat.format("Unknown Element found: {0}", child));
			}			
		}
		return searchProvider;		
	}

	@Override
	public void write(OutputNode node, SearchProvider value) throws Exception
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
}
