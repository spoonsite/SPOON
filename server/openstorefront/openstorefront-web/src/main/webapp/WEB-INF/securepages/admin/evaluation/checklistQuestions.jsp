<%-- 
    Document   : checklistQuestion
    Created on : Oct 11, 2016, 2:30:22 PM
    Author     : dshurtleff
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
	
	<stripes:layout-render name="../../../../layout/adminheader.jsp">		
	</stripes:layout-render>		
		
	<script src="scripts/component/importWindow.js?v=${appVersion}" type="text/javascript"></script>	
		
	<script type="text/javascript">
		/* global Ext, CoreUtil */
		Ext.onReady(function(){	
			
			var importWindow = Ext.create('OSF.component.ImportWindow', {
				fileTypeValue: 'CHKQUESTIONS',	
				uploadSuccess: function(form, action) {
					actionRefreshQuestion();
				}
			});
			
			var addEditWindow = Ext.create('Ext.window.Window', {
				title: 'Add/Edit Question',
				iconCls: 'fa fa-edit',
				modal: true,
				width: '75%',
				height: '75%',
				maxizable: true,
				layout: 'fit',
				items: [
					{
						xtype: 'form',
						itemId: 'form',
						scrollable: true,
						bodyStyle: 'padding: 10px;',
						layout: 'anchor',
						defaults: {
							labelAlign: 'top',
							labelSeparator: '',
							width: '100%'
						},
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
											var form = this.up('form');
											var win = this.up('window');
											var data = form.getValues();
											
											//check question
											if (!data.question) {
												Ext.Msg.show({
													title:'Validation',
													message: 'A question is required.',
													buttons: Ext.Msg.OK,
													icon: Ext.Msg.ERROR,
													fn: function(btn) {
														form.getForm().findField('question').markInvalid('A question is required');
													}
												});
											} else {
												
												var method = 'POST';
												var update = '';
												if (data.questionId) {
													update = '/' + data.questionId;
													method = 'PUT';
												}

												CoreUtil.submitForm({
													url: 'api/v1/resource/checklistquestions' + update,
													method: method,
													data: data,
													form: form,
													success: function(){
														actionRefreshQuestion();
														form.reset();
														win.close();
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
								name: 'questionId'
							},
							{
								xtype: 'textfield',
								fieldLabel: 'QID <span class="field-required" />',
								name: 'qid',
								maxLength: 60,
								allowBlank: false								
							},
							Ext.create('OSF.component.StandardComboBox', {
								name: 'evaluationSection',									
								allowBlank: false,									
								margin: '0 0 10 0',
								width: '100%',
								fieldLabel: 'Section <span class="field-required" />',
								forceSelection: true,								
								editable: false,
								typeAhead: false,	
								storeConfig: {
									url: 'api/v1/resource/lookuptypes/EvaluationSection'
								}
							}),							
							{
								xtype: 'panel',
								html: '<b>Question</b> <span class="field-required" />'
							},
							{
								xtype: 'tinymce_textarea',
								fieldStyle: 'font-family: Courier New; font-size: 12px;',
								style: { border: '0' },
								name: 'question',								
								height: 300,
								maxLength: 4096,
								tinyMCEConfig: CoreUtil.tinymceConfig()
							},			
							{
								xtype: 'panel',
								html: '<b>Narrative</b>'
							},
							{
								xtype: 'tinymce_textarea',
								fieldStyle: 'font-family: Courier New; font-size: 12px;',
								style: { border: '0' },
								name: 'narrative',								
								height: 300,
								maxLength: 16384,
								tinyMCEConfig: CoreUtil.tinymceConfig()
							},		
							{
								xtype: 'panel',
								html: '<b>Objective</b>'
							},
							{
								xtype: 'tinymce_textarea',
								fieldStyle: 'font-family: Courier New; font-size: 12px;',
								style: { border: '0' },
								name: 'objective',								
								height: 300,
								maxLength: 16384,
								tinyMCEConfig: CoreUtil.tinymceConfig()
							},									
							{
								xtype: 'panel',
								html: '<b>Score Criteria</b>'
							},
							{
								xtype: 'tinymce_textarea',
								fieldStyle: 'font-family: Courier New; font-size: 12px;',
								style: { border: '0' },
								name: 'scoreCriteria',								
								height: 300,
								maxLength: 4096,
								tinyMCEConfig: CoreUtil.tinymceConfig()
							}					
						]
					}
				]
			});
			
			var questionGrid = Ext.create('Ext.grid.Panel', {
				id: 'questionGrid',
				title: 'Manage Checklist Questions <i class="fa fa-question-circle"  data-qtip="Manage checklist question pool; questions are tied to responses and templates."></i>',
				columnLines: true,
				store: {	
					id: 'questionGridStore',
					autoLoad: true,
					pageSize: 250,
					remoteSort: true,
					sorters: [
						new Ext.util.Sorter({
							property: 'qid',
							direction: 'ASC'
						})
					],	
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
					proxy: CoreUtil.pagingProxy({
						type: 'ajax',
						url: 'api/v1/resource/checklistquestions',
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
					{ text: 'QID', dataIndex: 'qid', align: 'center', width: 125},				
					{ text: 'Section', dataIndex: 'evaluationSection', align: 'center', width: 200,
						renderer: function(value, metadata, record) {
							return record.get('evaluationSectionDescription');
						}
					},				
					{ text: 'Question', dataIndex: 'question', flex: 1,
						renderer: function(value, metadata, record) {
							return Ext.util.Format.stripTags(value);
						}
					},	
					{ text: 'Create Date', dataIndex: 'createDts', xtype: 'datecolumn', format:'m/d/y H:i:s',  width: 175 },
					{ text: 'Create User', dataIndex: 'createUser', width: 175 },
					{ text: 'Update Date', dataIndex: 'updateDts', xtype: 'datecolumn', format:'m/d/y H:i:s',  width: 175 },
					{ text: 'Update User', dataIndex: 'updateUser', width: 175 }
				],
				listeners: {
					itemdblclick: function(grid, record, item, index, e, opts){
						actionAddEditQuestion(record);
					},						
					selectionchange: function(selModel, selected, opts) {
						var tools = Ext.getCmp('questionGrid').getComponent('tools');

						if (selected.length > 0) {	
							tools.getComponent('view').setDisabled(false);
							tools.getComponent('edit').setDisabled(false);							
							tools.getComponent('togglestatus').setDisabled(false);
							tools.getComponent('delete').setDisabled(false);
						} else {
							tools.getComponent('view').setDisabled(true);
							tools.getComponent('edit').setDisabled(true);							
							tools.getComponent('togglestatus').setDisabled(true);
							tools.getComponent('delete').setDisabled(true);
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
										actionRefreshQuestion();
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
								iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction-edit',
								scale: 'medium',
								width: '110px',
								handler: function(){
									actionRefreshQuestion();
								}
							},
							{
								xtype: 'tbseparator'
							},
							{
								text: 'Add',
								iconCls: 'fa fa-2x fa-plus icon-button-color-save',
								scale: 'medium',
								width: '100px',
								handler: function(){
									actionAddEditQuestion();
								}
							},
							{
								text: 'Edit',
								iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
								itemId: 'edit',
								disabled: true,									
								scale: 'medium',
								width: '100px',
								handler: function(){
									var record = Ext.getCmp('questionGrid').getSelectionModel().getSelection()[0];
									actionAddEditQuestion(record);
								}
							},
							{
								text: 'View',
								iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction-view',
								itemId: 'view',
								disabled: true,									
								scale: 'medium',
								width: '100px',
								handler: function(){
									var record = Ext.getCmp('questionGrid').getSelectionModel().getSelection()[0];
									actionViewQuestion(record);
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
									var record = Ext.getCmp('questionGrid').getSelectionModel().getSelection()[0];
									actionToggleStatus(record);
								}
							},								
							{
								xtype: 'tbfill'
							},
							{
								text: 'Import',
								iconCls: 'fa fa-2x fa-upload icon-button-color-default icon-vertical-correction',
								scale: 'medium',
								handler: function(){
									actionImport();
								}
							},
							{
								text: 'Export',
								iconCls: 'fa fa-2x fa-download icon-button-color-default',
								scale: 'medium',
								handler: function(){
									actionExport();
								}
							},
							{
								xtype: 'tbseparator'
							},							
							{
								text: 'Delete',
								iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
								itemId: 'delete',
								disabled: true,																
								scale: 'medium',
								handler: function(){
									var record = Ext.getCmp('questionGrid').getSelectionModel().getSelection()[0];
									actionDelete(record);									
								}
							}
						]
					},
					{
						xtype: 'pagingtoolbar',
						dock: 'bottom',
						store: 'questionGridStore',
						displayInfo: true
					}						
				]
			});
			
			addComponentToMainViewPort(questionGrid);				
						
			var actionRefreshQuestion = function() {
				Ext.getCmp('questionGrid').getStore().load({
					url: 'api/v1/resource/checklistquestions'
				});					
			};
			
			var actionAddEditQuestion = function(record) {
				addEditWindow.show();
				addEditWindow.getComponent('form').reset();
				
				if (record) {
					addEditWindow.getComponent('form').loadRecord(record);
				}
			};
			
			var actionViewQuestion = function(record) {
								
				var actionPreviewNextRecord = function(next) {
					if (next) {
						Ext.getCmp('questionGrid').getSelectionModel().selectNext();						
					} else {
						Ext.getCmp('questionGrid').getSelectionModel().selectPrevious();						
					}
					viewWin.update(Ext.getCmp('questionGrid').getSelection()[0]);					
				};
				
				var previewCheckButtons = function() {	
					var tools = viewWin.getComponent('tools');
					
					if (Ext.getCmp('questionGrid').getSelectionModel().hasPrevious()) {
						tools.getComponent('viewWinTools-previousBtn').setDisabled(false);
					} else {
						tools.getComponent('viewWinTools-previousBtn').setDisabled(true);
					}
					
					if (Ext.getCmp('questionGrid').getSelectionModel().hasNext()) {
						tools.getComponent('viewWinTools-nextBtn').setDisabled(false);
					} else {
						tools.getComponent('viewWinTools-nextBtn').setDisabled(true);
					}					
				};				
				
				var viewWin = Ext.create('Ext.window.Window', {
					title: 'Question View',
					iconCls: 'fa fa-eye',
					modal: true,
					closeAction: 'destroy',
					width: '75%',
					height: '75%',
					maxizable: true,
					autoScroll: true,					
					bodyStyle: 'padding: 10px;',
					dockedItems: [
						{
							xtype: 'toolbar',
							itemId: 'tools',
							dock: 'bottom',
							items: [
								{
									text: 'Previous',
									itemId: 'viewWinTools-previousBtn',
									iconCls: 'fa fa-2x fa-arrow-left icon-button-color-default icon-vertical-correction',
									scale: 'medium',
									handler: function() {
										actionPreviewNextRecord(false);
									}									
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Close',
									iconCls: 'fa fa-2x fa-close icon-button-color-warning icon-vertical-correction',
									scale: 'medium',
									handler: function() {
										viewWin.close();
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Next',
									itemId: 'viewWinTools-nextBtn',
									iconCls: 'fa fa-2x fa-arrow-right icon-button-color-default icon-vertical-correction',
									iconAlign: 'right',
									scale: 'medium',
									handler: function() {
										actionPreviewNextRecord(true);
									}									
								}
							]
						}
					],
					tpl: '<h1>Question {qid}</h1>'+ 
						 '(<b>{evaluationSectionDescription}</b>)<br>' + 						 
						 '<h2>{question}</h2>' + 
						 '<tpl if="scoreCriteria"><h2>Scoring Criteria</h2><hr>'+ 
						 '{scoreCriteria}' + 
						 '</tpl>' +						 
						 '<tpl if="objective"><h2>Objective</h2><hr>'+ 
						 '{objective}' + 
						 '</tpl>' +
						 '<tpl if="narrative"><h2>Narrative</h2><hr>'+ 
						 '{narrative}' + 
						 '</tpl>'
				});				
				viewWin.show();
				viewWin.update(record);
				previewCheckButtons();
			};			
			
			var actionToggleStatus = function(record) {
				Ext.getCmp('questionGrid').setLoading("Updating Status...");
				var questionId = Ext.getCmp('questionGrid').getSelection()[0].get('questionId');
				var currentStatus = Ext.getCmp('questionGrid').getSelection()[0].get('activeStatus');

				var method = 'PUT';
				var urlEnd = '/activate';
				if (currentStatus === 'A') {
					method = 'DELETE';
					urlEnd = '';
				}					
				Ext.Ajax.request({
					url: 'api/v1/resource/checklistquestions/' + questionId + urlEnd,
					method: method,
					callback: function(){
						Ext.getCmp('questionGrid').setLoading(false);
					},
					success: function(response, opts){						
						actionRefreshQuestion();
					}
				});				
			};			
			
			var actionDelete = function(record) {
				
				questionGrid.setLoading('Checking for references...');
				Ext.Ajax.request({
					url: 'api/v1/resource/checklistquestions/' + record.get('questionId') + '/inuse',
					callback: function(){
						questionGrid.setLoading(false);
					},
					success: function(response, opts){
						var references = response.responseText;

						if (references && references !== 'false') {
							Ext.Msg.alert('Existing References', 'Unable to delete; Delete evaluation repsonses and checklist templates first.');
						} else {
							Ext.Msg.show({
								title:'Delete Question?',
								message: 'Are you sure you want to delete this question?',
								buttons: Ext.Msg.YESNO,
								icon: Ext.Msg.QUESTION,
								fn: function(btn) {
									if (btn === 'yes') {
										questionGrid.setLoading('Deleting question...');
										Ext.Ajax.request({
											url: 'api/v1/resource/checklistquestions/' + record.get('questionId') + '?force=true',
											method: 'DELETE',
											callback: function(){
												questionGrid.setLoading(false);
											},
											success: function(response, opts){
												actionRefreshQuestion();
											}
										});	
									} 
								}
							});									
						}
					}
				});				
			};		
			
			var actionImport = function() {
				importWindow.show();
			};	
			
			var actionExport = function() {
				
				var status = Ext.getCmp('filterActiveStatus').getValue();				
				window.location.href = 'api/v1/resource/checklistquestions/export?status=' + status;
				
			};				
		
		});
		
	</script>

    </stripes:layout-component>
</stripes:layout-render>
