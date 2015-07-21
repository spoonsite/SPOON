<%-- 
    Document   : angularLogin.jsp
    Created on : Apr 9, 2015, 10:30:05 AM
    Author     : dshurtleff
--%>
<%@page import="com.samaxes.filter.util.HTTPCacheHeader"%><%@page import="java.util.logging.Level"%><%@page import="java.util.logging.Logger"%><%@page import="org.apache.shiro.subject.Subject"%><%@page import="java.util.Enumeration"%><%@page import="edu.usu.sdl.openstorefront.service.manager.PropertiesManager"%><%@page import="edu.usu.sdl.openstorefront.security.HeaderAuthToken"%><%@page import="edu.usu.sdl.openstorefront.security.HeaderRealm"%><%@page import="org.apache.shiro.realm.Realm"%><%@page import="org.apache.shiro.web.mgt.DefaultWebSecurityManager"%><%@page import="org.apache.shiro.SecurityUtils"%><%@page import="edu.usu.sdl.openstorefront.web.init.ShiroAdjustedFilter"%><%@page  import="edu.usu.sdl.openstorefront.util.SecurityUtil" %><%@page contentType="text/html" pageEncoding="UTF-8"%><%
	out.clear();
	
           //Note: This is here to handle Virtual Angular routes and allow for redirecting back to the original request 
	response.setHeader(HTTPCacheHeader.CACHE_CONTROL.getName(), "no-cache, no-store");
	response.setDateHeader(HTTPCacheHeader.EXPIRES.getName(), 0L);	
		
	if (SecurityUtil.isLoggedIn()) {
		RequestDispatcher rd = request.getRequestDispatcher("/index.html");
		rd.include(request, response);		
	} else {
		
		if (HeaderRealm.isUsingHeaderRealm()) {
			boolean loginSuccessful = HeaderRealm.handleHeaderLogin(request);
			if (loginSuccessful == false) {						
				response.sendRedirect(response.encodeRedirectURL("403-forbidden.jsp?goto="+request.getSession().getAttribute(ShiroAdjustedFilter.REFERENCED_URL_ATTRIBUTE)));			
			} else {
				RequestDispatcher rd = request.getRequestDispatcher("/index.html");
				rd.include(request, response);
			}
		} else {
			String originalPage = (String) session.getAttribute(ShiroAdjustedFilter.REFERENCED_URL_ATTRIBUTE);
			RequestDispatcher rd = request.getRequestDispatcher("/login.jsp?gotoPage=" + originalPage);
			rd.forward(request, response);			
		}
	}	
%>