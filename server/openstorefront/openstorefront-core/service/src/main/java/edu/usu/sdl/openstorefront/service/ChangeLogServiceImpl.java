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
import edu.usu.sdl.openstorefront.core.entity.BaseComponent;
import edu.usu.sdl.openstorefront.core.entity.ChangeLog;
import edu.usu.sdl.openstorefront.core.entity.ChangeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklist;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklistRecommendation;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklistResponse;
import edu.usu.sdl.openstorefront.core.entity.LoggableModel;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.model.FieldChangeModel;
import edu.usu.sdl.openstorefront.core.util.EntityUtil;
import edu.usu.sdl.openstorefront.service.api.ChangeLogServicePrivate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

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
		for (FieldChangeModel change : changes) {

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

		if (save) {
			for (ChangeLog changeLog : changeLogs) {
				changeLog.populateBaseCreateFields();
				persistenceService.persist(changeLog);
			}
		}

		return changeLogs;
	}

	private <T extends StandardEntity> void setParent(ChangeLog changeLog, T original)
	{
		if (original instanceof BaseComponent) {

			BaseComponent baseComponent = (BaseComponent) original;
			changeLog.setParentEntity(Component.class.getSimpleName());
			changeLog.setParentEntityId(baseComponent.getComponentId());

		} else if (original instanceof EvaluationChecklist) {

			EvaluationChecklist evaluationChecklist = (EvaluationChecklist) original;
			changeLog.setParentEntity(Evaluation.class.getSimpleName());
			changeLog.setParentEntityId(evaluationChecklist.getEvaluationId());

		} else if (original instanceof EvaluationChecklistRecommendation) {

			EvaluationChecklistRecommendation evaluationChecklistRecommendation = (EvaluationChecklistRecommendation) original;
			changeLog.setParentEntity(EvaluationChecklist.class.getSimpleName());
			changeLog.setParentEntityId(evaluationChecklistRecommendation.getChecklistId());

		} else if (original instanceof EvaluationChecklistResponse) {

			EvaluationChecklistResponse evaluationChecklistResponse = (EvaluationChecklistResponse) original;
			changeLog.setParentEntity(EvaluationChecklist.class.getSimpleName());
			changeLog.setParentEntityId(evaluationChecklistResponse.getChecklistId());

		}
	}

	@Override
	public <T extends StandardEntity> ChangeLog addEntityChange(T addedEntity)
	{
		ChangeLog changeLog = new ChangeLog();
		changeLog.setChangeLogId(getPersistenceService().generateId());
		changeLog.setChangeType(ChangeType.ADDED);
		changeLog.setEntity(EntityUtil.getRealClassName(addedEntity.getClass().getSimpleName()));
		changeLog.setEntityId(EntityUtil.getPKFieldValue(addedEntity));
		setParent(changeLog, addedEntity);

		changeLog.populateBaseCreateFields();
		persistenceService.persist(changeLog);

		return changeLog;
	}

	@Override
	public <T extends StandardEntity> ChangeLog removeEntityChange(T removedEntity)
	{
		ChangeLog changeLog = new ChangeLog();
		String archivedValue = null;
		try {
			archivedValue = StringProcessor.defaultObjectMapper().writeValueAsString(removedEntity);
		} catch (JsonProcessingException ex) {
			LOG.log(Level.WARNING, "Unable to create a archive of entity. (Change Log) Entity: " + removedEntity.getClass().getSimpleName(), ex);
		}

		changeLog.setChangeLogId(getPersistenceService().generateId());
		changeLog.setChangeType(ChangeType.REMOVED);
		changeLog.setEntity(EntityUtil.getRealClassName(removedEntity.getClass().getSimpleName()));
		changeLog.setEntityId(EntityUtil.getPKFieldValue(removedEntity));
		changeLog.setArchivedEntity(archivedValue);
		setParent(changeLog, removedEntity);

		changeLog.populateBaseCreateFields();
		persistenceService.persist(changeLog);

		return changeLog;
	}

	@Override
	public <T extends StandardEntity> ChangeLog logStatusChange(T statusEntity, String newStatus)
	{
		ChangeLog changeLog = new ChangeLog();
		changeLog.setChangeLogId(getPersistenceService().generateId());
		changeLog.setChangeType(ChangeType.UPDATED);
		changeLog.setEntity(EntityUtil.getRealClassName(statusEntity.getClass().getSimpleName()));
		changeLog.setEntityId(EntityUtil.getPKFieldValue(statusEntity));
		changeLog.setField(StandardEntity.FIELD_ACTIVE_STATUS);
		changeLog.setOldValue(statusEntity.getActiveStatus());
		changeLog.setNewValue(newStatus);
		setParent(changeLog, statusEntity);

		changeLog.populateBaseCreateFields();
		persistenceService.persist(changeLog);

		return changeLog;
	}

	@Override
	public <T extends StandardEntity> ChangeLog removedAllEntityChange(T exampleRemovedEntity)
	{
		ChangeLog changeLog = new ChangeLog();

		changeLog.setChangeLogId(getPersistenceService().generateId());
		changeLog.setChangeType(ChangeType.REMOVED);
		changeLog.setEntity(EntityUtil.getRealClassName(exampleRemovedEntity.getClass().getSimpleName()));
		changeLog.setEntityId(ChangeLog.REMOVED_ALL_ID);

		setParent(changeLog, exampleRemovedEntity);

		changeLog.populateBaseCreateFields();
		persistenceService.persist(changeLog);

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
		ChangeLog changeLog = new ChangeLog();

		changeLog.setChangeLogId(getPersistenceService().generateId());
		changeLog.setChangeType(ChangeType.UPDATED);
		changeLog.setEntity(EntityUtil.getRealClassName(original.getClass().getSimpleName()));
		changeLog.setEntityId(EntityUtil.getPKFieldValue(original));
		changeLog.setField(fieldChanged);
		changeLog.setOldValue(oldValue);
		changeLog.setNewValue(newValue);
		setParent(changeLog, original);

		changeLog.populateBaseCreateFields();
		persistenceService.persist(changeLog);

		return changeLog;
	}

}
