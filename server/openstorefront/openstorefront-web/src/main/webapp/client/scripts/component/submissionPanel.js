/* 
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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

Ext.define('OSF.component.SubmissionPanel', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.SubmissionPanel',
	layout: 'border',
	formWarningMessage: '',

	submitForReviewUrl: function (componentId){
		return '../api/v1/resource/componentsubmissions/' + componentId+ '/submit';
	},
	
	initComponent: function () {
		this.callParent();
	
		var submissionPanel = this;
		submissionPanel.hideSecurityMarkings = true;		
				
		submissionPanel.cancelSubmissionHandler = function() {
			Ext.Msg.show({
				title:'Confirm Cancel?',
				message: 'Are you sure you want to cancel your submission? <br><br><b>Yes</b>, will remove submission<br> <b>No</b>, will cancel the form and will NOT remove existing submission',
				buttons: Ext.Msg.YESNOCANCEL,
				icon: Ext.Msg.QUESTION,				
				fn: function(btn) {
					if (btn === 'yes') {
						//remove submmision
						if (submissionPanel.componentId) {
							submissionPanel.setLoading('Canceling Submission...');
							Ext.Ajax.request({
								url: '../api/v1/resource/componentsubmissions/' + submissionPanel.componentId + '/inactivate',
								method: 'PUT',
								callback: function () {
									submissionPanel.setLoading(false);
								},
								success: function (response, opts) {
									if (submissionPanel.cancelSubmissionHandlerYes) {
										submissionPanel.cancelSubmissionHandlerYes();
									}
								}
							});							
						} else {
							if (submissionPanel.cancelSubmissionHandlerYes) {
								submissionPanel.cancelSubmissionHandlerYes();
							}						
						}
					} else if (btn === 'no') {
						submissionPanel.resetSubmission();				
						if (submissionPanel.cancelSubmissionHandlerNo) {
							submissionPanel.cancelSubmissionHandlerNo();
						}												
					} else {
						if (submissionPanel.cancelSubmissionHandlerCancel) {
							submissionPanel.cancelSubmissionHandlerCancel();
						}						
					} 
					
				}
			});			
		};		
		
		submissionPanel.navigation = Ext.create('Ext.panel.Panel', {
			region: 'west',
			minWidth: 225,
			maxWidth: 225,
			title: 'Steps',
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
					html: '<h2>Verify/Update your information and hit Next</h2>'
				},
				{
					xtype: 'textfield',
					name: 'firstName',
					fieldLabel: 'First Name <span class="field-required" />',
					labelSeparator: '',
					width: '100%',
					maxLength: 80,
					allowBlank: false
				},
				{
					xtype: 'textfield',
					name: 'lastName',
					fieldLabel: 'Last Name <span class="field-required" />',
					labelSeparator: '',
					width: '100%',
					maxLength: 80,
					allowBlank: false
				},
				{
					xtype: 'textfield',
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
					name: 'phone',
					fieldLabel: 'Phone <span class="field-required" />',
					labelSeparator: '',
					width: '100%',
					allowBlank: false,
					maxLength: 80
				},
				Ext.create('OSF.component.StandardComboBox', {
					name: 'organization',
					allowBlank: false,
					margin: '0 0 5 0',
					width: '100%',
					maxLength: 120,
					fieldLabel: 'Organization <span class="field-required" />',
					forceSelection: false,					
					valueField: 'description',
					storeConfig: {
						url: '/openstorefront/api/v1/resource/organizations/lookup'
					}
				}),				
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
		var loadAllAttributes = function(){
			Ext.Ajax.request({
				url: '../api/v1/resource/attributes',
				success: function(response, opts){
					allAttributes = Ext.decode(response.responseText);
				}
			});
		};
		loadAllAttributes();		
		
		submissionPanel.loadComponentAttributes = function() {
			if (submissionPanel.componentId) {
				var componentId = submissionPanel.componentId;
				Ext.Ajax.request({
					url: '../api/v1/resource/components/' + componentId + '/attributes/view',
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

			var requiredAttributes = [];
			var optionalAttributes = [];
			Ext.Array.each(allAttributes, function(attribute){
				if (!attribute.hideOnSubmission) {
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
						} else {
							requiredAttributes.push(attribute);
						}
					} else {
						optionalAttributes.push(attribute);
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
					var panel = submissionPanel.requiredForm.getComponent('requiredAttributePanel');					
					panel.removeAll();
					
					store.each(function(record) {
						
						var field = Ext.create('Ext.form.field.ComboBox', {
							record: record,
							fieldLabel: record.get('description') + ' <span class="field-required" />',
							forceSelection: true,
							queryMode: 'local',
							editable: false,
							typeAhead: false,
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
						url: '../api/v1/resource/componenttypes/submission'																				
					},
					listeners: {
						change: function(field, newValue, oldValue, opts) {
							if (newValue) {
								handleAttributes(newValue);

								var sectionPanel = submissionPanel.detailsPanel.getComponent('detailSections');

								sectionPanel.getComponent('optionalAttributes').setHidden(true);
								sectionPanel.getComponent('contactGrid').setHidden(true);
								sectionPanel.getComponent('resourceGrid').setHidden(true);							
								sectionPanel.getComponent('mediaGrid').setHidden(true);							
								sectionPanel.getComponent('dependenciesGrid').setHidden(true);
								sectionPanel.getComponent('metadataGrid').setHidden(true);
								sectionPanel.getComponent('relationshipsGrid').setHidden(true);
								sectionPanel.getComponent('tagGrid').setHidden(true);							

								var record = field.getSelection();
								if (record.get('dataEntryAttributes')){
									sectionPanel.getComponent('optionalAttributes').setHidden(false);
								} 
								if (record.get('dataEntryAttributes')){
									sectionPanel.getComponent('contactGrid').setHidden(false);
								} 
								if (record.get('dataEntryRelationships')){
									sectionPanel.getComponent('resourceGrid').setHidden(false);
								} 
								if (record.get('dataEntryContacts')){
									sectionPanel.getComponent('mediaGrid').setHidden(false);
								} 
								if (record.get('dataEntryResources')){							
									sectionPanel.getComponent('dependenciesGrid').setHidden(false);
								} 
								if (record.get('dataEntryMedia')){
									sectionPanel.getComponent('metadataGrid').setHidden(false);
								} 
								if (record.get('dataEntryDependancies')){
									sectionPanel.getComponent('relationshipsGrid').setHidden(false);
								} 
								if (record.get('dataEntryMetadata')){
									sectionPanel.getComponent('tagGrid').setHidden(false);
								}
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
						url: '../api/v1/resource/organizations/lookup'
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
					tinyMCEConfig: CoreUtil.tinymceConfig()
				},
				Ext.create('OSF.component.SecurityComboBox', {	
					itemId: 'securityMarkings',
					hidden: true
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
				url: '../api/v1/resource/components/' + componentId + '/' + opts.entity + '/' + recordId + subEntity + subEntityId, 
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
				title: 'Add Attribute',
				width: '50%',
				height: 150,
				layout: 'fit',
				items: [
					{
						xtype: 'form',
						itemId: 'attributeForm',
						bodyStyle: 'padding: 10px;',
						defaults: {
							labelAlign: 'right',
							labelSeparator: '',
							width: '100%'
						},
						items: [
							{
								xtype: 'combobox',
								itemId: 'attributeTypeCB',
								fieldLabel: 'Attribute Type <span class="field-required" />',
								name: 'type',
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
									],
									data: submissionPanel.optionalAttributes
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
								name: 'code',
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
						],
						dockedItems: [
							{
								xtype: 'toolbar',
								dock: 'bottom',
								items: [
									{
										text: 'Save',
										formBind: true,
										iconCls: 'fa fa-save',
										handler: function () {
											var attributeWindow = this.up('window');
											var form = this.up('form');
											var data = form.getValues();
											var componentId = submissionPanel.componentId;

											var newAttribute = {
												componentAttributePk: {
													attributeType: data.type,
													attributeCode: data.code
												}
											};											

											var method = 'POST';
											var update = '';

											CoreUtil.submitForm({
												url: '../api/v1/resource/components/' + componentId + '/attributes' + update,
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
																url: '../api/v1/resource/components/' + componentId + '/attributes/' + record.get('type') + '/' + record.get('code') + '/force',
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
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Cancel',
										iconCls: 'fa fa-close',
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
		}
		
		var addEditContact = function(record, grid) {
			
			var addWindow = Ext.create('Ext.window.Window', {
				closeAction: 'destory',
				modal: true,
				title: 'Add Contact',
				alwaysOnTop: true,
				width: '50%',
				height: 300,
				layout: 'fit',
				items: [
					{
						xtype: 'form',
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
								name: 'contactId'
							},
							Ext.create('OSF.component.StandardComboBox', {
								name: 'contactType',									
								allowBlank: false,								
								margin: '0 0 5 0',
								editable: false,
								typeAhead: false,
								width: '100%',
								fieldLabel: 'Contact Type <span class="field-required" />',
								storeConfig: {
									url: '../api/v1/resource/lookuptypes/ContactType',
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
									url: '../api/v1/resource/organizations/lookup'
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
								listConfig: {
									itemTpl: [
										 '{firstName} <span style="color: grey">({email})</span>'
									]
								},								
								storeConfig: {
									url: '../api/v1/resource/contacts/filtered'
								},
								listeners: {
									select: function(combo, record, opts) {
										record.set('contactId', null);
										combo.up('form').reset();
										combo.up('form').loadRecord(record);
									}
								}
							}),							
							{
								xtype: 'textfield',
								fieldLabel: 'Last Name <span class="field-required" />',									
								allowBlank: false,								
								maxLength: '80',
								name: 'lastName'
							},
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
										iconCls: 'fa fa-save',
										handler: function(){
											var form = this.up('form');
											var formWindow = this.up('window');
											var data = form.getValues();
											var componentId = submissionPanel.componentId;

											var method = 'POST';
											var update = '';
											if (data.contactId) {
												update = '/' + data.contactId;
												method = 'PUT';
											}

											CoreUtil.submitForm({
												url: '../api/v1/resource/components/' + componentId + '/contacts' + update,
												method: method,
												data: data,
												form: form,
												success: function(){
													grid.getStore().load({
														url: '../api/v1/resource/components/' + submissionPanel.componentId + '/contacts/view'
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
										iconCls: 'fa fa-close',
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
				title: 'Add External Link',
				width: '50%',
				height: 360,
				layout: 'fit',
				items: [
					{
						xtype: 'form',
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
									url: '../api/v1/resource/lookuptypes/ResourceType'
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
								name: 'file'
							},
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
										text: 'Save',
										formBind: true,
										iconCls: 'fa fa-save',
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
														url: '../api/v1/resource/components/' + componentId + '/resources' + update,
														method: method,
														removeBlankDataItems: true,
														data: data,
														form: form,
														success: function(){
															grid.getStore().load({
																url: '../api/v1/resource/components/' + submissionPanel.componentId + '/resources/view'
															});
															resourceWindow.close();
														}
													});
												} else {
													//upload
													form.submit({
														url: '../Resource.action?UploadResource',
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
														success: function(form, action, opt){
															grid.getStore().load({
																url: '../api/v1/resource/components/' + submissionPanel.componentId + '/resources/view'
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
										iconCls: 'fa fa-close',
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
				closeAction: 'destory',
				modal: true,
				alwaysOnTop: true,
				title: 'Add Media',
				width: '50%',
				height: 325,
				layout: 'fit',
				items: [
					{
						xtype: 'form',
						itemId: 'mediaForm',
						bodyStyle: 'padding: 10px;',
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
									url: '../api/v1/resource/lookuptypes/MediaType'
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
								xtype: 'filefield',
								itemId: 'upload',
								fieldLabel: 'Upload Media (Limit of 1GB)',																											
								name: 'file'
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
										iconCls: 'fa fa-save',
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
														url: '../api/v1/resource/components/' + componentId + '/media' + update,
														method: method,
														removeBlankDataItems: true,
														data: data,
														form: form,
														success: function(){
															grid.getStore().load({
																url: '../api/v1/resource/components/' + submissionPanel.componentId + '/media/view'
															});
															mediaWindow.close();
														}
													});
												} else {
													//upload
													form.submit({
														url: '../Media.action?UploadMedia',
														params: {
															'componentMedia.mediaTypeCode' : data.mediaTypeCode,
															'componentMedia.caption': data.caption,
															'componentMedia.link': data.link,
															'componentMedia.componentMediaId': data.componentMediaId,
															'componentMedia.componentId': componentId
														},
														method: 'POST',
														submitEmptyText: false,
														success: function(form, action, opt){
															grid.getStore().load({
																url: '../api/v1/resource/components/' + submissionPanel.componentId + '/media/view'
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
										iconCls: 'fa fa-close',
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
				title: 'Add Dependency',
				width: '50%',
				height: 370,
				layout: 'fit',				
				items: [
					{
						xtype: 'form',
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
										iconCls: 'fa fa-save',
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
												url: '../api/v1/resource/components/' + componentId + '/dependencies' + update,
												method: method,
												data: data,
												form: form,
												success: function(){
													grid.getStore().load({
														url: '../api/v1/resource/components/' + submissionPanel.componentId + '/dependencies/view'
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
										iconCls: 'fa fa-close',
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
		
		var addEditMetadata = function(record, grid){		
			var addWindow = Ext.create('Ext.window.Window', {
				closeAction: 'destory',
				modal: true,
				alwaysOnTop: true,
				title: 'Add Metadata',
				width: '50%',
				height: 250,
				layout: 'fit',
				items: [
					{
						xtype: 'form',
						itemId: 'metaForm',
						bodyStyle: 'padding: 10px;',
						defaults: {
							labelAlign: 'top',
							labelSeparator: '',
							width: '100%'
						},
						items: [
							{
								xtype: 'hidden',
								name: 'metadataId'
							},
							{
								xtype: 'textfield',
								fieldLabel: 'Label <span class="field-required" />',									
								allowBlank: false,									
								maxLength: '255',									
								name: 'label'
							},
							{
								xtype: 'textfield',
								fieldLabel: 'Value <span class="field-required" />',									
								allowBlank: false,									
								maxLength: '255',									
								name: 'value'
							},
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
										text: 'Save',
										formBind: true,
										iconCls: 'fa fa-save',
										handler: function(){
											var metaWindow = this.up('window');
											var form = this.up('form');
											var data = form.getValues();
											var componentId = submissionPanel.componentId;

											var method = 'POST';
											var update = '';
											if (data.metadataId) {
												update = '/' + data.metadataId;
												method = 'PUT';
											}

											CoreUtil.submitForm({
												url: '../api/v1/resource/components/' + componentId + '/metadata' + update,
												method: method,
												data: data,
												form: form,
												success: function(){
													grid.getStore().load({
														url: '../api/v1/resource/components/' + submissionPanel.componentId + '/metadata/view'
													});
													metaWindow.close();
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
				addWindow.getComponent('metaForm').loadRecord(record);
			}			
		};
		
		var addEditRelationship = function(record, grid) {

			var addWindow = Ext.create('Ext.window.Window', {
				closeAction: 'destory',
				modal: true,
				title: 'Add Relationship',
				alwaysOnTop: true,
				width: '50%',
				height: 250,
				layout: 'fit',
				items: [
					{
						xtype: 'form',
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
									url: '../api/v1/resource/lookuptypes/RelationshipType'
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
									url: '../api/v1/resource/componenttypes/lookup',
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
											url: '../api/v1/resource/components/lookup?status=A&approvalState=ALL' + componentType,		
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
									url: '../api/v1/resource/components/lookup?status=A&approvalState=ALL',
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
										iconCls: 'fa fa-save',
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
												url: '../api/v1/resource/components/' + componentId + '/relationships' + update,
												method: method,
												data: data,
												form: form,
												success: function(){
													grid.getStore().load({
														url: '../api/v1/resource/components/' + submissionPanel.componentId + '/relationships'
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
										iconCls: 'fa fa-close',
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
							html: '<h3>Add all attributes that are appropriate.</h3>'
						},
						{
							xtype: 'grid',
							itemId: 'optionalAttributes',
							title: 'Addtional Attributes <i class="fa fa-question-circle"  data-qtip="Attributes are filterable metadata about the entry."></i>',
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
											iconCls: 'fa fa-plus',
											handler: function(){
												addEditAttribute();
											}
										},
										{
											text: 'Edit',
											itemId: 'editBtn',
											iconCls: 'fa fa-edit',
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
											text: 'Remove',	
											itemId: 'removeBtn',
											iconCls: 'fa fa-trash',
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
											iconCls: 'fa fa-plus',
											handler: function(){
												var grid = this.up('grid');
												addEditContact(null, grid);
											}
										},
										{
											text: 'Edit',
											itemId: 'editBtn',
											iconCls: 'fa fa-edit',
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
											text: 'Remove',
											itemId: 'removeBtn',											
											disabled: true,
											iconCls: 'fa fa-trash',
											handler: function(){
												actionSubComponentRemove({
													grid: this.up('grid'),
													idField: 'contactId',
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
							html: '<h3>Add direct links external to Documentation, Binaries, Source Code, etc... where appropriate.</h3>'
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
											iconCls: 'fa fa-plus',
											handler: function(){
												var grid = this.up('grid');
												addEditResource(null, grid);
											}
										},
										{
											text: 'Edit',
											itemId: 'editBtn',
											iconCls: 'fa fa-edit',
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
											text: 'Remove',
											itemId: 'removeBtn',
											disabled: true,
											iconCls: 'fa fa-trash',
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
											iconCls: 'fa fa-plus',
											handler: function(){
												var grid = this.up('grid');
												addEditMedia(null, grid);
											}
										},
										{
											text: 'Edit',
											itemId: 'editBtn',
											disabled: true,
											iconCls: 'fa fa-plus',
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
											text: 'Remove',	
											itemId: 'removeBtn',
											disabled: true,
											iconCls: 'fa fa-trash',
											handler: function(){
												actionSubComponentRemove({
													grid: this.up('grid'),
													idField: 'componentMediaId',
													entity: 'media'
												});
											}
										}								
									]
								}
							]
						},
						{
							xtype: 'panel',
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
											iconCls: 'fa fa-plus',
											handler: function(){
												var grid = this.up('grid');
												addEditDependency(null, grid);
											}
										},
										{
											text: 'Edit',
											itemId: 'editBtn',
											iconCls: 'fa fa-edit',
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
											text: 'Remove',	
											itemId: 'removeBtn',
											disabled: true,
											iconCls: 'fa fa-trash',
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
							html: '<h3>Add relavant feature support or other metadata to describe the entry. Eg. Label: DIB Compatible  Value: 4.1+</h3>'
						},						
						{
							xtype: 'grid',
							itemId: 'metadataGrid',
							title: 'Metadata  <i class="fa fa-question-circle"  data-qtip="Add non-filterable items of information. (Eg. Label: CMAPI Compatible   Value: 1.3+)"></i>',
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
								{ text: 'Label', dataIndex: 'label',  width: 200 },
								{ text: 'Value',  dataIndex: 'value', flex: 1, minWidth: 200 },
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
											iconCls: 'fa fa-plus',
											handler: function(){
												var grid = this.up('grid');
												addEditMetadata(null, grid);
											}
										},
										{
											text: 'Edit',
											itemId: 'editBtn',
											iconCls: 'fa fa-edit',
											disabled: true,
											handler: function(){
												var grid = this.up('grid');
												var record = this.up('grid').getSelectionModel().getSelection()[0];
												addEditMetadata(record, grid);
											}
										},										
										{
											xtype: 'tbfill'
										},
										{
											text: 'Remove',
											itemId: 'removeBtn',
											disabled: true,
											iconCls: 'fa fa-trash',
											handler: function(){
												actionSubComponentRemove({
													grid: this.up('grid'),
													idField: 'metadataId',
													entity: 'metadata'
												});
											}
										}								
									]
								}
							]
						},
						{
							xtype: 'panel',
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
											iconCls: 'fa fa-plus',
											handler: function(){
												var grid = this.up('grid');												
												addEditRelationship(null, grid);
												
											}
										},
										{
											text: 'Edit',
											itemId: 'editBtn',
											iconCls: 'fa fa-edit',
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
											text: 'Remove',
											itemId: 'removeBtn',
											disabled: true,
											iconCls: 'fa fa-trash',
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
											iconCls: 'fa fa-plus',
											handler: function(){
												var grid = this.up('grid');
												
												var addTag = function(addWindow){
													var form = addWindow.down('form');
													var data = form.getValues();
													var componentId = submissionPanel.componentId;
													
													CoreUtil.submitForm({
														url: '../api/v1/resource/components/' + componentId + '/tags',
														method: 'POST',
														data: data,
														form: form,
														success: function(){
															grid.getStore().load({
																url: '../api/v1/resource/components/' + submissionPanel.componentId + '/tags'
															});
															addWindow.close();
														}
													});												
												};
												
												var addWindow = Ext.create('Ext.window.Window', {
													closeAction: 'destory',
													modal: true,
													alwaysOnTop: true,
													title: 'Add Tag',
													width: '40%',
													height: 200,
													layout: 'fit',
													items: [
														{
															xtype: 'form',
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
																		url: '../api/v1/resource/components/tags'
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
																			iconCls: 'fa fa-plus',
																			handler: function(){																				
																				addTag(this.up('window'));
																			}
																		},
																		{
																			xtype: 'tbfill'
																		},
																		{
																			text: 'Cancel',										
																			iconCls: 'fa fa-close',
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
											text: 'Remove',
											itemId: 'removeBtn',											
											disabled: true,
											iconCls: 'fa fa-trash',
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
			url: 'Router.action?page=shared/entrySimpleViewTemplate.jsp',
			success: function(response, opts){
				reviewViewTemplate.set(response.responseText, true);
			}
		});		
		
		submissionPanel.reviewPanel = Ext.create('Ext.panel.Panel', {
			layout: 'vbox',
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
							iconCls: 'fa fa-2x fa-arrow-left icon-top-padding-2',
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
							iconCls: 'fa fa-2x fa-save icon-top-padding-2',
							handler: function () {
																
																
							}
						},						
						{
							text: 'Submit For Review',
							itemId: 'Submit',
							hidden: true,
							scale: 'large',
							iconCls: 'fa fa-2x fa-check icon-top-padding-2',														
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
											url: '../api/v1/resource/componentsubmissions/' + submissionPanel.componentId + '/setNotifyMe',
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
							text: 'Cancel',
							scale: 'large',
							iconCls: 'fa fa-2x fa-close icon-top-padding-2',							
							handler: function () {	
								submissionPanel.cancelSubmissionHandler();								
							}
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Next',
							itemId: 'Next',
							scale: 'large',
							iconCls: 'fa fa-2x fa-arrow-right icon-top-padding-2',
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
		CoreService.brandingservice.getCurrentBanding().then(function(response, opts){
			var branding = Ext.decode(response.responseText);
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
					sections.getComponent('metadataGrid').down('#securityMarking').setHidden(false);
					sections.getComponent('tagGrid').down('#securityMarking').setHidden(false);						
				});
			}
		});
		
		
		submissionPanel.currentStep = 1;
		submissionPanel.changeSteps = function(forceProceed) {						
			var tools = submissionPanel.mainPanel.getComponent('tools');
					
			//confirm pervious steps validation and saving
			var proceed = false;
			if (forceProceed) {				
				proceed = true;
			} else {
				if (submissionPanel.currentStep === 2) {
				//vaildation contact info
				if (submissionPanel.submitterForm.isValid()) {
					proceed = true;
				} else {
					Ext.Msg.show({
						title: 'Validation',
						message: 'All required fields must be filled in with valid values.',
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.ERROR,
						fn: function(btn) {
						}
					});
					submissionPanel.currentStep=1;
				}
				
			} else if (submissionPanel.currentStep === 3) {
				
				if (!submissionPanel.requiredForm.isValid()) {
					Ext.Msg.show({
						title: 'Validation',
						message: 'All required fields must be filled in with valid values.',
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.ERROR,
						fn: function(btn) {
						}
					});	
					var form = submissionPanel.requiredForm;
					var data = form.getValues();
					
					if (!data.description) {
						form.getForm().markInvalid({
								description: 'Required'
						});
					}
					
					submissionPanel.currentStep=2;
				} else {
					
					submissionPanel.currentStep=2;					
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
					} else {
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
							CoreUtil.removeBlankDataItem(requireComponent.component);												
							CoreUtil.submitForm({
								url: '../api/v1/resource/components' + update,
								method: method,
								data: requireComponent,
								loadingText: 'Saving Entry...',
								removeBlankDataItems: true,
								form: form,
								success: function(response, opt){
									Ext.toast('Successfully Saved Record');

									var data = Ext.decode(response.responseText);
									submissionPanel.componentId = data.component.componentId;

									//save profile updates
									submissionPanel.setLoading('Updating Profile...');								
									var userProfile = Ext.apply(submissionPanel.usercontext, submissionPanel.submitterForm.getValues());
									userProfile.externalGuid = userProfile.guid;

									Ext.Ajax.request({
										url: '/openstorefront/api/v1/resource/userprofiles/' + userProfile.username,
										method: 'PUT',
										jsonData: userProfile,
										callback: function() {
											submissionPanel.setLoading(false);
										},
										success: function (response, opts) {
											Ext.toast('Updated User Profile');

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
												update = '/' + submitterData.contactId;
												contactMethod = 'PUT';
											}
											
											if (contactData.type){
												delete contactData.type;
											}
											
											Ext.Ajax.request({
												url: '../api/v1/resource/components/' + submissionPanel.componentId + '/contacts' + update,
												method:  contactMethod,
												jsonData: contactData,
												callback: function(){
													submissionPanel.setLoading(false);
												},
												success: function(response, opts) {
													submissionPanel.currentStep=3;	
													submissionPanel.changeSteps(true);
													contactStore.load({
														url: '../api/v1/resource/components/' + submissionPanel.componentId + '/contacts/view'		
													});
												}												
											});																						

										}
									});								
								},
								failure: function(response, opt){
								}
							});
						}
					}					
				}				
			} else {
					proceed = true;
				}
			}
				
			if (proceed) {
				tools.getComponent('Submit').setHidden(true);

				if (submissionPanel.editMode) {
					submissionPanel.navigation.getComponent('step1Btn').setDisabled(false);
					submissionPanel.navigation.getComponent('step2Btn').setDisabled(false);			
					submissionPanel.navigation.getComponent('step3Btn').setDisabled(false);
					submissionPanel.navigation.getComponent('step4Btn').setDisabled(false);					
				} else {
					submissionPanel.navigation.getComponent('step1Btn').setDisabled(false);
					submissionPanel.navigation.getComponent('step2Btn').setDisabled(true);			
					submissionPanel.navigation.getComponent('step3Btn').setDisabled(true);
					submissionPanel.navigation.getComponent('step4Btn').setDisabled(true);
				}

				submissionPanel.navigation.getComponent('step1Btn').setIconCls('');
				submissionPanel.navigation.getComponent('step2Btn').setIconCls('');
				submissionPanel.navigation.getComponent('step3Btn').setIconCls('');
				submissionPanel.navigation.getComponent('step4Btn').setIconCls('');

				//if  already save or editing show "Save and Exit"

				tools.getComponent('SaveAndExit').setHidden(true);

				if (submissionPanel.currentStep === 1) {			
					tools.getComponent('Previous').setDisabled(true);
					tools.getComponent('Next').setDisabled(false);				

					submissionPanel.mainPanel.getLayout().setActiveItem(submissionPanel.submitterForm);				

				} else if (submissionPanel.currentStep === 2) {
					tools.getComponent('Previous').setDisabled(false);
					tools.getComponent('Next').setDisabled(false);	
					submissionPanel.navigation.getComponent('step1Btn').setIconCls('fa fa-check');
					submissionPanel.navigation.getComponent('step2Btn').setDisabled(false);


					submissionPanel.mainPanel.getLayout().setActiveItem(submissionPanel.requiredForm);
					Ext.defer(function(){
						submissionPanel.mainPanel.updateLayout(true, true);
					}, 200)					
					
				} else if (submissionPanel.currentStep === 3) {
					tools.getComponent('Previous').setDisabled(false);
					tools.getComponent('Next').setDisabled(false);

					submissionPanel.navigation.getComponent('step1Btn').setIconCls('fa fa-check');
					submissionPanel.navigation.getComponent('step2Btn').setDisabled(false);
					submissionPanel.navigation.getComponent('step2Btn').setIconCls('fa fa-check');
					submissionPanel.navigation.getComponent('step3Btn').setDisabled(false);


					submissionPanel.mainPanel.getLayout().setActiveItem(submissionPanel.detailsPanel);
					submissionPanel.detailsPanel.updateLayout(true, true);

				} else if (submissionPanel.currentStep === 4) {
					tools.getComponent('Previous').setDisabled(false);
					tools.getComponent('Next').setDisabled(true);

					tools.getComponent('SaveAndExit').setHidden(true);
					tools.getComponent('Submit').setHidden(false);
					
					var reviewEntryPanel = submissionPanel.reviewPanel.getComponent('reviewEntryPanel');
					reviewEntryPanel.setLoading('Loading preview...');
					Ext.Ajax.request({
						url: '../api/v1/resource/components/' + submissionPanel.componentId + '/detail',
						callback: function(){
							reviewEntryPanel.setLoading(false);
						},
						success: function(response, opts){
							var data = Ext.decode(response.responseText);
							
							//remove attribute that should be hidden
							var attributesToShow = [];
							Ext.Array.each(data.attributes, function(item){
								if (!item.hideOnSubmission) {
									attributesToShow.push(item);
								}
							});
							data.attributes = attributesToShow;							
							reviewEntryPanel.update(data);
						}
					});
					

					submissionPanel.navigation.getComponent('step1Btn').setIconCls('fa fa-check');
					submissionPanel.navigation.getComponent('step2Btn').setDisabled(false);
					submissionPanel.navigation.getComponent('step2Btn').setIconCls('fa fa-check');
					submissionPanel.navigation.getComponent('step3Btn').setDisabled(false);
					submissionPanel.navigation.getComponent('step3Btn').setIconCls('fa fa-check');				
					submissionPanel.navigation.getComponent('step4Btn').setDisabled(false);

					submissionPanel.mainPanel.getLayout().setActiveItem(submissionPanel.reviewPanel);
				}				
			}
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
			url: '../api/v1/resource/components/' + submissionPanel.componentId,
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
			url: '../api/v1/resource/components/' + submissionPanel.componentId + '/tagsview'
		});
		detailSection.getComponent('relationshipsGrid').getStore().load({
			url: '../api/v1/resource/components/' + submissionPanel.componentId + '/relationships'
		});
		detailSection.getComponent('metadataGrid').getStore().load({
			url: '../api/v1/resource/components/' + submissionPanel.componentId + '/metadata/view'
		});
		detailSection.getComponent('dependenciesGrid').getStore().load({
			url: '../api/v1/resource/components/' + submissionPanel.componentId + '/dependencies/view'
		});		
		detailSection.getComponent('mediaGrid').getStore().load({
			url: '../api/v1/resource/components/' + submissionPanel.componentId + '/media/view'
		});		
		detailSection.getComponent('resourceGrid').getStore().load({
			url: '../api/v1/resource/components/' + submissionPanel.componentId + '/resources/view'
		});		
		detailSection.getComponent('contactGrid').getStore().load({
			url: '../api/v1/resource/components/' + submissionPanel.componentId + '/contacts/view'
		});		
		submissionPanel.loadComponentAttributes();	
		
	},	
	
	resetSubmission: function(editMode) {
		var submissionPanel = this;
		submissionPanel.componentId = null;
		submissionPanel.editMode = editMode;
		
		CoreService.usersevice.getCurrentUser().then(function (response) {
			var usercontext = Ext.decode(response.responseText);
			submissionPanel.submitterForm.getForm().setValues(usercontext);
			submissionPanel.usercontext = usercontext;
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
		detailSection.getComponent('metadataGrid').getStore().removeAll();
		detailSection.getComponent('dependenciesGrid').getStore().removeAll();
		detailSection.getComponent('mediaGrid').getStore().removeAll();
		detailSection.getComponent('resourceGrid').getStore().removeAll();
		detailSection.getComponent('contactGrid').getStore().removeAll();
		detailSection.getComponent('optionalAttributes').getStore().removeAll();
				
		//set notification to checked
		submissionPanel.reviewPanel.getComponent('approvalNotification').setValue(true);
				
	}	
	
	
});
