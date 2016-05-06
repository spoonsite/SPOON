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

    Document   : contacts
    Created on : Apr 28, 2016, 3:44:49 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
    <stripes:layout-component name="contents">		
	
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function() {
				
				var viewReferenceWindow = Ext.create('Ext.window.Window', {
					id: 'viewReferenceWindow',
					title: 'References',
					iconCls: 'fa fa-chain',
					modal: true,
					width: '65%',
					height: '50%',
					maximizable: true,
					layout: 'fit',
					items: [						
						{
							xtype: 'grid',
							itemId: 'referenceGrid',
							store: {
								autoLoad: false,
								proxy: {
									type: 'ajax',
									url: '../api/v1/resource/contacts/{contactId}/references'
								}
							},
							columns: [
								{ text: 'Entry Name', dataIndex: 'componentName', flex: 1},
								{ text: 'Entry ID', dataIndex: 'componentId', width: 200, hidden: true}
							]
						}
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [
								{
									text: 'Previous',
									id: 'referenceWinTools-previousBtn',
									iconCls: 'fa fa-arrow-left',									
									handler: function() {
										actionReferencesNextRecord(false);
									}									
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Close',
									iconCls: 'fa fa-close',
									handler: function() {
										this.up('window').hide();
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Next',
									id: 'referenceWinTools-nextBtn',
									iconCls: 'fa fa-arrow-right',
									iconAlign: 'right',
									handler: function() {
										actionReferencesNextRecord(true);
									}									
								}
							]
						}
					]
				});
				
				var actionReferencesNextRecord = function(next) {
					if (next) {
						Ext.getCmp('contactGrid').getSelectionModel().selectNext();						
					} else {
						Ext.getCmp('contactGrid').getSelectionModel().selectPrevious();						
					}
					actionView(Ext.getCmp('contactGrid').getSelection()[0]);						
				};
				
				var previewCheckButtons = function() {	
					if (Ext.getCmp('contactGrid').getSelectionModel().hasPrevious()) {
						Ext.getCmp('referenceWinTools-previousBtn').setDisabled(false);
					} else {
						Ext.getCmp('referenceWinTools-previousBtn').setDisabled(true);
					}
					
					if (Ext.getCmp('contactGrid').getSelectionModel().hasNext()) {
						Ext.getCmp('referenceWinTools-nextBtn').setDisabled(false);
					} else {
						Ext.getCmp('referenceWinTools-nextBtn').setDisabled(true);
					}					
				};				
				
				var addEditWindow = Ext.create('Ext.window.Window', {
					id: 'addEditWindow',
					title: 'Add/Edit',
					iconCls: 'fa fa-edit',
					modal: true,
					width: '65%',
					height: 410,
					maximizable: true,
					layout: 'fit',
					items: [
						{
							xtype: 'form',
							itemId: 'contactForm',
							layout: 'anchor',
							scrollable: 'true',
							bodyStyle: 'padding: 10px;',						
							defaults: {
								labelAlign: 'top',
								labelSeparator: '',
								width: '100%'
							},
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										{											
											text: 'Save',
											formBind: true,											
											iconCls: 'fa fa-save',
											handler: function(){	
												var win = this.up('window');
												var form = this.up('form');
												var data = form.getValues();

												var method = 'POST';
												var update = '';
												if (data.contactId) {
													update = '/' + data.contactId;
													method = 'PUT';
												}

												CoreUtil.submitForm({
													url: '../api/v1/resource/contacts' + update,
													method: method,
													data: data,
													form: form,
													success: function(){
														actionRefresh();
														form.reset();
														win.close();
													}
												});
											}
										},
										{
											xtype: 'tbfill'
										},
										{
											xtype: 'button',
											text: 'Cancel',										
											iconCls: 'fa fa-close',
											handler: function(){
												this.up('form').reset();
												this.up('window').close();
											}									
										}											
									]
								}
							],
							items: [
								{
									xtype: 'hidden',
									name: 'contactId'
								},
								Ext.create('OSF.component.StandardComboBox', {
									name: 'organization',									
									allowBlank: false,									
									margin: '0 0 10 0',
									width: '100%',
									fieldLabel: 'Organization <span class="field-required" />',
									forceSelection: false,
									valueField: 'description',
									storeConfig: {
										url: '../api/v1/resource/organizations/lookup'
									}
								}),								
								{
									xtype: 'textfield',
									fieldLabel: 'First Name <span class="field-required" />',									
									allowBlank: false,								
									maxLength: '80',
									name: 'firstName'
								},								
								{
									xtype: 'textfield',
									fieldLabel: 'Last Name <span class="field-required" />',									
									allowBlank: false,								
									maxLength: '80',
									name: 'lastName'
								},
								{
									xtype: 'textfield',
									fieldLabel: 'Email',																																	
									maxLength: '255',
									regex: new RegExp("[a-z0-9!#$%&'*+/=?^_`{|}~.-]+@[a-z0-9-]+(\.[a-z0-9-]+)*", "i"),
									regexText: 'Must be a valid email address. Eg. xxx@xxx.xxx',
									name: 'email'
								},
								{
									xtype: 'textfield',
									fieldLabel: 'Phone',																											
									maxLength: '120',
									name: 'phone'
								},
								Ext.create('OSF.component.SecurityComboBox', {	
									hidden: !${branding.allowSecurityMarkingsFlg}
								})								
							]
						}
					]					
				});
				
				
				var contactGrid = Ext.create('Ext.grid.Panel', {
					id: 'contactGrid',
					title: 'Contact Management <i class="fa fa-question-circle" data-qtip="Allows for managing contacts in one place for all entries." ></i>',
					columnLines: true,
					store: {
						autoLoad: true,
						id: 'contactGridStore',
						pageSize: 250,
						remoteSort: true,
						sorters: [
							new Ext.util.Sorter({
								property: 'lastName',
								direction: 'ASC'
							})
						],	
						fields: [
							{ name: 'createDts', 
								type: 'date',
								dateFormat: 'c'							
							},
							{ name: 'updateDts',
								type: 'date',
								dateFormat: 'c'				
							}							
						],
						proxy: CoreUtil.pagingProxy({
							type: 'ajax',
							url: '../api/v1/resource/contacts',
							reader: {
								type: 'json',
								rootProperty: 'data',
								totalProperty: 'totalNumber'
							}
						}),
						listeners: {
							beforeLoad: function(store, operation, eOpts){
								store.getProxy().extraParams = {
									status: Ext.getCmp('filterActiveStatus').getValue()									
								};
							}
						}							
					},
					columns: [					
						{ text: 'First Name', dataIndex: 'firstName', width: 200 },
						{ text: 'Last Name', dataIndex: 'lastName', width: 200 },
						{ text: 'Organization', dataIndex: 'organization', flex: 1, minWidth: 200 },
						{ text: 'Email', dataIndex: 'email', width: 200 },
						{ text: 'Phone', dataIndex: 'phone', width: 200 },
						{ text: 'Active Status', align: 'center', dataIndex: 'activeStatus', width: 150 },
						{ text: 'Contact Id', dataIndex: 'contactId', width: 200, hidden: true },
						{ text: 'Create User', dataIndex: 'createUser', width: 200, hidden: true },
						{ text: 'Create Date', dataIndex: 'createDts', width: 200, hidden: true },
						{ text: 'Update User', dataIndex: 'updateUser', width: 200, hidden: true },
						{ text: 'Update Date', dataIndex: 'updateDts', width: 200, hidden: true }
					],
					listeners: {
						itemdblclick: function(grid, record, item, index, e, opts){
							actionEdit(record);
						},						
						selectionchange: function(selModel, selected, opts) {
							var tools = Ext.getCmp('contactGrid').getComponent('tools');
							
							if (selected.length > 0) {	
								tools.getComponent('view').setDisabled(false);
								tools.getComponent('edit').setDisabled(false);
								tools.getComponent('merge').setDisabled(false);								
								tools.getComponent('togglestatus').setDisabled(false);
								tools.getComponent('delete').setDisabled(false);
							} else {
								tools.getComponent('view').setDisabled(true);
								tools.getComponent('edit').setDisabled(true);
								tools.getComponent('merge').setDisabled(true);
								tools.getComponent('togglestatus').setDisabled(true);
								tools.getComponent('delete').setDisabled(true);
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
										actionEdit(Ext.getCmp('contactGrid').getSelectionModel().getSelection()[0]);										
									}									
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'View References',
									itemId: 'view',
									scale: 'medium',
									disabled: true,
									iconCls: 'fa fa-2x fa-eye',
									handler: function () {
										actionView(Ext.getCmp('contactGrid').getSelectionModel().getSelection()[0]);										
									}									
								},
								{
									xtype: 'tbseparator'
								},								
								{
									text: 'Merge',
									itemId: 'merge',
									scale: 'medium',
									disabled: true,
									iconCls: 'fa fa-2x fa-compress',
									handler: function () {
										actionMerge(Ext.getCmp('contactGrid').getSelectionModel().getSelection()[0]);										
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
										actionToggleStatus(Ext.getCmp('contactGrid').getSelectionModel().getSelection()[0]);	
									}
								},
								{
									xtype: 'tbseparator'
								},									
								{
									text: 'Delete',
									itemId: 'delete',
									disabled: true,
									scale: 'medium',									
									iconCls: 'fa fa-2x fa-trash-o',
									handler: function() {
										actionDelete(Ext.getCmp('contactGrid').getSelectionModel().getSelection()[0]);	
									}
								}								
							]
						},
						{
							xtype: 'pagingtoolbar',
							dock: 'bottom',
							store: 'contactGridStore',
							displayInfo: true
						}					
					]					
				});
				
				var actionRefresh = function() {
					Ext.getCmp('contactGrid').getStore().load({
						url: '../api/v1/resource/contacts'
					});					
				};
				
				var actionView = function(record) {
					viewReferenceWindow.show();
					viewReferenceWindow.getComponent('referenceGrid').getStore().load({
						url: '../api/v1/resource/contacts/' + record.get('contactId') + '/references'
					});
					previewCheckButtons();
				};				
				
				var actionAdd = function() {
					addEditWindow.show();
					addEditWindow.getComponent('contactForm').reset();
					
				};

				var actionEdit = function(record) {
					addEditWindow.show();
					addEditWindow.getComponent('contactForm').reset();
					addEditWindow.getComponent('contactForm').loadRecord(record);
				};
		
				var actionMerge = function(record) {
					
					var mergeWindow = Ext.create('Ext.window.Window', {
						closeMethod: 'destroy',
						title: 'Merge',
						iconCls: 'fa fa-compress',
						modal: true,
						width: 400,
						height: 175,
						y: 200,
						layout: 'fit',
						items: [
							{
								xtype: 'form',								
								bodyStyle: 'padding: 10px',
								layout: 'anchor',
								items: [
									{
										xtype: 'panel',
										data: record,
										tpl: '{firstName}, {lastName} ({email})'
									},
									{
										xtype: 'combobox',
										itemId: 'mergeContactId',
										name: 'mergeContactId',
										width: '100%',
										fieldLabel: 'Merge Contact <span class="field-required" />',
										labelAlign: 'top',
										labelSeparator: '',
										valueField: 'contactId',
										displayField: 'display',										
										editable: false,
										typeAhead: false,										
										allowBlank: false,
										queryMode: 'remote',										
										listConfig: {
											itemTpl: [
												 '{firstName} <span style="color: grey">({email})</span>'
											]
										},
										store: {
											fields: [
												{ 
													name: 'display',
													mapping: function(data) {
														return data.firstName + ' - (' + data.email + ')';
													}
												}
											],
											proxy: {
												type: 'ajax',
												url: '../api/v1/resource/contacts/filtered'
											}
										}
									}
								],
								dockedItems: [
									{
										xtype: 'toolbar',
										dock: 'bottom',
										items: [
											{
												text: 'Save',
												formBind: true,
												iconCls: 'fa fa-save',
												handler: function() {
													var form = this.up('form');
													var win = this.up('window');
													var mergeContactId = form.getComponent('mergeContactId').getValue();
													
													CoreUtil.submitForm({
														url: '../api/v1/resource/contacts/' + record.get('contactId') + '/merge/' + mergeContactId,
														method: 'PUT',
														form: form,
														success: function(){
															win.close();
															actionRefresh();
														}														
													});
												}
											},
											{
												xtype: 'tbfill'
											},
											{
												text: 'Cancel',
												iconCls: 'fa fa-close',
												handler: function() {
													this.up('window').close();
												}												
											}
										]
									}
								]
							}
						]
					});
					mergeWindow.show();
				};

				var actionToggleStatus = function(record) {
					
					var newStatus = 'inactivate';
					var urlEnding = 'inactivate';
					if (record.get('activeStatus') === 'I') {
						newStatus = 'activate';
						urlEnding = 'activate';
					}
					
					Ext.Msg.show({
						title:'Update Status',
						message: 'Do you want to ' + newStatus + ' entry contact reference as well?',
						buttons: Ext.Msg.YESNOCANCEL,
						icon: Ext.Msg.QUESTION,
						fn: function(btn) {
							var proceed = true;
							var includeReferences = false;
							if (btn === 'yes') {
								includeReferences = true;
							} else if (btn === 'no') {
								includeReferences = false;							
							} else {
								proceed = false;
							} 
							
							if (proceed) {
								contactGrid.setLoading('Updating Status...');
								Ext.Ajax.request({
									url: '../api/v1/resource/contacts/' + record.get('contactId') + '/' + urlEnding,
									method: 'PUT',
									params: {
										includeReferences : includeReferences
									},									
									callback: function(){
										contactGrid.setLoading(false);
									},
									success: function(response, opts){
										actionRefresh();
									}
								});
							}
						}
					});
				};
		
				var actionDelete = function(record) {					
					//check references
					contactGrid.setLoading('Checking for references...');
					Ext.Ajax.request({
						url: '../api/v1/resource/contacts/' + record.get('contactId') + '/references',
						callback: function(){
							contactGrid.setLoading(false);
						},
						success: function(response, opts){
							var references = Ext.decode(response.responseText);
							
							if (references.length > 0) {
								Ext.Msg.alert('Reference', 'Unable to delete; Remove references first.');
							} else {
							
								Ext.Msg.show({
									title:'Delete Contact',
									message: 'Are you want to delete contact: ' + record.get('firstName') + ' ?',
									buttons: Ext.Msg.YESNO,
									icon: Ext.Msg.QUESTION,
									fn: function(btn) {
										if (btn === 'yes') {										
											contactGrid.setLoading('Deleting Contact...');
											Ext.Ajax.request({
												url: '../api/v1/resource/contacts/' + record.get('contactId'),
												method: 'DELETE',
												callback: function(){
													contactGrid.setLoading(false);
												},
												success: function(response, opts){
													actionRefresh();
												}
											});											
										}
									}
								});
							}
						}
					});
					
				};
				
				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [
						contactGrid
					]
				});					
				
			});
			
		</script>			
		
	</stripes:layout-component>
</stripes:layout-render>								