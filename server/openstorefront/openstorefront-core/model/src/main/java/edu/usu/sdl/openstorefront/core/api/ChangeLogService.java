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
package edu.usu.sdl.openstorefront.core.api;

import edu.usu.sdl.openstorefront.core.entity.ChangeLog;
import edu.usu.sdl.openstorefront.core.entity.LoggableModel;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public interface ChangeLogService
		extends AsyncService
{

	/**
	 * Pulls changes logs
	 *
	 * @param entity
	 * @param entityId
	 * @param includeChildren (pulls all child levels)
	 * @return
	 */
	public List<ChangeLog> getChangeLogs(String entity, String entityId, boolean includeChildren);

	/**
	 * Finds changes and saves them
	 *
	 * @param <T>
	 * @param original
	 * @param updated
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public <T extends StandardEntity & LoggableModel> List<ChangeLog> findUpdateChanges(T original, T updated);

	/**
	 * Find changes (optional saves changes) between the original and the
	 * updated entity
	 *
	 * @param <T>
	 * @param original
	 * @param updated
	 * @param save
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public <T extends StandardEntity & LoggableModel> List<ChangeLog> findUpdateChanges(T original, T updated, boolean save);

	/**
	 * Saves a new change for a field based on the original entity
	 *
	 * @param <T>
	 * @param original
	 * @param fieldChanged
	 * @param oldValue
	 * @param newValue
	 * @return
	 */
	public <T extends StandardEntity> ChangeLog logFieldChange(T original, String fieldChanged, String oldValue, String newValue);

	/**
	 * Save a added change record to parent entities history
	 *
	 * @param <T>
	 * @param addedEntity
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public <T extends StandardEntity> ChangeLog addEntityChange(T addedEntity);

	/**
	 * Create a record for a remove entity (Sub-entity)
	 *
	 * @param <T>
	 * @param removedEntityClass (This is needed to resolve proxy versions of
	 * entity)
	 * @param removedEntity
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public <T extends StandardEntity> ChangeLog removeEntityChange(Class<T> removedEntityClass, T removedEntity);

	/**
	 * This is used for the records deleting a set of (sub-entities)
	 *
	 * @param <T>
	 * @param exampleRemovedEntity
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public <T extends StandardEntity> ChangeLog removedAllEntityChange(T exampleRemovedEntity);

	/**
	 * Logs and active status change in cases where that not handle in an
	 * update.
	 *
	 * @param <T>
	 * @param statusEntity
	 * @param newStatus
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public <T extends StandardEntity> ChangeLog logStatusChange(final T statusEntity, String newStatus);

	/**
	 * Logs and active status change in cases where that not handle in an
	 * update.
	 *
	 * @param <T>
	 * @param statusEntity
	 * @param newStatus
	 * @param comment
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public <T extends StandardEntity> ChangeLog logStatusChange(final T statusEntity, String newStatus, String comment);

	/**
	 * Logs a generic change type (Example: use is for snapshots and restores)
	 *
	 * @param <T>
	 * @param entity
	 * @param changeType
	 * @param comment
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public <T extends StandardEntity> ChangeLog logOtherChange(T entity, String changeType, String comment);

	/**
	 * This will only suspend the current instance of the service
	 */
	public void suspendSaving();

	/**
	 * This will only resume the current instance of the service
	 */
	public void resumeSaving();

}
