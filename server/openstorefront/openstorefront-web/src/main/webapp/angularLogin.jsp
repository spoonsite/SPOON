<%-- 
    Document   : angularLogin.jsp
    Created on : Apr 9, 2015, 10:30:05 AM
    Author     : dshurtleff
--%><%@page import="java.util.logging.Level"%><%@page import="java.util.logging.Logger"%><%@page import="org.apache.shiro.subject.Subject"%><%@page import="java.util.Enumeration"%><%@page import="edu.usu.sdl.openstorefront.service.manager.PropertiesManager"%><%@page import="edu.usu.sdl.openstorefront.security.HeaderAuthToken"%><%@page import="edu.usu.sdl.openstorefront.security.HeaderRealm"%><%@page import="org.apache.shiro.realm.Realm"%><%@page import="org.apache.shiro.web.mgt.DefaultWebSecurityManager"%><%@page import="org.apache.shiro.SecurityUtils"%><%@page import="edu.usu.sdl.openstorefront.web.init.ShiroAdjustedFilter"%><%@page  import="edu.usu.sdl.openstorefront.util.SecurityUtil" %><%@page contentType="text/html" pageEncoding="UTF-8"%><%
	
           //Note: This is here to handle Virtual Angular routes and allow for redirecting back to the original request 
	if (SecurityUtil.isLoggedIn()) {
		RequestDispatcher rd = request.getRequestDispatcher("/index.html");
		rd.forward(request, response);		
	} else {
		
		final String STUB_HEADER = "X_STUBHEADER_X";
		
		//Check for open am set if so login and then send 
		Logger log = Logger.getLogger("angularLogin.jsp"); 
		
		org.apache.shiro.mgt.SecurityManager securityManager = SecurityUtils.getSecurityManager();		
		boolean loginHandled = false;
		boolean processHandling = true;
		if (securityManager instanceof DefaultWebSecurityManager) {
			DefaultWebSecurityManager webSecurityManager = (DefaultWebSecurityManager) securityManager;			
			for (Realm realm : webSecurityManager.getRealms()) {
				if (realm instanceof HeaderRealm) {
					HeaderAuthToken headerAuthToken = new HeaderAuthToken();
					headerAuthToken.setRequest(request);
					headerAuthToken.setAdminGroupName(PropertiesManager.getValue(PropertiesManager.KEY_OPENAM_HEADER_ADMIN_GROUP));
					headerAuthToken.setEmail(request.getHeader(PropertiesManager.getValue(PropertiesManager.KEY_OPENAM_HEADER_EMAIL, STUB_HEADER)));
					headerAuthToken.setFirstname(request.getHeader(PropertiesManager.getValue(PropertiesManager.KEY_OPENAM_HEADER_FIRSTNAME, STUB_HEADER)));

					Enumeration<String> groupValues = request.getHeaders(PropertiesManager.getValue(PropertiesManager.KEY_OPENAM_HEADER_GROUP, STUB_HEADER));
					StringBuilder group = new StringBuilder();
					while (groupValues.hasMoreElements()) {
						group.append(groupValues.nextElement());
						group.append(" | ");
					}

					headerAuthToken.setGroup(group.toString());
					headerAuthToken.setGuid(request.getHeader(PropertiesManager.getValue(PropertiesManager.KEY_OPENAM_HEADER_LDAPGUID, STUB_HEADER)));
					headerAuthToken.setLastname(request.getHeader(PropertiesManager.getValue(PropertiesManager.KEY_OPENAM_HEADER_LASTNAME, STUB_HEADER)));
					headerAuthToken.setOrganization(request.getHeader(PropertiesManager.getValue(PropertiesManager.KEY_OPENAM_HEADER_ORGANIZATION, STUB_HEADER)));
					headerAuthToken.setUsername(request.getHeader(PropertiesManager.getValue(PropertiesManager.KEY_OPENAM_HEADER_USERNAME, STUB_HEADER)));

					try {
						Subject currentUser = SecurityUtils.getSubject();
						currentUser.login(headerAuthToken);
						loginHandled = true;
					} catch (Exception ex) {								
						log.log(Level.SEVERE, "Check configuration", ex);
						response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unable to access system.");					
						processHandling = false;
					}
					break;
				}
			}			
		}
		
		//if not  send to login page	
		if (processHandling) {
				if (loginHandled == false) {
					String originalPage = (String) session.getAttribute(ShiroAdjustedFilter.REFERENCED_URL_ATTRIBUTE);
					RequestDispatcher rd = request.getRequestDispatcher("/login.jsp?gotoPage=" + originalPage);
					rd.forward(request, response);
				} else {
					RequestDispatcher rd = request.getRequestDispatcher("/index.html");
					rd.forward(request, response);
				}
		}
	}	
%>