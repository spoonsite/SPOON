package edu.usu.sdl.openstorefront.web.test;

import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.sort.BeanComparator;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
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
		sb.append("Success: <span style='font-weight:bold;color: green'>").append(successCount).append("</span> ");
		sb.append("Fail: <span style='font-weight:bold;color: red'>").append(tests.size() - successCount).append("</span>  ");

		return sb.toString();
	}

	public void runAllTests()
	{
		tests.stream().forEach((test) -> {
			test.runTest();
		});
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
