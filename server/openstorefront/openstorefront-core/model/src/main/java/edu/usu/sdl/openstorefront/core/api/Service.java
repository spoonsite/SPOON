package edu.usu.sdl.openstorefront.core.api;

import edu.usu.sdl.openstorefront.core.api.model.TaskRequest;

/**
 * Entry point into the services.
 *
 * @author dshurtleff
 */
public interface Service
{

	/**
	 * Handles alert entities and services
	 *
	 * @return
	 */
	public AlertService getAlertService();

	/**
	 * Handles attributes
	 *
	 * @return
	 */
	public AttributeService getAttributeService();

	/**
	 * Allows for asynchronous service calls.
	 *
	 * @param <T>
	 * @param originalProxy
	 * @return
	 */
	public <T extends AsyncService> T getAsyncProxy(T originalProxy);

	/**
	 * Allows for asynchronous service calls with handling options
	 *
	 * @param <T>
	 * @param originalProxy
	 * @param taskRequest
	 * @return
	 */
	public <T extends AsyncService> T getAsyncProxy(T originalProxy, TaskRequest taskRequest);

	/**
	 * Component services
	 *
	 * @return
	 */
	public ComponentService getComponentService();

	/**
	 * Handles all lookup tables
	 *
	 * @return
	 */
	public LookupService getLookupService();

	/**
	 * Handles organizations
	 *
	 * @return
	 */
	public OrganizationService getOrganizationService();

	/**
	 * Handles plugins
	 *
	 * @return
	 */
	public PluginService getPluginService();

	/**
	 * Handles persistence (generally just for querying...use services for data
	 * manipulation)
	 *
	 * @return
	 */
	public PersistenceService getPersistenceService();

	/**
	 * This gets a new persistence service for DB connection isolation
	 * (Transactions isolation)
	 *
	 * @return
	 */
	public PersistenceService getNewPersistenceService();

	/**
	 * Handles Reporting
	 *
	 * @return
	 */
	public ReportService getReportService();

	/**
	 * Handles Searching
	 *
	 * @return
	 */
	public SearchService getSearchService();

	/**
	 * Handles system and related entities
	 *
	 * @return
	 */
	public SystemService getSystemService();

	/**
	 * Handles system and related entities
	 *
	 * @return
	 */
	public SecurityService getSecurityService();

	/**
	 * Handles users
	 *
	 * @return
	 */
	public UserService getUserService();

	/**
	 * Handles importing data and file history
	 *
	 * @return
	 */
	public ImportService getImportService();

	/**
	 * Handles Notification Events
	 *
	 * @return
	 */
	public BrandingService getBrandingService();

	/**
	 * Handles Notification Events
	 *
	 * @return
	 */
	public NotificationService getNotificationService();

	/**
	 * Handles Feedback
	 *
	 * @return
	 */
	public FeedbackService getFeedbackService();

	/**
	 * Handles all Contacts
	 *
	 * @return
	 */
	public ContactService getContactService();

	/**
	 * Handles evaluation entities
	 *
	 * @return
	 */
	public EvaluationService getEvaluationService();

	/**
	 * Handles evaluation entities
	 *
	 * @return
	 */
	public ContentSectionService getContentSectionService();

	/**
	 * Handles evaluation entities
	 *
	 * @return
	 */
	public ChecklistService getChecklistService();

	/**
	 * Handles logging entity changes
	 *
	 * @return
	 */
	public ChangeLogService getChangeLogService();

	/**
	 * System archive service
	 *
	 * @return
	 */
	public SystemArchiveService getSystemArchiveService();

	/**
	 * Help Support entities
	 *
	 * @return
	 */
	public HelpSupportService getHelpSupportService();

}
