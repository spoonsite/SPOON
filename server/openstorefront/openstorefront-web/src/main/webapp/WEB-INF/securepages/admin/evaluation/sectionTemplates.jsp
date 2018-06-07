<%-- 
    Document   : sectionTemplates
    Created on : Oct 11, 2016, 2:30:55 PM
    Author     : dshurtleff
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
	
	<stripes:layout-render name="../../../../layout/adminheader.jsp">		
	</stripes:layout-render>		
		
	<script type="text/javascript">
		/* global Ext, CoreUtil */
		Ext.onReady(function(){	
			
			
			
			var showAddEditWindow = function(record) {				
			
				var addEditWindow = Ext.create('Ext.window.Window', {
					title: 'Add/Edit Section Template',
					iconCls: 'fa fa-edit',
					closeAction: 'destroy',
					modal: true,
					width: '75%',
					height: '75%',
					maximizable: true,
					layout: 'fit',
					listeners: {
						beforeclose: function(win, opts) {
							var form = win.queryById('templateForm');
							if (form.isDirty() && !win.forceClose) {
								Ext.Msg.show({
									title:'Discard Changes?',
									message: 'You have unsaved changes. Discard Changes?',
									buttons: Ext.Msg.YESNO,
									icon: Ext.Msg.QUESTION,
									fn: function(btn) {
										if (btn === 'yes') {
											win.forceClose = true;
											win.close();
										} 
									}
								});								
								return false;
							} else {
								return true;
							}
						}
					},
					items: [
						{
							xtype: 'form',
							itemId: 'templateForm',
							bodyStyle: 'padding: 10px;',
							scrollable: true,
							trackResetOnLoad: true,
							dockedItems: [	
								{
									xtype: 'toolbar',
									dock: 'top',
									items: [
										{
											text: 'Add Sub-Section',
											iconCls: 'fa fa-lg fa-plus icon-button-color-save icon-small-vertical-correction',																			
											handler: function(){
												Ext.getCmp('sectionPanel').expand();
												addSubSection(Ext.getCmp('sectionPanel'));
											}										
										}
									]
								},
								{
									xtype: 'toolbar',
									itemId: 'bottomTools',
									dock: 'bottom',
									items: [
										{
											text: 'Save',
											formBind: true,
											iconCls: 'fa fa-lg fa-save icon-button-color-save icon-small-vertical-correction',
											scale: 'medium',
											width: '100px',
											handler: function() {
												var form = this.up('form');
												var data = form.getValues();
												
												//un-pack
												var packedView = {
													contentSectionTemplate: {
														templateId: data.templateId,
														name: data.name,
														description: data.description
													},
													contentSection: {
														contentSectionId: data.contentSectionId,
														title: data.title,
														content: data.content,
														privateSection: data.privateSection ? data.privateSection : false,
														noContent: data.noContent ? data.noContent : false
													},
													subSections: []
												};
												
												//subsections	
												var subItems = Ext.getCmp('sectionPanel').items.items;
												var sectionOrder = 0;
												Ext.Array.each(subItems, function(item){
													if (item.subSection) {
														var subFormData = item.getValues();
																												
														var fieldGrid = item.getComponent('customFieldGrid');
														
														var customFields = [];
														fieldGrid.getStore().each(function(fieldRecord) {
															
															var values = fieldRecord.get('validValues').split(',');
															var valueValids = [];
															Ext.Array.each(values, function(value){
																if (value && value !== '')
																valueValids.push({
																	value: value
																});
															});
															
															customFields.push({
																label: fieldRecord.get('label'),
																fieldType: fieldRecord.get('fieldType'),
																validValues: valueValids
															});
														});
														
														packedView.subSections.push({
															order: sectionOrder,
															subSectionId: subFormData.subsectionId,
															title: subFormData.subsectionTitle,
															content: subFormData.subsectionContent,
															privateSection: subFormData.subsectionPrivateSection ? subFormData.subsectionPrivateSection : false,
															hideTitle: subFormData.subsectionHideTitle ? subFormData.subsectionHideTitle : false,
															noContent: data.subsectionNoContent ? data.subsectionNoContent : false,
															customFields: customFields
														});																
														
														sectionOrder++;
													}
												});
												
		
												//edit
												var method = 'POST';
												var update = '';
												if (data.templateId) {
													update = '/' + data.templateId;
													method = 'PUT';
												} 
												
												//save
												CoreUtil.submitForm({
													url: 'api/v1/resource/contentsectiontemplates' + update,
													method: method,
													data: packedView,
													form: form,
													success: function(){
														Ext.toast("Saved Successfully");
														actionRefresh();														
														
														var saveContinue = form.getComponent('bottomTools').getComponent('saveContinue').getValue();
														if (!saveContinue) {
															addEditWindow.forceClose = true;
															addEditWindow.close();
														}
													}
												});
												
											}										
										},
										{
											xtype: 'checkbox',
											itemId: 'saveContinue',
											checked: false,
											hidden: true,
											boxLabel: 'Save and Continue'
										},
										{
											xtype: 'tbfill'
										},
										{
											text: 'Cancel',									
											iconCls: 'fa fa-lg fa-close icon-button-color-warning icon-small-vertical-correction',
											scale: 'medium',
											handler: function() {
												this.up('window').close();
											}										
										}										
									]
								}
							],											
							items: [
								{
									xtype: 'hidden',
									name: 'templateId'
								},
								{
									xtype:'fieldset',
									title: 'Template',
									collapsible: true,
									defaults: {
										labelAlign: 'top',
										labelSeparator: '',
										width: '100%'
									},									 
									items: [
									{
										xtype: 'textfield',
										name: 'name',
										fieldLabel: 'Template Name <span class="field-required" />',
										allowBlank: false,
										maxLength: 255									
									},
									{
										xtype: 'panel',
										html: '<b>Template Description</b>'
									},
									{
										xtype: 'tinymce_textarea',
										fieldStyle: 'font-family: Courier New; font-size: 12px;',
										style: { border: '0' },
										name: 'description',								
										height: 300,
										maxLength: 16384,
										tinyMCEConfig: CoreUtil.tinymceConfig()
									}	
									]
								},
								{
									xtype:'fieldset',
									id: 'sectionPanel',
									title: 'Section',
									collapsible: true,
									defaults: {
										labelAlign: 'top',
										labelSeparator: '',
										width: '100%'
									},								
									items: [
										{
											xtype: 'hidden',
											name: 'contentSectionId'
										},										
										{
											xtype: 'textfield',
											name: 'title',
											fieldLabel: 'Title <span class="field-required" />',
											allowBlank: false,
											maxLength: 255									
										},
										{
											xtype: 'checkbox',
											name: 'privateSection',
											boxLabel: 'Private'
										},
										{
											xtype: 'checkbox',
											name: 'noContent',
											boxLabel: 'No Content',
											listeners: {
												change: function(field, newValue, oldValue) {
													if (newValue) {
														Ext.getCmp('sectionPanel').getComponent('content').setHidden(true);
														Ext.getCmp('sectionPanel').getComponent('content-label').setHidden(true);
													} else {
														Ext.getCmp('sectionPanel').getComponent('content').setHidden(false);
														Ext.getCmp('sectionPanel').getComponent('content-label').setHidden(false);
													}
												}
											}
										},											
										{
											xtype: 'panel',
											itemId: 'content-label',
											html: '<b>Default Content</b>'
										},
										{
											xtype: 'tinymce_textarea',
											itemId: 'content',
											fieldStyle: 'font-family: Courier New; font-size: 12px;',
											style: { border: '0' },
											name: 'content',								
											height: 300,
											maxLength: 16384,
											tinyMCEConfig: CoreUtil.tinymceConfig()
										}									
									]
								}
							]
						}
					]
				});


				var addSubSection = function(parentComponent) {

					var subSectionCmp = Ext.create('Ext.form.Panel',{
						title: 'Sub-Section',
						subSection: true,
						frame: true,
						margin: '0 0 10 0',
						collapsible: true,
						titleCollapse: true,
						closable: true,
						trackResetOnLoad: true,
						listeners: {
							beforeclose: function(panel, opt) {								
								//prompt
								if (!panel.forceClose) {
									Ext.Msg.show({
										title:'Close SubSection?',
										message: 'Are you want to remove sub-section?',
										buttons: Ext.Msg.YESNO,
										icon: Ext.Msg.QUESTION,
										fn: function(btn) {
											if (btn === 'yes') {
												panel.forceClose = true;
												panel.close();
											} 
										}
									});	
									return false;
								} else {
									return true;
								}
							}
						},						
						layout: 'anchor',
						bodyStyle: 'padding: 5px;',
						defaults: {
							width: '100%'
						},
						items: [
							{
								xtype: 'textfield',
								name: 'subsectionTitle',
								fieldLabel: 'Title <span class="field-required" />',
								allowBlank: false,
								maxLength: 255,
								enableKeyEvents: true,
								listeners: {
									keyup: function(field, e, opts) {
										var panel = field.up('panel');
										if (field.getValue()) {
											panel.setTitle(field.getValue());
										} else {
											panel.setTitle('Sub-Section');
										}
									}
								}
							},
							{
								xtype: 'checkbox',
								name: 'subsectionPrivateSection',
								boxLabel: 'Private'
							},
							{
								xtype: 'checkbox',
								name: 'subsectionHideTitle',
								boxLabel: 'Hide Title'
							},						
							{
								xtype: 'checkbox',
								name: 'subsectionNoContent',
								boxLabel: 'No Content',
								listeners: {
									change: function(field, newValue, oldValue) {
										if (newValue) {
											subSectionCmp.getComponent('content').setHidden(true);
										} else {
											subSectionCmp.getComponent('content').setHidden(false);
										}
									}
								}
							},							
							{
								xtype: 'panel',
								itemId: 'content',
								title: 'Default Content',
								collapsible: true,
								titleCollapse: true,
								items: [
									{
										xtype: 'tinymce_textarea',
										fieldStyle: 'font-family: Courier New; font-size: 12px;',
										style: { border: '0' },
										name: 'subsectionContent',		
										width: '100%',
										height: 300,
										maxLength: 16384,
										tinyMCEConfig: CoreUtil.tinymceConfig()
									}
								]
							},
							{
								xtype: 'grid',
								itemId: 'customFieldGrid',
								height: 250,
								dockedItems: [
									{
										xtype: 'toolbar',
										dock: 'top',
										itemId: 'tools',
										items: [											
											{
												text: 'Add Custom Field',
												iconCls: 'fa fa-lg fa-plus icon-button-color-save icon-small-vertical-correction',
												handler: function() {

													var customFieldWin = Ext.create('Ext.window.Window', {
														title: 'Add Custom Field',
														alwaysOnTop: true,
														closeAction: 'destroy',
														modal: true,
														width: '50%',
														height: 325,
														layout: 'fit',
														items: [
															{
																xtype: 'form',
																itemId:'form',
																bodyStyle: 'padding: 10px;',
																layout: 'anchor',
																defaults: {
																	width: '100%',
																	labelAlign: 'top',
																	labelSeparator: ''
																},
																items: [
																	{
																		xtype: 'textfield',
																		fieldLabel: 'Label <span class="field-required" />',
																		name: 'label',
																		maxLength: 255,
																		allowBlank: false
																	}, 
																	Ext.create('OSF.component.StandardComboBox', {
																		name: 'fieldType',									
																		allowBlank: false,								
																		margin: '0 0 0 0',
																		editable: false,
																		typeAhead: false,
																		width: '100%',
																		fieldLabel: 'Custom Field Type <span class="field-required" />',
																		storeConfig: {
																			url: 'api/v1/resource/lookuptypes/CustomFieldType'
																		},
																		listeners: {
																			change: function(field, newValue, oldValue, opts) {
																				var form = this.up('form');
																				if (newValue === 'COMBO' || newValue === 'COMBOEDIT') {																					
																					form.getComponent('validValueText').setHidden(false);
																					form.getComponent('tagToolbar').setHidden(false);
																				} else {
																					form.getComponent('validValueText').setHidden(true);
																					form.getComponent('tagToolbar').setHidden(true);
																				}
																			}
																		}
																	}), 
																	{
																		xtype: 'textfield',
																		itemId: 'validValueText',
																		fieldLabel: 'Valid Values',
																		name: 'validValues',
																		enableKeyEvents: true,
																		hidden: true,
																		listeners: {																			
																			specialkey: function(field, e){
																				if (e.getKey() === e.ENTER) {
																					var value = field.getValue();
																					var tagField = field.up('form').getComponent('tagToolbar');
																					var data = [];
																					data.push({
																						text: ''
																								
																					});
																					tagField.add({
																						text: value,
																						iconAlign: 'right',
																						iconCls: 'fa fa-close',
																						handler: function() {
																							var toolbar = this.up('toolbar');
																							toolbar.remove(this, true);
																						}
																					});
																					
																					field.reset();
																				}
																			}
																		}
																	},
																	{
																		xtype: 'toolbar',
																		hidden: true,
																		itemId: 'tagToolbar',
																		overflowHandler: 'scroller',
																		border: 1,
																		style: 'background: lightgrey;'
																	}
																],
																dockedItems: [
																	{
																		xtype: 'toolbar',
																		dock: 'bottom',
																		items: [
																			{
																				text: 'Add',
																				formBind: true,
																				iconCls: 'fa fa-lg fa-plus icon-button-color-save icon-small-vertical-correction',
																				handler: function(){
																					var form = this.up('form');
																					var grid = subSectionCmp.getComponent('customFieldGrid');
																					var dataRecords = [];
																					var itemValues = customFieldWin.getComponent('form').getComponent('tagToolbar').items.items;
																					
																					var data = form.getValues();
																					
																					var validValues = [];
																					Ext.Array.each(itemValues, function(btn){
																						validValues.push(btn.getText());
																					});
																					data.validValues = validValues.join();
																					
																					dataRecords.push(data);
																					
																					grid.getStore().loadData(dataRecords, true);
																					customFieldWin.close();
																				}
																			},
																			{
																				xtype: 'tbfill'
																			},
																			{
																				text: 'Cancel',
																				iconCls: 'fa fa-lg fa-close icon-button-color-warning',
																				handler: function(){
																					customFieldWin.close();
																				}																				
																			}
																		]
																	}
																]
															}
														]
														
													});
													customFieldWin.show();
												}
											},
											{
												xtype: 'tbfill'
											},
											{
												text: 'Delete',
												itemId: 'delete',
												disabled: true,
												iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
												handler: function() {
													var grid = this.up('grid');
													var selectedRecord = grid.getSelectionModel().getSelection()[0];
													grid.getStore().remove(selectedRecord);
												}												
											}
										]
									}
								],
								columnLines: true,
								store: {									
								},
								listeners: {
									selectionchange: function(selmodel, selection, opts) {
										var grid = subSectionCmp.getComponent('customFieldGrid');
										if (selection.length > 0) {
											grid.getComponent('tools').getComponent('delete').setDisabled(false);
										} else {
											grid.getComponent('tools').getComponent('delete').setDisabled(true);
										}
									}
								},
								columns: [
									{ text: 'Label', dataIndex: 'label', flex: 1 },									
									{ text: 'FieldType', dataIndex: 'fieldType', width: 250 },
									{ text: 'Valid Values', dataIndex: 'validValues', width: 250 }
								]
								
							}

						]

					});
					parentComponent.add(subSectionCmp);
					parentComponent.updateLayout(true, true);
					
					return subSectionCmp;
				};
				
				addEditWindow.show();
				if (record) {
					//pull full detail
					addEditWindow.setLoading(true);
					Ext.Ajax.request({
						url: 'api/v1/resource/contentsectiontemplates/' + record.get('templateId') + '/details',
						callback: function(){
							addEditWindow.setLoading(false);
						},
						success: function(response, opt) {
							var data = Ext.decode(response.responseText);
							
							var model = Ext.create('Ext.data.Model', {								
							});
							model.set({
								templateId: data.contentSectionTemplate.templateId,
								name: data.contentSectionTemplate.name,
								description: data.contentSectionTemplate.description,											
								contentSectionId: data.contentSection.contentSectionId,
								title: data.contentSection.title,
								content: data.contentSection.content,
								privateSection: data.contentSection.privateSection, 
								noContent: data.contentSection.noContent
							});
							
							Ext.Array.each(data.subSections, function(section){								
								var subSection = addSubSection(Ext.getCmp('sectionPanel'));
								
								var subModel = Ext.create('Ext.data.Model', {								
								});
								subModel.set({
									subsectionId: section.subSectionId,
									subsectionTitle: section.title,
									subsectionPrivateSection: section.privateSection,
									subsectionHideTitle: section.hideTitle,
									subsectionNoContent: section.noContent,
									subsectionContent: section.content
								});
								subSection.loadRecord(subModel);
								if (section.title) {
									subSection.setTitle(section.title);
								}
								
								var customFields = [];
								Ext.Array.each(section.customFields, function(field){
									var validValues = [];
									if (field.validValues) {
										Ext.Array.each(field.validValues, function(value) {
											validValues.push(value.value);
										});
									}
								
									customFields.push({
										fieldType: field.fieldType,
										label: field.label,
										validValues: validValues.join()
									});
								});
								subSection.getComponent('customFieldGrid').getStore().loadData(customFields);
							});
							
							addEditWindow.getComponent('templateForm').loadRecord(model);							
						}
						
					});					
					
				}				
			};
		
			var sectionGrid = Ext.create('Ext.grid.Panel', {
				id: 'sectionGrid',
				title: 'Manage Section Templates <i class="fa fa-question-circle" data-qtip="Manage section templates that can be added to evaluations."></i>',
				columnLines: true,
				store: {
					autoLoad: true,
					fields: [
						{ name: 'createDts', 
							type: 'date',
							dateFormat: 'c'							
						},
						{ name: 'updateDts',
							type: 'date',
							dateFormat: 'c'				
						}							
					],
					proxy: {
						type: 'ajax',
						url: 'api/v1/resource/contentsectiontemplates'
					},
					listeners: {
						beforeLoad: function(store, operation, eOpts){
							store.getProxy().extraParams = {
								status: Ext.getCmp('filterActiveStatus').getValue()									
							};
						}
					}					
				},				
				columns: [
					{ text: 'Name', dataIndex: 'name', width: 250 },
					{ text: 'Description', dataIndex: 'description', flex: 1,
						renderer: function(value, metadata, record) {
							return Ext.util.Format.stripTags(value);
						}
					},	
					{ text: 'Create Date', dataIndex: 'createDts', xtype: 'datecolumn', format:'m/d/y H:i:s',  width: 175 },
					{ text: 'Create User', dataIndex: 'createUser', width: 175 },
					{ text: 'Update Date', dataIndex: 'updateDts', xtype: 'datecolumn', format:'m/d/y H:i:s',  width: 175 },
					{ text: 'Update User', dataIndex: 'updateUser', width: 175 }
				],
				listeners: {
					itemdblclick: function(grid, record, item, index, e, opts){
						actionAddEditQuestion(record);
					},						
					selectionchange: function(selModel, selected, opts) {
						var tools = Ext.getCmp('sectionGrid').getComponent('tools');

						if (selected.length > 0) {
							tools.getComponent('edit').setDisabled(false);							
							tools.getComponent('togglestatus').setDisabled(false);
							tools.getComponent('delete').setDisabled(false);
						} else {							
							tools.getComponent('edit').setDisabled(true);							
							tools.getComponent('togglestatus').setDisabled(true);
							tools.getComponent('delete').setDisabled(true);
						}
					}
				},				
				dockedItems: [
					{
						xtype: 'toolbar',
						dock: 'top',
						items: [
							Ext.create('OSF.component.StandardComboBox', {
								id: 'filterActiveStatus',									
								emptyText: 'Active',
								value: 'A',
								fieldLabel: 'Active Status',
								name: 'activeStatus',									
								typeAhead: false,
								editable: false,
								width: 200,							
								listeners: {
									change: function(filter, newValue, oldValue, opts){
										actionRefresh();
									}
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
						dock: 'top',
						xtype: 'toolbar',
						itemId: 'tools',
						items: [
							{
								text: 'Refresh',
								iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
								scale: 'medium',
								width: '110px',
								handler: function(){
									actionRefresh();
								}
							},
							{
								xtype: 'tbseparator',
								requiredPermissions: ['ADMIN-EVALUATION-TEMPLATE-SECTION-CREATE']
							},
							{
								text: 'Add',
								iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction',
								width: '100px',
								scale: 'medium',
								requiredPermissions: ['ADMIN-EVALUATION-TEMPLATE-SECTION-CREATE'],
								handler: function(){
									actionAddEdit();
								}
							},							
							{
								text: 'Edit',
								iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
								itemId: 'edit',
								width: '100px',
								disabled: true,									
								scale: 'medium',
								requiredPermissions: ['ADMIN-EVALUATION-TEMPLATE-SECTION-UPDATE'],
								handler: function(){
									var record = Ext.getCmp('sectionGrid').getSelectionModel().getSelection()[0];
									actionAddEdit(record);
								}
							},
							{
								xtype: 'tbseparator',
								requiredPermissions: ['ADMIN-EVALUATION-TEMPLATE-SECTION-UPDATE']
							},
							{
								text: 'Toggle Status',
								iconCls: 'fa fa-2x fa-power-off icon-button-color-default',
								itemId: 'togglestatus',
								disabled: true,								
								scale: 'medium',
								requiredPermissions: ['ADMIN-EVALUATION-TEMPLATE-SECTION-UPDATE'],
								handler: function(){
									var record = Ext.getCmp('sectionGrid').getSelectionModel().getSelection()[0];
									actionToggleStatus(record);
								}
							},							
							{
								xtype: 'tbfill'
							},							
							{
								text: 'Delete',
								iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
								itemId: 'delete',
								disabled: true,									
								scale: 'medium',
								width: '110px',
								requiredPermissions: ['ADMIN-EVALUATION-TEMPLATE-SECTION-DELETE'],
								handler: function(){
									var record = Ext.getCmp('sectionGrid').getSelectionModel().getSelection()[0];
									actionDelete(record);
								}
							}					
							
						]
					}
				]				
			});
			
			addComponentToMainViewPort(sectionGrid);				
			
			var actionRefresh = function() {
				Ext.getCmp('sectionGrid').getStore().reload();					
			};			
		
			var actionAddEdit = function(record) {				
				showAddEditWindow(record);				
			};
			
			var actionToggleStatus = function(record) {
				Ext.getCmp('sectionGrid').setLoading("Updating Status...");
				var templateId = Ext.getCmp('sectionGrid').getSelection()[0].get('templateId');
				var currentStatus = Ext.getCmp('sectionGrid').getSelection()[0].get('activeStatus');

				var method = 'PUT';
				var urlEnd = '/activate';
				if (currentStatus === 'A') {
					method = 'DELETE';
					urlEnd = '';
				}					
				Ext.Ajax.request({
					url: 'api/v1/resource/contentsectiontemplates/' + templateId + urlEnd,
					method: method,
					callback: function(){
						Ext.getCmp('sectionGrid').setLoading(false);
					},
					success: function(response, opts){						
						actionRefresh();
					}
				});				
			};	
			
			var actionDelete = function(record) {
				
				sectionGrid.setLoading('Checking for references...');
				Ext.Ajax.request({
					url: 'api/v1/resource/contentsectiontemplates/' + record.get('templateId') + '/inuse',
					callback: function(){
						sectionGrid.setLoading(false);
					},
					success: function(response, opts){
						var references = response.responseText;

						if (references && references !== 'false') {
							Ext.Msg.alert('Existing References', 'Unable to delete; Delete evaluation template first.');
						} else {
							Ext.Msg.show({
								title:'Delete Content Template?',
								message: 'Are you sure you want to delete this content section template?',
								buttons: Ext.Msg.YESNO,
								icon: Ext.Msg.QUESTION,
								fn: function(btn) {
									if (btn === 'yes') {
										sectionGrid.setLoading('Deleting...');
										Ext.Ajax.request({
											url: 'api/v1/resource/contentsectiontemplates/' + record.get('templateId') + '?force=true',
											method: 'DELETE',
											callback: function(){
												sectionGrid.setLoading(false);
											},
											success: function(response, opts){
												actionRefresh();
											}
										});	
									} 
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
