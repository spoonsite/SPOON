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
		
	<script src="scripts/component/importWindow.js?v=${appVersion}" type="text/javascript"></script>	
		
	<script type="text/javascript">
		/* global Ext, CoreUtil, CoreService */
		Ext.onReady(function(){	
			
			var importWindow = Ext.create('OSF.component.ImportWindow', {					
				fileTypeReadyOnly: false,
				uploadSuccess: function(form, action) {
					Ext.getCmp('fileHistoryGrid').getStore().reload();
				}
			});
			
			CoreService.attributeservice.warmCache();
			
			var selectedMapFormat;
			
			var addEditAttributeCodeWin = Ext.create('Ext.window.Window', {
				title: 'Manage Codes',
				modal: true,
				alwaysOnTop: true,
				width: '60%',
				height: '50%',
				maximizable: true,
				layout: 'fit',
				listeners: {
					close: function() {
						var codeRecords = [];
						
						Ext.getCmp('codeGrid').getStore().each(function(record) {
							codeRecords.push(record.data);
						});						
						addEditAttributeCodeWin.attributeTypeRecord.set('attributeCodeXrefMap', codeRecords);
					}
				},
				dockedItems: [
					{
						xtype: 'toolbar',
						dock: 'bottom',
						items: [
							{
								xtype: 'tbfill'								
							}, 
							{
								text: 'Close',
								iconCls: 'fa fa-lg fa-close icon-button-color-warning',
								handler: function() {
									addEditAttributeCodeWin.close();
								}
							},
							{
								xtype: 'tbfill'
							}
						]
					}
				],
				items: [
					{
						xtype: 'grid',
						id: 'codeGrid',
						columnLines: true,
						store: {
						},
						columns: [
							{ text: 'Attribute Code', dataIndex: 'attributeCode', width: 250 },
							{ text: 'Attribute Label', dataIndex: 'attributeLabel', width: 250 },
							{ text: 'External Code', dataIndex: 'externalCode', flex: 1, minWidth: 200 }
						],
						listeners: {
							selectionchange: function(selmodel, selected, opts) {
								var tools = Ext.getCmp('codeGrid').getComponent('tools');
								if (selmodel.getCount() > 0) {									
									tools.getComponent('edit').setDisabled(false);
									tools.getComponent('remove').setDisabled(false);
								}  else {									
									tools.getComponent('edit').setDisabled(true);
									tools.getComponent('remove').setDisabled(true);															
								}
							}
						},							
						dockedItems: [
							{
								xtype: 'form',
								id:  'attributeCodeForm',
								dock: 'top',
								bodyStyle: 'padding: 10px;',
								layout: 'anchor',
								defaults: {
									width: '100%',
									labelAlign: 'top',
									labelSeparator: ''
								},
								items: [
									{
										xtype: 'combo',
										id: 'attributeCodeCB',
										name: 'attributeCode',
										fieldLabel: 'Attribute Code<span class="field-required" />',
										allowBlank: false,
										valueField: 'code',
										displayField: 'label',										
										store: {
											autoLoad: false,
											fields: [
												{name: 'code', mapping: function(data) {
													return data.attributeCodePk.attributeCode;
												}}
											],											
											proxy: {
												type: 'ajax',
												url: 'api/v1/resource/attributes/attributetypes/{type}/attributecodes'
											}
										},
										editable: false,
										typeAhead: false										
									},
									{
										xtype: 'textfield',
										name: 'externalCode',
										fieldLabel: 'External Code<span class="field-required" />',
										allowBlank: false,
										maxLength: 255
									},
									{
										xtype: 'container',
										layout: 'hbox',
										items: [
											{
												xtype: 'button',
												id: 'attributeCodeAddBtn',
												text: 'Add',
												formBind: true,
												margin: '0 20 10 0',
												width: '200',
												scale: 'medium',
												iconCls: 'fa fa-lg fa-plus',
												handler: function() {
													this.setText('Add');
													var form = this.up('form');
													var data = form.getValues();
													var grid = form.up('grid');
													
													var attributeTypeRecord = Ext.getCmp('attributeCodeCB').getSelection();													
													data.attributeLabel = CoreService.attributeservice.translateCode(attributeTypeRecord.data.attributeCodePk.attributeType, attributeTypeRecord.data.attributeCodePk.attributeCode);

													if (grid.editRecord) {
														grid.editRecord.set(data, {
															dirty: false
														});
														grid.editRecord = null;
													} else {																		
														grid.getStore().add(data);
													}
													addEditMapping.hasChanges=true;
													form.reset();
												}
											},
											{
												xtype: 'button',
												text: 'Cancel',
												margin: '5 0 00 0',
												iconCls: 'fa fa-lg fa-close',
												handler: function() {
													Ext.getCmp('attributeCodeAddBtn').setText('Add');
													var form = this.up('form');
													form.reset();
												}
											}															
										]
									}									
								]
							},
							{
								xtype: 'toolbar',
								itemId: 'tools',
								dock: 'top',
								items: [
									{
										text: 'Edit',
										itemId: 'edit',
										disabled: true,
										iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
										handler: function(){
											var grid = this.up('grid');																	
											var form = Ext.getCmp('attributeCodeForm');
											var record = grid.getSelectionModel().getSelection()[0];
											grid.editRecord = record;

											form.loadRecord(record);
											Ext.getCmp('attributeCodeAddBtn').setText('Update');											
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Delete',
										itemId: 'remove',
										disabled: true,
										iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
										handler: function(){
											var grid = this.up('grid');	
											var record = grid.getSelectionModel().getSelection()[0];
											grid.getStore().remove(record);											
										}
									}									
								]
							}
						]
					}
				]
			});
			
			var addEditMapping = Ext.create('Ext.window.Window', {
				title: 'Add/Edit Mapping',
				iconCls: 'fa fa-lg fa-edit icon-small-vertical-correction',
				modal: true,
				width: '80%',
				height: '80%',
				maximizable: true,
				layout: 'fit',
				listeners: {
					beforeclose: function(panel, opts) {
						if (panel.hasChanges) {
							Ext.Msg.show({
								title:'Save Changes?',
								message: 'You have unsaved changes. Would you like to save your changes?',
								buttons: Ext.Msg.YESNOCANCEL,
								icon: Ext.Msg.QUESTION,
								fn: function(btn) {
									if (btn === 'yes') {
										mappingActionSave();
										panel.hasChanges = false;
										addEditMapping.close();
									} else if (btn === 'no') {
										panel.hasChanges = false;
										addEditMapping.close();
									}
								}
							});
							return false;
						}
					}
				},
				items: [
					{
						xtype: 'form',
						id: 'mainForm',
						bodyStyle: 'padding: 20px;',
						layout: 'anchor',
						scrollable: true,
						items: [
							{	
								xtype: 'hidden',
								name: 'fileDataMapId'
							},
							{
								xtype: 'textfield',
								labelAlign: 'top',
								labelSeparator: '',
								width: '100%',
								name: 'name',
								fieldLabel: 'Data Mapping Name<span class="field-required" />',
								allowBlank: false
							},
							{
								xtype: 'combo',
								itemId: 'defaultComponentType',
								labelAlign: 'top',
								labelSeparator: '',
								width: '100%',
								name: 'defaultComponentType',
								fieldLabel: 'Default Entry Type',
								valueField: 'code',
								displayField: 'description',
								store: {
									autoLoad: true,
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/componenttypes/lookup'
									},
									listeners: {
										load: function (myStore, records, sucessful, opts) {
											myStore.add([{
												code: null,
												description: 'SELECT'
											}]);
										}
									}									
								},
								editable: false,
								typeAhead: false								
							
							},							
							{
								xtype: 'panel',
								itemId: 'fieldMapping',
								title: 'Fields',
								collapsible: true,
								titleCollapse: true,
								layout: 'anchor',
								items: [
									{
										xtype: 'form',
										layout: 'hbox',
										items: [
											{
												xtype: 'filefield',
												name: 'uploadFile',
												fieldLabel: 'Sample File with Fields',
												labelAlign: 'top',
												labelSeparator: '',
												allowBlank: false,
												flex: 1,
												margin: '0 10 0 0'
											},
											{
												xtype: 'button',
												text: 'Upload',												
												iconCls: 'fa fa-upload',
												formBind: true,
												requiredPermissions: ['ADMIN-DATA-IMPORT-EXPORT'],
												handler: function() {
													var uploadForm = this.up('form');
													var fileFieldCB = Ext.getCmp('fieldForm').getComponent('fileFieldCB');
													
													Ext.getCmp('mainForm').setLoading("Loading Fields...");
													uploadForm.submit({
														url: 'Upload.action?DataMapFields&fileFormat='+selectedMapFormat.get('code'),
														method: 'POST',														
														success: function(action, opts) {
															Ext.getCmp('mainForm').setLoading(false);
															var fieldData = Ext.decode(opts.response.responseText);															
															fileFieldCB.getStore().loadData(fieldData.data);
															Ext.toast({
																html: 'Loaded field from file',
																title: 'Upload Success',
																width: 250																
															 });
														},
														failure: function(response,opts){
															Ext.getCmp('mainForm').setLoading(false);
															Ext.Msg.show({
																title: 'Upload Failed',
																msg: 'The file upload was not successful. Check that the file meets the format requirements.',
																buttons: Ext.Msg.OK
															});															
														}
													});														
													
												},
												margin: Ext.isIE ? '32 0 0 0' : '30 0 0 0'
											}
										]
									},									
									{
										xtype: 'fieldset',									
										title: 'Add/Edit Field Mapping',
										layout: 'anchor',										
										items: [
											{
												xtype: 'form',
												id: 'fieldForm',
												layout: 'anchor',
												defaults: {
													labelAlign: 'top',
													width: '100%',
													labelSeparator: ''
												},
												items: [
													{
														xtype: 'combo',
														itemId: 'fileFieldCB',
														name: 'field',
														fieldLabel: 'File Field<span class="field-required" />',
														allowBlank: false,
														valueField: 'field',
														displayField: 'field',
														store: {
															autoLoad: false													
														},
														editable: true,
														typeAhead: true												
													},
													{
														xtype: 'tagfield',
														name: 'fieldTransforms',
														fieldLabel: 'Data Transforms',
														valueField: 'code',
														displayField: 'description',
														store: {
															autoLoad: true,
															proxy: {
																type: 'ajax',
																url: 'api/v1/service/datamapping/transforms'
															}
														},
														editable: true,
														typeAhead: false												
													},
													{
														xtype: 'combo',
														name: 'entityClass',
														fieldLabel: 'Entities<span class="field-required" />',
														allowBlank: false,
														valueField: 'code',
														displayField: 'description',
														store: {
															autoLoad: true,
															proxy: {
																type: 'ajax',
																url: 'api/v1/service/datamapping/mappingentities'
															}
														},
														editable: false,
														typeAhead: false,
														listeners: {
															change: function(cb, newValue, oldValue) {
																if  (newValue) {
																	var entityField = cb.up().getComponent('entityfieldId');
																	entityField.reset();																	
																	entityField.getStore().load({
																		url: 'api/v1/service/datamapping/entityfields/' + newValue
																	});
																	var pathEntityfield = cb.up().getComponent('pathEntityfieldId');
																	pathEntityfield.reset();
																	pathEntityfield.getStore().load({
																		url: 'api/v1/service/datamapping/entityfields/' + newValue
																	});	
																}
															}
														}
													},											
													{
														xtype: 'combo',
														itemId: 'entityfieldId',
														name: 'entityField',
														fieldLabel: 'Entity Field<span class="field-required" />',
														allowBlank: false,
														valueField: 'code',
														displayField: 'description',
														store: {
															autoLoad: false,
															proxy: {
																type: 'ajax',
																url: 'api/v1/service/datamapping/entityfields/{entity}'
															}
														},
														editable: false,
														typeAhead: false												
													},
													{
														xtype: 'checkbox',
														name: 'useAsAttributeLabel',
														boxLabel: 'Use As Attribute Label'												
													},
													{
														xtype: 'checkbox',
														name: 'concatenate',
														boxLabel: 'Concatenate'																								
													},
													{
														xtype: 'checkbox',
														name: 'addEndPathToValue',
														boxLabel: 'Add Path to Value'																								
													},
													{
														xtype: 'checkbox',
														name: 'fileAttachment',
														boxLabel: 'File Attachment'																								
													},													
													{
														xtype: 'combo',
														itemId: 'pathEntityfieldId',
														name: 'pathToEnityField',
														fieldLabel: 'Path to Entity Field',
														valueField: 'code',
														displayField: 'description',
														store: {
															autoLoad: false,
															proxy: {
																type: 'ajax',
																url: 'api/v1/service/datamapping/entityfields/{entity}'
															},
															listeners: {
																load: function (myStore, records, sucessful, opts) {
																	myStore.add([{
																		code: null,
																		description: 'SELECT'
																	}]);
																}
															}
														},
														editable: false,
														typeAhead: false												
													},											
													{
														xtype: 'tagfield',
														name: 'rawPathTransforms',
														fieldLabel: 'Path Transforms',
														valueField: 'code',
														displayField: 'description',
														store: {
															autoLoad: true,
															proxy: {
																type: 'ajax',
																url: 'api/v1/service/datamapping/transforms'
															}
														},
														editable: true,
														typeAhead: false
													},
													{
														xtype: 'container',
														layout: 'hbox',
														margin: '0 0 10 0',
														items: [
															{
																xtype: 'button',
																id: 'fieldAddBtn',
																text: 'Add',
																formBind: true,
																margin: '0 20 0 0',
																width: '100',
																scale: 'medium',
																iconCls: 'fa fa-lg fa-plus icon-small-vertical-correction',
																handler: function() {
																	this.setText('Add');
																	var form = this.up('form');
																	var data = form.getValues();
																	var grid = form.up().getComponent('fieldGrid');
																	
																	if (grid.editRecord) {
																		grid.editRecord.set(data, {
																			dirty: false
																		});
																		if (!data.useAsAttributeLabel) {
																			grid.editRecord.set({
																				useAsAttributeLabel: null
																			}, {dirty: false});
																		}
																		if (!data.concatenate) {
																			grid.editRecord.set({
																				concatenate: null
																			}, {dirty: false});
																		}
																		if (!data.addEndPathToValue) {
																			grid.editRecord.set({
																				addEndPathToValue: null
																			}, {dirty: false});
																		}																		
																		
																		grid.editRecord = null;
																	} else {																		
																		grid.getStore().add(data);
																	}
																	addEditMapping.hasChanges=true;
																	form.reset();
																}
															},
															{
																xtype: 'button',
																text: 'Cancel',
//																margin: '5 0 0 0',
																scale: 'medium',
																iconCls: 'fa fa-lg fa-close icon-small-vertical-correction',
																handler: function() {
																	Ext.getCmp('fieldAddBtn').setText('Add');
																	var form = this.up('form');
																	form.reset();
																}
															}															
														]
													}
												]
											},											
											{
												xtype: 'grid',
												id: 'fieldGrid',
												columnLines: true,											
												height: 350,
												margin: '0 0 10 0',
												border: true,
												store: {
												},
												listeners: {
													selectionchange: function(selmodel, selected, opts) {
														var tools = Ext.getCmp('fieldGrid').getComponent('tools');
														if (selmodel.getCount() > 0) {
															tools.getComponent('edit').setDisabled(false);
															tools.getComponent('remove').setDisabled(false);
														}  else {
															tools.getComponent('edit').setDisabled(true);
															tools.getComponent('remove').setDisabled(true);															
														}
													}
												},
												columns: [
													{ text: 'File Field', dataIndex: 'field', flex: 1, minWidth: 350},
													{ text: 'Tranforms', dataIndex: 'fieldTransforms', width: 200},
													{ text: 'Entity Class', dataIndex: 'entityClass', width: 150, 
														renderer: function(value) {
															var showValue = '';
															if (value) {
																var classparts = value.split('.');
																showValue = classparts[classparts.length - 1];
															}
															return showValue;
														}
													},
													{ text: 'Entity Field', dataIndex: 'entityField', width: 150},		
													{ text: 'Flags', columns: [														
														{ text: 'Use As Attribute Label', dataIndex: 'useAsAttributeLabel', width: 125},
														{ text: 'Concatenate', dataIndex: 'concatenate', width: 125},
														{ text: 'Add Path to Value', dataIndex: 'addEndPathToValue', width: 125},														
														{ text: 'File Attachment', dataIndex: 'fileAttachment', width: 125}
													]},
													{ text: 'Path to Entity Field', dataIndex: 'pathToEnityField', width: 125},
													{ text: 'Path Tranforms', dataIndex: 'rawPathTransforms', width: 200}
												],
												dockedItems: [
													{
														xtype: 'toolbar',
														itemId: 'tools',
														dock: 'top',
														items: [
															{
																text: 'Edit',
																itemId: 'edit',
																disabled: true,
																iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
																handler: function() {
																	var grid = this.up('grid');																	
																	var form = Ext.getCmp('fieldForm');
																	var record = grid.getSelectionModel().getSelection()[0];
																	grid.editRecord = record;
																	
																	form.loadRecord(record);
																	Ext.getCmp('fieldAddBtn').setText('Update');
																}
															},
															{
																xtype: 'tbfill'
															},
															{
																text: 'Delete',
																itemId: 'remove',
																disabled: true,
																iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
																handler: function() {
																	var grid = this.up('grid');	
																	var record = grid.getSelectionModel().getSelection()[0];
																	grid.getStore().remove(record);
																}																
															}
														]
													}
												]
											}
										]
									}
								]
							},
							{
								xtype: 'panel',
								padding: '0 0 10 0'
							},
							{
								xtype: 'panel',
								itemId: 'attributeMapping',
								title: 'Attributes',
								collapsible: true,
								titleCollapse: true,
								items: [
									{
										xtype: 'checkbox',
										id: 'chkMissingAttributeType',
										name: 'addMissingAttributeTypeFlg',
										boxLabel: 'Add Missing Attribute Types'	
									},
									{
										xtype: 'fieldset',
										title: 'Add/Edit Attribute Type Mapping',
										layout: 'anchor',
										defaults: {
											labelAlign: 'top',
											width: '100%'
										},
										items: [
											{
												xtype: 'form',
												id: 'attributeTypeForm',
												layout: 'anchor',
												defaults: {
													labelAlign: 'top',
													width: '100%',
													labelSeparator: ''
												},
												items: [
													{
														xtype: 'combo',
														name: 'attributeType',
														fieldLabel: 'Attribute Type<span class="field-required" />',
														allowBlank: false,
														valueField: 'attributeType',
														displayField: 'description',
														store: {
															autoLoad: true,
															proxy: {
																type: 'ajax',
																url: 'api/v1/resource/attributes/attributetypes',
																reader: {
																	type: 'json',
																	rootProperty: 'data'
																}
															}
														},
														editable: false,
														typeAhead: false,
														listeners: {
															change: function(cb, newValue, oldValue) {
																var defaultMappedCode = cb.up().getComponent('defaultMappedCode');
																defaultMappedCode.getStore().load({
																	url: 'api/v1/resource/attributes/attributetypes/' + newValue + '/attributecodes'
																});																										
															}
														}												
													},
													{
														xtype: 'textfield',
														name: 'externalType',
														fieldLabel: 'External Type<span class="field-required" />',
														allowBlank: false,
														maxValue: 255
													},
													{
														xtype: 'checkbox',
														name: 'addMissingCode',
														boxLabel: 'Add Missing Codes'													
													},
													{
														xtype: 'combo',
														itemId: 'defaultMappedCode',
														name: 'defaultMappedCode',
														fieldLabel: 'Default Mapped Code',
														valueField: 'code',
														displayField: 'label',
														store: {
															autoLoad: false,
															fields: [
																{name: 'code', mapping: function(data) {
																	return data.attributeCodePk.attributeCode;
																}}
															],
															proxy: {
																type: 'ajax',
																url: 'api/v1/resource/attributes/attributetypes'
															},
															listeners: {
																load: function (myStore, records, sucessful, opts) {
																	myStore.add([{
																		code: null,
																		description: 'SELECT'
																	}]);
																}
															}															
														},
														editable: false,
														typeAhead: false												
													},
													{
														xtype: 'container',
														layout: 'hbox',
														margin: '0 0 10 0',
														items: [
															{
																xtype: 'button',
																id: 'attributeTypeAddBtn',
																text: 'Add',
																formBind: true,
																margin: '0 20 0 0',
																width: '100',
																scale: 'medium',
																iconCls: 'fa fa-lg fa-plus icon-small-vertical-correction',
																handler: function() {
																	this.setText('Add');
																	var form = this.up('form');
																	var data = form.getValues();
																	var grid = form.up().getComponent('attributeTypeGrid');

																	data.attributeTypeLabel = CoreService.attributeservice.translateType(data.attributeType);
																	data.attributeCodeLabel = CoreService.attributeservice.translateCode(data.attributeType, data.defaultMappedCode);
																	
																	if (grid.editRecord) {
																		grid.editRecord.set(data, {
																			dirty: false
																		});
																		if (!data.addMissingCode) {
																			grid.editRecord.set({
																				addMissingCode: null
																			});
																		}

																		grid.editRecord = null;
																	} else {																		
																		grid.getStore().add(data);
																	}
																	addEditMapping.hasChanges=true;
																	form.reset();
																}
															},
															{
																xtype: 'button',
																text: 'Cancel',
//																margin: '5 0 0 0',
																scale: 'medium',
																iconCls: 'fa fa-lg fa-close icon-small-vertical-correction',
																handler: function() {
																	Ext.getCmp('attributeTypeAddBtn').setText('Add');
																	var form = this.up('form');
																	form.reset();
																}
															}															
														]
													}													
												]
											},
											{
												xtype: 'grid',
												id: 'attributeTypeGrid',
												columnLines: true,											
												height: 350,
												margin: '0 0 10 0',
												border: true,
												store: {																				
												},
												columns: [
													{ text: 'Attribute Type', dataIndex: 'attributeType', width: 175},
													{ text: 'Attribute Type Label', dataIndex: 'attributeTypeLabel', flex: 1, width: 200},
													{ text: 'External Type', dataIndex: 'externalType', width: 225},
													{ text: 'Add Missing Code', dataIndex: 'addMissingCode', width: 150},													
													{ text: 'Default Mapped Code', dataIndex: 'defaultMappedCode', width: 200},
													{ text: 'Default Mapped Label', dataIndex: 'attributeCodeLabel', width: 200}
												],
												listeners: {
													selectionchange: function(selmodel, selected, opts) {
														var tools = Ext.getCmp('attributeTypeGrid').getComponent('tools');
														if (selmodel.getCount() > 0) {
															tools.getComponent('addCodes').setDisabled(false);
															tools.getComponent('edit').setDisabled(false);
															tools.getComponent('remove').setDisabled(false);
														}  else {
															tools.getComponent('addCodes').setDisabled(true);
															tools.getComponent('edit').setDisabled(true);
															tools.getComponent('remove').setDisabled(true);															
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
																text: 'Manage Codes',
																itemId: 'addCodes',
																disabled: true,
																iconCls: 'fa fa-lg fa-plus icon-button-color-save',
																handler: function() {
																	var grid = this.up('grid');	
																	var record = grid.getSelectionModel().getSelection()[0];
																	
																	addEditAttributeCodeWin.show();
																	addEditAttributeCodeWin.attributeType = record.get('attributeType');
																	addEditAttributeCodeWin.attributeTypeRecord = record;
																
																	Ext.getCmp('attributeCodeCB').getStore().load({
																		url: 'api/v1/resource/attributes/attributetypes/' + record.get('attributeType') + '/attributecodes'
																	});
																		
																	Ext.getCmp('codeGrid').getStore().removeAll();	
																	if (record.get('attributeCodeXrefMap')) {
																		
																		Ext.Array.each(record.get('attributeCodeXrefMap'), function(code) {
																			code.attributeLabel = CoreService.attributeservice.translateCode(record.get('attributeType') , code.attributeCode);
																		});
																		
																		Ext.getCmp('codeGrid').getStore().loadData(record.get('attributeCodeXrefMap'));
																	} 																
																}																
															},
															{
																text: 'Edit',
																itemId: 'edit',
																disabled: true,
																iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
																handler: function() {
																	var grid = this.up('grid');																	
																	var form = Ext.getCmp('attributeTypeForm');
																	var record = grid.getSelectionModel().getSelection()[0];
																	grid.editRecord = record;
																	
																	form.loadRecord(record);
																	Ext.getCmp('attributeTypeAddBtn').setText('Update');																	
																}
															},
															{
																xtype: 'tbfill'
															},
															{
																text: 'Delete',
																itemId: 'remove',
																disabled: true,
																iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
																handler: function() {
																	var grid = this.up('grid');	
																	var record = grid.getSelectionModel().getSelection()[0];
																	grid.getStore().remove(record);																	
																}																
															}
														]
													}
												]
											}											
										]
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
										scale: 'medium',
										iconCls: 'fa fa-lg fa-save icon-button-color-save icon-small-vertical-correction',
										handler: function() {
											mappingActionSave();
										}
									},
									{
										xtype: 'checkbox',
										id: 'mappingSaveAndContinue',
										name: 'continue',
										boxLabel: 'Save and Continue'
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Cancel',
										scale: 'medium',
										iconCls: 'fa fa-lg fa-close icon-button-color-warning icon-small-vertical-correction',
										handler: function() {
											addEditMapping.close();
										}										
									}
								]
							}
						]
					}
				]
			});
			
			var mappingActionSave = function() {
				var mainForm = Ext.getCmp('mainForm');
				var mainFormData = mainForm.getValues();
				
				//custom Vaildation
				var valid = true;
				
				if (!mainFormData.name) {
					valid = false;
					
					Ext.Msg.show({
						title:'Validation',
						message: 'Data Mapping Name is Required',
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.ERROR,
						fn: function(btn) {
						}
					});												
				}
				
				//can't have multiple fields pointed to the same path
				var multipleSamePath = false;
				var fieldGrid = Ext.getCmp('fieldGrid');				
				var existingFieldPath = [];
				fieldGrid.getStore().each(function(record) {
					var found = Ext.Array.findBy(existingFieldPath, function(existing) {
						if (existing  === record.get('field')) {
							return true;
						}
					});
					if (found) {
						multipleSamePath = true;
					} else {
						existingFieldPath.push(record.get('field'));
					}
				});
				
				if (multipleSamePath) {
					valid = false;
					
					Ext.Msg.show({
						title:'Validation',
						message: 'Duplicate Field Paths;  field paths can only be set once.',
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.ERROR,
						fn: function(btn) {
						}
					});	
				}


				if (valid) {

					var dataMapModel = {
						fileDataMap: {
							fileFormat: selectedMapFormat.get('code'),
							name: mainFormData.name,
							defaultComponentType: mainFormData.defaultComponentType,
							dataMapFields: []
						},
						fileAttributeMap: {
							attributeTypeXrefMap: []
						}
					};

					//field maps
					var fieldGrid = Ext.getCmp('fieldGrid');
					fieldGrid.getStore().each(function(record){
						var data = record.getData();

						data.transforms = [];
						var transforms = data.fieldTransforms;
						Ext.Array.each(transforms, function(item){
							data.transforms.push({
								transform: item
							});
						});

						data.pathTransforms = [];
						transforms = data.rawPathTransforms;
						Ext.Array.each(transforms, function(item){
							data.pathTransforms.push({
								transform: item
							});
						});											

						dataMapModel.fileDataMap.dataMapFields.push(data);										
					});

					//attribute maps
					dataMapModel.fileAttributeMap.addMissingAttributeTypeFlg = Ext.getCmp('chkMissingAttributeType').getValue();

					var attributeTypeGrid = Ext.getCmp('attributeTypeGrid');
					attributeTypeGrid.getStore().each(function(record){
						var data = record.getData();
						dataMapModel.fileAttributeMap.attributeTypeXrefMap.push(data);										
					});		


					var method = 'POST';
					var endURL = '';
					if (mainFormData.fileDataMapId) {
						method = 'PUT';
						endURL = '/' + mainFormData.fileDataMapId;
					}
					
					mainForm.setLoading("Saving Mapping...");
					Ext.Ajax.request({
						url: 'api/v1/resource/filehistory/formats/' + selectedMapFormat.get('code') + '/mappings' + endURL,
						method: method,
						jsonData: dataMapModel,
						callback: function(){		
							mainForm.setLoading(false);							
						},
						success: function(response, opts) {
							addEditMapping.hasChanges = false;
							Ext.toast('Saved mapping', 'Saved');
							actionRefreshMappings();
							
							var continueMapping = Ext.getCmp('mappingSaveAndContinue').getValue();
							if (!continueMapping) {
								addEditMapping.close();
							}
						}
					});
				}				
			};
			
			
			var mappingPanel = Ext.create('Ext.grid.Panel', {
				title: 'Mapping',
				columnLines: true,
				store: {
					autoLoad: false,
					proxy: {
						type: 'ajax',
						url: 'api/v1/resource/filehistory/formats/{format}/mappings'
					}
				},
				columns: [
					{ text: 'Name', dataIndex: 'description', flex: 1, minWidth: 200 } 
				],
				listeners: {
					selectionchange: function(selmodel, selection, opt) {
						var tools = mappingPanel.getComponent('tools');
						if (selmodel.getCount() > 0) {
							tools.getComponent('edit').setDisabled(false);
							tools.getComponent('copy').setDisabled(false);
							tools.getComponent('preview').setDisabled(false);
							tools.getComponent('export').setDisabled(false);
							tools.getComponent('remove').setDisabled(false);
						} else {
							tools.getComponent('edit').setDisabled(true);
							tools.getComponent('copy').setDisabled(true);
							tools.getComponent('preview').setDisabled(true);
							tools.getComponent('export').setDisabled(true);
							tools.getComponent('remove').setDisabled(true);							
						}
					}
				},
				dockedItems: [
					{
						xtype: 'toolbar',
						itemId: 'filterbar',
						dock: 'top',
						items: [
							{
								xtype: 'combo',
								itemId: 'fileFormatFilter',
								name: 'fileFormat',
								fieldLabel: 'Mappable File Formats',
								width: 500,
								valueField: 'code',
								displayField: 'description',
								store: {
									autoLoad: true,
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/filehistory/formats/mappingformats'
									}
								},
								editable: false,
								typeAhead: false,
								listeners: {
									change: function(cb, newValue, oldValue) {
										mappingPanel.getStore().load({
											url: 'api/v1/resource/filehistory/formats/' + newValue + '/mappings'
										});
										if (newValue) {
											mappingPanel.getComponent('tools').setDisabled(false);
										} else {
											mappingPanel.getComponent('tools').setDisabled(true);
										}
										selectedMapFormat = cb.getSelection();										
									}
								}
							}
						]
					},
					{
						xtype: 'toolbar',
						itemId: 'tools',
						disabled: 'true',
						dock: 'top',
						items: [
							{
								text: 'Refresh',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
								handler: function() {
									actionRefreshMappings();
								}
							},
							{
								xtype: 'tbseparator'
							},
							{
								text: 'Add',
								scale: 'medium',
								width: '100px',
								iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction',
								handler: function() {
									actionShowMappingForm();
								}
							},
							{
								text: 'Edit',
								itemId: 'edit',
								disabled: true,
								width: '100px',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
								handler: function() {
									var grid = mappingPanel;
									var mapRecord = mappingPanel.getSelectionModel().getSelection()[0];
									
									actionShowMappingForm();
									
									addEditMapping.getComponent('mainForm').setLoading(true);
									Ext.Ajax.request({
										url: 'api/v1/resource/filehistory/formats/' + selectedMapFormat.get('code') 
											+ '/mappings/' + mapRecord.get('code'),
										method: 'GET',
										callback: function() {
											addEditMapping.getComponent('mainForm').setLoading(false);
										},
										success: function(response, opt) {
											var dataMap = Ext.decode(response.responseText);
											
											var mainRecord = Ext.create('Ext.data.Model', {												
											});
											mainRecord.set({
												fileDataMapId: mapRecord.get('code'),
												name: dataMap.fileDataMap.name,
												defaultComponentType: dataMap.fileDataMap.defaultComponentType
											});
											addEditMapping.getComponent('mainForm').loadRecord(mainRecord);
											
											
											var fieldRecords = [];
											Ext.Array.each(dataMap.fileDataMap.dataMapFields, function(fieldMap){
												
												if (fieldMap.transforms) {
													var transforms = [];
													Ext.Array.each(fieldMap.transforms, function(item){
														transforms.push(item.transform);
													});
													fieldMap.fieldTransforms = transforms;
												}
												
												if (fieldMap.pathTransforms) {
													transforms = [];
													Ext.Array.each(fieldMap.pathTransforms, function(item){
														transforms.push(item.transform);
													});
													fieldMap.rawPathTransforms = transforms;												
												}
												
												fieldRecords.push(fieldMap);
											});
											
											var fieldGrid = Ext.getCmp('fieldGrid');
											fieldGrid.getStore().loadData(fieldRecords);
											
											
											Ext.getCmp('chkMissingAttributeType').setValue(dataMap.fileAttributeMap.addMissingAttributeTypeFlg);											
											
											var attributeTypeGrid = Ext.getCmp('attributeTypeGrid');
											if (!dataMap.fileAttributeMap.attributeTypeXrefMap) {
												dataMap.fileAttributeMap.attributeTypeXrefMap = [];
											}						
											Ext.Array.each(dataMap.fileAttributeMap.attributeTypeXrefMap, function(type) {
												type.attributeTypeLabel = CoreService.attributeservice.translateType(type.attributeType);
												type.attributeCodeLabel = CoreService.attributeservice.translateCode(type.attributeType, type.defaultMappedCode);
											});
											
											attributeTypeGrid.getStore().loadData(dataMap.fileAttributeMap.attributeTypeXrefMap);
											
										}
									});							
									
								}
							},
							{
								xtype: 'tbseparator'
							},
							{
								text: 'Copy',
								itemId: 'copy',
								disabled: true,
								width: '120px',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-clone icon-vertical-correction-view icon-button-color-default',
								handler: function(){	
									var grid = mappingPanel;
									var record = mappingPanel.getSelectionModel().getSelection()[0];	
									
									grid.setLoading("Copying...");
									Ext.Ajax.request({
										url: 'api/v1/resource/filehistory/formats/' + selectedMapFormat.get('code') 
											+ '/mappings/' + record.get('code') + '/copy',
										method: 'POST',
										callback: function() {
											grid.setLoading(false);
										},
										success: function(response, opts) {
											actionRefreshMappings();
										}
									});									
									
								}
							},
							{
								text: 'Preview',
								itemId: 'preview',
								disabled: true,	
								scale: 'medium',
								width: '120px',
								iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction-view',
								handler: function(){									
									var record = mappingPanel.getSelectionModel().getSelection()[0];	
									
									var previewWin = Ext.create('Ext.window.Window', {
										title: 'Preview Mapping',
										iconCls: 'fa fa-lg fa-eye icon-small-vertical-correction',
										modal: true,
										width: '80%',
										height: '80%',
										maximizble: true,
										closeAction: 'destroy',
										layout: 'fit',
										items: [
											{
												xtype: 'panel',
												scrollable: true,
												bodyStyle: 'padding: 10px;',
												dockedItems: [
													{
														xtype: 'toolbar',
														dock: 'top',
														items: [
															{
																xtype: 'form',
																width: '100%',
																layout: 'hbox',
																items: [
																	{
																		xtype: 'filefield',
																		name: 'uploadFile',
																		fieldLabel: 'Sample File to Map',
																		labelAlign: 'top',
																		labelSeparator: '',
																		allowBlank: false,
																		flex: 1,
																		margin: '0 10 0 0'																		
																	},
																	{
																		xtype: 'button',
																		text: 'Upload',
																		formBind: true,
																		iconCls: 'fa fa-lg fa-upload',
																		requiredPermissions: ['ADMIN-DATA-IMPORT-EXPORT'],
																		handler: function() {
																			var uploadForm = this.up('form');
																			var previewPanel = uploadForm.up('panel');
																			
																			
																			
																			uploadForm.submit({
																				url: 'Upload.action?PreviewMapping&fileFormat='
																						+ selectedMapFormat.get('code') 
																						+ '&dataMappingId=' + record.get('code'),
																				method: 'POST',
																				success: function(action, opts) {																					
																					var uploadResponse = Ext.decode(opts.response.responseText);	
																					var message = Ext.util.Format.nl2br(uploadResponse.message);
																					message = message.replace(new RegExp(' ', 'g'), '&nbsp;');
																					var data = {
																						data: message
																					};
																					previewPanel.update(data);																					
																				},
																				failure: function(response,opts){
																					Ext.Msg.show({
																						title: 'Upload Failed',
																						msg: 'The file upload was not successful. Check that the file meets the format requirements.',
																						buttons: Ext.Msg.OK
																					});															
																				}
																			});														

																		},																		
																		margin: Ext.isIE ? '32 0 0 0' : '30 0 0 0'																		
																	}
																]
															}
														]
													},
													{
														xtype: 'toolbar',
														dock: 'bottom',
														items: [
															{
																xtype: 'tbfill'
															},
															{
																text: 'Close',
																iconCls: 'fa  fa-lg fa-close icon-button-color-warning',
																handler: function() {
																	previewWin.close();
																}
															},
															{
																xtype: 'tbfill'
															}
														]
													}
												],
												tpl: '{data}'
											}
										]
										
									});
									previewWin.show();
									
								}
							},
							{
								xtype: 'tbfill'
							},
							{
								text: 'Import',
								itemId: 'import',								
								scale: 'medium',
								iconCls: 'fa fa-2x fa-upload icon-button-color-default icon-vertical-correction',
								handler: function(){											
									var record = mappingPanel.getSelectionModel().getSelection()[0];
									
									var importWin = Ext.create('Ext.window.Window', {
										title: 'Import Mapping',
										iconCls: 'fa fa-lg fa-upload icon-small-vertical-correction',
										modal: true,
										width: 450,
										height: 180,
										y: 100,
										maximizble: true,
										closeAction: 'destroy',
										layout: 'fit',
										items: [
											{
												xtype: 'form',
												bodyStyle: 'padding: 10px;',
												dockedItems: [
													{
														xtype: 'toolbar',
														dock: 'bottom',
														items: [
															{
																text: 'Upload',
																iconCls: 'fa fa-lg fa-upload icon-button-color-default',
																requiredPermissions: ['ADMIN-DATA-IMPORT-EXPORT'],
																formBind: true,
																handler: function() {
																	var uploadForm = this.up('form');
																	var fileFieldCB = Ext.getCmp('fieldForm').getComponent('fileFieldCB');

																	uploadForm.submit({
																		url: 'Upload.action?ImportMapping',
																		method: 'POST',
																		success: function(action, opts) {
																			Ext.toast('Imported Mapping File', 'Upload Success');
																			actionRefreshMappings();
																			importWin.close();
																		},
																		failure: function(response,opts){
																			Ext.Msg.show({
																				title: 'Upload Failed',
																				msg: 'The file upload was not successful. Check that the file meets the format requirements.',
																				buttons: Ext.Msg.OK
																			});															
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
																	var uploadForm = this.up('form');
																	uploadForm.reset();
																	importWin.close();
																}
															}
														]
													}
												],
												layout: 'anchor',
												items: [
													{
														xtype: 'filefield',
														name: 'uploadFile',
														fieldLabel: 'Import Mapping File<span class="field-required" /',
														labelAlign: 'top',
														labelSeparator: '',
														allowBlank: false,
														width: '100%',
														margin: '0 10 0 0'
													}
												]
											}											
										]
									});
									importWin.show();
								}								
							},
							{
								text: 'Export',
								itemId: 'export',
								disabled: true,	
								scale: 'medium',
								iconCls: 'fa fa-2x fa-download icon-button-color-default',
								handler: function(){		
									var record = mappingPanel.getSelectionModel().getSelection()[0];	
									
									window.location.href = 'api/v1/resource/filehistory/formats/' + selectedMapFormat.get('code') 
									+ '/mappings/' + record.get('code') + '/export';
								}								
							},
							{
								xtype: 'tbseparator'
							},
							{
								text: 'Delete',
								itemId: 'remove',
								disabled: true,								
								scale: 'medium',
								iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
								handler: function() {
									var grid = mappingPanel;
									var record = mappingPanel.getSelectionModel().getSelection()[0];									
									
									Ext.Msg.show({
										title:'Delete Mapping',
										iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
										message: 'Are you sure you want to delete this mapping?',
										buttons: Ext.Msg.YESNO,
										icon: Ext.Msg.QUESTION,
										fn: function(btn) {
											if (btn === 'yes') {
												
												grid.setLoading("Removing...");
												Ext.Ajax.request({
													url: 'api/v1/resource/filehistory/formats/' + selectedMapFormat.get('code') 
														+ '/mappings/' + record.get('code'),
													method: 'DELETE',
													callback: function() {
														grid.setLoading(false);
													},
													success: function(response, opts) {
														actionRefreshMappings();
													}
												});
												
											} 
										}
									});									
								}
							}							
						]
					}					
				]
				
			});
			
			var actionRefreshMappings = function() {
				var value = mappingPanel.getComponent('filterbar').getComponent('fileFormatFilter').getValue();
				if (value) {
					mappingPanel.getStore().reload();
				}
			};
			
			var actionShowMappingForm = function() {
				addEditMapping.show();
				

				if (selectedMapFormat.get('fileType') === 'ATTRIBUTE') {
					addEditMapping.getComponent('mainForm').getComponent('attributeMapping').setHidden(true);
					addEditMapping.getComponent('mainForm').getComponent('defaultComponentType').setHidden(true);
				} else {
					addEditMapping.getComponent('mainForm').getComponent('attributeMapping').setHidden(false);
					addEditMapping.getComponent('mainForm').getComponent('defaultComponentType').setHidden(false);
				}
				addEditMapping.getComponent('mainForm').reset();	
				
				//reset grids
				Ext.getCmp('fieldGrid').getStore().removeAll();
				Ext.getCmp('attributeTypeGrid').getStore().removeAll();
				
				//Scroll at the end (the reset is moving the scroller)
				Ext.getCmp('mainForm').setScrollY(0);		
			};
			
			
			//////////////End of Mapping//////////////////
			
			var fileHistoryStore = Ext.create('Ext.data.Store', {
				pageSize: 100,
				remoteSort: true,	
				sorters: [
					new Ext.util.Sorter({
						property : 'updateDts',
						direction: 'DESC'
					})
				],
				fields: [
					"fileHistoryId",
					"fileFormatDescription",
					"fileTypeDescription",
					{
						name: 'createDts',
						type:	'date',
						dateFormat: 'c'
					},
					{
						name: 'startDts',
						type:	'date',
						dateFormat: 'c'
					},
					{
						name: 'completeDts',
						type:	'date',
						dateFormat: 'c'
					},					
					"createUser",
					"mimeType",
					"originalFilename",
					"dataSource",
					"warningsCount",
					"errorsCount",
					"fileFormat",
					"numberRecords",
					"recordsProcessed",					
					{ name: "progress", mapping: function(data){
						return (data.recordsProcessed / data.numberRecords);
					}},
					"recordsStored"
				],
				autoLoad: true,
				proxy: CoreUtil.pagingProxy({
					url: 'api/v1/resource/filehistory',					
					reader: {
					   type: 'json',
					   rootProperty: 'data',
					   totalProperty: 'totalNumber'
					}
				})				
			});
			
			var fileHistoryGrid = Ext.create('Ext.grid.Panel', {
				id: 'fileHistoryGrid',
				title: 'Import History',
				columnLines: true,
				enableColumnMove: false,
				store: fileHistoryStore,
				bodyCls: 'border_accent',
				columns: [
					{ 
						text: 'File Type',
						dataIndex: 'fileTypeDescription', 
						width: 150,
						sortable: false
					},
					{ 
						text: 'File Format',
						dataIndex: 'fileFormatDescription',
						width: 200,
						sortable: false
					},
					{
						text: 'Dates',
						columns: [
							{ text: 'Upload', align: 'center', dataIndex: 'createDts', width: 175, xtype: 'datecolumn', format:'m/d/y H:i:s'  },
							{ text: 'Start', align: 'center', dataIndex: 'startDts', width: 175, xtype: 'datecolumn', format:'m/d/y H:i:s'  },
							{ text: 'Complete', align: 'center', dataIndex: 'completeDts', width: 175, xtype: 'datecolumn', format:'m/d/y H:i:s' }												
						]
					},
					{ text: 'Filename', dataIndex: 'originalFilename', width: 200 },
					{ text: 'Warning/Errors', align: 'center', dataIndex: 'warningsCount', width: 175,
						renderer: function(value, metaData, record) {
							if (record.get('errorsCount') > 0) {
								metaData.tdCls = 'alert-danger';
							} else if (record.get('warningsCount') > 0) {
								metaData.tdCls = 'alert-warning';
							}
							return record.get('warningsCount') + ' / ' + record.get('errorsCount');  
						}
					},
					{ text: 'Data Source', dataIndex: 'dataSource', width: 200, hidden: true },
					{ text: 'Data Mapping Applied', dataIndex: 'fileMappingApplied', width: 200, hidden: true },
					{ text: 'Mime Type', dataIndex: 'mimeType', width: 175, hidden: true },
					{
						text: 'Record Stats',
						columns: [
							{ text: 'Stored', align: 'center', dataIndex: 'recordsStored', width: 100 },
							{ text: 'Processed', align: 'center', dataIndex: 'recordsProcessed', width: 100 },
							{ text: 'Total', align: 'center', dataIndex: 'numberRecords', width: 100 }
						]
					},
					{ text: 'Progress', dataIndex: 'progress', flex: 1, minWidth: 175,
					   xtype: 'widgetcolumn',
					   widget: {
						xtype: 'progressbarwidget',
						textTpl: '{value:percent}'
					   }
					}
				],
				dockedItems: [
					{
						xtype: 'toolbar',
						itemId: 'mainToolbar',
						dock: 'top',
						items: [
							{
								text: 'Refresh',								
								scale: 'medium',								
								iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
								handler: function () {
									this.up('grid').getStore().reload();
								}
							},
							{
								requiredPermissions: ['ADMIN-DATA-IMPORT-EXPORT'],
								xtype: 'tbseparator'
							},
							{					
								text: 'Import',								
								scale: 'medium',
								width: '120px',
								requiredPermissions: ['ADMIN-DATA-IMPORT-EXPORT'],
								iconCls: 'fa fa-2x fa-upload icon-vertical-correction-view icon-button-color-default',
								handler: function () {
									importWindow.show();
								}
							},
							
							{
								xtype: 'tbseparator'
							},							
							{
								text: 'View Details',
								itemId: 'viewDetailsBtn',
								scale: 'medium',
								width: '150px',
								iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction-view',
								disabled: true,
								handler: function () {
									actionViewDetails(Ext.getCmp('fileHistoryGrid').getSelectionModel().getSelection()[0]);
								}
							},							
							{
								xtype: 'tbfill'
							},
							{
								text: 'Reprocess',
								itemId: 'reprocessBtn',
								scale: 'medium',								
								iconCls: 'fa fa-2x fa-undo icon-button-color-refresh icon-vertical-correction',
								disabled: true,
								handler: function () {
									actionReprocess(Ext.getCmp('fileHistoryGrid').getSelectionModel().getSelection()[0]);
								}
							},
							{
								text: 'Rollback',
								itemId: 'rollbackBtn',
								scale: 'medium',								
								iconCls: 'fa fa-2x fa-close icon-vertical-correction icon-button-color-warning',
								disabled: true,
								handler: function () {
									actionRollback(Ext.getCmp('fileHistoryGrid').getSelectionModel().getSelection()[0]);
								}
							}							
						]
					},
					Ext.create('Ext.PagingToolbar', {
						store: fileHistoryStore,
						dock: 'bottom',
						displayInfo: true,
						displayMsg: 'Displaying Record {0} - {1} of {2}',
						emptyMsg: "No records to display",
						items: [
							{
								xtype: 'tbseparator'
							},
							{
								text: 'Download',
								id: 'downloadBtn',
								scale: 'medium',								
								iconCls: 'fa fa-2x fa-download icon-button-color-default',
								disabled: true,
								handler: function () {
									actionDownload(Ext.getCmp('fileHistoryGrid').getSelectionModel().getSelection()[0]);
								}
							},
							{
								xtype: 'tbseparator'
							},
							{
								xtype: 'panel',
								id: 'historyKeepTimePanel',
								html: ''
							}
						]
					})
				],
				listeners: {
					itemdblclick: function(grid, record, item, index, e, opts){
						actionViewDetails(record);
					},
					selectionchange: function(selectionModel, selected, opt){
						
						if (selectionModel.getCount() === 1) {
							Ext.getCmp('fileHistoryGrid').getComponent('mainToolbar').getComponent('viewDetailsBtn').setDisabled(false);
							Ext.getCmp('fileHistoryGrid').getComponent('mainToolbar').getComponent('reprocessBtn').setDisabled(false);
							Ext.getCmp('fileHistoryGrid').getComponent('mainToolbar').getComponent('rollbackBtn').setDisabled(false);							
							Ext.getCmp('downloadBtn').setDisabled(false);
						} else {
							Ext.getCmp('fileHistoryGrid').getComponent('mainToolbar').getComponent('viewDetailsBtn').setDisabled(true);
							Ext.getCmp('fileHistoryGrid').getComponent('mainToolbar').getComponent('reprocessBtn').setDisabled(true);
							Ext.getCmp('fileHistoryGrid').getComponent('mainToolbar').getComponent('rollbackBtn').setDisabled(true);
							Ext.getCmp('downloadBtn').setDisabled(true);
						}
					}
				} 				
			});			
			
			var mainPanel = Ext.create('Ext.tab.Panel', {
				title: 'Manage Imports <i class="fa fa-question-circle"  data-qtip="Allows for management of the data imports and their mappings."></i>',
				items: [
					fileHistoryGrid,
					mappingPanel					
				]
			});
			
			addComponentToMainViewPort(mainPanel);			
			
			var actionUpdateHistoryKeepTime = function() {				
				Ext.Ajax.request({
					url: 'api/v1/service/application/configproperties/filehistory.max.days',
					success: function (response) {
						var keyData = Ext.decode(response.responseText);
						Ext.getCmp('historyKeepTimePanel').update('*History is only kept for <b>' + keyData.description + '</b> day(s)');
					}
				});
			};
			actionUpdateHistoryKeepTime();
			
			var actionDownload = function(record) {
				window.location.href = 'api/v1/resource/filehistory/' + record.get('fileHistoryId') + '/download';
			};
			
			var detailWindow = Ext.create('Ext.window.Window', {
				title: 'Details',
				iconCls: 'fa fa-lg fa-info-circle icon-small-vertical-correction',
				modal: true,
				layout: 'fit',
				width: '60%',
				height: '40%',
				maximizable: true,
				listeners: {
					show: function (){
						this.removeCls("x-unselectable");
					}
				},				
				items: [
					{
						xtype: 'grid',
						itemId: 'detailGrid',
						columnLines: true,
						store: {
							fields: [
								"fileHistoryErrorId",
								"fileHistoryId",
								"recordNumber",
								"errorMessage",
								"fileHistoryErrorType"
							],
							autoLoad: false,
							proxy: {
								type: 'ajax',
								url: 'api/v1/resource/filehistory/'
							}
						}, 
						viewConfig: {
							enableTextSelection: true
						},	
						plugins: [
							{
								ptype: 'rowexpander',
								rowBodyTpl: '{errorMessage}'
							}
						],
						columns: [
							{ text: 'Record Number', dataIndex: 'recordNumber', width: 150 },
							{ text: 'Type', dataIndex: 'fileHistoryErrorType', width: 150 },
							{ text: 'Message', dataIndex: 'errorMessage', flex: 1, minWidth: 150, 
								renderer: function (value) {
									return Ext.String.ellipsis(value, 200);
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
								text: 'Previous',
								id: 'detailWindow-previousBtn',
								iconCls: 'fa fa-lg fa-arrow-left icon-button-color-default',									
								handler: function() {
									actionDetailsNextRecord(false);
								}									
							},
							{
								xtype: 'tbfill'
							},
							{
								text: 'Close',
								iconCls: 'fa fa-lg fa-close icon-button-color-warning',
								handler: function() {
									this.up('window').hide();
								}
							},
							{
								xtype: 'tbfill'
							},
							{
								text: 'Next',
								id: 'detailWindow-nextBtn',
								iconCls: 'fa fa-lg fa-arrow-right icon-button-color-default',
								iconAlign: 'right',
								handler: function() {
									actionDetailsNextRecord(true);
								}									
							}
						]
					}
				]
			});
			
			var actionDetailsNextRecord = function(next) {
				if (next) {
					Ext.getCmp('fileHistoryGrid').getSelectionModel().selectNext();						
				} else {
					Ext.getCmp('fileHistoryGrid').getSelectionModel().selectPrevious();						
				}
				actionViewDetails(Ext.getCmp('fileHistoryGrid').getSelection()[0]);					
			};			
			
			var previewCheckButtons = function() {	
				if (Ext.getCmp('fileHistoryGrid').getSelectionModel().hasPrevious()) {
					Ext.getCmp('detailWindow-previousBtn').setDisabled(false);
				} else {
					Ext.getCmp('detailWindow-previousBtn').setDisabled(true);
				}

				if (Ext.getCmp('fileHistoryGrid').getSelectionModel().hasNext()) {
					Ext.getCmp('detailWindow-nextBtn').setDisabled(false);
				} else {
					Ext.getCmp('detailWindow-nextBtn').setDisabled(true);
				}					
			};			
			
			var actionViewDetails = function(record){
				detailWindow.show();
				detailWindow.setTitle('Details - ' + Ext.util.Format.date(record.get('createDts'), 'm/d/y H:i:s') + ' - ' + record.get('originalFilename'));
				detailWindow.getComponent('detailGrid').getStore().load({
					url: 'api/v1/resource/filehistory/' + record.get('fileHistoryId') + '/errors'
				});
				previewCheckButtons();
			};
			
			var actionReprocess = function(record){
				Ext.Ajax.request({
					url: 'api/v1/resource/filehistory/'+ record.get('fileHistoryId') + '/reprocess',
					method: 'POST',
					success: function(response, opts){
						Ext.toast('Queued file for reprocessing', 'Success', 'br');
						Ext.getCmp('fileHistoryGrid').getStore().reload();
					}
				});
			};
			
			var actionRollback = function(record){
				Ext.Msg.show({
					title:'Rollback Changes?',
					message: 'Are you sure you want to rollback selected file? (May affect live data)',
					buttons: Ext.Msg.YESNO,
					icon: Ext.Msg.QUESTION,
					fn: function(btn) {
						if (btn === 'yes') {
							Ext.Ajax.request({
								url: 'api/v1/resource/filehistory/'+ record.get('fileHistoryId') + '/rollback',
								method: 'POST',
								success: function(response, opts){
									Ext.toast('Rolledback file', 'Success', 'br');
									Ext.getCmp('fileHistoryGrid').getStore().reload();
								}
							});							
						} 
					}
				});
			};			
			
		});		
	</script>	
		
	</stripes:layout-component>
</stripes:layout-render>	
