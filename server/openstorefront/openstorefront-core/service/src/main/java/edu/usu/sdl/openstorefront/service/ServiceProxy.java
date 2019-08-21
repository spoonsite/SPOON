/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.core.api.AlertService;
import edu.usu.sdl.openstorefront.core.api.AsyncService;
import edu.usu.sdl.openstorefront.core.api.AttributeService;
import edu.usu.sdl.openstorefront.core.api.BrandingService;
import edu.usu.sdl.openstorefront.core.api.ChangeLogService;
import edu.usu.sdl.openstorefront.core.api.ChecklistService;
import edu.usu.sdl.openstorefront.core.api.ComponentService;
import edu.usu.sdl.openstorefront.core.api.ContactService;
import edu.usu.sdl.openstorefront.core.api.ContentSectionService;
import edu.usu.sdl.openstorefront.core.api.EntityEventService;
import edu.usu.sdl.openstorefront.core.api.EvaluationService;
import edu.usu.sdl.openstorefront.core.api.FaqService;
import edu.usu.sdl.openstorefront.core.api.FeedbackService;
import edu.usu.sdl.openstorefront.core.api.HelpSupportService;
import edu.usu.sdl.openstorefront.core.api.ImportService;
import edu.usu.sdl.openstorefront.core.api.LookupService;
import edu.usu.sdl.openstorefront.core.api.NotificationService;
import edu.usu.sdl.openstorefront.core.api.OrganizationService;
import edu.usu.sdl.openstorefront.core.api.PersistenceService;
import edu.usu.sdl.openstorefront.core.api.PluginService;
import edu.usu.sdl.openstorefront.core.api.ReportService;
import edu.usu.sdl.openstorefront.core.api.SearchService;
import edu.usu.sdl.openstorefront.core.api.SecurityService;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.SubmissionFormService;
import edu.usu.sdl.openstorefront.core.api.SystemArchiveService;
import edu.usu.sdl.openstorefront.core.api.SystemService;
import edu.usu.sdl.openstorefront.core.api.UserService;
import edu.usu.sdl.openstorefront.core.api.WorkPlanService;
import edu.usu.sdl.openstorefront.core.api.model.TaskRequest;
import edu.usu.sdl.openstorefront.core.entity.ModificationType;
import edu.usu.sdl.openstorefront.core.filter.FilterEngine;
import edu.usu.sdl.openstorefront.service.api.AttributeServicePrivate;
import edu.usu.sdl.openstorefront.service.api.ChangeLogServicePrivate;
import edu.usu.sdl.openstorefront.service.api.ComponentServicePrivate;
import edu.usu.sdl.openstorefront.service.api.ImportServicePrivate;
import edu.usu.sdl.openstorefront.service.api.NotificationServicePrivate;
import edu.usu.sdl.openstorefront.service.api.PluginServicePrivate;
import edu.usu.sdl.openstorefront.service.api.ProxyFactory;
import edu.usu.sdl.openstorefront.service.api.SearchServicePrivate;
import edu.usu.sdl.openstorefront.service.api.SecurityServicePrivate;
import edu.usu.sdl.openstorefront.service.api.SystemArchiveServicePrivate;
import edu.usu.sdl.openstorefront.service.api.UserServicePrivate;
import edu.usu.sdl.openstorefront.service.manager.MongoDBManager;
import edu.usu.sdl.openstorefront.service.manager.OrientDBManager;
import edu.usu.sdl.openstorefront.service.repo.RepoFactory;
import edu.usu.sdl.openstorefront.service.test.TestPersistenceService;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Entry point to the service layer; Expecting one Service Proxy per thread. Not
 * thread Safe...there needs to be a new db connection per thread.
 *
 * @author dshurtleff
 */
