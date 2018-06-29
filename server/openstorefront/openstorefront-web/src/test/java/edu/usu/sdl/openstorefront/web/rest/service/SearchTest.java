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
package edu.usu.sdl.openstorefront.web.rest.service;

import com.orientechnologies.orient.core.record.impl.ODocument;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentMedia;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.service.test.TestPersistenceService;
import edu.usu.sdl.openstorefront.web.rest.JerseyShiroTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author kbair
 */
public class SearchTest extends JerseyShiroTest
{

	@Override
	protected Class<?> getRestClass()
	{
		return Search.class;
	}

	/**
	 * @POST @Path("/export")
	 */
	@Test
	public void exportEmptyListTest()
	{
		//Arrange
		MultivaluedMap<String, String> idMap = new MultivaluedHashMap<>();
		idMap.addAll("multipleIds", Arrays.asList());

		String expectedOutput = "\"Name\",\"Organization\",\"Description\",\"Last Updated Dts\",\"Entry Type\"\n";

		//Act
		Response response = target("v1/service/search/export")
				.request()
				.header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
				.post(Entity.form(idMap));

		//Assert
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("application/csv", response.getHeaderString("Content-Type"));
		Assert.assertEquals("attachment; filename=\"searchResults.csv\"", response.getHeaderString("Content-Disposition"));
		Assert.assertTrue(response.hasEntity());
		String actualOutput = response.readEntity(String.class);
		Assert.assertEquals(expectedOutput, actualOutput);
	}

	/**
	 * @POST @Path("/export")
	 */
	@Test
	public void exportTest()
	{
		//Arrange
		MultivaluedMap<String, String> idMap = new MultivaluedHashMap<>();
		idMap.addAll("multipleIds", Arrays.asList("a0000000-0000-0000-0000-000000000000", "b0000000-0000-0000-0000-000000000000", "c0000000-0000-0000-0000-000000000000", "d0000000-0000-0000-0000-000000000000"));

		ODocument item1 = Mockito.mock(ODocument.class);
		Mockito.when(item1.field("componentId")).thenReturn("a0000000-0000-0000-0000-000000000000");
		ODocument item2 = Mockito.mock(ODocument.class);
		Mockito.when(item2.field("componentId")).thenReturn("b0000000-0000-0000-0000-000000000000");
		ODocument item3 = Mockito.mock(ODocument.class);
		Mockito.when(item3.field("componentId")).thenReturn("c0000000-0000-0000-0000-000000000000");
		ODocument item4 = Mockito.mock(ODocument.class);
		Mockito.when(item4.field("componentId")).thenReturn("d0000000-0000-0000-0000-000000000000");
		List<ODocument> dbResults = Arrays.asList(item1, item2, item3, item4);

		TestPersistenceService persistenceService = ((TestPersistenceService) ServiceProxyFactory.getServiceProxy().getPersistenceService());
		persistenceService.addQuery("select componentId from Component where activeStatus='A' and approvalState='A'", dbResults);
		persistenceService.addQuery("select componentId from Component", dbResults);
		
		String timeZoneCode = TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);

		Calendar calendar = Calendar.getInstance();
		calendar.set(2000, 0, 1, 0, 0, 0);
		Date activityDate = calendar.getTime();
		Component comp1 = new Component();
		comp1.setComponentId("a0000000-0000-0000-0000-000000000000");
		comp1.setName("My Test 1");
		comp1.setComponentType("Test Component");
		comp1.setLastActivityDts(activityDate);
		comp1.setOrganization("My Test Org.");
		comp1.setDescription("simple text");
		Component comp2 = new Component();
		comp2.setComponentId("b0000000-0000-0000-0000-000000000000");
		comp2.setName("My Test 2");
		comp2.setComponentType("Test Component");
		comp2.setLastActivityDts(activityDate);
		comp2.setOrganization("My Test Org.");
		comp2.setDescription("text with, a comma");
		Component comp3 = new Component();
		comp3.setComponentId("c0000000-0000-0000-0000-000000000000");
		comp3.setName("My Test 3");
		comp3.setComponentType("Test Component");
		comp3.setLastActivityDts(activityDate);
		comp3.setOrganization("My Test Org.");
		comp3.setDescription("<head><body><h1>simple html</h1></body><html>");
		Component comp4 = new Component();
		comp4.setComponentId("d0000000-0000-0000-0000-000000000000");
		comp4.setName("My Test 4");
		comp4.setComponentType("Test Component");
		comp4.setLastActivityDts(activityDate);
		comp4.setOrganization("My Test Org.");
		comp4.setDescription("<p>This is my page.</p>\n" +
							"<p>&nbsp;<img src=\"Media.action?SectionMedia&amp;mediaId=09660868-4267-46db-a282-ed455bc3c359\" alt=\"\" /></p>\n" +
							"<ul>\n" +
							"<li>this is an&nbsp;unorderd&nbsp;list</li>\n" +
							"<li>list item</li>\n" +
							"</ul>\n" +
							"<p><strong>this is bold</strong></p>");
		persistenceService.addQuery("select from Component where activeStatus='A'and approvalState='A' and ( dataSensitivity IS NULL ) and ( dataSource IS NULL ) and  componentId IN :componentIdsParams", Arrays.asList(comp1, comp2, comp3, comp4));

