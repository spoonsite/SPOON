<!DOCTYPE html>
<%--
Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.

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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<head>
		<title>Open Storefront API</title>
		<meta charset="UTF-8">		
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link href="apidoc/css/ui-lightness/jquery-ui-1.10.4.custom.min.css" rel="stylesheet" type="text/css"/>
		<link href="apidoc/css/apidoc.css" rel="stylesheet" type="text/css"/>
		<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
		<script src="apidoc/script/jquery/jquery-1.11.1.min.js" type="text/javascript"></script>
		<script src="apidoc/script/jquery/jquery-ui-1.10.4.custom.min.js" type="text/javascript"></script>
	</head>
	<body>
		<header class="header">
			<span class="api-title">Open Storefront API <span id="appverison"></span></span>
			<span style="position: absolute; top: 10px; right: 0px; padding-right: 20px;"><a href="API.action?PrintView" style="color: yellow; ">Print View</a></span>
		</header>
		
		<div class="api-guide">
			<h4>API</h4>
			<ul>
				<li>
					<a href="#API.action?Page&page=intro" >Introduction</a>
				</li>
				<li>
					<a href="#API.action?Page&page=security" >Security</a>
				</li>
				<li>
					<a href="#API.action?Page&page=errorhandling" >Error Handling</a>
				</li>
				<li>
					<a href="#API.action?Page&page=lookup" >Lookup Table</a>
				</li>
				<li>
					<a href="#API.action?Page&page=attribute" >Attribute Table</a>
				</li>
			</ul>
			
			<h4 title="(/api/v1/resource/) - Resources are entities that the server manages.  API provides CRUD support, querying and some resource specific behavior.">
				Resources
			</h4>
			<ul>
				<c:forEach var="item" items="${actionBean.resourceClasses}">
					<li>
						<a href="#API.action?API&resourceClass=${item.code}">${item.description}</a>
					</li>					
				</c:forEach>
			</ul>
			
			<h4 title="Services are actions related that act across resources or apply behaviour to the system.">
				Services
			</h4>
			<ul>
				<c:forEach var="item" items="${actionBean.serviceClasses}">
					<li>
						<a href="#API.action?API&resourceClass=${item.code}&classPath=service">${item.description}</a>
					</li>					
				</c:forEach>				
			</ul>				
		</div>
		<div id="apidocId" class="api-doc">
		</div>
		
		<script type="text/javascript">
			$( document ).tooltip();
			$(document).ready(function(){
				var docToLoad = window.location.href.split("#");
				if (docToLoad[1] !== undefined && docToLoad[1] !== null){
					$('#apidocId').load(docToLoad[1]);
				} else {
					$('#apidocId').load('API.action?Page&page=intro');
				}
				
				window.onhashchange = function(){
					var docToLoad = window.location.href.split("#");
					if (docToLoad[1] !== undefined && docToLoad[1] !== null){
						$('#apidocId').load(docToLoad[1]);
					} else {
						$('#apidocId').load('API.action?Page&page=intro');
					}					
				};
				
				$('#appverison').load('System.action?AppVersion');
			});
			
		</script>
		
		<!--
		<footer class="footer">
			<center>Open Storefront API Version 1.0</center>
		</footer>
		-->
	</body>
</html>

