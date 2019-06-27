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
/* global Ext, CoreUtil */

Ext.define('OSF.component.EntryChangeRequestWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.EntryChangeRequestWindow',
	requires: [
		'OSF.common.ValidHtmlEditor',
		'OSF.customSubmission.SubmissionFormFullControl'
	],
	title: 'Change Requests',
	modal: true,
	maximizable: true,
	width: '80%',
	height: '80%',
	layout: {
		type: 'hbox',
		align: 'stretch'
	},	
	adminMode: false,
	dockedItems: [
		{
			xtype: 'toolbar',
			itemId: 'tools',
			dock: 'bottom',
			items: [
				{
					xtype: 'tbfill'
				}, 
				{
					text: 'Close',
					iconCls: 'fa fa-lg fa-close icon-button-color-warning',
					handler: function(){
						this.up('window').hide();
					}
				}
			]
		}						
	],
	initComponent: function () {
		this.callParent();
		
		var changeRequestWindow = this;
		
		var adminMode = changeRequestWindow.adminMode;
		
		var viewTemplate = new Ext.XTemplate();		
		Ext.Ajax.request({
			url: 'Router.action?page=shared/entrySimpleViewTemplate.jsp',
			success: function(response, opts){
				viewTemplate.set(response.responseText, true);
			}
		});

		var actionApproveChangeRequest = function() {
			Ext.create('Ext.window.Window', {
				title: 'Change Request Comments: ',
				iconCls: 'fa fa-lg fa-user',
				width: '50%',
				height: 380,
				y: 200,
				modal: true,
				layout: 'fit',
				items: [
					{
						xtype: 'form',
						itemId: 'changeRequestCommentForm',
						bodyStyle: 'padding: 10px',
						items: [ 
							{
								xtype	  : 'checkboxfield',
								boxLabel  : 'Send Email to Notify Vendor of Approval',
								name      : 'SendEmail',
								inputValue: '1',
								itemId    : 'sendEmailToVendorBool'
							},
							{
								xtype: 'osf-common-validhtmleditor',
								itemId: 'searchComment',
								fieldLabel: 'Comments <span class="field-required">',
								labelAlign: 'top',
								name: 'Comment name',
								width: '100%',
								displayField: 'Comment displayfield',
								store: {
									autoLoad: true
								}
							},
							{
								xtype: 'hidden',
								itemId: 'searchCommentId',
								name: 'commentId'
							}
						],
						dockedItems: [
							{
								xtype: 'toolbar',
								dock: 'bottom',
								items: [
									{
										text: 'Approve Change',
										formBind: true,
										iconCls: 'fa fa-lg fa-check-square-o icon-button-color-save',
										handler: function(){

											// Get Form
											var form = this.up('form');
											var comment = form.queryById('searchComment').value;
											if (comment == null){
												comment = "";
											}

											var data = {
												commentId: form.queryById('searchCommentId').value,
												commentType: "ADMIN",
												comment: comment,
												willSendEmail: form.queryById('sendEmailToVendorBool').value 
											};

											var changeRequestComponentId = changeRequestWindow.changeGrid.getSelection()[0].get('componentId');

											changeRequestWindow.setLoading('Approving Change...');
											Ext.Ajax.request({
												url: 'api/v1/resource/components/' + changeRequestComponentId + '/mergechangerequest',
												method: 'PUT',
												callback: function(){
													changeRequestWindow.setLoading(false);
												},
												success: function(response, opts) {
													if(data.comment != '' || data.willSendEmail != false){
														Ext.Ajax.request({
															url: 'api/v1/resource/components/' + changeRequestWindow.currentComponentId + '/comments',
															method: 'POST',
															jsonData: data,
															success: function(response, opts){
																
																if (response.responseText.indexOf('errors') !== -1) {
																// Provide Error Notification
																	Ext.toast({
																		title: 'Validation Error. The Server could not process the comment request. ',
																		html: 'Try changing the comment field. The comment field cannot be empty and must have a size smaller than 4096.',
																		width: 550,
																		autoCloseDelay: 10000
																	});
																}

															},
															failure: function(){
																Ext.toast({
																	title: 'Validation Error. The server could not process the request.',
																	html: 'Try changing the comment field. The comment field cannot be empty and must have a size smaller than 4096.',
																	width: 500,
																	autoCloseDelay: 10000
																});
															}
														});	
													}
													changeRequestWindow.changeGrid.getStore().reload();
													changeRequestWindow.loadCurrentView();								
													if (changeRequestWindow.successHandler) {
														changeRequestWindow.successHandler();
													}
												}
											});	

											form.reset();
											this.up('window').close();
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Cancel',
										iconCls: 'fa fa-lg fa-close icon-button-color-warning',
										handler: function(){
											this.up('window').close();
										}
									}
								]
							}
						]
					}
				]
			}).show();
		}

		changeRequestWindow.submissionPanel = Ext.create('OSF.component.SubmissionPanel', {											
			formWarningMessage: changeRequestWindow.changeRequestWarning,
			submitForReviewUrl: function (componentId){
				return 'api/v1/resource/componentsubmissions/' + componentId+ '/submitchangerequest';
			},
			cancelSubmissionHandlerYes: function(){
				changeRequestWindow.submissionWindow.completeClose=true;
				changeRequestWindow.submissionWindow.close();
				changeRequestWindow.submissionWindow.completeClose=false;
				if (changeRequestWindow.successHandler) {
					changeRequestWindow.successHandler();
				}
			},
			cancelSubmissionHandlerNo: function(){
				changeRequestWindow.submissionWindow.completeClose=true;
				changeRequestWindow.submissionWindow.close();
				changeRequestWindow.submissionWindow.completeClose=false;
				if (changeRequestWindow.successHandler) {
					changeRequestWindow.successHandler();
				}
			},				
			handleSubmissionSuccess: function(response, opts) {
				changeRequestWindow.submissionWindow.completeClose=true;
				changeRequestWindow.submissionWindow.close();
				changeRequestWindow.submissionWindow.completeClose=false;
				if (changeRequestWindow.changeGrid.getStore().getProxy().url) {
					changeRequestWindow.changeGrid.getStore().reload();
				}
				if (changeRequestWindow.successHandler) {
					changeRequestWindow.successHandler();
				}					
			}
		});
		
		changeRequestWindow.submissionWindow = Ext.create('Ext.window.Window', {				
			title: 'Change Request Form',
			iconCls: 'fa fa-file-text-o',
			layout: 'fit',
			modal: true,
			width: '90%',
			height: '90%',					
			maximizable: true,			
			completeClose: false,
			items: [
				changeRequestWindow.submissionPanel
			],
			listeners: {
				beforeclose: function(panel, opts) {
					if (!(panel.completeClose)){
						changeRequestWindow.submissionPanel.cancelSubmissionHandler(true);
					}
					return panel.completeClose;
				}
			}
		});

		changeRequestWindow.editChangeRequest = function(changeRequestId, record, editCallback) {
			changeRequestWindow.changeRequestId = changeRequestId;
			
			if (!adminMode) {
				//open form
				changeRequestWindow.submissionWindow.show();			
				changeRequestWindow.submissionPanel.editSubmission(changeRequestId);
				if (editCallback) {
					editCallback();
				}
			} else {
				changeRequestWindow.adminEditHandler(record, changeRequestWindow);
			}
		};
		
		changeRequestWindow.newChangeRequest = function(currentComponentId, callback, external) {
			Ext.Ajax.request({
				url: 'api/v1/resource/components/' + currentComponentId + '/changerequest',
				method: 'POST',
				callback: function () {
					changeRequestWindow.setLoading(false);
					if (callback) {
						callback();
					}
				},
				success: function (response, opts) {
					if (!external){
						changeRequestWindow.changeGrid.getStore().reload();
					}

					var data = Ext.decode(response.responseText);										
					changeRequestWindow.editChangeRequest(data.componentId);
					if (changeRequestWindow.successHandler) {
						changeRequestWindow.successHandler();
					}
				}
			});
		};
		
		changeRequestWindow.deleteChangeRequest = function(changeRequestId, external) {
			
			Ext.Msg.show({
				title: 'Delete Change Request?',
				message: 'Are you sure you want to delete selected change request? ',
				iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
				buttons: Ext.Msg.YESNO,
				icon: Ext.Msg.QUESTION,
				fn: function(btn) {
					if (btn === 'yes') {
						changeRequestWindow.setLoading('Removing change request...');
						Ext.Ajax.request({
							url: 'api/v1/resource/components/' + changeRequestId + '/cascade',
							method: 'DELETE',
							callback: function() {
								changeRequestWindow.setLoading(false);
							},
							success: function(response, opts) {	
								if (!external) {
									changeRequestWindow.changeGrid.getStore().reload();
									changeRequestWindow.changeViewPanel.update(undefined);
								}
								
								if (changeRequestWindow.successHandler) {
									changeRequestWindow.successHandler();
								}
							}
						});
					} 
				}
			});			
		};
				
		changeRequestWindow.changeGrid = Ext.create('Ext.grid.Panel', {
			region: 'west',
			split: true,
			border: true,
			columnLines: true,
			width: '33%',
			store: Ext.create('Ext.data.Store', {
				autoLoad: false,				
				proxy: {
					url: '',
					type: 'ajax'
				}, 
				fields: [
					{
						name: 'updateDts',
						type:	'date',
						dateFormat: 'c'
					}					
				],
				listeners: {
					load: function(store, records) {
						var grid = changeRequestWindow.changeGrid;
						var tools = grid.getComponent('tools');
						
						if (!adminMode) {
							if (records.length > 0) {
								tools.getComponent('newBtn').setHidden(true);
							} else {
								tools.getComponent('newBtn').setHidden(false);
							}
						}
					}
				}
			}),
			columns: [
				{ text: 'Name', dataIndex: 'name', width: 150 },
				{ text: 'Approval Status', align: 'center', dataIndex: 'approvalState', width: 150, tooltip: 'Changes are removed upon Approval',
					renderer: function(value, metaData){
						var text = value;
						if (value === 'A') {
							text = 'Approved';
							metaData.tdCls = 'alert-success';
						} else if (value === 'P') {
							text = 'Pending';
							metaData.tdCls = 'alert-warning';
						} else if (value === 'N') {
							text = 'Not Submitted';
						}
						return text;
					}
				},
				{ text: 'Update Date', dataIndex: 'updateDts', flex: 1, xtype: 'datecolumn', format:'m/d/y H:i:s' },
				{ text: 'Update User', dataIndex: 'updateUser', width: 200, hidden: true },
				{ text: 'Approval Email', dataIndex: 'notifyOfApprovalEmail', width: 200, hidden: true }
			],
			listeners: {
				selectionchange: function(selectionModel, records){
					var grid = changeRequestWindow.changeGrid;
					var tools = grid.getComponent('tools');
					if (records.length === 1) {
						tools.getComponent('editBtn').setDisabled(false);
						tools.getComponent('unsubmitBtn').setDisabled(false);
						tools.getComponent('removeBtn').setDisabled(false);
						tools.getComponent('tool-approveBtn').setDisabled(false);
						tools.queryById('actionBtn').setDisabled(false);
						tools.queryById('messageBtn').setDisabled(false);
						tools.queryById('viewSubmissionBtn').setDisabled(false);						
					} else {
						tools.getComponent('editBtn').setDisabled(true);
						tools.getComponent('unsubmitBtn').setDisabled(true);
						tools.getComponent('removeBtn').setDisabled(true);
						tools.getComponent('tool-approveBtn').setDisabled(true);
						tools.queryById('actionBtn').setDisabled(false);
						tools.queryById('messageBtn').setDisabled(true);
						tools.queryById('viewSubmissionBtn').setDisabled(false);
					}

					//load preview
					if (records[0]){
						var componentId = records[0].get('componentId');
						
						changeRequestWindow.changeViewPanel.setLoading(true);
						Ext.Ajax.request({
							url: 'api/v1/resource/components/' + componentId + '/detail',
							callback: function(){
								changeRequestWindow.changeViewPanel.setLoading(false);
							},
							success: function(response, opts) {
								var data = Ext.decode(response.responseText);								
								var root = data.componentTypeNestedModel;
								CoreUtil.traverseNestedModel(root, [], data);
								changeRequestWindow.changeViewPanel.update(data);								
							}
						});
					} else {
						changeRequestWindow.changeViewPanel.update(undefined);						
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
							text: 'Approve Change',
							itemId: 'tool-approveBtn',
							iconCls: 'fa fa-lg fa-check-square-o icon-button-color-save',
							disabled: true,
							hidden: true,
							handler: function(){
								actionApproveChangeRequest();
							}
						},						
						{
							text: 'New Change Request',
							itemId: 'newBtn',
							iconCls: 'fa fa-lg fa-plus icon-button-color-save',						
							handler: function(){								
								changeRequestWindow.setLoading('Creating a new change request...');
								changeRequestWindow.newChangeRequest(changeRequestWindow.currentComponentId);
							}
						},						
						{
							text: 'Edit',
							itemId: 'editBtn',
							iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
							disabled: true,
							handler: function(){ 
								var changeRequestComponentId = this.up('grid').getSelectionModel().getSelection()[0].get('componentId');
								changeRequestWindow.editChangeRequest(changeRequestComponentId, this.up('grid').getSelectionModel().getSelection()[0]);								
							}
						}, 
						{
							text: 'Action',
							itemId: 'actionBtn',
							hidden: true,
							disabled: true,
							menu: [
								{
									text: 'Message',
									itemId: 'messageBtn',
									iconCls: 'fa fa-lg fa-envelope-o',
									disabled: true,
									hidden: true,
									handler: function(){
										var emails = changeRequestWindow.changeGrid.getSelection()[0].get('ownerEmail');
										var messageWindow = Ext.create('OSF.component.MessageWindow', {					
											closeAction: 'destroy',
											alwaysOnTop: true,
											initialToUsers: emails
										}).show();
									}
								},								
								{
									text: 'View Submission form',
									itemId: 'viewSubmissionBtn',
									disabled: true,
									iconCls: 'fa fa-eye icon-button-color-view',									
									handler: function() {
										var record = changeRequestWindow.changeGrid.getSelection()[0];
										
										var previewWin = Ext.create('Ext.window.Window', {
											title: 'Preview',
											layout: 'fit',
											modal: true,
											closeAction: 'destroy',
											width: '80%',
											height: '80%',
											maximizable: true,
											dockedItems: [
												{
													xtype: 'panel',
													dock: 'top',
													html: '<div class="submission-form-preview alert-warning">Preview Mode - (Changes will NOT be saved)</div>'
												}
											],
											items: [
												{
													xtype: 'osf-customSubmission-SubmissionformFullControl',
													itemId: 'form',
													showCustomButton: true,
													previewMode: true,
													hideSave: true,
													customButtonHandler: function() {
														previewWin.close();
													}								
												}
											]
										});
										previewWin.show();

										previewWin.setLoading('Loading Submission Form data...');
										Ext.Ajax.request({
											url: 'api/v1/resource/components/' + record.get('componentId') + '/usersubmission',
											callback: function() {
												previewWin.setLoading(false);
											},
											success: function(response, opts) {							
												var userSubmission = Ext.decode(response.responseText);

												previewWin.setLoading('Loading Submission Form template...');
												Ext.Ajax.request({
													url: 'api/v1/resource/submissiontemplates/' + userSubmission.templateId,
													callback: function() {
														previewWin.setLoading(false);
													},
													success: function(responseTemplate, opts) {
														var template = Ext.decode(responseTemplate.responseText);

														previewWin.queryById('form').load(template, record.get('componentType'), userSubmission);
													}
												});							
											}
										});										
									}
								}
							]
						},
						{
							text: 'Unsubmit',
							itemId: 'unsubmitBtn',
							iconCls: 'fa fa-lg fa-close icon-button-color-warning',
							disabled: true,
							handler: function(){ 
								var changesGrid = this.up('grid');
								var changeRequestComponentId = this.up('grid').getSelectionModel().getSelection()[0].get('componentId');
								var name = this.up('grid').getSelectionModel().getSelection()[0].get('name');
								
								Ext.Msg.show({
									title:'Unsubmit?',
									message: 'Are sure you want to unsubmit change request <b>' + name + '</b>?',
									buttons: Ext.Msg.YESNO,
									icon: Ext.Msg.QUESTION,
									fn: function(btn) {
										if (btn === 'yes') {
											changeRequestWindow.setLoading('Unsubmitting...');
											Ext.Ajax.request({
												url: 'api/v1/resource/componentsubmissions/' + changeRequestComponentId + '/unsubmitchangerequest',
												method: 'PUT',
												callback: function(){
													changeRequestWindow.setLoading(false);
												},
												success: function(response, opts) {
													changesGrid.getStore().reload();
												}
											});	
										} 
									}
								});
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
								var changesGrid = this.up('grid');
								var changeRequestComponentId = this.up('grid').getSelectionModel().getSelection()[0].get('componentId');
								
								changeRequestWindow.deleteChangeRequest(changeRequestComponentId);
							}
						}
					]
				}
			]
		});
						
		changeRequestWindow.changeViewPanel = Ext.create('Ext.panel.Panel', {
			region: 'east',
			width: '33%',
			title: 'Selected Request',
			autoScroll: true,
			scrollable: true,
			border: true,
			split: true,
			bodyStyle: 'padding: 10px;',
			tpl: viewTemplate							
		});						
		
		changeRequestWindow.currentViewPanel = Ext.create('Ext.panel.Panel', {
			region: 'center',
			flex: 1,
			title: 'Current Entry',
			header: {
				cls: 'accent-background'
			},
			autoScroll: true,
			border: true,
			split: true,
			bodyStyle: 'padding: 10px;',
			tpl: viewTemplate
		});		
		
		changeRequestWindow.add(changeRequestWindow.changeGrid);
		changeRequestWindow.add(changeRequestWindow.changeViewPanel);
		changeRequestWindow.add(changeRequestWindow.currentViewPanel);
		
		if (adminMode) {
			var grid = changeRequestWindow.changeGrid;
			var tools = grid.getComponent('tools');
			tools.getComponent('newBtn').setHidden(true);	
			tools.getComponent('unsubmitBtn').setHidden(true);	
			tools.getComponent('tool-approveBtn').setHidden(false);
			tools.queryById('actionBtn').setHidden(false);
			tools.queryById('messageBtn').setHidden(false);
		} 
		
	},
	
	loadComponent: function(componentId, name) {
		var changeRequestWindow = this;
		
		changeRequestWindow.currentComponentId = componentId;
		changeRequestWindow.setTitle('<i class="fa fa-lg fa-edit"></i>' + '<span class="shift-window-text-right">Change Requests - </span>' + name);
		
		//load grid		
		changeRequestWindow.changeGrid.getStore().load({
			url:'api/v1/resource/components/' + componentId + '/pendingchanges'			
		});
		
		changeRequestWindow.loadCurrentView();
	},
	
	loadCurrentView: function(){
		var changeRequestWindow = this;
		//load current view
		changeRequestWindow.currentViewPanel.setLoading(true);		
		Ext.Ajax.request({
			url: 'api/v1/resource/components/' + changeRequestWindow.currentComponentId + '/detail',
			callback: function(){
				changeRequestWindow.currentViewPanel.setLoading(false);		
			}, 
			success: function(response, opts) {
				var data = Ext.decode(response.responseText);				
				var root = data.componentTypeNestedModel;
				CoreUtil.traverseNestedModel(root, [], data);
				changeRequestWindow.currentViewPanel.update(data);
			}
		});		
	}
	
});
