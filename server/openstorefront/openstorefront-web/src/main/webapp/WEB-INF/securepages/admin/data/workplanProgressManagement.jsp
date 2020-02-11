<%--
/* 
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
--%>
<%@include file="../../../../layout/includes.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">

		<stripes:layout-render name="../../../../layout/adminheader.jsp">
		</stripes:layout-render>

		<script type="text/javascript">
			/* global Ext, CoreUtil */
			// Ext.require('OSF.common.workPlanProgressComment');
			Ext.require('OSF.workplanProgress.CommentPanel');
			Ext.require('OSF.workplanProgress.ProgressView');
			Ext.onReady(function() {

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

				var componentViewWin = Ext.create('Ext.window.Window', {
					id: 'componentViewWin',
					title: 'Entry Details',
					iconCls: 'fa fa-lg fa-exchange',
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

				var linkGridStore = Ext.create('Ext.data.Store', {
					autoLoad: true,	
					pageSize: 100,
					remoteSort: true,
					sorters: [
						new Ext.util.Sorter({
							property : 'linkName',
							direction: 'ASC'
						})
					],
					fields: [
						{ name: 'currentStepName', mapping: function(data){
							return data.currentStep ? data.currentStep.name : 'N/A';
						}},
						{
							name: 'updateDts',
							type: 'date',
							dateFormat: 'c'
						}						
					],
					proxy: CoreUtil.pagingProxy({
						url: 'api/v1/resource/workplans/worklinks',							
						reader: {
							type: 'json',
							rootProperty: 'data',
							totalProperty: 'totalNumber'
						}
					}),
					listeners: {
						beforeLoad: function(store, operation, eOpts){
							store.getProxy().extraParams = {
								assignFilter: Ext.getCmp('componentGridFilter-AssignmentStatus').getValue() ? Ext.getCmp('componentGridFilter-AssignmentStatus').getValue() : null,
								showfinal: linkGrid.queryById('filter-showFinalStage').getValue() ? linkGrid.queryById('filter-showFinalStage').getValue() : false,
								searchName: linkGrid.queryById('filter-searchName').getValue() ? linkGrid.queryById('filter-searchName').getValue(): null
							};
						}
					}
				});

				var linkGrid = Ext.create('Ext.grid.Panel', {
					title: 'Work Plan Progress Management <i class="fa fa-lg fa-question-circle"  data-qtip="This tool gives the ability to review records in a work plan" ></i>',
					id: 'linkGrid',
					store: linkGridStore,
					columnLines: true,					
					viewConfig: {
						enableTextSelection: true
					},					
					columns: [
                       	{ text: 'Name', dataIndex: 'linkName', flex: 2, minWidth: 200,
							renderer: function(value, meta, record) {
								var display = value;
								if (record.get('userSubmissionId')) {
									display += ' <i class="fa fa-exclamation-triangle text-warning" data-qtip="Owner is making changes"></i>';
								}
								return display;
							}
						}, 						
						{ text: 'Link Type', dataIndex: 'linkType', width: 125, hidden: true }, 						
						{ text: 'Entry Type', dataIndex: 'componentTypeFullDescription', flex: 1, minWidth: 200	}, 						
						{ text: 'WorkPlan Name', dataIndex: 'workPlanName', width: 175, hidden: true },
						{ text: 'WorkPlanLink Id', dataIndex: 'workPlanLinkId', width: 175, hidden: true },
                        { text: 'Current Step', dataIndex: 'currentStepName', sortable: false, width: 175 },
						{ text: 'Status Marking', dataIndex: 'subStatusDescription', width: 175,
							renderer: function(value, meta, record) {
								if (value) {	
									var color = record.get('workPlanSubStatusColor');
									if (!Ext.String.startsWith(color, '#')) {
										color = '#' + color;
									}
									meta.tdStyle = 'font-weight: bold; background-color: ' + color;
									return value;
								}
								meta.tdStyle = 'font-style: italic; color: grey;';
								return 'Ready to Proceed';
							}
						},
						{ text: 'Assigned To', dataIndex: 'currentUserAssigned', width: 175,
							renderer: function(value, meta) {
								if (value) {
									return value;
								}
								meta.tdStyle = 'font-style: italic; color: grey;';
								return 'Unassigned';
							}
						},					
						{ text: 'Group Assigned', dataIndex: 'currentGroupAssigned', width: 175,							
							renderer: function(value, meta) {
								if (value) {
									return value;
								}
								meta.tdStyle = 'font-style: italic; color: grey;';
								return 'Unassigned';
							}
						},
						{ text: 'Last updated', dataIndex: 'updateDts', width: 175, xtype: 'datecolumn', format: 'm/d/y H:i:s' }
					],
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [
								Ext.create('OSF.component.StandardComboBox', {
									id: 'componentGridFilter-AssignmentStatus',
									emptyText: 'ALL',
									fieldLabel: 'Assignment Status',
									name: 'currentUserAssigned',
									typeAhead: false,
									editable: false,
									listeners: {
										change: function(filter, newValue, oldValue, opts){
											actionRefreshComponentGrid(true);
										}
									},
									storeConfig: {
										customStore: {
											fields: [
												'code',
												'description'
											],
											data: [
												{
													code: 'M',
													description: 'Assigned To Me'
												},
												{
													code: 'U',
													description: 'Unassigned'
												},
												{
													code: null,
													description: 'All'
												}
											]
										}
									}
								}),
								{
									xtype: 'panel',
									items: [
										{																	
											xtype: 'checkbox',
											itemId: 'filter-showFinalStage',
											name: 'showfinal',								
											margin: '20 0 0 0',
											width: 150,
											requiredPermissions: ['WORKFLOW-LINK-READ-ALL'],
											boxLabel: '<b>Show Final Stage</b>',
											listeners: {
												change: function(filter, newValue, oldValue, opts){													
													actionRefreshComponentGrid(true);
												}
											}
										}
									]
								},
								{
									xtype: 'textfield',
									itemId: 'filter-searchName',
									name: 'searchName',
									requiredPermissions: ['WORKFLOW-LINK-READ-ALL'],
									fieldLabel: 'Search By Name',
									labelAlign: 'top',
									labelSeparator: '',
									listeners: {
										change: {
											fn: function(filter, newValue, oldValue, opts){
												actionRefreshComponentGrid(true);
											},
											buffer: 1500
										}
									}
								},
							]
						},
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										actionRefreshComponentGrid();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'View',
									id: 'lookupGrid-tools-preview',
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction-view',
									disabled: true,
									handler: function () {
										actionViewComponent();
									}
								},
                                {
                                    xtype: 'tbseparator'
                                },
                                {
									text: 'Work/Process',
									id: 'lookupGrid-tools-work-process',
									scale: 'medium',
									width: '200px',
									iconCls: 'fa fa-2x fa-list-ol icon-button-color-view icon-vertical-correction-view',
									disabled: true,
									requiredPermissions: ['WORKFLOW-LINK-UPDATE'],
									handler: function () {
										var record = linkGrid.getSelection()[0];
										// check the required attributes of the component
										var submissionId = record.get('userSubmissionId');
										var componentId = record.get('componentId');
										if (submissionId) {
											//check submission
											actionWorkAndProcessComponent(record, true);
										} else if (componentId) {
											//check component
											Ext.getBody().mask('Checking Entry...');
											Ext.Ajax.request({
												url: 'api/v1/resource/components/' + componentId + '/validate',
												method: 'GET',
												success: function (response, opts) {
													Ext.getBody().unmask();
													var data = response.responseText ? Ext.JSON.decode(response.responseText) : undefined;
													invalidEntry = data && data.errors;
													actionWorkAndProcessComponent(record, invalidEntry, data);
												},
												failure: function (response, opts) {
													console.error("ERROR: ", response);
													Ext.getBody().unmask();
													actionWorkAndProcessComponent(record, true);
												}
											});
										}
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Assign',
									id: 'lookupGrid-tools-action',
									scale: 'medium',
									disabled: false,
									requiredPermissions: ['WORKFLOW-LINK-ASSIGN', 'WORKFLOW-LINK-ASSIGN-ANY'],
									menu: [
										{
											text: 'Assign To Me',
											id: 'lookupGrid-tools-action-me-assign',
											disabled: true,
											iconCls: 'fa fa-lg fas fa-user-circle icon-small-vertical-correction icon-button-color-default',
											permissionLogicalOperator: 'OR',
											requiredPermissions: ['WORKFLOW-LINK-ASSIGN-SELF', 'WORKFLOW-LINK-ASSIGN-ANY'],
											handler: function(){
												actionAssignToMe();
											}
										},
										{
											xtype: 'menuseparator',
											requiredPermissions: ['WORKFLOW-LINK-ASSIGN-ANY']
										},
										{
											text: 'Assign To Admin Group',
											id: 'lookupGrid-tools-action-admin-assign',
											disabled: true,
											iconCls: 'fa fa-lg fa-user-md icon-small-vertical-correction icon-button-color-default',
											requiredPermissions: ['WORKFLOW-LINK-ASSIGN-ANY'],
											handler: function(){
												actionAssignToAdmin();
											}
										},
										{
											text: 'Unassign',
											id: 'lookupGrid-tools-action-unassign',
											disabled: true,
											iconCls: 'fa fa-lg fas fa-user-times icon-small-vertical-correction icon-button-color-default',
											requiredPermissions: ['WORKFLOW-LINK-ASSIGN-ANY'],
											handler: function () {
												actionUnassign();
											}
										},                                       
										{
											text: 'Reassign',
											id: 'lookupGrid-tools-action-reassign',
											disabled: true,
											iconCls: 'fa fa-lg fa-users icon-small-vertical-correction icon-button-color-default',			
											permissionLogicalOperator: 'AND',
											requiredPermissions: ['WORKFLOW-LINK-ASSIGN-ANY', 'ADMIN-ROLE-MANAGEMENT-READ'],
											handler: function() {
												actionReassign();
											}
										}
									]
								}
							]
						},
						{
							xtype: 'osf.wp.progressView'
						}
					],
					listeners: {
						selectionchange: function(selectionModel, records, opts){
							checkGridTools();
							var record = linkGrid.getSelection()[0];
							var progressViewCmp = Ext.getCmp('workplan-progress-view');
							if (record) {
								progressViewCmp.addSteps(record);
							} else {
								progressViewCmp.setVisible(false);
							}
						}
					},
					bbar: Ext.create('Ext.PagingToolbar', {
						store: linkGridStore,
						displayInfo: true,
						displayMsg: 'Displaying Work Links {0} - {1} of {2}',
						emptyMsg: "No Work Links to display"
					})					
				});

				addComponentToMainViewPort(linkGrid);

				var checkGridTools = function() {

					if (linkGrid.getSelectionModel().getCount() === 1) {
						Ext.getCmp('lookupGrid-tools-preview').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-work-process').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-action-admin-assign').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-action-me-assign').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-action-unassign').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-action-reassign').setDisabled(false);

					}
					else if (linkGrid.getSelectionModel().getCount() > 1) {

						Ext.getCmp('lookupGrid-tools-preview').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-work-process').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-action-admin-assign').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-action-me-assign').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-action-unassign').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-action-reassign').setDisabled(true);

					} else {

						Ext.getCmp('lookupGrid-tools-preview').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-work-process').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-action-admin-assign').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-action-me-assign').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-action-unassign').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-action-reassign').setDisabled(true);
					}
				};

				var actionRefreshComponentGrid = function(resetPage){					
					if(resetPage)
					{						
						Ext.getCmp('linkGrid').getStore().loadPage(1);
					}
					else
					{
						Ext.getCmp('linkGrid').getStore().load();
					}
				};
								
				var actionViewComponent = function(){

					componentViewWin.show();
					var record = Ext.getCmp('linkGrid').getSelection()[0];
					var id;
					var isPartialSubmission = false;
					if(record.data){
						record = record.data;
						id = record.componentId;
						if (record.userSubmissionId) {
							id = record.userSubmissionId;
							isPartialSubmission = true;
						}
					}

					if (!isPartialSubmission) {
						componentViewWin.getComponent('previewPanel').show();
						entryCommentsPanel.setWidth('30%');
						entryCommentsPanel.setIsPartialSubmission(false);

						entryCommentsPanel.loadComponentComments(id);
						entryCommentsPanel.expand();
						previewContents.load('view.jsp?id=' + id +'&fullPage=true&embedded=true');
					}
					else {
						componentViewWin.getComponent('previewPanel').hide();
						entryCommentsPanel.setWidth('100%');

						entryCommentsPanel.setIsPartialSubmission(true);
						entryCommentsPanel.loadComponentComments(id);
					}
					
				};
				
				var actionWorkAndProcessComponent = function(record, invalidEntry, responseData){
					
					var workPlanId = record.get('workPlanId');
					var workPlanLinkId = record.get('workPlanLinkId');

					var errors = responseData && responseData.errors && responseData.errors.entry ? responseData.errors.entry : undefined;
					var errorsHtml = '';
					if (errors) {
						Ext.Array.each(errors, function(el) {
							errorsHtml += '<br/><p>' + el.value + '</p>'
						})
					}

					var processCompWin = Ext.create('Ext.window.Window', {				
						title: 'Workflow',
						iconCls: 'fa fa-lg fa-exchange',
						width: '50%',
						height: 400,
						closeAction: 'destroy',
						layout: 'fit',						
						modal: true,
						listeners: {
							afterRender: function(){
								var neededRoles = record.get("currentStep").roles;
								if(CoreService.userservice.UserHasRoles(neededRoles)) {
									this.queryById('complete').show();
									this.queryById('previous').show();
								}
							}
						},
						items: [
							{
								xtype: 'panel',
								itemId: 'stepInfo',
								width: '100%',
								scrollable: true,
								bodyStyle: 'padding: 10px;',
								tpl: new Ext.XTemplate(
									!!invalidEntry ?
										'<div style="padding: 0.5em;" class="alert-danger"><i class="fa fa-warning text-warning"></i>&nbsp; Unable to process workflow. The component has missing required attributes or has not been submitted. Please contact the owner of this entry or the admin.'
										+ errorsHtml + '</div>'
										: '',
									'<h1 style="text-align: center">Current Step - {name}</h1>',
									'<h3>Instructions: </h3>',
									'{description}'
								),
								dockedItems:[
									{
										xtype: 'combo',	
										dock: 'bottom',
										itemId: 'statusField',
										name: 'status',
										width: '100%',
										labelWidth: 200,
										margin: '10 10 10 10',
										allowBlank: true,
										editable: false,
										typeAhead: false,																
										fieldLabel: 'Work State <i class="fa fa-question-circle text-warning" data-qtip="Flags an item with a status to communicate state"></i>',
										disabled: !!invalidEntry,
										valueField: 'code',
										displayField: 'description',
										queryMode: 'local',
										store: {
											autoLoad: true,
											proxy: {
												type: 'ajax',
												url: 'api/v1/resource/lookuptypes/WorkPlanSubStatusType?addSelect=true'
											}
										},
										listeners: {
											change: function(field, newValue, oldValue) {
												processCompWin.setLoading('Updating Status...');
												
												Ext.Ajax.request({
													url: 'api/v1/resource/workplans/' + workPlanId + '/worklinks/' + workPlanLinkId + '/status/' + (newValue || 'RESET'),
													method: 'PUT',
													callback: function() {
														processCompWin.setLoading(false);
													},
													success: function(response, opt) {
														actionRefreshComponentGrid();
														Ext.toast('Successfully Updated Status');
													}
												});
												
											}
										}	
									}
								]
							}
						],
						dockedItems:[							
							{
								xtype: 'toolbar',
								border: true,
								dock: 'bottom',								
								items: [									
									{
										text: 'Go To Previous Step',
										iconCls: 'fa fa-lg fa-backward icon-button-color-save',
										itemId: 'previous',
										disabled: !!invalidEntry,
										hidden: true,
										handler: function(){
											processCompWin.setLoading('Updating State...');
											Ext.Ajax.request({
												url: 'api/v1/resource/workplans/' + workPlanId + '/worklinks/' + workPlanLinkId + '/previousstep',
												method: 'PUT',
												callback: function() {
													processCompWin.setLoading(false);
												},
												success: function(response, opt) {
													actionRefreshComponentGrid();
													Ext.toast('Successfully Updated Workflow');
													processCompWin.close();
												}												
											});
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Complete This Step',											
										iconCls: 'fa fa-lg fa-list-alt icon-button-color-save',
										itemId: 'complete',
										disabled: !!invalidEntry,
										hidden: true,
										handler: function(){
											processCompWin.setLoading('Updating State...');
											Ext.Ajax.request({
												url: 'api/v1/resource/workplans/' + workPlanId + '/worklinks/' + workPlanLinkId + '/nextstep',
												method: 'PUT',
												callback: function() {
													processCompWin.setLoading(false);
												},
												success: function(response, opt) {
													actionRefreshComponentGrid();
													Ext.toast('Successfully Updated Workflow');
													processCompWin.close();
												}												
											});											
										}
									}
								]
							}
						]
					});
					processCompWin.show();
					
					processCompWin.queryById('stepInfo').update(record.get('currentStep'));
					processCompWin.queryById('statusField').setValue(record.get('subStatus'));
					
				};
				
				var actionAssignToAdmin= function(){
					var assignToAdminCommentWin = Ext.create('Ext.window.Window', {				
						title: 'Assign entry to Admin',
						iconCls: 'fa fa-lg fa-comment',
						width: '50%',
						height: '50%',				
						modal: true,
						closeAction: 'destroy',
						layout: 'fit',
						items: [
							{
								xtype: 'form',
								itemId: 'form',																
								bodyStyle: 'padding: 10px;',
								layout: 'fit',
								items: [
									{	
										xtype: 'htmleditor',
										name: 'comment',
										allowBlank: false,									
										fieldBodyCls: 'form-comp-htmleditor-border',
										fieldLabel: 'Add Comment (Admin Visible Only)<span class="field-required" />',
										labelAlign: 'top',
										maxLength: 4000										
									}
								],
								dockedItems: [									
									{
										xtype: 'toolbar',
										dock: 'bottom',
										items: [
											{
												text: 'Assign to Admin',
												formBind: true,
												iconCls: 'fa fa-lg fa-comment icon-button-color-save',
												handler: function(){
													var formData = assignToAdminCommentWin.queryById('form').getValues();										
													if (!formData.comment || formData.comment === '') {
														Ext.Msg.show({
															title:'Missing Comment',
															message: 'Enter a comment to communicate what an admin needs to do.',
															buttons: Ext.Msg.OK,
															icon: Ext.Msg.ERROR,
															fn: function(btn) {											
															}
														});														
													} else {
														var comment = {
															commentType: 'SUBMISSION',
															privateComment: true,
															comment: formData.comment
														};

														var record = linkGrid.getSelection()[0];
														var workPlanId = record.get('workPlanId');
														var workPlanLinkId = record.get('workPlanLinkId');

														assignToAdminCommentWin.setLoading('Assigning...');
														Ext.Ajax.request({
															url: 'api/v1/resource/workplans/' + workPlanId + '/worklinks/' + workPlanLinkId + '/assignToAdmin',
															method: 'PUT',
															jsonData: comment,
															callback: function() {
																assignToAdminCommentWin.setLoading(false);
															},
															success: function(response, opt) {
																actionRefreshComponentGrid();														
																assignToAdminCommentWin.close();
															}												
														});	
													}
												}
											},
											{
												xtype: 'tbfill'
											},
											{
												text: 'Cancel',
												itemId: 'cancel',											
												iconCls: 'fa fa-lg fa-close icon-button-color-warning',
												handler: function() {
													this.up('window').close();
												}
											}											
										]
									}
								]
							}				
						]					

					});	
					assignToAdminCommentWin.show();
	
				};
				
				var actionAssignToMe = function(){
					var record = linkGrid.getSelection()[0];
					var workPlanId = record.get('workPlanId');
					var workPlanLinkId = record.get('workPlanLinkId');

					Ext.Ajax.request({
						url: 'api/v1/resource/workplans/' + workPlanId + '/worklinks/' + workPlanLinkId + '/assigntome',
						method: 'PUT',
						success: function(response, opt) {
							actionRefreshComponentGrid();
						}												
					});
				};
				
				var actionUnassign = function(){
					var record = linkGrid.getSelection()[0];
					var workPlanId = record.get('workPlanId');
					var workPlanLinkId = record.get('workPlanLinkId');

					Ext.Ajax.request({
						url: 'api/v1/resource/workplans/' + workPlanId + '/worklinks/' + workPlanLinkId + '/unassign',
						method: 'PUT',
						callback: function() {
							// processCompWin.setLoading(false);
						},
						success: function(response, opt) {
							actionRefreshComponentGrid();
							// Ext.toast('Successfully Updated Wor');
							// processCompWin.close();
						}												
					});				
				};
				
				var actionReassign = function(){
					
					var userAssignWin = Ext.create('Ext.window.Window', {						
						title: 'Change Group/Assignee - ',
						iconCls: 'fa fa-lg fa-user',
						width: '50%',
						autoHeight: true,						
						closeAction: 'destroy',
						modal: true,
						layout: 'fit',
						items: [
							{
								xtype: 'form',
								itemId: 'reassignForm',
								bodyStyle: 'padding: 10px',
								items: [
									Ext.create('OSF.component.StandardComboBox', {
										name: 'roleGroup',	
										itemId: 'assignGroupId',								
										allowBlank: false,
										editable: false,
										typeAhead: false,
										margin: '0 0 5 0',
										width: '100%',										
										fieldLabel: 'Group/Role <span class="field-required" />',
										queryMode: 'local',
										displayField:'description',
										valueField:'code',
										requiredPermissions: ['WORKFLOW-GROUP-ASSIGN'],
										storeConfig: {
											url: 'api/v1/resource/securityroles/lookup'
										},
										listeners: {
											beforerender:function(){

												// Get Permissions Group for Entry
												var currentGroupAssigned = linkGrid.getSelection()[0].data.currentGroupAssigned;
												
												// Check If Permissions Group is Set
												if(currentGroupAssigned && currentGroupAssigned != 'undefined'){

													// Set StandardCombobox With Default Perm. Group
													this.select(linkGrid.getSelection()[0].data.currentGroupAssigned);
												}

												// Permission Group is undefined, fire change event maunually
												else {
													this.fireEvent("change");
												}
											},
											change: function(filter, newValue, oldValue, opts){
												// Disappear reassignWarning
												userAssignWin.queryById('reassignWarning').setVisible(false);

												// Request Members Of Group
												userAssignWin.queryById('assignUserId').getStore().load({
													url: 'api/v1/resource/securityroles/'+ encodeURIComponent(newValue) +'/users',
													callback: function(){
														userAssignWin.queryById('assignUserId').setDisabled(false);
														userAssignWin.queryById('assignUserId-clear').setDisabled(false);
														
														// Show Appropriate Labeling/Warning
														// Check If Users Are Available After Load
														if(userAssignWin.queryById('assignUserId').store.data.items.length == 0){
															// Show Warning
															userAssignWin.queryById('reassignWarning').setVisible(true);
														}
													}
												});
											}
										}
									}),
									{
										xtype: 'label',
										itemId: 'reassignWarning',
										hidden:true,
										html:'<i class="fa fa-exclamation-triangle 3x" display:inline-block; text-align:left; font-size: 5em";"></i> SPOON Administrators: It appears there are no users assigned to this Permissions Group. Add users <a href="AdminTool.action?load=User-Management">here</a>, or change your <a href="AdminTool.action?load=Security-Roles">permissions</a> to be able change which group this entry is assigned to.'
									},
									{
										xtype: 'panel',
										layout: 'hbox',
										items: [											
											Ext.create('OSF.component.StandardComboBox', {
												name: 'username',
												itemId: 'assignUserId',								
												allowBlank: true,
												editable: false,
												typeAhead: false,
												margin: '0 0 5 0',
												flex: 1,
												fieldLabel: 'Assign User from Group',
												displayField: 'username',
												valueField: 'username',
												queryMode: 'local',
												disabled: true,
												store: {
													proxy: {
														type: 'ajax',
														url: 'api/v1/resource/securityroles'
													}											
												}
											}),
											{
												xtype: 'button',
												itemId: 'assignUserId-clear',
												disabled: true,
												text: 'Clear',
												margin: '30 0 0 0',
												handler: function() {
													userAssignWin.queryById('assignUserId').clearValue();
												}
											}											
										]
									}
								],
								dockedItems: [
									{
										xtype: 'toolbar',
										dock: 'bottom',
										items: [
											{
												text: 'Save',
												formBind: true,
												iconCls: 'fa fa-lg fa-save icon-button-color-save',
												handler: function(){
													
													var record = linkGrid.getSelection()[0];
													var workPlanId = record.get('workPlanId');
													var workPlanLinkId = record.get('workPlanLinkId');
													var queryData = userAssignWin.queryById('reassignForm').getValues();
													var queryParams =  '?roleGroup=' + queryData.roleGroup + '&username=' + queryData.username;
													
													userAssignWin.setLoading('Assigning...');

													Ext.Ajax.request({
														url: 'api/v1/resource/workplans/' + workPlanId + '/worklinks/' + workPlanLinkId + '/assign' + queryParams,
														method: 'PUT',
														callback: function() {
															userAssignWin.setLoading(false);
														},
														success: function(response, opt) {
															actionRefreshComponentGrid();
															userAssignWin.close();
														}												
													});		
													
												}
											},
											{
												xtype: 'tbfill'
											},
											{
												text: 'Cancel',
												iconCls: 'fa fa-lg fa-close icon-button-color-warning',
												handler: function(){
													userAssignWin.close();
												}
											}
										]
									}
								]
							}
						]
					});
					userAssignWin.show();
				};

			});

		</script>

	</stripes:layout-component>
</stripes:layout-render>
