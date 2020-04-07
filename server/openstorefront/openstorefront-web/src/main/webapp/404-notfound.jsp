<%-- 
	See LICENCE-APL.txt
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
	<link rel="shortcut icon" href="${pageContext.request.contextPath}/appicon.png" type="image/x-icon">
	
        <title>404 Resource Not Found</title>
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
				background: rgb(42, 58, 92);
				color: white;
			}			
			.auth-forms {
				background: white;
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
				color: black;
				border-bottom: lightgray 1px solid;
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
			body{
				background-color: beige;
				background-image: url(images/grid.png);
				background-repeat: repeat;
			}
		</style>		
    </head>
    <body>
		<div class="auth-forms">
			<div class="auth-content">
				<h1>404 - Resource Not Found</h1>
				<p>Sorry, but the page you were trying to view does not exist.</p>
				<p>Please update your Bookmarks</p>				
				<p>This was the result of either:</p>
				- a mistyped address<br>
				- an out-of-date link<br>
				- a resource that no longer exists<br>
				<br>				
				<a href="${pageContext.request.contextPath}">Return to Home Page</a> 		
			</div>
		</div>
  </body>
</html>
