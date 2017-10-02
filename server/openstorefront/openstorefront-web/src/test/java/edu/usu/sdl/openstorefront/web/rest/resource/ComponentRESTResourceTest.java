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

import com.orientechnologies.orient.core.record.impl.ODocument;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.RequiredForComponent;
import edu.usu.sdl.openstorefront.security.test.TestRealm;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import edu.usu.sdl.openstorefront.service.manager.OsgiManager;
import edu.usu.sdl.openstorefront.service.test.TestPersistenceService;
import edu.usu.sdl.openstorefront.web.init.ShiroAdjustedFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import junit.framework.Assert;
import org.glassfish.jersey.internal.util.Base64;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author kbair
 */
public class ComponentRESTResourceTest extends JerseyTest
{

	static String getBasicAuthHeader(String username, String password)
	{
		return "Basic " + Base64.encodeAsString(username + ":" + password);
	}

	@Override
	protected TestContainerFactory getTestContainerFactory()
	{
		return new GrizzlyWebTestContainerFactory();
	}

	@Override
	protected DeploymentContext configureDeployment()
	{
		return ServletDeploymentContext
				.forServlet(new ServletContainer(new ResourceConfig(ComponentRESTResource.class)))
				.addListener(edu.usu.sdl.openstorefront.security.test.TestShiroLoader.class)
				.addFilter(ShiroAdjustedFilter.class, "ShiroFilter")
				.build();
	}

	/**
	 * NOTE: (KB) I don't want to start the full system so start minimal pieces
	 * until they can be re-factored so only what is needed for the test is
	 * started
	 */
	@BeforeClass
	public static void init()
	{
		// NOTE: (KB) As this is an integration test it would be nice to hit the 
		// DB however I dont have a good way to get the DB in a clean state
		ServiceProxy.Test.setPersistenceServiceToTest();
		OsgiManager.init();
		OSFCacheManager.init();
	}

	@Before
	public void setup()
	{
		((TestPersistenceService) ServiceProxyFactory.getServiceProxy().getPersistenceService()).clear();
		TestRealm.clearLogin();
	}

	@AfterClass
	public static void cleanup()
	{
		OsgiManager.cleanup();
		OSFCacheManager.cleanUp();
	}

	@Test
	public void addComponentAttributeTest()
	{
		TestRealm.setupRoles("admin_entry", new HashSet<>(Arrays.asList(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)));
		TestRealm.setLogin("CompAttUser", "thisIsATest", new HashSet<>(Arrays.asList("admin_entry")));

		TestPersistenceService persistenceService = ((TestPersistenceService) ServiceProxyFactory.getServiceProxy().getPersistenceService());

		//Arrange
		String componentId = "b3b2925e-af08-448e-a866-652154431c28";
		Component c = new Component();
		persistenceService.addObject(c);
		ComponentAttribute attribute = new ComponentAttribute();
		//Act
		ComponentAttribute response = target("v1/resource/components")
				.path(componentId)
				.path("attributes")
				.request()
				.header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader("CompAttUser", "thisIsATest"))
				.post(Entity.entity(attribute, MediaType.APPLICATION_JSON_TYPE), ComponentAttribute.class);

