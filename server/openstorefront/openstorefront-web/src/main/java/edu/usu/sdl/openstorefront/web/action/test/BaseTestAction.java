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
			int failTestCount = 0;
			int testCount = 0;
			StringBuilder testCase = new StringBuilder();
			for (BaseTestCase test : testSuiteModel.getTests()) {
				testCount++;
				testCase.append("<div class='test-case ").append(test.isSuccess() ? "passed" : "failed").append("'>");
				String passed = "<span class='result passed'> PASSED </span>";
				if (test.isSuccess() == false) {
					passed = "<span class='result failed'> FAILED </span>";
				}
				
				testCase.append("<div class='heading'>Test: <span class='description'>").append(test.getDescription()).append("</span>").append(passed).append("</div>");

				//results or failure
				testCase.append("<div class='output'>Output: <br><br>");
				testCase.append(test.getResults());
				if (test.isSuccess() == false) {
					testCase.append("<br>Failure Reason: <br>");
					testCase.append(test.getFailureReason());
					failTestCount++;
				}
				testCase.append("</div></div>");
			}

			output.append("<div class='test-suite ").append(failTestCount == 0 ? "passed" : (failTestCount == testCount) ? "failed" : "").append("'>");
			output.append("<h2>").append(testSuiteModel.getName()).append("</h2>");
			output.append("<div class='test-list'>");
			output.append(testCase);
			output.append("<div class='test-suite-stat'>").append(testSuiteModel.statString()).append("</div></div></div>");
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
