package edu.usu.sdl.openstorefront.web.test;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.stripes.util.ResolverUtil;

/**
 * Handles container tests.
 *
 * @author dshurtleff
 */
public class TestSuiteModel
{

	private String name;
	private List<BaseTestCase> tests = new ArrayList<>();
	private long runTimeInMills = 0;

	public TestSuiteModel()
	{
	}

	public TestSuiteModel(String testPackage)
	{
		ResolverUtil resolverUtil = new ResolverUtil();
		resolverUtil.find(new ResolverUtil.IsA(BaseTestCase.class), testPackage);
		for (Object testObject : resolverUtil.getClasses()) {
			Class testClass = (Class) testObject;
			try {
				tests.add((BaseTestCase) testClass.newInstance());
			} catch (InstantiationException | IllegalAccessException ex) {
				throw new OpenStorefrontRuntimeException(ex);
			}
		}
		tests.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_DESCENDING, "description"));
	}

	public String statString()
	{
		StringBuilder sb = new StringBuilder();
		int successCount = 0;
		successCount = tests.stream().filter((test) -> (test.isSuccess())).map((_item) -> 1).reduce(successCount, Integer::sum);
		sb.append("Total: ").append(tests.size()).append(" ");
		sb.append("Success: <span style='font-weight:bold; padding: 5px; border-radius: 2px; 2px; 2px; 2px; background-color: green; color: white;'>").append(successCount).append("</span> ");
		sb.append("Fail: <span style='font-weight:bold; padding: 5px; border-radius: 2px; 2px; 2px; 2px; background-color: red; color: white;'>").append(tests.size() - successCount).append("</span>  ");
		sb.append("Run Time:  ").append(runTimeInMills).append("ms  ");

		return sb.toString();
	}

	public void runAllTests()
	{
		long startTime = System.currentTimeMillis();
		tests.stream().forEach((test) -> {
			test.runTest();
		});
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

}
