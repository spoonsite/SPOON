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

<style>
	.quickView {
		background-color: #555555;
		color: #fff;
		padding-left: 5px;
		border: 1px solid #ddd;
		font-size: 20px;
		padding-top: 5px;
		padding-bottom: 5px;		
	}
	.quickView-tableheader {
		background-color: lightgrey;
	}
	.quickView-tableall {    
		border: 1px solid #ddd;
		text-align: left;
		padding: 5px;
	}

	.quickView-table {
		border-collapse: collapse;
		width: 100%;
	}

	.quickView-table:nth-child(odd) {
		background: white;
	}

	.quickView-table:nth-child(even) {
		background: whitesmoke;
	}	

	.quickView-table-padding {
		padding: 15px;
	}	

	.review-section
	{
		width:100%;
		border-bottom-style:solid;
		border-bottom-width: thin;
		border-bottom-color:#000000;
	}

	.review-section .label
	{
		font-weight: bold;
	}
	.review-section .title, 
	.review-section .rating
	{
		font-weight: bold;
		font-size: 2em;
		padding: 10px 0px;
	}

	.review-section .comments
	{
		padding-bottom: 20px;
	}

	.review-section .details,
	.review-section .pros,
	.review-section .cons
	{
		display: inline-block;
		vertical-align: top;
		padding-bottom: 20px;
	}
	.review-section .pros,
	.review-section .cons
	{
		width: 20%;
		min-width: 100px;
	}
	.review-section .details
	{
		width: 58%;
	}

	.review-section .alert-warning
	{
		font-weight: bold;
		font-size: 1.25em;
		margin: 5px 0px;
	}

	.clearfix:after { 
	   content: "."; 
	   visibility: hidden; 
	   display: block; 
	   height: 0; 
	   clear: both;
	}

	.version-description {
		font-size: 11px;
		color: #a3a3a3;
	}
	.evaluation-section {
		width: 97%;
		margin-left: 3%;
	}

	.eval-visible-true, .eval-visible-false {
		max-height: 0;
		opacity: 0;
        overflow-y: hidden;
        -webkit-transition: all 1.0s ease-in-out;
        -moz-transition: all 1.0s ease-in-out;
        -o-transition: all 1.0s ease-in-out;
        transition: all 1.0s ease-in-out;
	}
	.eval-visible-true {
		max-height: 9999px;
		opacity: 1;
		visibility: visible;
	}
	.eval-toggle-caret {
		margin-right: 12px;
	}

	.evaluation-container {
		overflow-y: scroll;
	}

