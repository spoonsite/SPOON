<%-- 
/* 
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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

    Document   : systemStandby
    Created on : Apr 26, 2017, 9:42:50 AM
    Author     : dshurtleff
--%>

<%@page import="edu.usu.sdl.core.CoreSystem"%>
<%@page import="edu.usu.sdl.openstorefront.core.view.SystemStatusView"%>
<%@page import="edu.usu.sdl.openstorefront.common.manager.PropertiesManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=10, user-scalable=yes">
		
		<%
			String appVersion = PropertiesManager.getApplicationVersion();
			request.setAttribute("appVersion", appVersion);
			
			SystemStatusView systemStatusView = CoreSystem.getStatus();
			request.setAttribute("status", systemStatusView.getSystemStatus());
			request.setAttribute("detailedStatus", systemStatusView.getDetailedStatus());
		%>	
	
		
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/appicon.png" type="image/x-icon">	
		
        <title>Openstorefront - Standby</title>
		<style>
			body{
				background: whitesmoke;
				font-family: sans-serif;
			}
			
			.loader {
			  width: 150px;
			  height: 150px;
			  -webkit-perspective: 150px;
					  perspective: 150px;
			  position: absolute;
			  top: 50%;
			  left: 50%;
			  margin-top: -75px;
			  margin-left: -75px;
			}

			.loader__tile {
			  display: block;
			  float: left;
			  width: 33.33%;
			  height: 33.33%;
			  -webkit-animation-name: flip;
					  animation-name: flip;
			  -webkit-transform-style: preserve-3d;
					  transform-style: preserve-3d;
			  -webkit-animation-iteration-count: infinite;
					  animation-iteration-count: infinite;
			  -webkit-animation-duration: 4s;
					  animation-duration: 4s;
			  -webkit-animation-timing-function: ease-in-out;
					  animation-timing-function: ease-in-out;
			  -webkit-transform: rotateY(0deg);
					  transform: rotateY(0deg);
			  z-index: 0;
			}

			.loader__tile__1 {
			  background-color: #9aafd1;
			  -webkit-animation-delay: 0.3s;
					  animation-delay: 0.3s;
			}

			.loader__tile__2 {
			  background-color: #88a1c9;
			  -webkit-animation-delay: 0.6s;
					  animation-delay: 0.6s;
			}

			.loader__tile__3 {
			  background-color: #7e9ccc;
			  -webkit-animation-delay: 0.9s;
					  animation-delay: 0.9s;
			}

			.loader__tile__4 {
			  background-color: #6e91c9;
			  -webkit-animation-delay: 1.2s;
					  animation-delay: 1.2s;
			}

			.loader__tile__5 {
			  background-color: #ffc10a;
			  -webkit-animation-delay: 1.5s;
					  animation-delay: 1.5s;
			}

			.loader__tile__6 {
			  background-color: #6287c4;
			  -webkit-animation-delay: 1.8s;
					  animation-delay: 1.8s;
			}

			.loader__tile__7 {
			  background-color: #5377b2;
			  -webkit-animation-delay: 2.1s;
					  animation-delay: 2.1s;
			}

			.loader__tile__8 {
			  background-color: #3f629b;
			  -webkit-animation-delay: 2.4s;
					  animation-delay: 2.4s;
			}

			.loader__tile__9 {
			  background-color: #2e4b7a;
			  -webkit-animation-delay: 2.7s;
					  animation-delay: 2.7s;
			}

			@-webkit-keyframes flip {
			  0% {
				-webkit-transform: rotateY(0deg);
						transform: rotateY(0deg);
			  }
			  11% {
				-webkit-transform: rotateY(180deg);
						transform: rotateY(180deg);
			  }
			}

			@keyframes flip {
			  0% {
				-webkit-transform: rotateY(0deg);
						transform: rotateY(0deg);
			  }
			  11% {
				-webkit-transform: rotateY(180deg);
						transform: rotateY(180deg);
			  }
			}
			
			.status-text {
				position: absolute;
				left: 50%;
				top: 100px;				
				font-size: 24px;
				margin-left: -200px;
			}
			.version {
				position: absolute;
				bottom: 100px;
				width: 99%;
				text-align: center;
			}
		</style>		
		
    </head>
    <body>
		<div class="loader"><i class="loader__tile loader__tile__1"></i><i class="loader__tile loader__tile__2"></i><i class="loader__tile loader__tile__3"></i><i class="loader__tile loader__tile__4"></i><i class="loader__tile loader__tile__5"></i><i class="loader__tile loader__tile__6"></i><i class="loader__tile loader__tile__7"></i><i class="loader__tile loader__tile__8"></i><i class="loader__tile loader__tile__9"></i>						
		</div>
		<div class="status-text">
			Application is initializing please wait...<br><br><br>
			Current Status: <span id="status" style="font-weight: bold">${status}</span><br>
			<c:if test="detailedStatus">Details: ${detailedStatus}</c:if>
		</div>
		<div class="version">
			${appVersion}
		</div>
		
		<script type="text/javascript">
			//refresh page to see if it's ready
			setInterval(function(){
				window.location.reload();
			}, 6000);
		</script>		
    </body>
</html>
