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
package edu.usu.sdl.apiclient.rest.resource;

import edu.usu.sdl.openstorefront.core.entity.ContentSectionMedia;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklist;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklistRecommendation;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklistResponse;
import edu.usu.sdl.openstorefront.core.entity.EvaluationComment;
import edu.usu.sdl.openstorefront.core.model.ContentSectionAll;
import edu.usu.sdl.openstorefront.core.view.ChecklistResponseView;
import edu.usu.sdl.openstorefront.core.view.EvaluationChecklistRecommendationView;
import edu.usu.sdl.openstorefront.core.view.EvaluationFilterParams;
import java.util.List;
import javax.ws.rs.core.Response;

/**
 *
 * @author ccummings
 */
public class EvaluationResourceImpl
{

	public Response activateEvaluation(String evaluationId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public List<EvaluationChecklistRecommendationView> addChecklistRecommendation(String evaluationId, String checklistId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response addChecklistRecommendation(String evaluationId, String checklistId, EvaluationChecklistRecommendation recommendation)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response addSection(String evaluationId, String sectionTemplateId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response checkEvaluationEntry(String evaluationId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response copyEvaluation(String evaluationId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response createEvaluation(Evaluation evaluation)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response deleteComment(String evaluationId, String commentId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response deleteEvaluation(String evaluationId, boolean force)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response deleteRecommendation(String evaluationId, String checklistId, String recommendationId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response deleteSection(String evaluationId, String sectionId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void deleteSectionMedia(String evaluationId, String sectionId, String sectionMediaId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getAllEvaluationComments(String evaluationId, String entity, String entityId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public List<ChecklistResponseView> getChecklistResponse(String evaluationId, String checklistId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getChecklistResponse(String evaluationId, String checklistId, String responseId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getEvaluation(String evaluationId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getEvaluationChecklist(String evaluationId, String checklistId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getEvaluationComments(String evaluationId, String commentId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getEvaluationDetails(String evaluationId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getEvaluationSection(String evaluationId, String sectionId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getEvaluationSectionDeatils(String evaluationId, String sectionId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getEvaluationSections(String evaluationId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getEvaluationStats(String assignedUser, String assignedGroup)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getEvaluations(EvaluationFilterParams evaluationFilterParams)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getSectionMedia(String evaluationId, String sectionId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response postComment(String evaluationId, EvaluationComment comment)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response publishEvaluation(String evaluationId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response saveSection(String evaluationId, String sectionId, ContentSectionAll contentSectionAll)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response toggleAcknowlegeComment(String evaluationId, String commentId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response toggleAllowNewSecionEvaluation(String evaluationId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response unpublishEvaluation(String evaluationId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response updateChecklistRecommendation(String evaluationId, String checklistId, String recommendationId, EvaluationChecklistRecommendation recommendation)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response updateChecklistResponse(String evaluationId, String checklistId, String responseId, EvaluationChecklistResponse checklistResponse)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response updateComment(String evaluationId, String commentId, EvaluationComment comment)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response updateEvaluation(String evaluationId, Evaluation evaluation)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response updateEvaluationChecklist(String evaluationId, String checklistId, EvaluationChecklist evaluationChecklist)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response updateSectionMedia(String evaluationId, String sectionId, String sectionMediaId, ContentSectionMedia sectionMedia)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
