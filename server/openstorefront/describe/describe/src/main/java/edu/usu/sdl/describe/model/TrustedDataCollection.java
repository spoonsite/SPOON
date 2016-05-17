package edu.usu.sdl.describe.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 *
 * @author dshurtleff
 */
@Root(strict = false)
public class TrustedDataCollection
{
	@Attribute
	private String version;
	
	@Element(name="HandlingAssertion")
	private HandlingAssertion handlingAssertion;
	
	@Element(name="Assertion")
	private Assertion assertion;
	
	@Element(name="TrustedDataObject")
	private TrustedDataObject trustedDataObject;
		
	public TrustedDataCollection()
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

	public Assertion getAssertion()
	{
		return assertion;
	}

	public void setAssertion(Assertion assertion)
	{
		this.assertion = assertion;
	}

}
