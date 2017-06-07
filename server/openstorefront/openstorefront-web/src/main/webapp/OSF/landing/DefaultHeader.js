/* 
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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

/* global Ext */

Ext.define('OSF.landing.DefaultHeader', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-defaultheader',
	
	
	region: 'north',					
	border: false,					
	//cls: 'border_accent',	
	dockedItems: [						
		{
			xtype: 'toolbar',
			dock: 'top',								
			cls: 'nav-back-color',			
			listeners: {
				resize: function(toolbar, width, height, oldWidth, oldHeight, eOpts) {
					if (width < 1024) {
						toolbar.getComponent('spacer').setHidden(true);
						toolbar.getComponent('notificationBtn').setText('');										
					} else {
						toolbar.getComponent('spacer').setHidden(false);										
						toolbar.getComponent('notificationBtn').setText('Notifications');										
					}
					toolbar.updateLayout(true, true);
				}
			},
			items: [
				{
					xtype: 'tbspacer',
					itemId: 'spacer',
					width: 250
				},								
				{
					xtype: 'tbfill'
				},
				{
					xtype: 'tbtext',
					itemId: 'homeTitle',
					text: '',
					cls: 'page-title'
				},
				{
					xtype: 'tbfill'
				},
				{
					xtype: 'button',									
					itemId: 'notificationBtn',
					scale   : 'large',
					ui: 'default',
					iconCls: 'fa fa-2x fa-envelope-o',
					iconAlign: 'left',
					text: 'Notifications',
					handler: function() {
						//var headerPanel = this.up('panel');
						var notWin = Ext.create('OSF.component.NotificationWindow', {								
							closeAction: 'destroy'
						});
						notWin.show();
						//notWin.refreshData();
					}
				},		
				{
					xtype: 'osf-UserMenu',
					ui: 'default',
					initCallBack: function(usercontext) {				
						setupServerNotifications(usercontext);	
					}
				}
			]
		}						
	],
	initComponent: function () {
		this.callParent();			
		var headerPanel = this;
		
		var checkNotifications = function(){
			Ext.Ajax.request({
				url: 'api/v1/resource/notificationevent',
				success: function(response, opts) {
					var data = Ext.decode(response.responseText);

					var unreadCount = 0;
					Ext.Array.each(data.data, function(item){
						if (!item.readMessage) {
							unreadCount++;
						}
					});

					if (unreadCount > 0) {
						Ext.toast({
							title: 'Notifications',
							html: 'You have <span style="font-size: 16px; font-weight: bold;"> ' + unreadCount + ' unread</span> notifications.',
							align: 'br',
							closable: true
						});
					}
				}
			});

		};
		checkNotifications();		
		
	}	
	
});
