package edu.usu.sdl.openstorefront.web.test;

import edu.usu.sdl.openstorefront.service.ServiceProxy;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Test
 *
 * @author dshurtleff
 */
public class BaseTestCase
{

	private static final Logger log = Logger.getLogger(BaseTestCase.class.getName());

	protected static final String TEST_USER = "TEST-USER";

	protected boolean success;
	protected StringBuilder failureReason = new StringBuilder();
	protected StringBuilder results = new StringBuilder();
	protected ServiceProxy service = new ServiceProxy();
	protected String description;

	public BaseTestCase()
	{
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void runTest()
	{
		try {
			runInternalTest();
			if (failureReason.length() == 0) {
				success = true;
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "Test " + getDescription() + " Fail Trace: ", e);
			failureReason.append(e);
		}
	}

	protected void runInternalTest()
	{
	}

	public boolean isSuccess()
	{
		return success;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}

	public StringBuilder getFailureReason()
	{
		return failureReason;
	}

	public void setFailureReason(StringBuilder failureReason)
	{
		this.failureReason = failureReason;
	}

	public StringBuilder getResults()
	{
		return results;
	}

	public void setResults(StringBuilder results)
	{
		this.results = results;
	}

}
