package edu.usu.sdl.openstorefront.web.test;

import edu.usu.sdl.openstorefront.service.ServiceProxy;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base Test Case for all container tests
 *
 * @author dshurtleff
 */
public abstract class BaseTestCase
{

	private static final Logger log = Logger.getLogger(BaseTestCase.class.getName());

	protected static final String TEST_USER = "TEST-USER";

	protected boolean success;
	protected StringBuffer failureReason = new StringBuffer();
	protected StringBuffer results = new StringBuffer();
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

	protected abstract void runInternalTest();

	public boolean isSuccess()
	{
		return success;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}

	public StringBuffer getFailureReason()
	{
		return failureReason;
	}

	public void setFailureReason(StringBuffer failureReason)
	{
		this.failureReason = failureReason;
	}

	public StringBuffer getResults()
	{
		return results;
	}

	public void setResults(StringBuffer results)
	{
		this.results = results;
	}

}
