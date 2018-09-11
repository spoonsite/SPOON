<%-- 
/* 
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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

    Document   : externalLayout
    Created on : Feb 27, 2017, 3:25:00 PM
    Author     : dshurtleff
--%>

<%@page import="edu.usu.sdl.openstorefront.core.entity.Branding"%>
<%@page import="edu.usu.sdl.openstorefront.service.ServiceProxy"%>
<%@page import="edu.usu.sdl.openstorefront.common.manager.PropertiesManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-definition>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=10, user-scalable=yes">
	
		<link href="webjars/extjs/6.2.0/build/classic/theme-triton/resources/theme-triton-all-debug.css" rel="stylesheet" type="text/css"/>
		<link href="webjars/extjs/6.2.0/build/packages/ux/classic/triton/resources/ux-all-debug.css" rel="stylesheet" type="text/css"/>
		<link href="webjars/extjs/6.2.0/build/packages/charts/classic/triton/resources/charts-all-debug.css" rel="stylesheet" type="text/css"/>
		<link href="webjars/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
		<link href="Branding.action?CSS&template=extTritonTheme.css.jsp&v=${appVersion}" rel="stylesheet" type="text/css"/>	
		<link href="Branding.action?CSS&template=apptemplate.css.jsp&v=${appVersion}" rel="stylesheet" type="text/css"/>
		<link href="Branding.action?Override&v=${appVersion}" rel="stylesheet" type="text/css"/>	

		<link rel="shortcut icon" href="${pageContext.request.contextPath}/appicon.png" type="image/x-icon">			

		<script src="webjars/extjs/6.2.0/ext-bootstrap.js" type="text/javascript"></script>
		<script src="webjars/extjs/6.2.0/build/classic/theme-triton/theme-triton.js" type="text/javascript"></script>
		<script src="webjars/extjs/6.2.0/build/packages/ux/classic/ux-debug.js" type="text/javascript"></script>
		<script src="webjars/extjs/6.2.0/build/packages/charts/classic/charts-debug.js" type="text/javascript"></script>
		
		<script src="scripts/component/framePanel.js?v=${appVersion}" type="text/javascript"></script>			
		<script src="scripts/util/coreUtil.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/global/override.js?v=${appVersion}" type="text/javascript"></script>
		
		<%
			String appVersion = PropertiesManager.getInstance().getApplicationVersion();		
			request.setAttribute("appVersion", appVersion);

			Branding brandingView = ServiceProxy.getProxy().getBrandingService().getCurrentBrandingView();
			request.setAttribute("appTitle", brandingView.getApplicationName());
		%>
		
		<title>${appTitle}</title>
			<stripes:layout-component name="html_head"/>
			
		<style>	
			.field-required:after {
				color: red;
				font-weight: bold;
				content: "*";
			}
			
			body{
				background-color: beige;
				background-image: url(images/grid.png);
				background-repeat: repeat;
			}
		</style>	
		</head>
		<body>
			<div id="browserWarning" class="browser-warning" >
				<p>You are using an <strong>unsupported</strong> browser. The website may not work as intended.  Please switch to <strong>
				<a class="browser-warning-link" href="http://www.mozilla.org/en-US/firefox/new/">Firefox</a></strong> or <strong>
				<a class="browser-warning-link" href="https://www.google.com/intl/en-US/chrome/browser/">Chrome</a></strong>, or <strong>
				<a class="browser-warning-link" href="http://browsehappy.com/">upgrade your browser</a></strong> to improve your experience	
				<i class="fa fa-window-close-o fa-2x icon" aria-hidden="true"></i></p>	
		   </div>
		<stripes:layout-component name="body_header" />	


		<stripes:layout-component name="contents"/>


		<stripes:layout-component name="body_footer" />	
		<script type="text/javascript">
			//start fresh on index
			sessionStorage.clear();
			if (Ext.isIE10m) {
				Ext.get('browserWarning').setStyle({
					display: 'block'
				});
				Ext.get('browserWarning').on("click", function () {
					Ext.get('browserWarning').setStyle({
						display: 'none'
					});
				} );
				}

		</script>			
	
    </body>
</html>
</stripes:layout-definition>	