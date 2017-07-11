<#ftl strip_whitespace = true>

<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<style>
		body {
			font-family: Helvetica, Verdana, Arial, sans-serif; 
		}
		h1 { 
			background-color: #F1F1F1;
			padding: 5px; 
		} 
		tr:nth-child(odd) { 
			background-color: #eeeeee 
		} 
		tr:nth-child(even) {  
			background-color: white; 
		} 
		@media print {
			.pageBreak { 
				background-color: #F1F1F1;
			}
		} 
		table { 
			border: 1px black solid; 
			border-collapse: collapse;
			border-spacing: 0;
			width: 100%;
		} 
		table td,th { 
			padding-left: 5px; 
			padding-right: 5px; 
		} 
		th { 
			color: white; 
			background-color: #414e68; 
			border: 1px lightgray solid; 
		} 
		td { 
			border: 1px lightgray solid;
			padding: 5px;
		}
		tr:nthchild(odd) {
			backgroundcolor: #eeeeee;
		}
		tr:nthchild(even) {
			backgroundcolor: white;
		}
		.eval-version {
			color: #848484;
			font-size: 0.75em;
		}
		.eval-header {
			margin-bottom: 0px;
		}
		.evaluation-section {
			margin-left: 3%;
			width: 97%;
		}
		@media print {
			.pageBreak {
				page-break-after: always; 
			}
		}
		.rolling-container {
			width: 100%;        
		}
		.detail-eval-item {    
			padding: 5px;
			border: 1px solid grey;
			min-width: 311px;
			max-width: 100%;
			width: 16.7%;
			border-collapse: collapse;
			display: inline-block;
			width: 32%;
			height: 2em;
		}
		.detail-eval-item:hover  { 
			background-color: #f5f5f5;
		}
		.rolling-container-block {    
			float: left;		
		}
		.score-circle {
			display: inline-block;
			color: black;
			font-style: normal;
			letter-spacing: -5px;
		}
		.score-average {
			clear: both;
			float: right;
			margin-right: 11px;
			font-size: 0.75em;
		}
		.detail-eval-score {
			float: right;
			margin-right: 15px;
		}
		.component-tag {
			font-weight: bold;
			background-color: #5d5d5d;
			border-radius: 100%;
			padding: 5px;
			font-style: normal;
			color: #FFF;
		}
		.section-indent {
			margin-left: 25px;
		}
		.qa-metadata {
			margin-top: 0px;
			padding-top: 0px;
			font-size: 0.75em;
			color: #9a9a9a;
		}
		.qa-header {
			display: inline;
	</style>
</head>
<body>
	<div>
		<p>Component Details Report: ${reportDate}</p>
		<p>Entries (${reportSize})</p>
	</div>
	<hr />
	<#list components as component>
	
		<!--Organization Description-->
		<#if reportOptions.getDisplayOrgData() == true>
			<h1>${component.component.getName()}</h1>
			<p><b>${component.component.getOrganization()}</b></p>
			<br>
			<br>
		</#if>
		<div>
			<#if allowSecurityMargkingsFlg == true>
				${component.component.getSecurityMarkingType()}
			</#if>
		</div>
			
		<#if component.tags?has_content && reportOptions.getDisplayTags() == true>
			<div>
				<b>Tags: </b>
				<#list component.tags as tag>
					<i class="component-tag">${tag.text}</i>
				</#list>
			</div>
		</#if>
		
		<!--Description-->
		<#if reportOptions.getDisplayDescription()>
			<div>
				${component.component.getDescription()}
			</div>
		</#if>
		
		<!--Vitals-->
		<#if component.vitals?has_content && reportOptions.getDisplayVitals() == true>
			<h2>Vitals</h2>
			<table>
				<tr>
					<th>Vital</th>
					<th>Value</th>
				</tr>
				<#list component.vitals as vitals>
					<tr>
						<td>
							<b>${vitals.typeLabel}</b>
						</td>
						<td>
							${vitals.attributeLabel}
						</td>
					</tr>
				</#list>
			</table>
		</#if>
			
		<!--Contacts-->
		<#if component.contacts?has_content && reportOptions.getDisplayContacts() == true>
			<h2>Contacts</h2>
			<table>
				<tr>
					<th>Type</th>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Organization</th>
					<th>Email</th>
					<th>Phone</th>
				</tr>
				<#list component.contacts as contacts>
					<tr>
						<td><b>${contacts.type}</b></td>
						<td><#if contacts.firstName?has_content>${contacts.firstName}</#if></td>
						<td><#if contacts.lastName?has_content>${contacts.lastName}</#if></td>
						<td><#if contacts.org?has_content>${contacts.org}</#if></td>
						<td><#if contacts.email?has_content>${contacts.email}</#if></td>
						<td><#if contacts.phone?has_content>${contacts.phone}</#if></td>
					</tr>
				</#list>
			</table>
		</#if>
			
		<!--Resources-->
		<#if component.resources?has_content && reportOptions.getDisplayResources() == true>
			<h2>Resources</h2>
			<table>
				<tr>
					<th>Type</th>
					<th>Description</th>
					<th>Link</th>
					<th>Restricted (requires login/CAC)</th>
				</tr>
				<#list component.resources as resource>
					<tr>
						<td><b>${resource.type}</b></td>
						<td>${resource.description}</td>
						<td>${resource.link}</td>
						<td>${resource.restricted}</td>
					</tr>
				</#list>
			</table>
		</#if>
			
		<!--Dependencies-->
		<#if component.dependencies?has_content && reportOptions.getDisplayDependencies() == true>
			<h2>Dependencies</h2>
			<table>
				<#list component.dependencies as dependent>
					<tr>
						<td>
							<div><b>${dependent.name}</b> - ${dependent.version}</div>
							<#if dependent.link?has_content><div>${dependent.link}</div></#if>
							<div>${dependent.comment}</div>
						</td>
					</tr>
				</#list>
			</table>
		</#if>
			
		<!--Relationships-->
		<#if component.relationships?has_content && reportOptions.getDisplayRelationships() == true>
			<h2>Relationships</h2>
			<table>
				<tr>
					<th>Entry</th>
					<th>Relationship Type</th>
					<th>Related Entry</th>
				</tr>
				
				<#list component.relationships as relationship>
					<tr>
						<td>${relationship.componentName}</td>
						<td><b>${relationship.type}</b></td>
						<td>${relationship.targetName}</td>
					</tr>
				</#list>
			</table>
		</#if>
			
		<!--Reviews-->
		<#if component.reviews?has_content && reportOptions.getDisplayReportReviews() == true>
			<h2>Reviews</h2>
			<table>
				<tr>
					<!--<th>Title</th>-->
					<th>User</th>
					<th>Rating</th>
					<th>Last Used</th>
					<th>Recommended</th>
					<th>Pros</th>
					<th>Cons</th>
					<th>Comment</th>
				</tr>
				<#list component.reviews as review>
					<tr>
						<td>${review.username}</td>
						<td>${review.rating}/5</td>
						<td>${review.lastUsed?string('MM/yyyy')}</td>
						<td>${review.recommended?c}</td>
						
						<!--Pros-->
						<td>
							<#if review.pros?has_content>
								<#list review.pros as pro>
									<p>- ${pro.pro}</p>
								</#list>
							</#if>
						</td>
						
						<!--Cons-->
						<td>
							<#if review.cons?has_content>
								<#list review.cons as con>
									<p>- ${con.con}</p>
								</#list>
							</#if>
						</td>
						
						<td>${review.comment}</td>
					</tr>
				</#list>
			</table>
		</#if>
			
		<!--Q/A-->
		<#if component.QA?has_content && reportOptions.getDisplayQA() == true>
			<h2>Questions & Answers</h2>
			
			<!--questions-->
			<#list component.QA as qa>
				<div>
					<div>
						<h3 class="qa-header">Q. </h3>${qa.question}
					</div>
					<div class="qa-metadata">
						${qa.username} - ${qa.date?string('MM/DD/yyyy')}
					</div>
					
					<!--responses-->
					<#if qa.responses?has_content>
						<#list qa.responses as response>
							<div class="section-indent">
								<div>
									<h3 class="qa-header">A. </h3>${response.response}
								</div>
								<div class="qa-metadata">
									${response.username} - ${response.date?string('MM/dd/yyyy')}
								</div>
							</div>
						</#list>
					</#if>
				</div>
			</#list>
		</#if>
			
		<!--Evaluation-->
		<#if component.evaluations?has_content && (reportOptions.getDisplayEvalSummary() == true || reportOptions.getDisplayEvalDetails() == true)>
			<#assign flag = true>
			<#list component.evaluations as eval>
				<#if flag == true>
			
					<h2 class="eval-header">Evaluation</h2>
					<#if eval.version?has_content>
						<div class="eval-version">Version - ${eval.version}</div>
					</#if>
						
					<!--Evaluation Summary-->
					<#if reportOptions.getDisplayEvalSummary() == true || reportOptions.getDisplayEvalDetails() == true>
						<div class="evaluation-section">
							
							<!--Reusability Factors-->
							<#if eval.scores?has_content>
								<h3 class="eval-header">Reusability Factors (5 = best)</h3>
								<#assign scoreColumns = (eval.scores?size/10.0)?ceiling>
								<#list eval.scores as scoreItem>
									<div class="detail-eval-item">
										<span class="detail-eval-label">${scoreItem.factor} </span>
										<span class="detail-eval-score" data-qtip="${scoreItem.averageScore}">
											<#if scoreItem.score?is_number>
												<#list 2..scoreItem.score?number + 1 as ii>
													<i class="score-circle">&#1010${ii};</i>
												</#list>
											<#else>
												<b>N/A</b>
											</#if>
										</span>
										<div class="score-average">Average: ${scoreItem.averageScore}</div>
									</div>
								</#list>
							</#if>
							
							<!--Checklist summary-->
							<#if eval.checklistSummary?has_content>
								<h3 class="eval-header">Checklist Summary</h3>
								${eval.checklistSummary}
							</#if>

							<!--Checklist recommendations-->
							<#if eval.recommendations?has_content>
								<h3 class="eval-header">Evaluation Recommendations</h3>
								<table>
									<tr>
										<th>Type</th>
										<th>Recommendation</th>
										<th>Reason</th>
									</tr>
									<#list eval.recommendations as rec>
										<tr>
											<td style="width: 15%;">${rec.typeDescription}</td>
											<td style="width: 39%;">${rec.recommendation}</td>
											<td style="width: 45%;">${rec.reason}</td>
										</tr>
									</#list>
								</table>
							</#if>
						</div>
					</#if>
								
					<!--Evaluation Details-->
					<#if reportOptions.getDisplayEvalDetails() == true>
						<div class="evaluation-section">
							<!--Evaluation Sections-->
							<#if eval.evaluationSections?has_content>
								<#list eval.evaluationSections as section>
									<#if section.isPrivate == false>
										<h3 class="eval-header">${section.title}</h3>
										<#if section.hideContent == false>
											${section.content}
										</#if>

										<!--Sub Sections-->
										<#if section.subSections?has_content>
											<#list section.subSections as subSection>
												<#if subSection.isPrivate == false>
													<div class="evaluation-section">
														<#if subSection.hideTitle == false>
															<h3 class="eval-header">${subSection.title}</h3>
														</#if>
														<#if subSection.hideContent == false>
															${subSection.content}
														</#if>
													</div>
												</#if>
											</#list>
										</#if>
									</#if>
								</#list>
							</#if>
							
							<!--Checklist Details-->
							<#if eval.checklistDetails?has_content>
								<h3 class="eval-header">Evaluation Checklist Details</h3>
								<table>
									<tr>
										<th style="width: 2.5%;">QID</th>
										<th style="width: 5%;">Section</th>
										<th style="width: 45%;">Question</th>
										<th style="width: 2.5%;">Score</th>
										<th style="width: 45%;">Response</th>
									</tr>
									<#list eval.checklistDetails as detail>
										<tr>
											<td><#if detail.qId?has_content>${detail.qId}</#if></td>
											<td><#if detail.section?has_content>${detail.section}</#if></td>
											<td><#if detail.question?has_content>${detail.question}</#if></td>
											<td><#if detail.score?has_content>${detail.score}</#if></td>
											<td><#if detail.response?has_content>${detail.response}</#if></td>
										</tr>
									</#list>
								</table>
							</#if>
						</div>
					</#if>

					<#if reportOptions.getDisplayEvalVersions() != true>
						<#assign flag = false>
					</#if>
				</#if>
			</#list>
		</#if>
	</#list>
</body>
