<%-- 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.

    Document   : apidetailTemplate
    Created on : Feb 10, 2017, 12:02:13 PM
    Author     : dshurtleff
--%>

	<div id="api-method-{id}" class="api-method">
		<div class="api-method-header">
			&nbsp;&nbsp;<span class="{restMethod}">{restMethod}</span> - <b>{path}</b>
		</div>
		{description}<br><br>
		<tpl if="producesTypes">
			<b>Produces: </b> {producesTypes}<br>
		</tpl>
		<tpl if="consumesTypes">
			<b>Consumes: </b> {consumesTypes} <br>	
		</tpl>
		<tpl if="securityRestriction">
			<br>	
			<tpl if="securityRestriction.permissions.length &gt; 0">
				<b>Permissions:</b><br>
				<ul>
					<tpl for="securityRestriction.permissions">
						<li >{.}</li>
					</tpl>			
				</ul>				
			</tpl>
			<tpl if="securityRestriction.roles.length &gt; 0">
				<b>Roles:</b>
				<ul>							
					<tpl for="securityRestriction.roles">
						<li>
							{.}
						</li>
					</tpl>
				</ul>				
			</tpl>			
			<tpl if="securityRestriction.logicOperation && securityRestriction.permissions.length &gt; 1 || securityRestriction.roles.length &gt; 1">
				<b>Logic Operation:</b> {securityRestriction.logicOperation}<br>
			</tpl>
			<tpl if="securityRestriction.specialCheck">
				<b>Special Handling:</b> {securityRestriction.specialCheck}<br>
			</tpl>
		</tpl>	
		<tpl if="methodParams.length &gt; 0">
			<table width="100%" class="api-table">
				<tr>
					<th>Parameter</th>
					<th>Description</th>
					<th>Required</th>
					<th>Defaults</th>
					<th>Restrictions</th>
					<th>Parameter Type</th>
				</tr>		
				<tpl for="methodParams">
					<tr>
						<td>{parameterName}</td>
						<td>{parameterDescription}</td>
						<td align="center">{required}</td>
						<td align="center">{defaultValue}</td>
						<td>{restrictions}</td>
						<td align="center">{parameterType}</td>				
					</tr>
				</tpl>
			</table> 					
		</tpl>		
		<tpl if="responseObject || consumeObject">
			<h2>Details</h2>
					<tpl if="consumeObject">
						
						<div class="returnInfo">
							<div id="ctitle-{id}" class="returnInfo-title">	
								<tpl if="consumeObject.typeObject != null">
									Consume Object: <span class="value-object-name">{consumeObject.valueObjectName} ({consumeObject.typeObjectName})</span>									
								</tpl>
								<tpl if="consumeObject.typeObject == null">
									Consume Object: <span class="value-object-name">{consumeObject.valueObjectName}</span>
								</tpl>
							</div>
							<div id="cinfo-{id}" >								
								<tpl if="consumeObject.valueObject">	
									<h5>{consumeObject.valueDescription}</h5>
									<pre>
{consumeObject.valueObject}									
									</pre>								
									<table width="100%" class="api-table">
										<tr>
											<th style='text-align: left;'>Field Name</th>
											<th style='text-align: center;'>Required</th>
											<th style='text-align: left;'>Type</th>
											<th style='text-align: left;'>Validation</th>
											<th style='text-align: left;'>Description</th>
										</tr>
										<tpl for="consumeObject.valueFields">
										<tr>
											<td>
												{fieldName}
											</td>
											<td align="center">
												{required}
											</td>
											<td>
												{type}
											</td>
											<td>
												{validation}
											</td>
											<td>
												{description}
											</td>												
										</tr>
										</tpl>
									</table>								
								</tpl>
								<tpl if="consumeObject.typeObject">
									<h3>Data Type Details</h3>
									<h5>{consumeObject.typeDescription}</h5>
									<pre>
{consumeObject.typeObject}									
									</pre>								
									<table width="100%" class="api-table">
										<tr>
											<th style='text-align: left;'>Field Name</th>
											<th style='text-align: center;'>Required</th>
											<th style='text-align: left;'>Type</th>
											<th style='text-align: left;'>Validation</th>
											<th style='text-align: left;'>Description</th>
										</tr>
										<tpl for="consumeObject.typeFields">
										<tr>
											<td>
												{fieldName}
											</td>
											<td  align="center">
												{required}
											</td>
											<td>
												{type}
											</td>
											<td>
												{validation}
											</td>
											<td>
												{description}
											</td>												
										</tr>
										</tpl>
									</table>								
								</tpl>
								<tpl if="consumeObject.allComplexTypes.length &gt; 0">
									<h3>Complex Type(s) Details</h3>
									<tpl for="consumeObject.allComplexTypes">
										<h4>{name}</h4>
										<h5>{description}</h5>
										<pre>
{object}									
										</pre>								
										<table width="100%" class="api-table">
											<tr>
												<th style='text-align: left;'>Field Name</th>
												<th style='text-align: center;'>Required</th>
												<th style='text-align: left;'>Type</th>
												<th style='text-align: left;'>Validation</th>
												<th style='text-align: left;'>Description</th>
											</tr>
											<tpl for="fields">
											<tr>
												<td>
													{fieldName}
												</td>
												<td  align="center">
													{required}
												</td>
												<td>
													{type}
												</td>
												<td>
													{validation}
												</td>
												<td>
													{description}
												</td>												
											</tr>
											</tpl>
										</table>
									</tpl>
								</tpl>									
							</div>
						</div>
					</tpl>			
					<tpl if="responseObject">
						<div class="returnInfo">
							<div id="rtitle-{id}" class="returnInfo-title">	
								<tpl if="responseObject.typeObject != null">
									Response Object: <span class="value-object-name">{responseObject.valueObjectName} ({responseObject.typeObjectName})</span>
								</tpl>
								<tpl if="responseObject.typeObject == null">
									Response Object: <span class="value-object-name">{responseObject.valueObjectName}</span>
								</tpl>
							</div>

							<div id="rinfo-{id}">								
								<tpl if="responseObject.valueObject">
									<h5>{responseObject.valueDescription}</h5>
									<pre>
{responseObject.valueObject}									
									</pre>								
									<table width="100%" class="api-table">
										<tr>
											<th style='text-align: left;'>Field Name</th>
											<th style='text-align: center;'>Required</th>
											<th style='text-align: left;'>Type</th>
											<th style='text-align: left;'>Validation</th>
											<th style='text-align: left;'>Description</th>
										</tr>
										<tpl for="responseObject.valueFields">
										<tr>
											<td>
												{fieldName}
											</td>
											<td align="center">
												{required}
											</td>
											<td>
												{type}
											</td>
											<td>
												{validation}
											</td>
											<td>
												{description}
											</td>											
										</tr>
										</tpl>
									</table>								
								</tpl>
								<tpl if="responseObject.typeObject">
									<h3>Data Type Details</h3>
									<h5>{responseObject.typeDescription}</h5>
									<pre>
{responseObject.typeObject}									
									</pre>								
									<table width="100%" class="api-table">
										<tr>
											<th style='text-align: left;'>Field Name</th>
											<th style='text-align: center;'>Required</th>
											<th style='text-align: left;'>Type</th>
											<th style='text-align: left;'>Validation</th>
											<th style='text-align: left;'>Description</th>
										</tr>
										<tpl for="responseObject.typeFields">
										<tr>
											<td>
												{fieldName}
											</td>
											<td  align="center">
												{required}
											</td>
											<td>
												{type}
											</td>
											<td>
												{validation}
											</td>
											<td>
												{description}
											</td>											
										</tr>
										</tpl>
									</table>								
								</tpl>
								<tpl if="responseObject.allComplexTypes.length &gt; 0">
									<h3>Complex Type(s) Details</h3>
									<tpl for="responseObject.allComplexTypes">
										<h4>{name}</h4>
										<h5>{description}</h5>
										<pre>
{object}									
										</pre>								
										<table width="100%" class="api-table">
											<tr>
												<th style='text-align: left;'>Field Name</th>
												<th style='text-align: center;'>Required</th>
												<th style='text-align: left;'>Type</th>
												<th style='text-align: left;'>Validation</th>
												<th style='text-align: left;'>Description</th>
											</tr>
											<tpl for="fields">
											<tr>
												<td>
													{fieldName}
												</td>
												<td  align="center">
													{required}
												</td>
												<td>
													{type}
												</td>
												<td>
													{validation}
												</td>
												<td>
													{description}
												</td>												
											</tr>
											</tpl>
										</table>
									</tpl>
								</tpl>									
							</div>
						</div>

					</tpl>				
		</tpl>				
	</div>	
	<br><br>
