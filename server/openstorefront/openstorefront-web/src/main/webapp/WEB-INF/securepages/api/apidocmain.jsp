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
		<link href="webjars/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
		<link href="Branding.action?CSS&template=extTritonTheme.jsp&v=${appVersion}" rel="stylesheet" type="text/css"/>	
		<link href="Branding.action?CSS&template=apptemplate.jsp&v=${appVersion}" rel="stylesheet" type="text/css"/>
		<link href="Branding.action?Override&v=${appVersion}" rel="stylesheet" type="text/css"/>	

		<link rel="shortcut icon" href="${pageContext.request.contextPath}/appicon.png" type="image/x-icon">	
		<link href="apidoc/css/apidoc.css" rel="stylesheet" type="text/css"/>

		<script src="webjars/extjs/6.2.0/ext-bootstrap.js" type="text/javascript"></script>
		<script src="webjars/extjs/6.2.0/build/classic/theme-triton/theme-triton.js" type="text/javascript"></script>
		<script src="webjars/extjs/6.2.0/build/packages/ux/classic/ux-debug.js" type="text/javascript"></script>
		<script src="webjars/extjs/6.2.0/build/packages/charts/classic/charts-debug.js" type="text/javascript"></script>
		
		<script src="scripts/global/override.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/framePanel.js?v=${appVersion}" type="text/javascript"></script>	
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
											href: 'API.action?PrintView'
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
										'<a href="#loadPage=intro" >Introduction</a>',
									'</li>',
									'<li>',
										'<a href="#loadPage=security" >Security</a>',
									'</li>',
									'<li>',
										'<a href="#loadPage=errorhandling" >Error Handling</a>',
									'</li>',
									'<li>',
										'<a href="#loadLookupTables=lookup" >Lookup Table</a>',
									'</li>',
									'<li>',
										'<a href="#loadAttributeTables=attribute" >Attribute Table</a>',
									'</li>',
									'<li>',
										'<a href="#loadFrame=API.action?ViewEntities">Entities</a>',
									'</li>',
								'</ul>',								
								'<h4 title="(/api/v1/resource/) - Resources are entities that the server manages.  API provides CRUD support, querying and some resource specific behavior.">',
								'Resources',
								'</h4>',
								'<ul>',
								'	<tpl for="resourceClasses">',
								'		<li>',
								'			<a href="#loadService={code}">{description}</a>',
								'		</li>',
								'	</tpl>',
								'</ul>',
								'<h4 title="(/api/v1/service/) - Services are related actions that act across resources or apply behaviour to the system.">',
								'Services',
								'</h4>',
								'<ul>',
								'	<tpl for="serviceClasses">',
								'		<li>',
								'			<a href="#loadServiceResource={code}">{description}</a>',
								'		</li>',
								'	</tpl>',
								'</ul>'								
							)
						},
						{
							region: 'center',
							id: 'mainContent',
							title: 'Details',
							header: {
								style: 'background-color: black;'
							},							
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
				
				var loadPage = function(page, fullUrl) {		
					var basePath = 'API.action?Page&page=';
					if (fullUrl) {
						basePath = fullUrl;
					}
				
					var panel = Ext.create('Ext.panel.Panel', {
						scrollable: 'true',
						bodyStyle: 'padding: 20px;',
						loader: {
							autoLoad: true,	
							scripts: true,
							url: basePath + page
						}
					});
					Ext.getCmp('mainContent').removeAll();
					Ext.getCmp('mainContent').add(panel);				
				};
								
				
				var apiDetailTemplate = new Ext.XTemplate(						
				);

				Ext.Ajax.request({
					url: 'API.action?Page&page=apidetailTemplate',
					success: function(response, opts){
						apiDetailTemplate.set(response.responseText, true);
						
						var historyToken = Ext.util.History.getToken();	
						handleHistory(historyToken);

						Ext.util.History.on('change', function(historyToken, opts){
							handleHistory(historyToken);
						});						
					}
				});				
								
				var loadService = function(service, serviceType) {					
					
					var panel = Ext.create('Ext.panel.Panel', {
						layout: 'border',
						items: [
							{
								region: 'north',								
								itemId: 'nav',
								border: true,
								xtype: 'grid',
								split: true,
								maxHeight: 350,
								dockedItems: [
									{
										xtype: 'panel',
										itemId: 'header',
										tpl: new Ext.XTemplate(
											'<h1 style="border-bottom: 1px lightgrey solid; padding-top: 10px; padding-bottom: 10px;">{resourceName}</h1>',
											'<div style="padding: 10px;">{resourceDescription}</div>',
											'<tpl if="securityRestriction">',
											'	',
											'</tpl>'											
										)
									}
								],
								columnLines: true,
								store: {									
								},
								columns: [
									{ text: 'Method', dataIndex: 'restMethod', width: 175,
										renderer: function(value, meta, record) {
											return '<span class="' + value + '">' + value + '</span>';
										}
									},
									{ text: 'Path', dataIndex: 'path', flex: 1, minWidth: 250,
										renderer: function(value, meta, record) {
											return '<b>' + value + '</b>';
										}
									},
									{ text: 'Description', dataIndex: 'description', flex: 2, minWidth: 300}
								],
								viewConfig: {
									enableTextSelection: true
								},								
								listeners: {
									itemclick: function(grid, record, item, index, e, opts) {
										var details = panel.getComponent('details');
										//var detailEl = details.getEl();
										//var detailMethod = Ext.get('api-method-' + record.get('id'));										
										//detailMethod.scrollIntoView(detailEl, true, true, true);
										
										details.update(record);	
									}
								}
							},
							{
								region: 'center',								
								itemId: 'details',
								scrollable: true,
								bodyStyle: 'padding: 20px',
								height: 300,
								tpl: apiDetailTemplate
							}
						]
					});					
					Ext.getCmp('mainContent').removeAll();
					Ext.getCmp('mainContent').add(panel);
					
					panel.setLoading(true);
					var classPath = '';
					if (serviceType) {
						classPath = '&classPath=service';
					}
					
					Ext.Ajax.request({
						url: 'API.action?API&resourceClass=' + service + classPath,
						callback: function(){
							panel.setLoading(false);
						},
						success: function(response, opts) {
							var data = Ext.decode(response.responseText);
														
							Ext.Array.each(data.methods, function(item){																
								item.path = data.resourcePath + (item.methodPath ? item.methodPath : '');
							});
														
							panel.getComponent('nav').getComponent('header').update(data);	
							panel.getComponent('nav').getStore().loadRawData(data.methods);
								
							
							panel.updateLayout(true, true);
						}
					});
										
				};

				var loadLookupTables = function() {
					
					var panel = Ext.create('Ext.panel.Panel', {
						layout: 'border',
						items: [
							{
								region: 'west',
								title: 'Lookup Table',
								layout: 'fit',
								width: 375,
								split: true,
								items: [
									{
										xtype: 'grid',
										columnLines: true,
										store: {
											sorters: [
												new Ext.util.Sorter({
													property: 'code',
													direction: 'ASC'
												})
											],											
											autoLoad: true,
											proxy: {
												type: 'ajax',
												url: 'api/v1/resource/lookuptypes'
											}
										},
										columns: [
											{ text: 'Table', dataIndex: 'code', width: 175 },
											{ text: 'Description', dataIndex: 'description', flex: 1}
										],
										viewConfig: {
											enableTextSelection: true
										},
										listeners: {
											itemclick: function(grid, record, item, index, e, opts) {
												Ext.getCmp('lookupTableDetailGrid').getStore().load({
													url: 'api/v1/resource/lookuptypes/' + record.get('code')
												});
												Ext.getCmp('lookupTableDetailGrid').setTitle(record.get('code') + ' - ' + record.get('description'));
											}
										}
									}
								]
							},
							{
								region: 'center',
								id: 'lookupTableDetailGrid',
								xtype: 'grid',
								title: 'Values',
								columnLines: true,
								viewConfig: {
									enableTextSelection: true
								},								
								store: {
									sorters: [
										new Ext.util.Sorter({
											property: 'description',
											direction: 'ASC'
										})
									],
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/lookuptypes'
									}
								},
								columns: [
									{ text: 'Code', dataIndex: 'code', width: 200 },
									{ text: 'Description', dataIndex: 'description', flex: 1, minWidth: 200 }									
								]
							}
						]
					});					
					Ext.getCmp('mainContent').removeAll();
					Ext.getCmp('mainContent').add(panel);
				};
				
				var loadAttributeTables = function() {
					var panel = Ext.create('Ext.panel.Panel', {
						layout: 'border',
						items: [
							{
								region: 'west',
								title: 'Attributes',
								layout: 'fit',
								width: 275,
								split: true,
								items: [
									{
										xtype: 'grid',
										columnLines: true,
										store: {
											autoLoad: true,
											proxy: {
												type: 'ajax',
												url: 'api/v1/resource/attributes/attributetypes',
												reader: {
													type: 'json',
													rootProperty: 'data'
												}
											}
										},
										columns: [
											{ text: 'Attribute', dataIndex: 'description', flex: 1}
										],
										viewConfig: {
											enableTextSelection: true
										},
										listeners: {
											itemclick: function(grid, record, item, index, e, opts) {
												Ext.getCmp('attributeTableDetailGrid').getStore().load({
													url: 'api/v1/resource/attributes/attributetypes/'+ record.get('attributeType') + '/attributecodes' 
												});
												Ext.getCmp('attributeTableDetailGrid').setTitle(record.get('description'));
											}
										}
									}
								]
							},
							{
								region: 'center',
								id: 'attributeTableDetailGrid',
								xtype: 'grid',
								title: 'Values',
								columnLines: true,
								viewConfig: {
									enableTextSelection: true
								},								
								store: {									
									fields: [
										{ name: 'code', mapping: function(data) {
												return data.attributeCodePk.attributeCode;
											}
										}
									],
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/attributes/attributetypes'
									}
								},
								columns: [
									{ text: 'Code', dataIndex: 'code', width: 200 },
									{ text: 'Label', dataIndex: 'label', width: 300 },
									{ text: 'Description', dataIndex: 'description', flex: 1, minWidth: 200 }									
								]
							}
						]
					});					
					Ext.getCmp('mainContent').removeAll();
					Ext.getCmp('mainContent').add(panel);					
				};
				
				var loadFrame = function(page) {
					
					var frame = Ext.create('OSF.ux.IFrame', {
					});
					Ext.getCmp('mainContent').removeAll();
					Ext.getCmp('mainContent').add(frame);
					frame.load(page);
				};

				var handleHistory = function(historyToken) {
					if (historyToken) {
						var historyParts = historyToken.split('=');
						if (historyParts[0] === 'loadPage') {
							if (historyParts.length == 2){
								loadPage(historyParts[1]);
							} else {
								loadPage('', historyParts[2]);
							}
						} else if (historyParts[0] === 'loadService') {
							loadService(historyParts[1]);
						} else if (historyParts[0] === 'loadServiceResource') {
							loadService(historyParts[1], 'service');							
						} else if (historyParts[0] === 'loadLookupTables') {
							loadLookupTables();
						} else if (historyParts[0] === 'loadAttributeTables') {
							loadAttributeTables();
						} else if (historyParts[0] === 'loadFrame') {	
							loadFrame(historyParts[1]);
						} else {
							loadPage('intro');
						}
					} else {
						loadPage('intro');
					}
				};
				
				
			});
		</script>		
    </body>
</html>
