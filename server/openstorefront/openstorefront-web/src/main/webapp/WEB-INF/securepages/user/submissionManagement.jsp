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
									iconCls: 'fa fa-lg fa-arrow-left icon-button-color-default',									
									handler: function() {
										actionPreviewNextRecord(false);
									}									
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Close',
									iconCls: 'fa fa-lg fa-close icon-button-color-warning',
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
									iconCls: 'fa fa-lg fa-arrow-right icon-button-color-default',
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
							}							
						],
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/componentsubmissions'
						}
					},
					columns: [
						{ text: 'Name', dataIndex: 'name', flex: 1, minWidth: 200 },
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
									text = 'Not Submitted';
								}
								return text;
							}
						},
						{ text: 'Submit/Approve Date', align: 'center', dataIndex: 'submitApproveDts', width: 200, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Approval Email', dataIndex: 'notifyOfApprovalEmail', width: 200, sortable: false },
						{ text: 'Pending Change', align: 'center', dataIndex: 'statusOfPendingChange', width: 150, sortable: false },
						{ text: 'Pending Change Submit Date', align: 'center', dataIndex: 'pendingChangeSubmitDts', width: 250, xtype: 'datecolumn', format:'m/d/y H:i:s', hidden: true, sortable: false }
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
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										actionRefreshSubmission();
									}
								}, 
								{
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
									handler: function () {
										//Ext.getCmp('submissionWindow').show();
										//Ext.getCmp('submissionPanel').resetSubmission();
									}
								},
								{
									text: 'Edit',
									itemId: 'tbEdit',
									scale: 'medium',	
									disabled: true,
									width: '100px',
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
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
									width: '180px',
									iconCls: 'fa fa-2x fa-edit icon-button-color-default icon-vertical-correction-view',
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
												iconCls: 'fa fa-lg fa-eye icon-small-vertical-correction icon-button-color-default',
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
												iconCls: 'fa fa-lg fa-clone icon-small-vertical-correction icon-button-color-default',
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
										});
									}
								},
								{
									text: 'Delete Change',
									itemId: 'tbRemoveChangeRequest',
									hidden: true,
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
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
								tools.getComponent('tbDelete').setHidden(false);
								tools.getComponent('tbDelete').setDisabled(true);
								tools.getComponent('tbUnapprove').setHidden(true);
								
								if (record.get('approvalState') === 'A'){	
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
									tools.getComponent('tbEdit').setDisabled(false);
								}
								if (record.get('approvalState') === 'N'){
									tools.getComponent('tbDelete').setDisabled(false);
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
								tools.getComponent('tbDelete').setHidden(false);
								tools.getComponent('tbDelete').setDisabled(true);
								tools.getComponent('tbUnapprove').setHidden(true);
							}							
						}
					}
				});				
				
				addComponentToMainViewPort(submissionGrid);
				
				//newSubmission
				var loadAvailableSubmissionForms = function() {
					
					var submissionBtn = submissionGrid.queryById('newSubmission');
					
					submissionGrid.setLoading(true);
					Ext.Ajax.request({
						url: 'api/v1/resource/submissiontemplates/default',
						callback: function() {
							submissionGrid.setLoading(false);
						},
						success: function(response, opts) {
							var defaultTemplate = Ext.decode(response.responseText);							
							
							submissionGrid.setLoading(true);
							Ext.Ajax.request({
								url: 'api/v1/resource/componenttypes',
								callback: function() {
									submissionGrid.setLoading(false);
								},
								success: function(response, opts) {
									var componentTypes = Ext.decode(response.responseText);

									componentTypes = componentTypes.sort(function(a, b){
										return a.label.localeCompare(b.label);
									});

									var menuItems = [];
									Ext.Array.each(componentTypes, function(componentType){
										if (componentType.allowOnSubmission) {
											menuItems.push({
												text: componentType.label,
												componentType: componentType,
												submissionTemplateId: componentType.submissionTemplateId || defaultTemplate.submissionTemplateId,
												handler: function() {											
													loadSubmissionForm(this.submissionTemplateId, this.componentType.componentType);
												}
											});
										}
									});
									submissionBtn.setMenu({
										items: menuItems
									});
								}
							});							
						}
					});					
					
				};
				loadAvailableSubmissionForms();
								
				
				var actionRefreshSubmission = function(){
					Ext.getCmp('submissionGrid').getStore().load();
				};
				
				var actionPreviewComponent = function(){
					previewComponentWin.show();
					var componentId = Ext.getCmp('submissionGrid').getSelectionModel().getSelection()[0].get('componentId');
					previewContents.load('view.jsp?id=' + componentId +'&fullPage=true&embedded=true');
					previewCheckButtons();					
				};
				
				var currentUser;
				CoreService.userservice.getCurrentUser().then(function(user){
					currentUser = user;
				});
				
				var loadSubmissionForm = function(submissionTemplateId, componentType) {
					
					if (!submissionTemplateId) {						
						console.log("Need to Set default");
					}
					
					if (submissionTemplateId) {
						
						submissionGrid.setLoading('Loading Submission Form...');
						Ext.Ajax.request({
							url: 'api/v1/resource/submissiontemplates/' + submissionTemplateId,
							callback: function() {
								submissionGrid.setLoading(false);
							},
							success: function(response, opts) {
								var template = Ext.decode(response.responseText);
								
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
											itemId: 'form'								
										}
									]
								});
								submissionWin.show();
								submissionWin.queryById('form').load(template, componentType, null, true);
							}
						});
						
					}
					
				};
				
				
			});

        </script>

    </stripes:layout-component>
</stripes:layout-render>
