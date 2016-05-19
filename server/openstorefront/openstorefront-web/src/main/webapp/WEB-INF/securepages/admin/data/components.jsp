<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
          <stripes:layout-component name="contents">
			  
		<script src="scripts/component/importWindow.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/messageWindow.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/integrationConfigWindow.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/submissionPanel.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/entryChangeRequestWindow.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/savedSearchLinkInsertWindow.js?v=${appVersion}" type="text/javascript"></script>
		
		<form name="exportForm" action="../api/v1/resource/components/export" method="POST" >
			<p style="display: none;" id="exportFormIds">
			</p>      
		</form>
			  
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function(){	
				
			//Add/Edit forms ------>	
				
				//External Windows

				var ssInsertWindow = Ext.create('OSF.component.SavedSearchLinkInsertWindow', {					
					id: 'ssInsertWindow',
					alwaysOnTop: true
				});	

				var importWindow = Ext.create('OSF.component.ImportWindow', {					
				});
				
				var integrationWindow = Ext.create('OSF.component.IntegrationWindow', {	
					alwaysOnTop: true
				});			
				
				var changeRequestWindow = Ext.create('OSF.component.EntryChangeRequestWindow', {
					adminMode: true,
					successHandler: function() {
						actionRefreshComponentGrid();
					},
					adminEditHandler: function(record, changeRequestWindow) {
						actionAddEditComponent(record);
						mainAddEditWin.changeRequestCloseHandler = function(){
							changeRequestWindow.changeGrid.getStore().reload();
							mainAddEditWin.un('close', mainAddEditWin.changeRequestCloseHandler);
						};
						mainAddEditWin.on('close', mainAddEditWin.changeRequestCloseHandler);
					}					
				});
							
				//common stores
				var statusFilterStore = Ext.create('Ext.data.Store', {
					fields: ['code', 'desc'],
					data : [
						{"code":"A", "desc":"Active"},
						{"code":"I", "desc":"Inactive"}
					]
				});
			
				//entry form common actions
				var actionSubComponentToggleStatus = function(grid, idField, entity, subEntityId, subEntity, forceDelete, successFunc) {
					var status = grid.getSelection()[0].get('activeStatus');
					var recordId = grid.getSelection()[0].get(idField);
					var componentId = grid.getSelection()[0].get('componentId'); 
					if (!componentId) {
						componentId = grid.componentRecord.get('componentId');
					}
					subEntityId = subEntityId ? '/' + grid.getSelection()[0].get(subEntityId) : '';
					subEntity = subEntity ? '/' + subEntity : '';
										
					var urlEnding = '';
					var method = 'DELETE';
					
					if (status === 'I') {
						urlEnding = '/activate';
						method = 'PUT';
					} 
										
					if (forceDelete)
					{
						urlEnding = '/force';
						method = 'DELETE';
					}
					
					grid.setLoading('Updating status...');
					Ext.Ajax.request({
						url: '../api/v1/resource/components/' + componentId + '/' + entity + '/' + recordId + subEntity + subEntityId + urlEnding,
						method: method,
						callback: function(opt, success, response){
							grid.setLoading(false);
						},
						success: function(response, opts){
							if (successFunc) {
								successFunc();
							} else {
								grid.getStore().reload();
							}
						}
					});
					
				};
			
				//Sub Forms
				var actionAddTag = function(form, grid) {				
					var data = form.getValues();
					var componentId = grid.componentRecord.get('componentId');

					CoreUtil.submitForm({
						url: '../api/v1/resource/components/' + componentId + '/tags',
						method: 'POST',
						data: data,
						form: form,
						success: function(){
							grid.getStore().reload();
							form.reset();
						}
					});				
				};
				
				var tagGrid = Ext.create('Ext.grid.Panel', {
					id: 'tagGrid',
					title: 'Tags',					 
					tooltip: 'Searchable labels',
					columnLines: true,
					store: Ext.create('Ext.data.Store', {
						fields: [
							"text",
							"tagId",
							"activeStatus",
							"createUser",
							{
								name: 'createDts',
								type:	'date',
								dateFormat: 'c'
							}														
						],
						autoLoad: false,
						proxy: {
							type: 'ajax'							
						}
					}),
					columns: [
						{ text: 'Tag', dataIndex: 'text', flex: 1, minWidth: 200 },
						{ text: 'Create User', align: 'center', dataIndex: 'createUser', width: 150 },
						{ text: 'Create Date', dataIndex: 'createDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Security Marking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: !${branding.allowSecurityMarkingsFlg} }
					],
					listeners: {
						selectionchange: function(grid, record, index, opts){
							var fullgrid = Ext.getCmp('tagGrid');
							if (fullgrid.getSelectionModel().getCount() === 1) {
								fullgrid.down('toolbar').getComponent('removeBtn').setDisabled(false);
							} else {
								fullgrid.down('toolbar').getComponent('removeBtn').setDisabled(true);
							}
						}						
					},
					dockedItems: [
						{
							xtype: 'form',
							layout: 'anchor',
							padding: 10,
							defaults: {
								labelAlign: 'top',
								labelSeparator: ''
							},
							items: [
								{
									xtype: 'textfield',
									fieldLabel: 'Tag<span class="field-required" />',
									allowBlank: false,
									margin: '0 20 0 0',
									width: '100%',
									maxLength: 120,
									name: 'text',
									listeners: {
										specialkey: function(field, e){
											if (e.getKey() === e.ENTER) {
											   actionAddTag(this.up('form'), Ext.getCmp('tagGrid'));
											}
										}
									}
								},
								Ext.create('OSF.component.SecurityComboBox', {	
									hidden: !${branding.allowSecurityMarkingsFlg}
								}),								
								{
									xtype: 'button',
									text: 'Add',
									formBind: true,
									iconCls: 'fa fa-plus',
									handler: function(){
										actionAddTag(this.up('form'), Ext.getCmp('tagGrid'));
									}
								}
							]
						},						
						{
							xtype: 'toolbar',
							items: [							
								{
									text: 'Refresh',
									iconCls: 'fa fa-refresh',
									handler: function(){
										this.up('grid').getStore().reload();
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Remove',
									itemId: 'removeBtn',
									iconCls: 'fa fa-trash',									
									disabled: true,
									handler: function(){
										actionSubComponentToggleStatus(Ext.getCmp('tagGrid'), 'tagId', 'tags');
									}
								}
							]
						}
					]					
				});

				var questionGrid = Ext.create('Ext.grid.Panel', {
					id: 'questionGrid',
					width: '100%',
					flex: 1,
					columnLines: true,
					store: Ext.create('Ext.data.Store', {
						fields: [
							"questionId",
							"question",
							"organization",							
	   					     "createUser",							 
							{
								name: 'createDts',
								type:	'date',
								dateFormat: 'c'
							},							
							"updateUser",
							{
								name: 'updateDts',
								type:	'date',
								dateFormat: 'c'
							},
							"componentId",
							"userTypeCode",
							"activeStatus",
							"organization"										
						],
						autoLoad: false,
						proxy: {
							type: 'ajax'							
						}
					}),
					columns: [
						{ text: 'Question', dataIndex: 'question',  flex: 1, minWidth: 200 },
						{ text: 'Organization', dataIndex: 'organization', width: 150 },
						{ text: 'User', dataIndex: 'createUser', flex: 1, minWidth: 150 },					
						{ text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Security Marking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: !${branding.allowSecurityMarkingsFlg} }
					],
					listeners: {
						selectionchange: function(selectionModel, selectedRecords, opts){
							var fullgrid = Ext.getCmp('questionGrid');
							if (fullgrid.getSelectionModel().getCount() === 1) {
								fullgrid.down('toolbar').getComponent('statusToggleBtn').setDisabled(false);
								var componentId = fullgrid.componentRecord.get('componentId');						
								var questionId = selectedRecords[0].get('questionId');
							
								Ext.getCmp('questionResponseGrid').getStore().load({
									url: '../api/v1/resource/components/'+componentId+'/questions/'+questionId+'/responses'
								});								
							} else {
								fullgrid.down('toolbar').getComponent('statusToggleBtn').setDisabled(true);
								Ext.getCmp('questionResponseGrid').getStore().removeAll();
							}
						}						
					},
					dockedItems: [
						{
							xtype: 'toolbar',
							items: [
								{
									xtype: 'combobox',
									fieldLabel: 'Filter Status',
									store: statusFilterStore,
									forceSelection: true,
									queryMode: 'local',
									displayField: 'desc',
									valueField: 'code',
									value: 'A',
									listeners: {
										change: function(combo, newValue, oldValue, opts){
											this.up('grid').getStore().load({
												url: '../api/v1/resource/components/' + Ext.getCmp('questionGrid').componentRecord.get('componentId') + '/questions/view',
												params: {
													status: newValue
												}
											});
										}
									}
								}, 
								{
									text: 'Refresh',
									iconCls: 'fa fa-refresh',
									handler: function(){
										this.up('grid').getStore().reload();
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'ToggleStatus',
									itemId: 'statusToggleBtn',
									iconCls: 'fa fa-power-off',
									disabled: true,
									handler: function(){
										actionSubComponentToggleStatus(Ext.getCmp('questionGrid'), 'questionId', 'questions');
									}
								}
							]
						}
					]					
				});
				
				var questionContainer = Ext.create('Ext.panel.Panel', {
					title: 'Question',
					tooltip: 'User questions and answers',
					layout: 'vbox',
					items: [
						questionGrid,
						{
							xtype: 'grid',
							id: 'questionResponseGrid',
							title: 'Responses',
							columnLines: true,
							width: '100%',
							flex: 1,
							store: Ext.create('Ext.data.Store', {
								fields: [
									"questionId",
									"response",
									"organization",							
									'responseId',
									"createUser",							 									
									{
										name: 'createDts',
										type:	'date',
										dateFormat: 'c'
									},
									"updateUser",									
									{
										name: 'updateDts',
										type:	'date',
										dateFormat: 'c'
									},
									"componentId",
									"userTypeCode",
									"activeStatus",
									"organization"										
								],
								autoLoad: false,
								proxy: {
									type: 'ajax'							
								}
							}),
							columns: [
								{ text: 'Response', dataIndex: 'response',  flex: 1, minWidth: 200 },
								{ text: 'Organization', dataIndex: 'organization', width: 150 },
								{ text: 'User', dataIndex: 'createUser', wdth: 150 },	
								{ text: 'Active Status', align: 'center', dataIndex: 'activeStatus', width: 150 },					
								{ text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
								{ text: 'Security Marking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: !${branding.allowSecurityMarkingsFlg} }
							],
							listeners: {
								selectionchange: function(selectionModel, selectedRecords, opts){
									var fullgrid = Ext.getCmp('questionResponseGrid');
									if (fullgrid.getSelectionModel().getCount() === 1) {
										fullgrid.down('toolbar').getComponent('statusToggleBtn').setDisabled(false);
									} else {
										fullgrid.down('toolbar').getComponent('statusToggleBtn').setDisabled(true);
									}
								}						
							},							
							dockedItems: [
								{
									xtype: 'toolbar',
									items: [
										{
											text: 'ToggleStatus',
											itemId: 'statusToggleBtn',
											iconCls: 'fa fa-power-off',
											disabled: true,
											handler: function(){
												actionSubComponentToggleStatus(this.up('grid'), 'questionId', 'questions', 'responseId', 'responses');
											}
										}
									]
								}
							]
						}
					]
				});

				var reviewGrid = Ext.create('Ext.grid.Panel', {
					id: 'reviewGrid',
					title: 'Reviews',
					tooltip: 'User Reviews',
					columnLines: true,
					store: Ext.create('Ext.data.Store', {
						fields: [
							"username",
							"userTypeCode",
							"comment",
							"rating",
							"title",
							"userTimeCode",
							"lastUsed",
							{
								name: 'updateDate',
								type:	'date',
								dateFormat: 'c'
							},							
							"organization",
							"recommend",
							"componentId",
							"reviewId",
							"activeStatus",
							"name",
							{ name: "pros", mapping: function(data) {
									var proList='<ul>';							
									Ext.Array.each(data.pros, function(pro){
										proList += '<li>'+pro.text+'</li>';
									});	
									proList +='</ul>';
									return proList;
								}
							},
							{ name: "cons", mapping: function(data) {
									var conList='<ul>';							
									Ext.Array.each(data.cons, function(con){
										conList += '<li>'+con.text+'</li>';
									});	
									conList +='</ul>';
									return conList;
								}
							}														
						],
						autoLoad: false,
						proxy: {
							type: 'ajax'							
						}
					}),
					columns: [
						{ text: 'Title', dataIndex: 'title', width: 200 },
						{ text: 'Rating', align: 'center', dataIndex: 'rating', width: 100 },
						{ text: 'Comment', dataIndex: 'comment', flex: 1, minWidth: 200 },
						{ text: 'Pros', dataIndex: 'pros', width: 200 },
						{ text: 'Cons', dataIndex: 'cons', width: 200 },
						{ text: 'User', dataIndex: 'username', width: 150 },
						{ text: 'Update Date', dataIndex: 'updateDate', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Security Marking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: !${branding.allowSecurityMarkingsFlg} }
					],
					listeners: {
						selectionchange: function(grid, record, index, opts){
							var fullgrid = Ext.getCmp('reviewGrid');
							if (fullgrid.getSelectionModel().getCount() === 1) {
								fullgrid.down('toolbar').getComponent('statusToggleBtn').setDisabled(false);
							} else {
								fullgrid.down('toolbar').getComponent('statusToggleBtn').setDisabled(true);
							}
						}						
					},
					dockedItems: [
						{
							xtype: 'toolbar',
							items: [
								{
									xtype: 'combobox',
									fieldLabel: 'Filter Status',
									store: statusFilterStore,
									forceSelection: true,
									queryMode: 'local',
									displayField: 'desc',
									valueField: 'code',
									value: 'A',
									listeners: {
										change: function(combo, newValue, oldValue, opts){
											this.up('grid').getStore().load({
												url: '../api/v1/resource/components/' + Ext.getCmp('reviewGrid').componentRecord.get('componentId') + '/reviews/view',
												params: {
													status: newValue
												}
											});
										}
									}
								}, 
								{
									text: 'Refresh',
									iconCls: 'fa fa-refresh',
									handler: function(){
										this.up('grid').getStore().reload();
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'ToggleStatus',
									itemId: 'statusToggleBtn',
									iconCls: 'fa fa-power-off',
									disabled: true,
									handler: function(){
										actionSubComponentToggleStatus(Ext.getCmp('reviewGrid'), 'reviewId', 'reviews');
									}
								}
							]
						}
					]
				});

				var evaluationGrid = Ext.create('Ext.grid.Panel', {
					id: 'evaluationGrid',
					title: 'Evaluation',
					tooltip: 'Evaluation scores',
					columnLines: true,
					selModel: 'cellmodel',
					plugins: {
						ptype: 'cellediting',
						clicksToEdit: 1												
					},					
					store: Ext.create('Ext.data.Store', {
						fields: [
							"code",
							"description",
							"notAvailable",
							"evaluationSection",
							"actualScore",
							"existing",
							"name"
						],
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: '../api/v1/resource/lookuptypes/EvaluationSection'
						}
					}),
					columns: [
						{ text: 'Section', dataIndex: 'description', flex: 1, minWidth: 200 },
						{ text: 'Not Available', align: 'center', dataIndex: 'notAvailable', width: 150, 
							xtype: 'checkcolumn',
							listeners: {
								checkchange: function(checkColumn, rowIndex, checked, opt) {
									var record = Ext.getCmp('evaluationGrid').getStore().getAt(rowIndex);
									if (checked) {										
										record.actualScoreField.setDisabled(true);
									} else {
										record.actualScoreField.setDisabled(false);
									}
								}
							}
						},
						{ text: 'Score', dataIndex: 'actualScore', width: 200, 
							xtype: 'widgetcolumn',
							widget: {
								xtype: 'numberfield',
								minValue: 1,
								maxValue: 5,
								step: .5,
								decimalPrecision: 1,
								listeners: {
									afterrender: function(field){
										var record = field.getWidgetRecord();
										record.actualScoreField = field;
									},
									change: function(field, newValue, oldValue, opts) {
										var record = field.getWidgetRecord();
										record.set('actualScore', newValue);
										if (newValue) {
											record.set('notAvailable', false);
										}
									}
								}
							}
						},
						{ text: 'Existing', align: 'center', dataIndex: 'existing', width: 150,
							renderer: function(value) {
								if (value) {
									return "<i class='fa fa-check'></i>";
								} else {
									return "";
								}
							}
						},
						{
						 text: 'Action', 
						 dataIndex: '',
						 sortable: false,
						 xtype: 'widgetcolumn',
						 align: 'center',
						 width: 75,               
						 widget: {
						   xtype: 'button',
						   iconCls: 'fa fa-trash',
						   maxWidth: 25,						   
						   handler: function() {
							 var record = this.getWidgetRecord();
							 var componentId = Ext.getCmp('evaluationGrid').componentRecord.get('componentId');
								Ext.getCmp('evaluationGrid').setLoading('Clearing Section...');
								Ext.Ajax.request({
									url: '../api/v1/resource/components/' + componentId + '/sections/'+ record.get('code'),
									method: 'DELETE',
									callback: function(){
										Ext.getCmp('evaluationGrid').setLoading(false);
									},
									success: function(response, opts) {
										Ext.toast('Cleared evaluation Record', 'Success');
										record.set('notAvailable', null, { dirty: false });
										record.set('actualScore', null, { dirty: false });
										record.set('existing', null, { dirty: false });
									}
							 });
						   }
						 }
					   }
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Save All',
									iconCls: 'fa fa-save',
									handler: function(){
										var componentId = Ext.getCmp('evaluationGrid').componentRecord.get('componentId');
										var records = this.up('grid').getStore().getData();
										var sectionsToPost = [];
										Ext.Array.each(records.items, function(record){
											if (record.get('actualScore') || record.get('notAvailable')) {
												sectionsToPost.push({
													componentEvaluationSectionPk: {
														componentId: componentId,
														evaluationSection: record.get('code')
													},
													actualScore: record.get('actualScore'),
													notAvailable: record.get('notAvailable')
												});
										     }
										});
										Ext.getCmp('evaluationGrid').setLoading('Saving...');
										Ext.Ajax.request({
											url: '../api/v1/resource/components/' + componentId + '/sections/all',
											method: 'POST',
											jsonData: sectionsToPost,
											callback: function(){
												Ext.getCmp('evaluationGrid').setLoading(false);
											},
											success: function(response, opts) {
												Ext.toast('Saved Records', 'Success');
												loadEvalationData(componentId);
											}
										});
										
										
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Clear All Sections',
									iconCls: 'fa fa-trash',									
									handler: function(){			
										Ext.Msg.show({
											title:'Clear All Sections?',
											message: 'Are you sure you would like to clear all sections?',
											buttons: Ext.Msg.YESNO,
											icon: Ext.Msg.QUESTION,
											fn: function(btn) {
												if (btn === 'yes') {
													var componentId = Ext.getCmp('evaluationGrid').componentRecord.get('componentId');
													Ext.getCmp('evaluationGrid').setLoading('Clearing All Sections...');
													Ext.Ajax.request({
														url: '../api/v1/resource/components/' + componentId + '/sections',
														method: 'DELETE',
														callback: function(){
															Ext.getCmp('evaluationGrid').setLoading(false);
														},
														success: function(response, opts) {
															Ext.toast('Cleared evaluation records', 'Success');
															loadEvalationData(componentId);
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
				});
				
				var loadEvalationData = function(componentId){
					Ext.getCmp('evaluationGrid').setLoading(true);
					
					//clear data
					Ext.getCmp('evaluationGrid').getStore().each(function(record){
						record.set('notAvailable', null, { dirty: false });
						record.set('actualScore', null, { dirty: false });
						record.set('existing', null, { dirty: false });
						if (record.actualScoreField)
						{
							record.actualScoreField.setDisabled(false);
						}
					});
					
					Ext.Ajax.request({
						url: '../api/v1/resource/components/' + componentId + '/sections',
						callback: function(){
							Ext.getCmp('evaluationGrid').setLoading(false);
						},
						success: function(response, opt){
							var data = Ext.decode(response.responseText);
														
							Ext.getCmp('evaluationGrid').getStore().each(function(record){
								Ext.Array.each(data, function(section){
									if (section.componentEvaluationSectionPk.evaluationSection === record.get('code')) {
										record.set('notAvailable', section.notAvailable, { dirty: false });
										record.set('actualScore', section.actualScore, { dirty: false });
										record.set('existing', true, { dirty: false });										
									}	
								});							
							});
						}
					});
				};
	
				var metadataGrid = Ext.create('Ext.grid.Panel', {
					id: 'metadataGrid',
					title: 'Metadata',
					tooltip: 'Extra non-filterable information about an entry.',
					columnLines: true,
					store: Ext.create('Ext.data.Store', {
						fields: [
							"metadataId",
							"label",
							"value",
							{
								name: 'updateDts',
								type:	'date',
								dateFormat: 'c'
							},							
							"activeStatus"
						],
						autoLoad: false,
						proxy: {
							type: 'ajax'							
						}
					}),
					columns: [
						{ text: 'Label', dataIndex: 'label',  width: 200 },
						{ text: 'Value',  dataIndex: 'value', flex: 1, minWidth: 200 },
						{ text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Security Marking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: !${branding.allowSecurityMarkingsFlg} }
					],
					listeners: {
						itemdblclick: function(grid, record, item, index, e, opts){
							this.down('form').loadRecord(record);
						},
						selectionchange: function(grid, record, index, opts){
							var fullgrid = Ext.getCmp('metadataGrid');
							if (fullgrid.getSelectionModel().getCount() === 1) {
								fullgrid.down('toolbar').getComponent('editBtn').setDisabled(false);
								fullgrid.down('toolbar').getComponent('toggleStatusBtn').setDisabled(false);
							} else {
								fullgrid.down('toolbar').getComponent('editBtn').setDisabled(true);
								fullgrid.down('toolbar').getComponent('toggleStatusBtn').setDisabled(true);
							}
						}						
					},
					dockedItems: [
						{
							xtype: 'form',
							title: 'Add/Edit Metadata',
							collapsible: true,
							titleCollapse: true,
							border: true,
							layout: 'vbox',
							bodyStyle: 'padding: 10px;',
							margin: '0 0 5 0', 
							defaults: {
								labelAlign: 'top',
								labelSeparator: '',
								width: '100%'
							},
							buttonAlign: 'center',
							buttons: [
								{
									xtype: 'button',
									text: 'Save',
									formBind: true,
									margin: '0 20 0 0',
									iconCls: 'fa fa-save',
									handler: function(){	
										var form = this.up('form');
										var data = form.getValues();
										var componentId = Ext.getCmp('metadataGrid').componentRecord.get('componentId');

										var method = 'POST';
										var update = '';
										if (data.metadataId) {
											update = '/' + data.metadataId;
											method = 'PUT';
										}

										CoreUtil.submitForm({
											url: '../api/v1/resource/components/' + componentId + '/metadata' + update,
											method: method,
											data: data,
											form: form,
											success: function(){
												Ext.getCmp('metadataGrid').getStore().reload();
												form.reset();
											}
										});
									}
								},
								{
									xtype: 'button',
									text: 'Cancel',										
									iconCls: 'fa fa-close',
									handler: function(){
										this.up('form').reset();
									}									
								}								
							],
							items: [
								{
									xtype: 'hidden',
									name: 'metadataId'
								},
								{
									xtype: 'textfield',
									fieldLabel: 'Label <span class="field-required" />',									
									allowBlank: false,									
									maxLength: '255',									
									name: 'label'
								},
								{
									xtype: 'textfield',
									fieldLabel: 'Value <span class="field-required" />',									
									allowBlank: false,									
									maxLength: '255',									
									name: 'value'
								},
								Ext.create('OSF.component.SecurityComboBox', {	
									hidden: !${branding.allowSecurityMarkingsFlg}
								})								
							]
						},						
						{
							xtype: 'toolbar',
							items: [
								{
									xtype: 'combobox',
									fieldLabel: 'Filter Status',
									store: statusFilterStore,
									forceSelection: true,
									queryMode: 'local',
									displayField: 'desc',
									valueField: 'code',
									value: 'A',
									listeners: {
										change: function(combo, newValue, oldValue, opts){
											this.up('grid').getStore().load({
												url: '../api/v1/resource/components/' + Ext.getCmp('metadataGrid').componentRecord.get('componentId') + '/metadata/view',
												params: {
													status: newValue
												}
											});
										}
									}
								}, 								
								{
									text: 'Refresh',
									iconCls: 'fa fa-refresh',
									handler: function(){
										this.up('grid').getStore().reload();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Edit',
									itemId: 'editBtn',									
									iconCls: 'fa fa-edit',
									handler: function(){
										this.up('grid').down('form').loadRecord(Ext.getCmp('metadataGrid').getSelection()[0]);
									}									
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Toggle Status',
									itemId: 'toggleStatusBtn',
									iconCls: 'fa fa-power-off',									
									disabled: true,
									handler: function(){
										actionSubComponentToggleStatus(Ext.getCmp('metadataGrid'), 'metadataId', 'metadata');
									}
								}
							]
						}
					]									
				});

				var dependenciesGrid = Ext.create('Ext.grid.Panel', {
					id: 'dependenciesGrid',
					title: 'Dependencies',
					tooltip: 'External dependencies for a component',
					columnLines: true,
					store: Ext.create('Ext.data.Store', {
						fields: [
							"dependencyId",
							"dependencyName",
							"version",
							"dependancyReferenceLink",
							"comment",
							{
								name: 'updateDts',
								type:	'date',
								dateFormat: 'c'
							},														
							"activeStatus"
						],
						autoLoad: false,
						proxy: {
							type: 'ajax'							
						}
					}),
					columns: [
						{ text: 'Name', dataIndex: 'dependencyName',  width: 200 },
						{ text: 'Version',  dataIndex: 'version', width: 150 },
						{ text: 'Link',  dataIndex: 'dependancyReferenceLink', width: 200 },
						{ text: 'Comment',  dataIndex: 'comment', flex: 1, minWidth: 200 },
						{ text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s' },
						{ text: 'Security Marking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: !${branding.allowSecurityMarkingsFlg} }
					],
					listeners: {
						itemdblclick: function(grid, record, item, index, e, opts){
							this.down('form').reset();
							this.down('form').loadRecord(record);
						},
						selectionchange: function(grid, record, index, opts){
							var fullgrid = Ext.getCmp('dependenciesGrid');
							if (fullgrid.getSelectionModel().getCount() === 1) {
								fullgrid.down('toolbar').getComponent('editBtn').setDisabled(false);
								fullgrid.down('toolbar').getComponent('toggleStatusBtn').setDisabled(false);
							} else {
								fullgrid.down('toolbar').getComponent('editBtn').setDisabled(true);
								fullgrid.down('toolbar').getComponent('toggleStatusBtn').setDisabled(true);
							}
						}						
					},
					dockedItems: [
						{
							xtype: 'form',
							title: 'Add/Edit Dependency',
							collapsible: true,
							titleCollapse: true,
							border: true,
							layout: 'vbox',
							bodyStyle: 'padding: 10px;',
							margin: '0 0 5 0', 
							defaults: {
								labelAlign: 'top',
								labelSeparator: '',
								width: '100%'
							},
							buttonAlign: 'center',
							buttons: [
								{
									xtype: 'button',
									text: 'Save',
									formBind: true,
									margin: '0 20 0 0',
									iconCls: 'fa fa-save',
									handler: function(){	
										var form = this.up('form');
										var data = form.getValues();
										var componentId = Ext.getCmp('dependenciesGrid').componentRecord.get('componentId');

										var method = 'POST';
										var update = '';
										if (data.dependencyId) {
											update = '/' + data.dependencyId;
											method = 'PUT';
										}

										CoreUtil.submitForm({
											url: '../api/v1/resource/components/' + componentId + '/dependencies' + update,
											method: method,
											data: data,
											form: form,
											success: function(){
												Ext.getCmp('dependenciesGrid').getStore().reload();
												form.reset();
											}
										});
									}
								},
								{
									xtype: 'button',
									text: 'Cancel',										
									iconCls: 'fa fa-close',
									handler: function(){
										this.up('form').reset();
									}									
								}								
							],
							items: [
								{
									xtype: 'hidden',
									name: 'dependencyId'
								},
								{
									xtype: 'textfield',
									fieldLabel: 'Name <span class="field-required" />',									
									allowBlank: false,									
									maxLength: '255',
									name: 'dependencyName'
								},
								{
									xtype: 'textfield',
									fieldLabel: 'Version <span class="field-required" />',									
									allowBlank: false,								
									maxLength: '255',
									name: 'version'
								},
								{
									xtype: 'textfield',
									fieldLabel: 'External Link',															
									emptyText: 'http://dependency.com/download',									
									maxLength: '255',
									name: 'dependancyReferenceLink'
								},
								{
									xtype: 'textfield',
									fieldLabel: 'Comment',																											
									maxLength: '255',
									name: 'comment'
								},
								Ext.create('OSF.component.SecurityComboBox', {	
									hidden: !${branding.allowSecurityMarkingsFlg}
								})								
							]
						},						
						{
							xtype: 'toolbar',
							items: [
								{
									xtype: 'combobox',
									fieldLabel: 'Filter Status',
									store: statusFilterStore,
									forceSelection: true,
									queryMode: 'local',
									displayField: 'desc',
									valueField: 'code',
									value: 'A',
									listeners: {
										change: function(combo, newValue, oldValue, opts){
											this.up('grid').getStore().load({
												url: '../api/v1/resource/components/' + Ext.getCmp('dependenciesGrid').componentRecord.get('componentId') + '/dependencies/view',
												params: {
													status: newValue
												}
											});
										}
									}
								}, 								
								{
									text: 'Refresh',
									iconCls: 'fa fa-refresh',
									handler: function(){
										this.up('grid').getStore().reload();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Edit',
									itemId: 'editBtn',
									iconCls: 'fa fa-edit',
									handler: function(){
										this.up('grid').down('form').reset();
										this.up('grid').down('form').loadRecord(Ext.getCmp('dependenciesGrid').getSelection()[0]);
									}									
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Toggle Status',
									itemId: 'toggleStatusBtn',
									iconCls: 'fa fa-power-off',									
									disabled: true,
									handler: function(){
										actionSubComponentToggleStatus(Ext.getCmp('dependenciesGrid'), 'dependencyId', 'dependencies');
									}
								}
							]
						}
					]						
				});

				var mediaGrid = Ext.create('Ext.grid.Panel', {
					id: 'mediaGrid',
					title: 'Media',
					tooltip: 'Screenshots, video, other media for a entry.',
					columnLines: true,
					store: Ext.create('Ext.data.Store', {
						fields: [
							"componentMediaId",
							"contentType",
							"mediaTypeCode",
							"link",
							"mimeType",
							"caption",
							"fileName",
							"originalFileName",
							"originalLink",
							{
								name: 'updateDts',
								type:	'date',
								dateFormat: 'c'
							},							
							"activeStatus"
						],
						autoLoad: false,
						proxy: {
							type: 'ajax'							
						}
					}),					
					columns: [
						{ text: 'Media Type', dataIndex: 'contentType',  width: 200 },
						{ text: 'Caption',  dataIndex: 'caption', flex: 1, minWidth: 200 },
						{ text: 'Mime Type',  dataIndex: 'mimeType', width: 200 },
						{ text: 'Local Media Name',  dataIndex: 'originalFileName', width: 200 },
						{ text: 'Link',  dataIndex: 'originalLink', width: 200 },						
						{ text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s' },
						{ text: 'Security Marking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: !${branding.allowSecurityMarkingsFlg} }
					],
					listeners: {
						itemdblclick: function(grid, record, item, index, e, opts){
							this.down('form').reset();
							this.down('form').loadRecord(record);
							if (record.get('originalFileName')) {
								this.down('form').getComponent('upload').setFieldLabel('Current File: ' + record.get('originalFileName'));
							} else {
								this.down('form').getComponent('upload').setFieldLabel('Upload Media (limit 1GB)');
							}
						},
						selectionchange: function(grid, record, index, opts){
							var fullgrid = Ext.getCmp('mediaGrid');
							if (fullgrid.getSelectionModel().getCount() === 1) {
								fullgrid.down('toolbar').getComponent('editBtn').setDisabled(false);
								fullgrid.down('toolbar').getComponent('removeBtn').setDisabled(false);
								fullgrid.down('toolbar').getComponent('toggleStatusBtn').setDisabled(false);
							} else {
								fullgrid.down('toolbar').getComponent('editBtn').setDisabled(true);
								fullgrid.down('toolbar').getComponent('removeBtn').setDisabled(true);
								fullgrid.down('toolbar').getComponent('toggleStatusBtn').setDisabled(true);
							}
						}						
					},
					dockedItems: [
						{
							xtype: 'form',
							title: 'Add/Edit Media',
							collapsible: true,
							titleCollapse: true,
							border: true,
							layout: 'vbox',
							bodyStyle: 'padding: 10px;',
							margin: '0 0 5 0', 
							defaults: {
								labelAlign: 'top',
								labelSeparator: '',
								width: '100%'
							},
							buttonAlign: 'center',
							buttons: [
								{
									xtype: 'button',
									text: 'Save',
									formBind: true,
									margin: '0 20 0 0',
									iconCls: 'fa fa-save',
									handler: function(){	
										var mainForm = this.up('form');
										var data = mainForm.getValues();
										var componentId = Ext.getCmp('mediaGrid').componentRecord.get('componentId');
										
										data.fileSelected = mainForm.getComponent('upload').getValue();
										data.link = data.originalLink;
										data.originalName = data.originalFileName;
									
										if (!data.originalFileName && ((!data.link && !data.fileSelected) || (data.link && data.fileSelected))) {
											
											mainForm.getForm().markInvalid({
												file: 'Either a link or a file must be entered',
												originalLink: 'Either a link or a file must be entered'
											});
											
										} else {
											if (!data.fileSelected) {
												var method = 'POST';
												var update = '';
												if (data.componentMediaId) {
													update = '/' + data.componentMediaId;
													method = 'PUT';
												}

												CoreUtil.submitForm({
													url: '../api/v1/resource/components/' + componentId + '/media' + update,
													method: method,
													removeBlankDataItems: true,
													data: data,
													form: mainForm,
													success: function(){
														Ext.getCmp('mediaGrid').getStore().reload();
														mainForm.reset();
														mainForm.getComponent('upload').setFieldLabel('Upload Media (limit 1GB)');
													}
												});
											} else {
												//upload
											
												mainForm.submit({
													url: '../Media.action?UploadMedia',
													params: {
														'componentMedia.mediaTypeCode' : data.mediaTypeCode,
														'componentMedia.caption': data.caption,
														'componentMedia.link': data.link,
														'componentMedia.componentMediaId': data.componentMediaId,
														'componentMedia.componentId': componentId
													},
													method: 'POST',
													submitEmptyText: false,
													waitMsg: 'Uploading media please wait...',
													waitTitle: 'Uploading',
													success: function(formBasic, action, opt){
														Ext.getCmp('mediaGrid').getStore().reload();
														mainForm.reset();
														mainForm.getComponent('upload').setFieldLabel('Upload Media (limit 1GB)');
													}, 
													failure: function(formBasic, action, opt) {
														var errorResponse = Ext.decode(action.response.responseText);
														var errorObj = {};        
														Ext.Array.each(errorResponse.errors.entry, function(item, index, entry) {
															errorObj[item.key.replace('componentMedia', '')] = item.value;
														});
														mainForm.markInvalid(errorObj);
													}
												});
												
											}
										}
									}
								},
								{
									xtype: 'button',
									text: 'Cancel',										
									iconCls: 'fa fa-close',
									handler: function(){
										this.up('form').reset();
										this.up('form').getComponent('upload').setFieldLabel('Upload Media (limit 1GB)');
									}									
								}								
							],
							items: [
								{
									xtype: 'hidden',
									name: 'componentMediaId'
								},
								{
									xtype: 'hidden',
									name: 'originalFileName'
								},
								{
									xtype: 'hidden',
									name: 'fileName'
								},
								{
									xtype: 'hidden',
									name: 'mimeType'
								},
								Ext.create('OSF.component.StandardComboBox', {
									name: 'mediaTypeCode',									
									allowBlank: false,								
									margin: '0 0 0 0',
									editable: false,
									typeAhead: false,
									width: '100%',
									fieldLabel: 'Media Type <span class="field-required" />',
									storeConfig: {
										url: '../api/v1/resource/lookuptypes/MediaType'
									}
								}),
								{
									xtype: 'textfield',
									fieldLabel: 'Caption <span class="field-required" />',									
									allowBlank: false,									
									maxLength: '255',
									name: 'caption'
								},
								{
									xtype: 'filefield',
									itemId: 'upload',
									fieldLabel: 'Upload Media (Limit of 1GB)',																											
									name: 'file',
									listeners: {
										change: CoreUtil.handleMaxFileLimit
									}
								},
								{
									xtype: 'textfield',
									fieldLabel: 'Link',																																	
									maxLength: '255',									
									emptyText: 'http://www.example.com/image.png',
									name: 'originalLink'
								},
								Ext.create('OSF.component.SecurityComboBox', {	
									hidden: !${branding.allowSecurityMarkingsFlg}
								})								
							]
						},						
						{
							xtype: 'toolbar',
							items: [
								{
									xtype: 'combobox',
									fieldLabel: 'Filter Status',
									store: statusFilterStore,
									forceSelection: true,
									queryMode: 'local',
									displayField: 'desc',
									valueField: 'code',
									value: 'A',
									listeners: {
										change: function(combo, newValue, oldValue, opts){
											this.up('grid').getStore().load({
												url: '../api/v1/resource/components/' + Ext.getCmp('mediaGrid').componentRecord.get('componentId') + '/media/view',
												params: {
													status: newValue
												}
											});
										}
									}
								}, 								
								{
									text: 'Refresh',
									iconCls: 'fa fa-refresh',
									handler: function(){
										this.up('grid').getStore().reload();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Edit',
									itemId: 'editBtn',
									iconCls: 'fa fa-edit',
									handler: function(){
										var record = Ext.getCmp('mediaGrid').getSelection()[0];
										this.up('grid').down('form').reset();
										this.up('grid').down('form').loadRecord(record);
										if (record.get('originalFileName')) {
											this.up('grid').down('form').getComponent('upload').setFieldLabel('Current File: ' + record.get('originalFileName'));
										} else {
											this.up('grid').down('form').getComponent('upload').setFieldLabel('Upload Media (limit 1GB)');
										}
									}									
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Toggle Status',
									itemId: 'toggleStatusBtn',
									iconCls: 'fa fa-power-off',									
									disabled: true,
									handler: function(){
										actionSubComponentToggleStatus(Ext.getCmp('mediaGrid'), 'componentMediaId', 'media');
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Remove',
									itemId: 'removeBtn',
									iconCls: 'fa fa-trash',
									disabled: true,
									handler: function(){
										actionSubComponentToggleStatus(Ext.getCmp('mediaGrid'), 'componentMediaId', 'media', undefined, undefined, true);
									}									
								}
							]
						}
					]																
				});

				var resourcesGrid = Ext.create('Ext.grid.Panel', {
					id: 'resourcesGrid',
					title: 'Resources',
					tooltip: 'Location of entry artifacts.',
					columnLines: true,
					store: Ext.create('Ext.data.Store', {
						fields: [
							"resourceId",
							"resourceType",
							"resourceTypeDesc",
							"description",
							"link",
							"localResourceName",
							"mimeType",
							"originalFileName",
							"actualLink",
							"restricted",
							"originalLink",
							"componentId",							
							{
								name: 'updateDts',
								type:	'date',
								dateFormat: 'c'
							},							
							"activeStatus"
						],
						autoLoad: false,
						proxy: {
							type: 'ajax'							
						}
					}),					
					columns: [
						{ text: 'Resource Type', dataIndex: 'resourceTypeDesc',  width: 200 },
						{ text: 'Description',  dataIndex: 'description', width: 150 },
						{ text: 'Link',  dataIndex: 'originalLink', flex: 1, minWidth: 200 },
						{ text: 'Mime Type',  dataIndex: 'mimeType', width: 200 },
						{ text: 'Local Resource Name',  dataIndex: 'originalFileName', width: 200 },
						{ text: 'Restricted',  dataIndex: 'restricted', width: 150 },						
						{ text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s' },
						{ text: 'Security Marking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: !${branding.allowSecurityMarkingsFlg} }
					],
					listeners: {
						itemdblclick: function(grid, record, item, index, e, opts){
							this.down('form').reset();
							this.down('form').loadRecord(record);
							if (record.get('originalFileName')) {
								this.down('form').getComponent('upload').setFieldLabel('Current File: ' + record.get('originalFileName'));
							} else {
								this.down('form').getComponent('upload').setFieldLabel('Upload Resource (limit 1GB)');
							}
						},
						selectionchange: function(grid, record, index, opts){
							var fullgrid = Ext.getCmp('resourcesGrid');
							if (fullgrid.getSelectionModel().getCount() === 1) {
								fullgrid.down('toolbar').getComponent('editBtn').setDisabled(false);
								fullgrid.down('toolbar').getComponent('removeBtn').setDisabled(false);
								fullgrid.down('toolbar').getComponent('toggleStatusBtn').setDisabled(false);
							} else {
								fullgrid.down('toolbar').getComponent('editBtn').setDisabled(true);
								fullgrid.down('toolbar').getComponent('removeBtn').setDisabled(true);
								fullgrid.down('toolbar').getComponent('toggleStatusBtn').setDisabled(true);
							}
						}						
					},
					dockedItems: [
						{
							xtype: 'form',
							title: 'Add/Edit Resources',
							collapsible: true,
							titleCollapse: true,
							border: true,
							layout: 'vbox',
							bodyStyle: 'padding: 10px;',
							margin: '0 0 5 0', 
							defaults: {
								labelAlign: 'top',
								labelSeparator: '',
								width: '100%'
							},
							buttonAlign: 'center',
							buttons: [
								{
									xtype: 'button',
									text: 'Save',
									formBind: true,
									margin: '0 20 0 0',
									iconCls: 'fa fa-save',
									handler: function(){	
										var form = this.up('form');
										var data = form.getValues();
										var componentId = Ext.getCmp('resourcesGrid').componentRecord.get('componentId');
										
										data.fileSelected = form.getComponent('upload').getValue();
										data.link = data.originalLink;
										data.originalName = data.originalFileName;
									
										if (!data.originalFileName && ((!data.link && !data.fileSelected) || (data.link && data.fileSelected))) {
											
											form.getForm().markInvalid({
												file: 'Either a link or a file must be entered',
												originalLink: 'Either a link or a file must be entered'
											});
											
										} else {
											if (!data.fileSelected) {
												var method = 'POST';
												var update = '';
												if (data.resourceId) {
													update = '/' + data.resourceId;
													method = 'PUT';
												}

												CoreUtil.submitForm({
													url: '../api/v1/resource/components/' + componentId + '/resources' + update,
													method: method,
													removeBlankDataItems: true,
													data: data,
													form: form,
													success: function(){
														Ext.getCmp('resourcesGrid').getStore().reload();
														form.reset();
														form.getComponent('upload').setFieldLabel('Upload Resource (limit 1GB)');
													}
												});
											} else {
												//upload
												form.submit({
													url: '../Resource.action?UploadResource',
													params: {
														'componentResource.resourceType' : data.resourceType,
														'componentResource.description': data.description,
														'componentResource.restricted': data.restricted,
														'componentResource.link': data.link,
														'componentResource.resourceId': data.resourceId,
														'componentResource.componentId': componentId
													},
													method: 'POST',
													submitEmptyText: false,
													waitMsg: 'Uploading please wait...',
													waitTitle: 'Uploading',													
													success: function(formBasic, action, opt){
														Ext.getCmp('resourcesGrid').getStore().reload();
														form.reset();
														form.getComponent('upload').setFieldLabel('Upload Resource (limit 1GB)');
													}, 
													failure: function(formBasic, action, opt) {
														var errorResponse = Ext.decode(action.response.responseText);
														var errorObj = {};        
														Ext.Array.each(errorResponse.errors.entry, function(item, index, entry) {
															errorObj[item.key.replace('componentResource', '')] = item.value;
														});
														form.markInvalid(errorObj);
													}
												});
												
											}
										}
									}
								},
								{
									xtype: 'button',
									text: 'Cancel',										
									iconCls: 'fa fa-close',
									handler: function(){
										this.up('form').reset();
										this.up('form').getComponent('upload').setFieldLabel('Upload Resource (limit 1GB)');
									}									
								}								
							],
							items: [
								{
									xtype: 'hidden',
									name: 'resourceId'
								},
								{
									xtype: 'hidden',
									name: 'originalFileName'
								},
								{
									xtype: 'hidden',
									name: 'fileName'
								},
								{
									xtype: 'hidden',
									name: 'mimeType'
								},
								Ext.create('OSF.component.StandardComboBox', {
									name: 'resourceType',									
									allowBlank: false,								
									margin: '0 0 0 0',
									editable: false,
									typeAhead: false,
									width: '100%',
									fieldLabel: 'Resource Type <span class="field-required" />',
									storeConfig: {
										url: '../api/v1/resource/lookuptypes/ResourceType'
									}
								}),
								{
									xtype: 'textfield',
									fieldLabel: 'Description',																									
									maxLength: '255',
									name: 'description'
								},
								{
									xtype: 'checkbox',
									name: 'restricted',
									boxLabel: 'Restricted'
								},
								{
									xtype: 'textfield',
									fieldLabel: 'Link',																																	
									maxLength: '255',									
									emptyText: 'http://www.example.com/resource',
									name: 'originalLink'
								},
								{
									xtype: 'filefield',
									itemId: 'upload',
									fieldLabel: 'Upload Resource (Limit of 1GB)',																											
									name: 'file',
									listeners: {
										change: CoreUtil.handleMaxFileLimit
									}
								},
								Ext.create('OSF.component.SecurityComboBox', {	
									hidden: !${branding.allowSecurityMarkingsFlg}
								})								
							]
						},						
						{
							xtype: 'toolbar',
							items: [
								{
									xtype: 'combobox',
									fieldLabel: 'Filter Status',
									store: statusFilterStore,
									forceSelection: true,
									queryMode: 'local',
									displayField: 'desc',
									valueField: 'code',
									value: 'A',
									listeners: {
										change: function(combo, newValue, oldValue, opts){
											this.up('grid').getStore().load({
												url: '../api/v1/resource/components/' + Ext.getCmp('resourcesGrid').componentRecord.get('componentId') + '/resources/view',
												params: {
													status: newValue
												}
											});
										}
									}
								}, 								
								{
									text: 'Refresh',
									iconCls: 'fa fa-refresh',
									handler: function(){
										this.up('grid').getStore().reload();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Edit',
									itemId: 'editBtn',
									iconCls: 'fa fa-edit',
									handler: function(){
										var record = Ext.getCmp('resourcesGrid').getSelection()[0];
										this.up('grid').down('form').reset();
										this.up('grid').down('form').loadRecord(record);
										if (record.get('originalFileName')) {
											this.up('grid').down('form').getComponent('upload').setFieldLabel('Current File: ' + record.get('originalFileName'));
										} else {
											this.up('grid').down('form').getComponent('upload').setFieldLabel('Upload Resource (limit 1GB)');
										}
									}									
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Toggle Status',
									itemId: 'toggleStatusBtn',
									iconCls: 'fa fa-power-off',									
									disabled: true,
									handler: function(){
										actionSubComponentToggleStatus(Ext.getCmp('resourcesGrid'), 'resourceId', 'resources');
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Remove',
									itemId: 'removeBtn',
									iconCls: 'fa fa-trash',																		
									disabled: true,
									handler: function(){
										actionSubComponentToggleStatus(Ext.getCmp('resourcesGrid'), 'resourceId', 'resources', undefined, undefined, true);
									}									
								}
							]
						}
					]					
				});

				var contactGrid = Ext.create('Ext.grid.Panel', {
					id: 'contactGrid',
					title: 'Contacts',
					tooltip: 'Add government, technical, and other points of contact',
					columnLines: true,
					store: Ext.create('Ext.data.Store', {
						fields: [
							"contactId",
							"positionDescription",
							"contactType",
							"name",
							"firstName",
							"lastName",
							"email",
							"phone",
							"organization",
							{
								name: 'updateDts',
								type:	'date',
								dateFormat: 'c'
							},
							"activeStatus"
						],
						autoLoad: false,
						proxy: {
							type: 'ajax'							
						}
					}),					
					columns: [
						{ text: 'Contact Type', dataIndex: 'positionDescription',  width: 200 },
						{ text: 'First Name',  dataIndex: 'firstName', width: 200 },
						{ text: 'Last Name',  dataIndex: 'lastName', width: 200 },
						{ text: 'Email',  dataIndex: 'email', flex: 1, minWidth: 200 },
						{ text: 'Phone',  dataIndex: 'phone', width: 150 },
						{ text: 'Organization',  dataIndex: 'organization', width: 200 },
						{ text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s' },
						{ text: 'Entry Contact Id',  dataIndex: 'componentContactId', width: 200, hidden: true },
						{ text: 'Contact Id',  dataIndex: 'contactId', width: 200, hidden: true },
						{ text: 'Security Marking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: !${branding.allowSecurityMarkingsFlg} }
					],
					listeners: {
						itemdblclick: function(grid, record, item, index, e, opts){
							this.down('form').reset();
							this.down('form').loadRecord(record);
						},
						selectionchange: function(grid, record, index, opts){
							var fullgrid = Ext.getCmp('contactGrid');
							if (fullgrid.getSelectionModel().getCount() === 1) {
								fullgrid.down('toolbar').getComponent('editBtn').setDisabled(false);
								fullgrid.down('toolbar').getComponent('delete').setDisabled(false);
								fullgrid.down('toolbar').getComponent('toggleStatusBtn').setDisabled(false);
							} else {
								fullgrid.down('toolbar').getComponent('editBtn').setDisabled(true);
								fullgrid.down('toolbar').getComponent('delete').setDisabled(true);
								fullgrid.down('toolbar').getComponent('toggleStatusBtn').setDisabled(true);
							}
						}						
					},
					dockedItems: [
						{
							xtype: 'form',
							title: 'Add/Edit Contact',
							collapsible: true,
							titleCollapse: true,
							border: true,
							layout: 'vbox',
							bodyStyle: 'padding: 10px;',
							margin: '0 0 5 0', 
							defaults: {
								labelAlign: 'top',
								labelSeparator: '',
								width: '100%'
							},
							buttonAlign: 'center',
							buttons: [
								{
									xtype: 'button',
									text: 'Save',
									formBind: true,
									margin: '0 20 0 0',
									iconCls: 'fa fa-save',
									handler: function(){	
										var form = this.up('form');
										var data = form.getValues();
										var componentId = Ext.getCmp('contactGrid').componentRecord.get('componentId');

										var method = 'POST';
										var update = '';
										if (data.componentContactId) {
											update = '/' + data.componentContactId;
											method = 'PUT';
										}

										CoreUtil.submitForm({
											url: '../api/v1/resource/components/' + componentId + '/contacts' + update,
											method: method,
											data: data,
											form: form,
											success: function(){
												Ext.getCmp('contactGrid').getStore().reload();
												form.reset();
											}
										});
									}
								},
								{
									xtype: 'button',
									text: 'Cancel',										
									iconCls: 'fa fa-close',
									handler: function(){
										this.up('form').reset();
									}									
								}								
							],
							items: [
								{
									xtype: 'hidden',
									name: 'componentContactId'
								},								
								{
									xtype: 'hidden',
									name: 'contactId'
								},
								Ext.create('OSF.component.StandardComboBox', {
									name: 'contactType',	
									itemId: 'contactType',
									allowBlank: false,								
									margin: '0 0 0 0',
									editable: false,
									typeAhead: false,
									width: '100%',
									fieldLabel: 'Contact Type <span class="field-required" />',
									storeConfig: {
										url: '../api/v1/resource/lookuptypes/ContactType'
									}
								}),
								Ext.create('OSF.component.StandardComboBox', {
									name: 'organization',									
									allowBlank: false,									
									margin: '0 0 0 0',
									width: '100%',
									fieldLabel: 'Organization <span class="field-required" />',
									forceSelection: false,
									valueField: 'description',
									storeConfig: {
										url: '../api/v1/resource/organizations/lookup',
										sorters: [{
											property: 'description',
											direction: 'ASC'
										}]
									}
								}),								
								Ext.create('OSF.component.StandardComboBox', {
									name: 'firstName',									
									allowBlank: false,									
									margin: '0 0 5 0',
									width: '100%',
									fieldLabel: 'First Name  <span class="field-required" />',
									forceSelection: false,
									valueField: 'firstName',
									displayField: 'firstName',
									maxLength: '80',							
									listConfig: {
										itemTpl: [
											 '{firstName} <span style="color: grey">({email})</span>'
										]
									},								
									storeConfig: {
										url: '../api/v1/resource/contacts/filtered'
									},
									listeners: {
										select: function(combo, record, opts) {
											record.set('componentContactId', null);
											record.set('contactId', null);
											var contactType =  combo.up('form').getComponent('contactType').getValue();
											combo.up('form').reset();
											combo.up('form').loadRecord(record);
											combo.up('form').getComponent('contactType').setValue(contactType);
										}
									}
								}),
								{
									xtype: 'textfield',
									fieldLabel: 'Last Name <span class="field-required" />',									
									allowBlank: false,								
									maxLength: '80',
									name: 'lastName'
								},
								{
									xtype: 'textfield',
									fieldLabel: 'Email',																																	
									maxLength: '255',
									regex: new RegExp("[a-z0-9!#$%&'*+/=?^_`{|}~.-]+@[a-z0-9-]+(\.[a-z0-9-]+)*", "i"),
									regexText: 'Must be a valid email address. Eg. xxx@xxx.xxx',
									name: 'email'
								},
								{
									xtype: 'textfield',
									fieldLabel: 'Phone',																											
									maxLength: '120',
									name: 'phone'
								},
								Ext.create('OSF.component.SecurityComboBox', {	
									hidden: !${branding.allowSecurityMarkingsFlg}
								})								
							]
						},						
						{
							xtype: 'toolbar',
							items: [
								{
									xtype: 'combobox',
									fieldLabel: 'Filter Status',
									store: statusFilterStore,
									forceSelection: true,
									queryMode: 'local',
									displayField: 'desc',
									valueField: 'code',
									value: 'A',
									listeners: {
										change: function(combo, newValue, oldValue, opts){
											this.up('grid').getStore().load({
												url: '../api/v1/resource/components/' + Ext.getCmp('contactGrid').componentRecord.get('componentId') + '/contacts/view',
												params: {
													status: newValue
												}
											});
										}
									}
								}, 								
								{
									text: 'Refresh',
									iconCls: 'fa fa-refresh',
									handler: function(){
										this.up('grid').getStore().reload();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Edit',
									itemId: 'editBtn',
									iconCls: 'fa fa-edit',
									handler: function(){
										this.up('grid').down('form').reset();
										this.up('grid').down('form').loadRecord(Ext.getCmp('contactGrid').getSelection()[0]);
									}									
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Toggle Status',
									itemId: 'toggleStatusBtn',
									iconCls: 'fa fa-power-off',									
									disabled: true,
									handler: function(){
										actionSubComponentToggleStatus(Ext.getCmp('contactGrid'), 'componentContactId', 'contacts');
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Remove',
									itemId: 'delete',
									iconCls: 'fa fa-trash-o',									
									disabled: true,
									handler: function(){
										
										Ext.Msg.show({
											title:'Remove Contact?',
											message: 'Are you sure you want to delete the contact from this entry?',
											buttons: Ext.Msg.YESNO,
											icon: Ext.Msg.QUESTION,
											fn: function(btn) {
												if (btn === 'yes') {
													actionSubComponentToggleStatus(Ext.getCmp('contactGrid'), 'componentContactId', 'contacts', undefined, undefined, true);
												}  
											}
										});
									}
								}								
							]
						}
					]											
				});

				var relationshipsGrid = Ext.create('Ext.grid.Panel', {
					id: 'relationshipsGrid',
					title: 'Relationships',
					tooltip: 'Relationships are graphable connections between entries.',
					columnLines: true,
					store: Ext.create('Ext.data.Store', {
						fields: [
							"relationshipId",
							"ownerComponentId",
							"ownerComponentName",
							"ownerApproved",
							"targetComponentId",
							"targetComponentName",
							"targetApproved",
							"relationshipType",
							"relationshipTypeDescription",
							{
								name: 'updateDts',
								type:	'date',
								dateFormat: 'c'
							}
						],
						autoLoad: false,
						proxy: {
							type: 'ajax'							
						}
					}),					
					columns: [
						{ text: 'Relationship Owner', dataIndex: 'ownerComponentName',  width: 200 },
						{ text: 'Owner Approved', dataIndex: 'ownerApproved',  width: 150 },
						{ text: 'Type',  dataIndex: 'relationshipTypeDescription', width: 200 },
						{ text: 'Target',  dataIndex: 'targetComponentName', flex: 1, minWidth: 200 },						
						{ text: 'Target Approved',  dataIndex: 'targetApproved', width: 150 },		
						{ text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s' }
					],
					listeners: {
						selectionchange: function(grid, record, index, opts){
							var fullgrid = Ext.getCmp('relationshipsGrid');
							if (fullgrid.getSelectionModel().getCount() === 1) {								
								fullgrid.down('toolbar').getComponent('removeBtn').setDisabled(false);
							} else {								
								fullgrid.down('toolbar').getComponent('removeBtn').setDisabled(true);
							}
						}						
					},
					dockedItems: [
						{
							xtype: 'form',
							title: 'Add/Edit Relationship',
							collapsible: true,
							titleCollapse: true,
							border: true,
							layout: 'vbox',
							bodyStyle: 'padding: 10px;',
							margin: '0 0 5 0', 
							defaults: {
								labelAlign: 'top',
								labelSeparator: '',
								width: '100%'
							},
							buttonAlign: 'center',
							buttons: [
								{
									xtype: 'button',
									text: 'Save',
									formBind: true,
									margin: '0 20 0 0',
									iconCls: 'fa fa-save',
									handler: function(){	
										var form = this.up('form');
										var data = form.getValues();
										var componentId = Ext.getCmp('relationshipsGrid').componentRecord.get('componentId');

										var method = 'POST';
										var update = '';
										
										CoreUtil.submitForm({
											url: '../api/v1/resource/components/' + componentId + '/relationships' + update,
											method: method,
											data: data,
											form: form,
											success: function(){
												Ext.getCmp('relationshipsGrid').getStore().reload();
												form.reset();
											}
										});
									}
								},
								{
									xtype: 'button',
									text: 'Cancel',										
									iconCls: 'fa fa-close',
									handler: function(){
										this.up('form').reset();
									}									
								}								
							],
							items: [								
								Ext.create('OSF.component.StandardComboBox', {
									name: 'relationshipType',									
									allowBlank: false,
									editable: false,
									typeAhead: false,
									margin: '0 0 0 0',
									width: '100%',
									fieldLabel: 'Relationship Type <span class="field-required" />',
									storeConfig: {
										url: '../api/v1/resource/lookuptypes/RelationshipType'
									}
								}),
								Ext.create('OSF.component.StandardComboBox', {
									name: 'componentType',									
									allowBlank: true,
									editable: false,
									typeAhead: false,
									emptyText: 'All',
									margin: '0 0 0 0',
									width: '100%',
									fieldLabel: 'Entry Type',
									storeConfig: {
										url: '../api/v1/resource/componenttypes/lookup',
										addRecords: [
											{
												code: null,
												description: 'All'
											} 
										]
									},
									listeners: {
										change: function(cb, newValue, oldValue) {
											var componentType = '';
											if (newValue) {
												componentType = '&componentType=' + newValue;
											}
											Ext.getCmp('relationshipTargetCB').reset();
											Ext.getCmp('relationshipTargetCB').getStore().load({
												url: '../api/v1/resource/components/lookup?status=A&approvalState=ALL' + componentType	
											});
										}
									}
								}),								
								Ext.create('OSF.component.StandardComboBox', {
									id: 'relationshipTargetCB',
									name: 'relatedComponentId',									
									allowBlank: false,									
									margin: '0 0 0 0',
									width: '100%',
									fieldLabel: 'Target Entry <span class="field-required" />',
									forceSelection: false,
									storeConfig: {
										url: '../api/v1/resource/components/lookup?status=A&approvalState=ALL',
										autoLoad: false
									}
								})				
							]
						},						
						{
							xtype: 'toolbar',
							items: [						
								{
									text: 'Refresh',
									iconCls: 'fa fa-refresh',
									handler: function(){
										this.up('grid').getStore().reload();
									}
								},								
								{
									xtype: 'tbfill'
								},
								{
									text: 'Remove',
									itemId: 'removeBtn',
									iconCls: 'fa fa-trash',								
									disabled: true,
									handler: function(){
										actionSubComponentToggleStatus(Ext.getCmp('relationshipsGrid'), 'relationshipId', 'relationships');
									}
								}
							]
						}
					]																
				});
		
				var attributeGrid = Ext.create('Ext.grid.Panel', {
					id: 'attributeGrid',
					title: 'Attributes',
					tooltip: 'Attributes are filterable metadata about the entry.',
					columnLines: true,
					store: Ext.create('Ext.data.Store', {
						fields: [
							"type",
							"code",
							"typeDescription",
							"codeDescription",
							"orphan",
							"activeStatus",
							{
								name: 'updateDts',
								type:	'date',
								dateFormat: 'c'
							}
						],
						autoLoad: false,
						proxy: {
							type: 'ajax'							
						}
					}),				
					columns: [
						{ text: 'Attribute Type', dataIndex: 'typeDescription',  width: 200 },
						{ text: 'Attribute Code', dataIndex: 'codeDescription', flex: 1, minWidth: 200 },
						{ text: 'Update Date', dataIndex: 'updateDts', width: 175, xtype: 'datecolumn', format: 'm/d/y H:i:s' }
					],
					listeners: {
						selectionchange: function(grid, record, index, opts){
							var fullgrid = Ext.getCmp('attributeGrid');
							if (fullgrid.getSelectionModel().getCount() === 1) {								
								fullgrid.down('toolbar').getComponent('toggleStatusBtn').setDisabled(false);
								fullgrid.down('toolbar').getComponent('removeBtn').setDisabled(false);
							} else {								
								fullgrid.down('toolbar').getComponent('toggleStatusBtn').setDisabled(true);
								fullgrid.down('toolbar').getComponent('removeBtn').setDisabled(true);
							}
						}						
					},
					dockedItems: [
						{
							xtype: 'form',
							title: 'Add Attribute',
							collapsible: true,
							titleCollapse: true,
							border: true,
							layout: 'vbox',
							bodyStyle: 'padding: 10px;',
							margin: '0 0 5 0', 
							defaults: {
								labelAlign: 'top',
								labelSeparator: '',
								width: '100%'
							},
							buttonAlign: 'center',
							buttons: [
								{
									xtype: 'button',
									text: 'Save',
									formBind: true,
									margin: '0 20 0 0',
									iconCls: 'fa fa-save',
									handler: function(){	
										var form = this.up('form');
										var data = form.getValues();
										var componentId = Ext.getCmp('attributeGrid').componentRecord.get('componentId');
											
										data.componentAttributePk = {
											attributeType: data.attributeType,
											attributeCode: data.attributeCode
										};
											
										var method = 'POST';
										var update = '';										

										CoreUtil.submitForm({
											url: '../api/v1/resource/components/' + componentId + '/attributes' + update,
											method: method,
											data: data,
											form: form,
											success: function(){
												loadComponentAttributes(Ext.getCmp('attributeFilterActiveStatus').getValue());
												form.reset();
											}
										});
									}
								},
								{
									xtype: 'button',
									text: 'Cancel',										
									iconCls: 'fa fa-close',
									handler: function(){
										this.up('form').reset();
									}									
								}								
							],
							items: [
								{
									xtype: 'combobox',
									itemId: 'attributeTypeCB',
									fieldLabel: 'Attribute Type <span class="field-required" />',
									name: 'attributeType',
									forceSelection: true,	
									queryMode: 'local',
									editable: false,
									typeAhead: false,										
									allowBlank: false,
									valueField: 'attributeType',
									displayField: 'description',										
									store: Ext.create('Ext.data.Store', {
										fields: [
											"attributeType",
											"description"
										]																							
									}),
									listeners: {
										change: function (field, newValue, oldValue, opts) {
											field.up('form').getComponent('attributeCodeCB').clearValue();
											
											var record = field.getSelection();		
											if (record) {
												field.up('form').getComponent('attributeCodeCB').getStore().loadData(record.data.codes);
											} else {
												field.up('form').getComponent('attributeCodeCB').getStore().removeAll();
											}
										}
									}
								},
								{
									xtype: 'combobox',
									itemId: 'attributeCodeCB',
									fieldLabel: 'Attribute Code <span class="field-required" />',
									name: 'attributeCode',
									forceSelection: true,	
									queryMode: 'local',
									editable: false,
									typeAhead: false,										
									allowBlank: false,
									valueField: 'code',
									displayField: 'label',										
									store: Ext.create('Ext.data.Store', {
										fields: [
											"code",
											"label"
										]																							
									})									
								}
							]
						},						
						{
							xtype: 'toolbar',
							items: [
								{
									xtype: 'combobox',
									id: 'attributeFilterActiveStatus',
									fieldLabel: 'Filter Status',
									store: statusFilterStore,
									forceSelection: true,
									queryMode: 'local',
									displayField: 'desc',
									valueField: 'code',
									value: 'A',
									listeners: {
										change: function(combo, newValue, oldValue, opts){
											loadComponentAttributes(newValue);
										}
									}
								}, 								
								{
									text: 'Refresh',
									iconCls: 'fa fa-refresh',
									handler: function(){
										loadComponentAttributes(Ext.getCmp('attributeFilterActiveStatus').getValue());
									}
								},								
								{
									xtype: 'tbfill'
								},
								{
									text: 'Toggle Status',
									itemId: 'toggleStatusBtn',
									iconCls: 'fa fa-power-off',									
									disabled: true,
									handler: function(){
										actionSubComponentToggleStatus(Ext.getCmp('attributeGrid'), 'type', 'attributes', 'code', null, null, function(){
											loadComponentAttributes(Ext.getCmp('attributeFilterActiveStatus').getValue());
										});
									}
								},								
								{
									text: 'Remove',
									itemId: 'removeBtn',
									iconCls: 'fa fa-trash',									
									disabled: true,
									handler: function(){
										actionSubComponentToggleStatus(Ext.getCmp('attributeGrid'), 'type', 'attributes', 'code', null, true, function(){
											loadComponentAttributes(Ext.getCmp('attributeFilterActiveStatus').getValue());
										});
									}
								}
							]
						}
					]																					
				});
				
				var loadComponentAttributes = function(status) {
					if (Ext.getCmp('generalForm').componentRecord) {
						var componentId = Ext.getCmp('generalForm').componentRecord.get('componentId');
						Ext.Ajax.request({
							url: '../api/v1/resource/components/' + componentId + '/attributes/view',
							method: 'GET',
							params: {
								status: status
							},
							success: function(response, opts) {
								var data = Ext.decode(response.responseText);

								var requiredStore = Ext.data.StoreManager.lookup('requiredAttributeStore');

								var optionalAttributes = [];
								Ext.Array.each(data, function(attribute) {
									if (attribute.requiredFlg) {
										var found = false;
										requiredStore.each(function(record){
											if (record.get('attributeType') === attribute.type) {
												record.set('attributeCode', attribute.code, { dirty: false });
												found = true;
											}
										});
										if (!found) {
											//the component type  may not require this
											optionalAttributes.push(attribute);
										}
									} else {
										optionalAttributes.push(attribute);
									}
								});
								optionalAttributes.reverse();
								Ext.getCmp('attributeGrid').getStore().loadData(optionalAttributes);
							}
						});
					} else {
						var requiredStore = Ext.data.StoreManager.lookup('requiredAttributeStore');
						requiredStore.each(function(record) {
							record.set('attributeCode', null, { dirty: false} );
						});
					}
				};
				
				var handleAttributes = function(componentType) {
					
					var requiredAttributes = [];
					var optionalAttributes = [];
					// This is slightly difficult to follow,
					// but the basic gist is that we must check two lists to decide which attributes to show -
					// requiredRestrictions is a list of types for which the attribute is required
					// associatedComponentTypes is a list of types for which the attribute is optional
					// but if associatedComponentTypes is empty, it is optional for all.
					Ext.Array.each(allAttributes, function(attribute){
						if (attribute.requiredFlg) {
							if (attribute.requiredRestrictions) {
								var found = Ext.Array.findBy(attribute.requiredRestrictions, function(item){
									if (item.componentType === componentType) {
										return true;
									} else {
										return false;
									}
								});
								if (found) {
									// The required flag is set and this entry type is one which requires this attribute.
									requiredAttributes.push(attribute);
								}
								else {
									// --- Checking for Optional
									//
									// In this case, the 'Required' Flag is set but the entry we are dealing with is not an entry
									// type listed in the requiredRestrictions, i.e. not required for this entry type.
									// As a result, we need to check if it's allowed as an optional and then add it.
									// This is the same logic as seen below when the 'Required' flag is off.
									if (attribute.associatedComponentTypes) {
										var reqOptFound = Ext.Array.findBy(attribute.associatedComponentTypes, function(item) {
											if (item.componentType === componentType) {
												return true;
											} else {
												return false;
											}
										});
										if (reqOptFound) {
											optionalAttributes.push(attribute);
										}
									}
									else {
										// We have an empty list of associatedComponentTypes, therefore this attribute is
										// allowed for all entry types.
										optionalAttributes.push(attribute);
									}
									// 
									// --- End Checking for Optional
								}
							} else {
								// No list of types required for, so it's required for all. Add it.
								requiredAttributes.push(attribute);
							}
						} else {
							if (attribute.associatedComponentTypes) {
								var optFound = Ext.Array.findBy(attribute.associatedComponentTypes, function(item) {
									if (item.componentType === componentType) {
										return true;
									} else {
										return false;
									}
								});
								if (optFound) {
									// This entry type allows this attribute.
									optionalAttributes.push(attribute);
								}
							}
							else {
								// We have an empty list of associatedComponentTypes, therefore this attribute is
								// allowed for all entry types.
								optionalAttributes.push(attribute);
							}
						}
					});
					
					var requiredStore = Ext.data.StoreManager.lookup('requiredAttributeStore');
					
					requiredAttributes.reverse();
					requiredStore.loadData(requiredAttributes);
					
					Ext.getCmp('attributeGrid').down('form').getComponent('attributeTypeCB').getStore().loadData(optionalAttributes);
					loadComponentAttributes(Ext.getCmp('attributeFilterActiveStatus').getValue());
				};
			
				var requiredAttributeStore = Ext.create('Ext.data.Store', {
					storeId: 'requiredAttributeStore',
					fields: [
						"attributeType",
						"attributeCode",
						"description"
					],
					autoLoad: false,
					listeners: {
						datachanged: function(store) {
							var panel = Ext.getCmp('generalForm').getComponent('requiredAttributePanel');					
							panel.removeAll();

							store.each(function(record) {

								var field = Ext.create('Ext.form.field.ComboBox', {
									record: record,
									fieldLabel: record.get('description') + ' <span class="field-required" />',
									forceSelection: true,
									queryMode: 'local',
									editable: false,
									typeAhead: false,
									allowBlank: false,
									width: '100%',							
									labelWidth: 300,
									labelSepartor: '',							
									valueField: 'code',
									displayField: 'label',
									store: Ext.create('Ext.data.Store', {
										data: record.data.codes								
									}),
									listeners: {
										change: function(fieldLocal, newValue, oldValue, opts) {
											var recordLocal = fieldLocal.record;
											if (recordLocal) {
												recordLocal.set('attributeCode', newValue);																								
											}
										}
									}
								});						
								record.formField = field;
								panel.add(field);
							});
							panel.updateLayout(true, true);					
						},
						update: function(store, record, operation, modifiedFieldNames, details, opts) {
							if (record.formField) {
								record.formField.setValue(record.get('attributeCode'));
							}
						}
					}
				});
							
				var generalForm = Ext.create('Ext.form.Panel', {
					id: 'generalForm',
					title: 'General',
					tooltip: 'General and required information on an entry.',					
					autoScroll: true,					
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									xtype: 'tbfill'
								},
								{
									text: 'Message Submitter',
									iconCls: 'fa fa-envelope',
									handler: function() {
										
										//get submiter  
										var emails = '';
										Ext.getCmp('contactGrid').getStore().each(function(record){
											if (record.get('contactType') === 'SUB') {
												if (record.get('email')) {
													emails += record.get('email') + '; ';
												}
											}
										});
										
										var messageWindow = Ext.create('OSF.component.MessageWindow', {					
											closeAction: 'destory',
											alwaysOnTop: true,
											initialToUsers: emails
										}).show();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Integration',
									id: 'integrationBtn',
									iconCls: 'fa fa-gear',
									disabled: true,
									handler: function() {
										integrationWindow.show();
										integrationWindow.loadConfigs(Ext.getCmp('generalForm').componentRecord.get('componentId'));
									}									
								}
							]
						},
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [
								{
									text: 'Save',
									tooltip: 'Save General information and continue.',
									iconCls: 'fa fa-save',
									formBind: true,
									handler: function() {
										var form = this.up('form');
										var data = form.getValues();
										var componentId = '';
										var method = 'POST';
										var update = '';
										var edit = false;
										if (Ext.getCmp('generalForm').componentRecord){
											componentId = Ext.getCmp('generalForm').componentRecord.get('componentId');											
											update = '/' + componentId;
											method = 'PUT';
											edit = true;
										}												

										var requireComponent = {
											component: data,
											attributes: []
										};
										
										Ext.data.StoreManager.lookup('requiredAttributeStore').each(function(record){
											requireComponent.attributes.push({
												componentAttributePk: {
													attributeType: record.get('attributeType'),
													attributeCode: record.get('attributeCode')
												}
											});
										});
										
										if (!data.description) {
											form.getForm().markInvalid({
												description: 'Required'
											});
										} else {
											//make sure required 
											var validAttributes=true;
											Ext.Array.each(requireComponent.attributes, function(attribute){
												if (!attribute.componentAttributePk.attributeCode){
													validAttributes = false;
												}
											});

											if (!validAttributes) {
												Ext.Msg.show({
													title:'Validation Check',
													message: 'Missing Required Attributes; Check input.',
													buttons: Ext.Msg.OK,
													icon: Ext.Msg.ERROR,
													fn: function(btn) {													 
													}
												});

											} else {	
												CoreUtil.removeBlankDataItem(requireComponent.component);												
												CoreUtil.submitForm({
													url: '../api/v1/resource/components' + update,
													method: method,
													data: requireComponent,
													removeBlankDataItems: true,
													form: form,
													success: function(response, opt){
														Ext.toast('Successfully Saved Record');
														var data = Ext.decode(response.responseText);														
														
														var componentView = {};
														componentView.component = data.component;
														
														var record;
														if (!edit) {
															var record = Ext.getCmp('componentGrid').getStore().add(componentView.component);
															record = record[0];
															Ext.getCmp('componentGrid').getStore().reload();
														} else {
															Ext.Object.each(componentView.component, function(key, value, myself) {
																Ext.getCmp('generalForm').componentRecord.set(key, value, { dirty: false}); 
															});	
															record = Ext.getCmp('generalForm').componentRecord;
															Ext.getCmp('componentGrid').getStore().reload();
														}
														actionAddEditComponent(record);																																									
														
													},
													failure: function(response, opt){

													}
												});
											}
										}
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Done',
									tooltip: 'Close Add/Edit window',
									iconCls: 'fa fa-close',
									handler: function() {
										this.up('window').close();
									}													
								}
							]
						}
					],
					items: [
						{
							xtype: 'panel',
							title: 'Information',
							layout: 'hbox',
							collapsible: true,
							titleCollapse: true,
							border: true,
							bodyStyle: 'padding: 10px;',
							items: [
								{
									xtype: 'panel',
									width: '50%',		
									defaults: {
										labelWidth: 150,
										labelStyle: 'font-weight: bold;',
										width: '80%',
										xtype: 'displayfield',										
										readOnly: true
									},
									items: [
										{		
											xtype: 'displayfield',
											name: 'lastActivityDts',											
											fieldLabel: 'Last Activity Date',
											renderer: function(value, field){										
												return Ext.util.Format.date(value, 'm/d/y H:i:s');
											}
										},
										{											
											name: 'submittedDts',											
											fieldLabel: 'User Submitted Date',
											renderer: function(value, field){										
												return Ext.util.Format.date(value, 'm/d/y H:i:s');
											}
										},
										{
											name: 'lastModificationType',
											fieldLabel: 'Last Entry Method'
										},
										{
											name: 'approvedDts',
											fieldLabel: 'Approval Date',
											renderer: function(value, field){										
												return Ext.util.Format.date(value, 'm/d/y H:i:s');
											}
										},
										{											
											name: 'approvedUser',											
											fieldLabel: 'Approval User'
										}
									]
								},
								{
									xtype: 'panel',
									width: '50%',
									defaults: {
										labelWidth: 150,
										labelStyle: 'font-weight: bold;',
										width: '80%',
										xtype: 'displayfield',
										readOnly: true
									},
									items: [
										{
											name: 'externalId',
											fieldLabel: 'External Id'
										},
										{
											name: 'recordVersion',
											fieldLabel: 'Record Version'
										},
										{
											name: 'createDts',
											fieldLabel: 'Create Date',
											renderer: function(value, field){										
												return Ext.util.Format.date(value, 'm/d/y H:i:s');
											}							
										},
										{
											name: 'createUser',
											fieldLabel: 'Create User'
										},
										{
											name: 'updateUser',
											fieldLabel: 'Update User'
										}										
									]
								}
							]
						},
						{
							xtype: 'panel',
							bodyStyle: 'padding: 10px;',	
							layout: 'vbox',
							defaults: {
								labelAlign: 'top',
								labelSeparator: '',
								width: '100%'
							},
							items: [
								Ext.create('OSF.component.StandardComboBox', {
									id: 'componentTypeMainCB',
									fieldLabel: 'Entry Type <span class="field-required" />',
									name: 'componentType',
									allowBlank: false,								
									margin: '0 0 0 0',
									width: '100%',
									editable: false,
									typeAhead: false,										
									storeConfig: {
										url: '../api/v1/resource/componenttypes/lookup'																				
									},
									listeners: {
										change: function(field, newValue, oldValue, opts) {
											handleAttributes(newValue);
											checkFormTabs(Ext.getCmp('generalForm').componentRecord, newValue);
										}
									}
								}),						
								{
									xtype: 'textfield',
									name: 'name',
									fieldLabel: 'Name <span class="field-required" />',
									allowBlank: false,
									maxLength: 255														
								},
								{
									xtype: 'panel',
									html: '<b>Description</b> <span class="field-required" />'
								},
								//{
								//	xtype: 'htmleditor',
								//	fieldLabel: 'Description <span class="field-required" />',
								//	fieldBodyCls: 'form-comp-htmleditor-border',
								//	height: 300,
								//	width: '100%',
								//	name: 'description'
								//},
//								Ext.create('OSF.component.CKEditorField', {																
//									//allowBlank: false,
//									name: 'description',
//									height: 300,
//									maxLength: 32000	
//								}),
								{
									xtype: 'tinymce_textarea',
									fieldStyle: 'font-family: Courier New; font-size: 12px;',
									style: { border: '0' },
									name: 'description',
									width: '100%',
									height: 300,
									maxLength: 65536,
									tinyMCEConfig: CoreUtil.tinymceSearchEntryConfig()
								},								
								Ext.create('OSF.component.StandardComboBox', {
									name: 'organization',									
									allowBlank: false,									
									margin: '0 0 0 0',
									width: '100%',
									fieldLabel: 'Organization <span class="field-required" />',
									forceSelection: false,
									valueField: 'description',
									editable: true,
									storeConfig: {
										url: '../api/v1/resource/organizations/lookup',
										sorters: [{
											property: 'description',
											direction: 'ASC'
										}]
									}
								}),
								Ext.create('OSF.component.StandardComboBox', {														
									fieldLabel: 'Approval Status <span class="field-required" />',
									name: 'approvalState',
									allowBlank: false,								
									margin: '0 0 0 0',
									width: '100%',
									editable: false,
									typeAhead: false,										
									storeConfig: {
										url: '../api/v1/resource/lookuptypes/ApprovalStatus'																				
									}
								}),								
								{
									xtype: 'datefield',
									fieldLabel: 'Release Date',
									name: 'releaseDate'									
								},
								{
									xtype: 'textfield',
									fieldLabel: 'Version',
									name: 'version'																		
								},
								{
									xtype: 'textfield',
									fieldLabel: 'GUID',
									name: 'guid'																		
								},
								Ext.create('OSF.component.StandardComboBox', {														
									fieldLabel: 'Data Source',
									name: 'dataSource',	
									width: '100%',
									margin: '0 0 0 0',
									editable: false,
									typeAhead: false,
									storeConfig: {
										url: '../api/v1/resource/lookuptypes/DataSource'										
									}
								}),
								Ext.create('OSF.component.SecurityComboBox', {	
									hidden: !${branding.allowSecurityMarkingsFlg}
								})
							]							
						},
						{
							xtype: 'panel',
							itemId: 'requiredAttributePanel',
							title: 'Required Attributes',
							frame: true,
							bodyStyle: 'padding: 10px;',
							margin: '20 0 20 0'									
						}
					]
				});
				
				var allAttributes = [];
				var loadAllAttributes = function(){
					Ext.Ajax.request({
						url: '../api/v1/resource/attributes',
						success: function(response, opts){
							allAttributes = Ext.decode(response.responseText);
						}
					});
				};
				loadAllAttributes();
						
				var mainAddEditWin = Ext.create('Ext.window.Window', {					
					title: 'Entry Form',
					modal: true,
					maximizable: true,
					layout: 'fit',
					width: '80%',
					height: '90%',
					iconCls: 'fa fa-lg fa-edit',
					items: [
						{
							xtype: 'tabpanel',
							id: 'mainFormTabPanel',
							items: [
								generalForm,
								attributeGrid,
								relationshipsGrid,
								contactGrid,
								resourcesGrid,
								mediaGrid,
								dependenciesGrid,
								metadataGrid,
								evaluationGrid,
								reviewGrid,
								questionContainer,
								tagGrid
							]
						}
					]
				});
	
			//MAIN GRID -------------->			
				var versionViewTemplate = new Ext.XTemplate(						
				);
		
				Ext.Ajax.request({
					url: 'Router.action?page=shared/entrySimpleViewTemplate.jsp',
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
									{
										name: 'createDts',
										type:	'date',
										dateFormat: 'c'
									},									
									{			
										name: 'updateDts',
										type:	'date',
										dateFormat: 'c'
									},									
									  "updateUser",									 
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
											text: 'Remove',
											id: 'versionWin-tool-removeBtn',
											iconCls: 'fa fa-trash',											
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
							flex: 1,
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
								window.open('view.jsp?fullPage=true&id=' + Ext.getCmp('componentGrid').getSelection()[0].get('componentId'), "Preview");
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
				
				var changeOwnerWin = Ext.create('Ext.window.Window', {
					id: 'changeOwnerWin',
					title: 'Change Owner - ',
					iconCls: 'fa fa-user',
					width: '35%',
					height: 150,
					y: 200,
					modal: true,
					layout: 'fit',					
					items: [
						{
							xtype: 'form',
							itemId: 'changeOwnerForm',
							bodyStyle: 'padding: 10px',
							items: [
								{
									xtype: 'combobox',
									fieldLabel: 'Username <span class="field-required" />',
									labelAlign: 'top',
									labelSeparator: '',
									typeAhead: true,
									editable: true,
									allowBlank: false,
									name: 'createUser',
									width: '100%',
									valueField: 'username',
									forceSelection: false,	
									queryMode: 'local',									
									displayField: 'username',
									store: {
										autoLoad: true,
										proxy: {
											type: 'ajax',
											url: '../api/v1/resource/userprofiles',
											reader: {
												type: 'json',
												rootProperty: 'data',
												totalProperty: 'totalNumber'
											}
										}
									}
								}
							],
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										{
											text: 'Update',
											formBind: true,
											iconCls: 'fa fa-save',
											handler: function(){
												var ownerWindow = this.up('window');
												var form = this.up('form');
												var username = form.getForm().findField('createUser').getValue();
												form.setLoading('Updating Owner...');
												Ext.Ajax.request({
													url: '../api/v1/resource/components/' + ownerWindow.componentId + '/changeowner',
													method: 'PUT',
													rawData: username,
													callback: function(opts, success, response){
														form.setLoading(false);
													},
													success: function(response, opts) {
														ownerWindow.close();
														actionRefreshComponentGrid();														
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
												this.up('window').close();
											}
										}
									]
								}
							]
						}
					]
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
						{name: 'description', mapping: function(data){
							return data.component.description;
						}},
						{name: 'activeStatus', mapping: function(data){
							return data.component.activeStatus;
						}},
						{name: 'approvalState', mapping: function(data){
							return data.component.approvalState;
						}},
						{name: 'approvalStateLabel', mapping: function(data){
							return data.component.approvalStateLabel;
						}},					
						{name: 'approvedDts', 
							type:	'date',
							dateFormat: 'c',									
							mapping: function(data){								
							return data.component.approvedDts;
						}},
						{name: 'approvedUser', mapping: function(data){
							return data.component.approvedUser;
						}},
						{name: 'componentId', mapping: function(data){
							return data.component.componentId;
						}},
						{name: 'componentType', mapping: function(data){
							return data.component.componentType;
						}},
						{name: 'componentTypeLabel', mapping: function(data){
							return data.component.componentTypeLabel;
						}},					
						{name: 'organization', mapping: function(data){
							return data.component.organization;
						}},					
						{name: 'createDts', 
							type:	'date',
							dateFormat: 'c',																
							mapping: function(data){
							return data.component.createDts;
						}},
						{name: 'createUser', mapping: function(data){
							return data.component.createUser;
						}},
						{name: 'lastActivityDts', 
							type:	'date',
							dateFormat: 'c',																
							mapping: function(data){
							return data.component.lastActivityDts;
						}},
						{name: 'guid', mapping: function(data){
							return data.component.guid;
						}},					
						{name: 'externalId', mapping: function(data){
							return data.component.externalId;
						}},					
						{name: 'changeApprovalMode', mapping: function(data){
							return data.component.changeApprovalMode;
						}},					
						{name: 'notifyOfApprovalEmail', mapping: function(data){
							return data.component.notifyOfApprovalEmail;
						}},	
						{name: 'dataSource', mapping: function(data){
							return data.component.dataSource;
						}},
						{name: 'securityMarkingType', mapping: function(data){
							return data.component.securityMarkingType;
						}},	
						{name: 'securityMarkingDescription', mapping: function(data){
							return data.component.securityMarkingDescription;
						}},					
						{name: 'lastModificationType', mapping: function(data){
							return data.component.lastModificationType;
						}},						
						{name: 'fileHistoryId', mapping: function(data){
							return data.component.fileHistoryId;
						}},						
						{name: 'recordVersion', mapping: function(data){
							return data.component.recordVersion;
						}},											
						{name: 'submittedDts', 
							type:	'date',
							dateFormat: 'c',							
							mapping: function(data){
							return data.component.submittedDts;
						}},
						{name: 'updateDts', 
							type:	'date',
							dateFormat: 'c',																
							mapping: function(data){
							return data.component.updateDts;
						}},	
						{name: 'updateUser', mapping: function(data){
							return data.component.updateUser;
						}},
						{name: 'numberOfPendingChanges', mapping: function(data){
							return data.component.numberOfPendingChanges;
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
					}),
					listeners: {
						beforeLoad: function(store, operation, eOpts){
							store.getProxy().extraParams = {
								status: Ext.getCmp('componentGridFilter-ActiveStatus').getValue() ? Ext.getCmp('componentGridFilter-ActiveStatus').getValue() : 'ALL',
								approvalState: Ext.getCmp('componentGridFilter-ApprovalStatus').getValue() ? Ext.getCmp('componentGridFilter-ApprovalStatus').getValue() : 'ALL',
								componentType: Ext.getCmp('componentGridFilter-ComponentType').getValue() ? Ext.getCmp('componentGridFilter-ComponentType').getValue() : 'ALL'
							};
						}
					}					
				});	
				
				var mergeComponentWin = Ext.create('Ext.window.Window', {
					id: 'mergeComponentWin',
					title: 'Merge',
					width: '40%',
					height: 210,
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
									queryLocal: true,
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
							},
							renderer: function (value, meta, record) {
								return record.get('componentTypeLabel');
							}							
						},
						{ text: 'Description', dataIndex: 'description', flex: 1, minWidth: 150,
						 renderer: function(value){
							return Ext.util.Format.stripTags(value);
						}},
						{ text: 'Pending Changes', tooltip: 'See Action->Change Requests to view; Sort is not supported. ', align: 'center', dataIndex: 'numberOfPendingChanges', width: 150, sortable: false },
						{ text: 'Last Activity Date', dataIndex: 'lastActivityDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Submitted Date', dataIndex: 'submittedDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },						
						{ text: 'Approval State', align: 'center', dataIndex: 'approvalState', width: 125,
							renderer: function (value, meta, record) {
								return record.get('approvalStateLabel');
							}
						},
						{ text: 'Approval Date', dataIndex: 'approvedDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Active Status', align: 'center', dataIndex: 'activeStatus', width: 125 },
						{ text: 'Integration Management', dataIndex: 'integrationManagement', width: 175 },
						{ text: 'Update Date', dataIndex: 'updateDts', width: 175, hidden: true, xtype: 'datecolumn', format:'m/d/y H:i:s'},
						{ text: 'Update User', dataIndex: 'updateUser', width: 175, hidden: true },
						{ text: 'Create Date', dataIndex: 'createDts', width: 175, hidden: true, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Create User (Owner)', dataIndex: 'createUser', width: 175, hidden: true },
						{ text: 'Component Id', dataIndex: 'componentId', width: 175, hidden: true },
						{ text: 'Security Marking', dataIndex: 'securityMarkingDescription', width: 175, hidden: true }
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
									typeAhead: false,
									editable: false,
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
									typeAhead: false,
									editable: false,									
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
									valueField: 'componentType',
									displayField: 'label',
									typeAhead: false,
									editable: false,
									listeners: {
										change: function(filter, newValue, oldValue, opts){
											actionRefreshComponentGrid();
										}
									},									
									storeConfig: {
										url: '../api/v1/resource/componenttypes',
										model: undefined,										
										fields: [
											'componentType',
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
										actionAddEditComponent();
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
										actionAddEditComponent(Ext.getCmp('componentGrid').getSelection()[0]);
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
											text: 'Change Owner',											
											iconCls: 'fa fa-user',
											handler: function(){
												actionChangeOwner(Ext.getCmp('componentGrid').getSelection()[0]);
											}
										},
										{
											text: 'Change Requests',																		
											iconCls: 'fa fa-edit',
											handler: function () {												
												actionChangeRequests(Ext.getCmp('componentGrid').getSelection()[0]);
											}
										},										
										{
											xtype: 'menuseparator'
										},										
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
										importWindow.show();
									}
								},	
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
						}
					],
					listeners: {
						itemdblclick: function(grid, record, item, index, e, opts){
							actionAddEditComponent(record);
						},
						selectionchange: function(selectionModel, records, opts){
							checkComponetGridTools();
						}
					},
					bbar: Ext.create('Ext.PagingToolbar', {
						store: maingridStore,
						displayInfo: true,
						displayMsg: 'Displaying Entries {0} - {1} of {2}',
						emptyMsg: "No entries to display"
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
					} else if (componentGrid.getSelectionModel().getCount() > 1) {
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
				
				var actionChangeRequests = function(record) {
					var componentId = record.get('componentId');
					var name = record.get('name');
					changeRequestWindow.show();
					changeRequestWindow.loadComponent(componentId, name);					
				};
				
				var actionChangeOwner = function(record) {
					Ext.getCmp('changeOwnerWin').show();
					Ext.getCmp('changeOwnerWin').componentId = record.get('componentId');
					Ext.getCmp('changeOwnerWin').setTitle('Change Owner - ' + record.get('name'));
					Ext.getCmp('changeOwnerWin').getComponent('changeOwnerForm').loadRecord(record);
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
				
				
				var actionAddEditComponent = function(record) {
					mainAddEditWin.show();		
					
					Ext.getCmp('componentTypeMainCB').suspendEvent('change');
						
					generalForm.reset();
					generalForm.componentRecord = record;
					Ext.getCmp('mainFormTabPanel').setActiveTab(generalForm);
					
					if (record) {			
						mainAddEditWin.setTitle('Entry Form: ' + record.get('name'));
						checkFormTabs(record);
						generalForm.loadRecord(record);
						handleAttributes(record.get('componentType'));
						Ext.getCmp('integrationBtn').setDisabled(false);
						Ext.defer(function(){
							generalForm.getForm().findField('description').setValue(record.get('description'));
						}, 100);
					} else {								
						mainAddEditWin.setTitle('Entry Form:  NEW ENTRY');						
						hideSubComponentTabs();
						var requiredStore = Ext.data.StoreManager.lookup('requiredAttributeStore');
						requiredStore.removeAll();
												
						Ext.getCmp('componentGrid').getSelectionModel().deselectAll(); 
						Ext.getCmp('integrationBtn').setDisabled(true);
						
					}
					Ext.getCmp('componentTypeMainCB').resumeEvent('change');
				};
				
				var hideSubComponentTabs = function(attempt){
					if (!attempt) {
						attempt = 1;
					}
					
					if (attempt > 3) {
						mainAddEditWin.hide();
						Ext.Msg.alert('Error', 'Form is unstable please refresh page.');
						return;
					}
					
					//There seems to bug aria stuff that error after resource or media changed
					//second time seems to work
					try {						
						attributeGrid.setDisabled(true);					
						relationshipsGrid.setDisabled(true);
						contactGrid.setDisabled(true);
						resourcesGrid.setDisabled(true);
						mediaGrid.setDisabled(true);
						dependenciesGrid.setDisabled(true);
						metadataGrid.setDisabled(true);
						evaluationGrid.setDisabled(true);
						reviewGrid.setDisabled(true);
						questionContainer.setDisabled(true);
						tagGrid.setDisabled(true);
					} catch (e) {
						attempt++;
						hideSubComponentTabs(attempt);
					}
				};
								
				var checkFormTabs = function(record, componentType) {
					hideSubComponentTabs();
					if (record) {
						var showSubTab = function(grid, url, container){							
							grid.setDisabled(false);
							grid.componentRecord = record;
							if (url) {
								grid.getStore().load({
									url: url
								});
							}
							if (container) {
								container.setDisabled(false);
							}
						};
						
						if (!componentType) {
							componentType = record.get('componentType');
						}

						Ext.Ajax.request({
							url: '../api/v1/resource/componenttypes/' + componentType,
							success: function(response, opts) {
								var data = Ext.decode(response.responseText);

								if (data.dataEntryAttributes) {	
									showSubTab(attributeGrid);  //, '../api/v1/resource/components/' + record.get('componentId')+ '/attributes/view');
								}
								if (data.dataEntryRelationships) {	
									showSubTab(relationshipsGrid, '../api/v1/resource/components/' + record.get('componentId')+ '/relationships');
									Ext.getCmp('relationshipTargetCB').getStore().load();
								}
								if (data.dataEntryContacts) {	
									showSubTab(contactGrid, '../api/v1/resource/components/' + record.get('componentId')+ '/contacts/view');
								}
								if (data.dataEntryResources) {	
									showSubTab(resourcesGrid, '../api/v1/resource/components/' + record.get('componentId') + '/resources/view');
									resourcesGrid.down('form').getComponent('upload').disabled = true;
									resourcesGrid.down('form').getComponent('upload').enable();
								}
								if (data.dataEntryMedia) {	
									showSubTab(mediaGrid, '../api/v1/resource/components/' + record.get('componentId')+ '/media/view');
									
									//Fix: bug with disabled file fields
									mediaGrid.down('form').getComponent('upload').disabled = true;
									mediaGrid.down('form').getComponent('upload').enable();
								}							
								if (data.dataEntryDependancies) {	
									showSubTab(dependenciesGrid, '../api/v1/resource/components/' + record.get('componentId')+ '/dependencies/view');
								}
								if (data.dataEntryMetadata) {	
									showSubTab(metadataGrid, '../api/v1/resource/components/' + record.get('componentId')+ '/metadata/view');
								}
								if (data.dataEntryEvaluationInformation) {	
									showSubTab(evaluationGrid); 
									loadEvalationData(record.get('componentId'));
								}							
								if (data.dataEntryReviews) {	
									showSubTab(reviewGrid, '../api/v1/resource/components/' + record.get('componentId')+ '/reviews/view');
								}
								if (data.dataEntryQuestions) {	
									showSubTab(questionGrid, '../api/v1/resource/components/' + record.get('componentId')+ '/questions/view', questionContainer);
								}	

								//tags should be on all entries
								showSubTab(tagGrid, '../api/v1/resource/components/' + record.get('componentId')+ '/tagsview');
							}						
						});	
					}
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
					previewContents.load('view.jsp?fullPage=true&hideSecurityBanner=true&id=' + id);
					previewCheckButtons();
				};

			});			
			
		</script>

	</stripes:layout-component>
</stripes:layout-render>
