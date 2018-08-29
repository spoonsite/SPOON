<#ftl strip_whitespace = true>

<!DOCTYPE html>
<html>
	<head>
		<title>Action Report - ${createTime?datetime}</title>
		<meta charset="UTF-8"></meta>
		<meta name="viewport" content="width=device-width, initial-scale=1.0"></meta>
		<style>
			body {
				font-family: Helvetica, Verdana, Arial, sans-serif; 
			}
			h1.section-header-first { 
				background-color: #F1F1F1;
				padding: 5px; 
			} 
			h1.section-header { 
				page-break-before: always;
				background-color: #F1F1F1;
				padding: 5px; 
			} 
			
			h2 {
				margin-bottom: 0;
				padding-bottom: 0;
			}
			.section-user-data {
				font-size: 0.75em;
				color: #848484;
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
			@media print {
				.pageBreak {
					page-break-after: always; 
				}
			}
			.rolling-container-block {    
				float: left;		
			}
			.section-indent {
				margin-left: 25px;
				border-left: solid #ccc 1px;
				padding-left: 10px;
			}
			.sub-section {
				border-bottom: solid #ccc 1px;
			}
			.review-score-section {
				font-size: 1.25em;
			}
			.section-indent-border {
				border-bottom: 1px solid #ccc;
				padding-bottom: 1em;
				margin-bottom: 2em;
			}
		</style>
	</head>
	<body>
		<div>		
			<p>Action Report: ${createTime?datetime}</p>
			<hr></hr>
			
			<#if !outstandingItems()>
				<h1>There are no administrative tasks that require attention.</h1>
			<#else>
				<!--Admin Entries-->
				<#if pendingAdminEntries?has_content>
					<h1 class="section-header-first">Pending Admin Entries</h1>
					<#list pendingAdminEntries as adminEntry>
						<div class="section-indent section-indent-border">
							<h2>${adminEntry.getName()}</h2>
							<div class="section-user-data">
								${adminEntry.getCreateUser()} - ${adminEntry.getCreateDts()?date}
							</div>
							<p>${adminEntry.getDescription()}</p>
						</div>
					</#list>
				</#if>

				<!--User Submissions-->
				<#if pendingUserEntries?has_content>
					<h1 class="section-header">Pending User Submissions</h1>
					<#list pendingUserEntries as userEntry>
						<div class="section-indent section-indent-border">
							<h2>${userEntry.getName()}</h2>
							<div class="section-user-data">
								${userEntry.getCreateUser()} - ${userEntry.getCreateDts()?date}
							</div>
							<p>${userEntry.getDescription()}</p>
						</div>
					</#list>
				</#if>

				<!--Change Requests-->
				<#if changeRequests?has_content>
					<h1 class="section-header">User Change Requests</h1>
					<#list changeRequests as request>
						<div class="section-indent section-indent-border">
							<h2>${request.getName()}</h2>
							<div class="section-user-data">
								${request.getCreateUser()} - ${request.getCreateDts()?date}
							</div>
							<p>${request.getDescription()}</p>
						</div>
					</#list>
				</#if>

				<!--Reviews-->
				<#if pendingReviews?has_content>
					<h1 class="section-header">Pending Reviews</h1>
					<#list pendingReviews as review>
						<div class="section-indent section-indent-border">
							<h2>${review.getTitle()}</h2>
							<div class="section-user-data">
								${review.username} - ${review.updateDate?date}
							</div>
							<div class="review-score-section">
								<p>Entry: ${review.name}</p>
								<p>Recommended: <b><#if review.recommend??>Yes<#else>No</#if></b></p>
								<p>Score: <#list 1..review.getRating() as ii> &#9733; </#list> (${review.getRating()})</p>
							</div>
							<p>${review.getComment()}</p>
						</div>
					</#list>
				</#if>

				<!--Questions-->
				<#if pendingQuestions?has_content>
					<h1 class="section-header">Pending Questions</h1>
					<#list pendingQuestions as question>
						<div class="section-indent section-indent-border">
							<div class="section-user-data">
								${question.username} - ${question.createDts?date}
							</div>
							<div class="review-score-section">
								<p>Entry: <b>${question.componentName}</b></p>
							</div>
							<p>${question.question}</p>
						</div>
					</#list>
				</#if>

				<!--Question Responses-->
				<#if pendingResponses?has_content>
					<h1 class="section-header">Pending Question Responses</h1>
					<#list pendingResponses as response>
						<div class="section-indent section-indent-border">
							<div class="section-user-data">
								<p><b>Entry:</b> ${response.componentName}</p>
								<p><b>In response to the question:</b> "${response.questionText}"</p>
							</div>
							<div class="section-indent">
								<h4 style="color: #848484;">${response.username} - ${response.updateDts?date}</h4>
								<p>${response.response}</p>
							</div>						
						</div>
					</#list>
				</#if>

				<!--Feedback-->
				<#if pendingFeedbackTickets?has_content>
					<h1 class="section-header">Outstanding Feedback</h1>
					<#list pendingFeedbackTickets as ticket>
						<div class="section-indent section-indent-border">
							<div class="section-user-data">
								<div>${ticket.getCreateUser()} - ${ticket.getCreateDts()?date}</div>
								<div><b>Organization</b>: ${(ticket.getOrganization())!"N/A"}</div>
								<div><b>Email</b>: ${ticket.getEmail()}</div>
								<div><b>Summary</b>: ${ticket.getSummary()}</div>
							</div>
							<p>${ticket.getDescription()}</p>
						</div>
					</#list>
				</#if>

				<!--Users-->
				<#if pendingUsers?has_content>
					<h1 class="section-header">Pending Users</h1>
					<#list pendingUsers as user>
						<div class="section-indent">
							<div><b>Username</b>: ${user.getUsername()}</div>
							<div><b>Date Created</b>: ${user.getCreateDts()?date}</div>
						</div>
						<br />
					</#list>
				</#if>

				<!--Evaluations-->
				<#if pendingEvaluations?has_content>
					<h1 class="section-header">Unpublished Evaluations</h1>
					<#list pendingEvaluations as eval>
						<div class="section-indent section-indent-border">
							<h4>Entry: ${eval.componentName}</h4>
							<div class="section-indent">
								<div><b>Version</b>: ${eval.version}</div>
								<div><b>Assigned User</b>: ${eval.assignedUser}</div>
								<div><b>Assigned Group</b>: ${eval.assignedGroup}</div>
								<div><b>Workflow Status</b>: ${eval.workflowStatusDescription}</div>
							</div>
							<br />							
						</div>
					</#list>
				</#if>
			</#if>
		</div>
	</body>
</html>
