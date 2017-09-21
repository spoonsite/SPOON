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

Ext.define('OSF.form.Section', {
	extend: 'Ext.form.Panel',
	alias: 'osf.form.Section',

	layout: 'fit',	
	listeners: {
		close: function(panel, opts) {
			if (panel.saveTask) {
				panel.saveTask.cancel();
			}
		}
	},
	dockedItems: [
		{
			xtype: 'checkbox',
			name: 'privateSection',
			dock: 'top',
			boxLabel: 'Private <i class="fa fa-question-circle" data-qtip="Hides whole section when published"></i>',
			listeners: {
				change: function(field, newValue, oldValue, opts) {	
					var sectionForm = field.up('panel');
					if (!sectionForm.initialSet) {
						sectionForm.markUnsaved();	
					}
				}
			}												
		},
		{
			xtype: 'toolbar',
			itemId: 'tools',
			dock: 'bottom',
			items: [		
				{
					xtype: 'combo',
					itemId: 'workflowStatus',
					name: 'workflowStatus',										
					labelAlign: 'right',												
					margin: '0 0 5 0',
					editable: false,
					typeAhead: false,
					width: 400,
					fieldLabel: 'Status <span class="field-required" />',	
					displayField: 'description',
					valueField: 'code',
					labelSeparator: '',
					store: {
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/lookuptypes/WorkflowStatus'
						}
					}		
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Save',
					itemId: 'saveBtn',
					iconCls: 'fa fa-lg fa-save icon-button-color-save',
					handler: function() {										
						var sectionForm = this.up('panel');
						sectionForm.saveData();
					}
				},				
				{
					xtype: 'tbtext',
					itemId: 'status'
				},				
				{
					text: '&nbsp;Manage Media',					
					iconCls: 'fa fa-lg fa-image icon-button-color-default',
					scale: 'small',
					handler: function(){
						var sectionForm = this.up('form');
						
						var mediaWindow = Ext.create('Ext.window.Window',{
							title: 'Section Media',
							iconCls: 'fa fa-lg fa-image',
							modal: true,
							closeAction: 'destroy',
							width: 800,
							height: 400,
							maximizable: true,
							layout: 'fit',
							items: [
								{
									xtype: 'grid',
									store: {
										autoLoad: true,
										fields: [
											{
												name: 'updateDts',
												type:	'date',
												dateFormat: 'c'
											}
										],
										proxy: {
											type: 'ajax',
											url: 'api/v1/resource/evaluations/' + sectionForm.evaluationId + '/sections/' + sectionForm.sectionId + '/media'
										}
									},
									columnLines: true,
									columns: [		
										{ text: 'Media Type', dataIndex: 'mediaTypeDescription', width: 150, hidden: true },
										{ text: 'Original Name', dataIndex: 'originalName', flex: 1, minWidth: 200 },
										{ text: 'Mime Type', dataIndex: 'mimeType', width: 150, hidden: true },
										{ text: 'Private', dataIndex: 'privateMedia', width: 150 },
										{ text: 'Caption', dataIndex: 'caption', flex: 2, minWidth: 200 },		
										{ text: 'Preview', dataIndex: 'caption', width: 200, 
											renderer: function(value, meta, record) {
												if (record.get('mediaTypeCode') === 'VID') {
													return '<video width="170" height="120" controls><source src="Media.action?SectionMedia&mediaId=' + record.get('contentSectionMediaId') + '#t=10" onloadedmetadata="this.currentTime=10;" type="' + (record.get('mimeType') ? record.get('mimeType') : 'video/mp4') + '" ><i class="fa fa-5x fa-file-video-o"></i></video>';
												} else if (record.get('mediaTypeCode') === 'IMG') {
													return '<img src="Media.action?SectionMedia&mediaId=' + record.get('contentSectionMediaId') + '" alt="' + value + '" width="100%" />';													
												} else {
													return '<a href="' + record.get('link') + '">Download</a>';
												}
											}
										},		
										{ text: 'Update User', dataIndex: 'updateUser', width: 200, hidden: true },
										{ text: 'Update Date', dataIndex: 'updateDts', width: 200, xtype: 'datecolumn', format: 'm/d/y H:i:s', hidden: true }
									],
									listeners: {
										selectionChange: function(selModel, selected, opts) {
											var grid = mediaWindow.down('grid');
											var tools = grid.getComponent('tools');
											if (selected.length > 0) {
												tools.getComponent('edit').setDisabled(false);
												tools.getComponent('delete').setDisabled(false);
											} else {
												tools.getComponent('edit').setDisabled(true);
												tools.getComponent('delete').setDisabled(true);
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
													text: 'Edit Metadata',
													itemId: 'edit',
													iconCls: 'fa fa-lg fa-edit icon-button-color-edit icon-lg-vertical-correction',
													disabled: true,
													handler: function() {
														var grid = this.up('grid');
														var record = grid.getSelectionModel().getSelection()[0];
														
														//set private and caption
														var editWindow = Ext.create('Ext.window.Window', {
															title: 'Edit Media',
															iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
															width: 400,
															height: 300,
															modal: true,
															closeAction: 'destroy',
															layout: 'fit',
															items: [
																{
																	xtype: 'form',
																	scrollable: 'true',
																	layout: 'anchor',																	
																	bodyStyle: 'padding: 10px;',
																	items: [
																		Ext.create('OSF.component.StandardComboBox', {
																			name: 'mediaTypeCode',									
																			allowBlank: false,								
																			margin: '0 0 5 0',
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
																			fieldLabel: 'Caption',
																			labelAlign: 'top',
																			name: 'caption',
																			width: '100%',
																			maxLength: 255,
																			labelSeparator: ''
																		}, 
																		{
																			xtype: 'checkbox',
																			boxLabel: 'Private',
																			name: 'privateMedia'
																		}
																	],
																	dockedItems: [
																		{
																			xtype: 'toolbar',
																			dock: 'bottom',
																			items: [
																				{
																					text: 'Save',
																					iconCls: 'fa fa-lg fa-save icon-button-color-save',
																					handler: function() {
																						var form = this.up('form');
																						var formData = form.getValues();
																						
																						record.set('caption', formData.caption);
																						record.set('privateMedia', formData.privateMedia);
																						record.set('mediaTypeCode', formData.mediaTypeCode);
																						
																						var recordData = record.data;
																						delete recordData.type;
																						
																						CoreUtil.submitForm({
																							url: 'api/v1/resource/evaluations/' +
																								sectionForm.evaluationId +
																								'/sections/' +
																								sectionForm.sectionId + 
																								'/media/' +
																								record.get('contentSectionMediaId'),
																							method: 'PUT',
																							form: form,
																							data: recordData,																							
																							success: function(form, action) {
																								grid.getStore().load();
																								editWindow.close();
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
																					handler: function() {
																						editWindow.close();
																					}																					
																				}
																			]
																		}
																	]
																}
															]
														});
														editWindow.show();
														editWindow.down('form').loadRecord(record);
													}
												},
												{
													xtype: 'tbseparator'
												},
												{
													text: 'Upload',
													iconCls: 'fa fa-lg fa-upload icon-button-color-default',
													handler: function() {
														var grid = this.up('grid');
														
														var uploadWin = Ext.create('Ext.window.Window', {
															title: 'Upload Media',
															iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
															width: 400,
															height: 375,
															modal: true,
															closeAction: 'destroy',
															layout: 'fit',
															items: [
																{
																	xtype: 'form',
																	scrollable: 'true',
																	layout: 'anchor',																	
																	bodyStyle: 'padding: 10px;',
																	items: [
																		Ext.create('OSF.component.StandardComboBox', {
																			name: 'mediaTypeCode',									
																			allowBlank: false,								
																			margin: '0 0 5 0',
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
																			fieldLabel: 'Caption',
																			labelAlign: 'top',
																			name: 'caption',
																			width: '100%',
																			maxLength: 255,
																			labelSeparator: ''
																		}, 
																		{
																			xtype: 'checkbox',
																			boxLabel: 'Private',
																			name: 'privateMedia'
																		},
																		{
																			xtype: 'filefield',
																			name: 'file',
																			fieldLabel: 'Upload file',
																			labelAlign: 'top',
																			labelSeparator: '',
																			width: '100%'
																		}
																	],
																	dockedItems: [
																		{
																			xtype: 'toolbar',
																			dock: 'bottom',
																			items: [
																				{
																					text: 'Save',
																					iconCls: 'fa fa-lg fa-save icon-button-color-save',
																					handler: function() {
																						var uploadForm = this.up('form');
																						var formData = uploadForm.getValues();
																						
																						uploadForm.setLoading("Uploading Media...");																							
																						uploadForm.submit({
																							url: 'Media.action?UploadSectionMedia',
																							method: 'POST',
																							params: {
																								'contentSectionMedia.mediaTypeCode': formData.mediaTypeCode,
																								'contentSectionMedia.contentSectionId': sectionForm.sectionId,
																								'contentSectionMedia.caption': formData.caption,
																								'contentSectionMedia.privateMedia': formData.privateMedia ? formData.privateMedia : false
																							},
																							callback: function() {
																								uploadForm.setLoading(false);
																							},
																							success: function(form, action) {	
																								uploadForm.setLoading(false);
																								grid.getStore().load();
																								uploadWin.close();
																							},
																							failure: function(form, action){																								
																								uploadForm.setLoading(false);
																								Ext.Msg.show({
																									title: 'Upload Failed',
																									msg: 'The file upload was not successful.',
																									icon: Ext.Msg.ERROR,
																									buttons: Ext.Msg.OK
																								});	
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
																					handler: function() {
																						uploadWin.close();
																					}																					
																				}
																			]																			
																		}
																	]
																}
															]
														});
														uploadWin.show();
														
													}
												},
												{
													xtype: 'tbfill'
												},
												{
													text: 'Delete',
													itemId: 'delete',
													iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
													disabled: true,
													handler: function() {
														var grid = this.up('grid');
														var record = grid.getSelectionModel().getSelection()[0];
														Ext.Msg.show({
															title:'Delete Media?',
															message: 'Are you sure you want delete selected media?',
															buttons: Ext.Msg.YESNO,
															icon: Ext.Msg.QUESTION,
															fn: function(btn) {
																if (btn === 'yes') {
																	
																	mediaWindow.setLoading('Deleting Media...');
																	Ext.Ajax.request({
																		url: 'api/v1/resource/evaluations/' +
																		sectionForm.evaluationId +
																		'/sections/' +
																		sectionForm.sectionId + 
																		'/media/' +
																		record.get('contentSectionMediaId'),
																		method: 'DELETE',
																		callback: function() {
																			mediaWindow.setLoading(false);
																		},
																		success: function(response, opts) {
																			grid.getStore().load();
																		}
																	});
																	
																}
															}
														});	
													}
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
											xtype: 'tbfill'
										},
										{
											text: 'Close',
											iconCls: 'fa fa-lg fa-close icon-button-color-warning icon-lg-vertical-correction',
											scale: 'medium',
											handler: function() {
												mediaWindow.close();
											}
										},
										{
											xtype: 'tbfill'
										}
									]
								}
							]
						});
						mediaWindow.show();
						
					}
				}
			]
		}
	],
	initComponent: function () {		
		this.callParent();
		
		var sectionForm = this;
		
		sectionForm.saveTask = new Ext.util.DelayedTask(function(){
			sectionForm.saveData();
		});	
		
	},
	loadData: function(evaluationId, componentId, data, opts) {
	
		var sectionForm = this;
		
		sectionForm.setLoading(true);
		Ext.Ajax.request({
			url: 'api/v1/resource/evaluations/' + evaluationId + '/sections/' + data.section.contentSectionId + '/details',
			callback: function() {
				sectionForm.setLoading(false);
			},
			success: function(response, opts) {
				var originalData = Ext.decode(response.responseText);
				
				sectionForm.originalData = originalData;
				
				var record = Ext.create('Ext.data.Model', {			
				});
				record.set(originalData.section);
				sectionForm.initialSet = true;
				sectionForm.loadRecord(record);
				sectionForm.initialSet = false;
				sectionForm.evaluationId = evaluationId;
				sectionForm.sectionId = data.section.contentSectionId;
				
				var mediaSelectionUrl = function() {
					return 'api/v1/resource/evaluations/' + evaluationId + '/sections/' + data.section.contentSectionId + '/media';
				};		
				
				var mediaUploadHandler = function(uploadForm, mediaInsertWindow){
					//check name 
					uploadForm.setLoading("Uploading Media...");	
					var formData = uploadForm.getValues();
					uploadForm.submit({
						url: 'Media.action?UploadSectionMedia',
						method: 'POST',
						params: {
							'contentSectionMedia.mediaTypeCode': mediaInsertWindow.mediaToShow,
							'contentSectionMedia.contentSectionId': data.section.contentSectionId,
							'contentSectionMedia.caption': formData['temporaryMedia.name'],
							'contentSectionMedia.privateMedia': false
						},
						callback: function() {
							uploadForm.setLoading(false);
						},
						success: function(form, action) {		
							uploadForm.setLoading(false);
							var sectionMediaResponse = Ext.decode(action.response.responseText);
							var sectionMedia = sectionMediaResponse.data[0];
							
							var link = "Media.action?SectionMedia&mediaId=" + sectionMedia.contentSectionMediaId;							
							mediaInsertWindow.insertInlineMedia(link, name, mediaInsertWindow.mediaToShow, sectionMedia.mimeType);													
							mediaInsertWindow.close();
						},
						failure: function(form, action){
							uploadForm.setLoading(false);
							Ext.Msg.show({
								title: 'Upload Failed',
								msg: 'The file upload was not successful.',
								icon: Ext.Msg.ERROR,
								buttons: Ext.Msg.OK
							});	
						}
					});
				};
			
				if (originalData.subsections && originalData.subsections.length > 0) {
					var contentPanel = Ext.create('Ext.panel.Panel', {
						scrollable: true,
						itemId: 'contentPanel',
						bodyStyle: 'padding: 20px;'
					});
					var items = [];			
					if (!originalData.section.noContent) {			
						var border = '0';
						if (originalData.section.privateSection) {
							border = '1px dash steelblue';
						}

						items.push({
							xtype: 'tinymce_textarea',											
							fieldStyle: 'font-family: Courier New; font-size: 12px;',
							style: { border: border },					
							name: 'content',			
							maxLength: 1048576,
							height: 400,
							width: '100%',
							value: originalData.section.content,
							tinyMCEConfig: Ext.apply(CoreUtil.tinymceConfig(), {
									mediaSelectionUrl: mediaSelectionUrl,
									mediaUploadHandler: mediaUploadHandler
							}),
							listeners: {
								change: {									
									fn: function(field, newValue, oldValue) {
										sectionForm.markUnsaved();
									}
								}
							}
						});				
					}

					//create sub-sections
					Ext.Array.each(originalData.subsections, function(subsection){

						var extraTitleInfo = '';
						var border = '1px';
						if (subsection.hideTitle) {
							extraTitleInfo = " (Hidden on View)";
						}
						if (subsection.privateSection) {							
							border = '1px dash steelblue';
						}

						var subItems = [];	
						if (!subsection.noContent) {
							subItems.push({
								xtype: 'tinymce_textarea',												
								fieldStyle: 'font-family: Courier New; font-size: 12px;',
								style: { border: border },								
								name: 'subcontent',			
								maxLength: 1048576,
								height: 400,
								value: subsection.content,
								tinyMCEConfig: Ext.apply(CoreUtil.tinymceConfig(), {
										mediaSelectionUrl: mediaSelectionUrl,
										mediaUploadHandler: mediaUploadHandler
								}),
								listeners: {
									change: {										
										fn: function(field, newValue, oldValue) {
											sectionForm.markUnsaved();
										}
									}
								}
							});
						}

						Ext.Array.each(subsection.customFields, function(field) {

							if (field.fieldType === 'TEXT') {
								subItems.push({
									xtype: 'textbox',
									name: 'customValue',
									customField: field,
									fieldLabel: field.label,
									value: field.value,
									maxLength: 1048576,
									listeners: {
										change: {											
											fn: function(field, newValue, oldValue) {
												sectionForm.markUnsaved();
											}
										}
									}
								});
							} else if (field.fieldType === 'RICHTEXT') {
								subItems.push(
									{
										xtype: 'panel',						
										html: '<b>' + field.label + '</b>'
									},
									{
										xtype: 'tinymce_textarea',											
										fieldStyle: 'font-family: Courier New; font-size: 12px;',
										style: { border: border },					
										name: 'customValue',			
										customField: field,
										maxLength: 1048576,
										height: 250,
										width: '100%',
										value: field.value,
										tinyMCEConfig: Ext.apply(CoreUtil.tinymceConfig(), {
												mediaSelectionUrl: mediaSelectionUrl,
												mediaUploadHandler: mediaUploadHandler
										}),
										listeners: {
											change: {												
												fn: function(field, newValue, oldValue) {
													sectionForm.markUnsaved();
												}
											}
										}								
									}
								);						
							} else if (field.fieldType === 'COMBO') {
								subItems.push({
									xtype: 'combobox',
									name: 'customValue',
									customField: field,
									fieldLabel: field.label,
									value: field.value,
									editable: false,
									typeAhead: false,
									forceSelection: true,
									displayField: 'value',
									valueField: 'value',
									store: {
										data: field.validValues
									},
									listeners: {
										change: {											
											fn: function(field, newValue, oldValue) {
												sectionForm.markUnsaved();
											}
										}
									}								
								});						
							} else if (field.fieldType === 'COMBOEDIT') {
								subItems.push({
									xtype: 'combobox',
									name: 'customValue',
									customField: field,
									fieldLabel: field.label,
									value: field.value,
									editable: true,
									typeAhead: true,							
									displayField: 'value',
									valueField: 'value',
									store: {
										data: field.validValues
									},
									listeners: {
										change: {											
											fn: function(field, newValue, oldValue) {
												sectionForm.markUnsaved();
											}
										}
									}								
								});							
							} else if (field.fieldType === 'CHECKBOX') {
								subItems.push({
									xtype: 'checkbox',
									name: 'customValue',
									customField: field,
									boxLabel: field.label,
									value: field.value,
									listeners: {
										change: {											
											fn: function(field, newValue, oldValue) {
												sectionForm.markUnsaved();
											}
										}
									}								
								});
							}

						});

						var subSectionPanel = Ext.create('Ext.panel.Panel', {
							title: subsection.title + extraTitleInfo,
							border: border,
							subsection: subsection,
							layout: 'anchor',
							defaults: {
								labelAlign: 'right',
								width: '100%'
							},
							dockedItems: [
								{
									xtype: 'checkbox',
									name: 'subPrivateSection',
									dock: 'top',
									value: subsection.privateSection,
									boxLabel: 'Private <i class="fa fa-question-circle" data-qtip="Hides this section when published"></i>',
									listeners: {
										change: function(field, newValue, oldValue, opts) {	
											subsection.privateSection = newValue;
											sectionForm.markUnsaved();
										}
									}												
								}								
							],
							items: subItems
						});
						items.push(subSectionPanel);
					});

					contentPanel.add(items);
					sectionForm.add(contentPanel);
				} else {
					if (originalData.section.noContent) {
						sectionForm.update("There's no content allowed for this section. Check template.");
					} else {
						sectionForm.add({
							xtype: 'tinymce_textarea',											
							fieldStyle: 'font-family: Courier New; font-size: 12px;',
							style: { border: '0' },					
							name: 'content',			
							maxLength: 1048576,
							value: originalData.section.content,
							tinyMCEConfig: Ext.apply(CoreUtil.tinymceConfig(), {
									mediaSelectionUrl: mediaSelectionUrl,
									mediaUploadHandler: mediaUploadHandler
							}),
							listeners: {
								change: {									
									fn: function(field, newValue, oldValue) {
										sectionForm.markUnsaved();
									}
								}
							}	
						});
					}
				}

				if (originalData.section.privateSection) {		
					sectionForm.initialSet = true;
					sectionForm.getForm().setValues({
						privateSection: originalData.section.privateSection
					});
					sectionForm.initialSet = false;
				}

				sectionForm.getComponent('tools').getComponent('workflowStatus').on('change', function(field, newValue, oldValue){
					sectionForm.saveData();
				}, undefined, {
					buffer: 1000
				});				
			}
		});
		
		opts.commentPanel.loadComments(evaluationId, data.section.title, componentId);
	},
	markUnsaved: function () {
		var sectionForm = this;
		sectionForm.saveTask.delay(1000*60*3);	
		
		if (!sectionForm.unsavedChanges) {
			sectionForm.getComponent('tools').getComponent('status').setText('<span style="color: red; font-weight: bold;">Unsaved Changes</span>');		
			sectionForm.unsavedChanges = true;
		}
	},		
	saveData: function(){
		
		var sectionForm = this;
		
		if (sectionForm.firstLoadDone) {
		
			var data = sectionForm.getValues();
			
			sectionForm.originalData.section.privateSection = data.privateSection ? data.privateSection : false;
			sectionForm.originalData.section.content = data.content;
			sectionForm.originalData.section.workflowStatus = data.workflowStatus;

			var contentSectionAll = {
				section: sectionForm.originalData.section,
				subsections: []
			};

			var contentPanel = sectionForm.getComponent('contentPanel');
			if (contentPanel) {
				Ext.Array.each(contentPanel.items.items, function(subsectionPanel){
					
					if (subsectionPanel.subsection) {
						//find the matching original data
						var originalSubSection = null;
						Ext.Array.each(sectionForm.originalData.subsections, function(subSection){
							if (subsectionPanel.subsection.subSectionId === subSection.subSectionId) {
								originalSubSection = subSection;
							}
						});

						//update data
						if (originalSubSection) {

							Ext.Array.each(subsectionPanel.items.items, function(formItem){
								if (formItem.name === 'subcontent') {
									originalSubSection.content = formItem.getValue();									
								}
								if (formItem.name === 'subPrivateSection') {
									originalSubSection.privateSection = formItem.getValue();
								}
							});	


							if (originalSubSection.customFields && originalSubSection.customFields.length > 0) {						
								var customFieldIndex = 0;
								Ext.Array.each(subsectionPanel.items.items, function(formItem){
									if (formItem.name !== 'subcontent') {
										originalSubSection.customFields[customFieldIndex].value = formItem.getValue();
										customFieldIndex++;
									}								
								});						
							}						
							contentSectionAll.subsections.push(originalSubSection);
						}	
					}
						
				});
			}
			sectionForm.saveTask.cancel();
			
			sectionForm.getComponent('tools').getComponent('saveBtn').setLoading("Saving...");
			CoreUtil.submitForm({
				url: 'api/v1/resource/evaluations/' + 
					sectionForm.evaluationId
					+ '/sections/' + 
					sectionForm.originalData.section.contentSectionId,
				method: 'PUT',
				data: contentSectionAll,
				form: sectionForm,
				noLoadmask: true,
				callback: function(form, action) {
					sectionForm.getComponent('tools').getComponent('saveBtn').setLoading(false);
				},
				success: function(action, opts) {			
					Ext.toast('Saved Section');
					sectionForm.getComponent('tools').getComponent('status').setText('Saved at ' + Ext.Date.format(new Date(), 'g:i:s A'));					
					sectionForm.unsavedChanges = false;
				}	
			});			
		
		} else {
			sectionForm.firstLoadDone = true;
		}
	}
	
});
