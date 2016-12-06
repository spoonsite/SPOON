<%-- 
/* 
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
 */
    Document   : entity
    Created on : Sep 14, 2015, 12:49:21 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="shortcut icon" href="/${pageContext.request.contextPath}/appicon.png" type="image/x-icon">
		<title>Openstorefront Entities</title>
		<style>
			body{
				padding: 20px;
				font-family: Helvetica, Verdana, Arial, sans-serif;	
				margin: 0px;
				background-color: white;	
			}

			@media print {
				.pageBreak {

					page-break-after: always;
				}
				.hidePrint{
					display: none;
				}	
			}			

			a {
				color: black
			}

			a:visited {
				color: black
			}

			a:hover {
				color: #ff4800;
			}

			h1{
				float: left;
				width: 100%;
				margin: 0px;	
				text-align: center;
				/* text-shadow: 2px 2px 2px grey; */
			}

			table{
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
				/* text-shadow: 2px 2px 2px black; */
				background-color: #2C3342;
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

		</style>
	</head>
	<body>
		<h1>Open Storefront Entities </h1>		
		<span style="text-align: center; width: 100%; margin: 0px; float: left;">Version: ${actionBean.applicationVersion}</span>	

		<h2><a name="toc">Entity List</a></h2>
		<hr>
		<table style="width: 100%">
			<tr>
				<th>
					Entity
				</th>
				<th>
					description
				</th>				
			</tr>
		<c:forEach var="docModel" items="${actionBean.entityDocModels}">
			<tr>
				<td style="width: 300px;">
					<a href="#${docModel.name}">${docModel.name}</a>
				</td>
				<td>
					<span style="color: gray;">${docModel.description}</span>
				</td>
			</tr>
			</c:forEach>			
		</table>
		
			

		<hr>
		<div class="pageBreak"></div>
		<br>
		<c:forEach var="docModel" items="${actionBean.entityDocModels}">
			<a name="${docModel.name}"></a>			
			<h2>${docModel.name}</h2>
			<i>${docModel.description}</i>
			<br>
			<c:if test="${!empty docModel.parentEntities}">
				<br>
				<b>Parent Entities:</b><br>			
				<c:forEach var="parentModel" items="${docModel.parentEntities}">
					<a href="#${parentModel.name}">${parentModel.name}</a><br>
				</c:forEach>
			</c:if>
			<c:if test="${!empty docModel.implementedEntities}">
				<br>
				<b>Associated Entities:</b><br>	
				<c:forEach var="impModel" items="${docModel.implementedEntities}">
					<a href="#${impModel.name}">${impModel.name}</a><br>
				</c:forEach>
			</c:if>	
			<table style="width: 100%">
				<thead>
					<th>Field</th>
					<th>Description</th>
					<th>Type</th>
					<th>Constraints</th>
				</thead>
				<tbody>
					<c:forEach var="fieldModel" items="${docModel.fieldModels}">
					<tr>
						<td>							
							<b>${fieldModel.name}</b>
							<c:if test="${fieldModel.primaryKey}">
								<span style="color: red; font-weight: bold">PK</span>
							</c:if>
							<br>	
							<span style="color: lightslategrey; font-size: 9px">${fieldModel.originClass}</span>
						</td>
						<td>	
							${fieldModel.description}
						</td>
						<td style="text-align: center">								
							<c:if test="${!fieldModel.embeddedType}">
								${fieldModel.type}
							</c:if>
							<c:if test="${fieldModel.embeddedType}">
								<a href="#${fieldModel.type}">${fieldModel.type}</a>
							</c:if>							
							
							<c:if test="${fieldModel.genericType != null}">
								( ${fieldModel.genericType} )
							</c:if>
						</td>						
						<td>
							<c:if test="${!empty fieldModel.constraints}">
								<table style="width: 100%">
									<tr>
										<th>
											Name
										</th>
										<th>
											Description
										</th>
										<th>
											Rules
										</th>
										<th>
											Related Classes
										</th>										
									</tr>							
									<c:forEach var="conModel" items="${fieldModel.constraints}">

										<tr>
											<td style="font-weight: bold">	
												${conModel.name}
											</td>
											<td>											
												${conModel.description}
											</td>
											<td>
												${conModel.rules}
											</td>
											<td style="text-align: center;">
												<c:forEach var="related" items="${conModel.relatedClasses}">
													<a href="#${related}">${related}</a><br>
												</c:forEach>
											</td>										
										</tr>

									</c:forEach>
								</table>
							</c:if>
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
			<br>
			<a href="#toc">Table of Contents</a>		
			<hr> 
			<div class="pageBreak"></div>
			
		</c:forEach>
		

	</body>
</html>
