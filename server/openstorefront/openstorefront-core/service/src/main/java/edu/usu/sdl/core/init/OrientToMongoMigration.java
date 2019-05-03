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
package edu.usu.sdl.core.init;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.WriteModel;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import edu.usu.sdl.core.CoreSystem;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.entity.BaseEntity;
import edu.usu.sdl.openstorefront.core.entity.ComponentEvaluationSection;
import edu.usu.sdl.openstorefront.core.entity.ComponentExternalDependency;
import edu.usu.sdl.openstorefront.core.entity.ComponentTypeTemplate;
import edu.usu.sdl.openstorefront.core.entity.ComponentVersionHistory;
import edu.usu.sdl.openstorefront.core.entity.ContentSectionMedia;
import edu.usu.sdl.openstorefront.core.entity.FileAttributeMap;
import edu.usu.sdl.openstorefront.core.entity.FileDataMap;
import edu.usu.sdl.openstorefront.core.entity.SystemArchive;
import edu.usu.sdl.openstorefront.core.entity.SystemArchiveError;
import edu.usu.sdl.openstorefront.core.entity.TemplateBlock;
import edu.usu.sdl.openstorefront.core.entity.TemporaryMedia;
import edu.usu.sdl.openstorefront.core.entity.UserSavedSearch;
import edu.usu.sdl.openstorefront.core.entity.UserSubmissionComment;
import edu.usu.sdl.openstorefront.core.entity.UserSubmissionMedia;
import edu.usu.sdl.openstorefront.service.MongoPersistenceServiceImpl;
import edu.usu.sdl.openstorefront.service.manager.OrientDBManager;
import edu.usu.sdl.openstorefront.service.repo.MongoQueryUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class OrientToMongoMigration
		extends PostInitApplyOnce
{

	private static final Logger LOG = Logger.getLogger(OrientToMongoMigration.class.getName());

	private StringBuilder details = new StringBuilder();

	public OrientToMongoMigration()
	{
		super("DB-MIGRATION-Mongo");
	}

	@Override
	@SuppressWarnings("unchecked")
	protected String internalApply()
	{
		//check mongo in use
		if (!Convert.toBoolean(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_DB_USE_MONGO, "false"))) {
			return SKIP_APPLY;
		}

		//check for existing orient install
		File orientDir = new File(FileSystemManager.getInstance().getBaseDirectory() + "/db/databases/openstorefront");
		if (!orientDir.exists()) {
			return "Orient was not used; No Migration applied";
		}

		//proceed
		LOG.log(Level.INFO, "Staring DB Migration");
		CoreSystem.standby("Working on DB Migration");

		//init Orient
		updateStatus("Initializing Orient");
		OrientDBManager orientDBManager = OrientDBManager.getInstance();
		orientDBManager.initialize();

		MongoQueryUtil queryUtil = ((MongoPersistenceServiceImpl) service.getPersistenceService()).getQueryUtil();

		int tablesTransered = 1;
		try {

			List<String> tablesToTransfer = simpleTables();
			int totalTablesToTranser = tablesToTransfer.size();
			for (String className : tablesToTransfer) {

				//read table from Orient
				updateStatus("Reading: " + className + " (" + tablesTransered + " / " + totalTablesToTranser + ")");
				String query = "select from " + className;
				Class entityClass;
				try {
					entityClass = Class.forName(OpenStorefrontConstant.ENTITY_PACKAGE + "." + className);
				} catch (ClassNotFoundException ex) {
					throw new OpenStorefrontRuntimeException(ex);
				}

				OObjectDatabaseTx orientDB = orientDBManager.getConnection();
				List<BaseEntity> nonProxyRecords = new ArrayList<>();
				try {
					List<BaseEntity> records = orientDB.query(new OSQLSynchQuery<>(query), new HashMap<>());
					for (BaseEntity dbproxy : records) {
						BaseEntity nonProxied = orientDB.detachAll(dbproxy, true);
						nonProxyRecords.add(nonProxied);
					}
				} finally {
					orientDB.close();
				}

				//Mongo Drop collection (so we get an exact match)
				updateStatus("Droping collection: " + className);
				MongoCollection collection = queryUtil.getCollectionForEntity(entityClass);
				collection.drop();

				if (nonProxyRecords.isEmpty()) {
					updateStatus("No records to write.");
				} else {
					updateStatus("Writing to: " + className + " " + nonProxyRecords.size() + " records");
					List<WriteModel> writeModels = new ArrayList<>();
					for (BaseEntity entity : nonProxyRecords) {
						writeModels.add(new InsertOneModel(entity));
					}

					BulkWriteResult bulkWriteResult = collection.bulkWrite(writeModels);
					updateStatus("Inserted: " + bulkWriteResult.getInsertedCount() + " records");
				}

				tablesTransered++;
			}
		} finally {
			updateStatus("Shutdown Orient");
			orientDBManager.shutdown();
		}

		CoreSystem.resume("Done with DB Migration");
		LOG.log(Level.INFO, "Done with DB Migration");
		return "Transfered: " + tablesTransered;
	}

	private void updateStatus(String message)
	{
		LOG.log(Level.INFO, message);
		details.append(message).append("<br>");
		CoreSystem.setDetailedStatus(details.toString());
	}

	private List<String> simpleTables()
	{
		return Arrays.asList(
				"Alert",
				"ApplicationProperty",
				"AsyncTask",
				"AttributeCode",
				"AttributeType",
				"AttributeXRefMap",
				"AttributeXRefType",
				"Branding",
				"ChangeLog",
				"ChecklistQuestion",
				"ChecklistTemplate",
				"Component",
				"ComponentAttribute",
				"ComponentComment",
				"ComponentContact",
				"ComponentEvaluationSection",
				ComponentEvaluationSection.class.getSimpleName(),
				ComponentExternalDependency.class.getSimpleName(),
				"ComponentContact",
				"ComponentIntegration",
				"ComponentIntegrationConfig",
				"ComponentMedia",
				"ComponentQuestion",
				"ComponentQuestionResponse",
				"ComponentRelationship",
				"ComponentReview",
				"ComponentReviewCon",
				"ComponentReviewPro",
				"ComponentTag",
				"ComponentTracking",
				"ComponentType",
				ComponentTypeTemplate.class.getSimpleName(),
				"ComponentUpdateQueue",
				ComponentVersionHistory.class.getSimpleName(),
				"Contact",
				"ContactType",
				"ContentSection",
				ContentSectionMedia.class.getSimpleName(),
				"ContentSectionTemplate",
				"ContentSubSection",
				"DBLogRecord",
				"DashboardWidget",
				"DataSensitivity",
				"DataSource",
				"ErrorTicket",
				"Evaluation",
				"EvaluationChecklist",
				"EvaluationChecklistRecommendation",
				"EvaluationChecklistResponse",
				"EvaluationComment",
				"EvaluationSection",
				"EvaluationTemplate",
				"ExperienceTimeType",
				"Faq",
				"FaqCategoryType",
				"FeedbackTicket",
				FileAttributeMap.class.getSimpleName(),
				FileDataMap.class.getSimpleName(),
				"FileHistory",
				"FileHistoryError",
				"GeneralMedia",
				"HelpSection",
				"Highlight",
				"MediaFile",
				"NotificationEvent",
				"NotificationEventReadStatus",
				"Organization",
				"OrganizationType",
				"Plugin",
				"RecommendationType",
				"RelationshipType",
				"Report",
				"ResourceType",
				"ReviewCon",
				"ReviewPro",
				"ScheduledReport",
				"SearchOptions",
				"SecurityMarkingType",
				"SecurityPolicy",
				"SecurityRole",
				"SubmissionFormTemplate",
				"SupportMedia",
				SystemArchive.class.getSimpleName(),
				SystemArchiveError.class.getSimpleName(),
				"SystemSearch",
				TemplateBlock.class.getSimpleName(),
				TemporaryMedia.class.getSimpleName(),
				"TestEntity",
				"UserDashboard",
				"UserMessage",
				"UserProfile",
				"UserRegistration",
				"UserRole",
				UserSavedSearch.class.getSimpleName(),
				"UserSecurity",
				UserSubmissionComment.class.getSimpleName(),
				"UserSubmission",
				UserSubmissionMedia.class.getSimpleName(),
				"UserTracking",
				"UserTypeCode",
				"UserWatch",
				"WorkPlan",
				"WorkPlanLink",
				"WorkPlanSubStatusType",
				"WorkflowStatus"
		);
	}

	@Override
	public int getPriority()
	{
		return 1;
	}

}
