package edu.usu.sdl.describe.model;

import edu.usu.sdl.describe.parser.TrustedDataConverter;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

/**
 *
 * @author dshurtleff
 */
@Root(strict = false)
@Convert(TrustedDataConverter.class)
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
		
	@Namespace(reference = "http://www.w3.org/2001/XMLSchema-instance", prefix = "tdf")
	@Element()
	private TrustedDataObject tdfTrustedDataObject;
	
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

	public TrustedDataObject getTrustedDataObject()
	{
		return trustedDataObject;
	}

	public void setTrustedDataObject(TrustedDataObject trustedDataObject)
	{
		this.trustedDataObject = trustedDataObject;
	}

	public TrustedDataObject getTdfTrustedDataObject()
	{
		return tdfTrustedDataObject;
	}

	public void setTdfTrustedDataObject(TrustedDataObject tdfTrustedDataObject)
	{
		this.tdfTrustedDataObject = tdfTrustedDataObject;
	}

}
