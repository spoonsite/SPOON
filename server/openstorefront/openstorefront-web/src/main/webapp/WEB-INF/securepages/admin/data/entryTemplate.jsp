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

    Document   : entryTemplate
    Created on : Mar 21, 2016, 2:43:11 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
	<stripes:layout-component name="contents">
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function(){			
				
				var getAllDataField = function(data, output, parentLevel) {
					
					Ext.Object.each(data, function(key, value, myself) {
						console.log(key + ":" + value);
						if (Ext.isArray(value)) {
							parentLevel++;
							Ext.Array.each(value, function(item){	
								output += '<b>' + key + '</b> [ Showing 1 Item ] <br>';
								Ext.Object.each(item, function(key, value, myself) {
										output += '&nbsp; &nbsp; -> <b>' + key + '</b> : ' + Ext.String.ellipsis(value, 150) + '<br>';
								});
								return false;
							});
						} else if (Ext.isObject(value)) {
							parentLevel++;
							//output += getAllDataField(value, output, parentLevel);
						} else {
							var spacer = '';
							for (var i=0; i<parentLevel; i++) {
								spacer += ' ';
							}
													
							output += spacer + '<b>' + key + '</b> : ' + Ext.String.ellipsis(Ext.util.Format.stripTags(value), 150) + '<br>';
						}																
					});
					return output;
				};
				
				var addEditWindow = Ext.create('Ext.window.Window', {
					id: 'addEditWindow',
					title: 'Add/Edit Template',
					iconCls: 'fa fa-file-o',
					modal: true,
					width: '80%',
					height: '80%',
					maximizable: true,
					layout: 'fit',
					items: [
						{
							xtype: 'panel',
							layout: {
								type: 'hbox',
								align: 'stretch'
							},
							items: [
								{
									xtype: 'panel',
									title: 'Toolbox',
									width: '40%',
									collapsible: true,
									titleCollapse: true,
									collapseDirection: 'left',
									split: true,
									scrollable: true,
									bodyStyle: 'padding: 10px;',
									layout: 'anchor',
									items: [
										{
											xtype: 'combobox',
											fieldLabel: 'Sample Data',
											labelAlign: 'top',
											name: 'entry',		
											width: '100%',
											queryMode: 'remote',
											emptyText: 'Select Entry',
											displayField: 'description',											
											valueField: 'code',
											store: {
												proxy: {
													type: 'ajax',
													url: '../api/v1/resource/components/lookup'
												}
											},
											listeners: {
												change: function(cb, newValue, oldValue, opts) {
													
													if (newValue) {
														Ext.getCmp('dataFieldPanel').setLoading(true);
														Ext.Ajax.request({
															url: '../api/v1/resource/components/' + newValue + '/detail',
															callback: function(){
																Ext.getCmp('dataFieldPanel').setLoading(false);
															},
															success: function(response, opts){
																var data = Ext.decode(response.responseText);

																var fieldDisplay = getAllDataField(data, '', 0);

																Ext.getCmp('dataFieldPanel').update(fieldDisplay);
															}
														});
													}
													
												}
											}
										},
										{
											xtype: 'panel',
											id: 'dataFieldPanel',
											title: 'Data Fields',
											border: true,	
											width: '100%',
											collapsible: true,
											titleCollapse: true,
											bodyStyle: 'padding: 10px;',
											margin: '0 0 10 0'											
										},
										{
											xtype: 'panel',
											title: 'Template Blocks',											
											width: '100%',
											collapsible: true,											
											titleCollapse: true,											
											bodyStyle: 'padding: 10px;',
											border: true											
										}
									]
								},
								{
									xtype: 'tabpanel',
									flex: 1,
									border: true,
									split: true,
									tabBar: {
										style: 'padding-top: 0px;'
									},
									items: [
										{
											xtype: 'panel',
											title: 'Visual Design'											
										},
										{
											xtype: 'panel',
											title: 'Code'											
										},
										{
											xtype: 'panel',
											title: 'Preview'											
										}										
									]
								}
							]
						}
					],
					dockedItems: [
						{
							xtype: 'textfield',							
							dock: 'top',
							name: 'name',
							fieldLabel: 'Name<span class="field-required" />',
							allowBlank: false,							
							margin: '10 10 10 10',
							maxLength: 255
						},
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [
								{
									text: 'Save/Continue',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-save',
									handler: function(){
										
									}
								},															
								{
									xtype: 'tbfill'
								},
								{
									text: 'Close',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-close',
									handler: function(){
										
									}
								}								
							]
						}
					]					
				});
				
				var templateGrid = Ext.create('Ext.grid.Panel', {
					id: 'templateGrid',
					title: 'Entry Templates <i class="fa fa-question-circle"  data-qtip="Allows for defining custom template for entries" ></i>',
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
							url: '../api/v1/resource/componenttypetemplates',
							extraParams: {
								all: true
							}							
						}						
					},
					columnLines: true,
					columns: [						
						{ text: 'Name', dataIndex: 'name', flex: 1, minWidth: 200 },
						{ text: 'Active Status', align: 'center', dataIndex: 'activeStatus', width: 150 },
						{ text: 'Create User', dataIndex: 'createUser', width: 150, hidden: true },
						{ text: 'Create Date', dataIndex: 'createDts', width: 150, hidden: true, xtype: 'datecolumn', format:'m/d/y H:i:s' },						
						{ text: 'Update User', dataIndex: 'updateUser', width: 150 },
						{ text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' }
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
									handler: function () {
										actionRefresh();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Add',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-plus',
									handler: function () {
										actionAdd();
									}
								},
								{
									xtype: 'tbseparator'
								}, 								
								{
									text: 'Edit',
									id: 'lookupGrid-tools-edit',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-edit',
									disabled: true,
									handler: function () {
										actionEdit(Ext.getCmp('templateGrid').getSelection()[0]);
									}								
								},
								{
									xtype: 'tbfill'
								},								
								{
									text: 'Toggle Status',
									id: 'lookupGrid-tools-status',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-power-off',
									disabled: true,
									handler: function () {
										actionToggleStatus();
									}								
								}								
							]
						}
					],
					listeners: {
						itemdblclick: function(grid, record, item, index, e, opts){
							actionEdit(record);
						},
						selectionchange: function(grid, record, index, opts){
							checkEntryGridTools();
						}
					}					
				});
				
				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [
						templateGrid
					]
				});				

				var checkEntryGridTools = function() {
					if (Ext.getCmp('templateGrid').getSelectionModel().getCount() === 1) {
						Ext.getCmp('lookupGrid-tools-edit').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-status').setDisabled(false);
					} else {
						Ext.getCmp('lookupGrid-tools-edit').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-status').setDisabled(true);						
					}
				};
				
				var actionRefresh = function() {
					Ext.getCmp('templateGrid').getStore().reload();
				};

				var actionAdd = function() {
					addEditWindow.show();
					
				};

				var actionEdit = function() {
					addEditWindow.show();
					
				};				
				
				var actionToggleStatus = function() {
					Ext.getCmp('templateGrid').setLoading("Updating Status...");
					var type = Ext.getCmp('templateGrid').getSelection()[0].get('templateId');
					var currentStatus = Ext.getCmp('templateGrid').getSelection()[0].get('activeStatus');
					
					var method = 'PUT';
					var urlEnd = '/activate';
					if (currentStatus === 'A') {
						method = 'DELETE';
						urlEnd = '';
					}					
					Ext.Ajax.request({
						url: '../api/v1/resource/componenttypetemplates/' + type + urlEnd,
						method: method,
						callback: function(){
							Ext.getCmp('templateGrid').setLoading(false);
						},
						success: function(response, opts){
							actionRefreshEntryGrid();
						}
					});					
				};				
				

			});
			
		</script>	
		
	</stripes:layout-component>
</stripes:layout-render>