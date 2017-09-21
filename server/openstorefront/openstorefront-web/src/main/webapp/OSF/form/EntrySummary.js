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

Ext.define('OSF.form.EntrySummary', {
	extend: 'Ext.form.Panel',
	alias: 'osf.form.EntrySummary',

	layout: 'fit',
	listeners: {
		close: function(panel, opts) {
			if (panel.saveTask) {
				panel.saveTask.cancel();
			}
		}
	},
	bodyStyle: 'padding: 20px',
	dockedItems: [
		{
			xtype: 'panel',
			itemId: 'topform',
			dock: 'top',
			layout: 'anchor',
			bodyStyle: 'padding: 10px 20px 0px 20px;',
			defaults: {
				width: '100%',
				labelAlign: 'right'
			},				
			items: [
				{
					xtype: 'textfield',
					itemId: 'name',
					name: 'name',
					fieldLabel: 'Name <span class="field-required" />',
					allowBlank: false,
					maxLength: 255														
				},		
				{
					xtype: 'combo',
					itemId: 'organization',
					name: 'organization',									
					allowBlank: false,															
					fieldLabel: 'Organization <span class="field-required" />',
					forceSelection: false,
					valueField: 'description',
					displayField: 'description',
					editable: true,
					queryMode: 'remote',
					store: {				
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/organizations/lookup'
						},
						sorters: [{
							property: 'description',
							direction: 'ASC'
						}]
					}
				},	
				{
					xtype: 'datefield',
					itemId: 'releaseDate',
					fieldLabel: 'Release Date <i class="fa fa-question-circle" data-qtip="This is the last release date of the entry not the evaluation. Typically leave as is."></i>',
					name: 'releaseDate',
					labelSeparator: '',
					submitFormat: 'Y-m-d\\TH:i:s.u'
				},
				{
					xtype: 'textfield',
					itemId: 'version',
					fieldLabel: 'Version <i class="fa fa-question-circle" data-qtip="This is the version of the entry and not of the evaluation. Typically leave blank."></i>',
					labelSeparator: '',
					name: 'version'																		
				},										
				{
					xtype: 'panel',
					html: '<b>Description</b> <span class="field-required" />'
				}				
			]
		},
		{
			xtype: 'toolbar',
			itemId: 'tools',
			dock: 'bottom',
			items: [
				{
					xtype: 'tbfill'
				},
				{
					text: 'Save',
					itemId: 'saveBtn',
					iconCls: 'fa fa-lg fa-save icon-button-color-save',
					handler: function() {										
						var entryForm = this.up('panel');
						entryForm.saveData();
					}
				},				
				{
					xtype: 'tbtext',
					itemId: 'status'
				}				
			]
		}
	],	
	items: [
		
	],
	initComponent: function () {		
		this.callParent();
		
		var entryForm = this;
		
		entryForm.add(
			{
				xtype: 'tinymce_textarea',
				itemId: 'description',
				fieldStyle: 'font-family: Courier New; font-size: 12px;',
				style: { border: '0' },
				margin: '-20 0 0 0',
				name: 'description',			
				maxLength: 65536,
				tinyMCEConfig: Ext.apply(CoreUtil.tinymceSearchEntryConfig("osfmediaretriever"), {
					mediaSelectionUrl: function(){					
						return 'api/v1/resource/components/' + entryForm.componentId + '/media/view';					
					}
				})			
			}				
		);

		entryForm.saveTask = new Ext.util.DelayedTask(function(){
			entryForm.saveData();
		});	
	},
	
	loadData: function(evaluationId, componentId, data, opts) {
		var entryForm = this;
		
		entryForm.setLoading(true);
		Ext.Ajax.request({
			url: 'api/v1/resource/components/' + componentId,
			callback: function() {
				entryForm.setLoading(false);
			},
			success: function(response, opt) {
				var component = Ext.decode(response.responseText);
				var record = Ext.create('Ext.data.Model',{					
				});
				record.set(component);				
				entryForm.loadRecord(record);
				entryForm.componentId = componentId;
				entryForm.componentData = component;
				
				if (opts && opts.mainForm) {
					entryForm.refreshCallback = opts.mainForm.refreshCallback;
				}
				
				//set change event
				entryForm.getComponent('description').on('change', function(){
					entryForm.markUnsaved();
				}, undefined);
				entryForm.getComponent('topform').getComponent('name').on('change', function(){
					entryForm.markUnsaved();
				}, undefined);
				entryForm.getComponent('topform').getComponent('organization').on('change', function(){
					entryForm.markUnsaved();
				}, undefined);				
				entryForm.getComponent('topform').getComponent('releaseDate').on('change', function(){
					entryForm.markUnsaved();
				}, undefined);	
				entryForm.getComponent('topform').getComponent('version').on('change', function(){
					entryForm.markUnsaved();
				}, undefined);					
				
			}
		});	
		
		opts.commentPanel.loadComments(evaluationId, "Entry Summmary", componentId);
	},
	markUnsaved: function () {
		var entryForm = this;
		entryForm.saveTask.delay(1000*60*3);	
		
		if (!entryForm.unsavedChanges) {
			entryForm.getComponent('tools').getComponent('status').setText('<span style="color: red; font-weight: bold;">Unsaved Changes</span>');
			entryForm.unsavedChanges = true;
		}
	},	
	saveData: function() {		
		var entryForm = this;
		
		//make sure it's valid		
		var data = entryForm.getValues();
	
		
		if (!entryForm.saving &&
			entryForm.isValid() &&
			data.description &&
			data.description !== '') {
		
			entryForm.saving = true;
			Ext.Ajax.request({
				url: 'api/v1/resource/components/' + entryForm.componentId + '/attributes',
				success: function(response, opts) {
					var attributes = Ext.decode(response.responseText);
					
					Ext.Ajax.request({
						url: 'api/v1/resource/attributes/attributetypes/required?componentType=' + entryForm.componentData.componentType,
						success: function(response, opts) {
							
							var requiredAttributes = Ext.decode(response.responseText);
							var missingAttributes = [];
							
							data.componentType = entryForm.componentData.componentType;
							data.approvalState = entryForm.componentData.approvalState;							
							if (entryForm.componentData.dataSource) {
								data.dataSource = entryForm.componentData.dataSource;
							}
							if (entryForm.componentData.securityMarkingType) {
								data.securityMarkingType = entryForm.componentData.securityMarkingType;
							}							
							if (entryForm.componentData.dataSensitivity) {
								data.dataSensitivity = entryForm.componentData.dataSensitivity;
							}							
							
							var requiredForComponent = {
								component: data,
								attributes: []						
							};
							Ext.Array.each(attributes, function(attribute){
								requiredForComponent.attributes.push({
									componentAttributePk: {
										attributeType: attribute.componentAttributePk.attributeType,
										attributeCode: attribute.componentAttributePk.attributeCode
									}
								});								
							});
							
							var hasAllRequiredAttributes = false;
							Ext.Array.each(requiredAttributes, function(required){
								var found = false;
								Ext.Array.each(attributes, function(attribute){
									if (required.attributeType === attribute.componentAttributePk.attributeType) {
										found = true;
									}
								});				
								if (!found) {
									missingAttributes.push(required);
								}
							});
							
							if (missingAttributes.length > 0) {
								//fill in defaults if possible	
								var promptAttributes = [];
								Ext.Array.each(missingAttributes, function(attributeType){
									if (attributeType.defaultAttributeCode) {
										requiredForComponent.attributes.push({
											componentAttributePk: {
												attributeType: attributeType.attributeType,
												attributeCode: attributeType.defaultAttributeCode
											}
										});	
									} else  {
										promptAttributes.push(attributeType);
									}
								});
								if (promptAttributes.length > 0) {
									entryForm.saving = false;
									
									//prompt for any remaining
									var promptWin = Ext.create('Ext.window.Window', {
										title: 'Missing Required Attributes',
										modal: true,
										closeAction: 'destroy',
										closable: false,
										width: 400,
										height: (100 * promptAttributes.length) + 75,
										layout: 'fit',
										items: [
											{
												xtype: 'form',
												scrollable: false,
												itemId: 'form',
												bodyStyle: 'padding: 10px;',
												layout: 'anchor',
												defaults: {
													labelAlign: 'top',
													width: '100%',
													labelSeparator: '',
													allowBlank: false
												},
												items: [],
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
																	var attributeForm = this.up('form');
																	var attributeItems = attributeForm.items.items;

																	var countOfSaves = 0;
																	var completeSave = function() {
																		countOfSaves++;
																		if (countOfSaves === promptAttributes.length) {
																			promptWin.setLoading(false);
																			promptWin.close();
																			Ext.toast('Updated required Attributes; Continuing with save...');																	
																			entryForm.saveData();
																		}
																	};																	
																	
																	promptWin.setLoading('Saving...');																	
																	Ext.Array.each(attributeItems, function(item) {

																		var promise = Ext.Ajax.request({
																			url: 'api/v1/resource/components/' + entryForm.componentId + '/attributes',
																			method: 'POST',
																			jsonData: {
																				componentAttributePk: {
																					attributeType: item.attribute.attributeType,
																					attributeCode: item.getValue()	
																				}
																			}
																		});					
																		promise.then(function(response){
																			completeSave();
																		});																
																	});																														
																}
															}
														]
													}
												]												
											}
										]
									});
									
									//add prompts
									var requiredToAdd = [];
									Ext.Array.each(promptAttributes, function(promptAttribute){
										requiredToAdd.push({
											xtype: 'combo',
											name: 'attribute',
											attribute: promptAttribute,
											valueField: 'attributeCode',
											displayField: 'label',
											emptyText: 'Select',
											fieldLabel: promptAttribute.attributeType + ' <span class="field-required" />',
											store: {
												autoLoad: true,
												fields: [
													{ name: 'attributeCode', mapping: function(mappedData) {
															return mappedData.attributeCodePk.attributeCode;
														}
													}
												],
												proxy: {
													type: 'ajax',
													url: 'api/v1/resource/attributes/attributetypes/' + promptAttribute.attributeType + '/attributecodes'
												}
											}
										});
									});
									promptWin.queryById('form').add(requiredToAdd);
									promptWin.show();
									
								} else {
									hasAllRequiredAttributes = true;
								}
							} else {
								hasAllRequiredAttributes = true;
							}
							entryForm.saveTask.cancel();
							
							if (hasAllRequiredAttributes) {
								entryForm.getComponent('tools').getComponent('saveBtn').setLoading("Saving...");
								CoreUtil.submitForm({
									url: 'api/v1/resource/components/' + 
										entryForm.componentId,
									method: 'PUT',
									data: requiredForComponent,
									form: entryForm,
									noLoadmask: true,
									callback: function() {
										entryForm.saving = false;
										entryForm.getComponent('tools').getComponent('saveBtn').setLoading(false);
									},
									success: function(action, opts) {							

										Ext.toast('Saved Entry Summary');
										entryForm.getComponent('tools').getComponent('status').setText('Saved at ' + Ext.Date.format(new Date(), 'g:i:s A'));
										entryForm.unsavedChanges = false;
										
										if (entryForm.refreshCallback) {
											entryForm.refreshCallback();
										}
									}	
								});	
							}
							
							
						}
					});

				},
				failure: function(response, opt) {
					entryForm.saving = false;
				}
			});
			
		}
		
	}
	
});
