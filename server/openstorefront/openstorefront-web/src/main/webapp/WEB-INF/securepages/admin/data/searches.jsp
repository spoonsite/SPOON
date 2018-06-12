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

    Document   : searches
    Created on : Mar 21, 2016, 11:08:45 AM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
		
		<stripes:layout-render name="../../../../layout/adminheader.jsp">		
		</stripes:layout-render>		
		
		<script src="scripts/component/advancedSearch.js?v=${appVersion}" type="text/javascript"></script>
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function() {

	
				var createSearchWindow = function() {
					var advanceSearch = Ext.create('OSF.component.AdvancedSearchPanel', {	
						itemId: 'advanceSearch'
					});
					
					var searchWindow = Ext.create('Ext.window.Window', {						
						title: 'Add/Edit Search',
						iconCls: 'fa fa-lg fa-search-plus',
						closeAction: 'destroy',
						modal: true,
						width: '80%',
						height: '80%',					
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
										autoEl: {
											'data-test': 'saveSearchCritBtn'
										},
										iconCls: 'fa fa-lg fa-save icon-button-color-save',
										handler: function(){
											actionSaveSearch();
										}
									},
									{
										xtype: 'tbfill'
									},								
									{
										text: 'Preview Results',
										iconCls: 'fa fa-lg fa-eye icon-button-color-view',
										handler: function(){
											advanceSearch.previewResults();
										}							
									},

									{
										xtype: 'tbfill'
									},
									{
										text: 'Cancel',
										iconCls: 'fa fa-lg fa-close icon-button-color-warning',
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
															searchWindow.close();
														} 
													}
												});											
											} else {
												searchWindow.close();
											}
										}
									}
								]
							}
						]					
					});

					var actionSaveSearch = function() {
						var searchPanel = advanceSearch;
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
							url: 'api/v1/resource/systemsearches' + endUrl,
							method: method,
							jsonData: search,
							callback: function() {
								searchPanel.setLoading(false);
							},
							success: function(response, opts){
								Ext.toast("Successfully saved search.");
								actionRefresh();
								searchWindow.close();
							}
						});
					};
					return searchWindow;
				};
					
				var searchGrid = Ext.create('Ext.grid.Panel', {
					id: 'searchgrid',
					title: 'Public Saved Searches <i class="fa fa-question-circle"  data-qtip="Allows for saving searches to be used in highlights and entries" ></i>',
					columnLines: true,
					store: {
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/systemsearches',							
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
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
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
									width: '100px',
									autoEl: {
										"data-test": "addBtnSearches"
									},
									iconCls: 'fa fa-2x fa-plus icon-button-color-save',
									requiredPermissions: ['ADMIN-SEARCH-CREATE'],
									handler: function () {
										actionAdd();										
									}									
								},								
								{
									text: 'Edit',
									itemId: 'edit',
									scale: 'medium',
									width: '100px',
									disabled: true,
									autoEl: {
										"data-test": "editBtnSearches"
									},
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
									requiredPermissions: ['ADMIN-SEARCH-UPDATE'],
									handler: function () {
										actionEdit(Ext.getCmp('searchgrid').getSelectionModel().getSelection()[0]);										
									}									
								},								
								{
									requiredPermissions: ['ADMIN-SEARCH-UPDATE'],
									xtype: 'tbseparator'
								},
								{
									text: 'Toggle Status',
									itemId: 'togglestatus',
									autoEl: {
										"data-test": "toggleBtnSearches"
									},
									disabled: true,
									scale: 'medium',									
									iconCls: 'fa fa-2x fa-power-off icon-button-color-default icon-vertical-correction',
									requiredPermissions: ['ADMIN-SEARCH-UPDATE'],
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
						url: 'api/v1/resource/systemsearches',
						params: {
							status: Ext.getCmp('filterActiveStatus').getValue()
						}
					});
				};
				
				var actionAdd = function() {
					var searchWindow = createSearchWindow();
					searchWindow.show();
					var advanceSearch = searchWindow.queryById('advanceSearch');
					advanceSearch.reset();
					advanceSearch.searchId = null;
					searchWindow.getComponent('searchName').reset();
					advanceSearch.changed = false;					
				};
				
				var actionEdit = function(record) {
					var searchWindow = createSearchWindow();
					var searchRequest = Ext.decode(record.get('searchRequest'));
					searchWindow.show();
					var advanceSearch = searchWindow.queryById('advanceSearch');					
					advanceSearch.searchId = record.get('searchId');
					advanceSearch.edit(searchRequest.searchElements);					
					searchWindow.getComponent('searchName').setValue(record.get('searchName'));
					advanceSearch.changed = false;
					
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
						url: 'api/v1/resource/systemsearches/' + record.get('searchId') + endUrl,
						method: method,
						callback: function(){
							Ext.getCmp('searchgrid').setLoading(false);
						},
						success: function(){
							actionRefresh();
						}
					});			
			
				};
						
				addComponentToMainViewPort(searchGrid);
				
			});
			
		</script>			
		
	</stripes:layout-component>
</stripes:layout-render>	