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

<stripes:layout-render name="../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">

		<stripes:layout-render name="../../../layout/userheader.jsp">
		</stripes:layout-render>

		
		<script src="scripts/component/messageWindow.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/integrationConfigWindow.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/submissionPanel.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/entryChangeRequestWindow.js?v=${appVersion}" type="text/javascript"></script>

		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function() {

				//MAIN GRID -------------->
				var versionViewTemplate = new Ext.XTemplate();

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
						// {name: 'name', mapping: function(data){
						// 	return data.component.name;
						// }},
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
					]
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
									// requiredPermissions: ['ADMIN-ENTRY-READ'],
                                    // STUB IN PERMISSION HERE
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
									// requiredPermissions: ['ADMIN-ENTRY-READ'],
                                    // STUB IN PERMISSION HERE
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
									requiredPermissions: [
                                        // STUB IN REQUIRED PERMISSIONS FOR ACTION HERE
										// 'ADMIN-ENTRY-CHANGEOWNER',
										// 'ADMIN-ENTRY-CHANGETYPE',
										// 'ADMIN-ENTRY-CHANGEREQUEST-MANAGEMENT',
										// 'ADMIN-ENTRY-APPROVE',
										// 'ADMIN-ENTRY-CREATE',
										// 'ADMIN-ENTRY-MERGE',
										// 'ADMIN-ENTRY-VERSION-READ',
										// 'ADMIN-ENTRY-UPDATE',
										// 'ADMIN-ENTRY-DELETE'
									],
									menu: [
										{
											text: 'Assign To Admin',
											iconCls: 'fa fa-lg fa-user icon-small-vertical-correction icon-button-color-default',
                                            // STUB IN PERMISSION HERE
											// requiredPermissions: ['ADMIN-ENTRY-CHANGEOWNER'],
											handler: function(){
												actionAssignToAdmin();
											}
										},
										{
											text: 'Assign To Me',
											iconCls: 'fa fa-lg fa-exchange icon-small-vertical-correction icon-button-color-default',
											// requiredPermissions: ['ADMIN-ENTRY-CHANGETYPE'],
                                            // STUB IN PERMISSION HERE!
											handler: function(){
												actionAssignToMe();
											}
										},
										{
											text: 'Unassign',
											iconCls: 'fa fa-lg fa-edit icon-small-vertical-correction icon-button-color-default',
											// requiredPermissions: ['ADMIN-ENTRY-CHANGEREQUEST-MANAGEMENT'],
                                            // STUB IN PERMISSION HERE
											handler: function () {
												actionUnassign();
											}
										},
										{
											xtype: 'menuseparator',
											requiredPermissions: ['ADMIN-ENTRY-CREATE', 'ADMIN-ENTRY-MERGE', 'ADMIN-ENTRY-VERSION-READ']
										},                                       
										{
											text: 'Reassign',
											iconCls: 'fa fa-lg fa-share icon-small-vertical-correction',											
											// requiredPermissions: ['ADMIN-ENTRY-APPROVE'],
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
				}
				var actionWorkAndProcessComponent = function(){
					console.log('do the work');	
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
				}

			});

		</script>

	</stripes:layout-component>
</stripes:layout-render>
