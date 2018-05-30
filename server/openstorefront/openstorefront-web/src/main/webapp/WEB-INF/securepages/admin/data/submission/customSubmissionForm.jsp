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

	<link rel="stylesheet" href="css/customForms.css">	
		
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
					},
					listeners: {
						beforeLoad: function(store, operation, eOpts){
							store.getProxy().extraParams = {
								status: Ext.getCmp('filterActiveStatus').getValue()
							};
						}
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
							tools.getComponent('togglestatus').setDisabled(false);
							tools.getComponent('copy').setDisabled(false);							
							tools.getComponent('preview').setDisabled(false);
							tools.getComponent('delete').setDisabled(false);
						} else {
							tools.getComponent('edit').setDisabled(true);
							tools.getComponent('togglestatus').setDisabled(true);
							tools.getComponent('copy').setDisabled(true);
							tools.getComponent('preview').setDisabled(true);
							tools.getComponent('delete').setDisabled(true);
						}
					}
				},
				columns: [
					{ text: 'Name', dataIndex: 'name', flex: 10 },
					{ text: 'Description', dataIndex: 'description', flex: 10 },
					{ text: 'Form Completion Status <i class="fa fa-question-circle" data-qtip="Indicates that a form is ready for use"></i>', dataIndex: 'templateStatusLabel', align: 'center', flex: 5 },
					{ text: 'Active Status', dataIndex: 'activeStatus', align: 'center', flex: 4 },
					{ text: 'Create Date', dataIndex: 'createDts', xtype: 'datecolumn', format:'m/d/y H:i:s',  flex: 4 },
					{ text: 'Create User', dataIndex: 'createUser', flex: 4 },
					{ text: 'Update Date', dataIndex: 'updateDts', xtype: 'datecolumn', format:'m/d/y H:i:s',  flex: 4 },
					{ text: 'Update User', dataIndex: 'updateUser', flex: 4 }
				],
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
								text: 'Preview',
								iconCls: 'fa fa-2x fa-eye icon-button-color-view',
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
						height: 260,
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
				
				var entryTypeSelect = Ext.create('OSF.customSubmissionTool.EntryTypeSelectWindow', {
					selectCallBack: function(entryType) {
						var previewWin = Ext.create('Ext.window.Window', {
							title: 'Preview',
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
									showCustomButton: true,
									hideSave: true,
									customButtonHandler: function() {
										previewWin.close();
									}								
								}
							]
						});
						previewWin.show();

						previewWin.queryById('form').load(record.data, entryType);						
					}
				});
				entryTypeSelect.show();
				
			};
					
		});
		
	</script>

    </stripes:layout-component>
</stripes:layout-render>
