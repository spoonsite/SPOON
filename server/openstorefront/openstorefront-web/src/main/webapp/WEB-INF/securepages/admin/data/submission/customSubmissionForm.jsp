<%--
/* 
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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

 Author: cyearsley
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
	
	<stripes:layout-render name="../../../../../layout/adminheader.jsp">
	</stripes:layout-render>

	<script src="scripts/component/importWindow.js?v=${appVersion}" type="text/javascript"></script>
	
	<link rel="stylesheet" href="css/customForms.css">	
	
	<form name="exportForm" action="api/v1/resource/submissiontemplates/export" method="POST" >
		<p style="display: none;" id="exportFormIds">
		</p>
	</form>	
		
	<script type="text/javascript">
		/* global Ext, CoreUtil */
		Ext.require('OSF.customSubmissionTool.Window');
		Ext.require('OSF.customSubmission.SubmissionFormFullControl');
		Ext.require('OSF.customSubmissionTool.EntryTypeSelectWindow');
				
		Ext.onReady(function(){	
			
			var formTemplateGrid = Ext.create('Ext.grid.Panel', {
				id: 'formTemplateGrid',
				title: 'Manage Custom Submission Forms <i class="fa fa-question-circle" data-qtip="Add, edit, and delete custom submission forms"></i>',
				columnLines: true,
				store: {
					autoLoad: true,
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
					proxy: {
					 	type: 'ajax',
					 	url: 'api/v1/resource/submissiontemplates'
					}
				},
				listeners: {
					itemdblclick: function(grid, record, item, index, e, opts){
						actionAddEdit(record);
					},	
					selectionchange: function(selModel, selected, opts) {
						var tools = Ext.getCmp('formTemplateGrid').getComponent('tools');

						if (selected.length > 0) {
							tools.getComponent('edit').setDisabled(false);
							tools.getComponent('copy').setDisabled(false);							
							tools.getComponent('preview').setDisabled(false);
							tools.getComponent('export').setDisabled(false);

							if (selected[0].get('templateStatus') === 'VERIFIED') {
								tools.getComponent('togglestatus').setDisabled(false);
							}
							else {
								tools.getComponent('togglestatus').setDisabled(true);
							}
							
							if (selected[0].get('templateStatus') === 'VERIFIED' && selected[0].get('activeStatus') === 'A') {
								tools.getComponent('edit').setDisabled(true);
							}							
							
							if (selected[0].get('templateStatus') === 'PENDING_VERIFICATION') {
								tools.getComponent('verify').setDisabled(false);							
							} else {
								tools.getComponent('verify').setDisabled(true);
							}
							
							if (!selected[0].get('defaultTemplate')) {								
								tools.getComponent('delete').setDisabled(false);
							} else {
								tools.getComponent('delete').setDisabled(true);
							}
						} else {
							tools.getComponent('edit').setDisabled(true);
							tools.getComponent('togglestatus').setDisabled(true);
							tools.getComponent('copy').setDisabled(true);
							tools.getComponent('preview').setDisabled(true);
							tools.getComponent('export').setDisabled(true);
							tools.getComponent('delete').setDisabled(true);
							tools.getComponent('verify').setDisabled(true);
						}
					}
				},
				columns: [
					{ text: 'Name', dataIndex: 'name', flex: 7, 
						renderer: function(value, meta, record) {
							if (record.get('defaultTemplate')) {
								return value + ' (<b>Default</b>)';
							} else {
								return value;
							}
						}
					},
					{ text: 'Description', dataIndex: 'description', flex: 10 },
					{ text: 'Form Completion Status <i class="fa fa-question-circle" data-qtip="Indicates that a form is ready for use"></i>', dataIndex: 'templateStatusLabel', align: 'center', flex: 5,
						renderer: function(value, meta, record) {
							if (record.get('templateStatus') === 'INCOMPLETE') {
								meta.tdCls = 'alert-danger';
							} else if (record.get('templateStatus') === 'PENDING_VERIFICATION') {
								meta.tdCls = 'alert-warning';
							} else {
								meta.tdCls = 'alert-success';
							}
							return record.get('templateStatusLabel');
						}
					},
					{ text: 'Entry Type', dataIndex: 'entryTypeLabel', align: 'center', flex: 7,
						renderer: function (value) {
							if (typeof value !== 'undefined') {
								return value;
							}
							else {
								return '<span style="color: #ccc; font-style: italic;">None</span>'
							}
						}
					},
					{ text: 'Active Status', dataIndex: 'activeStatus', align: 'center', flex: 4 },
					{ text: 'Create Date', dataIndex: 'createDts', xtype: 'datecolumn', format:'m/d/y H:i:s',  flex: 4 },
					{ text: 'Create User', dataIndex: 'createUser', flex: 4 },
					{ text: 'Update Date', dataIndex: 'updateDts', xtype: 'datecolumn', format:'m/d/y H:i:s',  flex: 4 },
					{ text: 'Update User', dataIndex: 'updateUser', flex: 4 }
				],
				dockedItems: [	
					{
						dock: 'top',
						xtype: 'toolbar',
						itemId: 'tools',
						items: [
							{
								text: 'Refresh',
								iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
								scale: 'medium',
								handler: function(){
									actionRefresh();
								}
							},
							{
								xtype: 'tbseparator'
							},
							{
								text: 'Add Form',
								iconCls: 'fa fa-2x fa-plus icon-button-color-save',
								width: '150px',
								scale: 'medium',
								handler: function(){
									actionAddEdit();
								}
							},
							{
								text: 'Edit Form',
								iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
								itemId: 'edit',
								width: '150px',
								disabled: true,
								scale: 'medium',
								handler: function(){
									var record = Ext.getCmp('formTemplateGrid').getSelectionModel().getSelection()[0];
									actionAddEdit(record);
								}
							},
							{
								xtype: 'tbseparator'
							},
							{
								text: 'Copy',
								iconCls: 'fa fa-2x fa-copy icon-button-color-default',
								itemId: 'copy',
								disabled: true,	
								scale: 'medium',
								handler: function(){
									var record = Ext.getCmp('formTemplateGrid').getSelectionModel().getSelection()[0];
									actionCopy(record);
								}
							},							
							{
								text: 'Toggle Active Status',
								iconCls: 'fa fa-2x fa-power-off icon-button-color-default',
								itemId: 'togglestatus',
								disabled: true,	
								scale: 'medium',
								handler: function(){
									var record = Ext.getCmp('formTemplateGrid').getSelectionModel().getSelection()[0];
									actionToggleStatus(record);
								}
							},
							{
								xtype: 'tbseparator'
							},
							{
								text: 'Verify',
								iconCls: 'fa fa-2x fa-check icon-button-color-save icon-vertical-correction',
								itemId: 'verify',
								disabled: true,	
								scale: 'medium',
								handler: function(){
									var record = Ext.getCmp('formTemplateGrid').getSelectionModel().getSelection()[0];
									actionVerifyTemplate(record);
								}
							},							
							{
								text: 'Preview', 
								iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction',
								itemId: 'preview',
								disabled: true,	
								scale: 'medium',
								handler: function(){
									var record = Ext.getCmp('formTemplateGrid').getSelectionModel().getSelection()[0];
									actionPreview(record);
								}
							},							
							{
								xtype: 'tbfill'
							},
							{		
								text: 'Import',
								iconCls: 'fa fa-2x fa-upload icon-button-color-default icon-vertical-correction',
								itemId: 'import',								
								scale: 'medium',
								handler: function(){									
									actionImport();
								}
							},
							{
								text: 'Export',
								iconCls: 'fa fa-2x fa-download icon-button-color-default icon-vertical-correction',
								itemId: 'export',
								disabled: true,
								scale: 'medium',
								handler: function(){									
									actionExport();
								}
							},
							{
								xtype: 'tbseparator'
							},
							{
								text: 'Delete',
								iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
								itemId: 'delete',
								disabled: true,
								scale: 'medium',
								handler: function(){
									var record = Ext.getCmp('formTemplateGrid').getSelectionModel().getSelection()[0];
									actionDelete(record);
								}
							}
						]
					}
				]
			});
			
			addComponentToMainViewPort(formTemplateGrid);
			
			var actionRefresh = function() {
				Ext.getCmp('formTemplateGrid').getStore().reload();
			};
		
			var actionAddEdit = function(record) {
				
				if (record) {
					//edit
					Ext.create('OSF.customSubmissionTool.Window', {
						template: record.data,
						saveCallback: function(updatedTemplate) {
							actionRefresh();
						}
					}).show();

				} else {
					//add
					var addWindow = Ext.create('Ext.window.Window',	{
						width: 400,
						height: 330,
						modal: true,
						closeAction: 'destroy',
						title: 'Add Submission Template',					
						items: [
							{
								xtype: 'form',
								itemId: 'createForm',
								layout: 'anchor',	
								scrollable: true,
								bodyStyle: 'padding: 10px',
								defaults: {
									labelAlign: 'top',
									width: '100%'
								},
								items: [
									{
										xtype: 'textfield',
										fieldLabel: 'New Form Name <i class="fa fa-question-circle" data-qtip="This is what the form template will be identified by"></i>',									
										name: 'name',
										maxLength: 255,
										allowBlank: false
									},
									{
										xtype: 'textfield',
										fieldLabel: 'Description',									
										name: 'description',
										maxLength: 1024
									},
									{
										xtype: 'combobox',
										fieldLabel: 'Entry Type',
										itemId: 'entryType',
										name: 'entryType',
										displayField: 'description',
										valueField: 'code',
										editable: false,
										typeAhead: false,
										allowBlank: false,
										store: {
											autoLoad: true,
											proxy: {
												type: 'ajax',
												url: 'api/v1/resource/componenttypes/lookup'
											}
										}
									}
								],
								dockedItems: [{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										{
											xtype: 'button',
											formBind: true,
											text: 'Create Form',
											iconCls: 'fa fa-save icon-button-color-save',
											listeners: {
												click: function () {
													var createForm = this.up('form');

													var data = createForm.getValues();
													data.templateStatus = 'INCOMPLETE';
													data.activeStatus = 'I';

													CoreUtil.submitForm({
														url: 'api/v1/resource/submissiontemplates',
														method: 'POST',
														data: data,
														form: createForm,
														success: function(response, opts){
															var newTemplate = Ext.decode(response.responseText);

															actionRefresh();
															Ext.create('OSF.customSubmissionTool.Window', {
																template: newTemplate,
																saveCallback: function(updatedTemplate) {
																	actionRefresh();
																}
															}).show();
															addWindow.close();
														}
													});



												}
											}
										},
										'->',
										{
											xtype: 'button',
											text: 'Cancel',
											iconCls: 'fa fa-close icon-button-color-warning',
											listeners: {
												click: function () {
													addWindow.close();
												}
											}
										}
									]
								}]
							}
						]
					});
					addWindow.show();
				}
				
			};
			
			var actionToggleStatus = function(record) {
				
				var action = 'inactivate';
				if (record.get('activeStatus') === 'I') {
					action = 'activate';
				} 
				var toggleAction = function () {
					formTemplateGrid.setLoading('Updating Status...');
					Ext.Ajax.request({
						url: 'api/v1/resource/submissiontemplates/' + record.get('submissionTemplateId') + '/' + action,
						method: 'PUT',
						callback: function() {
							formTemplateGrid.setLoading(false);
						},
						success: function(response, opt) {
							actionRefresh();
						}
					});		
				};

				var activeRecord = formTemplateGrid.getStore().getData().items.reduce(function (acc, item) {
					var itemEntryType = item.get('entryType');
					if (itemEntryType && itemEntryType === record.get('entryType') && item.get('activeStatus') === 'A') {
						acc = item;
					}
					return acc;
				}, null);

				if (activeRecord !== null && action === 'activate') {
					Ext.Msg.show({
						title: 'Set Form Template to Active?',
						iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
						message: 'You are trying to activate a template that has an entry type of <span style="font-weight: bold;">"' + record.get('entryType') + '"</span><br />' +
								'which is already in use by another template.<br />' +
								'<span style="color: red;">Activating this template will decativate "<span style="font-weight: bold;">' + activeRecord.get('name') + '</span>".</span> <br />' +
								'Are you sure you want to active this form template?',

						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						fn: function (btn) {
							if (btn === 'yes') {
								toggleAction();
							}
						}
					});
				}
				else {
					toggleAction();
				}
				
			};
			
			var actionCopy = function(record) {
				
				var promptWindow = Ext.create('Ext.window.Window', {
					title: 'Copy Template',
					layout: 'fit',
					closeAction: 'destroy',
					modal: true,
					width: 400,
					height: 200,
					items: [
						{
							xtype: 'form',
							scrollable: true,
							bodyStyle: 'padding: 10px;',
							layout: 'anchor',
							items: [
								{
									xtype: 'textfield',
									name: 'name',
									fieldLabel: 'Enter a New Name <span class="field-required" />',
									width: '100%',
									labelAlign: 'top',
									allowBlank: false,
									maxLength: 255
									
								}
							],
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										{
											text: 'Copy',
											formBind: true,
											iconCls: 'fa fa-lg fa-copy icon-button-color-default',
											handler: function() {
												var form = this.up('form');
												var formData = form.getValues();
												
												var copiedData = Ext.clone(record.data);
												copiedData.submissionTemplateId = null;
												copiedData.name = formData.name; 
												
												promptWindow.setLoading('Copying...');
												Ext.Ajax.request({
													url: 'api/v1/resource/submissiontemplates',
													method: 'POST',
													jsonData: copiedData,
													callback: function() {
														promptWindow.setLoading(false);
													},
													success: function(response, opts){														
														actionRefresh();													
														promptWindow.close();
													}
												});
												
											}
										},
										{
											xtype: 'tbfill'
										},
										{
											text: 'Close',
											iconCls: 'fa fa-lg fa-close icon-button-color-warning',
											handler: function() {
												promptWindow.close();
											}											
										}
									]
								}
							]
						}
					]
					
				});
				promptWindow.show();
				
			};
			
			var actionDelete = function(record) {
				Ext.Msg.show({
					title:'Delete Form Template?',
					iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
					message: 'Are you sure you want to delete this form template?',
					buttons: Ext.Msg.YESNO,
					icon: Ext.Msg.QUESTION,
					fn: function(btn) {
						if (btn === 'yes') {
							
							formTemplateGrid.setLoading('Deleting...');
							Ext.Ajax.request({
								url: 'api/v1/resource/submissiontemplates/' + record.get('submissionTemplateId'),
								method: 'DELETE',
								callback: function() {
									formTemplateGrid.setLoading(false);
								},
								success: function(response, opt) {
									actionRefresh();
								}
							});
							
						}
					}
				});
			};
			
			var actionPreview = function(record) {
				
				var previewWin = Ext.create('Ext.window.Window', {
					title: 'Preview',
					layout: 'fit',
					modal: true,
					closeAction: 'destroy',
					width: '80%',
					height: '80%',
					maximizable: true,
					dockedItems: [
						{
							xtype: 'panel',
							dock: 'top',
							html: '<div class="submission-form-preview alert-warning">Preview Mode - (Changes will NOT be saved)</div>'
						}
					],
					items: [
						{
							xtype: 'osf-customSubmission-SubmissionformFullControl',
							itemId: 'form',
							showCustomButton: true,
							previewMode: true,
							hideSave: true,
							customButtonHandler: function() {
								previewWin.close();
							}								
						}
					]
				});
				previewWin.show();
				
				var doLoad = function(componentType) {
					record.data.entryType = componentType;
					previewWin.queryById('form').load(record.data, componentType);						
				};
				
				if (!record.getData().entryType) {
					//prompt
					var entryTypeSelect = Ext.create('OSF.customSubmissionTool.EntryTypeSelectWindow', {
						closable: false,
						alwaysOnTop: true,						
						onEsc: Ext.emptyFn,						
						selectCallBack: function(entryType) {
							doLoad(entryType);
						}
					});
					entryTypeSelect.show();
				} else {
					doLoad(record.getData().entryType);
				}
				
			};
			
			var actionVerifyTemplate = function(record) {
				var previewWin = Ext.create('Ext.window.Window', {
					title: 'Verify Template',
					layout: 'fit',
					modal: true,
					closeAction: 'destroy',
					width: '80%',
					height: '80%',
					maximizable: true,
					items: [
						{
							xtype: 'osf-customSubmission-SubmissionformFullControl',
							itemId: 'form',
							verifyMode: true,
							submitText: 'Verify Submission',
							submissionSuccess: function() {
								actionRefresh();
								previewWin.close();
							}
						}
					]
				});
				previewWin.show();

				var doLoad = function(componentType) {
					record.data.entryType = componentType;
					previewWin.queryById('form').load(record.data, componentType, null, true);		
				};
				
				if (!record.getData().entryType) {
					//prompt
					var entryTypeSelect = Ext.create('OSF.customSubmissionTool.EntryTypeSelectWindow', {
						closable: false,
						alwaysOnTop: true,						
						onEsc: Ext.emptyFn,						
						selectCallBack: function(entryType) {
							doLoad(entryType);
						}
					});
					entryTypeSelect.show();
				} else {
					doLoad(record.getData().entryType);
				}
								
			};
			
			var actionImport = function() {
				
				var importWindow = Ext.create('OSF.component.ImportWindow', {
					fileTypeValue: 'SUBTEMPLATE',	
					closeAction: 'destroy',
					uploadSuccess: function(form, action) {
						actionRefresh();
					}
				});
				importWindow.show();
			};
			
			var actionExport = function() {
				var ids = "";

				Ext.Array.each(formTemplateGrid.getSelection(), function(record) {
					ids += '<input type="hidden" name="id" ';
					ids += 'value="' + record.get('submissionTemplateId') +'" />';
				});

				var token = Ext.util.Cookies.get('X-Csrf-Token');
				if (token) {						
					ids += '<input type="hidden" name="X-Csrf-Token" ';
					ids += 'value="' + token + '" />';
				}
				document.getElementById('exportFormIds').innerHTML = ids;

				document['exportForm'].submit();				
			};			
					
		});
		
	</script>

    </stripes:layout-component>
</stripes:layout-render>
