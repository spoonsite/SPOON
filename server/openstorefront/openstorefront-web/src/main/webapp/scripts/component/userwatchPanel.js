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
				name: 'lastSubmitDts',
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
			url: 'api/v1/resource/userprofile/watches/'
		}		
	},		
	columns: [
		{ text: 'Entry', dataIndex: 'componentName', flex: 1, minWidth: 200, 
			renderer: function(value, meta, record) {
				if (record.get('lastUpdateDts') > record.get('lastViewDts')) {
					return '<span class="label-text-bold">' + value + '</span><span class="updated-watch text-success"> UPDATED </span>';
				} else {
					return '<span class="label-text-bold">' + value + '</span>';
				}
			}
		},	
		{ text: 'Last Vendor Update', align: 'center', dataIndex: 'lastSubmitDts', width: 230,
			renderer: function(value, meta, record) {
				
				// Check Last Vendor Update Date @see{Component#SubmittedDts}, add green-bg class if later than user view
				if(!value || value == 'undefined' || value == null){
					return "Imported / NA";
				}
				else if (record.get('lastUpdateDts') > record.get('lastViewDts')) {
					meta.tdCls = 'alert-success';
				}
				return Ext.util.Format.date(value, 'm/d/y H:i:s');
			}
		},
		{ text: 'Vendor Update Approved Date', align: 'center', dataIndex: 'lastUpdateDts', width: 200,
			renderer: function(value, meta, record) {
				if (record.get('lastUpdateDts') > record.get('lastViewDts')) {
					meta.tdCls = 'alert-success';
				}
				return Ext.util.Format.date(value, 'm/d/y H:i:s');
			}
		},
		{ text: 'Last Date Viewed By You', align: 'center', dataIndex: 'lastViewDts', width: 200,
			renderer: function(value, meta, record) {
				return Ext.util.Format.date(value, 'm/d/y H:i:s');
			}			
		},
		{ text: 'Send Email Notification', align: 'center', dataIndex: 'notifyFlg', width: 200,
			renderer: function(value, meta, record) {
				if (value) {
					meta.tdCls = 'alert-success';
					return '<i class="fa fa-lg fa-check"></i>';
				} else {
					meta.tdCls = 'alert-danger';
					return '<i class="fa fa-lg fa-close"></i>';
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
					iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
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
					width: '150px',
					iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction-view',
					handler: function () {
						var grid = this.up('grid');
						var componentId = this.up('grid').getSelectionModel().getSelection()[0].get('componentId');
						var frame = Ext.create('OSF.ux.IFrame', {							
						});

						var entryViewWindow = Ext.create('Ext.window.Window', {
							title: 'View Entry',
							iconCls: 'fa fa-lg fa-eye',
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
						frame.load('view.jsp?fullPage=true&embedded=true&id=' + componentId);	
						
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
					iconCls: 'fa fa-2x fa-power-off icon-button-color-default',
					handler: function () {
						var grid = this.up('grid');
						var record = this.up('grid').getSelectionModel().getSelection()[0];
						if (record.get('notifyFlg')) {
							record.set('notifyFlg', false);
						} else {
							record.set('notifyFlg', true);
						}
						grid.setLoading("Updating...");
						
						// server can't handle null members - some entries lack dates
						if(!record.data.lastSubmitDts){
							// server can handle undefined
							record.data.lastSubmitDts = undefined
						}
						
						Ext.Ajax.request({
							url:'api/v1/resource/userprofiles/'+grid.user+'/watches/'+record.get('watchId'),
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
					text: 'Delete',
					itemId: 'delete',
					scale: 'medium',
					disabled: true,
					iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
					handler: function () {
						var grid = this.up('grid');
						var record = this.up('grid').getSelectionModel().getSelection()[0];
						
						Ext.Msg.show({
							title:'Delete Watch?',
							iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
							message: 'Are you sure you want to delete this watch?',
							buttons: Ext.Msg.YESNO,
							icon: Ext.Msg.QUESTION,
							fn: function(btn) {
								if (btn === 'yes') {
									grid.setLoading("Deleting...");
									Ext.Ajax.request({
										url: 'api/v1/resource/userprofiles/'+grid.user+'/watches/'+record.get('watchId'),
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
		
		CoreService.userservice.getCurrentUser().then(function(userProfile) {					
			watchPanel.user = userProfile.username;
			watchPanel.getStore().load({
				url: 'api/v1/resource/userprofiles/'+ watchPanel.user +'/watches/'
			});
		});
	},
	
	actionRefresh: function() {
		var watchPanel = this;		
		watchPanel.getStore().load({
			url: 'api/v1/resource/userprofiles/'+ watchPanel.user +'/watches/'
		});
	}
	
});