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

/* global Ext, CoreUtil */

Ext.define('OSF.widget.Feedback', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.Feedback',

	layout: 'fit',
	
	initComponent: function () {
		this.callParent();
		
		var feedbackPanel = this;
		
		var viewWindow = Ext.create('Ext.window.Window', {
			title: 'Details',
			iconCls: 'fa fa-eye',
			modal: true,
			width: '70%',
			height: '60%',
			maximizable: true,
			scrollable: 'true',
			bodyStyle: 'padding: 10px;',
			tpl:  new Ext.XTemplate(
				'<h2><b>Summary:</b> {summary}</h2>',
				'<b>Reported Issue Type:</b> {ticketType} <br>',						
				'<b>Submitted Date:</b> {[Ext.util.Format.date(values.createDts, "m/d/y H:i:s")]} <br>',
				'<br>',
				'<b>Description:</b><br>',
				'{description}',
				'<br><br>',
				'<table class="info-table">',
				'<tr class="info-table"><td class="info-table" style="width: 20%;"><b>Reported Issue Type</b></td><td class="info-table"> {ticketType}</td></tr>',
				'<tr class="info-table"><td class="info-table" style="width: 20%;"><b>Reported Username</b></td><td class="info-table"> {username}</td></tr>',
				'<tr class="info-table"><td class="info-table" style="width: 20%;"><b>Reported Firstname</b></td><td class="info-table"> {firstname}</td></tr>',
				'<tr class="info-table"><td class="info-table" style="width: 20%;"><b>Reported Lastname</b></td><td class="info-table"> {lastname}</td></tr>',
				'<tr class="info-table"><td class="info-table" style="width: 20%;"><b>Reported Organization</b></td><td class="info-table"> {organization}</td></tr>',
				'<tr class="info-table"><td class="info-table" style="width: 20%;"><b>Reported Email</b></td><td class="info-table"> {email}</td></tr>',
				'<tr class="info-table"><td class="info-table" style="width: 20%;"><b>Reported Phone</b></td><td class="info-table"> {phone}</td></tr>',
				'<tr class="info-table"><td class="info-table" style="width: 20%;"><b>Web Location</b></td><td class="info-table"> {webInformation.location}</td></tr>',
				'<tr class="info-table"><td class="info-table" style="width: 20%;"><b>Web User-agent</b></td><td class="info-table"> {webInformation.userAgent}</td></tr>',
				'<tr class="info-table"><td class="info-table" style="width: 20%;"><b>Web Referrer</b></td><td class="info-table"> {webInformation.referrer}</td></tr>',
				'<tr class="info-table"><td class="info-table" style="width: 20%;"><b>Web Screen Resolution</b></td><td class="info-table"> {webInformation.screenResolution}</td></tr>',
				'</table>'		
			)
		});			
		
		var actionView = function(record) {
			viewWindow.show();
			viewWindow.update(record);
		};			
		
		
		feedbackPanel.grid = Ext.create('Ext.grid.Panel', {
			columnLines: true,
			store: {
				pageSize: 100,
				autoLoad: true,
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
					},
					{
						name: 'lastViewDts',
						type:	'date',
						dateFormat: 'c'
					}	
				],
				proxy: CoreUtil.pagingProxy({
					type: 'ajax',
					url: '../api/v1/resource/feedbacktickets',
					reader: {
						type: 'json',
						rootProperty: 'data',
						totalProperty: 'totalNumber'
					}							
				})						
			},			
			columns: [
				{ text: 'Ticket Type', dataIndex: 'ticketType',  width: 150},
				{ text: 'Summary', dataIndex: 'summary', flex: 1, minWidth: 200},
				{ text: 'Description', dataIndex: 'description', flex: 2, minWidth: 200},
				{ text: 'Create Date', dataIndex: 'createDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
				{ text: 'Username', dataIndex: 'username', width: 200}			
			],
			listeners: {
				itemdblclick: function(grid, record, item, index, e, opts){
					actionView(record);
				},						
				selectionchange: function(selModel, selected, opts) {
					var tools = this.getComponent('tools');

					if (selected.length > 0) {	
						tools.getComponent('view').setDisabled(false);							

						var record = selected[0];
						if (record.get('activeStatus') === 'A') {
							tools.getComponent('complete').setDisabled(false);
						} else {
							tools.getComponent('complete').setDisabled(true);
						}
					} else {
						tools.getComponent('view').setDisabled(true);
						tools.getComponent('complete').setDisabled(true);
					}
				}
			},
			dockedItems: [
				{			
					xtype: 'toolbar',
					dock: 'top',
					itemId: 'tools',
					items: [
						{
							text: 'View Details',
							itemId: 'view',
							scale: 'medium',
							disabled: true,
							iconCls: 'fa fa-2x fa-edit',
							handler: function () {
								actionView(this.up('grid').getSelectionModel().getSelection()[0]);										
							}									
						},								
						{
							xtype: 'tbseparator'
						},
						{
							text: 'Mark Complete',
							itemId: 'complete',
							scale: 'medium',
							disabled: true,
							iconCls: 'fa fa-2x fa-edit',
							handler: function () {
								actionMarkStatus(this.up('grid'), this.up('grid').getSelectionModel().getSelection()[0], true);										
							}									
						}						
					]
				}
			]
		});
		
		var actionMarkStatus = function(grid, record, complete) {

			var operation = '/markoutstanding';
			if (complete) {
				operation = '/markcomplete';
			}

			grid.setLoading('Updating Status...');
			Ext.Ajax.request({
				url: '../api/v1/resource/feedbacktickets/' + record.get('feedbackId') +operation ,
				method: 'PUT',
				callback: function(){
					grid.setLoading(false);
				},
				success: function(response, opts) {
					feedbackPanel.refresh();
				}
			});		
		};		
		
		
		feedbackPanel.add(feedbackPanel.grid);
		
	},
	refresh: function() {
		var feedbackPanel = this;
		feedbackPanel.grid.getStore().reload();
	}	
	
});
