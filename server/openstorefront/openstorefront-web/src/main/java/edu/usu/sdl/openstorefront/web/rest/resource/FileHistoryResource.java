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

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.FileDataMap;
import edu.usu.sdl.openstorefront.core.entity.FileFormat;
import edu.usu.sdl.openstorefront.core.entity.FileHistory;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryError;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryErrorType;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.model.DataMapModel;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.view.FileHistoryView;
import edu.usu.sdl.openstorefront.core.view.FileHistoryViewWrapper;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
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
	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
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
		if (SecurityUtil.isEntryAdminUser() == false) {
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

		List<FileHistory> fileHistories = service.getPersistenceService().queryByExample(queryByExample);

		FileHistoryViewWrapper fileHistoryViewWrapper = new FileHistoryViewWrapper();
		fileHistoryViewWrapper.getData().addAll(FileHistoryView.toView(fileHistories));
		fileHistoryViewWrapper.setTotalNumber(service.getPersistenceService().countByExampleSimple(queryByExample));

		//gather warnings and errors
		Map<String, List<FileHistoryError>> errorMap = service.getImportService().fileHistoryErrorMap();
		fileHistoryViewWrapper.getData().forEach(record -> {
			List<FileHistoryError> errors = errorMap.get(record.getFileHistoryId());
			if (errors != null) {
				long warningCount = errors.stream().filter(error -> error.getFileHistoryErrorType().equals(FileHistoryErrorType.WARNING)).count();
				record.setWarningsCount(warningCount);
				record.setErrorsCount(errors.size() - warningCount);
			}
		});

		return sendSingleEntityResponse(fileHistoryViewWrapper);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
	@APIDescription("Gets errors for a file")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(FileHistoryError.class)
	@Path("/{fileHistoryId}/errors")
	public List<FileHistoryError> getErrors(
			@PathParam("fileHistoryId") String fileHistoryId
	)
	{
		FileHistoryError fileHistoryError = new FileHistoryError();
		fileHistoryError.setFileHistoryId(fileHistoryId);
		return fileHistoryError.findByExample();
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
	@APIDescription("Download the original file")
	@Produces({MediaType.WILDCARD})
	@Path("/{fileHistoryId}/download")
	public Response downloadFileHistory(
			@PathParam("fileHistoryId") String fileHistoryId
	)
	{
		FileHistory fileHistory = new FileHistory();
		fileHistory.setFileHistoryId(fileHistoryId);
		fileHistory = fileHistory.find();
		if (fileHistory != null) {

			java.nio.file.Path path = fileHistory.pathToFileName();

			if (path.toFile().exists()) {
				Response.ResponseBuilder responseBuilder = Response.ok((StreamingOutput) (OutputStream output) -> {
					Files.copy(path, output);
				});
				responseBuilder.header("Content-Type", fileHistory.getMimeType());
				responseBuilder.header("Content-Disposition", "attachment; filename=\"" + StringProcessor.getJustFileName(fileHistory.getOriginalFilename()) + "\"");
				return responseBuilder.build();
			}
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(FileHistory.class)
	@Path("/{fileHistoryId}/reprocess")
	public Response reprocessFile(
			@PathParam("fileHistoryId") String fileHistoryId
	)
	{
		FileHistory fileHistory = new FileHistory();
		fileHistory.setFileHistoryId(fileHistoryId);
		fileHistory = fileHistory.find();
		if (fileHistory != null) {
			service.getImportService().reprocessFile(fileHistoryId);
		}

		return sendSingleEntityResponse(fileHistory);
	}

	//TODO: get rollback effect (Check what the rollback would do)
	@POST
	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{fileHistoryId}/rollback")
	public Response rollbackFile(
			@PathParam("fileHistoryId") String fileHistoryId
	)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();

		FileHistory fileHistory = new FileHistory();
		fileHistory.setFileHistoryId(fileHistoryId);
		fileHistory = fileHistory.find();
		if (fileHistory != null) {
			service.getImportService().rollback(fileHistoryId);
			response = Response.ok().build();
		}

		return response;
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
	@APIDescription("Gets file format for a type")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(FileFormat.class)
	@Path("/filetypes/{type}/formats")
	public List<FileFormat> getFileTypesForFormat(
			@PathParam("type") String type
	)
	{
		List<FileFormat> formats = service.getImportService().findFileFormats(type);
		return formats;
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
	@APIDescription("Gets all file formats that support mapping")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(FileFormat.class)
	@Path("/formats/mappingformats")
	public List<FileFormat> getMappingFormats()
	{
		List<FileFormat> formats = service.getImportService().getFileFormatsMapping();
		return formats;
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
	@APIDescription("Gets data mappings for a format.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(LookupModel.class)
	@Path("/formats/{format}/mappings")
	public List<LookupModel> getMappingsForFormat(
			@PathParam("format") String format
	)
	{
		List<LookupModel> mappings = new ArrayList<>();

		FileDataMap fileDataMapExample = new FileDataMap();
		fileDataMapExample.setActiveStatus(FileDataMap.ACTIVE_STATUS);
		fileDataMapExample.setFileFormat(format);

		List<FileDataMap> fileDataMaps = fileDataMapExample.findByExample();
		for (FileDataMap fileDataMap : fileDataMaps) {
			LookupModel lookupModel = new LookupModel();
			lookupModel.setCode(fileDataMap.getFileDataMapId());
			lookupModel.setDescription(fileDataMap.getName());

			mappings.add(lookupModel);
		}
		mappings.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, LookupModel.DESCRIPTION_FIELD));

		return mappings;
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
	@APIDescription("Deletes data mapping(s)")
	@Path("/formats/{format}/mappings/{fileDataMapId}")
	public Response removeDataMapping(
			@PathParam("format") String format,
			@PathParam("fileDataMapId") String fileDateMapId
	)
	{
		FileDataMap fileDataMapExample = new FileDataMap();
		fileDataMapExample.setFileFormat(format);
		fileDataMapExample.setFileDataMapId(fileDateMapId);

		fileDataMapExample = fileDataMapExample.find();
		if (fileDataMapExample != null) {
			service.getImportService().removeFileDataMap(fileDateMapId);
		}
		return Response.noContent().build();
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
	@APIDescription("Creates a new data mapping")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/formats/{format}/mappings")
	public Response createNewMapping(
			@PathParam("format") String format,
			DataMapModel dataMapModel
	)
	{
		return handleMappingSave(dataMapModel, true);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
	@APIDescription("Get a full data mapping record")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(DataMapModel.class)
	@Path("/formats/{format}/mappings/{fileDataMapId}")
	public Response getFileMapping(
			@PathParam("format") String format,
			@PathParam("fileDataMapId") String fileDateMapId
	)
	{
		DataMapModel dataMapModel = service.getImportService().getDataMap(fileDateMapId);
		return sendSingleEntityResponse(dataMapModel);
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
	@APIDescription("Copies data mapping record")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(FileDataMap.class)
	@Path("/formats/{format}/mappings/{fileDataMapId}/copy")
	public Response copyMapping(
			@PathParam("format") String format,
			@PathParam("fileDataMapId") String fileDateMapId
	)
	{
		FileDataMap fileDataMapCreated = null;

		FileDataMap fileDataMapExample = new FileDataMap();
		fileDataMapExample.setFileFormat(format);
		fileDataMapExample.setFileDataMapId(fileDateMapId);

		fileDataMapExample = fileDataMapExample.find();
		if (fileDataMapExample != null) {
			fileDataMapCreated = service.getImportService().copyDataMap(fileDateMapId);
		}
		return sendSingleEntityResponse(fileDataMapCreated);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
	@APIDescription("Exports data mapping record")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(FileDataMap.class)
	@Path("/formats/{format}/mappings/{fileDataMapId}/export")
	public Response exportMapping(
			@PathParam("format") String format,
			@PathParam("fileDataMapId") String fileDateMapId
	)
	{
		FileDataMap fileDataMap = new FileDataMap();
		fileDataMap.setFileFormat(format);
		fileDataMap.setFileDataMapId(fileDateMapId);

		fileDataMap = fileDataMap.find();
		if (fileDataMap != null) {

			DataMapModel dataMapModel = service.getImportService().getDataMap(fileDateMapId);
			String data;
			try {
				data = StringProcessor.defaultObjectMapper().writeValueAsString(dataMapModel);
			} catch (JsonProcessingException ex) {
				throw new OpenStorefrontRuntimeException("Unable to export file map. Unable able to generate JSON.", ex);
			}

			Response.ResponseBuilder response = Response.ok(data);
			response.header("Content-Type", MediaType.APPLICATION_JSON);
			response.header("Content-Disposition", "attachment; filename=\"fileImportMap-"
					+ StringProcessor.formatForFilename(fileDataMap.getName())
					+ ".json\"");
			return response.build();

		}
		return sendSingleEntityResponse(null);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
	@APIDescription("Updates a data mapping")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/formats/{format}/mappings/{fileDataMapId}")
	public Response updateMapping(
			@PathParam("format") String format,
			@PathParam("fileDataMapId") String fileDateMapId,
			DataMapModel dataMapModel
	)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();

		FileDataMap fileDataMapExample = new FileDataMap();
		fileDataMapExample.setFileFormat(format);
		fileDataMapExample.setFileDataMapId(fileDateMapId);

		fileDataMapExample = fileDataMapExample.find();
		if (fileDataMapExample != null) {
			dataMapModel.getFileDataMap().setFileDataMapId(fileDateMapId);
			dataMapModel.getFileDataMap().setFileFormat(format);
			response = handleMappingSave(dataMapModel, false);
		}
		return response;
	}

	private Response handleMappingSave(DataMapModel dataMapModel, boolean post)
	{
		ValidationModel validationModel = new ValidationModel(dataMapModel);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);

		if (validationResult.valid()) {
			FileDataMap fileDataMap = service.getImportService().saveFileDataMap(dataMapModel);

			if (post) {
				return Response.created(URI.create(
						"v1/resource/filehistory/formats/"
						+ fileDataMap.getFileFormat()
						+ "/mappings/" + fileDataMap.getFileDataMapId()))
						.entity(fileDataMap).build();
			} else {
				return sendSingleEntityResponse(fileDataMap);
			}
		}
		return sendSingleEntityResponse(validationResult.toRestError());
	}

}
