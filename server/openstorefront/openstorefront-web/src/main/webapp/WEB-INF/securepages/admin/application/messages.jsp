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

				//
				//
				//  USER MESSAGE PANEL
				//
				//

				var userMessageGridStore = Ext.create('Ext.data.Store', {
					autoLoad: true,
					pageSize: 100,
					remoteSort: true,
					sorters: [
						new Ext.util.Sorter({
							property: 'updateDts',
							direction: 'DESC'
						})
					],	
					fields: [
						{
							name: 'createDts',
							type:	'date',
							dateFormat: 'c'
						},						
						{
							name: 'updateDts',
							type:	'date',
							dateFormat: 'c'
						}
					],
					proxy: CoreUtil.pagingProxy({
						url: 'api/v1/resource/usermessages',											
						reader: {
							type: 'json',
							rootProperty: 'data',
							totalProperty: 'totalNumber'
						}
					}),
					listeners: {
						beforeLoad: function(store, operation, eOpts){
							store.getProxy().extraParams = {
								'status': Ext.getCmp('userMessageFilter-ActiveStatus').getValue() ? Ext.getCmp('userMessageFilter-ActiveStatus').getValue() : 'A'
							};
						}
					}
				});

				var userMessageGrid = Ext.create('Ext.grid.Panel', {
					title: '',
					id: 'userMessageGrid',
					store: userMessageGridStore,
					columnLines: true,
					bodyCls: 'border_accent',
					plugins: 'gridfilters',
					enableLocking: true,
					columns: [
						{text: 'Username / Email', dataIndex: 'username', width: 200, flex: 1, lockable: true, sortable: false,
							renderer: function (val, meta, record, rowIndex) {
								if (typeof val === "undefined") {
									if (record.get('emailAddress')) {
										return record.get('emailAddress');
									}
									else {
										return null;
									}
								}
								return val;
							}
						},
						{text: 'Message Type', align: 'center', dataIndex: 'userMessageType', width: 125},
						{text: 'Component Id / Alert Id', dataIndex: 'alertId', width: 200,
							renderer: function (val, meta, record, rowIndex) {
								if (typeof val === "undefined") {
									if (record.get('componentId')) {
										return record.get('componentId');
									}
									else {
										return null;
									}
								}
								mCheckNavButtons();
								return val;
							}
						},
						{text: 'Status', dataIndex: 'activeStatus', width: 75},
						{text: 'Create Date', dataIndex: 'createDts', width: 125, xtype: 'datecolumn', format: 'm/d/y H:i:s'},
						{text: 'Update Date', dataIndex: 'updateDts', width: 125, xtype: 'datecolumn', format: 'm/d/y H:i:s'},						
						{text: 'Subject', dataIndex: 'subject', width: 150},
						{text: 'Sent Email Address', dataIndex: 'sentEmailAddress', width: 200, flex: 1}
					],
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [
								Ext.create('OSF.component.StandardComboBox', {
									id: 'userMessageFilter-ActiveStatus',
									fieldLabel: 'Active Status',
									name: 'activeStatus',
									emptyText: '',
									value: 'A',
									listeners: {
										change: function (filter, newValue, oldValue, opts) {
											mRefreshGrid();
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
							flex: 1,
							width: '100%',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									id: 'mRefreshButton',
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									tip: 'Refresh the list of messages',
									handler: function () {
										mRefreshGrid();
									},
									tooltip: 'Refresh the list of messages'
								},
								{
									xtype: 'tbseparator',
									requiredPermissions: ['ADMIN-MESSAGE-MANAGEMENT-READ']
								},
								{
									text: 'View',
									id: 'mViewButton',
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction-view',
									disabled: true,
									requiredPermissions: ['ADMIN-MESSAGE-MANAGEMENT-READ'],
									handler: function () {
										mViewMessage();
									},
									tooltip: 'View the message data'
								},
								{
									xtype: 'tbfill'

								},
								{
									text: 'Process Messages Now',
									scale: 'medium',
									id: 'mProcessNowButton',
									iconCls: 'fa fa-2x fa-bolt icon-button-color-run icon-vertical-correction',
									disabled: false,
									requiredPermissions: ['ADMIN-MESSAGE-MANAGEMENT-UPDATE'],
									handler: function () {
										mProcessMessagesNow();
									},
									tooltip: 'Process all active messages and send them immediately.'

								},
								{
									text: 'Cleanup Old Messages Now',
									id: 'mCleanUpNowButton',
									scale: 'medium',
									width: '250px',
									iconCls: 'fa fa-2x fa-eraser icon-button-color-default icon-vertical-correction-eraser',
									disabled: false,
									requiredPermissions: ['ADMIN-MESSAGE-MANAGEMENT-UPDATE'],
									handler: function () {
										mCleanupOldMessagesNow();
									},
									tooltip: 'Deletes old archive data now. Default is 28 days.'

								},
								{
									xtype: 'tbseparator',
									requiredPermissions: ['ADMIN-MESSAGE-MANAGEMENT-DELETE']
								},
								{
									text: 'Delete',
									id: 'mDeleteButton',
									cls: 'alert-danger',
									scale: 'medium',
									width: '110px',
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									disabled: true,
									requiredPermissions: ['ADMIN-MESSAGE-MANAGEMENT-DELETE'],
									handler: function () {
										mDeleteMessage();
									},
									tooltip: 'Delete a message'
								}
							]
						},
						{
							xtype: 'pagingtoolbar',
							dock: 'bottom',
							store: userMessageGridStore,
							displayInfo: true
						}
					],
					listeners: {
						itemdblclick: function (grid, record, item, index, e, opts) {
							mViewMessage();
						},
						selectionchange: function (grid, record, index, opts) {
							mCheckNavButtons();
						}
					}
				});


				//
				//   USER MESSAGE FUNCTIONS
				//

				//
				// Check which buttons should be on and which should be off
				//
				var mCheckNavButtons = function () {
					var cnt = userMessageGrid.getSelectionModel().getCount();
					if (cnt === 1) {
						Ext.getCmp('mViewButton').setDisabled(false);
						Ext.getCmp('mDeleteButton').setDisabled(false);

					} else if (cnt > 1) {
						Ext.getCmp('mViewButton').setDisabled(true);
						Ext.getCmp('mDeleteButton').setDisabled(true);

					} else {
						Ext.getCmp('mViewButton').setDisabled(true);
						Ext.getCmp('mDeleteButton').setDisabled(true);
					}
				};

				//
				//  Refresh and reload the grid
				//
				var mRefreshGrid = function () {
					Ext.getCmp('userMessageGrid').getStore().load({
						params: {
							status: Ext.getCmp('userMessageFilter-ActiveStatus').getValue() ? Ext.getCmp('userMessageFilter-ActiveStatus').getValue() : 'A'
						}
					});
				};

				//
				//  Delete a message
				//
				var mDeleteMessage = function () {

					var selectedObj = Ext.getCmp('userMessageGrid').getSelection()[0];

					Ext.Msg.show({
						title: 'Delete Message?',
						message: 'Are you sure you want to delete the selected message?',
						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						fn: function (btn) {
							if (btn === 'yes') {
								Ext.getCmp('userMessageGrid').setLoading(true);
								var messageId = selectedObj.data.userMessageId;

								Ext.Ajax.request({
									url: 'api/v1/resource/usermessages/' + messageId,
									method: 'DELETE',
									success: function (response, opts) {
										Ext.getCmp('userMessageGrid').setLoading(false);
										mRefreshGrid();
									},
									failure: function (response, opts) {
										Ext.getCmp('userMessageGrid').setLoading(false);
									}
								});
							}
						}
					});
				};

				//
				// View Message
				//
				var mViewMessage = function (whichone) {

					var record = Ext.getCmp('userMessageGrid').getSelectionModel().getSelection()[0];


					var message = "No message was generated or sent.";
					if (record.get('subject')) {
						message = "<b>Subject:</b><br><br>" + record.get('subject') + "<br><br>";
						if (record.get('bodyOfMessage')) {
							message += "<b>Message:</b><br><br> " + record.get('bodyOfMessage');
						}						
					}
					
					Ext.create('Ext.window.Window', {
						title: 'View Message',
						iconCls: 'fa fa-lg fa-eye icon-small-vertical-correction',
						width: '30%',
						height: '70%',
						bodyStyle: 'padding: 10px;',
						y: 40,
						modal: true,
						maximizable: false,
						closeAction: 'destroy',						
						autoScroll: true,
						listeners:	{
							show: function() {        
								this.removeCls("x-unselectable");    
							}
						},							
						html: message
					}).show();
				};

				//
				// Process Messages Now
				//
				var mProcessMessagesNow = function () {
					Ext.toast('Processing Messages...');
					Ext.Ajax.request({
						url: 'api/v1/resource/usermessages/processnow',
						method: 'POST',
						success: function (response, opts) {
							mRefreshGrid();

						}
					});
				};

				//
				// Cleanup Messages Now
				//
				var mCleanupOldMessagesNow = function () {
					Ext.toast('Cleaning Up Old Messages...');
					Ext.Ajax.request({
						url: 'api/v1/resource/usermessages/cleanold',
						method: 'POST',
						success: function (response, opts) {
							Ext.toast('Successful Cleaning Up Old Messages');
							mRefreshGrid();
						}
					});

				};
				//
				//
				//  NOTIFICATIONS PANEL
				//
				//

				var notificationsGridStore = Ext.create('Ext.data.Store', {
					id: 'notificationsGridStore',
					autoLoad: false,
					pageSize: 100,
					remoteSort: true,
					sorters: [
						new Ext.util.Sorter({
							property: 'username',
							direction: 'ASC'
						})
					],
					fields: [
						{
							name: 'updateDts',
							type:	'date',
							dateFormat: 'c'
						}
					],					
					proxy: CoreUtil.pagingProxy({
						url: 'api/v1/resource/notificationevent/all',
						method: 'GET',
						reader: {
							type: 'json',
							rootProperty: 'data',
							totalProperty: 'totalNumber'
						}
					})
				});

				var notificationsGrid = Ext.create('Ext.grid.Panel', {
					title: '',
					id: 'notificationsGrid',
					store: notificationsGridStore,
					columnLines: true,
					bodyCls: 'border_accent',
					plugins: 'gridfilters',
					enableLocking: true,
					columns: [
						{text: 'Target User', dataIndex: 'username', width: 150, lockable: true,
							renderer: function (val, meta, record, rowIndex) {

								if (typeof val === "undefined") {
									return 'All';
								}
								else
								{
									return val;
								}
							}
						},
						{text: 'Message', dataIndex: 'message', width: 200, flex: 1},
						{text: 'Event Type', dataIndex: 'eventType', width: 150,
							renderer: function(value, metaData, record, rowIndex) {
								
								// Set Tooltip
								metaData.tdAttr = 'data-qtip="' + record.get('eventTypeDescription') + '"';
								
								// Simply Return Value
								return value;
							}
						},
						{
							text: 'Update Date', 
							dataIndex: 'updateDts',
							width: 150,
							xtype: 'datecolumn', 
							format: 'm/d/y H:i:s'
						}
					],
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									id: 'nRefreshButton',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh',
									tooltip: 'Refresh Notifications',
									handler: function () {
										nRefresh();
									}
								},
								{
									xtype: 'tbseparator',
									requiredPermissions: ['ADMIN-MESSAGE-MANAGEMENT-CREATE']
								},
								{
									text: 'Create Admin Message',
									scale: 'medium',
									id: 'nAdminMessageButton',
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction',
									tooltip: 'Create admin message',
									requiredPermissions: ['ADMIN-NOTIFICATION-EVENT-CREATE'],
									handler: function () {
										nAdminMessage();
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									id: 'nDeleteButton',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									disabled: true,
									requiredPermissions: ['ADMIN-NOTIFICATION-EVENT-DELETE'],
									handler: function () {
										nDelete();
									}
								},
							]
						},
						{
							xtype: 'pagingtoolbar',
							dock: 'bottom',
							store: notificationsGridStore,
							displayInfo: true
						}
					],
					listeners: {
						selectionchange: function (grid, record, index, opts) {
							nCheckNavButtons();
						}
					}
				});



				//
				//   NOTIFICATION FUNCTIONS
				//

				//
				// Check which buttons should be on and which should be off
				//
				var nCheckNavButtons = function () {

					if (notificationsGrid.getSelectionModel().getCount() === 1) {
						Ext.getCmp('nDeleteButton').setDisabled(false);

					} else if (notificationsGrid.getSelectionModel().getCount() > 1) {
						Ext.getCmp('nDeleteButton').setDisabled(true);
					} else {
						Ext.getCmp('nDeleteButton').setDisabled(true);
					}
				};

				//
				//  Refresh and reload the grid
				//
				var nRefresh = function () {

					Ext.getCmp('notificationsGrid').getStore().load({
						params: {
							status: 'A'
						}
					});
				};

				//
				//  Send an Admin Message Window
				//
				var nAdminMessage = function () {

					var sendAdminMsgWin = Ext.create('Ext.window.Window', {
						id: 'sendAdminMsgWin',
						title: 'Send Admin Message',
						iconCls: 'fa fa-envelope-o',
						width: '30%',
						minHeight: 350,
						bodyStyle: 'padding: 10px;',
						y: 40,
						modal: true,
						maximizable: false,
						closeAction: 'destroy',
						layout: 'vbox',
						items: [
								{
									xtype: 'UserSingleSelectComboBox',
									id: 'username_combo',
									name: 'username',
									valueField: 'code',
									fieldLabel: 'Send to',
									addAll: true,
									value: 'All Users',
									width: '100%',
									labelAlign: 'top',
									forceSelection: true
								},
							{
								xtype: 'textareafield',
								id: 'message_adm',
								name: 'message',
								fieldLabel: 'Message',
								width: '100%',
								height: 200,
								maxLength: 300
							}],
						dockedItems: [
							{
								dock: 'bottom',
								xtype: 'toolbar',
								items: [
									{
										text: 'Send',
										formBind: true,
										iconCls: 'fa fa-lg fa-envelope-o icon-button-color-save',
										handler: function () {
											var msgtosend = {};

											if (Ext.getCmp('username_combo').value) {
												msgtosend.username = Ext.getCmp('username_combo').value;
											}

											msgtosend.message = Ext.getCmp('message_adm').getValue();

											if (msgtosend.message === '' || msgtosend.message === 'Message Required')
											{
												Ext.getCmp('message_adm').setValue('Message Required');
												return;
											}

											msgtosend.message = Ext.getCmp('message_adm').value;
											Ext.toast('Sending Admin Message..');

											Ext.Ajax.request({
												url: 'api/v1/resource/notificationevent',
												method: 'POST',
												jsonData: msgtosend,
												success: function (response, opts) {
													Ext.toast('Admin Message Sent Successfully');
													Ext.getCmp('notificationsGrid').getStore().load();
													Ext.getCmp('sendAdminMsgWin').close();
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
											Ext.getCmp('sendAdminMsgWin').close();
										}
									}
								]
							}
						]
					}).show();
				};

				//
				// Delete Notification
				//
				var nDelete = function () {

					var selectedObj = Ext.getCmp('notificationsGrid').getSelection()[0];
					Ext.Msg.show({
						title: 'Delete Notification?',
						iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
						message: 'Are you sure you want to delete the selected notification?',
						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						fn: function (btn) {
							if (btn === 'yes') {
								Ext.getCmp('notificationsGrid').setLoading(true);
								var eventId = selectedObj.data.eventId;
								Ext.Ajax.request({
									url: 'api/v1/resource/notificationevent/' + eventId,
									method: 'DELETE',
									success: function (response, opts) {
										Ext.getCmp('notificationsGrid').setLoading(false);
										Ext.getCmp('notificationsGrid').getStore().load();
									},
									failure: function (response, opts) {
										Ext.getCmp('notificationsGrid').setLoading(false);
									}
								});
							}
						}
					});
				};

				var messagePanel = Ext.create('Ext.panel.Panel', {
					title: 'User Messages',
					iconCls: 'fa fa-lg fa-user icon-small-vertical-correction',
					layout: 'fit',
					items: [
						userMessageGrid
					]
				});

				var notificationsPanel = Ext.create('Ext.panel.Panel', {
					title: 'Event Notifications',
					iconCls: 'fa fa-lg fa-bell-o icon-small-vertical-correction',
					layout: 'fit',
					items: [
						notificationsGrid
					]
				});

				var msgTabPanel = Ext.create('Ext.tab.Panel', {
					title: 'Manage Messages &nbsp; <i class="fa fa-lg fa-question-circle"  data-qtip="User messages are queued messages from users. The primary usage for messages is from watches. This tool allows for viewing of queued messages as well as viewing of archived messages. Event Notifications are messages sent internally to user to notify them of event in the application." ></i>',
					layout: 'fit',
					items: [
						messagePanel,
						notificationsPanel
					],
					listeners: {
						tabchange: function (tabPanel, newTab, oldTab, index) {

							if (newTab.title === 'User Messages') {
								mRefreshGrid();
							}
							else if (newTab.title === 'Event Notifications') {
								nRefresh();
							}
						}
					}
				});

				addComponentToMainViewPort(msgTabPanel);
				
			});

        </script>

    </stripes:layout-component>
</stripes:layout-render>
