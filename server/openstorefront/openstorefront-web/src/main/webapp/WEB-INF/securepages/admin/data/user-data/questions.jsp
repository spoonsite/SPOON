
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../../client/layout/adminlayout.jsp">
	<stripes:layout-component name="contents">

		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function () {
				var componentStore = Ext.create('Ext.data.Store', {
					// This store only grabs components which have Questions.
					storeId: 'componentStore',
					autoLoad: true,
					sorters: 'componentName',
					fields: [
						'componentId',
						'componentName'
					],
					proxy: {
						id: 'componentStoreProxy',
						type: 'ajax',
						url: '../api/v1/resource/components/questionviews'
					},
					listeners: {
						load: function (theStore) {
							// Since the API returns multiple listings,
							// we must remove duplicate entries of components
							theStore.each(function (i) {
								theStore.each(function (j) {
									// check first if i and j exist
									// then if they are different entries
									// and then if they have the same componentId
									if (i && j && i.internalId !== j.internalId && i.data.componentId === j.data.componentId) {
										theStore.remove(j);
									}
								});
							});
						}
					}
				});

				var questionStore = Ext.create('Ext.data.Store', {
					// This store gets modified heavily when actionSelectedComponent() invokes.
					storeId: 'questionStore'
				});

				var answerStore = Ext.create('Ext.data.Store', {
					// This store gets modified heavily when actionSelectedQuestion() invokes.
					storeId: 'answerStore'
				});

				var userTypeStore = Ext.create('Ext.data.Store', {
					storeId: 'userTypeStore',
					autoLoad: true,
					fields: ['code', 'description'],
					proxy: {type: 'ajax', url: '../api/v1/resource/lookuptypes/UserTypeCode/view'}
				});

				var getUserType = function getUserType(code) {
					if (code)
						return userTypeStore.getData().find('code', code).data.description;
					return '';
				};


				var securityTypeStore = Ext.create('Ext.data.Store', {
					storeId: 'securityTypeStore',
					autoLoad: true,
					fields: ['code', 'description'],
					proxy: {type: 'ajax', url: '../api/v1/resource/lookuptypes/SecurityMarkingType/view'}
				});

				var getSecurityType = function getSecurityType(code) {
					if (code)
						return securityTypeStore.getData().find('code', code).data.description;
					return '';
				};

				var componentPanel = Ext.create('Ext.grid.Panel', {
					flex: 2,
					store: componentStore,
					layout: 'fit',
					viewConfig: {
						emptyText: 'No components exist with the selected status.'
					},
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-refresh',
									handler: function () {
										componentPanel.getStore().load();
										componentPanel.getSelectionModel().deselectAll();
										answerStore.setProxy(undefined);
										answerStore.load();
										questionStore.setProxy(undefined);
										questionStore.load();
									}
								}
							]
						}
					],
					columns: [
						{
							text: 'Components',
							dataIndex: 'componentName',
							flex: 1
						}
					],
					listeners: {
						select: function(rowModel, record) {
							actionSelectedComponent(record.data.componentId);
						}
					}
				});

				var questionPanel = Ext.create('Ext.grid.Panel', {
					flex: 3,
					layout: 'fit',
					store: questionStore,
					viewConfig: {
						emptyText: 'Please select a component.',
						deferEmptyText: false
					},
					listeners: {
						select: function(rowModel, record) {
							var componentId = componentPanel.getSelection()[0].data.componentId;
							var questionId = record.data.questionId;
							actionSelectedQuestion(componentId, questionId);
						}
					},
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Deactivate',
									id: 'question-activateButton',
									scale: 'medium',
									disabled: true,
									iconCls: 'fa fa-2x fa-power-off',
									handler: function () {
										var cmpSel = componentPanel.getSelectionModel().hasSelection();
										var qSel = questionPanel.getSelectionModel().hasSelection();
										if (cmpSel && qSel) {
											var componentId = componentPanel.getSelection()[0].data.componentId;
											var questionId = questionPanel.getSelection()[0].data.questionId;
											toggleQuestion(componentId, questionId);
										}
									}
								}
							]
						}
					],
					columns: [
						{
							text: 'Question',
							dataIndex: 'question',
							renderer: function (value, metaData, record) {
								var userType = getUserType(record.data.userTypeCode);
								var html = "<strong>" + value + "</strong>";
								html += '<p style="color: #999; margin-bottom: 0.5em;">';
								html += '<i class="fa fa-user fa-fw"></i> ' + record.data.createUser + " (";
								html += userType + ') &middot; ';
								html += record.data.organization + "";
								html += "</p>";
								return html;
							},
							flex: 5
						},
						{
							text: 'Created',
							dataIndex: 'createDts',
							flex: 1.5,
							xtype: 'datecolumn',
							format: 'm/d/y H:i:s'
						},
						{
							hidden: true,
							text: 'User',
							dataIndex: 'createUser',
							flex: 1
						},
						{
							hidden: true,
							text: 'Organization',
							dataIndex: 'organization',
							flex: 1
						},
						{
							hidden: true,
							text: 'Update Date',
							dataIndex: 'updateDts',
							flex: 2,
							xtype: 'datecolumn',
							format: 'm/d/y H:i:s'
						},
						{
							hidden: true,
							text: 'Security Type',
							dataIndex: 'securityMarkingType',
							flex: 2,
							renderer: getSecurityType()
						}
					]
				});


				var answerPanel = Ext.create('Ext.grid.Panel', {
					flex: 3,
					layout: 'fit',
					store: answerStore,
					viewConfig: {
						emptyText: 'Please select a component.',
						deferEmptyText: false
					},
					listeners: {
						select: function(rowModel, record) {
							var componentId = componentPanel.getSelection()[0].data.componentId;
							var questionId = questionPanel.getSelection()[0].data.questionId;
							var answerId = record.data.answerId;
							actionSelectedAnswer(componentId, questionId, answerId);
						}
					},
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									xtype: 'combobox',
									id: 'answer-activeStatus',
									emptyText: 'Active',
									value: 'A',
									fieldLabel: 'Active Status',
									name: 'answer-activeStatus',
									displayField: 'description',
									valueField: 'code',
									listeners: {
										change: function (filter, newValue, oldValue, opts) {
											answerPanel.getStore().filter('activeStatus', newValue);
											var actButton = Ext.getCmp('answer-activateButton');
											if (newValue === 'A') {
												actButton.setText('Deactivate');
											}
											else {
												actButton.setText('Activate');
											}
											actButton.disable();
										}
									},
									store: Ext.create('Ext.data.Store', {
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
									})
								},
								{
									text: 'Deactivate',
									id: 'answer-activateButton',
									scale: 'medium',
									disabled: true,
									iconCls: 'fa fa-2x fa-power-off',
									handler: function () {
										var cmpSel = componentPanel.getSelectionModel().hasSelection();
										var qSel = questionPanel.getSelectionModel().hasSelection();
										var aSel = answerPanel.getSelectionModel().hasSelection();
										if (cmpSel && qSel && aSel) {
											var componentId = componentPanel.getSelection()[0].data.componentId;
											var questionId = questionPanel.getSelection()[0].data.questionId;
											var answerId = answerPanel.getSelection()[0].data.responseId;
											toggleAnswer(componentId, questionId, answerId);
										}
									}
								}
							]
						}
					],
					columns: [
						{
							text: 'Answer',
							dataIndex: 'response',
							flex: 5,
							renderer: function (value, metaData, record) {
								var userType = getUserType(record.data.userTypeCode);
								var html = value;
								html += '<p style="color: #999; margin-bottom: 0.5em;">';
								html += '<i class="fa fa-user fa-fw"></i> ' + record.data.createUser + " (";
								html += userType + ') &middot; ';
								html += record.data.organization + "";
								html += "</p>";
								return html;
							}
						},
						{
							text: 'Created',
							dataIndex: 'createDts',
							flex: 1.5,
							xtype: 'datecolumn',
							format: 'm/d/y H:i:s'
						},
						{
							hidden: true,
							text: 'User',
							dataIndex: 'createUser',
							flex: 1
						},
						{
							hidden: true,
							text: 'Organization',
							dataIndex: 'organization',
							flex: 1
						},
						{
							hidden: true,
							text: 'Update Date',
							dataIndex: 'updateDts',
							flex: 2,
							xtype: 'datecolumn',
							format: 'm/d/y H:i:s'
						},
						{
							hidden: true,
							text: 'Security Type',
							dataIndex: 'securityMarkingType',
							flex: 2,
							renderer: getSecurityType()
						}
					]
				});


				var mainPanel = Ext.create('Ext.panel.Panel', {
					title: 'Manage Questions <i class="fa fa-question-circle"  data-qtip="User questions and answers about a component."></i>',
					bodyPadding: '6em',
					layout: {
						type: 'hbox',
						align: 'stretch',
						pack: 'start',
						fit: 'fit'
					},
					defaults: {
					},
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									xtype: 'combobox',
									id: 'question-activeStatus',
									emptyText: 'Active questions',
									fieldLabel: 'Show Components with:',
									labelWidth: '250px',
									name: 'question-activeStatus',
									displayField: 'description',
									valueField: 'code',
									listeners: {
										change: function (filter, newValue, oldValue, opts) {
											var qButton = Ext.getCmp('question-activateButton');
											var newUrl = '../api/v1/resource/components/questionviews';
											if (newValue === 'A') {
												qButton.setText('Deactivate');
												newUrl += '?status=A';
											}
											else {
												qButton.setText('Activate');
												newUrl += '?status=I';
											}
											qButton.disable();
											componentPanel.getStore().getProxy().setUrl(newUrl);
											componentPanel.getStore().load();
										}
									},
									store: Ext.create('Ext.data.Store', {
										fields: [
											'code',
											'description'
										],
										data: [
											{
												code: 'A',
												description: 'Active questions'
											},
											{
												code: 'I',
												description: 'Inactive questions'
											}
										]
									})
								}
							]
						}
					],
					items: [
						componentPanel,
						{
							xtype: 'splitter',
							style: 'background-color: transparent;'
						},
						questionPanel,
						{
							xtype: 'splitter',
							style: 'background-color: transparent;'
						},
						answerPanel
					]
				});
				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [
						mainPanel
					]
				});

				var actionSelectedComponent = function actionSelectedComponent(componentId) {
					// Set Proxy and Load Questions
					Ext.getCmp('question-activateButton').disable();
					Ext.getCmp('answer-activateButton').disable();
					questionStore.setProxy({
						id: 'questionStoreProxy',
						url: '/openstorefront/api/v1/resource/components/' + componentId + '/questions',
						type: 'ajax'
					});
					questionStore.load();
					answerStore.setProxy(undefined);
					answerPanel.getView().emptyText = '<div class="x-grid-empty">Please select a question.</div>';
					answerStore.load();
				};


				var actionSelectedQuestion = function actionSelectedQuestion(componentId, questionId) {
					// Set Proxy and Load Answers
					var apiUrl = '/openstorefront/api/v1/resource/components/';
					apiUrl += componentId + '/questions/' + questionId + '/responses';
					answerStore.setProxy({
						id: 'answerStoreProxy',
						url: apiUrl,
						type: 'ajax'
					});
					// Since x-grid-empty is only applied on the intial viewConfig,
					// we must add it to our emptyText if we want proper styling.
					answerPanel.getView().emptyText = '<div class="x-grid-empty">This question has no answers with the selected status.</div>';
					answerStore.load();
					var filterSelection = Ext.getCmp('answer-activeStatus').getValue();
					answerStore.filter('activeStatus', filterSelection);
					Ext.getCmp('question-activateButton').enable();
					Ext.getCmp('answer-activateButton').disable();
				};

				
				var actionSelectedAnswer = function actionSelectedAnswer(componentId, questionId, answerId)  {
					Ext.getCmp('answer-activateButton').enable();
				};


				var toggleQuestion = function toggleQuestion(componentId, questionId) {
					console.log('Toggle Question ' + questionId);
				};

				var toggleAnswer = function toggleAnswer(componentId, questionId, answerId) {
					console.log('Toggle Answer ' + answerId);
				};


			});

		</script>
	</stripes:layout-component>
</stripes:layout-render>