/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.core.api.ChangeLogService;
import edu.usu.sdl.openstorefront.core.api.ContentSectionService;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.ContentSection;
import edu.usu.sdl.openstorefront.core.entity.ContentSubSection;
import edu.usu.sdl.openstorefront.core.entity.TemporaryMedia;
import edu.usu.sdl.openstorefront.core.model.ContentSectionAll;
import edu.usu.sdl.openstorefront.service.test.TestPersistenceService;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;

/**
 *
 * @author kbair
 */
public class ContentSectionServiceTest
{
	@Test
	public void saveAllNoImagesInContent()
	{
		//Arrange
		ServiceProxy.Test.setPersistenceServiceToTest();
		TestPersistenceService persistenceService = (TestPersistenceService)ServiceProxy.getProxy().getPersistenceService();
		
		ContentSection section = new ContentSection();
		section.setContent("This is a test with no images");
		String contentSectionId = persistenceService.generateId();
		section.setContentSectionId(contentSectionId);
		ContentSubSection subsection1 = new ContentSubSection();
		subsection1.setContent("This is a test with no images in subsection");
		subsection1.setSubSectionId(persistenceService.generateId());
		ContentSubSection subsectionSpy1 = Mockito.spy(subsection1);
		ContentSubSection subsectionSpy2 = Mockito.spy(subsection1);
		persistenceService.addQuery(ContentSubSection.class, Arrays.asList(subsectionSpy1));
		
		Service mockService = Mockito.mock(Service.class);
		persistenceService.addObjectWithId(ContentSection.class, contentSectionId, section);
		
		Mockito.when(mockService.getPersistenceService()).thenReturn(persistenceService);
		ChangeLogService changeLogService = Mockito.mock(ChangeLogService.class);
		Mockito.when(changeLogService.findUpdateChanges(Mockito.any(),Mockito.any())).thenReturn(new ArrayList<>());
		Mockito.when(mockService.getChangeLogService()).thenReturn(changeLogService);
		
		BundleContext bundleContext = Mockito.mock(BundleContext.class);
		Mockito.when(bundleContext.getServiceReference(Service.class)).thenReturn(null);
		Mockito.when(bundleContext.getService(Mockito.any())).thenReturn(mockService);
		ServiceProxyFactory.setContext(bundleContext);
		
		ContentSection sectionSpy = Mockito.spy(section);
		
		ContentSectionAll contentSectionAll = new ContentSectionAll();
		contentSectionAll.setSection(sectionSpy);
		contentSectionAll.setSubsections(Arrays.asList(subsectionSpy2));
		//Act
		ContentSectionService service = new ContentSectionServiceImpl();
		service.saveAll(contentSectionAll);
		//Assert
		Mockito.verify(sectionSpy).setContent("<html>\n <head></head>\n <body>\n  This is a test with no images\n </body>\n</html>");
		Mockito.verify(subsectionSpy1).setContent("<html>\n <head></head>\n <body>\n  This is a test with no images in subsection\n </body>\n</html>");
		Mockito.verify(subsectionSpy2).setContent("<html>\n <head></head>\n <body>\n  This is a test with no images in subsection\n </body>\n</html>");
	}
	
	@Test
	public void saveAllNullContent()
	{
		//Arrange
		ServiceProxy.Test.setPersistenceServiceToTest();
		TestPersistenceService persistenceService = (TestPersistenceService)ServiceProxy.getProxy().getPersistenceService();
		
		ContentSection section = new ContentSection();
		section.setContent(null);
		String contentSectionId = persistenceService.generateId();
		section.setContentSectionId(contentSectionId);
		ContentSubSection subsection1 = new ContentSubSection();
		subsection1.setContent(null);
		subsection1.setSubSectionId(persistenceService.generateId());
		ContentSubSection subsectionSpy1 = Mockito.spy(subsection1);
		ContentSubSection subsectionSpy2 = Mockito.spy(subsection1);
		persistenceService.addQuery(ContentSubSection.class, Arrays.asList(subsectionSpy1));
		
		Service mockService = Mockito.mock(Service.class);
		persistenceService.addObjectWithId(ContentSection.class, contentSectionId, section);
		
		Mockito.when(mockService.getPersistenceService()).thenReturn(persistenceService);
		ChangeLogService changeLogService = Mockito.mock(ChangeLogService.class);
		Mockito.when(changeLogService.findUpdateChanges(Mockito.any(),Mockito.any())).thenReturn(new ArrayList<>());
		Mockito.when(mockService.getChangeLogService()).thenReturn(changeLogService);
		
		BundleContext bundleContext = Mockito.mock(BundleContext.class);
		Mockito.when(bundleContext.getServiceReference(Service.class)).thenReturn(null);
		Mockito.when(bundleContext.getService(Mockito.any())).thenReturn(mockService);
		ServiceProxyFactory.setContext(bundleContext);
		
		ContentSection sectionSpy = Mockito.spy(section);
		
		ContentSectionAll contentSectionAll = new ContentSectionAll();
		contentSectionAll.setSection(sectionSpy);
		contentSectionAll.setSubsections(Arrays.asList(subsectionSpy2));
		//Act
		ContentSectionService service = new ContentSectionServiceImpl();
		service.saveAll(contentSectionAll);
		
		//Assert
		Mockito.verify(sectionSpy).setContent(null);
		Mockito.verify(subsectionSpy1).setContent(null);
		Mockito.verify(subsectionSpy2).setContent(null);
	}
	
