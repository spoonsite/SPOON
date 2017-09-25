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

		attributePanel.loadComponentAttributes = function(status) {
			if (!status) {					
				var tools = attributePanel.attributeGrid.getComponent('tools');				
				status = tools.getComponent('attributeFilterActiveStatus').getValue();				
			}

			attributePanel.attributeGrid.setLoading(true);
			var componentId = attributePanel.componentId;
			Ext.Ajax.request({
				url: 'api/v1/resource/components/' + componentId + '/attributes/view',
				method: 'GET',
				params: {
					status: status
				},
				callback: function() {
					attributePanel.attributeGrid.setLoading(false);
				},
				success: function(response, opts) {
					var data = Ext.decode(response.responseText);

					var optionalAttributes = [];
					Ext.Array.each(data, function(attribute) {
						if (!attribute.requiredFlg) {															
							optionalAttributes.push(attribute);
						}
					});
					optionalAttributes.reverse();
					attributePanel.attributeGrid.getStore().loadData(optionalAttributes);
				}
			});			
		};

		attributePanel.attributeGrid = Ext.create('Ext.grid.Panel', {
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
					var fullgrid = attributePanel.attributeGrid;
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
							iconCls: 'fa fa-lg fa-save',
							handler: function(){	
								var form = this.up('form');
								var data = form.getValues();
								var componentId = attributePanel.componentId;

								data.componentAttributePk = {
									attributeType: data.attributeType,
									attributeCode: data.attributeCode
								};
								var valid = true;
								var selectedAttributes = form.queryById('attributeTypeCB').getSelection();
								var attributeType = selectedAttributes.data;
								if (attributeType.attributeValueType === 'NUMBER') {
									if (!Ext.isNumeric(data.attributeCode)) {									
										valid = false;
									}
									if (valid) {
										//check percision; this will enforce max allowed
										try {
											var valueNumber = new Number(data.attributeCode);
											data.attributeCode = valueNumber.toString();
											data.componentAttributePk.attributeCode = valueNumber.toString();
										} catch(e) {
											valid = false;
										}
									}									
								}								
								
								if (!valid) {
									Ext.Msg.show({
										title:'Validation Error',
										message: 'Attribute Code must be numberic with decimal precision <= 20 for this attribute type',
										buttons: Ext.Msg.OK,
										icon: Ext.Msg.ERROR,
										fn: function(btn) {
											if (btn === 'OK') {
												form.getForm().markInvalid({
													attributeCode: 'Must be a number for this attribute Type'
												});
											} 
										}
									});
								} else {
									var method = 'POST';
									var update = '';										

									CoreUtil.submitForm({
										url: 'api/v1/resource/components/' + componentId + '/attributes' + update,
										method: method,
										data: data,
										form: form,
										success: function(){										
											attributePanel.loadComponentAttributes();
											form.reset();
										}
									});
								}
							}
						},
						{
							xtype: 'button',
							text: 'Cancel',										
							iconCls: 'fa fa-lg fa-close',
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
							editable: true,
							typeAhead: true,										
							allowBlank: false,
							valueField: 'attributeType',
							displayField: 'description',										
							store: {
								autoLoad: false,
								proxy: {
									type: 'ajax',
									url: 'api/v1/resource/attributes'									
								},
								filters: [
									{
										property: 'requiredFlg',
										value: 'false'
									}
								],
								listeners: {
									load: function(store, records, opts) {
										store.filterBy(function(attribute){
											if (attribute.associatedComponentTypes) {
												var optFound = Ext.Array.findBy(attribute.associatedComponentTypes, function(item) {
													if (item.componentType === attributePanel.component.componentType) {
														return true;
													} else {
														return false;
													}
												});
												if (optFound) {
													return true;
												} else {
													return false;
												}
											}else {
												return true;
											}
										});
									}
								}
							},
							listeners: {
								change: function (field, newValue, oldValue, opts) {
									field.up('form').getComponent('attributeCodeCB').clearValue();

									var record = field.getSelection();		
									if (record) {
										field.up('form').getComponent('attributeCodeCB').getStore().loadData(record.data.codes);
										
										if (record.get("allowUserGeneratedCodes")) {																							
											field.up('form').getComponent('attributeCodeCB').setEditable(true);
										} else {											
											field.up('form').getComponent('attributeCodeCB').setEditable(false);
										}																				
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
					itemId: 'tools',
					items: [
						{
							xtype: 'combobox',
							itemId: 'attributeFilterActiveStatus',
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
									attributePanel.loadComponentAttributes(newValue);
								}
							}
						}, 								
						{
							text: 'Refresh',
							iconCls: 'fa fa-lg fa-refresh icon-button-color-refresh',
							handler: function(){
								attributePanel.loadComponentAttributes();
							}
						},								
						{
							xtype: 'tbseparator'
						},
						{
							text: 'Toggle Status',
							itemId: 'toggleStatusBtn',
							iconCls: 'fa fa-lg fa-power-off icon-button-color-default',									
							disabled: true,
							handler: function(){
								CoreUtil.actionSubComponentToggleStatus(attributePanel.attributeGrid, 'type', 'attributes', 'code', null, null, function(){
									attributePanel.loadComponentAttributes();
								});
							}
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Delete',
							itemId: 'removeBtn',
							iconCls: 'fa fa-trash fa-lg icon-button-color-warning',									
							disabled: true,
							handler: function(){
								CoreUtil.actionSubComponentToggleStatus(attributePanel.attributeGrid, 'type', 'attributes', 'code', null, true, function(){
									attributePanel.loadComponentAttributes();
								});
							}
						}
					]
				}
			]																					
		});

		attributePanel.add(attributePanel.attributeGrid);
	},	
	loadData: function(evaluationId, componentId, data, opts) {
		//just load option (filter out required)
		var attributePanel = this;
		
		attributePanel.componentId = componentId;
		attributePanel.attributeGrid.componentId = componentId;
		attributePanel.loadComponentAttributes();
		
		var form = attributePanel.attributeGrid.down('form');
		form.setLoading(true);
		Ext.Ajax.request({
			url: 'api/v1/resource/components/' + attributePanel.componentId,
			callback: function() {
				form.setLoading(false);
			},
			success: function(response, opts) {
				var component = Ext.decode(response.responseText);
				attributePanel.component = component;
				attributePanel.attributeGrid.down('form').getComponent('attributeTypeCB').getStore().load();
			}
		});
		
		
		if (opts && opts.commentPanel) {
			opts.commentPanel.loadComments(evaluationId, "Attribute", componentId);
		}
	}
	
});


