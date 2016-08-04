<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
    <stripes:layout-component name="contents">
	
	<script src="scripts/component/importWindow.js?v=${appVersion}" type="text/javascript"></script>	
		
	<script type="text/javascript">
		/* global Ext, CoreUtil */
		Ext.onReady(function(){	
			
			var importWindow = Ext.create('OSF.component.ImportWindow', {					
				fileTypeReadyOnly: false,
				uploadSuccess: function(form, action) {
					Ext.getCmp('fileHistoryGrid').getStore().reload();
				}
			});
			
			var selectedMapFormat;
			
			var addEditAttributeCodeWin = Ext.create('Ext.window.Window', {
				title: 'Manage Codes',
				modal: true,
				alwaysOnTop: true,
				width: '60%',
				height: '50%',
				maximizable: true,
				layout: 'fit',				
				items: [
					{
						xtype: 'grid',
						id: 'codeGrid',
						columnLines: true,
						store: {							
						},
						columns: [
							{ text: 'Attribute Code', dataIndex: 'attributeCode', width: 250 },
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
										valueField: 'attributeType',
										displayField: 'description',
										store: {
											autoLoad: false,
											proxy: {
												type: 'ajax',
												url: '../api/v1/resource/attributes/attributetypes/{type}/attributecodes'
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
												iconCls: 'fa fa-2x fa-plus',
												handler: function() {
													this.setText('Add');
													var form = this.up('form');
													var data = form.getValues();
													var grid = form.up('grid');

													if (grid.editRecord) {
														grid.editRecord.set(data, {
															dirty: false
														});
														grid.editRecord = null;
													} else {																		
														grid.getStore().add(data);
													}
													form.reset();
												}
											},
											{
												xtype: 'button',
												text: 'Cancel',
												margin: '8 0 00 0',
												iconCls: 'fa  fa-close',
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
										iconCls: 'fa fa-edit',
										handler: function(){
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
										text: 'Remove',
										itemId: 'remove',
										disabled: true,
										iconCls: 'fa fa-remove',
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
				modal: true,
				width: '80%',
				height: '80%',
				maximizable: true,
				layout: 'fit',
				items: [
					{
						xtype: 'form',
						itemId: 'mainForm',
						bodyStyle: 'padding: 20px;',
						layout: 'anchor',
						autoScroll: true,
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
												handler: function() {
													var uploadForm = this.up('form');
													var fileFieldCB = Ext.getCmp('fieldForm').getComponent('fileFieldCB');
													
													uploadForm.submit({
														url: '../Upload.action?DataMapFields&fileFormat='+selectedMapFormat.get('code'),
														method: 'POST',
														success: function(action, opts) {
															var fieldData = Ext.decode(opts.response.responseText);															
															fileFieldCB.getStore().loadData(fieldData.data);
															Ext.toast('Loaded field from file', 'Upload Success');
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
												margin: '25 0 0 0'
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
																url: '../api/v1/service/datamapping/transforms'
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
																url: '../api/v1/service/datamapping/mappingentities'
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
																		url: '../api/v1/service/datamapping/entityfields/' + newValue
																	});
																	var pathEntityfield = cb.up().getComponent('pathEntityfieldId');
																	pathEntityfield.reset();
																	pathEntityfield.getStore().load({
																		url: '../api/v1/service/datamapping/entityfields/' + newValue
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
																url: '../api/v1/service/datamapping/entityfields/{entity}'
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
																url: '../api/v1/service/datamapping/entityfields/{entity}'
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
																url: '../api/v1/service/datamapping/transforms'
															}
														},
														editable: true,
														typeAhead: false
													},
													{
														xtype: 'container',
														layout: 'hbox',
														items: [
															{
																xtype: 'button',
																id: 'fieldAddBtn',
																text: 'Add',
																formBind: true,
																margin: '0 20 10 0',
																width: '200',
																scale: 'medium',
																iconCls: 'fa fa-2x fa-plus',
																handler: function() {
																	this.setText('Add');
																	var form = this.up('form');
																	var data = form.getValues();
																	var grid = form.up().getComponent('fieldGrid');
																	
																	if (grid.editRecord) {
																		grid.editRecord.set(data, {
																			dirty: false
																		});
																		grid.editRecord = null;
																	} else {																		
																		grid.getStore().add(data);
																	}
																	form.reset();
																}
															},
															{
																xtype: 'button',
																text: 'Cancel',
																margin: '8 0 00 0',
																iconCls: 'fa  fa-close',
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
													{ text: 'File Field', dataIndex: 'field', flex: 1, minWidth: 150},
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
													{ text: 'Use As Attribute Label', dataIndex: 'useAsAttributeLabel', width: 125},
													{ text: 'Concatenate', dataIndex: 'concatenate', width: 125},
													{ text: 'Add Path to Value', dataIndex: 'addEndPathToValue', width: 125},
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
																iconCls: 'fa fa-edit',
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
																text: 'Remove',
																itemId: 'remove',
																disabled: true,
																iconCls: 'fa fa-close',
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
																url: '../api/v1/resource/attributes/attributetypes'
															}
														},
														editable: false,
														typeAhead: false,
														listeners: {
															change: function(cb, newValue, oldValue) {
																var defaultMappedCode = cb.up().getComponent('defaultMappedCode');
																defaultMappedCode.getStore().load({
																	url: '../api/v1/resource/attributes/attributetypes/' + newValue + '/attributecodes'
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
														valueField: 'attributeType',
														displayField: 'description',
														store: {
															autoLoad: false,
															proxy: {
																type: 'ajax',
																url: '../api/v1/resource/attributes/attributetypes'
															}
														},
														editable: false,
														typeAhead: false												
													}
												]
											},
											{
												xtype: 'container',
												layout: 'hbox',
												items: [
													{
														xtype: 'button',
														id: 'attributeTypeAddBtn',
														text: 'Add',
														formBind: true,
														margin: '0 20 10 0',
														width: '200',
														scale: 'medium',
														iconCls: 'fa fa-2x fa-plus',
														handler: function() {
															this.setText('Add');
															var form = this.up('form');
															var data = form.getValues();
															var grid = form.up().getComponent('attributeTypeGrid');

															if (grid.editRecord) {
																grid.editRecord.set(data, {
																	dirty: false
																});
																grid.editRecord = null;
															} else {																		
																grid.getStore().add(data);
															}
															form.reset();
														}
													},
													{
														xtype: 'button',
														text: 'Cancel',
														margin: '8 0 00 0',
														iconCls: 'fa  fa-close',
														handler: function() {
															Ext.getCmp('attributeTypeAddBtn').setText('Add');
															var form = this.up('form');
															form.reset();
														}
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
													{ text: 'Attribute Type', dataIndex: 'attributeType', flex: 1, minWidth: 150},													
													{ text: 'External Type', dataIndex: 'externalType', width: 225},
													{ text: 'Add Missing Code', dataIndex: 'addMissingCode', width: 150},													
													{ text: 'Use As Attribute Label', dataIndex: 'defaultMappedCode', width: 200}
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
																text: 'Manages Codes',
																itemId: 'addCodes',
																disabled: true,
																iconCls: 'fa fa-plus',
																handler: function() {
																	var grid = this.up('grid');	
																	var record = grid.getSelectionModel().getSelection()[0];
																	
																	addEditAttributeCodeWin.show();
																	//load code for type for CB
																	//load code mappings on the record
																	
																}																
															},
															{
																text: 'Edit',
																itemId: 'edit',
																disabled: true,
																iconCls: 'fa fa-edit',
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
																text: 'Remove',
																itemId: 'remove',
																disabled: true,
																iconCls: 'fa fa-close',
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
										iconCls: 'fa fa-2x fa-save',
										handler: function() {
											var mainForm = this.up('form');
											var mainFormData = mainForm.getValues();
											//name is only thing required
											
											if (!mainFormData.name) {
												Ext.Msg.show({
													title:'Validation',
													message: 'Data Mapping Name is Required',
													buttons: Ext.Msg.OK,
													icon: Ext.Msg.ERROR,
													fn: function(btn) {
													}
												});												
											} else {
											
												var dataMapModel = {
													fileDataMap: {
														fileFormat: selectedMapFormat.get('code'),
														name: mainFormData.name,
														dataMapFields: []
													},
													fileAttributeMap: {
														attributeTypeXrefMap: []
													}
												}

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
													url: '../api/v1/resource/filehistory/formats/' + selectedMapFormat.get('code') + '/mappings' + endURL,
													method: method,
													jsonData: dataMapModel,
													callback: function(){		
														mainForm.setLoading(false);
													},
													success: function(response, opts) {
														Ext.toast('Saved mapping', 'Saved');
														actionRefreshMappings();
														addEditMapping.close();
													}
												});
											}
											
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Cancel',
										scale: 'medium',
										iconCls: 'fa fa-2x fa-close',
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
			
			
			var mappingPanel = Ext.create('Ext.grid.Panel', {
				title: 'Mapping',
				columnLines: true,
				store: {
					autoLoad: false,
					proxy: {
						type: 'ajax',
						url: '../api/v1/resource/filehistory/formats/{format}/mappings'
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
							tools.getComponent('remove').setDisabled(false);
						} else {
							tools.getComponent('edit').setDisabled(true);
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
										url: '../api/v1/resource/filehistory/formats/mappingformats'
									}
								},
								editable: false,
								typeAhead: false,
								listeners: {
									change: function(cb, newValue, oldValue) {
										mappingPanel.getStore().load({
											url: '../api/v1/resource/filehistory/formats/' + newValue + '/mappings'
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
								iconCls: 'fa  fa-2x fa-refresh',
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
								iconCls: 'fa fa-2x fa-plus',
								handler: function() {
									actionShowMappingForm();
								}
							},
							{
								text: 'Edit',
								itemId: 'edit',
								disabled: true,
								scale: 'medium',
								iconCls: 'fa fa-2x fa-edit',
								handler: function() {
									var grid = mappingPanel;
									var mapRecord = mappingPanel.getSelectionModel().getSelection()[0];
									
									actionShowMappingForm();
									
									addEditMapping.getComponent('mainForm').setLoading(true);
									Ext.Ajax.request({
										url: '../api/v1/resource/filehistory/formats/' + selectedMapFormat.get('code') 
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
												name: dataMap.fileDataMap.name
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
											if (!dataMap.fileAttributeMap.attributeCodeXrefMap) {
												dataMap.fileAttributeMap.attributeCodeXrefMap = [];
											}											
											attributeTypeGrid.getStore().loadData(dataMap.fileAttributeMap.attributeCodeXrefMap);
											
										}
									});							
									
								}
							},							
							{
								xtype: 'tbfill'
							},
							{
								text: 'Remove',
								itemId: 'remove',
								disabled: true,								
								scale: 'medium',
								iconCls: 'fa fa-2x fa-close',
								handler: function() {
									var grid = mappingPanel;
									var record = mappingPanel.getSelectionModel().getSelection()[0];									
									
									Ext.Msg.show({
										title:'Remove Mapping',
										message: 'Are you sure you want to remove this mapping?',
										buttons: Ext.Msg.YESNO,
										icon: Ext.Msg.QUESTION,
										fn: function(btn) {
											if (btn === 'yes') {
												
												grid.setLoading("Removing...");
												Ext.Ajax.request({
													url: '../api/v1/resource/filehistory/formats/' + selectedMapFormat.get('code') 
														+ '/mappings/' + record.get('code'),
													method: 'DELETE',
													callback: function() {
														grid.setLoading(false);
													},
													success: function(response, opts) {
														actionRefreshMappings();
													}
												})
												
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
				} else {
					addEditMapping.getComponent('mainForm').getComponent('attributeMapping').setHidden(false);
				}
				addEditMapping.getComponent('mainForm').reset();	
				
				//reset grids
				
		
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
					url: '../api/v1/resource/filehistory',					
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
								iconCls: 'fa fa-2x fa-refresh',
								handler: function () {
									this.up('grid').getStore().reload();
								}
							},
							{
								xtype: 'tbseparator'
							},
							{
								text: 'View Details',
								itemId: 'viewDetailsBtn',
								scale: 'medium',								
								iconCls: 'fa fa-2x fa-binoculars',
								disabled: true,
								handler: function () {
									actionViewDetails(Ext.getCmp('fileHistoryGrid').getSelectionModel().getSelection()[0]);
								}
							},
							{
								xtype: 'tbseparator'
							},							
							{					
								text: 'Import',								
								scale: 'medium',								
								iconCls: 'fa fa-2x fa-upload',
								handler: function () {
									importWindow.show();
								}
							},							
							{
								xtype: 'tbfill'
							},
							{
								text: 'Reprocess',
								itemId: 'reprocessBtn',
								scale: 'medium',								
								iconCls: 'fa fa-2x fa-reply',
								disabled: true,
								handler: function () {
									actionReprocess(Ext.getCmp('fileHistoryGrid').getSelectionModel().getSelection()[0]);
								}
							},
							{
								text: 'Rollback',
								itemId: 'rollbackBtn',
								scale: 'medium',								
								iconCls: 'fa fa-2x fa-close',
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
								iconCls: 'fa fa-2x fa-download',
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
			
			Ext.create('Ext.container.Viewport', {
				layout: 'fit',
				items: [
					mainPanel
				]
			});
			
			var actionUpdateHistoryKeepTime = function() {				
				Ext.Ajax.request({
					url: '../api/v1/service/application/configproperties/filehistory.max.days',
					success: function (response) {
						var keyData = Ext.decode(response.responseText);
						Ext.getCmp('historyKeepTimePanel').update('*History is only kept for <b>' + keyData.description + '</b> day(s)');
					}
				});
			};
			actionUpdateHistoryKeepTime();
			
			var actionDownload = function(record) {
				window.location.href = '../api/v1/resource/filehistory/' + record.get('fileHistoryId') + '/download';
			};
			
			var detailWindow = Ext.create('Ext.window.Window', {
				title: 'Details',
				modal: true,
				layout: 'fit',
				width: '60%',
				height: '40%',
				maximizable: true,
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
								url: '../api/v1/resource/filehistory/'
							}
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
								iconCls: 'fa fa-arrow-left',									
								handler: function() {
									actionDetailsNextRecord(false);
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
								id: 'detailWindow-nextBtn',
								iconCls: 'fa fa-arrow-right',
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
					url: '../api/v1/resource/filehistory/' + record.get('fileHistoryId') + '/errors'
				});
				previewCheckButtons();
			};
			
			var actionReprocess = function(record){
				Ext.Ajax.request({
					url: '../api/v1/resource/filehistory/'+ record.get('fileHistoryId') + '/reprocess',
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
								url: '../api/v1/resource/filehistory/'+ record.get('fileHistoryId') + '/rollback',
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
