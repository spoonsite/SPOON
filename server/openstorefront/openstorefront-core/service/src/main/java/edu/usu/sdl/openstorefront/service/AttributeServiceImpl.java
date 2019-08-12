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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.api.AttributeService;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.QueryType;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.AlertType;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.AttributeXRefMap;
import edu.usu.sdl.openstorefront.core.entity.AttributeXRefType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentTypeRestriction;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryOption;
import edu.usu.sdl.openstorefront.core.entity.LookupEntity;
import edu.usu.sdl.openstorefront.core.entity.ReportOption;
import edu.usu.sdl.openstorefront.core.entity.ScheduledReport;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormField;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormFieldType;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormSection;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormTemplate;
import edu.usu.sdl.openstorefront.core.model.AlertContext;
import edu.usu.sdl.openstorefront.core.model.Architecture;
import edu.usu.sdl.openstorefront.core.model.AttributeAll;
import edu.usu.sdl.openstorefront.core.model.AttributeXrefModel;
import edu.usu.sdl.openstorefront.core.model.BulkComponentAttributeChange;
import edu.usu.sdl.openstorefront.core.sort.ArchitectureComparator;
import edu.usu.sdl.openstorefront.core.util.EntityUtil;
import edu.usu.sdl.openstorefront.core.view.AttributeCodeSave;
import edu.usu.sdl.openstorefront.core.view.AttributeCodeView;
import edu.usu.sdl.openstorefront.core.view.AttributeCodeWrapper;
import edu.usu.sdl.openstorefront.core.view.AttributeFilterParams;
import edu.usu.sdl.openstorefront.core.view.AttributeTypeAdminView;
import edu.usu.sdl.openstorefront.core.view.AttributeTypeWrapper;
import edu.usu.sdl.openstorefront.core.view.AttributeXRefView;
import edu.usu.sdl.openstorefront.core.view.NewAttributeCode;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.api.AttributeServicePrivate;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import edu.usu.sdl.openstorefront.service.search.SearchStatTable;
import edu.usu.sdl.openstorefront.validation.CleanKeySanitizer;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.sf.ehcache.Element;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Handles Attribute information
 */
