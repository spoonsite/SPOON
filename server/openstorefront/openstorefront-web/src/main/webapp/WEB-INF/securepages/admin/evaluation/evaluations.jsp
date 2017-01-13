<%-- 
    Document   : evaluations
    Created on : Oct 11, 2016, 2:28:31 PM
    Author     : dshurtleff
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
	
	<stripes:layout-render name="../../../../layout/adminheader.jsp">		
	</stripes:layout-render>		
		
	<script src="scripts/component/evaluationForm.js?v=${appVersion}" type="text/javascript"></script>	
		
	<script type="text/javascript">
		/* global Ext, CoreUtil */
		Ext.onReady(function(){	
			
		
			var createEvaluationWin = Ext.create('Ext.window.Window', {
				title: 'Create Evaluation',
				modal: true,
				width: 500,
				height: 550,
				layout: 'fit',
				items: [
					{
						xtype: 'form',
						itemId: 'form',
						bodyStyle: 'padding: 10px;',
						scrollable: true,
						dockedItems: [
							{
								xtype: 'toolbar',
								dock: 'bottom',
								items: [
									{
										text: 'Create',
										iconCls: 'fa fa-save',
										handler: function(){
											var form = this.up('form');
											var data = form.getValues();
											
											CoreUtil.submitForm({
												url: 'api/v1/resource/evaluations',
												method: 'POST',
												data: data,
												form: form,
												success: function(){
													actionRefresh();
													form.reset();
													createEvaluationWin.close();
												}
											});
											
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Close',
										iconCls: 'fa fa-close',
										handler: function(){
											createEvaluationWin.close();
										}										
									}
								]
							}
						],
						layout: 'anchor',
						defaults: {
							labelAlign: 'top',
							labelSeparator: '',							
							width: '100%'
						},
						items: [							
							{
								xtype: 'combobox',
								fieldLabel: 'Entry <span class="field-required" />',
								name: 'componentId',
								displayField: 'description',
								valueField: 'code',								
								emptyText: 'Select an entry',
								allowBlank: false,
								forceSelection: true,
								store: {									
									autoLoad: true,
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/components/lookup?approvalState=ALL'
									}
								}						
							},
							{
								xtype: 'combobox',
								fieldLabel: 'Evaluation Template <span class="field-required" />',
								name: 'templateId',
								displayField: 'description',
								valueField: 'templateId',								
								emptyText: 'Select a Template',								
								allowBlank: false,
								typeAhead: false,
								editable: false,
								store: {									
									autoLoad: true,
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/evaluationtemplates'
									}
								}						
							},							
							{
								xtype: 'textfield',
								fieldLabel: 'Version <span class="field-required" />',
								name: 'version',								
								allowBlank: false,
								maxLength: 255
							},
							{
								xtype: 'combo',
								itemId: 'workflowStatus',
								name: 'workflowStatus',
								allowBlank: false,															
								editable: false,
								typeAhead: false,	
								valueField: 'code',
								displayField: 'description',
								fieldLabel: 'Status <span class="field-required" />',								
								store: {
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/lookuptypes/WorkflowStatus'
									},
									listeners: {
										load: function(store, records) {											
											if (records && records.length > 0) {												
												var field = createEvaluationWin.getComponent('form').getComponent('workflowStatus');
												field.setValue(records[0].get('code'));
											}
										}										
									}
								}
							},							
							{
								xtype: 'combobox',
								name: 'assignedGroup',
								fieldLabel: 'Assign to Group',
								displayField: 'description',
								valueField: 'code',								
								emptyText: 'Unassigned'
								//TODO: add Group
							},
							{
								xtype: 'combobox',
								name: 'assignedUser',
								fieldLabel: 'Assign to User',
								displayField: 'description',
								valueField: 'code',								
								emptyText: 'Unassigned',								
								store: {									
									autoLoad: true,
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/userprofiles/lookup'
									}
								}								
							},
							{
								xtype: 'checkbox',
								name: 'allowNewSections',
								boxLabel: 'Allow Adding Sections'
							},
							{
								xtype: 'checkbox',
								name: 'allowNewSubSections',
								boxLabel: 'Allow Adding Sub-Sections'
							}
						]
					}
				]
			});			
			
			var evaluationGrid = Ext.create('Ext.grid.Panel', {
				id: 'evaluationGrid',
				title: 'Manage Evaluation <i class="fa fa-question-circle"  data-qtip="Evaluations allow for creating detailed consumer report for an entry."></i>',
				columnLines: true,
				store: {
					id: 'evaluationGridStore',
					autoLoad: true,
					pageSize: 250,
					remoteSort: true,
					sorters: [
						new Ext.util.Sorter({
							property: 'componentName',
							direction: 'ASC'
						})
					],						
					fields: [
						{ 
							name: 'createDts', 
							type: 'date',
							dateFormat: 'c'							
						},
						{ 
							name: 'updateDts',
							type: 'date',
							dateFormat: 'c'				
						}							
					],
					proxy: CoreUtil.pagingProxy({
						type: 'ajax',
						url: 'api/v1/resource/evaluations',
						reader: {
							type: 'json',
							rootProperty: 'data',
							totalProperty: 'totalNumber'
						}
					}),										
					listeners: {
						beforeLoad: function(store, operation, eOpts){
							store.getProxy().extraParams = {
								'status': Ext.getCmp('filterActiveStatus').getValue(),
								'workflowStatus': Ext.getCmp('filterWorkflowStatus').getValue()
							};
						}
					}
				},				
				columns: [
					{ text: 'Entry Name', dataIndex: 'componentName', flex: 1},
					{ text: 'Version', dataIndex: 'version', align: 'center', width: 225 },
					{ text: 'Published', dataIndex: 'published', align: 'center', width: 175,
						renderer: function(value) {
							if (value) {
								return '<span class="fa fa-check text-success"></span>';
							} else {
								return '<span class="fa fa-close text-danger"></span>';
							}
						}
					},
					{ text: 'Assigned Group', dataIndex: 'assignedGroup', align: 'center', width: 175 },					
					{ text: 'Assigned User', dataIndex: 'assignedUser', align: 'center', width: 175},
					{ text: 'Status', dataIndex: 'workflowStatus', align: 'center', width: 175,
						renderer: function(value, meta, record) {
							if (value === 'INPROGRESS') {
								meta.tdCls = 'alert-warning';
							} else if (value === 'WAIT') {
								meta.tdCls = 'alert-info';
							} else if (value === 'COMPLETE') {
								meta.tdCls = 'alert-success';
							} else if (value === 'HOLD') {
								meta.tdCls = 'alert-danager';
							}
							
							return record.get('workflowStatusDescription');
						}
					},					
					{ text: 'Create User', dataIndex: 'createUser', width: 175, hidden: true  },
					{ text: 'Update Date', dataIndex: 'updateDts', xtype: 'datecolumn', format:'m/d/y H:i:s',  width: 175 },
					{ text: 'Update User', dataIndex: 'updateUser', width: 175 }
				],
				listeners: {
					itemdblclick: function(grid, record, item, index, e, opts){
						actionAddEditQuestion(record);
					},						
					selectionchange: function(selModel, selected, opts) {
						var tools = Ext.getCmp('evaluationGrid').getComponent('tools');

						if (selected.length > 0) {	
							tools.getComponent('action').setDisabled(false);
							tools.getComponent('edit').setDisabled(false);	
							tools.getComponent('togglestatus').setDisabled(false);							
						} else {							
							tools.getComponent('action').setDisabled(true);
							tools.getComponent('edit').setDisabled(true);														
							tools.getComponent('togglestatus').setDisabled(true);
						}
						
						if (selected.length > 0 && selected[0].get('published')) {
							Ext.getCmp('publish').setDisabled(true);
							tools.getComponent('edit').setDisabled(false);
							Ext.getCmp('unpublish').setDisabled(false);
						} else {
							Ext.getCmp('publish').setDisabled(false);
							Ext.getCmp('unpublish').setDisabled(true);
						}
						
					}
				},				
				dockedItems: [
					{
						xtype: 'toolbar',
						dock: 'top',						
						items: [
							Ext.create('OSF.component.StandardComboBox', {
								id: 'filterActiveStatus',									
								emptyText: 'Active',
								value: 'A',
								fieldLabel: 'Active Status',
								name: 'activeStatus',									
								typeAhead: false,
								editable: false,
								width: 200,							
								listeners: {
									change: function(filter, newValue, oldValue, opts){
										actionRefresh();
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
											}
										]
									}
								}
							}),
							Ext.create('OSF.component.StandardComboBox', {
								id: 'filterWorkflowStatus',
								name: 'workflowStatus',								
								allowBlank: false,								
								margin: '0 0 5 0',
								editable: false,
								typeAhead: false,
								emptyText: 'All',
								width: 200,	
								fieldLabel: 'Workflow Status',								
								storeConfig: {
									url: 'api/v1/resource/lookuptypes/WorkflowStatus',
									addRecords: [
										{
											code: null,
											description: 'All'
										}
									]
								},
								listeners: {
									change: function(filter, newValue, oldValue, opts){
										actionRefresh();
									}
								}			
							})
						]
					},					
					{
						dock: 'top',
						itemId: 'tools',
						xtype: 'toolbar',
						items: [
							{
								text: 'Refresh',
								iconCls: 'fa fa-2x fa-refresh',
								scale: 'medium',
								handler: function(){
									actionRefresh();
								}
							},
							{
								xtype: 'tbseparator'
							},
							{
								text: 'Create',
								iconCls: 'fa fa-2x fa-plus text-success',
								scale: 'medium',
								handler: function(){
									addEditEvaluation();
								}
							},							
							{
								text: 'Edit',
								iconCls: 'fa fa-2x fa-edit',
								itemId: 'edit',
								disabled: true,								
								scale: 'medium',
								handler: function(){
									var record = Ext.getCmp('evaluationGrid').getSelectionModel().getSelection()[0];
									addEditEvaluation(record);
								}
							},
							{
								xtype: 'tbseparator'
							},
							{
								text: 'Action',
								itemId: 'action',
								disabled: true,									
								scale: 'medium',
								menu: [
									{
										text: 'Publish',
										id: 'publish',
										iconCls: 'fa fa-check text-success',
										handler: function(){
											var record = Ext.getCmp('evaluationGrid').getSelectionModel().getSelection()[0];
											publish(record);
										}										
									},
									{
										xtype: 'menuseparator'
									},									
									{
										text: 'Copy',										
										iconCls: 'fa fa-copy',
										handler: function(){
											var record = Ext.getCmp('evaluationGrid').getSelectionModel().getSelection()[0];
											copy(record);
										}										
									},
									{
										xtype: 'menuseparator'
									},
									{
										text: 'Assign Group',
										iconCls: 'fa fa-users',
										handler: function(){
											var record = Ext.getCmp('evaluationGrid').getSelectionModel().getSelection()[0];
											actionAssignGroup(record);
										}										
									},	
									{
										text: 'Assign User',
										iconCls: 'fa fa-user',
										handler: function(){
											var record = Ext.getCmp('evaluationGrid').getSelectionModel().getSelection()[0];
											actionAssignUser(record);
										}										
									},									
									{
										xtype: 'menuseparator'
									},
									{
										text: 'Unpublish',
										id: 'unpublish',
										iconCls: 'fa fa-close text-danger',
										handler: function(){
											var record = Ext.getCmp('evaluationGrid').getSelectionModel().getSelection()[0];
											unpublish(record);
										}										
									},
									{
										xtype: 'menuseparator'
									},
									{
										text: 'Delete',
										iconCls: 'fa fa-close text-danger',
										cls: 'alert-danger',
										handler: function(){
											var record = Ext.getCmp('evaluationGrid').getSelectionModel().getSelection()[0];
											actionDelete(record);
										}										
									}									
								]
							},							
							{
								xtype: 'tbfill'
							},																				
							{
								text: 'Toggle Staus',
								iconCls: 'fa fa-2x fa-power-off text-warning',
								itemId: 'togglestatus',
								disabled: true,									
								scale: 'medium',
								handler: function(){
									var record = Ext.getCmp('evaluationGrid').getSelectionModel().getSelection()[0];
									actionToggleStatus(record);
								}
							}								
						]
					},
					{
						xtype: 'pagingtoolbar',
						dock: 'bottom',
						store: 'evaluationGridStore',
						displayInfo: true
					}
				]				
			});
			
			addComponentToMainViewPort(evaluationGrid);	
			
			var actionRefresh = function(){
				evaluationGrid.getStore().reload();
			};
			
			var addEditEvaluation = function(record){
				
				if (record) {
					
					evaluationGrid.setLoading('Checking evaluation entry...');
					Ext.Ajax.request({
						url: 'api/v1/resource/evaluations/' + record.get('evaluationId') + '/checkentry',
						method: 'PUT',
						callback: function(){
							evaluationGrid.setLoading(false);
						},
						success: function(response, opts) {
							var evalformWin = Ext.create('OSF.component.EvaluationFormWindow', {
								title: 'Evaluation Form - ' + record.get('componentName')
							});
							evalformWin.show();
							
							var evaluation = Ext.decode(response.responseText);
							evalformWin.loadEval(record.get('evaluationId'), evaluation.componentId, function(){
								actionRefresh();
							});
							
							if (evaluation.componentId !== record.get('componentId')) {
								actionRefresh();
							}														
						}
					});					
				} else {
					createEvaluationWin.show();
					createEvaluationWin.getComponent('form').getComponent('workflowStatus').getStore().load();
					createEvaluationWin.getComponent('form').reset();
				}
			};
			
			var actionAssignGroup = function(record) {
				
			};

			var actionAssignUser = function(record) {
				
			};

			var copy = function(record) {
				evaluationGrid.setLoading('Copying...');
				Ext.Ajax.request({
					url: 'api/v1/resource/evaluations/' + record.get('evaluationId') + '/copy',
					method: 'POST',
					callback: function(){
						evaluationGrid.setLoading(false);
					},
					success: function(response, opts){
						actionRefresh();
					}
				});	
			};

			var publish = function(record){
				Ext.Msg.show({
					title:'Publish Evaluation?',
					message: 'Are you sure you want to PUBLISH this evalaution?',
					buttons: Ext.Msg.YESNO,
					icon: Ext.Msg.QUESTION,
					fn: function(btn) {
						if (btn === 'yes') {
							evaluationGrid.setLoading('Publishing...');
							Ext.Ajax.request({
								url: 'api/v1/resource/evaluations/' + record.get('evaluationId') + '/publish',
								method: 'PUT',
								callback: function(){
									evaluationGrid.setLoading(false);
								},
								success: function(response, opts){
									actionRefresh();
								}
							});	
						} 
					}
				});				
			};

			var unpublish = function(record){
				Ext.Msg.show({
					title:'Unpublish Evaluation?',
					message: 'Are you sure you want to UNPUBLISH this evalaution?',
					buttons: Ext.Msg.YESNO,
					icon: Ext.Msg.QUESTION,
					fn: function(btn) {
						if (btn === 'yes') {
							evaluationGrid.setLoading('Publishing...');
							Ext.Ajax.request({
								url: 'api/v1/resource/evaluations/' + record.get('evaluationId') + '/unpublish',
								method: 'PUT',
								callback: function(){
									evaluationGrid.setLoading(false);
								},
								success: function(response, opts){
									actionRefresh();
								}
							});	
						} 
					}
				});				
			};

			var actionToggleStatus = function(record) {
				Ext.getCmp('evaluationGrid').setLoading("Updating Status...");
				var evaluationId = Ext.getCmp('evaluationGrid').getSelection()[0].get('evaluationId');
				var currentStatus = Ext.getCmp('evaluationGrid').getSelection()[0].get('activeStatus');

				var method = 'PUT';
				var urlEnd = '/activate';
				if (currentStatus === 'A') {
					method = 'DELETE';
					urlEnd = '';
				}					
				Ext.Ajax.request({
					url: 'api/v1/resource/evaluations/' + evaluationId + urlEnd,
					method: method,
					callback: function(){
						Ext.getCmp('evaluationGrid').setLoading(false);
					},
					success: function(response, opts){						
						actionRefresh();
					}
				});				
			};	
			
			var actionDelete = function(record) {
				Ext.Msg.show({
					title:'Delete Evaluation?',
					message: 'Are you sure you want to delete this evalaution?',
					buttons: Ext.Msg.YESNO,
					icon: Ext.Msg.QUESTION,
					fn: function(btn) {
						if (btn === 'yes') {
							evaluationGrid.setLoading('Deleting...');
							Ext.Ajax.request({
								url: 'api/v1/resource/evaluations/' + record.get('evaluationId') + '?force=true',
								method: 'DELETE',
								callback: function(){
									evaluationGrid.setLoading(false);
								},
								success: function(response, opts){
									actionRefresh();
								}
							});	
						} 
					}
				});					
			};			
		
		});
		
	</script>

    </stripes:layout-component>
</stripes:layout-render>	