<%-- 
Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>

<%@page import="edu.usu.sdl.openstorefront.security.SecurityUtil"%>
<%-- 
    Created on : Jan 28, 2016, 4:27:30 PM
    Author     : dshurtleff
--%>

<%@page import="edu.usu.sdl.openstorefront.common.manager.PropertiesManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-definition>
	
	<%
		String appVersion = PropertiesManager.getApplicationVersion();
		request.setAttribute("appVersion", appVersion);
		request.setAttribute("user", SecurityUtil.getCurrentUserName());
		request.setAttribute("usercontext", SecurityUtil.getUserContext());
		request.setAttribute("admin", SecurityUtil.isAdminUser());
	%>	
	
		
	<stripes:layout-component name="contents"/>		  
	
</stripes:layout-definition>