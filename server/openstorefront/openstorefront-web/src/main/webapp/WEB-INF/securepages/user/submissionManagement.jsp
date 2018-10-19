<%-- 
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

    Document   : submission
    Created on : Dec 21, 2015, 1:00:29 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
		
		<stripes:layout-render name="../../../layout/userheader.jsp">		
		</stripes:layout-render>			

		<script src="scripts/component/submissionPanel.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/entryChangeRequestWindow.js?v=${appVersion}" type="text/javascript"></script>
				
        <script type="text/javascript">
			/* global Ext, CoreUtil, CoreService */
			Ext.require('OSF.customSubmission.SubmissionFormFullControl');			
			Ext.require('OSF.workplanProgress.CommentPanel');
			Ext.require('OSF.workplanProgress.ProgressView');

			Ext.onReady(function () {
				
				CoreService.brandingservice.getCurrentBranding().then(function(branding){					
					
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
						iconCls: 'fa fa-lg fa-file-text-o',
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
				
				var entryCommentsPanel = Ext.create('OSF.workplanProgress.CommentPanel', {
					region: 'east',
					width: '30%',
					animCollapse: false,
					collapsible: true,
					collapseDirection: 'left',
					titleCollapse: true,
					style: 'background: #ffffff;',
					itemId: 'entryCommentsPanel'
				});
		
				var previewComponentWin = Ext.create('Ext.window.Window', {
					id: 'previewComponentWin',
					title: 'Entry Details',					
					maximizable: true,
					modal: true,
					width: '75%',
					height: '75%',
					defaults: {
						collapsible: true,
						split: true,
						bodyPadding: 0
					},
					layout: 'border',
					items: [
						entryCommentsPanel,
						{
							xtype: 'panel',
							title: 'Preview',
							collapsible: false,
							region: 'center',
							margin: '0 0 0 0',
							width: '75%',
							height: '80%',
							title: 'Entry Info',
							iconCls: 'fa fa-lg fa-eye',
							layout: 'fit',
							itemId: 'previewPanel',
							items: [
								previewContents
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
					var selectedRecord = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0];							
					actionPreviewComponent(selectedRecord);					
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
					title: 'Submissions <i class="fa fa-lg fa-question-circle"  data-qtip="Manage your entry submissions here."></i>',
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
							},
							{
								name: 'updateDts',
								type:	'date',
								dateFormat: 'c'
							},							
							{
								name: 'submitApproveDts',
								type:	'date',
								dateFormat: 'c',
								mapping: function(data) {
									if (data.approvalState === 'A') {
										return data.approvedDts;
									} else {
										return data.submittedDts;
									}
								}
							},
							{
								name: 'fullname', 
								mapping: function(data) {
									var fullName = '';
								
									if (data.submissionOriginalComponentId) {
										fullName = '<i class="fa fa-exclamation-triangle text-warning"></i>' + data.name + ' (Incomplete Change Request)';
									} else if (data.userSubmissionId) {
										fullName = '<i class="fa fa-exclamation-triangle text-warning"></i>' + data.name + ' (Incomplete Submission)';
									} else {
										fullName = data.name;
									}
									return fullName;
								}
							}
						],
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/componentsubmissions'
						}
					},
					columns: [
						{ text: 'Name', dataIndex: 'fullname', flex: 1, minWidth: 200
						},
						{ text: 'Description', dataIndex: 'description', flex: 2, minWidth: 250,
						 renderer: function(value){
							return Ext.util.Format.stripTags(value);
						}},
						{ text: 'Type', dataIndex: 'componentType', align: 'center', width: 200,
							renderer: function(value, meta, record) {
								return record.get('componentTypeLabel');
							}
						},
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
									metaData.tdCls = 'alert-danger';
									text = 'Not Submitted';
								}
								return text;
							}
						},
						{ text: 'Submit/Approve Date', align: 'center', dataIndex: 'submitApproveDts', width: 200, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Approval Email', dataIndex: 'notifyOfApprovalEmail', width: 200, sortable: false },
						{ text: 'Pending Change', align: 'center', dataIndex: 'statusOfPendingChange', width: 150, sortable: false },
						{ text: 'Pending Change Submit Date', align: 'center', dataIndex: 'pendingChangeSubmitDts', width: 250, xtype: 'datecolumn', format:'m/d/y H:i:s', hidden: true, sortable: false },
						{ text: 'Last Update Date', align: 'center', dataIndex: 'updateDts', width: 200, xtype: 'datecolumn', format:'m/d/y H:i:s' }
					],
					dockedItems: [
						{
							xtype: 'osf.wp.progressView'
						},
						{
							dock: 'top',
							xtype: 'toolbar',
							itemId: 'tools',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										actionRefreshSubmission();
									}
								}, 
								{
									requiredPermissions: ['USER-SUBMISSIONS-CHANGEREQUEST', 'USER-SUBMISSIONS-UPDATE'],
									xtype: 'tbseparator'
								},
								{
									text: 'New Submission',
									itemId: 'newSubmission',
									scale: 'medium',
									autoEl: {
										"data-test": "newSubmissionBtn"
									},
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction',									
									requiredPermissions: ['USER-SUBMISSIONS-CREATE'],
									handler: function () {
										actionNewSubmission();
									}
								},
								{
									text: 'Edit',
									itemId: 'tbEdit',
									scale: 'medium',	
									disabled: true,
									width: '100px',
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
									requiredPermissions: ['USER-SUBMISSIONS-UPDATE'],
									handler: function () {
										var componentId = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0].get('componentId');
										var record = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0];
										
										if (record.get('userSubmissionId')) {										
											loadSubmissionForm(record.get('componentType'), record);
										} else {
											editExistingEntry(componentId);
										}
										
									}
								},
								{
									text: 'Request Change',
									itemId: 'tbSubmitChange',
									tooltip: 'Create or edit a change request for an approved entry.',
									hidden: true,									
									scale: 'medium',
									width: '180px',
									iconCls: 'fa fa-2x fa-edit icon-button-color-default icon-vertical-correction-view',
									requiredPermissions: ['USER-SUBMISSIONS-CHANGEREQUEST'],
									beforePermissionsCheckSuccess: function () {
										return false;
									},
									handler: function () {
										var record = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0];										
										var componentId = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0].get('componentId');
										
										if (record.get('pendingChangeComponentId')) {
											editChangeRequest(record.get('pendingChangeComponentId'));
										} else {									
											createChangeRequest(componentId);
										}
									}
								},								
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Comments',
									iconCls: 'fa fa-2x fa-commenting-o icon-vertical-correction-view icon-button-color-edit',
									scale: 'medium',
									itemId: 'commentsButton',
									disabled: true,
									handler: function () {
										var selectedRecord = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0];
										actionPreviewComponent(selectedRecord, true);
									}
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
												iconCls: 'fa fa-lg fa-eye icon-small-vertical-correction icon-button-color-default',
												requiredPermissions: ['USER-SUBMISSIONS-READ'],
												handler: function () {
													var selectedRecord = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0];
													actionPreviewComponent(selectedRecord, false);
												}
											},
											{
												requiredPermissions: ['USER-SUBMISSIONS-CREATE'],
												xtype: 'menuseparator'
											},
											{
												text: 'Copy',
												itemId: 'tbCopy',
												iconCls: 'fa fa-lg fa-clone icon-small-vertical-correction icon-button-color-default',
												requiredPermissions: ['USER-SUBMISSIONS-CREATE'],
												handler: function () {
													var componentId = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0].get('componentId');

													Ext.getCmp('submissionGrid').setLoading('Copying submission...');
													Ext.Ajax.request({
														url: 'api/v1/resource/componentsubmissions/' + componentId + '/copy',
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
												requiredPermissions: ['USER-SUBMISSIONS-READ', 'USER-SUBMISSIONS-CREATE'],
												xtype: 'menuseparator'
											},											
											{
												text: 'Toggle Notify',
												itemId: 'tbNotify',
												iconCls: 'fa fa-lg fa-envelope-o icon-small-vertical-correction icon-button-color-default',
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
															url: 'api/v1/resource/componentsubmissions/' + componentId + '/setNotifyMe',
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
									width: '180px',
									iconCls: 'fa fa-2x fa-comment-o icon-button-color-default icon-vertical-correction-view',
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
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
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
														url: 'api/v1/resource/componentsubmissions/' + componentId + '/unsubmit',
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
									disabled: true,
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									requiredPermissions: ['USER-SUBMISSIONS-DELETE'],
									handler: function () {
										var componentId = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0].get('componentId');
										var name = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0].get('name');
										var record = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0];
										Ext.Msg.show({
											title:'Delete?',
											message: 'Are sure you want to delete: ' + name + '?',
											buttons: Ext.Msg.YESNO,
											icon: Ext.Msg.QUESTION,
											fn: function(btn) {
												if (btn === 'yes') {
													Ext.getCmp('submissionGrid').setLoading('Removing...');
													if (record.get('userSubmissionId')) {
														Ext.Ajax.request({
															url: 'api/v1/resource/usersubmissions/' + record.get('userSubmissionId'),
															method: 'DELETE',
															callback: function(){
																Ext.getCmp('submissionGrid').setLoading(false);
															},
															success: function(response, opts) {
																actionRefreshSubmission();
															}
														});	
													} else {
													
														Ext.Ajax.request({
															url: 'api/v1/resource/componentsubmissions/' + componentId + '/inactivate',
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
											}
										});
									}
								},
								{
									text: 'Delete Change',
									itemId: 'tbRemoveChangeRequest',
									hidden: true,
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									requiredPermissions: ['USER-SUBMISSIONS-DELETE'],
									handler: function () {
										var record = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0];
										
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

							var getWorkLink = function(worklinkType, resourceID) {
								url = undefined; 
								if (!resourceID) {
									return;
								}
								switch (worklinkType) {
									case 'Submission':
										url = 'api/v1/resource/usersubmissions/' + resourceID + '/worklink'
										break;
									case 'Component':
										url = 'api/v1/resource/components/' + resourceID + '/worklink'
										break;
									case 'ChangeRequest':
										url = 'api/v1/resource/components/' + resourceID + '/worklink'
										break;
									default:
										break;
								}
								if (url) {
									Ext.Ajax.request({
										url: url,
										method: 'GET',
										success: function(response, opts) {
											// display the ProgressView component
											record = Ext.data.Record.create(Ext.JSON.decode(response.responseText));
											var progressViewCmp = Ext.getCmp('workplan-progress-view');
											progressViewCmp.addSteps(record);
											progressViewCmp.setVisible(true);
										},
										failure: function(response, opts) {
											console.error('failed to fetch resource');
										}
									})
								}
							}
							
							if (selected.length > 0) {
								var record = selected[0];
								var worklinkType = undefined;
								var resourceID = undefined;
								
								tools.getComponent('tbEdit').setDisabled(true);
								tools.getComponent('options').setDisabled(false);
								tools.getComponent('commentsButton').setDisabled(false);
								
								//hiddens
								tools.getComponent('tbSubmitChange').setHidden(true);
								tools.getComponent('tbRemoveChangeRequest').setHidden(true);
								
								tools.getComponent('tbUnsubmit').setHidden(true);
								tools.getComponent('tbDelete').setHidden(false);
								tools.getComponent('tbDelete').setDisabled(true);
								tools.getComponent('tbUnapprove').setHidden(true);
								
								if (record.get('approvalState') === 'A'){
									worklinkType = 'Component';
									resourceID = record.get('componentId');
									tools.getComponent('tbDelete').setHidden(true);
									tools.getComponent('tbSubmitChange').setHidden(false);
									tools.getComponent('tbUnapprove').setHidden(false);
									
									if (record.get('statusOfPendingChange')) {
										tools.getComponent('tbSubmitChange').setText('Edit Change');
										tools.getComponent('tbSubmitChange').setIconCls('fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit');
									} else {
										tools.getComponent('tbSubmitChange').setText('Request Change');
										tools.getComponent('tbSubmitChange').setIconCls('fa fa-2x fa-comment-o icon-button-color-default icon-vertical-correction-view');

									}
								}
								if (record.get('approvalState') === 'P'){
									//tools.getComponent('tbUnsubmit').setHidden(false);
									worklinkType = 'Component';
									resourceID = record.get('componentId');
									tools.getComponent('tbEdit').setDisabled(false);
								}
								if (record.get('approvalState') === 'N'){
									tools.getComponent('tbDelete').setDisabled(false);
									tools.getComponent('tbEdit').setDisabled(false);
								}
								
								if (record.get('statusOfPendingChange')) {
									worklinkType = 'ChangeRequest';
									resourceID = record.get('pendingChangeComponentId');
									tools.getComponent('tbRemoveChangeRequest').setHidden(false);
								}
								
								if (record.get('userSubmissionId')) {
									worklinkType = 'Submission';
									resourceID = record.get('userSubmissionId');
									tools.getComponent('options').setDisabled(true);
								}
								getWorkLink(worklinkType, resourceID);
								
							} else {
								tools.getComponent('tbEdit').setDisabled(true);
								tools.getComponent('options').setDisabled(true);							
								tools.getComponent('commentsButton').setDisabled(true);
								
								//hiddens
								tools.getComponent('tbSubmitChange').setHidden(true);
								tools.getComponent('tbRemoveChangeRequest').setHidden(true);
								tools.getComponent('tbUnsubmit').setHidden(true);
								tools.getComponent('tbDelete').setHidden(false);
								tools.getComponent('tbDelete').setDisabled(true);
								tools.getComponent('tbUnapprove').setHidden(true);
								Ext.getCmp('workplan-progress-view').setVisible(false);
							}							
						}
					}
				});				
				
				addComponentToMainViewPort(submissionGrid);
				
				//newSubmission
				var availableSubmissions = [];
				var loadAvailableSubmissionForms = function() {					
	
					submissionGrid.setLoading(true);
					Ext.Ajax.request({
						url: 'api/v1/resource/componenttypes/nested',
						callback: function() {
							submissionGrid.setLoading(false);
						},
						success: function(response, opts) {
							var componentTypeRoot = Ext.decode(response.responseText);

							var flattenComponentTypes = function(root, indent) {
								if (root.componentType) {

									if (root.componentType.allowOnSubmission) {
										availableSubmissions.push({
											componentType: root.componentType.componentType,											
											label: root.componentType.label,
											description: root.componentType.description,
											iconUrl: root.componentType.iconUrl,
											indent: indent
										});
									}
								}

								root.children = root.children.sort(function(a, b){
									return a.componentType.label.toLowerCase().localeCompare(b.componentType.label.toLowerCase());
								});										
								Ext.Array.each(root.children, function(child){											
									flattenComponentTypes(child, (indent + 10));
								});
							};
							flattenComponentTypes(componentTypeRoot, 0);

						}
					});	
				};
				loadAvailableSubmissionForms();
								
				var actionNewSubmission = function() {
					
					var entryTypeSelectWin = Ext.create('Ext.window.Window', {
						title: 'Select Entry Type Form',
						modal: true,
						scrollable: true,
						closeAction: 'destroy',					
						width: '65%',
						minWidth: 400,
						height: '50%',
						minHeight: 300,
						layout: 'fit',
						bodyStyle: 'padding: 20px;',
						items: [
							{
								xtype: 'dataview',
								itemId: 'dataview',
								store: {								
								},
								itemSelector: 'div.entry-type',
								emptyText: 'No Submission Forms available',
								selectedItemCls: 'entry-type-select',
								tpl: new Ext.XTemplate(
									'<tpl for=".">',	
									'<div style="width: 100%; margin-bottom: 10px; padding-left: {indent}px;" class="entry-type entry-type-selector">',
									'	<table style="width: 100%;">',
									'		<tr><tpl if="iconUrl"><td><img scr="{iconUrl}"></td></tpl>',
									'			<td>',
									'				<h1>{label}</h1>',
									'				{description}',
									'				<hr>',
									'			</td>',
									'		</tr>',	
									'	</table>',
									'</div>',
									'</tpl>'		
								),
								selectionModel: {
									toggleOnClick: true
								},
								listeners: {
									itemclick: function(view, record, node, index, e, opts) {
										entryTypeSelectWin.submissionTemplateId = record.get('submissionTemplateId');
										entryTypeSelectWin.componentType = record.get('componentType');										
									}
								}
							}
						],
						dockedItems: [
							{
								xtype: 'toolbar',
								dock: 'bottom',
								items: [
									{
										text: 'Create',
										iconCls: 'fa fa-2x fa-plus icon-vertical-correction icon-button-color-save',
										scale: 'medium',
										handler: function() {											
											loadSubmissionForm(entryTypeSelectWin.componentType);											
											entryTypeSelectWin.close();
										}										
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Cancel',
										iconCls: 'fa fa-2x fa-close icon-vertical-correction icon-button-color-warning',
										scale: 'medium',
										handler: function() {
											entryTypeSelectWin.close();
										}										
									}
								]
							}
						]						
						
					});
					entryTypeSelectWin.show();		
					entryTypeSelectWin.queryById('dataview').getStore().loadData(availableSubmissions);
					if (availableSubmissions.length > 0) {
						entryTypeSelectWin.queryById('dataview').getSelectionModel().select(0);
						var typeRecord = entryTypeSelectWin.queryById('dataview').getStore().getAt(0);
						entryTypeSelectWin.submissionTemplateId = typeRecord.get('submissionTemplateId');
						entryTypeSelectWin.componentType = typeRecord.get('componentType');	
					}
					
				};			
								

				
				var actionRefreshSubmission = function(){
					Ext.getCmp('submissionGrid').getStore().load();
				};
				
				var actionPreviewComponent = function(record, isViewingComments) {

					previewComponentWin.setTitle(record.get('fullname'));
					if(record.get("statusOfPendingChange") === "Pending"){
						previewComponentWin.setTitle(record.get('fullname') + " (CHANGE REQUEST)");
					}

					var isPartialSubmission = record.get('userSubmissionId') ? true : false;
			
					var id = record.get('componentId');
					if (record.get('userSubmissionId')) {
						id = record.get('userSubmissionId');
					} else if (record.get('pendingChangeComponentId')) {
						id = record.get('pendingChangeComponentId');
					}
			
					previewComponentWin.show();
					if (!isPartialSubmission) {
						previewComponentWin.getComponent('previewPanel').show();
						entryCommentsPanel.setWidth('30%');
						entryCommentsPanel.setIsPartialSubmission(false);

						entryCommentsPanel.loadComponentComments(id);
						if (isViewingComments) {
							entryCommentsPanel.expand();
						}
						else {
							entryCommentsPanel.collapse();
						}
						previewContents.load('view.jsp?id=' + id +'&fullPage=true&embedded=true');
					}
					else {
						previewComponentWin.getComponent('previewPanel').hide();
						entryCommentsPanel.setWidth('100%');

						entryCommentsPanel.setIsPartialSubmission(true);
						entryCommentsPanel.loadComponentComments(id);
					}
					//previewCheckButtons();					
				};
				
				var currentUser;
				CoreService.userservice.getCurrentUser().then(function(user){
					currentUser = user;
				});
				
				var createChangeRequest = function(componentId) {
		
					submissionGrid.setLoading('Creating change request...');
					Ext.Ajax.request({
						url: 'api/v1/resource/components/' + componentId + '/changerequestforsubmission',
						method: 'POST',
						callback: function() {
							submissionGrid.setLoading(false);
						},
						success: function(response, opts) {
							var userSubmission = Ext.decode(response.responseText);
														
							loadSubmissionForm(userSubmission.componentType, null, userSubmission);
							actionRefreshSubmission();
						}
					});
		
				};
				
				var editChangeRequest = function(componentId) {
		
					submissionGrid.setLoading('Editing change request...');
					Ext.Ajax.request({
						url: 'api/v1/resource/components/' + componentId + '/editchangerequest',
						method: 'POST',
						callback: function() {
							submissionGrid.setLoading(false);
						},
						success: function(response, opts) {
							var userSubmission = Ext.decode(response.responseText);
														
							loadSubmissionForm(userSubmission.componentType, null, userSubmission);
							actionRefreshSubmission();
						}
					});
		
				};		
				
				var editExistingEntry = function(componentId) {
					
					submissionGrid.setLoading('Loading Submission...');
					Ext.Ajax.request({
						url: 'api/v1/resource/components/' + componentId + '/usersubmission',
						method: 'POST',
						callback: function() {
							submissionGrid.setLoading(false);
						},
						success: function(response, opts) {
							var userSubmission = Ext.decode(response.responseText);
														
							loadSubmissionForm(userSubmission.componentType, null, userSubmission);
							actionRefreshSubmission();
						}
					});
					
				};
				
				var loadSubmissionForm = function(componentType, record, userSubmissionExisting) {
						
					submissionGrid.setLoading('Loading Submission Form...');
					Ext.Ajax.request({
						url: 'api/v1/resource/submissiontemplates/componenttype/' + componentType,
						callback: function() {
							submissionGrid.setLoading(false);
						},
						success: function(response, opts) {
							var template = Ext.decode(response.responseText);

							// reset the entryType so it uses the record's entry type (not the entry type of the template)
							// Since the template entry type can be blank
							template.entryType = componentType;

							var submissionWin = Ext.create('Ext.window.Window', {
								title: 'Submission',
								layout: 'fit',
								modal: true,
								closeAction: 'destroy',
								width: '80%',
								height: '80%',
								maximizable: true,									
								items: [
									{
										xtype: 'osf-customSubmission-SubmissionformFullControl',
										itemId: 'controlForm',
										finishInitialSave: function() {
											actionRefreshSubmission();
										},
										submissionSuccess: function() {
											actionRefreshSubmission();
											submissionWin.skipSave = true;
											submissionWin.close();
										}
									}
								],
								listeners: {
									beforeclose: function(panel, opts) {
										var form = panel.queryById('submissionForm');
										if (form.userSubmission && !submissionWin.skipSave) {
											panel.queryById('controlForm').saveSubmission();
											Ext.Msg.alert({
												title: 'Warning',
												message: 'This entry was never submitted for review.<br>To submit for review proceed to the last page and click submit.',
												icon: Ext.Msg.WARNING,
												buttons: Ext.Msg.OK
											});
										}
									}
								}
							});
							submissionWin.show();

							var finishLoadingForm = function(userSubmission) {
								submissionWin.queryById('controlForm').load(template, componentType, userSubmission, true);					
							};

							if (record) {

								//load user submission
								submissionWin.setLoading(true);
								Ext.Ajax.request({
									url: 'api/v1/resource/usersubmissions/' + record.get('userSubmissionId'),
									callback: function(){
										submissionWin.setLoading(false);
									},
									success: function(response, opt) {
										var userSubmission = Ext.decode(response.responseText);
										finishLoadingForm(userSubmission);
									}
								});
							} else {									
								finishLoadingForm(userSubmissionExisting);
							}								

						}
					});
					
				};				
				
			});

        </script>

    </stripes:layout-component>
</stripes:layout-render>
