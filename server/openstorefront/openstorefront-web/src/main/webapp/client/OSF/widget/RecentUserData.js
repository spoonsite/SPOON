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

/* global Ext, CoreUtil, CoreService */

Ext.define('OSF.widget.RecentUserData', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.RecentUserData',

	layout: 'fit',
	
	initComponent: function () {
		this.callParent();
		
		var userDataPanel = this;
		
		userDataPanel.grid = Ext.create('Ext.grid.Panel', {
			columnLines: true,
			store: {
				fields: [
					{
						name: 'updateDts',
						type:	'date',
						dateFormat: 'c'
					}
				],
				proxy: {
					type: 'memory'	,
					reader: {
						type: 'json'
					}
				}
			},
			columns: [
				{ text: 'Data Type', dataIndex: 'dataType', width: 150 },
				{ text: 'Data', dataIndex: 'data', flex: 1, minWidth: 150 },
				{ text: 'User', dataIndex: 'username', width: 150 },
				{ text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s'  }
			]
		});
		
		userDataPanel.add(userDataPanel.grid);
		userDataPanel.refresh();
		
	},
	refresh: function(){
		var userDataPanel = this;
		
		var data = [];
		userDataPanel.setLoading("Loading Questions...");
		Ext.Ajax.request({
			url: '../api/v1/resource/components/questionviews',
			callback: function() {
				userDataPanel.setLoading(false);
			},
			success: function(response, opts) {
				var questions = Ext.decode(response.responseText);
				userDataPanel.setLoading("Loading Review...");
				Ext.Ajax.request({
					url: '../api/v1/resource/components/reviewviews',
					callback: function() {
						userDataPanel.setLoading(false);
					},					
					success: function(response, opts) {
						var reviews = Ext.decode(response.responseText);
						userDataPanel.setLoading("Loading Tags...");
						Ext.Ajax.request({
							url: '../api/v1/resource/components/tagviews',
							callback: function() {
								userDataPanel.setLoading(false);
							},							
							success: function(response, opts) {
								var tags = Ext.decode(response.responseText);
								
								userDataPanel.setLoading("Loading Contacts...");
								Ext.Ajax.request({
									url: '../api/v1/resource/contacts',
									callback: function() {
										userDataPanel.setLoading(false);
									},									
									success: function(response, opts) {
										var contacts = Ext.decode(response.responseText);
										
										Ext.Array.each(questions, function(item){
											data.push({
												dataType: 'Question',
												data: item.question,
												username: item.username,
												updateDts: item.updateDts
											});
										});
										
										Ext.Array.each(reviews, function(item){
											data.push({
												dataType: 'Reviews',
												data: item.title,
												username: item.username,
												updateDts: item.updateDate
											});
										});
										
										Ext.Array.each(tags, function(item){
											data.push({
												dataType: 'Tags',
												data: item.text,
												username: item.createUser,
												updateDts: item.createDts
											});
										});
										
										Ext.Array.each(contacts.data, function(item){
											if (!item.adminModified && item.updateUser !== 'ANONYMOUS') {												
												data.push({
													dataType: 'Contact',
													data: item.firstName + ' ' + item.lastName,
													username: item.updateUser,
													updateDts: item.updateDts
												});
											}
										});										
										
										//filter by date
										data = Ext.Array.filter(data, function(item){
											var now = new Date();
											var max = Ext.Date.subtract(now, Ext.Date.DAY, 30);
											var updateDts = Ext.Date.parse(item.updateDts, 'c');
											if (updateDts < max) {
												return false;
											} else {
												return true;
											}
										});


										userDataPanel.grid.getStore().loadData(data);
									}
								});
							}
						});
					}
				});				
			}
		});
		
		
		
		
	}

});
