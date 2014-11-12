/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.usu.sdl.openstorefront.web.action.test;

import edu.usu.sdl.openstorefront.web.test.TestSuiteModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.lang.StringUtils;

/**
 * Lookup Service Test
 *
 * @author dshurtleff
 */
public class ServiceTest
		extends BaseTestAction
{

	private String suite;
	private String test;
	private List<TestSuiteModel> testSuites = new ArrayList();

	@DefaultHandler
	public Resolution testPage()
	{
		testSuites = listOfTestSuites();
		return new ForwardResolution("/WEB-INF/securepages/test/containerTests.jsp");
	}

	@HandlesEvent("RunTest")
	public Resolution runTest()
	{
		testSuites = listOfTestSuites();

		if (StringUtils.isNotBlank(suite)) {
			testSuites = testSuites.stream().filter(item -> item.getName().equals(suite)).collect(Collectors.toList());
			if (StringUtils.isNotBlank(test)) {
				TestSuiteModel specificTestModel = new TestSuiteModel();
				specificTestModel.setName("Single Test");
				for (TestSuiteModel testSuiteModel : testSuites) {
					specificTestModel.getTests().addAll(testSuiteModel.getTests().stream().filter(item -> item.getDescription().equals(test)).collect(Collectors.toList()));
				}
				testSuites = new ArrayList<>();
				testSuites.add(specificTestModel);
			}
		}
		testSuites.forEach(testSuite -> {
			testSuite.runAllTests();
		});
		return printAndSendReport(testSuites);
	}

	private List<TestSuiteModel> listOfTestSuites()
	{
		List<TestSuiteModel> testSuiteModels = new ArrayList<>();

		TestSuiteModel testSuiteModel = new TestSuiteModel("edu.usu.sdl.openstorefront.web.test.system");
		testSuiteModel.setName("System Tests");
		testSuiteModels.add(testSuiteModel);

		testSuiteModel = new TestSuiteModel("edu.usu.sdl.openstorefront.web.test.attribute");
		testSuiteModel.setName("Attribute Tests");
		testSuiteModels.add(testSuiteModel);

		testSuiteModel = new TestSuiteModel("edu.usu.sdl.openstorefront.web.test.component");
		testSuiteModel.setName("Component Tests");
		testSuiteModels.add(testSuiteModel);

		testSuiteModel = new TestSuiteModel("edu.usu.sdl.openstorefront.web.test.lookup");
		testSuiteModel.setName("Lookup Tests");
		testSuiteModels.add(testSuiteModel);

		testSuiteModel = new TestSuiteModel("edu.usu.sdl.openstorefront.web.test.user");
		testSuiteModel.setName("User Tests");
		testSuiteModels.add(testSuiteModel);

		testSuiteModel = new TestSuiteModel("edu.usu.sdl.openstorefront.web.test.search");
		testSuiteModel.setName("Search Tests");
		testSuiteModels.add(testSuiteModel);

		testSuiteModel = new TestSuiteModel("edu.usu.sdl.openstorefront.web.test.integration");
		testSuiteModel.setName("Integration Tests");
		testSuiteModels.add(testSuiteModel);

		return testSuiteModels;
	}

	public String getSuite()
	{
		return suite;
	}

	public void setSuite(String suite)
	{
		this.suite = suite;
	}

	public String getTest()
	{
		return test;
	}

	public void setTest(String test)
	{
		this.test = test;
	}

	public List<TestSuiteModel> getTestSuites()
	{
		return testSuites;
	}

	public void setTestSuites(List<TestSuiteModel> testSuites)
	{
		this.testSuites = testSuites;
	}

}
