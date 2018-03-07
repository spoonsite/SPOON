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
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
	<stripes:layout-component name="contents">

		<stripes:layout-render name="../../../../layout/adminheader.jsp">		
		</stripes:layout-render>		
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function () {

				Ext.create('Ext.data.Store', {
					storeId: 'alertStore',
					autoLoad: true,
					fields: [
						'name',
						'activeStatus',
						'alertType',
						'alertTypeDescription',
						'alertId',
						'emailAddresses',
						'userDataAlertOption',
						'systemErrorAlertOption',
						{
							name: 'createDts',
							type: 'date',
							dateFormat: 'c'
						},
						{
							name: 'updateDts',
							type: 'date',
							dateFormat: 'c'
						}						
					],
					proxy: {
						id: 'alertStoreProxy',
						type: 'ajax',
						url: 'api/v1/resource/alerts'
					}
				});


				var alertGrid = Ext.create('Ext.grid.Panel', {
					title: 'Manage Alerts &nbsp;<i class="fa fa-lg fa-question-circle"  data-qtip="Alerts are triggers set up to watch the data <br />that an administrator can subscribe to."></i>',
					id: 'alertGrid',
					store: Ext.data.StoreManager.lookup('alertStore'),
					columnLines: true,
					selModel: 'rowmodel',
					columns: [
						{text: 'Name', dataIndex: 'name', width: 225 },
						{text: 'Type', dataIndex: 'alertTypeDescription', width: 225 },
						{
							text: 'Email Addresses',
							dataIndex: 'emailAddresses',
							sortable: false,
							width: 300,
							renderer: function (value) {
								if (value.length > 1) {
									var emailList = '';
									Ext.Array.each(value, function (item, index) {
										emailList += item.email;
										emailList += index === value.length-1 ? '<br />' : ', <br />';
									});
									return emailList;
								} else { 
									return value[0].email;
								}
							}
						},
						{
							text: 'Options',
							dataIndex: 'userDataAlertOption',
							sortable: false,
							flex: 1,
							renderer: function (value, metaData, record) {
								var option = record.get('userDataAlertOption');
								var listOfOptions = [];
								if (option) {
									if (option.alertOnTags) {
										listOfOptions.push('<span class="alerts-option-items"> Tags </span>');
									}
									if (option.alertOnReviews) {
										listOfOptions.push('<span class="alerts-option-items"> Reviews </span>');
									}
									if (option.alertOnQuestions) {
										listOfOptions.push('<span class="alerts-option-items"> Questions/Responses </span>');
									}
									if (option.alertOnContactUpdate) {
										listOfOptions.push('<span class="alerts-option-items"> Contact Update </span>');
									}																		
									if (option.alertOnUserAttributeCodes) {
										listOfOptions.push('<span class="alerts-option-items"> User-Created Attribute Codes </span>');
									}
								} else if (record.get('systemErrorAlertOption')) {
									option = record.get('systemErrorAlertOption');
									if (option.alertOnSystem) {
										listOfOptions.push('<span class="alerts-option-items"> System Errors </span>');
									}
									if (option.alertOnREST) {
										listOfOptions.push('<span class="alerts-option-items"> API Errors </span>');
									}
									if (option.alertOnIntegration) {
										listOfOptions.push('<span class="alerts-option-items"> Integration Errors </span>');
									}
									if (option.alertOnReport) {
										listOfOptions.push('<span class="alerts-option-items"> Report Errors </span>');
									}
								} else if (record.get('userManagementAlertOption')) {
									option = record.get('userManagementAlertOption');
									if (option.alertOnUserRegistration) {
										listOfOptions.push('<span class="alerts-option-items"> User Registration </span>');
									}
									if (option.alertOnUserNeedsApproval) {
										listOfOptions.push('<span class="alerts-option-items"> User Need Approval </span>');
									}
								} else if (record.get('componentTypeAlertOptions')) {
									labels = record.get('componentTypeAlertOptionLabels')
									if (labels) {
										Ext.Array.forEach(labels, function(element) {
											listOfOptions.push('<span class="alerts-option-items"> ' + element + ' </span>');
										})
									}
								}
								return '<div style="height: 25px;">' + listOfOptions.join(' ') + '</div>';
							}
						},
						{
							text: 'Create User', dataIndex: 'createUser', width: 150, hidden: true
						},				
						{
							text: 'Create Date', dataIndex: 'createDts', xtype: 'datecolumn', format: 'm/d/y H:i:s', width: 150, hidden: true
						},						
						{
							text: 'Update User', dataIndex: 'updateUser', width: 150, hidden: true
						},
						{
							text: 'Update Date', dataIndex: 'updateDts', xtype: 'datecolumn', format: 'm/d/y H:i:s', width: 150, hidden: true
						}						
					],
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [
								Ext.create('OSF.component.StandardComboBox', {
									id: 'alertFilter-ActiveStatus',
									emptyText: 'Active',
									fieldLabel: 'Active Status',
									name: 'activeStatus',
									listeners: {
										change: function (filter, newValue, oldValue, opts) {
											if (newValue) {
												var store = Ext.data.StoreManager.lookup('alertStore');
												if (newValue === 'A') {
													store.setProxy({
														id: 'alertStoreProxy',
														type: 'ajax',
														url: 'api/v1/resource/alerts?status=A'
													});
													Ext.getCmp('alertGrid-tools-toggleActivation').setText("Toggle Status");
												} else {
													store.setProxy({
														id: 'alertStoreProxy',
														type: 'ajax',
														url: 'api/v1/resource/alerts?status=I'
													});
													Ext.getCmp('alertGrid-tools-toggleActivation').setText("Toggle Status");
												}
												store.load();
												Ext.getCmp('alertGrid').getSelectionModel().deselectAll();
												Ext.getCmp('alertGrid-tools-edit').setDisabled(true);
												Ext.getCmp('alertGrid-tools-toggleActivation').setDisabled(true);
											}
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
								})]
						},
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										Ext.getCmp('alertGrid').getStore().load();
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
									handler: function () {
										actionEditAlertForm(null);
									}
								},
								{
									text: 'Edit',
									id: 'alertGrid-tools-edit',
									disabled: true,
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
									handler: function () {
										var record = Ext.getCmp('alertGrid').getSelection()[0];
										actionEditAlertForm(record);
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Toggle Status',
									id: 'alertGrid-tools-toggleActivation',
									iconCls: 'fa fa-2x fa-power-off icon-button-color-default icon-vertical-correction',
									disabled: true,
									scale: 'medium',
									handler: function () {
										var record = Ext.getCmp('alertGrid').getSelection()[0];
										actionToggleActivation(record);
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									id: 'alertGrid-tools-delete',
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									disabled: true,
									scale: 'medium',
									handler: function () {
										var record = Ext.getCmp('alertGrid').getSelection()[0];
										actionDeleteAlert(record);
									}
								}

							]
						}
					],
					listeners: {
						itemdblclick: function (grid, record, item, index, e, opts) {
							actionEditAlertForm(record);
						},
						selectionchange: function (grid, record, index, opts) {
							if (Ext.getCmp('alertGrid').getSelectionModel().hasSelection()) {
								Ext.getCmp('alertGrid-tools-edit').enable(true);
								Ext.getCmp('alertGrid-tools-toggleActivation').enable(true);
								Ext.getCmp('alertGrid-tools-delete').enable(true);
							} else {
								Ext.getCmp('alertGrid-tools-edit').disable();
								Ext.getCmp('alertGrid-tools-toggleActivation').disable();
								Ext.getCmp('alertGrid-tools-delete').disable();
							}
						}
					}
				});



				var alertAddEditWin = Ext.create('Ext.window.Window', {
					id: 'alertAddEditWin',
					title: 'Add/Edit Alert',
					modal: true,
					width: 600,
					height: 600,
					y: '10em',
//					iconCls: 'fa fa-lg fa-edit icon-small-vertical-correction',
					layout: 'fit',
					items: [
						{
							xtype: 'form',
							id: 'editAlertForm',
							layout: 'vbox',
							scrollable: true,
							bodyStyle: 'padding: 10px;',
							defaults: {
								labelAlign: 'top',
								width: '100%'
							},
							items: [
								{
									xtype: 'textfield',
									id: 'alertEntryForm-Name',
									fieldLabel: 'Name<span class="field-required" />',
									name: 'name'
								},
								{
									xtype: 'combobox',
									fieldLabel: 'Alert Type<span class="field-required" />',
									id: 'alertEntryForm-Type',
									forceSelection: true,
									displayField: 'description',
									valueField: 'code',
									value: 'CMPSUB',
									name: 'alertType',
									listeners: {
										change: function (combo, newValue, oldValue, opts) {
											Ext.getCmp('systemErrorOptions').hide();
											Ext.getCmp('userDataOptions').hide();
											Ext.getCmp('userManagementOptions').hide();
											Ext.getCmp('alertEntryForm-entryTypes').hide();
											Ext.getCmp('alertEntryForm-entryTypes').allowBlank = true;
											switch (newValue) {
												case 'SYSERROR':
													Ext.getCmp('systemErrorOptions').show();
													break;
												case 'USERD':
													Ext.getCmp('userDataOptions').show();
													break;
												case 'USERMANG':
													Ext.getCmp('userManagementOptions').show();
													break;
												case 'CHGREQ':
												case 'CMPSUB':
													Ext.getCmp('alertEntryForm-entryTypes').allowBlank = false;
													Ext.getCmp('alertEntryForm-entryTypes').show();
													break;
											}
										}
									},
									store: {
										autoLoad: true,
										proxy: {
											type: 'ajax',
											url: 'api/v1/resource/lookuptypes/AlertType'
										}
									}
								},
								{
									xtype: 'tagfield',
									fieldLabel: 'Select a Component Type<span class="field-required" />',
									id: 'alertEntryForm-entryTypes',
									name: 'entryTypeAlertOption',
									valueField: 'componentType',
									displayField: 'label',
									allowBlank: false,
									store: {
										autoLoad: true,
										proxy: {
											type: 'ajax',
											url: 'api/v1/resource/componenttypes'
										}
									}
								},
								{
									xtype: 'textfield',
									fieldLabel: 'Email Addresses<span class="field-required"></span>',
									// id is 'email' for validation purposes.
									id: 'email',
									name: 'emailAddresses',
									allowBlank: false
								},
								{
									xtype: 'fieldcontainer',
									id: 'systemErrorOptions',
									name: 'systemErrorAlertOption',
									fieldLabel: 'System Error Options',
									defaultType: 'checkboxfield',																		
									hidden: true,									
									items: [
										// The names in this section
										// must correspond to the names
										// seen in the dataset.
										{
											boxLabel: 'System Errors',
											name: 'alertOnSystem',
											id: 'sysErrors-sysErrors'
										},
										{
											boxLabel: 'API Errors',
											name: 'alertOnREST',
											id: 'sysErrors-apiErrors'
										},
										{
											boxLabel: 'Integration Errors',
											name: 'alertOnIntegration',
											id: 'sysErrors-integrationErrors'
										},
										{
											boxLabel: 'Report Errors',
											name: 'alertOnReport',
											id: 'sysErrors-repErrors'
										}
									]
								},
								{
									xtype: 'fieldcontainer',
									id: 'userDataOptions',
									name: 'userDataAlertOption',
									fieldLabel: 'User Data Options',
									defaultType: 'checkboxfield',
									hidden: true,
									items: [
										{
											boxLabel: 'Tags',
											name: 'alertOnTags',
											id: 'userData-tags'
										},
										{
											boxLabel: 'Reviews',
											name: 'alertOnReviews',
											id: 'userData-reviews'
										},
										{
											boxLabel: 'Questions/Responses',
											name: 'alertOnQuestions',
											id: 'userData-questions'
										},
										{
											boxLabel: 'Contact Update',
											name: 'alertOnContactUpdate',
											id: 'userData-contactUpdate'
										},
										{
											boxLabel: 'User-Created Attribute Codes',
											labelWidth: 250,
											name: 'alertOnUserAttributeCodes',
											id: 'userData-attributeCodes'
										}							
									]
								},
								{
									xtype: 'fieldcontainer',
									id: 'userManagementOptions',
									name: 'userManagementAlertOption',
									fieldLabel: 'User Management Options',
									defaultType: 'checkboxfield',																		
									hidden: true,									
									items: [										
										{
											boxLabel: 'User Registration',
											name: 'alertOnUserRegistration',
											id: 'usermanage-registration'
										},
										{
											boxLabel: 'User Needs Approval',
											name: 'alertOnUserNeedsApproval',
											id: 'usermanage-approval'
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
											text: 'Save',
											iconCls: 'fa fa-lg fa-save icon-button-color-save',
											formBind: true,
											handler: function () {
												var method = Ext.getCmp('editAlertForm').edit ? 'PUT' : 'POST';

												// Build PUT/POST Data
												var data = {};

												// Compile un-nested data
												var flatData = Ext.getCmp('editAlertForm').getValues();
												data.alertType = flatData.alertType;
												data.name = flatData.name;

												// Compile emailAddresses
												var stringEmails = flatData.emailAddresses.split(";");
												var emailAddresses = [];
												stringEmails.forEach(function (currentValue, index, array) {
													stringEmails[index] = currentValue.trim();
													if (stringEmails[index] !== '') {
														emailAddresses.push({"email": stringEmails[index]});
													}
												});
												data.emailAddresses = emailAddresses;


												// Compile UserDataOptions
												if (flatData.alertType === 'USERD') {
													var ud = {};
													ud.alertOnTags = (flatData.alertOnTags === "true");
													ud.alertOnReviews = (flatData.alertOnReviews === "true");
													ud.alertOnQuestions = (flatData.alertOnQuestions === "true");
													ud.alertOnContactUpdate = (flatData.alertOnContactUpdate === "true");
													ud.alertOnUserAttributeCodes = (flatData.alertOnUserAttributeCodes === "true");
													data.userDataAlertOption = ud;
												}

												// Compile SystemErrorAlertOptions
												if (flatData.alertType === 'SYSERROR') {
													var se = {};
													se.alertOnSystem = (flatData.alertOnSystem === "true");
													se.alertOnREST = (flatData.alertOnREST === "true");
													se.alertOnReport = (flatData.alertOnReport === "true");
													se.alertOnIntegration = (flatData.alertOnIntegration === "true");
													data.systemErrorAlertOption = se;
												}
												
												if (flatData.alertType === 'USERMANG') {											
													data.userManagementAlertOption = {
														alertOnUserRegistration: flatData.alertOnUserRegistration,
														alertOnUserNeedsApproval: flatData.alertOnUserNeedsApproval
													};
												}

												if (flatData.alertType === 'CMPSUB' || flatData.alertType === 'CHGREQ') {
													var compIDs = flatData.entryTypeAlertOption;
													var componentTypes = [];
													Ext.Array.forEach(compIDs, function(el) {
														componentTypes.push({"componentType": el});
													});
													data.componentTypeAlertOptions = componentTypes;
												}


												// Submit Data
												var url = Ext.getCmp('editAlertForm').edit ? 'api/v1/resource/alerts/' + Ext.getCmp('editAlertForm').alertId : 'api/v1/resource/alerts';
												CoreUtil.submitForm({
													url: url,
													method: method,
													data: data,
													removeBlankDataItems: true,
													form: Ext.getCmp('editAlertForm'),
													success: function (response, opts) {
														// Server responded OK
														// Now we check for actual success based on
														// the server's response object.
														var errorResponse = Ext.decode(response.responseText);

														// Confusingly, you will only see the "success"
														// property in the response if the success
														// is success = false. Therefore
														// the appearance of the success property actually
														// means there was a failure.

														if (!errorResponse.hasOwnProperty('success')) {
															// Validation succeeded.
															Ext.toast('Saved Successfully', '', 'tr');
															Ext.getCmp('editAlertForm').setLoading(false);
															Ext.getCmp('editAlertForm').reset();
															Ext.getCmp('alertAddEditWin').hide();
															Ext.getCmp('alertGrid').getStore().load();
															Ext.getCmp('alertGrid').getSelectionModel().deselectAll();
															Ext.getCmp('alertGrid-tools-edit').setDisabled(true);
															Ext.getCmp('alertGrid-tools-toggleActivation').setDisabled(true);
														} else {
															// Validation failed

															// Compile an object to pass to ExtJS Form
															// that allows validation messages
															// using the markInvalid() method.
															var errorObj = {};
															Ext.Array.each(errorResponse.errors.entry, function (item, index, entry) {
																errorObj[item.key] = item.value;
															});
															var form = Ext.getCmp('editAlertForm').getForm();
															form.markInvalid(errorObj);
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
											iconCls: 'fa fa-lg fa-close icon-button-color-warning',
											handler: function () {
												Ext.getCmp('editAlertForm').reset();
												Ext.getCmp('alertAddEditWin').hide();
											}
										}
									]
								}
							]
						}
					]
				});


				var actionEditAlertForm = function (record) {
					alertAddEditWin.show();
					Ext.getCmp('editAlertForm').edit = false;
					Ext.getCmp('editAlertForm').reset(true);
					if (record) {
						// This is an edit form.
						alertAddEditWin.setTitle('<i class="fa fa-lg fa-edit"></i>' + '<span class="shift-window-text-right">Edit Attribute</span>');
						Ext.getCmp('editAlertForm').edit = true;
						Ext.getCmp('editAlertForm').alertId = record.data.alertId;
						var form = Ext.getCmp('editAlertForm');
						form.loadRecord(record);

						// Process E-mail Addresses
						var value = '';
						Ext.Array.each(record.data.emailAddresses, function (data) {
							value += data.email;
							value += "; ";
						});
						Ext.getCmp('email').setValue(value);

						// Process User Data Options
						if (record.data.userDataAlertOption) {
							var userData = Ext.getCmp('userDataOptions').query('checkboxfield');
							// Loop through checkboxes in the checkboxfield,
							// and set the value to the corresponding name in the record
							Ext.each(userData, function (checkbox) {
								checkbox.setValue(record['data']['userDataAlertOption'][checkbox.name]);
							});
						}


						// Process System Error Options -- same as above
						if (record.data.systemErrorAlertOption) {
							var systemError = Ext.getCmp('systemErrorOptions').query('checkboxfield');
							Ext.each(systemError, function (checkbox) {
								checkbox.setValue(record['data']['systemErrorAlertOption'][checkbox.name]);
							});
						}



					} else {
						// This is an add form.
						alertAddEditWin.setTitle('<i class="fa fa-plus"></i>' + '<span class="shift-window-text-right">Add Alert</span>');
					}
				};



				var actionToggleActivation = function (record) {
					if (record) {
						var alertId = record.data.alertId;
						var active = record.data.activeStatus;
						if (active === 'A') {
							var method = "DELETE";
							var url = 'api/v1/resource/alerts/' + alertId;
							var what = "deactivate";
						} else if (active === 'I') {
							var method = "POST";
							var url = 'api/v1/resource/alerts/' + alertId + "/activate";
							var what = "activate";
						} else {
							Ext.MessageBox.alert("Record Not Recognized", "Error: Record is not active or inactive.");
							return false;
						}

						Ext.Ajax.request({
							url: url,
							method: method,
							success: function (response, opts) {
								var message = 'Successfully ' + what + 'd "' + record.data.name + '"';
								Ext.toast(message, '', 'tr');
								// The ordering below is necessary
								// to get Ext to disable the buttons.
								Ext.getCmp('alertGrid').getStore().load();
								Ext.getCmp('alertGrid').getSelectionModel().deselectAll();
								Ext.getCmp('alertGrid-tools-toggleActivation').disable();
								Ext.getCmp('alertGrid-tools-edit').disable();
								Ext.getCmp('alertGrid-tools-delete').disable();
							},
							failure: function (response, opts) {
								Ext.MessageBox.alert('Failed to' + what,
										"Error: Could not " + what + ' "' + record.data.name + '"');
							}
						});

					} else {
						Ext.MessageBox.alert("No Record Selected", "Error: You have not selected a record.");
					}
				};


				var actionDeleteAlert = function (record) {
					if (record) {
						var alertId = record.data.alertId;
						var title = '<i class="fa fa-lg fa-warning"></i>' + '<span class="shift-window-text-right">Delete Alert</span>';
						var msg = 'Are you sure you want to delete "' + record.data.name + '"?';
						Ext.MessageBox.confirm(title, msg, function (btn) {
							if (btn === 'yes') {
								var url = 'api/v1/resource/alerts/' + alertId + "/force";
								var method = "DELETE";
								Ext.Ajax.request({
									url: url,
									method: method,
									success: function (response, opts) {
										var message = 'Successfully deleted "' + record.data.name + '"';
										Ext.toast(message, '', 'tr');
										// The ordering below is necessary
										// to get Ext to disable the buttons.
										Ext.getCmp('alertGrid').getStore().load();
										Ext.getCmp('alertGrid').getSelectionModel().deselectAll();
										Ext.getCmp('alertGrid-tools-toggleActivation').disable();
										Ext.getCmp('alertGrid-tools-edit').disable();
										Ext.getCmp('alertGrid-tools-delete').disable();
									},
									failure: function (response, opts) {
										Ext.MessageBox.alert('Failed to delete',
												'Error: Could not delete "' + record.data.name + '"');
									}
								});
							}
						});



					} else {
						Ext.MessageBox.alert("No Record Selected", "Error: You have not selected a record.");
					}
				};

				addComponentToMainViewPort(alertGrid);

			});

		</script>
	</stripes:layout-component>
</stripes:layout-render>
