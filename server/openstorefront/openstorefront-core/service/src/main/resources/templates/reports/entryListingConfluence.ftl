<#ftl strip_whitespace = true>

<h1>${title}</h1>
<b>Entries: ${totalRecords}</b>
<br />
<br />

<#if recentlyUpdated.data?has_content>
<ac:structured-macro ac:name="ui-expand">
	<ac:parameter ac:name="title">${recentlyUpdated.title}</ac:parameter>
	<ac:parameter ac:name="expanded">true</ac:parameter>
	<ac:rich-text-body>
		<table>
			<thead>
				<tr>
					<th>Name</th>
					<th>Description</th>
					<th style="text-align: center;">Entry Type</th>
					<th style="text-align: center;">Last Updated</th>
					<th style="text-align: center;">Evaluation Status</th>
				</tr>	
			</thead>
			<tbody>
				<#list recentlyUpdated.data as listing>
					<tr>
						<td style="width: 200px;">
							<a href="${listing.viewLink}" target="_blank">${listing.name}</a>
						</td>	
						<td>
							${listing.shortDescription}
						</td>
						<td style="width: 150px; text-align: center;">
							${listing.entryType}
						</td>							
						<td style="width: 150px; text-align: center;">
							${listing.lastUpdatedDts?date}
						</td>
						<td style="width: 150px; text-align: center;">
							${listing.evaluationStatus}
						</td>
					</tr>		
				</#list>
			</tbody>	
		</table>		
	</ac:rich-text-body>
</ac:structured-macro>
</#if>

<#if recentlyEvaluated.data?has_content>
<ac:structured-macro ac:name="ui-expand">
	<ac:parameter ac:name="title">${recentlyEvaluated.title}</ac:parameter>
	<ac:parameter ac:name="expanded">true</ac:parameter>
	<ac:rich-text-body>
		<table>
			<thead>
				<tr>
					<th>Name</th>
					<th>Description</th>
					<th style="text-align: center;">Entry Type</th>
					<th style="text-align: center;">Evaluation Completed Date</th>
					<th style="text-align: center;">Evaluation Status</th>
				</tr>	
			</thead>
			<tbody>
				<#list recentlyEvaluated.data as listing>
					<tr>
						<td style="width: 200px;">
							<a href="${listing.viewLink}" target="_blank">${listing.name}</a>
						</td>	
						<td>
							${listing.shortDescription}
						</td>
						<td style="width: 150px; text-align: center;">
							${listing.entryType}
						</td>							
						<td style="width: 225px; text-align: center;">
							${listing.lastUpdatedDts?date}
						</td>
						<td style="width: 150px; text-align: center;">
							${listing.evaluationStatus}
						</td>
					</tr>		
				</#list>
			</tbody>	
		</table>		
	</ac:rich-text-body>
</ac:structured-macro>
</#if>

<#if data?has_content>
<h2>Full Index</h2>
	<ac:structured-macro ac:name="ui-expand">
		<ac:parameter ac:name="expanded">true</ac:parameter>
		<ac:rich-text-body>			
			<table>
				<thead>
					<tr>
						<th>Name</th>
						<th>Description</th>
						<th style="text-align: center;">Entry Type</th>
						<th style="text-align: center;">Last Updated</th>
						<th style="text-align: center;">Evaluation Status</th>
					</tr>	
				</thead>
				<tbody>
					<#list data as dataSet>
						<#list dataSet.data as listing>
							<tr>
								<td style="width: 200px;">
									<a href="${listing.viewLink}" target="_blank">${listing.name}</a>
								</td>	
								<td>
									${listing.shortDescription}
								</td>
								<td style="width: 150px; text-align: center;">
									${listing.entryType}
								</td>							
								<td style="width: 150px; text-align: center;">
									${listing.lastUpdatedDts?date}
								</td>
								<td style="width: 150px; text-align: center;">
									${listing.evaluationStatus}
								</td>
							</tr>		
						</#list>
					</#list>
				</tbody>	
			</table>		
		</ac:rich-text-body>
	</ac:structured-macro>
</#if>	

