<%-- 
    Document   : printapi
    Created on : Jan 5, 2015, 12:35:49 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="apidoc/css/apidoc.css" rel="stylesheet" type="text/css"/>
		<script src="apidoc/script/jquery/jquery-1.11.1.min.js" type="text/javascript"></script>
		<link href="apidoc/css/apiprint.css" rel="stylesheet" type="text/css"/>
		 <script src="apidoc/script/reference.js" type="text/javascript"></script>
        <title>Open Storefront API - ${actionBean.applicationVersion}</title>
    </head>
    <body>
		<div style="height: 200px"></div>
		
		<h1>Open Storefront API </h1>		
		<span style="text-align: center; width: 100%; margin: 0px; float: left;">Version: ${actionBean.applicationVersion}</span>		
		
		<div class="spacer  pageBreak">
		</div>
		
		<div id="intro"></div>
		
		<div class="spacer  pageBreak">
		</div>

		<div id="security"></div>		
		
		<div class="spacer  pageBreak">
		</div>
		
		<div id="errorhandling"></div>		
		
		<div class="spacer  pageBreak">
		</div>

		<h1>API</h1>
		<hr>

		<c:forEach var="resourceModel" items="${actionBean.allResources}">
			<br><br><br>
 <h1>${resourceModel.resourceName}</h1>
	      ${resourceModel.resourceDescription}<br>
	<c:if test="${resourceModel.requireAdmin}">
		<br>
		<span class="resource-admin">Requires Admin Privilege</span>	  
	</c:if>	  
	
	<h3>API Path: ${resourceModel.resourcePath}</h3>
	<c:if test="${!empty resourceModel.methods}">
		<h2>Paths</h2>
		<ul>
		<c:forEach var="item" items="${resourceModel.methods}">
			<li><span class="${item.restMethod}" style="line-height: 28px;">${item.restMethod}</span> - ${resourceModel.resourcePath}${item.methodPath}</li>
		</c:forEach>
		</ul>	
	</c:if>
	
	
	<c:if test="${!empty resourceModel.resourceParams}">
		<h3>Resource Parameters: </h3>	  
		<table>
			<tr>
				<th>Parameter</th>
				<th>Description</th>
				<th>Required</th>
				<th>Default Value</th>
				<th>Restrictions</th>
				<th>Parameter Type</th>
			</tr>		
			<c:forEach var="item" items="${resourceModel.resourceParams}">
			<tr>
				<td>${item.parameterName}</td>
				<td>${item.parameterDescription}</td>
				<td>${item.required}</td>
				<td>${item.defaultValue}</td>
				<td>${item.restrictions}</td>
				<td>${item.parameterType}</td>				
			</tr>
			</c:forEach>
		</table> 
		
	</c:if>
	<c:if test="${!empty resourceModel.methods}">
		<h2>Details</h2>
		<table width="100%">
			<tr>
				<th style='text-align: center;'>Method</th>
				<th style='text-align: center;'>Requires Admin</th>
				<th style='text-align: left;'>Description</th>
				<th style='text-align: left;'>Path</th>
				<th style='text-align: left;'>Parameters</th>				
				<th style='text-align: left;'>Produces/Consumes Type(s)</th>
			</tr>		
			<c:forEach var="item" items="${resourceModel.methods}">
			<tr style="background-color: white;">
				<td align="center"><span class="${item.restMethod}">${item.restMethod}</span></td>
				<td align="center">${item.requireAdmin}</td>
				<td>${item.description}</td>
				<td id="${item.id}TD"><span class="resourcePath">${resourceModel.resourcePath}${item.methodPath}</span></td>
				<td>
					<c:if test="${!empty item.methodParams}">
					<table width="100%">
						<tr>
							<th>Parameter</th>
							<th>Description</th>
							<th>Required</th>
							<th>Defaults</th>
							<th>Restrictions</th>
							<th>Parameter Type</th>
						</tr>		
						<c:forEach var="methodParam" items="${item.methodParams}">
						<tr>
							<td>${methodParam.parameterName}</td>
							<td>${methodParam.parameterDescription}</td>
							<td>${methodParam.required}</td>
							<td>${methodParam.defaultValue}</td>
							<td>${methodParam.restrictions}</td>
							<td>${methodParam.parameterType}</td>				
						</tr>
						</c:forEach>
					</table> 					
					</c:if>
				</td>
				<td>
					<c:if test="${item.producesTypes != null}">
						<b>Produces:</b><br> 					
						${item.producesTypes}<br>						
					</c:if>
					<c:if test="${item.consumesTypes != null}">
						<b>Consumes:</b><br> 					
						${item.consumesTypes}<br>						
					</c:if>
				</td>				
			</tr>
			<c:if test="${item.consumeObject != null}">
				<tr style="background-color: lightgrey;">
					<td colspan="6">
						<div class="returnInfo">
							<div id="ctitle-${item.id}" class="returnInfo-title">	
								<c:if test="${item.consumeObject.typeObject != null}">
									Consume Object: <span class="value-object-name">${item.consumeObject.valueObjectName} (${item.consumeObject.typeObjectName})</span>									
								</c:if>
								<c:if test="${item.consumeObject.typeObject == null}">
									Consume Object: <span class="value-object-name">${item.consumeObject.valueObjectName}</span>
								</c:if>
							</div>
							<div id="cinfo-${item.id}" class="returnInfo-contents-show">								
								<c:if test="${item.consumeObject.valueObject != null}">
									<h5>${item.consumeObject.valueDescription}</h5>
									<pre>
