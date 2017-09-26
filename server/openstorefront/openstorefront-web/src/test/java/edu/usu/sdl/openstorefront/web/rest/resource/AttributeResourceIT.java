/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */
package edu.usu.sdl.openstorefront.web.rest.resource;

import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.manager.OsgiManager;
import edu.usu.sdl.openstorefront.service.test.TestPersistenceService;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author kbair
 */
public class AttributeResourceIT extends JerseyTest
{
	@Override
	protected Application configure()
	{
		return new ResourceConfig(AttributeResource.class);
	}

	@BeforeClass
	public static void init()
	{
		//NOTE: (KB) I don't want to start the full system so start minimal pieces untill they can be refactored
		ServiceProxy.Test.setPersistenceServiceToTest();
		OsgiManager.init();
	}

	@AfterClass
	public static void cleanup()
	{
		OsgiManager.cleanup();
	}

	@Test
	public void getRequiredAttributeTypesTest()
	{
		TestPersistenceService persistenceService = ((TestPersistenceService)ServiceProxyFactory.getServiceProxy().getPersistenceService());
		List<AttributeType> expected = new ArrayList<>();
		AttributeType type = new AttributeType();
		type.setActiveStatus(AttributeType.ACTIVE_STATUS);
		type.setAttributeType("TEST");
		expected.add(type);
		persistenceService.addQuery(AttributeType.class, expected);
		List<AttributeType> response = target("v1/resource/attributes/attributetypes/required")
				.queryParam("componentType", "ARTICLE")
				.request()
				.get(new GenericType<List<AttributeType>>()
				{
				});
		Assert.assertNotNull(response);
		Assert.assertArrayEquals(expected.toArray(), response.toArray());

	}

	@Test
	public void getOptionalAttributeTypesTest()
	{
		AttributeType[] expected = new AttributeType[1];
		AttributeType type = new AttributeType();
		type.setActiveStatus(AttributeType.ACTIVE_STATUS);
		type.setAttributeType("TEST");
		expected[0] = type;
		List<AttributeType> response = target("v1/resource/attributes/attributetypes/optional")
				.queryParam("componentType", "ARTICLE")
				.request()
				.get(new GenericType<List<AttributeType>>()
				{
				});
		Assert.assertNotNull(response);
		Assert.assertArrayEquals(expected, response.toArray());
	}
}
