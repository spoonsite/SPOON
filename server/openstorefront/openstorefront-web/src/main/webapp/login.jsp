<%-- 
    Document   : login
    Created on : Apr 25, 2014, 3:18:20 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
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
			body{
				background-color: beige;
			}
		</style>
	</head>
	<body>
	<div class="auth-forms">
	  <div class="auth-content">
		<div class="row" style="padding-left: 20px;padding-right: 20px;">
		  <h2>Log In</h2>
		  <form id="loginForm" action="Login.action?Login" method="POST">
			  <span >By logging in you are consenting to these conditions</span>
			<div class="disclaimer">
			  <h1>WARNING:</h1>
			  <p>
				You are accessing a U.S. Government (USG) Information System (IS) that is provided for USG-authorized use only. By using this IS (which includes any device attached to this IS), you consent to the following conditions: 1) The USG routinely intercepts and monitors communication on this IS for purposes including, but not limited to, penetration testing, COMSEC monitoring, network operations and defense, personnel misconduct (PM), law enforcement (LE), and counterintelligence (CI) investigations, 2) At any time, the ISG may inspect and seize data stored on this IS, 3) Communications using, or data stored on this IS are not private, are subject to routine monitoring, interception, and search, and may be disclosed or used for any USG -authorized purpose. 4) This IS includes security measures (e.g. authentication and access controls) to protect USG interests not for your personal benefit or privacy. 5) Notwithstanding the above, using this IS does not constitute consent to PM, LE, or CI investigative searching or monitoring of the content of privileged communication, or work product, related to personal representation or services by attorneys, psychotherapists, or clergy, and their assistants. Such communication and work product are private and confidential.
			  </p>
			</div>		
			<div style="width: 300px; margin: 0px auto;">
				<input type="hidden" id="gotoPageId" name="gotoPage"  />	
				<input type="text" name="username" placeholder="Username" class="form-control" autofocus autocomplete="false" style="width: 200px;">
				<br>
				<input type="password" name="password" placeholder="Password" class="form-control" autocomplete="false" style="width: 200px;">
				<br>
				<br>
				<input type="submit" value="Log in" style="width: auto;" class="btn btn-primary" />
			</div>
		  </form>
		</div>
	  </div>
	</div>	
		<script type="text/javascript">
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
				}
		</script>
	</body>
</html>




