<#ftl strip_whitespace = true>

<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<style>
		body {
			font-family: Helvetica, Verdana, Arial, sans-serif; 
		}
		h1 { 
			background-color: #F1F1F1;
		} 
		table { 
			border: 1px black solid; 
			border-collapse: collapse;
			border-spacing: 0;
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
		tr:nth-child(odd) { 
			background-color: #eeeeee 
		} 
		tr:nth-child(even) {  
			background-color: white; 
		} 
		@media print {
			.pageBreak { 
				page-break-after: always; 
			}
		}
	</style>
</head>
<body>
	<div>
		<p>Component Details Report: ${reportDate}</p>
		<p>Entries (${reportSize})</p>
	</div>
	<hr />
	<#list components as component>
		<h1>${component.component.getName()}</h1>
		<p><b>${component.component.getOrganization()}</b></p>
		<br>
		<br>
		<div>
			<#if allowSecurityMargkingsFlg == true>
				${component.component.getSecurityMarkingType()}
			</#if>
			${component.component.getDescription()}

		</div>
		
		<!--Vitals-->
		<#if component.vitals?has_content>
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
			
		<!--Meta Data-->
		<#if component.metaData?has_content>
			<h2>MetaData</h2>
			<table>
				<tr>
					<th>Label</th>
					<th>Value</th>
				</tr>
				<#list component.metaData as metaData>
					<tr>
						<td><b>${metaData.label}</b></td>
						<td>${metaData.value}</td>
					</tr>
				</#list>
			</table>
		</#if>
			
		<!--Contacts-->
		<#if component.contacts?has_content>
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
						<td>${contacts.firstName}</td>
						<td>${contacts.lastName}</td>
						<td>${contacts.org}</td>
						<td>${contacts.email}</td>
						<td>${contacts.phone}</td>
					</tr>
				</#list>
			</table>
		</#if>
			
		<!--Resources-->
		<#if component.resources?has_content>
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
	</#list>
</body>
