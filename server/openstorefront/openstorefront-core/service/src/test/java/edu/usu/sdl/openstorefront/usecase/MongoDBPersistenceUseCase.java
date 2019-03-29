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

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.EmailAddress;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.WorkFlowStepActionOption;
import edu.usu.sdl.openstorefront.core.entity.WorkPlan;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanComponentType;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStep;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStepAction;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStepEvent;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStepRole;
import edu.usu.sdl.openstorefront.security.UserContext;
import edu.usu.sdl.openstorefront.service.MongoPersistenceServiceImpl;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.api.ProxyFactory;
import edu.usu.sdl.openstorefront.service.manager.MongoDBManager;
import edu.usu.sdl.openstorefront.service.repo.MongoQueryUtil;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author dshurtleff
 */
public class MongoDBPersistenceUseCase
{

	@Test
	public void testPersistenceEngine()
	{
		MongoDBManager dbManager = initialize();

		UserContext userContext = new UserContext();
		UserProfile userProfile = new UserProfile();
		userProfile.setUsername("GUEST");
		userContext.setUserProfile(userProfile);
		userContext.setGuest(true);

		MongoQueryUtil queryUtil = new MongoQueryUtil(userContext, dbManager);

		MongoPersistenceServiceImpl persistenceService = new MongoPersistenceServiceImpl(dbManager, queryUtil);
		try {
			generateTestData(persistenceService);
			queryTests(persistenceService);
		} finally {
			cleanup(dbManager);
		}
	}

	private MongoDBManager initialize()
	{
		PropertiesManager mockPropertiesManager = Mockito.mock(PropertiesManager.class);
		Mockito.when(mockPropertiesManager.getValueDefinedDefault(PropertiesManager.KEY_MONGO_CONNECTION_URL)).thenReturn("mongodb://localhost:27017");
		Mockito.when(mockPropertiesManager.getValue(PropertiesManager.KEY_MONGO_DATABASE, MongoDBManager.DEFAULT_DATABASE)).thenReturn("persisttest");

		MongoDBManager dBManager = new MongoDBManager(mockPropertiesManager);
		dBManager.initialize();

		ValidationUtil.setSkipValidationForTesting(true);

		//mock ServiceProxy
		ServiceProxy mockProxy = Mockito.mock(ServiceProxy.class);

		ProxyFactory proxyFactory = (modificationType) -> {
			mockProxy.setModificationType(modificationType);
			return mockProxy;
		};

		ServiceProxy.setProxyFactory(proxyFactory);

		return dBManager;
	}

	private void generateTestData(MongoPersistenceServiceImpl persistenceService)
	{
		List<Component> components = generateComponents();
		components.forEach(component -> {
			persistenceService.persist(component);
		});

		List<AttributeCode> attributes = generateAttributeCodes();
		attributes.forEach(attribute -> {
			persistenceService.persist(attribute);
		});

		List<WorkPlan> workPlans = generateWorkPlan();
		workPlans.forEach(workPlan -> {
			persistenceService.persist(workPlan);
		});

	}

	private List<Component> generateComponents()
	{
		List<Component> components = new ArrayList<>();

		Component component = new Component();
		component.setComponentId(StringProcessor.uniqueId());
		component.setName("A");
		component.setActiveStatus(Component.ACTIVE_STATUS);
		component.setComponentType("DI2E Component");
		component.setApprovalState(ApprovalStatus.APPROVED);
		component.setDescription("This is test A");
		//component.setDataSource("Junk");
		component.setCreateDts(TimeUtil.currentDate());
		component.setCreateUser("Test");
		component.setUpdateDts(TimeUtil.currentDate());
		component.setUpdateUser("Test");

		components.add(component);

		component = new Component();
		component.setComponentId(StringProcessor.uniqueId());
		component.setName("B");
		component.setActiveStatus(Component.ACTIVE_STATUS);
		component.setComponentType("DI2E Component");
		component.setApprovalState(ApprovalStatus.PENDING);
		component.setDescription("This is test B");
		//component.setDataSource("Junk");
		component.setCreateDts(TimeUtil.currentDate());
		component.setCreateUser("Test");
		component.setUpdateDts(TimeUtil.currentDate());
		component.setUpdateUser("Test");

		components.add(component);

		component = new Component();
		component.setComponentId(StringProcessor.uniqueId());
		component.setName("C");
		component.setActiveStatus(Component.INACTIVE_STATUS);
		component.setComponentType("DI2E Component");
		component.setApprovalState(ApprovalStatus.PENDING);
		component.setDescription("This is test C");
		component.setCreateDts(TimeUtil.currentDate());
		component.setCreateUser("Test");
		component.setUpdateDts(TimeUtil.currentDate());
		component.setUpdateUser("Test");

		components.add(component);
		return components;
	}

	private List<AttributeCode> generateAttributeCodes()
	{
		List<AttributeCode> attributeCodes = new ArrayList<>();

		AttributeCode attributeCode = new AttributeCode();
		attributeCode.setActiveStatus(AttributeCode.ACTIVE_STATUS);
		AttributeCodePk attributeCodePk = new AttributeCodePk();
		attributeCodePk.setAttributeCode("APPLE");
		attributeCodePk.setAttributeType("FRUIT");
		attributeCode.setAttributeCodePk(attributeCodePk);
		attributeCode.setLabel("Test A");
		attributeCode.setCreateDts(TimeUtil.currentDate());
		attributeCode.setCreateUser("Test");
		attributeCode.setUpdateDts(TimeUtil.currentDate());
		attributeCode.setUpdateUser("Test");

		attributeCodes.add(attributeCode);

		attributeCode = new AttributeCode();
		attributeCode.setActiveStatus(AttributeCode.ACTIVE_STATUS);
		attributeCodePk = new AttributeCodePk();
		attributeCodePk.setAttributeCode("BANANA");
		attributeCodePk.setAttributeType("FRUIT");
		attributeCode.setAttributeCodePk(attributeCodePk);
		attributeCode.setLabel("Test B");
		attributeCode.setCreateDts(TimeUtil.currentDate());
		attributeCode.setCreateUser("Test");
		attributeCode.setUpdateDts(TimeUtil.currentDate());
		attributeCode.setUpdateUser("Test");

		attributeCodes.add(attributeCode);

		return attributeCodes;
	}

