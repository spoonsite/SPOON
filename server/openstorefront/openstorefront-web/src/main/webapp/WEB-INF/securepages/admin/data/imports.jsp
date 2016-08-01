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
						bodyStyle: 'padding: 20px;',
						layout: 'anchor',
						autoScroll: true,
						items: [
							{
								xtype: 'panel',
								title: 'Fields',
								collapsible: true,
								titleCollapse: true,
								layout: 'anchor',
								items: [
									{
										xtype: 'container',
										layout: 'hbox',
										items: [
											{
												xtype: 'filefield',
												name: 'upload',
												fieldLabel: 'Sample File with Fields',
												labelAlign: 'top',
												flex: 1,
												margin: '0 10 0 0'
											},
											{
												xtype: 'button',
												text: 'Upload',
												iconCls: 'fa fa-upload',
												handler: function() {
													
												},
												margin: '25 0 0 0'
											}
										]
									},									
									{
										xtype: 'fieldset',
										title: 'Add/Edit Field Mapping',
										layout: 'anchor',
										defaults: {
											labelAlign: 'top',
											width: '100%'
										},
										items: [
											{
												xtype: 'combo',
												name: 'field',
												fieldLabel: 'File Field<span class="field-required" />',
												valueField: 'code',
												displayField: 'description',
												store: {
													autoLoad: false													
												},
												editable: false,
												typeAhead: false												
											},
											{
												xtype: 'tagfield',
												name: 'transforms',
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
												name: 'entity',
												fieldLabel: 'Entities<span class="field-required" />',
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
														var entityField = cb.up().getComponent('entityfieldId');
														entityField.getStore().load({
															url: '../api/v1/service/datamapping/entityfields/' + newValue
														});
														var pathEntityfield = cb.up().getComponent('pathEntityfieldId');
														pathEntityfield.getStore().load({
															url: '../api/v1/service/datamapping/entityfields/' + newValue
														});																												
													}
												}
											},											
											{
												xtype: 'combo',
												itemId: 'entityfieldId',
												name: 'entityfield',
												fieldLabel: 'Entity Field<span class="field-required" />',
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
												name: 'setPathToEnityField',
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
												xtype: 'combo',
												name: 'pathTransforms',
												fieldLabel: 'Entities',
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
														var entityField = cb.up().getComponent('entityfieldId');
														entityField.getStore().load({
															url: '../api/v1/service/datamapping/entityfields/' + newValue
														});
													}
												}
											},											
											{
												xtype: 'button',
												text: 'Add',
												margin: '0 0 10 0',
												width: '150',
												iconCls: 'fa fa-plus',
												handler: function() {
													
												}
											},
											{
												xtype: 'grid',
												columnLines: true,											
												height: 350,
												margin: '0 0 10 0',
												border: true,
												store: {
													
												},
												columns: [
													{ text: 'File Field', dataIndex: 'field', flex: 1, minWidth: 150},
													{ text: 'Tranforms', dataIndex: 'fieldTransforms', width: 200},
													{ text: 'Entity Class', dataIndex: 'entityClass', width: 150},
													{ text: 'Entity Field', dataIndex: 'entityField', width: 150},													
													{ text: 'Use As Attribute Label', dataIndex: 'useAsAttributeLabel', width: 125},
													{ text: 'Concatenate', dataIndex: 'concatenate', width: 125},
													{ text: 'Add Path to Value', dataIndex: 'addEndPathToValue', width: 125},
													{ text: 'Add Path to Entity Field', dataIndex: 'setPathToEnityField', width: 125},
													{ text: 'Path Tranforms', dataIndex: 'pathTransforms', width: 200}
												],
												dockedItems: [
													{
														xtype: 'toolbar',
														dock: 'top',
														items: [
															{
																text: 'Edit',
																disabled: true,
																iconCls: 'fa fa-edit',
																handler: function() {
																	
																}
															},
															{
																xtype: 'tbfill'
															},
															{
																text: 'Remove',
																disabled: true,
																iconCls: 'fa fa-close',
																handler: function() {
																	
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
								title: 'Attributes',
								collapsible: true,
								titleCollapse: true,
								items: [
									{
										xtype: 'checkbox',
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
												xtype: 'combo',
												name: 'attributeType',
												fieldLabel: 'Attribute Type<span class="field-required" />',
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
											},
											{
												xtype: 'button',
												text: 'Add',
												margin: '0 0 10 0',
												width: '150',
												iconCls: 'fa fa-plus',
												handler: function() {
													
												}
											},
											{
												xtype: 'grid',
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
												dockedItems: [
													{
														xtype: 'toolbar',
														dock: 'top',
														items: [
															{
																text: 'Add Codes',
																disabled: true,
																iconCls: 'fa fa-plus',
																handler: function() {
																	
																}																
															},
															{
																text: 'Edit',
																disabled: true,
																iconCls: 'fa fa-edit',
																handler: function() {
																	
																}
															},
															{
																xtype: 'tbfill'
															},
															{
																text: 'Remove',
																disabled: true,
																iconCls: 'fa fa-close',
																handler: function() {
																	
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
										formBind: true,
										scale: 'medium',
										iconCls: 'fa fa-2x fa-save',
										handler: function() {
											
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
					{ text: 'Name', dataIndex: 'name', flex: 1, minWidth: 200 } 
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
									var value = mappingPanel.getComponent('filterbar').getComponent('fileFormatFilter').getValue();
									if (value) {
										mappingPanel.getStore().refresh();
									}
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
									addEditMapping.show();
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
									var record = mappingPanel.getSelectionModel().getSelection()[0];
									
									addEditMapping.show();
									
									//TODO: load Mapping
									
																	
									
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
												
											} 
										}
									});									
								}
							}							
						]
					}					
				]
				
			});
			
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
