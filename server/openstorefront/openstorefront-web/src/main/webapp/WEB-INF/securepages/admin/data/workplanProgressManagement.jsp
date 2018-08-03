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
			Ext.onReady(function() {

				var versionViewTemplate = new Ext.XTemplate();

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

				var AssignToAdminCommentWin = Ext.create('Ext.window.Window', {
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
							xtype: 'panel',
							id: 'workflowComments',
							title: 'Workflow Comments',
							iconCls: 'fa fa-lg fa-comment',	
							region:'east',
							floatable: false,
							margin: '0 0 0 0',
							collapsed: true,
							collapsible: true,
							animCollapse: false,
							titleCollapse: true,
							width: 300,
							minWidth: 100,
							maxWidth: 650,	
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
																	id        : 'checkbox1'
																}
															]
														},
														{
															xtype: 'toolbar',
															items: [
																{
																	text: 'Comment',
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
																	handler: function(){																						
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

				var processCompWin = Ext.create('Ext.window.Window', {
					id: 'processCompWin',
					title: 'Entry Workflow',
					iconCls: 'fa fa-lg fa-exchange',
					width: '50%',
					height: '75%',
					layout: 'anchor',
					modal: true,
					items: [
						{
							xtype: 'panel',
							title: 'This is some step in the workplan of the thing.'
						},
						Ext.create('OSF.component.StandardComboBox', {
							name: 'Sub-Status',									
							allowBlank: true,
							editable: false,
							typeAhead: false,
							height: 60,
							fieldLabel: 'Sub-Status',
							storeConfig: {
								url: 'api/v1/resource/lookuptypes/WorkPlanSubStatusType'
							},
						}),
						{
							xtype: 'form',
							title: 'Step insts for completion pulled from a query'
						},
					],
					dockedItems:[
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
									xtype: 'toolbar',
									items: [
										{
											text: 'Go To Previous Step',
											iconCls: 'fa fa-lg fa-backward icon-button-color-save',
											handler: function(){													
											}
										},
										{
											xtype: 'tbfill'
										},
										{
											text: 'Complete This Step',											
											iconCls: 'fa fa-lg fa-list-alt icon-button-color-save',
											handler: function(){																						
											}
										}
									]
								}


							]
						}
					]
				});

				var maingridStore = Ext.create('Ext.data.Store', {
					autoLoad: true,
					pageSize: 300,
					remoteSort: true,
					sorters: [
						new Ext.util.Sorter({
							property : 'name',
							direction: 'ASC'
						})
					],
					fields:[
						{name: 'name', mapping: function(data){
							return data.component.name;
						}}
					],
					proxy: CoreUtil.pagingProxy({
						url: 'api/v1/resource/components/filterable',
						extraParams: {
							status: 'ALL',
							approvalState: 'ALL',
							componentType: 'ALL'
						},
						reader: {
						   type: 'json',
						   rootProperty: 'components',
						   totalProperty: 'totalNumber'
						}
					}),
				});

				var componentGrid = Ext.create('Ext.grid.Panel', {
					title: 'Work Plan Progress Management <i class="fa fa-lg fa-question-circle"  data-qtip="This tool gives the ability to review records in a work plan" ></i>',
					id: 'workPlanLinkGrid',
					store: maingridStore,
					columnLines: true,
					bodyCls: 'border_accent',
					// viewConfig: {
					// 	enableTextSelection: true
					// },					
					// selModel: {
					// 	   selType: 'checkboxmodel'
					// },
					// plugins: 'gridfilters',
					columns: [
                       	{ text: 'Name', dataIndex: 'name', flex: 1 }, 
                        { text: 'Current Workflow step', flex: 1 },
						{ text: 'Sub Status', flex: 1 },
						{ text: 'Assignee', flex: 1, sortable: true },
						{ text: 'Type', dataIndex: 'componentType', flex: 1,
                     		renderer: function (value, meta, record) {
								return record.get('componentTypeLabel');
							}
						}, 
						{ text: 'State', flex: 1, hidden: false }
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
								}),
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
										actionWorkAndProcessComponent();
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
						itemdblclick: function(grid, record, item, index, e, opts){
							// actionAddEditComponent(record);
                            console.log('you double clicked')
						},
						selectionchange: function(selectionModel, records, opts){
							checkComponetGridTools();
						}
					},
					bbar: Ext.create('Ext.PagingToolbar', {
						store: maingridStore,
						displayInfo: true,
						displayMsg: 'Displaying Entries {0} - {1} of {2}',
						emptyMsg: "No entries to display"
					})
				});

				addComponentToMainViewPort(componentGrid);

				var checkComponetGridTools = function() {

					if (componentGrid.getSelectionModel().getCount() === 1) {
						console.log('you selected one!!!!');
						Ext.getCmp('lookupGrid-tools-preview').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-work-process').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-action-admin-assign').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-action-me-assign').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-action-unassign').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-action-reassign').setDisabled(false);

					}
					else if (componentGrid.getSelectionModel().getCount() > 1) {
						console.log('more than one!!!');

						Ext.getCmp('lookupGrid-tools-preview').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-work-process').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-action-admin-assign').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-action-me-assign').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-action-unassign').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-action-reassign').setDisabled(true);

					}
					else {

						Ext.getCmp('lookupGrid-tools-preview').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-work-process').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-action-admin-assign').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-action-me-assign').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-action-unassign').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-action-reassign').setDisabled(true);

					}
				};

				var actionRefreshComponentGrid = function(input){
					Ext.getCmp('workPlanLinkGrid').getStore().load();
				}
				var actionViewComponent = function(){
					console.log('view the thang');	
					// Ext.getCmp('componentViewWin').show();

					componentViewWin.show();
					var comp_id = Ext.getCmp('workPlanLinkGrid').getSelection()[0].data.component.componentId;
					// console.log(comp_id);
					previewContents.load('view.jsp?fullPage=true&embedded=true&hideSecurityBanner=true&id=' + comp_id);
				}
				var actionWorkAndProcessComponent = function(){
					console.log('do the work');	
					Ext.getCmp('processCompWin').show();
				}
				var actionAssignToAdmin= function(){
					console.log('AssignToAdmin');
					Ext.getCmp('AssignToAdminCommentWin').show();	
				}
				var actionAssignToMe = function(){
					console.log('AssignToMe');	
				}
				var actionUnassign = function(){
					console.log('UnAssign');	
				}
				var actionReassign = function(){
					console.log('Reassign');	
					Ext.getCmp('userAssignWin').show();
				}

			});

		</script>

	</stripes:layout-component>
</stripes:layout-render>
