<%-- 
	See License-APL.txt
    Document   : login
    Created on : Apr 25, 2014, 3:18:20 PM
    Author     : dshurtleff
--%>

<%@page import="edu.usu.sdl.openstorefront.core.entity.SecurityPolicy"%>
<%@page import="edu.usu.sdl.openstorefront.core.entity.Branding"%>
<%@page import="edu.usu.sdl.openstorefront.service.ServiceProxy"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	Branding branding = ServiceProxy.getProxy().getBrandingService().getCurrentBrandingView();
	request.setAttribute("branding", branding);
	
	SecurityPolicy securityPolicy = ServiceProxy.getProxy().getSecurityService().getSecurityPolicy();
	request.setAttribute("allowRegistration", securityPolicy.getAllowRegistration());
	
%>
<html>
	<head>
		
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/appicon.png" type="image/x-icon">
		<script src="apidoc/script/jquery/jquery-1.11.1.min.js" type="text/javascript"></script>
		
		<script type="text/javascript">
		   try {
			if (window.self !== window.top) {
				window.parent.location.href = "/${pageContext.request.contextPath}/login.jsp?gotoPage="+window.parent.location.pathname;
			}
		   } catch (e) {
			//top
		   } 
		</script>
		
		<style>
			.disclaimer{
				
			}
			.auth-content .disclaimer {
				margin: 20px;
				text-align: left;
				border: 1px solid rgb(169, 169, 169);
				border-image-source: initial;
				border-image-slice: initial;
				border-image-width: initial;
				border-image-outset: initial;
				border-image-repeat: initial;
				border-radius: 10px;
				padding: 10px;
				background: ${branding.primaryColor};
				color: white;
			}			
			.auth-forms {
				background: rgba(255,255,255,.7);
				width: 80%;
				border: 1px solid rgb(169, 169, 169);
				border-image-source: initial;
				border-image-slice: initial;
				border-image-width: initial;
				border-image-outset: initial;
				border-image-repeat: initial;
				border-radius: 10px;
				padding: 20px;
				margin: 20px auto 0px;
				text-align: center;
			}
			h1, .h1 {
				font-size: 36px;
			}
			h1, h2, h3 {
				margin-top: 20px;
				margin-bottom: 10px;
			}
			h1, h2, h3, h4, h5, h6, .h1, .h2, .h3, .h4, .h5, .h6 {
				font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
				font-weight: 500;
				line-height: 1.1;
				color: inherit;
			}			
			.auth-content {
				display: inline-block;
			}
			.auth-content h1 {
				color: white;
			}			
			.row {
				margin-left: -15px;
				margin-right: -15px;
			}
			.auth-content input {
				width: 360px;
				display: inline-block;
			}	
			.btn-primary {
				color: white;
				background-color: rgb(85, 85, 85);
				border-color: rgb(72, 72, 72);				
			}
			
			.btn {
				display: inline-block;
				margin-bottom: 0px;
				font-weight: normal;
				text-align: center;
				vertical-align: middle;
				cursor: pointer;
				background-image: none;
				border: 1px solid transparent;
				border-image-source: initial;
				border-image-slice: initial;
				border-image-width: initial;
				border-image-outset: initial;
				border-image-repeat: initial;
				white-space: nowrap;
				padding: 6px 12px;
				font-size: 14px;
				line-height: 1.428571429;
				border-radius: 4px;
				-webkit-user-select: none;
			}
			.form-control {
				display: block;
				width: 100%;
				height: 34px;
				padding: 6px 12px;
				font-size: 14px;
				line-height: 1.428571429;
				color: rgb(85, 85, 85);
				vertical-align: middle;
				background-color: white;
				background-image: none;
				border: 1px solid rgb(204, 204, 204);
				border-image-source: initial;
				border-image-slice: initial;
				border-image-width: initial;
				border-image-outset: initial;
				border-image-repeat: initial;
				border-radius: 4px;
				-webkit-box-shadow: rgba(0, 0, 0, 0.0745098) 0px 1px 1px inset;
				box-shadow: rgba(0, 0, 0, 0.0745098) 0px 1px 1px inset;
				transition: border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
				-webkit-transition: border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
			}
			.loginError {
				display: block;
				color: red;
				padding: 5px;
				width: 100%;
				text-align: center; 
				border: red 2px solid;
				font-size: 14px;
			}
			.clearError {
				display: none;
			}
			.showError {
				display: block;
			}
			.errorField {
				border: red 2px solid;
			}
			body{
				background-color: beige;
				background-image: url(images/grid.png);
				background-repeat: repeat;
			}
			.hidden {
				display: none; 
				visibility: hidden;
			}
		</style>
	</head>
	<body>

		
	<div class="auth-forms">
	  <div class="auth-content">
		<div class="row" style="padding-left: 20px;padding-right: 20px;">
		  <h2>Log In</h2>
		  <form id="loginForm" action="Login.action?Login" method="POST">
			 <%=branding.getLoginWarning() %>				 
			<div style="width: 500px; margin: 0px auto;">
				<p id="serverError" class="clearError" >
					Unable to connect to server.  Refresh page and try again.
				</p>	
			
				
				
				<input type="hidden" id="gotoPageId" name="gotoPage"  />	
				Username <br>
				<input type="text" name="username" id="username" placeholder="Username" class="form-control" autofocus autocomplete="false" style="width: 200px;">
				<p id="usernameError" class="clearError"></p> 				
				<br>
				<br>
				Password <br>
				<input type="password" name="password" id="password" placeholder="Password" class="form-control" autocomplete="false" style="width: 200px;" onkeypress="if (event.keyCode === 13){ submitForm(); } ">
				<p id="passwordError" class="clearError"></p>					
				<br>
				<br>
				<input type="button" value="Log in" style="width: 100px;" class="btn btn-primary" onclick="submitForm();" />									
				
				<br>
				<br>
				<br>
				<br>
				<span id="registration" class="hidden"><a href="registration.jsp">Sign up</a> |</span> <a href="resetPassword.jsp">Forgot Password</a> 
				
			</div>
		  </form>
		</div>
	  </div>
    	</div>	
		<script type="text/javascript">
			
			sessionStorage.clear();
			
			$(document).ready(function(){
				if (${allowRegistration}) {
					$('#registration').removeClass('hidden');
				}
			});
			
			var QueryString = function () {				
				  var query_string = {};
				  var query = window.location.search.substring(1);
				  var vars = query.split("&");
				  for (var i=0;i<vars.length;i++) {
					var pair = vars[i].split("=");
						// If first entry with this name
					if (typeof query_string[pair[0]] === "undefined") {
					  query_string[pair[0]] = pair[1];
						// If second entry with this name
					} else if (typeof query_string[pair[0]] === "string") {
					  var arr = [ query_string[pair[0]], pair[1] ];
					  query_string[pair[0]] = arr;
						// If third or later entry with this name
					} else {
					  query_string[pair[0]].push(pair[1]);
					}
				  } 
					return query_string;
				} ();
				if (QueryString.gotoPage != undefined)
				{
					document.getElementById('gotoPageId').value = QueryString.gotoPage;
				} else {
					document.getElementById('gotoPageId').value = "${REFERENCED_URL}";
				}
			
			function submitForm(){
				$("#username").removeClass("errorField");	
				$("#password").removeClass("errorField");
				$("#usernameError").removeClass("showError");
				$("#passwordError").removeClass("showError");
				$("#serverError").removeClass("loginError");
				$("#usernameError").addClass("clearError");
				$("#passwordError").addClass("clearError");				
				$("#serverError").addClass("clearError");
				 	
				 $.ajax({
					 type: "POST",
					 url: 'Login.action?Login',
					 data: {
						username: $('#username').val(),
						password: $('#password').val(),
						gotoPage: $('#gotoPageId').val()
					 },
					 success: function(data) {	
						 if (data.success === false) {
							if (data.errors.password){
								$("#password").addClass("errorField");
								$("#passwordError").removeClass("clearError");
								$("#passwordError").addClass("showError");
								$("#passwordError").html(data.errors.password);
							} if (data.errors.username){
								$("#username").addClass("errorField");
								$("#usernameError").removeClass("clearError");
								$("#usernameError").addClass("showError");
								$("#usernameError").html(data.errors.username);
							}							
						 } else {							
							if (window.location.href.indexOf("login.jsp") > -1) {
								window.location.href = data.message; 
							} else {					
								if (data.message && data.message !== '') {
									window.location.href = data.message;
								} else {
									window.location.reload();
								}
							}
						 }						 
					 },
					 error: function(xhr, status, errorThrown) {	
						$("#serverError").addClass("loginError");							
					 }
				 });
			}			
							
		</script>		
	</body>
</html>




