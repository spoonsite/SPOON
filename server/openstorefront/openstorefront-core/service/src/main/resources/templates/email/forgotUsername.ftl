<#ftl strip_whitespace = true>

<#assign charset="UTF-8">
<!DOCTYPE html>
<html>
	<head>
		<title>${title}</title>
		<meta charset="${charset}">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		</head>
	<body>
		<#if username??>
			Your username is: <br><br> ${username} <br><br>
						
			If you have forgotten your password. Click on "Forgot Password" on the login page.<br><br>
			
			*Note: There may be multiple accounts associated with your email.<br>
			If there is an account that you don't recognize contact ${replyName} at ${replyAddress}
		<#else>
			<b>Unable to find a username associated with your email.</b> <br><br>
			The email address must match the email used to create the user. (case sensitive)<br>
			Email address can be changed on your user profile after logging in.<br><br>
			
			Contact ${replyName} at ${replyAddress} for questions.</p>
        </#if>
	</body>
</html>
