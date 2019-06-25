<#ftl strip_whitespace = true>

<!DOCTYPE html>
<html>
	<head>		
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
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
				background-color: #535456; 
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
			.name {
				font-size: 16px;
				padding: 20px;
			}
		</style>
	</head>
	<body>
		<h1>${title}</h1>
		<b>Entries: ${totalRecords}</b>
		<br>

		<#if recentlyUpdated.data?has_content>
			<h2>${recentlyUpdated.title}</h2>
			<table>
				<thead>
					<tr>
						<th>Name</th>
						<th>Description</th>
						<th>Entry Type</th>
						<th>Last Vendor Update / Update Approved On</th>
						<th>Evaluation Status</th>
					</tr>	
				</thead>
				<tbody>				
					<#list recentlyUpdated.data as listing>
						<tr>
							<td class="name">
								<a href="${listing.viewLink}" target="_blank">${listing.name}</a>
							</td>	
							<td>
								${listing.shortDescription}
							</td>
							<td style="text-align: center;">
								${listing.entryType}
							</td>							
							<td style="text-align: center;">
								<table style="border:0;margin:0;">
									<tr>
										<td> <#--Imported Entries lack a lastSubmitDts-->
											<#if listing.lastSubmitDts?has_content>
												${listing.lastSubmitDts?string('MMMM dd, yyyy hh\x3Amm\x3Ass a zzz')}
											<#else>Imported On</#if>
										</td>
									</tr>
									<tr><td>${listing.lastVendorUpdateApprovedDate?string('MMMM dd, yyyy hh\x3Amm\x3Ass a zzz')}</td><tr>
								</table>
							</td>
							<td style="text-align: center;">
								${listing.evaluationStatus}
							</td>
						</tr>		
					</#list>
				</tbody>	
			</table>
		</#if>
	
		<#if recentlyEvaluated.data?has_content>
			<h2>${recentlyEvaluated.title}</h2>
			<table>
				<thead>
					<tr>
						<th>Name</th>
						<th>Description</th>
						<th>Entry Type</th>
						<th>Last Vender Update Approved</th>
						<th>Last System Update</th>
						<th>Evaluation Status</th>
					</tr>	
				</thead>
				<tbody>
					<#list recentlyEvaluated.data as listing>
						<tr>
							<td class="name">
								<a href="${listing.viewLink}" target="_blank">${listing.name}</a>
							</td>	
							<td>
								${listing.shortDescription}
							</td>
							<td style="width: 200px; text-align: center;">
								${listing.entryType}
							</td>		
							<td style="width: 200px; text-align: center;">
								${listing.lastVendorUpdateApprovedDate?date}
							</td>					
							<td style="width: 200px; text-align: center;">
								${listing.lastUpdatedDts?date}
							</td>
							<td style="width: 200px; text-align: center;">
								${listing.evaluationStatus}
							</td>
						</tr>		
					</#list>
				</tbody>	
			</table>		
		</#if>

		<#if data?has_content>
			<h2>Full Index</h2>
			<#list data as dataSet>			
					<h3>${dataSet.title}</h3>
					<table>
						<thead>
							<tr>
								<th>Name</th>
								<th>Description</th>
								<th>Entry Type</th>
								<th>Last Vendor Update / Approved On</th>
								<th>Evaluation Status</th>
							</tr>	
						</thead>
						<tbody>
							<#list dataSet.data as listing>
								<tr>
									<td class="name">
										<a href="${listing.viewLink}" target="_blank">${listing.name}</a>
									</td>	
									<td>
										${listing.shortDescription}
									</td>
									<td style=" text-align: center;">
										${listing.entryType}
									</td>	
									<td style="width: 200px; text-align: center;">
										<table style="border:0;margin:0;">
											<tr>
												<td> <#--Imported Entries lack a lastSubmitDts-->
													<#if listing.lastSubmitDts?has_content>
														${listing.lastSubmitDts?string('MMMM dd, yyyy hh\x3Amm\x3Ass a zzz')}
													<#else>Imported On</#if>
												</td>
											</tr>
											<tr><td>${listing.lastVendorUpdateApprovedDate?string('MMMM dd, yyyy hh\x3Amm\x3Ass a zzz')}</td><tr>
										</table>
									</td>
									<td style=" text-align: center;">
										${listing.evaluationStatus}
									</td>
								</tr>		
							</#list>
						</tbody>	
					</table>
					<br>
			</#list>	
		</#if>		
	</body>
</html>
