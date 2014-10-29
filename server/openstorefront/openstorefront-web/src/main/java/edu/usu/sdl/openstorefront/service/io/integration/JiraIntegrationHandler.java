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
package edu.usu.sdl.openstorefront.service.io.integration;

import com.atlassian.jira.rest.client.api.domain.Issue;
import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.manager.JiraManager;
import edu.usu.sdl.openstorefront.service.manager.resource.JiraClient;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.ComponentIntegrationConfig;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class JiraIntegrationHandler
		extends BaseIntegrationHandler
{

	public static final String STATUS_FIELD = "Status";

	private static final Logger log = Logger.getLogger(JiraIntegrationHandler.class.getName());

	public JiraIntegrationHandler(ComponentIntegrationConfig integrationConfig)
	{
		super(integrationConfig);
	}

	@Override
	public void processConfig()
	{
		log.finer("Pull Component");
		Component component = serviceProxy.getPersistenceService().findById(Component.class, integrationConfig.getComponentId());

		log.finer("Pull Ticket");
		try (JiraClient jiraClient = JiraManager.getClient()) {
			Issue issue = jiraClient.getTicket(integrationConfig.getIssueNumber());
			if (issue != null) {
				serviceProxy.getComponentServicePrivate().mapComponentAttributes(issue, integrationConfig);
			} else {
				throw new OpenStorefrontRuntimeException("Unable to find Jira Ticket: " + integrationConfig.getIssueNumber() + " for component " + component.getName(), "Check the config also check Jira.");
			}
		}
	}

}
