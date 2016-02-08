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
						{text: 'Ticket ID', dataIndex: 'errorTicketId', flex: 1},
						{text: 'Update Date', dataIndex: 'updateDts', flex: 2},
						{text: 'Client IP', dataIndex: 'clientIp', flex: 1},
						{text: 'Called Action', dataIndex: 'calledAction', flex: 4.5},
						{text: 'Message', dataIndex: 'message', flex: 9},
						{text: 'Type', dataIndex: 'errorTypeCode', flex: 0.5}
					]
				});

				var appStatePropGrid = Ext.create('Ext.grid.Panel', {
					title: 'Application State Properties',
					id: 'appStatePropGrid'
				});

				var appInitPropGrid = Ext.create('Ext.grid.Panel', {
					title: 'Application Initilization Properties',
					id: 'appInitPropGrid'
				});

				var logGrid = Ext.create('Ext.grid.Panel', {
					title: 'Logs and Logging',
					id: 'logGrid'
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
						appInitPropGrid,
						logGrid,
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
