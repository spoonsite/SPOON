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
	
		String appVersion = PropertiesManager.getInstance().getApplicationVersion();
		request.setAttribute("appVersion", appVersion);
		request.setAttribute("user", SecurityUtil.getCurrentUserName());
		request.setAttribute("usercontext", SecurityUtil.getUserContext());
		request.setAttribute("admin", SecurityUtil.isEntryAdminUser());			
	%>	

	<link href="webjars/extjs/6.2.0/build/classic/theme-triton/resources/theme-triton-all-debug.css" rel="stylesheet" type="text/css"/>
	<link href="webjars/extjs/6.2.0/build/packages/ux/classic/triton/resources/ux-all-debug.css" rel="stylesheet" type="text/css"/>
	<link href="webjars/extjs/6.2.0/build/packages/charts/classic/triton/resources/charts-all-debug.css" rel="stylesheet" type="text/css"/>
	<link href="webjars/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
	<link href="Branding.action?CSS&template=extTritonTheme.css.jsp&v=${appVersion}&brandingId=${param.brandingId}" rel="stylesheet" type="text/css"/>	
	<link href="Branding.action?CSS&template=apptemplate.css.jsp&brandingId=${param.brandingId}&v=${appVersion}" rel="stylesheet" type="text/css"/>
	<link href="Branding.action?Override&brandingId=${param.brandingId}&v=${appVersion}" rel="stylesheet" type="text/css"/>	
	
	<script src="webjars/extjs/6.2.0/ext-bootstrap.js" type="text/javascript"></script>
	<script src="webjars/extjs/6.2.0/build/classic/theme-triton/theme-triton.js" type="text/javascript"></script>
	<script src="webjars/extjs/6.2.0/build/packages/ux/classic/ux-debug.js" type="text/javascript"></script>
	<script src="webjars/extjs/6.2.0/build/packages/charts/classic/charts-debug.js" type="text/javascript"></script>
	
	<%-- Core Utils --%>	
	<script src="scripts/util/coreUtil.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/util/dateUtil.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/global/coreService.js?v=${appVersion}" type="text/javascript"></script>		
	<script src="scripts/global/override.js?v=${appVersion}" type="text/javascript"></script>	
	
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
