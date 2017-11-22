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
import edu.usu.sdl.openstorefront.core.entity.SecurityRole;
import edu.usu.sdl.openstorefront.core.entity.SecurityRoleData;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.TagView;
import edu.usu.sdl.openstorefront.service.test.TestPersistenceService;
import edu.usu.sdl.openstorefront.web.rest.JerseyShiroTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import org.junit.Assert;
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
	 *
	 * @GET
	 * @Path("")
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
		persistenceService.addQuery("select from Component where activeStatus='A'and approvalState='A' and ( dataSensitivity IS NULL ) and ( dataSource IS NULL ) and  componentId IN :componentIdsParams", dbResults2);

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

	/**
	 * @POST @Path("/{id}/attributes")
	 */
	@Test
	public void addComponentAttributeTest()
	{

		//Arrange
		setSinglePermission(SecurityPermission.ADMIN_ENTRY_MANAGEMENT);

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

	/**
	 * @PUT @Path("/{id}/attributes/{attributeType}/{attributeCode}/activate")
	 */
	@Test
	public void activateComponentAttributeTest()
	{
		//Arrange
		setSinglePermission(SecurityPermission.ADMIN_ENTRY_MANAGEMENT);

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
		persistenceService.addObjectWithId(ComponentAttribute.class, savedAttribute.getComponentAttributePk(), savedAttribute);
		persistenceService.addObjectWithId(ComponentAttribute.class, savedAttribute.getComponentAttributePk(), savedAttribute); // Object is loaded twice

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

	/**
	 * @DELETE @Path("/{id}/attributes/{attributeType}/{attributeCode}")
	 */
	@Test
	public void inactivateComponentAttributeTest()
	{
		//Arrange
		setSinglePermission(SecurityPermission.ADMIN_ENTRY_MANAGEMENT);

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
		persistenceService.addObjectWithId(ComponentAttribute.class, savedAttribute.getComponentAttributePk(), savedAttribute);

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

	/**
	 * @POST @Path("/{id}/attributeList")
	 */
	@Test
	public void addComponentAttributeListTest()
	{

		//Arrange
		setSinglePermission(SecurityPermission.ADMIN_ENTRY_MANAGEMENT);

		String componentId = "b3b2925e-af08-448e-a866-652154431c28";

		TestPersistenceService persistenceService = ((TestPersistenceService) ServiceProxyFactory.getServiceProxy().getPersistenceService());
		Component componentToUpdate = new Component();
		componentToUpdate.setComponentId(componentId);
		persistenceService.addObject(componentToUpdate);

		AttributeType type1 = new AttributeType();
		type1.setAttributeType("TESTATT1");
		type1.setAllowMultipleFlg(false);

		AttributeType type2 = new AttributeType();
		type2.setAttributeType("TESTATT2");
		type2.setAllowMultipleFlg(false);

		AttributeType type3 = new AttributeType();
		type3.setAttributeType("TESTATT3");
		type3.setAllowMultipleFlg(false);

		persistenceService.addQuery(AttributeType.class, Arrays.asList(type1));
		persistenceService.addQuery(AttributeType.class, Arrays.asList(type2));
		persistenceService.addQuery(AttributeType.class, Arrays.asList(type3));

		AttributeCode code1 = new AttributeCode();
		code1.setAttributeCodePk(new AttributeCodePk());
		code1.getAttributeCodePk().setAttributeCode("CODE1");
		code1.getAttributeCodePk().setAttributeType("TESTATT1");

		AttributeCode code2 = new AttributeCode();
		code2.setAttributeCodePk(new AttributeCodePk());
		code2.getAttributeCodePk().setAttributeCode("CODE2");
		code2.getAttributeCodePk().setAttributeType("TESTATT2");

		AttributeCode code3 = new AttributeCode();
		code3.setAttributeCodePk(new AttributeCodePk());
		code3.getAttributeCodePk().setAttributeCode("CODE3");
		code3.getAttributeCodePk().setAttributeType("TESTATT3");

		persistenceService.addQuery(AttributeCode.class, Arrays.asList(code1));
		persistenceService.addQuery(AttributeCode.class, Arrays.asList(code2));
		persistenceService.addQuery(AttributeCode.class, Arrays.asList(code3));

		List<ComponentAttribute> postAttributeList = new ArrayList<>();
		ComponentAttribute postAttribute1 = new ComponentAttribute();
		postAttribute1.setComponentAttributePk(new ComponentAttributePk());
		postAttribute1.getComponentAttributePk().setAttributeCode("CODE1");
		postAttribute1.getComponentAttributePk().setAttributeType("TESTATT1");
		postAttributeList.add(postAttribute1);

		ComponentAttribute postAttribute2 = new ComponentAttribute();
		postAttribute2.setComponentAttributePk(new ComponentAttributePk());
		postAttribute2.getComponentAttributePk().setAttributeCode("CODE2");
		postAttribute2.getComponentAttributePk().setAttributeType("TESTATT2");
		postAttributeList.add(postAttribute2);

		ComponentAttribute postAttribute3 = new ComponentAttribute();
		postAttribute3.setComponentAttributePk(new ComponentAttributePk());
		postAttribute3.getComponentAttributePk().setAttributeCode("CODE3");
		postAttribute3.getComponentAttributePk().setAttributeType("TESTATT3");
		postAttributeList.add(postAttribute3);

		ComponentAttribute expectedAttribute = new ComponentAttribute();
		expectedAttribute.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
		expectedAttribute.setComponentAttributePk(new ComponentAttributePk());
		expectedAttribute.getComponentAttributePk().setAttributeCode("CODE1");
		expectedAttribute.getComponentAttributePk().setAttributeType("TESTATT1");
		//Act
		Response response = target("v1/resource/components")
				.path(componentId)
				.path("attributeList")
				.request()
				.header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
				.post(Entity.json(new GenericEntity<List<ComponentAttribute>>(postAttributeList)
				{
				}));

		//Assert
		Assert.assertEquals(200, response.getStatus());
	}

	/**
	 * @GET @Path("/tagviews")
	 */
	@Test
	public void getAllComponentTagsTest()
	{
		SecurityRole role1 = new SecurityRole();
		role1.setActiveStatus(SecurityRole.ACTIVE_STATUS);
		role1.setAllowUnspecifiedDataSensitivity(Boolean.TRUE);
		role1.setAllowUnspecifiedDataSource(Boolean.TRUE);
		SecurityRoleData security = new SecurityRoleData();
		security.setDataSource("SOURCE1");
		security.setDataSensitivity("DATA1");
		role1.setDataSecurity(Arrays.asList(security));
		getUserContext().setRoles(Arrays.asList(role1));
		//Arrange
		TestPersistenceService persistenceService = ((TestPersistenceService) ServiceProxyFactory.getServiceProxy().getPersistenceService());

		ComponentTag tag1 = new ComponentTag();
		tag1.setActiveStatus(ComponentTag.ACTIVE_STATUS);
		tag1.setComponentId("a0000000-0000-0000-0000-000000000000");
		tag1.setText("TAG1");

		ComponentTag tag2 = new ComponentTag();
		tag2.setActiveStatus(ComponentTag.ACTIVE_STATUS);
		tag2.setComponentId("b0000000-0000-0000-0000-000000000000");
		tag2.setText("TAG2");

		ComponentTag tag3 = new ComponentTag();
		tag3.setActiveStatus(ComponentTag.ACTIVE_STATUS);
		tag3.setComponentId("c0000000-0000-0000-0000-000000000000");
		tag3.setText("TAG3");

		ComponentTag tag4 = new ComponentTag();
		tag4.setActiveStatus(ComponentTag.ACTIVE_STATUS);
		tag4.setComponentId("d0000000-0000-0000-0000-000000000000");
		tag4.setText("TAG4");

		ComponentTag tag5 = new ComponentTag();
		tag5.setActiveStatus(ComponentTag.ACTIVE_STATUS);
		tag5.setComponentId("e0000000-0000-0000-0000-000000000000");
		tag5.setText("TAG5");
		List<ComponentTag> tags = Arrays.asList(tag1, tag2, tag3, tag4, tag5);
		persistenceService.addQuery(ComponentTag.class, tags);

		String query = "select componentId, name from Component";
		ODocument item1 = Mockito.mock(ODocument.class);
		Mockito.when(item1.field("componentId")).thenReturn("a0000000-0000-0000-0000-000000000000");
		Mockito.when(item1.field("name")).thenReturn("Test Component 1");
		ODocument item2 = Mockito.mock(ODocument.class);
		Mockito.when(item2.field("componentId")).thenReturn("b0000000-0000-0000-0000-000000000000");
		Mockito.when(item2.field("name")).thenReturn("Test Component 2");
		ODocument item3 = Mockito.mock(ODocument.class);
		Mockito.when(item3.field("componentId")).thenReturn("c0000000-0000-0000-0000-000000000000");
		Mockito.when(item3.field("name")).thenReturn("Test Component 3");
		ODocument item4 = Mockito.mock(ODocument.class);
		Mockito.when(item4.field("componentId")).thenReturn("c0000000-0000-0000-0000-000000000000");
		Mockito.when(item4.field("name")).thenReturn("Test Component 3");
		ODocument item5 = Mockito.mock(ODocument.class);
		Mockito.when(item5.field("componentId")).thenReturn("c0000000-0000-0000-0000-000000000000");
		Mockito.when(item5.field("name")).thenReturn("Test Component 3");
		List<ODocument> items = Arrays.asList(item1, item2, item3, item4, item5);
		//NOTE: it might be good to add a sticky bit to persistenceService.addQuery()
		tags.forEach(tag -> {
			persistenceService.addQuery(query, items);
		});

		Component comp1 = new Component();
		comp1.setActiveStatus(Component.ACTIVE_STATUS);
		comp1.setComponentId("a0000000-0000-0000-0000-000000000000");
		persistenceService.addObject(comp1);

		Component comp2 = new Component();
		comp2.setActiveStatus(Component.ACTIVE_STATUS);
		comp2.setComponentId("b0000000-0000-0000-0000-000000000000");
		comp2.setDataSensitivity("DATA1");
		persistenceService.addObject(comp2);

		Component comp3 = new Component();
		comp3.setActiveStatus(Component.ACTIVE_STATUS);
		comp3.setComponentId("c0000000-0000-0000-0000-000000000000");
		comp3.setDataSensitivity("DATA2");
		persistenceService.addObject(comp3);

		Component comp4 = new Component();
		comp4.setActiveStatus(Component.ACTIVE_STATUS);
		comp4.setComponentId("d0000000-0000-0000-0000-000000000000");
		comp4.setDataSource("SOURCE1");
		persistenceService.addObject(comp4);

		Component comp5 = new Component();
		comp5.setActiveStatus(Component.ACTIVE_STATUS);
		comp5.setComponentId("e0000000-0000-0000-0000-000000000000");
		comp5.setDataSource("SOURCE2");
		persistenceService.addObject(comp5);

		//Act
		List<TagView> response = target("v1/resource/components/tagviews")
				.request()
				.header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
				.get(new GenericType<List<TagView>>()
				{
				});

		//Assert
		Assert.assertEquals(3, response.size());
		List<String> tagNames = response.stream().map(TagView::getText).collect(Collectors.toList());
		Assert.assertEquals(Arrays.asList("TAG1","TAG2","TAG4"), tagNames);
	}
}
