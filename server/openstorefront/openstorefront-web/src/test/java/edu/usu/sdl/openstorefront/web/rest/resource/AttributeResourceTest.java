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

import edu.usu.sdl.openstorefront.core.api.ComponentService;
import edu.usu.sdl.openstorefront.core.api.LookupService;
import edu.usu.sdl.openstorefront.core.api.PersistenceService;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.BaseEntity;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.ComponentTypeRestriction;
import edu.usu.sdl.openstorefront.core.entity.SecurityMarkingType;
import edu.usu.sdl.openstorefront.core.view.AttributeCodeView;
import edu.usu.sdl.openstorefront.core.view.AttributeTypeView;
import edu.usu.sdl.openstorefront.service.AttributeServiceImpl;
import edu.usu.sdl.openstorefront.service.test.TestPersistenceService;
import edu.usu.sdl.openstorefront.web.rest.JerseyShiroTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author kbair
 */
public class AttributeResourceTest extends JerseyShiroTest
{

	@Override
	protected Class<?> getRestClass()
	{
		return AttributeResource.class;
	}

	/**
	 * @GET @PATH("/attributetypes/required")
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void getRequiredAttributeTypesTest()
	{
		//TestPersistenceService persistenceService = ((TestPersistenceService) ServiceProxyFactory.getServiceProxy().getPersistenceService());

		Service service = Mockito.mock(Service.class);

		ComponentService componentService = Mockito.mock(ComponentService.class);

		List<ComponentType> componentTypes = new ArrayList<>();
		ComponentType componentType = new ComponentType();
		componentType.setComponentType("COMP");
		componentTypes.add(componentType);

		Mockito.when(componentService.getAllComponentTypes()).thenReturn(componentTypes);
		Mockito.when(service.getComponentService()).thenReturn(componentService);

		PersistenceService persistenceService = Mockito.mock(PersistenceService.class);
		Mockito.when(service.getPersistenceService()).thenReturn(persistenceService);

		LookupService lookupService = Mockito.mock(LookupService.class);
		Mockito.when(service.getLookupService()).thenReturn(lookupService);
		Mockito.when(lookupService.getLookupEnity(SecurityMarkingType.class, null)).thenReturn(null);

		AttributeServiceImpl attributeService = Mockito.mock(AttributeServiceImpl.class);
		Mockito.when(attributeService.getAllAttributeCodes(AttributeCode.ACTIVE_STATUS)).thenReturn(new ArrayList<>());
		Mockito.when(attributeService.findRequiredAttributes("ARTICLE", false)).thenCallRealMethod();

		Mockito.when(service.getAttributeService()).thenReturn(attributeService);
		Mockito.when(attributeService.getComponentService()).thenReturn(componentService);

		ServiceProxyFactory.setTestService(service);

		//Arrange
		List<ComponentTypeRestriction> requiredRestrictions = new ArrayList<>();
		ComponentTypeRestriction comp = new ComponentTypeRestriction();
		comp.setComponentType("ARTICLE");
		comp.setStorageVersion("1");
		requiredRestrictions.add(comp);

		AttributeType type1 = new AttributeType();
		type1.setActiveStatus(AttributeType.ACTIVE_STATUS);
		type1.setAttributeType("TEST1");

		AttributeType type2 = new AttributeType();
		type2.setActiveStatus(AttributeType.ACTIVE_STATUS);
		type2.setAttributeType("TEST2");
		type2.setRequiredFlg(Boolean.TRUE);
		type2.setRequiredRestrictions(requiredRestrictions);

		List<AttributeType> attributes = new ArrayList<>();
		attributes.add(type1);
		attributes.add(type2);

		AttributeType attributeTypeExample = new AttributeType();
		attributeTypeExample.setActiveStatus(AttributeType.ACTIVE_STATUS);
		attributeTypeExample.setRequiredFlg(Boolean.TRUE);

		Mockito.when(persistenceService.queryByExample(Mockito.any(BaseEntity.class))).thenReturn((List) attributes);

		List<AttributeTypeView> expected = new ArrayList<>();
		AttributeTypeView expectedType = new AttributeTypeView();
		expectedType.setActiveStatus(AttributeType.ACTIVE_STATUS);
		expectedType.setAttributeType("TEST2");
		expected.add(expectedType);

		//Act
		List<AttributeTypeView> response = target("v1/resource/attributes")
				.path("required")
				.queryParam("componentType", "ARTICLE")
				.request()
				.header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
				.get(new GenericType<List<AttributeTypeView>>()
				{
				});

		//Assert
		Assert.assertNotNull(response);
		Assert.assertEquals(expected.get(0).getAttributeType(), response.get(0).getAttributeType());

	}

	/**
	 * @GET @PATH("")
	 */
	@Test
	public void getAttributeViewTest()
	{

		//Arrange
		TestPersistenceService persistenceService = ((TestPersistenceService) ServiceProxyFactory.getServiceProxy().getPersistenceService());
		List<ComponentTypeRestriction> requiredRestrictions = new ArrayList<>();
		ComponentTypeRestriction comp = new ComponentTypeRestriction();
		comp.setComponentType("COMP");
		comp.setStorageVersion("1");
		requiredRestrictions.add(comp);

		AttributeType type1 = new AttributeType();
		type1.setActiveStatus(AttributeType.ACTIVE_STATUS);
		type1.setRequiredFlg(Boolean.FALSE);
		type1.setAttributeType("TESTATT1");

		AttributeType type2 = new AttributeType();
		type2.setActiveStatus(AttributeType.ACTIVE_STATUS);
		type2.setRequiredFlg(Boolean.TRUE);
		type2.setAttributeType("TESTATT2");
		type2.setRequiredRestrictions(requiredRestrictions);

		AttributeType type3 = new AttributeType();
		type3.setActiveStatus(AttributeType.ACTIVE_STATUS);
		type3.setRequiredFlg(Boolean.TRUE);
		type3.setAttributeType("TESTATT3");

		persistenceService.addQuery(AttributeType.class, Arrays.asList(type1, type2, type3));
		persistenceService.addQuery(AttributeType.class, Arrays.asList(type1, type2, type3)); // attribute list queried twice

		AttributeCode code1 = new AttributeCode();
		code1.setActiveStatus(AttributeType.ACTIVE_STATUS);
		code1.setLabel("CODE1");
		code1.setAttributeCodePk(new AttributeCodePk());
		code1.getAttributeCodePk().setAttributeCode("CODE1");
		code1.getAttributeCodePk().setAttributeType("TESTATT1");

		AttributeCode code2 = new AttributeCode();
		code2.setActiveStatus(AttributeType.ACTIVE_STATUS);
		code2.setLabel("CODE2");
		code2.setAttributeCodePk(new AttributeCodePk());
		code2.getAttributeCodePk().setAttributeCode("CODE2");
		code2.getAttributeCodePk().setAttributeType("TESTATT2");

		AttributeCode code3 = new AttributeCode();
		code3.setActiveStatus(AttributeType.ACTIVE_STATUS);
		code3.setLabel("CODE3");
		code3.setAttributeCodePk(new AttributeCodePk());
		code3.getAttributeCodePk().setAttributeCode("CODE3");
		code3.getAttributeCodePk().setAttributeType("TESTATT3");

		AttributeCode code4 = new AttributeCode();
		code4.setActiveStatus(AttributeType.INACTIVE_STATUS);
		code4.setLabel("CODE4");
		code4.setAttributeCodePk(new AttributeCodePk());
		code4.getAttributeCodePk().setAttributeCode("CODE4");
		code4.getAttributeCodePk().setAttributeType("TESTATT1");

		persistenceService.addQuery(AttributeCode.class, Arrays.asList(code1, code2, code3, code4));

		AttributeTypeView expectedView1 = new AttributeTypeView();
		expectedView1.setAttributeType("TESTATT1");
		AttributeCodeView expectedView1Code1 = new AttributeCodeView();
		expectedView1Code1.setCode("CODE1");
		expectedView1.setCodes(Arrays.asList(expectedView1Code1));

		AttributeTypeView expectedView2 = new AttributeTypeView();
		expectedView2.setAttributeType("TESTATT2");
		AttributeCodeView expectedView2Code1 = new AttributeCodeView();
		expectedView2Code1.setCode("CODE2");
		expectedView2.setCodes(Arrays.asList(expectedView2Code1));

		AttributeTypeView expectedView3 = new AttributeTypeView();
		expectedView3.setAttributeType("TESTATT3");
		AttributeCodeView expectedView3Code1 = new AttributeCodeView();
		expectedView3Code1.setCode("CODE3");
		expectedView3.setCodes(Arrays.asList(expectedView3Code1));

		List<AttributeTypeView> expectedList = Arrays.asList(expectedView1, expectedView2, expectedView3);
		AttributeType expectedQueryExample = new AttributeType();
		expectedQueryExample.setActiveStatus(AttributeType.ACTIVE_STATUS);
		//Act
		List<AttributeTypeView> response = target("v1/resource/attributes")
				.queryParam("important", "false")
				.request()
				.header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
				.get(new GenericType<List<AttributeTypeView>>()
				{
				});

		//Assert
		Assert.assertNotNull(response);
		Assert.assertEquals(expectedList.size(), response.size());
		for (int i = 0; i < expectedList.size(); i++) {
			//FIXME: no compareTo on views need to implement it
//			Assert.assertEquals(0, expectedList.get(i).compareTo(response.get(i)));
			AttributeTypeView expected = expectedList.get(i);
			AttributeTypeView actual = response.get(i);
			Assert.assertEquals(expected.getCodes().size(), actual.getCodes().size());
			Assert.assertEquals(expected.getAttributeType(), actual.getAttributeType());
		}
	}

