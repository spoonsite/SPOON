<%--
/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
		 <h1 style="padding-bottom: 10px;">Error Handling</h1>
		 <hr>
		<h2>Common Application Success Codes</h2>	
		Note: Redirection follows the HTTP protocols see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html">HTTP codes</a><br>
		<table>
			<tr>
				<th style='text-align: left;'>
					Code
				</th>
				<th style='text-align: left;'>
					Description
				</th>
			</tr>
			<tr>
				<td width="175">
					200 OK
				</td>
				<td>
					The request has succeeded.
				</td>
			</tr>
			<tr>
				<td>
					201 Created
				</td>
				<td>
					The request has been fulfilled and resulted in a new resource being created.
					Location header is returned with the URI for the new resource.
				</td>
			</tr>			
		</table>		
		
		<h2>Common Application Error Codes</h2>		
		<table>
			<tr>
				<th style='text-align: left;'>
					Code
				</th>
				<th style='text-align: left;'>
					Description
				</th>
			</tr>
			<tr>
				<td width="175">
					400 Bad Request
				</td>
				<td>
					The request could not be understood by the server due to malformed syntax. 
					Also can occur if the  required parameters are missing.
				</td>
			</tr>			
			<tr>
				<td width="175">
					401 Unauthorized
				</td>
				<td>
					The request requires user authentication. 
				</td>
			</tr>
			<tr>
				<td>
					404 Not Found
				</td>
				<td>
					The server has not found anything matching the Request-URI.
				</td>
			</tr>	
			<tr>
				<td>
					405 Method Not Allowed
				</td>
				<td>
					The method specified in the Request-Line is not allowed for the resource identified by the Request-URI.
					Also note, some methods require the user to have the correct permissions before accepting the request.
				</td>
			</tr>			
		</table>
		<br>
		
		<h2>Validation Error Model</h2>		
		<pre>
{
	"success": "",
	"errors": [
	'fieldname' : 'error message', 
	...
	]
}			
		</pre>		
		
		<h2>System Error Model</h2>
		(Internal Errors;  Typically unexpected)
		<pre>
{
	"message": "",
	"errorTicketNumber": "",
	"potentialResolution": ""
}			
		</pre>		

    </body>
</html>
