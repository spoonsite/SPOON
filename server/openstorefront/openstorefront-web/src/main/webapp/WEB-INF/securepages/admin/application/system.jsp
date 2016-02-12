<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
    <stripes:layout-component name="contents">
<style>
			.list-group-item:first-child {
				border-top-right-radius: 4px;
				border-top-left-radius: 4px;
			}
			.list-group-item {
				position: relative;
				display: block;
				padding: 10px 15px;
				margin-bottom: -1px;
				background-color: #8A8A8A;
				border: 1px solid #464545;
				font-size: 14px;
			}
			.list-group {
				/* margin-bottom: 20px; */
				padding-left: 0;
				color: white;
			}
			ul, ol {
				margin-top: 0;
				margin-bottom: 10.5px;
			}
			.badge {
				display: inline-block;
				min-width: 10px;
				padding: 3px 7px;
				font-size: 13px;
				font-weight: bold;
				color: #ffffff;
				line-height: 1;
				vertical-align: middle;
				white-space: nowrap;
				text-align: center;
				float: right;
				background-color: rgba(68,30,90,1);
				border-radius: 10px;
			}
			.green-highlight{
				color: lightgreen
			}
		</style>

		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function () {

				var tplSystemDetailStats = new Ext.XTemplate(
					'<div style="padding:10px;"><ul class="list-group">',
					'<li class="list-group-item">Application Version: <span class="badge">{applicationVersion}</span></li>',
					'<li class="list-group-item">Uptime: <span class="badge">{upTime}</span></li>',
					'<li class="list-group-item">Start Time: <span class="badge">{startTime}</span></li>',
					'</ul><div>'
				);

				var tplSystemDetailStats2 = new Ext.XTemplate(
					'<div style="padding:10px;"><ul class="list-group">',
					'<li class="list-group-item">Processor Count: <span class="badge">{processorCount}</span></li>',
					'<li class="list-group-item">Live Threads / Total Threads: <span class="badge">{liveThreadCount}/{totalThreadCount}</span></li>',
					'<li class="list-group-item">System Load: <span class="badge">{systemLoad}</span></li>',
					'</ul><div>'

				);

				var tplGarbageCollection = new Ext.XTemplate(
					'<ul style="padding: 10px;"><tpl for="garbageCollectionInfos">',
					'<li><strong>{.}</strong></li>',
					'</tpl></ul>'
				);

				var memoryPanel = Ext.create('Ext.panel.Panel', {
					defaults: {
						padding: '10px',
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
						url: '/openstorefront/api/v1/service/application/status',
						reader: {
							type: 'json',
							rootProperty: 'memoryPools'
						}
					}
				});


				var memoryPoolsGrid = Ext.create('Ext.grid.Panel', {
					id: 'memoryPoolsGrid',
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
					layout: {
						type: 'vbox',
						align: 'stretch'
					},
					items: [
						{
							xtype: 'panel',
							layout: {
								type: 'hbox',
								align: 'stretch'
							},
							items: [
								{
									xtype: 'panel',
									id: 'systemDetailStats',
									tpl: tplSystemDetailStats,
									flex: 1,
								},
								{
									xtype: 'panel',
									id: 'systemDetailStats2',
									tpl: tplSystemDetailStats2,
									flex: 1,
								},
							]
						},
						memoryPanel,
						memoryPoolsGrid,
						{
							xtype: 'panel',
							title: 'Garbage Collection',
							style: {
								padding: '10px'
							},
							id: 'garbageCollectionPanel',
							tpl: tplGarbageCollection,
							flex: 1,
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
									iconCls: 'fa fa-2x fa-refresh',
									handler: function () {
										actionLoadSystemData();
									}
								},
								{
									xtype: 'label',
									text: 'Automatically Update:'
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
											pressed: true,
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
											name: 'autoUpdate',
											handler: function () {
												Ext.TaskManager.stop(systemDataUpdater);
											}
										}
									]
								}
							]
						},
					]
				});

				var threadStatusStore = Ext.create('Ext.data.Store', {
					autoLoad: true,
					storeId: 'threadStatusStore',
					proxy: {
						id: 'threadStatusStoreProxy',
						type: 'ajax',
						url: '/openstorefront/api/v1/service/application/threads'
					}
				});

				var threadStatus = Ext.create('Ext.grid.Panel', {
					title: 'Threads Status',
					id: 'threadStatus',
					store: threadStatusStore,
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-refresh',
									handler: function () {
										threadStatusStore.load();
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
					]
				});

				var systemPropertiesStore = Ext.create('Ext.data.Store', {
					autoLoad: true,
					storeId: 'systemPropertiesStore',
					proxy: {
						id: 'systemPropertiesStoreProxy',
						type: 'ajax',
						url: '/openstorefront/api/v1/service/application/status',
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
									iconCls: 'fa fa-2x fa-refresh',
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
							url: '/openstorefront/api/v1/resource/errortickets',
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
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-refresh',
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
									iconCls: 'fa fa-2x fa-eye icon-vertical-correction',
									handler: function () {
										var record = Ext.getCmp('errorTicketsGrid').getSelection()[0];
										actionViewErrorTicket(record);
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
					columns: [
						{text: 'Ticket ID', dataIndex: 'errorTicketId', flex: 1.5, cellWrap: true},
						{
							text: 'Update Date',
							dataIndex: 'updateDts',
							xtype: 'datecolumn',
							format: 'm/d/y H:i:s',
							flex: 1.5
						},
						{text: 'Client IP', dataIndex: 'clientIp', flex: 1},
						{text: 'Called Action', dataIndex: 'calledAction', flex: 4.5, cellWrap: true},
						{text: 'Message', dataIndex: 'message', flex: 9, cellWrap: true},
						{text: 'Type', dataIndex: 'errorTypeCode', flex: 0.5}
					],
					listeners: {
						selectionchange: function (grid, record, index, opts) {
							if (Ext.getCmp('errorTicketsGrid').getSelectionModel().hasSelection()) {
								Ext.getCmp('errorTicketsGrid-tools-view').enable();
							} else {
								Ext.getCmp('errorTicketsGrid-tools-view').disable();
							}
						}
					}

				});

				var viewErrorTicketWindow = Ext.create('Ext.window.Window', {
					id: 'viewErrorTicketWindow',
					title: 'View Error Ticket Information',
					iconCls: 'fa fa-info-circle',
					width: '80%',
					height: 600,
					autoScroll: true,
					bodyStyle: 'padding: 10px;',
					y: 40,
					modal: true,
					maximizable: false,
					layout: 'vbox',
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
										iconCls: 'fa fa-close',
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
						url: '/openstorefront/api/v1/resource/errortickets/' + record.data.errorTicketId + '/ticket',
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
						url: '/openstorefront/api/v1/resource/applicationproperties'
						}
				});
				
				var appStatePropGrid = Ext.create('Ext.grid.Panel', {
					title: 'Application State Properties',
					id: 'appStatePropGrid',
					store: appStatePropStore,
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
						},
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
									iconCls: 'fa fa-2x fa-refresh',
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
									iconCls: 'fa fa-2x fa-edit',
									handler: function () {
										var record = Ext.getCmp('appStatePropGrid').getSelection()[0];
										actionEditStateProp(record);
									}
								},

							]
						}
				]
				});
				
				var editAppStatePropWin = Ext.create('Ext.window.Window', {
					id: 'editAppStatePropWin',
					title: 'Edit Application State Property',
					modal: true,
					width: '35%',
					height: 275,
					y: '10em',
					iconCls: 'fa fa-lg fa-edit',
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
									fieldLabel: 'Key',
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
								},

							],
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										{
											text: 'Save',
											iconCls: 'fa fa-save',
											formBind: true,	
											handler: function() {
												var key = Ext.getCmp('appStatePropForm').key;
												var url = '/openstorefront/api/v1/resource/applicationproperties/';
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
											iconCls: 'fa fa-close',
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
						url: '/openstorefront/api/v1/service/application/configproperties',
					}
				});

				var sysConfigPropGrid = Ext.create('Ext.grid.Panel', {
					title: 'System Configuration Properties',
					id: 'sysConfigPropGrid',
					store: sysConfigPropStore,
					columnLines: true,
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-refresh',
									handler: function () {
										sysConfigPropStore.load();
									}
								},
							]
						}
					],
					columns: [
						{text: 'Key', dataIndex: 'code', flex: 2},
						{text: 'Value', dataIndex: 'description', flex: 5, cellWrap: true}
					]
				});

				var logStore = Ext.create('Ext.data.Store', {
					id: 'logStore',
					autoLoad: true,
					pageSize: 100,
					remoteSort: true,
					proxy: CoreUtil.pagingProxy({
							type: 'ajax',
							url: '/openstorefront/api/v1/service/application/logrecords',
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
						{text: 'Level', dataIndex: 'level', flex: 1, cellWrap: true},
						{text: 'Logger Name', dataIndex: 'loggerName', flex: 4, cellWrap: true},
						{text: 'Message', dataIndex: 'message', flex: 9, cellWrap: true},
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-refresh',
									handler: function () {
										logStore.load();
									}
								},
								{
									text: 'View Details',
									id: 'logGrid-tools-view',
									disabled: true,
									scale: 'medium',
									iconCls: 'fa fa-2x fa-eye icon-vertical-correction',
									handler: function () {
										var record = Ext.getCmp('logGrid').getSelection()[0];
										actionViewLogDetail(record);
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

				var loggerStore = Ext.create('Ext.data.Store', {
					id: 'loggerStore',
					autoLoad: true,
					proxy: {
						type: 'ajax',
						url: '/openstorefront/api/v1/service/application/loggers'
					},
				});

				var viewLogRecordWindow = Ext.create('Ext.window.Window', {
					id: 'viewLogRecordWindow',
					title: 'View Log Record',
					iconCls: 'fa fa-info-circle',
					width: '80%',
					height: 600,
					autoScroll: true,
					bodyStyle: 'padding: 10px;',
					y: 40,
					modal: true,
					maximizable: false,
					layout: 'vbox',
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
										iconCls: 'fa fa-close',
										handler: function () {
											this.up('window').hide();
										}
									}							
								]
							}
						]
				});

				var actionViewLogDetail = function actionViewLogRecord(record) {
					viewLogRecordWindow.show();
				}

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
									iconCls: 'fa fa-2x fa-refresh',
									handler: function () {
										loggerStore.load();
									}
								},
								{
									text: 'Edit',
									scale: 'medium',
									id: 'loggerGrid-tools-edit',
									iconCls: 'fa fa-2x fa-edit',
									disabled: true,
									handler: function () {
										var record = Ext.getCmp('loggerGrid').getSelection()[0];
										actionEditLogger(record);
									}
								},
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
					iconCls: 'fa fa-lg fa-edit',
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
											{level: 'ALL', display: 'ALL'},
										]
									}
								},
							],
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										{
											text: 'Save',
											iconCls: 'fa fa-save',
											formBind: true,	
											handler: function() {
												var key = Ext.getCmp('loggerForm').getValues().name;
												var url = '/openstorefront/api/v1/service/application/logger/';
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
											iconCls: 'fa fa-close',
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
					tabRotation: 0,
					tabBar: {
						border: false
					},
					items: [loggerGrid, logGrid]
				});


				var pluginGrid = Ext.create('Ext.grid.Panel', {
					title: 'Plugins',
					id: 'pluginGrid'
				});

				var searchControlPanel = Ext.create('Ext.grid.Panel', {
					title: 'Search Control',
					id: 'searchControlPanel'
				});

				var recentChangesPanel = Ext.create('Ext.grid.Panel', {
					title: 'Recent Changes E-mail',
					id: 'recentChangesPanel'
				});



				var systemMainPanel = Ext.create('Ext.tab.Panel', {
					title: 'System Management <i class="fa fa-question-circle"  data-qtip="View the system status and manage system properties"></i>',
					width: 400,
					height: 400,
					items: [
						statusPanel,
						errorTicketsGrid,
						appStatePropGrid,
						sysConfigPropGrid,
						logPanel,
						pluginGrid,
						searchControlPanel,
						recentChangesPanel
					]
				});

				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [systemMainPanel]
				});

			var actionLoadSystemData = function (update) {

				if (update) {
				} else {
					statusStats.setLoading(true);
				}

				Ext.Ajax.request({
					url: '/openstorefront/api/v1/service/application/status',
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
						actionLoadSystemData(update = true)
					},
					interval: 1000
				}


				Ext.TaskManager.start(systemDataUpdater);


			});

		</script>
    </stripes:layout-component>
</stripes:layout-render>
