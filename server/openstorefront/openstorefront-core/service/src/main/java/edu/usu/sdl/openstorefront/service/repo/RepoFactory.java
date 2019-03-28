/*
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.repo;

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.service.repo.api.AttributeRepo;
import edu.usu.sdl.openstorefront.service.repo.api.ComponentRepo;
import edu.usu.sdl.openstorefront.service.repo.api.EvaluationRepo;
import edu.usu.sdl.openstorefront.service.repo.api.NotificationRepo;
import edu.usu.sdl.openstorefront.service.repo.api.OrganizationRepo;
import edu.usu.sdl.openstorefront.service.repo.api.StandardEntityRepo;
import edu.usu.sdl.openstorefront.service.repo.api.UserRepo;

/**
 *
 * @author dshurtleff
 */
public class RepoFactory
{

	private PropertiesManager propertiesManager;

	public RepoFactory()
	{
		propertiesManager = PropertiesManager.getInstance();
	}

	public RepoFactory(PropertiesManager propertiesManager)
	{
		this.propertiesManager = propertiesManager;
	}

	private boolean useMongo()
	{
		return Convert.toBoolean(propertiesManager.getValue(PropertiesManager.KEY_DB_USE_MONGO, "false"));
	}

	public StandardEntityRepo getStandardEntityRepo()
	{
		StandardEntityRepo standardEntityRepo;
		if (useMongo()) {
			standardEntityRepo = new StandardEntityMongoRepoImpl();
		} else {
			standardEntityRepo = new StandardEntityOrientRepoImpl();
		}
		return standardEntityRepo;
	}

	public ComponentRepo getComponentRepo()
	{
		ComponentRepo componentRepo;
		if (useMongo()) {
			componentRepo = new ComponentMongoRepoImpl();
		} else {
			componentRepo = new ComponentOrientRepoImpl();
		}
		return componentRepo;
	}

	public NotificationRepo getNotificationRepo()
	{
		NotificationRepo notificationRepo;
		if (useMongo()) {
			notificationRepo = new NotificationMongoRepoImpl();
		} else {
			notificationRepo = new NotificationOrientRepoImpl();
		}
		return notificationRepo;
	}

	public OrganizationRepo getOrganizationRepo()
	{
		OrganizationRepo organizationRepo;
		if (useMongo()) {
			organizationRepo = new OrganizationMongoRepoImpl();
		} else {
			organizationRepo = new OrganizationOrientRepoImpl();
		}
		return organizationRepo;
	}

	public UserRepo getUserRepo()
	{
		UserRepo userRepo;
		if (useMongo()) {
			userRepo = new UserMongoRepoImpl();
		} else {
			userRepo = new UserOrientRepoImpl();
		}
		return userRepo;
	}

	public AttributeRepo getAttributeRepo()
	{
		AttributeRepo attributeRepo;
		if (useMongo()) {
			attributeRepo = new AttributeMongoRepoImpl();
		} else {
			attributeRepo = new AttributeOrientRepoImpl();
		}
		return attributeRepo;
	}

	public EvaluationRepo getEvaluationRepo()
	{
		EvaluationRepo evaluationRepo;
		if (useMongo()) {
			evaluationRepo = new EvaluationMongoRepoImpl();
		} else {
			evaluationRepo = new EvaluationOrientRepoImpl();
		}
		return evaluationRepo;
	}

}
