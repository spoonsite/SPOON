<%--
Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>

<%@page import="edu.usu.sdl.openstorefront.common.manager.PropertiesManager"%>
<%
	String appVersion = PropertiesManager.getInstance().getApplicationVersion();
	request.setAttribute("appVersion", appVersion);
%>

<link href="webjars/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>

<style>
	.print-general-block {
		border: 1px solid black;		
	}

	.print-section {
		padding: 5px;
	}

	.print-right-block {
		float: right;
		position: relative;
		left: -1px;
		width: 250px;		
	}

	.print-left-block {
		overflow: hidden;		
	}	

	.print-tags {
		padding: 3px;		
		border: 1px solid lightgrey;
		border-radius: 2px 2px 2px 2px;
	}

	.print-table {
		border-collapse: collapse;
		border: 1px solid lightgrey;			
	}

	td.print-table {
		border-collapse: collapse;
		border: 1px solid lightgrey;
		padding: 5px;
		max-width: 100px;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	tr.print-table:nth-child(odd) {
		background: whitesmoke;
	}

	.version-description {
		font-size: 11px;
		color: #a3a3a3;
	}

	.score-td {
		padding: 5px 15px 5px 15px;
	}

	.score-average {
		text-align: center;
		font-weight: bold;
	}

</style>

<!-- Right Block -->
<tpl if="show.evaluation">
	<tpl if="evaluation && evaluation.evaluationSections && evaluation.evaluationSections.length &gt; 0">
		<div class="print-right-block print-general-block print-section">
			<h2>Reusability Factors (5=best)</h2>
			<table class="print-table" style="width: 100%">
				<tpl for="evaluation.evaluationSections">
					<tr class="print-table">
						<td class="print-table">{name}</td>
						<td class="print-table">{display}</td>
					</tr>
				</tpl>
			</table>
		</div>
	</tpl>
</tpl>

<tpl if="!show.evaluation || !evaluation || !evaluation.evaluationSections || !evaluation.evaluationSections.length &gt; 0">
	<tpl if="(show.vitals && vitals.length &gt; 0) || (show.contacts && contacts.length &gt; 0)">
		<div class="print-right-block print-general-block print-section">
			<tpl if="show.vitals ">
				<tpl if="vitals && vitals.length &gt; 0">		
					<h3>Vitals:</h3>
					<table class="print-table" style="width: 100%">
						<tpl for="vitals">
							<tr class="print-table">
								<td class="print-table">
									<b>{label}</b>	
									<tpl if="privateFlag"> <span class="private-badge">private</span></tpl>
								</td>
								<td class="print-table alert-{highlightStyle}">
									<tpl if="securityMarkingType">({securityMarkingType}) </tpl><b>{value}</b>
									<tpl if="comment"><hr>Comment: {comment}</tpl>
								</td>
							</tr>
						</tpl>					
					</table>			
				</tpl>			
			</tpl>
			<tpl if="show.contacts">
				<tpl if="contacts && contacts.length &gt; 0">
					<h3>Points of Contact:</h3>		
					<tpl for="contacts">
						<div style="font-size: 14px;">
							{positionDescription}
						</div>
						<table class="print-table" style="width: 100%">
							<tr class="print-table">
								<td class="print-table">
									Name
								</td>
								<td class="print-table">
							<tpl if="securityMarkingType">({securityMarkingType}) </tpl><b>{name}</b><br>
							{organization}
							</td>
							</tr>
							<tr class="print-table">
								<td class="print-table">
									Email / Phone 
								</td>
								<td class="print-table">
							<tpl if="email">{email}</tpl><br>
							<tpl if="phone">{phone}</tpl>
							</td>
							</tr>						
						</table>
					</tpl>								
				</tpl>
			</tpl>				
		</div>	
	</tpl>
</tpl>



<!-- Left block -->
<tpl if="show.general || show.badges">
	<div class="print-left-block print-general-block">
		<tpl if="show.general">
			<div class="print-section">
				<b>Entry:</b> {componentName}<br>
				<b>Organization:</b> {organization}<br>
				<b>Last Activity Date:</b> {lastActivityDate:date("m/d/Y H:i:s")}<br>
				<tpl if="show.views">
					<b>Views:</b> {componentViews}<br>
				</tpl>
				<tpl if="securityMarkingType"><b>Highest Classification:</b> ({securityMarkingType})</tpl>
				<tpl if="show.badges">
					<tpl for="attributes">
						<tpl if="badgeUrl"><img src="{badgeUrl}" title="{codeDescription}" width="40" /></tpl>
					</tpl>
				</tpl>				
			</div>
		</tpl>	
	</div>
</tpl>	
<tpl if="show.tags">
	<div class="print-left-block print-general-block print-section" style="margin-top: -1px;" >
		<tpl if="tags">
			<b>Tags: </b>
			<tpl for="tags">
				<span class="print-tags">
					<tpl if="securityMarkingType">({securityMarkingType}) </tpl>{text}
				</span>
			</tpl>
		</tpl>
	</div>
</tpl>
<tpl if="show.description">
	<div class="print-left-block print-section">
		<h3><tpl if="componentSecurityMarkingType">({componentSecurityMarkingType}) </tpl>Description: </h3>
		{description}
	</div>
</tpl>
<tpl if="show.resources">
	<tpl if="resources && resources.length &gt; 0">
		<div class="print-left-block print-section">
			<h3>Resources:</h3>			
			<table class="print-table" style="width: 100%">
				<tr class="print-table">
					<td class="print-table">
						<b>Resource Type / Link</b>
					</td>
				</tr>					
				<tpl for="resources">
					<tr class="print-table">
						<td class="print-table">
							<b>{resourceTypeDesc}</b><br>
					<tpl if="securityMarkingType">({securityMarkingType}) </tpl>{link}
					</td>
					</tr>
				</tpl>					
			</table>				
		</div>		
	</tpl>
</tpl>

<tpl if="show.evaluation && evaluation && evaluation.evaluationSections && evaluation.evaluationSections.length &gt; 0">
	<tpl if="show.vitals ">
		<tpl if="vitals && vitals.length &gt; 0">
			<div class="print-left-block print-section ">
				<h3>Vitals:</h3>
				<table class="print-table" style="width: 100%">
					<tpl for="vitals">
						<tr class="print-table">
							<td class="print-table">
								<b>{label}</b>	
								<tpl if="privateFlag"> <span class="private-badge">private</span></tpl>
							</td>
							<td class="print-table alert-{highlightStyle}">
								<tpl if="securityMarkingType">({securityMarkingType}) </tpl><b>{value}</b>
								<tpl if="comment"><hr>Comment: {comment}</tpl>
							</td>
						</tr>
					</tpl>					
				</table>
			</div>
		</tpl>	
	</tpl>
	<tpl if="show.contacts">
		<tpl if="contacts && contacts.length &gt; 0">
			<div class="print-left-block print-section">
				<h3>Points of Contact:</h3>
				<tpl for="contacts">
					<div style="font-size: 14px;">
						{positionDescription}
					</div>
					<table class="print-table" style="width: 100%">
						<tr class="print-table">
							<td class="print-table" style="width: 200px;">
								Name
							</td>
							<td class="print-table">
						<tpl if="securityMarkingType">({securityMarkingType}) </tpl><b>{name}</b><br>
						{organization}
						</td>
						</tr>
						<tr class="print-table">
							<td class="print-table">
								Email / Phone 
							</td>
							<td class="print-table">
						<tpl if="email">{email}</tpl><br>
						<tpl if="phone">{phone}</tpl>
						</td>
						</tr>						
					</table>
				</tpl>			
			</div>		
		</tpl>
	</tpl>
</tpl>

<tpl if="show.dependencies">
	<tpl if="dependencies && dependencies.length &gt; 0">
		<div class="print-left-block print-section">
			<h3>Dependencies:</h3>				
			<table class="print-table" style="width: 100%">										
				<tpl for="dependencies">
					<tr class="print-table">
						<td class="print-table">
					<tpl if="securityMarkingType">({securityMarkingType}) </tpl><b>{dependencyName} {version}</b><br>
					{comment}
					</td>
					</tr>
				</tpl>					
			</table>				
		</div>		
	</tpl>
</tpl>
<tpl if="show.relationships">
	<tpl if="relationships && relationships.length &gt; 0">
		<div class="print-left-block print-section">
			<h3>Relationships:</h3>				
			<table class="print-table" style="width: 100%">										
				<tpl for="relationships">
					<tr class="print-table">
						<td class="print-table">
							{ownerComponentName}								
						</td>
						<td class="print-table" style="text-align: center">
							<b>{relationshipTypeDescription}</b>								
						</td>
						<td class="print-table">
							{targetComponentName}								
						</td>							
					</tr>
				</tpl>					
			</table>				
		</div>		
	</tpl>
</tpl>

<tpl if="show.reviews">
	<tpl if="reviews && reviews.length &gt; 0">
		<div class="pageBreak">
			<h2>Reviews</h2>
			<hr>
			<tpl for="reviews">
				<table style="width:100%">
					<tpl if="activeStatus == 'P'"><tr><td colspan="3" class="alert-warning" style="text-align: center; font-size: 1.5em; font-weight: bold;"><i class="fa fa-warning"></i> Review pending admin approval before being made public.</td></tr></tpl>
					<tr>
						<td valign="top">
							<h1><tpl if="securityMarkingType">({securityMarkingType}) </tpl>{title} <br> <tpl for="ratingStars"><i class="fa fa-{star} rating-star-color"></i></tpl></h1>								
							<div class="review-who-section">{username} ({userTypeCode}) - {[Ext.util.Format.date(values.updateDate, "m/d/y")]}<tpl if="recommend"> - <b>Recommend</b></tpl></div><br>
							<b>Organization:</b> {organization}<br>
							<b>Experience:</b> {userTimeCode}<br>							
							<b>Last Used:</b> {[Ext.util.Format.date(values.lastUsed, "m/Y")]}<br>
						</td>
						<td valign="top" width="20%">
					<tpl if="pros.length &gt; 0">					
						<div class="review-pro-con-header">Pros</div>
						<tpl for="pros">
							- {text}<br>
						</tpl></tpl>
					</td>
					<td valign="top" width="20%">
					<tpl if="cons.length &gt; 0">
						<div class="review-pro-con-header">Cons</div>
						<tpl for="cons">
							- {text}<br>
						</tpl></tpl>
					</td>
					</tr></table>
				<br><b>Comments:</b><br>{comment}
				<br><br><hr>
			</tpl>			
		</div>
	</tpl>
</tpl>
<tpl if="show.questions">
	<tpl if="questions && questions.length &gt; 0">
		<div class="pageBreak">
			<h2>Questions</h2>
			<hr>
			<tpl for="questions">
				<tpl if="activeStatus == 'P'"><div class="alert-warning" style="text-align: center; font-size: 1.5em; font-weight: bold;"><i class="fa fa-warning"></i> Question pending admin approval before being made public.</div></tpl>
				<div class="question-question"><span class="question-response-letter-q">Q.</span> <tpl if="securityMarkingType">({securityMarkingType}) </tpl><b>{question}</b></div>
				<div class="question-info">
					{username} ({userType}) - {[Ext.util.Format.date(values.questionUpdateDts, "m/Y")]}
				</div>
				<div style="padding-left: 10px; padding-right: 10px;">
					<tpl for="responses">
						<tpl if="activeStatus == 'P'"><div class="alert-warning" style="text-align: center; font-size: 1.5em; font-weight: bold;"><i class="fa fa-warning"></i> Answer pending admin approval before being made public.</div></tpl>
						<div class="question-response"><span class="question-response-letter">A.</span> <tpl if="securityMarkingType">({securityMarkingType}) </tpl>{response}</div>
						<div class="question-info">{username} ({userType}) - {[Ext.util.Format.date(values.answeredDate, "m/d/Y")]}</div><br>	
						<hr>
					</tpl>
				</div>
			</tpl>
		</div>	
	</tpl>
</tpl>
<tpl if="show.media">
	<tpl if="componentMedia && componentMedia.length &gt; 0">
		<div class="pageBreak">
			<h2>Media</h2>
			<hr>
			<tpl for="componentMedia">
				<tpl if="mediaTypeCode == 'IMG'">
					<img src="{link}" style="width: 100%"><br>
					<tpl if="securityMarkingType">({securityMarkingType}) </tpl><tpl if="caption">{caption}<br></tpl>
				</tpl>
				<tpl if="mediaTypeCode != 'IMG'">
					<b>Non-Printable:</b> {contentType}<tpl if="securityMarkingType">({securityMarkingType}) </tpl><tpl if="caption"> - {caption}</tpl><tpl if="originalFileName"> - {originalFileName}</tpl><br>
				</tpl>			
				<br>
			</tpl>
		</div>	
	</tpl>
</tpl>

<!-- Evaluation Details -->
<tpl for="fullEvaluations">
	<div class="pageBreak" style="clear: both;">
		<tpl if="parent.show['evaluationDetails'+evaluation.version]">
			<br />
			<h2 class="quickView toggle-collapse">Evaluation
				<tpl if="evaluationCount &gt; 1">
					<div class="version-description">version - {evaluation.version}</div>
				</tpl>
			</h2>

			<!-- Reusability Factors -->
			<tpl if="parent.show['evalReusability'+evaluation.version] && evaluationScores && evaluationScores.length &gt; 0">
				<h3 class="quickView toggle-collapse">Reusability Factors (5=best) <div data-qtip="Expand panel" style="float: right;" data-ref="toolEl" class=" x-tool-tool-el x-tool-img x-tool-expand-bottom eval-toggle-caret" role="presentation"></div></h3>
				<table class="score-table">
					<tr>
						<tpl for="evaluationScores">
								<td class="score-td">&nbsp;&nbsp;{title}:</td>
								<td class="score-average">{average}</td>
								{[xindex%3==0?'</tr><tr>':'']}
						</tpl>
					</tr>
				</table>
			</tpl>

			<!-- Checklist Summary -->
			<tpl if="parent.show['evalChecklistSummary'+evaluation.version] && checkListAll.evaluationChecklist.summary">
				<div>
					<h3 class="quickView toggle-collapse">Evaluation Checklist Summary:</h3>
					<p>{checkListAll.evaluationChecklist.summary}</p>
				</div>
			</tpl>

			<!-- Evaluation Recommendations -->
			<tpl if="parent.show['evalRecommendations'+evaluation.version] && checkListAll.recommendations && checkListAll.recommendations.length &gt; 0">
				<h3 class="quickView toggle-collapse">Evaluation Recommendations <div data-qtip="Expand panel" style="float: right;" data-ref="toolEl" class=" x-tool-tool-el x-tool-img x-tool-expand-bottom eval-toggle-caret" role="presentation"></div></h3>
				<table class="quickView-table" width="100%">
					<tr><th class="details-table">Type</th><th class="details-table">Recommendation</th><th class="details-table">Reason</th></tr>
					<tpl for="checkListAll.recommendations">
						<tr class="details-table">
							<td class="details-table">{recommendationTypeDescription}</td>
							<td class="details-table" style="text-align: center">{recommendation}</td>
							<td class="details-table">{reason}</td>
						</tr>
					</tpl>
				</table>
			</tpl>

			<!-- Content Sections -->
			<tpl if="parent.show['evalContentSections'+evaluation.version] && contentSections && contentSections.length &gt; 0">
				<tpl for="contentSections">
					<h3 class="quickView toggle-collapse">{section.title}</h3>
					<div>{section.content}</div>
					<tpl if="subsections.length &gt; 0">
						<tpl for="subsections">
							<div>
								<b>{title}</b>
							</div>
							{content}
						</tpl>
					</tpl>
				</tpl>
			</tpl>

			<!-- Evaluation Checklist Details -->
			<tpl if="parent.show['evalChecklistDetails'+evaluation.version] && checkListAll.responses && checkListAll.responses.length &gt; 0">
				<h3 class="quickView toggle-collapse">Evaluation Checklist Details <div data-qtip="Expand panel" style="float: right;" data-ref="toolEl" class=" x-tool-tool-el x-tool-img x-tool-expand-bottom eval-toggle-caret" role="presentation"></div></h3>
				<table class="quickView-table" width="100%">
					<tr>
						<th class="details-table">QID</th>
						<th class="details-table">Section</th>
						<th class="details-table">Question</th>
						<th class="details-table">Score</th>
						<th class="details-table">Response</th>
					</tr>
					<tpl for="checkListAll.responses">
						<tr class="details-table">
							<!-- QID - Question Details -->
							<td class="details-table">
								<b>{question.qid}</b>
							</td>

							<!-- Section -->
							<td class="details-table" style="text-align: center">{question.evaluationSectionDescription}</td>

							<!-- Question -->
							<td class="details-table">{question.question}</td>

							<!-- Score - Score Details -->
							<td class="details-table">
								<b>{score}</b>
							</td>

							<!-- Response -->
							<td class="details-table">{response}</td>
						</tr>
						<tr><td></td></tr>
					</tpl>
				</table>
			</tpl>
		</tpl>
	</div>
</tpl>