public class AttributeServiceImpl
		extends ServiceProxy
		implements AttributeService, AttributeServicePrivate
{

	private static final Logger LOG = Logger.getLogger(AttributeServiceImpl.class.getName());

	@Override
	@SuppressWarnings("unchecked")
	public List<AttributeCode> getAllAttributeCodes(String activeStatus)
	{
		List<AttributeCode> attributeCodes;
		Element element = OSFCacheManager.getAttributeCodeAllCache().get(OSFCacheManager.ALLCODE_KEY);
		if (element != null) {
			attributeCodes = (List<AttributeCode>) element.getObjectValue();
		} else {
			// This may pull all of records (right now we are try to cache everything to speed lookup
			// throughout the system.  Later, you can consider improving effeciency by determining the
			// set of data really need to be gathered.
			attributeCodes = persistenceService.queryByExample(new AttributeCode());
			element = new Element(OSFCacheManager.ALLCODE_KEY, attributeCodes);
			OSFCacheManager.getAttributeCodeAllCache().put(element);

			List<AttributeCode> activeCodesOnly = attributeCodes.stream()
					.filter(code -> code.getActiveStatus().equals(AttributeCode.ACTIVE_STATUS))
					.collect(Collectors.toList());
			warmAttributeCaches(activeCodesOnly);
		}
		if (StringUtils.isNotBlank(activeStatus)) {
			attributeCodes = attributeCodes.stream().filter(code -> code.getActiveStatus().equals(activeStatus)).collect(Collectors.toList());
		}
		return attributeCodes;
	}

	@Override
	public Boolean checkIfCodeExistsForType(String type, String code)
	{
		List<AttributeCode> codes = findCodesForType(type);
		for (AttributeCode c : codes) {
			if (c.getAttributeCodePk().getAttributeCode().equals(code)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String crushGeneralNumericString(String inputNumber)
	{
		try {
			// Try to cast... if this fails then catch the error.
			BigDecimal bigDecimal = new BigDecimal(inputNumber);
		} catch (NumberFormatException e) {
			return inputNumber;
		}
		// If the number contains an E or e return.
		Boolean inputNumberIsScientificNotation = false;
		if (inputNumber.indexOf('E') != -1) {
			inputNumberIsScientificNotation = true;
		}
		if (inputNumber.indexOf('e') != -1) {
			inputNumberIsScientificNotation = true;
		}

		Boolean magnitudeIsGreaterThanOne = false;
		int numberLength = inputNumber.length();

		BigDecimal bigDecimalCast = new BigDecimal(inputNumber);
		BigDecimal oneValue = new BigDecimal(1);

		int result = bigDecimalCast.abs().compareTo(oneValue);
		// result =  0; if bigDecimalCast and oneValue are equal.
		// result =  1; if bigDecimalCast is greater than oneValue.
		// result = -1; if bigDecimalCast is less than oneValue.

		// Is bigDecimalCast greater than one?
		if (result >= 0) {
			magnitudeIsGreaterThanOne = true;
		}

		if (inputNumberIsScientificNotation) {
			inputNumber = bigDecimalCast.toPlainString();
			numberLength = inputNumber.length();
		}

		if (magnitudeIsGreaterThanOne) {
			if (inputNumber.indexOf('.') != -1) {
				if ((numberLength - inputNumber.indexOf('.')) > 5) {
					// only show 3 decimal places after the decimal point
					return inputNumber.substring(0, inputNumber.indexOf('.') + 4);
				}
				return inputNumber;
			}
		}

		if (!magnitudeIsGreaterThanOne) {
			// Find first non zero thing after the decimal and show 3 decimal places after it.
			int firstNonZeroIndex = 0;
			for (int i = 0; i < numberLength; i++) {
				if ((inputNumber.charAt(i) == '-') || (inputNumber.charAt(i) == '.') || (inputNumber.charAt(i) == '0')) {
					continue;
				}
				firstNonZeroIndex = i;
				break;
			}
			if (numberLength - firstNonZeroIndex > 5) {
				BigDecimal bd = new BigDecimal(Double.parseDouble(inputNumber.substring(0, firstNonZeroIndex + 4))).setScale(firstNonZeroIndex + 1, RoundingMode.HALF_UP);
				return Double.toString(bd.doubleValue());
			}
			return inputNumber;
		}
		return inputNumber;
	}

	@Override
	public List<AttributeCode> findCodesForType(String type)
	{
		return findCodesForType(type, false);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AttributeCode> findCodesForType(String type, boolean all)
	{
		List<AttributeCode> attributeCodes;
		if (all) {
			AttributeCode attributeCodeExample = new AttributeCode();
			AttributeCodePk attributeCodePk = new AttributeCodePk();
			attributeCodePk.setAttributeType(type);
			attributeCodeExample.setAttributeCodePk(attributeCodePk);
			attributeCodes = persistenceService.queryByExample(new QueryByExample<>(attributeCodeExample));
		} else {
			Element element;
			element = OSFCacheManager.getAttributeCache().get(type);
			if (element != null) {
				attributeCodes = (List<AttributeCode>) element.getObjectValue();
			} else {

				AttributeCode attributeCodeExample = new AttributeCode();
				AttributeCodePk attributeCodePk = new AttributeCodePk();
				attributeCodePk.setAttributeType(type);
				attributeCodeExample.setAttributeCodePk(attributeCodePk);
				attributeCodeExample.setActiveStatus(AttributeCode.ACTIVE_STATUS);

				attributeCodes = persistenceService.queryByExample(new QueryByExample<>(attributeCodeExample));
				element = new Element(type, attributeCodes);
				OSFCacheManager.getAttributeCache().put(element);
			}
		}
		return attributeCodes;
	}

	@Override
	public void saveAttributeType(AttributeType attributeType)
	{
		saveAttributeType(attributeType, true);
	}

	@Override
	public void saveAttributeType(AttributeType attributeType, boolean updateIndexes)
	{
		getAttributeServicePrivate().performSaveAttributeType(attributeType);

		// the advanced search filter needs a way to know when to refresh it's cache 
		// @see{SearchStatTable#isThereNewAttributeTypeSaved}
		// SearchStatTable.setThereIsNewAttributeTypeSaved(true);
		SearchStatTable.isThereNewAttributeTypeSaved = true;

		if (updateIndexes) {
			ComponentAttributePk componentAttributePk = new ComponentAttributePk();
			componentAttributePk.setAttributeType(attributeType.getAttributeType());
			ComponentAttribute componentAttribute = new ComponentAttribute();
			componentAttribute.setComponentAttributePk(componentAttributePk);
			List<ComponentAttribute> componentAttributes = getPersistenceService().queryByExample(componentAttribute);

			List<Component> components = new ArrayList<>();
			componentAttributes.stream().forEach((attr) -> {
				components.add(persistenceService.findById(Component.class, attr.getComponentAttributePk().getComponentId()));
			});
			getSearchService().indexComponents(components);
		}
	}

	@Override
	public void performSaveAttributeType(AttributeType attributeType)
	{
		attributeType.applyDefaultValues();

		AttributeType existing = persistenceService.findById(AttributeType.class, attributeType.getAttributeType());

		ValidationResult validationResult = attributeType.customValidation();
		if (validationResult.valid() == false) {
			throw new OpenStorefrontRuntimeException(validationResult.toString());
		}

		if (existing != null) {
			//remove to inactivate
			existing.updateFields(attributeType);
			persistenceService.persist(existing);
		} else {
			attributeType.populateBaseCreateFields();
			persistenceService.persist(attributeType);
		}
		cleanCaches(attributeType.getAttributeType());
	}

	private void cleanCaches(String attributeType)
	{
		OSFCacheManager.getAttributeCache().remove(attributeType);
		OSFCacheManager.getAttributeTypeCache().remove(attributeType);
		OSFCacheManager.getAttributeCodeAllCache().removeAll();
	}

	@Override
	public ValidationResult saveAttributeCode(AttributeCode attributeCode)
	{
		return saveAttributeCode(attributeCode, true);
	}

	@Override
	public ValidationResult saveAttributeCode(AttributeCode attributeCode, boolean updateIndexes)
	{
		ValidationResult validationResult = getAttributeServicePrivate().performSaveAttributeCode(attributeCode);

		if (updateIndexes && validationResult.valid()) {
			ComponentAttributePk pk = new ComponentAttributePk();
			pk.setAttributeType(attributeCode.getAttributeCodePk().getAttributeType());
			pk.setAttributeCode(attributeCode.getAttributeCodePk().getAttributeCode());
			ComponentAttribute example = new ComponentAttribute();
			example.setComponentAttributePk(pk);

			List<ComponentAttribute> componentAttributes = getPersistenceService().queryByExample(new QueryByExample<>(example));

			List<Component> components = new ArrayList<>();
			componentAttributes.stream().forEach((attr) -> {
				components.add(persistenceService.findById(Component.class, attr.getComponentAttributePk().getComponentId()));
			});
			getSearchService().indexComponents(components);
		}
		return validationResult;
	}

	@Override
	public ValidationResult performSaveAttributeCode(AttributeCode attributeCode)
	{
		AttributeCode existing = persistenceService.findById(AttributeCode.class, attributeCode.getAttributeCodePk());

		ValidationResult validationResult = attributeCode.customValidation(this);
		if (validationResult.valid()) {
			if (existing != null) {
				existing.updateFields(attributeCode);
				persistenceService.persist(existing);
			} else {
				attributeCode.populateBaseCreateFields();
				persistenceService.persist(attributeCode);
			}
			cleanCaches(attributeCode.getAttributeCodePk().getAttributeType());
		}
		return validationResult;
	}

	@Override
	public void saveAttributeCodeAttachment(AttributeCode attributeCode, InputStream fileInput)
	{
		Objects.requireNonNull(attributeCode);
		Objects.requireNonNull(attributeCode.getAttributeCodePk());
		Objects.requireNonNull(fileInput);

		AttributeCode existing = persistenceService.findById(AttributeCode.class, attributeCode.getAttributeCodePk());
		if (existing != null) {
			if (StringUtils.isNotBlank(attributeCode.getAttachmentOriginalFileName())) {
				existing.setAttachmentOriginalFileName(attributeCode.getAttachmentOriginalFileName());
			}
			if (StringUtils.isNotBlank(attributeCode.getAttachmentMimeType())) {
				existing.setAttachmentMimeType(attributeCode.getAttachmentMimeType());
			}
			existing.setAttachmentFileName(existing.getAttributeCodePk().toKey().replace("#", "-"));

			try (InputStream in = fileInput) {
				Files.copy(in, existing.pathToAttachment(), StandardCopyOption.REPLACE_EXISTING);
				persistenceService.persist(existing);
			} catch (IOException ex) {
				throw new OpenStorefrontRuntimeException("Unable to store attachment.", "Contact System Admin.  Check file permissions and disk space ", ex);
			}
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find attribute code.", "Check code: " + attributeCode.getAttributeCodePk());
		}
	}

	@Override
	public void removeAttributeCodeAttachment(AttributeCode attributeCode)
	{
		Objects.requireNonNull(attributeCode);

		//Warning in a transaction this can cause the unexpected behavior
		//do the delete seperately
		deleteCodeAttachment(attributeCode);

		attributeCode.setAttachmentFileName("");
		attributeCode.setAttachmentMimeType("");
		attributeCode.setAttachmentOriginalFileName("");
		persistenceService.persist(attributeCode);

	}

	private void deleteCodeAttachment(AttributeCode attributeCode)
	{
		Objects.requireNonNull(attributeCode);

		Path path = attributeCode.pathToAttachment();
		if (path != null && path.toFile().exists()) {
			boolean deleted = path.toFile().delete();
			if (deleted == false) {
				LOG.log(Level.WARNING, MessageFormat.format("Unable to delete attribute attatchment. File might be in use. Path: {0}", path.toString()));
			}
		}
	}

	private String attributeLabelToCode(String code)
	{
		CleanKeySanitizer sanitizer = new CleanKeySanitizer();
		return sanitizer.santize(StringUtils.left(code.toUpperCase(), OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)).toString();
	}

	@Override
	public List<AttributeCode> saveUserCodes(AttributeCodeSave attributeCodeSave)
	{
		List<AttributeCode> updatedCodes = new ArrayList<>();
		for (NewAttributeCode saveCode : attributeCodeSave.getUserAttributes()) {

			String key = attributeLabelToCode(saveCode.getAttributeCodeLabel());

			AttributeCode newAttributeCode = new AttributeCode();
			newAttributeCode.setLabel(saveCode.getAttributeCodeLabel());
			AttributeCodePk newAttributeCodePk = new AttributeCodePk();
			newAttributeCodePk.setAttributeType(saveCode.getAttributeType());
			newAttributeCodePk.setAttributeCode(key);
			newAttributeCode.setAttributeCodePk(newAttributeCodePk);

			AttributeType attributeType = getPersistenceService().findById(AttributeType.class, saveCode.getAttributeType());
			if (attributeType != null) {
				// The attribute type must allow user-generated codes to continue
				if (Convert.toBoolean(attributeType.getAllowUserGeneratedCodes())) {

					//see if it already exist...if so do nothing. So we don't alert.
					AttributeCode existing = getPersistenceService().findById(AttributeCode.class, newAttributeCodePk);
					if (existing == null) {
						ValidationModel validationModel = new ValidationModel(newAttributeCode);
						validationModel.setConsumeFieldsOnly(true);
						ValidationResult validationResult = ValidationUtil.validate(validationModel);
						if (validationResult.valid()) {
							validationResult = saveAttributeCode(newAttributeCode, false);

							if (validationResult.valid()) {
								AlertContext alertContext = new AlertContext();
								alertContext.setAlertType(AlertType.USER_DATA);
								alertContext.setDataTrigger(newAttributeCode);
								getAlertService().checkAlert(alertContext);
								updatedCodes.add(newAttributeCode);
							}
						}
						if (!validationResult.valid()) {
							LOG.log(Level.WARNING, MessageFormat.format("Attribute Code (label) failed: {0} Message: {1}", new Object[]{newAttributeCode.getLabel(), validationResult.toString()}));
						}
					} else {
						updatedCodes.add(newAttributeCode);
					}
				} else {
					LOG.log(Level.WARNING, MessageFormat.format("Attribute type does not support user codes Type: {0}", saveCode.getAttributeType()));
				}
			} else {
				LOG.log(Level.WARNING, MessageFormat.format("Unable to find attribute type: {0}", saveCode.getAttributeType()));
			}
		}

		return updatedCodes;
	}

	@Override
	public void removeAttributeType(String type
	)
	{
		Objects.requireNonNull(type, "Type is required.");

		AttributeType attributeType = persistenceService.findById(AttributeType.class, type);
		if (attributeType != null) {
			attributeType.setActiveStatus(AttributeCode.INACTIVE_STATUS);
			attributeType.setUpdateDts(TimeUtil.currentDate());
			attributeType.setUpdateUser(SecurityUtil.getCurrentUserName());
			persistenceService.persist(attributeType);

			BulkComponentAttributeChange bulkComponentAttributeChange = new BulkComponentAttributeChange();
			bulkComponentAttributeChange.setAttributes(getComponentAttributes(type, null));
			bulkComponentAttributeChange.setOpertionType(BulkComponentAttributeChange.OpertionType.INACTIVE);

			//Stay in the same transaction
			(new ComponentServiceImpl(persistenceService)).bulkComponentAttributeChange(bulkComponentAttributeChange);

			cleanCaches(type);
		}
	}

	private List<ComponentAttribute> getComponentAttributes(String type, String code)
	{
		ComponentAttribute componentAttributeExample = new ComponentAttribute();
		ComponentAttributePk componentAttributePk = new ComponentAttributePk();
		componentAttributePk.setAttributeType(type);
		componentAttributePk.setAttributeCode(code);
		componentAttributeExample.setComponentAttributePk(componentAttributePk);
		QueryByExample queryByExample = new QueryByExample<>(componentAttributeExample);
		queryByExample.setReturnNonProxied(false);
		return persistenceService.queryByExample(queryByExample);
	}

	@Override
	public void removeAttributeCode(AttributeCodePk attributeCodePk)
	{
		Objects.requireNonNull(attributeCodePk, "AttributeCodePk is required.");

		AttributeCode attributeCode = persistenceService.findById(AttributeCode.class, attributeCodePk);
		if (attributeCode != null) {
			attributeCode.setActiveStatus(AttributeCode.INACTIVE_STATUS);
			attributeCode.setUpdateDts(TimeUtil.currentDate());
			attributeCode.setUpdateUser(SecurityUtil.getCurrentUserName());
			persistenceService.persist(attributeCode);

			BulkComponentAttributeChange bulkComponentAttributeChange = new BulkComponentAttributeChange();
			bulkComponentAttributeChange.setAttributes(getComponentAttributes(attributeCodePk.getAttributeType(), attributeCodePk.getAttributeCode()));
			bulkComponentAttributeChange.setOpertionType(BulkComponentAttributeChange.OpertionType.INACTIVE);
			(new ComponentServiceImpl(persistenceService)).bulkComponentAttributeChange(bulkComponentAttributeChange);

			cleanCaches(attributeCodePk.getAttributeType());
		}
	}

	@Override
	public void cascadeDeleteAttributeType(String type)
	{
		Objects.requireNonNull(type, "Attribute type is required.");

		AttributeType attributeType = persistenceService.findById(AttributeType.class, type);
		if (attributeType != null) {

			AttributeCode attributeCodeExample = new AttributeCode();
			AttributeCodePk attributeCodePk = new AttributeCodePk();
			attributeCodePk.setAttributeType(type);
			attributeCodeExample.setAttributeCodePk(attributeCodePk);

			// Remove attachments
			List<AttributeCode> attributeCodes = persistenceService.queryByExample(attributeCodeExample);
			for (AttributeCode attributeCode : attributeCodes) {
				deleteCodeAttachment(attributeCode);
			}
			persistenceService.deleteByExample(attributeCodeExample);

			deleteAttributeXrefType(type);

			ScheduledReport scheduledReport = new ScheduledReport();
			ReportOption reportOption = new ReportOption();
			reportOption.setCategory(type);
			scheduledReport.setReportOption(reportOption);
			persistenceService.deleteByExample(scheduledReport);

			persistenceService.delete(attributeType);

			BulkComponentAttributeChange bulkComponentAttributeChange = new BulkComponentAttributeChange();
			bulkComponentAttributeChange.setAttributes(getComponentAttributes(type, null));
			bulkComponentAttributeChange.setOpertionType(BulkComponentAttributeChange.OpertionType.DELETE);
			(new ComponentServiceImpl(persistenceService)).bulkComponentAttributeChange(bulkComponentAttributeChange);

			cleanCaches(type);
		}
	}

	@Override
	public void cascadeDeleteAttributeCode(AttributeCodePk attributeCodePk)
	{
		Objects.requireNonNull(attributeCodePk, "AttributeCodePk is required.");

		AttributeCode attributeCode = persistenceService.findById(AttributeCode.class, attributeCodePk);
		if (attributeCode != null) {

			deleteCodeAttachment(attributeCode);

			AttributeXRefMap example = new AttributeXRefMap();
			example.setAttributeType(attributeCodePk.getAttributeType());
			example.setLocalCode(attributeCodePk.getAttributeCode());
			persistenceService.deleteByExample(example);

			persistenceService.delete(attributeCode);

			BulkComponentAttributeChange bulkComponentAttributeChange = new BulkComponentAttributeChange();
			bulkComponentAttributeChange.setAttributes(getComponentAttributes(attributeCodePk.getAttributeType(), attributeCodePk.getAttributeCode()));
			bulkComponentAttributeChange.setOpertionType(BulkComponentAttributeChange.OpertionType.DELETE);
			(new ComponentServiceImpl(persistenceService)).bulkComponentAttributeChange(bulkComponentAttributeChange);

			cleanCaches(attributeCodePk.getAttributeType());
		}
	}

	@Override
	public void activateAttributeCode(AttributeCodePk attributeCodePk)
	{
		Objects.requireNonNull(attributeCodePk, "AttributeCodePk is required.");

		AttributeCode attributeCode = persistenceService.findById(AttributeCode.class, attributeCodePk);
		if (attributeCode != null) {
			attributeCode.setActiveStatus(AttributeCode.ACTIVE_STATUS);
			attributeCode.setUpdateDts(TimeUtil.currentDate());
			attributeCode.setUpdateUser(SecurityUtil.getCurrentUserName());
			persistenceService.persist(attributeCode);

			BulkComponentAttributeChange bulkComponentAttributeChange = new BulkComponentAttributeChange();
			bulkComponentAttributeChange.setAttributes(getComponentAttributes(attributeCodePk.getAttributeType(), attributeCodePk.getAttributeCode()));
			bulkComponentAttributeChange.setOpertionType(BulkComponentAttributeChange.OpertionType.ACTIVATE);
			(new ComponentServiceImpl(persistenceService)).bulkComponentAttributeChange(bulkComponentAttributeChange);

			cleanCaches(attributeCodePk.getAttributeType());
		}
	}

	@Override
	public ValidationResult syncAttribute(Map<AttributeType, List<AttributeCode>> attributeMap)
	{
		AttributeType attributeTypeExample = new AttributeType();
		List<AttributeType> attributeTypes = persistenceService.queryByExample(new QueryByExample<>(attributeTypeExample));
		Map<String, AttributeType> existingAttributeMap = new HashMap<>();
		attributeTypes.stream().forEach((attributeType) -> {
			existingAttributeMap.put(attributeType.getAttributeType(), attributeType);
		});
		ValidationResult syncResult = new ValidationResult();
		for (AttributeType attributeType : attributeMap.keySet()) {

			try {

				ValidationModel validationModel = new ValidationModel(attributeType);
				validationModel.setConsumeFieldsOnly(true);
				ValidationResult attributeTypeResult = ValidationUtil.validate(validationModel);
				syncResult.merge(attributeTypeResult);
				if (attributeTypeResult.valid()) {
					attributeType.setAttributeType(attributeType.getAttributeType().replace(ReflectionUtil.COMPOSITE_KEY_SEPERATOR, ReflectionUtil.COMPOSITE_KEY_REPLACER));

					AttributeType existing = existingAttributeMap.get(attributeType.getAttributeType());
					if (existing != null) {
						existing.setDescription(attributeType.getDescription());
						existing.setAllowMultipleFlg(attributeType.getAllowMultipleFlg());
						existing.setArchitectureFlg(attributeType.getArchitectureFlg());
						existing.setImportantFlg(attributeType.getImportantFlg());
						existing.setRequiredFlg(attributeType.getRequiredFlg());
						existing.setAllowUserGeneratedCodes(attributeType.getAllowUserGeneratedCodes());
						existing.setVisibleFlg(attributeType.getVisibleFlg());
						existing.setDetailedDescription(attributeType.getDetailedDescription());
						existing.setHideOnSubmission(attributeType.getHideOnSubmission());
						existing.setDefaultAttributeCode(attributeType.getDefaultAttributeCode());
						existing.setActiveStatus(AttributeType.ACTIVE_STATUS);
						existing.setCreateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
						existing.setUpdateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
						saveAttributeType(existing, false);
					} else {
						attributeType.setActiveStatus(AttributeType.ACTIVE_STATUS);
						attributeType.setCreateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
						attributeType.setUpdateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
						saveAttributeType(attributeType, false);
					}

					List<AttributeCode> existingAttributeCodes = findCodesForType(attributeType.getAttributeType());
					Map<String, AttributeCode> existingCodeMap = new HashMap<>();
					for (AttributeCode attributeCode : existingAttributeCodes) {
						existingCodeMap.put(attributeCode.getAttributeCodePk().toKey(), attributeCode);
					}

					Set<String> newCodeSet = new HashSet<>();
					List<AttributeCode> attributeCodes = attributeMap.get(attributeType);
					for (AttributeCode attributeCode : attributeCodes) {
						try {
							ValidationModel validationModelCode = new ValidationModel(attributeCode);
							validationModelCode.setConsumeFieldsOnly(true);
							ValidationResult validationResultCode = ValidationUtil.validate(validationModelCode);
							if (validationResultCode.valid()) {
								attributeCode.getAttributeCodePk().setAttributeCode(attributeCode.getAttributeCodePk().getAttributeCode().replace(ReflectionUtil.COMPOSITE_KEY_SEPERATOR, ReflectionUtil.COMPOSITE_KEY_REPLACER));

								AttributeCode existingCode = existingCodeMap.get(attributeCode.getAttributeCodePk().toKey());
								if (existingCode != null) {
									if (EntityUtil.isObjectsDifferent(existingCode, attributeCode, true)) {
										existingCode.setDescription(attributeCode.getDescription());
										existingCode.setDetailUrl(attributeCode.getDetailUrl());
										existingCode.setLabel(attributeCode.getLabel());
										existingCode.setArchitectureCode(attributeCode.getArchitectureCode());
										existingCode.setBadgeUrl(attributeCode.getBadgeUrl());
										existingCode.setGroupCode(attributeCode.getGroupCode());
										existingCode.setSortOrder(attributeCode.getSortOrder());
										existingCode.setHighlightStyle(attributeCode.getHighlightStyle());
										existingCode.setActiveStatus(AttributeCode.ACTIVE_STATUS);
										existingCode.setCreateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
										existingCode.setUpdateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
										validationResultCode.merge(saveAttributeCode(existingCode, false));
									}
								} else {
									attributeCode.setActiveStatus(AttributeCode.ACTIVE_STATUS);
									attributeCode.setCreateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
									attributeCode.setUpdateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
									validationResultCode.merge(saveAttributeCode(attributeCode, false));
								}
								if (validationResultCode.valid()) {
									newCodeSet.add(attributeCode.getAttributeCodePk().toKey());
								}
							} else {
								LOG.log(Level.WARNING, MessageFormat.format("(Data Sync) Unable to Add  Attribute Code:  {0} Validation Issues:\n{1}", new Object[]{attributeCode.getAttributeCodePk().toKey(), validationResultCode.toString()}));
							}
							syncResult.merge(validationResultCode);
						} catch (Exception e) {
							LOG.log(Level.SEVERE, "Unable to save attribute code: " + attributeCode.getAttributeCodePk().toKey(), e);
						}
					}
					//inactive missing codes
					existingAttributeCodes.stream().forEach((attributeCode) -> {
						if (newCodeSet.contains(attributeCode.getAttributeCodePk().toKey()) == false) {
							attributeCode.setActiveStatus(LookupEntity.INACTIVE_STATUS);
							attributeCode.setUpdateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
							removeAttributeCode(attributeCode.getAttributeCodePk());
						}
					});
				} else {
					LOG.log(Level.WARNING, MessageFormat.format("(Data Sync) Unable to Add Type:  {0} Validation Issues:\n{1}", new Object[]{attributeType.getAttributeType(), attributeTypeResult.toString()}));
				}
			} catch (Exception e) {
				LOG.log(Level.SEVERE, "Unable to save attribute type:" + attributeType.getAttributeType(), e);
			}
		}
		//Clear cache
		OSFCacheManager.getAttributeTypeCache().removeAll();
		OSFCacheManager.getAttributeCache().removeAll();
		OSFCacheManager.getAttributeCodeAllCache().removeAll();

		getSearchService().saveAll();
		return syncResult;
	}

	@Override
	public AttributeCode findCodeForType(AttributeCodePk pk)
	{
		AttributeCode attributeCode = null;
		List<AttributeCode> attributeCodes = findCodesForType(pk.getAttributeType());
		String cleanCode = attributeLabelToCode(pk.getAttributeCode());
		for (AttributeCode attributeCodeCheck : attributeCodes) {
			if (attributeCodeCheck.getAttributeCodePk().getAttributeCode().equals(cleanCode)) {
				attributeCode = attributeCodeCheck;
				break;
			}
		}

		return attributeCode;
	}

	@Override
	public AttributeType findType(String type)
	{
		AttributeType attributeType = null;

		Element element = OSFCacheManager.getAttributeTypeCache().get(type);
		if (element != null) {
			attributeType = (AttributeType) element.getObjectValue();
		} else {
			AttributeType attributeTypeExample = new AttributeType();
			attributeTypeExample.setActiveStatus(AttributeType.ACTIVE_STATUS);
			List<AttributeType> attributeTypes = persistenceService.queryByExample(new QueryByExample<>(attributeTypeExample));
			for (AttributeType attributeTypeCheck : attributeTypes) {
				if (attributeTypeCheck.getAttributeType().equals(type)) {
					attributeType = attributeTypeCheck;
				}
				element = new Element(attributeTypeCheck.getAttributeType(), attributeTypeCheck);
				OSFCacheManager.getAttributeTypeCache().put(element);
			}
		}

		return attributeType;
	}

	@Override
	public Architecture generateArchitecture(String attributeType)
	{
		Architecture architecture = new Architecture();

		AttributeType attributeTypeFull = persistenceService.findById(AttributeType.class, attributeType);
		if (attributeTypeFull != null) {
			if (attributeTypeFull.getArchitectureFlg()) {
				architecture.setName(attributeTypeFull.getDescription());
				architecture.setAttributeType(attributeType);

				String rootCode = "0";
				List<AttributeCode> attributeCodes = findCodesForType(attributeType);
				for (AttributeCode attributeCode : attributeCodes) {
					if (rootCode.equals(attributeCode.adjustedArchitectureCode())) {
						architecture.setAttributeCode(attributeCode.adjustedArchitectureCode());
						architecture.setDescription(attributeCode.getDescription());
					} else {
						String codeTokens[] = attributeCode.adjustedArchitectureCode().split(Pattern.quote("."));
						Architecture rootArchtecture = architecture;
						StringBuilder codeKey = new StringBuilder();
						for (int i = 0; i < codeTokens.length - 1; i++) {
							codeKey.append(codeTokens[i]);

							//put in stubs as needed
							boolean found = false;
							for (Architecture child : rootArchtecture.getChildren()) {
								if (child.getAttributeCode().equals(codeKey.toString())) {
									found = true;
									rootArchtecture = child;
									break;
								}
							}
							if (!found) {
								Architecture newChild = new Architecture();
								newChild.setAttributeCode(codeKey.toString());
								newChild.setAttributeType(attributeType);
								rootArchtecture.getChildren().add(newChild);
								rootArchtecture = newChild;
							}
							codeKey.append(".");
						}
						//now find the correct postion and add/update
						boolean found = false;
						for (Architecture child : rootArchtecture.getChildren()) {
							if (child.getAttributeCode().equals(attributeCode.adjustedArchitectureCode())) {
								child.setName(attributeCode.getLabel());
								child.setDescription(attributeCode.getDescription());
								found = true;
							}
						}
						if (!found) {
							Architecture newChild = new Architecture();
							newChild.setAttributeCode(attributeCode.adjustedArchitectureCode());
							newChild.setOriginalAttributeCode(attributeCode.getAttributeCodePk().getAttributeCode());
							newChild.setArchitectureCode(attributeCode.getArchitectureCode());
							newChild.setSortOrder(attributeCode.getSortOrder());
							newChild.setAttributeType(attributeType);
							newChild.setName(attributeCode.getLabel());
							newChild.setDescription(attributeCode.getDescription());
							rootArchtecture.getChildren().add(newChild);
						}
					}
				}

			} else {
				throw new OpenStorefrontRuntimeException("Attribute Type is not an architecture: " + attributeType, "Make sure type is an architecture.");
			}
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find attribute type: " + attributeType, "Check type code.");
		}
		sortArchitecture(architecture.getChildren());
		return architecture;
	}

	private void sortArchitecture(List<Architecture> architectures)
	{
		if (architectures.isEmpty()) {
			return;
		}

		for (Architecture architecture : architectures) {
			sortArchitecture(architecture.getChildren());
		}
		architectures.sort(new ArchitectureComparator<>());
	}

	@Override
	public List<AttributeXRefType> getAttributeXrefTypes(AttributeXrefModel attributeXrefModel)
	{
		AttributeXRefType xrefAttributeTypeExample = new AttributeXRefType();
		xrefAttributeTypeExample.setActiveStatus(AttributeXRefType.ACTIVE_STATUS);
		xrefAttributeTypeExample.setIntegrationType(attributeXrefModel.getIntegrationType());
		xrefAttributeTypeExample.setProjectType(attributeXrefModel.getProjectKey());
		xrefAttributeTypeExample.setIssueType(attributeXrefModel.getIssueType());
		List<AttributeXRefType> xrefAttributeTypes = persistenceService.queryByExample(xrefAttributeTypeExample);
		return xrefAttributeTypes;
	}

	@Override
	public Map<String, Map<String, String>> getAttributeXrefMapFieldMap()
	{
		Map<String, Map<String, String>> attributeCodeMap = new HashMap<>();

		AttributeXRefMap xrefAttributeMapExample = new AttributeXRefMap();
		xrefAttributeMapExample.setActiveStatus(AttributeXRefMap.ACTIVE_STATUS);

		List<AttributeXRefMap> xrefAttributeMaps = persistenceService.queryByExample(xrefAttributeMapExample);
		for (AttributeXRefMap xrefAttributeMap : xrefAttributeMaps) {

			if (attributeCodeMap.containsKey(xrefAttributeMap.getAttributeType())) {
				Map<String, String> codeMap = attributeCodeMap.get(xrefAttributeMap.getAttributeType());
				if (codeMap.containsKey(xrefAttributeMap.getExternalCode())) {

					//should only have one external code if there's a dup we'll only use one.
					//(however, which  code  is used depends on the order that came in.  which is not  determinate)
					//First one we hit wins
					LOG.log(Level.WARNING, MessageFormat.format("Duplicate external code for attribute type: {0} Code: {1}", new Object[]{xrefAttributeMap.getAttributeType(), xrefAttributeMap.getExternalCode()}));
				} else {
					codeMap.put(xrefAttributeMap.getExternalCode(), xrefAttributeMap.getLocalCode());
				}
			} else {
				Map<String, String> codeMap = new HashMap<>();
				codeMap.put(xrefAttributeMap.getExternalCode(), xrefAttributeMap.getLocalCode());
				attributeCodeMap.put(xrefAttributeMap.getAttributeType(), codeMap);
			}
		}

		return attributeCodeMap;
	}

	@Override
	public void saveAttributeXrefMap(AttributeXRefView attributeXRefView)
	{
		AttributeXRefType type = persistenceService.findById(AttributeXRefType.class, attributeXRefView.getType().getAttributeType());
		if (type != null) {
			type.setAttributeType(attributeXRefView.getType().getAttributeType());
			type.setActiveStatus(attributeXRefView.getType().getActiveStatus());
			type.setFieldId(attributeXRefView.getType().getFieldId());
			type.setFieldName(attributeXRefView.getType().getFieldName());
			type.setIntegrationType(attributeXRefView.getType().getIntegrationType());
			type.setIssueType(attributeXRefView.getType().getIssueType());
			type.setProjectType(attributeXRefView.getType().getProjectType());
			persistenceService.persist(type);
			AttributeXRefMap mapTemp = new AttributeXRefMap();
			mapTemp.setAttributeType(type.getAttributeType());
			List<AttributeXRefMap> tempMaps = persistenceService.queryByExample(new QueryByExample<>(mapTemp));
			for (AttributeXRefMap tempMap : tempMaps) {
				mapTemp = persistenceService.findById(AttributeXRefMap.class, tempMap.getXrefId());
				persistenceService.delete(mapTemp);
			}

			for (AttributeXRefMap map : attributeXRefView.getMap()) {
				AttributeXRefMap temp = persistenceService.queryOneByExample(map);
				if (temp != null) {
					temp.setActiveStatus(map.getActiveStatus());
					temp.setAttributeType(map.getAttributeType());
					temp.setExternalCode(map.getExternalCode());
					temp.setLocalCode(map.getLocalCode());
					persistenceService.persist(temp);
				} else {
					map.setActiveStatus(AttributeXRefMap.ACTIVE_STATUS);
					map.setXrefId(persistenceService.generateId());
					persistenceService.persist(map);
				}
			}
		} else {
			attributeXRefView.getType().setActiveStatus(AttributeXRefType.ACTIVE_STATUS);
			persistenceService.persist(attributeXRefView.getType());
			for (AttributeXRefMap map : attributeXRefView.getMap()) {
				map.setActiveStatus(AttributeXRefMap.ACTIVE_STATUS);
				map.setXrefId(persistenceService.generateId());
				persistenceService.persist(map);
			}
		}

	}

	@Override
	public void deleteAttributeXrefType(String attributeType)
	{
		AttributeXRefMap example = new AttributeXRefMap();
		example.setAttributeType(attributeType);
		persistenceService.deleteByExample(example);

		AttributeXRefType attributeXRefType = persistenceService.findById(AttributeXRefType.class, attributeType);
		if (attributeXRefType != null) {
			persistenceService.delete(attributeXRefType);
		}
	}

	@Override
	public void activateAttributeType(String type)
	{
		AttributeType attributeType = persistenceService.findById(AttributeType.class, type);

		if (attributeType != null) {
			attributeType.setActiveStatus(AttributeType.ACTIVE_STATUS);
			attributeType.setUpdateDts(TimeUtil.currentDate());
			attributeType.setUpdateUser(SecurityUtil.getCurrentUserName());
			persistenceService.persist(attributeType);

			BulkComponentAttributeChange bulkComponentAttributeChange = new BulkComponentAttributeChange();
			bulkComponentAttributeChange.setAttributes(getComponentAttributes(type, null));
			bulkComponentAttributeChange.setOpertionType(BulkComponentAttributeChange.OpertionType.ACTIVATE);
			(new ComponentServiceImpl(persistenceService)).bulkComponentAttributeChange(bulkComponentAttributeChange);

			cleanCaches(type);
		} else {
			throw new OpenStorefrontRuntimeException("Unable to save sort order.  Attribute Type: " + type, "Check data");
		}
	}

	@Override
	public void saveAttributeCodeSortOrder(AttributeCodePk attributeCodePk, Integer sortOrder)
	{
		Objects.requireNonNull(attributeCodePk, "Attribute Code PK is required");
		AttributeCode code = persistenceService.findById(AttributeCode.class, attributeCodePk);
		if (code != null) {
			code.setSortOrder(sortOrder);
			persistenceService.persist(code);

			cleanCaches(attributeCodePk.getAttributeType());
		} else {
			throw new OpenStorefrontRuntimeException("Unable to save sort order.  Attribute code: " + attributeCodePk.toString(), "Check data");
		}
	}

	@Override
	public AttributeTypeWrapper getFilteredTypes(AttributeFilterParams filter)
	{
		AttributeTypeWrapper result = new AttributeTypeWrapper();

		AttributeType attributeExample = new AttributeType();
		if (filter.getAll() == null || filter.getAll() == false) {
			attributeExample.setActiveStatus(filter.getStatus());
		}
		QueryByExample<AttributeType> queryByExample = new QueryByExample<>(attributeExample);

		// If given, filter the search by name
		if (StringUtils.isNotBlank(filter.getAttributeTypeDescription())) {
			SpecialOperatorModel<AttributeType> specialOperatorModel = new SpecialOperatorModel<>();
			AttributeType attributeTypeLikeExample = new AttributeType();
			attributeTypeLikeExample.setDescription("%" + filter.getAttributeTypeDescription().toLowerCase() + "%");

			specialOperatorModel.setExample(attributeTypeLikeExample);
			specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LIKE);
			specialOperatorModel.getGenerateStatementOption().setMethod(GenerateStatementOption.METHOD_LOWER_CASE);
			queryByExample.getExtraWhereCauses().add(specialOperatorModel);
		}

		queryByExample.setMaxResults(filter.getMax());
		queryByExample.setFirstResult(filter.getOffset());
		queryByExample.setSortDirection(filter.getSortOrder());

		AttributeType attributeOrderExample = new AttributeType();
		Field sortField = ReflectionUtil.getField(attributeOrderExample, filter.getSortField());
		if (sortField != null) {
			try {
				BeanUtils.setProperty(attributeOrderExample, sortField.getName(), QueryByExample.getFlagForType(sortField.getType()));
			} catch (IllegalAccessException | InvocationTargetException ex) {
				LOG.log(Level.WARNING, "Unable to set sort field", ex);
			}
			queryByExample.setOrderBy(attributeOrderExample);
		}

		List<AttributeType> attributes = persistenceService.queryByExample(queryByExample);
		result.setData(AttributeTypeAdminView.toView(attributes));

		queryByExample.setQueryType(QueryType.COUNT);
		result.setTotalNumber(persistenceService.countByExample(queryByExample));
		return result;
	}

	@Override
	public AttributeCodeWrapper getFilteredCodes(AttributeFilterParams filter, String type)
	{
		AttributeCodeWrapper result = new AttributeCodeWrapper();
		AttributeCodePk pk = new AttributeCodePk();
		pk.setAttributeType(type);
		AttributeCode attributeExample = new AttributeCode();
		attributeExample.setAttributeCodePk(pk);
		if (filter.getAll() == null || filter.getAll() == false) {
			attributeExample.setActiveStatus(filter.getStatus());
		}
		QueryByExample<AttributeCode> queryByExample = new QueryByExample<>(attributeExample);

		// If given, filter the search by name
		if (StringUtils.isNotBlank(filter.getAttributeCodeLabel())) {
			SpecialOperatorModel<AttributeCode> specialOperatorModel = new SpecialOperatorModel<>();
			AttributeCode attributeCodeLikeExample = new AttributeCode();
			attributeCodeLikeExample.setLabel("%" + filter.getAttributeCodeLabel().toLowerCase() + "%");

			specialOperatorModel.setExample(attributeCodeLikeExample);
			specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LIKE);
			specialOperatorModel.getGenerateStatementOption().setMethod(GenerateStatementOption.METHOD_LOWER_CASE);
			queryByExample.getExtraWhereCauses().add(specialOperatorModel);
		}

		queryByExample.setMaxResults(filter.getMax());
		queryByExample.setFirstResult(filter.getOffset());
		queryByExample.setSortDirection(filter.getSortOrder());

		AttributeCode attributeOrderExample = new AttributeCode();
		if ("code".equals(filter.getSortField())) {
			AttributeCodePk newPk = new AttributeCodePk();
			newPk.setAttributeCode(QueryByExample.STRING_FLAG);
			attributeOrderExample.setAttributeCodePk(newPk);
			queryByExample.setOrderBy(attributeOrderExample);
		} else {

			Field sortField = ReflectionUtil.getField(attributeOrderExample, filter.getSortField());
			if (sortField != null) {
				try {
					BeanUtils.setProperty(attributeOrderExample, sortField.getName(), QueryByExample.getFlagForType(sortField.getType()));
				} catch (IllegalAccessException | InvocationTargetException ex) {
					LOG.log(Level.WARNING, "Unable to set sort field", ex);
				}
				queryByExample.setOrderBy(attributeOrderExample);
			}
		}

		List<AttributeCode> attributes = persistenceService.queryByExample(queryByExample);
		List<AttributeCodeView> views = AttributeCodeView.toViews(attributes);

		result.setData(views);

		queryByExample.setQueryType(QueryType.COUNT);
		result.setTotalNumber(persistenceService.countByExample(queryByExample));
		return result;
	}

	@Override
	public void changeAttributeCode(AttributeCodePk attributeCodePk, String newCode)
	{

		String query = "Update " + ComponentAttributePk.class.getSimpleName() + " set attributeCode = :attributeCodeParamReplace where attributeCode = :oldCodeParam and attributeType = :attributeTypeParam";
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("attributeCodeParamReplace", newCode);
		parameters.put("oldCodeParam", attributeCodePk.getAttributeCode());
		parameters.put("attributeTypeParam", attributeCodePk.getAttributeType());

		persistenceService.runDbCommand(query, parameters);

		query = "Update " + AttributeCodePk.class.getSimpleName() + " set attributeCode = :attributeCodeParamReplace where attributeCode = :oldCodeParam and attributeType = :attributeTypeParam";
		parameters = new HashMap<>();
		parameters.put("attributeCodeParamReplace", newCode);
		parameters.put("oldCodeParam", attributeCodePk.getAttributeCode());
		parameters.put("attributeTypeParam", attributeCodePk.getAttributeType());

		persistenceService.runDbCommand(query, parameters);

		cleanCaches(attributeCodePk.getAttributeType());
	}

	@Override
	public ValidationResult importAttributes(List<AttributeAll> attributes, FileHistoryOption options)
	{
		Map<AttributeType, List<AttributeCode>> attributeMap = new HashMap<>();

		for (AttributeAll attributeAll : attributes) {
			if (attributeMap.containsKey(attributeAll.getAttributeType())) {
				attributeMap.get(attributeAll.getAttributeType()).addAll(attributeAll.getAttributeCodes());
			} else {
				attributeMap.put(attributeAll.getAttributeType(), attributeAll.getAttributeCodes());
			}
		}
		return syncAttribute(attributeMap);
	}

	@Override
	public void warmAttributeCaches()
	{
		warmAttributeCaches(null);
	}

	private void warmAttributeCaches(List<AttributeCode> attributeCodes)
	{
		if (attributeCodes == null) {
			Element element = OSFCacheManager.getAttributeCodeAllCache().get(OSFCacheManager.ALLCODE_KEY);
			if (element == null) {
				//warm caches (ignore return)
				getAllAttributeCodes(AttributeCode.ACTIVE_STATUS);
			}
		} else {

			//populate type->code cache
			Map<String, List<AttributeCode>> codeMap = attributeCodes.stream()
					.collect(Collectors.groupingBy(AttributeCode::typeField));

			for (String type : codeMap.keySet()) {
				Element element = new Element(type, codeMap.get(type));
				OSFCacheManager.getAttributeCache().put(element);
			}

			//populate the type cache
			AttributeType attributeTypeExample = new AttributeType();
			attributeTypeExample.setActiveStatus(AttributeType.ACTIVE_STATUS);
			List<AttributeType> attributeTypes = persistenceService.queryByExample(new QueryByExample<>(attributeTypeExample));
			for (AttributeType attributeTypeCheck : attributeTypes) {
				Element element = new Element(attributeTypeCheck.getAttributeType(), attributeTypeCheck);
				OSFCacheManager.getAttributeTypeCache().put(element);
			}

		}

	}

	@Override
	public List<AttributeType> findRequiredAttributes(String componentType, boolean submissionTypesOnly, String submissionTemplateId)
	{
		return findRequiredAttributes(componentType, submissionTypesOnly, false, submissionTemplateId);
	}

	@Override
	public List<AttributeType> findRequiredAttributes(String componentType, boolean submissionTypesOnly, boolean skipFilterNoCodes, String submissionTemplateId)
	{
		List<AttributeType> requiredAttributes = new ArrayList<>();

		AttributeType attributeTypeExample = new AttributeType();
		attributeTypeExample.setActiveStatus(AttributeType.ACTIVE_STATUS);

		List<AttributeType> attributeTypes = attributeTypeExample.findByExample();
		for (AttributeType attributeType : attributeTypes) {

			if (attributeType.getRequiredRestrictions() != null && !attributeType.getRequiredRestrictions().isEmpty()) {
				filterRequiredAttributes(attributeType, componentType, skipFilterNoCodes, requiredAttributes);
			}
		}

		if (submissionTypesOnly) {
			requiredAttributes.removeIf((attribute) -> {
				return Convert.toBoolean(attribute.getHideOnSubmission());
			});

			//filter out attribute already on submission form
			if (StringUtils.isNotBlank(submissionTemplateId)) {
				SubmissionFormTemplate template = persistenceService.findById(SubmissionFormTemplate.class, submissionTemplateId);
				if (template != null) {
					Set<String> alreadyInForm = findSingleAttributeTypesInForm(template);
					requiredAttributes.removeIf(type -> alreadyInForm.contains(type.getAttributeType()));
				}
			}
		}
		return requiredAttributes;
	}

	private Set<String> findSingleAttributeTypesInForm(SubmissionFormTemplate template)
	{
		Set<String> attributeTypesInForm = new HashSet<>();

		for (SubmissionFormSection section : template.getSections()) {
			for (SubmissionFormField field : section.getFields()) {

				switch (field.getFieldType()) {

					case SubmissionFormFieldType.ATTRIBUTE_SINGLE:
					case SubmissionFormFieldType.ATTRIBUTE_RADIO:
					case SubmissionFormFieldType.ATTRIBUTE_MULTI_CHECKBOX:
						attributeTypesInForm.add(field.getAttributeType());
						break;
				}

			}
		}
		return attributeTypesInForm;
	}

	private void filterRequiredAttributes(AttributeType attributeType, String componentType, boolean skipFilterNoCodes, List<AttributeType> requiredAttributes)
	{
		for (ComponentTypeRestriction restriction : attributeType.getRequiredRestrictions()) {
			if (restriction.getComponentType().equals(componentType)) {

				if (skipFilterNoCodes) {
					requiredAttributes.add(attributeType);
				} else {
					List<AttributeCode> codes = findCodesForType(attributeType.getAttributeType());
					if (!codes.isEmpty() || Convert.toBoolean(attributeType.getAllowUserGeneratedCodes())) {
						requiredAttributes.add(attributeType);
					}
				}
			}
		}
	}

	@Override
	public List<AttributeType> findOptionalAttributes(String componentType, boolean submissionTypesOnly, String submissionTemplateId)
	{
		List<AttributeType> optionalAttributes = new ArrayList<>();

		AttributeType attributeTypeExample = new AttributeType();
		attributeTypeExample.setActiveStatus(AttributeType.ACTIVE_STATUS);

		List<AttributeType> attributeTypes = attributeTypeExample.findByExample();

		attributeTypes.forEach((attributeType) -> {

			if (attributeType.getOptionalRestrictions() != null && !attributeType.getOptionalRestrictions().isEmpty()) {
				for (ComponentTypeRestriction restriction : attributeType.getOptionalRestrictions()) {
					if (restriction.getComponentType().equals(componentType)) {
						optionalAttributes.add(attributeType);
					}
				}
			}

		});

		if (submissionTypesOnly) {
			optionalAttributes.removeIf((attribute) -> {
				return Convert.toBoolean(attribute.getHideOnSubmission());
			});

			//filter out attribute already on submission form
			if (StringUtils.isNotBlank(submissionTemplateId)) {
				SubmissionFormTemplate template = persistenceService.findById(SubmissionFormTemplate.class, submissionTemplateId);
				if (template != null) {
					Set<String> alreadyInForm = findSingleAttributeTypesInForm(template);
					optionalAttributes.removeIf(type -> alreadyInForm.contains(type.getAttributeType()));
				}
			}
		}
		return optionalAttributes;
	}

}
