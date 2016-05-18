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
package edu.usu.sdl.describe.model;

import edu.usu.sdl.describe.parser.TrustedDataObjectConverter;
import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

/**
 *
 * @author dshurtleff
 */

@Root(strict = false)
@Convert(TrustedDataObjectConverter.class)
public class TrustedDataObject
{
	@Attribute
	private String version;
	
	@Element(name="HandlingAssertion")			
	private HandlingAssertion handlingAssertion;

	private List<Assertion> assertions = new ArrayList<>();
	
	@Attribute(name="uri") 
	@Path("ReferenceValuePayload")		
	private String payloadURI;

	public TrustedDataObject()
	{
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public HandlingAssertion getHandlingAssertion()
	{
		return handlingAssertion;
	}

	public void setHandlingAssertion(HandlingAssertion handlingAssertion)
	{
		this.handlingAssertion = handlingAssertion;
	}

	public String getPayloadURI()
	{
		return payloadURI;
	}

	public void setPayloadURI(String payloadURI)
	{
		this.payloadURI = payloadURI;
	}

	public List<Assertion> getAssertions()
	{
		return assertions;
	}

	public void setAssertions(List<Assertion> assertions)
	{
		this.assertions = assertions;
	}

}