		ODocument componentAttributePk1 = Mockito.mock(ODocument.class);
		Mockito.when(componentAttributePk1.field("componentId")).thenReturn("a0000000-0000-0000-0000-000000000000");
		Mockito.when(componentAttributePk1.field("attributeCode")).thenReturn("TESTATT");
		Mockito.when(componentAttributePk1.field("attributeType")).thenReturn("TestAttribute");
		ODocument componentAttributePk2 = Mockito.mock(ODocument.class);
		Mockito.when(componentAttributePk2.field("componentId")).thenReturn("b0000000-0000-0000-0000-000000000000");
		Mockito.when(componentAttributePk2.field("attributeCode")).thenReturn("TESTATT");
		Mockito.when(componentAttributePk2.field("attributeType")).thenReturn("TestAttribute");
		ODocument componentAttributePk3 = Mockito.mock(ODocument.class);
		Mockito.when(componentAttributePk3.field("componentId")).thenReturn("c0000000-0000-0000-0000-000000000000");
		Mockito.when(componentAttributePk3.field("attributeCode")).thenReturn("TESTATT");
		Mockito.when(componentAttributePk3.field("attributeType")).thenReturn("TestAttribute");
		ODocument componentAttributePk4 = Mockito.mock(ODocument.class);
		Mockito.when(componentAttributePk4.field("componentId")).thenReturn("c0000000-0000-0000-0000-000000000000");
		Mockito.when(componentAttributePk4.field("attributeCode")).thenReturn("TESTATT");
		Mockito.when(componentAttributePk4.field("attributeType")).thenReturn("TestAttribute");

		persistenceService.addQuery("select componentAttributePk.attributeType as attributeType, componentAttributePk.attributeCode as attributeCode, componentId from ComponentAttribute where activeStatus='A' and componentId IN :componentIdsParams", Arrays.asList(componentAttributePk1, componentAttributePk2, componentAttributePk3, componentAttributePk4));
		persistenceService.addQuery("select from ComponentReview where activeStatus='A' and componentId IN :componentIdsParams", new ArrayList<>());
		persistenceService.addQuery("select from ComponentTag where activeStatus='A' and componentId IN :componentIdsParams", new ArrayList<>());

		ComponentType componentTypeExample = new ComponentType();
		componentTypeExample.setComponentType("Test Component");
		componentTypeExample.setLabel("Test Component");
		componentTypeExample.setComponentTypeTemplate("testtemplate");
		persistenceService.addQuery(ComponentType.class, Arrays.asList(componentTypeExample));

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

		String expectedOutput = "\"Name\",\"Organization\",\"Description\",\"Last Updated Dts\",\"Entry Type\"\n"
				+ "\"My Test 1\",\"My Test Org.\",\"simple text\",\"01/01/2000 00:00:00 " + timeZoneCode + "\",\"Test Component\"\n"
				+ "\"My Test 2\",\"My Test Org.\",\"text with, a comma\",\"01/01/2000 00:00:00 " + timeZoneCode + "\",\"Test Component\"\n"
				+ "\"My Test 3\",\"My Test Org.\",\"simple html\",\"01/01/2000 00:00:00 " + timeZoneCode + "\",\"Test Component\"\n"
				+ "\"My Test 4\",\"My Test Org.\",\"This is my page.   this is an unorderd list list item this is bold\",\"01/01/2000 00:00:00 " + timeZoneCode + "\",\"Test Component\"\n";

		//Act
		Response response = target("v1/service/search/export")
				.request()
				.header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
				.post(Entity.form(idMap));

		//Assert
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("application/csv", response.getHeaderString("Content-Type"));
		Assert.assertEquals("attachment; filename=\"searchResults.csv\"", response.getHeaderString("Content-Disposition"));
		Assert.assertTrue(response.hasEntity());
		String actualOutput = response.readEntity(String.class);
		Assert.assertEquals(expectedOutput, actualOutput);
	}
}
