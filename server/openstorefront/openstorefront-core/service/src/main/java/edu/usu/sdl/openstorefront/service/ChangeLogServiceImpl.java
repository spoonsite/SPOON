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

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.api.ChangeLogService;
import edu.usu.sdl.openstorefront.core.entity.ChangeLog;
import edu.usu.sdl.openstorefront.core.entity.ChangeType;
import edu.usu.sdl.openstorefront.core.entity.ContentSection;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklist;
import edu.usu.sdl.openstorefront.core.entity.LoggableModel;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.model.FieldChangeModel;
import edu.usu.sdl.openstorefront.core.util.EntityUtil;
import edu.usu.sdl.openstorefront.service.api.ChangeLogServicePrivate;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Handles creating change log record
 *
 * @author dshurtleff
 */
public class ChangeLogServiceImpl
		extends ServiceProxy
		implements ChangeLogService, ChangeLogServicePrivate
{

	private static final Logger LOG = Logger.getLogger(ChangeLogServiceImpl.class.getName());

	//This is only valid for this instance (See supend/resume)
	private boolean saveChanges = true;

	@Override
	public <T extends StandardEntity & LoggableModel> List<ChangeLog> findUpdateChanges(T original, T updated)
	{
		return findUpdateChanges(original, updated, true);
	}

	@Override
	public <T extends StandardEntity & LoggableModel> List<ChangeLog> findUpdateChanges(T original, T updated, boolean save)
	{
		List<ChangeLog> changeLogs = new ArrayList<>();

		List<FieldChangeModel> changes = original.findChanges(updated);
		original = unwrapProxy(original);

		for (FieldChangeModel change : changes) {

			if ((change.getNewValue() != null && change.getNewValue().equals(change.getOldValue()) == false)
					|| (change.getOldValue() != null && change.getNewValue() == null)) {

				ChangeLog changeLog = new ChangeLog();

				changeLog.setChangeLogId(getPersistenceService().generateId());
				changeLog.setChangeType(ChangeType.UPDATED);
				changeLog.setEntity(EntityUtil.getRealClassName(original.getClass().getSimpleName()));
				changeLog.setEntityId(EntityUtil.getPKFieldValue(original));
				changeLog.setField(change.getField());
				changeLog.setOldValue(change.getOldValue());
				changeLog.setNewValue(change.getNewValue());
				setParent(changeLog, original);

				changeLogs.add(changeLog);
			}
		}

		if (save) {
			for (ChangeLog changeLog : changeLogs) {
				saveChangeRecord(changeLog);
			}
		}

		return changeLogs;
	}

	private void saveChangeRecord(ChangeLog changeLog)
	{
		if (saveChanges) {
			changeLog.populateBaseCreateFields();
			persistenceService.persist(changeLog);
		}
	}

	private <T extends StandardEntity> T unwrapProxy(T original)
	{
		T unwrapped;
		if (persistenceService.isProxy(original)) {
			unwrapped = persistenceService.deattachAll(original);
		} else {
			unwrapped = original;
		}
		return unwrapped;
	}

	private <T extends StandardEntity> void setParent(ChangeLog changeLog, T original)
	{
		if (original instanceof LoggableModel) {
			LoggableModel loggableModel = (LoggableModel) original;
			loggableModel.setChangeParent(changeLog);
		}
	}

	@Override
	public <T extends StandardEntity> ChangeLog addEntityChange(T addedEntity)
	{
		ChangeLog changeLog = new ChangeLog();

		addedEntity = unwrapProxy(addedEntity);

		changeLog.setChangeLogId(getPersistenceService().generateId());
		changeLog.setChangeType(ChangeType.ADDED);
		changeLog.setEntity(EntityUtil.getRealClassName(addedEntity.getClass().getSimpleName()));
		changeLog.setEntityId(EntityUtil.getPKFieldValue(addedEntity));
		if (addedEntity instanceof LoggableModel) {
			LoggableModel loggableModel = (LoggableModel) addedEntity;
			changeLog.setComment(loggableModel.addRemoveComment());
		}
		setParent(changeLog, addedEntity);
		saveChangeRecord(changeLog);

		return changeLog;
	}

	@Override
	public <T extends StandardEntity> ChangeLog removeEntityChange(Class<T> removedEntityClass, T removedEntity)
	{
		ChangeLog changeLog = new ChangeLog();

		removedEntity = unwrapProxy(removedEntity);

		String archivedValue = null;
		try {
			T copy = removedEntityClass.newInstance();
			BeanUtils.copyProperties(copy, removedEntity);

			archivedValue = StringProcessor.defaultObjectMapper().writeValueAsString(copy);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | JsonProcessingException ex) {
			LOG.log(Level.WARNING, "Unable to create a archive of entity. (Change Log) Entity: " + removedEntity.getClass().getSimpleName(), ex);
		}

		changeLog.setChangeLogId(getPersistenceService().generateId());
		changeLog.setChangeType(ChangeType.REMOVED);
		changeLog.setEntity(EntityUtil.getRealClassName(removedEntity.getClass().getSimpleName()));
		changeLog.setEntityId(EntityUtil.getPKFieldValue(removedEntity));
		if (removedEntity instanceof LoggableModel) {
			LoggableModel loggableModel = (LoggableModel) removedEntity;
			changeLog.setComment(loggableModel.addRemoveComment());
		}
		changeLog.setArchivedEntity(archivedValue);
		setParent(changeLog, removedEntity);

		saveChangeRecord(changeLog);

		return changeLog;
	}

	@Override
	public <T extends StandardEntity> ChangeLog logStatusChange(T statusEntity, String newStatus)
	{
		return logStatusChange(statusEntity, newStatus, null);
	}

	@Override
	public <T extends StandardEntity> ChangeLog logStatusChange(T statusEntity, String newStatus, String comment)
	{
		ChangeLog changeLog = new ChangeLog();

		statusEntity = unwrapProxy(statusEntity);

		changeLog.setChangeLogId(getPersistenceService().generateId());
		changeLog.setChangeType(ChangeType.UPDATED);
		changeLog.setEntity(EntityUtil.getRealClassName(statusEntity.getClass().getSimpleName()));
		changeLog.setEntityId(EntityUtil.getPKFieldValue(statusEntity));
		changeLog.setField(StandardEntity.FIELD_ACTIVE_STATUS);
		changeLog.setOldValue(statusEntity.getActiveStatus());
		changeLog.setNewValue(newStatus);
		if (StringUtils.isNotBlank(comment)) {
			changeLog.setComment(comment);
		}
		setParent(changeLog, statusEntity);

		saveChangeRecord(changeLog);

		return changeLog;
	}

	@Override
	public <T extends StandardEntity> ChangeLog removedAllEntityChange(T exampleRemovedEntity)
	{
		ChangeLog changeLog = new ChangeLog();

		exampleRemovedEntity = unwrapProxy(exampleRemovedEntity);

		changeLog.setChangeLogId(getPersistenceService().generateId());
		changeLog.setChangeType(ChangeType.REMOVED);
		changeLog.setEntity(EntityUtil.getRealClassName(exampleRemovedEntity.getClass().getSimpleName()));
		changeLog.setEntityId(ChangeLog.REMOVED_ALL_ID);
		changeLog.setComment("All Records Removed");

		setParent(changeLog, exampleRemovedEntity);

		saveChangeRecord(changeLog);

		return changeLog;
	}

	@Override
	public void removeChangeLogs(String entity, String entityId)
	{
		Objects.requireNonNull(entity);
		Objects.requireNonNull(entityId);

		ChangeLog changeLogExample = new ChangeLog();
		changeLogExample.setParentEntity(EntityUtil.getRealClassName(entity));
		changeLogExample.setParentEntityId(entityId);
		long childRecordsRemoved = persistenceService.deleteByExample(changeLogExample);
		LOG.log(Level.FINE, MessageFormat.format("Removed: {0} child change log records.", childRecordsRemoved));

		changeLogExample = new ChangeLog();
		changeLogExample.setEntity(EntityUtil.getRealClassName(entity));
		changeLogExample.setEntityId(entityId);
		long recordsRemoved = persistenceService.deleteByExample(changeLogExample);
		LOG.log(Level.FINE, MessageFormat.format("Removed: {0} change log records.", recordsRemoved));

	}

	@Override
	public <T extends StandardEntity> ChangeLog logFieldChange(T original, String fieldChanged, String oldValue, String newValue)
	{
		ChangeLog changeLog = null;

		if ((newValue != null && newValue.equals(oldValue) == false)
				|| (oldValue != null && newValue == null)) {

			original = unwrapProxy(original);

			changeLog = new ChangeLog();
			changeLog.setChangeLogId(getPersistenceService().generateId());
			changeLog.setChangeType(ChangeType.UPDATED);
			changeLog.setEntity(EntityUtil.getRealClassName(original.getClass().getSimpleName()));
			changeLog.setEntityId(EntityUtil.getPKFieldValue(original));
			changeLog.setField(fieldChanged);
			changeLog.setOldValue(oldValue);
			changeLog.setNewValue(newValue);
			setParent(changeLog, original);

			saveChangeRecord(changeLog);
		}

		return changeLog;
	}

	@Override
	public <T extends StandardEntity> ChangeLog logOtherChange(T entity, String changeType, String comment)
	{
		ChangeLog changeLog = new ChangeLog();

		entity = unwrapProxy(entity);

		changeLog.setChangeLogId(getPersistenceService().generateId());
		changeLog.setChangeType(changeType);
		changeLog.setEntity(EntityUtil.getRealClassName(entity.getClass().getSimpleName()));
		changeLog.setEntityId(EntityUtil.getPKFieldValue(entity));
		changeLog.setComment(comment);
		setParent(changeLog, entity);

		saveChangeRecord(changeLog);

		return changeLog;
	}

	@Override
	public List<ChangeLog> getChangeLogs(String entity, String entityId, boolean includeChildren)
	{
		ChangeLog changeLogExample = new ChangeLog();
		changeLogExample.setActiveStatus(ChangeLog.ACTIVE_STATUS);
		changeLogExample.setEntity(entity);
		changeLogExample.setEntityId(entityId);

		List<ChangeLog> changeLogs = changeLogExample.findByExample();

		if (includeChildren) {
			changeLogExample = new ChangeLog();
			changeLogExample.setActiveStatus(ChangeLog.ACTIVE_STATUS);
			changeLogExample.setParentEntity(entity);
			changeLogExample.setParentEntityId(entityId);

			List<ChangeLog> childrenChanges = changeLogExample.findByExample();
			changeLogs.addAll(childrenChanges);

			if (Evaluation.class.getSimpleName().equals(entity)) {
				ContentSection contentSectionExample = new ContentSection();
				contentSectionExample.setEntity(Evaluation.class.getSimpleName());
				contentSectionExample.setEntityId(entityId);

				List<ContentSection> contentSections = contentSectionExample.findByExample();
				for (ContentSection contentSection : contentSections) {

					changeLogExample = new ChangeLog();
					changeLogExample.setActiveStatus(ChangeLog.ACTIVE_STATUS);
					changeLogExample.setEntity(ContentSection.class.getSimpleName());
					changeLogExample.setEntityId(contentSection.getContentSectionId());

					childrenChanges = changeLogExample.findByExample();
					changeLogs.addAll(childrenChanges);

					changeLogExample = new ChangeLog();
					changeLogExample.setActiveStatus(ChangeLog.ACTIVE_STATUS);
					changeLogExample.setParentEntity(ContentSection.class.getSimpleName());
					changeLogExample.setParentEntityId(contentSection.getContentSectionId());

					childrenChanges = changeLogExample.findByExample();
					changeLogs.addAll(childrenChanges);
				}

				//get checklist children
				EvaluationChecklist evaluationChecklistExample = new EvaluationChecklist();
				evaluationChecklistExample.setEvaluationId(entityId);
				List<EvaluationChecklist> checklists = evaluationChecklistExample.findByExample();
				for (EvaluationChecklist checklist : checklists) {

					changeLogExample = new ChangeLog();
					changeLogExample.setActiveStatus(ChangeLog.ACTIVE_STATUS);
					changeLogExample.setParentEntity(EvaluationChecklist.class.getSimpleName());
					changeLogExample.setParentEntityId(checklist.getChecklistId());

					childrenChanges = changeLogExample.findByExample();
					changeLogs.addAll(childrenChanges);
				}
			}

		}

		return changeLogs;
	}

	@Override
	public void suspendSaving()
	{
		saveChanges = false;
		LOG.log(Level.FINE, "Supended Change Logging");
	}

	@Override
	public void resumeSaving()
	{
		saveChanges = true;
		LOG.log(Level.FINE, "Resumed Change Logging");
	}

}
