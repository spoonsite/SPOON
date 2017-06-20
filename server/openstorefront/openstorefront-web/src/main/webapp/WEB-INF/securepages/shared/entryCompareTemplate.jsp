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
		<tpl if="badgeUrl"><img src="{badgeUrl}" title="{codeDescription}" width="40" /></tpl>
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
	<b>Description</b>
	<hr>
	<section>
		<p>{description}</p>
	</section>
	<br>
	<br>
	<tpl if="evaluation.evaluationSections && evaluation.evaluationSections.length &gt; 0">
		<section>
			<h3 class="quickView">Evaluation Information</h3>
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
		</section>
	</tpl>		
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
	<tpl if="componentMedia && componentMedia.length &gt; 0">
		<section>
			<h3 class="quickView">Media</h3>
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
										</tpl>
										<tpl if="resources && resources.length &gt; 0">
											<section>
												<h3 class="quickView">Resources</h3>
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
										</tpl>	
										<tpl if="contacts && contacts.length &gt; 0">	
											<section>
												<h3 class="quickView">Contacts</h3>
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
										</tpl>
										<tpl if="dependencies && dependencies.length &gt; 0">
											<section>
												<h3 class="quickView">Dependencies</h3>
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
										</tpl>
										<tpl if="vitals && vitals.length &gt; 0">
											<section>
												<h3 class="quickView">Entry Vitals</h3>
												<table class="quickView-table" border="1">				
													<tpl for="vitals">
														<tr class="quickView-table">
															<td class="quickView-tableall" style="width: 30%;"><b>{label}</b></td>
															<td class="quickView-tableall">{value}</td>
														</tr>			
													</tpl>
												</table>
											</section>		
										</tpl>	
										<tpl if="relationships && relationships.length &gt; 0">
											<section>
												<h3 class="quickView">Relationships</h3>
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
										</tpl>
										<tpl if="reviews && reviews.length &gt; 0">
											<section>
												<h3 class="quickView">User Reviews</h3>
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
										</tpl>
										<tpl if="questions && questions.length &gt; 0">
											<section>
												<h3 class="quickView">User Questions</h3>
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
										</tpl>
									</tpl>

