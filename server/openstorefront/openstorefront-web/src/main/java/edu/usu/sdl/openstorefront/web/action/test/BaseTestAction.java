package edu.usu.sdl.openstorefront.web.action.test;

import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.web.action.BaseAction;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import edu.usu.sdl.openstorefront.web.test.TestSuiteModel;
import java.util.List;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;

/**
 * This base class of Service Container tests
 *
 * @author dshurtleff
 */
public abstract class BaseTestAction
		extends BaseAction
{

	private boolean summary;
	private boolean useRest;
	private String user;

	protected ServiceProxy testServiceProxy()
	{
		return new ServiceProxy();
	}

	protected Resolution printAndSendReport(List<TestSuiteModel> testSuiteModels)
	{
		StringBuilder output = printReport(testSuiteModels);
		return sendReport(output.toString());
	}

	protected StringBuilder printReport(List<TestSuiteModel> testSuiteModels)
	{
		StringBuilder output = new StringBuilder();
		for (TestSuiteModel testSuiteModel : testSuiteModels) {
			output.append(printReport(testSuiteModel));
		}
		return output;
	}

	protected StringBuilder printReport(TestSuiteModel testSuiteModel)
	{
		StringBuilder output = new StringBuilder();
		if (summary) {
			for (BaseTestCase test : testSuiteModel.getTests()) {
				String passed = "<span style='color: green'> PASSED </span>";
				if (test.isSuccess() == false) {
					passed = "<span style='color: red'> FAILED </span>";
				}

				output.append("Test: <b>").append(test.getDescription()).append("</b>...").append(passed).append(" <br>");
			}
		} else {
			output.append("<h2>").append(testSuiteModel.getName()).append("</h2>");
			output.append("<hr>");

			for (BaseTestCase test : testSuiteModel.getTests()) {
				String passed = "<span style='padding: 5px; border-radius: 2px; 2px; 2px; 2px; background-color: green; color: white; font-weight: bold; font-size: 16px'> PASSED </span>";
				if (test.isSuccess() == false) {
					passed = "<span style='padding: 5px; border-radius: 2px; 2px; 2px; 2px; background-color: red; color: white; font-weight: bold; font-size: 16px> FAILED </span>";
				}
				output.append("Test: <b>").append(test.getDescription()).append("</b>...").append(passed).append("<br>");

				//results or failure
				output.append("<div style='font-size: 9px; color: grey; border: 1px solid grey;'>Output: <br><br>");
				output.append(test.getResults());
				if (test.isSuccess() == false) {
					output.append("<br>Failure Reason: <br>");
					output.append(test.getFailureReason());
				}
				output.append("</div>").append("<br>");
			}

			output.append("<br><br>").append(testSuiteModel.statString()).append("<br>");
			output.append("<hr>");
		}
		return output;
	}

	protected Resolution sendReport(String reportData)
	{
		return new StreamingResolution("text/html", reportData);
	}

	public boolean getSummary()
	{
		return summary;
	}

	public void setSummary(boolean summary)
	{
		this.summary = summary;
	}

	public boolean isUseRest()
	{
		return useRest;
	}

	public void setUseRest(boolean useRest)
	{
		this.useRest = useRest;
	}

	public String getUser()
	{
		return user;
	}

	public void setUser(String user)
	{
		this.user = user;
	}

}
