<#ftl strip_whitespace = true>

<#assign charset="UTF-8">
<!DOCTYPE html>
<html>
	<head>
		<title>Email Comment</title>
		<meta charset="${charset}">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		</head>
	<body>
			This is an auto generated email to show the comments that were made to a component
			that you are subscribed to. <br
			<strong>Entry:</strong> ${entryName} <br>
			<strong>Current-Step:</strong> ${currentStep} <br>
			<strong>author:</strong> ${author} <br><br><br>	
			<strong>Comment:</strong> ${comment} <br><br><br>
			<strong>Reply-Instructions:</strong> ${replyInstructions} <br>
	</body>
</html>