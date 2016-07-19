/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* global Ext, CoreService */

Ext.define('OSF.component.UserWatchPanel', {
	extend: 'Ext.grid.Panel',
	alias: 'osf.widget.UserWatchPanel',
	
	columnLines: true,	
	store: {
		sorters: [{
			property: 'componentName',
			direction: 'ASC'
		}],	
		autoLoad: false,
		fields: [
			{
				name: 'createDts',
				type:	'date',
				dateFormat: 'c'
			},
			{
				name: 'lastUpdateDts',
				type:	'date',
				dateFormat: 'c'
			},
			{
				name: 'lastViewDts',
				type:	'date',
				dateFormat: 'c'
			}			
		],
		proxy: {
			type: 'ajax',
			url: '../api/v1/resource/userprofile/watches/'
		}		
	},		
	columns: [
		{ text: 'Entry', dataIndex: 'componentName', flex: 1, minWidth: 200, 
			renderer: function(value, meta, record) {
				if (record.get('lastUpdateDts') > record.get('lastViewDts')) {
					meta.tdCls = 'alert-success';
					return '<h2>' + value + '</h2><br>*Updated';
				} else {
					return '<h2>' + value + '</h2>';
				}
			}
		},	
		{ text: 'Entry Last Update Date', align: 'center', dataIndex: 'lastUpdateDts', width: 200,
			renderer: function(value, meta, record) {
				if (record.get('lastUpdateDts') > record.get('lastViewDts')) {
					meta.tdCls = 'alert-success';
				}
				return Ext.util.Format.date(value, 'm/d/y H:i:s');
			}
		},
		{ text: 'Last View Date', align: 'center', dataIndex: 'lastViewDts', width: 200,
			renderer: function(value, meta, record) {
				if (record.get('lastUpdateDts') > record.get('lastViewDts')) {
					meta.tdCls = 'alert-success';
				}
				return Ext.util.Format.date(value, 'm/d/y H:i:s');
			}			
		},
		{ text: 'Send Email Notification', align: 'center', dataIndex: 'notifyFlg', width: 200,
			renderer: function(value, meta, record) {
				if (value) {
					meta.tdCls = 'alert-success';
					return '<i class="fa fa-4x fa-check"></i>';
				} else {
					meta.tdCls = 'alert-danger';
					return '<i class="fa fa-4x fa-close"></i>';
				}
			}
		},
		{ text: 'Create Date', align: 'center', dataIndex: 'createDts', width: 200, hidden: true, xtype: 'datecolumn', format:'m/d/y H:i:s' }
	],	
	dockedItems: [
		{
			dock: 'top',
			xtype: 'toolbar',
			itemId: 'tools',
			items: [
				{
					text: 'Refresh',
					scale: 'medium',								
					iconCls: 'fa fa-2x fa-refresh',
					handler: function () {
						this.up('grid').actionRefresh();
					}
				},
				{
					xtype: 'tbseparator'
				},
				{
					text: 'View Entry',
					itemId: 'view',
					scale: 'medium',
					disabled: true,
					iconCls: 'fa fa-2x fa-binoculars',
					handler: function () {
						var grid = this.up('grid');
						var componentId = this.up('grid').getSelectionModel().getSelection()[0].get('componentId');
						var frame = Ext.create('OSF.ux.IFrame', {							
						});

						var entryViewWindow = Ext.create('Ext.window.Window', {
							title: 'Entry',
							maximizable: true,
							modal: true,
							closeMode: 'destroy',
							width: '70%',
							height: '70%',
							layout: 'fit',
							items: [
								frame
							]
						});					
						entryViewWindow.show();
						frame.load('view.jsp?fullPage=true&id=' + componentId);	
						
						frame.on('load', function(){
							Ext.defer(function(){
								grid.actionRefresh();
							}, 2000);
						});
					}									
				},
				{
					xtype: 'tbseparator'
				},				
				{
					text: 'Toggle Email Notification',
					itemId: 'toggle',
					scale: 'medium',
					disabled: true,
					iconCls: 'fa fa-2x fa-toggle-on',
					handler: function () {
						var grid = this.up('grid');
						var record = this.up('grid').getSelectionModel().getSelection()[0];
						if (record.get('notifyFlg')) {
							record.set('notifyFlg', false);
						} else {
							record.set('notifyFlg', true);
						}
						grid.setLoading("Updating...");
						Ext.Ajax.request({
							url:'../api/v1/resource/userprofiles/'+grid.user+'/watches/'+record.get('watchId'),
							method: 'PUT',
							jsonData: record.data,
							callback: function(){
								grid.setLoading(false);
							},
							success: function(){
								grid.actionRefresh();
							}														
						});
					}									
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Remove Watch',
					itemId: 'delete',
					scale: 'medium',
					disabled: true,
					iconCls: 'fa fa-2x fa-trash-o',
					handler: function () {
						var grid = this.up('grid');
						var record = this.up('grid').getSelectionModel().getSelection()[0];
						
						Ext.Msg.show({
							title:'Remove Watch?',
							message: 'Are you sure you want to remove this Watch?',
							buttons: Ext.Msg.YESNO,
							icon: Ext.Msg.QUESTION,
							fn: function(btn) {
								if (btn === 'yes') {
									grid.setLoading("Removing...");
									Ext.Ajax.request({
										url: '../api/v1/resource/userprofiles/'+grid.user+'/watches/'+record.get('watchId'),
										method: 'DELETE',
										callback: function(){
											grid.setLoading(false);
										},
										success: function(){
											grid.actionRefresh();
										}
									});
								} 
							}
						});							
					}									
				}								
			]
		}		
	],
	listeners: {	
		selectionchange: function(selectionModel, selected, opts){
			var tools = this.getComponent('tools');

			if (selected.length > 0) {	
				tools.getComponent('view').setDisabled(false);
				tools.getComponent('toggle').setDisabled(false);
				tools.getComponent('delete').setDisabled(false);
			} else {
				tools.getComponent('view').setDisabled(true);
				tools.getComponent('toggle').setDisabled(true);
				tools.getComponent('delete').setDisabled(true);
			}
		}
	},	

	initComponent: function () {
		this.callParent();

		var watchPanel = this;	
		
		CoreService.usersevice.getCurrentUser().then(function(response) {
			var userProfile = Ext.decode(response.responseText);			
			watchPanel.user = userProfile.username;
			watchPanel.getStore().load({
				url: '../api/v1/resource/userprofiles/'+ watchPanel.user +'/watches/'
			});
		});
	},
	
	actionRefresh: function() {
		var watchPanel = this;		
		watchPanel.getStore().load({
			url: '../api/v1/resource/userprofiles/'+ watchPanel.user +'/watches/'
		});
	}
	
});