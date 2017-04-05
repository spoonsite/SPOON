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
		request.setAttribute("admin", SecurityUtil.isEntryAdminUser());
	%>	
	
		
	<stripes:layout-component name="contents"/>		  
	
</stripes:layout-definition>