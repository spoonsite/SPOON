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

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.WorkPlan;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanComponentType;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStep;
import java.util.ArrayList;
import java.util.List;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class MongoDBUseCase
{

	@Test
	public void mongoTest()
	{
		MongoClient mongoClient = MongoClients.create();

		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		MongoDatabase database = mongoClient.getDatabase("mydb").withCodecRegistry(pojoCodecRegistry);

		// get a handle to the "component" collection
		MongoCollection<Component> collection = database.getCollection("component-test", Component.class);

		// drop all the data in it
		collection.drop();

		Component component = new Component();
		component.setComponentId(StringProcessor.uniqueId());
		component.setName("Test-Mongo");
		component.setDescription("Test Description-jpa");
		component.setComponentType("MYType");
		component.setOrganization("TEST-Mongo");
		component.setApprovalState(ApprovalStatus.APPROVED);
		component.setGuid("5555555");
		component.setLastActivityDts(TimeUtil.currentDate());
		component.setActiveStatus("A");
		component.setCreateUser("ds");
		component.setCreateDts(TimeUtil.currentDate());
		component.setUpdateUser("ds");
		component.setUpdateDts(TimeUtil.currentDate());

		collection.insertOne(component);

		Component myComponent = collection.find().first();
		System.out.println(myComponent.getName());

		//complex model case
		WorkPlan workPlan = new WorkPlan();
		workPlan.setName("Test");
		workPlan.setWorkPlanId(StringProcessor.uniqueId());

		WorkPlanComponentType componentType = new WorkPlanComponentType();
		componentType.setComponentType("MYTEST");
		List<WorkPlanComponentType> types = new ArrayList<>();
		types.add(componentType);
		workPlan.setComponentTypes(types);

		WorkPlanStep workPlanStep = new WorkPlanStep();
		workPlanStep.setName("Step 1");
		workPlanStep.setWorkPlanStepId(StringProcessor.uniqueId());
		workPlanStep.setStepOrder(1);

		List<WorkPlanStep> steps = new ArrayList<>();
		steps.add(workPlanStep);
		workPlan.setSteps(steps);

		MongoCollection<WorkPlan> collectionWorkPlan = database.getCollection("workplan-test", WorkPlan.class);

		collectionWorkPlan.insertOne(workPlan);

		WorkPlan myWorkplan = collectionWorkPlan.find().first();
		if (myWorkplan != null) {
			System.out.println(myWorkplan.getName());
			myWorkplan.getSteps().forEach(s -> {
				System.out.println("Step: " + s.getName());
			});
		}

		// Clean up
		database.drop();

		// release resources
		mongoClient.close();

	}

}
