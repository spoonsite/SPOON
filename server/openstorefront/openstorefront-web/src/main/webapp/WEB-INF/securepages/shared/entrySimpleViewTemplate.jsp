
<style>
	.quickView {
		background-color: #555555;
		color: #fff;
		padding-left: 5px;
		border: 1px solid #ddd;
	}
	.quickView-tableheader {
		background-color: lightgrey;
	}
	.quickView-tableall {    
		border: 1px solid #ddd;
		text-align: left;
	}

	.quickView-table {
		border-collapse: collapse;
		width: 100%;
	}

	.quickView-table-padding {
		padding: 15px;
	}	
</style>

<tpl if="name">
	<h3 class="quickView">{name}</h3>
	<p><b>Organization:</b> {organization}</p>
	<tpl if="version">
		<p><b>Version:</b> {version}</p>
	</tpl>
	<tpl if="releaseDate">
		<p><b>Release Date:</b> {releaseDate}</p>
	</tpl>
	<p><b>Entry Type:</b> {componentTypeLabel}</p>
	<br>
	<b>Description</b>
	<hr>
	<p>{description}</p>
	<br>
	<br>
	<tpl if="attributes && attributes.length &gt; 0">
		<section>
			<h3 class="quickView">Attributes</h3>
			<table class="quickView-table" border="1">				
				<tpl for="attributes">
					<tr>
						<td class="quickView-tableall"><b>{typeDescription}</b></td>
						<td class="quickView-tableall">{codeDescription}</td>
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
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Organization</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">First Name</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Last Name</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Email</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Phone</th>
				</tr>
				<tpl for="contacts">
					<tr>
						<td class="quickView-tableall">{positionDescription}</td>
						<td class="quickView-tableall">{organization}</td>
						<td class="quickView-tableall">{firstName}</td>
						<td class="quickView-tableall">{lastName}</td>
						<td class="quickView-tableall">{email}</td>
						<td class="quickView-tableall">{phone}</td>
					</tr>			
				</tpl>
			</table>	
		</section>
	</tpl>
	<tpl if="dependencies && dependencies.length &gt; 0">
		<section>
			<h3 class="quickView">Dependencies</h3>
			<table class="quickView-table" border="1">	
				<tr>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Name</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Version</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Reference Link</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Comment</th>
				</tr>		
				<tpl for="dependencies">
					<tr>
						<td class="quickView-tableall">{dependencyName}</td>
						<td class="quickView-tableall">{version}</td>
						<td class="quickView-tableall">{dependancyReferenceLink}</td>
						<td class="quickView-tableall">{comment}</td>
					</tr>			
				</tpl>
			</table>	
		</section>		
	</tpl>
	<tpl if="relationships && relationships.length &gt; 0">
		<section>
			<h3 class="quickView">Relationships</h3>
			<table class="quickView-table" border="1">	
				<tr>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Type</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Related Entry</th>
				</tr>		
				<tpl for="relationships">
					<tr>
						<td class="quickView-tableall">{relationshipType}</td>
						<td class="quickView-tableall">{targetComponentName}</td>
					</tr>			
				</tpl>
			</table>	
		</section>	
	</tpl>
	<tpl if="componentMedia && componentMedia.length &gt; 0">
		<section>
			<h3 class="quickView">Media</h3>
			<table class="quickView-table" border="1">	
				<tr>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Type</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Mime Type</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Link</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Filename</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Caption</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Media</th>
				</tr>		
				<tpl for="componentMedia">
					<tr>
						<td class="quickView-tableall">{contentType}</td>
						<td class="quickView-tableall">{mimeType}</td>
						<td class="quickView-tableall">{originalLink}</td>
						<td class="quickView-tableall">{originalFileName}</td>
						<td class="quickView-tableall">{caption}</td>
						<td class="quickView-tableall">
							<tpl if='mediaTypeCode == "IMG"'>
								<img src="{link}" width="150" />
							</tpl>
							<tpl if='mediaTypeCode != "IMG"'>
								<a href='{link}'>Download</a>
							</tpl>
						</td>
					</tr>			
				</tpl>
			</table>	
		</section>		
	</tpl>
	<tpl if="resources && resources.length &gt; 0">
		<section>
			<h3 class="quickView">Resources</h3>
			<table class="quickView-table" border="1" >	
				<tr>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Type</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Mime Type</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Resource Link</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Link</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Filename</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Description</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Restricted</th>
				</tr>		
				<tpl for="resources">
					<tr>
						<td class="quickView-tableall">{resourceTypeDesc}</td>
						<td class="quickView-tableall">{mimeType}</td>
						<td class="quickView-tableall"><a href='{actualLink}' target="_blank">link</a></td>
						<td class="quickView-tableall">{originalLink}</td>
						<td class="quickView-tableall">{originalFileName}</td>
						<td class="quickView-tableall">{description}</td>
						<td class="quickView-tableall">{restricted}</td>
					</tr>			
				</tpl>
			</table>	
		</section>		
	</tpl>
	<!--
	<section>
		<h3 class="quickView">Integration</h3>
	</section>
	-->
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
						<td class="quickView-tableall">{actualScore}</td>
					</tr>
				</tpl>
			</table>		
		</section>
	</tpl>	
	<tpl if="metadata && metadata.length &gt; 0">
		<section>
			<h3 class="quickView">Metadata</h3>			
			<table class="quickView-table" border="1">								
				<tpl for="metadata">
					<tr>
						<td class="quickView-tableall"><b>{label}</b></td>
						<td class="quickView-tableall">{value}</td>
					</tr>			
				</tpl>
			</table>	
		</section>		
	</tpl>
	<tpl if="tags && tags.length &gt; 0">
		<section>
			<h3 class="quickView">Tags</h3>
			<ul border="1">				
				<tpl for="tags">
					<li>
						{text}
					</li>			
				</tpl>
			</ul>	
		</section>		
	</tpl>
	<tpl if="reviews && reviews.length &gt; 0">
		<section>
			<h3 class="quickView">User Reviews</h3>
			<table class="quickView-table" border="1" >	
				<tr>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">User Name</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Organization</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Review</th>
				</tr>		
				<tpl for="reviews">			
					<tr>
						<td class="quickView-tableall">{username}</td>
						<td class="quickView-tableall">{organization}</td>
						<td class="quickView-tableall">
							{title}<br>
							{rating}<br><br>
							{comment}<br>
							<br>
							PROs<br>
							<ul>
								<tpl for="pros">
									<li>{text}</li>
								</tpl>
							</ul><br>
							CONs<br>
							<ul>
								<tpl for="pros">
									<li>{text}</li>
								</tpl>
							</ul><br>

						</td>
					</tr>
				</tpl>
			</table>	
		</section>		
	</tpl>
	<tpl if="questions && questions.length &gt; 0">
		<section>
			<h3 class="quickView">User Questions</h3>
			<table class="quickView-table" border="1" >	
				<tr>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">User Name</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Organization</th>
					<th class="quickView-tableheader quickView-tableall quickView-table-padding">Question</th>
				</tr>		
				<tpl for="questions">			
					<tr>
						<td class="quickView-tableall">{username}</td>
						<td class="quickView-tableall">{organization}</td>
						<td class="quickView-tableall">
							{question}<br><br>
							<b>Responses</b>
							<ul>
								<tpl for="responses">
									<li>{response}</li>
								</tpl>	
							</ul>
						</td>
					</tr>
				</tpl>
			</table>	
		</section>
	</tpl>
</tpl>
