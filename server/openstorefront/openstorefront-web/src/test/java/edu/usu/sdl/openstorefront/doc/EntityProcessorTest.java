/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.doc;

import edu.usu.sdl.openstorefront.core.entity.BaseEntity;
import edu.usu.sdl.openstorefront.doc.model.EntityDocModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.stripes.util.ResolverUtil;
import org.junit.Test;

/**
 * Test processing Entities
 *
 * @author dshurtleff
 */
public class EntityProcessorTest
{

	@Test
	public void testProcessRestClass()
	{

		System.out.println("Checking Entities");
		ResolverUtil resolverUtil = new ResolverUtil();
		resolverUtil.find(new ResolverUtil.IsA(BaseEntity.class), "edu.usu.sdl.openstorefront.core.entity");

		Map<String, Class> classMap = new HashMap<>();
		List<Class> classList = new ArrayList<>();
		classList.addAll(resolverUtil.getClasses());
		for (Class entityClass : classList) {
			System.out.println("Found Base Entity: " + entityClass.getName());
			classMap.put(entityClass.getName(), entityClass);
		}
		//pickup any embedded items
		resolverUtil.find(new ResolverUtil.IsA(Serializable.class), "edu.usu.sdl.openstorefront.core.entity");
		classList = new ArrayList<>();
		classList.addAll(resolverUtil.getClasses());
		for (Class entityClass : classList) {
			System.out.println("Found Serialization Entity: " + entityClass.getName());
			classMap.put(entityClass.getName(), entityClass);
		}

		List<EntityDocModel> docModels = EntityProcessor.processEntites(new ArrayList<>(classMap.values()));
		docModels.forEach(doc -> {
			System.out.println("Entity Processed: " + doc.getName());
		});

		System.out.println("Done");

	}

}
