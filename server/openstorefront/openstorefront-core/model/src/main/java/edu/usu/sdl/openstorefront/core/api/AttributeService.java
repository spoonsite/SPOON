/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.AttributeXRefType;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryOption;
import edu.usu.sdl.openstorefront.core.model.Architecture;
import edu.usu.sdl.openstorefront.core.model.AttributeAll;
import edu.usu.sdl.openstorefront.core.model.AttributeXrefModel;
import edu.usu.sdl.openstorefront.core.view.AttributeCodeSave;
import edu.usu.sdl.openstorefront.core.view.AttributeCodeWrapper;
import edu.usu.sdl.openstorefront.core.view.AttributeFilterParams;
import edu.usu.sdl.openstorefront.core.view.AttributeTypeWrapper;
import edu.usu.sdl.openstorefront.core.view.AttributeXRefView;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface AttributeService
		extends AsyncService
{

	/**
	 * This is cached set of codes (post filters the status) This will be clear
	 * upon data modification
	 *
	 * @param activeStatus
	 * @return codes
	 */
	public List<AttributeCode> getAllAttributeCodes(String activeStatus);

	/**
	 * This used to page filter results
	 *
	 * @param filter
	 * @return
	 */
	public AttributeTypeWrapper getFilteredTypes(AttributeFilterParams filter);

	/**
	 * This used to page filter results
	 *
	 * @param filter
	 * @param type
	 * @return
	 */
	public AttributeCodeWrapper getFilteredCodes(AttributeFilterParams filter, String type);

	/**
	 * Checks if a code exists for a given type
	 *
	 * @param type a string representing the attribute type
	 * @param code a string representing the attribute code
	 * @return true if code the code exists for the given type
	 */
	public Boolean checkIfCodeExistsForType(String type, String code);

	/**
	 * Gets the codes for a type Note active codes are cached.
	 *
	 * @param type
	 * @param all
	 * @return if type doesn't exist it return empty list
	 */
	public List<AttributeCode> findCodesForType(String type, boolean all);

	/**
	 * Gets the active code for a type
	 *
	 * @param type
	 * @return if type doesn't exist it return empty list
	 */
	public List<AttributeCode> findCodesForType(String type);

	/**
	 * This will lookup code in an efficient matter
	 *
	 * @param pk
	 * @return code or null if not found
	 */
	public AttributeCode findCodeForType(AttributeCodePk pk);

	/**
	 * This will lookup type in an efficient matter
	 *
	 * @param type
	 * @return
	 */
	public AttributeType findType(String type);

	/**
	 * Saves type and Updates Indexes
	 *
	 * @param attributeType
	 */
	public void saveAttributeType(AttributeType attributeType);

	/**
	 * Saves type
	 *
	 * @param attributeType
	 * @param updateIndexes (For Searching)
	 */
	public void saveAttributeType(AttributeType attributeType, boolean updateIndexes);

	/**
	 * Saves code
	 *
	 * @param attributeCode
	 * @return
	 */
	public ValidationResult saveAttributeCode(AttributeCode attributeCode);

	/**
	 * Saves code and Updates Indexes
	 *
	 * @param attributeCode
	 * @param updateIndexes (For Searching)
	 * @return
	 */
	public ValidationResult saveAttributeCode(AttributeCode attributeCode, boolean updateIndexes);

	/**
	 * Save user generated code which require special handling
	 *
	 * @param attributeCodeSave
	 * @return Attribute Codes changed
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public List<AttributeCode> saveUserCodes(AttributeCodeSave attributeCodeSave);

	/**
	 * InActivates Type. Also it will inactive associated componentAttribute
	 *
	 * @param type
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void removeAttributeType(String type);

	/**
	 * Warning: This hard deletes of Attribute Type and all codes and associated
	 * information
	 *
	 * @param type
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void cascadeDeleteAttributeType(String type);

	/**
	 * Activates Type; Also it will inactive associated componentAttribute
	 *
	 * @param type
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void activateAttributeType(String type);

	/**
	 * Inactivate Code; Also it will active associated componentAttribute
	 *
	 * @param attributeCodePk
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void removeAttributeCode(AttributeCodePk attributeCodePk);

	/**
	 * Activates a attribute Code. Also it will active associated
	 * componentAttribute
	 *
	 * @param attributeCodePk
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void activateAttributeCode(AttributeCodePk attributeCodePk);

	/**
	 * Warning: This hard deletes of Attribute code and associated information
	 *
	 * @param attributeCodePk
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void cascadeDeleteAttributeCode(AttributeCodePk attributeCodePk);

	/**
	 * Sync the db with the attribute code Map Note this won't remove types
	 * because of the multiple file imports It will remove/inactivate codes.
	 *
	 * @param attributeMap
	 * @return validation results
	 */
	public ValidationResult syncAttribute(Map<AttributeType, List<AttributeCode>> attributeMap);

	/**
	 * Builds and Architecture given a attribute type NOTE: AttributeType must
	 * an architecture with codes in the following format: 1.1.1
	 *
	 * @param attributeType
	 * @return
	 */
	public Architecture generateArchitecture(String attributeType);

	/**
	 * Gets the active xref types for an IntegrationType
	 *
	 * @param attributeXrefModel
	 * @return
	 */
	public List<AttributeXRefType> getAttributeXrefTypes(AttributeXrefModel attributeXrefModel);

	/**
	 * Gets the code mappings
	 *
	 * @return Attribute key, external code, our code
	 */
	public Map<String, Map<String, String>> getAttributeXrefMapFieldMap();

	/**
	 * Save Attribute Mapping
	 *
	 * @param attributeXRefView
	 */
	public void saveAttributeXrefMap(AttributeXRefView attributeXRefView);

	/**
	 * This removes the type and all mappings
	 *
	 * @param attributeType
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void deleteAttributeXrefType(String attributeType);

	/**
	 * Updates the sort order of code
	 *
	 * @param attributeCodePk
	 * @param sortOrder
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveAttributeCodeSortOrder(AttributeCodePk attributeCodePk, Integer sortOrder);

	/**
	 * Update the code and all reference to the old code
	 *
	 * @param attributeCodePk
	 * @param newCode
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void changeAttributeCode(AttributeCodePk attributeCodePk, String newCode);

	/**
	 * Takes a list of AttributeAlls and saves the associated attribute types
	 * and codes.
	 *
	 * @param attributes
	 * @param options
	 * @return validation results
	 */
	public ValidationResult importAttributes(List<AttributeAll> attributes, FileHistoryOption options);

	/**
	 * Saves an attachment to an attribute code
	 *
	 * @param attributeCode
	 * @param fileInput
	 */
	public void saveAttributeCodeAttachment(AttributeCode attributeCode, InputStream fileInput);

	/**
	 * Removes an attachment to an attribute code
	 *
	 * @param attributeCode
	 */
	public void removeAttributeCodeAttachment(AttributeCode attributeCode);

	/**
	 * gets the required attributes giving a component type The Attribute Type
	 * must have codes or allow user generated code for this return the
	 * attribute type.
	 *
	 * @param componentType
	 * @param submissionTypesOnly
	 * @return
	 */
	public List<AttributeType> findRequiredAttributes(String componentType, boolean submissionTypesOnly);

	/**
	 * @See findRequiredAttributes(String componentType, boolean
	 * submissionTypesOnly)
	 * @param componentType
	 * @param submissionTypesOnly
	 * @param skipFilterNoCodes
	 * @return
	 */
	public List<AttributeType> findRequiredAttributes(String componentType, boolean submissionTypesOnly, boolean skipFilterNoCodes);

	/**
	 * gets the optional attributes giving a component type
	 *
	 * @param componentType
	 * @param submissionTypesOnly
	 * @return
	 */
	public List<AttributeType> findOptionalAttributes(String componentType, boolean submissionTypesOnly);

}
