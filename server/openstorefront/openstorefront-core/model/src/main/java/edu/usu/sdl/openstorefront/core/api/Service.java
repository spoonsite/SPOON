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
	 * Handles users
	 *
	 * @return
	 */
	public UserService getUserService();

}
