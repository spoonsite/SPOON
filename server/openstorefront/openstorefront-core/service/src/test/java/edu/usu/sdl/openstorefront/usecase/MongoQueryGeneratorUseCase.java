/*
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.usecase;

import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.WorkPlan;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanComponentType;
import edu.usu.sdl.openstorefront.service.repo.MongoQueryUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class MongoQueryGeneratorUseCase
{

	@Test
	public void testGenerateFieldsMap()
	{
		MongoQueryUtil queryUtil = new MongoQueryUtil(null, null);

		Map<String, Object> fieldMap = queryUtil.generateFieldMap(getComponentExample());
		System.out.println("Component");
		System.out.println(fieldMap);

		fieldMap = queryUtil.generateFieldMap(getAttributeExample());
		System.out.println("Attribute");
		System.out.println(fieldMap);

		fieldMap = queryUtil.generateFieldMap(getWorkplanExample());
		System.out.println("Attribute");
		System.out.println(fieldMap);
	}

	private Component getComponentExample()
	{
		Component component = new Component();
		component.setActiveStatus(Component.ACTIVE_STATUS);
		component.setName("Test-Mongo");
		return component;
	}

	private AttributeCode getAttributeExample()
	{
		AttributeCode attributeCode = new AttributeCode();
		attributeCode.setActiveStatus(AttributeCode.ACTIVE_STATUS);
		AttributeCodePk attributeCodePk = new AttributeCodePk();
		attributeCodePk.setAttributeCode("Apple");
		attributeCodePk.setAttributeType("FRUIT");
		attributeCode.setAttributeCodePk(attributeCodePk);

		return attributeCode;
	}

	private WorkPlan getWorkplanExample()
	{
		WorkPlan workPlan = new WorkPlan();
		workPlan.setName("Test");

		List<WorkPlanComponentType> componentTypes = new ArrayList<>();
		WorkPlanComponentType componentType = new WorkPlanComponentType();
		componentType.setComponentType(ComponentType.COMPONENT);
		componentTypes.add(componentType);
		workPlan.setComponentTypes(componentTypes);

		return workPlan;
	}

	@Test
	public void testRegex()
	{
		String testMessage = "This is a fruit called an apple";

		Pattern pattern = Pattern.compile("^this", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(testMessage);

		System.out.println(matcher.find());

		pattern = Pattern.compile("aPple$", Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(testMessage);

		System.out.println(matcher.find());

		pattern = Pattern.compile("fruit", Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(testMessage);

		System.out.println(matcher.find());

	}

}
