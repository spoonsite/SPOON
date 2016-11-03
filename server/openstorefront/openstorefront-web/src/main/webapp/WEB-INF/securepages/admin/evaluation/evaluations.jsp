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
				height: 375,
				layout: 'fit',
				items: [
					{
						xtype: 'form',
						itemId: 'form',
						bodyStyle: 'padding: 10px;',
						dockedItems: [
							{
								xtype: 'toolbar',
								dock: 'bottom',
								items: [
									{
										text: 'Create',
										iconCls: 'fa fa-save',
										handler: function(){
											
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
								displayField: 'description',
								valueField: 'code',								
								emptyText: 'Select an entry',
								allowBlank: false,
								store: {									
									autoLoad: true,
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/components/lookup'
									}
								}						
							},
							{
								xtype: 'combobox',
								fieldLabel: 'Evaluation Template <span class="field-required" />',
								displayField: 'description',
								valueField: 'name',								
								emptyText: 'Select a Template',
								allowBlank: false,
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
								xtype: 'combobox',
								fieldLabel: 'Assign to Group',
								displayField: 'description',
								valueField: 'code',								
								emptyText: 'Select an group',
								allowBlank: false
								//TODO: add Group
							},
							{
								xtype: 'combobox',
								fieldLabel: 'Assign to User',
								displayField: 'description',
								valueField: 'code',								
								emptyText: 'Select an user',
								allowBlank: false,
								store: {									
									autoLoad: true,
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/userprofiles/lookup'
									}
								}								
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
								status: Ext.getCmp('filterActiveStatus').getValue()									
							};
						}
					}
				},				
				columns: [
					{ text: 'Entry Name', dataIndex: 'componentName', flex: 1},
					{ text: 'Version', dataIndex: 'version', align: 'center', width: 225 },
					{ text: 'Published', dataIndex: 'published', align: 'center', width: 175 },
					{ text: 'Assigned Group', dataIndex: 'assignedGroup', align: 'center', width: 175 },					
					{ text: 'Assigned User', dataIndex: 'assignedUser', align: 'center', width: 175},
					{ text: 'Status', dataIndex: 'status', align: 'center', width: 175},
					{ text: 'Progress', dataIndex: 'progress', align: 'center', width: 175},
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
							tools.getComponent('inactivate').setDisabled(false);							
						} else {							
							tools.getComponent('action').setDisabled(true);
							tools.getComponent('edit').setDisabled(true);														
							tools.getComponent('inactivate').setDisabled(true);
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
							}) 															
						]
					},					
					{
						dock: 'top',
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
										iconCls: 'fa fa-2x fa-check text-success',
										handler: function(){
											var record = Ext.getCmp('evaluationGrid').getSelectionModel().getSelection()[0];
											publish(record);
										}										
									},
									{
										xtype: 'menuseparator'
									},
									{
										text: 'Unpublish',
										iconCls: 'fa fa-2x fa-check text-danger',
										handler: function(){
											var record = Ext.getCmp('evaluationGrid').getSelectionModel().getSelection()[0];
											unpublish(record);
										}										
									}									
								]
							},							
							{
								xtype: 'tbfill'
							},																				
							{
								text: 'Inactivate',
								iconCls: 'fa fa-2x fa-close text-danger',
								itemId: 'inactivate',
								disabled: true,									
								scale: 'medium',
								handler: function(){
									var record = Ext.getCmp('evaluationGrid').getSelectionModel().getSelection()[0];
									inactivate(record);
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
					var evalformWin = Ext.create('OSF.component.EvaluationFormWindow', {					
					});
					evalformWin.show();
					
					//Load form
					
				} else {
					createEvaluationWin.show();
					createEvaluationWin.getComponent('form').reset();
			   }
			};
			
			var publish = function(record){
				
			};

			var unpublish = function(record){
				
			};

			var inactivate = function(record){
				
			};
		
		});
		
	</script>

    </stripes:layout-component>
</stripes:layout-render>	