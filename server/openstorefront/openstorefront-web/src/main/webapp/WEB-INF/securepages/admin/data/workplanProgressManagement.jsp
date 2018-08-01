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
--%>
<%@include file="../../../../layout/includes.jsp" %>

<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">

		<stripes:layout-render name="../../../../layout/adminheader.jsp">
		</stripes:layout-render>

		
		<!-- <script src="scripts/component/messageWindow.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/integrationConfigWindow.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/submissionPanel.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/entryChangeRequestWindow.js?v=${appVersion}" type="text/javascript"></script>


		<script src="scripts/component/workPlanProgressComment.js?v=${appVersion}" type="text/javascript"></script> -->

		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.require('OSF.common.workPlanProgressComment');
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

				// var commentPanel = Ext.create('Ext.panel.Panel', {
				// 	title: 'Comments',
				// 	iconCls: 'fa fa-lg fa-comment',
				// 	region: 'east',			
				// 	collapsed: true,
				// 	collapsible: true,
				// 	animCollapse: false,
				// 	floatable: false,
				// 	titleCollapse: true,
				// 	width: 375,
				// 	minWidth: 250,
				// 	split: true,			
				// 	bodyStyle: 'background: white;',
				// 	layout: 'fit',
				// 	items: [
				// 		{
				// 			xtype: 'panel',
				// 			itemId: 'comments',
				// 			bodyStyle: 'padding: 10px;',
				// 			scrollable: true,
				// 			items: [						
				// 			],
				// 			dockedItems: [
				// 				{
				// 					xtype: 'form',
				// 					itemId: 'form',
				// 					dock: 'bottom',
				// 					layout: 'anchor',
				// 					items: [
				// 						{
				// 							xtype: 'hidden',
				// 							name: 'commentId'
				// 						},
				// 						{
				// 							xtype: 'hidden',
				// 							name: 'replyCommentId'
				// 						},
				// 						{
				// 							xtype: 'htmleditor',
				// 							name: 'comment',									
				// 							width: '100%',
				// 							fieldBodyCls: 'form-comp-htmleditor-border',
				// 							maxLength: 4000
				// 						}
				// 					],
				// 					dockedItems: [
				// 						{
				// 							xtype: 'toolbar',
				// 							dock: 'bottom',
				// 							items: [
				// 								{
				// 									text: 'Comment',
				// 									iconCls: 'fa fa-lg fa-comment icon-button-color-save',
				// 									handler: function(){
				// 										// var form = this.up('form');
				// 										// var data = form.getValues();
				// 										// data.acknowledge = false;
														
				// 										// var method = 'POST';
				// 										// var update = '';		
				// 										// if (data.commentId) {
				// 										// 	method = 'PUT',
				// 										// 	update = '/' + data.commentId;		
				// 										// }
				// 										// var evaluationId = rootEvalPanel.commentPanel.lastLoadOpt.evaluationId;
				// 										// var entity = rootEvalPanel.commentPanel.lastLoadOpt.entity;
				// 										// var entityId = rootEvalPanel.commentPanel.lastLoadOpt.entityId;
				// 										// if (!entity) {
				// 										// 	data.entity = 'Evaluation';
				// 										// 	data.entityId = evaluationId;	
				// 										// } else {
				// 										// 	data.entity = entity;
				// 										// 	data.entityId = entityId;
				// 										// }
														
				// 										// CoreUtil.submitForm({
				// 										// 	url: 'api/v1/resource/evaluations/' + evaluationId + '/comments' + update,
				// 										// 	method: method,
				// 										// 	data: data,
				// 										// 	form: form,
				// 										// 	success: function(){
				// 										// 		rootEvalPanel.commentPanel.loadComments(evaluationId, entity, entityId);														
				// 										// 		form.reset();
																
				// 										// 		if (rootEvalPanel.commentPanel.getComponent('comments').replyMessage) {
				// 										// 			rootEvalPanel.commentPanel.getComponent('comments').removeDocked(rootEvalPanel.commentPanel.getComponent('comments').replyMessage, true);
				// 										// 			rootEvalPanel.commentPanel.getComponent('comments').replyMessage = null;
				// 										// 		}
				// 										// 		if (rootEvalPanel.commentPanel.getComponent('comments').editMessage) {
				// 										// 			rootEvalPanel.commentPanel.getComponent('comments').removeDocked(rootEvalPanel.commentPanel.getComponent('comments').editMessage, true);
				// 										// 			rootEvalPanel.commentPanel.getComponent('comments').editMessage = null;
				// 										// 		}														
				// 										// 	}
				// 										// });												
														
				// 									}
				// 								},
				// 								{
				// 									xtype: 'tbfill'
				// 								},
				// 								{
				// 									text: 'Cancel',
				// 									itemId: 'cancel',											
				// 									iconCls: 'fa fa-lg fa-close icon-button-color-warning',
				// 									handler: function(){										
				// 										// var form = this.up('form');
				// 										// form.reset();
				// 										// if (rootEvalPanel.commentPanel.getComponent('comments').replyMessage) {
				// 										// 	rootEvalPanel.commentPanel.getComponent('comments').removeDocked(rootEvalPanel.commentPanel.getComponent('comments').replyMessage, true);
				// 										// 	rootEvalPanel.commentPanel.getComponent('comments').replyMessage = null;
				// 										// }
				// 										// if (rootEvalPanel.commentPanel.getComponent('comments').editMessage) {
				// 										// 	rootEvalPanel.commentPanel.getComponent('comments').removeDocked(rootEvalPanel.commentPanel.getComponent('comments').editMessage, true);
				// 										// 	rootEvalPanel.commentPanel.getComponent('comments').editMessage = null;
				// 										// }												
				// 									}
				// 								}
				// 							]
				// 						}
				// 					]
				// 				}
				// 			]
				// 		}				
				// 	],
				// 	listeners: {
				// 		afterrender: function () {

				// 			// if (rootEvalPanel.readOnly) {
				// 			// 	var subCommentPanel = rootEvalPanel.query('[itemId=comments]')[0];

				// 			// 	Ext.Array.forEach(subCommentPanel.query('panel'), function (el) {
				// 			// 		el.setStyle('pointer-events', 'none');
				// 			// 	});
				// 			// 	Ext.Array.forEach(subCommentPanel.query('htmleditor'), function (el) {
				// 			// 		el.setDisabled(true);
				// 			// 		el.setVisible(false);
				// 			// 	});
				// 			// 	Ext.Array.forEach(subCommentPanel.query('button'), function (el) {
				// 			// 		el.setDisabled(true);
				// 			// 		el.setVisible(false);
				// 			// 	});
				// 			// }
				// 		}
				// 	}
				// });

				var previewContents = Ext.create('OSF.ux.IFrame', {
					src: ''
				});

				var componentViewWin = Ext.create('Ext.window.Window', {
					id: 'componentViewWin',
					title: 'Entry Details',
					iconCls: 'fa fa-lg fa-exchange',
					maximizable: true,
					// bodyBorder: true,
					// frame: true,
					width: '75%',
					height: '75%',
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
							// frame: true,
							// margins: '5 0 0 0',
							margins: '0 0 0 0',
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
							// margins: '5 0 0 0',
							margins: '0 0 0 0',

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
					height: 450,
					y: 200,
					modal: true,
					layout: 'fit'
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
						}},
						// {name: 'description', mapping: function(data){
						// 	return data.component.description;
						// }},
						// {name: 'activeStatus', mapping: function(data){
						// 	return data.component.activeStatus;
						// }},
						// {name: 'approvalState', mapping: function(data){
						// 	return data.component.approvalState;
						// }},
						// {name: 'approvalStateLabel', mapping: function(data){
						// 	return data.component.approvalStateLabel;
						// }},
						// {name: 'approvedDts',
						// 	type:	'date',
						// 	dateFormat: 'c',
						// 	mapping: function(data){
						// 	return data.component.approvedDts;
						// }},
						// {name: 'approvedUser', mapping: function(data){
						// 	return data.component.approvedUser;
						// }},
						// {name: 'componentId', mapping: function(data){
						// 	return data.component.componentId;
						// }},
						// {name: 'componentType', mapping: function(data){
						// 	return data.component.componentType;
						// }},
						// {name: 'componentTypeLabel', mapping: function(data){
						// 	return data.component.componentTypeLabel;
						// }},
						// {name: 'organization', mapping: function(data){
						// 	return data.component.organization;
						// }},
						// {name: 'createDts',
						// 	type:	'date',
						// 	dateFormat: 'c',
						// 	mapping: function(data){
						// 	return data.component.createDts;
						// }},
						// {name: 'createUser', mapping: function(data){
						// 	return data.component.createUser;
						// }},
						// {name: 'lastActivityDts',
						// 	type:	'date',
						// 	dateFormat: 'c',
						// 	mapping: function(data){
						// 	return data.component.lastActivityDts;
						// }},
						// {name: 'guid', mapping: function(data){
						// 	return data.component.guid;
						// }},
						// {name: 'externalId', mapping: function(data){
						// 	return data.component.externalId;
						// }},
						// {name: 'changeApprovalMode', mapping: function(data){
						// 	return data.component.changeApprovalMode;
						// }},
						// {name: 'notifyOfApprovalEmail', mapping: function(data){
						// 	return data.component.notifyOfApprovalEmail;
						// }},					
						// {name: 'currentDataOwner', mapping: function(data){
						// 	return data.component.currentDataOwner;
						// }},					
						// {name: 'dataSource', mapping: function(data){
						// 	return data.component.dataSource;
						// }},
						// {name: 'dataSensitivity', mapping: function(data){
						// 	return data.component.dataSensitivity;
						// }},
						// {name: 'securityMarkingType', mapping: function(data){
						// 	return data.component.securityMarkingType;
						// }},
						// {name: 'securityMarkingDescription', mapping: function(data){
						// 	return data.component.securityMarkingDescription;
						// }},
						// {name: 'lastModificationType', mapping: function(data){
						// 	return data.component.lastModificationType;
						// }},
						// {name: 'fileHistoryId', mapping: function(data){
						// 	return data.component.fileHistoryId;
						// }},
						// {name: 'recordVersion', mapping: function(data){
						// 	return data.component.recordVersion;
						// }},
						// {name: 'submittedDts',
						// 	type:	'date',
						// 	dateFormat: 'c',
						// 	mapping: function(data){
						// 	return data.component.submittedDts;
						// }},
						// {name: 'updateDts',
						// 	type:	'date',
						// 	dateFormat: 'c',
						// 	mapping: function(data){
						// 	return data.component.updateDts;
						// }},
						// {name: 'updateUser', mapping: function(data){
						// 	return data.component.updateUser;
						// }},
						// {name: 'numberOfPendingChanges', mapping: function(data){
						// 	return data.component.numberOfPendingChanges;
						// }},
						// 'integrationManagement'
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
					title: 'Manage Entries <i class="fa fa-lg fa-question-circle"  data-qtip="This tool allows for manipulating all data related to an entry" ></i>',
					id: 'componentGrid',
					store: maingridStore,
					columnLines: true,
					bodyCls: 'border_accent',
					viewConfig: {
						enableTextSelection: true
					},					
					selModel: {
						   selType: 'checkboxmodel'
					},
					plugins: 'gridfilters',
					columns: [
                       	{ text: 'Name', dataIndex: 'name', width: 130,
							filter: {
								type: 'string'
							}
						}, 
                        { text: 'Current Workflow step', width: 150 },
						{ text: 'Sub Status', align: 'center', width: 125 },
						{ text: 'Assignee', width: 175, sortable: true },
						{ text: 'Type', align: 'center', dataIndex: 'componentType', width: 125,
							filter: {
								type: 'list'
							},
                     		renderer: function (value, meta, record) {
								return record.get('componentTypeLabel');
							}
						}, 
						{ text: 'State', width: 175, hidden: false }
						// { text: 'Name', dataIndex: 'name', width: 275, minWidth: 350 , flex: 150,
						// 	filter: {
						// 		type: 'string'
						// 	}
						// },
						// { text: 'Type', align: 'center', dataIndex: 'componentType', width: 125,
						// 	filter: {
						// 		type: 'list'
						// 	},
						// 	renderer: function (value, meta, record) {
						// 		return record.get('componentTypeLabel');
						// 	}
						// },
						// { text: 'Description', dataIndex: 'description', flex: 1, minWidth: 150, hidden:true,
						//  renderer: function(value){
						// 	return Ext.util.Format.stripTags(value);
						// }},
						// { text: 'Pending Changes', tooltip: 'See Action->Change Requests to view', align: 'center', dataIndex: 'numberOfPendingChanges', width: 150 },
						// { text: 'Last Activity Date', dataIndex: 'lastActivityDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						// { text: 'Submitted Date', dataIndex: 'submittedDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						// { text: 'Approval State', align: 'center', dataIndex: 'approvalState', width: 125,
						// 	renderer: function (value, meta, record) {
						// 		return record.get('approvalStateLabel');
						// 	}
						// },
						// { text: 'Approval Date', dataIndex: 'approvedDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						// { text: 'Active Status', align: 'center', dataIndex: 'activeStatus', width: 125 },
						// { text: 'Integration Management', dataIndex: 'integrationManagement', width: 175, sortable: false },
						// { text: 'Current Owner', dataIndex: 'currentDataOwner', width: 175, sortable: false },
						// { text: 'Update Date', dataIndex: 'updateDts', width: 175, hidden: true, xtype: 'datecolumn', format:'m/d/y H:i:s'},
						// { text: 'Update User', dataIndex: 'updateUser', width: 175, hidden: true },
						// { text: 'Create Date', dataIndex: 'createDts', width: 175, hidden: true, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						// { text: 'Create User', dataIndex: 'createUser', width: 175, hidden: true },
						// { text: 'Component Id', dataIndex: 'componentId', width: 175, hidden: true },
						// { text: 'Security Marking', dataIndex: 'securityMarkingDescription', width: 175, hidden: true, sortable: false }
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
									disabled: false,
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
									width: '100px',
									iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction-view',
									disabled: false,
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
											iconCls: 'fa fa-lg fa-user icon-small-vertical-correction icon-button-color-default',
                                            // STUB IN PERMISSION HERE
											requiredPermissions: ['WORKFLOW-LINK-ASSIGN', 'WORKFLOW-LINK-ASSIGN-ANY'],
											handler: function(){
												actionAssignToAdmin();
											}
										},
										{
											text: 'Assign To Me',
											iconCls: 'fa fa-lg fa-exchange icon-small-vertical-correction icon-button-color-default',
											requiredPermissions: ['WORKFLOW-LINK-ASSIGN', 'WORKFLOW-LINK-ASSIGN-ANY'],
                                            // STUB IN PERMISSION HERE!
											handler: function(){
												actionAssignToMe();
											}
										},
										{
											text: 'Unassign',
											iconCls: 'fa fa-lg fa-edit icon-small-vertical-correction icon-button-color-default',
											requiredPermissions: ['WORKFLOW-LINK-ASSIGN', 'WORKFLOW-LINK-ASSIGN-ANY'],
                                            // STUB IN PERMISSION HERE
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
											iconCls: 'fa fa-lg fa-share icon-small-vertical-correction',											
											requiredPermissions: ['WORKFLOW-LINK-ASSIGN-ANY'],
                                            // STUB IN PERMISSION HERE
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
							// checkComponetGridTools();
                            console.log('you made a selection that does nothing!')
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

				var actionRefreshComponentGrid = function(input){
					console.log('You refreshed the screen with input: ' + input);	
				}
				var actionViewComponent = function(){
					console.log('view the thang');	
					// Ext.getCmp('componentViewWin').show();

					componentViewWin.show();
					var comp_id = Ext.getCmp('componentGrid').getSelection()[0].data.component.componentId;
					// console.log(comp_id);
					previewContents.load('view.jsp?fullPage=true&embedded=true&hideSecurityBanner=true&id=' + comp_id);
				}
				var actionWorkAndProcessComponent = function(){
					console.log('do the work');	
					Ext.getCmp('processCompWin').show();
				}
				var actionAssignToAdmin= function(){
					console.log('AssignToAdmin');	
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
