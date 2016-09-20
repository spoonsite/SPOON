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
<%-- 
    Document   : brandingPreview
    Created on : Apr 13, 2016, 4:56:20 PM
    Author     : dshurtleff
--%>

<%@page import="edu.usu.sdl.openstorefront.core.entity.Branding"%>
<%@page import="edu.usu.sdl.openstorefront.service.ServiceProxy"%>
<%@page import="edu.usu.sdl.openstorefront.security.SecurityUtil"%>
<%@page import="edu.usu.sdl.openstorefront.common.manager.PropertiesManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>

	<%
		
		Branding branding = new Branding();
		branding.setBrandingId(request.getParameter("brandingId"));
		branding = branding.find();
		request.setAttribute("branding", branding);
	
		String appVersion = PropertiesManager.getApplicationVersion();
		request.setAttribute("appVersion", appVersion);
		request.setAttribute("user", SecurityUtil.getCurrentUserName());
		request.setAttribute("usercontext", SecurityUtil.getUserContext());
		request.setAttribute("admin", SecurityUtil.isAdminUser());			
	%>	

	<link href="webjars/extjs/6.0.0/build/classic/theme-neptune/resources/theme-neptune-all-debug.css" rel="stylesheet" type="text/css"/>
	<link href="webjars/extjs/6.0.0/build/packages/ux/classic/neptune/resources/ux-all-debug.css" rel="stylesheet" type="text/css"/>
	<link href="webjars/extjs/6.0.0/build/packages/charts/classic/neptune/resources/charts-all-debug.css" rel="stylesheet" type="text/css"/>
	<link href="webjars/font-awesome/4.4.0/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
	<link href="Branding.action?CSS&template=extTheme.jsp&brandingId=${param.brandingId}&v=${appVersion}" rel="stylesheet" type="text/css"/>
	<link href="Branding.action?CSS&template=apptemplate.jsp&brandingId=${param.brandingId}&v=${appVersion}" rel="stylesheet" type="text/css"/>
	<link href="Branding.action?Override&brandingId=${param.brandingId}&v=${appVersion}" rel="stylesheet" type="text/css"/>		
	
	<script src="webjars/extjs/6.0.0/ext-bootstrap.js" type="text/javascript"></script>
	<script src="webjars/extjs/6.0.0/build/classic/theme-neptune/theme-neptune.js" type="text/javascript"></script>
	<script src="webjars/extjs/6.0.0/build/packages/ux/classic/ux-debug.js" type="text/javascript"></script>
	<script src="webjars/extjs/6.0.0/build/packages/charts/classic/charts-debug.js" type="text/javascript"></script>
	
	<%-- Core Utils --%>	
	<script src="scripts/global/override.js?v=${appVersion}" type="text/javascript"></script>	
	<script src="scripts/util/coreUtil.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/util/dateUtil.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/global/coreService.js?v=${appVersion}" type="text/javascript"></script>		
	
	<script type="text/javascript">
		/* global Ext, CoreUtil */
		Ext.onReady(function () {

			var testWindow = Ext.create('Ext.window.Window', {
				title: 'Primary Color',
				width: '80%',
				height: '80%',
				draggable: false,
				maximizable: true,
				scrollable: true,
				items: [
					{
						xtype: 'panel',
						id: 'loginWarningPanel',
						title: 'Panel header - Login Warning',
						tpl: '{loginWarning}'
					}, 
					{
						xtype: 'panel',
						id: 'footerPanel',
						title: 'Panel header - Footer',
						tpl: '{landingPageFooter}'
					}
				]
			});
			testWindow.show();

			Ext.create('Ext.container.Viewport', {
				layout: 'border',
				items: [
					{
						xtype: 'panel',
						region: 'north',
						id: 'topNavPanel'							
					},
					{
						region: 'center',
						xtype: 'panel',
						dockedItems: [
							{
								xtype: 'toolbar',
								dock: 'top',
								items: [
									{
										text: 'Show Window',
										handler: function(){
											testWindow.show();
										}
									}
								]
							}
						]
					}
				]
			});

			CoreService.brandingservice.getBranding('${param.brandingId}').then(function(response, opts){
				var branding = Ext.decode(response.responseText);
				if (branding.securityBannerText && branding.securityBannerText !== '') {
					Ext.getCmp('topNavPanel').addDocked(CoreUtil.securityBannerPanel({
						securityBannerText: branding.securityBannerText
					}), 0);
				}
				Ext.getCmp('loginWarningPanel').update(branding);
				Ext.getCmp('footerPanel').update(branding);
			});				


		});
	</script>
