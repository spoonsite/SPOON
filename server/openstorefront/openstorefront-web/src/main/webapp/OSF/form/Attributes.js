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
/* global Ext, CoreUtil, CoreService */

Ext.require('OSF.form.MultipleAttributes');
Ext.define('OSF.form.Attributes', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.form.Attributes',
	requiredPermissions: ['ADMIN-ENTRY-ATTR-MANAGEMENT'],
	beforePermissionsCheckFailure: function () { return false; },
	beforePermissionsCheckSuccess: function () { return false; },
	preventDefaultAction: true,

	layout: 'fit',
	
	initComponent: function () {

		this.callParent();

		var attributePanel = this;

		attributePanel.loadComponentAttributes = function (status) { 
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
				callback: function () {
					attributePanel.attributeGrid.setLoading(false);
				},
				success: function (response, opts) {
					var data = Ext.decode(response.responseText);

					var optionalAttributes = [];
					Ext.Array.each(data, function (attribute) {
						var required = false;
						
						if (attribute.requiredRestrictions) {
							Ext.Array.each(attribute.requiredRestrictions, function(restriction) {
								if (restriction.componentType === attributePanel.component.componentType) {
									required = true;
								}
							});
						}						
						if (!required) {
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
			viewConfig: {
				enableTextSelection: true
			},
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
						type: 'date',
						dateFormat: 'c'
					}
				],
				autoLoad: false,
				proxy: {
					type: 'ajax'
				}
			}),
			columns: [
				{text: 'Attribute Type', dataIndex: 'typeDescription', width: 200},
				{text: 'Attribute Code', dataIndex: 'codeDescription', flex: 1, minWidth: 200},
				{text: 'Comment', dataIndex: 'comment', flex: 2, minWidth: 200},
				{text: 'Private Flag', dataIndex: 'privateFlag', width: 150},
				{text: 'Update Date', dataIndex: 'updateDts', width: 175, xtype: 'datecolumn', format: 'm/d/y H:i:s'}
			],
			listeners: {
				selectionchange: function (grid, record, index, opts) {
					var fullgrid = attributePanel.attributeGrid;
					if (fullgrid.getSelectionModel().getCount() === 1) {
						fullgrid.down('toolbar').getComponent('toggleStatusBtn').setDisabled(false);
						fullgrid.down('toolbar').getComponent('edit').setDisabled(false);						
						fullgrid.down('toolbar').getComponent('removeBtn').setDisabled(false);
					} else {
						fullgrid.down('toolbar').getComponent('toggleStatusBtn').setDisabled(true);
						fullgrid.down('toolbar').getComponent('edit').setDisabled(true);
						fullgrid.down('toolbar').getComponent('removeBtn').setDisabled(true);
					}
				}
			},
			dockedItems: [
				{
					xtype: 'form',
					itemId: 'form',
					title: 'Add Attribute',
					collapsible: true,
					titleCollapse: true,
					border: true,
					layout: 'vbox',
					bodyStyle: 'padding: 10px;',
					margin: '0 0 5 0',
					defaults: {
						labelAlign: 'right',
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
							handler: function () {
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
								if (attributeType.attributeValueType === 'NUMBER' && Ext.String.endsWith(data.attributeCode, ".")) {																			//check percision; this will enforce max allowed
									try {
										var valueNumber = new Number(data.attributeCode);
										if (isNaN(valueNumber))
											throw "Bad Format";
										data.attributeCode = valueNumber.toString();
										data.componentAttributePk.attributeCode = valueNumber.toString();
									} catch (e) {
										valid = false;
										form.getForm().markInvalid({
											attributeCode: 'Number must not have a decimal point or have at least one digit after the decimal point.'
										});
									}
								}
								if (valid)
								{
									CoreUtil.submitForm({
										url: 'api/v1/resource/components/' + componentId + '/attributes',
										method: 'POST',
										data: data,
										form: form,
										success: function () {
											attributePanel.loadComponentAttributes();
											form.reset();
										}
									});
								}
							}
						},
						{
							xtype: 'button',
							text: 'Add Multiple Attributes',
							iconCls: 'fa fa-lg fa-plus',
							margin: '0 20 0 0',
							handler: function () {

								//	Create multiple attributes form panel
								var multipleAttributesFP = Ext.create('OSF.form.MultipleAttributes', {
									componentId: attributePanel.componentId,
									componentType: attributePanel.component.componentType,
									viewHiddenAttributes: true
								});

								var multipleAttributesWin = Ext.create('Ext.window.Window', {
									title: 'Add Attributes',
									iconCls: 'fa fa-lg fa-plus icon-small-vertical-correction',
									modal: true,
									width: 700,
									closeAction: 'destroy',
									height: '50%',
									layout: 'fit',
									items: [
										multipleAttributesFP
									],
									dockedItems: [{
											xtype: 'toolbar',
											itemId: 'buttonToolBar',
											dock: 'bottom',
											items: [
												{
													xtype: 'tbfill'
												},
												{
													xtype: 'button',
													text: 'Save',
													formBind: true,
													margin: '0 20 0 0',
													iconCls: 'fa fa-lg fa-save icon-button-color-save',
													handler: function () {
														multipleAttributesFP.submit(function (attrForm) {
															attrForm.up('window').close();
															attributePanel.loadComponentAttributes();
														});
													}
												},
												{
													xtype: 'button',
													text: 'Cancel',
													iconCls: 'fa fa-lg fa-close icon-button-color-warning',
													handler: function () {
														multipleAttributesFP.cancel(function (attrForm) {
															attrForm.up('window').close();
														});
													}
												},
												{
													xtype: 'tbfill'
												}
											]
										}
									]
								});
								multipleAttributesWin.show();
							}
						},
						{
							xtype: 'button',
							text: 'Cancel',
							iconCls: 'fa fa-lg fa-close',
							handler: function () {
								this.up('form').reset();
							}
						}
					],
					items: [
						{
							xtype: 'panel',
							layout: 'hbox',
							margin: '0 0 10 0',
							width: '100%',
							itemId: 'attributeTypePanel',
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
									labelWidth: 150,
									labelAlign: 'right',
									flex: 1,
									listConfig: {
										getInnerTpl: function () {
											return '{description} <tpl if="detailedDescription"><i class="fa fa-question-circle" data-qtip=\'{detailedDescription}\'></i></tpl>';
										}
									},
									store: {
										autoLoad: false,
										proxy: {
											type: 'ajax',
											url: 'api/v1/resource/attributes'
										}
									},
									listeners: {
										change: function (field, newValue, oldValue, opts) {
											var cbox = field.up('form').getComponent('attributeCodeCB');
											cbox.clearValue();

											var record = field.getSelection();
											if (record) {
												cbox.getStore().loadData(record.data.codes);
												cbox.vtype = (record.data.attributeValueType === 'NUMBER') ? 'AttributeNumber' : undefined;
												cbox.setEditable(record.get("allowUserGeneratedCodes"));
											} else {
												cbox.getStore().removeAll();
												cbox.vtype = undefined;
											}
										}
									}
								},
								{
									xtype: 'button',
									itemId: 'addAttributeType',
									text: 'Add',
									iconCls: 'fa fa-lg fa-plus icon-button-color-save',
									minWidth: 100,
									hidden: true,
									handler: function () {
										var attributeTypeCb = attributePanel.queryById('attributeTypeCB');


										var addTypeWin = Ext.create('Ext.window.Window', {
											title: 'Add Type',
											iconCls: 'fa fa-plus',
											closeAction: 'destroy',
											alwaysOnTop: true,
											modal: true,
											width: 400,
											height: 380,
											layout: 'fit',
											items: [
												{
													xtype: 'form',
													scrollable: true,
													layout: 'anchor',
													bodyStyle: 'padding: 10px',
													defaults: {
														labelAlign: 'top',
														labelSeparator: '',
														width: '100%'
													},
													items: [
														{
															xtype: 'textfield',
															name: 'label',
															fieldLabel: 'Label <span class="field-required" />',
															allowBlank: false,
															maxLength: 255
														},
														{
															xtype: 'textarea',
															name: 'detailedDescription',
															fieldLabel: 'Description',
															maxLength: 255
														},
														{
															xtype: 'combobox',
															fieldLabel: 'Code Label Value Type <span class="field-required" />',
															displayField: 'description',
															valueField: 'code',
															typeAhead: false,
															editable: false,
															allowBlank: false,
															name: 'attributeValueType',
															store: {
																autoLoad: true,
																proxy: {
																	type: 'ajax',
																	url: 'api/v1/resource/lookuptypes/AttributeValueType'
																}
															}
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
																	iconCls: 'fa fa-lg fa-save icon-button-color-save',
																	handler: function () {
																		var form = this.up('form');
																		var data = form.getValues();
																		var addTypeWin = this.up('window');

																		CoreUtil.submitForm({
																			url: 'api/v1/resource/attributes/attributetypes/metadata?componentType=' + encodeURIComponent(attributePanel.component.componentType),
																			method: 'POST',
																			data: data,
																			form: form,
																			success: function (response, opts) {
																				attributeTypeCb.getStore().load({
																					url: 'api/v1/resource/attributes/optional?componentType=' + encodeURIComponent(attributePanel.component.componentType)
																				});
																				addTypeWin.close();
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
																	handler: function () {
																		this.up('window').close();
																	}
																}
															]
														}
													]
												}
											]
										});
										addTypeWin.show();

									}
								}
							]
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
							labelWidth: 150,
							listConfig: {
								getInnerTpl: function () {
									return '{label} <tpl if="description"><i class="fa fa-question-circle" data-qtip=\'{description}\'></i></tpl>';
								}
							},
							store: Ext.create('Ext.data.Store', {
								fields: [
									"code",
									"label"
								]
							})
						},						
						{
							xtype: 'textarea',
							name: 'comment',
							fieldLabel: 'Comment',
							labelWidth: 150,
							maxLength: 4096
						},
						{
							xtype: 'checkbox',
							name: 'privateFlag',
							margin: '0 0 0 155',							
							boxLabel: '<b>Private Flag</b>'
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
									{code: 'A', description: 'Active'},
									{code: 'I', description: 'Inactive'}
								]
							},
							forceSelection: true,
							queryMode: 'local',
							displayField: 'description',
							valueField: 'code',
							value: 'A',
							listeners: {
								change: function (combo, newValue, oldValue, opts) {
									attributePanel.loadComponentAttributes(newValue);
								}
							}
						},
						{
							text: 'Refresh',
							iconCls: 'fa fa-lg fa-refresh icon-button-color-refresh',
							handler: function () {
								attributePanel.loadComponentAttributes();
							}
						},
						{
							xtype: 'tbseparator'
						},
						{
							text: 'Edit',
							itemId: 'edit',
							iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
							disabled: true,
							handler: function () {
								var record = attributePanel.attributeGrid.getSelection()[0];
								actionEdit(record);
							}
						},						
						{
							xtype: 'tbseparator',
							hidden: attributePanel.hideToggleStatus || false
						},
						{
							text: 'Toggle Status',
							itemId: 'toggleStatusBtn',
							iconCls: 'fa fa-lg fa-power-off icon-button-color-default',
							disabled: true,
							hidden: attributePanel.hideToggleStatus || false,
							handler: function () {
								CoreUtil.actionSubComponentToggleStatus(attributePanel.attributeGrid, 'type', 'attributes', 'code', null, null, function () {
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
							handler: function () {
								CoreUtil.actionSubComponentToggleStatus(attributePanel.attributeGrid, 'type', 'attributes', 'code', null, true, function () {
									attributePanel.loadComponentAttributes();
								});
							}
						}
					]
				}
			]
		});
		
		var actionEdit = function(record) {
			record.set({
				attributeType: record.get('type'),
				attributeCode: record.get('code')				
			});			
			attributePanel.attributeGrid.queryById('form').loadRecord(record);			
		};

		attributePanel.add(attributePanel.attributeGrid);
	},
	loadData: function (evaluationId, componentId, data, opts, callback) {
		//just load option (filter out required)
		var attributePanel = this;

		attributePanel.componentId = componentId;
		attributePanel.attributeGrid.componentId = componentId;

		var form = attributePanel.attributeGrid.down('form');
		form.setLoading(true);
		Ext.Ajax.request({
			url: 'api/v1/resource/components/' + attributePanel.componentId,
			callback: function () {
				form.setLoading(false);
			},
			success: function (response, opts) {
				var component = Ext.decode(response.responseText);
				attributePanel.component = component;
				attributePanel.loadComponentAttributes();
				attributePanel.attributeGrid.queryById('attributeTypeCB').getStore().load({
					url: 'api/v1/resource/attributes/optional?componentType=' + component.componentType 
				});
			}
		});


		if (opts && opts.commentPanel) {
			opts.commentPanel.loadComments(evaluationId, "Attribute", componentId);
		}
		CoreService.userservice.getCurrentUser().then(function (user) {

			if (CoreService.userservice.userHasPermission(user, 'ALLOW-USER-ATTRIBUTE-TYPE-CREATION')) {
				attributePanel.queryById('addAttributeType').setHidden(false);
			}

		});

		if(callback){
			callback();
		}
	}

});