		//Assert
	}

	@Test
	public void activateComponentAttributeTest()
	{
		//Arrange
		String componentId = "b3b2925e-af08-448e-a866-652154431c28";
		RequiredForComponent attribute = new RequiredForComponent();
		//Act
		ComponentAttribute response = target("v1/resource/components")
				.path(componentId)
				.path("attributes")
				.request()
				.put(Entity.entity(attribute, MediaType.APPLICATION_JSON_TYPE), ComponentAttribute.class);

		//Assert
	}

	/**
	 * Basic test for getting a list of components from the rest API
	 *
	 * NOTE: this is written more to demonstrate how to write a test on a more
	 * complex REST call then it was for a given use case. if you have a use
	 * case in mind please adapt this to match so the test provides more than
	 * just an example
	 */
	@Test
	public void getComponentsTest()
	{
		//Arrange
		List<ODocument> dbResults = new ArrayList<>();
		// NOTE: (KB) as a general rule I don't like to mock thirdparty classes 
		// however why is ODocument exposed above the persistance service layer 
		ODocument item1 = Mockito.mock(ODocument.class);
		Mockito.when(item1.field("componentId")).thenReturn("a6b8c6c2-3ac1-40d3-b08a-f52f82dacd4d");
		dbResults.add(item1);

		TestPersistenceService persistenceService = ((TestPersistenceService) ServiceProxyFactory.getServiceProxy().getPersistenceService());
		persistenceService.addQuery("select componentId from Component where activeStatus='A' and approvalState='A'", dbResults);

		List<Component> dbResults2 = new ArrayList<>();
		Component comp = new Component();
		comp.setComponentId("a6b8c6c2-3ac1-40d3-b08a-f52f82dacd4d");
		comp.setName("My Test");
		dbResults2.add(comp);
		persistenceService.addQuery("select from Component where activeStatus='A'and approvalState='A' and  componentId IN :componentIdsParams", dbResults2);

		List<ODocument> dbResults3 = new ArrayList<>();
		ODocument componentAttributePk = Mockito.mock(ODocument.class);
		Mockito.when(componentAttributePk.field("componentId")).thenReturn("a6b8c6c2-3ac1-40d3-b08a-f52f82dacd4d");
		Mockito.when(componentAttributePk.field("attributeCode")).thenReturn("TESTATT");
		Mockito.when(componentAttributePk.field("attributeType")).thenReturn("TestAttribute");
		dbResults3.add(componentAttributePk);

		persistenceService.addQuery("select componentAttributePk.attributeType as attributeType, componentAttributePk.attributeCode as attributeCode, componentId from ComponentAttribute where activeStatus='A' and componentId IN :componentIdsParams", dbResults3);

		List<ComponentReview> dbResults4 = new ArrayList<>();
		ComponentReview review = new ComponentReview();
		review.setComponentId("a6b8c6c2-3ac1-40d3-b08a-f52f82dacd4d");
		review.setRating(5);
		dbResults4.add(review);
		persistenceService.addQuery("select from ComponentReview where activeStatus='A' and componentId IN :componentIdsParams", dbResults4);

		List<ComponentTag> dbResults5 = new ArrayList<>();
		ComponentTag tag = new ComponentTag();
		tag.setComponentId("a6b8c6c2-3ac1-40d3-b08a-f52f82dacd4d");
		dbResults5.add(tag);
		persistenceService.addQuery("select from ComponentTag where activeStatus='A' and componentId IN :componentIdsParams", dbResults5);

		List<AttributeCode> codes = new ArrayList<>();
		AttributeCode code = new AttributeCode();
		code.setActiveStatus(AttributeCode.ACTIVE_STATUS);
		AttributeCodePk attributeCodePk = new AttributeCodePk();
		attributeCodePk.setAttributeType("TestAttribute");
		attributeCodePk.setAttributeCode("TESTATT");
		code.setAttributeCodePk(attributeCodePk);
		codes.add(code);
		persistenceService.addQuery(AttributeCode.class, codes);

		List<AttributeType> types = new ArrayList<>();
		AttributeType type = new AttributeType();
		type.setAttributeType("TestAttribute");
		types.add(type);
		persistenceService.addQuery(AttributeType.class, types);

		//Act
		List<ComponentSearchView> response = target("v1/resource/components")
				.request()
				.get(new GenericType<List<ComponentSearchView>>()
				{
				});

		//Assert
		Assert.assertEquals(1, response.size());
		ComponentSearchView actual = response.get(0);
		Assert.assertEquals((Integer) 5, actual.getAverageRating());
		Assert.assertEquals("My Test", actual.getName());
		Assert.assertEquals("a6b8c6c2-3ac1-40d3-b08a-f52f82dacd4d", actual.getComponentId());
		Assert.assertEquals(1, actual.getAttributes().size());
		Assert.assertEquals("TestAttribute", actual.getAttributes().get(0).getType());

	}
}
