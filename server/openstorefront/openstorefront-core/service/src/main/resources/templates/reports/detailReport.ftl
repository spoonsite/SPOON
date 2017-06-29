<#ftl strip_whitespace = true>

<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>
    <body>
		<#list components as component>
			Component Name: ${component.getName()}
		</#list>
		THIS IS A TEST!!!!!
    </body>
</html>
