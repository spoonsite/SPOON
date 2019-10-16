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
package edu.usu.sdl.openstorefront.service.repo.api;

import edu.usu.sdl.openstorefront.core.api.PersistenceService;
import edu.usu.sdl.openstorefront.core.entity.Organization;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public interface OrganizationRepo
{

	/**
	 * Find References that have no organization
	 *
	 * @param <T>
	 * @param entity
	 * @return
	 */
	<T> List<T> findReferencesNoOrg(T entity);

	/**
	 * Organization update requires special handling to deal with
	 *
	 * @param existing
	 * @param updateOrganization
	 * @return
	 */
	Organization handleOrganizationUpdate(PersistenceService persistenceService, Organization existing, Organization updateOrganization);

}
