package edu.usu.sdl.openstorefront.web.test;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.stripes.util.ResolverUtil;

/**
 * Handles container tests.
 *
 * @author dshurtleff
 * @author kbair
 */
public class TestSuiteModel
{

	private static final Logger LOG = Logger.getLogger(TestSuiteModel.class.getName());

	private String name;
	private List<BaseTestCase> tests = new ArrayList<>();
	private long runTimeInMills = 0;

	public TestSuiteModel()
	{
	}

	public TestSuiteModel(String testPackage)
	{
		ResolverUtil resolverUtil = new ResolverUtil();
		try {
			resolverUtil.find(new ResolverUtil.IsA(BaseTestCase.class), testPackage);
		} catch (Exception e) {
			LOG.log(Level.WARNING, "Unable resolve all test cases; may have partial results.");
		}
		for (Object testObject : resolverUtil.getClasses()) {
			Class testClass = (Class) testObject;
			try {
				tests.add((BaseTestCase) testClass.newInstance());
			} catch (InstantiationException | IllegalAccessException ex) {
				throw new OpenStorefrontRuntimeException(ex);
			}
		}
		tests.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, "description"));
	}

	public String statString()
	{
		StringBuilder sb = new StringBuilder();
		int successCount = 0;
		successCount = tests.stream().filter((test) -> (test.isSuccess())).map((_item) -> 1).reduce(successCount, Integer::sum);
		sb.append("Total: ").append(tests.size()).append(" ");
		sb.append("Success: <span class='result passed'>").append(successCount).append("</span> ");
		sb.append("Fail: <span class='result failed'>").append(tests.size() - successCount).append("</span>  ");
		sb.append("Run Time:  ").append(runTimeInMills).append("ms  ");

		return sb.toString();
	}

	public void runAllTests()
	{
		long startTime = System.currentTimeMillis();
		for (BaseTestCase test : tests) {
			test.runTest();
		}
		runTimeInMills = System.currentTimeMillis() - startTime;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<BaseTestCase> getTests()
	{
		return tests;
	}

	public void setTests(List<BaseTestCase> tests)
	{
		this.tests = tests;
	}

	public long getRunTimeInMills()
	{
		return runTimeInMills;
	}
}
