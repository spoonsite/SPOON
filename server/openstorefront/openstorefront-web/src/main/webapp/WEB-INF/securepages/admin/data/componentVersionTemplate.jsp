
<style>
	h3 {
		background-color: #555555;
		color: #fff;	
	}
	table {
		width: 100%
	}
</style>

<h3>{name}</h3>
<p>{organization}</p>
<p>Version: {version}</p>
<p>Release Date: {releaseDate}</p>
<p>Type: {componentType}</p>
<br>
<b>Description </b>
<hr>
<p>{description}</p>
<br>
<br>
<section>
	<h3>Attributes</h3>
	<table border="1">				
		<tpl for="attributes">
			<tr>
				<td>{typeDescription}</td>
				<td>{codeDescription}</td>
			</tr>			
		</tpl>
	</table>
</section>
<section>
	<h3>Contacts</h3>
	<table border="1">	
		<tr>
			<th>Position</th>
			<th>Organization</th>
			<th>First Name</th>
			<th>Last Name</th>
			<th>Email</th>
			<th>Phone</th>
		</tr>
		<tpl for="contacts">
			<tr>
				<td>{positionDescription}</td>
				<td>{organization}</td>
				<td>{firstName}</td>
				<td>{lastName}</td>
				<td>{email}</td>
				<td>{phone}</td>
			</tr>			
		</tpl>
	</table>	
</section>
<section>
	<h3>Dependencies</h3>
	<table border="1">	
		<tr>
			<th>Name</th>
			<th>Version</th>
			<th>Reference Link</th>
			<th>Comment</th>
		</tr>		
		<tpl for="dependencies">
			<tr>
				<td>{dependencyName}</td>
				<td>{version}</td>
				<td>{dependancyReferenceLink}</td>
				<td>{comment}</td>
			</tr>			
		</tpl>
	</table>	
</section>
<section>
	<h3>Relationships</h3>
	<table border="1">	
		<tr>
			<th>Type</th>
			<th>Related Entry</th>
		</tr>		
		<tpl for="relationships">
			<tr>
				<td>{relationshipType}</td>
				<td>{targetComponentName}</td>
			</tr>			
		</tpl>
	</table>	
</section>
<section>
	<h3>Media</h3>
	<table border="1">	
		<tr>
			<th>Type</th>
			<th>Mime Type</th>
			<th>Link</th>
			<th>Filename</th>
			<th>Caption</th>
		</tr>		
		<tpl for="componentMedia">
			<tr>
				<td>{contentType}</td>
				<td>{mimeType}</td>
				<td>{originalLink}</td>
				<td>{originalFileName}</td>
				<td>{caption}</td>
			</tr>			
		</tpl>
	</table>	
</section>
<section>
	<h3>Resources</h3>
	<table border="1" >	
		<tr>
			<th>Type</th>
			<th>Mime Type</th>
			<th>Link</th>
			<th>Filename</th>
			<th>Description</th>
			<th>Restricted</th>
		</tr>		
		<tpl for="resources">
			<tr>
				<td>{resourceTypeDesc}</td>
				<td>{mimeType}</td>
				<td>{originalLink}</td>
				<td>{originalFileName}</td>
				<td>{description}</td>
				<td>{restricted}</td>
			</tr>			
		</tpl>
	</table>	
</section>
<!--
<section>
	<h3>Integration</h3>
</section>
-->
<section>
	<h3>Evaluation Information</h3>
	<table border="1" >	
		<tr>
			<th>Name</th>
			<th>Score</th>
		</tr>		
		<tpl for="evaluation.evaluationSections">			
			<tr>
				<td>{name}</td>
				<td>{actualScore}</td>
			</tr>
		</tpl>
	</table>		
</section>
<section>
	<h3>Metadata</h3>
	<table border="1">				
		<tpl for="metadata">
			<tr>
				<td>{label}</td>
				<td>{value}</td>
			</tr>			
		</tpl>
	</table>	
</section>
<section>
	<h3>Tags</h3>
	<table border="1">				
		<tpl for="tags">
			<tr>
				<td>{text}</td>			
			</tr>			
		</tpl>
	</table>	
</section>
<section>
	<h3>User Reviews</h3>
	<table border="1" >	
		<tr>
			<th>User Name</th>
			<th>Organization</th>
			<th>Review</th>
		</tr>		
		<tpl for="reviews">			
			<tr>
				<td>{username}</td>
				<td>{organization}</td>
				<td>
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
<section>
	<h3>User Questions</h3>
	<table border="1" >	
		<tr>
			<th>User Name</th>
			<th>Organization</th>
			<th>Question</th>
		</tr>		
		<tpl for="questions">			
			<tr>
				<td>{username}</td>
				<td>{organization}</td>
				<td>
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