${item.consumeObject.valueObject}									
									</pre>								
									<table>
										<tr>
											<th style='text-align: left;'>Field Name</th>
											<th style='text-align: center;'>Required</th>
											<th style='text-align: left;'>Type</th>
											<th style='text-align: left;'>Validation</th>
											<th style='text-align: left;'>Description</th>
										</tr>
										<c:forEach var="field" items="${item.consumeObject.valueFields}">
										<tr>
											<td>
												${field.fieldName}
											</td>
											<td align="center">
												${field.required}
											</td>
											<td>
												${field.type}
											</td>
											<td>
												${field.validation}
											</td>
											<td>
												${field.description}
											</td>											
										</tr>
										</c:forEach>
									</table>								
								</c:if>
								<c:if test="${item.consumeObject.typeObject != null}">
									<h3>Data Type Details</h3>
									<h5>${item.consumeObject.typeDescription}</h5>
									<pre>
${item.consumeObject.typeObject}									
									</pre>								
									<table>
										<tr>
											<th style='text-align: left;'>Field Name</th>
											<th style='text-align: center;'>Required</th>
											<th style='text-align: left;'>Type</th>
											<th style='text-align: left;'>Validation</th>
											<th style='text-align: left;'>Description</th>
										</tr>
										<c:forEach var="field" items="${item.consumeObject.typeFields}">
										<tr>
											<td>
												${field.fieldName}
											</td>
											<td  align="center">
												${field.required}
											</td>
											<td>
												${field.type}
											</td>
											<td>
												${field.validation}
											</td>
											<td>
												${field.description}
											</td>											
										</tr>
										</c:forEach>
									</table>								
								</c:if>
								<c:if test="${!empty item.consumeObject.allComplexTypes}">
									<h3>Complex Type(s) Details</h3>
									<c:forEach var="complexType" items="${item.consumeObject.allComplexTypes}">
										<h4>${complexType.name}</h4>
										<h5>${complexType.description}</h5>
										<pre>
${complexType.object}									
										</pre>								
										<table>
											<tr>
												<th style='text-align: left;'>Field Name</th>
												<th style='text-align: center;'>Required</th>
												<th style='text-align: left;'>Type</th>
												<th style='text-align: left;'>Validation</th>
												<th style='text-align: left;'>Description</th>
											</tr>
											<c:forEach var="field" items="${complexType.fields}">
											<tr>
												<td>
													${field.fieldName}
												</td>
												<td  align="center">
													${field.required}
												</td>
												<td>
													${field.type}
												</td>
												<td>
													${field.validation}
												</td>
												<td>
													${field.description}
												</td>													
											</tr>
											</c:forEach>
										</table>
									</c:forEach>
								</c:if>									
							</div>
						</div>
							
					</td>					
				</tr>	
			</c:if>			
			<c:if test="${item.responseObject != null}">
				<tr style="background-color: lightgrey;">
					<td colspan="6">
						<div class="returnInfo">
							<div id="rtitle-${item.id}" class="returnInfo-title">	
								<c:if test="${item.responseObject.typeObject != null}">
									Response Object: <span class="value-object-name">${item.responseObject.valueObjectName} (${item.responseObject.typeObjectName})</span>
								</c:if>
								<c:if test="${item.responseObject.typeObject == null}">
									Response Object: <span class="value-object-name">${item.responseObject.valueObjectName}</span>
								</c:if>
							</div>
													
							<div id="rinfo-${item.id}" class="returnInfo-contents-show">								
								<c:if test="${item.responseObject.valueObject != null}">
									<h5>${item.responseObject.valueDescription}</h5>
									<pre>
