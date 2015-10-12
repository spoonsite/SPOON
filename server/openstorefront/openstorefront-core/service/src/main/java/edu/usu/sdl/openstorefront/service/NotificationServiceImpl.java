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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.api.NotificationService;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.NotificationEvent;
import edu.usu.sdl.openstorefront.core.entity.NotificationEventReadStatus;
import edu.usu.sdl.openstorefront.core.spi.NotificationEventListerner;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.NotificationEventView;
import edu.usu.sdl.openstorefront.core.view.NotificationEventWrapper;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.sf.ehcache.Element;
import net.sourceforge.stripes.util.bean.BeanUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * Handles Notification Events
 *
 * @author dshurtleff
 */
public class NotificationServiceImpl
		extends ServiceProxy
		implements NotificationService
{

	private static final String LISTENER_KEY = "NOTIFICATION_LISTENERS";

	@Override
	public NotificationEventWrapper getAllEventsForUser(String username, FilterQueryParams queryParams)
	{
		NotificationEventWrapper notificationEventWrapper = new NotificationEventWrapper();

		List<NotificationEvent> notificationEvents = new ArrayList<>();

		//for all users
		NotificationEvent notificationEventExample = new NotificationEvent();
		notificationEventExample.setActiveStatus(NotificationEvent.ACTIVE_STATUS);
		QueryByExample queryByExample = new QueryByExample(notificationEventExample);

		NotificationEvent notificationNotExample = new NotificationEvent();
		notificationNotExample.setUsername(QueryByExample.STRING_FLAG);
		notificationNotExample.setRoleGroup(QueryByExample.STRING_FLAG);
		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel(notificationNotExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_NULL);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		NotificationEvent notificationEventStart = new NotificationEvent();
		notificationEventStart.setCreateDts(queryParams.getStart());

		NotificationEvent notificationEventEnd = new NotificationEvent();
		notificationEventEnd.setCreateDts(queryParams.getEnd());

		SpecialOperatorModel specialOperatorStartModel = new SpecialOperatorModel();
		specialOperatorStartModel.setExample(notificationEventStart);
		specialOperatorStartModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN);
		queryByExample.getExtraWhereCauses().add(specialOperatorStartModel);

		SpecialOperatorModel specialOperatorEndModel = new SpecialOperatorModel();
		specialOperatorEndModel.setExample(notificationEventEnd);
		specialOperatorEndModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorEndModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorEndModel);

		queryByExample.setMaxResults(queryParams.getMax());
		queryByExample.setFirstResult(queryParams.getOffset());
		queryByExample.setSortDirection(queryParams.getSortOrder());

		NotificationEvent notificationEventSortExample = new NotificationEvent();
		Field sortField = ReflectionUtil.getField(notificationEventSortExample, queryParams.getSortField());
		if (sortField != null) {
			BeanUtil.setPropertyValue(sortField.getName(), notificationEventSortExample, QueryByExample.getFlagForType(sortField.getType()));
			queryByExample.setOrderBy(notificationEventSortExample);
		}

		notificationEvents.addAll(persistenceService.queryByExample(NotificationEvent.class, queryByExample));
		notificationEventWrapper.setTotalNumber(persistenceService.countByExample(queryByExample));

		//TODO: groups (lookup userprofile and get the users groups and then query for those groups.)
		//////////////////
		notificationEventExample = new NotificationEvent();
		notificationEventExample.setActiveStatus(NotificationEvent.ACTIVE_STATUS);
		notificationEventExample.setUsername(username);
		queryByExample = new QueryByExample(notificationEventExample);
		queryByExample.getExtraWhereCauses().add(specialOperatorStartModel);
		queryByExample.getExtraWhereCauses().add(specialOperatorEndModel);

		queryByExample.setMaxResults(queryParams.getMax());
		queryByExample.setFirstResult(queryParams.getOffset());
		queryByExample.setSortDirection(queryParams.getSortOrder());
		notificationEventSortExample = new NotificationEvent();
		sortField = ReflectionUtil.getField(notificationEventSortExample, queryParams.getSortField());
		if (sortField != null) {
			BeanUtil.setPropertyValue(sortField.getName(), notificationEventSortExample, QueryByExample.getFlagForType(sortField.getType()));
			queryByExample.setOrderBy(notificationEventSortExample);
		}
		List<NotificationEvent> userEvents = persistenceService.queryByExample(NotificationEvent.class, queryByExample);
		for (NotificationEvent event : userEvents) {
			if (StringUtils.isNotBlank(event.getUsername())) {
				notificationEvents.add(event);
			}
		}
		notificationEventWrapper.setTotalNumber(notificationEventWrapper.getTotalNumber() + persistenceService.countByExample(queryByExample));

		notificationEvents = queryParams.filter(notificationEvents);
		notificationEventWrapper.setResults(notificationEvents.size());
		notificationEventWrapper.setData(NotificationEventView.toView(notificationEvents));

		//mark read flag
		Set<String> eventIds = new HashSet<>();
		for (NotificationEventView view : notificationEventWrapper.getData()) {
			eventIds.add(view.getEventId());
		}

		if (eventIds.isEmpty() == false) {
			String query = "select from " + NotificationEventReadStatus.class.getSimpleName() + " where username = :usernameParam and eventId in :eventIdSetParam ";
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("usernameParam", username);
			paramMap.put("eventIdSetParam", eventIds);

			List<NotificationEventReadStatus> readStatuses = persistenceService.query(query, paramMap);

			Set<String> readSet = new HashSet<>();
			for (NotificationEventReadStatus readStatus : readStatuses) {
				readSet.add(readStatus.getEventId());
			}

			for (NotificationEventView view : notificationEventWrapper.getData()) {
				if (readSet.contains(view.getEventId())) {
					view.setReadMessage(true);
				}
			}

		}

		return notificationEventWrapper;
	}

	@Override
	public void registerNotificationListerner(NotificationEventListerner notificationEventListerner)
	{
		Element element = OSFCacheManager.getApplicationCache().get(LISTENER_KEY);
		if (element == null) {
			List<NotificationEventListerner> listeners = new ArrayList<>();
			element = new Element(LISTENER_KEY, listeners);
			OSFCacheManager.getApplicationCache().put(element);
		}
		((List<NotificationEventListerner>) element.getObjectValue()).add(notificationEventListerner);
	}

	@Override
	public NotificationEvent postEvent(NotificationEvent notificationEvent)
	{
		notificationEvent.setEventId(persistenceService.generateId());
		notificationEvent.populateBaseCreateFields();
		notificationEvent = persistenceService.persist(notificationEvent);

		Element element = OSFCacheManager.getApplicationCache().get(LISTENER_KEY);
		if (element != null) {
			List<NotificationEventListerner> listerners = (List<NotificationEventListerner>) element.getObjectValue();
			for (NotificationEventListerner listerner : listerners) {
				listerner.processEvent(persistenceService.deattachAll(notificationEvent));
			}
		}
		return notificationEvent;
	}

	@Override
	public void deleteEvent(String eventId)
	{
		NotificationEvent notificationEvent = persistenceService.findById(NotificationEvent.class, eventId);
		if (notificationEvent != null) {

			NotificationEventReadStatus notificationEventReadStatus = new NotificationEventReadStatus();
			notificationEventReadStatus.setEventId(eventId);
			persistenceService.deleteByExample(notificationEventReadStatus);

			persistenceService.delete(notificationEvent);
		}
	}

	@Override
	public void cleanupOldEvents()
	{
		int maxDays = Convert.toInteger(PropertiesManager.getValue(PropertiesManager.KEY_FILE_HISTORY_KEEP_DAYS, "7"));

		LocalDateTime archiveTime = LocalDateTime.now();
		archiveTime = archiveTime.minusDays(maxDays);
		archiveTime = archiveTime.truncatedTo(ChronoUnit.DAYS);
		String deleteQuery = "updateDts < :maxUpdateDts";

		ZonedDateTime zdt = archiveTime.atZone(ZoneId.systemDefault());
		Date archiveDts = Date.from(zdt.toInstant());

		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("maxUpdateDts", archiveDts);

		persistenceService.deleteByQuery(NotificationEvent.class, deleteQuery, queryParams);
	}

	@Override
	public void markEventAsRead(String eventId, String username)
	{
		Objects.requireNonNull(eventId);
		Objects.requireNonNull(username);

		NotificationEventReadStatus notificationEventReadStatus = new NotificationEventReadStatus();
		notificationEventReadStatus.setReadStatusId(persistenceService.generateId());
		notificationEventReadStatus.setEventId(eventId);
		notificationEventReadStatus.setUsername(username);

		persistenceService.persist(notificationEventReadStatus);
	}

}
