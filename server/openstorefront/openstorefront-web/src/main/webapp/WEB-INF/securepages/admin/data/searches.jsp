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

    Document   : searches
    Created on : Mar 21, 2016, 11:08:45 AM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
    <stripes:layout-component name="contents">
		
		<script src="scripts/component/advanceSearch.js?v=${appVersion}" type="text/javascript"></script>
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function() {
				
				var advanceSearch = Ext.create('OSF.component.AdvanceSearchPanel', {
					id: 'advanceSearch'
				});
	
				var searchWindow = Ext.create('Ext.window.Window', {
					id: 'searchWindow',
					title: 'Add/Edit Search',
					iconCls: 'fa fa-search-plus',
					modal: true,
					width: '80%',
					hieght: '80%',
					y: 50,
					maximizable: true,
					scrollable: true,
					layout: 'card',
					items: [
						advanceSearch
					],
					dockedItems: [
						{
							xtype: 'textfield',
							id: 'searchName',
							width: '100%',
							name: 'searchName',
							allowBlank: false,
							maxLength: 255,
							margin: '10 10 0 10',
							labelAlign: 'top',
							fieldLabel: 'Search Name <span class="field-required" />',
							labelSeparator: '',							
							listeners: {
								change: function(field, newValue, oldvalue, opts){										
									advanceSearch.changed = true;
								}
							}							
						},
						{
							html: '<hr>',
							dock: 'top'
						},
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [
								{
									text: 'Save',
									iconCls: 'fa fa-save',
									handler: function(){
										actionSaveSearch();
									}
								},
								{
									xtype: 'tbfill'
								},								
								{
									text: 'Preview Results',
									iconCls: 'fa fa-eye',
									handler: function(){
										Ext.getCmp('advanceSearch').previewResults();
									}							
								},

								{
									xtype: 'tbfill'
								},
								{
									text: 'Cancel',
									iconCls: 'fa fa-close',
									handler: function () {
										if (advanceSearch.changed) {
											Ext.Msg.show({
												title:'Save Changes?',
												message: 'You are closing a search that has unsaved changes. Would you like to save your changes?',
												buttons: Ext.Msg.YESNOCANCEL,
												icon: Ext.Msg.QUESTION,
												fn: function(btn) {
													if (btn === 'yes') {
														actionSaveSearch();														
													} else if (btn === 'no') {
														Ext.getCmp('searchWindow').close();
													} 
												}
											});											
										} else {
											Ext.getCmp('searchWindow').close();
										}
									}
								}
							]
						}
					]					
				});
				
				var actionSaveSearch = function() {
					var searchPanel = Ext.getCmp('advanceSearch');
					var search = searchPanel.getSearch();
					var searchName = Ext.getCmp('searchName').getValue();

					//check name and search elements
					if (!searchName || searchName === '') {
							Ext.Msg.show({
								title:'Validation',
								message: 'Please enter Search Name.',
								buttons: Ext.Msg.OK,
								icon: Ext.Msg.ERROR,
								fn: function(btn) {							
								}	
							});
						return;
					}

					if (!search) {
						Ext.Msg.show({
							title:'Validation',
							message: 'Enter at least one Search Criteria',
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.ERROR,
							fn: function(btn) {							
							}	
						});											
						return;
					}

					var search = {
						searchName: searchName,
						searchRequest: Ext.encode(search)
					};

					var method = 'POST';
					var endUrl = '';

					if (searchPanel.searchId) {
						method = 'PUT';
						endUrl = '/' +searchPanel.searchId;
					}

					searchPanel.setLoading('Saving...');
					Ext.Ajax.request({
						url: '../api/v1/resource/systemsearches' + endUrl,
						method: method,
						jsonData: search,
						callback: function() {
							searchPanel.setLoading(false);
						},
						success: function(response, opts){
							Ext.toast("Successfully saved search.");
							actionRefresh();
							Ext.getCmp('searchWindow').close();
						}
					});
				};
					
				var searchGrid = Ext.create('Ext.grid.Panel', {
					id: 'searchgrid',
					title: 'Public Saved Searches <i class="fa fa-question-circle"  data-qtip="Allows for saving searches to be used in highlights and entries" ></i>',
					columnLines: true,
					store: {
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: '../api/v1/resource/systemsearches',							
							reader: {
								type: 'json',
								rootProperty: 'data'
							}
						}
					},
					columns: [
						{ text: 'Search', dataIndex: 'searchName', flex: 1, minWidth: 200,
							renderer: function(value, meta) {
								meta.tdStyle = 'font-size: 16px';
								return value;
							}
						}
					],
					listeners: {
						itemdblclick: function(grid, record, item, index, e, opts){
							actionEdit(record);
						},						
						selectionchange: function(selModel, selected, opts) {
							var tools = Ext.getCmp('searchgrid').getComponent('tools');
							
							if (selected.length > 0) {	
								tools.getComponent('edit').setDisabled(false);
								tools.getComponent('togglestatus').setDisabled(false);
							} else {
								tools.getComponent('edit').setDisabled(true);
								tools.getComponent('togglestatus').setDisabled(true);
							}
						}
					},
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								Ext.create('OSF.component.StandardComboBox', {
									id: 'filterActiveStatus',									
									emptyText: 'Active',
									value: 'A',
									fieldLabel: 'Active Status',
									name: 'activeStatus',									
									typeAhead: false,
									editable: false,
									width: 200,							
									listeners: {
										change: function(filter, newValue, oldValue, opts){
											actionRefresh();
										}
									},
									storeConfig: {
										customStore: {
											fields: [
												'code',
												'description'
											],
											data: [												
												{
													code: 'A',
													description: 'Active'
												},
												{
													code: 'I',
													description: 'Inactive'
												}
											]
										}
									}
								})								
							]
						},
						{
							xtype: 'toolbar',
							itemId: 'tools',
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
									itemId: 'add',
									scale: 'medium',
									
									iconCls: 'fa fa-2x fa-plus',
									handler: function () {
										actionAdd();										
									}									
								},								
								{
									text: 'Edit',
									itemId: 'edit',
									scale: 'medium',
									disabled: true,
									iconCls: 'fa fa-2x fa-edit',
									handler: function () {
										actionEdit(Ext.getCmp('searchgrid').getSelectionModel().getSelection()[0]);										
									}									
								},								
								{
									xtype: 'tbfill'
								},
								{
									text: 'Toggle Status',
									itemId: 'togglestatus',
									disabled: true,
									scale: 'medium',									
									iconCls: 'fa fa-2x fa-power-off',
									handler: function() {
										actionToggleStatus(Ext.getCmp('searchgrid').getSelectionModel().getSelection()[0]);	
									}
								}
							]
						}						
					]
				});		
				
				var actionRefresh = function() {
					Ext.getCmp('searchgrid').getStore().load({
						url: '../api/v1/resource/systemsearches',
						params: {
							status: Ext.getCmp('filterActiveStatus').getValue()
						}
					});
				};
				
				var actionAdd = function() {
					searchWindow.show();
					advanceSearch.reset();
					advanceSearch.searchId = null;
					searchWindow.getComponent('searchName').reset();
				};
				
				var actionEdit = function(record) {
					var searchRequest = Ext.decode(record.get('searchRequest'));
					searchWindow.show();
					advanceSearch.searchId = record.get('searchId');
					advanceSearch.edit(searchRequest.searchElements);
					searchWindow.getComponent('searchName').setValue(record.get('searchName'));
				};

				var actionToggleStatus = function(record) {
					
					var method = 'DELETE';
					var endUrl = '';
					if (record.get('activeStatus') === 'I') {
						method = 'PUT';
						endUrl = '/activate';
					} 
					
					Ext.getCmp('searchgrid').setLoading('Updating status...');
					Ext.Ajax.request({
						url: '../api/v1/resource/systemsearches/' + record.get('searchId') + endUrl,
						method: method,
						callback: function(){
							Ext.getCmp('searchgrid').setLoading(false);
						},
						success: function(){
							actionRefresh();
						}
					});			
			
				};
						
				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [
						searchGrid
					]
				});				
				
			});
			
		</script>			
		
	</stripes:layout-component>
</stripes:layout-render>	