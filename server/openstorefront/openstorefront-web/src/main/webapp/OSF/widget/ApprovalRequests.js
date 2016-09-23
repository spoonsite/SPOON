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
				autoLoad: true,
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
				],
				proxy: {
					type: 'ajax',
					url: 'api/v1/resource/components/filterable',					
					reader: {
						type: 'json',
						rootProperty: 'components',
						totalProperty: 'totalNumber'
					}
				},
				listeners: {											
					beforeLoad: function(store, operation, eOpts){
						store.getProxy().extraParams = {							
							approvalState: 'ALL'
						};
					},					
					load: function(store, records, successful, opts) {
						//filter pending and pending changes
						store.filterBy(function(record){
							if (record.get('approvalState') === 'P') {
								return true;
							} else {
								if (record.get('numberOfPendingChanges') && record.get('numberOfPendingChanges') > 0) {
									if (record.get('statusOfPendingChange') === 'Pending') {
										return true;
									}
								}
							}
							return false;
						});						
					}
				}				
			},
			columns: [
				{ text: 'Request Type', dataIndex: 'requestType', width: 150 },
				{ text: 'Name', dataIndex: 'name', flex: 1, minWidth: 175 },
				{ text: 'User', dataIndex: 'updateUser', width: 175 },
				{ text: 'Submission Date', dataIndex: 'submittedDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s' }
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
							itemId: 'preview',
							iconCls: 'fa fa-binoculars',
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
													iconCls: 'fa fa-2x fa-check',
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
		
	},	
	refresh: function() {
		var approvalPanel = this;
		approvalPanel.grid.getStore().reload();
	}
	
});