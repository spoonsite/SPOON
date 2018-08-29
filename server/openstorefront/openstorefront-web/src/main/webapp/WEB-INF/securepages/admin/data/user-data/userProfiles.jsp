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
<stripes:layout-render name="../../../../../layout/toplevelLayout.jsp">
	<stripes:layout-component name="contents">

		<stripes:layout-render name="../../../../../layout/adminheader.jsp">		
		</stripes:layout-render>		
		
		<script src="scripts/component/messageWindow.js?v=${appVersion}" type="text/javascript"></script>

		<form name="exportForm" action="api/v1/resource/userprofiles/export" method="POST">
			<p style="display: none;" id="exportFormUserIds"></p>
		</form>

		<form name="toggleForm" id="toggleForm" action="api/v1/resource/userprofiles/multiple" method="DELETE">
			<p style="display: none;" id="toggleFormUserIds"></p>
		</form>

		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function () {

				var userProfileStore = Ext.create('Ext.data.Store', {
					storeId: 'userProfileStore',
					autoLoad: true,
					pageSize: 100,
					remoteSort: true,
					sorters: [
						new Ext.util.Sorter({
							property: 'lastLoginDts',
							direction: 'DESC'
						})
					],
					fields: [
						{
							name: 'lastLoginDts',
							type: 'date',
							dateFormat: 'c'						
						}
					],
					proxy: CoreUtil.pagingProxy({
						id: 'userProfileStoreProxy',
						extraParams: {
							status: 'A'
						},
						reader: {
							type: 'json',
							rootProperty: 'data',
							totalProperty: 'totalNumber'
						},
						url: 'api/v1/resource/userprofiles'											
					}),
					listeners: {
						beforeLoad: function(store, operation, eOpts) {
							
							// Get Active Status
							var activeStatus = Ext.getCmp('userProfileGrid-filter-ActiveStatus').getValue();
							
							// Get Search Field
							var searchField = Ext.getCmp('userProfileGrid-filter-SearchField').getValue();
							
							// Get Search Value
							var searchValue = Ext.getCmp('userProfileGrid-filter-SearchValue').getValue();
							
							// Clear Previous Parameters
							store.getProxy().extraParams = {};
							
							// Check For Search
							if (searchField != null && searchField != "" && searchValue != null && searchValue != "") {
								
								// Adjust Query String Parameters
								store.getProxy().extraParams['searchField'] = searchField;
								store.getProxy().extraParams['searchValue'] = searchValue;
							}
							
							// Check For 'ALL'
							if (activeStatus === 'ALL') {
								
								// Set Extra Filter Parameters
								store.getProxy().extraParams['all'] = true;
							}
							else {
							
								// Set Extra Filter Parameters
								store.getProxy().extraParams['status'] = activeStatus ? activeStatus : 'A';
							}
						}
					}						
				});


				var userProfileGrid = Ext.create('Ext.grid.Panel', {
					title: 'Manage User Profiles <i class="fa fa-question-circle"  data-qtip="A user profile represents a user in the system and contains the user\'s information."></i>',
					id: 'userProfileGrid',
					selModel: {
						selType: 'checkboxmodel'
					},
					store: userProfileStore,
					columnLines: true,
					requiredPermissions: ['ADMIN-USER-MANAGEMENT-PROFILES-READ'],
					permissionCheckFailure: function () {
						Ext.toast({
							html: 'You do not have permissions to view the data on this page',
							title: 'Invalid Permissions',
							align: 'b'
						});
					},
					columns: {
						defaults: {
							cellWrap: true
						},
						items: [
							{
								flex: 1,
								text: 'Active Status',
								dataIndex: 'activeStatus'
							},
							{
								flex: 1,
								text: 'Username',
								dataIndex: 'username'
							},
							{
								flex: 1.5,
								text: 'First Name',
								dataIndex: 'firstName'
							},
							{
								flex: 1.5,
								text: 'Last Name',
								dataIndex: 'lastName'
							},
							{
								flex: 1.5,
								text: 'Organization',
								dataIndex: 'organization'
							},
							{
								flex: 1.5,
								text: 'User Type',
								dataIndex: 'userTypeCode',
								renderer: function (value, metaData, record) {
									return record.get('userTypeDescription');
								}
							},
							{
								flex: 2,
								text: 'Last Login',
								dataIndex: 'lastLoginDts',
								xtype: 'datecolumn',
								format: 'm/d/y H:i:s',
								sortable: false
							},
							{
								flex: 3,
								text: 'Email',
								dataIndex: 'email'
							},
							{
								hidden: true,
								flex: 1,
								text: 'Phone',
								dataIndex: 'phone'
							},
							{
								flex: 3,
								text: 'GUID',
								dataIndex: 'guid',
								sortable: false
							},
							{
								flex: 2,
								text: 'Send Change Emails',
								dataIndex: 'notifyOfNew',
								align: 'center',
								renderer: function (value, meta, record) {
									if (value) {
										meta.tdCls = 'alert-success';
										return '<i class="fa fa-lg fa-check"></i>';
									} else {
										meta.tdCls = 'alert-danger';
										return '<i class="fa fa-lg fa-close"></i>';
									}
								}
							}
						]
					},
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [
								{
									xtype: 'fieldset',
									title: 'Filter',
									layout: 'hbox',
									items: [
										Ext.create('OSF.component.StandardComboBox', {
											id: 'userProfileGrid-filter-ActiveStatus',											
											fieldLabel: 'Active Status',
											emptyText: '',
											name: 'activeStatus',
											value: 'A',
											editable: false,
											typeAhead: false,
											margin: '0 0 10 0',
											listeners: {
												change: function (filter, newValue, oldValue, opts) {
													if (newValue) {												
														userProfileStore.loadPage(1);

														Ext.getCmp('userProfileGrid').getSelectionModel().deselectAll();
														Ext.getCmp('userProfileGrid-tools-edit').disable();
														Ext.getCmp('userProfileGrid-tools-toggleActivation').disable();
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
															code: 'ALL',
															description: 'All'
														},
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
									xtype: 'fieldset',
									title: 'Search',
									layout: 'hbox',
									items: [
										
										Ext.create('OSF.component.StandardComboBox', {
											id: 'userProfileGrid-filter-SearchField',
											fieldLabel: 'Select Field',
											emptyText: '',
											name: 'searchField',											
											value: 'username',
											editable: false,
											typeAhead: false,
											margin: '0 5 10 0',
											storeConfig: {
												customStore: {
													fields: [
														'code',
														'description'
													],
													data: [
														{
															code: 'username',
															description: 'Username'
														},
														{
															code: 'firstName',
															description: 'First Name'
														},
														{
															code: 'lastName',
															description: 'Last Name'
														},
														{
															code: 'organization',
															description: 'Organization'
														},
														{
															code: 'email',
															description: 'Email'
														}														
													]
												}
											},
											listeners: {

												change: function(filter, newValue, oldValue, opts) {

													// Get Search Value Field Value
													var searchValue = Ext.getCmp('userProfileGrid-filter-SearchValue').getValue();

													// Check If Field Is Empty
													if (searchValue != null && searchValue != "") {

														userProfileStore.loadPage(1);
													}
												}
											}
										}),
										{
											xtype: 'textfield',
											id: 'userProfileGrid-filter-SearchValue',
											fieldLabel: 'Enter Value',
											labelAlign: 'top',
											labelSeparator: '',
											name: 'searchValue',
											margin: '0 0 10 5',
											listeners: {

												change: {

													buffer: 1000,
													fn: function() {

														userProfileStore.loadPage(1);
													}
												}
											}
										}
									]
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
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-vertical-correction icon-button-color-refresh',
									handler: function () {
										userProfileStore.load();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Edit',
									id: 'userProfileGrid-tools-edit',
									disabled: true,
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
									requiredPermissions: ['ADMIN-USER-MANAGEMENT-PROFILES-UPDATE'],
									handler: function () {
										var record = Ext.getCmp('userProfileGrid').getSelection()[0];
										actionEditUser(record);
									}
								},
								{
									requiredPermissions: ['ADMIN-USER-MANAGEMENT-PROFILES-UPDATE', 'ADMIN-MESSAGE-MANAGEMENT-CREATE'],
									xtype: 'tbseparator'
								},
								{
									// For cryptic reasons, we must add a space before message
									// to get proper spacing for this button
									text: '&nbsp;Message',
									id: 'userProfileGrid-tools-message',
									hidden: true,
									disabled: true,
									scale: 'medium',
									width: '130px',
									iconCls: 'fa fa-2x fa-envelope-o icon-vertical-correction icon-button-color-save',
									iconAlign: 'left',
									requiredPermissions: ['ADMIN-MESSAGE-MANAGEMENT-CREATE'],
									handler: function () {
										var records = Ext.getCmp('userProfileGrid').getSelection();
										actionMessageUser(records);
									}
								},
								{
									text: 'Toggle Status',
									id: 'userProfileGrid-tools-toggleActivation',
									iconCls: 'fa fa-2x fa-power-off icon-button-color-default',
									disabled: true,
									scale: 'medium',
									tooltip: 'Activates/Deactivates',
									requiredPermissions: ['ADMIN-USER-MANAGEMENT-PROFILES-UPDATE'],
									handler: function () {
										var records = Ext.getCmp('userProfileGrid').getSelection();
										if (records.length > 1) {
											actionToggleUsers(records);
										} else {
											actionToggleUser(records[0]);
										}
									}
								},								
								{
									requiredPermissions: ['ADMIN-DATA-IMPORT-EXPORT'],
									xtype: 'tbfill'
								},
								{
									text: 'Export',
									scale: 'medium',
									id: 'userProfileGrid-tools-export',
									width: '150px',
									iconCls: 'fa fa-2x fa-download icon-button-color-default icon-vertical-correction-edit',
									requiredPermissions: ['ADMIN-DATA-IMPORT-EXPORT'],
									menu: [
										{
											text: 'All Profiles',
											id: 'userProfileGrid-tools-export-all',
											iconCls: 'fa fa-user icon-small-vertical-correction',
											handler: function() {
												var records = userProfileGrid.getStore().getData().items;
												actionExportUser([]);
											}
										},
										{
											text: 'Profiles on Current Page',
											id: 'userProfileGrid-tools-export-shown',
											iconCls: 'fa fa-user icon-small-vertical-correction',
											handler: function() {
												var records = userProfileGrid.getStore().getData().items;
												actionExportUser(records);
											}
										},
										{
											text: 'Selected Profiles',
											id: 'userProfileGrid-tools-export-selected',
											iconCls: 'fa fa-user icon-small-vertical-correction',
											disabled: true,
											handler: function() {
												var records = userProfileGrid.getSelection();
												actionExportUser(records);
											}
										}
									]
								}
							]
						},
						{
							xtype: 'pagingtoolbar',
							dock: 'bottom',
							store: 'userProfileStore',
							displayInfo: true
						}
					],
					listeners: {
						selectionchange: function (grid, record, index, opts) {
							if (Ext.getCmp('userProfileGrid').getSelectionModel().getCount() === 1) {
								Ext.getCmp('userProfileGrid-tools-export-selected').enable();
								Ext.getCmp('userProfileGrid-tools-toggleActivation').enable();
								// Only allow editing or messaging when the grid is showing active users.
								if (Ext.getCmp('userProfileGrid-filter-ActiveStatus').getValue() === 'A') {
									Ext.getCmp('userProfileGrid-tools-edit').enable();
									Ext.getCmp('userProfileGrid-tools-message').enable();
								}
							} else {
								Ext.getCmp('userProfileGrid-tools-toggleActivation').disable();
								Ext.getCmp('userProfileGrid-tools-edit').disable();
								Ext.getCmp('userProfileGrid-tools-message').disable();
								if (Ext.getCmp('userProfileGrid').getSelectionModel().getCount() > 1) {
									Ext.getCmp('userProfileGrid-tools-export-selected').enable();
									Ext.getCmp('userProfileGrid-tools-toggleActivation').enable();
									if (Ext.getCmp('userProfileGrid-filter-ActiveStatus').getValue() === 'A') {
										Ext.getCmp('userProfileGrid-tools-message').enable();
									}
								} else {
									Ext.getCmp('userProfileGrid-tools-export-selected').disable();
									Ext.getCmp('userProfileGrid-tools-message').disable();
								}
							}
						}
					}
				});

				var actionToggleUser = function actionToggleUser(record) {
					if (record) {
						var active = record.data.activeStatus;
						var username = record.data.username;
						if (active === 'A') {
							var method = "DELETE";
							var url = 'api/v1/resource/userprofiles/';
							url += username;
							var what = "deactivate";
						} else if (active === 'I') {
							var method = "PUT";
							var url = 'api/v1/resource/userprofiles/';
							url += username + '/reactivate';
							var what = "activate";
						} else {
							Ext.MessageBox.alert("Record Not Recognized", "Error: Record is not active or inactive.");
							return false;
						}

						Ext.Ajax.request({
							url: url,
							method: method,
							success: function (response, opts) {
								var message = 'Successfully ' + what + 'd user "' + username + '"';
								Ext.toast(message, '', 'tr');
								Ext.getCmp('userProfileGrid').getStore().load();
								Ext.getCmp('userProfileGrid').getSelectionModel().deselectAll();
								Ext.getCmp('userProfileGrid-tools-toggleActivation').disable();
								Ext.getCmp('userProfileGrid-tools-edit').disable();
							},
							failure: function (response, opts) {
								Ext.MessageBox.alert('Failed to' + what,
										"Error: Could not " + what + ' user "' + username + '"');
							}
						});

					} else {
						Ext.MessageBox.alert("No User Selected", "Error: You have not selected a user.");
					}
				};


				var actionEditUser = function actionEditUser(record) {
					if (record) {
						var userProfileWin = Ext.create('OSF.component.UserProfileWindow', {
							closeMethod: 'destroy',
							loadUser: record.data.username,
							saveCallback: function () {
								Ext.getCmp('userProfileGrid').getStore().load();
							}
						}).show();
					} else {
						Ext.MessageBox.alert("No User Selected", "Error: You have not selected a user.");
					}
				};

				addComponentToMainViewPort(userProfileGrid);

				var actionMessageUser = function actionMessageUser(records) {
					if (records) {
						var emails = [];
						Ext.Array.each(records, function (record) {
							emails.push(record.getData().email);
						});
						if (emails.length > 1) {
							var msg = 'All recipients inside the "To" box are able to see the e-mail addresses of all other recipients ';
							msg += 'in the "To" box. If you wish to keep the e-mail addresses hidden from the group, use the "BCC" box.';
							msg += '<br /><br /> Please select which box the selected users should appear in.';
							Ext.MessageBox.show({
								title: 'Choose E-mail Method for Selected Users',
								msg: msg,
								buttonText: {yes: "Use  'To'  Box", no: "Use  'BCC'  Box"},
								fn: function (btn) {
									if (btn === 'yes') {
										var messageWindow = Ext.create('OSF.component.MessageWindow', {
											closeAction: 'destroy',
											initialToUsers: emails
										}).show();
									} else if (btn === 'no') {
										var messageWindow = Ext.create('OSF.component.MessageWindow', {
											closeAction: 'destroy',
											initialBccUsers: emails
										}).show();
									}
								}
							});
						} else if (emails.length === 1) {
							var messageWindow = Ext.create('OSF.component.MessageWindow', {
								closeAction: 'destroy',
								initialToUsers: emails
							}).show();
						}
					} else {
						Ext.MessageBox.alert("No User Selected", "Error: You have not selected a user.");
					}
				};

				var actionExportUser = function actionExportUser(records) {
					
					// Initialize Export IDs
					var userIdInputs = "";
					
					// Loop Through Records
					Ext.Array.each(records, function (record) {
						
						// Add Username To Form
						userIdInputs += '<input type="hidden" name="userId" ';
						userIdInputs += 'value="' + record.get('username') + '" />';
					});
					
					// Get CSRF Token From Cookie
					var token = Ext.util.Cookies.get('X-Csrf-Token');
					
					// Ensure CSRF Token Is Available
					if (token) {
						
						// Add CSRF Token To Form
						userIdInputs += '<input type="hidden" name="X-Csrf-Token" ';
						userIdInputs += 'value="' + token + '" />';
					}
					
					// Set Form
					document.getElementById('exportFormUserIds').innerHTML = userIdInputs;
					
					// Submit Form
					document.exportForm.submit();
				};

				var actionToggleUsers = function actionToggleUsers(records) {
					var active = Ext.getCmp('userProfileGrid-filter-ActiveStatus').getValue();
					var url = 'api/v1/resource/userprofiles/multiple';
					if (active === 'A') {
						var method = "DELETE";
						var what = "deactivate";
					} else if (active === 'I') {
						var method = "PUT";
						var what = "activate";
					} else {
						Ext.MessageBox.alert("Failed", "Failed to toggle status.");
						return false;
					}

					var users = [];
					Ext.Array.each(records, function (record) {
						users.push(record.get('username'));
					});

					Ext.Ajax.request({
						url: url,
						method: method,
						jsonData: users,
						success: function (response, opts) {
							var message = 'Successfully ' + what + 'd users';
							Ext.toast(message, '', 'tr');
							Ext.getCmp('userProfileGrid').getStore().load();
							Ext.getCmp('userProfileGrid').getSelectionModel().deselectAll();
							Ext.getCmp('userProfileGrid-tools-toggleActivation').disable();
							Ext.getCmp('userProfileGrid-tools-edit').disable();
						},
						failure: function (response, opts) {
							Ext.MessageBox.alert('Failed to ' + what,
									"Error: Could not " + what + ' users');
						}
					});
				}
			});

		</script>
	</stripes:layout-component>
</stripes:layout-render>