	private List<WorkPlan> generateWorkPlan()
	{
		List<WorkPlan> workPlans = new ArrayList<>();

		WorkPlan workPlan = new WorkPlan();
		workPlan.setName("A");
		workPlan.setActiveStatus(WorkPlan.ACTIVE_STATUS);
		workPlan.setDefaultWorkPlan(Boolean.FALSE);
		workPlan.setCreateDts(TimeUtil.currentDate());
		workPlan.setCreateUser("Test");
		workPlan.setUpdateDts(TimeUtil.currentDate());
		workPlan.setUpdateUser("Test");

		List<WorkPlanComponentType> componentTypes = new ArrayList<>();
		WorkPlanComponentType componentType = new WorkPlanComponentType();
		componentType.setComponentType(ComponentType.COMPONENT);
		componentTypes.add(componentType);

		componentType.setComponentType(ComponentType.ARTICLE);
		componentTypes.add(componentType);
		workPlan.setComponentTypes(componentTypes);

		List<WorkPlanStep> steps = new ArrayList<>();
		WorkPlanStep workPlanStep = new WorkPlanStep();
		workPlanStep.setName("Step 1");
		workPlanStep.setDescription("Check step 1");

		List<WorkPlanStepAction> actions = new ArrayList<>();
		WorkPlanStepAction stepAction = new WorkPlanStepAction();
		stepAction.setActionOrder(1);
		WorkFlowStepActionOption workFlowStepActionOption = new WorkFlowStepActionOption();
		workFlowStepActionOption.setEmailMessage("Test Message");
		workFlowStepActionOption.setEmailSubject("Subject");
		List<EmailAddress> emailAddresses = new ArrayList<>();
		EmailAddress email = new EmailAddress();
		email.setEmail("test@test.com");
		emailAddresses.add(email);
		workFlowStepActionOption.setFixedEmails(emailAddresses);
		stepAction.setActionOption(workFlowStepActionOption);
		actions.add(stepAction);

		workPlanStep.setActions(actions);

		List<WorkPlanStepRole> roles = new ArrayList<>();
		WorkPlanStepRole role = new WorkPlanStepRole();
		role.setSecurityRole("TEST-ROLE");
		roles.add(role);
		workPlanStep.setStepRole(roles);

		List<WorkPlanStepEvent> events = new ArrayList<>();
		WorkPlanStepEvent event = new WorkPlanStepEvent();
		event.setEntityEventType("Event");
		events.add(event);
		workPlanStep.setTriggerEvents(events);

		steps.add(workPlanStep);
		workPlan.setSteps(steps);

		workPlans.add(workPlan);

		return workPlans;
	}

	private void queryTests(MongoPersistenceServiceImpl persistenceService)
	{
		componentQueries(persistenceService);
		attributeQueries(persistenceService);
		//workPlanQueries(persistenceService);
	}

	private void componentQueries(MongoPersistenceServiceImpl persistenceService)
	{
		Component componentExample = new Component();
		componentExample.setActiveStatus(Component.ACTIVE_STATUS);

		List<Component> foundRecords = persistenceService.queryByExample(componentExample);

		//should be  A, B
		foundRecords.forEach(c -> System.out.println("Found: " + c.getName()));

		Component foundById = persistenceService.findById(Component.class, foundRecords.get(0).getComponentId());
		System.out.println("Found By Id: " + foundById.getName());

		//count
		long count = persistenceService.countByExample(componentExample);
		System.out.println("Active Record count: " + count);

		//update
		foundById.setUpdateDts(TimeUtil.currentDate());
		foundById.setUpdateUser("New User");
		foundById.setDescription("Updated record");
		persistenceService.persist(foundById);

		foundById = persistenceService.findById(Component.class, foundRecords.get(0).getComponentId());
		System.out.println("Updated Record " + foundById.getName() + " Description: " + foundById.getDescription());

		//update by example
		Component componentUpdateExample = new Component();
		componentUpdateExample.setDataSource("BulkUpdate");
		persistenceService.updateByExample(Component.class, componentUpdateExample, componentExample);

		foundRecords = persistenceService.queryByExample(componentExample);
		foundRecords.forEach(c -> System.out.println("Found: " + c.getName() + " Datasource: " + c.getDataSource()));

		//delete by example
		persistenceService.deleteByExample(componentExample);

		count = persistenceService.countByExample(componentExample);
		System.out.println("Record count after delete: " + count);
	}

	private void attributeQueries(MongoPersistenceServiceImpl persistenceService)
	{
		AttributeCode example = new AttributeCode();
		AttributeCodePk attributeCodePk = new AttributeCodePk();
		attributeCodePk.setAttributeCode("APPLE");
		attributeCodePk.setAttributeType("FRUIT");
		example.setAttributeCodePk(attributeCodePk);

		List<AttributeCode> attributes = persistenceService.queryByExample(example);
		attributes.forEach(a -> System.out.println("Found: " + a.getLabel()));

	}

	private void workPlanQueries(MongoPersistenceServiceImpl persistenceService)
	{

	}

	private void cleanup(MongoDBManager dbManager)
	{
		dbManager.getConnection().drop();
		dbManager.shutdown();
	}

}
