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
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
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

		//attributes
		//workplans
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

		//Three components
		return components;
	}

	private void queryTests(MongoPersistenceServiceImpl persistenceService)
	{
		Component componentExample = new Component();
		componentExample.setActiveStatus(Component.ACTIVE_STATUS);

		List<Component> foundRecords = persistenceService.queryByExample(componentExample);

		//should be  A, B
		foundRecords.forEach(c -> System.out.println("Found: " + c.getName()));

	}

	private void cleanup(MongoDBManager dbManager)
	{
		dbManager.getConnection().drop();
		dbManager.shutdown();
	}

}