public class ServiceProxy
		implements Service
{

	private String modificationType = ModificationType.API;

	private PersistenceService persistenceService;
	private LookupService lookupService;
	private AttributeService attributeService;
	private AttributeServicePrivate attributeServicePrivate;
	private ComponentService componentService;
	private ComponentServicePrivate componentServicePrivate;
	private SearchService searchService;
	private SearchServicePrivate searchServicePrivate;
	private UserService userService;
	private UserServicePrivate userServicePrivate;
	private SystemService systemService;
	private AlertService alertService;
	private ReportService reportService;
	private OrganizationService organizationService;
	private PluginService pluginService;
	private PluginServicePrivate pluginServicePrivate;
	private ImportService importService;
	private ImportServicePrivate importServicePrivate;
	private BrandingService brandingService;
	private NotificationService notificationService;
	private NotificationServicePrivate notificationServicePrivate;
	private FeedbackService feedbackService;
	private ContactService contactService;
	private EvaluationService evaluationService;
	private ChecklistService checklistService;
	private ContentSectionService contentSectionService;
	private SecurityService securityService;
	private SecurityServicePrivate securityServicePrivate;
	private ChangeLogService changeLogService;
	private ChangeLogServicePrivate changeLogServicePrivate;
	private SystemArchiveService systemArchiveService;
	private SystemArchiveServicePrivate systemArchiveServicePrivate;
	private HelpSupportService helpSupportService;
	private FaqService faqService;
	private WorkPlanService workPlanService;
	private SubmissionFormService submissionFormService;
	private EntityEventService entityEventService;

	private FilterEngine filterEngine;
	private static ProxyFactory proxyFactory = null;
	private RepoFactory repoFactory;

	public ServiceProxy()
	{
		if (Test.isMockPersistenceService.get()) {
			this.persistenceService = Test.mockPersistanceService;
		} else if (Test.isTestPersistenceService.get()) {
			this.persistenceService = new TestPersistenceService();
		}
		repoFactory = new RepoFactory();
	}

	public ServiceProxy(String modificationType)
	{
		this.modificationType = modificationType;

		if (Test.isMockPersistenceService.get()) {
			this.persistenceService = Test.mockPersistanceService;
		} else if (Test.isTestPersistenceService.get()) {
			this.persistenceService = new TestPersistenceService();
		}
		repoFactory = new RepoFactory();
	}

	public ServiceProxy(PersistenceService persistenceService)
	{
		this.persistenceService = persistenceService;
	}

	public static ServiceProxy getProxy()
	{
		if (proxyFactory != null) {
			return proxyFactory.getServiceProxy(ModificationType.API);
		} else {
			return new ServiceProxy(ModificationType.API);
		}
	}

	public static void setProxyFactory(ProxyFactory newFactory)
	{
		proxyFactory = newFactory;
	}

	public static ServiceProxy getProxy(String modificationType)
	{
		if (proxyFactory != null) {
			return proxyFactory.getServiceProxy(modificationType);
		} else {
			return new ServiceProxy(modificationType);
		}
	}

	public FilterEngine getFilterEngine()
	{
		//*must be lazy loaded otherwise it would create circular reference
		if (filterEngine == null) {
			filterEngine = FilterEngine.getInstance();
		}
		return filterEngine;
	}

	public void setFilterEngine(FilterEngine filterEngine)
	{
		this.filterEngine = filterEngine;
	}

	@Override
	public void reset()
	{
		persistenceService = null;
		lookupService = null;
		attributeService = null;
		attributeServicePrivate = null;
		componentService = null;
		componentServicePrivate = null;
		searchService = null;
		searchServicePrivate = null;
		userService = null;
		userServicePrivate = null;
		systemService = null;
		alertService = null;
		reportService = null;
		organizationService = null;
		pluginService = null;
		pluginServicePrivate = null;
		importService = null;
		importServicePrivate = null;
		brandingService = null;
		notificationService = null;
		notificationServicePrivate = null;
		feedbackService = null;
		contactService = null;
		evaluationService = null;
		checklistService = null;
		contentSectionService = null;
		securityService = null;
		securityServicePrivate = null;
		changeLogService = null;
		changeLogServicePrivate = null;
		systemArchiveService = null;
		systemArchiveServicePrivate = null;
		helpSupportService = null;
		faqService = null;
		workPlanService = null;
		entityEventService = null;
		submissionFormService = null;

	}

	@Override
	public PersistenceService getPersistenceService()
	{
		if (persistenceService == null) {
			persistenceService = createPersistenceService();
		}
		return persistenceService;
	}

	@Override
	public PersistenceService getNewPersistenceService()
	{
		return createPersistenceService();
	}

	protected PersistenceService createPersistenceService()
	{
		PersistenceService persistenceServiceLocal;

		if (Test.isTestPersistenceService.get()) {
			persistenceServiceLocal = new TestPersistenceService();
		} else {
			boolean useMongo = Convert.toBoolean(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_DB_USE_MONGO, "false"));

			if (useMongo) {
				persistenceServiceLocal = new MongoPersistenceServiceImpl(MongoDBManager.getInstance());
			} else {
				persistenceServiceLocal = new OrientPersistenceService(OrientDBManager.getInstance());
			}
		}

		return persistenceServiceLocal;
	}

	@Override
	public LookupService getLookupService()
	{
		if (lookupService == null) {
			lookupService = DynamicProxy.newInstance(new LookupServiceImpl());
		}
		return lookupService;
	}

	@Override
	public AttributeService getAttributeService()
	{
		if (attributeService == null) {
			attributeService = DynamicProxy.newInstance(new AttributeServiceImpl());
		}
		return attributeService;
	}

	@Override
	public ComponentService getComponentService()
	{
		if (componentService == null) {
			componentService = DynamicProxy.newInstance(new ComponentServiceImpl());
		}
		return componentService;
	}

	public ComponentServicePrivate getComponentServicePrivate()
	{
		if (componentServicePrivate == null) {
			componentServicePrivate = DynamicProxy.newInstance(new ComponentServiceImpl());
		}
		return componentServicePrivate;
	}

	@Override
	public SearchService getSearchService()
	{
		if (searchService == null) {
			searchService = DynamicProxy.newInstance(new SearchServiceImpl());
		}
		return searchService;
	}

	public SearchServicePrivate getSearchServicePrivate()
	{
		if (searchServicePrivate == null) {
			searchServicePrivate = DynamicProxy.newInstance(new SearchServiceImpl());
		}
		return searchServicePrivate;
	}

	@Override
	public UserService getUserService()
	{
		if (userService == null) {
			userService = DynamicProxy.newInstance(new UserServiceImpl());
		}
		return userService;
	}

	public UserServicePrivate getUserServicePrivate()
	{
		if (userService == null) {
			userServicePrivate = DynamicProxy.newInstance(new UserServiceImpl());
		}
		return userServicePrivate;
	}

	@Override
	public SystemService getSystemService()
	{
		if (systemService == null) {
			systemService = DynamicProxy.newInstance(new SystemServiceImpl());
		}
		return systemService;
	}

	@Override
	public AlertService getAlertService()
	{
		if (alertService == null) {
			alertService = DynamicProxy.newInstance(new AlertServiceImpl());
		}
		return alertService;
	}

	@Override
	public ReportService getReportService()
	{
		if (reportService == null) {
			reportService = DynamicProxy.newInstance(new ReportServiceImpl());
		}
		return reportService;
	}

	@Override
	public OrganizationService getOrganizationService()
	{
		if (organizationService == null) {
			organizationService = DynamicProxy.newInstance(new OrganizationServiceImpl());
		}
		return organizationService;
	}

	@Override
	public PluginService getPluginService()
	{
		if (pluginService == null) {
			pluginService = DynamicProxy.newInstance(new PluginServiceImpl());
		}
		return pluginService;
	}

	public PluginServicePrivate getPluginServicePrivate()
	{
		if (pluginServicePrivate == null) {
			pluginServicePrivate = DynamicProxy.newInstance(new PluginServiceImpl());
		}
		return pluginServicePrivate;
	}

	public AttributeServicePrivate getAttributeServicePrivate()
	{
		if (attributeServicePrivate == null) {
			attributeServicePrivate = DynamicProxy.newInstance(new AttributeServiceImpl());
		}
		return attributeServicePrivate;
	}

	@Override
	public ImportService getImportService()
	{
		if (importService == null) {
			importService = DynamicProxy.newInstance(new ImportServiceImpl());
		}
		return importService;
	}

	public ImportServicePrivate getImportServicePrivate()
	{
		if (importServicePrivate == null) {
			importServicePrivate = DynamicProxy.newInstance(new ImportServiceImpl());
		}
		return importServicePrivate;
	}

	@Override
	public BrandingService getBrandingService()
	{
		if (brandingService == null) {
			brandingService = DynamicProxy.newInstance(new BrandingServiceImpl());
		}
		return brandingService;
	}

	@Override
	public NotificationService getNotificationService()
	{
		if (notificationService == null) {
			notificationService = DynamicProxy.newInstance(new NotificationServiceImpl());
		}
		return notificationService;
	}

	public NotificationServicePrivate getNotificationServicePrivate()
	{
		if (notificationServicePrivate == null) {
			notificationServicePrivate = DynamicProxy.newInstance(new NotificationServiceImpl());
		}
		return notificationServicePrivate;
	}

	@Override
	public FeedbackService getFeedbackService()
	{
		if (feedbackService == null) {
			feedbackService = DynamicProxy.newInstance(new FeedbackServiceImpl());
		}
		return feedbackService;
	}

	@Override
	public ContactService getContactService()
	{
		if (contactService == null) {
			contactService = DynamicProxy.newInstance(new ContactServiceImpl());
		}
		return contactService;
	}

	@Override
	public <T extends AsyncService> T getAsyncProxy(T originalProxy)
	{
		TaskRequest taskRequest = new TaskRequest();
		taskRequest.setAllowMultiple(true);
		taskRequest.setName("Aync Service Call");
		return getAsyncProxy(originalProxy, taskRequest);
	}

	@Override
	public <T extends AsyncService> T getAsyncProxy(T originalProxy, TaskRequest taskRequest)
	{
		Objects.requireNonNull(originalProxy, "Original Service is required");
		T asyncService = AsyncProxy.newInstance(originalProxy, taskRequest);
		return asyncService;
	}

	public String getModificationType()
	{
		return modificationType;
	}

	public void setModificationType(String modificationType)
	{
		this.modificationType = modificationType;
	}

	@Override
	public EvaluationService getEvaluationService()
	{
		if (evaluationService == null) {
			evaluationService = DynamicProxy.newInstance(new EvaluationServiceImpl());
		}
		return evaluationService;
	}

	@Override
	public ContentSectionService getContentSectionService()
	{
		if (contentSectionService == null) {
			contentSectionService = DynamicProxy.newInstance(new ContentSectionServiceImpl());
		}
		return contentSectionService;
	}

	@Override
	public ChecklistService getChecklistService()
	{
		if (checklistService == null) {
			checklistService = DynamicProxy.newInstance(new ChecklistServiceImpl());
		}
		return checklistService;
	}

	@Override
	public SecurityService getSecurityService()
	{
		if (securityService == null) {
			securityService = DynamicProxy.newInstance(new SecurityServiceImpl());
		}
		return securityService;
	}

	public SecurityServicePrivate getSecurityServicePrivate()
	{
		if (securityServicePrivate == null) {
			securityServicePrivate = DynamicProxy.newInstance(new SecurityServiceImpl());
		}
		return securityServicePrivate;
	}

	@Override
	public ChangeLogService getChangeLogService()
	{
		if (changeLogService == null) {
			changeLogService = DynamicProxy.newInstance(new ChangeLogServiceImpl());
		}
		return changeLogService;
	}

	public ChangeLogServicePrivate getChangeLogServicePrivate()
	{
		if (changeLogServicePrivate == null) {
			changeLogServicePrivate = DynamicProxy.newInstance(new ChangeLogServiceImpl());
		}
		return changeLogServicePrivate;
	}

	@Override
	public SystemArchiveService getSystemArchiveService()
	{
		if (systemArchiveService == null) {
			systemArchiveService = DynamicProxy.newInstance(new SystemArchiveServiceImpl());
		}
		return systemArchiveService;
	}

	public SystemArchiveServicePrivate getSystemArchiveServicePrivate()
	{
		if (systemArchiveServicePrivate == null) {
			systemArchiveServicePrivate = DynamicProxy.newInstance(new SystemArchiveServiceImpl());
		}
		return systemArchiveServicePrivate;
	}

	@Override
	public HelpSupportService getHelpSupportService()
	{
		if (helpSupportService == null) {
			helpSupportService = DynamicProxy.newInstance(new HelpSupportServiceImpl());
		}
		return helpSupportService;
	}

	@Override
	public FaqService getFaqService()
	{
		if (faqService == null) {
			faqService = DynamicProxy.newInstance(new FaqServiceImpl());
		}
		return faqService;

	}

	@Override
	public WorkPlanService getWorkPlanService()
	{
		if (workPlanService == null) {
			workPlanService = DynamicProxy.newInstance(new WorkPlanServiceImpl());
		}
		return workPlanService;
	}

	public EntityEventService getEntityEventService()
	{
		if (entityEventService == null) {
			entityEventService = DynamicProxy.newInstance(new EntityEventServiceImpl());
		}
		return entityEventService;
	}

	@Override
	public SubmissionFormService getSubmissionFormService()
	{
		if (submissionFormService == null) {
			submissionFormService = DynamicProxy.newInstance(new SubmissionFormServiceImpl());
		}
		return submissionFormService;
	}

	public static class Test
	{

		private static AtomicBoolean isTestPersistenceService = new AtomicBoolean(false);
		private static AtomicBoolean isMockPersistenceService = new AtomicBoolean(false);
		private static PersistenceService mockPersistanceService = null;

		public static void setPersistenceServiceToTest()
		{
			isTestPersistenceService.set(true);
		}

		public static void setPersistenceServiceToMock(PersistenceService persistanceService)
		{
			isMockPersistenceService.set(true);
			mockPersistanceService = persistanceService;
		}

		public static void clearPersistenceMock()
		{
			isMockPersistenceService.set(false);
			mockPersistanceService = null;
		}

		public static void clearTest()
		{
			isTestPersistenceService.set(false);
		}
	}

	public void setPersistenceService(PersistenceService persistenceService)
	{
		this.persistenceService = persistenceService;
	}

	public RepoFactory getRepoFactory()
	{
		return repoFactory;
	}

	public void setRepoFactory(RepoFactory repoFactory)
	{
		this.repoFactory = repoFactory;
	}

}
