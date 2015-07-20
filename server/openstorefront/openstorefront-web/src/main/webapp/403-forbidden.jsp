<%-- 
    Document   : 403-forbbiden
    Created on : Sep 25, 2014, 4:51:09 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page  import="edu.usu.sdl.openstorefront.util.SecurityUtil" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>403 Forbidden</title>
    </head>
    <body>
        <h1>Forbidden</h1>
	${exception}	
	<%
		if (SecurityUtil.isLoggedIn()) {
			String gotoPage = request.getParameter("goto");
			if (gotoPage != null) {
				if (gotoPage.startsWith("/")){
					gotoPage = gotoPage.substring(1, gotoPage.length());
				}
				response.sendRedirect(response.encodeRedirectURL(gotoPage));			
			}
		} else {		
			out.print("Awaiting login from Auth System");						
		}
	%>	
    </body>
</html>
