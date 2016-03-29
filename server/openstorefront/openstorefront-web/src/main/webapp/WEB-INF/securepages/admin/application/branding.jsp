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
    Document   : branding
    Created on : Mar 29, 2016, 4:31:55 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
    <stripes:layout-component name="contents">

        <script type="text/javascript">
            /* global Ext, CoreUtil */
            
		Ext.onReady(function () {
				
			var brandingGrid = Ext.create('Ext.grid.Panel', {
				title: 'Manage Branding <i class="fa fa-question-circle"  data-qtip="This tool allows the ability to set the graphic design and theme characteristics for the site." ></i>',
				id: 'brandingGrid',
				columnLines: true,
				store: {
					fields: [
						{
							name: 'createDts',
							type:	'date',
							dateFormat: 'c'
						},								
						{
							name: 'updateDts',
							type:	'date',
							dateFormat: 'c'
						}							
					],					
					autoLoad: true,
					proxy: {
						type: 'ajax',
						url: '../api/v1/resource/branding/'
					}
				},
				columns: [
					{ text: 'Name', dataIndex: 'name', minWidth: 200, flex: 1 },
					{ text: 'Application Name', dataIndex: 'applicationName', width: 200 },
					{ text: 'Status', align: 'center', dataIndex: 'activeStatus', width: 150},
					{ text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s'},
					{ text: 'Update User', dataIndex: 'updateUser', width: 150},
					{text: 'Create Date', dataIndex: 'createDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s', hidden: true},
					{text: 'Create User', dataIndex: 'createUser', width: 150, hidden: true}					
				],
				dockedItems: [
					{
						xtype: 'toolbar',
						dock: 'top',
						items: [
							{
								text: 'Refresh',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-refresh',
								hander: function() {
									
								}
							},
							{
								text: 'Add',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-plus',
								hander: function() {
									
								}
							},
							{
								text: 'Edit',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-edit',
								disabled: true,
								hander: function() {
									
								}
							},	
							{
								xtype: 'tbseparator'
							},
							{
								text: 'Activate',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-power-off',
								disabled: true,
								hander: function() {
									
								}
							},	
							{
								xtype: 'tbfill'
							},
							{
								text: 'Delete',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-power-off',
								disabled: true,
								hander: function() {
									
								}
							}						
						]
					}
				]
			});
			
			Ext.create('Ext.container.Viewport', {
				layout: 'fit',
				items: [
					brandingGrid
				]
			});
			
			
		
		});

        </script>

    </stripes:layout-component>
</stripes:layout-render>