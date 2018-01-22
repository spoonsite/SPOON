<%-- 
    Document   : evaluationTemplates
    Created on : Oct 11, 2016, 2:29:27 PM
    Author     : dshurtleff
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
			var getEvaluations = function(parentWindow, form, saveCallback)
			{
				var selectEvaluationWin = Ext.create('Ext.window.Window', {
					title: 'Select Evaluations',
					iconCls: 'fa fa-lg fa-edit icon-small-vertical-correction',
					modal: true,
					width: 1050,
					maxHeight: '80%',
					layout: 'fit',
					closeAction: 'destroy',
					items: [
					Ext.create('Ext.grid.Panel', {
						id: 'evaluationGrid',
						title: 'Update Evaluation <i class="fa fa-question-circle"  data-qtip="Updating evaluations may result in a loss of work."></i>',
						columnLines: true,
						selModel: {
							selType: 'checkboxmodel'
						},
						store: {
							id: 'evaluationGridStore',
							autoLoad: false,
							remoteSort: true,
							sorters: [
								new Ext.util.Sorter({
									property: 'componentName',
									direction: 'ASC'
								})
							],				
							proxy: {
								// paging will not work as you won't be able to select items on different pages
								type: 'ajax',
								url: 'api/v1/resource/evaluations?published=false',
								reader: {
									type: 'json',
									rootProperty: 'data',
									totalProperty: 'totalNumber'
								}
							}
						},				
						columns: [
							{ text: 'Entry Name', dataIndex: 'componentName', flex: 1},
							{ text: 'Version', dataIndex: 'version', align: 'center', width: 175 },
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
							}
						]			
					})
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [
							{
								text: 'Update',
								iconCls: 'fa fa-2x fa-check icon-button-color-save icon-vertical-correction-edit',
								width: '110px',
								scale: 'medium',
								handler: function() {
									var evaluationIdsToUpdate = [];
									var rows = Ext.getCmp('evaluationGrid').getSelectionModel().getSelection();
									Ext.Array.each(rows, function (item) {
										evaluationIdsToUpdate.push(item.data.evaluationId);
									});
									
									var win = this.up('window');												
									win.close();
									if (typeof saveCallback === "function") {
										saveCallback(parentWindow, form, evaluationIdsToUpdate);
									}
								}
							},
							{
								xtype: 'tbfill'
							},
							{
								text: 'Skip',									
								iconCls: 'fa fa-2x fa-close icon-button-color-warning icon-vertical-correction',
								scale: 'medium',
								handler: function() {
									var win = this.up('window');												
									win.close();
									if (typeof saveCallback === "function") {
										saveCallback(parentWindow, form);
									}
								}										
							}]
						}
					]
				});
				var data = form.getValues();
				if(data.templateId)
				{
					Ext.getCmp('evaluationGrid').getStore().load({
						url: 'api/v1/resource/evaluations?published=false&templateId=' + data.templateId
					 });
				 }
				selectEvaluationWin.show();	
			};
		
			var addEditWindow = Ext.create('Ext.window.Window', {
				title: 'Add/Edit Evaluation Template',
				iconCls: 'fa fa-edit',
				modal: true,
				width: '75%',
				height: '75%',
				maximizable: true,
				layout: 'fit',
				items: [
					{
						xtype: 'form',
						itemId: 'form',
						layout: 'border',						
						dockedItems: [
							{
								xtype: 'toolbar',
								dock: 'bottom',
								items: [
									{
										text: 'Save',
										formBind: true,
										iconCls: 'fa fa-2x fa-save icon-button-color-save icon-vertical-correction-edit',
										width: '110px',
										scale: 'medium',
										handler: function() {
											var saveTemplate = function(win, form, evaluationIdsToUpdate) {
												var evaluationData = form.getValues();

												evaluationData.sectionTemplates = [];											
												Ext.getCmp('sectionsInTemplate').getStore().each(function(item){
													evaluationData.sectionTemplates.push({
														sectionTemplateId: item.get('templateId')
													});
												});

												var method = 'POST';
												var update = '';
												if (evaluationData.templateId) {
													update = '/' + evaluationData.templateId;
													method = 'PUT';
												}
												var data = {
													evaluationTemplate: evaluationData,
													evaluationIdsToUpdate: evaluationIdsToUpdate
												};
												
												CoreUtil.submitForm({
													url: 'api/v1/resource/evaluationtemplates' + update,
													method: method,
													data: data,
													form: form,
													success: function(){
														actionRefresh();
														form.reset();
														win.close();
													}
												});	
											};
											
											var form = this.up('form');
											var win = this.up('window');
											if(Ext.getCmp('updatePending').getRawValue())
											{
												getEvaluations(win, form, saveTemplate);
											}
											else
											{
												saveTemplate(win, form);
											}
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Cancel',									
										iconCls: 'fa fa-2x fa-close icon-button-color-warning icon-vertical-correction',
										scale: 'medium',
										handler: function() {
											this.up('window').close();
										}										
									}							
								]
							}
						],
						items: [
							{
								xtype: 'hidden',
								name: 'templateId'
							},
							{
								xtype: 'panel',
								region: 'north',
								layout: 'anchor',
								bodyStyle: 'padding: 10px;',
								defaults: {
									labelAlign: 'top',
									labelSeparator: '',
									width: '100%'
								},
								items: [
									{
										xtype: 'textfield',
										name: 'name',
										fieldLabel: 'Name <span class="field-required" />',
										allowBlank: false,
										maxLength: 255										
									},
									{
										xtype: 'textfield',
										name: 'description',
										fieldLabel: 'Description <span class="field-required" />',
										allowBlank: false,
										maxLength: 255										
									},
									Ext.create('OSF.component.StandardComboBox', {
										name: 'checklistTemplateId',	
										allowBlank: false,									
										margin: '0 0 10 0',
										width: '100%',
										fieldLabel: 'Checklist Template <span class="field-required" />',
										valueField: 'checklistTemplateId',
										displayField: 'name',
										forceSelection: true,								
										editable: false,
										typeAhead: false,	
										storeConfig: {
											url: 'api/v1/resource/checklisttemplates'
										}
									}),
									{
										xtype: 'checkboxfield',
										id: 'updatePending',
										boxLabel: 'Update unpublished Evaluations'		
									}									
								]
							},
							{
								xtype: 'panel',
								region: 'center',
								layout: {
									type: 'hbox',									
									align: 'stretch'
								},
								items: [
									{
										xtype: 'grid',
										title: 'Section Pool - <span class="alert-warning">drag to add <i class="fa fa-lg fa-arrow-right"></i></span>', 
										id: 'sectionPool',
										width: '50%',
										margin: '0 5 0 0',
										columnLines: true,
										store: {
											autoLoad: false,
											sorters: [
												new Ext.util.Sorter({
													property: 'qid',
													direction: 'ASC'
												})
											],
											proxy: {
												type: 'ajax',
												url: 'api/v1/resource/contentsectiontemplates',
												reader: {
													type: 'json',
													rootProperty: 'data',
													totalProperty: 'totalNumber'
												}												
											}
										},
										viewConfig: {
											plugins: {
												ptype: 'gridviewdragdrop',
												dragText: 'Drag and drop to add to template'
											}
										},										
										columns: [
											{ text: 'Template Name', dataIndex: 'name', align: 'center', width: 175 },
											{ text: 'Description', dataIndex: 'description', flex: 1,
												renderer: function(value, metadata, record) {
													return Ext.util.Format.stripTags(value);
												}										
											}
										]
									},
									{
										xtype: 'grid',
										id: 'sectionsInTemplate',
										title: 'Sections In Template - <span class="alert-warning"><i class="fa fa-lg fa-arrow-left"></i> drag to remove</span>',
										width: '50%',
										columnLines: true,
										store: {											
										},
										viewConfig: {
											plugins: {
												ptype: 'gridviewdragdrop',
												dragText: 'Drag and drop to delete from template'												
											},
											listeners: {
												drop: function(node, data, overModel, dropPostition, opts){													
												}
											}
										},										
										columns: [
											{ text: 'Template Name', dataIndex: 'name', align: 'center', width: 175 },
											{ text: 'Description', dataIndex: 'description', flex: 1,
												renderer: function(value, metadata, record) {
													return Ext.util.Format.stripTags(value);
												}										
											}
										]
									}
								]
							}
						]
					}
				]
			});
			
			var templateGrid = Ext.create('Ext.grid.Panel', {
				id: 'templateGrid',
				title: 'Manage Evaluation Templates <i class="fa fa-question-circle"  data-qtip="Top level template used to create evaluations."></i>',
				columnLines: true,
				store: {
					autoLoad: true,
					fields: [
						{ name: 'createDts', 
							type: 'date',
							dateFormat: 'c'							
						},
						{ name: 'updateDts',
							type: 'date',
							dateFormat: 'c'				
						}							
					],
					proxy: {
						type: 'ajax',
						url: 'api/v1/resource/evaluationtemplates'
					},
					listeners: {
						beforeLoad: function(store, operation, eOpts){
							store.getProxy().extraParams = {
								status: Ext.getCmp('filterActiveStatus').getValue()									
							};
						}
					}					
				},
				listeners: {
					itemdblclick: function(grid, record, item, index, e, opts){
						actionAddEdit(record);
					},						
					selectionchange: function(selModel, selected, opts) {
						var tools = Ext.getCmp('templateGrid').getComponent('tools');

						if (selected.length > 0) {															
							tools.getComponent('edit').setDisabled(false);							
							tools.getComponent('togglestatus').setDisabled(false);
							tools.getComponent('delete').setDisabled(false);
						} else {
							tools.getComponent('edit').setDisabled(true);							
							tools.getComponent('togglestatus').setDisabled(true);
							tools.getComponent('delete').setDisabled(true);
						}
					}
				},					
				columns: [
					{ text: 'Name', dataIndex: 'name', flex: 1},
					{ text: 'Description', dataIndex: 'description', align: 'center', width: 225 },					
					{ text: 'Create Date', dataIndex: 'createDts', xtype: 'datecolumn', format:'m/d/y H:i:s',  width: 175 },
					{ text: 'Create User', dataIndex: 'createUser', width: 175 },
					{ text: 'Update Date', dataIndex: 'updateDts', xtype: 'datecolumn', format:'m/d/y H:i:s',  width: 175 },
					{ text: 'Update User', dataIndex: 'updateUser', width: 175 }
				],
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
						itemId: 'tools',
						items: [
							{
								text: 'Refresh',
								iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
								scale: 'medium',
								handler: function(){
									actionRefresh();
								}
							},
							{
								xtype: 'tbseparator'
							},
							{
								text: 'Add',
								iconCls: 'fa fa-2x fa-plus icon-button-color-save',
								width: '100px',
								scale: 'medium',
								handler: function(){
									actionAddEdit();
								}
							},							
							{
								text: 'Edit',
								iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
								itemId: 'edit',
								width: '100px',
								disabled: true,									
								scale: 'medium',
								handler: function(){
									var record = Ext.getCmp('templateGrid').getSelectionModel().getSelection()[0];
									actionAddEdit(record);
								}
							},
							{
								xtype: 'tbseparator'
							},
							{
								text: 'Toggle Status',
								iconCls: 'fa fa-2x fa-power-off icon-button-color-default',
								itemId: 'togglestatus',
								disabled: true,								
								scale: 'medium',
								handler: function(){
									var record = Ext.getCmp('templateGrid').getSelectionModel().getSelection()[0];
									actionToggleStatus(record);
								}
							},							
							{
								xtype: 'tbfill'
							},																				
							{
								text: 'Delete',
								iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
								itemId: 'delete',
								disabled: true,									
								scale: 'medium',
								handler: function(){
									var record = Ext.getCmp('templateGrid').getSelectionModel().getSelection()[0];
									actionDelete(record);
								}
							}					
							
						]
					}
				]				
			});
			
			addComponentToMainViewPort(templateGrid);				
			
			var actionRefresh = function() {
				Ext.getCmp('templateGrid').getStore().reload();					
			};
		
			var actionAddEdit = function(record) {
				addEditWindow.show();
				addEditWindow.getComponent('form').reset();
				
				Ext.getCmp('sectionsInTemplate').getStore().removeAll();
				if (record) {
					addEditWindow.getComponent('form').loadRecord(record);
					
					Ext.getCmp('sectionPool').getStore().load(function(records, operation, success) {
						var recordsInTemplate = [];
						var recordsAvaliable = [];
						Ext.Array.each(records, function(section) {
							var sectionInTemplate = false;
							
							if (record.get('sectionTemplates')) {
								Ext.Array.each(record.get('sectionTemplates'), function(template) {
									if (template.sectionTemplateId === section.get('templateId')) {
										sectionInTemplate = true;
									}
								});
							}
							
							if (sectionInTemplate) {
								recordsInTemplate.push(section);
							} else {
								recordsAvaliable.push(section);
							}
						});
						Ext.getCmp('sectionPool').getStore().loadData(recordsAvaliable);
						Ext.getCmp('sectionsInTemplate').getStore().loadData(recordsInTemplate);
					});					
				} else {
					Ext.getCmp('sectionPool').getStore().load();
				}
			};
			
			var actionToggleStatus = function(record) {
				Ext.getCmp('templateGrid').setLoading("Updating Status...");
				var templateId = Ext.getCmp('templateGrid').getSelection()[0].get('templateId');
				var currentStatus = Ext.getCmp('templateGrid').getSelection()[0].get('activeStatus');

				var method = 'PUT';
				var urlEnd = '/activate';
				if (currentStatus === 'A') {
					method = 'DELETE';
					urlEnd = '';
				}					
				Ext.Ajax.request({
					url: 'api/v1/resource/evaluationtemplates/' + templateId + urlEnd,
					method: method,
					callback: function(){
						Ext.getCmp('templateGrid').setLoading(false);
					},
					success: function(response, opts){						
						actionRefresh();
					}
				});				
			};				
			
			var actionDelete = function(record) {
				Ext.Msg.show({
					title:'Delete Evaluation Template?',
					iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
					message: 'Are you sure you want to delete this template?',
					buttons: Ext.Msg.YESNO,
					icon: Ext.Msg.QUESTION,
					fn: function(btn) {
						if (btn === 'yes') {
							templateGrid.setLoading('Deleting...');
							Ext.Ajax.request({
								url: 'api/v1/resource/evaluationtemplates/' + record.get('templateId') + '?force=true',
								method: 'DELETE',
								callback: function(){
									templateGrid.setLoading(false);
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
