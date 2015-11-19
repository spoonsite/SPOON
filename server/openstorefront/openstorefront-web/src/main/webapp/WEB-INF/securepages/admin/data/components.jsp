<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
          <stripes:layout-component name="contents">

		<form name="exportForm" action="../api/v1/resource/components/export" method="POST" >
			<p style="display: none;" id="exportFormIds">
			</p>      
		</form>
			  
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function(){	
				

		
			//MAIN GRID 
				
				var versionViewTemplate = new Ext.XTemplate(						
				);
		
				Ext.Ajax.request({
					url: 'Router.action?page=admin/data/componentVersionTemplate.jsp',
					success: function(response, opts){
						versionViewTemplate.set(response.responseText, true);
					}
				});
				
				var versionWinRestorePrompt = Ext.create('Ext.window.Window', {
					modal: true,								
					width: 300,
					bodyStyle: 'padding: 10px;',
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [
								{
									text: 'Restore',
									iconCls: 'fa fa-reply',
									handler: function(){
										this.up('window').hide();
										
										var componentId = Ext.getCmp('componentGrid').getSelection()[0].get('componentId');
										var versionHistoryId = Ext.getCmp('versionGrid').getSelection()[0].get('versionHistoryId');										
										var options = this.up('window').down('form').getValues();
										
										//load
										Ext.getCmp('versionWin').setLoading("Restoring version...");
										Ext.Ajax.request({
											url: '../api/v1/resource/components/' + componentId + '/versionhistory/' + versionHistoryId + '/restore',
											method: 'PUT',
											jsonData: options,
											success: function(response, opts){
												Ext.getCmp('versionWin').setLoading(false);
												Ext.toast('Restored Version Successfully.');
												versionLoadCurrent();
												Ext.getCmp('versionGrid').getSelectionModel().deselectAll();
											},
											failure: function(response, opts) {
												Ext.getCmp('versionWin').setLoading(false);
											}
										});
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Cancel',
									iconCls: 'fa fa-close',
									handler: function(){
										this.up('window').hide();
									}									
								}
							]
						}
					],						
					items: [
						{
							xtype: 'panel',
							html: 'Select Restore Options:'
						},
						{
							xtype: 'form',
							items: [
								{
									xtype: 'checkbox',
									boxLabel: 'Reviews',
									name: 'restoreReviews'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Questions',
									name: 'restoreQuestions'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Tags',
									name: 'restoreTags'
								},
								{
									xtype: 'checkbox',
									boxLabel: 'Integration',
									name: 'restoreIntegration'
								}								
							]
						}
					],	
					title:'Restore Version?',
					message: 'Select Restore Options'
				});
				
				var versionWin = Ext.create('Ext.window.Window', {
					id: 'versionWin',
					title: 'Versions',					
					maximizable: true,
					width: '80%',
					height: '80%',
					modal: true,
					layout: {
						type: 'hbox',
						align: 'stretch'
					},					
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [
								{
									text: 'Restore',
									id: 'versionWin-tool-restoreBtn',
									iconCls: 'fa fa-reply',
									disabled: true,
									handler: function(){
										//prompt
										versionWinRestorePrompt.show();
									}
								},
								{
									xtype: 'tbfill'
								}, 
								{
									text: 'Close',
									iconCls: 'fa fa-close',
									handler: function(){
										this.up('window').hide();
									}
								}
							]
						}						
					],
					items: [						
						{
							xtype: 'grid',
							id: 'versionGrid',
							region: 'west',
							split: true,
							border: true,
							columnLines: true,
							width: '33%',
							store: Ext.create('Ext.data.Store', {
								autoLoad: false,
								fields: [
									  "createUser",
									  "createDts",
									  "updateUser",
									  "updateDts",
									  "adminModified",
									  "componentId",
									  "versionHistoryId",
									  "version",
									  "fileHistoryId"
								],
								proxy: {
									url: '',
									type: 'ajax'
								}
							}),
							columns: [
								{ text: 'Version', dataIndex: 'version', width: 150 },
								{ text: 'Archive User', dataIndex: 'createUser', width: 150 },
								{ text: 'Archive Date', dataIndex: 'createDts', flex: 1, xtype: 'datecolumn', format:'m/d/y H:i:s' }
							],
							listeners: {
								selectionchange: function(grid, record, index, opts){
									if (grid.getSelected().length === 1) {
										Ext.getCmp('versionWin-tool-restoreBtn').setDisabled(false);
										Ext.getCmp('versionWin-tool-removeBtn').setDisabled(false);
									} else {
										Ext.getCmp('versionWin-tool-restoreBtn').setDisabled(true);
										Ext.getCmp('versionWin-tool-removeBtn').setDisabled(true);
									}

									//load preview
									if (record && record[0]){
										var componentId = Ext.getCmp('componentGrid').getSelection()[0].get('componentId');
										Ext.Ajax.request({
											url: '../api/v1/resource/components/' + componentId + '/versionhistory/' + record[0].get('versionHistoryId') + '/view',
											success: function(response, opts) {
												var data = Ext.decode(response.responseText);
												Ext.getCmp('versionWin-snapshotVersionPanel').update(data);
												Ext.getCmp('versionWin-snapshotVersionPanel').setTitle('Selected Version - ' + record[0].get('version'));
											}
										});
									} else {
										Ext.getCmp('versionWin-snapshotVersionPanel').update(undefined);
										Ext.getCmp('versionWin-snapshotVersionPanel').setTitle('Selected Version');	
									}
								}	
							},
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'top',
									items: [
										{
											text: 'Create Snapshot',
											iconCls: 'fa fa-plus',
											tooltip: 'Creates snapshot of the current verison',
											handler: function(){
												var versionWin = this.up('window');
												versionWin.setLoading("Snapshoting current version...");
												var componentId = Ext.getCmp('componentGrid').getSelection()[0].get('componentId');
												Ext.Ajax.request({
													url: '../api/v1/resource/components/' + componentId + '/versionhistory',
													method: 'POST',
													success: function(response, opts) {
														versionWin.setLoading(false);
														Ext.getCmp('versionGrid').getStore().reload();
													},
													failure: function(response, opts) {
														versionWin.setLoading(false);														
													}
												});
											}
										},
										{
											xtype: 'tbfill'
										},
										{
											text: '<span style="color: white">Remove</span>',
											id: 'versionWin-tool-removeBtn',
											iconCls: 'icon-color-light fa fa-trash',
											ui: 'danger',
											disabled: true,
											handler: function(){
												var componentId = Ext.getCmp('componentGrid').getSelection()[0].get('componentId');
												var versionHistoryId = Ext.getCmp('versionGrid').getSelection()[0].get('versionHistoryId');
												var version = Ext.getCmp('versionGrid').getSelection()[0].get('version');
												var versionWin = this.up('window');
												
												Ext.Msg.show({
													title: 'Delete Version?',
													message: 'Are you sure you want to delete version:  ' + version +' ?',
													buttons: Ext.Msg.YESNO,
													icon: Ext.Msg.QUESTION,
													fn: function(btn) {
														if (btn === 'yes') {
															versionWin.setLoading('Removing version...');
															Ext.Ajax.request({
																url: '../api/v1/resource/components/' + componentId + '/versionhistory/' + versionHistoryId,
																method: 'DELETE',
																success: function(response, opts) {
																	versionWin.setLoading(false);
																	Ext.getCmp('versionGrid').getStore().reload();
																},
																failure: function(response, opts) {
																	versionWin.setLoading(false);
																}
															});
														} 
													}
												});

												
											}
										}
									]
								}
							]
						},
						{
							xtype: 'panel',
							id: 'versionWin-snapshotVersionPanel',
							region: 'east',
							width: '33%',
							title: 'Selected Version',
							autoScroll: true,
							scrollable: true,
							border: true,
							split: true,
							bodyStyle: 'padding: 10px;',
							tpl: versionViewTemplate							
						},						
						{
							xtype: 'panel',
							id: 'versionWin-currentVersionPanel',
							region: 'center',
							width: '33%',
							title: 'Current Version',
							header: {
								cls: 'accent-background'
							},
							autoScroll: true,
							border: true,
							split: true,
							bodyStyle: 'padding: 10px;',
							tpl: versionViewTemplate
						}						
					]
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
								window.open('../single?id=' + Ext.getCmp('componentGrid').getSelection()[0].get('componentId'), "Preview");
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
									iconCls: 'fa fa-arrow-left',									
									handler: function() {
										actionPreviewNextRecord(false);
									}									
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Close',
									iconCls: 'fa fa-close',
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
									iconCls: 'fa fa-arrow-right',
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
						Ext.getCmp('componentGrid').getSelectionModel().selectNext();						
					} else {
						Ext.getCmp('componentGrid').getSelectionModel().selectPrevious();						
					}
					actionPreviewComponent(Ext.getCmp('componentGrid').getSelection()[0].get('componentId'));					
				};
				
				var previewCheckButtons = function() {	
					if (Ext.getCmp('componentGrid').getSelectionModel().hasPrevious()) {
						Ext.getCmp('previewWinTools-previousBtn').setDisabled(false);
					} else {
						Ext.getCmp('previewWinTools-previousBtn').setDisabled(true);
					}
					
					if (Ext.getCmp('componentGrid').getSelectionModel().hasNext()) {
						Ext.getCmp('previewWinTools-nextBtn').setDisabled(false);
					} else {
						Ext.getCmp('previewWinTools-nextBtn').setDisabled(true);
					}					
				};
				
				var maingridStore = Ext.create('Ext.data.Store', {				
					autoLoad: true,
					pageSize: 300,
					remoteSort: true,	
					sorters: [
						new Ext.util.Sorter({
							property : 'name',
							direction: 'DESC'
						})
					],
					fields:[ 
						{name: 'name', mapping: function(data){
							return data.component.name;
						}},
						{name: 'description', mapping: function(data){
							return data.component.description;
						}},
						{name: 'activeStatus', mapping: function(data){
							return data.component.activeStatus;
						}},
						{name: 'approvalState', mapping: function(data){
							return data.component.approvalState;
						}},
						{name: 'approvedDts', mapping: function(data){
							return data.component.approvedDts;
						}},
						{name: 'componentId', mapping: function(data){
							return data.component.componentId;
						}},
						{name: 'componentType', mapping: function(data){
							return data.component.componentType;
						}},
						{name: 'createDts', mapping: function(data){
							return data.component.createDts;
						}},
						{name: 'createUser', mapping: function(data){
							return data.component.createUser;
						}},
						{name: 'lastActivityDts', mapping: function(data){
							return data.component.lastActivityDts;
						}},
						{name: 'submittedDts', mapping: function(data){
							return data.component.submittedDts;
						}},
						{name: 'updateDts', mapping: function(data){
							return data.component.updateDts;
						}},	
						{name: 'updateUser', mapping: function(data){
							return data.component.updateUser;
						}},
						'integrationManagement'						
					],
					proxy: CoreUtil.pagingProxy({
						url: '../api/v1/resource/components/filterable',
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
					})
				});	
				
				var mergeComponentWin = Ext.create('Ext.window.Window', {
					id: 'mergeComponentWin',
					title: 'Merge',
					width: '40%',
					modal: true,
					layout: 'fit',
					items: [
						{
							xtype: 'form',
							itemId: 'mergeForm',
							layout: 'vbox',
							bodyStyle: 'padding: 10px;',
							defaults: {
								labelAlign: 'top'
							},							
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										{
											text: 'Merge',
											formBind: true,
											iconCls: 'fa fa-exchange',
											handler: function() {
												
												var mergeForm = this.up('form');
												var data = mergeForm.getValues();
												
												//check data for same id
												if (data.mergeComponentId === data.targetComponentId) {
													mergeForm.getComponent('targetComponent').markInvalid('Target Component must be different than merge component.');													
												} else {	
													mergeForm.setLoading("Merging...");
													Ext.Ajax.request({
														url: '../api/v1/resource/components/' + data.mergeComponentId + '/' + data.targetComponentId + '/merge',
														method: 'POST',
														success: function(response, opts){
															mergeForm.setLoading(false);

															Ext.getCmp('mergeComponentWin').hide();
															actionRefreshComponentGrid();
														},
														failure: function(response, opts){
															mergeForm.setLoading(false);
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
											iconCls: 'fa fa-close',
											handler: function() {
												this.up('window').hide();
											}																						
										}
									]
								}
							], 
							items: [
								{
									xtype: 'combobox',
									name: 'mergeComponentId',
									fieldLabel: 'Merge Component',
									store: maingridStore,
									valueField: 'componentId',
									width: '100%',
									displayField: 'name',
									readOnly: true
								},
								Ext.create('OSF.component.StandardComboBox', {
									name: 'targetComponentId',
									itemId: 'targetComponent',
									allowBlank: false,
									width: '100%',
									margin: '0 0 0 0',
									fieldLabel: 'Target Component',
									storeConfig: {
										url: '../api/v1/resource/components/lookup?all=true',
										autoLoad: false
									}
								})
							]
						}
					]
				});				
				
				var componentGrid = Ext.create('Ext.grid.Panel', {	
					title: 'Manage Entries <i class="fa fa-question-circle"  data-qtip="This tool allows for manipulating all data related to an entry" ></i>',
					id: 'componentGrid',
					store: maingridStore,
					columnLines: true,				
					bodyCls: 'border_accent',
					selModel: {
						   selType: 'checkboxmodel'        
					},
					plugins: 'gridfilters',
					enableLocking: true,
					columns: [
						{ text: 'Name', dataIndex: 'name', width: 275, lockable: true,
							filter: {
								type: 'string'
							}	
						},
						{ text: 'Type', align: 'center', dataIndex: 'componentType', width: 125,
							filter: {
								type: 'list'
							}	
						},
						{ text: 'Description', dataIndex: 'description', flex: 1, minWidth: 150,
						 renderer: function(value){
							return Ext.util.Format.stripTags(value);
						}},
						{ text: 'Last Activity Date', dataIndex: 'lastActivityDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Submitted Date', dataIndex: 'submittedDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },						
						{ text: 'Approval State', align: 'center', dataIndex: 'approvalState', width: 150 },
						{ text: 'Approval Date', dataIndex: 'approvedDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Active Status', align: 'center', dataIndex: 'activeStatus', width: 150 },
						{ text: 'Integration Management', dataIndex: 'integrationManagement', width: 175 },
						{ text: 'Update Date', dataIndex: 'updateDts', width: 175, hidden: true, xtype: 'datecolumn', format:'m/d/y H:i:s'},
						{ text: 'Update User', dataIndex: 'updateUser', width: 175, hidden: true },
						{ text: 'Create Date', dataIndex: 'createDts', width: 175, hidden: true, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Create User', dataIndex: 'createUser', width: 175, hidden: true },
						{ text: 'Component Id', dataIndex: 'componentId', width: 175, hidden: true }
					],
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',							
							items: [
								Ext.create('OSF.component.StandardComboBox', {
									id: 'componentGridFilter-ActiveStatus',
									emptyText: 'All',
									fieldLabel: 'Active Status',
									name: 'activeStatus',									
									listeners: {
										change: function(filter, newValue, oldValue, opts){
											actionRefreshComponentGrid();
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
													description: 'Active'
												},
												{
													code: 'I',
													description: 'Inactive'
												},
												{
													code: 'ALL',
													description: 'All'
												}
											]
										}
									}
								}),
								Ext.create('OSF.component.StandardComboBox', {
									id: 'componentGridFilter-ApprovalStatus',
									emptyText: 'All',
									fieldLabel: 'Approval State',
									name: 'approvalState',								
									listeners: {
										change: function(filter, newValue, oldValue, opts){
											actionRefreshComponentGrid();
										}
									},									
									storeConfig: {
										url: '../api/v1/resource/lookuptypes/ApprovalStatus',
										addRecords: [
											{
												code: null,
												description: 'All'
											}
										]
									}
								}),
								Ext.create('OSF.component.StandardComboBox', {
									id: 'componentGridFilter-ComponentType',
									emptyText: 'All',
									fieldLabel: 'Entry Type',
									name: 'componentType',
									valueField: 'type',
									displayField: 'label',									
									listeners: {
										change: function(filter, newValue, oldValue, opts){
											actionRefreshComponentGrid();
										}
									},									
									storeConfig: {
										url: '../api/v1/resource/componenttypes',
										model: undefined,										
										fields: [
											'type',
											'label'
										],
										addRecords: [
											{
												type: null,
												label: 'All'
											}
										]
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
									iconCls: 'fa fa-2x fa-refresh',
									handler: function () {
										actionRefreshComponentGrid();
									}
								}, 
								{
									xtype: 'tbseparator'
								}, 
								{
									text: 'Add',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-plus',
									handler: function () {
										
									}
								},
								{
									xtype: 'tbseparator'
								}, 								
								{
									text: 'Edit',
									id: 'lookupGrid-tools-edit',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-edit',
									disabled: true,
									handler: function () {
										
									}								
								},
								{
									text: 'View',
									id: 'lookupGrid-tools-preview',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-binoculars',
									disabled: true,
									handler: function () {
										actionPreviewComponent(Ext.getCmp('componentGrid').getSelection()[0].get('componentId'));
									}								
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Approve',
									id: 'lookupGrid-tools-approve',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-check',
									disabled: true,
									handler: function () {									
										actionApproveComponent();
									}									
								},
								{
									text: 'Action',
									id: 'lookupGrid-tools-action',
									scale: 'medium',																	
									disabled: true,
									menu: [
										{
											text: 'Copy',											
											iconCls: 'fa fa-copy',
											handler: function(){
												actionCopyComponent();
											}
										},
										{
											text: 'Merge',
											iconCls: 'fa fa-exchange',
											handler: function(){
												actionMergeComponent();
											}
										},
										{
											text: 'Versions',
											iconCls: 'fa fa-object-ungroup',
											handler: function(){
												actionVersions();
											}
										},
										{
											xtype: 'menuseparator'
										},
										{
											text: 'Toggle Status',
											iconCls: 'fa fa-power-off',
											handler: function(){
												actionToggleStatus();
											}
										},
										{
											xtype: 'menuseparator'
										},
										{
											text: 'Delete',
											cls: 'alert-danger',
											iconCls: 'fa fa-trash',
											handler: function(){
												actionDeleteComponent();
											}											
										}
									]																		
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Import',
									iconCls: 'fa fa-2x fa-upload',
									scale: 'medium',
									handler: function () {
										
									}
								}
							]
						}
					],
					listeners: {
						itemdblclick: function(grid, record, item, index, e, opts){
							actionEditCompomentForm(record);
						},
						selectionchange: function(grid, record, index, opts){
							checkComponetGridTools();
						}
					},
					bbar: Ext.create('Ext.PagingToolbar', {
						store: maingridStore,
						displayInfo: true,
						displayMsg: 'Displaying Entries {0} - {1} of {2}',
						emptyMsg: "No entries to display",
						items: [
							{
								xtype: 'tbseparator'
							},
							{
								text: 'Export',
								id: 'lookupGrid-tools-export',
								iconCls: 'fa fa-2x fa-download',
								scale: 'medium',
								disabled: true,
								handler: function () {
									actionExportComponents();
								}
							}
						]
					})
				});
				
				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [
						componentGrid
					]
				});
				
				var checkComponetGridTools = function() {
					if (componentGrid.getSelectionModel().getCount() === 1) {
						Ext.getCmp('lookupGrid-tools-export').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-edit').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-preview').setDisabled(false);
						
						var approvalState = Ext.getCmp('componentGrid').getSelection()[0].get('approvalState');
						if (approvalState !== 'A') {
							Ext.getCmp('lookupGrid-tools-approve').setDisabled(false);
						} else {
							Ext.getCmp('lookupGrid-tools-approve').setDisabled(true);
						}
						Ext.getCmp('lookupGrid-tools-action').setDisabled(false);						
					} else if (componentGrid.getSelectionModel().getCount() > 1){
						Ext.getCmp('lookupGrid-tools-export').setDisabled(false);
						
						Ext.getCmp('lookupGrid-tools-edit').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-preview').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-approve').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-action').setDisabled(true);
					} else {
						Ext.getCmp('lookupGrid-tools-export').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-edit').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-preview').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-approve').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-action').setDisabled(true);
					}
				};
				
				var actionRefreshComponentGrid = function() {
					Ext.getCmp('componentGrid').getStore().load({
						params: {
							status: Ext.getCmp('componentGridFilter-ActiveStatus').getValue() ? Ext.getCmp('componentGridFilter-ActiveStatus').getValue() : 'ALL',
							approvalState: Ext.getCmp('componentGridFilter-ApprovalStatus').getValue() ? Ext.getCmp('componentGridFilter-ApprovalStatus').getValue() : 'ALL',
							componentType: Ext.getCmp('componentGridFilter-ComponentType').getValue() ? Ext.getCmp('componentGridFilter-ComponentType').getValue() : 'ALL'
						}
					});
				};
				
				var actionApproveComponent = function() {
					Ext.getCmp('componentGrid').setLoading(true);
					var componentId = Ext.getCmp('componentGrid').getSelection()[0].get('componentId');
					Ext.Ajax.request({
						url: '../api/v1/resource/components/' + componentId + '/approve',
						method: 'PUT',
						success: function(response, opts){
							Ext.getCmp('componentGrid').setLoading(false);
							actionRefreshComponentGrid();
						},
						failure: function(response, opts){
							Ext.getCmp('componentGrid').setLoading(false);
						}
					});
				};
				
				var actionToggleStatus = function(){
					Ext.getCmp('componentGrid').setLoading(true);
					var componentId = Ext.getCmp('componentGrid').getSelection()[0].get('componentId');
					var currentStatus = Ext.getCmp('componentGrid').getSelection()[0].get('activeStatus');
					
					var method = 'PUT';
					var urlEnd = '/activate';
					if (currentStatus === 'A') {
						method = 'DELETE';
						urlEnd = '';
					}					
					Ext.Ajax.request({
						url: '../api/v1/resource/components/' + componentId + urlEnd,
						method: method,
						success: function(response, opts){
							Ext.getCmp('componentGrid').setLoading(false);
							actionRefreshComponentGrid();
						},
						failure: function(response, opts){
							Ext.getCmp('componentGrid').setLoading(false);
						}
					});					
				};
				
				var actionDeleteComponent = function() {
					var componentId = Ext.getCmp('componentGrid').getSelection()[0].get('componentId');
					var name = Ext.getCmp('componentGrid').getSelection()[0].get('name');
					Ext.Msg.show({
						title: 'Delete Component?',
						message: 'Are you sure you want to delete:  ' + name +' ?',
						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						fn: function(btn) {
							if (btn === 'yes') {
								Ext.getCmp('componentGrid').setLoading(true);
								Ext.Ajax.request({
									url: '../api/v1/resource/components/' + componentId + '/cascade',
									method: 'DELETE',
									success: function(response, opts) {
										Ext.getCmp('componentGrid').setLoading(false);
										actionRefreshComponentGrid();
									},
									failure: function(response, opts) {
										Ext.getCmp('componentGrid').setLoading(false);
									}
								});
							} 
						}
					});
				};
				
				var actionCopyComponent = function() {
					Ext.getCmp('componentGrid').setLoading('Copying please wait...');
					var componentId = Ext.getCmp('componentGrid').getSelection()[0].get('componentId');
					Ext.Ajax.request({
						url: '../api/v1/resource/components/' + componentId + '/copy',
						method: 'POST',
						success: function(response, opts){
							Ext.getCmp('componentGrid').setLoading(false);
							actionRefreshComponentGrid();
						},
						failure: function(response, opts){
							Ext.getCmp('componentGrid').setLoading(false);
						}
					});				
				};
				
				var actionMergeComponent = function() {
					Ext.getCmp('mergeComponentWin').show();
					Ext.getCmp('mergeComponentWin').getComponent('mergeForm').reset(true);
					
					var record = Ext.create('Ext.data.Model', {
						fields: [
							'mergeComponentId',
							'targetComponentId'
						]
					});
					record.set('mergeComponentId', Ext.getCmp('componentGrid').getSelection()[0].get('componentId'));
					
					Ext.getCmp('mergeComponentWin').getComponent('mergeForm').loadRecord(record);
					Ext.getCmp('mergeComponentWin').getComponent('mergeForm').getComponent('targetComponent').getStore().load();
				};
				
				var actionVersions = function() {
					versionWin.show();
					
					var componentId = Ext.getCmp('componentGrid').getSelection()[0].get('componentId');
					Ext.getCmp('versionGrid').getStore().load({
						url: '../api/v1/resource/components/' + componentId + '/versionhistory'
					});
					
					//load current verison
					versionLoadCurrent();
				};
				
				var versionLoadCurrent = function() {
					var componentId = Ext.getCmp('componentGrid').getSelection()[0].get('componentId');
					Ext.Ajax.request({
						url: '../api/v1/resource/components/' + componentId + '/detail',
						success: function(response, opts) {
							var data = Ext.decode(response.responseText);
							Ext.getCmp('versionWin-currentVersionPanel').update(data);								
							Ext.getCmp('versionWin-currentVersionPanel').setTitle('Current Version - ' + data.recordVersion);
							Ext.getCmp('versionWin-snapshotVersionPanel').update(undefined);
							Ext.getCmp('versionWin-snapshotVersionPanel').setTitle('Selected Version');	
						}
					});					
				};
				
				var actionExportComponents = function() {
					
					var ids = "";
					Ext.Array.each(componentGrid.getSelection(), function(record){
						
						ids = ids + '<input type="hidden" name="id" value="' + record.get('componentId') +'" />';
						
					});
					document.getElementById('exportFormIds').innerHTML = ids;					
					document.exportForm.submit();
				};
				
				var actionPreviewComponent = function(id){	
					previewComponentWin.show();	
					previewContents.load('../single?id=' + id +'&hideNav=true');
					previewCheckButtons();
				};

			});			
			
		</script>

	</stripes:layout-component>
</stripes:layout-render>