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
		<p>Here is your user registration email verification code.</p>
		<p>${verificationCode}</p>
		<p>If you are not the intended user of this message, please delete this message. <br>
			Contact ${replyName} at ${replyAddress} for questions.</p>
		</body>
	</html>