</style>
<tpl if="name">
	<h1>{name}</h1>
	<p>{organization}</p>
	<tpl if="version">
		<p><b>Version:</b> {version}</p>
	</tpl>
	<tpl if="releaseDate">
		<p><b>Release Date:</b> {releaseDate}</p>
	</tpl>
	<p><b>Entry Type:</b> {componentTypeLabel}</p>
	<tpl for="attributes">
		<tpl if="badgeUrl">
			<img src="{badgeUrl}" title="{codeDescription}" width="40" />
		</tpl>
	</tpl>
	<tpl if="releaseDate">
		<br>
		<b>Tags: </b>
		<tpl for="tags">
			<span class="searchresults-tag">
				{text}
			</span>
		</tpl>		
	</tpl>	
	<br>
	<br>
	<div>
		<h3 class="quickView toggle-collapse">Description <div style="float: right;" data-ref="toolEl" class=" x-tool-tool-el x-tool-img x-tool-expand-top eval-toggle-caret" role="presentation"></div></h3>
		<section class="eval-visible-true">
			<p>{description}</p>
			<br>
			<br>
		</section>
	</div>
	<tpl if="evaluation.evaluationSections && evaluation.evaluationSections.length &gt; 0">
		<div>
			<h3 class="quickView toggle-collapse">Evaluation Information <div style="float: right;" data-ref="toolEl" class=" x-tool-tool-el x-tool-img x-tool-expand-bottom eval-toggle-caret" role="presentation"></div></h3>
			<section class="eval-visible-false">
				<table class="quickView-table" border="1" >	
					<tr>
						<th class="quickView-tableheader quickView-tableall quickView-table-padding">Name</th>
						<th class="quickView-tableheader quickView-tableall quickView-table-padding">Score</th>
					</tr>		
					<tpl for="evaluation.evaluationSections">			
						<tr>
							<td class="quickView-tableall">{name}</td>
							<td class="quickView-tableall">{display}</td>
						</tr>
					</tpl>
				</table>		
				<tpl if="evalLevels && (evalLevels.level || evalLevels.state || evalLevels.intent)">
					<section>
						<br>
						<table class="details-table" width="100%">					
							<tpl if="evalLevels.level">
								<tr class="details-table">
									<th class="details-table"><b>{evalLevels.level.typeDesciption}</b></th>
									<td class="details-table highlight-{evalLevels.level.highlightStyle}" ><h3>{evalLevels.level.label}</h3>{evalLevels.level.description}</td>
								</tr>
							</tpl>
							<tpl if="evalLevels.state">
								<tr class="details-table">
									<th class="details-table"><b>{evalLevels.state.typeDesciption}</b></th>
									<td class="details-table highlight-{evalLevels.state.highlightStyle}" ><h3>{evalLevels.state.label}</h3>{evalLevels.state.description}</td>
								</tr>
							</tpl>
							<tpl if="evalLevels.intent">
								<tr class="details-table">
									<th class="details-table"><b>{evalLevels.intent.typeDesciption}</b></th>
									<td class="details-table highlight-{evalLevels.intent.highlightStyle}" ><h3>{evalLevels.intent.label}</h3>{evalLevels.intent.description}</td>
								</tr>
							</tpl>
						</table>		
					</section>		
				</tpl>
			</section>
		</div>
	</tpl>		
	<tpl if="componentMedia && componentMedia.length &gt; 0">
		<div>
			<h3 class="quickView toggle-collapse">Media <div style="float: right;" data-ref="toolEl" class=" x-tool-tool-el x-tool-img x-tool-expand-bottom eval-toggle-caret" role="presentation"></div></h3>
			<section class="eval-visible-false">
				<table><tr><td>
					<tpl for="componentMedia">				
						<div class="detail-media-block-quick">
							<tpl switch="mediaTypeCode">
								<tpl case="IMG">
									<img src="{link}" height="150" alt="{[values.caption ? values.caption : values.filename]}"  />		
								<tpl case="AUD">
									<i class="fa fa-file-sound-o" style="font-size: 11em;"></i><br><br>
								<tpl case="VID">
									<i class="fa fa-file-video-o" style="font-size: 11em;"></i><br><br>
								<tpl case="ARC">
									<i class="fa fa-file-archive-o" style="font-size: 11em;" ></i><br><br>
								<tpl case="TEX">
									<i class="fa fa-file-text-o" style="font-size: 11em;"></i><br><br>
								<tpl default>
									<i class="fa fa-file-o" style="font-size: 11em;" ></i><br><br>
							</tpl>
							<tpl if="caption"><p class="detail-media-caption">{caption}</p></tpl>
						</div>					
					</tpl>
					</td></tr>	
				</table>				

			</section>		
		</div>
	</tpl>
	<tpl if="resources && resources.length &gt; 0">
		<div>
			<h3 class="quickView toggle-collapse">Resources <div style="float: right;" data-ref="toolEl" class=" x-tool-tool-el x-tool-img x-tool-expand-bottom eval-toggle-caret" role="presentation"></div></h3>
			<section class="eval-visible-false">
				<table class="quickView-table" border="1" >
					<tr>
						<th class="quickView-tableheader quickView-tableall quickView-table-padding">Type</th>															
						<th class="quickView-tableheader quickView-tableall quickView-table-padding">Link</th>					
					</tr>		
					<tpl for="resources">
						<tr class="quickView-table">
							<td class="quickView-tableall" style="width: 175px;">{resourceTypeDesc}</td>						
							<td class="quickView-tableall">
								<a href='{actualLink}' target="_blank">{originalLink}</a><br>
								{description}
							</td>
						</tr>			
					</tpl>
				</table>	
			</section>		
		</div>
	</tpl>	
	<tpl if="contacts && contacts.length &gt; 0">	
		<div>
			<h3 class="quickView toggle-collapse">Contacts <div style="float: right;" data-ref="toolEl" class=" x-tool-tool-el x-tool-img x-tool-expand-bottom eval-toggle-caret" role="presentation"></div></h3>
			<section class="eval-visible-false">
				<table class="quickView-table" border="1">	
					<tr>
						<th class="quickView-tableheader quickView-tableall quickView-table-padding">Position</th>
						<th class="quickView-tableheader quickView-tableall quickView-table-padding">Info</th>
					</tr>
					<tpl for="contacts">
						<tr class="quickView-table">
							<td class="quickView-tableall">{positionDescription}</td>
							<td class="quickView-tableall">
								<b>{firstName} {lastName}</b><br>
								{organization}<br>
								{email}<br>
								{phone}
							</td>
						</tr>			
					</tpl>
				</table>	
			</section>
		</div>
	</tpl>
	<tpl if="dependencies && dependencies.length &gt; 0">
		<div>
			<h3 class="quickView toggle-collapse">Dependencies <div style="float: right;" data-ref="toolEl" class=" x-tool-tool-el x-tool-img x-tool-expand-bottom eval-toggle-caret" role="presentation"></div></h3>
			<section class="eval-visible-false">
				<table class="details-table" width="100%">
					<tpl for="dependencies">
						<tr class="quickView-table">
							<td class="details-table"><b>{dependencyName} {version}</b> <br>
						<tpl if="dependancyReferenceLink"><a href="{dependancyReferenceLink}" class="details-table" target="_blank">{dependancyReferenceLink}</a><br></tpl> 
						<tpl if="comment">{comment}</tpl> 
						</td>
						</tr>
					</tpl>
				</table>	
			</section>		
		</div>
	</tpl>
	<tpl if="vitals && vitals.length &gt; 0">
		<div>
			<h3 class="quickView toggle-collapse">Entry Vitals <div style="float: right;" data-ref="toolEl" class=" x-tool-tool-el x-tool-img x-tool-expand-bottom eval-toggle-caret" role="presentation"></div></h3>
			<section class="eval-visible-false">
				<table class="quickView-table" border="1">				
					<tpl for="vitals">
						<tr class="quickView-table">
							<td class="quickView-tableall" style="width: 30%;"><b>{label}</b></td>
							<td class="quickView-tableall">{value}</td>
						</tr>			
					</tpl>
				</table>
			</section>		
		</div>
	</tpl>	
	<tpl if="relationships && relationships.length &gt; 0">
		<div>
			<h3 class="quickView toggle-collapse">Relationships <div style="float: right;" data-ref="toolEl" class=" x-tool-tool-el x-tool-img x-tool-expand-bottom eval-toggle-caret" role="presentation"></div></h3>
			<section class="eval-visible-false">
				<table class="quickView-table" width="100%">
					<tr><th class="details-table">Entry</th><th class="details-table">Relationship Type</th><th class="details-table">Related Entry</th></tr>
					<tpl for="relationships">
						<tr class="details-table">
							<td class="details-table">{ownerComponentName}</td>
							<td class="details-table" style="text-align: center"><b>{relationshipTypeDescription}</b></td>
							<td class="details-table"><a href="view.jsp?id={targetComponentId}" class="details-table" target="_blank">{targetComponentName}</a></td>
						</tr>
					</tpl>
				</table>			
			</section>	
		</div>
	</tpl>
	<tpl if="reviews && reviews.length &gt; 0">
		<div>
			<h3 class="quickView toggle-collapse">User Reviews <div style="float: right;" data-ref="toolEl" class=" x-tool-tool-el x-tool-img x-tool-expand-bottom eval-toggle-caret" role="presentation"></div></h3>
			<section class="eval-visible-false">
				<tpl for="reviews">
					<div class="review-section">
						<tpl if="activeStatus == 'P'"><div class="alert-warning" style="text-align: center;"><i class="fa fa-warning"></i> Review pending admin approval before being made public.</div></tpl>
						<div class="details">
							<div class="title">{title}</div>
							<div class="rating"><tpl for="ratingStars"><i class="fa fa-{star} rating-star-color"></i></tpl></div>
							<div class="review-who-section">
								{username} ({userTypeCode}) - {[Ext.util.Format.date(values.updateDate, "m/d/y")]}<tpl if="recommend"> - <strong>Recommend</strong></tpl>	
							</div>
							<div><span class="label">Organization:</span> {organization}</div>
							<div><span class="label">Experience:</span> {userTimeCode}</div>							
							<div><span class="label">Last Used:</span> {[Ext.util.Format.date(values.lastUsed, "m/Y")]}</div>
						</div>			
						<div class="pros">
							<tpl if="pros.length &gt; 0">					
								<div class="review-pro-con-header">Pros</div>
								<tpl for="pros">
									- {text}<br>
								</tpl></tpl>
						</div>		
						<div class="cons">
							<tpl if="cons.length &gt; 0">
								<div class="review-pro-con-header">Cons</div>
								<tpl for="cons">
									- {text}<br>
								</tpl></tpl>
						</div>				
						<div class="comments">
							<span class="label">Comments:</span>			
							<div>{comment}</div>
						</div>				
					</div>
				</tpl>
			</section>		
		</div>
	</tpl>
	<tpl if="questions && questions.length &gt; 0">
		<div>
			<h3 class="quickView toggle-collapse">User Questions <div style="float: right;" data-ref="toolEl" class=" x-tool-tool-el x-tool-img x-tool-expand-bottom eval-toggle-caret" role="presentation"></div></h3>
			<section class="eval-visible-false">
				<tpl for="questions">
					<tpl if="activeStatus == 'P'"><div class="alert-warning" style="text-align: center; font-weight: bold;"><i class="fa fa-warning"></i> Question pending admin approval before being made public.</div></tpl>
					<div class="question-question"><span class="question-response-letter-q">Q.</span> <b>{question}</b></div>
					<div class="question-info">
						{username} ({userType}) - {[Ext.util.Format.date(values.questionUpdateDts, "m/Y")]}
					</div>
					<div style="padding-left: 10px; padding-right: 10px;">
						<tpl for="responses">
							<tpl if="activeStatus == 'P'"><div class="alert-warning" style="text-align: center; font-weight: bold;"><i class="fa fa-warning"></i> Answer pending admin approval before being made public.</div></tpl>
							<div class="question-response"><span class="question-response-letter">A.</span> {response}</div>
							<div class="question-info">{username} ({userType}) - {[Ext.util.Format.date(values.answeredDate, "m/d/Y")]}</div><br>	
							<hr>
						</tpl>
					</div>			
				</tpl>	
			</section>
		</div>
	</tpl>

	<!-- Evaluations -->
	<tpl for="fullEvaluations">
		<div>
			<h3 class="quickView toggle-collapse">Evaluation <div style="float: right;" data-ref="toolEl" class=" x-tool-tool-el x-tool-img x-tool-expand-bottom eval-toggle-caret" role="presentation"></div>
				<tpl if="evaluationCount &gt; 1">
					<div class="version-description">version - {evaluation.version}</div>
				</tpl>
			</h3>
			<div class="evaluation-section clearfix">
				<section class="eval-visible-false">
					<tpl if="evaluationScores && evaluationScores.length &gt; 0">
						<div>
							<h3 class="quickView toggle-collapse">Reusability Factors (5=best) <div style="float: right;" data-ref="toolEl" class=" x-tool-tool-el x-tool-img x-tool-expand-bottom eval-toggle-caret" role="presentation"></div></h3>
							<section class="eval-visible-false">
								<div class="rolling-container clearfix">			
									<div class="rolling-container-row">
										<tpl for="evaluationScores">	
											<div class="rolling-container-block">
												<div class="detail-eval-item ">
													<span class="detail-eval-label">{title} <tpl if="sectionDescription"><i class="fa fa-question-circle" data-qtip="{sectionDescription}" data-qtitle="{name}" data-qalignTarget="bl-tl" data-qclosable="true" ></i></tpl></span>
													<span class="detail-eval-score" data-qtip="{average}">{display}</span>
												</div>	
											</div>
										</tpl>
									</div>
								</div>
							</section>
						</div>	
					</tpl>
					<tpl if="checkListAll.evaluationChecklist.summary">
						<div>
							<h3 class="quickView toggle-collapse">Evaluation Checklist Summary <div style="float: right;" data-ref="toolEl" class=" x-tool-tool-el x-tool-img x-tool-expand-bottom eval-toggle-caret" role="presentation"></div></h3>
							<section class="eval-visible-false">
								{checkListAll.evaluationChecklist.summary}
							</section>	
						</div>
					</tpl>
					<tpl if="checkListAll.recommendations && checkListAll.recommendations.length &gt; 0">
						<div>
							<h3 class="quickView toggle-collapse">Evaluation Recommendations <div style="float: right;" data-ref="toolEl" class=" x-tool-tool-el x-tool-img x-tool-expand-bottom eval-toggle-caret" role="presentation"></div></h3>
							<section class="eval-visible-false">
								<table class="quickView-table" width="100%">
									<tr><th class="details-table">Type</th><th class="details-table">Recommendation</th><th class="details-table">Reason</th></tr>
									<tpl for="checkListAll.recommendations">
										<tr class="details-table">
											<td class="details-table"><b>{recommendationTypeDescription}</b></td>
											<td class="details-table" style="text-align: center"><b>{recommendation}</b></td>
											<td class="details-table">{reason}</td>
										</tr>
									</tpl>
								</table>
							</section>
						</div>
					</tpl>
					<tpl if="contentSections && contentSections.length &gt; 0">
						<tpl for="contentSections">
							<div>
								<h3 class="quickView toggle-collapse">{section.title} <div style="float: right;" data-ref="toolEl" class=" x-tool-tool-el x-tool-img x-tool-expand-bottom eval-toggle-caret" role="presentation"></div></h3>
								<section class="eval-visible-false">
									<div>{section.content}</div>
									<tpl if="subsections.length &gt; 0">
										<tpl for="subsections">
											<div>
												<b>{title}</b>
											</div>
											{content}
										</tpl>
									</tpl>
								</section>
							</div>
						</tpl>
					</tpl>
					<tpl if="checkListAll.responses && checkListAll.responses.length &gt; 0">
						<div>
							<h3 class="quickView toggle-collapse">Evaluation Checklist Details <div style="float: right;" data-ref="toolEl" class=" x-tool-tool-el x-tool-img x-tool-expand-bottom eval-toggle-caret" role="presentation"></div></h3>
							<section class="eval-visible-false">
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
									</tpl>
								</table>
							</section>
						</div>
					</tpl>
				</section>
			</div>
		</div>
	</tpl>
</tpl>
