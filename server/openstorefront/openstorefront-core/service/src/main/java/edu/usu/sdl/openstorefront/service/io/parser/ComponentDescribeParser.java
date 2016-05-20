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
package edu.usu.sdl.openstorefront.service.io.parser;

import edu.usu.sdl.describe.model.Assertion;
import edu.usu.sdl.describe.model.PointOfContact;
import edu.usu.sdl.describe.model.SearchProvider;
import edu.usu.sdl.describe.model.StructuredStatement;
import edu.usu.sdl.describe.model.TrustedDataCollection;
import edu.usu.sdl.describe.model.TrustedDataObject;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentContact;
import edu.usu.sdl.openstorefront.core.entity.ContactType;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.service.io.reader.GenericReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * Parses a describe record
 * @author dshurtleff
 */
public class ComponentDescribeParser
	extends BaseComponentParser	
{
	private static final Logger log = Logger.getLogger(ComponentDescribeParser.class.getName());
	
	@Override
	public String checkFormat(String mimeType, InputStream input)
	{
		String errorMessage = null;
		Serializer serializer = new Persister();
		try {
			serializer.read(TrustedDataCollection.class, input);
		} catch (Exception ex) {
			log.log(Level.FINEST, "Unable to read file", ex);
			errorMessage = "Unable to read format.  The file must be an XML Describe Record.";
		}
		return errorMessage;
	}

	@Override
	protected GenericReader getReader(InputStream in)
	{
		return new GenericReader<TrustedDataCollection>(in)
		{
			private List<TrustedDataCollection> collections = new ArrayList<>();

			@Override
			public void preProcess()
			{
				Serializer serializer = new Persister();
				try {
					TrustedDataCollection dataCollection = serializer.read(TrustedDataCollection.class, in);
					collections.add(dataCollection);
					totalRecords = 1;
				} catch (Exception ex) {
					throw new OpenStorefrontRuntimeException("Unable to read xml record.", "Check data", ex);
				}
			}

			@Override
			public TrustedDataCollection nextRecord()
			{
				if (collections.size() > 0) {
					currentRecordNumber++;
					return collections.remove(0);
				} else {
					return null;
				}
			}

		};
	}

	@Override
	protected <T> Object parseRecord(T record)
	{
		TrustedDataCollection dataCollection = (TrustedDataCollection) record;
		
		List<ComponentAll> allData = new ArrayList<>();		
		for (TrustedDataObject dataObject : dataCollection.getTrustedDataObjects()) {
			allData.addAll(processAssertions(dataObject.getAssertions()));
		}
		
		for (ComponentAll componentAll : allData) {
			if (validateRecord(componentAll)) {
				addRecordToStorage(componentAll);
			}
		}		

		return null;
	}
	
	private List<ComponentAll> processAssertions(List<Assertion> assertions) 
	{
		List<ComponentAll> componentAlls = new ArrayList<>();
		
		StructuredStatement resourceCollection = new StructuredStatement();
		
		for (Assertion assertion : assertions)		
		{
			if (assertion.getStructuredStatement().getSearchProvider() != null)
			{
				ComponentAll componentAll= defaultComponentAll();
				Component component = componentAll.getComponent();	
				
				SearchProvider searchProvider = assertion.getStructuredStatement().getSearchProvider();
				
				component.setName(searchProvider.getGeneralInfo().getName());
				component.setDescription(searchProvider.getGeneralInfo().getDescription());
				component.setGuid(searchProvider.getGeneralInfo().getGuid());
				component.setSecurityMarkingType(getSecurityMarking(searchProvider.getGeneralInfo().getDescriptionClassification()));
				
				for (PointOfContact contact : searchProvider.getGeneralInfo().getContacts()) {					
					ComponentContact componentContact = new ComponentContact();
					if (contact.getOrganization() != null) {
						component.setOrganization(contact.getOrganization().getName());
						
						componentContact.setContactType(ContactType.GOVERNMENT);
						
						
					}
					
					
					
				}
				
				//add attributes
				
				//add resources
				
				
				
				
				componentAlls.add(componentAll);
				
			} else if (assertion.getStructuredStatement().getResource() != null) {
				resourceCollection.setResource(assertion.getStructuredStatement().getResource());				
			} else if (assertion.getStructuredStatement().getContentCollection() != null) { 
				resourceCollection.setContentCollection(assertion.getStructuredStatement().getContentCollection());	
			}
		}		
		
		if (resourceCollection.getResource() != null &&
			resourceCollection.getContentCollection() != null)
		{
			ComponentAll componentAll= defaultComponentAll();
			Component component = componentAll.getComponent();	
		
			//combine Resource and contentCollection into one record
			
			
			componentAlls.add(componentAll);
		}
				
		return componentAlls;		
	} 
	

	

}
