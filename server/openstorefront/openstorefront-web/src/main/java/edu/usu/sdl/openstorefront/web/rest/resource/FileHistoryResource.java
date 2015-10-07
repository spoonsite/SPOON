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
package edu.usu.sdl.openstorefront.web.rest.resource;

import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.FileHistory;
import edu.usu.sdl.openstorefront.core.view.FileHistoryView;
import edu.usu.sdl.openstorefront.core.view.FileHistoryViewWrapper;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.lang.reflect.Field;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.sourceforge.stripes.util.bean.BeanUtil;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/filehistory")
@APIDescription("File History hold the record of imported file.<br> To create a new record POST to Upload.action?UploadFile *Admin Role required")
public class FileHistoryResource
		extends BaseResource
{

	@GET
	@APIDescription("Gets file history records.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(FileHistoryViewWrapper.class)
	public Response getFileHistoryRecords(@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		FileHistory fileHistoryExample = new FileHistory();
		fileHistoryExample.setActiveStatus(filterQueryParams.getStatus());
		if (SecurityUtil.isAdminUser() == false) {
			fileHistoryExample.setCreateUser(SecurityUtil.getCurrentUserName());
		}

		FileHistory fileHistoryStartExample = new FileHistory();
		fileHistoryStartExample.setCreateDts(filterQueryParams.getStart());

		FileHistory fileHistoryEndExample = new FileHistory();
		fileHistoryEndExample.setCreateDts(filterQueryParams.getEnd());

		QueryByExample queryByExample = new QueryByExample(fileHistoryExample);

		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(fileHistoryStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(fileHistoryEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		queryByExample.setMaxResults(filterQueryParams.getMax());
		queryByExample.setFirstResult(filterQueryParams.getOffset());
		queryByExample.setSortDirection(filterQueryParams.getSortOrder());

		FileHistory fileHistorySortExample = new FileHistory();
		Field sortField = ReflectionUtil.getField(fileHistorySortExample, filterQueryParams.getSortField());
		if (sortField != null) {
			BeanUtil.setPropertyValue(sortField.getName(), fileHistorySortExample, QueryByExample.getFlagForType(sortField.getType()));
			queryByExample.setOrderBy(fileHistorySortExample);
		}

		List<FileHistory> fileHistories = service.getPersistenceService().queryByExample(FileHistory.class, queryByExample);

		FileHistoryViewWrapper fileHistoryViewWrapper = new FileHistoryViewWrapper();
		fileHistoryViewWrapper.getData().addAll(FileHistoryView.toView(fileHistories));
		fileHistoryViewWrapper.setTotalNumber(service.getPersistenceService().countByExample(queryByExample));

		return sendSingleEntityResponse(fileHistoryViewWrapper);
	}

	//TODO: get errors
	//TODO: get rollback effect (Check what the rollback would do)
	//TODO: rollback (Option to restore record; override or sync)
	//TODO: formats for file type
}
