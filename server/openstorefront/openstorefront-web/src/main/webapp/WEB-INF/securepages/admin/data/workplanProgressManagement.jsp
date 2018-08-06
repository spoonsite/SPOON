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


				var userAssignWin = Ext.create('Ext.window.Window', {
					id: 'userAssignWin',
					title: 'Assign entry to user:',
					iconCls: 'fa fa-lg fa-exchange',
					width: '50%',
					height: 450,
					y: 200,
					modal: true,
					layout: 'fit'
				});

				var assignToAdminCommentWin = Ext.create('Ext.window.Window', {
					id: 'AssignToAdminCommentWin',
					title: 'Assign entry to Admin:',
					iconCls: 'fa fa-lg fa-exchange',
						width: '50%',
					height: '75%',
					y: 200,
					modal: true,
					layout: 'fit',
					items: [
						{
							xtype: 'panel',
							id: 'workflowCommentsAssignAdmin',
							title: 'Workflow Comments',
							iconCls: 'fa fa-lg fa-comment',	
							bodyStyle: 'background: white;',
							layout: 'fit',
							items: [
								{
									xtype: 'panel',
									itemId: 'comments',
									bodyStyle: 'padding: 10px;',
									scrollable: true,
									items: [						
									],
									dockedItems: [
										{
											xtype: 'form',
											itemId: 'form',
											dock: 'bottom',
											layout: 'anchor',
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
													layout: {
														vertical: true,
														type: 'hbox',
														align: 'stretch'
													},
													items: [
														{
															xtype: 'fieldcontainer',
															fieldLabel: 'Private',
															defaultType: 'checkboxfield',
															items: [
																{
																	inputValue: '1',
																	id        : 'checkbox2'
																}
															]
														},
														{
															xtype: 'toolbar',
															items: [
																{
																	text: 'Assign to Admin',
																	iconCls: 'fa fa-lg fa-comment icon-button-color-save',
																	handler: function(){													
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
										}
									]
								}				
							],
							listeners: {
								afterrender: function () {
								}
							}
						}

					]					
				
				});

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
							title: 'Main Content',
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
										},
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
							region: 'east'
						}
					]
				});


				var linkGrid = Ext.create('Ext.grid.Panel', {
					title: 'Work Plan Progress Management <i class="fa fa-lg fa-question-circle"  data-qtip="This tool gives the ability to review records in a work plan" ></i>',
					id: 'linkGrid',
					store: {
						autoLoad: true,					
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
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/workplans/worklinks',						
							reader: {
							   type: 'json'
							}
						}
					},
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
									emptyText: 'All',
									fieldLabel: 'Assignment Status',
									name: 'assignmentStatus',
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
													code: 'A',
													description: 'Assigned'
												},
												{
													code: 'U',
													description: 'Unassigned'
												},
												{
													code: 'ALL',
													description: 'All'
												}
											]
										}
									}
								})
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
											iconCls: 'fa fa-lg fa-user icon-small-vertical-correction icon-button-color-default',
											requiredPermissions: ['WORKFLOW-LINK-ASSIGN', 'WORKFLOW-LINK-ASSIGN-ANY'],
											handler: function(){
												actionAssignToAdmin();
											}
										},
										{
											text: 'Assign To Me',
											id: 'lookupGrid-tools-action-me-assign',
											disabled: true,
											iconCls: 'fa fa-lg fas fa-wrench icon-small-vertical-correction icon-button-color-default',
											requiredPermissions: ['WORKFLOW-LINK-ASSIGN', 'WORKFLOW-LINK-ASSIGN-ANY'],
											handler: function(){
												actionAssignToMe();
											}
										},
										{
											text: 'Unassign',
											id: 'lookupGrid-tools-action-unassign',
											disabled: true,
											iconCls: 'fa fa-lg fas fa-undo icon-small-vertical-correction icon-button-color-default',
											requiredPermissions: ['WORKFLOW-LINK-ASSIGN', 'WORKFLOW-LINK-ASSIGN-ANY'],
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
											iconCls: 'fa fa-lg fa-exchange icon-small-vertical-correction',											
											requiredPermissions: ['WORKFLOW-LINK-ASSIGN-ANY'],
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
						}
					}
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

				var actionRefreshComponentGrid = function(input){
					Ext.getCmp('linkGrid').getStore().load();
				};
								
				var actionViewComponent = function(){
					console.log('view the thang');	
					// Ext.getCmp('componentViewWin').show();

					componentViewWin.show();
					var comp_id = Ext.getCmp('linkGrid').getSelection()[0].data.component.componentId;
					// console.log(comp_id);
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
					console.log('AssignToAdmin');
					Ext.getCmp('AssignToAdminCommentWin').show();	
				};
				
				var actionAssignToMe = function(){
					console.log('AssignToMe');	
				};
				
				var actionUnassign = function(){
					console.log('UnAssign');	
				};
				
				var actionReassign = function(){
					console.log('Reassign');	
					Ext.getCmp('userAssignWin').show();
				};

			});

		</script>

	</stripes:layout-component>
</stripes:layout-render>
