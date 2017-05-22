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
<stripes:layout-render name="../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
		
		<stripes:layout-render name="../../../layout/userheader.jsp">		
		</stripes:layout-render>			
		
		<script src="scripts/component/advancedSearch.js?v=${appVersion}" type="text/javascript"></script>
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function() {
				
				var advanceSearch = Ext.create('OSF.component.AdvancedSearchPanel', {
					id: 'advanceSearch'
				});
	
				var searchWindow = Ext.create('Ext.window.Window', {
					id: 'searchWindow',
					title: 'Add/Edit Search',
					iconCls: 'fa fa-lg fa-search-plus',
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
							itemId: 'searchName',
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
										Ext.getCmp('advanceSearch').previewResults();
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

					var userSearch = {
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
						url: 'api/v1/resource/usersavedsearches' + endUrl,
						method: method,
						jsonData: userSearch,
						callback: function() {
							searchPanel.setLoading(false);
						},
						success: function(response, opts){
							Ext.toast("Save Search Successfully.");
							actionRefresh();
							Ext.getCmp('searchWindow').close();
						}
					});					
				};
	
				
				var searchGrid = Ext.create('Ext.grid.Panel', {					
					id: 'searchgrid',
					title: 'Saved Searches <i class="fa fa-lg fa-question-circle"  data-qtip="Allows for managing saved searches" ></i>',
					columnLines: true,
					store: {
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/usersavedsearches/user/current'
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
								tools.getComponent('delete').setDisabled(false);
							} else {
								tools.getComponent('edit').setDisabled(true);
								tools.getComponent('delete').setDisabled(true);
							}
						}
					},
					dockedItems: [
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
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction',
									handler: function () {
										actionAdd();										
									}									
								},								
								{
									text: 'Edit',
									itemId: 'edit',
									scale: 'medium',
									disabled: true,
									width: '100px',
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
									handler: function () {
										actionEdit(Ext.getCmp('searchgrid').getSelectionModel().getSelection()[0]);										
									}									
								},								
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									itemId: 'delete',
									disabled: true,
									scale: 'medium',									
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									handler: function() {
										actionDelete(Ext.getCmp('searchgrid').getSelectionModel().getSelection()[0]);	
									}
								}
							]
						}						
					]
				});		
				
				var actionRefresh = function() {
					Ext.getCmp('searchgrid').getStore().reload();
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
					advanceSearch.searchId = record.get('userSearchId');
					advanceSearch.edit(searchRequest.searchElements);
					searchWindow.getComponent('searchName').setValue(record.get('searchName'));
				};

				var actionDelete = function(record) {
					Ext.Msg.show({
						title:'Delete Search?',
						iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
						message: 'Are you sure you want to delete this search?',
						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						fn: function(btn) {
							if (btn === 'yes') {
								Ext.getCmp('searchgrid').setLoading("Removing...");
								Ext.Ajax.request({
									url: 'api/v1/resource/usersavedsearches/' + record.get('userSearchId'),
									method: 'DELETE',
									callback: function(){
										Ext.getCmp('searchgrid').setLoading(false);
									},
									success: function(){
										actionRefresh();
									}
								});
							} 
						}
					});						
				};
						
				addComponentToMainViewPort(searchGrid);
				
			});
			
		</script>			
		
	</stripes:layout-component>
</stripes:layout-render>	