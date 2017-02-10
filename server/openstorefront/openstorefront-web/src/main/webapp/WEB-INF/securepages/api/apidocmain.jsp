<%-- 
    Document   : apidocmain
    Created on : Feb 9, 2017, 4:35:50 PM
    Author     : dshurtleff
--%>

<%@page import="edu.usu.sdl.openstorefront.common.manager.PropertiesManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="webjars/extjs/6.2.0/build/classic/theme-triton/resources/theme-triton-all-debug.css" rel="stylesheet" type="text/css"/>
		<link href="webjars/extjs/6.2.0/build/packages/ux/classic/triton/resources/ux-all-debug.css" rel="stylesheet" type="text/css"/>
		<link href="webjars/extjs/6.2.0/build/packages/charts/classic/triton/resources/charts-all-debug.css" rel="stylesheet" type="text/css"/>
		<link href="webjars/font-awesome/4.4.0/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
		<link href="Branding.action?CSS&template=extTritonTheme.jsp&v=${appVersion}" rel="stylesheet" type="text/css"/>	
		<link href="Branding.action?CSS&template=apptemplate.jsp&v=${appVersion}" rel="stylesheet" type="text/css"/>
		<link href="Branding.action?Override&v=${appVersion}" rel="stylesheet" type="text/css"/>	

		<link rel="shortcut icon" href="${pageContext.request.contextPath}/appicon.png" type="image/x-icon">	
		<link href="apidoc/css/apidoc.css" rel="stylesheet" type="text/css"/>

		<script src="webjars/extjs/6.2.0/ext-bootstrap.js" type="text/javascript"></script>
		<script src="webjars/extjs/6.2.0/build/classic/theme-triton/theme-triton.js" type="text/javascript"></script>
		<script src="webjars/extjs/6.2.0/build/packages/ux/classic/ux-debug.js" type="text/javascript"></script>
		<script src="webjars/extjs/6.2.0/build/packages/charts/classic/charts-debug.js" type="text/javascript"></script>
    </head>
    <body>
        
	<%
		String appVersion = PropertiesManager.getApplicationVersion();		
		request.setAttribute("appVersion", appVersion);
	%>		
		
		<script type="text/javascript">
			Ext.onReady(function(){
				
				
				Ext.create('Ext.container.Viewport', {
					layout: 'border',
					items: [
						{
							region: 'north',
							dockedItems: [
								{
									xtype: 'toolbar',
									cls: 'header',
									items: [
										{
											xtype: 'tbtext',											
											text: '<span class="api-title">Open Storefront API ${appVersion}</span>'
										},
										{
											xtype: 'tbfill'
										},
										{
											text: 'Print View',
											iconCls: 'fa fa-print',
											handler: function(){
												
											}
										}
									]
								}
							]
						},
						{
							region: 'west',
							id: 'navigation',
							title: 'Resources',
							minWidth: 300,
							split: true,
							scrollable: true,
							tpl: new Ext.XTemplate(
								'Test'
							)
						},
						{
							region: 'center',
							title: 'Details'
							
						}
					]
				});
								
			});
		</script>		
    </body>
</html>
