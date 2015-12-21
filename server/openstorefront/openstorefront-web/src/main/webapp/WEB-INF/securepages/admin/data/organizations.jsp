<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
	<stripes:layout-component name="contents">
		
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
							url: '../api/v1/resource/organizations/',
							reader: {
								type: 'json',
								rootProperty: 'data',
								totalProperty: 'totalNumber'
							}
						})
					}),
					columnLines: true,
					columns: [						
						{ text: 'Name', dataIndex: 'name', minWidth: 200, flex:1},
						{ text: 'Description', dataIndex: 'description', flex: 1, minWidth: 200 },
						{ text: 'Type', dataIndex: 'organizationType', width: 200 },
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
							items: [
								{
									text: 'Refresh',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-refresh',
									tooltip: 'Refresh Grid',
									handler: function () {
										refreshGrid();
									}
								},
								{
									text: 'Add',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-plus',
									tooltip: 'Add record',
									handler: function () {
										addRecord();
									}
								}, 								
								{
									text: 'Edit',
									id: 'editButton',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-edit',
									disabled: true,
									tooltip: 'Edit selected record',
									handler: function () {
										editRecord();
									}								
								},							
								{
									text: 'Merge',
									id: 'mergeButton',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-compress',
									disabled: true,
									tooltip: 'Merge selected record into another record',
									handler: function () {
										mergeRecords();
									}								
								},
								{
									text: 'References',
									id: 'refButton',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-paperclip',
									tooltip: 'View selected record references',
									disabled: true,
									handler: function () {
										referenceRecords();
									}								
								},
								{	text: 'Delete',
									id: 'deleteButton',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-trash',
									disabled: true,
									tooltip: 'Delete record',
									handler: function () {
										deleteRecord();
								    }
								},
								{
									xtype: 'tbfill'
								},
								{	text: 'Run Extraction',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-play',
									tooltip: 'Start extraction of organizations from metadata',
									handler: function () {
										runExtraction();
								    }
								},
								{	text: '"No Organization" References',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-file-text-o',
									tooltip: 'Entries without organizations',
									handler: function () {
										noOrg();
								    }
								}
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
							console.log("double click");
						},
						selectionchange: function(grid, record, index, opts){
							console.log("change selection");
							checkButtonChanges();
						}
					}
				});
				
				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [
						orgGrid
					]
				});
				
				var selectedObj=null;
				
				var checkButtonChanges = function() {
					var cnt = Ext.getCmp('orgGrid').getSelectionModel().getCount();
					if ( cnt === 1) {
						Ext.getCmp('editButton').setDisabled(false);
						Ext.getCmp('mergeButton').setDisabled(false);
						Ext.getCmp('refButton').setDisabled(false);
						Ext.getCmp('deleteButton').setDisabled(false);
					} else {
						Ext.getCmp('editButton').setDisabled(true);
						Ext.getCmp('mergeButton').setDisabled(true);
						Ext.getCmp('refButton').setDisabled(true);
						Ext.getCmp('deleteButton').setDisabled(true);						
					}
				};
				
				var refreshGrid = function() {
					Ext.getCmp('orgGrid').getStore().load();				
				};
				
				var addRecord = function() {
//					addEditWin.show();
//					
//					//reset form
//					Ext.getCmp('entryForm').reset(true);
//					Ext.getCmp('entryForm').edit = false;
//					Ext.getCmp('entryForm-type').setReadOnly(false);
				};
				
				var editRecord = function(record) {
//					addEditWin.show();
//					
//					Ext.getCmp('entryForm').reset(true);
//					Ext.getCmp('entryForm').edit = true;
//					
//					//load form
//					Ext.getCmp('entryForm').loadRecord(record);
//					Ext.getCmp('entryForm-type').setReadOnly(true);
				};
				
				var mergeRecord = function() {
					
				};
				
				var referenceRecords = function(){
				     selectedObj = Ext.getCmp('orgGrid').getSelection()[0].data;
					 console.log("selObj",selectedObj);
					 
					 Ext.getCmp('refWin').show();
				};
				
				var deleteRecord = function(){
					selectedObj = Ext.getCmp('orgGrid').getSelection()[0].data;
					Ext.Ajax.request({
						url: '../api/v1/resource/organizations/'+encodeURIComponent(selectedObj.organizationId)+'/references',
						method: 'GET',
						success: function (response, opts) {
							theData = response.responseText;
							if(theData.length!==0){
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
												url: '../api/v1/resource/organizations/'+encodeURIComponent(selectedObj.organizationId),
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

                    Ext.getCmp('noOrgWin').show();
  
				};
				
				var runExtraction = function(){
				    Ext.toast('Performing organization meta-data extraction ...');
					Ext.Ajax.request({
						url: '../api/v1/resource/organizations/extract',
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
				//  NO ORG WINDOW
				//
				//
				var noOrgWin = Ext.create('Ext.window.Window', {
					id: 'noOrgWin',
					title: 'No Organization References',
					modal: true,
					width: '40%',
					height: '50%',
					items:[
						{
							id: 'noOrgGrid',
							title: '',
							store: Ext.create('Ext.data.Store', {
								autoLoad: true,
								id: 'noOrgGridStore',
								pageSize: 100,
								remoteSort: true,
								sorters: [
									new Ext.util.Sorter({
										property: 'componentName',
										direction: 'ASC'
									})
								],
								proxy: CoreUtil.pagingProxy({
									type: 'ajax',
									url: '../api/v1/resource/organizations/references',
									reader: {
										type: 'json',
										rootProperty: 'data',
										totalProperty: 'totalNumber'
									}
								})
							}),
							columnLines: true,
							columns: [						
								{ text: 'Name', dataIndex: 'componentName', minWidth: 200, flex:1},
								{ text: 'Reference Name', dataIndex: 'referenceName', flex: 1, minWidth: 200 },
								{ text: 'Reference Type', dataIndex: 'referenceType', width: 200 }
							]
						}
					],
					dockedItems: [
					{
						
							xtype: 'pagingtoolbar',
							dock: 'bottom',
							store: 'noOrgGridStore',
							displayInfo: true
					}]

					});
				
				//
				//
				//  REF WINDOW
				//
				//
				var refWin = Ext.create('Ext.window.Window', {
					id: 'refWin',
					title: 'Organization References',
					modal: true,
					width: '40%',
					height: '50%',
					layout:'fit',
					maximizable: true,
					scrollable:true,
					listeners:{
						show: function () {
							var refurl = '../api/v1/resource/organizations/'+encodeURIComponent(selectedObj.organizationId)+ '/references';
							console.log("url",refurl);
							var store = Ext.data.StoreManager.lookup('refGridStore');
							store.setProxy({
									type: 'ajax',
									url: refurl,
									method: 'GET'
								});
							
							store.load();
						}
					},
					items:[
						{
							xtype:'grid',
							id: 'refGrid',
							width:'100%',
							title: '',
							store: Ext.create('Ext.data.Store', {
								autoLoad: false,
								id: 'refGridStore',
								pageSize: 100,
								remoteSort: true,
								sorters: [
									new Ext.util.Sorter({
										property: 'name',
										direction: 'ASC'
									})
								],
								fields: [
									{name: 'componentName', mapping: function (data) {
											console.log("DATA",data);
										}} 
											
								]
								
							}),
							columnLines: true,
							columns: [						
								{ text: 'Name', dataIndex: 'componentName', minWidth: 200, flex:1},
								{ text: 'Reference Name', dataIndex: 'referenceName', flex: 1, minWidth: 200 },
								{ text: 'Reference Type', dataIndex: 'referenceType', width: 200 }
							]
						}
					]
					});
				
				
				
//				var addEditWin = Ext.create('Ext.window.Window', {
//					id: 'addEditWin',
//					title: 'Entry',
//					modal: true,
//					width: '40%',
//					items: [
//						{
//							xtype: 'form',
//							id: 'entryForm',
//							layout: 'vbox',
//							scrollable: true,
//							bodyStyle: 'padding: 10px;',
//							defaults: {
//								labelAlign: 'top'
//							},
//							items: [
//								{
//									xtype: 'textfield',
//									id: 'entryForm-type',
//									fieldLabel: 'Type Code<span class="field-required" />',
//									name: 'componentType',
//									width: '100%',
//									allowBlank: false,
//									maxLength: 20									
//								},
//								{
//									xtype: 'textfield',									
//									fieldLabel: 'Label<span class="field-required" />',
//									name: 'label',
//									allowBlank: false,
//									width: '100%',
//									maxLength: 80																		
//								},
//								{
//									xtype: 'htmleditor',
//									name: 'description',
//									fieldLabel: ' Description<span class="field-required" />',
//									allowBlank: false,
//									width: '100%',
//									fieldBodyCls: 'form-comp-htmleditor-border',
//									maxLength: 255
//								},				
//								{
//									xtype: 'panel',
//									html: '<b>Data Entry</b>'
//								},
//								{
//									xtype: 'checkbox',
//									boxLabel: 'Attributes',
//									name: 'dataEntryAttributes'
//								},
//								{
//									xtype: 'checkbox',
//									boxLabel: 'Relationships ',
//									name: 'dataEntryRelationships'
//								},
//								{
//									xtype: 'checkbox',
//									boxLabel: 'Contacts',
//									name: 'dataEntryContacts'
//								},
//								{
//									xtype: 'checkbox',
//									boxLabel: 'Resources',
//									name: 'dataEntryResources'
//								},
//								{
//									xtype: 'checkbox',
//									boxLabel: 'Media',
//									name: 'dataEntryMedia'
//								},
//								{
//									xtype: 'checkbox',
//									boxLabel: 'Dependancies',
//									name: 'dataEntryDependancies'
//								},
//								{
//									xtype: 'checkbox',
//									boxLabel: 'Metadata',
//									name: 'dataEntryMetadata'
//								},
//								{
//									xtype: 'checkbox',
//									boxLabel: 'Evaluation Information',
//									name: 'dataEntryEvaluationInformation'
//								},
//								{
//									xtype: 'checkbox',
//									boxLabel: 'Reviews',
//									name: 'dataEntryReviews'
//								},
//								{
//									xtype: 'checkbox',
//									boxLabel: 'Questions',
//									name: 'dataEntryQuestions'
//								},								
//								Ext.create('OSF.component.StandardComboBox', {
//									name: 'componentTypeTemplate',																		
//									width: '100%',
//									margin: '0 0 0 0',
//									fieldLabel: 'Override Template',
//									emptyText: 'Default',
//									storeConfig: {
//										url: '../api/v1/resource/componenttypetemplates/lookup'
//									}
//								})
//							],
//							dockedItems: [
//								{
//									xtype: 'toolbar',
//									dock: 'bottom',
//									items: [
//										{
//											text: 'Save',
//											iconCls: 'fa fa-save',
//											formBind: true,
//											handler: function(){
//												var method = Ext.getCmp('entryForm').edit ? 'PUT' : 'POST'; 												
//												var data = Ext.getCmp('entryForm').getValues();
//												var url = Ext.getCmp('entryForm').edit ? '../api/v1/resource/componenttypes/' + data.componentType : '../api/v1/resource/componenttypes';       
//
//												CoreUtil.submitForm({
//													url: url,
//													method: method,
//													data: data,
//													removeBlankDataItems: true,
//													form: Ext.getCmp('entryForm'),
//													success: function(response, opts) {
//														Ext.toast('Saved Successfully', '', 'tr');
//														Ext.getCmp('entryForm').setLoading(false);
//														Ext.getCmp('addEditWin').hide();													
//														actionRefreshEntryGrid();												
//													}
//												});												
//											}
//										},
//										{
//											xtype: 'tbfill'
//										},
//										{
//											text: 'Cancel',
//											iconCls: 'fa fa-close',
//											handler: function(){
//												Ext.getCmp('addEditWin').close();
//											}											
//										}
//									]
//								}
//							]
//						}
//					]
//				});
				
			});
		</script>	
		
	</stripes:layout-component>
</stripes:layout-render>