<%-- 
Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

    Document   : submission
    Created on : Dec 21, 2015, 1:00:29 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../client/layout/usertoolslayout.jsp">
    <stripes:layout-component name="contents">

	<script src="scripts/component/submissionPanel.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/component/entryChangeRequestWindow.js?v=${appVersion}" type="text/javascript"></script>
		
        <script type="text/javascript">
			/* global Ext, CoreUtil, CoreService */

			Ext.onReady(function () {
				
				CoreService.brandingservice.getCurrentBranding().then(function(response, opts){
					var branding = Ext.decode(response.responseText);
					
					var submissionPanel = Ext.create('OSF.component.SubmissionPanel', {							
						id: 'submissionPanel',
						userInputWarning: branding.userInputWarning,
						formWarningMessage: branding.submissionFormWarning,
						cancelSubmissionHandlerYes: function(){
							Ext.getCmp('submissionWindow').completeClose=true;
							Ext.getCmp('submissionWindow').close();
							Ext.getCmp('submissionWindow').completeClose=false;
							actionRefreshSubmission();
						},
						cancelSubmissionHandlerNo: function(){
							Ext.getCmp('submissionWindow').completeClose=true;
							Ext.getCmp('submissionWindow').close();
							Ext.getCmp('submissionWindow').completeClose=false;
							actionRefreshSubmission();
						},
						handleSubmissionSuccess: function(response, opts) {
							Ext.getCmp('submissionWindow').completeClose=true;
							Ext.getCmp('submissionWindow').close();
							Ext.getCmp('submissionWindow').completeClose=false;
							actionRefreshSubmission();
						}					
					});

					var submissionWindow = Ext.create('Ext.window.Window', {
						id: 'submissionWindow',
						title: 'Submission Form',
						iconCls: 'fa fa-file-text-o',
						layout: 'fit',
						modal: true,
						width: '90%',
						height: '90%',					
						maximizable: true,
						completeClose: false,
						items: [
							submissionPanel
						],
						listeners: {
							beforeclose: function(panel, opts) {
								if (!(Ext.getCmp('submissionWindow').completeClose)){
									submissionPanel.cancelSubmissionHandler(true);
								}
								return panel.completeClose;
							}
						}
					});		
					
					var changeRequestWindow = Ext.create('OSF.component.EntryChangeRequestWindow', {
						id: 'changeRequestWindow',
						changeRequestWarning: branding.changeRequestWarning,
						successHandler: function() {
							Ext.getCmp('submissionGrid').setLoading(false);
							Ext.getCmp('submissionGrid').getStore().reload();
						}
					});										
				});		

				var previewContents = Ext.create('OSF.ux.IFrame', {
					src: ''
				});								
				var previewComponentWin = Ext.create('Ext.window.Window', {
					width: '70%',
					height: '80%',
					maximizable: true,
					title: 'Preview',
					modal: true,
					layout: 'fit',
					items: [
						previewContents
					],
					tools: [
						{
							type: 'up',
							tooltip: 'popout preview',
							handler: function(){
								window.open('view.jsp?fullPage=true&id=' + Ext.getCmp('submissionGrid').getSelection()[0].get('componentId'), "Preview");
							}
						}
					], 
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [
								{
									text: 'Previous',
									id: 'previewWinTools-previousBtn',
									iconCls: 'fa fa-arrow-left',									
									handler: function() {
										actionPreviewNextRecord(false);
									}									
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Close',
									iconCls: 'fa fa-close',
									handler: function() {
										this.up('window').hide();
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Next',
									id: 'previewWinTools-nextBtn',
									iconCls: 'fa fa-arrow-right',
									iconAlign: 'right',
									handler: function() {
										actionPreviewNextRecord(true);
									}									
								}
							]
						}
					]
				});				
				var actionPreviewNextRecord = function(next) {
					if (next) {
						Ext.getCmp('submissionGrid').getSelectionModel().selectNext();						
					} else {
						Ext.getCmp('submissionGrid').getSelectionModel().selectPrevious();						
					}
					actionPreviewComponent();					
				};				
				var previewCheckButtons = function() {	
					if (Ext.getCmp('submissionGrid').getSelectionModel().hasPrevious()) {
						Ext.getCmp('previewWinTools-previousBtn').setDisabled(false);
					} else {
						Ext.getCmp('previewWinTools-previousBtn').setDisabled(true);
					}
					
					if (Ext.getCmp('submissionGrid').getSelectionModel().hasNext()) {
						Ext.getCmp('previewWinTools-nextBtn').setDisabled(false);
					} else {
						Ext.getCmp('previewWinTools-nextBtn').setDisabled(true);
					}					
				};				
				
				var submissionGrid = Ext.create('Ext.grid.Panel', {	
					title: 'Submissions <i class="fa fa-question-circle"  data-qtip="Manage your entry submissions here."></i>',
					id: 'submissionGrid',
					columnLines: true,
					store: {
						sorters: [{
							property: 'name',
							direction: 'ASC'
						}],						
						autoLoad: true,
						fields: [
							{
								name: 'submittedDts',
								type:	'date',
								dateFormat: 'c'
							}
						],
						proxy: {
							type: 'ajax',
							url: '../api/v1/resource/componentsubmissions'
						}
					},
					columns: [
						{ text: 'name', dataIndex: 'name', flex: 1, minWidth: 200 },
						{ text: 'Description', dataIndex: 'description', flex: 2, minWidth: 250,
						 renderer: function(value){
							return Ext.util.Format.stripTags(value);
						}},
						{ text: 'Status', align: 'center', dataIndex: 'approvalState', width: 200,
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
						{ text: 'Submission Date', dataIndex: 'submittedDts', width: 200, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Approval Email', dataIndex: 'notifyOfApprovalEmail', width: 200 },
						{ text: 'Pending Change', align: 'center', dataIndex: 'statusOfPendingChange', width: 150 }	
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
									iconCls: 'fa fa-2x fa-refresh',
									handler: function () {
										actionRefreshSubmission();
									}
								}, 
								{
									xtype: 'tbseparator'
								},
								{
									text: 'New Submission',									
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-plus',									
									handler: function () {
										Ext.getCmp('submissionWindow').show();
										Ext.getCmp('submissionPanel').resetSubmission();
									}
								},
								{
									text: 'Edit',
									itemId: 'tbEdit',
									scale: 'medium',	
									disabled: true,
									iconCls: 'fa fa-2x fa-edit',
									handler: function () {
										var componentId = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0].get('componentId');
										Ext.getCmp('submissionWindow').show();
										Ext.getCmp('submissionPanel').editSubmission(componentId);
									}
								},
								{
									text: 'Request Change',
									itemId: 'tbSubmitChange',
									tooltip: 'Create or edit a change request for an approved entry.',
									hidden: true,									
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-edit',
									handler: function () {
										var record = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0];										
										var componentId = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0].get('componentId');
										//var name = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0].get('name');
										
										if (record.get('statusOfPendingChange')) {		
											Ext.getCmp('submissionGrid').setLoading(true);
											Ext.getCmp('changeRequestWindow').editChangeRequest(record.get('pendingChangeComponentId'), function(){
												Ext.getCmp('submissionGrid').setLoading(false);
											});
										} else {
											Ext.getCmp('submissionGrid').setLoading("Creating Change Request...");
											Ext.getCmp('changeRequestWindow').newChangeRequest(componentId, function(){
												Ext.getCmp('submissionGrid').setLoading(false);
											}, true);
										}
									}
								},								
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Options',
									itemId: 'options',
									scale: 'medium',	
									disabled: true,									
									menu: {
										items: [
											{
												text: 'Preview',
												itemId: 'tbPreview',
												iconCls: 'fa fa-binoculars',
												handler: function () {
													actionPreviewComponent();
												}
											},
											{
												xtype: 'menuseparator'
											},
											{
												text: 'Copy',
												itemId: 'tbCopy',
												iconCls: 'fa fa-copy',
												handler: function () {
													var componentId = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0].get('componentId');

													Ext.getCmp('submissionGrid').setLoading('Copying submission...');
													Ext.Ajax.request({
														url: '../api/v1/resource/componentsubmissions/' + componentId + '/copy',
														method: 'POST',
														callback: function(){
															Ext.getCmp('submissionGrid').setLoading(false);
														},
														success: function(response, opts) {
															actionRefreshSubmission();
														}
													});
												}
											},
											{
												xtype: 'menuseparator'
											},											
											{
												text: 'Toggle Notify',
												itemId: 'tbNotify',
												iconCls: 'fa fa-envelope',
												handler: function () {
													if (currentUser && currentUser.email) {
														var componentId = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0].get('componentId');
														var currentEmail = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0].get('notifyOfApprovalEmail');

														var newEmail = currentUser.email;
														if (currentEmail) {
															newEmail = null;												
														}
														Ext.getCmp('submissionGrid').setLoading('Updating Notification...');
														Ext.Ajax.request({
															url: '../api/v1/resource/componentsubmissions/' + componentId + '/setNotifyMe',
															method: 'PUT',
															rawData: newEmail,
															callback: function(){
																Ext.getCmp('submissionGrid').setLoading(false);
															},
															success: function(response, opts) {
																actionRefreshSubmission();
															}
														});											

													} else {
														Ext.Msg.show({
															title:'Missing Email',
															message: 'Please set an email on your profile first.',
															buttons: Ext.Msg.OK,
															icon: Ext.Msg.ERROR,
															fn: function(btn) {													 
															}
														});											
													}
												}
											}											
										]
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Request Removal',
									itemId: 'tbUnapprove',
									tooltip: 'This will send a request to the administation asking for the selected record to be unapproved.',
									hidden: true,									
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-comment',
									handler: function () {
										var componentId = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0].get('componentId');
										var name = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0].get('name');
										
										var feedbackWin = Ext.create('OSF.component.FeedbackWindow', {
											closeAction: 'destroy',
											title: 'Request Removal',
											extraDescription: 'Entry Name: ' + name,
											hideType: 'Request entry to be Unapproved',
											hideSummary: Ext.String.ellipsis(name, 50),
											labelForDescription: 'Reason',
											successHandler: function() {
												Ext.getCmp('submissionGrid').getStore().reload();
											}
										});
										feedbackWin.show();
									}
								},
								{
									text: 'Unsubmit',
									itemId: 'tbUnsubmit',
									hidden: true,									
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-close',
									handler: function () {
										var componentId = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0].get('componentId');
										var name = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0].get('name');
										Ext.Msg.show({
											title:'Unsubmit?',
											message: 'Are sure you want to unsubmit submission <b>' + name + '</b>?',
											buttons: Ext.Msg.YESNO,
											icon: Ext.Msg.QUESTION,
											fn: function(btn) {
												if (btn === 'yes') {
													Ext.getCmp('submissionGrid').setLoading('Unsubmitting...');
													Ext.Ajax.request({
														url: '../api/v1/resource/componentsubmissions/' + componentId + '/unsubmit',
														method: 'PUT',
														callback: function(){
															Ext.getCmp('submissionGrid').setLoading(false);
														},
														success: function(response, opts) {
															actionRefreshSubmission();
														}
													});	
												} 
											}
										});										
									}
								},
								{
									text: 'Delete',
									itemId: 'tbDelete',
									hidden: true,
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-trash',
									handler: function () {
										var componentId = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0].get('componentId');
										var name = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0].get('name');
										Ext.Msg.show({
											title:'Delete?',
											message: 'Are sure you want to delete: ' + name + '?',
											buttons: Ext.Msg.YESNO,
											icon: Ext.Msg.QUESTION,
											fn: function(btn) {
												if (btn === 'yes') {
													Ext.getCmp('submissionGrid').setLoading('Removing...');
													Ext.Ajax.request({
														url: '../api/v1/resource/componentsubmissions/' + componentId + '/inactivate',
														method: 'PUT',
														callback: function(){
															Ext.getCmp('submissionGrid').setLoading(false);
														},
														success: function(response, opts) {
															actionRefreshSubmission();
														}
													});	
												} 
											}
										});
									}
								},
								{
									text: 'Remove Change Request',
									itemId: 'tbRemoveChangeRequest',
									hidden: true,
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-trash',
									handler: function () {
										var record = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0];
										//var name = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0].get('name');
										
										Ext.getCmp('changeRequestWindow').deleteChangeRequest(record.get('pendingChangeComponentId'), true);
									}									
								}
							]
						}
					],
					listeners: {
						itemdblclick: function(grid, record, item, index, e, opts){
							
						},
						selectionchange: function(selectionModel, selected, opts){
							var tools = Ext.getCmp('submissionGrid').getComponent('tools');
							
							if (selected.length > 0) {	
								var record = selected[0];	
								
								tools.getComponent('tbEdit').setDisabled(true);
								tools.getComponent('options').setDisabled(false);

								
								//hiddens
								tools.getComponent('tbSubmitChange').setHidden(true);
								tools.getComponent('tbRemoveChangeRequest').setHidden(true);
								
								tools.getComponent('tbUnsubmit').setHidden(true);
								tools.getComponent('tbDelete').setHidden(true);
								tools.getComponent('tbUnapprove').setHidden(true);
								
								if (record.get('approvalState') === 'A'){									
									tools.getComponent('tbSubmitChange').setHidden(false);
									tools.getComponent('tbUnapprove').setHidden(false);
									
									if (record.get('statusOfPendingChange')) {
										tools.getComponent('tbSubmitChange').setText('Edit Change');
									} else {
										tools.getComponent('tbSubmitChange').setText('Request Change');
									}
								}
								if (record.get('approvalState') === 'P'){
									//tools.getComponent('tbUnsubmit').setHidden(false);
									tools.getComponent('tbEdit').setDisabled(false);
								}
								if (record.get('approvalState') === 'N'){
									tools.getComponent('tbDelete').setHidden(false);
									tools.getComponent('tbEdit').setDisabled(false);
								}
								
								if (record.get('statusOfPendingChange')) {
									tools.getComponent('tbRemoveChangeRequest').setHidden(false);
								}
								
							} else {
								tools.getComponent('tbEdit').setDisabled(true);
								tools.getComponent('options').setDisabled(true);							
								
								//hiddens
								tools.getComponent('tbSubmitChange').setHidden(true);
								tools.getComponent('tbRemoveChangeRequest').setHidden(true);
								tools.getComponent('tbUnsubmit').setHidden(true);
								tools.getComponent('tbDelete').setHidden(true);
								tools.getComponent('tbUnapprove').setHidden(true);
							}							
						}
					}
				});				
				
				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [
						submissionGrid
					]
				});
				
				var actionRefreshSubmission = function(){
					Ext.getCmp('submissionGrid').getStore().load();
				};
				
				var actionPreviewComponent = function(){
					previewComponentWin.show();
					var componentId = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0].get('componentId');
					previewContents.load('view.jsp?id=' + componentId +'&fullPage=true');
					previewCheckButtons();					
				};
				
				var currentUser;
				CoreService.usersevice.getCurrentUser().then(function(response, opts){
					currentUser = Ext.decode(response.responseText);
				});
				
			});

        </script>

    </stripes:layout-component>
</stripes:layout-render>
