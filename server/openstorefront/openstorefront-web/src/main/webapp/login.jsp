<%-- 
	See License-APL.txt
    Document   : login
    Created on : Apr 25, 2014, 3:18:20 PM
    Author     : dshurtleff
--%>

<%@page import="edu.usu.sdl.openstorefront.core.entity.SecurityPolicy"%>
<%@page import="edu.usu.sdl.openstorefront.core.entity.Branding"%>
<%@page import="edu.usu.sdl.openstorefront.service.ServiceProxy"%>
<%@page import="edu.usu.sdl.openstorefront.common.manager.PropertiesManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	Branding branding = ServiceProxy.getProxy().getBrandingService().getCurrentBrandingView();
	request.setAttribute("branding", branding);
	
	//Media needs to blocked; Branding is open...restrict to branding media;
	String loginLogo = branding.getPrimaryLogoUrl().replace("Media.action", "Branding.action");
	request.setAttribute("loginLogo", loginLogo);
	
	SecurityPolicy securityPolicy = ServiceProxy.getProxy().getSecurityService().getSecurityPolicy();
	request.setAttribute("allowRegistration", securityPolicy.getAllowRegistration());
	
	String appVersion = PropertiesManager.getApplicationVersion();		
	request.setAttribute("appVersion", appVersion);
	
