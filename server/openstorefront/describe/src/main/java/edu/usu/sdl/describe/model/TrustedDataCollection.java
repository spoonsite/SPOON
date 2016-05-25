package edu.usu.sdl.describe.model;

import edu.usu.sdl.describe.parser.TrustedDataConverter;
import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
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
	
	@Element(name="HandlingAssertion", required = false)
	private HandlingAssertion handlingAssertion;
	
	@Element(name="Assertion", required = false)
	private Assertion assertion;
	
	@Element(name="TrustedDataObject")
	private List<TrustedDataObject> trustedDataObjects =  new ArrayList<>();

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

	public List<TrustedDataObject> getTrustedDataObjects()
	{
		return trustedDataObjects;
	}

	public void setTrustedDataObjects(List<TrustedDataObject> trustedDataObjects)
	{
		this.trustedDataObjects = trustedDataObjects;
	}

}
