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
/* global Ext, CoreUtil, CoreService */

Ext.define('OSF.component.SubmissionPanel', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.SubmissionPanel',
	layout: 'border',
	formWarningMessage: '',
	
	submitForReviewUrl: function (componentId){
		return 'api/v1/resource/componentsubmissions/' + componentId+ '/submit';
	},
	
	initComponent: function () {
		this.callParent();
	
		var submissionPanel = this;
		submissionPanel.hideSecurityMarkings = true;		
				
		submissionPanel.cancelSubmissionHandler = function(promptForSave) {
			
			var normalCancelHandling = function() {
				submissionPanel.resetSubmission();				
				if (submissionPanel.cancelSubmissionHandlerYes) {
					submissionPanel.cancelSubmissionHandlerYes();
				}
			};
			
			if (submissionPanel.currentStep === 2) {
				if (promptForSave) {
					Ext.Msg.show({
						title:'Save Changes?',
						message: 'Would you like to save your changes?',
						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						fn: function(btn) {
							if (btn === 'yes') {
								submissionPanel.handleRequiredFormSave(function(response, opts){
									normalCancelHandling();
								});
							} else if (btn === 'no') {
								normalCancelHandling();
							} 
						}
					});
				} else {				
					submissionPanel.handleRequiredFormSave(function(response, opts){
						normalCancelHandling();
					});
				}
			} else {
				if (submissionPanel.currentStep !== 1) {
					Ext.toast('Saved Submission');
				}
				normalCancelHandling();
			}
			
		};		
		
		submissionPanel.navigation = Ext.create('Ext.panel.Panel', {
			region: 'west',
			minWidth: 225,
			maxWidth: 225,			
			bodyStyle: 'padding: 10px;',
			frame: true,			
			defaults: {
				margin: '0 0 10 0',
				textAlign: 'left'
			},
			dockedItems: [				
			],
			items: [
				{
					xtype: 'button',
					itemId: 'step1Btn',
					text: '1. Getting Started',
					width: '100%',
					handler: function(){
						submissionPanel.currentStep=1;
						submissionPanel.changeSteps();
					}
				},
				{
					xtype: 'button',
					itemId: 'step2Btn',
					text: '2. Required Information',
					disabled: true,
					width: '100%',
					handler: function(){
						submissionPanel.currentStep=2;
						submissionPanel.changeSteps();						
					}
				},
				{
					xtype: 'button',
					text: '3. Details',
					itemId: 'step3Btn',
					disabled: true,
					width: '100%',
					handler: function(){
						submissionPanel.currentStep=3;
						submissionPanel.changeSteps();												
					}
				},
				{
					xtype: 'button',
					itemId: 'step4Btn',
					text: '4. Review',
					disabled: true,
					width: '100%',
					handler: function(){
						submissionPanel.currentStep=4;
						submissionPanel.changeSteps();																		
					}
				}
			]
		});
		
		submissionPanel.submitterForm = Ext.create('Ext.form.Panel', {
			autoScroll: true,
			defaults: {
				labelAlign: 'right',
				padding: '0 0 10 0'
			},
			items: [
				{
					xtype: 'panel',					
					html: '<h2>1. Verify/Update your information and hit Next</h2>'
				},
				{
					xtype: 'textfield',
					itemId: 'firstName',
					name: 'firstName',
					fieldLabel: 'First Name <span class="field-required" />',
					labelSeparator: '',
					width: '100%',
					maxLength: 80,
					allowBlank: false
				},
				{
					xtype: 'textfield',
					itemId: 'lastName',
					name: 'lastName',
					fieldLabel: 'Last Name <span class="field-required" />',
					labelSeparator: '',
					width: '100%',
					maxLength: 80,
					allowBlank: false
				},
				{
					xtype: 'textfield',
					itemId: 'email',
					name: 'email',
					inputType: 'email',						
					fieldLabel: 'Email <span class="field-required" />',
					labelSeparator: '',
					width: '100%',
					maxLength: 1000,
					allowBlank: false
				},				
				{
					xtype: 'textfield',
					itemId: 'phone',
					name: 'phone',
					fieldLabel: 'Phone <span class="field-required" />',
					labelSeparator: '',
					width: '100%',
					allowBlank: false,
					maxLength: 80
				},
				Ext.create('OSF.component.StandardComboBox', {
					itemId: 'organization',
					name: 'organization',
					allowBlank: false,
					margin: '0 0 5 0',
					width: '100%',
					maxLength: 120,
					fieldLabel: 'Organization <span class="field-required" />',
					forceSelection: false,					
					valueField: 'description',
					storeConfig: {
						url: 'api/v1/resource/organizations/lookup'
					}
				}),	
				{
					xtype: 'panel',
					itemId: 'externalManagementMessage',
					html: ''					
				},
				{
					xtype: 'panel',					
					frame: true,
					border: true,
					hidden: submissionPanel.formWarningMessage ? false : true,
					width: '100%',
					margin: '40 0 0 0',
					padding: '0 0 0 0',
					layout: {
						type: 'hbox',
						align: 'stretch'
					},
					items: [
						{
							xtype: 'panel',	
							bodyCls: 'app-info-box',						
							html: '<table style="height: 100%"><tr><td valign="center"><i class="fa fa-2x fa-info-circle"></i></td></tr></table>'
						},								
						{
							xtype: 'panel',
							flex: 1,
							html: submissionPanel.formWarningMessage ? '<div style="padding: 10px 0px 10px 10px;">' + submissionPanel.formWarningMessage  + '</div>' : ''
						}
					]					
				}
			]
		});
		
		var allAttributes = [];
		var loadAllAttributes = function(callback){
			Ext.Ajax.request({
				url: 'api/v1/resource/attributes',
				success: function(response, opts){
					allAttributes = Ext.decode(response.responseText);
					if (callback) {
						callback();
					}
				}
			});
		};
		loadAllAttributes();		
		
		submissionPanel.loadComponentAttributes = function() {
			if (submissionPanel.componentId) {
				var componentId = submissionPanel.componentId;
				Ext.Ajax.request({
					url: 'api/v1/resource/components/' + componentId + '/attributes/view',
					method: 'GET',					
					success: function(response, opts) {
						var data = Ext.decode(response.responseText);

						var requiredStore = submissionPanel.requiredAttributeStore;

						var optionalAttributes = [];
						Ext.Array.each(data, function(attribute) {
							if (!attribute.hideOnSubmission) {
								if (attribute.requiredFlg) {
									var found = false;
									requiredStore.each(function(record){
										if (record.get('attributeType') === attribute.type) {
											record.set('attributeCode', attribute.code, { dirty: false });
											found = true;
										}
									});
									if (!found) {
										//the component type  may not require this
										optionalAttributes.push(attribute);
									}
								} else {
									optionalAttributes.push(attribute);
								}
							}
						});
						optionalAttributes.reverse();
						var detailSections = submissionPanel.detailsPanel.getComponent('detailSections');
						detailSections.getComponent('optionalAttributes').getStore().loadData(optionalAttributes);
					}
				});
			} else {
				var requiredStore = submissionPanel.requiredAttributeStore;
				requiredStore.each(function(record) {
					record.set('attributeCode', null, { dirty: false} );
				});
			}
		};

		var handleAttributes = function(componentType) {
			if (!componentType) {
				componentType = submissionPanel.componentTypeSelected;
			}

			var requiredAttributes = [];
			var optionalAttributes = [];
			Ext.Array.each(allAttributes, function(attribute){
				if (!attribute.hideOnSubmission) {
					// This is slightly difficult to follow,
					// but the basic gist is that we must check two lists to decide which attributes to show -
					// requiredRestrictions is a list of types for which the attribute is required
					// associatedComponentTypes is a list of types for which the attribute is optional
					// but if associatedComponentTypes is empty, it is optional for all.
					if (attribute.requiredFlg) {
						if (attribute.requiredRestrictions) {
							var found = Ext.Array.findBy(attribute.requiredRestrictions, function(item){
								if (item.componentType === componentType) {
									return true;
								} else {
									return false;
								}
							});
							if (found) {
								requiredAttributes.push(attribute);
							}
							else {
								// --- Checking for Optional
								//
								// In this case, the 'Required' Flag is set but the entry we are dealing with is not an entry
								// type listed in the requiredRestrictions, i.e. not required for this entry type.
								// As a result, we need to check if it's allowed as an optional and then add it.
								// This is the same logic as seen below when the 'Required' flag is off.
								if (attribute.associatedComponentTypes) {
									var reqOptFound = Ext.Array.findBy(attribute.associatedComponentTypes, function(item) {
										if (item.componentType === componentType) {
											return true;
										} else {
											return false;
										}
									});
									if (reqOptFound) {
										optionalAttributes.push(attribute);
									}
								}
								else {
									// We have an empty list of associatedComponentTypes, therefore this attribute is
									// allowed for all entry types.
									optionalAttributes.push(attribute);
								}
								// 
								// --- End Checking for Optional
							}
						} else {
							// No list of types required for, so it's required for all. Add it.
							requiredAttributes.push(attribute);
						}
					} else {
						if (attribute.associatedComponentTypes) {
							var optFound = Ext.Array.findBy(attribute.associatedComponentTypes, function(item){
								if (item.componentType === componentType) {
									return true;
								} else {
									return false;
								}
							});
							if (optFound) {
								// This entry type allows this attribute.
								optionalAttributes.push(attribute);
							}
						}
						else {
							// We have an empty list of associatedComponentTypes, therefore this attribute is
							// allowed for all entry types.
							optionalAttributes.push(attribute);
						}
					}
				}
			});

			var requiredStore = submissionPanel.requiredAttributeStore;

			requiredAttributes.reverse();
			requiredStore.loadData(requiredAttributes);

			submissionPanel.optionalAttributes = optionalAttributes;						
		};		
		
		submissionPanel.requiredAttributeStore = Ext.create('Ext.data.Store', {
			fields: [
				"attributeType",
				"attributeCode",
				"description"
			],
			autoLoad: false,
			listeners: {
				datachanged: function(store) {
					if (submissionPanel.requiredAttributeStore.ignore) return;
					var panel = submissionPanel.requiredForm.getComponent('requiredAttributePanel');					
					panel.removeAll();
					
					store.each(function(record) {
						
						var field = Ext.create('Ext.form.field.ComboBox', {
							record: record,
							fieldLabel: record.get('description') + ' <span class="field-required" />',
							queryMode: 'local',
							editable: record.get('allowUserGeneratedCodes'),
							typeAhead: record.get('allowUserGeneratedCodes'),
							allowBlank: false,
							width: '100%',							
							labelWidth: 300,
							labelSepartor: '',							
							valueField: 'code',
							displayField: 'label',
							store: Ext.create('Ext.data.Store', {
								data: record.data.codes								
							}),
							listeners: {
								change: function(fieldLocal, newValue, oldValue, opts) {
									var recordLocal = fieldLocal.record;
									if (recordLocal) {
										recordLocal.set('attributeCode', newValue);																								
									}
								}
							}
						});						
						record.formField = field;
						panel.add(field);
					});
					panel.updateLayout(true, true);					
				},
				update: function(store, record, operation, modifiedFieldNames, details, opts) {
					if (record.formField) {
						record.formField.setValue(record.get('attributeCode'));
					}
				}
			}
		});
						
		submissionPanel.requiredForm = Ext.create('Ext.form.Panel', {
			autoScroll: true,
			defaults: {
				labelAlign: 'top',
				labelSeparator: ''
			},
			items: [
				{
					xtype: 'panel',
					html: '<h1>2. Enter Required Information:</h1><h2>Please fill in each field and hit Next</h2>'
				},
				Ext.create('OSF.component.StandardComboBox', {					
					fieldLabel: 'Entry Type <span class="field-required" />',
					name: 'componentType',
					allowBlank: false,								
					margin: '0 0 0 0',
					width: '100%',
					editable: false,
					typeAhead: false,
					displayField: 'label',
					valueField: 'componentType',
					storeConfig: {
						url: 'api/v1/resource/componenttypes/submission'																				
					},
					listeners: {
						change: function(field, newValue, oldValue, opts) {
							if (newValue) {								
								handleAttributes(newValue);
								submissionPanel.componentTypeSelected = newValue;

								var sectionPanel = submissionPanel.detailsPanel.getComponent('detailSections');

								sectionPanel.getComponent('optionalAttributes').setHidden(true);								
								sectionPanel.getComponent('contactGrid').setHidden(true);
								sectionPanel.getComponent('resourceGrid').setHidden(true);							
								sectionPanel.getComponent('mediaGrid').setHidden(true);							
								sectionPanel.getComponent('dependenciesGrid').setHidden(true);								
								sectionPanel.getComponent('relationshipsGrid').setHidden(true);
								sectionPanel.getComponent('tagGrid').setHidden(true);							
								
								sectionPanel.getComponent('optionalAttributes-help').setHidden(true);								
								sectionPanel.getComponent('contactGrid-help').setHidden(true);
								sectionPanel.getComponent('resourceGrid-help').setHidden(true);							
								sectionPanel.getComponent('mediaGrid-help').setHidden(true);							
								sectionPanel.getComponent('dependenciesGrid-help').setHidden(true);								
								sectionPanel.getComponent('relationshipsGrid-help').setHidden(true);
								sectionPanel.getComponent('tagGrid-help').setHidden(true);							
								

								var record = field.getSelection();
								if (record.get('dataEntryAttributes')){
									sectionPanel.getComponent('optionalAttributes').setHidden(false);
									sectionPanel.getComponent('optionalAttributes-help').setHidden(false);
								} 
								if (record.get('dataEntryContacts')){
									sectionPanel.getComponent('contactGrid').setHidden(false);
									sectionPanel.getComponent('contactGrid-help').setHidden(false);
								} 
								if (record.get('dataEntryResources')){
									sectionPanel.getComponent('resourceGrid').setHidden(false);
									sectionPanel.getComponent('resourceGrid-help').setHidden(false);
								} 
								if (record.get('dataEntryMedia')){
									sectionPanel.getComponent('mediaGrid').setHidden(false);
									sectionPanel.getComponent('mediaGrid-help').setHidden(false);
								} 
								if (record.get('dataEntryDependancies')){							
									sectionPanel.getComponent('dependenciesGrid').setHidden(false);
									sectionPanel.getComponent('dependenciesGrid-help').setHidden(false);
								} 
								if (record.get('dataEntryRelationships')){
									sectionPanel.getComponent('relationshipsGrid').setHidden(false);
									sectionPanel.getComponent('relationshipsGrid-help').setHidden(false);
								} 								
								sectionPanel.getComponent('tagGrid').setHidden(false);		
								sectionPanel.getComponent('tagGrid-help').setHidden(false);
							}
							
						}
					}
				}),
				{
					xtype: 'textfield',
					name: 'name',
					width: '100%',
					fieldLabel: 'Entry Name <span class="field-required" />',
					allowBlank: false,
					maxLength: 255														
				},
				Ext.create('OSF.component.StandardComboBox', {
					name: 'organization',									
					allowBlank: false,									
					margin: '0 0 10 0',
					width: '100%',
					fieldLabel: 'Organization/Company responsible for the Entry <span class="field-required" />',
					forceSelection: false,
					valueField: 'description',
					editable: true,
					storeConfig: {
						url: 'api/v1/resource/organizations/lookup',
						sorters: [{
							property: 'description',
							direction: 'ASC'
						}]

					}
				}),				
				{
					xtype: 'panel',
					html: '<b>Description</b> <span class="field-required" />'
				},
				{
					xtype: 'tinymce_textarea',
					itemId: 'descriptionField',
					fieldStyle: 'font-family: Courier New; font-size: 12px;',
					style: { border: '0' },
					name: 'description',
					width: '100%',
					height: 300,
					maxLength: 65536,
					emptyText: (submissionPanel.userInputWarning ? submissionPanel.userInputWarning : '' ) + '<br><br>Include an easy to read description of the product, focusing on what it is and what it does.',
					tinyMCEConfig: Ext.apply(CoreUtil.tinymceConfig("osfmediaretriever"), {
						mediaSelectionUrl: function(){
							if (submissionPanel.componentId) {					
								return 'api/v1/resource/components/' + submissionPanel.componentId + '/media/view';
							} else {
								return 'api/v1/resource/components/NEW/media/view';
							}
						}						
					})
				},
				Ext.create('OSF.component.SecurityComboBox', {	
					itemId: 'securityMarkings',
					hidden: true
				}),
				Ext.create('OSF.component.DataSensitivityComboBox', {			
					width: '100%'
				}),				
				Ext.create('OSF.component.DataSourceComboBox', {			
					name: 'dataSource',
					hideOnNoData: true,
					width: '100%'
				}),				
				{
					xtype: 'panel',
					itemId: 'requiredAttributePanel',
					title: 'Attributes',
					frame: true,
					bodyStyle: 'padding: 10px;',
					margin: '20 0 20 0'									
				}
			]			
		});
		
		var actionSubComponentRemove = function(opts) {
			var grid = opts.grid;
			var componentId = submissionPanel.componentId;
			var recordId = grid.getSelection()[0].get(opts.idField);
			var subEntityId = opts.subEntityId ? '/' + grid.getSelection()[0].get(opts.subEntityId) : '';
			var subEntity = opts.subEntity ? '/' + opts.subEntity : '';
			
			grid.setLoading('Removing...');
			Ext.Ajax.request({
				url: 'api/v1/resource/components/' + componentId + '/' + opts.entity + '/' + recordId + subEntity + subEntityId, 
				method: 'DELETE',
				callback: function(opt, success, response){
					grid.setLoading(false);
				},
				success: function(response, responseOpts){
					if (opts.successFunc) {
						opts.successFunc(response, opts);
					} else {
						grid.getStore().reload();
					}
				}
			});
		};	
		
		var addEditAttribute = function(record) {
			var addWindow = Ext.create('Ext.window.Window', {
				closeAction: 'destory',
				modal: true,
				alwaysOnTop: true,
				title: '<i class="fa fa-plus"></i>' + '<span class="shift-window-text-right">Add Attribute</span>',
				width: '50%',
//				height: '300px',
				minHeight: 200,
				maxHeight: 300,
				layout: 'fit',
				items: [
					{
						xtype: 'form',
						scrollable: true,
						itemId: 'attributeForm',
						bodyStyle: 'padding: 10px;',
						defaults: {
							labelAlign: 'right',
							labelSeparator: '',
							width: '100%'
						},
						items: [
							{
								xtype: 'panel',
								layout: 'hbox',
								margin: '0 0 10 0',
								items: [
									{
										xtype: 'combobox',
										itemId: 'attributeTypeCB',
										fieldLabel: 'Attribute Type <span class="field-required" />',
										name: 'type',
										flex: 1,
										labelWidth: 150,
										labelAlign: 'right',
										labelSeparator: '',
										forceSelection: true,
										queryMode: 'local',
										editable: true,
										typeAhead: true,
										allowBlank: false,
										valueField: 'attributeType',
										displayField: 'description',
										store: Ext.create('Ext.data.Store', {
											sorters: [
												{
													property: 'description',
													direction: 'ASC',
													transform: function(item) {
														if (item) {
															item = item.toLowerCase();
														}
														return item;
													}
												}
											],
											fields: [
												"attributeType",
												"description"
											],
											data: submissionPanel.optionalAttributes
										}),
										listeners: {

											change: function (field, newValue, oldValue, opts) {

												// Clear Previously Selected Code
												var codeField = field.up('form').getComponent('attributeCodeCB');
												codeField.clearValue();

												codeField.setLoading(true);
												Ext.Ajax.request({
													url: 'api/v1/resource/attributes/attributetypes/' + newValue + '/attributecodes',
													callback: function(){
														codeField.setLoading(false);
													},
													success: function(response, opts) {
														var attributeCodes = Ext.decode(response.responseText);

														var lookUpCodes = [];
														Ext.Array.each(attributeCodes, function(attributeCode) {
															lookUpCodes.push({
																code: attributeCode.attributeCodePk.attributeCode,
																label: attributeCode.label
															});
														});
														codeField.getStore().loadData(lookUpCodes);
													}
												});

												var record = field.getSelection();

												// Check For Selected Type
												if (record) {

													// Check For User-Generated Codes Being Enabled
													if (record.get("allowUserGeneratedCodes")) {												
														// Allow Editing Of ComboBox
														codeField.setEditable(true);
													}
													else {
														// Disallow Editing Of ComboBox
														codeField.setEditable(false);
													}
												}
												else {

													// Nothing Selcted, Remove All Codes
													codeField.getStore().removeAll();
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
										handler: function() {
											var attributeTypeCb = addWindow.queryById('attributeTypeCB');
											
											
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
																				url: 'api/v1/resource/attributes/attributetypes/metadata',
																				method: 'POST',
																				data: data,
																				form: form,
																				success: function (response, opts) {
																					loadAllAttributes(function(){
																						handleAttributes();
																					});						
																					
																					var newAttribute = Ext.decode(response.responseText);
																					attributeTypeCb.getStore().add(newAttribute);	
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
								name: 'code',
								queryMode: 'local',
								labelWidth: 150,								
								editable: false,
								typeAhead: false,
								allowBlank: false,
								valueField: 'code',
								displayField: 'label',
								store: Ext.create('Ext.data.Store', {
									sorters: [
										{
											property: 'label',
											direction: 'ASC',
											transform: function(item) {
												if (item) {
													item = item.toLowerCase();
												}
												return item;
											}
										}
									],
									fields: [
										"code",
										"label"
									]
								})
							}
						],
						dockedItems: [
							{
								xtype: 'toolbar',
								dock: 'bottom',
								items: [
									{
										text: 'Save',
										itemId: 'saveButton',
										formBind: true,
										iconCls: 'fa fa-lg fa-save icon-button-color-save',
										handler: function () {
											var attributeWindow = this.up('window');
											var form = this.up('form');
											var data = form.getValues();
											var componentId = submissionPanel.componentId;
																					
											var handleSaveAttribute = function(newCode) {
												
												var newAttribute = {
													componentAttributePk: {
														attributeType: data.type,
														attributeCode: newCode ? newCode : data.code
													}
												};
												
												var method = 'POST';
												var update = '';

												CoreUtil.submitForm({
													url: 'api/v1/resource/components/' + componentId + '/attributes' + update,
													method: method,
													data: newAttribute,
													form: form,
													success: function () {
														if (record) {
															if (newAttribute.componentAttributePk.attributeType !== record.get('type')
																|| newAttribute.componentAttributePk.attributeCode !== record.get('code'))
															{
																attributeWindow.setLoading(true);
																Ext.Ajax.request({
																	url: 'api/v1/resource/components/' + componentId + '/attributes/' + record.get('type') + '/' + record.get('code') + '/force',
																	method: 'DELETE',
																	callback: function() {
																		attributeWindow.setLoading(false);
																	},
																	success: function() {
																		submissionPanel.loadComponentAttributes();
																		attributeWindow.close();
																	}
																});
															} else {
																attributeWindow.close();
															}
														} else {													
															submissionPanel.loadComponentAttributes();
															attributeWindow.close();
														}
													}
												});
											};

											var type = form.queryById('attributeTypeCB').getSelection();											
											
											// The attribute may allow user generated codes. If so, the code
											// may not already exist.
											if (type.get('allowUserGeneratedCodes')) {
												var label = form.queryById('attributeCodeCB').getValue();
												var store = form.queryById('attributeCodeCB').getStore();
												var found = false;
												store.each(function(item) {
													if (item.get('code') === label) {
														found = true;
													}
												});

												if (!found) {
													
													//check
													var valid = true;
													if (type.get('attributeValueType') === 'NUMBER') {
														if (!Ext.isNumeric(label)) {
															valid = false;
														}
														if (valid) {														
															try {
																var valueNumber = new Number(label);
																label = valueNumber.toString();																
															} catch(e) {
																valid = false;
															}
														}														
													}
													
													if (!valid) {
														Ext.Msg.show({
															title:'Validation Error',
															message: 'Attribute Code must be numberic for this attribute type',
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
														// Build Request Data
														var newAttributeData = {														
															userAttributes: [
																{
																	attributeCodeLabel: label,
																	attributeType: data.type
																}
															]
														};

														form.setLoading("Add new attribute...");
														Ext.Ajax.request({
															url: 'api/v1/resource/attributes/attributetypes/usercodes',
															method: 'POST',
															jsonData: newAttributeData,
															callback: function() {
																form.setLoading(false);
															},
															success: function(response, opts) {
																Ext.toast('Successfully added user attribute code.', '', 'tr');																														
																CoreService.attributeservice.labelToCode(label).then(function(response, opts) {
																	handleSaveAttribute(response.responseText); 
																	loadAllAttributes();
																});						
															},
															failure: function(response, opts) {
																Ext.MessageBox.show({
																	title:'Failed to Save',
																	message: 'Failed to generate the new attribute code. Try again or use an existing attribute code.',
																	buttons: Ext.Msg.OK,
																	icon: Ext.Msg.ERROR										
																});
															}
														});	
													}	
												} else {
													handleSaveAttribute();
												}
												
											} else {
												handleSaveAttribute();
											}
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
			}).show();
			
			if (record) {
				addWindow.getComponent('attributeForm').loadRecord(record);
			}	
						
			CoreService.userservice.getCurrentUser().then(function(user){

				if (CoreService.userservice.userHasPermisson(user, 'ALLOW-USER-ATTRIBUTE-TYPE-CREATION')) {
					addWindow.queryById('addAttributeType').setHidden(false);
				}

			});			
			
		};		
		
		var addEditContact = function(record, grid) {
			
			var addWindow = Ext.create('Ext.window.Window', {
				closeAction: 'destory',
				modal: true,
				title: '<i class="fa fa-plus"></i>' + '<span class="shift-window-text-right">Add Contact</span>',
				alwaysOnTop: true,
				width: '50%',
				maxHeight: 600,
				layout: 'fit',
				items: [
					{
						xtype: 'form',
						scrollable: true,
						itemId: 'contactForm',
						bodyStyle: 'padding: 10px;',
						defaults: {
							labelAlign: 'right',
							labelSeparator: '',
							width: '100%'
						},
						items: [
							{
								xtype: 'hidden',
								name: 'componentContactId'
							},							
							{
								xtype: 'hidden',
								name: 'contactId'
							},
							Ext.create('OSF.component.StandardComboBox', {
								name: 'contactType',	
								itemId: 'contactType',
								allowBlank: false,								
								margin: '0 0 5 0',
								editable: false,
								typeAhead: false,
								width: '100%',
								fieldLabel: 'Contact Type <span class="field-required" />',
								storeConfig: {
									url: 'api/v1/resource/lookuptypes/ContactType',
									filters: [{
										property: 'code',
										operator: '!=',
										value: /SUB/
									}]
								}
							}),
							Ext.create('OSF.component.StandardComboBox', {
								name: 'organization',									
								allowBlank: false,									
								margin: '0 0 5 0',
								width: '100%',
								fieldLabel: 'Organization <span class="field-required" />',
								forceSelection: false,
								valueField: 'description',
								storeConfig: {
									url: 'api/v1/resource/organizations/lookup'
								}
							}),							
							Ext.create('OSF.component.StandardComboBox', {
								name: 'firstName',									
								allowBlank: false,									
								margin: '0 0 5 0',
								width: '100%',
								fieldLabel: 'First Name  <span class="field-required" />',
								forceSelection: false,
								valueField: 'firstName',
								displayField: 'firstName',
								maxLength: '80',
								typeAhead: false,
								autoSelect: false,
								selectOnTab: false,
								assertValue: function(){
								},								
								listConfig: {
									itemTpl: [
										 '{firstName} <span style="color: grey">({email})</span>'
									]
								},								
								storeConfig: {
									url: 'api/v1/resource/contacts/filtered'
								},
								listeners: {
									select: function(combo, record, opts) {
										record.set('componentContactId', null);
										record.set('contactId', null);
										var contactType =  combo.up('form').getComponent('contactType').getValue();
										combo.up('form').reset();
										combo.up('form').loadRecord(record);
										combo.up('form').getComponent('contactType').setValue(contactType);
									}
								}
							}),
							Ext.create('OSF.component.StandardComboBox', {
								name: 'lastName',									
								allowBlank: false,									
								margin: '0 0 5 0',
								width: '100%',
								fieldLabel: 'Last Name <span class="field-required" />',
								forceSelection: false,
								valueField: 'lastName',
								displayField: 'lastName',
								maxLength: '80',
								typeAhead: false,
								autoSelect: false,
								selectOnTab: false,
								assertValue: function(){
								},								
								listConfig: {
									itemTpl: [
										 '{lastName} <span style="color: grey">({email})</span>'
									]
								},								
								storeConfig: {
									url: 'api/v1/resource/contacts/filtered'
								},
								listeners: {
									select: function(combo, record, opts) {
										record.set('componentContactId', null);
										record.set('contactId', null);
										var contactType =  combo.up('form').getComponent('contactType').getValue();
										combo.up('form').reset();
										combo.up('form').loadRecord(record);
										combo.up('form').getComponent('contactType').setValue(contactType);
									}
								}
							}),									
							{
								xtype: 'textfield',
								fieldLabel: 'Email <span class="field-required" />',																																	
								maxLength: '255',
								allowBlank: false,	
								regex: new RegExp("[a-z0-9!#$%&'*+/=?^_`{|}~.-]+@[a-z0-9-]+(\.[a-z0-9-]+)*", "i"),
								regexText: 'Must be a valid email address. Eg. xxx@xxx.xxx',
								name: 'email'
							},
							{
								xtype: 'textfield',
								fieldLabel: 'Phone <span class="field-required" />',
								allowBlank: false,	
								maxLength: '120',
								name: 'phone'
							},
							Ext.create('OSF.component.SecurityComboBox', {	
								itemId: 'securityMarkings',
								hidden: submissionPanel.hideSecurityMarkings
							}),
							Ext.create('OSF.component.DataSensitivityComboBox', {			
								width: '100%'
							})							
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
										handler: function(){
											var form = this.up('form');
											var formWindow = this.up('window');
											var data = form.getValues();
											var componentId = submissionPanel.componentId;

											var method = 'POST';
											var update = '';
											if (data.componentContactId) {
												update = '/' + data.componentContactId;
												method = 'PUT';
											}

											CoreUtil.submitForm({
												url: 'api/v1/resource/components/' + componentId + '/contacts' + update,
												method: method,
												data: data,
												form: form,
												success: function(){
													grid.getStore().load({
														url: 'api/v1/resource/components/' + submissionPanel.componentId + '/contacts/view'
													});
													formWindow.close();
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
											this.up('window').close();
										}
									}
								]
							}																
						]
					}
				]
			}).show();
			
			if (record) {
				addWindow.getComponent('contactForm').loadRecord(record);
			}
		};
		
		var addEditResource = function(record, grid){
			var addWindow = Ext.create('Ext.window.Window', {
				closeAction: 'destory',
				modal: true,
				alwaysOnTop: true,
				title: '<i class="fa fa-plus"></i>' + '<span class="shift-window-text-right">Add External Link</span>',
				width: '50%',
				maxHeight: 600,
				layout: 'fit',
				items: [
					{
						xtype: 'form',
						scrollable: true,
						itemId: 'resourceForm',
						bodyStyle: 'padding: 10px;',
						defaults: {
							labelAlign: 'top',
							labelSeparator: '',
							width: '100%'
						},
						items: [
							{
								xtype: 'hidden',
								name: 'resourceId'
							},
							{
								xtype: 'hidden',
								name: 'originalFileName'
							},
							{
								xtype: 'hidden',
								name: 'fileName'
							},
							{
								xtype: 'hidden',
								name: 'mimeType'
							},							
							Ext.create('OSF.component.StandardComboBox', {
								name: 'resourceType',									
								allowBlank: false,								
								margin: '0 0 0 0',
								editable: false,
								typeAhead: false,
								width: '100%',
								fieldLabel: 'Resource Type <span class="field-required" />',
								storeConfig: {
									url: 'api/v1/resource/lookuptypes/ResourceType'
								}
							}),
							{
								xtype: 'textfield',
								fieldLabel: 'Description',																									
								maxLength: '255',
								name: 'description'
							},
							{
								xtype: 'checkbox',
								name: 'restricted',
								boxLabel: 'Restricted'
							},
							{
								xtype: 'button',
								text: 'External Link',
								menu: [
									{
										text: 'External Link',
										handler: function(){
											var form = this.up('form');
											var button = this.up('button');											
											button.setText('External Link');											
											form.getForm().findField('file').setHidden(true);
											form.getForm().findField('originalLink').setHidden(false);											
										}
									},
									{
										text: 'Local Resource',
										handler: function(btn){
											var form = this.up('form');
											var button = this.up('button');
											button.setText('Local Resource');
											form.getForm().findField('file').setHidden(false);
											form.getForm().findField('originalLink').setHidden(true);
										}
									}																		
								]
							},							
							{
								xtype: 'textfield',
								fieldLabel: 'Link',																																	
								maxLength: '255',									
								emptyText: 'http://www.example.com/resource',
								name: 'originalLink'
							},
							{
								xtype: 'filefield',
								itemId: 'upload',
								hidden: true,
								fieldLabel: 'Upload Resource (Limit of 1GB)',																											
								name: 'file',
								listeners: {
									change: CoreUtil.handleMaxFileLimit
								}	
							},
							Ext.create('OSF.component.SecurityComboBox', {	
								itemId: 'securityMarkings',
								hidden: submissionPanel.hideSecurityMarkings
							}),
							Ext.create('OSF.component.DataSensitivityComboBox', {			
								width: '100%'
							})
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
										handler: function(){
											var resourceWindow = this.up('window');
											var form = this.up('form');
											var data = form.getValues();
											var componentId = submissionPanel.componentId;

											data.fileSelected = form.getComponent('upload').getValue();
											data.link = data.originalLink;
											data.originalName = data.originalFileName;

											if (!data.originalFileName && ((!data.link && !data.fileSelected) || (data.link && data.fileSelected))) {

												form.getForm().markInvalid({
													file: 'Either a link or a file must be entered',
													originalLink: 'Either a link or a file must be entered'
												});

											} else {
												if (!data.fileSelected) {
													var method = 'POST';
													var update = '';
													if (data.resourceId) {
														update = '/' + data.resourceId;
														method = 'PUT';
													}

													CoreUtil.submitForm({
														url: 'api/v1/resource/components/' + componentId + '/resources' + update,
														method: method,
														removeBlankDataItems: true,
														data: data,
														form: form,
														success: function(){
															grid.getStore().load({
																url: 'api/v1/resource/components/' + submissionPanel.componentId + '/resources/view'
															});
															resourceWindow.close();
														}
													});
												} else {
													//upload
													form.submit({
														url: 'Resource.action?UploadResource',
														params: {
															'componentResource.resourceType' : data.resourceType,
															'componentResource.description': data.description,
															'componentResource.restricted': data.restricted,
															'componentResource.link': data.link,
															'componentResource.resourceId': data.resourceId,
															'componentResource.componentId': componentId
														},
														method: 'POST',
														submitEmptyText: false,
														waitMsg: 'Uploading please wait...',
														waitTitle: 'Uploading',															
														success: function(form, action, opt){
															grid.getStore().load({
																url: 'api/v1/resource/components/' + submissionPanel.componentId + '/resources/view'
															});
															resourceWindow.close();
														}, 
														failure: function(form, action, opt) {
															var errorResponse = Ext.decode(action.response.responseText);
															var errorObj = {};        
															Ext.Array.each(errorResponse.errors.entry, function(item, index, entry) {
																errorObj[item.key.replace('componentResource', '')] = item.value;
															});
															form.markInvalid(errorObj);
														}
													});

												}
											}
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Cancel',										
										iconCls: 'fa fa-lg fa-close icon-button-color-warning',
										handler: function(){
											this.up('window').close();
										}
									}
								]
							}																
						]															
					}
				]													
			}).show();	
			
			if (record) {
				addWindow.getComponent('resourceForm').loadRecord(record);
			}
		};
		
		var addEditMedia = function(record, grid){
			var addWindow = Ext.create('Ext.window.Window', {
				closeAction: 'destroy',
				modal: true,
				alwaysOnTop: true,
				title: '<i class="fa fa-plus"></i>' + '<span class="shift-window-text-right">Add Media</span>',
				width: '50%',
				maxHeight: 700,
				layout: 'fit',
				items: [
					{
						xtype: 'form',
						itemId: 'mediaForm',
						bodyStyle: 'padding: 10px;',
						autoScroll: true,
						defaults: {
							labelAlign: 'top',
							labelSeparator: '',
							width: '100%'
						},
						items: [
							{
								xtype: 'hidden',
								name: 'componentMediaId'
							},
							{
								xtype: 'hidden',
								name: 'originalFileName'
							},
							{
								xtype: 'hidden',
								name: 'fileName'
							},
							{
								xtype: 'hidden',
								name: 'mimeType'
							},							
							Ext.create('OSF.component.StandardComboBox', {
								name: 'mediaTypeCode',									
								allowBlank: false,								
								margin: '0 0 0 0',
								editable: false,
								typeAhead: false,
								width: '100%',
								fieldLabel: 'Media Type <span class="field-required" />',
								storeConfig: {
									url: 'api/v1/resource/lookuptypes/MediaType'
								}
							}),
							{
								xtype: 'textfield',
								fieldLabel: 'Caption <span class="field-required" />',									
								allowBlank: false,									
								maxLength: '255',
								name: 'caption'
							},
							{
								xtype: 'button',
								text: 'Local Resource',
								menu: [
									{
										text: 'Local Resource',
										handler: function(){
											var form = this.up('form');
											var button = this.up('button');
											button.setText('Local Resource');
											form.getForm().findField('file').setHidden(false);
											form.getForm().findField('originalLink').setHidden(true);
										}
									},
									{
										text: 'External Link',
										handler: function(){
											var form = this.up('form');
											var button = this.up('button');
											button.setText('External Link');
											form.getForm().findField('file').setHidden(true);
											form.getForm().findField('originalLink').setHidden(false);											
										}
									}									
								]
							},
							{
								xtype: 'checkbox',
								fieldLabel: 'Hide In Carousel',
								name: 'hideInDisplay'
							},
							{
								xtype: 'checkbox',
								fieldLabel: 'Used Inline',
								name: 'usedInline'
							},
							{
								xtype: 'checkbox',
								fieldLabel: 'Icon <i class="fa fa-question-circle"  data-qtip="Designates a media item to be used as an icon. There should only be one active on a entry at a time."></i>',
								name: 'iconFlag'
							},								
							{
								xtype: 'filefield',
								itemId: 'upload',
								fieldLabel: 'Upload Media (Limit of 1GB)',
								name: 'file',
								listeners: {
									change: CoreUtil.handleMaxFileLimit
								}
							},
							{
								xtype: 'textfield',
								fieldLabel: 'Link',	
								hidden: true,
								maxLength: '255',									
								emptyText: 'http://www.example.com/image.png',
								name: 'originalLink'
							},
							Ext.create('OSF.component.SecurityComboBox', {	
								itemId: 'securityMarkings',
								hidden: submissionPanel.hideSecurityMarkings
							}),
							Ext.create('OSF.component.DataSensitivityComboBox', {			
								width: '100%'
							})							
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
										handler: function(){
											var mediaWindow = this.up('window');
											var form = this.up('form');
											var data = form.getValues();
											var componentId = submissionPanel.componentId;

											data.fileSelected = form.getComponent('upload').getValue();
											data.link = data.originalLink;
											data.originalName = data.originalFileName;

											if (!data.originalFileName && ((!data.link && !data.fileSelected) || (data.link && data.fileSelected))) {

												form.getForm().markInvalid({
													file: 'Either a link or a file must be entered',
													originalLink: 'Either a link or a file must be entered'
												});

											} else {
												if (!data.fileSelected) {
													var method = 'POST';
													var update = '';
													if (data.componentMediaId) {
														update = '/' + data.componentMediaId;
														method = 'PUT';
													}

													CoreUtil.submitForm({
														url: 'api/v1/resource/components/' + componentId + '/media' + update,
														method: method,
														removeBlankDataItems: true,
														data: data,
														form: form,
														success: function(){
															grid.getStore().load({
																url: 'api/v1/resource/components/' + submissionPanel.componentId + '/media/view'
															});
															mediaWindow.close();
														}
													});
												} else {
													//upload
													form.submit({
														url: 'Media.action?UploadMedia',
														params: {
															'componentMedia.mediaTypeCode' : data.mediaTypeCode,
															'componentMedia.caption': data.caption,
															'componentMedia.link': data.link,
															'componentMedia.hideInDisplay': data.hideInDisplay,
															'componentMedia.usedInline': data.usedInline,
															'componentMedia.componentMediaId': data.componentMediaId,
															'componentMedia.componentId': componentId
														},
														method: 'POST',
														submitEmptyText: false,
														waitMsg: 'Uploading please wait...',
														waitTitle: 'Uploading',														
														success: function(form, action, opt){
															grid.getStore().load({
																url: 'api/v1/resource/components/' + submissionPanel.componentId + '/media/view'
															});
															mediaWindow.close();
														}, 
														failure: function(form, action, opt) {
															var errorResponse = Ext.decode(action.response.responseText);
															var errorObj = {};        
															Ext.Array.each(errorResponse.errors.entry, function(item, index, entry) {
																errorObj[item.key.replace('componentMedia', '')] = item.value;
															});
															form.markInvalid(errorObj);
														}
													});

												}
											}
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Cancel',										
										iconCls: 'fa fa-lg fa-close icon-button-color-warning',
										handler: function(){
											this.up('window').close();
										}
									}
								]
							}																
						]															
					}
				]
			}).show();

			if (record) {
				addWindow.getComponent('mediaForm').loadRecord(record);
			}			
		};
		
		var addEditDependency = function(record, grid){
			var addWindow = Ext.create('Ext.window.Window', {
				closeAction: 'destory',
				modal: true,
				alwaysOnTop: true,
				title: '<i class="fa fa-plus"></i>' + '<span class="shift-window-text-right">Add Dependency</span>',
				width: '50%',
				height: 470,
				layout: 'fit',				
				items: [
					{
						xtype: 'form',
						scrollable: true,
						itemId: 'depForm',
						bodyStyle: 'padding: 10px;',
						defaults: {
							labelAlign: 'top',
							labelSeparator: '',
							width: '100%'
						},
						items: [
							{
								xtype: 'hidden',
								name: 'dependencyId'
							},
							{
								xtype: 'textfield',
								fieldLabel: 'Name <span class="field-required" />',									
								allowBlank: false,									
								maxLength: '255',
								name: 'dependencyName'
							},
							{
								xtype: 'textfield',
								fieldLabel: 'Version <span class="field-required" />',									
								allowBlank: false,								
								maxLength: '255',
								name: 'version'
							},
							{
								xtype: 'textfield',
								fieldLabel: 'External Link',															
								emptyText: 'External Link',									
								maxLength: '255',
								name: 'dependancyReferenceLink'
							},
							{
								xtype: 'textfield',
								fieldLabel: 'Comment',																											
								maxLength: '255',
								name: 'comment'
							},							
							Ext.create('OSF.component.SecurityComboBox', {	
								itemId: 'securityMarkings',
								hidden: submissionPanel.hideSecurityMarkings
							}),
							Ext.create('OSF.component.DataSensitivityComboBox', {			
								width: '100%'
							})							
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
										handler: function(){
											var depWindow = this.up('window');
											var form = this.up('form');
											var data = form.getValues();
											var componentId = submissionPanel.componentId;

											var method = 'POST';
											var update = '';
											if (data.dependencyId) {
												update = '/' + data.dependencyId;
												method = 'PUT';
											}

											CoreUtil.submitForm({
												url: 'api/v1/resource/components/' + componentId + '/dependencies' + update,
												method: method,
												data: data,
												form: form,
												success: function(){
													grid.getStore().load({
														url: 'api/v1/resource/components/' + submissionPanel.componentId + '/dependencies/view'
													});
													depWindow.close();
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
											this.up('window').close();
										}
									}
								]
							}																
						]															
					}
				]
			}).show();
			
			if (record) {
				addWindow.getComponent('depForm').loadRecord(record);
			}			
		};
	
		var addEditRelationship = function(record, grid) {

			var addWindow = Ext.create('Ext.window.Window', {
				closeAction: 'destory',
				modal: true,
				title: '<i class="fa fa-plus"></i>' + '<span class="shift-window-text-right">Add Relationship</span>',
				alwaysOnTop: true,
				width: '50%',
				minHeight: 300,
				maxHeight: 450,
				layout: 'fit',
				items: [
					{
						xtype: 'form',
						scrollable: true,
						itemId: 'relationshipForm',
						bodyStyle: 'padding: 10px;',
						defaults: {
							labelAlign: 'top',
							labelSeparator: '',
							width: '100%'
						},
						items: [
							{
								xtype: 'hidden',
								name: 'relationshipId'
							},							
							Ext.create('OSF.component.StandardComboBox', {
								name: 'relationshipType',									
								allowBlank: false,
								editable: false,
								typeAhead: false,
								margin: '0 0 0 0',
								width: '100%',
								fieldLabel: 'Relationship Type <span class="field-required" />',
								storeConfig: {
									url: 'api/v1/resource/lookuptypes/RelationshipType'
								}
							}),
							Ext.create('OSF.component.StandardComboBox', {
								name: 'componentType',									
								allowBlank: true,
								editable: false,
								typeAhead: false,
								emptyText: 'All',
								margin: '0 0 0 0',
								width: '100%',
								fieldLabel: 'Entry Type',
								storeConfig: {
									url: 'api/v1/resource/componenttypes/lookup',
									addRecords: [
										{
											code: null,
											description: 'All'
										} 
									]
								},
								listeners: {
									change: function(cb, newValue, oldValue) {
										var form = cb.up('form');
										var componentType = '';
										if (newValue) {
											componentType = '&componentType=' + newValue;
										}
										form.getComponent('relationshipTargetCB').reset();
										form.getComponent('relationshipTargetCB').getStore().load({
											url: 'api/v1/resource/components/lookup?status=A&approvalState=ALL' + componentType		
										});
									}
								}
							}),	
							Ext.create('OSF.component.StandardComboBox', {																	
								itemId: 'relationshipTargetCB',
								name: 'relatedComponentId',									
								allowBlank: false,									
								margin: '0 0 0 0',
								width: '100%',
								fieldLabel: 'Target Entry <span class="field-required" />',
								forceSelection: true,
								storeConfig: {
									url: 'api/v1/resource/components/lookup?status=A&approvalState=ALL',
									autoLoad: true
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
										formBind: true,
										iconCls: 'fa fa-lg fa-save icon-button-color-save',
										handler: function(){
											var relationShipWindow = this.up('window');
											var form = this.up('form');
											var data = form.getValues();
											var componentId = submissionPanel.componentId;

											var method = 'POST';
											var update = '';
											if (data.relationshipId) {
												method = 'PUT',
												update = '/' + data.relationshipId;		
											}

											CoreUtil.submitForm({
												url: 'api/v1/resource/components/' + componentId + '/relationships' + update,
												method: method,
												data: data,
												form: form,
												success: function(){
													grid.getStore().load({
														url: 'api/v1/resource/components/' + submissionPanel.componentId + '/relationships'
													});																										
													relationShipWindow.close();
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
											this.up('window').close();
										}
									}
								]
							}																
						]															
					}
				]
			}).show();	
			
		
			if (record) {
				record.set('relatedComponentId', record.get('targetComponentId'));
				addWindow.getComponent('relationshipForm').loadRecord(record);
			}				
			
		};		
		
		submissionPanel.detailsPanel = Ext.create('Ext.panel.Panel', {
			autoScroll: true,
			items: [
				{
					xtype: 'panel',
					html: '<h1>3. Additional Details:</h1><h3>Fill in as many details as possible. The more details the easier it is for others to discover this entry.<br>Include additional points of contact, related screenshots and attributes</h3>'
				},
				{
					xtype: 'panel',
					itemId: 'detailSections', 
					bodyStyle: 'padding: 0px 20px 0px 20px;',
					items: [
						{
							xtype: 'panel',
							itemId: 'optionalAttributes-help',
							html: '<h3>Add all attributes that are appropriate.</h3>'
						},
						{
							xtype: 'grid',
							itemId: 'optionalAttributes',
							title: 'Additional Attributes <i class="fa fa-question-circle"  data-qtip="Attributes are filterable metadata about the entry."></i>',
							collapsible: true,
							titleCollapse: true,
							margin: '0 0 20 0',
							frame: true,	
							border: true,
							columnLines: true,
							store: Ext.create('Ext.data.Store', {
								autoLoad: false,
								proxy: {
									type: 'ajax',
									url: ''
								}
							}),
							forceFit: true,
							columns: [
								{ text: 'Attribute Type', dataIndex: 'typeDescription',  width: 250 },
								{ text: 'Attribute', dataIndex: 'codeDescription', flex: 1, width: 200 }
							],
							listeners: {
								afterrender: function(grid, opts){
									grid.getSelectionModel().grid = grid;
								},
								selectionchange:  function(selectionModel, selection, opts){
									var tools = selectionModel.grid.getComponent('tools');
									if (selectionModel.getCount() > 0) {
										tools.getComponent('editBtn').setDisabled(false);
										tools.getComponent('removeBtn').setDisabled(false);
									} else {
										tools.getComponent('editBtn').setDisabled(true);
										tools.getComponent('removeBtn').setDisabled(true);
									}
								}
							},
							dockedItems: [
								{
									xtype: 'toolbar',
									itemId: 'tools',
									items: [
										{
											text: 'Add',
											iconCls: 'fa fa-lg fa-plus icon-button-color-save',
											handler: function(){
												addEditAttribute();
											}
										},
										{
											text: 'Edit',
											itemId: 'editBtn',
											iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
											disabled: true,
											handler: function(){												
												var record = this.up('grid').getSelectionModel().getSelection()[0];
												addEditAttribute(record);
											}
										},										
										{
											xtype: 'tbfill'
										},
										{
											text: 'Delete',	
											itemId: 'removeBtn',
											iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
											disabled: true,
											handler: function(){
												actionSubComponentRemove({
													grid: this.up('grid'),
													idField: 'type',
													entity: 'attributes',													
													subEntityId: 'code',
													successFunc: function(reponse, opt) {
														submissionPanel.loadComponentAttributes();
													}
												});
											}
										}								
									]
								}
							]
						},
						{
							xtype: 'panel',
							itemId: 'contactGrid-help',
							html: '<h3>Add government, technical, and other points of contact.</h3>'
						},						
						{
							xtype: 'grid',
							itemId: 'contactGrid',
							title: 'Contacts  <i class="fa fa-question-circle"  data-qtip="Add government, technical, and other points of contact."></i>',
							collapsible: true,
							titleCollapse: true,
							margin: '0 0 20 0',
							frame: true,	
							border: true,
							columnLines: true,
							store: Ext.create('Ext.data.Store', {
								autoLoad: false,								
								proxy: {
									type: 'ajax'							
								}
							}),
							forceFit: true,
							columns: [
								{ text: 'Contact Type', dataIndex: 'positionDescription',  width: 200 },
								{ text: 'First Name',  dataIndex: 'firstName', width: 200 },
								{ text: 'Last Name',  dataIndex: 'lastName', width: 200 },
								{ text: 'Email',  dataIndex: 'email', width: 200 },
								{ text: 'Phone',  dataIndex: 'phone', width: 150 },
								{ text: 'Organization',  dataIndex: 'organization', width: 200 },
								{ text: 'Security Marking', itemId: 'securityMarking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: true }								
							],
							listeners: {						
								afterrender: function(grid, opts){
									grid.getSelectionModel().grid = grid;
								},
								selectionchange:  function(selectionModel, selection, opts){
									var tools = selectionModel.grid.getComponent('tools');
									if (selectionModel.getCount() > 0) {
										tools.getComponent('removeBtn').setDisabled(false);
										tools.getComponent('editBtn').setDisabled(false);
									} else {
										tools.getComponent('removeBtn').setDisabled(true);
										tools.getComponent('editBtn').setDisabled(true);
									}
								}
							},
							dockedItems: [
								{
									xtype: 'toolbar',
									itemId: 'tools',
									items: [
										{
											text: 'Add',
											iconCls: 'fa fa-lg fa-plus icon-button-color-save',
											handler: function(){
												var grid = this.up('grid');
												addEditContact(null, grid);
											}
										},
										{
											text: 'Edit',
											itemId: 'editBtn',
											iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
											disabled: true,
											handler: function(){
												var grid = this.up('grid');
												var record = this.up('grid').getSelectionModel().getSelection()[0];
												addEditContact(record, grid);
											}
										},										
										{
											xtype: 'tbfill'
										},
										{
											text: 'Delete',
											itemId: 'removeBtn',											
											disabled: true,
											iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
											handler: function(){
												actionSubComponentRemove({
													grid: this.up('grid'),
													idField: 'componentContactId',
													entity: 'contacts'
												});
											}
										}								
									]
								}
							]
						},
						{
							xtype: 'panel',
							itemId: 'resourceGrid-help',
							html: '<h3>Add direct links to external Documentation, Binaries, Source Code, etc... where appropriate.</h3>'
						},						
						{
							xtype: 'grid',
							itemId: 'resourceGrid',
							title: 'External Links  <i class="fa fa-question-circle"  data-qtip="Add links to home page, documentation, binaries, etc... If there is no external link for a resource it may be uploaded so that it may be shared with the community.  Typically, the resources are managed externally. "></i>',
							collapsible: true,
							titleCollapse: true,
							margin: '0 0 20 0',
							frame: true,	
							border: true,
							columnLines: true,
							store: Ext.create('Ext.data.Store', {
								autoLoad: false,
								proxy: {
									type: 'ajax'							
								}
							}),
							forceFit: true,
							columns: [
								{ text: 'Resource Type', dataIndex: 'resourceTypeDesc',  width: 200 },
								{ text: 'Description',  dataIndex: 'description', width: 150 },
								{ text: 'Link',  dataIndex: 'originalLink', flex: 1, minWidth: 200 },
								{ text: 'Mime Type',  dataIndex: 'mimeType', width: 200 },
								{ text: 'Local Resource Name',  dataIndex: 'originalFileName', width: 200 },
								{ text: 'Restricted',  dataIndex: 'restricted', width: 150 },
								{ text: 'Security Marking', itemId: 'securityMarking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: true }								
							],
							listeners: {						
								afterrender: function(grid, opts){
									grid.getSelectionModel().grid = grid;
								},
								selectionchange:  function(selectionModel, selection, opts){
									var tools = selectionModel.grid.getComponent('tools');
									if (selectionModel.getCount() > 0) {
										tools.getComponent('removeBtn').setDisabled(false);
										tools.getComponent('editBtn').setDisabled(false);
									} else {
										tools.getComponent('removeBtn').setDisabled(true);
										tools.getComponent('editBtn').setDisabled(false);
									}
								}
							},
							dockedItems: [
								{
									xtype: 'toolbar',
									itemId: 'tools',
									items: [
										{
											text: 'Add',
											iconCls: 'fa fa-lg fa-plus icon-button-color-save',
											handler: function(){
												var grid = this.up('grid');
												addEditResource(null, grid);
											}
										},
										{
											text: 'Edit',
											itemId: 'editBtn',
											iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
											disabled: true,
											handler: function(){
												var grid = this.up('grid');
												var record = this.up('grid').getSelectionModel().getSelection()[0];
												addEditResource(record, grid);
											}
										},										
										{
											xtype: 'tbfill'
										},
										{
											text: 'Delete',	
											itemId: 'removeBtn',
											disabled: true,
											iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
											handler: function(){
												actionSubComponentRemove({
													grid: this.up('grid'),
													idField: 'resourceId',
													entity: 'resources'
												});
											}
										}								
									]
								}
							]
						},
						{
							xtype: 'panel',
							itemId: 'mediaGrid-help',
							html: '<h3>Upload screenshots and related media.</h3>'
						},						
						{
							xtype: 'grid',
							itemId: 'mediaGrid',
							title: 'Media  <i class="fa fa-question-circle"  data-qtip="Media show up in the media carousel section on the details page for this component.<br><br>Add screen shots, company logos, short videos clips, etc... Typically, media is uploaded as that makes it the most accessible.  External resources can be pointed to by using an external link. External resources work best with a valid SSL certificate and are unrestricted (ie. no login)."></i>',
							collapsible: true,
							titleCollapse: true,
							margin: '0 0 20 0',
							frame: true,	
							border: true,
							columnLines: true,
							store: Ext.create('Ext.data.Store', {
								autoLoad: false,
								proxy: {
									type: 'ajax'							
								}
							}),
							forceFit: true,
							columns: [
								{ text: 'Media Type', dataIndex: 'contentType',  width: 200 },
								{ text: 'Caption',  dataIndex: 'caption', width: 200 },
								{ text: 'Mime Type',  dataIndex: 'mimeType', width: 200 },
								{ text: 'Local Media Name',  dataIndex: 'originalFileName', width: 200 },
								{ text: 'Link',  dataIndex: 'originalLink', width: 200 },
								{ text: 'Icon Flag',  dataIndex: 'iconFlag', width: 150, hidden: true },
								{ text: 'Security Marking', itemId: 'securityMarking', dataIndex: 'securityMarkingDescription', width: 150, hidden: true }								
							],
							listeners: {						
								afterrender: function(grid, opts){
									grid.getSelectionModel().grid = grid;
								},
								selectionchange:  function(selectionModel, selection, opts){
									var tools = selectionModel.grid.getComponent('tools');
									if (selectionModel.getCount() > 0) {
										tools.getComponent('removeBtn').setDisabled(false);
										tools.getComponent('editBtn').setDisabled(false);
									} else {
										tools.getComponent('removeBtn').setDisabled(true);
										tools.getComponent('editBtn').setDisabled(true);
									}
								}
							},
							dockedItems: [
								{
									xtype: 'toolbar',
									itemId: 'tools',
									items: [
										{
											text: 'Add',
											iconCls: 'fa fa-lg fa-plus icon-button-color-save',
											handler: function(){
												var grid = this.up('grid');
												addEditMedia(null, grid);
											}
										},
										{
											text: 'Edit',
											itemId: 'editBtn',
											disabled: true,
											iconCls: 'fa fa-lg fa-pencil-square-o icon-button-color-edit',
											handler: function(){
												var grid = this.up('grid');
												var record = this.up('grid').getSelectionModel().getSelection()[0];
												addEditMedia(record, grid);
											}
										},										
										{
											xtype: 'tbfill'
										},
										{
											text: 'Delete',
											itemId: 'removeBtn',
											disabled: true,
											iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
											handler: function(){
												var record = this.up('grid').getSelection()[0];
												var neededGrid = this.up('grid');
												if (record.get('usedInline')) {
													var msg = 'This media has been marked as being used inline. This means that the media is being used in a description, etc. ';
													msg += 'If you delete this media, that reference will no longer be valid and the media will not be available where it is referenced elsewhere.';
													msg += '<br><br>Do you still wish to delete this media?';
													Ext.Msg.confirm('Media Used Inline', msg, function (btn) { 
														if (btn ==='yes') {
															actionSubComponentRemove({
																grid: neededGrid,
																idField: 'componentMediaId',
																entity: 'media'
															});
														}
													});
												} else {
													actionSubComponentRemove({
														grid: this.up('grid'),
														idField: 'componentMediaId',
														entity: 'media'
													});
												}

											}
										}								
									]
								}
							]
						},
						{
							xtype: 'panel',
							itemId: 'dependenciesGrid-help',
							html: '<h3>Add necessary software/hardware dependancies not included with the component.</h3>'
						},							
						{
							xtype: 'grid',
							itemId: 'dependenciesGrid',
							title: 'Dependencies  <i class="fa fa-question-circle"  data-qtip="Describe what is needed to utilize the component. Enter each dependency separately.  (Eg. Java 7+,  MySql 5+)"></i>',
							collapsible: true,
							titleCollapse: true,
							margin: '0 0 20 0',
							frame: true,	
							border: true,
							columnLines: true,
							store: Ext.create('Ext.data.Store', {
								autoLoad: false,
								proxy: {
									type: 'ajax'							
								}
							}),
							forceFit: true,
							columns: [
								{ text: 'Name', dataIndex: 'dependencyName',  width: 200 },
								{ text: 'Version',  dataIndex: 'version', width: 150 },
								{ text: 'Link',  dataIndex: 'dependancyReferenceLink', width: 200 },
								{ text: 'Comment',  dataIndex: 'comment', flex: 1, minWidth: 200 },
								{ text: 'Security Marking', itemId: 'securityMarking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: true }								
							],
							listeners: {						
								afterrender: function(grid, opts){
									grid.getSelectionModel().grid = grid;
								},
								selectionchange:  function(selectionModel, selection, opts){
									var tools = selectionModel.grid.getComponent('tools');
									if (selectionModel.getCount() > 0) {
										tools.getComponent('removeBtn').setDisabled(false);
										tools.getComponent('editBtn').setDisabled(false);
									} else {
										tools.getComponent('removeBtn').setDisabled(true);
										tools.getComponent('editBtn').setDisabled(true);
									}
								}
							},
							dockedItems: [
								{
									xtype: 'toolbar',
									itemId: 'tools',
									items: [
										{
											text: 'Add',
											iconCls: 'fa fa-lg fa-plus icon-button-color-save',
											handler: function(){
												var grid = this.up('grid');
												addEditDependency(null, grid);
											}
										},
										{
											text: 'Edit',
											itemId: 'editBtn',
											iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
											disabled: true,
											handler: function(){
												var grid = this.up('grid');
												var record = this.up('grid').getSelectionModel().getSelection()[0];
												addEditDependency(record, grid);
											}
										},										
										{
											xtype: 'tbfill'
										},
										{
											text: 'Delete',	
											itemId: 'removeBtn',
											disabled: true,
											iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
											handler: function(){
												actionSubComponentRemove({
													grid: this.up('grid'),
													idField: 'dependencyId',
													entity: 'dependencies'
												});
											}
										}								
									]
								}
							]
						},
						{
							xtype: 'panel',
							itemId: 'relationshipsGrid-help',
							html: '<h3>Add relationship links to existing entries. Eg. DDF is a component of DIB</h3>'
						},						
						{
							xtype: 'grid',
							itemId: 'relationshipsGrid',
							title: 'Relationships  <i class="fa fa-question-circle"  data-qtip="Relationships are used to show connections between entries."></i>',
							collapsible: true,
							titleCollapse: true,
							margin: '0 0 20 0',
							frame: true,	
							border: true,
							columnLines: true,
							store: Ext.create('Ext.data.Store', {
								autoLoad: false,
								proxy: {
									type: 'ajax',
									url: ''
								}
							}),
							forceFit: true,
							columns: [
								{ text: 'Relationship Owner', dataIndex: 'ownerComponentName',  width: 200 },
								{ text: 'Owner Approved', dataIndex: 'ownerApproved',  width: 150 },
								{ text: 'Type',  dataIndex: 'relationshipTypeDescription', width: 200 },
								{ text: 'Target',  dataIndex: 'targetComponentName', width: 200 },						
								{ text: 'Target Approved',  dataIndex: 'targetApproved', width: 150 }						
							],
							listeners: {						
								afterrender: function(grid, opts){
									grid.getSelectionModel().grid = grid;
								},
								selectionchange:  function(selectionModel, selection, opts){
									var tools = selectionModel.grid.getComponent('tools');
									if (selectionModel.getCount() > 0) {
										tools.getComponent('editBtn').setDisabled(false);
										tools.getComponent('removeBtn').setDisabled(false);
									} else {
										tools.getComponent('editBtn').setDisabled(true);
										tools.getComponent('removeBtn').setDisabled(true);
									}
								}
							},
							dockedItems: [
								{
									xtype: 'toolbar',
									itemId: 'tools',
									items: [
										{
											text: 'Add',
											iconCls: 'fa fa-lg fa-plus icon-button-color-save',
											handler: function(){
												var grid = this.up('grid');												
												addEditRelationship(null, grid);
												
											}
										},
										{
											text: 'Edit',
											itemId: 'editBtn',
											iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
											disabled: true,
											handler: function(){	
												var grid = this.up('grid');	
												var record = this.up('grid').getSelectionModel().getSelection()[0];
												addEditRelationship(record, grid);
											}
										},										
										{
											xtype: 'tbfill'
										},
										{
											text: 'Delete',
											itemId: 'removeBtn',
											disabled: true,
											iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
											handler: function(){
												actionSubComponentRemove({
													grid: this.up('grid'),
													idField: 'relationshipId',
													entity: 'relationships'
												});
											}
										}								
									]
								}
							]
						},
						{
							xtype: 'panel',
							itemId: 'tagGrid-help',
							html: '<h3>Add relevant tags to help with discovery.</h3>'
						},						
						{
							xtype: 'grid',
							itemId: 'tagGrid',
							title: 'Tags  <i class="fa fa-question-circle"  data-qtip="Add keywords that can help potential user discover the entry."></i>',
							collapsible: true,
							titleCollapse: true,
							margin: '0 0 20 0',
							frame: true,	
							border: true,
							columnLines: true,
							store: Ext.create('Ext.data.Store', {
								autoLoad: false,
								proxy: {
									type: 'ajax'							
								}
							}),
							forceFit: true,
							columns: [
								{ text: 'Tag', dataIndex: 'text', flex: 1, minWidth: 200 },
								{ text: 'Security Marking',  itemId: 'securityMarking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: true }
							],
							listeners: {		
								afterrender: function(grid, opts){
									grid.getSelectionModel().grid = grid;
								},
								selectionchange:  function(selectionModel, selection, opts){
									var tools = selectionModel.grid.getComponent('tools');
									if (selectionModel.getCount() > 0) {
										tools.getComponent('removeBtn').setDisabled(false);
									} else {
										tools.getComponent('removeBtn').setDisabled(true);
									}
								}
							},
							dockedItems: [
								{
									xtype: 'toolbar',
									itemId: 'tools',
									items: [
										{
											text: 'Add',
											iconCls: 'fa fa-lg fa-plus icon-button-color-save',
											handler: function(){
												var grid = this.up('grid');
												
												var addTag = function(addWindow){
													var form = addWindow.down('form');
													var data = form.getValues();
													var componentId = submissionPanel.componentId;
													
													CoreUtil.submitForm({
														url: 'api/v1/resource/components/' + componentId + '/tags',
														method: 'POST',
														data: data,
														form: form,
														success: function(){
															grid.getStore().load({
																url: 'api/v1/resource/components/' + submissionPanel.componentId + '/tags'
															});
															addWindow.close();
														}
													});												
												};
												
												var addWindow = Ext.create('Ext.window.Window', {
													closeAction: 'destory',
													modal: true,
													alwaysOnTop: true,
													title: '<i class="fa fa-plus"></i>' + '<span class="shift-window-text-right">Add Tag</span>',
													width: '40%',
													minHeight: 200,
													maxHeight: 450,
													layout: 'fit',
													items: [
														{
															xtype: 'form',
															scrollable: true,
															bodyStyle: 'padding: 10px',
															defaults: {
																labelAlign: 'top'
															},
															items: [
																Ext.create('OSF.component.StandardComboBox', {
																	name: 'text',									
																	allowBlank: false,									
																	margin: '0 0 0 0',
																	width: '100%',
																	fieldLabel: 'Tag<span class="field-required" />',
																	forceSelection: false,
																	valueField: 'text',
																	displayField: 'text',
																	maxLength: 120,
																	storeConfig: {
																		url: 'api/v1/resource/components/tags'
																	}
																}),
																Ext.create('OSF.component.SecurityComboBox', {	
																	itemId: 'securityMarkings',
																	hidden: submissionPanel.hideSecurityMarkings
																})																
															],
															dockedItems: [
																{
																	xtype: 'toolbar',
																	dock: 'bottom',
																	items: [
																		{
																			text: 'Add',
																			formBind: true,
																			iconCls: 'fa fa-lg fa-plus icon-button-color-save',
																			handler: function(){																				
																				addTag(this.up('window'));
																			}
																		},
																		{
																			xtype: 'tbfill'
																		},
																		{
																			text: 'Cancel',										
																			iconCls: 'fa fa-lg fa-close icon-button-color-warning',
																			handler: function(){
																				this.up('window').close();
																			}
																		}																		
																	]
																}
															]
														}
													]
												}).show();
											}
										},
										{
											xtype: 'tbfill'
										},
										{
											text: 'Delete',
											itemId: 'removeBtn',											
											disabled: true,
											iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
											handler: function(){
												actionSubComponentRemove({
													grid: this.up('grid'),
													idField: 'tagId',
													entity: 'tags'
												});
											}
										}								
									]
								}
							]
						}						
					]
				}

			]			
		});		
		
		var reviewViewTemplate = new Ext.XTemplate();
		Ext.Ajax.request({
			url: 'Router.action?page=shared/entryCompareTemplate.jsp',
			success: function(response, opts){
				reviewViewTemplate.set(response.responseText, true);
			}
		});		
		
		submissionPanel.reviewPanel = Ext.create('Ext.panel.Panel', {
			layout: 'vbox',
			listeners: {
				activate: function () {
					var initialToggleElement = document.querySelectorAll('.toggle-collapse')[0];

					// Wait until the the template has been rendered
					var templateStateCheckInterval = setInterval(function () {

						// the template has refreshed
						if (initialToggleElement != document.querySelectorAll('.toggle-collapse')[0]) {
							clearInterval(templateStateCheckInterval);
							var toggleElements = document.querySelectorAll('.toggle-collapse');
							for (ii = 0; ii < toggleElements.length; ii += 1) {
								toggleElements[ii].removeEventListener('click', CoreUtil.toggleEventListener);
								toggleElements[ii].addEventListener('click', CoreUtil.toggleEventListener);
							}
						}
					}, 100);
				}
			},
			items: [
				{
					xtype: 'panel',
					html: '<h1>4. Review and Submit Entry:</h1>'
				},
				{		
					xtype: 'panel',					
					frame: true,
					border: true,
					width: '100%',					
					padding: '0 0 0 0',
					html: '<span class="app-info-box"><i class="fa fa-2x fa-info-circle"></i></span> <div style="padding: 10px 0px 10px 10px;">This entry must be submitted before an admin can approve it.</div>'
				}, 
				{
					xtype: 'checkbox',
					itemId: 'approvalNotification',
					boxLabel: 'Email an Approval Notification',
					checked: true,
					name: 'sendApprovalNotification'					
				},
				{
					xtype: 'panel',
					itemId: 'reviewEntryPanel',
					title: 'Entry Quick View',
					flex: 1,
					width: '100%',
					frame: true,
					border: true,
					autoScroll: true,
					bodyStyle: 'padding: 10px;',
					tpl: reviewViewTemplate
				}
			]			
		});		
		
		submissionPanel.mainPanel = Ext.create('Ext.panel.Panel', {
			layout: 'card',
			region: 'center',
			bodyStyle: 'background: white; padding: 10px;',
			items: [
				submissionPanel.submitterForm,
				submissionPanel.requiredForm,
				submissionPanel.detailsPanel,
				submissionPanel.reviewPanel
			],
			dockedItems: [
				{
					xtype: 'panel',
					itemId: 'userInputWarning',
					dock: 'top',
					html: ''
				},
				{
					xtype: 'toolbar',
					dock: 'bottom',
					itemId: 'tools',
					items: [
						{
							text: 'Previous',
							itemId: 'Previous',
							disabled: true,
							scale: 'large',
							iconCls: 'fa fa-2x fa-arrow-left icon-button-color-default icon-top-padding-2',
							handler: function () {
								submissionPanel.currentStep--;
								submissionPanel.changeSteps();
							}
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Save and Exit',
							itemId: 'SaveAndExit',
							hidden: true,
							scale: 'large',
							iconCls: 'fa fa-2x fa-save icon-button-color-save icon-top-padding-2',
							handler: function () {
																
																
							}
						},						
						{
							text: '<span style="color:green">Submit For Review</span>',
							itemId: 'Submit',
							hidden: true,
							scale: 'large',							
							iconCls: 'fa fa-2x fa-check icon-top-padding-2 icon-button-color-refresh',														
							handler: function () {
								
								submissionPanel.setLoading('Submitting Entry...');
								Ext.Ajax.request({
									url: submissionPanel.submitForReviewUrl(submissionPanel.componentId),
									method: 'PUT',
									callback: function(){
										submissionPanel.setLoading(false);
									},
									success: function(response, opts){
										Ext.toast('Entry has been submitted for approval.', 'Success');
										
										//set notification
										var newEmail = submissionPanel.submitterForm.getForm().findField('email').getValue();
										if (submissionPanel.reviewPanel.getComponent('approvalNotification').getValue() === false) {																
											newEmail = null;												
										}
										
										submissionPanel.setLoading('Updating Notification...');
										Ext.Ajax.request({
											url: 'api/v1/resource/componentsubmissions/' + submissionPanel.componentId + '/setNotifyMe',
											method: 'PUT',
											rawData: newEmail,
											callback: function(){
												submissionPanel.setLoading(false);
											},
											success: function(response, opts) {
												Ext.toast('Updated notification');
												
												if (submissionPanel.handleSubmissionSuccess) {
													submissionPanel.handleSubmissionSuccess(response, opts);
												}
											}
										});	
									}
								});								
																
							}
						},												
						{
							text: 'Save and Edit Later',
							itemId: 'SaveLater',
							scale: 'large',
							hidden: true,
							iconCls: 'fa fa-2x fa-save icon-button-color-save icon-top-padding-2',							
							handler: function () {					
								var promptForSave = false;
								submissionPanel.cancelSubmissionHandler(promptForSave);																								
							}
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Next',
							itemId: 'Next',
							scale: 'large',
							iconCls: 'fa fa-2x fa-arrow-right icon-button-color-default icon-top-padding-2',
							iconAlign: 'right',
							handler: function () {
								submissionPanel.currentStep++;
								submissionPanel.changeSteps();
							}
						}
					]
				}
			]
		});
		
		//Query Branding
		CoreService.brandingservice.getCurrentBranding().then(function(branding){			
			if (branding.userInputWarning) {
				submissionPanel.mainPanel.getComponent('userInputWarning').update('<div class="alert-warning" style="text-align: center">' + 
				'<i class="fa fa-warning"></i> ' + branding.userInputWarning + 
				'</div>');						
			}
			if (branding.allowSecurityMarkingsFlg) {
				submissionPanel.requiredForm.getComponent('securityMarkings').setHidden(false);
				submissionPanel.hideSecurityMarkings = false;
								
				
				var sections = submissionPanel.detailsPanel.getComponent('detailSections');					
				
				submissionPanel.detailsPanel.on('activate', function(panel, opts){
					sections.getComponent('contactGrid').down('#securityMarking').setHidden(false);
					sections.getComponent('resourceGrid').down('#securityMarking').setHidden(false);
					sections.getComponent('mediaGrid').down('#securityMarking').setHidden(false);
					sections.getComponent('dependenciesGrid').down('#securityMarking').setHidden(false);					
					sections.getComponent('tagGrid').down('#securityMarking').setHidden(false);						
				});
			}
		});
	
		
		submissionPanel.currentStep = 1;
		submissionPanel.changeSteps = function(forceProceed) {	
			
			// Get Tools
			var tools = submissionPanel.mainPanel.getComponent('tools');
					
			// Initialize Proceed Flag
			var proceed = false;
			
			// Check If Proceeding Is Forced
			if (forceProceed) {
				
				// Proceed
				proceed = true;
			}
			else {
				
				// Check Current Step
				if (submissionPanel.currentStep === 2) {
					
					// Validate Step 1
					if (submissionPanel.submitterForm.isValid()) {
						
						// Proceed
						proceed = true;
					}
					else {
						
						// Provide Error Feedback
						Ext.Msg.show({
							
							title: 'Validation',
							message: 'All required fields must be filled in with valid values.  (See Step 1)',
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.ERROR,
							fn: function(btn) { }
						});
						
						// Return To Step 1
						submissionPanel.currentStep = 1;
					}
				}
				else if (submissionPanel.currentStep === 3) {

					// Validate Step 2
					if (!submissionPanel.requiredForm.isValid()) {
						
						// Provide Error Feedback
						Ext.Msg.show({
							
							title: 'Validation',
							message: 'All required fields must be filled in with valid values.  (See Step 2)',
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.ERROR,
							fn: function(btn) { }
						});	
						
						// Get Form
						var form = submissionPanel.requiredForm;
						
						// Get Form Data
						var data = form.getValues();
						
						// Ensure Description Is Filled In
						if (!data.description) {
							
							// Mark Field As Invalid
							form.getForm().markInvalid({
								
								description: 'Required'
							});
						}

						// Return To Step 2
						submissionPanel.currentStep = 2;
					}
					else {

//						// Return To Step 2
//						submissionPanel.currentStep = 2;
						
						// Save Data From Step 2
						submissionPanel.handleRequiredFormSave(function(){																	
							submissionPanel.currentStep = 3;
							submissionPanel.changeSteps(3);		
						});
						
						// Proceed
						proceed = false;
						submissionPanel.currentStep = 2;						
					}
				}
				else if (submissionPanel.currentStep === 4) {

					// Validate Step 1
					if (submissionPanel.submitterForm.isValid()) {

						// Validate Step 2
						if (!submissionPanel.requiredForm.isValid()) {
							
							// Provide Error Feedback
							Ext.Msg.show({
								
								title: 'Validation',
								message: 'All required fields must be filled in with valid values.  (See Step 2)',
								buttons: Ext.Msg.OK,
								icon: Ext.Msg.ERROR,
								fn: function(btn) { }
							});
							
							// Get Form
							var form = submissionPanel.requiredForm;
							
							// Get Form Data
							var data = form.getValues();

							// Ensure Description Is Filled In
							if (!data.description) {
								
								// Mark Field As Invalid
								form.getForm().markInvalid({
									
									description: 'Required'
								});
							}

							// Return To Step 2
							submissionPanel.currentStep = 2;
						} else {
							
							// Save Data From Step 2
							submissionPanel.handleRequiredFormSave(function(){
								// Get Panel
								var reviewEntryPanel = submissionPanel.reviewPanel.getComponent('reviewEntryPanel');

								// Mask Panel
								reviewEntryPanel.setLoading('Loading preview...');

								// Request From Server
								Ext.Ajax.request({

									url: 'api/v1/resource/components/' + submissionPanel.componentId + '/detail',
									callback: function(){

										// Remove Panel Mask
										reviewEntryPanel.setLoading(false);
									},
									success: function(response, opts){
										// Get Response Data
										var data = Ext.decode(response.responseText);

										// Initialize Empty Attribute Array
										var attributesToShow = [];

										// Loop Through Attributes
										Ext.Array.each(data.attributes, function(item) {

											// Check If Attribute Should Be Shown
											if (!item.hideOnSubmission) {

												// Add Shown Attribute To Array
												attributesToShow.push(item);
											}
										});

										// Remove Hidden Attributes
										data.attributes = attributesToShow;	

										// Process Complete Record Data
										data = CoreUtil.processEntry(data);

										CoreUtil.calculateEvalutationScore({
											fullEvaluations: data.fullEvaluations,
											evaluation: data.fullEvaluations,
											success: function (newData) {
												data.fullEvaluations = newData.fullEvaluations;
												// Display Complete Record Data
												reviewEntryPanel.update(data);
											}
										});
								
									}
								});
														
							});
							
							// Proceed
							proceed = true;							
						}
					} else {
						
						// Provide Error Feedback
						Ext.Msg.show({
							
							title: 'Validation',
							message: 'All required fields must be filled in with valid values.  (See Step 1)',
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.ERROR,
							fn: function(btn) { }
						});
						
						// Return To Step 1
						submissionPanel.currentStep = 1;
					}
				}
				else {
					
					// Proceed
					proceed = true;
				}
			}
			
			// Check If User Can Proceed
			if (proceed) {
				
				// Get Tools
				tools.getComponent('Submit').setHidden(true);

				// Check If Form Is In Edit Mode
				if (submissionPanel.editMode) {
					
					// Enable All Buttons
					submissionPanel.navigation.getComponent('step1Btn').setDisabled(false);
					submissionPanel.navigation.getComponent('step2Btn').setDisabled(false);			
					submissionPanel.navigation.getComponent('step3Btn').setDisabled(false);
					submissionPanel.navigation.getComponent('step4Btn').setDisabled(false);
				}
				else {
					
					// Enable One Button
					submissionPanel.navigation.getComponent('step1Btn').setDisabled(false);
					submissionPanel.navigation.getComponent('step2Btn').setDisabled(true);			
					submissionPanel.navigation.getComponent('step3Btn').setDisabled(true);
					submissionPanel.navigation.getComponent('step4Btn').setDisabled(true);					
				}

				// Clear Button Styling
				submissionPanel.navigation.getComponent('step1Btn').setIconCls('');
				submissionPanel.navigation.getComponent('step2Btn').setIconCls('');
				submissionPanel.navigation.getComponent('step3Btn').setIconCls('');
				submissionPanel.navigation.getComponent('step4Btn').setIconCls('');

				// Hide Save Buttons
				tools.getComponent('SaveAndExit').setHidden(true);
				tools.getComponent('SaveLater').setHidden(true);

				// Check Current Step
				if (submissionPanel.currentStep === 1) {
					
					// Handle Next/Previous Buttons
					tools.getComponent('Previous').setDisabled(true);
					tools.getComponent('Next').setDisabled(false);				

					// Activate Form
					submissionPanel.mainPanel.getLayout().setActiveItem(submissionPanel.submitterForm);
				}
				else if (submissionPanel.currentStep === 2) {
					
					// Handle Next/Previous Buttons
					tools.getComponent('Previous').setDisabled(false);
					tools.getComponent('Next').setDisabled(false);	
					
					// Indicate Step 1 Complete
					submissionPanel.navigation.getComponent('step1Btn').setIconCls('fa fa-check');
					
					// Prohibit Reloading Same Step
					submissionPanel.navigation.getComponent('step2Btn').setDisabled(false);

					// Activate Required Panel
					submissionPanel.mainPanel.getLayout().setActiveItem(submissionPanel.requiredForm);
					
					// Pause Briefly
					Ext.defer(function() {
						
						// Update Panel
						submissionPanel.mainPanel.updateLayout(true, true);
					}
					, 200);					
					
					// Display Save Later Button
					tools.getComponent('SaveLater').setHidden(false);
					
				}
				else if (submissionPanel.currentStep === 3) {
					
					// Handle Next/Previous Buttons
					tools.getComponent('Previous').setDisabled(false);
					tools.getComponent('Next').setDisabled(false);

					// Set Button Styling & Enable Buttons
					submissionPanel.navigation.getComponent('step1Btn').setIconCls('fa fa-check');
					submissionPanel.navigation.getComponent('step2Btn').setDisabled(false);
					submissionPanel.navigation.getComponent('step2Btn').setIconCls('fa fa-check');
					submissionPanel.navigation.getComponent('step3Btn').setDisabled(false);

					// Enable Save Later Button
					tools.getComponent('SaveLater').setHidden(false);
					
					// Activate Details Panel
					submissionPanel.mainPanel.getLayout().setActiveItem(submissionPanel.detailsPanel);
					
					// Update Panel
					Ext.defer(function(){
						submissionPanel.detailsPanel.updateLayout(true, true);
					}, 250);					
				}
				else if (submissionPanel.currentStep === 4) {
					
					// Handle Next/Previous Buttons
					tools.getComponent('Previous').setDisabled(false);
					tools.getComponent('Next').setDisabled(true);

					// Enable Save Later & Submit Buttons
					tools.getComponent('SaveLater').setHidden(false);
					tools.getComponent('Submit').setHidden(false);
					

					
					// Set Button Styles & Enable Buttons
					submissionPanel.navigation.getComponent('step1Btn').setIconCls('fa fa-check');
					submissionPanel.navigation.getComponent('step2Btn').setDisabled(false);
					submissionPanel.navigation.getComponent('step2Btn').setIconCls('fa fa-check');
					submissionPanel.navigation.getComponent('step3Btn').setDisabled(false);
					submissionPanel.navigation.getComponent('step3Btn').setIconCls('fa fa-check');				
					submissionPanel.navigation.getComponent('step4Btn').setDisabled(false);

					// Activate Review PAnel
					submissionPanel.mainPanel.getLayout().setActiveItem(submissionPanel.reviewPanel);
				}				
			}
		};	
		
		submissionPanel.saveReady = true;
		submissionPanel.saveRequiredFormTask = new Ext.util.DelayedTask();
		
		submissionPanel.handleRequiredFormSave = function(successCallback){
			submissionPanel.saveRequiredFormTask.delay(500, function(){
				if (!submissionPanel.saveReady) {
					return;
				}

				var form = submissionPanel.requiredForm;
				var data = form.getValues();
				var componentId = '';
				var method = 'POST';
				var update = '';

				data.approvalState = 'N';

				if (submissionPanel.componentId){
					componentId = submissionPanel.componentId;											
					update = '/' + componentId;
					method = 'PUT';					
				}												

				var requireComponent = {
					component: data,
					attributes: []
				};

				submissionPanel.requiredAttributeStore.each(function(record){

					requireComponent.attributes.push({
						componentAttributePk: {
							attributeType: record.get('attributeType'),
							attributeCode: record.get('attributeCode')
						}
					});
				});

				if (!data.description) {
					form.getForm().markInvalid({
						description: 'Required'
					});
				} 
				else {
					//make sure required 
					var validAttributes=true;
					Ext.Array.each(requireComponent.attributes, function(attribute){
						if (!attribute.componentAttributePk.attributeCode){
							validAttributes = false;
						}
					});

					if (!validAttributes) {

						submissionPanel.requiredAttributeStore.each(function(record){
							if (!record.get('attributeCode')) {
								record.formField.markInvalid('Required');
							}
						});							

						Ext.Msg.show({
							title:'Validation Check',
							message: 'Missing Required Attributes',
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.ERROR,
							fn: function(btn) {													 
							}
						});

					} else {	
						submissionPanel.saveReady = false;

						var handleMainFormSave = function() {
							CoreUtil.removeBlankDataItem(requireComponent.component);												
							CoreUtil.submitForm({
							url: 'api/v1/resource/components' + update,
							method: method,
							data: requireComponent,
							loadingText: 'Saving Entry...',
							removeBlankDataItems: true,
							form: form,
							success: function(response, opt){
								Ext.toast('Successfully Saved Record', '', 'tr');

								var data = Ext.decode(response.responseText);
								if (data.componentId) {
									submissionPanel.componentId = data.componentId;
								} else {
									submissionPanel.componentId = data.component.componentId;
								}

								//save profile updates
								submissionPanel.setLoading('Updating Profile...');								
								var userProfile = Ext.apply(submissionPanel.usercontext, submissionPanel.submitterForm.getValues());
								userProfile.externalGuid = userProfile.guid;

								Ext.Ajax.request({
									url: 'api/v1/resource/userprofiles/' + userProfile.username,
									method: 'PUT',
									jsonData: userProfile,
									callback: function() {
										submissionPanel.setLoading(false);
									},
									success: function (response, opts) {
										Ext.toast('Updated User Profile', '', 'tr');

										//save submitter (if it exist than update)										
										submissionPanel.setLoading('Updating Submitter...');

										//check for submitter
										var contactStore = submissionPanel.detailsPanel.getComponent('detailSections').getComponent('contactGrid').getStore();
										var submitterRecord = contactStore.findRecord('contactType', 'SUB');
										var contactData = {
											contactType: 'SUB',
											firstName: userProfile.firstName,
											lastName: userProfile.lastName,
											email: userProfile.email,
											phone: userProfile.phone,
											organization: userProfile.organization												
										};
										var submitterData = null;
										if (submitterRecord) {
											submitterData = submitterRecord.getData();
											contactData = Ext.apply(submitterData, contactData);
										} 

										var contactMethod = 'POST';
										var update = '';
										if (submitterData) {
											update = '/' + submitterData.componentContactId;
											contactMethod = 'PUT';
										}

										if (contactData.type){
											delete contactData.type;
										}

										Ext.Ajax.request({
											url: 'api/v1/resource/components/' + submissionPanel.componentId + '/contacts' + update,
											method:  contactMethod,
											jsonData: contactData,
											callback: function(){
												submissionPanel.setLoading(false);
											},
											success: function(response, opts) {
												contactStore.load({
													url: 'api/v1/resource/components/' + submissionPanel.componentId + '/contacts/view',
													callback: function() {
														submissionPanel.saveReady = true;
														if (successCallback) {
															successCallback(response, opts);
														}	
													}
												});

											}												
										});																						

									},
									failure: function() {
										submissionPanel.saveReady = true;
									}
								});								
							},
							failure: function(response, opt){
								submissionPanel.saveReady = true;
								submissionPanel.currentStep = 2;
								submissionPanel.changeSteps(2);
							}
							});
						};

						var saveUserCodes = false;
						var userCodesToSave = [];
						submissionPanel.requiredAttributeStore.each(function(record) {
							if (record.get('allowUserGeneratedCodes')) {
								saveUserCodes = true;

								var currentCode = record.formField.getSelection();
								var codeLabel;
								if (currentCode) {
									codeLabel = currentCode.get('label');
								} else {
									codeLabel = record.formField.getValue();
								}
								userCodesToSave.push({
									attributeCodeLabel: codeLabel,
									attributeType: record.get('attributeType')
								});														
							}
						});

						if (saveUserCodes) {										
							var newAttributes = {
								userAttributes: userCodesToSave
							};	
							form.setLoading('Saving New Attributes...');
							Ext.Ajax.request({
								url: 'api/v1/resource/attributes/attributetypes/usercodes',
								method: 'POST',
								jsonData: newAttributes,
								callback: function() {
									form.setLoading(false);
								},
								success: function(response, opts) {	

									//update the codes
									var savedAttributes = Ext.decode(response.responseText);
									Ext.Array.each(requireComponent.attributes, function(attribute){
										Ext.Array.each(savedAttributes, function(newAttributes) {
											if (attribute.componentAttributePk.attributeType === newAttributes.attributeCodePk.attributeType &&
												attribute.componentAttributePk.attributeCode === newAttributes.label) {
												attribute.componentAttributePk.attributeCode = newAttributes.attributeCodePk.attributeCode;									
											}
										});									
									});

									handleMainFormSave();
									loadAllAttributes();
								},
								failure: function(response, opts) {
									submissionPanel.saveReady = true;
									Ext.MessageBox.show({
										title:'Failed to Save',
										message: 'Failed adding the new attribute code. Try again or use an existing attribute code.',
										buttons: Ext.Msg.OK,
										icon: Ext.Msg.ERROR										
									});
								}
							});						
						} else {
							handleMainFormSave();
						}
					}
				}				

			});
		};		
		
		submissionPanel.add(submissionPanel.mainPanel);
		submissionPanel.add(submissionPanel.navigation);
				
	},
	
	editSubmission: function(componentId) {
		var submissionPanel = this;		
		submissionPanel.resetSubmission(true);
		
		//load record
		submissionPanel.componentId = componentId;		
		
		submissionPanel.setLoading('Loading...');
		Ext.Ajax.request({
			url: 'api/v1/resource/components/' + submissionPanel.componentId,
			callback: function(){
				submissionPanel.setLoading(false);
			},
			success: function(response, opts){
				var data = Ext.decode(response.responseText);
				
				//load required form				
				var componentModel = Ext.create('Ext.data.Model',{});
				componentModel.set(data);
				submissionPanel.requiredForm.loadRecord(componentModel);				
								
				//set notify checkbox
				if (!data.notifyOfApprovalEmail) {
					submissionPanel.reviewPanel.getComponent('approvalNotification').setValue(false);
				} else {
					submissionPanel.reviewPanel.getComponent('approvalNotification').setValue(true);
				}
				
			}
		});	
		
		//load details
		var detailSection = submissionPanel.detailsPanel.getComponent('detailSections');
		detailSection.getComponent('tagGrid').getStore().load({
			url: 'api/v1/resource/components/' + submissionPanel.componentId + '/tagsview'
		});
		detailSection.getComponent('relationshipsGrid').getStore().load({
			url: 'api/v1/resource/components/' + submissionPanel.componentId + '/relationships'
		});
		detailSection.getComponent('dependenciesGrid').getStore().load({
			url: 'api/v1/resource/components/' + submissionPanel.componentId + '/dependencies/view'
		});		
		detailSection.getComponent('mediaGrid').getStore().load({
			url: 'api/v1/resource/components/' + submissionPanel.componentId + '/media/view'
		});		
		detailSection.getComponent('resourceGrid').getStore().load({
			url: 'api/v1/resource/components/' + submissionPanel.componentId + '/resources/view'
		});		
		detailSection.getComponent('contactGrid').getStore().load({
			url: 'api/v1/resource/components/' + submissionPanel.componentId + '/contacts/view'
		});		

		submissionPanel.loadComponentAttributes();	

		// Since we're loading a new entry, scroll to top of each panel.
		submissionPanel.reviewPanel.body.scrollTo('Top', 0, true);
		submissionPanel.detailsPanel.body.scrollTo('Top', 0, true);
		submissionPanel.requiredForm.body.scrollTo('Top', 0, true);
		
	},	
	
	resetSubmission: function(editMode) {
		var submissionPanel = this;
		submissionPanel.componentId = null;
		submissionPanel.editMode = editMode;
		
		CoreService.userservice.getCurrentUser().then(function (usercontext) {			
			submissionPanel.submitterForm.getForm().setValues(usercontext);
			submissionPanel.usercontext = usercontext;
			
			CoreService.systemservice.getSecurityPolicy().then(function(policy){
				if (policy.disableUserInfoEdit) {
					
					submissionPanel.submitterForm.queryById('firstName').setDisabled(true);
					submissionPanel.submitterForm.queryById('lastName').setDisabled(true);
					submissionPanel.submitterForm.queryById('email').setDisabled(true);				
					submissionPanel.submitterForm.queryById('phone').setDisabled(true);
					submissionPanel.submitterForm.queryById('organization').setDisabled(true);

					if (policy.externalUserManagementText) {
						submissionPanel.submitterForm.queryById('externalManagementMessage').update('<br><br><i class="fa fa-2x fa-warning text-warning"></i> ' + policy.externalUserManagementText);
					}
				}
			});				
			
		});
		
		submissionPanel.currentStep = 1;
		submissionPanel.changeSteps();
				
		submissionPanel.requiredForm.reset();
		
		//clear required attributes		
		submissionPanel.loadComponentAttributes();
		submissionPanel.requiredAttributeStore.removeAll();		
		
		//clear details
		var detailSection = submissionPanel.detailsPanel.getComponent('detailSections');
		detailSection.getComponent('tagGrid').getStore().removeAll();
		detailSection.getComponent('relationshipsGrid').getStore().removeAll();		
		detailSection.getComponent('dependenciesGrid').getStore().removeAll();
		detailSection.getComponent('mediaGrid').getStore().removeAll();
		detailSection.getComponent('resourceGrid').getStore().removeAll();
		detailSection.getComponent('contactGrid').getStore().removeAll();
		detailSection.getComponent('optionalAttributes').getStore().removeAll();
				
		//set notification to checked
		submissionPanel.reviewPanel.getComponent('approvalNotification').setValue(true);

		// Scroll forms to top
		submissionPanel.reviewPanel.body.scrollTo('Top', 0, true);
		submissionPanel.detailsPanel.body.scrollTo('Top', 0, true);
		submissionPanel.requiredForm.body.scrollTo('Top', 0, true);
				
	}	
	
	
});
