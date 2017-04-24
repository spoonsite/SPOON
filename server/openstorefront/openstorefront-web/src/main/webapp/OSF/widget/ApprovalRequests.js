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

/* global Ext */

Ext.define('OSF.widget.ApprovalRequests', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.ApprovalRequests',

	layout: 'fit',
	
	initComponent: function () {
		this.callParent();
		
		var approvalPanel = this;
		
		approvalPanel.grid = Ext.create('Ext.grid.Panel', {
			columnLines: true,
			store: {
				autoLoad: false,
				fields: [
					{
						name: 'submittedDts',
						type: 'date',
						dateFormat: 'c',
						mapping: function(data) {
							if (data.component.pendingChangeSubmitDts) {
								return data.component.pendingChangeSubmitDts;
							} else {
								return data.component.submittedDts;
							}
						}
					},
					{
						name: 'name',
						mapping: function(data) {
							return data.component.name;
						}
					},
					{
						name: 'approvalState',
						mapping: function(data) {
							return data.component.approvalState;
						}
					},
					{
						name: 'numberOfPendingChanges',
						mapping: function(data) {
							return data.component.numberOfPendingChanges;
						}
					},	
					{
						name: 'componentId',
						mapping: function(data) {
							return data.component.componentId;
						}
					},	
					{
						name: 'pendingChangeComponentId',
						mapping: function(data) {
							return data.component.pendingChangeComponentId;
						}
					},						
					{
						name: 'updateUser',
						mapping: function(data) {
							if (data.component.pendingChangeSubmitUser) {
								return data.component.pendingChangeSubmitUser;
							} else {
								return data.component.updateUser;
							}							
						}
					},
					{
						name: 'statusOfPendingChange',
						mapping: function(data) {
							return data.component.statusOfPendingChange;
						}
					},					
					{
						name: 'requestType',
						mapping: function(data) {
							if (data.numberOfPendingChanges && data.numberOfPendingChanges > 0) {
								return 'Change Request';
							} else {
								return 'Submission';
							}							
						}
					}					
				]
			},
			columns: [
				{ text: 'Request Type', dataIndex: 'requestType', width: 150 },
				{ text: 'Name', dataIndex: 'name', flex: 1, minWidth: 200 },
				{ text: 'User', dataIndex: 'updateUser', width: 200 },
				{ text: 'Submission Date', dataIndex: 'submittedDts', width: 180, xtype: 'datecolumn', format: 'm/d/y H:i:s' }
			],
			listeners: {
				selectionchange: function(selModel, selected, opts) {
					if (selModel.getCount() > 0) {
						approvalPanel.grid.getComponent('tools').getComponent('preview').setDisabled(false);
					} else {
						approvalPanel.grid.getComponent('tools').getComponent('preview').setDisabled(true);
					}
				}
			},
			dockedItems: [
				{
					xtype: 'toolbar',
					itemId: 'tools',
					dock: 'top',
					items: [
						{
							text: 'Preview Request',
							scale: 'medium',
							itemId: 'preview',
							width: '180px',
							iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction-view',
							disabled: true,
							handler: function() {
								var record = approvalPanel.grid.getSelectionModel().getSelection()[0];
									
								var componentId  = record.get('componentId');
								var changeRequest = false;
								if (record.get('requestType') === 'Change Request') {
									componentId  = record.get('pendingChangeComponentId');
									changeRequest = true;
								}
								
								var previewContents = Ext.create('OSF.ux.IFrame', {
									src: ''
								});								
								var previewComponentWin = Ext.create('Ext.window.Window', {
									width: '70%',
									height: '80%',
									maximizable: true,
									closeAction: 'destroy',
									title: 'Pending Approval for ' + record.get('name'),
									modal: true,
									layout: 'fit',
									items: [
										previewContents
									],								
									dockedItems: [
										{
											xtype: 'toolbar',
											dock: 'bottom',
											items: [										
												{
													text: 'Approve',
													scale: 'medium',
													iconCls: 'fa fa-2x fa-check-square-o',
													handler: function(){
														var win = this.up('window');
														var approveUrl = 'api/v1/resource/components/' + componentId + '/approve';
														if (changeRequest) {
															approveUrl = 'api/v1/resource/components/' + componentId + '/mergechangerequest';
														} 
												
														win.setLoading('Approving Entry...');
														Ext.Ajax.request({
															url: approveUrl,
															method: 'PUT',
															callback: function (){
																win.setLoading(false);
															},
															success: function(response, opts){																	
																win.close();
																approvalPanel.refresh();																	
															}
														});															
													}
												},
												{
													xtype: 'tbfill'
												},
												{
													text: 'Close',
													scale: 'medium',
													iconCls: 'fa fa-2x fa-close',
													handler: function(){
														this.up('window').close();
													}													
												}
											]
										}
									]
								});
								previewComponentWin.show();
								previewContents.load('view.jsp?id=' + componentId +'&fullPage=true');
							}
						}
					]
				}
			]
		});
		
		approvalPanel.add(approvalPanel.grid);	
		
		approvalPanel.reloadRequests = function() {
			
			approvalPanel.grid.setLoading(true);
			
			Ext.Ajax.request({
				url: 'api/v1/resource/components/filterable?approvalState=ALL',
				callback: function(){
					approvalPanel.grid.setLoading(false);
				},
				success: function(response, opts) {
					var record = Ext.decode(response.responseText);
				
					var filterResults = [];
					var pendingEntries = [];
					filterResults = record.components;
					Ext.Array.each(filterResults, function(result){
						if (result.component.approvalState === 'P') {
							pendingEntries.push(result);
						} else {
							if (result.component.numberOfPendingChanges && result.component.numberOfPendingChanges > 0) {
								if (result.component.statusOfPendingChange === 'Pending') {
									pendingEntries.push(result);
								}
							}
						}
					});
					
					approvalPanel.grid.getStore().loadRawData(pendingEntries);
				}
			});
			
		}
		approvalPanel.reloadRequests();
	},
	
	refresh: function() {
		var approvalPanel = this;
		approvalPanel.reloadRequests();
	}
	
});