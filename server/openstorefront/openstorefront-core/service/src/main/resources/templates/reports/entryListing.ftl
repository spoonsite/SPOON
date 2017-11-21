<#ftl strip_whitespace = true>

<!DOCTYPE html>
<html>
	<head>
		<title>${title}</title>
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
		<b>Entries: ${data?size}</b>
		
		<table>
			<thead>
				<tr>
					<th>Name</th>
					<th>Description</th>
					<th>Entry Type</th>
					<th>Last Updated</th>
					<th>Evaluation Status</th>
				</tr>	
			</thead>
			<tbody>
				<#list data as listing>
					<tr>
						<td class="name">
							<a href="${listing.viewLink}" target="blank">${listing.name}</a>
						</td>	
						<td>
							${listing.shortDescription}
						</td>
						<td style="width: 200px; text-align: center;">
							${listing.entryType}
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

	</body>
</html>
