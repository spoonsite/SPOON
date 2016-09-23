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
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
	<stripes:layout-component name="contents">
		
		<stripes:layout-render name="../../../../layout/adminheader.jsp">		
		</stripes:layout-render>		
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function(){	
				
				var addEditWin = Ext.create('Ext.window.Window', {
					id: 'addEditWin',
					title: 'Entry',
					modal: true,
					width: '40%',
					height: '90%',
					layout: 'fit',
					items: [
						{
							xtype: 'form',
							id: 'entryForm',
							layout: 'vbox',
							autoScroll: true,
							scrollable: true,							
							bodyStyle: 'padding: 10px;',
							defaults: {
								labelAlign: 'top'
							},
							items: [
								{
									xtype: 'textfield',
									id: 'entryForm-type',
									fieldLabel: 'Type Code<span class="field-required" />',
									name: 'componentType',
									width: '100%',
									allowBlank: false,
									maxLength: 20									
								},
								{
									xtype: 'textfield',									
									fieldLabel: 'Label<span class="field-required" />',
									name: 'label',
									allowBlank: false,
									width: '100%',
									maxLength: 80																		
								},
								{
									xtype: 'htmleditor',
									name: 'description',
									fieldLabel: ' Description<span class="field-required" />',
									allowBlank: false,
									width: '100%',
									fieldBodyCls: 'form-comp-htmleditor-border',
									maxLength: 65536,
									margin: '0 0 30 0'
								},																
								{
									xtype: 'panel',
									html: '<b>Data Entry</b>'
								},
								{
									xtype: 'checkbox',
									boxLabel: '<b>Allow On Submission Form</b>',
									name: 'allowOnSubmission'
								},								
								{
									xtype: 'checkbox',
									boxLabel: 'Attributes',
									name: 'dataEntryAttributes'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Relationships ',
									name: 'dataEntryRelationships'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Contacts',
									name: 'dataEntryContacts'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Resources',
									name: 'dataEntryResources'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Media',
									name: 'dataEntryMedia'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Dependencies',
									name: 'dataEntryDependencies'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Metadata',
									name: 'dataEntryMetadata'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Evaluation Information',
									name: 'dataEntryEvaluationInformation'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Reviews',
									name: 'dataEntryReviews'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Questions',
									name: 'dataEntryQuestions'
								},								
								Ext.create('OSF.component.StandardComboBox', {
									name: 'componentTypeTemplate',																		
									width: '100%',
									margin: '0 0 0 0',
									fieldLabel: 'Override Template',
									emptyText: 'Default',
									storeConfig: {
										url: 'api/v1/resource/componenttypetemplates/lookup',
										addRecords: [
											{
												code: null,
												description: 'Default'
											} 
										]
									}
								})
							],
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										{
											text: 'Save',
											iconCls: 'fa fa-save',
											formBind: true,
											handler: function(){
												var method = Ext.getCmp('entryForm').edit ? 'PUT' : 'POST'; 												
												var data = Ext.getCmp('entryForm').getValues();
												var url = Ext.getCmp('entryForm').edit ? 'api/v1/resource/componenttypes/' + data.componentType : 'api/v1/resource/componenttypes';       

												CoreUtil.submitForm({
													url: url,
													method: method,
													data: data,
													removeBlankDataItems: true,
													form: Ext.getCmp('entryForm'),
													success: function(response, opts) {
														Ext.toast('Saved Successfully', '', 'tr');
														Ext.getCmp('entryForm').setLoading(false);
														Ext.getCmp('addEditWin').hide();													
														actionRefreshEntryGrid();												
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
											handler: function(){
												Ext.getCmp('addEditWin').close();
											}											
										}
									]
								}
							]
						}
					]
				});
				
				var entryGrid = Ext.create('Ext.grid.Panel', {
					id: 'entryGrid',
					title: 'Entry Types <i class="fa fa-question-circle"  data-qtip="Allows for defining entry types" ></i>',
					store: Ext.create('Ext.data.Store', {
						fields: [
							'componentType',
							'updateUser',							
							{
								name: 'updateDts',
								type:	'date',
								dateFormat: 'c'
							},							
							'activeStatus',
							'label',
							'description',
							'componentTypeTemplate'
						],
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/componenttypes',
							extraParams: {
								all: true
							}
						}
					}),
					columnLines: true,
					columns: [						
						{ text: 'Type Code', dataIndex: 'componentType', width: 125 },
						{ text: 'Label', dataIndex: 'label', width: 200 },
						{ text: 'Description', dataIndex: 'description', flex: 1, minWidth: 200 },
						{ text: 'Template Override', dataIndex: 'templateName', width: 150 },
						{ text: 'Active Status', align: 'center', dataIndex: 'activeStatus', width: 150 },
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
										actionRefreshEntryGrid();
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
										actionAddEntry();
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
										actionEditEntry(Ext.getCmp('entryGrid').getSelection()[0]);
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
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Remove',
									id: 'lookupGrid-tools-remove',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-close text-danger',
									disabled: true,
									handler: function () {
										actionRemoveType();
									}									
								}
							]
						}
					],
					listeners: {
						itemdblclick: function(grid, record, item, index, e, opts){
							actionEditEntry(record);
						},
						selectionchange: function(grid, record, index, opts){
							checkEntryGridTools();
						}
					}
				});
				
				addComponentToMainViewPort(entryGrid);
				
				var checkEntryGridTools = function() {
					if (Ext.getCmp('entryGrid').getSelectionModel().getCount() === 1) {
						Ext.getCmp('lookupGrid-tools-edit').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-status').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-remove').setDisabled(false);						
					} else {
						Ext.getCmp('lookupGrid-tools-edit').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-status').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-remove').setDisabled(true);
					}
				};
				
				var actionRefreshEntryGrid = function() {
					Ext.getCmp('entryGrid').getStore().load();				
				};
				
				var actionAddEntry = function() {
					addEditWin.show();
					
					//reset form
					Ext.getCmp('entryForm').reset(true);
					Ext.getCmp('entryForm').edit = false;
					Ext.getCmp('entryForm-type').setReadOnly(false);
				};
				
				var actionEditEntry = function(record) {
					addEditWin.show();
					
					Ext.getCmp('entryForm').reset(true);
					Ext.getCmp('entryForm').edit = true;
					
					//load form
					Ext.getCmp('entryForm').loadRecord(record);
					Ext.getCmp('entryForm-type').setReadOnly(true);
				};
				
				var actionToggleStatus = function() {
					Ext.getCmp('entryGrid').setLoading("Updating Status...");
					var type = Ext.getCmp('entryGrid').getSelection()[0].get('componentType');
					var currentStatus = Ext.getCmp('entryGrid').getSelection()[0].get('activeStatus');
					
					var method = 'PUT';
					var urlEnd = '/activate';
					if (currentStatus === 'A') {
						method = 'DELETE';
						urlEnd = '';
					}					
					Ext.Ajax.request({
						url: 'api/v1/resource/componenttypes/' + type + urlEnd,
						method: method,
						callback: function(){
							Ext.getCmp('entryGrid').setLoading(false);
						},
						success: function(response, opts){						
							actionRefreshEntryGrid();
						}
					});
				};
				
				var actionRemoveType = function() {
					var typeToRemove = Ext.getCmp('entryGrid').getSelection()[0].get('componentType');
					
					var promptWindow = Ext.create('Ext.window.Window', {
						title: 'Move Existing Data',
						y: 200,
						width: 400,
						height: 125,
						modal: true,
						layout: 'fit',
						items: [
							{
								xtype: 'form',
								bodyStyle: 'padding: 10px',
								dockedItems: [
									{
										xtype: 'toolbar',
										dock: 'bottom',
										items: [
											{
												text: 'Apply',
												formBind: true,
												iconCls: 'fa fa-check',
												handler: function(){
													var form = this.up('form');
													var data = form.getValues();
													
													Ext.getCmp('entryGrid').setLoading("Removing entry type...");
													Ext.Ajax.request({
														url: 'api/v1/resource/componenttypes/' + typeToRemove + '?newtype=' + data.componentType,
														method: 'DELETE',
														callback: function(){
															Ext.getCmp('entryGrid').setLoading(false);
														},
														success: function(response, opts){	
															promptWindow.close();
															actionRefreshEntryGrid();
														}
													});													
												}
											},
											{
												xtype: 'tbfill'
											},
											{
												text: 'Cancel',
												formbind: true,
												iconCls: 'fa fa-close',
												handler: function(){
													promptWindow.close();
												}												
											}
										]
									}
								],
								layout: 'anchor',
								items: [
									{
										xtype: 'combobox',
										width: '100%',
										name: 'componentType',
										fieldLabel: 'Entry Type<span class="field-required" />',
										valueField: 'code',
										displayField: 'description',
										allowBlank: false,
										editable: false,
										typeahead: false,
										store: {
											autoLoad: true,
											sorters: [{
												property: 'description',
												direction: 'ASC'													
											}],
											proxy: {
												type: 'ajax',
												url: 'api/v1/resource/componenttypes/lookup'
											},
											listeners: {
												load: function(store, records, successful, opts) {
													
													store.filterBy(function(record){
														if (record.get('code') === typeToRemove) {
															return false;
														} else {
															return true;
														}
													});
												}
											}
										}
									}
								]
							}
						]
					});
					promptWindow.show();
					
				};
				
			});
		</script>	
		
	</stripes:layout-component>
</stripes:layout-render>