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
/* global Ext, CoreService */

Ext.define('OSF.component.NotificationPanel', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.NotificationPanel',
	layout: 'fit',
	initComponent: function () {
		this.callParent();


		var notPanel = this;
		notPanel.loadAll = false;

		var dataStore = Ext.create('Ext.data.Store', {
			autoLoad: true,
			sorters: [
				new Ext.util.Sorter({
					property: 'createDts',
					direction: 'DESC'
				})
			],			
			fields: [
				'eventId',
				'eventType',
				'eventTypeDescription',
				'message',
				'username',
				'roleGroup',
				'entityName',
				'entityId',
				'entityMetaDataStatus',
				{
					name: 'createDts',
					type: 'date',
					dateFormat: 'c'
				},
				{
					name: 'updateDts',
					type: 'date',
					dateFormat: 'c'
				},
				'readMessage'
			],
			proxy: {
				type: 'ajax',
				url: 'api/v1/resource/notificationevent',
				reader: {
					type: 'json',
					rootProperty: 'data',
					totalProperty: 'totalNumber'
				}
			}
		});

		var actionMarkAsRead = function (record) {
			CoreService.userservice.getCurrentUser().then(function (usercontext) {		
				Ext.Ajax.request({
					url: 'api/v1/resource/notificationevent/' + record.get('eventId'),
					method: 'PUT',
					jsonData: {
						username: usercontext.username
					},
					success: function (response) {
						notPanel.notificationGrid.getStore().load({
							params: {
								all: notPanel.loadAll
							}
						});
					}
				});
			});
		};

		var actionRemove = function (record) {
			Ext.Ajax.request({
				url: 'api/v1/resource/notificationevent/' + record.get('eventId'),
				method: 'DELETE',
				success: function (response) {
					notPanel.notificationGrid.getStore().load({
						params: {
							all: notPanel.loadAll
						}
					});
				}
			});
		};

		notPanel.notificationGrid = Ext.create('Ext.grid.Panel', {
			store: dataStore,
			columnLines: true,
			bodyStyle: 'background-color: white',
			columns: [
				{
					text: 'Unread',
					width: 75,
					align: 'center',
					dataIndex: 'readMessage',
					//xtype: 'widgetcolumn',
					renderer: function (value) {
						if (value) {
							return '';
						} else {
							return '<i class="fa fa-check" title="Mark as read"></i>';
						}
					}
				},
				{text: 'Event Date', dataIndex: 'createDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s'},
				{text: 'Event Type', groupable: 'true', dataIndex: 'eventType', width: 175,
					renderer: function(value, metaData, record, rowIndex) {

						// Set Tooltip
						metaData.tdAttr = 'data-qtip="' + record.get('eventTypeDescription') + '"';

						// Simply Return Value
						return value;
					}
				},
				{text: 'Message', dataIndex: 'message', flex: 1,
					renderer: function (value, metadata, record) {
						switch (record.get('eventType')) {
							case 'WATCH':
								return value + '<i>View the changes <a href="view.jsp?fullPage=true&id=' + record.get('entityId') + '" target="_top"><strong>here</strong></a>.</i>';
								break;
							case 'REPORT':
								return value + '<i>View/Download the report <a href="UserTool.action?load=Reports&dc=' + Math.random() + '" target="_top"><strong>here</strong></a></i>.';
								break;
							case 'ADMIN':
								return '<i class="fa fa-warning"></i>&nbsp;' + value;
								break;
							case 'TASK':
							case 'IMPORT':
							default:
								return value;
								break;
						}
					}
				},
				{
					text: 'Action',
					dataIndex: '',
					sortable: false,
					xtype: 'widgetcolumn',
					align: 'center',
					width: 75,
					requiredPermissions: ['ADMIN-NOTIFICATION-EVENT-DELETE'],
					widget: {
						xtype: 'button',
						iconCls: 'fa fa-trash',
						maxWidth: 25,
						cls: 'button-danger',
						handler: function () {
							var record = this.getWidgetRecord();
							actionRemove(record);
						}
					}
				}
			],
			listeners: {
				cellclick: function (grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {
					if (cellIndex === 0) {
						actionMarkAsRead(record);
					}
				}
			},
			dockedItems: [
				{
					dock: 'top',
					xtype: 'toolbar',
					items: [
						{
							text: 'Refresh',
							scale: 'medium',
							iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
							handler: function () {
								this.up('grid').getStore().load({
									params: {
										all: notPanel.loadAll
									}
								});
							}
						},
						{
							xtype: 'tbseparator'
						},
						{
							text: 'Unread',
							scale: 'medium',
							pressed: true,
							toggleGroup: 'filter',
							handler: function () {
								notPanel.loadAll = false,
										this.up('grid').getStore().load({
									params: {
										all: false
									}
								});
							}
						},
						{
							text: 'All',
							scale: 'medium',
							toggleGroup: 'filter',
							handler: function () {
								notPanel.loadAll = true,
										this.up('grid').getStore().load({
									params: {
										all: true
									}
								});
							}
						},
						{
							xtype: 'tbfill'
						}, 
						{
							text: 'Delete All',
							scale: 'medium',
							iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
							requiredPermissions: ['ADMIN-NOTIFICATION-EVENT-DELETE'],
							handler: function () {
								var notificationStore = this.up('grid').getStore();
								
								Ext.Msg.show({
									title:'Delete All Notifications?',
									iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
									message: 'Are you sure you want to delete all notifications?',
									buttons: Ext.Msg.YESNO,
									icon: Ext.Msg.QUESTION,
									fn: function(btn) {
										if (btn === 'yes') {
											notPanel.notificationGrid.setLoading("Deleting notifications...");
											Ext.Ajax.request({
												url: 'api/v1/resource/notificationevent/currentuser',
												method: 'DELETE',
												callback: function(){
													notPanel.notificationGrid.setLoading(false);
												},
												success: function(){
													notificationStore.load();													
												}
											});										
										} 
									}
								});
							}
						}
					]
				},
				{
					dock: 'bottom',
					xtype: 'panel',
					html: 'Loading...',
					listeners: {
						beforerender: function (panel) {
							Ext.Ajax.request({
								url: 'api/v1/service/application/configproperties/notification.max.days',
								success: function (response) {
									var keyData = Ext.decode(response.responseText);
									panel.update('*Notifications time out after <b>' + keyData.description + '</b> day(s)');
								}
							});
						}
					}
				}
			]
		});

		notPanel.add(notPanel.notificationGrid);

	},
	refreshData: function () {
		this.notificationGrid.getStore().load({
			params: {
				all: this.loadAll
			}
		});
	}

});

Ext.define('OSF.component.NotificationWindow', {
  extend: 'Ext.window.Window',
  alias: 'osf.widget.NotificationWindow',
  
  title: 'Notifications',
  iconCls: 'fa fa-envelope-o',
  y: 40,
  width: '80%',
  modal: true,
  closeAction: 'hide',
  layout: 'fit',  
  height: '50%',
  maximizable: true,
  
  initComponent: function() {
     this.callParent();
     
    var notWin = this;
    notWin.notPanel = Ext.create('OSF.component.NotificationPanel', {      
    });
    notWin.notificationGrid = notWin.notPanel.notificationGrid;
      
    notWin.add(notWin.notPanel);
   
  },
  
  refreshData: function(){
    this.notificationGrid.getStore().load({
        params: {
          all: this.loadAll
        }
    });
  }
  

});

