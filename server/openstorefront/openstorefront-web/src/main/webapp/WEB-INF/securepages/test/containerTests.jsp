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
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="../apidoc/css/apidoc.css" rel="stylesheet" type="text/css"/>
		<script src="../apidoc/script/jquery/jquery-3.2.1.min.js" type="text/javascript"></script>		
        <title>Container Test Page</title>
		<style>
			.test-guide{
				left: 0px;
				position: fixed;
				top: 0px;
				bottom: 0px;
				margin-top: 45px;
				padding-left: 10px;
				border-right: 1px solid #dadfe5;
				overflow: hidden;
				overflow-y: auto;
				width: 300px;				
				z-index: 25;	
				background-color: white;
			}

			.test-doc{
				background: white;
				margin-top: 25px;
				margin-left: 300px;
				min-width: 750px;		
			}

			.loading{
				text-align: center;
				font-size: 24px;
				padding: 25% 0px 0px 0px;
				background-color: #666666;
				color: whitesmoke;
				top: 0px;
				left: 0px;
				bottom: 0px;
				right: 0px;				
			}

			.loading-top{
				position: fixed;
				top: 0px;
				left: 0px;
				bottom: 0px;
				right: 0px;
				opacity: .7;
				text-align: center;
				font-size: 24px;
				padding: 25% 0px 0px 0px;
				z-index: 9999;
				background-color: black;
				color: white;
				animation-name: example;
				animation-duration: 1s;
				animation-iteration-count: infinite;
				animation-direction: alternate;
			}

			@keyframes example {
				from {color: white;}
				to {color: yellow;}
			}			
			.test-case .result,
			.test-suite-stat .result,
			.header .passed,
			.header .failed,
			.header .all
			{
				padding: 5px; 
				border-radius: 2px;
				color: white; 
				font-weight: bold; 
				font-size: 16px;
				display: inline-block;
				margin: 5px;
			}	
			.test-case .result.passed,
			.test-suite-stat .result.passed,
			.header .passed
			{
				background-color: green; 
			}
			.test-case .result.failed,
			.test-suite-stat .result.failed,
			.header .failed
			{
				background-color: red; 
			}
			.header .all
			{
				background-color: blue;
			}
			.test-case .output
			{
				font-size: 9px; 
				color: grey; 
				border: 1px solid grey;
				padding: 5px;
			}
			.test-case .heading .description
			{
				font-weight: bold;
			}
			.test-suite .test-list
			{
				border-width: 1px 0px;
				border-color: #000000;
				border-style: solid;
			}
			.test-suite-stat 
			{
				margin: 20px 0px 0px 0px;
			}
			#fullbody.passed .test-suite.failed,
			#fullbody.passed .test-case.failed
			{
				display: none;
			}
			#fullbody.failed .test-suite.passed,
			#fullbody.failed .test-case.passed
			{
				display: none;
			}
			#header-buttons
			{
				text-align: right;
			}
			#header-buttons span
			{
				cursor: pointer;
			}
		</style>
    </head>
    <body id="fullbody">
		<header class="header">
			<span class="api-title">Open Storefront Container Tests</span>
			<div id="header-buttons">
				<span class="all">ALL</span>
				<span class="passed">PASSED</span>
				<span class="failed">FAILED</span>
			</div>
		</header>

		<div id="nav" class="test-guide" >
			<ul>
				<li>
					<a onclick="runTest('');" style="cursor: pointer">Run All Tests</a>
					<hr>
				</li>				
				<c:forEach var="testSuite" items="${actionBean.testSuites}">
					<li>
						<a onclick="runTest('&suite=${testSuite.name}');" style="cursor: pointer">${testSuite.name}</a>
						<ul>	
							<c:forEach var="item" items="${testSuite.tests}">						
								<li>
									<a onclick="runTest('&suite=${testSuite.name}&test=${item.description}');" style="cursor: pointer">${item.description}</a>
								</li>	
							</c:forEach>					
						</ul>
					</li>
				</c:forEach>	
			</ul>	
		</div>
		<div id="outputId" class="test-doc">
		</div>		

		<script type="text/javascript">
			$("#header-buttons span").click(function ()
			{
				$("#fullbody").attr("class", $(this).attr("class"));
			});
			function runTest(query)
			{
				$('#fullbody').append("<div id='nav-loader' class='loading-top'>Running Test(s)...</div>");
				$('#outputId').load('ServiceTest.action?RunTest' + query.replace(/ /g, '%20'),
						function (responseText, status, xhr) {
							$('#nav-loader').remove();
							$("#fullbody").attr("class", "");
						}
				);
				$(document).ajaxError(function (event, request, settings) {
					$('#nav-loader').remove();
					$("#fullbody").attr("class", "");
				});
			}
		</script>

    </body>
</html>
