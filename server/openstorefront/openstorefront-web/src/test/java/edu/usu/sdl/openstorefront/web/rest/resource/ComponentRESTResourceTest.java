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
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentMedia;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.service.test.TestPersistenceService;
import edu.usu.sdl.openstorefront.web.rest.JerseyShiroTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author kbair
 */
public class ComponentRESTResourceTest extends JerseyShiroTest
{
	
	@Override
	protected Class<?> getRestClass()
	{
		return ComponentRESTResource.class;
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
		persistenceService.addQuery("select componentId from Component", dbResults);

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

		persistenceService.addQuery(ComponentType.class, new ArrayList<>());
		
		persistenceService.addQuery(ComponentMedia.class, new ArrayList<>());
		
		
		
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
				.header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
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

	@Test
	public void addComponentAttributeTest()
	{

		//Arrange
		setPermissions(new HashSet<>(Arrays.asList(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)));
		
		String componentId = "b3b2925e-af08-448e-a866-652154431c28";

		TestPersistenceService persistenceService = ((TestPersistenceService) ServiceProxyFactory.getServiceProxy().getPersistenceService());
		Component componentToUpdate = new Component();
		componentToUpdate.setComponentId(componentId);
		persistenceService.addObject(componentToUpdate);
		
		List<AttributeType> attributeTypes = new ArrayList<>();
		AttributeType type = new AttributeType();
		type.setAttributeType("TESTATT");
		type.setAllowMultipleFlg(false);
		attributeTypes.add(type);
		persistenceService.addQuery(AttributeType.class, attributeTypes);
		
		List<AttributeCode> attributeCodes = new ArrayList<>();
		AttributeCode code = new AttributeCode();
		code.setAttributeCodePk(new AttributeCodePk());
		code.getAttributeCodePk().setAttributeCode("CODE1");
		code.getAttributeCodePk().setAttributeType("TESTATT");
		attributeCodes.add(code);
		persistenceService.addQuery(AttributeCode.class, attributeCodes);
				
		ComponentAttribute postAttribute = new ComponentAttribute();
		postAttribute.setComponentAttributePk(new ComponentAttributePk());
		postAttribute.getComponentAttributePk().setAttributeCode("CODE1");
		postAttribute.getComponentAttributePk().setAttributeType("TESTATT");
		
		
		
		ComponentAttribute expectedAttribute = new ComponentAttribute();
		expectedAttribute.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
		expectedAttribute.setComponentAttributePk(new ComponentAttributePk());
		expectedAttribute.getComponentAttributePk().setAttributeCode("CODE1");
		expectedAttribute.getComponentAttributePk().setAttributeType("TESTATT");
		//Act
		ComponentAttribute response = target("v1/resource/components")
				.path(componentId)
				.path("attributes")
				.request()
				.header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
				.post(Entity.json(postAttribute), ComponentAttribute.class);
		
		//Assert
		Assert.assertNotNull(response.getComponentId());
		expectedAttribute.setComponentId(response.getComponentId()); // set system generated id
		expectedAttribute.getComponentAttributePk().setComponentId(response.getComponentId()); // set system generated id
		Assert.assertEquals(0, response.compareTo(expectedAttribute));
	}

	@Test
	public void activateComponentAttributeTest()
	{
		//Arrange
		setPermissions(new HashSet<>(Arrays.asList(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)));
		
		String componentId = "b3b2925e-af08-448e-a866-652154431c28";
		String attributeType = "TESTATT";
		String attributeCode = "CODE1";		
		
		TestPersistenceService persistenceService = ((TestPersistenceService) ServiceProxyFactory.getServiceProxy().getPersistenceService());
		Component componentToUpdate = new Component();
		componentToUpdate.setComponentId(componentId);
		persistenceService.addObject(componentToUpdate);
		
		ComponentAttribute savedAttribute = new ComponentAttribute();
		savedAttribute.setComponentId(componentId);
		savedAttribute.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
		savedAttribute.setComponentAttributePk(new ComponentAttributePk());
		savedAttribute.getComponentAttributePk().setAttributeCode(attributeCode);
		savedAttribute.getComponentAttributePk().setAttributeType(attributeType);
		savedAttribute.getComponentAttributePk().setComponentId(componentId);
		persistenceService.addObjectWithId(ComponentAttribute.class,savedAttribute.getComponentAttributePk(),savedAttribute);
		persistenceService.addObjectWithId(ComponentAttribute.class,savedAttribute.getComponentAttributePk(),savedAttribute); // Object is loaded twice
				
		ComponentAttribute expectedAttribute = new ComponentAttribute();
		expectedAttribute.setComponentId(componentId);
		expectedAttribute.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
		expectedAttribute.setComponentAttributePk(new ComponentAttributePk());
		expectedAttribute.getComponentAttributePk().setAttributeCode(attributeCode);
		expectedAttribute.getComponentAttributePk().setAttributeType(attributeType);
		expectedAttribute.getComponentAttributePk().setComponentId(componentId);
		
		//Act
		ComponentAttribute response = target("v1/resource/components")
				.path(componentId)
				.path("attributes")
				.path(attributeType)
				.path(attributeCode)
				.path("activate")
				.request()
				.header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
				.put(Entity.json(new ComponentAttribute()), ComponentAttribute.class);

		//Assert
		Assert.assertEquals(0, response.compareTo(expectedAttribute));
	}
	
	@Test
	public void inactivateComponentAttributeTest()
	{
		//Arrange
		setPermissions(new HashSet<>(Arrays.asList(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)));
		
		String componentId = "b3b2925e-af08-448e-a866-652154431c28";
		String attributeType = "TESTATT";
		String attributeCode = "CODE1";		
		
		TestPersistenceService persistenceService = ((TestPersistenceService) ServiceProxyFactory.getServiceProxy().getPersistenceService());
		Component componentToUpdate = new Component();
		componentToUpdate.setComponentId(componentId);
		persistenceService.addObject(componentToUpdate);
		
		ComponentAttribute savedAttribute = new ComponentAttribute();
		savedAttribute.setComponentId(componentId);
		savedAttribute.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
		savedAttribute.setComponentAttributePk(new ComponentAttributePk());
		savedAttribute.getComponentAttributePk().setAttributeCode(attributeCode);
		savedAttribute.getComponentAttributePk().setAttributeType(attributeType);
		savedAttribute.getComponentAttributePk().setComponentId(componentId);
		persistenceService.addObjectWithId(ComponentAttribute.class,savedAttribute.getComponentAttributePk(),savedAttribute);
		
		//Act
		Response response = target("v1/resource/components")
				.path(componentId)
				.path("attributes")
				.path(attributeType)
				.path(attributeCode)
				.request()
				.header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
				.delete();

		//Assert
		Assert.assertEquals(200, response.getStatus());
	}

}
