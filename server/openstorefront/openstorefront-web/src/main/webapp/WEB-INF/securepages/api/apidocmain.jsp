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
							border: true,
							bodyStyle: 'background-color: white; padding: 10px;',							
							collapsible: true,							
							minWidth: 300,
							split: true,
							scrollable: true,
							tpl: new Ext.XTemplate(
								'<h4>API</h4>',
								'<ul>',
									'<li>',
										'<a href="#API.action?Page&page=intro" >Introduction</a>',
									'</li>',
									'<li>',
										'<a href="#API.action?Page&page=security" >Security</a>',
									'</li>',
									'<li>',
										'<a href="#API.action?Page&page=errorhandling" >Error Handling</a>',
									'</li>',
									'<li>',
										'<a href="#API.action?Page&page=lookup" >Lookup Table</a>',
									'</li>',
									'<li>',
										'<a href="#API.action?Page&page=attribute" >Attribute Table</a>',
									'</li>',
									'<li>',
										'<a href="API.action?ViewEntities" target="_blank">Entities</a>',
									'</li>',
								'</ul>',								
								'<h4 title="(/api/v1/resource/) - Resources are entities that the server manages.  API provides CRUD support, querying and some resource specific behavior.">',
								'Resources',
								'</h4>',
								'<ul>',
								'	<tpl for="resourceClasses">',
								'		<li>',
								'			<a href="#API.action?API&resourceClass={code}">{description}</a>',
								'		</li>',
								'	</tpl>',
								'</ul>',
								'<h4 title="(/api/v1/service/) - Services are related actions that act across resources or apply behaviour to the system.">',
								'Services',
								'</h4>',
								'<ul>',
								'	<tpl for="serviceClasses">',
								'		<li>',
								'			<a href="#API.action?API&resourceClass={code}">{description}</a>',
								'		</li>',
								'	</tpl>',
								'</ul>'								
							)
						},
						{
							region: 'center',
							id: 'mainContent',
							title: 'Details',
							border: true,
							layout: 'fit'
							
						}
					]
				});
				
				//load navigation
				Ext.getCmp('navigation').setLoading(true);
				Ext.Ajax.request({
					url: 'API.action?Services',
					callback: function() {
						Ext.getCmp('navigation').setLoading(false);
					},
					success: function(response, opts) {
						var data = Ext.decode(response.responseText);
						Ext.getCmp('navigation').update(data);
					}
				});
				
				
				var loadPage = function(page) {
					
					var panel = Ext.create('Ext.panel.Panel', {
						scrollable: 'true',
						bodyStyle: 'padding: 20px;',
						loader: {
							autoLoad: true,							
							url: 'API.action?Page&page=' + page
						}
					});
					Ext.getCmp('mainContent').removeAll();
					Ext.getCmp('mainContent').add(panel);				
				};
				loadPage('intro');
				
				var loadService = function(service) {
					
				};

				var loadLookupTables = function() {
					
				};
				
				var loadAttributeTables = function() {
					
				};


			});
		</script>		
    </body>
</html>
