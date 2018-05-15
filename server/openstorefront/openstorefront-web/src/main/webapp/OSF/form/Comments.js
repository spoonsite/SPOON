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
/* global Ext, CoreUtil */

Ext.define('OSF.form.Comments', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.form.Comments',
	layout: 'fit',
	
	initComponent: function () {
		this.callParent();
		var commentPanel = this;
		var actionAddComment = function(form){
			var data = form.getValues();
			var componentId = commentPanel.componentId;
			var method = 'POST';
			var update = '';
			if(data.commentId){
				update = '/' + data.commentId;
				method = 'PUT'
			}
			CoreUtil.submitForm({
				url: 'api/v1/resource/components/' + componentId + '/comments' + update,
				method: method,
				data: data,
				form: form,
				success: function(){
					commentPanel.commentGrid.getStore().reload();
					form.reset();
				}
			});	
		};

		commentPanel.commentGrid = Ext.create('Ext.grid.Panel',{
			columnLines: true,
			store: Ext.create('Ext.data.Store', {
				fields: [			
					{
						name: 'createDts',
						type:	'date',
						dateFormat: 'c'
					}														
				],
				autoLoad: false,
				proxy: {
					type: 'ajax'							
				}
			}),
			columns: [
				{ text: 'Comment', dataIndex: 'comment', flex: 1, minWidth: 200 },
				{ text: 'Comment Type', align: 'center', dataIndex: 'commentType', width: 150 },
				{ text: 'Create User', align: 'center', dataIndex: 'createUser', width: 150 },
				{ text: 'Create Date', dataIndex: 'createDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
				{ text: 'Security Marking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: true },
				{ text: 'Data Sensitivity',  dataIndex: 'dataSensitivity', width: 150, hidden: true }
			],
			listeners: {
				selectionchange: function(grid, record, index, opts){
					var fullgrid = commentPanel.commentGrid;
					if (fullgrid.getSelectionModel().getCount() === 1) {
						fullgrid.down('toolbar').getComponent('removeBtn').setDisabled(false);
						fullgrid.down('toolbar').getComponent('edit').setDisabled(false);
					} else {
						fullgrid.down('toolbar').getComponent('removeBtn').setDisabled(true);
						fullgrid.down('toolbar').getComponent('edit').setDisabled(true);
					}
				}						
			},
            dockedItems: [
				{
					xtype: 'form',
					itemId: 'form',
					title: 'Comments:',
					layout: 'anchor',
					padding: 10,
					defaults: {
						labelAlign: 'top',
						labelSeparator: ''
					},
					buttonAlign: 'center',
					buttons: [
						{
							xtype: 'button',
							text: 'Save',
							formBind: true,
							iconCls: 'fa fa-save',
							margin: '30 0 0 0',
							minWidth: 75,
							handler: function(){
								actionAddComment(this.up('form'));
							}
						},
						{
							xtype: 'button',
							text: 'cancel',
							iconCls: 'fa fa-close',
							margin: '30 0 0 0',
							minWidth: 75,
							handler: function(){
								this.up('form').reset();
							}
						}
					],
					items: [
						{
							xtype: 'textarea',
							name: 'comment',
							fieldLabel: 'Component Comments:',
							width: '100%',
							labelWidth: 150,
							maxLength: 4096
						},
						{
							xtype: 'form',
							items: [
								Ext.create('OSF.component.StandardComboBox', {
									name: 'commentType',									
									allowBlank: false,
									editable: false,
									typeAhead: false,
									margin: '0 0 5 0',
									width: '100%',
									fieldLabel: 'Comment Type <span class="field-required" />',
									storeConfig: {
										url: 'api/v1/resource/lookuptypes/ComponentCommentType'
									},
								})
							]
						},
						{
							xtype: 'hidden',
							name: 'commentId'
						},
					]
				},						
				{
					xtype: 'toolbar',
					items: [							
						{
							text: 'Refresh',
							iconCls: 'fa fa-lg fa-refresh icon-button-color-refresh',
							handler: function(){
								this.up('grid').getStore().reload();
							}
						},
						{
							text: 'Edit',
							itemId: 'edit',
							iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
							disabled: true,
							handler: function () {
								var record = commentPanel.commentGrid.getSelection()[0];
								actionEdit(record);
							}
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Delete',
							itemId: 'removeBtn',
							iconCls: 'fa fa-lg fa-trash icon-button-color-warning',									
							disabled: true,
							handler: function(){
								var comment = commentPanel.commentGrid.getSelection()[0];
								var commentId = comment.data.commentId;
								var componentId= commentPanel.componentId;
								Ext.Msg.show({
									title: 'Delete Commment?',
									message: 'Are you sure you want to delete the comment?',
									buttons: Ext.Msg.YESNO,
									icon: Ext.Msg.QUESTION,
									fn: function(btn) {
										if (btn === 'yes') {
											Ext.Ajax.request({
												url: 'api/v1/resource/components/' + componentId + '/comments/' + commentId,
												method: 'DELETE',
												success: function(response, opts) {
													// Check For Errors
													if (response.responseText.indexOf('errors') !== -1) {
														// Provide Error Notification
														Ext.toast('An Entry Failed To Delete', 'Error');
														// Provide Log Information
														console.log(response);
													}
													commentPanel.commentGrid.getStore().reload();
													commentPanel.commentGrid.down('form').reset();
												}
											});
										}
									}
								});
							}
						}
					]
				}
			]
		});
		var actionEdit = function(record) {		
			commentPanel.commentGrid.queryById('form').loadRecord(record);
		};
		commentPanel.add(commentPanel.commentGrid);
	},
	loadData: function(evaluationId, componentId, data, opts, callback) {
        
		var commentPanel = this;
		commentPanel.componentId = componentId;
		commentPanel.commentGrid.componentId = componentId;

		commentPanel.commentGrid.getStore().load({
			url: 'api/v1/resource/components/' + componentId + '/comments'
		});
		
		if (callback) {
			callback();
		}
	}
});

