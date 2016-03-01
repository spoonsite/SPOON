<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
    <stripes:layout-component name="contents">
	
	<script type="text/javascript">
		/* global Ext, CoreUtil */
		Ext.onReady(function(){	
	

			var attributeStore = Ext.create('Ext.data.Store', {
				id: 'attributeStore',
				autoLoad: true,
				sorters: [
					new Ext.util.Sorter({
						property: 'description',
						direction: 'ASC'
					})
				],	
				proxy: {
					type: 'ajax',
					url: '/openstorefront/api/v1/resource/attributes/attributetypes?all=true',
					reader: {
						type: 'json',
						rootProperty: 'data'
					}
				}
			});


			var gridColorRenderer = function gridColorRenderer(value, metadata, record) {
				if (value) 
					metadata.tdCls = 'alert-success';
				else 
					metadata.tdCls = 'alert-danger';
				return value;
			};


			var attributeGrid = Ext.create('Ext.grid.Panel', {
				id: 'attributeGrid',
				title: 'Manage Attributes <i class="fa fa-question-circle"  data-qtip="Attributes are used to categorize components and other listings. They can be searched on and filtered. They represent the metadata for a listing. Attribute Types represent a category and a code represents a specific value. The data is linked by the type and code which allows for a simple change of the description."></i>',
				store: 'attributeStore',
				listeners: {
					selectionchange: function (grid, record, index, opts) {
						if (Ext.getCmp('attributeGrid').getSelectionModel().hasSelection()) {
							Ext.getCmp('attributeGrid-tools-edit').enable();
							Ext.getCmp('attributeGrid-tools-toggleActivation').enable();
							if (record[0].data.activeStatus === 'A') {
								Ext.getCmp('attributeGrid-tools-toggleActivation').setText('Deactivate');
							}
							else {
								Ext.getCmp('attributeGrid-tools-toggleActivation').setText('Activate');
							}
							Ext.getCmp('attributeGrid-tools-delete').enable();
							Ext.getCmp('attributeGrid-tools-export').enable();
						} else {
							Ext.getCmp('attributeGrid-tools-edit').disable();
							Ext.getCmp('attributeGrid-tools-toggleActivation').disable();
							Ext.getCmp('attributeGrid-tools-delete').disable();
							Ext.getCmp('attributeGrid-tools-export').disable();
						}
					}
				},
				columnLines: true,
				columns: [
					{text: 'Description', dataIndex: 'description', flex: 2},
					{text: 'Type Code', dataIndex: 'attributeType', flex: 2},
					{
						text: 'Visible', 
						dataIndex: 'visibleFlg', 
						flex: 1, 
						renderer: gridColorRenderer
					},
					{
						text: 'Required',
						dataIndex: 'requiredFlg',
						flex: 1, 
						renderer: gridColorRenderer
					},
					{
						text: 'Important',
						dataIndex: 'importantFlg',
						flex: 1, 
						renderer: gridColorRenderer
					},
					{
						text: 'Architecture',
						dataIndex: 'architectureFlg',
						flex: 1, 
						renderer: gridColorRenderer
					},
					{
						text: 'Allow Multiple',
						dataIndex: 'allowMultipleFlg',
						flex: 1, 
						renderer: gridColorRenderer
					},
					{
						text: 'Hide On Submission',
						dataIndex: 'hideOnSubmission',
						flex: 1, 
						renderer: gridColorRenderer
					},
					{
						text: 'Default Code',
						dataIndex: 'defaultAttributeCode',
						flex: 1
					},
					{
						text: 'Status',
						dataIndex: 'activeStatus',
						align: 'center',
						flex: 0.5
					}
				],
				dockedItems: [
					{
						dock: 'top',
						xtype: 'toolbar',
						items: [
							Ext.create('OSF.component.StandardComboBox', {
								id: 'attributeFilter-activeStatus',
								emptyText: 'Show All',
								fieldLabel: 'Active Status',
								name: 'activeStatus',
								listeners: {
									change: function (filter, newValue, oldValue, opts) {
										if (newValue === 'A') {
											attributeStore.filter('activeStatus','A');
										}
										else {
											attributeStore.filter('activeStatus', 'I');
										}
									},
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
						dock: 'top',
						items: [
							{
								text: 'Refresh',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-refresh',
								handler: function () {
									attributeStore.load();
								}
							},
							{ 
								xtype: 'tbseparator'
							},
							{
								text: 'Add New Type',
								id: 'attributeGrid-tools-add',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-plus',
								handler: function() {
									actionAddAttribute();
								}
							},
							{
								text: 'Edit',
								id: 'attributeGrid-tools-edit',
								scale: 'medium',
								disabled: true,
								iconCls: 'fa fa-2x fa-edit',
								handler: function() {
									var record = attributeGrid.getSelection()[0];
									actionEditAttribute(record);
								}
							},
							{
								text: 'Deactivate',
								id: 'attributeGrid-tools-toggleActivation',
								scale: 'medium',
								disabled: true,
								iconCls: 'fa fa-2x fa-power-off',
								handler: function() {
									var record = attributeGrid.getSelection()[0];
									actionToggleAttributeStatus(record);
								}
							},
							{
								text: 'Delete',
								id: 'attributeGrid-tools-delete',
								scale: 'medium',
								disabled: true,
								iconCls: 'fa fa-2x fa-trash',
								handler: function() {
									var record = attributeGrid.getSelection()[0];
									actionDeleteAttribute(record);
								}
							},
							{
								xtype: 'tbfill',
							},
							{
								text: 'Import',
								id: 'attributeGrid-tools-import',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-upload',
								handler: function() {
									var record = attributeGrid.getSelection()[0];
									actionImportAttribute(record);
								}
							},
							{
								text: 'Export',
								id: 'attributeGrid-tools-export',
								scale: 'medium',
								disabled: true,
								iconCls: 'fa fa-2x fa-download',
								handler: function() {
									var record = attributeGrid.getSelection()[0];
									actionExportAttribute(record);
								}
							}
						]
					}
				]
			});

			var actionAddAttribute = function actionAddAttribute() {
				Ext.getCmp('editAttributeForm').reset();
				editAttributeWin.edit = false;
				editAttributeWin.setTitle('Add Attribute');
				editAttributeWin.show();
				Ext.getCmp('editAttributeForm-code').setEditable(true);
				Ext.getCmp('editAttributeForm-defaultCode').hide();
			};


			var actionEditAttribute = function actionEditAttribute(record) {
				Ext.getCmp('editAttributeForm').loadRecord(record);
				editAttributeWin.edit = true;
				editAttributeWin.setTitle('Edit Attribute - ' + record.data.attributeType);
				editAttributeWin.show();
				Ext.getCmp('editAttributeForm-defaultCode').show();
				Ext.getCmp('editAttributeForm-code').setEditable(false);
				// Retreive codes to populate form options
				var url = '/openstorefront/api/v1/resource/attributes/attributetypes/';
				url += record.data.attributeType;
				url += '/attributecodeviews';
				Ext.getCmp('editAttributeForm-defaultCode').setStore({
					autoLoad: true,
					proxy: {
						type: 'ajax',
						url: url,
						reader: {
							type: 'json',
							rootProperty: 'data'
						}
					}
				});
			};

			var actionToggleAttributeStatus = function actionToggleAttributeStatus(record) {
				var url = '/openstorefront/api/v1/resource/attributes/attributetypes/';
				url += record.data.attributeType;
				if (record.data.activeStatus === 'A') {
					var what = 'deactivate';
					var method = 'DELETE';
				}
				else {
					var what = 'activate';
					var method = 'POST';
				}
				Ext.Ajax.request({
					url: url,
					method: method,
					success: function(response, opt){
						Ext.toast('Successfully' + what + 'd attribute type', '', 'tr');
						attributeStore.load();
					},
					failure: function(response, opt){
						Ext.toast('Failed to ' + what + ' attribute type', '', 'tr');
					}
				});

			};

			var actionDeleteAttribute = function actionDeleteAttribute(record) {
				var url = '/openstorefront/api/v1/resource/attributes/attributetypes/';
				url += record.data.attributeType + '/force';
				Ext.Ajax.request({
					url: url,
					method: 'DELETE',
					success: function(response, opt){
						Ext.toast('Successfully started deletion of attribute type. Refresh after task completes.', '', 'tr');
						attributeStore.load();
					},
					failure: function(response, opt){
						Ext.toast('Failed to start deletion of attribute type', '', 'tr');
					}
				});
			};

			var actionImportAttribute = function actionImportAttribute(record) {

			};

			var actionExportAttribute = function actionExportAttribute(record) {

			};


			var editAttributeWin = Ext.create('Ext.window.Window', {
					id: 'editAttributeWin',
					title: 'Add/Edit Attribute',
					modal: true,
					width: '50%',
					y: '10em',
					layout: 'fit',
					items: [
						{
							xtype: 'form',
							id: 'editAttributeForm',
							layout: 'vbox',
							scrollable: true,
							bodyStyle: 'padding: 10px;',
							defaults: {
								labelAlign: 'top',
								width: '100%'
							},
							items: [
								{
									xtype: 'textfield',
									id: 'editAttributeForm-label',
									fieldLabel: 'Label<span class="field-required" />',
									name: 'description'
								},
								{
									xtype: 'textfield',
									id: 'editAttributeForm-code',
									fieldLabel: 'Type Code<span class="field-required" />',
									name: 'attributeType',
								},
								{
									xtype: 'combobox',
									fieldLabel: 'Default Code',
									id: 'editAttributeForm-defaultCode',
									displayField: 'code',
									valueField: 'code',
									typeAhead: false,
									editable: false,
									value: '',
									name: 'defaultCode',
									hidden: true,
								},
								{
									xtype: 'panel',
									html: '<b>Detailed Description</b>'
								},
								{
									xtype: 'tinymce_textarea',
									fieldStyle: 'font-family: Courier New; font-size: 12px;',
									style: {border: '0'},
									name: 'detailedDescription',
									width: '100%',
									height: 300,
									maxLength: 255,
									tinyMCEConfig: CoreUtil.tinymceConfig()
								},
								{
									xtype: 'panel',
									html: '<b>Flags:</b>'
								},
								{
									xtype: 'fieldcontainer',
									layout: 'hbox',
									defaultType: 'checkboxfield',
									defaultLayout: '100%',
									defaults: {
										flex: 1
									},
									items: [
										{
											name: 'requiredFlg',
											boxLabel: 'Required'
										},
										{
											name: 'visibleFlg',
											boxLabel: 'Visible'
										},
										{
											name: 'importantFlg',
											boxLabel: 'Important'
										},
										{
											name: 'architectureFlg',
											boxLabel: 'Architecture'
										},
										{
											name: 'allowMultipleFlg',
											boxLabel: 'Allow Multiple'
										},
										{
											name: 'hideOnSubmission',
											boxLabel: 'Hide on Submission',
											listeners: {
												change: function(box, newValue) {
													var select = Ext.getCmp('editAttributeForm-defaultCode');
													if (newValue === true) {
														select.setFieldLabel('Default Code<span class="field-required" />');
														select.allowBlank = false;
													}
													else {

														select.setFieldLabel('Default Code');
														select.allowBlank = true;
														select.clearInvalid();
													}
												}
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
											iconCls: 'fa fa-save',
											formBind: true,
											handler: function () {
												var form = Ext.getCmp('editAttributeForm');
												if (form.isValid()) {
													var formData = form.getValues();
													var edit = editAttributeWin.edit;
													var url = '/openstorefront/api/v1/resource/attributes/attributetypes'
													var method = 'POST';
													if (edit) {
														url += '/' + formData.attributeType;
														method = 'PUT';
													}

													console.log('url: ' + url);
													console.log('me: ' + method);
													console.log(formData);


												}
											}
										},
										{
											xtype: 'tbfill'
										},
										{
											text: 'Cancel',
											iconCls: 'fa fa-close',
											handler: function () {
												Ext.getCmp('editAttributeForm').reset();
												Ext.getCmp('editAttributeWin').hide();
											}
										}
									]
								}
							]
						}
					]
				});

			Ext.create('Ext.container.Viewport', {
				layout: 'fit',
				items: [
					attributeGrid
				]
			});


		});		
		</script>
		</stripes:layout-component>
		</stripes:layout-render>
