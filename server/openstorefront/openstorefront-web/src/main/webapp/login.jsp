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
	// control css classes that may be set on html tag: no-overview no-registration no-registration-video
%>
<html> 
	<head>
		<!-- ***USER-NOT-LOGIN*** -->
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/appicon.png" type="image/x-icon">

		<link href="webjars/extjs/6.2.0/build/classic/theme-triton/resources/theme-triton-all-debug.css" rel="stylesheet" type="text/css"/>
		<link href="webjars/extjs/6.2.0/build/packages/ux/classic/triton/resources/ux-all-debug.css" rel="stylesheet" type="text/css"/>
		<link href="webjars/extjs/6.2.0/build/packages/charts/classic/triton/resources/charts-all-debug.css" rel="stylesheet" type="text/css"/>
		<link href="webjars/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
		<script src="apidoc/script/jquery/jquery-3.2.1.min.js" type="text/javascript"></script>

		<script src="webjars/extjs/6.2.0/ext-bootstrap.js" type="text/javascript"></script>
		<script src="webjars/extjs/6.2.0/build/classic/theme-triton/theme-triton.js" type="text/javascript"></script>
		<script src="webjars/extjs/6.2.0/build/packages/ux/classic/ux-debug.js" type="text/javascript"></script>
		<script src="webjars/extjs/6.2.0/build/packages/charts/classic/charts-debug.js" type="text/javascript"></script>
		<title>${branding.getApplicationName()}</title>

		<script type="text/javascript">
			try {
				if (window.self !== window.top) {
					window.parent.location.href = "/${pageContext.request.contextPath}/login.jsp?gotoPage=" + window.parent.location.pathname;
				}
			} catch (e) {
				//top
			}
		</script>

		<style>
			.x-body{
				min-width:320px;
				display:flex;
				flex-direction:row;
			}
			/* by having a parent with flex-direction:row, the min-height bug in IE 10/11 doesn't stick around. */
			.ie-flex-fix
			{	
				display: flex;
				flex-direction: column;
				height: 100vh;
				width:100%;
			}
			.page-header
			{
				flex-shrink:0;
			}
			.page-content
			{
				flex: 1;
				min-height:700px;
			}
			.footer{
				padding: 10px;
				background: ${branding.primaryColor};
				color: ${branding.primaryTextColor};
				display: flex;
				justify-content: center;
				flex-shrink:0;
			}
			.footer .footer-content{
				width:1000px;
			}
			.auth-forms {
				background: rgba(255,255,255,.7);
				width: 1100px;
				border: 1px solid rgb(169, 169, 169);
				border-image-source: initial;
				border-image-slice: initial;
				border-image-width: initial;
				border-image-outset: initial;
				border-image-repeat: initial;
				border-radius: 10px;
				padding: 20px;
				margin: 20px auto 20px;
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
				width:inherit;
				display: inline-block;
			}
			.auth-content h1 {
				color: white;
			}

			.btn-primary {
				color: white;
				background-color: rgb(85, 85, 85);
				border-color: rgb(72, 72, 72);				
			}
			a.btn-primary {
				text-decoration:none;
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
				min-width:100px;
			}

			.form-control
			{
				padding-bottom:20px;
			}
			.form-control span
			{
				display: block;
			}
			.form-control input {
				display: block;
				width: 90%;
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

			.login-header {
				width: 100%;
				height: 56px;
				background-color: ${branding.primaryColor};
				color: ${branding.primaryTextColor};
			}

			.login-header h1 {
				margin: auto;
				text-align: center;
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
				text-align:center;
				width: 700px;
			}

			.right-col, .left-col {
				margin-left: 15px;
				margin-right: 15px;
				display:inline-block;
				vertical-align: text-top;
			}
			.right-col {
				width:320px;
			}

			#left-content {
				text-align:center;
			}
			#left-content video{
				height:350px;
			}

			#registration {
				border-bottom-style: solid;
				border-bottom-width: 1px;
				padding-bottom: 10px;
				margin-bottom:25px;
				width:100%;
			}
			#forgot-password-links{
				padding-top:10px;
			}
			#registration-video{
				position: fixed; /* Sit on top of the page content */
				display: none; /* Hidden by default */
				width: 100%; /* Full width (cover the whole page) */
				height: 100%; /* Full height (cover the whole page) */
				top: 0; 
				left: 0;
				right: 0;
				bottom: 0;
				background-color: rgba(0,0,0,0.5); /* Black background with opacity */
				z-index: 2; /* Specify a stack order in case you're using a different order for other elements */
			}
			#menu{
				float:right;
				padding-top: 6px;
				padding-right: 6px;
			}
			.dialog {
				width:600px;
				margin: 0;
				position: absolute;
				top: 50%;
				left: 50%;
				margin-right: -50%;
				transform: translate(-50%, -50%);
				border-color:black;
				border-style:solid;
				border-width:1px;
			}
			.dialog .header{
				background: ${branding.primaryColor};
				color: ${branding.primaryTextColor};
				height: 32px;
			}
			.dialog .header h2
			{
				margin-top:0px;
				padding-top:5px;
				padding-left:15px;

			}
			.dialog .content, .dialog .content video{
				width:100%;
				background:black;
			}
			.dialog .buttons{
				background:white;
				padding:5px;
				text-align:center;
			}
			.no-registration-video #registration-video-link{
				display: none;
			}
			.no-registration #registration{
				display: none;
			}
			.no-overview #left-content {
				display:none;
			}
			.no-overview .auth-content {
				display:flex;
				flex-direction:column;
				flex:1;
				justify-content: center;
				flex-shrink:0;
			}
			.no-overview .right-col,.no-overview .left-col {
				width:100%;
				display:flex;
				flex-direction:row;
				justify-content: center;
			}
			.no-overview #registration
			{
				padding-right:100px;
				border-bottom:0;
			}

			.no-overview .auth-content input {
				width: 280px;
			}	
			.no-overview .auth-content input[type=button] {
				width: inherit;
			}
			@media (max-width: 1160px){
				#left-content video{
					height:225px;
				}

				.logos {
					width: 450px;
				}
				.no-overview .logos {
					width: 700px;
				}
				.auth-forms {
					width:850px;
				}
				.page-content
				{
					min-height:550px;
				}
				.footer .footer-content{
					width:auto;
				}
			}
			@media (max-width: 910px){
				#left-content video{
					height:150px;
				}

				.logos {
					width: 300px;
				}

				.no-overview .logos {
					width: 450px;
				}
				.auth-forms {
					width:700px;
				}
				.no-overview #registration
				{
					padding-right:20px;
				}
			}
			@media (max-width: 767px){
				.auth-forms {
					width:420px;
				}
				.no-overview .logos {
					width: 300px;
				}
				.auth-content .left-col {
					border-bottom-style:solid;
					border-bottom-width:1px;
					float:none;
				}
				.no-overview .left-col {
					border:none;
					float:none;
				}
				.auth-content .left-col, .auth-content .right-col {
					width:90%;
				}
				.page-content{
					min-height:inherit;
				}
				.x-body, .ie-flex-fix, .footer{
					display:block;
				}

				.no-overview .right-col,.no-overview .left-col {
					display:block;
				}
				
				.dialog {
					width:450px;
				}
			}
			@media (max-width: 490px){
				.dialog { 
					min-width: 300px;
					width: 100%;
					left:0px;
					right:auto;
					transform: translate(0%, -50%);
				}
				.auth-forms {
					border:0px;
					width:100%;
					padding: 0px;
					margin: 5px auto 5px;
				}
				.login-header {
					height: 38px;

				}
				.login-header h1 {
					padding-top: 5px;

				}
				.sub-header h2 {
					font-size: 12px;		
					padding-left: 5px;
					padding-right: 5px;			
				}
				.auth-content input {
					width: 280px;
				}	
				.auth-content input[type=button] {
					width: inherit;
				}
				.page-content{
					min-height:inherit;
				}
				.x-body, .ie-flex-fix, .footer{
					display:block;
				}
				#menu .x-btn{
					height:27px;
					padding: 2px;
				}
			}
		</style>
		<link href="Branding.action?CSS&template=extTritonTheme.jsp&v=${appVersion}" rel="stylesheet" type="text/css"/>	
		<link href="Branding.action?CSS&template=apptemplate.jsp&v=${appVersion}" rel="stylesheet" type="text/css"/>
		<link href="Branding.action?Override&v=${appVersion}" rel="stylesheet" type="text/css"/>	
	</head>
	<body>
		<div class="ie-flex-fix">
			<div class="page-header">
				<div id="menu"><!-- Ext generated Menu --></div>
				<div class="login-header">	
					<h1>${branding.landingPageTitle}</h1>        
				</div>

				<div class="sub-header">
					<h2>${branding.landingPageBanner}</h2>
				</div>
			</div>
			<div class="page-content">
				<div id="browserWarning" class="browser-warning" >
					<p>You are using an <strong>unsupported</strong> browser. The website may not work as intended.  Please switch to <strong>
							<a class="browser-warning-link" href="http://www.mozilla.org/en-US/firefox/new/">Firefox</a></strong> or <strong>
							<a class="browser-warning-link" href="https://www.google.com/intl/en-US/chrome/browser/">Chrome</a></strong>, or <strong>
							<a class="browser-warning-link" href="http://browsehappy.com/">upgrade your browser</a></strong> to improve your experience
						<i class="fa fa-window-close-o fa-2x icon" aria-hidden="true"></i></p>	
				</div>
				<div class="auth-forms">
					<div class="auth-content">
						<p id="serverError" class="clearError" >
							Unable to connect to server or server failure.  Refresh page and try again.
						</p>
						<div class="left-col">
							<div class="logos">
								${branding.loginLogoBlock}
							</div>
							<div id="left-content">
							</div>
						</div>
						<div class="right-col">
							<div>
								<div id="registration">
									<h2>Sign up for an Account.</h2>
									<ol>
										<li>Go to the Signup page.</li>
										<li>Follow the steps on the screen to set up your account.</li>
										<li>Use the account you created to Sign In.</li>
									</ol>
									<a class="btn btn-primary" href="registration.jsp">Sign up</a>
									<a id="registration-video-link" class="btn btn-primary">How to Video <i class="fa fa-play-circle-o"></i></a>
								</div>
								<div id="registration-video">
									<!-- built my own dialog box because Ext.window.Window does not have good responsive support -->
									<div class="dialog">
										<div class="header"></div>
										<div class="content"></div>
										<div class="buttons"><input class="btn btn-primary" type="button" value="Close" /></div>
									</div>
								</div>
							</div>
							<div>
								<h2>Log In</h2>
								<form id="loginForm" action="Login.action?Login" method="POST">
									<div class="form-control">
										<input type="hidden" id="gotoPageId" name="gotoPage"  />	
										<span>Username</span>
										<input type="text" name="username" id="username" placeholder="Username" autofocus autocomplete="false">
										<p id="usernameError" class="clearError errorText"></p>
									</div>
									<div class="form-control">
										<span>Password</span>
										<input type="password" name="password" id="password" placeholder="Password" autocomplete="false" onkeypress="if (event.keyCode === 13) {
													keyPressLogin();
												}
											   ">
										<p id="passwordError" class="clearError errorText"></p>
									</div>
									<input type="button" value="Log in" class="btn btn-primary" onclick="submitForm();" />
								</form>
								<div id="forgot-password-links">
									<a href="resetPassword.jsp">Forgot Password</a> | <a href="forgotUser.jsp">Forgot Username</a>
								</div>
							</div>
						</div>
					</div>	
				</div>
			</div>
			<div class="footer">
				<div class="footer-content">
					<%=branding.getLoginWarning()%>	
				</div>
			</div>
		</div>
		<script type="text/javascript">

			sessionStorage.clear();

			Ext.define('Login.SupportMenu', {
				extend: 'Ext.button.Button',
				scale: 'large',
				ui: 'default',
				maxWidth: 250,
				initCallBack: null,
				showUserTools: true,
				showAdminTools: true,
				showEvaluatorTools: true,
				showHelp: true,
				showFeedback: true,
				text: 'Support',
				menu: {
					minWidth: 250
				},
				listeners: {
					menuhide: function (button, menu, opts) {
						var element = menu.getEl();
						if (element !== undefined) {
							element.setLeft(0);
						}
					}
				},
//				helpWin: Ext.create('OSF.component.HelpWindow', {}),
//				feedbackWin: Ext.create('OSF.component.FeedbackWindow', {}),
				customMenuItems: [],
				initComponent: function () {
					this.callParent();
					var userMenu = this;
					var menu = userMenu.getMenu();
					userMenu.loadMenu = function () {
						menu.removeAll();
						var menuItems = [];
						if (userMenu.showHelp) {
							menuItems.push({
								text: '<b>Frequently asked Questions</b>',
								iconCls: 'fa fa-2x fa-question-circle icon-button-color-default',
								handler: function () {
									userMenu.helpWin.show();
								}
							});
						}

						if (userMenu.showHelp) {
							menuItems.push({
								text: '<b>Tutorials / Videos</b>',
								iconCls: 'fa fa-2x fa-lightbulb-o icon-button-color-default',
								handler: function () {
									userMenu.helpWin.show();
								}
							});
						}

						if (userMenu.showFeedback) {
							menuItems.push({
								text: '<b>Contact Us</b>',
								iconCls: 'fa fa-2x fa-commenting icon-button-color-default',
								handler: function () {
									userMenu.feedbackWin.show();
								}
							});
						}
						menu.add(menuItems);
					};
					userMenu.loadMenu();
					menu.on('beforerender', function () {
						this.setWidth(this.up('button').getWidth());
					});
				}

			});

			var QueryString = function () {
				var query_string = {};
				var query = window.location.search.substring(1);
				var vars = query.split("&");
				for (var i = 0; i < vars.length; i++) {
					var pair = vars[i].split("=");
					// If first entry with this name
					if (typeof query_string[pair[0]] === "undefined") {
						query_string[pair[0]] = pair[1];
						// If second entry with this name
					} else if (typeof query_string[pair[0]] === "string") {
						var arr = [query_string[pair[0]], pair[1]];
						query_string[pair[0]] = arr;
						// If third or later entry with this name
					} else {
						query_string[pair[0]].push(pair[1]);
					}
				}
				return query_string;
			}();
			var submitForm = function () {
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
					for (var i = 0; i < ca.length; i++) {
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
							'X-Csrf-Token': token
						}
					],
					data: {
						username: $('#username').val(),
						password: $('#password').val(),
						gotoPage: $('#gotoPageId').val()
					},
					success: function (data) {
						if (data.success === false) {
							if (data.errors.password) {
								$("#password").addClass("errorField");
								$("#passwordError").removeClass("clearError");
								$("#passwordError").addClass("showError");
								$("#passwordError").html(data.errors.password);
							}
							if (data.errors.username) {
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
					error: function (xhr, status, errorThrown) {
						if (xhr.status === 403) {
							alert('403 - Already logged in on this browser. Logout and try again.');
						} else {
							$("#serverError").addClass("loginError");
						}
					}
				});
			};
			var keyPressLogin = function () {
				clearTimeout($.data(this, 'logintimer'));
				var wait = setTimeout(submitForm, 500);
				$(this).data('logintimer', wait);
			};
			$(function () {

				if (Ext.isIE10m) {
					$('#browserWarning').show();
					$('#browserWarning').click(function () {
						$('#browserWarning').hide();
					});
				}

				if (!${allowRegistration}) {
					$('html').addClass("no-registration");
				}
				if ("${branding.getLoginOverviewVideoUrl()}") {
					$("#left-content").html("<video controls src='${branding.getLoginOverviewVideoUrl()}' />");
				} else {
					$('html').addClass("no-overview");
				}
				if ("${branding.getLoginRegistrationVideoUrl()}") {
					var dialog = $("#registration-video .dialog");
					$(".header", dialog).html("<h2>How to Register</h2>");
					$(".content", dialog).html("<video autoplay='autoplay' controls src='${branding.getLoginRegistrationVideoUrl()}' />");
					$(".buttons .btn", dialog).click(function () {
						$('#registration-video').hide();
					});
				} else {
					$('html').addClass("no-registration-video");
				}

				$('#registration-video-link').click(function () {
					$('#registration-video').show();
				});
				if (QueryString.gotoPage !== undefined)
				{
					document.getElementById('gotoPageId').value = QueryString.gotoPage;
				} else {
					document.getElementById('gotoPageId').value = "${REFERENCED_URL}";
				}
				// commenting out this line uncomment when working on FAQ ticket (STORE-2211)
				//Ext.create('Login.SupportMenu', {renderTo: "menu"});
			});


		</script>	
	</body>
</html>




