
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../../client/layout/adminlayout.jsp">
	<stripes:layout-component name="contents">

		<script src="scripts/component/userProfileWindow.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/messageWindow.js?v=${appVersion}" type="text/javascript"></script>

		<form name="exportForm" action="/openstorefront/api/v1/resource/userprofiles/export" method="POST">
			<p style="display: none;" id="exportFormUserIds"></p>
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
							property: 'eventDts',
							direction: 'DESC'
						})
					],
					proxy: CoreUtil.pagingProxy({
						id: 'userProfileStoreProxy',
						type: 'ajax',
						reader: {
							type: 'json',
							rootProperty: 'data',
							totalProperty: 'totalNumber'
						},
						url: '/openstorefront/api/v1/resource/userprofiles?status=A'
					})
				});

				var userTypeStore = Ext.create('Ext.data.Store', {
					storeId: 'userTypeStore',
					autoLoad: true,
					fields: ['code', 'description'],
					proxy: {type: 'ajax', url: '../api/v1/resource/lookuptypes/UserTypeCode/view'}
				});

				var getUserType = function getUserType(code) {
					if (code)
						return userTypeStore.getData().find('code', code).data.description;
					return '';
				};


				var userProfileGrid = Ext.create('Ext.grid.Panel', {
					title: 'Manage User Profiles <i class="fa fa-question-circle"  data-qtip="A user profile represents a user in the system and contains the user\'s information."></i>',
					id: 'userProfileGrid',
					selModel: {
						selType: 'checkboxmodel'
					},
					store: userProfileStore,
					columnLines: true,
					columns: {
						defaults: {
							cellWrap: true
						},
						items: [
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
									return getUserType(value);
								}
							},
							{
								flex: 2,
								text: 'Last Login', 
								dataIndex: 'lastLoginDts',
								xtype: 'datecolumn',
								format: 'm/d/y H:i:s'
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
								flex: 5,
								text: 'GUID', 
								dataIndex: 'guid',
								sortable: false
							}
						]
					},
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [
								Ext.create('OSF.component.StandardComboBox', {
									id: 'userProfileGrid-filter-ActiveStatus',
									emptyText: 'Active',
									fieldLabel: 'Active Status',
									name: 'activeStatus',
									value: 'A',
									listeners: {
										change: function (filter, newValue, oldValue, opts) {
											if (newValue) {
												var store = userProfileStore;
												var url = '/openstorefront/api/v1/resource/userprofiles?';
												if (newValue === 'A') {
													url += 'status=A';
													Ext.getCmp('userProfileGrid-tools-toggleActivation').setText("Deactivate");
												} else {
													url += 'status=I';
													Ext.getCmp('userProfileGrid-tools-toggleActivation').setText("Activate");
												}
												store.setProxy({
													type: 'ajax',
													url: url,
													reader: {
														type: 'json',
														rootProperty: 'data'
													}
												});
												store.load();
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
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-refresh icon-vertical-correction',
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
									iconCls: 'fa fa-2x fa-edit',
									handler: function () {
										var record = Ext.getCmp('userProfileGrid').getSelection()[0];
										actionEditUser(record);
									}
								},
								{
									text: 'Deactivate',
									id: 'userProfileGrid-tools-toggleActivation',
									iconCls: 'fa fa-2x fa-power-off',
									disabled: true,
									scale: 'medium',
									handler: function () {
										var record = Ext.getCmp('userProfileGrid').getSelection()[0];
										actionToggleUser(record);
									}
								},
								{
									// For cryptic reasons, we must add a space before message
									// to get proper spacing for this button
									text: '&nbsp;Message',
									id: 'userProfileGrid-tools-message',
									disabled: true,
									scale: 'medium',
									iconCls: 'fa fa-2x fa-envelope-o icon-vertical-correction',
									iconAlign: 'left',
									handler: function () {
										var records = Ext.getCmp('userProfileGrid').getSelection();
										actionMessageUser(records);
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Export',
									scale: 'medium',
									id: 'userProfileGrid-tools-export',
									iconCls: 'fa fa-2x fa-download',
									disabled: true,
									handler: function() {
										var records = userProfileGrid.getSelection();
										actionExportUser(records);
									}
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
								Ext.getCmp('userProfileGrid-tools-toggleActivation').enable();
								Ext.getCmp('userProfileGrid-tools-export').enable();
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
									Ext.getCmp('userProfileGrid-tools-export').enable();
									if (Ext.getCmp('userProfileGrid-filter-ActiveStatus').getValue() === 'A') {
										Ext.getCmp('userProfileGrid-tools-message').enable();
									}
								} else {
									Ext.getCmp('userProfileGrid-tools-export').disable();
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
							var url = '/openstorefront/api/v1/resource/userprofiles/';
							url += username;
							var what = "deactivate";
						} else if (active === 'I') {
							var method = "PUT";
							var url = '/openstorefront/api/v1/resource/userprofiles/';
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
							saveCallback: function() {
								Ext.getCmp('userProfileGrid').getStore().load();
							}
						}).show();
					} else {
						Ext.MessageBox.alert("No User Selected", "Error: You have not selected a user.");
					}
				};

				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [
						userProfileGrid
					]
				});


				var actionMessageUser = function actionMessageUser(records) {
					if (records) {
						var emails = [];
						Ext.Array.each(records, function(record) {
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
								fn: function(btn) {
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
					var userIdInputs = "";
					Ext.Array.each(records, function(record) {
						userIdInputs += '<input type="hidden" name="userId" ';
						userIdInputs += 'value="' + record.get('username') +'" />';
					});
					document.getElementById('exportFormUserIds').innerHTML = userIdInputs;
					document.exportForm.submit();
				};

			});

		</script>
	</stripes:layout-component>
</stripes:layout-render>