${item.responseObject.valueObject}									
									</pre>								
									<table>
										<tr>
											<th style='text-align: left;'>Field Name</th>
											<th style='text-align: center;'>Required</th>
											<th style='text-align: left;'>Type</th>
											<th style='text-align: left;'>Validation</th>
											<th style='text-align: left;'>Description</th>
										</tr>
										<c:forEach var="field" items="${item.responseObject.valueFields}">
										<tr>
											<td>
												${field.fieldName}
											</td>
											<td align="center">
												${field.required}
											</td>
											<td>
												${field.type}
											</td>
											<td>
												${field.description}
											</td>											
										</tr>
										</c:forEach>
									</table>								
								</c:if>
								<c:if test="${item.responseObject.typeObject != null}">
									<h3>Data Type Details</h3>
									<h5>${item.responseObject.typeDescription}</h5>
									<pre>
${item.responseObject.typeObject}									
									</pre>								
									<table>
										<tr>
											<th style='text-align: left;'>Field Name</th>
											<th style='text-align: center;'>Required</th>
											<th style='text-align: left;'>Type</th>
											<th style='text-align: left;'>Validation</th>
											<th style='text-align: left;'>Description</th>
										</tr>
										<c:forEach var="field" items="${item.responseObject.typeFields}">
										<tr>
											<td>
												${field.fieldName}
											</td>
											<td  align="center">
												${field.required}
											</td>
											<td>
												${field.type}
											</td>
											<td>
												${field.validation}
											</td>	
											<td>
												${field.description}
											</td>											
										</tr>
										</c:forEach>
									</table>								
								</c:if>
								<c:if test="${!empty item.responseObject.allComplexTypes}">
									<h3>Complex Type(s) Details</h3>
									<c:forEach var="complexType" items="${item.responseObject.allComplexTypes}">
										<h4>${complexType.name}</h4>
										<h5>${complexType.description}</h5>
										<pre>
${complexType.object}									
										</pre>								
										<table>
											<tr>
												<th style='text-align: left;'>Field Name</th>
												<th style='text-align: center;'>Required</th>
												<th style='text-align: left;'>Type</th>
												<th style='text-align: left;'>Validation</th>
												<th style='text-align: left;'>Description</th>
											</tr>
											<c:forEach var="field" items="${complexType.fields}">
											<tr>
												<td>
													${field.fieldName}
												</td>
												<td  align="center">
													${field.required}
												</td>
												<td>
													${field.type}
												</td>
												<td>
													${field.validation}
												</td>
												<td>
													${field.description}
												</td>												
											</tr>
											</c:forEach>
										</table>
									</c:forEach>
								</c:if>									
							</div>
						</div>
							
					</td>					
				</tr>	
			</c:if>				
			</c:forEach>
		</table> 
		<div class="pageBreak">
		</div>	
	</c:if>		
</c:forEach>				
		
		<div class="spacer  pageBreak">
		</div>
		
		<h1>Appendix</h1>
		<hr>
		
		<h2>Lookup Table Reference</h2>
		<div id="lookup"></div>
		
		<div class="spacer"></div>
		
		<h2>Attribute Table Reference</h2>   
		<div id="attributes"></div>
		
	
		<script type="text/javascript">
			$(document).ready(function(){
				$('#intro').load('API.action?Page&page=intro');
				$('#security').load('API.action?Page&page=security');
				$('#errorhandling').load('API.action?Page&page=errorhandling');
				
				 doLookups('#lookup');				
				 doAttributes('#attributes');
				 
				 window.print();
			});			
		</script>		
		
    </body>
</html>		
