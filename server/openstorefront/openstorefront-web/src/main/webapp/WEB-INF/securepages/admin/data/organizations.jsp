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
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function(){	
				
				var orgGrid = Ext.create('Ext.grid.Panel', {
					id: 'orgGrid',
					title: 'Manage Organizations <i class="fa fa-question-circle"  data-qtip="Organizations found within the metadata of the site." ></i>',
					store: Ext.create('Ext.data.Store', {
						autoLoad: true,
						id: 'orgGridStore',
						pageSize: 100,
						remoteSort: true,
						sorters: [
							new Ext.util.Sorter({
								property: 'name',
								direction: 'ASC'
							})
						],
						proxy: CoreUtil.pagingProxy({
							type: 'ajax',
							url: 'api/v1/resource/organizations/',
							reader: {
								type: 'json',
								rootProperty: 'data',
								totalProperty: 'totalNumber'
							}
						}),
						listeners: {
							beforeLoad: function(store, operation, eOpts){
								store.getProxy().extraParams = {
									name: Ext.getCmp('orgGrid-filterByName').getValue() ? Ext.getCmp('orgGrid-filterByName').getValue() : null,									
								};
							}
						}						
					}),
					columnLines: true,
					columns: [											
						{ text: 'Name', dataIndex: 'name', minWidth: 200, flex:1},
						{ text: 'Description', dataIndex: 'description', flex: 1, minWidth: 200 },
						{ text: 'Type', dataIndex: 'organizationType', width: 200,
							renderer: function(value, meta, record) {
								return record.get('organizationTypeDescription');
							}
						},
						{ text: 'Logo', dataIndex: 'logoOriginalFileName', width: 200, align: 'center',
							renderer: function(value, meta, record) {
								if (value) {
									return '<img width=175 src="Media.action?OrganizationLogo&organizationId=' + record.get('organizationId') + '"><br>' + value;
								} else {
									return value;
								}
							}
						},	
						{ text: 'Web Site', dataIndex: 'homeUrl', width: 200, hidden:true },
						{ text: 'Address', dataIndex: 'address', width: 150, hidden:true },
						{ text: 'Agency', dataIndex: 'agency', width: 150, hidden:true },
						{ text: 'Department', dataIndex: 'department', width: 150, hidden:true },
						{ text: 'Contact Name', dataIndex: 'contactName', width: 150, hidden:true },
						{ text: 'Contact Phone', dataIndex: 'contactPhone', width: 150, hidden:true },
						{ text: 'Contact Email', dataIndex: 'contactEmail', width: 150, hidden:true }
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [{
								xtype: 'textfield',
								id: 'orgGrid-filterByName',
								name: 'name',
								fieldLabel: 'Filter by Name',
								labelAlign: 'top',
								labelSeparator: '',
								maxLength: 255,
								width: 300,
								listeners: {
									change: {
										fn: function(field, newValue, oldValue, opts) {
											refreshGrid();
										},
										buffer: 1500
									}
								}
							}]
						},
						
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									tooltip: 'Refresh Grid',
									handler: function () {
										refreshGrid();
									}
								},
								{
									requiredPermissions: ['ADMIN-ORGANIZATION-CREATE', 'ADMIN-ORGANIZATION-UPDATE'],
									xtype: 'tbseparator'
								},
								{
									text: 'Add',
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-corretion',
									tooltip: 'Add record',
									requiredPermissions: ['ADMIN-ORGANIZATION-CREATE'],
									handler: function () {
										addRecord();
									}
								}, 								
								{
									text: 'Edit',
									id: 'editButton',
									scale: 'medium',								
									width: '100px',
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
									disabled: true,
									tooltip: 'Edit selected record',
									requiredPermissions: ['ADMIN-ORGANIZATION-UPDATE'],
									handler: function () {
										editRecord();
									}								
								},
								{
									requiredPermissions: ['ADMIN-ORGANIZATION-UPDATE'],
									xtype: 'tbseparator'
								},
								{									
									text: 'Logo',
									id: 'logoBtn',
									scale: 'medium',
									disabled: true,
									iconCls: 'fa fa-2x fa-file-picture-o icon-button-color-default icon-vertical-correction',
									requiredPermissions: ['ADMIN-ORGANIZATION-UPDATE'],
									handler: function () {
										manageLogoAction();
									}
								},
								{
									xtype: 'tbseparator'
								},								
								{
									text: 'References',
									id: 'refButton',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-link icon-button-color-default icon-vertical-correction',
									tooltip: 'View selected record references',
									disabled: true,
									handler: function () {
										referenceRecords();
									}								
								},
								{
									scale: 'medium',
									height: '38px',
									text: '<span class="fa-stack"><i class="fa fa-link fa-stack-1x icon-horizontal-correction"></i><i class="fa fa-2x fa-ban fa-stack-1x icon-button-color-warning icon-horizontal-correction"></i></span>"No Organization" References',
									tooltip: 'Entries without organizations',
									handler: function () {
										noOrg();
								    }
								},
								{
									text: 'Merge',
									id: 'mergeButton',
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-compress icon-button-color-default icon-vertical-correction',
									disabled: true,
									tooltip: 'Merge selected record into another record',
									requiredPermissions: ['ADMIN-ORGANIZATION-MERGE'],
									handler: function () {
										mergeRecords();
									}								
								},
								{
									xtype: 'tbfill'
								},
								{	text: 'Run Extraction',
									id: 'runExtractorBtn',
									scale: 'medium',
									hidden: true,
									iconCls: 'fa fa-2x fa-bolt icon-button-color-run icon-vertical-correction',
									tooltip: 'Start extraction of organizations from metadata',
									handler: function () {
										runExtraction();
								    }
								},
								{
									xtype: 'tbseparator'
								},
								{	text: 'Delete',
									id: 'deleteButton',
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									disabled: true,
									tooltip: 'Delete record',
									requiredPermissions: ['ADMIN-ORGANIZATION-DELETE'],
									handler: function () {
										deleteRecord();
								    }
								},
							]
						},
						{
							xtype: 'pagingtoolbar',
							dock: 'bottom',
							store: 'orgGridStore',
							displayInfo: true
						}
					],
					listeners: {
						itemdblclick: function(grid, record, item, index, e, opts){
							//console.log("double click");
							editRecord();
						},
						selectionchange: function(grid, record, index, opts){
							//console.log("change selection");
							checkButtonChanges();
						}
					}
				});
				
				addComponentToMainViewPort(orgGrid);
				
				
				CoreService.userservice.getCurrentUser().then(function(user){
					if (CoreService.userservice.userHasPermission(user, "ADMIN-ORGANIZATION-EXTRACTION")) {
						Ext.getCmp('runExtractorBtn').setHidden(false);					
					}
				});
				
				
				var selectedObj=null;
				
				var checkButtonChanges = function() {
					var cnt = Ext.getCmp('orgGrid').getSelectionModel().getCount();
					if ( cnt === 1) {
						Ext.getCmp('editButton').setDisabled(false);
						Ext.getCmp('logoBtn').setDisabled(false);						
						Ext.getCmp('mergeButton').setDisabled(false);
						Ext.getCmp('refButton').setDisabled(false);
						Ext.getCmp('deleteButton').setDisabled(false);						
					} else {
						Ext.getCmp('editButton').setDisabled(true);
						Ext.getCmp('logoBtn').setDisabled(true);
						Ext.getCmp('mergeButton').setDisabled(true);
						Ext.getCmp('refButton').setDisabled(true);
						Ext.getCmp('deleteButton').setDisabled(true);						
					}
				};
				
				var refreshGrid = function() {
					Ext.getCmp('orgGrid').getStore().load();				
				};
				
				var addRecord = function() {
					addEditWin.show();
					addEditWin.setTitle('<i class="fa fa-lg fa-plus small-vertical-correction"></i>' + ' &nbsp;&nbsp;' + 'Add Organization');
//					//reset form
					Ext.getCmp('entryForm').reset(true);
					Ext.getCmp('entryForm').edit = false;
					
				};
				
				var editRecord = function() {
					
					addEditWin.show();
					addEditWin.setTitle('<i class="fa fa-lg fa-edit small-vertical-correction"></i>' + ' &nbsp;&nbsp;' + 'Edit Organization');
					selectedObj = Ext.getCmp('orgGrid').getSelection()[0];
					Ext.getCmp('entryForm').reset(true);
					Ext.getCmp('entryForm').edit = true;
					//load form
					Ext.getCmp('entryForm').loadRecord(selectedObj);
				};
				
				var manageLogoAction = function() {
					var record = Ext.getCmp('orgGrid').getSelection()[0];
					
					var addEditLogoWin = Ext.create('Ext.window.Window', {
						title: 'Manage Logo',
						iconCls: 'fa fa-lg fa-file-picture-o',
						closeAction: 'destroy',
						width: 600,	
						height: 300,
						layout: 'fit',
						modal: true,
						items: [
							{
								xtype: 'form',
								itemId: 'logoForm',		
								scrollable: true,
								bodyStyle: 'padding: 10px;',
								items: [
									{
										xtype: 'panel',
										width: '100%',
										layout: 'center',
										items: [
											{
												xtype: 'image',
												itemId: 'logoImg',
												width: 100,
												margin: '0 20 0 0'
											}											
										]
									}
								],
								dockedItems: [
									{
										xtype: 'filefield',
										itemId: 'uploadFile',
										name: 'file',
										width: '100%',
										flex: 1,		
										allowBlank: false,
										margin: '10 10 10 10',
										fieldLabel: 'Import Logo <span class="field-required"/>',										
										buttonText: 'Select File...'								
									},
									{
										xtype: 'toolbar',
										dock: 'bottom',
										items: [
											{
												text: 'Upload Logo',
												formBind: true, 
												iconCls: 'fa fa-lg fa-upload ',
												requiredPermissions: ['ADMIN-ORGANIZATION-UPDATE', 'ADMIN-ORGANIZATION-CREATE'],
												handler: function(){
													var logoForm = addEditLogoWin.queryById('logoForm');
													
													logoForm.setLoading("Uploading Logo...");
													logoForm.submit({
														url: 'Media.action?UploadOrganizationLogo&organizationId=' + record.get('organizationId'),															
														callback: function(){
															logoForm.setLoading(false);
														},
														success: function(form, action) {
															Ext.toast('Saved Successfully', '', 'tr');																																									
															refreshGrid();	
															addEditLogoWin.close();
														}
													});												
												}
											},
											{
												xtype: 'tbfill'
											},
											{
												text: 'Delete Logo',
												iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
												handler: function(){
													Ext.Msg.show({
														title:'Delete Logo?',
														message: 'Are you sure you want to delete logo?',
														buttons: Ext.Msg.YESNO,
														icon: Ext.Msg.QUESTION,
														fn: function(btn) {
															if (btn === 'yes') {
																Ext.getCmp('orgGrid').setLoading('Deleting Logo...');
																Ext.Ajax.request({
																	url: 'api/v1/resource/organizations/' + record.get('organizationId') + '/logo',
																	method: 'DELETE',
																	callback: function(){
																		Ext.getCmp('orgGrid').setLoading(false);
																	},
																	success: function() {	
																		refreshGrid();
																		addEditLogoWin.close();																		
																	}
																});
															} 
														}
													});	
												}										
											},
											{
												xtype: 'tbfill'
											},									
											{
												text: 'Close',
												iconCls: 'fa fa-lg fa-close icon-button-color-warning',
												handler: function(){
													addEditLogoWin.close();
												}										
											}
										]
									}
								]	
							}
						]
					});
					addEditLogoWin.show();
					
					if (record.get('logoOriginalFileName')) {
						addEditLogoWin.queryById('logoImg').setSrc('Media.action?OrganizationLogo&organizationId=' + record.get('organizationId'));
					} else {
						addEditLogoWin.queryById('logoImg').setSrc(null);
					}
					
				};
				
				var mergeRecords = function() {
				    selectedObj = Ext.getCmp('orgGrid').getSelection()[0].data;
					
					//console.log("Org ID", selectedObj);
					Ext.getCmp('mergeForm').reset(true);
					
				    Ext.getCmp('targetId').setValue(selectedObj.organizationId);
					Ext.getCmp('targetOrganization').setValue(selectedObj.name);
				 
					Ext.getCmp('mergeId').getStore().load({
						url: 'api/v1/resource/organizations'						
					});
					
		            mergeWin.show();
					
				};
				
				var referenceRecords = function(){
				    selectedObj = Ext.getCmp('orgGrid').getSelection()[0].data;
					showReferences('api/v1/resource/organizations/'+encodeURIComponent(selectedObj.organizationId)+ '/references', 
					selectedObj.name + ' - References');
				};
				
				var deleteRecord = function(){
					selectedObj = Ext.getCmp('orgGrid').getSelection()[0].data;
					Ext.Ajax.request({
						url: 'api/v1/resource/organizations/'+encodeURIComponent(selectedObj.organizationId)+'/references',
						method: 'GET',
						success: function (response, opts) {
							
							var theData=[];
							theData = JSON.parse(response.responseText);
							//console.log("response:",theData.length);
							if(0 < theData.length){
								Ext.toast('That organization has references and cannot be deleted.');
								return;
							}
							else{
								Ext.Msg.show({
									title: 'Delete Organization?',
									message: 'Are you sure you want to delete the selected organization?',
									buttons: Ext.Msg.YESNO,
									icon: Ext.Msg.QUESTION,
									fn: function (btn) {
										if (btn === 'yes') {
											Ext.getCmp('orgGrid').setLoading(true);
											Ext.Ajax.request({
												url: 'api/v1/resource/organizations/'+encodeURIComponent(selectedObj.organizationId),
												method: 'DELETE',
												success: function (response, opts) {
													Ext.getCmp('orgGrid').setLoading(false);
													refreshGrid();
												},
												failure: function (response, opts) {
													Ext.getCmp('orgGrid').setLoading(false);
												}
											});
										}
									}
								});
							}
						},
						failure: function (response, opts) {
							Ext.toast('Failed to check for organization references.');
						}
					});
					
					
					
				};
				
				var noOrg = function(){
                    showReferences('api/v1/resource/organizations/references', '"No Organization" - References');					
				};
				
				var runExtraction = function(){
				    Ext.toast('Performing organization meta-data extraction ...');
					Ext.Ajax.request({
						url: 'api/v1/resource/organizations/extract',
						method: 'POST',
						success: function (response, opts) {
							Ext.toast('Organization meta-data extraction complete');
							refreshGrid();
						},
						failure: function (response, opts) {
							Ext.toast('Organization meta-data extraction failed');
						}
					});	
				};
				
				//
				//
				//  reference Window
				//
				//
				var showReferences = function(url, title) {
				
					var noOrgStore = Ext.create('Ext.data.Store', {
						autoLoad: true,									
						pageSize: 100,
						remoteSort: true,
						sorters: [
							new Ext.util.Sorter({
								property: 'referenceName',
								direction: 'ASC'
							})
						],
						fields: [
							{name: 'referenceType', mapping: function (data) {

									var retStr ='';
									if(typeof data.componentName !== 'undefined'){
										retStr=data.referenceType + '<br/><div style="font-size:.7em;">Entry: '+data.componentName+'</div>';
									}
									else{
										retStr=data.referenceType;
									}
									return retStr;
								}
							}
						],
						proxy: CoreUtil.pagingProxy({
							type: 'ajax',
							url: url,
							reader: {
								type: 'json',
								rootProperty: 'data',
								totalProperty: 'totalNumber'
							}
						})
					});
		
					var noOrgWin = Ext.create('Ext.window.Window', {
						title: title,
						iconCls: 'fa fa-lg fa-exclamation-circle icon-small-vertical-correction',
						modal: true,
						width: '70%',
						height: '50%',
						closeAction: 'destroy',
						layout: 'fit',
						listeners:	{
							show: function() {        
								this.removeCls("x-unselectable");    
							}
						},
						items:[
							{
								xtype:'grid',								
								title: '',
								store: noOrgStore,
								columnLines: true,
								viewConfig: {
									enableTextSelection: true
								},	
								columns: [															
									{ text: 'Reference Name', dataIndex: 'referenceName', flex: 1, minWidth: 200,
										tooltip: 'References missing information are sorted to the top initially'
									},
									{ text: 'Reference Type', dataIndex: 'referenceType', align: 'center', width: 200 },
									{ text: 'Reference Id', dataIndex: 'referenceId', flex: 1, minWidth: 200 }
								]

							}
						],
						dockedItems: [{
								xtype: 'pagingtoolbar',
								dock: 'bottom',
								store: noOrgStore,
								displayInfo: true
						}]

					});
					noOrgWin.show();					
				}
				
				
				
				//
				//
				//  Merge Window
				//
				//
				var mergeWin = Ext.create('Ext.window.Window', {
					id: 'mergeWin',
					title: 'Merge Organizations',
					iconCls: 'fa fa-lg fa-compress icon-small-vertical-correction',
					modal: true,
					width: '50%',
					height: 260,
					y: 40,
					layout: 'fit',
					resizable: false,
					items: [
						{	xtype: 'form',
							id: 'mergeForm',
							scrollable: true,
							layout:'fit',
							items: [
								{
									xtype: 'panel',
									style: 'padding: 10px;',
									layout: 'vbox',
									defaults: {
										labelAlign: 'top'
									},
									items:[
										{
											xtype: 'textfield',
											id: 'targetId',
											name: 'targetId',
											style: 'padding-top: 5px;',
											width: '100%',
											hidden: true
										},
										{
											xtype: 'combobox',
											name: 'mergeId',
											id: 'mergeId',
											fieldLabel: 'Merge references from',
											width: '100%',
											maxLength: 50,
											displayField: 'name',
											valueField: 'organizationId',
											editable: true,
											typeAhead: true,
											forceSelection: true,
											allowBlank: false,
											store: {
												autoLoad: false,
												sorters: [
													new Ext.util.Sorter({
														property: 'name',
														direction: 'ASC'
													})
												],												
												proxy: {
													type: 'ajax',
													url: 'api/v1/resource/organizations',
													reader: {
														type: 'json',
														rootProperty: 'data',
														totalProperty: 'totalNumber'
													}													
												}
											}
										},
										{
											xtype: 'textfield',
											id: 'targetOrganization',
											fieldLabel: 'Into this target organization',
											name: 'targetOrganization',
											style: 'padding-top: 5px;',
											width: '100%',
											readOnly:true
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
										text: 'Apply',
										iconCls: 'fa fa-lg fa-check icon-button-color-save',
										formBind: true,
										handler: function(){
											var data = Ext.getCmp('mergeForm').getValues();
											if(data.mergeId === ''){
												Ext.toast('You must enter an merge organization that merges into the target.', '', 'tr');
												return;
											}
											else if(data.mergeId === data.targetId){
												Ext.toast('You cannot merge the same organizations together.', '', 'tr');
												return;
											}

											var url = 'api/v1/resource/organizations/'+data.targetId+'/merge/'+data.mergeId;     
											Ext.getCmp('mergeForm').setLoading(true);

											CoreUtil.submitForm({
												url: url,
												method: 'POST',
												removeBlankDataItems: true,
												form: Ext.getCmp('mergeForm'),
												success: function(response, opts) {
													Ext.toast('Merged Successfully', '', 'tr');
													Ext.getCmp('mergeForm').setLoading(false);
													Ext.getCmp('mergeWin').hide();													
													refreshGrid();												
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
											Ext.getCmp('mergeWin').close();
										}											
									}
								]
							}
						]
					}
					]				   
				});
				
				//
				//
				//  ADD EDIT ORG WINDOW
				//
				//
				var orgTypeStore = Ext.create('Ext.data.Store', {
					id:'orgTypeStore',
					autoLoad: true,
					sorters: [
						new Ext.util.Sorter({
							property: 'description',
							direction: 'DESC'
						})
					],
					proxy: CoreUtil.pagingProxy({
						url: 'api/v1/resource/lookuptypes/OrganizationType',
						reader: {
							type: 'json',							
						}
					})
				});
				
				var addEditWin = Ext.create('Ext.window.Window', {
					id: 'addEditWin',
					title: 'Organization',
					modal: true,
					width: '70%',					
					resizable: false,
					layout: 'fit',
					items: [
						{
							xtype: 'form',
							id: 'entryForm',
							scrollable: true,
							layout:'column',
							defaults: {
								labelAlign: 'top'
							},
							items: [
								{
									xtype: 'panel',
									columnWidth: 0.5,
									title: 'Organization Information',
									defaults: {
										labelAlign: 'top'
									},
									style: 'padding: 10px;',
									items: [
										{
											xtype: 'textfield',
											id: 'name',
											fieldLabel: 'Name<span class="field-required" />',
											name: 'name',
											style: 'padding-top: 5px;',
											width: '100%',
											allowBlank: false,
											maxLength: 255		
										},
										{
											xtype: 'textarea',
											id: 'description',
											fieldLabel: 'Description',
											name: 'description',
											style: 'padding-top: 5px;',
											width: '100%',
											maxLength: 16384		
										},
										{
											xtype: 'textfield',
											id: 'homeUrl',
											fieldLabel: 'Organization URL',
											name: 'homeUrl',
											style: 'padding-top: 5px;',
											width: '100%',
											maxLength: 255		
										},
										{
											xtype: 'combobox',
											name: 'organizationType',
											id: 'organizationType',
											fieldLabel: 'Organization Type',
											width: '100%',
											maxLength: 50,
											store: orgTypeStore,
											displayField: 'description',
											valueField: 'code',
											editable: false,
											allowBlank: true		
										}										
									]
								},
								{
									xtype: 'panel',
									columnWidth: 0.5,
									title: 'Main Contact Information',
									defaults: {
										labelAlign: 'top'
									},
									style: 'padding: 10px;',
									items: [
										{
											xtype: 'textfield',
											id: 'contactName',
											fieldLabel: 'Contact Name',
											name: 'contactName',
											style: 'padding-top: 5px;',
											width: '100%',
											maxLength: 255	
										},
										{
											xtype: 'textfield',
											id: 'contactPhone',
											fieldLabel: 'Phone',
											name: 'contactPhone',
											style: 'padding-top: 5px;',
											width: '100%',
											maxLength: 255	
										},
										{
											xtype: 'textfield',
											id: 'contactEmail',
											fieldLabel: 'Email',
											name: 'contactEmail',
											inputType: 'email',
											vtype: 'email',
											style: 'padding-top: 5px;',
											width: '100%',
											maxLength: 255	
										},
										{
											xtype: 'textfield',
											id: 'agency',
											fieldLabel: 'Agency',
											name: 'agency',
											style: 'padding-top: 5px;',
											width: '100%',
											maxLength: 255	
										},
										{
											xtype: 'textfield',
											id: 'department',
											fieldLabel: 'Department',
											name: 'department',
											style: 'padding-top: 5px;',
											width: '100%',
											maxLength: 255	
										},
										{
											xtype: 'textfield',
											id: 'address',
											fieldLabel: 'Address',
											name: 'address',
											style: 'padding-top: 5px;',
											width: '100%',
											maxLength: 255	
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
											iconCls: 'fa fa-lg fa-save icon-button-color-save',
											formBind: true,
											handler: function(){
												var mainForm = Ext.getCmp('entryForm');
												var method = mainForm.edit ? 'PUT' : 'POST'; 												
												var data = Ext.getCmp('entryForm').getValues();
												var url = Ext.getCmp('entryForm').edit ? 'api/v1/resource/organizations/' + selectedObj.data.organizationId : 'api/v1/resource/organizations';       
                                                //console.log("Made It ",selectedObj);
												CoreUtil.submitForm({
													url: url,
													method: method,
													data: data,
													removeBlankDataItems: true,
													form: mainForm,
													success: function(response, opts) {
														var org = Ext.decode(response.responseText);
														
														Ext.toast('Saved Successfully', '', 'tr');														
														Ext.getCmp('addEditWin').hide();													
														refreshGrid();
																								
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
				
			});
		</script>	
		
	</stripes:layout-component>
</stripes:layout-render>