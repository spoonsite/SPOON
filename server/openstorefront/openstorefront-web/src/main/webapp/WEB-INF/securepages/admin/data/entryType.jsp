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
				
				var mediaWindow = Ext.create('OSF.component.MediaInsertWindow', {

					isEditor: false,
					mediaSelectionUrl: 'api/v1/resource/generalmedia',
					closeAction: 'hide',
					mediaHandler: function(link) {
						Ext.getCmp('iconUrlField').setValue(encodeURI(link));
					}
				});				
				
				
				var addEditWin = Ext.create('Ext.window.Window', {
					id: 'addEditWin',
					iconCls: 'fa fa-lg fa-edit icon-small-vertical-correction',
					title: 'Add/Edit Entry Type',
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
									id: 'entryForm-type-label',
									fieldLabel: 'Label<span class="field-required" />',
									name: 'label',
									allowBlank: false,
									width: '100%',
									maxLength: 80																		
								},
								{
									xtype: 'htmleditor',
									name: 'description',
									id: 'entryForm-type-description',
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
									name: 'allowOnSubmission',
									id: 'entryForm-radio-allow-on-sub'
								},								
								{
									xtype: 'checkbox',
									boxLabel: 'Attributes',
									name: 'dataEntryAttributes',
									id: 'entryForm-radio-attributes'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Relationships ',
									name: 'dataEntryRelationships',
									id: 'entryForm-radio-relationships'									
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Contacts',
									name: 'dataEntryContacts',
									id: 'entryForm-radio-contacts'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Resources',
									name: 'dataEntryResources',
									id: 'entryForm-radio-resources'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Media',
									name: 'dataEntryMedia',
									id: 'entryForm-radio-media'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Dependencies',
									name: 'dataEntryDependencies',
									id: 'entryForm-radio-dependencies'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Evaluation Information',
									name: 'dataEntryEvaluationInformation',
									id: 'entryForm-radio-eval-info'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Reviews',
									name: 'dataEntryReviews',
									id: 'entryForm-radio-reviews'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Questions',
									name: 'dataEntryQuestions',
									id: 'entryForm-radio-questions'
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
								}),
								{
									xtype: 'label',
									text: 'Icon URL:',
									margin: '0 5 0 0',
									style: {
										fontWeight: 'bold'
									}
								},
								{
									layout: 'hbox',
									width: '100%',
									margin: '5px 0 0 0',
									items: [
										{
											xtype: 'textfield',
											id: 'iconUrlField',
											name: 'iconUrl',
											flex: 4
										},
										{
											xtype: 'button',
											text: 'Insert Media',
											flex: 1,
											handler: function() {

												mediaWindow.show();
											}
										}
									]
								}								
							],
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										{
											text: 'Save',
											iconCls: 'fa fa-lg fa-save icon-button-color-save',
											id: 'entryTypeForm-save',
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
											iconCls: 'fa fa-lg fa-close icon-button-color-warning',
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
									id: 'entryTypeRefreshBtn',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										actionRefreshEntryGrid();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Add',
									id: 'entryTypeAddBtn',
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction',
									handler: function () {
										actionAddEntry();
									}
								},								
								{
									text: 'Edit',
									id: 'lookupGrid-tools-edit',
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
									disabled: true,
									handler: function () {
										actionEditEntry(Ext.getCmp('entryGrid').getSelection()[0]);
									}								
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Toggle Status',
									id: 'lookupGrid-tools-status',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-power-off icon-button-color-default icon-vertical-correction',
									disabled: true,
									handler: function () {
										actionToggleStatus();
									}								
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									id: 'lookupGrid-tools-remove',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
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
						iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
						title: 'Delete Entry Type?',
						y: 200,
						width: 400,
						minHeight: 175,
						modal: true,
						layout: 'fit',
						closeAction: 'destroy',
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
												id 'applyBtnDeleteEntryType',
												iconCls: 'fa fa-lg fa-check icon-button-color-save',
												handler: function(){
													var form = this.up('form');
													var data = form.getValues();
													
													Ext.getCmp('entryGrid').setLoading("Deleting entry type...");
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
												iconCls: 'fa fa-lg fa-close icon-button-color-warning',
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
										anchor: '100% 10%',
										name: 'componentType',
										itemId: 'moveExistingDataComboBox',
										labelAlign: 'top',
										fieldLabel: 'Move existing data to<span class="field-required" />',
										valueField: 'code',
										emptyText: 'Select Entry Type',
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