	//@Test - this test will fail due to File I/O issues not sure how to get past that yet
	public void saveAllContentWithImages()
	{
		//Arrange
		ServiceProxy.Test.setPersistenceServiceToTest();
		TestPersistenceService persistenceService = (TestPersistenceService)ServiceProxy.getProxy().getPersistenceService();
		
		ContentSection section = new ContentSection();
		section.setContent("<html>\n <head></head>\n <body>\n  This is a test with images <img src=\"Media.action?TemporaryMedia&name=de8173a33349852f7e2294a611bdfde4d5cf911a\" />\n </body>\n</html>");
		String contentSectionId = persistenceService.generateId();
		section.setContentSectionId(contentSectionId);
		ContentSubSection subsection1 = new ContentSubSection();
		subsection1.setContent(null);
		subsection1.setSubSectionId(persistenceService.generateId());
		ContentSubSection subsectionSpy1 = Mockito.spy(subsection1);
		ContentSubSection subsectionSpy2 = Mockito.spy(subsection1);
		persistenceService.addQuery(ContentSubSection.class, Arrays.asList(subsectionSpy1));
		
		Service mockService = Mockito.mock(Service.class);
		persistenceService.addObjectWithId(ContentSection.class, contentSectionId, section);
		
		Mockito.when(mockService.getPersistenceService()).thenReturn(persistenceService);
		ChangeLogService changeLogService = Mockito.mock(ChangeLogService.class);
		Mockito.when(changeLogService.findUpdateChanges(Mockito.any(),Mockito.any())).thenReturn(new ArrayList<>());
		Mockito.when(mockService.getChangeLogService()).thenReturn(changeLogService);
		
		BundleContext bundleContext = Mockito.mock(BundleContext.class);
		Mockito.when(bundleContext.getServiceReference(Service.class)).thenReturn(null);
		Mockito.when(bundleContext.getService(Mockito.any())).thenReturn(mockService);
		ServiceProxyFactory.setContext(bundleContext);
		
		ContentSection sectionSpy = Mockito.spy(section);
		
		ContentSectionAll contentSectionAll = new ContentSectionAll();
		contentSectionAll.setSection(sectionSpy);
		contentSectionAll.setSubsections(Arrays.asList(subsectionSpy2));
		
		TemporaryMedia tempMedia = new TemporaryMedia();
		tempMedia.setName("de8173a33349852f7e2294a611bdfde4d5cf911a");
		tempMedia.setFileName("de8173a33349852f7e2294a611bdfde4d5cf911a.png");
		tempMedia.setMimeType("image/png");
		tempMedia.setOriginalSourceURL("test.png");
		persistenceService.addObjectWithId(TemporaryMedia.class, "de8173a33349852f7e2294a611bdfde4d5cf911a", tempMedia);
		
		//Act
		ContentSectionService service = new ContentSectionServiceImpl();
		service.saveAll(contentSectionAll);
		
		//Assert
		Mockito.verify(sectionSpy).setContent("<html>\n <head></head>\n <body>\n  This is a test with images <img src=\"SectionMedia&mediaId=\" />\n </body>\n</html>");
		Mockito.verify(subsectionSpy1).setContent(null);
		Mockito.verify(subsectionSpy2).setContent(null);
	}
}