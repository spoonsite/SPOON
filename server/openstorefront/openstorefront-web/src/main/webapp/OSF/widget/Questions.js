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

Ext.define('OSF.widget.Questions', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.Questions',

	layout: 'fit',
	
	initComponent: function () {
		this.callParent();
		
		var questionPanel = this;
		
		questionPanel.grid = Ext.create('Ext.grid.Panel', {
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
						name: 'questionUpdateDts',
						type:	'date',
						dateFormat: 'c'
					}
				],
				proxy: {
					type: 'ajax',
					url: 'api/v1/resource/componentquestions/'
				}				
			},
			columns: [
				{ text: 'Entry', dataIndex: 'componentName', width: 275 },
				{ text: 'Question', dataIndex: 'question', flex: 1, minWidth: 200 },
				{ text: 'Post Date', dataIndex: 'createDts', width: 200, xtype: 'datecolumn', format:'m/d/y H:i:s' },
				{ text: 'Update Date', dataIndex: 'questionUpdateDts', width: 200, xtype: 'datecolumn', format:'m/d/y H:i:s' }				
			],
			listeners: {
				itemdblclick: function(grid, record, item, index, e, opts){
					viewResponse(record);
				},
				selectionchange: function(selectionModel, selected, opts){
					var tools = this.getComponent('tools');

					if (selected.length > 0) {	
						tools.getComponent('view').setDisabled(false);
					} else {
						tools.getComponent('view').setDisabled(true);
					}
				}
			},			
			dockedItems: [
				{
					dock: 'top',
					xtype: 'toolbar',
					itemId: 'tools',
					items: [
						{
							text: 'View Responses',
							itemId: 'view',
							scale: 'medium',
							disabled: true,
							iconCls: 'fa fa-2x fa-binoculars',
							handler: function () {
								var record = this.up('grid').getSelectionModel().getSelection()[0];								
								viewResponse(record);
							}							
						}
					]
				}
			]
		});
		
		var viewResponse = function(record) {
			
			var responseWin = Ext.create('Ext.window.Window', {
				title: 'Responses',
				modal: true,
				width: '70%',
				height: '60%',
				maximizable: true,
				closeMode: 'destroy',
				layout: 'fit',
				items: [
					{
						xtype: 'grid',
						columnLines: true,
						store: {
							sorters: [{
								property: 'componentName',
								direction: 'ASC'
							}],	
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
								}
							],
							proxy: {
								type: 'ajax',
								url: 'api/v1/resource/componentquestions/' + record.get('questionId') + '/responses'
							}		
						},
						columns: [
							{ text: 'Entry', dataIndex: 'componentName', width: 275	},
							{ text: 'Response', dataIndex: 'response', flex: 1, minWidth: 200 },
							{ text: 'Post Date', dataIndex: 'answeredDate', width: 200, xtype: 'datecolumn', format:'m/d/y H:i:s' },
							{ text: 'Update Date', dataIndex: 'updateDts', width: 200, xtype: 'datecolumn', format:'m/d/y H:i:s' }
						],						
						dockedItems: [
							{
								dock: 'top',
								xtype: 'panel',
								data: record.data,
								tpl: '<div style="padding: 10px; font-weight: bold;">{question}</div>'
							}					
						]
					}
				]
			});
				
			responseWin.show();
		};	
		
		questionPanel.add(questionPanel.grid);
		
		CoreService.usersevice.getCurrentUser().then(function(response){
			var userProfile = Ext.decode(response.responseText);
			questionPanel.grid.getStore().load({
				url: 'api/v1/resource/componentquestions/' + userProfile.username
			});
			
		});		
		
	},
	refresh: function() {
		var questionPanel = this;
		questionPanel.grid.getStore().reload();		
	}
	
});
