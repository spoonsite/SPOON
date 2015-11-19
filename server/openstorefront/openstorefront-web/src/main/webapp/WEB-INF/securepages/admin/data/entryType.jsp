<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
	<stripes:layout-component name="contents">
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function(){	
				
				var addEditWin = Ext.create('Ext.window.Window', {
					id: 'addEditWin',
					title: 'Entry',
					modal: true,
					width: '40%',
					items: [
						{
							xtype: 'form',
							id: 'entryForm',
							layout: 'vbox',
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
									maxLength: 255
								},				
								{
									xtype: 'panel',
									html: 'Data Entry'
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
									boxLabel: 'Dependancies',
									name: 'dataEntryDependancies'
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
								Ext.create('OSF.component.StandardComboBox', {
									name: 'componentTypeTemplate',																		
									width: '100%',
									margin: '0 0 0 0',
									fieldLabel: 'Override Template',
									emptyText: 'Default',
									storeConfig: {
										url: '../api/v1/resource/componenttypetemplates/lookup'
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
												var url = Ext.getCmp('entryForm').edit ? '../api/v1/resource/componenttypes/' + data.componentType : '../api/v1/resource/componenttypes';       

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
							'updateDts',	
							'activeStatus',
							'label',
							'description',
							'componentTypeTemplate'
						],
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: '../api/v1/resource/componenttypes',
							extraParams: {
								all: true
							}
						}
					}),
					columnLines: true,
					columns: [						
						{ text: 'Type Code', dataIndex: 'componentType', width: 125 },
						{ text: 'Type', dataIndex: 'label', width: 200 },
						{ text: 'Description', dataIndex: 'description', flex: 1, minWidth: 200 },
						{ text: 'Template Override', dataIndex: 'componentTypeTemplate', width: 150 },
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
									xtype: 'tbseparator'
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
				
				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [
						entryGrid
					]
				});
				
				var checkEntryGridTools = function() {
					if (Ext.getCmp('entryGrid').getSelectionModel().getCount() === 1) {
						Ext.getCmp('lookupGrid-tools-edit').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-status').setDisabled(false);
					} else {
						Ext.getCmp('lookupGrid-tools-edit').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-status').setDisabled(true);						
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
					Ext.getCmp('entryGrid').setLoading("Updating Status");
					var type = Ext.getCmp('entryGrid').getSelection()[0].get('type');
					var currentStatus = Ext.getCmp('entryGrid').getSelection()[0].get('activeStatus');
					
					var method = 'PUT';
					var urlEnd = '/activate';
					if (currentStatus === 'A') {
						method = 'DELETE';
						urlEnd = '';
					}					
					Ext.Ajax.request({
						url: '../api/v1/resource/componenttypes/' + type + urlEnd,
						method: method,
						success: function(response, opts){
							Ext.getCmp('entryGrid').setLoading(false);
							actionRefreshEntryGrid();
						},
						failure: function(response, opts){
							Ext.getCmp('entryGrid').setLoading(false);
						}
					});
				};
				
			});
		</script>	
		
	</stripes:layout-component>
</stripes:layout-render>