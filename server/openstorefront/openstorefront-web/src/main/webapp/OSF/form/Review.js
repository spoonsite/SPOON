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
			bufferedRenderer: false,
			viewConfig: {
				enableTextSelection: true
			},				
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
						tools.getComponent('reply').setDisabled(false);
						
						//check ownership or user is admin (later this will be has permission)
						var record = records[0];						
						if (reviewForm.user.admin || 
							record.get('createUser') === reviewForm.user.username) {
						
							tools.getComponent('edit').setDisabled(false);
							tools.getComponent('delete').setDisabled(false);							
						}
						
					} else {
						tools.getComponent('acknowledge').setDisabled(true);
						tools.getComponent('reply').setDisabled(true);
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
				{ text: 'Evaluation Item', dataIndex: 'entity', width: 200,				
					renderer: function(value) {
						return '<b>'+ value + '</b>';
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
							text: 'Reply',
							itemId: 'reply',
							iconCls: 'fa fa-lg fa-reply icon-button-color-refresh icon-small-vertical-correction',
							scale: 'medium',
							disabled: true,							
							handler: function(){
								var record = reviewForm.grid.getSelectionModel().getSelection()[0];	
								actionEditReply(record, true);
							}							
						},						
						{
							text: 'Edit',
							itemId: 'edit',
							iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
							width: '100px',
							scale: 'medium',
							disabled: true,							
							handler: function(){
								var record = reviewForm.grid.getSelectionModel().getSelection()[0];		
								actionEditReply(record, false);
							}							
						},
						{
							xtype: 'tbseparator'
						},
						{
							text: 'Toggle Acknowledge',
							itemId: 'acknowledge',
							iconCls: 'fa fa-2x fa-power-off icon-button-color-default icon-vertical-correction',
							scale: 'medium',
							disabled: true,
							handler: function(){
								var record = reviewForm.grid.getSelectionModel().getSelection()[0];								
								reviewForm.grid.setLoading('Updating record...');								
								Ext.Ajax.request({
									url: 'api/v1/resource/evaluations/' + record.get('evaluationId') + '/comments/' + record.get('commentId') + '/acknowlege',
									method: 'PUT',
									callback: function(){
										reviewForm.grid.setLoading(false);
									},
									success: function(response, opts) {
										reviewForm.grid.getStore().reload();
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
							iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
							scale: 'medium',
							disabled: true,							
							handler: function(){
								var record = reviewForm.grid.getSelectionModel().getSelection()[0];		
								actionDelete(record);
							}							
						}
					]
				}
			]
		});
		
		var actionEditReply = function(record, reply) {
			
			var recordName = record.get('createUser') + ' ' + record.get('createDts');
			
			var editWindow = Ext.create('Ext.window.Window', {
				title: reply ? 'Reply to Comment - ' + recordName : 'Edit Comment - ' + recordName,
				modal: true,
				closeAction: 'destroy',
				width: '60%',
				height: '50%',
				layout: 'fit',
				items: [
					{
						xtype: 'form',
						itemId: 'form',
						layout: 'fit',
						items: [
							{
								xtype: 'hidden',
								name: 'commentId'
							},
							{
								xtype: 'hidden',
								name: 'replyCommentId'
							},
							{
								xtype: 'hidden',
								name: 'entity'
							},
							{
								xtype: 'hidden',
								name: 'entityId'
							},							
							{
								xtype: 'htmleditor',
								name: 'comment',									
								width: '100%',
								fieldBodyCls: 'form-comp-htmleditor-border',
								maxLength: 4000
							}
						],
						dockedItems: [
							{
								xtype: 'toolbar',
								dock: 'bottom',
								items: [
									{
										text: 'Comment',
										iconCls: 'fa fa-2x fa-comment icon-button-color-save icon-vertical-correction-save',
										width: '130px',
										scale: 'medium',
										handler: function() {
											var form = this.up('form');
											var data = form.getValues();
											data.acknowledge = false;

											var method = 'POST';
											var update = '';		
											if (data.commentId) {
												method = 'PUT',
												update = '/' + data.commentId 		
											}
											var evaluationId = record.get('evaluationId');

											if (!data.entity) {
												data.entity = 'Evaluation';
												data.entityId = evaluationId;	
											}

											CoreUtil.submitForm({
												url: 'api/v1/resource/evaluations/' + evaluationId + '/comments' + update,
												method: method,
												data: data,
												form: form,
												success: function(){
													reviewForm.grid.getStore().reload();
													editWindow.close();
												}
											});												
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Cancel',
										iconCls: 'fa fa-2x fa-close icon-button-color-warning icon-vertical-correction',
										scale: 'medium',
										handler: function() {
											editWindow.close();
										}										
									}
								]
							}
						]
					}
				]
			});
			editWindow.show();
			
			if (reply) {
				var replyRecord = Ext.create('Ext.data.Model', {					
				});			
				replyRecord.set('replyCommentId', record.get('commentId'));
				replyRecord.set('entity', record.get('entity'));
				replyRecord.set('entityId', record.get('entityId'));
				
				editWindow.getComponent('form').loadRecord(replyRecord);
			} else {
				editWindow.getComponent('form').loadRecord(record);
			}
			
		};
		
		var actionDelete = function(record) {
			Ext.Msg.show({
				title:'Delete Comment',
				iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
				message: 'Are you sure you want DELETE this comment?',
				buttons: Ext.Msg.YESNO,
				icon: Ext.Msg.QUESTION,
				fn: function(btn) {
					if (btn === 'yes') {
						reviewForm.grid.setLoading('Deleting...');
						Ext.Ajax.request({
							url: 'api/v1/resource/evaluations/' + record.get('evaluationId') + '/comments/' + record.get('commentId'),
							method: 'DELETE',
							callback: function() {
								reviewForm.grid.setLoading(false);
							},
							success: function() {
								reviewForm.grid.getStore().reload();
							}
						});
					} 
				}
			});		
			
		};
		
		
		reviewForm.add(reviewForm.grid);
	},
	loadData: function(evaluationId, componentId, data, opts) {
		var reviewForm = this;
		
		reviewForm.evaluationId = evaluationId;
		reviewForm.grid.getStore().load({
			url: 'api/v1/resource/evaluations/' + evaluationId + '/comments'
		});
		reviewForm.user = opts.user;
		reviewForm.mainForm = opts.mainForm;
	
	}
});

