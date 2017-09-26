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
import edu.usu.sdl.openstorefront.core.entity.ComponentTypeRestriction;
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
import org.junit.Before;
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

	@Before
	public void setup()
	{
		((TestPersistenceService) ServiceProxyFactory.getServiceProxy().getPersistenceService()).clear();
	}

	@AfterClass
	public static void cleanup()
	{
		OsgiManager.cleanup();
	}

	@Test
	public void getRequiredAttributeTypesTest()
	{
		//Arrange
		List<ComponentTypeRestriction> requiredRestrictions = new ArrayList<>();
		ComponentTypeRestriction comp = new ComponentTypeRestriction();
		comp.setComponentType("COMP");
		comp.setStorageVersion("1");
		requiredRestrictions.add(comp);

		List<AttributeType> dbResults = new ArrayList<>();
		AttributeType type1 = new AttributeType();
		type1.setActiveStatus(AttributeType.ACTIVE_STATUS);
		type1.setAttributeType("TEST1");
		type1.setRequiredFlg(Boolean.TRUE);

		AttributeType type2 = new AttributeType();
		type2.setActiveStatus(AttributeType.ACTIVE_STATUS);
		type2.setAttributeType("TEST2");
		type2.setRequiredFlg(Boolean.TRUE);
		type2.setRequiredRestrictions(requiredRestrictions);

		dbResults.add(type1);
		dbResults.add(type2);
		((TestPersistenceService) ServiceProxyFactory.getServiceProxy().getPersistenceService()).addQuery(AttributeType.class, dbResults);

		List<AttributeType> expected = new ArrayList<>();
		AttributeType expectedType = new AttributeType();
		expectedType.setActiveStatus(AttributeType.ACTIVE_STATUS);
		expectedType.setAttributeType("TEST1");
		expected.add(expectedType);
		
		//Act
		List<AttributeType> response = target("v1/resource/attributes/attributetypes/required")
				.queryParam("componentType", "ARTICLE")
				.request()
				.get(new GenericType<List<AttributeType>>()
				{
				});
		
		//Assert
		Assert.assertNotNull(response);
		Assert.assertArrayEquals(expected.toArray(), response.toArray());
	}

	@Test
	public void getOptionalAttributeTypesTest()
	{
		//Arrange
		List<ComponentTypeRestriction> requiredRestrictions = new ArrayList<>();
		ComponentTypeRestriction comp = new ComponentTypeRestriction();
		comp.setComponentType("COMP");
		comp.setStorageVersion("1");
		requiredRestrictions.add(comp);

		List<AttributeType> dbResults = new ArrayList<>();
		AttributeType type1 = new AttributeType();
		type1.setActiveStatus(AttributeType.ACTIVE_STATUS);
		type1.setRequiredFlg(Boolean.FALSE);
		type1.setAttributeType("TEST1");

		AttributeType type2 = new AttributeType();
		type2.setActiveStatus(AttributeType.ACTIVE_STATUS);
		type2.setRequiredFlg(Boolean.TRUE);
		type2.setAttributeType("TEST2");
		type2.setRequiredRestrictions(requiredRestrictions);
		
		AttributeType type3 = new AttributeType();
		type3.setActiveStatus(AttributeType.ACTIVE_STATUS);
		type3.setRequiredFlg(Boolean.TRUE);
		type3.setAttributeType("TEST3");

		dbResults.add(type1);
		dbResults.add(type2);
		dbResults.add(type3);
		((TestPersistenceService) ServiceProxyFactory.getServiceProxy().getPersistenceService()).addQuery(AttributeType.class, dbResults);

		List<AttributeType> expected = new ArrayList<>();
		AttributeType expectedType1 = new AttributeType();
		expectedType1.setActiveStatus(AttributeType.ACTIVE_STATUS);
		expectedType1.setRequiredFlg(Boolean.FALSE);
		expectedType1.setAttributeType("TEST1");
		
		AttributeType expectedType2 = new AttributeType();
		expectedType2.setActiveStatus(AttributeType.ACTIVE_STATUS);
		expectedType2.setRequiredFlg(Boolean.TRUE);
		expectedType2.setAttributeType("TEST2");
		expectedType2.setRequiredRestrictions(requiredRestrictions);
		
		expected.add(expectedType1);
		expected.add(expectedType2);

		//Act
		List<AttributeType> response = target("v1/resource/attributes/attributetypes/optional")
				.queryParam("componentType", "ARTICLE")
				.request()
				.get(new GenericType<List<AttributeType>>()
				{
				});

		//Assert
		Assert.assertNotNull(response);
		Assert.assertArrayEquals(expected.toArray(), response.toArray());
	}
}
