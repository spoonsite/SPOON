<%-- 
Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.

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


<%-- 
    Document   : adminlayout
    Created on : Oct 28, 2015, 4:55:30 PM
    Author     : dshurtleff
--%>

<%@page import="edu.usu.sdl.openstorefront.security.SecurityUtil"%>
<%@page import="edu.usu.sdl.openstorefront.common.manager.PropertiesManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-definition>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

	
	<%
		String appVersion = PropertiesManager.getApplicationVersion();
		request.setAttribute("appVersion", appVersion);
		request.setAttribute("user", SecurityUtil.getCurrentUserName());
		request.setAttribute("usercontext", SecurityUtil.getUserContext());
		request.setAttribute("admin", SecurityUtil.isAdminUser());			
	%>	

	<link href="../webjars/extjs/6.0.0/build/classic/theme-neptune/resources/theme-neptune-all-debug.css" rel="stylesheet" type="text/css"/>
	<link href="../webjars/extjs/6.0.0/build/packages/ux/classic/neptune/resources/ux-all-debug.css" rel="stylesheet" type="text/css"/>
	<link href="../webjars/extjs/6.0.0/build/packages/charts/classic/neptune/resources/charts-all-debug.css" rel="stylesheet" type="text/css"/>
	<link href="../webjars/font-awesome/4.4.0/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
	<link href="css/defaultExtTheme.css?v=${appVersion}" rel="stylesheet" type="text/css"/>
	<link href="css/app.css?v=${appVersion}" rel="stylesheet" type="text/css"/>
	
	<script src="../webjars/extjs/6.0.0/ext-bootstrap.js" type="text/javascript"></script>
	<script src="../webjars/extjs/6.0.0/build/classic/theme-neptune/theme-neptune.js" type="text/javascript"></script>
	<script src="../webjars/extjs/6.0.0/build/packages/ux/classic/ux-debug.js" type="text/javascript"></script>
	<script src="../webjars/extjs/6.0.0/build/packages/charts/classic/charts-debug.js" type="text/javascript"></script>
	<script src="scripts/socket.io.js" type="text/javascript"></script>	
	<script src="../webjars/tinymcetextarea/5.1/tinymce/tinymce.min.js" type="text/javascript"></script>
	<script src="../webjars/tinymcetextarea/5.1/TinyMCETextArea.js" type="text/javascript"></script>
	
	<%-- Core Utils --%>	
	<script src="scripts/global/override.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/util/coreUtil.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/util/dateUtil.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/global/coreService.js?v=${appVersion}" type="text/javascript"></script>	
	
	<%-- Custom Components --%>		
	<script src="scripts/component/standardComboBox.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/component/notificationPanel.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/component/notificationWindow.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/component/framePanel.js?v=${appVersion}" type="text/javascript"></script>	
	<script src="scripts/component/userProfilePanel.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/component/userProfileWindow.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/component/feedbackWindow.js?v=${appVersion}" type="text/javascript"></script>
	
	<title>Admin Tools</title>
        <stripes:layout-component name="html_head"/>
    </head>
    <body>
	<stripes:layout-component name="body_header" />	
	
	
          <stripes:layout-component name="contents"/>
		  
	
	<stripes:layout-component name="body_footer" />	
    </body>
</html>
</stripes:layout-definition>