	/**
	 * @GET @PATH("/optional")
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void getOptionalAttributeViewTest()
	{
		//Arrange
		//TestPersistenceService persistenceService = ((TestPersistenceService) ServiceProxyFactory.getServiceProxy().getPersistenceService());

		Service service = Mockito.mock(Service.class);

		ComponentService componentService = Mockito.mock(ComponentService.class);

		List<ComponentType> componentTypes = new ArrayList<>();
		ComponentType componentType = new ComponentType();
		componentType.setComponentType("COMP");
		componentTypes.add(componentType);

		Mockito.when(componentService.getAllComponentTypes()).thenReturn(componentTypes);
		Mockito.when(service.getComponentService()).thenReturn(componentService);

		PersistenceService persistenceService = Mockito.mock(PersistenceService.class);
		Mockito.when(service.getPersistenceService()).thenReturn(persistenceService);

		LookupService lookupService = Mockito.mock(LookupService.class);
		Mockito.when(service.getLookupService()).thenReturn(lookupService);
		Mockito.when(lookupService.getLookupEnity(SecurityMarkingType.class, null)).thenReturn(null);

		ServiceProxyFactory.setTestService(service);

		List<ComponentTypeRestriction> requiredRestrictions = new ArrayList<>();
		ComponentTypeRestriction comp = new ComponentTypeRestriction();
		comp.setComponentType("COMP");
		comp.setStorageVersion("1");
		requiredRestrictions.add(comp);

		AttributeType type1 = new AttributeType();
		type1.setActiveStatus(AttributeType.ACTIVE_STATUS);
		type1.setRequiredFlg(Boolean.FALSE);
		type1.setAttributeType("TESTATT1");

		List<ComponentTypeRestriction> optional = new ArrayList<>();
		comp = new ComponentTypeRestriction();
		comp.setComponentType("ARTICLE");
		comp.setStorageVersion("1");
		optional.add(comp);
		type1.setOptionalRestrictions(optional);

		AttributeType type2 = new AttributeType();
		type2.setActiveStatus(AttributeType.ACTIVE_STATUS);
		type2.setRequiredFlg(Boolean.TRUE);
		type2.setAttributeType("TESTATT2");
		type2.setRequiredRestrictions(requiredRestrictions);
		optional = new ArrayList<>();
		comp = new ComponentTypeRestriction();
		comp.setComponentType("ARTICLE");
		comp.setStorageVersion("1");
		optional.add(comp);
		type2.setOptionalRestrictions(optional);

		AttributeType type3 = new AttributeType();
		type3.setActiveStatus(AttributeType.ACTIVE_STATUS);
		type3.setRequiredFlg(Boolean.TRUE);
		type3.setAttributeType("TESTATT3");

		List<AttributeType> attributes = new ArrayList<>();
		attributes.add(type1);
		attributes.add(type2);
		attributes.add(type3);

		Mockito.when(persistenceService.queryByExample(Mockito.any(BaseEntity.class))).thenReturn((List) attributes);
		//persistenceService.addQuery(AttributeType.class, Arrays.asList(type1, type2, type3));
		//persistenceService.addQuery(AttributeType.class, Arrays.asList(type1, type2, type3)); // attribute list queried twice

		AttributeCode code1 = new AttributeCode();
		code1.setActiveStatus(AttributeType.ACTIVE_STATUS);
		code1.setLabel("CODE1");
		code1.setAttributeCodePk(new AttributeCodePk());
		code1.getAttributeCodePk().setAttributeCode("CODE1");
		code1.getAttributeCodePk().setAttributeType("TESTATT1");

		AttributeCode code2 = new AttributeCode();
		code2.setActiveStatus(AttributeType.ACTIVE_STATUS);
		code2.setLabel("CODE2");
		code2.setAttributeCodePk(new AttributeCodePk());
		code2.getAttributeCodePk().setAttributeCode("CODE2");
		code2.getAttributeCodePk().setAttributeType("TESTATT2");

		AttributeCode code3 = new AttributeCode();
		code3.setActiveStatus(AttributeType.ACTIVE_STATUS);
		code3.setLabel("CODE3");
		code3.setAttributeCodePk(new AttributeCodePk());
		code3.getAttributeCodePk().setAttributeCode("CODE3");
		code3.getAttributeCodePk().setAttributeType("TESTATT3");

		AttributeCode code4 = new AttributeCode();
		code4.setActiveStatus(AttributeType.INACTIVE_STATUS);
		code4.setLabel("CODE4");
		code4.setAttributeCodePk(new AttributeCodePk());
		code4.getAttributeCodePk().setAttributeCode("CODE4");
		code4.getAttributeCodePk().setAttributeType("TESTATT1");

		List<AttributeCode> attributeCodes = new ArrayList<>();
		attributeCodes.add(code1);
		attributeCodes.add(code2);
		attributeCodes.add(code3);

		AttributeServiceImpl attributeService = Mockito.mock(AttributeServiceImpl.class);

		Mockito.when(attributeService.getAllAttributeCodes(AttributeCode.ACTIVE_STATUS)).thenReturn(attributeCodes);
		Mockito.when(service.getAttributeService()).thenReturn(attributeService);

		Mockito.when(attributeService.findOptionalAttributes("ARTICLE", false)).thenCallRealMethod();
		Mockito.when(attributeService.getComponentService()).thenReturn(componentService);

		//	persistenceService.addQuery(AttributeCode.class, Arrays.asList(code1, code2, code3, code4));
		AttributeTypeView expectedView1 = new AttributeTypeView();
		expectedView1.setAttributeType("TESTATT1");
		AttributeCodeView expectedView1Code1 = new AttributeCodeView();
		expectedView1Code1.setCode("CODE1");
		expectedView1.setCodes(Arrays.asList(expectedView1Code1));

		AttributeTypeView expectedView2 = new AttributeTypeView();
		expectedView2.setAttributeType("TESTATT2");
		AttributeCodeView expectedView2Code1 = new AttributeCodeView();
		expectedView2Code1.setCode("CODE2");
		expectedView2.setCodes(Arrays.asList(expectedView2Code1));

		List<AttributeTypeView> expectedList = Arrays.asList(expectedView1, expectedView2);
		AttributeType expectedQueryExample = new AttributeType();
		expectedQueryExample.setActiveStatus(AttributeType.ACTIVE_STATUS);

		//Act
		List<AttributeTypeView> response = target("v1/resource/attributes")
				.path("optional")
				.queryParam("componentType", "ARTICLE")
				.request()
				.header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
				.get(new GenericType<List<AttributeTypeView>>()
				{
				});

		//Assert
		Assert.assertNotNull(response);
		Assert.assertEquals(expectedList.size(), response.size());
		for (int i = 0; i < expectedList.size(); i++) {
			//FIXME: no compareTo on views need to implement it
//			Assert.assertEquals(0, expectedList.get(i).compareTo(response.get(i)));
			AttributeTypeView expected = expectedList.get(i);
			AttributeTypeView actual = response.get(i);
			Assert.assertEquals(expected.getCodes().size(), actual.getCodes().size());
			Assert.assertEquals(expected.getAttributeType(), actual.getAttributeType());
		}
	}
}
