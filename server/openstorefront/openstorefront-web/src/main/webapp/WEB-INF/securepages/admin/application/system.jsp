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

				var tplSystemDetailStats = new Ext.XTemplate(
					'<div style="padding:10px;"><ul class="list-group">',
					'<li class="stat-list-group-item">Application Version: <span class="stat-badge">{applicationVersion}</span></li>',
					'<li class="stat-list-group-item">Uptime: <span class="stat-badge">{upTime}</span></li>',
					'<li class="stat-list-group-item">Start Time: <span class="stat-badge">{startTime}</span></li>',
					'<li class="stat-list-group-item">Disk Space: <span class="stat-badge">{freeDiskSpace} MB / {totalDiskSpace} MB</span></li>',
					'</ul><div>'
				);

				var tplSystemDetailStats2 = new Ext.XTemplate(
					'<div style="padding:10px;"><ul class="list-group">',
					'<li class="stat-list-group-item">Processor Count: <span class="stat-badge">{processorCount}</span></li>',
					'<li class="stat-list-group-item">Live Threads / Total Threads: <span class="stat-badge">{liveThreadCount}/{totalThreadCount}</span></li>',
					'<li class="stat-list-group-item">System Load: <span class="stat-badge">{systemLoad}</span></li>',
					'<li class="stat-list-group-item">Root Storage Path: <span class="stat-badge">{rootStoragePath}</span></li>',
					'</ul><div>'

				);

				var tplGarbageCollection = new Ext.XTemplate(
					'<ul style="padding: 10px;"><tpl for="garbageCollectionInfos">',
					'<li><strong>{.}</strong></li>',
					'</tpl></ul>'
				);

				var memoryPanel = Ext.create('Ext.panel.Panel', {
					defaults: {
						padding: '10px'
					},
					items: [
						{ xtype: 'panel', layout: 'hbox', items: [
								{xtype: 'label', html: '<strong>Heap Memory</strong>', flex: 1},
								{
									xtype: 'progressbar',
									padding: '0 10 0 10',
									id: 'heapMemoryBar',
									flex: 9
								}]
						},
						{ xtype: 'panel', layout: 'hbox', items: [
								{xtype: 'label', html: '<strong>Non-Heap Memory</strong>', flex: 1},
								{
									xtype: 'progressbar',
									padding: '0 10 0 10',
									id: 'nonHeapMemoryBar',
									flex: 9
								}]
						}

					]
				});

				var memoryPoolStore = Ext.create('Ext.data.Store', {
					autoLoad: true,
					storeId: 'memoryPoolStore',
					fields: [
						{
							name: "used",
							mapping: function (data) {
								var used = data.usedKb / data.maxKb;
								if (used === Infinity) used = 0;
								return used;
							}
						}
					],
					proxy: {
						id: 'memoryPoolStoreProxy',
						type: 'ajax',
						url: 'api/v1/service/application/status',
						reader: {
							type: 'json',
							rootProperty: 'memoryPools'
						}
					}
				});

				var memoryPoolsGrid = Ext.create('Ext.grid.Panel', {
					id: 'memoryPoolsGrid',
					width: '100%',
					collapsible: true,
					titleCollapse: true,
					style: {
						padding: '10px'
					},
					viewConfig: {
						loadMask: false
					},
					title: 'Memory Pools',
					store: memoryPoolStore,
					columnsLines: true,
					columns: [
						{text: 'Name', dataIndex: 'name', flex: 1},
						{text: 'Init (K)', dataIndex: 'initKb', flex: 1},
						{text: 'Used (K)', dataIndex: 'usedKb', flex: 1},
						{text: 'Max (K)', dataIndex: 'maxKb', flex: 1},
						{text: 'Committed (K)', dataIndex: 'commitedKb', flex: 1},
						{text: '', dataIndex: 'used', flex: 1,
							xtype: 'widgetcolumn',
							widget: {
								xtype: 'progressbarwidget',
								textTpl: '{value:percent}'
							}
						}
					]
				});

				var statusStats = Ext.create('Ext.panel.Panel', {
					title: 'Statistics',
					id: 'statusStats',
					scrollable: true,					
					layout: 'anchor',
					items: [
						{
							xtype: 'panel',
							width: '100%',
							layout: {
								type: 'hbox',
								align: 'stretch'
							},
							items: [
								{
									xtype: 'panel',
									id: 'systemDetailStats',
									tpl: tplSystemDetailStats,
									flex: 1
								},
								{
									xtype: 'panel',
									id: 'systemDetailStats2',
									tpl: tplSystemDetailStats2,
									flex: 1
								}
							]
						},
						memoryPanel,
						memoryPoolsGrid,
						{
							xtype: 'panel',
							width: '100%',
							title: 'Garbage Collection',
							collapsible: true,
							titleCollapse: true,
							style: {
								padding: '10px'
							},
							id: 'garbageCollectionPanel',
							tpl: tplGarbageCollection,
							flex: 1
						}
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										actionLoadSystemData();
									}
								},
								{
									xtype: 'label',
									text: 'Automatically Update:',
									cls: 'label-text-bold'									
								},
								{
									xtype: 'segmentedbutton',
									scale: 'medium',
									items: [
										{
											enableToggle: true,
											scale: 'medium',
											toggleGroup: 'auto',
											id: 'autoYes',
											text: 'Yes',
											name: 'autoUpdate',
											handler: function () {
												Ext.TaskManager.start(systemDataUpdater);
											}
										},
										{
											enableToggle: true,
											scale: 'medium',
											toggleGroup: 'auto',
											id: 'autoNo',
											text: 'No',
											pressed: true,
											name: 'autoUpdate',
											handler: function () {
												Ext.TaskManager.stop(systemDataUpdater);
											}
										}
									]
								}
							]
						}
					]
				});
				

				var threadStatusStore = Ext.create('Ext.data.Store', {
					autoLoad: true,
					storeId: 'threadStatusStore',
					proxy: {
						id: 'threadStatusStoreProxy',
						type: 'ajax',
						url: 'api/v1/service/application/threads'
					}
				});

				var threadStatus = Ext.create('Ext.grid.Panel', {
					title: 'Threads Status',
					id: 'threadStatus',
					store: threadStatusStore,
					bufferedRenderer: false,
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										threadStatusStore.load();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'View',
									id: 'threadStatus-view',
									disabled: true,
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction-view',
									handler: function () {
										var record = Ext.getCmp('threadStatus').getSelectionModel().getSelection()[0];
										
										var detailWin = Ext.create('Ext.window.Window', {
											title: 'Full Stack - ' + record.get("name"),
											modal: true,
											closeAction: 'destroy',
											width: '80%',
											height: '80%',
											maximizable: true,
											scrollable: true,
											bodyStyle: 'padding: 20px;',
											html: ''
										});
										detailWin.show();
										
										detailWin.setLoading(true);
										Ext.Ajax.request({
											url: 'api/v1/service/application/threads/' + record.get("id") + '/stack',
											callback: function(){
												detailWin.setLoading(false);
											},
											success: function(response, opts) {
												detailWin.setHtml(response.responseText);
											},
											failure: function(response, opts) {
												detailWin.setHtml("Selected thread is no longer available.");
											}
										});
									}									
								}								
							]
						}
					],
					columnLines: true,
					columns: [
						{text: 'Thread ID', dataIndex: 'id', flex: 1},
						{text: 'Name', dataIndex: 'name', flex: 4},
						{text: 'Status', dataIndex: 'status', flex: 2},
						{text: 'Details', dataIndex: 'details', flex: 12}
					],
					listeners: {
						selectionchange: function(selectionModel, records, opts) {
							if (selectionModel.getCount() > 0) {
								Ext.getCmp('threadStatus-view').setDisabled(false);
							} else {
								Ext.getCmp('threadStatus-view').setDisabled(true);
							}
						}
					}
				});

				var systemPropertiesStore = Ext.create('Ext.data.Store', {
					autoLoad: true,
					storeId: 'systemPropertiesStore',
					proxy: {
						id: 'systemPropertiesStoreProxy',
						type: 'ajax',
						url: 'api/v1/service/application/status',
						reader: {
							type: 'json',
							rootProperty: 'systemProperties.entry'
						}
					}
				});

				var systemProperties = Ext.create('Ext.grid.Panel', {
					title: 'System Properties',
					id: 'systemProperties',
					store: systemPropertiesStore,
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										systemPropertiesStore.load();
									}
								}
							]
						}
					],
					columnLines: true,
					columns: [
						{text: 'Key', dataIndex: 'key', flex: 1},
						{text: 'Value', dataIndex: 'value', flex: 6, cellWrap: true}
					]
				});

				var statusPanel = Ext.create('Ext.tab.Panel', {
					title: 'Status',
					id: 'statusPanel',
					tabPosition: 'left',
					tabRotation: 0,
					requiredPermissions: ['ADMIN-SYSTEM-MANAGEMENT-STATUS'],
					actionOnInvalidPermission: 'destroy',
					tabBar: {
						border: false
					},
					items: [statusStats, threadStatus, systemProperties]
				});

				var errorTicketsStore = Ext.create('Ext.data.Store', {
					autoLoad: true,
					storeId: 'errorTicketsStore',
					pageSize: 100,
					remoteSort: true,
					sorters: [
						new Ext.util.Sorter({
							property: 'updateDts',
							direction: 'DESC'
						})
					],
					proxy: CoreUtil.pagingProxy({
							type: 'ajax',
							url: 'api/v1/resource/errortickets',
							reader: {
								type: 'json',
								rootProperty: 'errorTickets',
								totalProperty: 'totalNumber'
							}
						})
				});

				var errorTicketsGrid = Ext.create('Ext.grid.Panel', {
					title: 'Error Tickets',
					id: 'errorTicketsGrid',
					store: errorTicketsStore,
					plugins: 'gridfilters',
					bufferedRenderer: false,
					requiredPermissions: ['ADMIN-SYSTEM-MANAGEMENT-ERROR-TICKET'],
					actionOnInvalidPermission: 'destroy',
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										errorTicketsStore.load();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'View Details',
									id: 'errorTicketsGrid-tools-view',
									disabled: true,
									scale: 'medium',
									width: '150px',
									iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction-view',
									handler: function () {
										var record = Ext.getCmp('errorTicketsGrid').getSelection()[0];
										actionViewErrorTicket(record);
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									id: 'errorTicketsGrid-tools-delete',
									disabled: true,
									scale: 'medium',
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									handler: function () {
										var records = Ext.getCmp('errorTicketsGrid').getSelection();
										actionDeleteTickets(records);
									}									
								}
							]
						},
						{
							xtype: 'pagingtoolbar',
							dock: 'bottom',
							store: 'errorTicketsStore',
							displayInfo: true
						}
					],
					columnLines: true,
					selModel: {
						selType: 'checkboxmodel' 
					},
					columns: [
						{text: 'Ticket ID', dataIndex: 'errorTicketId', flex: 1.5, cellWrap: true,
							filter: {
								type: 'string'
							}	
						},
						{
							text: 'Update Date',
							dataIndex: 'updateDts',
							xtype: 'datecolumn',
							format: 'm/d/y H:i:s',
							flex: 1.5
						},
						{text: 'Client IP', dataIndex: 'clientIp', flex: 1},
						{text: 'Called Action', dataIndex: 'calledAction', flex: 4.3, cellWrap: true},
						{text: 'Message', dataIndex: 'message', flex: 9, cellWrap: true},
						{text: 'Type', dataIndex: 'errorTypeCode', flex: 0.7}
					],
					listeners: {
						selectionchange: function (grid, records, index, opts) {
							if (Ext.getCmp('errorTicketsGrid').getSelectionModel().hasSelection()) {
								if (records.length === 1) {
									Ext.getCmp('errorTicketsGrid-tools-view').enable();
								} else {
									Ext.getCmp('errorTicketsGrid-tools-view').disable();
								}
								Ext.getCmp('errorTicketsGrid-tools-delete').enable();
							} else {
								Ext.getCmp('errorTicketsGrid-tools-view').disable();
								Ext.getCmp('errorTicketsGrid-tools-delete').disable();
							}
						}
					}

				});
				
				var actionDeleteTickets = function(records) {
					
					
					Ext.Msg.show({
						title: 'Delete Error Ticket(s)',
						iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
						message: 'Are you sure you want to delete selected ticket(s)?',
						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						fn: function(btn) {
							if (btn === 'yes') {
								
								var ids = [];
								Ext.Array.each(records, function(record){
									ids.push(record.get('errorTicketId'));
								});
								
								errorTicketsGrid.setLoading("Removing Tickets...");
								Ext.Ajax.request({
									url: 'api/v1/resource/errortickets',
									method: 'DELETE',
									jsonData: {
										ids: ids
									},
									callback: function() {
										errorTicketsGrid.setLoading(false);
									},
									success: function(response, opts) {
										errorTicketsStore.load();
									}
								});
																
							} 
						}
					});					
					
				};

				var viewErrorTicketWindow = Ext.create('Ext.window.Window', {
					id: 'viewErrorTicketWindow',
					title: 'View Error Ticket Information',
					iconCls: 'fa fa-lg fa-info-circle',
					width: '80%',
					height: 600,
					autoScroll: true,
					bodyStyle: 'padding: 10px;',
					y: 40,
					modal: true,
					maximizable: false,
					layout: 'vbox',
					listeners: {
						show: function() {        
							this.removeCls('x-unselectable');    
						}
					},
					items: [
						{
							xtype: 'panel',
							id: 'errorTicketDetailPanel',
							tpl: new Ext.XTemplate ('<p>{info}</p>'),
							autoScroll: true,
							width: '100%',
							flex: 1
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
										xtype: 'button',
										text: 'Close',
										iconCls: 'fa fa-lg fa-close icon-button-color-warning',
										handler: function () {
											this.up('window').hide();
										}
									}							
								]
							}
						]
				});

				var actionViewErrorTicket = function actionViewErrorTicket(record) {
					Ext.Ajax.request({
						url: 'api/v1/resource/errortickets/' + record.data.errorTicketId + '/ticket',
						success: function(response, opt){
							var data = {};
							data.info = response.responseText;
							Ext.getCmp('errorTicketDetailPanel').update(data);
							viewErrorTicketWindow.show();	
						},
						failure: function(response, opt){
						}
					});
				};

				var appStatePropStore = Ext.create('Ext.data.Store', {
					autoLoad: true,
					storeId: 'appStatePropStore',
					sorters: [
						new Ext.util.Sorter({
							property: 'key',
							direction: 'ASC'
						})
					],
					proxy: {
						type: 'ajax',
						url: 'api/v1/resource/applicationproperties'
						}
				});
				
				var appStatePropGrid = Ext.create('Ext.grid.Panel', {
					title: 'Application State Properties',
					id: 'appStatePropGrid',
					store: appStatePropStore,
					requiredPermissions: ['ADMIN-SYSTEM-MANAGEMENT-APP-PROP'],
					actionOnInvalidPermission: 'destroy',
					columnLines: true,
					columns: [
						{text: 'Key', dataIndex: 'key', flex: 3},
						{text: 'Value', dataIndex: 'value', flex: 5, cellWrap: true},
						{text: 'Update User', dataIndex: 'updateUser', flex: 1},
						{
							text: 'Update Date', 
							dataIndex: 'updateDts',
							xtype: 'datecolumn',
							format: 'm/d/y H:i:s',
							flex: 2
						}
					],
					listeners: {
						selectionchange: function (grid, record, index, opts) {
							if (Ext.getCmp('appStatePropGrid').getSelectionModel().hasSelection()) {
								Ext.getCmp('appStatePropGrid-tools-edit').enable();
							}
							else {
								Ext.getCmp('appStatePropGrid-tools-edit').disable();
							}
						 }
					},
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										appStatePropStore.load();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Edit',
									id: 'appStatePropGrid-tools-edit',
									disabled: true,
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
									handler: function () {
										var record = Ext.getCmp('appStatePropGrid').getSelection()[0];
										actionEditStateProp(record);
									}
								}
							]
						}
				]
				});
				
				var editAppStatePropWin = Ext.create('Ext.window.Window', {
					id: 'editAppStatePropWin',
					title: 'Edit Application State Property',
					modal: true,
					width: '35%',
					minHeight: 300,
					y: '10em',
					iconCls: 'fa fa-lg fa-edit icon-small-vertical-correction',
					layout: 'fit',
					items: [
						{
							xtype: 'form',
							id: 'appStatePropForm',
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
									id: 'appStatePropForm-key',
									fieldLabel: 'Key <span class="field-required" />',
									name: 'key',
									readOnly: true
								},
								{
									xtype: 'textfield',
									id: 'appStatePropForm-value',
									fieldLabel: 'Value<span class="field-required" />',
									name: 'value'
								},
								{ 
									xtype: 'label',
									text: "Note: The proper syntax for the value depends upon the property to which it belongs. This is especially true for dates, which should have the format yyyy-MM-dd'T'HH:mm:ss.SSS'Z'.",
									style: {
										paddingTop: '20px'
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
											iconCls: 'fa fa-lg fa-save icon-button-color-save',
											formBind: true,	
											handler: function() {
												var key = Ext.getCmp('appStatePropForm').key;
												var url = 'api/v1/resource/applicationproperties/';
												url+= key;
												var method = 'PUT';
												var value = Ext.getCmp('appStatePropForm').getValues()['value'];
												CoreUtil.submitForm({
													url: url,
													method: method,
													data: value,
													removeBlankDataItems: true,
													form: Ext.getCmp('appStatePropForm'),
													success: function (response, opts) {
														Ext.getCmp('appStatePropGrid-tools-edit').disable();
														Ext.getCmp('appStatePropForm').reset();
														Ext.getCmp('editAppStatePropWin').hide();
														appStatePropStore.load();
														Ext.toast('Successfully saved property.', '', 'tr');
													},
													failure: function (response, opts) {
														Ext.toast('Failed to save property.', '', 'tr');
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
												Ext.getCmp('appStatePropForm').reset();
												Ext.getCmp('editAppStatePropWin').hide();
											}
										}
									]
								}
							]
						}
					]
				});

				var actionEditStateProp = function actionEditStateProp(record) {
					editAppStatePropWin.show();
					var form = Ext.getCmp('appStatePropForm');
					form.loadRecord(record);
					form.key = record.data.key;
				};

				var sysConfigPropStore = Ext.create('Ext.data.Store', {
					id: 'sysConfigPropStore',
					autoLoad: true,
					sorters: [
						new Ext.util.Sorter({
							property: 'code',
							direction: 'ASC'
						})
					],
					proxy: {
						type: 'ajax',
						url: 'api/v1/service/application/configproperties'
					},
					listeners: {
						load: function () {
							updateDbLoggerStatus(false);
						}
					}
				});

				var sysConfigPropGrid = Ext.create('Ext.grid.Panel', {
					title: 'System Configuration Properties',
					id: 'sysConfigPropGrid',
					store: sysConfigPropStore,
					requiredPermissions: ['ADMIN-SYSTEM-MANAGEMENT-CONFIG-PROP-READ'],
					actionOnInvalidPermission: 'destroy',
					columnLines: true,
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										sysConfigPropStore.load();
									}
								},
								{
									xtype: 'tbseparator',
									requiredPermissions: ['ADMIN-SYSTEM-MANAGEMENT-CONFIG-PROP-UPDATE']
								},
								{
									text: 'Add',
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction',
									requiredPermissions: ['ADMIN-SYSTEM-MANAGEMENT-CONFIG-PROP-UPDATE'],
									handler: function() {
										addSysConfigProp();
									}
								},
								{
									text: 'Edit',
									id: 'sysConfigPropGrid-tools-edit',
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
									disabled: true,
									requiredPermissions: ['ADMIN-SYSTEM-MANAGEMENT-CONFIG-PROP-UPDATE'],
									handler: function() {
										var record = Ext.getCmp('sysConfigPropGrid').getSelection()[0];
										editSysConfigProp(record);
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									id: 'sysConfigPropGrid-tools-delete',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									disabled: true,
									requiredPermissions: ['ADMIN-SYSTEM-MANAGEMENT-CONFIG-PROP-DELETE'],
									handler: function() {
										var record = Ext.getCmp('sysConfigPropGrid').getSelection()[0];
										deleteSysConfigProp(record);
									}
								}
							]
						}
					],
					columns: [
						{text: 'Key', dataIndex: 'code', flex: 2},
						{text: 'Value', dataIndex: 'description', flex: 5, cellWrap: true}
					],
					listeners: {
						selectionchange: function (grid, record, index, opts) {
							if (Ext.getCmp('sysConfigPropGrid').getSelectionModel().hasSelection()) {
								Ext.getCmp('sysConfigPropGrid-tools-edit').enable();
								Ext.getCmp('sysConfigPropGrid-tools-delete').enable();
							} else {
								Ext.getCmp('sysConfigPropGrid-tools-edit').disable();
								Ext.getCmp('sysConfigPropGrid-tools-delete').disable();
							}
						}
					}
				});

				var editSysConfigPropWin = Ext.create('Ext.window.Window', {
					id: 'editSysConfigPropWin',
					title: 'Add/Edit System Configuration Property',
					modal: true,
					width: '35%',
					minHeight: 250,
					y: '10em',
					iconCls: 'fa fa-lg fa-edit icon-small-vertical-correction',
					layout: 'fit',
					items: [
						{
							xtype: 'form',
							id: 'configPropForm',
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
									id: 'configPropForm-key',
									fieldLabel: 'Key<span class="field-required" />',
									name: 'code',
									allowBlank: false
								},
								{
									xtype: 'textfield',
									id: 'configPropForm-value',
									fieldLabel: 'Value<span class="field-required" />',
									name: 'description'
								},
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
											handler: function() {
												var url = 'api/v1/service/application/configproperties';
												var method = 'POST';
												var form = Ext.getCmp('configPropForm');
												if (form.isValid()) {
													formData = form.getValues();
													CoreUtil.submitForm({
														url: url,
														method: method,
														data: formData,
														// Set false for this one -- it's different
														removeBlankDataItems: false,
														form: Ext.getCmp('configPropForm'),
														success: function (response, opts) {
															Ext.getCmp('sysConfigPropGrid-tools-edit').disable();
															Ext.getCmp('configPropForm').reset();
															Ext.getCmp('editSysConfigPropWin').hide();
															sysConfigPropStore.load();
															Ext.toast('Successfully saved property.', '', 'tr');
														},
														failure: function (response, opts) {
															Ext.toast('Failed to save property.', '', 'tr');
														}
													});

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
												Ext.getCmp('configPropForm').reset();
												Ext.getCmp('editSysConfigPropWin').hide();
											}
										}
									]
								}
							]
						}
					]
				});

				var addSysConfigProp = function addSysConfigProp(record) {
					editSysConfigPropWin.show();
					var form = Ext.getCmp('configPropForm');
					form.reset();
				};

				var editSysConfigProp = function editSysConfigProp(record) {
					editSysConfigPropWin.show();
					var form = Ext.getCmp('configPropForm');
					form.loadRecord(record);
				};


				var deleteSysConfigProp = function deleteSysConfigProp(record) {
					var url = 'api/v1/service/application/configproperties/';
					url += encodeURIComponent(record.data.code);

					var msg = 'Are you sure you want to delete "' + record.getData().code + '"?';
					Ext.MessageBox.confirm('<i class="fa fa-lg fa-warning icon-small-vertical-correction"></i>&nbsp;&nbsp;' + 'Delete Property?', msg, function(btn) {
						if (btn === 'yes') {
							Ext.Ajax.request({
								url: url,
								method: 'DELETE',
								success: function(response, opt){
									Ext.toast('Successfully deleted property', '', 'tr');
									sysConfigPropStore.load();
								},
								failure: function(response, opt){
									Ext.toast('Failed to delete property', '', 'tr');
								}
							});
						}
					});
				};


				var logStore = Ext.create('Ext.data.Store', {
					id: 'logStore',
					autoLoad: true,
					pageSize: 100,
					remoteSort: true,
					sorters: [
						new Ext.util.Sorter({
							property: 'eventDts',
							direction: 'DESC'
						})
					],
					fields: [
						{
							name: 'eventDts',
							type:	'date',
							dateFormat: 'c'
						}
					],
					proxy: CoreUtil.pagingProxy({
							type: 'ajax',
							url: 'api/v1/service/application/logrecords',
							reader: {
								type: 'json',
								rootProperty: 'logRecords',
								totalProperty: 'totalNumber'
							}
					})
				});
			
				var logGrid = Ext.create('Ext.grid.Panel', {
					title: 'Logs',
					id: 'logGrid',
					store: logStore,
					listeners: {
						selectionchange: function (grid, record, index, opts) {
							if (Ext.getCmp('logGrid').getSelectionModel().hasSelection()) {
								Ext.getCmp('logGrid-tools-view').enable();
							} else {
								Ext.getCmp('logGrid-tools-view').disable();
							}
						}
					},
					columns: [
						{ 
							text: 'Event Time',
							dataIndex: 'eventDts',
							xtype: 'datecolumn',
							format: 'm/d/y H:i:s:u',
							flex: 1.5
						},
						{text: 'Level', dataIndex: 'level', flex: 1, cellWrap: true,
							renderer: function(value, metadata, record) {
								if (value === 'FINE') 
									metadata.tdCls = 'alert-success';
								else if (value === 'WARNING')
									metadata.tdCls = 'alert-warning';
								else if (value === 'SEVERE')
									metadata.tdCls = 'alert-danger';
								return value;
							}
						},
						{text: 'Logger Name', dataIndex: 'loggerName', flex: 4, cellWrap: true},
						{text: 'Message', dataIndex: 'message', flex: 9, cellWrap: true}
					],
					bufferedRenderer: false,
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										logStore.load();
										updateDbLoggerStatus();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'View Details',
									id: 'logGrid-tools-view',
									disabled: true,
									scale: 'medium',
									width: '140px',
									iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction-view',
									handler: function () {
										var record = Ext.getCmp('logGrid').getSelection()[0];
										actionViewLogDetail(record);
									}
								},
								{ 
									xtype: 'tbfill'
								},
								{
									xtype: 'label',
								    html: '<strong>Database Logger:</strong>'
								},
								{
									xtype: 'label',
									id: 'dbLogStatusLabel',
									text: "On",
									style: {
										color: 'green',
										fontWeight: 'bold'
									}
								},
								{
									scale: 'medium',
									id: 'dbLoggerButton',
									text: 'Disable',
									handler: function () {
										toggleDbLogger();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									scale: 'medium',
									id: 'clearDbLog',
									text: 'Delete All Records',
									iconCls: 'fa fa-trash fa-2x icon-button-color-warning icon-vertical-correction',
									tooltip: "Doesn't affect server logs. Note: The application will automatically clear old records exceeding max allowed.",
									handler: function () {
										deleteAllRecords();
									}
								}
							]
						},
						{
							xtype: 'pagingtoolbar',
							dock: 'bottom',
							store: 'logStore',
							displayInfo: true
						}
					]
				});
				
				var deleteAllRecords = function() {
					
					Ext.MessageBox.confirm('<i class="fa fa-lg fa-warning icon-small-vertical-correction"></i>&nbsp;&nbsp;' + 'Delete All Records?', 'Are you sure you want to delete all log records?', function(btn) {
						if (btn === 'yes') {
							Ext.Ajax.request({
								url: 'api/v1/service/application/logrecords',
								method: 'DELETE',
								success: function(response, opt){
									Ext.toast('Deleted log records', '', 'tr');
									logStore.load();
								},
								failure: function(response, opt){
									Ext.toast('Failed to delete log records', '', 'tr');
								}
							});
						}
					});
					
				}

				var toggleDbLogger = function toggleDbLogger() {
					if (Ext.getCmp('dbLogStatusLabel').text === 'On'){
						var what = 'off';
						var url = 'api/v1/service/application/dblogger/false';
					}
					else {
						var what = 'on';
						var url = 'api/v1/service/application/dblogger/true';
					}
					
					Ext.Ajax.request({
						url: url,
						method: 'PUT',
						success: function(response, opt){
							Ext.toast('Successfuly turned DB logger ' + what, '', 'tr');
							updateDbLoggerStatus();
						},
						failure: function(response, opt){
							Ext.toast('Failed to turn DB logger ' + what, '', 'tr');
							updateDbLoggerStatus();
						}
					});
				};

				var updateDbLoggerStatus = function updateDbLoggerStatus(reload) {
					var label = Ext.getCmp('dbLogStatusLabel');
					var button = Ext.getCmp('dbLoggerButton');
					reload = typeof reload !== 'undefined' ? reload : true;

					if (reload) {
						sysConfigPropStore.load();	
					}

					var data = sysConfigPropStore.getData();
					var find = data.find('code', 'dblog.on');
					var status = false;
					if (find) { 
						status = find.data.description;
					}

					if (status === 'true' && label !== 'undefined') {
						if(label !== undefined){
						label.setText('On');
						label.setStyle({color: 'green'});
						button.setText('Disable');
						}
					}
					else {
						if(label !== undefined){
						label.setText('Off');
						label.setStyle({color: 'red'});
						button.setText('Enable');
						}
					}

				};



				var loggerStore = Ext.create('Ext.data.Store', {
					id: 'loggerStore',
					autoLoad: true,
					proxy: {
						type: 'ajax',
						url: 'api/v1/service/application/loggers'
					}
				});

				var viewLogRecordWindow = Ext.create('Ext.window.Window', {
					id: 'viewLogRecordWindow',
					title: 'View Log Record',
					iconCls: 'fa fa-lg fa-eye icon-small-vertical-correction',
					width: '50%',
					minHeight: 400,
					autoScroll: true,
					bodyStyle: 'padding: 10px;',
					y: 40,
					modal: true,
					maximizable: false,
					layout: 'vbox',
					listeners: {
						show: function() {        
							this.removeCls('x-unselectable');    
						}
					},					
					items: [
						{
							xtype: 'panel',
							id: 'logRecordViewPanel',
							tpl: new Ext.XTemplate (
								'<p>Level: {level}</p>',
								'<p>Thread ID: {threadId}</p>',
								'<p>Sequence Number: {sequenceNumber}</p>',
								'<p>Source Class: {sourceClass}</p>',
								'<p>Source Method: {sourceMethod}</p>',
								'<p>Message: {message}</p>',
								'<p>Stack Trace: {stackTrace}</p>'
								),
							autoScroll: true,
							width: '100%',
							flex: 1
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
										xtype: 'button',
										text: 'Close',
										iconCls: 'fa fa-lg fa-close icon-button-color-warning',
										handler: function () {
											this.up('window').hide();
										}
									}							
								]
							}
						]
				});

				var actionViewLogDetail = function actionViewLogRecord(record) {
					Ext.getCmp('logRecordViewPanel').update(record);
					viewLogRecordWindow.show();
				};

				var loggerGrid = Ext.create('Ext.grid.Panel', {
					title: 'Logger Levels',
					id: 'loggerGrid',
					store: loggerStore,
					listeners: {
						selectionchange: function (grid, record, index, opts) {
							if (Ext.getCmp('loggerGrid').getSelectionModel().hasSelection()) {
								Ext.getCmp('loggerGrid-tools-edit').enable();
							} else {
								Ext.getCmp('loggerGrid-tools-edit').disable();
							}
						}
					},
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										loggerStore.load();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Edit',
									scale: 'medium',
									id: 'loggerGrid-tools-edit',
									width: '100px',
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
									disabled: true,
									handler: function () {
										var record = Ext.getCmp('loggerGrid').getSelection()[0];
										actionEditLogger(record);
									}
								}
							]
						}
					],

					columnLines: true,
					columns: [
						{text: 'Name', dataIndex: 'name', flex: 6},
						{text: 'Level', dataIndex: 'level', flex: 1},
						{text: 'Handlers', dataIndex: 'handlers', flex: 5}
					]
				});

				var actionEditLogger = function actionEditLogger(record) {
					editLoggerWin.show();
					var form = Ext.getCmp('loggerForm');
					form.loadRecord(record);
				};
				
				var editLoggerWin = Ext.create('Ext.window.Window', {
					id: 'editLoggerWin',
					title: 'Edit Logger Level',
					modal: true,
					width: '35%',
					height: 255,
					y: '20em',
					iconCls: 'fa fa-lg fa-edit icon-small-vertical-correction',
					layout: 'fit',
					items: [
						{
							xtype: 'form',
							id: 'loggerForm',
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
									id: 'loggerForm-key',
									fieldLabel: 'Name',
									name: 'name',
									readOnly: true
								},
								{
									xtype: 'combobox',
									id: 'loggerForm-value',
									fieldLabel: 'Level<span class="field-required" />',
									name: 'level',
									displayField: 'display',
									valueField: 'level',
									store: {
										data: [
											{level: '', display: 'Use Parent Level'},
											{level: 'OFF', display: 'OFF'},
											{level: 'SEVERE', display: 'SEVERE'},
											{level: 'WARNING', display: 'WARNING'},
											{level: 'CONFIG', display: 'CONFIG'},
											{level: 'INFO', display: 'INFO'},
											{level: 'FINE', display: 'FINE'},
											{level: 'FINER', display: 'FINER'},
											{level: 'FINEST', display: 'FINEST'},
											{level: 'ALL', display: 'ALL'}
										]
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
											iconCls: 'fa fa-lg fa-save icon-button-color-save',
											formBind: true,	
											handler: function() {
												var key = Ext.getCmp('loggerForm').getValues().name;
												var url = 'api/v1/service/application/logger/';
												url+= key;
												url+= '/level';
												var method = 'PUT';
												var level = Ext.getCmp('loggerForm').getValues().level;
												CoreUtil.submitForm({
													url: url,
													method: method,
													data: level,
													removeBlankDataItems: true,
													form: Ext.getCmp('loggerForm'),
													success: function (response, opts) {
														Ext.getCmp('loggerGrid-tools-edit').disable();
														Ext.getCmp('loggerForm').reset();
														Ext.getCmp('editLoggerWin').hide();
														loggerStore.load();
														Ext.toast('Successfully saved level.', '', 'tr');
													},
													failure: function (response, opts) {
														Ext.toast('Failed to save level.', '', 'tr');
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
												Ext.getCmp('loggerForm').reset();
												Ext.getCmp('editLoggerWin').hide();
											}
										}
									]
								}
							]
						}
					]
				});

				var logPanel = Ext.create('Ext.tab.Panel', {
					title: 'Logs and Logging',
					id: 'logPanel',
					tabPosition: 'left',
					requiredPermissions: ['ADMIN-SYSTEM-MANAGEMENT-LOGGING'],
					actionOnInvalidPermission: 'destroy',
					tabRotation: 0,
					tabBar: {
						border: false
					},
					items: [loggerGrid, logGrid]

				});

				var pluginStore = Ext.create('Ext.data.Store', {
					id: 'pluginStore',
					autoLoad: true,
					proxy: {
						type: 'ajax',
						url: 'api/v1/resource/plugins'
					}
				});

				var pluginGrid = Ext.create('Ext.grid.Panel', {
					id: 'pluginGrid',
					title: 'Plugins',
					store: pluginStore,
					requiredPermissions: ['ADMIN-SYSTEM-MANAGEMENT-PLUGIN'],
					actionOnInvalidPermission: 'destroy',
					columnLines: true,
					columns: [
						{text: 'ID', dataIndex: 'pluginId', flex: 1, cellWrap: true, hidden: true},
						{text: 'Runtime ID', dataIndex: 'runtimeId', flex: 1},
						{text: 'Name', dataIndex: 'name', flex: 2, cellWrap: true},
						{text: 'Description', dataIndex: 'description', flex: 4, cellWrap: true},
						{text: 'Version', dataIndex: 'version', flex: 1},
						{text: 'Update Date', dataIndex: 'lastModifed', flex: 1.5,
							xtype: 'datecolumn',
							format: 'm/d/y H:i:s:u'
						},
						{text: 'State', dataIndex: 'pluginRuntimeState', flex: 1,
							renderer: function(value, metadata, record) {
								if (value === 'Active') 
									metadata.tdCls = 'alert-success';
								else if (value === 'Resolved')
									metadata.tdCls = 'alert-warning';
								return value;
							}
						},
						{text: 'Core Module', dataIndex: 'coreModule', flex: 1, align: 'center',
							renderer: CoreUtil.renderer.booleanRenderer
						},
						{text: 'Actual Filename', dataIndex: 'actualFilename', flex: 2, hidden: true},
						{text: 'System Location', dataIndex: 'location', flex: 2, hidden: true},
						{text: 'Type', dataIndex: 'pluginType', flex: 1}
					],
					listeners: {
						selectionchange: function (grid, record, index, opts) {
							if (Ext.getCmp('pluginGrid').getSelectionModel().hasSelection()
							   	&& record[0].data.pluginId !== undefined)
							   {
								console.log(record);
								if (record[0].data.pluginRuntimeState === 'Active') {
									Ext.getCmp('pluginGrid-tools-start').disable();
								}
								else  {
									Ext.getCmp('pluginGrid-tools-start').enable();
								}
								if (record[0].data.pluginRuntimeState === 'Resolved') {
									Ext.getCmp('pluginGrid-tools-stop').disable();
								}
								else {
									Ext.getCmp('pluginGrid-tools-stop').enable();
								}
								Ext.getCmp('pluginGrid-tools-download').enable();
								Ext.getCmp('pluginGrid-tools-uninstall').enable();
							} else {
								Ext.getCmp('pluginGrid-tools-start').disable();
								Ext.getCmp('pluginGrid-tools-stop').disable();
								Ext.getCmp('pluginGrid-tools-download').disable();
								Ext.getCmp('pluginGrid-tools-uninstall').disable();
							}
						}
					},
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										pluginStore.load();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Add Plugin',
									id: 'pluginGrid-tools-addPlugin',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction',
									handler: function () {
										actionAddPlugin();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Start',
									id: 'pluginGrid-tools-start',
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-play-circle icon-button-color-run icon-vertical-correction',
									disabled: true,
									handler: function () {
										var record = Ext.getCmp('pluginGrid').getSelection()[0];
										actionStartPlugin(record);
									}
								},
								{
									text: 'Stop',
									id: 'pluginGrid-tools-stop',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-stop-circle icon-button-color-default icon-vertical-correction',
									disabled: true,
									handler: function () {
										var record = Ext.getCmp('pluginGrid').getSelection()[0];
										actionStopPlugin(record);
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Download',
									id: 'pluginGrid-tools-download',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-download icon-button-color-default icon-vertical-correction',
									disabled: true,
									handler: function () {
										var record = Ext.getCmp('pluginGrid').getSelection()[0];
										actionDownloadPlugin(record);
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Uninstall',
									id: 'pluginGrid-tools-uninstall',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									disabled: true,
									handler: function () {
										var record = Ext.getCmp('pluginGrid').getSelection()[0];
										actionUninstallPlugin(record);
									}
								}
							]
						}
					]
				});

				var actionAddPlugin = function actionAddPlugin(record) {
					addPluginWindow.show();
				};

				var actionStartPlugin = function actionStartPlugin(record) {
					var url = 'api/v1/resource/plugins/';
					url += record.data.pluginId + '/start';
					Ext.Ajax.request({
						url: url,
						method: 'POST',
						success: function(response, opt){
							Ext.toast('Successfully started plugin', '', 'tr');
							pluginStore.load();
						},
						failure: function(response, opt){
							Ext.toast('Failed to start plugin', '', 'tr');
						}
					});
				};

				var actionStopPlugin = function actionStopPlugin(record) {
					var url = 'api/v1/resource/plugins/';
					url += record.data.pluginId + '/stop';
					Ext.Ajax.request({
						url: url,
						method: 'POST',
						success: function(response, opt){
							Ext.toast('Successfully stopped plugin', '', 'tr');
							pluginStore.load();
						},
						failure: function(response, opt){
							Ext.toast('Failed to stop plugin', '', 'tr');
						}
					});
				};

				var actionDownloadPlugin = function actionDownloadPlugin(record) {
					var url = 'api/v1/resource/plugins/';
					url += record.data.pluginId + '/download';
					window.location.href = url;					
				};

				var actionUninstallPlugin = function actionUninstallPlugin(record) {
					
					Ext.Msg.show({
						title:'Uninstall Plugin?',
						iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
						message: 'Are you sure you want to uninstall <b>' + record.get('name') + '</b>?',
						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						fn: function(btn) {
							if (btn === 'yes') {
								var url = 'api/v1/resource/plugins/';
								url += record.data.pluginId;
								Ext.Ajax.request({
									url: url,
									method: 'DELETE',
									success: function(response, opt){
										Ext.toast('Successfully uninstalled plugin', '', 'tr');
										pluginStore.load();
									},
									failure: function(response, opt){
										Ext.toast('Failed to uninstall plugin', '', 'tr');
									}
								});
							} 
						}
					});					
					
				};

				var addPluginWindow = Ext.create('Ext.window.Window', {
					id: 'addPluginWindow',
					title: 'Add Plugin',
					iconCls: 'fa fa-lg fa-info-circle',
					width: '40%',
					height: 175,
					y: 60,
					modal: true,
					maximizable: false,
					bodyStyle : 'padding: 10px;',
					layout: 'fit',
					items: [
						{
							xtype: 'form',
							id: 'pluginUploadForm',
							layout: 'vbox',
							defaults: {
								labelAlign: 'top',
								width: '100%'
							},
							items: [
								{
									xtype: 'filefield',
									name: 'uploadFile',
									width: '100%',
									allowBlank: false,
									fieldLabel: 'Choose a OSGi jar/war to upload <span class="field-required" />',
									buttonText: 'Select File...'
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
									text: 'Upload Plugin',
									iconCls: 'fa fa-lg fa-upload icon-button-color-default',
									formBind: true,	
									requiredPermissions: ['ADMIN-SYSTEM-MANAGEMENT-PLUGIN'],
									handler: function() {
										var form = Ext.getCmp('pluginUploadForm');
										if (form.isValid()) {
											form.submit({
												url: '/openstorefront/Upload.action?UploadPlugin',
												waitMsg: 'Uploading plugin...',
												success: function () {
													Ext.toast('Successfully uploaded plugin.', '', 'tr');
													Ext.Msg.alert('Success', 'The plugin will install at the run of the deployment job. Click the refresh button to confirm deployment.');
													addPluginWindow.hide();
												},
												failure: function () {
													Ext.toast('Error uploading plugin.', '', 'tr');
													Ext.Msg.alert('Failure', 'The plugin failed to install. Please check the file and ensure it is a valid OSGi JAR/WAR file.');
												}
											});
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
										Ext.getCmp('addPluginWindow').hide();
									}
								}
							]
						}
					]
				});
				
				var managersGrid = Ext.create('Ext.grid.Panel', {
					title: 'Managers',
					columnLines: true,
					requiredPermissions: ['ADMIN-SYSTEM-MANAGEMENT-MANAGERS'],
					actionOnInvalidPermission: 'destroy',
					store: {
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/service/application/managers'
						}						
					},
					listeners: {
						selectionchange: function (selectionModel, records, index, opts) {
							var tools  = managersGrid.getComponent('tools');
							if (selectionModel.getCount() > 0 ) {								
								if (records[0].get('started')) {
									tools.getComponent('start').setDisabled(true);
								} else {
									tools.getComponent('start').setDisabled(false);
								}
								if (records[0].get('started')) {
									tools.getComponent('stop').setDisabled(false);
									tools.getComponent('restart').setDisabled(false);
								} else {
									tools.getComponent('stop').setDisabled(true);
									tools.getComponent('restart').setDisabled(true);
								}								
							} else {
								tools.getComponent('start').setDisabled(true);
								tools.getComponent('stop').setDisabled(true);
								tools.getComponent('restart').setDisabled(true);
							}							
						}							
					},
					columns: [
						{ text: 'Name', dataIndex: 'name', width: 250 },
						{ text: 'Class', dataIndex: 'managerClass', minWidth: 250, flex: 1 },
						{ text: 'Started', dataIndex: 'started', align: 'center', width: 150, 
							renderer: CoreUtil.renderer.booleanRenderer
						},
						{ text: 'Order', dataIndex: 'order', align: 'center', width: 150 }
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							itemId: 'tools',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',									handler: function(){
										this.up('grid').getStore().reload();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Start',
									itemId: 'start',
									disabled: true,
									scale: 'medium',
									iconCls: 'fa fa-2x fa-play-circle icon-button-color-run icon-vertical-correction',
									handler: function(){
										var grid = this.up('grid');
										var record = this.up('grid').getSelectionModel().getSelection()[0];										
										
										grid.setLoading('Starting Manager...');
										Ext.Ajax.request({
											url: 'api/v1/service/application/managers/' + record.get('managerClass') + '/start',
											method: 'PUT',
											callback: function(){
												grid.setLoading(false);
											},
											success: function(){
												
												grid.getStore().reload();
											}
										});
										
									}									
								},
								{
									text: 'Stop',
									itemId: 'stop',
									disabled: true,
									scale: 'medium',
									iconCls: 'fa fa-2x fa-stop-circle icon-button-color-default icon-vertical-correction',
									handler: function(){
										var grid = this.up('grid');
										var record = this.up('grid').getSelectionModel().getSelection()[0];
										
										Ext.Msg.show({
											title:'Stop Manager?',
											message: 'Are you sure you want to stop this manager? <br> The application may be unstable or some feature will not available.',
											buttons: Ext.Msg.YESNO,
											icon: Ext.Msg.QUESTION,
											fn: function(btn) {
												if (btn === 'yes') {
													grid.setLoading('Stopping Manager...');
													Ext.Ajax.request({
														url: 'api/v1/service/application/managers/' + record.get('managerClass') + '/stop',
														method: 'PUT',
														callback: function(){
															grid.setLoading(false);
														},
														success: function(){
															grid.getStore().reload();
														}											
													});													
												} 
											}
										});											
										

									}									
								},
								{
									text: 'Restart Manager',
									itemId: 'restart',
									disabled: true,
									scale: 'medium',
									minWidth: '140px',
									iconCls: 'fa fa-2x fa-repeat icon-button-color-refresh icon-vertical-correction',
									handler: function(){
										var grid = this.up('grid');
										var record = this.up('grid').getSelectionModel().getSelection()[0];										
										
										grid.setLoading('Restarting Manager...');
										Ext.Ajax.request({
											url: 'api/v1/service/application/managers/' + record.get('managerClass') + '/restart',
											method: 'PUT',
											callback: function(){
												grid.setLoading(false);
											},
											success: function(){												
												grid.getStore().reload();
											}											
										});										
									}									
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Restart Application',
									scale: 'medium',
									minWidth: '140px',
									iconCls: 'fa fa-2x fa-repeat icon-button-color-refresh icon-vertical-correction',
									handler: function(){
										var grid = this.up('grid');																		
										
										Ext.Msg.show({
											title:'Restart Application?',
											message: 'Are you sure you want to restart? <br> The application will be unavailable while restarting.',
											buttons: Ext.Msg.YESNO,
											icon: Ext.Msg.QUESTION,
											fn: function(btn) {
												if (btn === 'yes') {
													grid.setLoading('Restarting Application...');
													Ext.Ajax.request({
														url: 'api/v1/service/application/restart',
														method: 'POST',
														callback: function(){
															grid.setLoading(false);
														},
														success: function(){
															grid.getStore().reload();
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
				});
				
				var cacheGrid = Ext.create('Ext.grid.Panel', {
					title: 'Cache',
					requiredPermissions: ['ADMIN-SYSTEM-MANAGEMENT-CACHE'],
					actionOnInvalidPermission: 'destroy',
					columnLines: true,
					store: {	
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/service/application/caches'
						}
					},
					columns: [
						{ text: 'Name', dataIndex: 'name', minWidth: 200, flex: 1 },
						{ text: 'Hits', dataIndex: 'hitCount', width: 200 },
						{ text: 'Misses', dataIndex: 'missCount', width: 200 },
						{ text: 'Hit Ratio', dataIndex: 'hitRatio', width: 200,
						   xtype: 'widgetcolumn',
						   widget: {
							xtype: 'progressbarwidget',
							textTpl: '{value:percent}'
						   }							
						},						
						{ text: 'Rough Count', dataIndex: 'roughCount', minWidth: 200 }
					],
					listeners: {
						selectionchange: function (selectionModel, records, index, opts) {
							var tools  = cacheGrid.getComponent('tools');
							if (selectionModel.getCount() > 0 ) {
								tools.getComponent('flushBtn').setDisabled(false);
							} else {
								tools.getComponent('flushBtn').setDisabled(true);
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
									text: 'Refresh',
									scale: 'medium',
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',									handler: function(){
										this.up('grid').getStore().reload();
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Flush Cache',
									itemId: 'flushBtn',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									disabled: true,
									handler: function(){
										var grid = this.up('grid');
										var record = this.up('grid').getSelectionModel().getSelection()[0];
										
										grid.setLoading('Flushing Cache...');
										Ext.Ajax.request({
											url: 'api/v1/service/application/caches/' + record.get('name') + '/flush',
											method: 'PUT',
											callback: function(){
												grid.setLoading(false);
											},
											success: function(response){
												Ext.toast("Cleared " + record.get('name') + " cache.");
												grid.getStore().reload();												
											}
										});										
									}									
								}
							]
						}
					]
				});	
				

				var searchControlPanel = Ext.create('Ext.panel.Panel', {
					title: 'Search Control',
					id: 'searchControlPanel',
					style: 'padding: 10px',
					requiredPermissions: ['ADMIN-SYSTEM-MANAGEMENT-SEARCH-CONTROL'],
					actionOnInvalidPermission: 'destroy',
					items: [
						{
							text: 'Re-Index Listings',
							xtype: 'button',
							scale: 'medium',
							iconCls: 'fa fa-2x fa-undo icon-vertical-correction',
							tooltip: 'This will re-index the listings. The search results will be affected while running.',
							handler: function () {
								Ext.Ajax.request({
									url: 'api/v1/service/search/resetSolr',
									method: 'POST',
									success: function(response, opt) {
										Ext.toast('Successfully sent re-index request', '', 'tr');
									},
									failure: function(response, opt) {
										Ext.toast('Failed to send re-index request', '', 'tr');
									}
								});
							}
						},
						{
							xtype: 'fieldset',
							title: 'Index Fields To Search',						
							width: '100%',
							layout: 'anchor',							
							items: [
								{
									xtype: 'form',
									itemId: 'searchOptionForm',									
									items: [
										{
											xtype: 'panel',
											html: '<i class="fa fa-warning"></i><span style="color: red; font-weight: bold"> If no search fields are selected, the index search will always return all entries.</span>'
										},
										{
											xtype: 'checkbox',
											name: 'searchName',
											boxLabel: 'Entry Names'
										},
										{
											xtype: 'checkbox',
											name: 'searchDescription',
											boxLabel: 'Entry Descriptions'
										},
										{
											xtype: 'checkbox',
											name: 'searchOrganization',
											boxLabel: 'Entry Organizations'								
										}, 										
										{
											xtype: 'checkbox',
											name: 'searchAttributes',
											boxLabel: 'Attributes'
										},
										{
											xtype: 'checkbox',
											name: 'searchTags',
											boxLabel: 'Tags'
										},
										{
											text: 'Save Search Options',
											xtype: 'button',
											scale: 'medium',
											iconCls: 'fa fa-2x fa-save icon-vertical-correction',
											tooltip: 'This will apply the above settings to searches.',
											handler: function () {
												var form = searchControlPanel.queryById('searchOptionForm');
												var data = searchControlPanel.queryById('searchOptionForm').getValues();
												CoreUtil.submitForm({
													url: 'api/v1/resource/searchoptions/global',	
													method: 'PUT',
													removeBlankDataItems: true,													
													form: form,
													data: data,
													success: function (response, opts) {
														Ext.toast('Successfully applied the search options.', '', 'tr');
													},
													failure: function (response, opts) {
														Ext.toast('Failed to apply the search options.', '', 'tr');
													}
												});												
												
											}
										}
									]
								}
							]
						}						
					]
				});

				searchControlPanel.setLoading(true);
				Ext.Ajax.request({
					url: 'api/v1/resource/searchoptions/global',
					method: 'GET',
					callback: function() {
						searchControlPanel.setLoading(false);
					},
					success: function(response, opts){
						var data = Ext.decode(response.responseText);
						
						var record = Ext.create('Ext.data.Model', {});
						record.set(data);						
						searchControlPanel.queryById('searchOptionForm').loadRecord(record);
					}
				});

				var recentChangesPanel = Ext.create('Ext.panel.Panel', {
					title: 'Recent Changes E-mail',
					id: 'recentChangesPanel',
					requiredPermissions: ['ADMIN-SYSTEM-MANAGEMENT-RECENT-CHANGES'],
					actionOnInvalidPermission: 'destroy',
					items: [
						{
							xtype: 'form',
							id: 'recentChangeForm',
							padding: 20,
							layout: 'vbox',
							width: '40%',
							defaults: {
								labelAlign: 'top',
								width: '100%',								
								style: 'font-weight: bold'
							},
							items: [
								{ 
									xtype: 'panel',
									id: 'emailSendDates',	
									margin: '0 0 0 -39',
									tpl: [
										'<ul class="list-group">',
										'	<li class="stat-list-group-item">Last Automated Email Sent: <span class="stat-badge">',
										'	{[Ext.Date.format(Ext.Date.parse(values.lastSentDts, "c"), "m/d/y H:i:s A ")]}',
										'	<li class="stat-list-group-item">Next Automated Email Sent: <span class="stat-badge">',
										'	{[Ext.Date.format(Ext.Date.parse(values.nextSendDts, "c"), "m/d/y H:i:s A ")]}',
										'</span></li></ul'
									]
								},
								{
									xtype: 'datefield',
									id: 'sinceDate',
									fieldLabel: 'Show Changes Since Date',
									allowBlank: false,
									maxValue: new Date()
								},
								{
									xtype: 'textfield',
									id: 'toEmail',
									fieldLabel: 'Send to this Email: (leave blank to send to all users who have requested to be notified)',
									allowBlank: true,
									maxLength: 100,
									vtype: 'email',
								},
								{
									xtype: 'button',
									maxWidth: 300,
									maxHeight: 75,
									scale: 'medium',
									text: 'Send Recent Changes Email',
									iconCls: 'fa fa-2x fa-envelope-o icon-vertical-correction-send',
									handler: function() {
										var form = Ext.getCmp('recentChangeForm');
										if (form.isValid()) {
											data = {};
											data.emailAddress = Ext.getCmp('toEmail').value;
											data.lastRunDts = Ext.Date.format(Ext.getCmp('sinceDate').value,'m/d/Y');
											// For some reason this uses URL parameters
											var url = 'api/v1/service/notification/recent-changes';
											url += '?emailAddress=' + data.emailAddress;
											url += '&lastRunDts=' + data.lastRunDts;
											Ext.Ajax.request({
												url: url,
												method: 'POST',
												success: function(response, opt){
													Ext.toast('Successfully sent request', '', 'tr');
												},
												failure: function(response, opt) {
													Ext.toast('Email request failed', '', 'tr');
												}


											});
										}
										else {
											Ext.Msg.alert('Errors/Missing information', 'There are errors in the form. Please complete the form before sending.');
										}
									}
								}								
							]
						}
					]
				});

				var actionLoadRecentChangesInfo = function actionLoadRecentChangesInfo() {
					Ext.Ajax.request({
						url: 'api/v1/service/notification/recent-changes/status',
						success: function(response, opt){
							var data = Ext.decode(response.responseText);							
							Ext.getCmp('emailSendDates').update(data);
						}
					});
				};

				actionLoadRecentChangesInfo();

				var systemMainPanel = Ext.create('Ext.tab.Panel', {
					title: 'System Management &nbsp; <i class="fa fa-lg fa-question-circle"  data-qtip="View the system status and manage system properties"></i>',					
					items: [
						statusPanel,
						errorTicketsGrid,
						appStatePropGrid,
						sysConfigPropGrid,
						logPanel,
						pluginGrid,
						managersGrid,
						cacheGrid,
						searchControlPanel,
						recentChangesPanel
					],
					listeners:{
						remove: function(){
							if(this.items.length===0){
								Ext.toast({html: 'You do not have access permissions to see any data on this view.', title:'No Access', align:'b'}); 
								
							}
						}	
					}
				});

				addComponentToMainViewPort(systemMainPanel);



				var actionLoadSystemData = function (update) {

					if (update) {
					} else {
						statusStats.setLoading(true);
					}

					Ext.Ajax.request({
						url: 'api/v1/service/application/status',
						success: function(response, opt){
							var data = Ext.decode(response.responseText);
							Ext.getCmp('systemDetailStats').update(data);
							Ext.getCmp('systemDetailStats2').update(data);
							Ext.getCmp('garbageCollectionPanel').update(data);

							Ext.getCmp('heapMemoryBar').setValue(data.heapMemoryStatus.usedKb / data.heapMemoryStatus.maxKb);
							Ext.getCmp('nonHeapMemoryBar').setValue(data.nonHeapMemoryStatus.usedKb / data.nonHeapMemoryStatus.commitedKb);
							memoryPoolStore.load();
							
							statusStats.setLoading(false);
						},
						failure: function(response, opt){
							statusStats.setLoading(false);
						}
					});
				};
				actionLoadSystemData();

				var systemDataUpdater = {
					id: 'systemDataUpdater',
					run: function () {
						actionLoadSystemData(update=true);
					},
					interval: 2000
				};


			});

			</script>
			</stripes:layout-component>
</stripes:layout-render>
