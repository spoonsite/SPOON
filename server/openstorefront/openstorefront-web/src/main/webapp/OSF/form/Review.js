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

Ext.define('OSF.form.Review', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.form.Review',

	layout: 'fit',	
	initComponent: function () {		
		this.callParent();
		
		var reviewForm = this;
		
		reviewForm.grid = Ext.create('Ext.grid.Panel', {
			columnLines: true,
			store: {
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
					url: ''
				}
			},
			listeners: {
				selectionchange: function(selModel, records, index, opts){
					var tools = reviewForm.grid.getComponent('tools');
					if (records.length > 0) {
						tools.getComponent('acknowledge').setDisabled(false);
						
						//check ownership or user is admin (later this will be has permission)
						var record = records[0];						
						if (reviewForm.user.admin || 
							record.get('createUser') === reviewForm.user.username) {
						
							tools.getComponent('edit').setDisabled(false);
							tools.getComponent('delete').setDisabled(false);							
						}
						
					} else {
						tools.getComponent('acknowledge').setDisabled(true);
						tools.getComponent('edit').setDisabled(true);
						tools.getComponent('delete').setDisabled(true);
					}										
				}
			},
			columns: [
				{ text: 'Acknowledge', dataIndex: 'acknowledge', align: 'center', width: 75,
					renderer: function(value) {
						if (value) {
							return '<span class="fa fa-lg fa-check text-success"></span>';
						} else {
							return '<span class="fa fa-lg fa-close text-danger"></span>';
						}
					}
				},
				{ text: 'Evaluation Item', align: 'center', dataIndex: 'entity', width: 200,
					xtype: 'widgetcolumn',
					widget: {
						xtype: 'button'						
					},
					onWidgetAttach: function (column, widget, record) {
						if (record.get('entity') === 'Evaluation') {
							widget.setText('Evaluation');
						}
					}
				},
				{ text: 'Comment', dataIndex: 'comment', flex: 1, minWidth: 250, cellWrap: true },
				{ text: 'Posted User', dataIndex: 'createUser', width: 200 },
				{ text: 'Posted Date', dataIndex: 'createDts', width: 200 }
			],
			dockedItems: [
				{
					xtype: 'toolbar',
					dock: 'top',
					itemId: 'tools',
					items: [
						{
							text: 'Toggle Acknowledge',
							itemId: 'acknowledge',
							iconCls: 'fa fa-2x fa-check text-success',
							scale: 'medium',
							disabled: true,
							handler: function(){
								
							}
						},
						{
							text: 'Edit',
							itemId: 'edit',
							iconCls: 'fa fa-2x fa-edit',
							scale: 'medium',
							disabled: true,							
							handler: function(){
								
							}							
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Delete',
							itemId: 'delete',
							iconCls: 'fa fa-2x fa-close text-danager',
							scale: 'medium',
							disabled: true,							
							handler: function(){
								
							}							
						}
					]
				}
			]
		});
		reviewForm.add(reviewForm.grid);
	},
	loadData: function(evaluationId, componentId, data, opts) {
		var reviewForm = this;
		
		reviewForm.grid.getStore().load({
			url: 'api/v1/resource/evaluations/' + evaluationId + '/comments'
		});
		reviewForm.user = opts.user;
		
		opts.commentPanel.loadComments(evaluationId, null, null);
	}
});

