<%--
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
--%>
<%@include file="../../../../layout/includes.jsp" %>

<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">

		<stripes:layout-render name="../../../../layout/adminheader.jsp">
		</stripes:layout-render>

		<script src="scripts/component/importWindow.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/messageWindow.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/integrationConfigWindow.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/submissionPanel.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/entryChangeRequestWindow.js?v=${appVersion}" type="text/javascript"></script>

		<form name="exportForm" action="api/v1/resource/components/export" method="POST" >
			<p style="display: none;" id="exportFormIds">
			</p>
		</form>

		<form name="exportFormDescribe" action="api/v1/resource/components/export/describe" method="POST" >
			<p style="display: none;" id="exportFormDescribeIds">
			</p>
		</form>

		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.require('OSF.common.AttributeCodeSelect');
			Ext.require('OSF.form.Attributes');
			Ext.require('OSF.form.Contacts');
			Ext.require('OSF.form.Dependencies');
			Ext.require('OSF.form.Media');			
			Ext.require('OSF.form.Relationships');
			Ext.require('OSF.form.Resources');
			Ext.require('OSF.form.EntryQuestions');
			Ext.require('OSF.form.EntryReviews');
			Ext.require('OSF.form.OldEvaluationSummary');
			Ext.require('OSF.form.Tags');
			Ext.require('OSF.form.Comments');
			Ext.require('OSF.common.ValidHtmlEditor');
			
			Ext.onReady(function() {
				//Add/Edit forms ------>

				//External Windows
				var importWindow = Ext.create('OSF.component.ImportWindow', {
				});

				var integrationWindow = Ext.create('OSF.component.IntegrationWindow', {
					alwaysOnTop: true
				});

				var changeRequestWindow = Ext.create('OSF.component.EntryChangeRequestWindow', {
					adminMode: true,
					successHandler: function() {
						actionRefreshComponentGrid();
					},
					adminEditHandler: function(record, changeRequestWindow) {
						var mainAddEditWin = actionAddEditComponent(record);
						mainAddEditWin.changeRequestCloseHandler = function(){
							changeRequestWindow.changeGrid.getStore().reload();
							mainAddEditWin.un('close', mainAddEditWin.changeRequestCloseHandler);
						};
						mainAddEditWin.on('close', mainAddEditWin.changeRequestCloseHandler);
					}
				});

				var changeHistory = Ext.create('OSF.component.ChangeLogWindow', {
				});

				//Sub Forms
				var handleAttributes = function(componentType, loadingForm) {
										
					loadingForm.setLoading(true);
					Ext.Ajax.request({
						url: 'api/v1/resource/attributes/required?componentType=' + componentType,
						callback: function() {
							loadingForm.setLoading(false);
						},
						success: function(response, opt) {
							var requiredStore = Ext.data.StoreManager.lookup('requiredAttributeStore');
							var data = Ext.decode(response.responseText);
							
							Ext.Array.sort(data, function(a, b) {
								return a.description.localeCompare(b.description);								
							});
							
							requiredStore.loadData(data);
						}
					});					
				};

				var allComponentTypes = [];

				Ext.Ajax.request({
					url: 'api/v1/resource/componenttypes',
					success: function(response, opts) {
						allComponentTypes = Ext.decode(response.responseText);
					}
				});

				var createAddEditWin = function () {

					var requiredAttributeStore = Ext.create('Ext.data.Store', {
						storeId: 'requiredAttributeStore',
						autoLoad: false,
						listeners: {
							datachanged: function(store) {
								var panel = generalForm.getComponent('requiredAttributePanel');
								panel.removeAll();

								store.each(function(record) {
									
									var field = Ext.create('OSF.common.AttributeCodeSelect', {
											fieldConfig: {	
												record: record,
												listeners: {
													change: function(fieldLocal, newValue, oldValue, opts) {
														var recordLocal = fieldLocal.record;
														if (recordLocal) {
															recordLocal.set('attributeCode', newValue);
														}
													}
												}
											},
											attributeTypeView: record.data,											
											record: record
									});																				
									record.formField = field;
									panel.add(field);
								});
								panel.updateLayout(true, true);
								panel.fireEvent('ready');
							},
							update: function(store, record, operation, modifiedFieldNames, details, opts) {
								if (record.formField) {
									record.formField.setValue(record.get('attributeCode'));
								}
							}
						}
					});

					var generalForm = Ext.create('Ext.form.Panel', {
						title: 'General',
						tooltip: 'General and required information on an entry.',
						autoScroll: true,
						dockedItems: [
							{
								xtype: 'toolbar',
								dock: 'top',
								items: [
									{
										xtype: 'tbfill'
									},
									{
										text: 'Change History',
										itemId: 'changeHistoryBtn',
										iconCls: 'fa fa-lg fa-history icon-button-color-default icon-top-padding',
										handler: function() {
											changeHistory.show();
											changeHistory.load({
												entity: 'Component',
												entityId: generalForm.componentRecord.get('componentId'),
												includeChildren: true
											});
										}
									},
									{
										xtype: 'tbseparator',
										requiredPermissions: ['ADMIN-MESSAGE-MANAGEMENT-CREATE']
									},
									{
										text: 'Message Submitter',
										iconCls: 'fa fa-lg fa-envelope-o icon-button-color-default',
										requiredPermissions: ['ADMIN-MESSAGE-MANAGEMENT-CREATE'],
										handler: function() {

											//get submitter
											var emails = '';

											if (generalForm.componentRecord) {
												Ext.toast('', 'Loading Contact Please Wait...', 'br');
												Ext.Ajax.request({
													url: 'api/v1/resource/components/' + generalForm.componentRecord.get('componentId') + '/contacts/view',
													success: function(response, opt){
														var contacts = Ext.decode(response.responseText);

														Ext.Array.each(contacts, function(contact) {
															if (contact.contactType === 'SUB') {
																if (contact.email) {
																	emails += contact.email + '; ';
																}
															}
														});

														var messageWindow = Ext.create('OSF.component.MessageWindow', {
															closeAction: 'destroy',
															alwaysOnTop: true,
															initialToUsers: emails
														}).show();
													}

												});
											} else {
												var messageWindow = Ext.create('OSF.component.MessageWindow', {
														closeAction: 'destroy',
														alwaysOnTop: true,
														initialToUsers: emails
													}).show();
											}

										}
									},
									{
										xtype: 'tbseparator'
									},
									{
										text: 'Integration',
										itemId: 'integrationBtn',
										iconCls: 'fa fa-lg fa-gear icon-button-color-default icon-top-padding',
										disabled: true,
										handler: function() {
											integrationWindow.show();
											integrationWindow.loadConfigs(generalForm.componentRecord.get('componentId'));
										}
									}
								]
							},
							{
								xtype: 'toolbar',
								dock: 'bottom',
								items: [
									{
										text: 'Save',
										tooltip: 'Save General information and continue.',
										iconCls: 'fa fa-lg fa-save icon-button-color-save',
										formBind: true,
										handler: function() {

											var form = this.up('form');
											var data = form.getValues();
											var componentId = '';
											var method = 'POST';
											var update = '';
											var edit = false;
											if (generalForm.componentRecord){
												componentId = generalForm.componentRecord.get('componentId');
												update = '/' + componentId;
												method = 'PUT';
												edit = true;
											}

											var requireComponent = {
												component: data,
												attributes: []
											};

											Ext.data.StoreManager.lookup('requiredAttributeStore').each(function(record){
												
												if (Ext.isArray(record.get('attributeCode'))) {
													Ext.Array.each(record.get('attributeCode'), function(code) {
														requireComponent.attributes.push({
															componentAttributePk: {
																attributeType: record.get('attributeType'),
																attributeCode: code
															}
														});
													});
												} else {
													requireComponent.attributes.push({
														componentAttributePk: {
															attributeType: record.get('attributeType'),
															attributeCode: record.get('attributeCode')
														}
													});
												}												
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
													Ext.Msg.show({
														title:'Validation Check',
														message: 'Missing Required Attributes; Check input.',
														buttons: Ext.Msg.OK,
														icon: Ext.Msg.ERROR,
														fn: function(btn) {
														}
													});

												} else {
													CoreUtil.removeBlankDataItem(requireComponent.component);
													CoreUtil.submitForm({
														url: 'api/v1/resource/components' + update,
														method: method,
														data: requireComponent,
														removeBlankDataItems: true,
														form: form,
														success: function(response, opt){

															// Loading Mask Was Removed Once 'Success' Was Reached
															// Re-Add Loading Mask Until Store Is Updated
															form.setLoading('Saving...');

															Ext.toast('Successfully Saved Record');
															var data = Ext.decode(response.responseText);

															// Store Component ID
															var componentId;
															if (data.component) {
																componentId = data.component.componentId;
															} else {
																componentId = data.componentId;
															}

															// Store Component Store
															var store = Ext.getCmp('componentGrid').getStore();

															// Reload Store
															store.reload({

																// Create Function To Fire When Reload Is Complete
																callback: function() {

																	// Store (Updated) Record With Matching Component ID
																	var record = null;
																	store.each(function(storeRecord) {
																		if (storeRecord.get('componentId') === componentId) {
																			record = storeRecord;
																		}
																	});

																	if (record === null) {
																		//means this is a change request
																		record = Ext.create('Ext.data.Model', {
																		});
																		record.set(data);
																	}

																	// Remove Loading Mask
																	form.setLoading(false);
																	mainAddEditWin.close();

																	// Refresh Add/Edit Window
																	actionAddEditComponent(record);

																}
															});
														},
														failure: function(response, opt){

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
										text: 'Close',
										tooltip: 'Close Add/Edit window',
										iconCls: 'fa fa-lg fa-times icon-button-color-warning',
										handler: function() {
											this.up('window').close();
										}
									}
								]
							}
						],
						items: [
							{
								xtype: 'panel',
								title: 'Information',
								layout: 'hbox',
								collapsible: true,
								titleCollapse: true,
								border: true,
								bodyStyle: 'padding: 10px;',
								items: [
									{
										xtype: 'panel',
										width: '50%',
										defaults: {
											labelWidth: 150,
											labelStyle: 'font-weight: bold;',
											width: '80%',
											xtype: 'displayfield',
											readOnly: true
										},
										items: [
											{
												xtype: 'displayfield',
												name: 'lastActivityDts',
												fieldLabel: 'Last Activity Date',
												renderer: function(value, field){
													return Ext.util.Format.date(value, 'm/d/y H:i:s');
												}
											},
											{
												name: 'submittedDts',
												fieldLabel: 'User Submitted Date',
												renderer: function(value, field){
													return Ext.util.Format.date(value, 'm/d/y H:i:s');
												}
											},
											{
												name: 'lastModificationType',
												fieldLabel: 'Last Entry Method'
											},
											{
												name: 'approvedDts',
												fieldLabel: 'Approval Date',
												renderer: function(value, field){
													return Ext.util.Format.date(value, 'm/d/y H:i:s');
												}
											},
											{
												name: 'approvedUser',
												fieldLabel: 'Approval User'
											}
										]
									},
									{
										xtype: 'panel',
										width: '50%',
										defaults: {
											labelWidth: 150,
											labelStyle: 'font-weight: bold;',
											width: '80%',
											xtype: 'displayfield',
											readOnly: true
										},
										items: [
											{
												name: 'externalId',
												fieldLabel: 'External Id'
											},
											{
												name: 'recordVersion',
												fieldLabel: 'Record Version'
											},
											{
												name: 'createDts',
												fieldLabel: 'Create Date',
												renderer: function(value, field){
													return Ext.util.Format.date(value, 'm/d/y H:i:s');
												}
											},
											{
												name: 'createUser',
												fieldLabel: 'Create User'
											},
											{
												name: 'updateUser',
												fieldLabel: 'Update User'
											}
										]
									}
								]
							},
							{
								xtype: 'panel',
								bodyStyle: 'padding: 10px;',
								layout: 'vbox',
								defaults: {
									labelAlign: 'top',
									labelSeparator: '',
									width: '100%'
								},
								items: [
									Ext.create('OSF.component.StandardComboBox', {
										itemId: 'componentTypeMainCB',
										fieldLabel: 'Entry Type <span class="field-required" />',
										name: 'componentType',
										allowBlank: false,
										margin: '0 0 0 0',
										width: '100%',
										editable: false,
										typeAhead: false,
										requiredPermissions: ['ADMIN-ENTRY-CHANGETYPE'],
										beforePermissionsCheckFailure: function () {
											if (mainAddEditWin.generalForm.componentRecord) {
												return true;
											}
											this.show();
											return false;
										},
										storeConfig: {
											url: 'api/v1/resource/componenttypes/lookup'
										},
										listeners: {
											change: function(field, newValue, oldValue, opts) {
												handleAttributes(newValue, generalForm);
											}
										}
									}),
									{
										xtype: 'textfield',
										name: 'name',
										fieldLabel: 'Name <span class="field-required" />',
										allowBlank: false,
										maxLength: 255
									},
									{
										xtype: 'panel',
										html: '<b>Description</b> <span class="field-required" />'
									},
									{
										xtype: 'tinymce_textarea',
										fieldStyle: 'font-family: Courier New; font-size: 12px;',
										style: { border: '0' },
										name: 'description',
										width: '100%',
										height: 300,
										maxLength: 65536,
										tinyMCEConfig: Ext.apply(CoreUtil.tinymceSearchEntryConfig("osfmediaretriever"), {
											mediaSelectionUrl: function(){
												if (generalForm.componentRecord) {
													var componentId = generalForm.componentRecord.get('componentId');
													return 'api/v1/resource/components/' + componentId + '/media/view';
												} else {
													return 'api/v1/resource/components/NEW/media/view';
												}
											}
										})
									},
									Ext.create('OSF.component.StandardComboBox', {
										name: 'organization',
										allowBlank: false,
										margin: '0 0 0 0',
										width: '100%',
										fieldLabel: 'Organization <span class="field-required" />',
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
									Ext.create('OSF.component.StandardComboBox', {
										fieldLabel: 'Approval Status <span class="field-required" />',
										name: 'approvalState',
										allowBlank: false,
										margin: '0 0 0 0',
										width: '100%',
										editable: false,
										typeAhead: false,
										requiredPermissions: ['ADMIN-ENTRY-APPROVE'],
										preventDefaultAction: true,
										beforePermissionsCheckFailure: function () {
											if (mainAddEditWin.generalForm.componentRecord) {
												return true;
											}
											return false;
										},
										storeConfig: {
											url: 'api/v1/resource/lookuptypes/ApprovalStatus'
										}
									}),
									{
										xtype: 'datefield',
										fieldLabel: 'Release Date',
										name: 'releaseDate',
										submitFormat: 'Y-m-d\\TH:i:s.u'
									},
									{
										xtype: 'textfield',
										fieldLabel: 'Version',
										name: 'version'
									},
									{
										xtype: 'textfield',
										fieldLabel: 'GUID',
										name: 'guid'
									},
									Ext.create('OSF.component.DataSensitivityComboBox', {
										width: '100%',
										listeners: {
											ready: function(combo) {
												if (generalForm.componentRecord) {
													combo.setValue(generalForm.componentRecord.get('dataSensitivity'));
												}
											}
										}
									}),
									Ext.create('OSF.component.DataSourceComboBox', {
										name: 'dataSource',
										hideOnNoData: true,
										width: '100%',
										listeners: {
											ready: function(combo) {
												if (generalForm.componentRecord) {
													combo.setValue(generalForm.componentRecord.get('dataSource'));
												}
											}
										}
									}),
									Ext.create('OSF.component.SecurityComboBox', {
									})
								]
							},
							{
								xtype: 'panel',
								itemId: 'requiredAttributePanel',
								title: 'Required Attributes',
								frame: true,
								bodyStyle: 'padding: 10px;',
								margin: '20 0 20 0'
							}
						]
					});

					generalForm.loadComponentAttributes = function() {
						if (generalForm.componentRecord) {
							var componentId = generalForm.componentRecord.get('componentId');
							Ext.Ajax.request({
								url: 'api/v1/resource/components/' + componentId + '/attributes/view',
								method: 'GET',
								success: function(response, opts) {
									var data = Ext.decode(response.responseText);
									var requiredStore = Ext.data.StoreManager.lookup('requiredAttributeStore');

									var attributeTypeToValue = {										
									};
									Ext.Array.each(data, function(attribute) {										
										if (attributeTypeToValue[attribute.type]) {
											attributeTypeToValue[attribute.type].push(attribute.code);
										} else {
											var values = [];
											values.push(attribute.code);
											attributeTypeToValue[attribute.type] = values;
										}
									});

									Ext.Array.each(data, function(attribute) {
										var required = false;
										
										if (attribute.requiredRestrictions) {
											Ext.Array.each(attribute.requiredRestrictions, function(restriction) {
												if (restriction.componentType === generalForm.componentRecord.get('componentType')) {
													required = true;
												}
											});
										}
										
										if (required) {

											//group values of same type
											var value = attribute.code;
											if (attributeTypeToValue[attribute.type] && 
												attributeTypeToValue[attribute.type].length > 1) {
												value = attributeTypeToValue[attribute.type];
											}
											
											requiredStore.each(function(record){
												if (record.get('attributeType') === attribute.type) {
													record.set('attributeCode', value, { dirty: false });
													record.formField.setValue(value);
												}
											});

										}
									});

								}
							});
						} else {
							var requiredStore = Ext.data.StoreManager.lookup('requiredAttributeStore');
							requiredStore.each(function(record) {
								record.set('attributeCode', null, { dirty: false} );
							});
						}
					};

					var mainAddEditWin = Ext.create('Ext.window.Window', {
						title: 'Entry Form',
						modal: true,
						maximizable: true,
						layout: 'fit',
						width: '80%',
						height: '90%',
						iconCls: 'fa fa-lg fa-edit',
						closeAction: 'destroy',
						generalForm: generalForm,
						listeners:
						{
							show: function(){
								this.removeCls("x-unselectable");
							}
						},
						items: [
							{
								xtype: 'tabpanel',
								itemId: 'tabpanel',
								listeners: {
									add: function (tabPanel, item) {

										// defer to push the check for valid permissions back on the stack
										Ext.defer(function () {
											if (!item.hasValidPermissions) {
												
												// check to see which tab to hide
												var tabToHide = tabPanel.getTabBar().getRefItems().reduce(function (acc, tab) {
													if (tab.title === item.title) {
														acc = tab;
													}
													return acc;
												}, null);
												tabToHide.hide();
											}
										}, 1);
										return true;
									}	
								},
								items: [
									generalForm
								]
							}
						]
					});

					return mainAddEditWin;
				};

				//MAIN GRID -------------->
				var versionViewTemplate = new Ext.XTemplate();

				Ext.Ajax.request({
					url: 'Router.action?page=shared/entrySimpleViewTemplate.jsp',
					success: function(response, opts){
						versionViewTemplate.set(response.responseText, true);
					}
				});

				var versionWinRestorePrompt = Ext.create('Ext.window.Window', {
					modal: true,
					width: 300,
					bodyStyle: 'padding: 10px;',
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [
								{
									text: 'Restore',
									iconCls: 'fa fa-lg fa-reply',
									handler: function(){
										this.up('window').hide();

										var componentId = Ext.getCmp('componentGrid').getSelection()[0].get('componentId');
										var versionHistoryId = Ext.getCmp('versionGrid').getSelection()[0].get('versionHistoryId');
										var options = this.up('window').down('form').getValues();

										//load
										Ext.getCmp('versionWin').setLoading("Restoring version...");
										Ext.Ajax.request({
											url: 'api/v1/resource/components/' + componentId + '/versionhistory/' + versionHistoryId + '/restore',
											method: 'PUT',
											jsonData: options,
											success: function(response, opts){
												Ext.getCmp('versionWin').setLoading(false);
												Ext.toast('Restored Version Successfully.');
												versionLoadCurrent();
												Ext.getCmp('versionGrid').getSelectionModel().deselectAll();
											},
											failure: function(response, opts) {
												Ext.getCmp('versionWin').setLoading(false);
											}
										});
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Cancel',
									iconCls: 'fa fa-lg fa-close',
									handler: function(){
										this.up('window').hide();
									}
								}
							]
						}
					],
					items: [
						{
							xtype: 'panel',
							html: 'Select Restore Options:'
						},
						{
							xtype: 'form',
							items: [
								{
									xtype: 'checkbox',
									boxLabel: 'Reviews',
									name: 'restoreReviews'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Questions',
									name: 'restoreQuestions'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Tags',
									name: 'restoreTags'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Integration',
									name: 'restoreIntegration'
								}
							]
						}
					],
					title:'Restore Version?',
					message: 'Select Restore Options'
				});

				var versionWin = Ext.create('Ext.window.Window', {
					id: 'versionWin',
					title: 'Versions',
					maximizable: true,
					width: '80%',
					height: '80%',
					modal: true,
					layout: {
						type: 'hbox',
						align: 'stretch'
					},
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [
								{
									text: 'Restore',
									id: 'versionWin-tool-restoreBtn',
									iconCls: 'fa fa-lg fa-reply icon-button-color-refresh',
									disabled: true,
									requiredPermissions: ['ADMIN-ENTRY-VERSION-RESTORE'],
									handler: function(){
										//prompt
										versionWinRestorePrompt.show();
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Close',
									iconCls: 'fa fa-lg fa-close icon-button-color-warning',
									handler: function(){
										this.up('window').hide();
									}
								}
							]
						}
					],
					items: [
						{
							xtype: 'grid',
							id: 'versionGrid',
							region: 'west',
							split: true,
							border: true,
							columnLines: true,
							width: '33%',
							store: Ext.create('Ext.data.Store', {
								autoLoad: false,
								fields: [
									  "createUser",
									  "createDts",
									{
										name: 'createDts',
										type:	'date',
										dateFormat: 'c'
									},
									{
										name: 'updateDts',
										type:	'date',
										dateFormat: 'c'
									},
									  "updateUser",
									  "adminModified",
									  "componentId",
									  "versionHistoryId",
									  "version",
									  "fileHistoryId"
								],
								proxy: {
									url: '',
									type: 'ajax'
								}
							}),
							columns: [
								{ text: 'Version', dataIndex: 'version', width: 150 },
								{ text: 'Archive User', dataIndex: 'createUser', width: 150 },
								{ text: 'Archive Date', dataIndex: 'createDts', flex: 1, xtype: 'datecolumn', format:'m/d/y H:i:s' }
							],
							listeners: {
								selectionchange: function(grid, record, index, opts){
									if (grid.getSelected().length === 1) {
										Ext.getCmp('versionWin-tool-restoreBtn').setDisabled(false);
										Ext.getCmp('versionWin-tool-removeBtn').setDisabled(false);
									} else {
										Ext.getCmp('versionWin-tool-restoreBtn').setDisabled(true);
										Ext.getCmp('versionWin-tool-removeBtn').setDisabled(true);
									}

									//load preview
									if (record && record[0]){
										var componentId = Ext.getCmp('componentGrid').getSelection()[0].get('componentId');
										Ext.Ajax.request({
											url: 'api/v1/resource/components/' + componentId + '/versionhistory/' + record[0].get('versionHistoryId') + '/view',
											success: function(response, opts) {
												var data = Ext.decode(response.responseText);
												Ext.getCmp('versionWin-snapshotVersionPanel').update(data);
												Ext.getCmp('versionWin-snapshotVersionPanel').setTitle('Selected Version - ' + record[0].get('version'));
											}
										});
									} else {
										Ext.getCmp('versionWin-snapshotVersionPanel').update(undefined);
										Ext.getCmp('versionWin-snapshotVersionPanel').setTitle('Selected Version');
									}
								}
							},
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'top',
									items: [
										{
											text: 'Create Snapshot',
											iconCls: 'fa fa-lg fa-plus icon-button-color-save icon-small-vertical-correction',
											tooltip: 'Creates snapshot of the current verison',
											requiredPermissions: ['ADMIN-ENTRY-VERSION-CREATE'],
											handler: function(){
												var versionWin = this.up('window');
												versionWin.setLoading("Snapshoting current version...");
												var componentId = Ext.getCmp('componentGrid').getSelection()[0].get('componentId');
												Ext.Ajax.request({
													url: 'api/v1/resource/components/' + componentId + '/versionhistory',
													method: 'POST',
													success: function(response, opts) {
														versionWin.setLoading(false);
														Ext.getCmp('versionGrid').getStore().reload();
													},
													failure: function(response, opts) {
														versionWin.setLoading(false);
													}
												});
											}
										},
										{
											xtype: 'tbfill'
										},
										{
											text: 'Delete',
											id: 'versionWin-tool-removeBtn',
											iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
											disabled: true,
											requiredPermissions: ['ADMIN-ENTRY-VERSION-DELETE'],
											handler: function(){
												var componentId = Ext.getCmp('componentGrid').getSelection()[0].get('componentId');
												var versionHistoryId = Ext.getCmp('versionGrid').getSelection()[0].get('versionHistoryId');
												var version = Ext.getCmp('versionGrid').getSelection()[0].get('version');
												var versionWin = this.up('window');

												Ext.Msg.show({
													title: 'Delete Version?',
													message: 'Are you sure you want to delete version:  ' + version +' ?',
													buttons: Ext.Msg.YESNO,
													icon: Ext.Msg.QUESTION,
													fn: function(btn) {
														if (btn === 'yes') {
															versionWin.setLoading('Deleting version...');
															Ext.Ajax.request({
																url: 'api/v1/resource/components/' + componentId + '/versionhistory/' + versionHistoryId,
																method: 'DELETE',
																success: function(response, opts) {
																	versionWin.setLoading(false);
																	Ext.getCmp('versionGrid').getStore().reload();
																},
																failure: function(response, opts) {
																	versionWin.setLoading(false);
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
						},
						{
							xtype: 'panel',
							id: 'versionWin-snapshotVersionPanel',
							region: 'east',
							width: '33%',
							title: 'Selected Version',
							autoScroll: true,
							scrollable: true,
							border: true,
							split: true,
							bodyStyle: 'padding: 10px;',
							tpl: versionViewTemplate
						},
						{
							xtype: 'panel',
							id: 'versionWin-currentVersionPanel',
							region: 'center',
							flex: 1,
							title: 'Current Version',
							header: {
								cls: 'accent-background'
							},
							autoScroll: true,
							border: true,
							split: true,
							bodyStyle: 'padding: 10px;',
							tpl: versionViewTemplate
						}
					]
				});

				var previewContents = Ext.create('OSF.ux.IFrame', {
					src: ''
				});

				var previewComponentWin = Ext.create('Ext.window.Window', {
					width: '70%',
					height: '80%',
					maximizable: true,
					title: 'Preview',
					iconCls: 'fa fa-lg fa-eye',
					modal: true,
					layout: 'fit',
					items: [
						previewContents
					],
					tools: [
						{
							type: 'up',
							tooltip: 'popout preview',
							handler: function(){
								window.open('view.jsp?fullPage=true&id=' + Ext.getCmp('componentGrid').getSelection()[0].get('componentId'), "Preview");
							}
						}
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [
								{
									text: 'Previous',
									id: 'previewWinTools-previousBtn',
									iconCls: 'fa fa-lg fa-arrow-left icon-button-color-default',
									handler: function() {
										actionPreviewNextRecord(false);
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Close',
									iconCls: 'fa fa-lg fa-close icon-button-color-warning',
									handler: function() {
										this.up('window').hide();
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Next',
									id: 'previewWinTools-nextBtn',
									iconCls: 'fa fa-lg fa-arrow-right icon-button-color-default',
									iconAlign: 'right',
									handler: function() {
										actionPreviewNextRecord(true);
									}
								}
							]
						}
					]
				});

				var actionPreviewNextRecord = function(next) {
					if (next) {
						Ext.getCmp('componentGrid').getSelectionModel().selectNext();
					} else {
						Ext.getCmp('componentGrid').getSelectionModel().selectPrevious();
					}
					actionPreviewComponent(Ext.getCmp('componentGrid').getSelection()[0].get('componentId'));
				};

				var previewCheckButtons = function() {
					if (Ext.getCmp('componentGrid').getSelectionModel().hasPrevious()) {
						Ext.getCmp('previewWinTools-previousBtn').setDisabled(false);
					} else {
						Ext.getCmp('previewWinTools-previousBtn').setDisabled(true);
					}

					if (Ext.getCmp('componentGrid').getSelectionModel().hasNext()) {
						Ext.getCmp('previewWinTools-nextBtn').setDisabled(false);
					} else {
						Ext.getCmp('previewWinTools-nextBtn').setDisabled(true);
					}
				};
				
				var showMultiApproveForm = function(componentId, componentName) {
					
					var approveWin = Ext.create('Ext.window.Window', {
						title: 'Approve Related Entries',
						layout: 'fit',
						modal: true,
						width: '50%',
						height: '50%',
						closeMode: 'destroy',
						items: [
							{
								xtype: 'grid',
								store: {
									autoLoad: true,
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/components/' + componentId + '/relationships?pullAllChildren=true'
									}
								},
								selModel: {
									selType: 'checkboxmodel'
								},
								columnLines: true,
								columns: [
									{ text: 'Origin Entry', dataIndex: 'ownerComponentName', width: 175, hidden: true },
									{ text: 'Relationship', dataIndex: 'relationshipTypeDescription', width: 175 },
									{ text: 'Related Entry', dataIndex: 'targetComponentName', flex: 1, minWidth: 150 },									
									{ text: 'Approved', dataIndex: 'targetApproved', align: 'center', 
										renderer: CoreUtil.renderer.booleanRenderer
									},
									{
										xtype: 'actioncolumn',
										width:50,
										items: [
											{
												iconCls: 'x-fa fa-eye',
												tooltip: 'view',
												handler: function(grid, rowIndex, colIndex) {
													var rec = grid.getStore().getAt(rowIndex);
													actionPreviewComponent(rec.get('targetComponentId'));
												}
 											}
										]
										
									}
								],
								dockedItems: [
									{
										xtype: 'panel',
										dock: 'top',
										margin: '5 5 5 5',
										html: 'Approve: <b>' + componentName + '</b> and all selected entries.'
									},
									{
										xtype: 'toolbar',
										dock: 'bottom',
										items: [
											{
												text: 'Approve',
												scale: 'medium',
												iconCls: 'fa fa-2x fa-check-square-o icon-button-color-save icon-vertical-correction',
												handler: function() {
													var grid = this.up('grid');
													
													var idsToApprove = [];
													idsToApprove.push(componentId);
													var records = grid.getSelection();
													Ext.Array.each(records, function(item){
														idsToApprove.push(item.get('targetComponentId'));
													});
													
													approveWin.setLoading('Approving Entries');												
													
													Ext.Ajax.request({
														url: 'api/v1/resource/components/approve',
														method: 'PUT',
														jsonData: idsToApprove,
														callback: function() {
															approveWin.setLoading(false);
														},
														success: function(response, opts) {
															actionRefreshComponentGrid(true);
															approveWin.close();
														}
													});
													
												}
											},
											{
												xtype: 'tbfill'
											},
											{
												text: 'Close',
												scale: 'medium',
												iconCls: 'fa fa-2x fa-close icon-button-color-warning',
												handler: function() {
													approveWin.close();
												}
											}
										]
									}
								]
							}
						]						
					});
		
					approveWin.show();					
				};
				
				var toggleWin = Ext.create('Ext.window.Window',{
					id: 'toggleWin',
					title: 'Toggle ',
					iconCls: 'fa fa-lg fa-exchange',
					width: '50%',
					height: 350,
					y: 200,
					modal: true,
					layout: 'fit',
					items: [
						{
							xtype: 'form',
							itemId: 'toggleForm',
							bodyStyle: 'padding: 10px',
							items: [
								{
									xtype: 'osf-common-validhtmleditor',
									itemId: 'searchComment',
									fieldLabel: 'Optional Comments',
									labelAlign: 'top',
									name: 'Comment name',
									width: '100%',
									displayField: 'Comment displayfield',
									store: {
										autoLoad: true
									}
								},
								{
									xtype: 'hidden',
									itemId: 'searchCommentId',
									name: 'commentId'
								}
							],
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										{
											text: 'Toggle',
											formBind: true,
											iconCls: 'fa fa-lg fa-power-off icon-small-vertical-correction icon-button-color-default',
											handler: function(){
												var form = this.up('form');
												Ext.getCmp('componentGrid').setLoading('Toggling the selected entries...');
												var componentIds = Ext.getCmp('componentGrid').getSelection().map(function (item) {
													return item.getData().componentId;
												});
												var data = {
													componentIds: componentIds,
													comment: {
														commentType: 'ADMIN',
														comment: form.queryById('searchComment').getValue()
													}
												};
												if(data.comment.comment === ''){
													data.comment = null;
												}
												Ext.Ajax.request({
													url: 'api/v1/resource/components/togglestatus',
													method: 'PUT',
													jsonData: data,
													callback: function(){
														Ext.getCmp('componentGrid').setLoading(false);
													},
													success: function(response, opts) {
														if (response.responseText !== '') {
															if( response.responseText.indexOf('errors') !== -1) {
															// provide error notification
																Ext.toast({
																	title: 'validation error. the server could not process the request. ',
																	html: 'try changing the comment field. the comment field cannot be empty and must have a size smaller than 4096.',
																	width: 550,
																	autoclosedelay: 10000
																});
															}
														}
														actionRefreshComponentGrid();
														form.reset();
													},
													failure: function(){
														Ext.toast({
															title: 'validation error. the server could not process the request. ',
															html: 'try changing the comment field. the comment field cannot be empty and must have a size smaller than 4096.',
															width: 550,
															autoclosedelay: 10000
														});
													}
												});
												this.up('window').close();
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
				});

				var changeTypeWin = Ext.create('Ext.window.Window', {
					id: 'changeTypeWin',
					title: 'Change Type - ',
					iconCls: 'fa fa-lg fa-exchange',
						width: '50%',
					height: 450,
					y: 200,
					modal: true,
					layout: 'fit',
					items: [
						{
							xtype: 'form',
							itemId: 'changeTypeForm',
							bodyStyle: 'padding: 10px',
							items: [
								{
									xtype: 'combobox',
									fieldLabel: 'Type <span class="field-required" />',
									labelAlign: 'top',
									labelSeparator: '',
									typeAhead: true,
									editable: true,
									allowBlank: false,
									name: 'componentType',
									width: '100%',
									valueField: 'code',
									queryMode: 'local',
									displayField: 'description',
									store: {
										autoLoad: true,
										proxy: {
											type: 'ajax',
											url: 'api/v1/resource/componenttypes/lookup'
										}
									}
								},
								{
									xtype: 'osf-common-validhtmleditor',
									itemId: 'searchComment',
									fieldLabel: 'Optional Comments',
									labelAlign: 'top',
									name: 'Comment name',
									width: '100%',
									displayField: 'Comment displayfield',
									store: {
										autoLoad: true
									}
								},
								{
									xtype: 'hidden',
									itemId: 'searchCommentId',
									name: 'commentId'
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
											handler: function(){
												Ext.getCmp('componentGrid').setLoading('Changing the type for the selected entries...');
												var componentIds = Ext.getCmp('componentGrid').getSelection().map(function (item) {
													return item.getData().componentId;
												});
												var componentType = this.up('form').getForm().findField('componentType').getValue();
												var form = this.up('form');
												var data = {
													componentIds: componentIds,
													comment: {
														commentType: 'ADMIN',
														comment: form.queryById('searchComment').getValue()

													},
													newType: componentType
												};
												if(data.comment.comment === ''){
													data.comment = null;
												}
												Ext.Ajax.request({
													url: 'api/v1/resource/components/changetype',
													method: 'PUT',
													jsonData: data,
													callback: function(){
														Ext.getCmp('componentGrid').setLoading(false);
													},
													success: function(response, opts) {
														if (response.responseText !== '') {
															if( response.responseText.indexOf('errors') !== -1) {
															// provide error notification
																Ext.toast({
																	title: 'validation error. the server could not process the request. ',
																	html: 'try changing the comment field. the comment field cannot be empty and must have a size smaller than 4096.',
																	width: 550,
																	autoclosedelay: 10000
																});
															}
														}
														form.reset();
														actionRefreshComponentGrid();
													},
													failure: function(){
														Ext.toast({
															title: 'validation error. the server could not process the request. ',
															html: 'try changing the comment field. the comment field cannot be empty and must have a size smaller than 4096.',
															width: 550,
															autoclosedelay: 10000
														});
													}
												});
												this.up('window').close();
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
				});

				var maingridStore = Ext.create('Ext.data.Store', {
					autoLoad: true,
					pageSize: 300,
					remoteSort: true,
					sorters: [
						new Ext.util.Sorter({
							property : 'name',
							direction: 'ASC'
						})
					],
					fields:[
						{name: 'name', mapping: function(data){
							return data.component.name;
						}},
						{name: 'description', mapping: function(data){
							return data.component.description;
						}},
						{name: 'activeStatus', mapping: function(data){
							return data.component.activeStatus;
						}},
						{name: 'approvalState', mapping: function(data){
							return data.component.approvalState;
						}},
						{name: 'approvalStateLabel', mapping: function(data){
							return data.component.approvalStateLabel;
						}},
						{name: 'approvedDts',
							type:	'date',
							dateFormat: 'c',
							mapping: function(data){
							return data.component.approvedDts;
						}},
						{name: 'approvedUser', mapping: function(data){
							return data.component.approvedUser;
						}},
						{name: 'componentId', mapping: function(data){
							return data.component.componentId;
						}},
						{name: 'componentType', mapping: function(data){
							return data.component.componentType;
						}},
						{name: 'componentTypeLabel', mapping: function(data){
							return data.component.componentTypeLabel;
						}},
						{name: 'organization', mapping: function(data){
							return data.component.organization;
						}},
						{name: 'createDts',
							type:	'date',
							dateFormat: 'c',
							mapping: function(data){
							return data.component.createDts;
						}},
						{name: 'createUser', mapping: function(data){
							return data.component.createUser;
						}},
						{name: 'lastActivityDts',
							type:	'date',
							dateFormat: 'c',
							mapping: function(data){
							return data.component.lastActivityDts;
						}},
						{name: 'guid', mapping: function(data){
							return data.component.guid;
						}},
						{name: 'externalId', mapping: function(data){
							return data.component.externalId;
						}},
						{name: 'changeApprovalMode', mapping: function(data){
							return data.component.changeApprovalMode;
						}},
						{name: 'notifyOfApprovalEmail', mapping: function(data){
							return data.component.notifyOfApprovalEmail;
						}},					
						{name: 'currentDataOwner', mapping: function(data){
							return data.component.currentDataOwner;
						}},					
						{name: 'dataSource', mapping: function(data){
							return data.component.dataSource;
						}},
						{name: 'dataSensitivity', mapping: function(data){
							return data.component.dataSensitivity;
						}},
						{name: 'securityMarkingType', mapping: function(data){
							return data.component.securityMarkingType;
						}},
						{name: 'securityMarkingDescription', mapping: function(data){
							return data.component.securityMarkingDescription;
						}},
						{name: 'lastModificationType', mapping: function(data){
							return data.component.lastModificationType;
						}},
						{name: 'fileHistoryId', mapping: function(data){
							return data.component.fileHistoryId;
						}},
						{name: 'recordVersion', mapping: function(data){
							return data.component.recordVersion;
						}},
						{name: 'submittedDts',
							type:	'date',
							dateFormat: 'c',
							mapping: function(data){
							return data.component.submittedDts;
						}},
						{name: 'updateDts',
							type:	'date',
							dateFormat: 'c',
							mapping: function(data){
							return data.component.updateDts;
						}},
						{name: 'updateUser', mapping: function(data){
							return data.component.updateUser;
						}},
						{name: 'numberOfPendingChanges', mapping: function(data){
							return data.component.numberOfPendingChanges;
						}},
						'integrationManagement'
					],
					proxy: CoreUtil.pagingProxy({
						url: 'api/v1/resource/components/filterable',
						extraParams: {
							status: 'ALL',
							approvalState: 'ALL',
							componentType: 'ALL'
						},
						reader: {
						   type: 'json',
						   rootProperty: 'components',
						   totalProperty: 'totalNumber'
						}
					}),
					listeners: {
						beforeLoad: function(store, operation, eOpts){
							store.getProxy().extraParams = {
								status: Ext.getCmp('componentGridFilter-ActiveStatus').getValue() ? Ext.getCmp('componentGridFilter-ActiveStatus').getValue() : 'ALL',
								approvalState: Ext.getCmp('componentGridFilter-ApprovalStatus').getValue() ? Ext.getCmp('componentGridFilter-ApprovalStatus').getValue() : 'ALL',
								componentType: Ext.getCmp('componentGridFilter-ComponentType').getValue() ? Ext.getCmp('componentGridFilter-ComponentType').getValue() : 'ALL',
								componentName: Ext.getCmp('componentGridFilter-Name').getValue() ? Ext.getCmp('componentGridFilter-Name').getValue() : ''
							};
						}
					}
				});

				var componentGrid = Ext.create('Ext.grid.Panel', {
					title: 'Manage Entries <i class="fa fa-lg fa-question-circle"  data-qtip="This tool allows for manipulating all data related to an entry" ></i>',
					id: 'componentGrid',
					store: maingridStore,
					columnLines: true,
					bodyCls: 'border_accent',
					viewConfig: {
						enableTextSelection: true
					},					
					selModel: {
						   selType: 'checkboxmodel'
					},
					plugins: 'gridfilters',
					//enableLocking: true,
					columns: [
						{ text: 'Name', dataIndex: 'name', width: 275, minWidth: 350 , flex: 150,
							filter: {
								type: 'string'
							}
						},
						{ text: 'Type', align: 'center', dataIndex: 'componentType', width: 125,
							filter: {
								type: 'list'
							},
							renderer: function (value, meta, record) {
								return record.get('componentTypeLabel');
							}
						},
						{ text: 'Description', dataIndex: 'description', flex: 1, minWidth: 150, hidden:true,
						 renderer: function(value){
							return Ext.util.Format.stripTags(value);
						}},
						{ text: 'Pending Changes', tooltip: 'See Action->Change Requests to view', align: 'center', dataIndex: 'numberOfPendingChanges', width: 150 },
						{ text: 'Last Activity Date', dataIndex: 'lastActivityDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Submitted Date', dataIndex: 'submittedDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Approval State', align: 'center', dataIndex: 'approvalState', width: 125,
							renderer: function (value, meta, record) {
								return record.get('approvalStateLabel');
							}
						},
						{ text: 'Approval Date', dataIndex: 'approvedDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Active Status', align: 'center', dataIndex: 'activeStatus', width: 125 },
						{ text: 'Integration Management', dataIndex: 'integrationManagement', width: 175, sortable: false },
						{ text: 'Current Owner', dataIndex: 'currentDataOwner', width: 175, sortable: false },
						{ text: 'Update Date', dataIndex: 'updateDts', width: 175, hidden: true, xtype: 'datecolumn', format:'m/d/y H:i:s'},
						{ text: 'Update User', dataIndex: 'updateUser', width: 175, hidden: true },
						{ text: 'Create Date', dataIndex: 'createDts', width: 175, hidden: true, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Create User', dataIndex: 'createUser', width: 175, hidden: true },
						{ text: 'Component Id', dataIndex: 'componentId', width: 175, hidden: true },
						{ text: 'Security Marking', dataIndex: 'securityMarkingDescription', width: 175, hidden: true, sortable: false }
					],
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [
								Ext.create('OSF.component.StandardComboBox', {
									id: 'componentGridFilter-ActiveStatus',
									emptyText: 'All',
									fieldLabel: 'Active Status',
									name: 'activeStatus',
									typeAhead: false,
									editable: false,
									listeners: {
										change: function(filter, newValue, oldValue, opts){
											actionRefreshComponentGrid(true);
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
												},
												{
													code: 'ALL',
													description: 'All'
												}
											]
										}
									}
								}),
								Ext.create('OSF.component.StandardComboBox', {
									id: 'componentGridFilter-ApprovalStatus',
									emptyText: 'All',
									fieldLabel: 'Approval State',
									name: 'approvalState',
									typeAhead: false,
									editable: false,
									listeners: {
										change: function(filter, newValue, oldValue, opts){
											actionRefreshComponentGrid(true);
										}
									},
									storeConfig: {
										url: 'api/v1/resource/lookuptypes/ApprovalStatus',
										addRecords: [
											{
												code: null,
												description: 'All'
											}
										]
									}
								}),
								Ext.create('OSF.component.StandardComboBox', {
									id: 'componentGridFilter-ComponentType',
									emptyText: 'All',
									fieldLabel: 'Entry Type',
									name: 'componentType',
									valueField: 'code',
									displayField: 'description',
									typeAhead: false,
									matchFieldWidth: false,
									editable: false,
									listeners: {
										change: function(filter, newValue, oldValue, opts){
											actionRefreshComponentGrid(true);
										}
									},
									storeConfig: {
										url: 'api/v1/resource/componenttypes/lookup',
										model: undefined,
										fields: [
											'componentType',
											'label'
										],
										addRecords: [
											{
												type: null,
												label: 'All'
											}
										]
									}
								}),
								{
									xtype: 'textfield',
									id: 'componentGridFilter-Name',
									fieldLabel: 'Name',
									name: 'name',
									emptyText: 'Filter By Name',
									labelAlign: 'top',
									labelSeparator: '',
									listeners: {
										change: {
											fn: function(field, newValue, oldValue, opts) {
												actionRefreshComponentGrid(true);
											},
											buffer: 1500
										}
									}
								}
							]
						},
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										actionRefreshComponentGrid();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Add',
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction',
									requiredPermissions: ['ADMIN-ENTRY-CREATE'],
									handler: function () {
										actionAddEditComponent();
									}
								},
								{
									text: 'Edit',
									id: 'lookupGrid-tools-edit',
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-view',
									disabled: true,
									requiredPermissions: ['ADMIN-ENTRY-UPDATE'],
									handler: function () {
										actionAddEditComponent(Ext.getCmp('componentGrid').getSelection()[0]);
									}
								},
								{
									text: 'View',
									id: 'lookupGrid-tools-preview',
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction-view',
									disabled: true,
									requiredPermissions: ['ADMIN-ENTRY-READ'],
									handler: function () {
										actionPreviewComponent(Ext.getCmp('componentGrid').getSelection()[0].get('componentId'));
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									xtype: 'splitbutton',
									text: 'Approve',
									id: 'lookupGrid-tools-approve',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-check-square-o icon-button-color-save icon-vertical-correction',
									disabled: true,
									requiredPermissions: ['ADMIN-ENTRY-APPROVE'],
									handler: function () {
										actionApproveComponent();
									},
									menu: {
										items: [
											{
												text: 'Approve Related Entries',
												iconCls: 'fa fa-lg fa-share icon-small-vertical-correction',											
												handler: function() {
													var record = Ext.getCmp('componentGrid').getSelection()[0];
													showMultiApproveForm(record.get('componentId'), record.get('name'));
												}
											}
										]
									}
								},
								{
									text: 'Action',
									id: 'lookupGrid-tools-action',
									scale: 'medium',
									disabled: true,
									requiredPermissions: [
										'ADMIN-ENTRY-CHANGEOWNER',
										'ADMIN-ENTRY-CHANGETYPE',
										'ADMIN-ENTRY-CHANGEREQUEST-MANAGEMENT',
										'ADMIN-ENTRY-APPROVE',
										'ADMIN-ENTRY-CREATE',
										'ADMIN-ENTRY-MERGE',
										'ADMIN-ENTRY-VERSION-READ',
										'ADMIN-ENTRY-UPDATE',
										'ADMIN-ENTRY-DELETE'
									],
									menu: [
										{
											text: 'Change Owner',
											iconCls: 'fa fa-lg fa-user icon-small-vertical-correction icon-button-color-default',
											requiredPermissions: ['ADMIN-ENTRY-CHANGEOWNER'],
											handler: function(){
												actionChangeOwner();
											}
										},
										{
											text: 'Change Type',
											iconCls: 'fa fa-lg fa-exchange icon-small-vertical-correction icon-button-color-default',
											requiredPermissions: ['ADMIN-ENTRY-CHANGETYPE'],
											handler: function(){
												actionChangeType();
											}
										},
										{
											id: 'lookupGrid-tools-action-changeRequests',
											text: 'Change Requests',
											iconCls: 'fa fa-lg fa-edit icon-small-vertical-correction icon-button-color-default',
											requiredPermissions: ['ADMIN-ENTRY-CHANGEREQUEST-MANAGEMENT'],
											handler: function () {
												actionChangeRequests(Ext.getCmp('componentGrid').getSelection()[0]);
											}
										},
										{
											text: 'Approve Related Entries',
											iconCls: 'fa fa-lg fa-share icon-small-vertical-correction',											
											requiredPermissions: ['ADMIN-ENTRY-APPROVE'],
											handler: function() {
												var record = Ext.getCmp('componentGrid').getSelection()[0];
												showMultiApproveForm(record.get('componentId'), record.get('name'));
											}
										},										
										{
											xtype: 'menuseparator',
											requiredPermissions: ['ADMIN-ENTRY-CREATE', 'ADMIN-ENTRY-MERGE', 'ADMIN-ENTRY-VERSION-READ']
										},
										{
											id: 'lookupGrid-tools-action-copy',
											text: 'Copy',
											iconCls: 'fa fa-lg fa-clone icon-small-vertical-correction icon-button-color-default',
											requiredPermissions: ['ADMIN-ENTRY-CREATE'],
											handler: function(){
												actionCopyComponent();
											}
										},
										{
											id: 'lookupGrid-tools-action-versions',
											text: 'Versions',
											iconCls: 'fa fa-lg fa-object-ungroup icon-small-vertical-correction icon-button-color-default',
											requiredPermissions: ['ADMIN-ENTRY-VERSION-READ'],
											handler: function(){
												actionVersions();
											}
										},
										{
											xtype: 'menuseparator',
											requiredPermissions: ['ADMIN-ENTRY-TOGGLE-STATUS']
										},
										{
											text: 'Toggle Status',
											iconCls: 'fa fa-lg fa-power-off icon-small-vertical-correction icon-button-color-default',
											requiredPermissions: ['ADMIN-ENTRY-TOGGLE-STATUS'],
											handler: function(){
												actionToggleStatus();
											}
										},
										{
											xtype: 'menuseparator',
											requiredPermissions: ['ADMIN-ENTRY-DELETE']
										},
										{
											text: 'Delete',
											cls: 'alert-danger',
											iconCls: 'fa fa-lg fa-trash icon-small-vertical-correction icon-button-color-default',
											requiredPermissions: ['ADMIN-ENTRY-DELETE'],
											handler: function() {
												actionDeleteComponent();
											}
										}
									]
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Import',
									id: 'lookupGrid-tools-import',
									hidden: true,
									iconCls: 'fa fa-2x fa-upload icon-button-color-default icon-vertical-correction-view',
									scale: 'medium',
									width: '110px',
									handler: function () {
										importWindow.show();
									}
								},
								{
									text: 'Export',
									id: 'lookupGrid-tools-export',
									hidden: true,
									iconCls: 'fa fa-2x fa-download icon-button-color-default',
									scale: 'medium',
									disabled: true,
									menu: {
										items: [
											{
												text: 'Standard',
												handler: function() {
													actionExportComponents('exportForm');
												}
											},
											{
												text: 'Describe',
												handler: function() {
													actionExportComponents('exportFormDescribe');
												}
											}
										]
									}
								}
							]
						}
					],
					listeners: {
						itemdblclick: function(grid, record, item, index, e, opts){
							actionAddEditComponent(record);
						},
						selectionchange: function(selectionModel, records, opts){
							checkComponetGridTools();
						}
					},
					bbar: Ext.create('Ext.PagingToolbar', {
						store: maingridStore,
						displayInfo: true,
						displayMsg: 'Displaying Entries {0} - {1} of {2}',
						emptyMsg: "No entries to display"
					})
				});

				addComponentToMainViewPort(componentGrid);

				CoreService.userservice.getCurrentUser().then(function(user){
					if (CoreService.userservice.userHasPermission(user, "ADMIN-DATA-IMPORT-EXPORT")) {
						Ext.getCmp('lookupGrid-tools-import').setHidden(false);
						Ext.getCmp('lookupGrid-tools-export').setHidden(false);
					}
				});

				var checkComponetGridTools = function() {

					if (componentGrid.getSelectionModel().getCount() === 1) {

						// Enable Tools & Buttons
						Ext.getCmp('lookupGrid-tools-export').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-edit').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-preview').setDisabled(false);

						// Store Approval State
						var approvalState = Ext.getCmp('componentGrid').getSelection()[0].get('approvalState');

						// Check If Entry Is Approved
						if (approvalState !== 'A') {

							// Enable Approval Button
							Ext.getCmp('lookupGrid-tools-approve').setDisabled(false);
						}
						else {

							// Disable Approval Button
							Ext.getCmp('lookupGrid-tools-approve').setDisabled(true);
						}

						// Enable Action Button
						Ext.getCmp('lookupGrid-tools-action').setDisabled(false);

						// Ensure Pieces Of The Action Button Are Enabled
						Ext.getCmp('lookupGrid-tools-action-changeRequests').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-action-copy').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-action-versions').setDisabled(false);
					}
					else if (componentGrid.getSelectionModel().getCount() > 1) {

						// Enable/Disable Tools & Buttons
						Ext.getCmp('lookupGrid-tools-export').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-edit').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-preview').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-approve').setDisabled(true);

						// Ensure Action Button Is Enabled
						Ext.getCmp('lookupGrid-tools-action').setDisabled(false);

						// Disable Pieces Of The Action Button
						Ext.getCmp('lookupGrid-tools-action-changeRequests').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-action-copy').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-action-versions').setDisabled(true);
					}
					else {

						// Disable Tools & Buttons
						Ext.getCmp('lookupGrid-tools-export').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-edit').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-preview').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-approve').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-action').setDisabled(true);
					}
				};

				var actionChangeRequests = function(record) {
					var componentId = record.get('componentId');
					var name = record.get('name');
					changeRequestWindow.show();
					changeRequestWindow.loadComponent(componentId, name);
				};
				
				var changeOwnerWinCreated = false;

				var actionChangeOwner = function() {
					if(!changeOwnerWinCreated){
						changeOwnerWinCreated = true;
						Ext.create('Ext.window.Window', {
							id: 'changeOwnerWin',
							title: 'Change Owner - ',
							iconCls: 'fa fa-lg fa-user',
							width: '50%',
							height: 450,
							y: 200,
							modal: true,
							layout: 'fit',
							items: [
								{
									xtype: 'form',
									itemId: 'changeOwnerForm',
									bodyStyle: 'padding: 10px',
									items: [
										{
											xtype: 'combobox',
											fieldLabel: 'Username <span class="field-required" />',
											labelAlign: 'top',
											labelSeparator: '',
											typeAhead: true,
											editable: true,
											allowBlank: false,
											name: 'currentDataOwner',
											width: '100%',
											valueField: 'username',
											forceSelection: false,
											queryMode: 'local',
											displayField: 'username',
											store: {
												autoLoad: true,
												proxy: {
													type: 'ajax',
													url: 'api/v1/resource/userprofiles',
													reader: {
														type: 'json',
														rootProperty: 'data',
														totalProperty: 'totalNumber'
													}
												}
											}
										},
										{
											xtype: 'osf-common-validhtmleditor',
											itemId: 'searchComment',
											fieldLabel: 'Optional Comments',
											labelAlign: 'top',
											name: 'Comment name',
											width: '100%',
											displayField: 'Comment displayfield',
											store: {
												autoLoad: true
											}
										},
										{
											xtype: 'hidden',
											itemId: 'searchCommentId',
											name: 'commentId'
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
													handler: function(){
														Ext.getCmp('componentGrid').setLoading('Changing the owner for the selected entries...');
														var componentIds = Ext.getCmp('componentGrid').getSelection().map(function (item) {
															return item.getData().componentId;
														});
														var form = this.up('form');
														var username = form.getForm().findField('currentDataOwner').getValue();
														var data = {
															componentIds: componentIds,
															comment: {
																commentType: 'ADMIN',
																comment: form.queryById('searchComment').getValue()
															},
															newOwner: username
														};
														if(data.comment.comment === ''){
															data.comment = null;
														}
														Ext.Ajax.request({
															url: 'api/v1/resource/components/changeowner',
															method: 'PUT',
															jsonData: data,
															callback: function(){
																Ext.getCmp('componentGrid').setLoading(false);
															},
															success: function(response, opts) {
																if (response.responseText !== '') {
																	if( response.responseText.indexOf('errors') !== -1) {
																	// provide error notification
																		Ext.toast({
																			title: 'validation error. the server could not process the request. ',
																			html: 'try changing the comment field. the comment field cannot be empty and must have a size smaller than 4096.',
																			width: 550,
																			autoclosedelay: 10000
																		});
																	}
																}
																actionRefreshComponentGrid();
																form.reset();
															},
															failure: function(){
																Ext.toast({
																	title: 'validation error. the server could not process the request. ',
																	html: 'try changing the comment field. the comment field cannot be empty and must have a size smaller than 4096.',
																	width: 550,
																	autoclosedelay: 10000
																});
															}
														});
														this.up('window').close();
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
						});
					}
					// Get Selection
					var selection = Ext.getCmp('componentGrid').getSelection();

					// Get Number Of Selected
					var selected = componentGrid.getSelectionModel().getCount();

					// Check For Single Selection
					if (selected === 1) {

						Ext.getCmp('changeOwnerWin').setTitle('Change Owner - ' + selection[0].get('name'));
						Ext.getCmp('changeOwnerWin').getComponent('changeOwnerForm').loadRecord(selection[0]);
					}
					else {

						Ext.getCmp('changeOwnerWin').setTitle('Change Owner - ' + selected + ' Records');
					}

					// Display Window
					Ext.getCmp('changeOwnerWin').show();
				};

				var actionChangeType = function() {

					// Get Selection
					var selection = Ext.getCmp('componentGrid').getSelection();

					// Get Number Of Selected
					var selected = componentGrid.getSelectionModel().getCount();

					// Check For Single Selection
					if (selected === 1) {

						Ext.getCmp('changeTypeWin').setTitle('Change Type - ' + selection[0].get('name'));
						Ext.getCmp('changeTypeWin').getComponent('changeTypeForm').loadRecord(selection[0]);
					}
					else {

						Ext.getCmp('changeTypeWin').setTitle('Change Type - ' + selected + ' Records');
					}

					// Display Window
					Ext.getCmp('changeTypeWin').show();
				};

				var actionRefreshComponentGrid = function(resetPage) {
					var searchPram = {
							status: Ext.getCmp('componentGridFilter-ActiveStatus').getValue() ? Ext.getCmp('componentGridFilter-ActiveStatus').getValue() : 'ALL',
							approvalState: Ext.getCmp('componentGridFilter-ApprovalStatus').getValue() ? Ext.getCmp('componentGridFilter-ApprovalStatus').getValue() : 'ALL',
							componentType: Ext.getCmp('componentGridFilter-ComponentType').getValue() ? Ext.getCmp('componentGridFilter-ComponentType').getValue() : 'ALL',
							componentName: Ext.getCmp('componentGridFilter-Name').getValue() ? Ext.getCmp('componentGridFilter-Name').getValue() : ''
						};
					if(resetPage)
					{
						Ext.getCmp('componentGrid').getStore().loadPage(1,{
							params: searchPram
						});
					}
					else
					{
						Ext.getCmp('componentGrid').getStore().load({
							params: searchPram
						});

					}
				};

				var actionAddEditComponent = function(record) {

					var mainAddEditWin = createAddEditWin();
					mainAddEditWin.show();

					mainAddEditWin.generalForm.componentRecord = record;

					mainAddEditWin.generalForm.queryById('componentTypeMainCB').suspendEvent('change');

					if (record) {
						mainAddEditWin.setTitle('Entry Form: ' + record.get('name'));

						checkFormTabs(mainAddEditWin, record);

						mainAddEditWin.generalForm.loadRecord(record);
						handleAttributes(record.get('componentType'), mainAddEditWin.generalForm);
						//Ext.defer(function(){
					//		mainAddEditWin.generalForm.loadComponentAttributes();
					//	}, 1000);
						mainAddEditWin.generalForm.queryById('requiredAttributePanel').on('ready', function(){
							mainAddEditWin.generalForm.loadComponentAttributes();
						});


						mainAddEditWin.generalForm.queryById('integrationBtn').setDisabled(false);
						mainAddEditWin.generalForm.queryById('changeHistoryBtn').setDisabled(false);
					} else {
						mainAddEditWin.setTitle('Entry Form:  NEW ENTRY');

						var requiredStore = Ext.data.StoreManager.lookup('requiredAttributeStore');
						requiredStore.removeAll();

						Ext.getCmp('componentGrid').getSelectionModel().deselectAll();
						mainAddEditWin.generalForm.queryById('integrationBtn').setDisabled(true);
						mainAddEditWin.generalForm.queryById('changeHistoryBtn').setDisabled(true);

					}
					mainAddEditWin.generalForm.queryById('componentTypeMainCB').resumeEvent('change');

				   return mainAddEditWin;
				};

				var checkFormTabs = function(mainAddEditWin, record, componentType) {

					if (record) {

						var componentId = record.get('componentId');
						if (!componentType) {
							componentType = record.get('componentType');
						}

						//remove all extra tabs
						var tabpanel = mainAddEditWin.getComponent('tabpanel');
						tabpanel.items.each(function(panel) {
							if (panel.subPanel) {
								panel.subPanel.close();
							}
						});


						var panelsToAdd = [];
						var addSubTab = function(panelName, title, tooltip) {
							var subTab = Ext.create(panelName, {
								title: title,
								tooltip: tooltip
							});
							//tabpanel.add(subTab);
							panelsToAdd.push(subTab);
							//subTab.loadData(null, componentId, null, null);
						};

						Ext.Array.each(allComponentTypes, function(type){
							if (type.componentType === componentType) {
								if (type.dataEntryAttributes) {
									addSubTab('OSF.form.Attributes', 'Attributes', 'Manage Attributes');
								}
								if (type.dataEntryRelationships) {
									addSubTab('OSF.form.Relationships', 'Relationships', 'Manage Relationships');
								}
								if (type.dataEntryContacts) {
									addSubTab('OSF.form.Contacts', 'Contacts', 'Manage Contacts');
								}
								if (type.dataEntryResources) {
									addSubTab('OSF.form.Resources', 'Resources', 'Manage Resources');
								}
								if (type.dataEntryMedia) {
									addSubTab('OSF.form.Media', 'Media', 'Manage Media');
								}
								if (type.dataEntryDependencies) {
									addSubTab('OSF.form.Dependencies', 'Dependencies', 'Manage Dependencies');
								}							
								if (type.dataEntryEvaluationInformation) {
									addSubTab('OSF.form.OldEvaluationSummary', 'Evaluation Summary', 'This is Deprecatied');
								}
								if (type.dataEntryReviews) {
									addSubTab('OSF.form.EntryReviews', 'Reviews', 'User reviews');
								}
								if (type.dataEntryQuestions) {
									addSubTab('OSF.form.EntryQuestions', 'Questions', 'User Questions');
								}
								addSubTab('OSF.form.Tags', 'Tags', 'Searchable Labels');
								addSubTab('OSF.form.Comments', 'Comments', 'Admin Comments');
							}
						});
						tabpanel.add(panelsToAdd);
						Ext.Array.each(panelsToAdd, function(subTab){
							subTab.loadData(null, componentId, null, null);
						});
					}

				};

				var actionApproveComponent = function() {
					Ext.getCmp('componentGrid').setLoading(true);
					var componentId = Ext.getCmp('componentGrid').getSelection()[0].get('componentId');
					Ext.Ajax.request({
						url: 'api/v1/resource/components/' + componentId + '/approve',
						method: 'PUT',
						success: function(response, opts){
							Ext.getCmp('componentGrid').setLoading(false);
							actionRefreshComponentGrid();
						},
						failure: function(response, opts){
							Ext.getCmp('componentGrid').setLoading(false);
						}
					});
				};

				var actionToggleStatus = function() {
					Ext.getCmp('toggleWin').show();
				};

				var actionDeleteComponent = function() {

					// Get Selection
					var selection = Ext.getCmp('componentGrid').getSelection();

					// Get Number Of Selected
					var selected = componentGrid.getSelectionModel().getCount();

					// Check If Only One Record Selected
					if (selected === 1) {

						var name = selection[0].get('name');
					}
					else {

						var name = selected + ' Records';
					}

					// Confirm Delete Operation
					Ext.Msg.show({
						title: 'Delete Component?',
						message: 'Are you sure you want to delete:  ' + name +' ?',
						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						fn: function(btn) {

							if (btn === 'yes') {

								// Indicate To User Deletion Is Occurring
								Ext.getCmp('componentGrid').setLoading(true);

								// Initialize Update Counter
								var componentDeleteCount = 0;

								// Loop Through Selection
								for (i = 0; i < selected; i++) {

									// Get Component ID
									var componentId = selection[i].get('componentId');

									// Make Request
									Ext.Ajax.request({

										url: 'api/v1/resource/components/' + componentId + '/cascade',
										method: 'DELETE',
										success: function(response, opts) {

											// Check For Errors
											if (response.responseText.indexOf('errors') !== -1) {

												// Provide Error Notification
												Ext.toast('An Entry Failed To Delete', 'Error');

												// Provide Log Information
												console.log(response);
											}

											// Check If We Are On The Final Request
											if (++componentDeleteCount === selected) {

												// Provide Success Notification
												Ext.toast('Selected entries have been deleted', 'Success');

												// Refresh Grid
												actionRefreshComponentGrid();

												// Unmask Grid
												Ext.getCmp('componentGrid').setLoading(false);
											}
										}
									});
								}
							}
						}
					});
				};

				var actionCopyComponent = function() {
					Ext.getCmp('componentGrid').setLoading('Copying please wait...');
					var componentId = Ext.getCmp('componentGrid').getSelection()[0].get('componentId');
					Ext.Ajax.request({
						url: 'api/v1/resource/components/' + componentId + '/copy',
						method: 'POST',
						success: function(response, opts){
							Ext.getCmp('componentGrid').setLoading(false);
							actionRefreshComponentGrid();
						},
						failure: function(response, opts){
							Ext.getCmp('componentGrid').setLoading(false);
						}
					});
				};

				var actionVersions = function() {
					versionWin.show();

					var componentId = Ext.getCmp('componentGrid').getSelection()[0].get('componentId');
					Ext.getCmp('versionGrid').getStore().load({
						url: 'api/v1/resource/components/' + componentId + '/versionhistory'
					});

					//load current verison
					versionLoadCurrent();
				};

				var versionLoadCurrent = function() {
					var componentId = Ext.getCmp('componentGrid').getSelection()[0].get('componentId');
					Ext.Ajax.request({
						url: 'api/v1/resource/components/' + componentId + '/detail',
						success: function(response, opts) {
							var data = Ext.decode(response.responseText);
							Ext.getCmp('versionWin-currentVersionPanel').update(data);
							Ext.getCmp('versionWin-currentVersionPanel').setTitle('Current Version - ' + data.recordVersion);
							Ext.getCmp('versionWin-snapshotVersionPanel').update(undefined);
							Ext.getCmp('versionWin-snapshotVersionPanel').setTitle('Selected Version');
						}
					});
				};

				var actionExportComponents = function(exportForm) {

					// Initialize Export IDs
					var ids = "";

					// Loop Through Records
					Ext.Array.each(componentGrid.getSelection(), function(record) {

						// Add Component ID To Form
						ids += '<input type="hidden" name="id" ';
						ids += 'value="' + record.get('componentId') +'" />';
					});

					// Get CSRF Token From Cookie
					var token = Ext.util.Cookies.get('X-Csrf-Token');

					// Ensure CSRF Token Is Available
					if (token) {

						// Add CSRF Token To Form
						ids += '<input type="hidden" name="X-Csrf-Token" ';
						ids += 'value="' + token + '" />';
					}

					// Set Form
					document.getElementById(exportForm + 'Ids').innerHTML = ids;

					// Submit Form
					document[exportForm].submit();
				};

				var actionPreviewComponent = function(id){
					previewComponentWin.show();
					previewContents.load('view.jsp?fullPage=true&embedded=true&hideSecurityBanner=true&id=' + id);
					previewCheckButtons();
				};

			});

		</script>

	</stripes:layout-component>
</stripes:layout-render>
