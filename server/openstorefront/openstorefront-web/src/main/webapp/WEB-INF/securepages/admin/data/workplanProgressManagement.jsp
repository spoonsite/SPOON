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

<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">

		<stripes:layout-render name="../../../../layout/adminheader.jsp">
		</stripes:layout-render>

		<script type="text/javascript">
			/* global Ext, CoreUtil */
			// Ext.require('OSF.common.workPlanProgressComment');
			Ext.require('OSF.workplanProgress.CommentPanel');
			Ext.onReady(function() {

				var previewContents = Ext.create('OSF.ux.IFrame', {
					src: ''
				});

				var componentViewWin = Ext.create('Ext.window.Window', {
					id: 'componentViewWin',
					title: 'Entry Details',
					iconCls: 'fa fa-lg fa-exchange',
					maximizable: true,
					width: '75%',
					height: '75%',
					modal: true,
					defaults: {
						collapsible: true,
						split: true,
						bodyPadding: 0
					},
					layout: 'border',
					items: [
						{
							xtype: 'panel',
							collapsible: false,
							region: 'center',
							margin: '0 0 0 0',
							width: '75%',
							height: '80%',
							title: 'Entry Info',
							iconCls: 'fa fa-lg fa-eye',
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
											text: 'Close',
											iconCls: 'fa fa-lg fa-close icon-button-color-warning',
											handler: function() {
												this.up('window').hide();
											}
										}
									]
								}
							]
						},
						{
							xtype: 'osf.wp.commentpanel',
							floatable: false,
							collapsed: false,
							collapsible: true,
							animCollapse: false,
							titleCollapse: true,
							width: 500,
							minWidth: 250,
							maxWidth: 650,
							bodyStyle: 'background: white;',
							region: 'east',
							itemId: 'commentPanel'
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
                        { text: 'Current Step', dataIndex: 'currentStepName', width: 175 },
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
								{
									xtype: 'container',
									itemId: 'stepsLegendContainer',
									id: 'workplan-progress-management-worklink-status-legend',
									hidden: true,
									width: 120,
									padding: '0 0 0 15',
									style: 'border-right: 1px solid #ccc',
									layout: {
										type: 'vbox',
										pack: 'center'
									},
									defaults: {
										margin: '0 0 10 0'
									},
									items: [
										{
											xtype: 'container',
											html: '<div style="width: 100%;" data-qtip="The current step of the workplan"><div class="wp-step-lengend current-step"></div>&nbsp;<strong>Current Step</strong></div>'
										},
										{
											xtype: 'container',
											html: '<div style="width: 100%;" data-qtip="This step has been completed or needs to be completed"><div class="wp-step-lengend wp-step"></div>&nbsp;<strong>Step</strong></div>'
										}
									]
								},
								{
									// the container will be populated when an item is selected
									xtype: 'container',
									itemId: 'workplanStatusContainer',
									id: 'workplan-progress-management-worklink-status',
									layout: {
										type: 'hbox',
										pack: 'center',
										scrollable: 'x',
										height: '100%',
										padding: '0 30 20 20',
										cls: 'step-container',
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
										actionWorkAndProcessComponent(record);
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Action',
									id: 'lookupGrid-tools-action',
									scale: 'medium',
									disabled: false,
									requiredPermissions: ['WORKFLOW-LINK-ASSIGN', 'WORKFLOW-LINK-ASSIGN-ANY'],
									menu: [
										{
											text: 'Assign To Admin',
											id: 'lookupGrid-tools-action-admin-assign',
											disabled: true,
											iconCls: 'fa fa-lg fa-user-md icon-small-vertical-correction icon-button-color-default',
											requiredPermissions: ['WORKFLOW-LINK-ASSIGN'],
											handler: function(){
												actionAssignToAdmin();
											}
										},
										{
											text: 'Assign To Me',
											id: 'lookupGrid-tools-action-me-assign',
											disabled: true,
											iconCls: 'fa fa-lg fas fa-user-circle icon-small-vertical-correction icon-button-color-default',
											requiredPermissions: ['WORKFLOW-LINK-ASSIGN'],
											handler: function(){
												actionAssignToMe();
											}
										},
										{
											text: 'Unassign',
											id: 'lookupGrid-tools-action-unassign',
											disabled: true,
											iconCls: 'fa fa-lg fas fa-user-times icon-small-vertical-correction icon-button-color-default',
											requiredPermissions: ['WORKFLOW-LINK-ASSIGN'],
											handler: function () {
												actionUnassign();
											}
										},
										{
											xtype: 'menuseparator',
											requiredPermissions: ['WORKFLOW-LINK-ASSIGN-ANY']
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
						}
					],
					listeners: {
						selectionchange: function(selectionModel, records, opts){
							checkGridTools();
							var record = linkGrid.getSelection()[0];
							var steps = record.get('steps');
							var currentStep = record.get('currentStep');
							var statusCmp = Ext.getCmp('workplan-progress-management-worklink-status');
							var statusLegendCmp = Ext.getCmp('workplan-progress-management-worklink-status-legend');
							statusLegendCmp.setVisible(true);
							statusCmp.removeAll();
							Ext.Array.forEach(steps, function (el, index) {
								statusCmp.add({
									xtype: 'container',
									html:	'<div class="step-view-container ' + (index === steps.length - 1 ? 'last-step' : ' ') + '">' + 
												'<span class="wp-step-label ' + (index === steps.length - 1 ? 'last-step' : ' ') + '">' + el.name + '</span>' +
												'<div data-qtip="' + el.description + '"' + 
												'class="step-view ' + 
												(el.workPlanStepId === currentStep.workPlanStepId ? ' current-step ' : ' wp-step ') +
												(index === steps.length - 1 ? ' last-step ' : ' ') +
												'"></div>' +
											'</div>'
								});
							});
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
					var comp_id = Ext.getCmp('linkGrid').getSelection()[0].data.componentId;
					componentViewWin.down('[itemId=commentPanel]').loadComponentComments(comp_id);
					previewContents.load('view.jsp?fullPage=true&embedded=true&hideSecurityBanner=true&id=' + comp_id);
				};
				
				var actionWorkAndProcessComponent = function(record){
					
					var workPlanId = record.get('workPlanId');
					var workPlanLinkId = record.get('workPlanLinkId');
										
					var processCompWin = Ext.create('Ext.window.Window', {				
						title: 'Workflow',
						iconCls: 'fa fa-lg fa-exchange',
						width: '50%',
						height: 400,
						closeAction: 'destroy',
						layout: 'fit',						
						modal: true,
						items: [
							{
								xtype: 'panel',
								itemId: 'stepInfo',
								width: '100%',
								scrollable: true,
								bodyStyle: 'padding: 10px;',
								tpl: new Ext.XTemplate(
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
						callback: function() {
							// processCompWin.setLoading(false);
						},
						success: function(response, opt) {
							actionRefreshComponentGrid();
							// Ext.toast('Successfully Updated Wor');
							// processCompWin.close();
						}												
					});
					// console.log('AssignToMe', 'api/v1/resource/workplans/{workPlanId}/worklinks/{workLinkId}/assign');	
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
						height: 250,						
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
										storeConfig: {
											url: 'api/v1/resource/securityroles/lookup'
										},
										listeners: {
											change: function(filter, newValue, oldValue, opts){
												userAssignWin.queryById('assignUserId').getStore().load({
													url: 'api/v1/resource/securityroles/'+ encodeURIComponent(newValue) +'/users',
													callback: function(){
														userAssignWin.queryById('assignUserId').setDisabled(false);
														userAssignWin.queryById('assignUserId-clear').setDisabled(false);														
													}
												});
											}
										}
									}),
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
												fieldLabel: 'Assign User',
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
