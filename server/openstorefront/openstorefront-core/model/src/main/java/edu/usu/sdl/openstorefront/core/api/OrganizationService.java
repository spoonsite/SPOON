/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.core.api;

import edu.usu.sdl.openstorefront.common.exception.AttachedReferencesException;
import edu.usu.sdl.openstorefront.core.entity.Organization;
import edu.usu.sdl.openstorefront.core.model.OrgReference;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public interface OrganizationService
		extends AsyncService
{

	/**
	 * Adds an organization if it doesn't already exist.
	 *
	 * @param organizationName (This should be the Name and not the organization
	 * id)
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void addOrganization(String organizationName);

	/**
	 * Save the whole record
	 *
	 * @param organization
	 * @return Saved record
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public Organization saveOrganization(Organization organization);

	/**
	 * Save organization and logo
	 *
	 * @param organization
	 * @param fileInput
	 */
	public void saveOrganizationLogo(Organization organization, InputStream fileInput);

	/**
	 * Deletes an Organization Logo
	 *
	 * @param organizationId
	 */
	public void deleteOrganizationLogo(String organizationId);

	/**
	 * Find all organizations in the data and updates organization table
	 */
	public void extractOrganizations();

	/**
	 * Merge to organizations and update all related data and then remove the
	 * merged Org.
	 *
	 * @param targetOrganizationId
	 * @param toMergeOrganizationId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void mergeOrganizations(String targetOrganizationId, String toMergeOrganizationId);

	/**
	 * Gathers all references for an organization
	 *
	 * @param organization (if null or blank it will return records with no
	 * organization set) OrganizationId or Name
	 * @param onlyActive
	 * @param onlyApprovedComponents
	 * @return references
	 */
	public List<OrgReference> findReferences(String organization, boolean onlyActive, boolean onlyApprovedComponents);

	/**
	 * Deletes an Organization only if there no references exist
	 *
	 * @param organizationId
	 * @throws
	 * edu.usu.sdl.openstorefront.common.exception.AttachedReferencesException
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void removeOrganization(String organizationId) throws AttachedReferencesException;

}
