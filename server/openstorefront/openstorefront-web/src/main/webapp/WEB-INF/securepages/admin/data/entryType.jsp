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
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
	<stripes:layout-component name="contents">
		
		<stripes:layout-render name="../../../../layout/adminheader.jsp">		
		</stripes:layout-render>	

		<script src="scripts/plugin/cellToCellDragDrop.js?v=${appVersion}" type="text/javascript"></script>		
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */			
			Ext.require('OSF.tool.EntryTypeAttributes');
			Ext.require('OSF.tool.EntryTypeEntryAssignment');
			
			Ext.onReady(function(){	
				
				window.entryTypeAssignedRender = function (data, type) {
					data = data.split(',');
					var windowConfig = {
						modal: true,
						minWidth: 200,
						closeAction: 'destroy',
						listeners:	{
							show: function() {        
								this.removeCls("x-unselectable");    
							}
						},
						title: type === 'roles' ? 'Assigned Groups' : 'Assigned Users'
					};

					if (type === 'roles') {
						Ext.Array.forEach(data, function (el, index) {
							data[index] = {html: el, xtype: 'container', padding: 15};
						});
						Ext.create('Ext.window.Window', Ext.apply({items: data}, windowConfig)).show();
					}
					else {

						Ext.Ajax.request({
							url: 'api/v1/resource/userprofiles/lookup',
							success: function (response) {

								var allUsers = Ext.decode(response.responseText);
								for (var i = 0; i < allUsers.length; i += 1) {
									for (var j = 0; j < data.length; j += 1) {

										if (allUsers[i].code === data[j]) {
											data[j] = {html: allUsers[i].description, padding: 15};
										}
									}
								}

								Ext.create('Ext.window.Window', Ext.apply({items: data}, windowConfig)).show();
							}
						});
					}
				};				
				
				var entryTypeAssignedCellTemplate = new Ext.XTemplate(						
				);

				// load template for assigned group and assigned user cells in the entryGrid...
				//	then add the grid to the view port
				Ext.Ajax.request({
					url: 'Router.action?page=shared/entryTypeAssignedCellTemplate.jsp',
					success: function(response, opts){
						entryTypeAssignedCellTemplate.set(response.responseText, true);
						addComponentToMainViewPort(entryGrid);
					}
				});	

				var mediaWindow = Ext.create('OSF.component.MediaInsertWindow', {

					isEditor: false,
					mediaSelectionUrl: 'api/v1/resource/generalmedia',
					closeAction: 'hide',
					mediaHandler: function(link) {
						Ext.getCmp('iconUrlField').setValue(link);
					}
				});				
				
				
				var addEditWin = Ext.create('Ext.window.Window', {
					id: 'addEditWin',
					iconCls: 'fa fa-lg fa-edit icon-small-vertical-correction',
					title: 'Add/Edit Entry Type',
					modal: true,
					width: '40%',
					height: '90%',
					layout: 'fit',
					items: [
						{
							xtype: 'form',
							id: 'entryForm',
							layout: 'vbox',
							autoScroll: true,
							scrollable: true,							
							bodyStyle: 'padding: 10px;',
							defaults: {
								labelAlign: 'top'
							},
							items: [
								{
									xtype: 'textfield',
									id: 'entryForm-type',
									fieldLabel: 'Type Code<span class="field-required" />',
									name: 'componentType',
									width: '100%',
									allowBlank: false,
									maxLength: 20									
								},
								{
									xtype: 'textfield',
									id: 'entryForm-type-label',
									fieldLabel: 'Label<span class="field-required" />',
									name: 'label',
									allowBlank: false,
									width: '100%',
									maxLength: 80																		
								},
								{
									xtype: 'htmleditor',
									name: 'description',
									id: 'entryForm-type-description',
									fieldLabel: ' Description<span class="field-required" />',
									allowBlank: false,
									width: '100%',
									fieldBodyCls: 'form-comp-htmleditor-border',
									maxLength: 65536
								},																
								Ext.create('OSF.component.StandardComboBox', {
									name: 'parentComponentType',																		
									id: 'parentComponentTypeId',
									width: '100%',
									margin: '0 0 15 0',
									fieldLabel: 'Parent Type',
									emptyText: 'None',
									storeConfig: {
										url: 'api/v1/resource/componenttypes/lookup',
										addRecords: [
											{
												code: null,
												description: 'None'
											} 
										]
									}
								}),
								{
									fieldLabel: 'Assigned User(s)',
									xtype: 'UserMultiSelectComboBox',
									width: '100%',
									name: 'assignedUsers',
									queryMode: 'local'
								},
								{
									fieldLabel: 'Assigned Group(s)',
									xtype: 'RoleGroupMultiSelectComboBox',
									width: '100%',
									margin: '0 0 15 0',
									name: 'assignedGroups'
								},
								{
									xtype: 'container',
									width: '100%',
									html: '<b>Data Entry</b>'
								},
								{
									xtype: 'container',
									layout: 'column',
									width: '100%',
									items: [
										{
											columnWidth: 0.33,
											items: [
												{
													xtype: 'checkbox',
													boxLabel: 'Allow On Submission Form',
													name: 'allowOnSubmission',
													id: 'entryForm-radio-allow-on-sub'
												},								
												{
													xtype: 'checkbox',
													boxLabel: 'Attributes',
													name: 'dataEntryAttributes',
													id: 'entryForm-radio-attributes'
												},
												{
													xtype: 'checkbox',
													boxLabel: 'Relationships ',
													name: 'dataEntryRelationships',
													id: 'entryForm-radio-relationships'									
												},
												{
													xtype: 'checkbox',
													boxLabel: 'Contacts',
													name: 'dataEntryContacts',
													id: 'entryForm-radio-contacts'
												}
											]
										},
										{
											columnWidth: 0.33,
											items: [
												{
													xtype: 'checkbox',
													boxLabel: 'Resources',
													name: 'dataEntryResources',
													id: 'entryForm-radio-resources'
												},
												{
													xtype: 'checkbox',
													boxLabel: 'Media',
													name: 'dataEntryMedia',
													id: 'entryForm-radio-media'
												},
												{
													xtype: 'checkbox',
													boxLabel: 'Dependencies',
													name: 'dataEntryDependencies',
													id: 'entryForm-radio-dependencies'
												},
												{
													xtype: 'checkbox',
													boxLabel: 'Evaluation Information',
													name: 'dataEntryEvaluationInformation',
													id: 'entryForm-radio-eval-info'
												}
											]
										},
										{
											columnWidth: 0.33,
											items: [
												{
													xtype: 'checkbox',
													boxLabel: 'Reviews',
													name: 'dataEntryReviews',
													id: 'entryForm-radio-reviews'
												},
												{
													xtype: 'checkbox',
													boxLabel: 'Questions',
													name: 'dataEntryQuestions',
													id: 'entryForm-radio-questions'
												}
											]
										}
									]
								},								
								Ext.create('OSF.component.StandardComboBox', {
									name: 'componentTypeTemplate',																		
									width: '100%',
									margin: '0 0 20 0',
									fieldLabel: 'Override Template',
									emptyText: 'Default',
									storeConfig: {
										url: 'api/v1/resource/componenttypetemplates/lookup',
										addRecords: [
											{
												code: null,
												description: 'Default'
											} 
										]
									}
								}),
								{
									xtype: 'label',
									text: 'Icon URL:',
									margin: '0 5 0 0',
									style: {
										fontWeight: 'bold'
									}
								},
								{
									layout: 'hbox',
									width: '100%',
									margin: '5px 0 0 0',
									items: [
										{
											xtype: 'textfield',
											id: 'iconUrlField',
											name: 'iconUrl',
											flex: 4
										},
										{
											xtype: 'button',
											text: 'Insert Media',
											flex: 1,
											handler: function() {
												
												mediaWindow.show();
											}
										}
									]
								},								
								{
									margin: '10 0 0 0',
									xtype: 'checkbox',
									width: '100%',
									boxLabel: '<b>Include Icon in Search Results</b>',
									value: false,
									name: 'includeIconInSearch'
								}
							],
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										{
											text: 'Save',
											iconCls: 'fa fa-lg fa-save icon-button-color-save',
											id: 'entryTypeForm-save',
											formBind: true,
											handler: function(){
												var method = Ext.getCmp('entryForm').edit ? 'PUT' : 'POST'; 												
												var data = Ext.getCmp('entryForm').getValues();
												var url = Ext.getCmp('entryForm').edit ? 'api/v1/resource/componenttypes/' + data.componentType : 'api/v1/resource/componenttypes';       

												// reformat data for roleLink and userLink
												if (data.assignedUsers) {
													Ext.Array.forEach(data.assignedUsers, function (el, index) {
														data.assignedUsers[index] = {username: el};
													});
												}
												if (data.assignedGroups) {
													Ext.Array.forEach(data.assignedGroups, function (el, index) {
														data.assignedGroups[index] = {roleName: el};
													});
												}

												CoreUtil.submitForm({
													url: url,
													method: method,
													data: data,
													removeBlankDataItems: true,
													form: Ext.getCmp('entryForm'),
													success: function(response, opts) {
														Ext.toast('Saved Successfully', '', 'tr');
														Ext.getCmp('entryForm').setLoading(false);
														Ext.getCmp('addEditWin').hide();													
														actionRefreshEntryGrid();												
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
												Ext.getCmp('addEditWin').close();
											}											
										}
									]
								}
							]
						}
					]
				});

				var entryGrid = Ext.create('Ext.tree.Panel', {
					rowLines: true,
					columnsLines: true,
					id: 'entryGrid',
					title: 'Entry Types <i class="fa fa-question-circle"  data-qtip="Allows for defining entry types" ></i>',
					viewConfig: {
						plugins: {
							ptype: 'celltocelldragdrop',
							enableDrop: true,
							rowFocus: true,
							onEnter: function (target, dd, e, dragData) {

								dragData.invalidNodes = getInvalidNodes(dragData.record, []);
							},
							onOut: function(target, dd, e, dragData) {

								var originName = dragData.record.getData().componentType.componentType; 
								dd.ddel.innerText = originName;
							},
							onDrop: function (target, dd, e, dragData) {

								if (target.record !== dragData.record && getInvalidNodes(dragData.record).indexOf(target.record) === -1) {

									if (dragData.record.parentNode.childNodes.length === 1) {
										dragData.record.parentNode.data.leaf = true;
									}

									target.record.insertChild(0, dragData.record);
									target.record.expand();

									// save the record
									dragData.record.getData().componentType.parentComponentType = target.record.getData().componentType.componentType;
									saveEntryTypeRecord(dragData.record);
								}
							}
						}
					},
					rootVisible: false,
					store: {
						type: 'tree',
						fields: [{
									'name': 'compID',
								    'mapping': function(data) {
										return data.componentType.componentType;
								    }
								},
								{
									'name': 'label',
								    'mapping': function(data) {
										return data.componentType.label;
									}
								},
								{
									'name': 'description',
									'mapping': function(data) {
										return data.componentType.description;
									}
								},
								{
									'name': 'templateName',
									'mapping': function(data) {
										if (data.componentType.template) {
											return data.componentType.template.templateName;
										}
										return 'Default';
									}
								},
								{
									'name': 'activeStatus',
									'mapping': function(data) {
										return data.componentType.activeStatus;
									}
								},
								{
									'name': 'updateUser',
									'mapping': function(data) {
										return data.componentType.updateUser;
									}
								},
								{
									'name': 'updateDts',
									'mapping': function(data) {
										return data.componentType.updateDts;
									}
								}],
						listeners: {

							load: function (self, records) {

								setChildrenLayout(records, 0);
							}
						},
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/componenttypes/nested',
							extraParams: {
								all: true
							}
						}
					},
					columnLines: true,
					columns: [						
						{ 
							text: 'Label',
							dataIndex: 'label',
							xtype: 'treecolumn',
							flex: 20
						},
						{ 
							text: 'Type Code',
							dataIndex: 'compID',
							flex: 20
						},
						{ 
							text: 'Description',
							dataIndex: 'description',
							flex: 30,
							hidden: true
						},
						{ 
							text: 'Template Override',
							dataIndex: 'templateName',
							flex: 10,
							renderer: function (componentType, rowData) {

								var rowRecord = entryGrid.getStore().getData().items[rowData.rowIndex];

								var templateData = getTemplateData(rowRecord, false);

								if (templateData.cameFromAncestor) {

									var rootComponentType = templateData.rootNode.getData().componentType;
									var iconStyle = 'style="padding: 2px; border-radius: 10px; background: #777; color: #fff;"';
									var iconCls = 'fa fa-sitemap';
									var tip = 'Template inherited from \'<b>' + rootComponentType.componentType + '</b>\'';

									return '<i data-qtip="' + tip + '"><i class="' + iconCls + '" ' + iconStyle + '></i> ' + rootComponentType.template.templateName + '</i>';
								}
								else {
									return templateData.label;
								}
							}
						},
						{ 
							text: 'Assigned User',
							align: 'center',
							sortable: false,
							flex: 15,
							renderer: function (componentType, rowData) {

								var rowRecord = entryGrid.getStore().getData().items[rowData.rowIndex];

								var userData = getAssignedData(rowRecord, false, 'users');

								userData.type = 'users';

								return entryTypeAssignedCellTemplate.apply(userData);
							}
						},
						{ 
							text: 'Assigned Group',
							align: 'center',
							sortable: false,
							flex: 15,
							renderer: function (componentType, rowData) {

								var rowRecord = entryGrid.getStore().getData().items[rowData.rowIndex];

								var groupData = getAssignedData(rowRecord, false, 'roles');

								groupData.type = 'roles';

								return entryTypeAssignedCellTemplate.apply(groupData);
							}
						},
						{ 
							text: 'Active Status',
							align: 'center',
							dataIndex: 'activeStatus',
							flex: 10
						},
						{ 
							text: 'Update User',
							dataIndex: 'updateUser',
							flex: 10
						},
						{ 
							text: 'Update Date',
							dataIndex: 'updateDts',
							flex: 10,
							hidden: true,
							xtype: 'datecolumn',
							format:'m/d/y H:i:s'
						}
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									id: 'entryTypeRefreshBtn',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										actionRefreshEntryGrid();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Add',
									id: 'entryTypeAddBtn',
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction',
									handler: function () {
										actionAddEntry();
									}
								},								
								{
									text: 'Edit',
									id: 'lookupGrid-tools-edit',
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
									disabled: true,
									handler: function () {
										actionEditEntry(Ext.getCmp('entryGrid').getSelection()[0]);
									}								
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Action',
									scale: 'medium',
									menu: [
										{
											text: 'Entry Assignment',
											iconCls: 'fa fa-exchange fa-2x icon-button-color-default',
											handler: function() {												
												actionEntryAssignment();
											}
										},
										{
											text: 'Attribute Assignment',
											iconCls: 'fa fa-list-alt fa-2x icon-button-color-default',
											handler: function() {												
												actionAttributeAssignment();
											}											
										},										
										{
											xtype: 'menuseparator'
										},
										{
											text: 'Move to Top',
											id: 'moveToTopBtn',
											disabled: true,
											iconCls: 'fa fa-level-up fa-2x icon-button-color-default',
											tooltip: 'Moves the selected record to the top-most level',
											handler: function () {
												actionMoveToTop();
											}
										},
										{
											xtype: 'menuseparator'
										},
										{
											text: 'Toggle Status',
											id: 'lookupGrid-tools-status',
											disabled: true,
											iconCls: 'fa fa-power-off fa-2x icon-button-color-default',
											handler: function () {
												actionToggleStatus();
											}	
										}
									]
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									id: 'lookupGrid-tools-remove',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									disabled: true,
									handler: function () {
										actionRemoveType();
									}									
								}
							]
						}
					],
					listeners: {
						beforeitemdblclick: function (grid, record) {
							actionEditEntry(record);
							return false;
						},
						selectionchange: function(grid, record, index, opts){
							checkEntryGridTools();
						}
					}
				});
				
				// recursively gets a list of target nodes/records that can't be dragged to
				var getInvalidNodes = function (currentRecord) {
					invalidNodes = [];

					invalidNodes.push(currentRecord);

					for (var ii = 0; ii < currentRecord.childNodes.length; ii += 1) {
						invalidNodes = invalidNodes.concat(getInvalidNodes(currentRecord.childNodes[ii]));
					}

					return invalidNodes;
				};

				// saves and entry type record, generally this should only be used when saving a record
				//	in a less standard way (e.g. not via a form)
				var saveEntryTypeRecord = function (record) {

					Ext.Ajax.request({
						url: 'api/v1/resource/componenttypes/' + record.getData().componentType.componentType,
						method: 'PUT',
						jsonData: record.getData().componentType
					});
				};

				// formats children tree layout (expands items, and sets items as leafs)
				var setChildrenLayout = function (children) {

					if (children.length === 0) {
						return;
					}

					Ext.Array.forEach(children, function (child) {

						if (child.childNodes.length === 0) {
							child.data.leaf = true;
							child.triggerUIUpdate();
						}
						child.expand();
						setChildrenLayout(child.childNodes);
					});
				};

				// recusively find the parent (or self) that has the root template.
				// NOTE: we cannot simply do this thorugh rowRecord.getData().componentType.template because
				//	we do not want to have to save the record, retrieve the new data, and refresh the grid every
				//	time we drag a record.
				// @returns String
				var getTemplateData = function (node, inherited) {

					if (node.parentNode === null) {
						return {label: '<i style="color: #ccc;">Default</i>', cameFromAncestor: false, rootNode: null};
					}
					if (node.getData().componentType.template && !node.getData().componentType.template.cameFromAncestor) {
						return {label: node.getData().componentType.template.templateName, cameFromAncestor: inherited, rootNode: node};
					}

					return getTemplateData(node.parentNode, true);
				};

				// Factory for finding the parent (or self) that has the root assigned group or assigned user group.
				// NOTE: "
				// @returns [String]
				var getAssignedData = function (node, inherited, assignedType) {

					var typeGroup = assignedType === 'roles' ? 'roles' : 'users';
					var dataGroup = assignedType === 'roles' ? 'roles' : 'usernames';

					if (node.parentNode === null) {
						return {data: ['<i style="color: #ccc;">Not assigned</i>'], cameFromAncestor: false, rootNode: null, isAssigned: false};
					}
					if (node.getData().componentType[typeGroup] && !node.getData().componentType[typeGroup].cameFromAncestor) {
						return {data: node.getData().componentType[typeGroup][dataGroup], cameFromAncestor: inherited, rootNode: node, isAssigned: true};
					}

					return getAssignedData(node.parentNode, true, assignedType);
				};
				
				var checkEntryGridTools = function() {
					if (Ext.getCmp('entryGrid').getSelectionModel().getCount() === 1) {
						Ext.getCmp('lookupGrid-tools-edit').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-status').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-remove').setDisabled(false);						
						Ext.getCmp('moveToTopBtn').setDisabled(false);
					} else {
						Ext.getCmp('lookupGrid-tools-edit').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-status').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-remove').setDisabled(true);
						Ext.getCmp('moveToTopBtn').setDisabled(true);
					}
				};
				
				var actionRefreshEntryGrid = function() {
					Ext.getCmp('entryGrid').getStore().load();				
				};
				
				var actionAddEntry = function() {
					Ext.getCmp('parentComponentTypeId').getStore().load();
					addEditWin.show();
					
					//reset form
					Ext.getCmp('entryForm').reset(true);
					Ext.getCmp('entryForm').edit = false;
					Ext.getCmp('entryForm-type').setReadOnly(false);
				};
				
				var actionEditEntry = function(record) {

					var formattedDataModel = Ext.create('Ext.data.Model');
					Ext.apply(formattedDataModel.data, record.data.componentType);

					formattedDataModel.data.assignedUsers = [];
					formattedDataModel.data.assignedGroups = [];

					if (record.getData().componentType.assignedUsers) {
						Ext.Array.forEach(record.getData().componentType.assignedUsers, function (userObj, index) {
							formattedDataModel.data.assignedUsers[index] = userObj.username;
						});
					}

					if (record.getData().componentType.assignedGroups) {
						Ext.Array.forEach(record.getData().componentType.assignedGroups, function (groupObj, index) {
							formattedDataModel.data.assignedGroups[index] = groupObj.roleName;
						});
					}

					Ext.getCmp('parentComponentTypeId').getStore().load();
					addEditWin.show();
					
					Ext.getCmp('entryForm').reset(true);
					Ext.getCmp('entryForm').edit = true;
					
					//load form
					Ext.getCmp('entryForm').loadRecord(formattedDataModel);
					Ext.getCmp('entryForm-type').setReadOnly(true);
				};
				
				var actionToggleStatus = function() {

					Ext.getCmp('entryGrid').setLoading("Updating Status...");
					var type = Ext.getCmp('entryGrid').getSelection()[0].get('componentType').componentType;
					var currentStatus = Ext.getCmp('entryGrid').getSelection()[0].get('componentType').activeStatus;
					
					var method = 'PUT';
					var urlEnd = '/activate';
					if (currentStatus === 'A') {
						method = 'DELETE';
						urlEnd = '';
					}					
					Ext.Ajax.request({
						url: 'api/v1/resource/componenttypes/' + type + urlEnd,
						method: method,
						callback: function(){
							Ext.getCmp('entryGrid').setLoading(false);
						},
						success: function(response, opts){						
							actionRefreshEntryGrid();
						}
					});
				};
				
				var actionEntryAssignment = function() {
					var attributeWin = Ext.create('OSF.tool.EntryTypeEntryAssignment', {
					});
					attributeWin.show();
				};
				
				var actionAttributeAssignment = function() {
					
					var attributeWin = Ext.create('OSF.tool.EntryTypeAttributes', {
					});
					attributeWin.show();		
				};				

				var actionMoveToTop = function () {
					
					var entryGrid = Ext.getCmp('entryGrid');
					var selectedRecord = entryGrid.getSelection()[0];

					selectedRecord.getData().componentType.parentComponentType = null;
					saveEntryTypeRecord(selectedRecord);

					if (selectedRecord.parentNode.childNodes.length === 1) {
						selectedRecord.parentNode.data.leaf = true;
					}
					entryGrid.getStore().getRoot().insertChild(0, selectedRecord);
				};
				
				var actionRemoveType = function() {
					var typeToRemove = Ext.getCmp('entryGrid').getSelection()[0].get('componentType').componentType;
					var children = Ext.getCmp('entryGrid').getStore().getRoot().store.root.childNodes;
					//get flat list of all nodes that are not typeToRemove and its children
					var traverseTree = function(typeCode, flatList, children) {
						if (children.length === 0) {
							return;
						}
						Ext.Array.forEach(children, function(child) {
							if (child.data.compID !== typeCode) {
								flatList.push(child.data.compID);
								traverseTree(typeCode, flatList, child.childNodes);
							}
						})
					}
					flatList = [];
					traverseTree(typeToRemove, flatList, children);
					console.log(flatList);


					var promptWindow = Ext.create('Ext.window.Window', {
						iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
						title: 'Delete Entry Type?',
						y: 200,
						width: 400,
						minHeight: 175,
						modal: true,
						layout: 'fit',
						closeAction: 'destroy',
						items: [
							{
								xtype: 'form',
								bodyStyle: 'padding: 10px',
								dockedItems: [
									{
										xtype: 'toolbar',
										dock: 'bottom',
										items: [
											{
												text: 'Apply',
												formBind: true,
												id: 'applyBtnDeleteEntryType',
												iconCls: 'fa fa-lg fa-check icon-button-color-save',
												handler: function(){
													var form = this.up('form');
													var data = form.getValues();
																										
													Ext.getCmp('entryGrid').setLoading("Deleting entry type...");
													Ext.Ajax.request({
														url: 'api/v1/resource/componenttypes/' + typeToRemove + '?newtype=' + data.componentType,
														method: 'DELETE',
														callback: function(){
															Ext.getCmp('entryGrid').setLoading(false);
														},
														success: function(response, opts){																
															actionRefreshEntryGrid();
														}
													});													
													promptWindow.close();
												}
											},
											{
												xtype: 'tbfill'
											},
											{
												text: 'Cancel',
												formbind: true,
												iconCls: 'fa fa-lg fa-close icon-button-color-warning',
												handler: function(){
													promptWindow.close();
												}												
											}
										]
									}
								],
								layout: 'anchor',
								items: [
									{
										xtype: 'combobox',
										anchor: '100% 10%',
										name: 'componentType',
										id: 'moveExistingDataComboBox',
										labelAlign: 'top',
										fieldLabel: 'Move existing data to<span class="field-required" />',
										valueField: 'code',
										emptyText: 'Select Entry Type',
										displayField: 'description',
										allowBlank: false,
										editable: false,
										typeahead: false,
										store: {
											autoLoad: true,
											sorters: [{
												property: 'description',
												direction: 'ASC'													
											}],
											proxy: {
												type: 'ajax',
												url: 'api/v1/resource/componenttypes/lookup'
											},
											listeners: {
												load: function(store, records, successful, opts) {
													
													store.filterBy(function(record){
														//need to exclude itself and its children
														if (flatList.indexOf(record.get('code')) >= 0) {
															return true;
														} else {
															return false;
														}
													});
												}
											}
										}
									}
								]
							}
						]
					});
					promptWindow.show();
					
				};
				
			});
		</script>	
		
	</stripes:layout-component>
</stripes:layout-render>