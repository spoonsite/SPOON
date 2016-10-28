/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/* global Ext, CoreUtil */

Ext.define('OSF.form.Attributes', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.form.Attributes',

	layout: 'fit',
	initComponent: function () {		
		this.callParent();
		
		var attributePanel = this;

		var attributeGrid = Ext.create('Ext.grid.Panel', {
			columnLines: true,
			store: Ext.create('Ext.data.Store', {
				fields: [
					"type",
					"code",
					"typeDescription",
					"codeDescription",
					"orphan",
					"activeStatus",
					{
						name: 'updateDts',
						type:	'date',
						dateFormat: 'c'
					}
				],
				autoLoad: false,
				proxy: {
					type: 'ajax'							
				}
			}),				
			columns: [
				{ text: 'Attribute Type', dataIndex: 'typeDescription',  width: 200 },
				{ text: 'Attribute Code', dataIndex: 'codeDescription', flex: 1, minWidth: 200 },
				{ text: 'Update Date', dataIndex: 'updateDts', width: 175, xtype: 'datecolumn', format: 'm/d/y H:i:s' }
			],
			listeners: {
				selectionchange: function(grid, record, index, opts){
					var fullgrid = Ext.getCmp('attributeGrid');
					if (fullgrid.getSelectionModel().getCount() === 1) {								
						fullgrid.down('toolbar').getComponent('toggleStatusBtn').setDisabled(false);
						fullgrid.down('toolbar').getComponent('removeBtn').setDisabled(false);
					} else {								
						fullgrid.down('toolbar').getComponent('toggleStatusBtn').setDisabled(true);
						fullgrid.down('toolbar').getComponent('removeBtn').setDisabled(true);
					}
				}						
			},
			dockedItems: [
				{
					xtype: 'form',
					title: 'Add Attribute',
					collapsible: true,
					titleCollapse: true,
					border: true,
					layout: 'vbox',
					bodyStyle: 'padding: 10px;',
					margin: '0 0 5 0', 
					defaults: {
						labelAlign: 'top',
						labelSeparator: '',
						width: '100%'
					},
					buttonAlign: 'center',
					buttons: [
						{
							xtype: 'button',
							text: 'Save',
							formBind: true,
							margin: '0 20 0 0',
							iconCls: 'fa fa-save',
							handler: function(){	
								var form = this.up('form');
								var data = form.getValues();
								var componentId = Ext.getCmp('attributeGrid').componentRecord.get('componentId');

								data.componentAttributePk = {
									attributeType: data.attributeType,
									attributeCode: data.attributeCode
								};

								var method = 'POST';
								var update = '';										

								CoreUtil.submitForm({
									url: 'api/v1/resource/components/' + componentId + '/attributes' + update,
									method: method,
									data: data,
									form: form,
									success: function(){
										loadComponentAttributes(Ext.getCmp('attributeFilterActiveStatus').getValue());
										form.reset();
									}
								});
							}
						},
						{
							xtype: 'button',
							text: 'Cancel',										
							iconCls: 'fa fa-close',
							handler: function(){
								this.up('form').reset();
							}									
						}								
					],
					items: [
						{
							xtype: 'combobox',
							itemId: 'attributeTypeCB',
							fieldLabel: 'Attribute Type <span class="field-required" />',
							name: 'attributeType',
							forceSelection: true,	
							queryMode: 'local',
							editable: false,
							typeAhead: false,										
							allowBlank: false,
							valueField: 'attributeType',
							displayField: 'description',										
							store: Ext.create('Ext.data.Store', {
								fields: [
									"attributeType",
									"description"
								]																							
							}),
							listeners: {
								change: function (field, newValue, oldValue, opts) {
									field.up('form').getComponent('attributeCodeCB').clearValue();

									var record = field.getSelection();		
									if (record) {
										field.up('form').getComponent('attributeCodeCB').getStore().loadData(record.data.codes);
									} else {
										field.up('form').getComponent('attributeCodeCB').getStore().removeAll();
									}
								}
							}
						},
						{
							xtype: 'combobox',
							itemId: 'attributeCodeCB',
							fieldLabel: 'Attribute Code <span class="field-required" />',
							name: 'attributeCode',
							forceSelection: true,	
							queryMode: 'local',
							editable: false,
							typeAhead: false,										
							allowBlank: false,
							valueField: 'code',
							displayField: 'label',										
							store: Ext.create('Ext.data.Store', {
								fields: [
									"code",
									"label"
								]																							
							})									
						}
					]
				},						
				{
					xtype: 'toolbar',
					items: [
						{
							xtype: 'combobox',
							id: 'attributeFilterActiveStatus',
							fieldLabel: 'Filter Status',
							store: {
								data: [												
									{ code: 'A', description: 'Active' },
									{ code: 'I', description: 'Inactive' }
								]
							},
							forceSelection: true,
							queryMode: 'local',
							displayField: 'description',
							valueField: 'code',
							value: 'A',
							listeners: {
								change: function(combo, newValue, oldValue, opts){
									loadComponentAttributes(newValue);
								}
							}
						}, 								
						{
							text: 'Refresh',
							iconCls: 'fa fa-refresh',
							handler: function(){
								loadComponentAttributes(Ext.getCmp('attributeFilterActiveStatus').getValue());
							}
						},								
						{
							xtype: 'tbfill'
						},
						{
							text: 'Toggle Status',
							itemId: 'toggleStatusBtn',
							iconCls: 'fa fa-power-off',									
							disabled: true,
							handler: function(){
								actionSubComponentToggleStatus(Ext.getCmp('attributeGrid'), 'type', 'attributes', 'code', null, null, function(){
									loadComponentAttributes(Ext.getCmp('attributeFilterActiveStatus').getValue());
								});
							}
						},								
						{
							text: 'Remove',
							itemId: 'removeBtn',
							iconCls: 'fa fa-trash',									
							disabled: true,
							handler: function(){
								actionSubComponentToggleStatus(Ext.getCmp('attributeGrid'), 'type', 'attributes', 'code', null, true, function(){
									loadComponentAttributes(Ext.getCmp('attributeFilterActiveStatus').getValue());
								});
							}
						}
					]
				}
			]																					
		});

		attributePanel.add(attributeGrid);
	}
	
});