%>
<html>
	<head>
		<!-- ***USER-NOT-LOGIN*** -->
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/appicon.png" type="image/x-icon">
		
		<link href="webjars/extjs/6.2.0/build/classic/theme-triton/resources/theme-triton-all-debug.css" rel="stylesheet" type="text/css"/>
		<link href="webjars/extjs/6.2.0/build/packages/ux/classic/triton/resources/ux-all-debug.css" rel="stylesheet" type="text/css"/>
		<link href="webjars/extjs/6.2.0/build/packages/charts/classic/triton/resources/charts-all-debug.css" rel="stylesheet" type="text/css"/>
		<link href="webjars/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
		<script src="apidoc/script/jquery/jquery-1.11.1.min.js" type="text/javascript"></script>
		
		<script src="webjars/extjs/6.2.0/ext-bootstrap.js" type="text/javascript"></script>
		<script src="webjars/extjs/6.2.0/build/classic/theme-triton/theme-triton.js" type="text/javascript"></script>
		<script src="webjars/extjs/6.2.0/build/packages/ux/classic/ux-debug.js" type="text/javascript"></script>
		<script src="webjars/extjs/6.2.0/build/packages/charts/classic/charts-debug.js" type="text/javascript"></script>
		<title>${branding.getApplicationName()}</title>
		
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
				display: block !important;
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
				margin: 0px;
			}
			.hidden {
				display: none; 
				visibility: hidden;
			}
			
			.login-header {
			   width: 100%;
			   height: 56px;
			   background-color: ${branding.primaryColor};
			}
			
			.login-header h1 {
				margin: auto;
				text-align: center;
				color: ${branding.primaryTextColor};
				padding-top: 10px;
				font-size: 32px;
				font-style: normal;
				letter-spacing: 1px;
				font-weight: 400;
			}

			.sub-header {
				width: 100%;
				height: 59px;
				background-color: ${branding.quoteColor};
				border-top: solid 3px ${branding.accentColor};
			}
			
			.sub-header h2 {
				margin: auto;
				text-align: center;
				color: ${branding.primaryTextColor};
				padding-top: 15px;
				font-size: 18px;
				font-style: italic;
				letter-spacing: 1px;
				font-weight: 100;
			}	
			
			.logos {
			   margin-bottom: 20px;
			}
			
		</style>
		<link href="Branding.action?CSS&template=extTritonTheme.jsp&v=${appVersion}" rel="stylesheet" type="text/css"/>	
		<link href="Branding.action?CSS&template=apptemplate.jsp&v=${appVersion}" rel="stylesheet" type="text/css"/>
		<link href="Branding.action?Override&v=${appVersion}" rel="stylesheet" type="text/css"/>	
	</head>
	<body>
	<div id="browserWarning" class="browser-warning" >
		 <p>You are using an <strong>unsupported</strong> browser. The website may not work as intended.  Please switch to <strong>
		 <a class="browser-warning-link" href="http://www.mozilla.org/en-US/firefox/new/">Firefox</a></strong> or <strong>
		 <a class="browser-warning-link" href="https://www.google.com/intl/en-US/chrome/browser/">Chrome</a></strong>, or <strong>
		 <a class="browser-warning-link" href="http://browsehappy.com/">upgrade your browser</a></strong> to improve your experience
		 <i class="fa fa-window-close-o fa-2x icon" aria-hidden="true"></i></p>	
	</div>
 	<div class="login-header">	
    	<h1>${branding.landingPageTitle}</h1>        
    </div>
    
    <div class="sub-header">
      	<h2>${branding.landingPageBanner}</h2>
    </div>		
	<div class="auth-forms">
	  <div class="auth-content">
		<div class="row" style="padding-left: 20px;padding-right: 20px;">
		  <h2>Log In</h2>
		  <div class="logos">
			  ${branding.loginLogoBlock}
		  </div>		  
		  <form id="loginForm" action="Login.action?Login" method="POST">
			 <%=branding.getLoginWarning() %>				 
			<div style="width: 500px; margin: 0px auto;">
				<p id="serverError" class="clearError" >
					Unable to connect to server or server failure.  Refresh page and try again.
				</p>	
				
				
				
				<input type="hidden" id="gotoPageId" name="gotoPage"  />	
				Username <br>
				<input type="text" name="username" id="username" placeholder="Username" class="form-control" autofocus autocomplete="false" style="width: 200px;">
				<p id="usernameError" class="clearError" style="color: red; font-weight: bold"></p> 				
				<br>
				<br>
				Password <br>
				<input type="password" name="password" id="password" placeholder="Password" class="form-control" autocomplete="false" style="width: 200px;" onkeypress="if (event.keyCode === 13){ keyPressLogin(); } ">
				<p id="passwordError" class="clearError" style="color: red; font-weight: bold"></p>					
				<br>
				<br>
				<input type="button" value="Log in" style="width: 100px;" class="btn btn-primary" onclick="submitForm();" />									
				
				<br>
				<br>
				<br>
				<br>
				<span id="registration" class="hidden"><a href="registration.jsp">Sign up</a> |</span> <a href="resetPassword.jsp">Forgot Password</a> | <a href="forgotUser.jsp">Forgot Username</a> 
				
			</div>
		  </form>
		</div>
	  </div>
    	</div>	
		<script type="text/javascript">
			
			sessionStorage.clear();
			
			if (Ext.isIE10m) {
				Ext.get('browserWarning').setStyle({
					display: 'block'
				});

				Ext.get('browserWarning').on("click", function () {
					Ext.get('browserWarning').setStyle({
						display: 'none'
					});
				} );
			} 
		
		
			$(document).ready(function(){
				if (${allowRegistration}) {
					$('#registration').removeClass('hidden');
				}
			});
			
			var keyPressLogin = function() {
				clearTimeout($.data(this, 'logintimer'));
				var wait = setTimeout(submitForm, 500);
				$(this).data('logintimer', wait);
			};
			
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
				 	
					
				function getCookie(cname) {
					var name = cname + "=";
					var decodedCookie = decodeURIComponent(document.cookie);
					var ca = decodedCookie.split(';');
					for(var i = 0; i <ca.length; i++) {
						var c = ca[i];
						while (c.charAt(0) === ' ') {
							c = c.substring(1);
						}
						if (c.indexOf(name) === 0) {
							return c.substring(name.length, c.length);
						}
					}
					return "";
				}					
				
				var token = getCookie('X-Csrf-Token');
		
				$.ajax({
					 type: "POST",
					 url: 'Login.action?Login',
					 headers: [
						{
							'X-Csrf-Token':	token
						}
					 ],
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
						if (xhr.status === 403) {
							alert('403 - Already logged in on this browser. Logout and try again.');
						} else {
							$("#serverError").addClass("loginError");							
						}
					 }
				 });
			}		
						
		</script>		
	</body>
</html>




