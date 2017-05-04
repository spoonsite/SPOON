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
<stripes:layout-render name="../../../../../layout/toplevelLayout.jsp">
	<stripes:layout-component name="contents">

		<stripes:layout-render name="../../../../../layout/adminheader.jsp">		
		</stripes:layout-render>
		
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
						url: 'api/v1/resource/components/questionviews'
					},
					listeners: {
						load: function (store) {
							// Since the API returns multiple listings,
							// we must remove duplicate entries of components
							var componentIds = {};
							var questionsToRemove = [];
							store.each(function(question){								
								if (componentIds[question.get('componentId')]) {
									questionsToRemove.push(question)
								} else {
									componentIds[question.get('componentId')] = true;
								}
							});
							store.remove(questionsToRemove);
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
					proxy: {type: 'ajax', url: 'api/v1/resource/lookuptypes/UserTypeCode/view'}
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
					proxy: {type: 'ajax', url: 'api/v1/resource/lookuptypes/SecurityMarkingType/view'}
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
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
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
									text: 'Toggle Status',
									id: 'question-activateButton',
									scale: 'medium',
									disabled: true,
									iconCls: 'fa fa-2x fa-power-off icon-button-color-default icon-vertical-correction',
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
								html += '<i class="fa fa-user fa-fw"></i> ' + record.data.username + " (";
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
							dataIndex: 'username',
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
									value: 'A',
									editable: false,
									fieldLabel: 'Active Status',
									name: 'answer-activeStatus',
									displayField: 'description',
									valueField: 'code',
									listeners: {
										change: function (filter, newValue, oldValue, opts) {
											answerPanel.getView().emptyText = '<div class="x-grid-empty">This question has no answers with the selected status.</div>';
											answerPanel.getStore().filter('activeStatus', newValue);
											var actButton = Ext.getCmp('answer-activateButton');
											if (newValue === 'A') {
												actButton.setText('Toggle Status');
											}
											else {
												actButton.setText('Toggle Status');
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
									text: 'Toggle Status',
									id: 'answer-activateButton',
									scale: 'medium',
									disabled: true,
									iconCls: 'fa fa-2x fa-power-off icon-button-color-default icon-vertical-correction',
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
							text: 'Status',
							dataIndex: 'activeStatus',
							flex: 1
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
									value: 'A',
									editable: false,									
									fieldLabel: 'Show Entries with:',
									labelWidth: '250px',
									name: 'question-activeStatus',
									displayField: 'description',
									valueField: 'code',
									listeners: {
										change: function (filter, newValue, oldValue, opts) {
											var qButton = Ext.getCmp('question-activateButton');
											var aButton = Ext.getCmp('answer-activateButton');
											var newUrl = 'api/v1/resource/components/questionviews';
											if (newValue === 'A') {
												qButton.setText('Toggle Status');
												newUrl += '?status=A';
											}
											else {
												qButton.setText('Toggle Status');
												newUrl += '?status=I';
											}
											qButton.disable();
											aButton.disable();
											componentPanel.getStore().getProxy().setUrl(newUrl);
											componentPanel.getStore().load();
											questionPanel.getStore().setProxy(undefined);
											questionPanel.getStore().load();
											answerPanel.getStore().setProxy(undefined);
											answerPanel.getView().emptyText = '<div class="x-grid-empty">Please select a question.</div>';
											answerPanel.getStore().load();

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

				addComponentToMainViewPort(mainPanel);

				var actionSelectedComponent = function actionSelectedComponent(componentId) {
					// Set Proxy and Load Questions
					Ext.getCmp('question-activateButton').disable();
					Ext.getCmp('answer-activateButton').disable();
					var activeStatus = Ext.getCmp('question-activeStatus').getValue();
					questionStore.setProxy({
						id: 'questionStoreProxy',
						url: 'api/v1/resource/components/' + componentId + '/questions/view?status=' + activeStatus,
						type: 'ajax'
					});
					questionStore.load();
					answerStore.setProxy(undefined);
					answerPanel.getView().emptyText = '<div class="x-grid-empty">Please select a question.</div>';
					answerStore.load();
				};


				var actionSelectedQuestion = function actionSelectedQuestion(componentId, questionId) {
					// Set Proxy and Load Answers
					var apiUrl = 'api/v1/resource/components/';
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

					if (filterSelection === 'ALL') {
						answerPanel.getView().emptyText = '<div class="x-grid-empty">This question has no answers.</div>';
					}
					else {
						answerStore.filter('activeStatus', filterSelection);
					}

					Ext.getCmp('question-activateButton').enable();
					Ext.getCmp('answer-activateButton').disable();
				};

				
				var actionSelectedAnswer = function actionSelectedAnswer(componentId, questionId, answerId)  {
					Ext.getCmp('answer-activateButton').enable();
				};


				var toggleQuestion = function toggleQuestion(componentId, questionId) {
					var activeStatus = Ext.getCmp('question-activeStatus').getValue();
					if (activeStatus === 'A') {
						var method = 'DELETE';
						var url = 'api/v1/resource/components/';
						url += componentId + '/questions/' + questionId;
						var what = 'deactivate';
					}
					else {
						var method = 'PUT';
						var url = 'api/v1/resource/components/';
						url += componentId + '/questions/' + questionId + '/activate';
						var what = 'activate';
					}

					Ext.Ajax.request({
							url: url,
							method: method,
							success: function (response, opts) {
								var message = 'Successfully ' + what + 'd question.';
								Ext.toast(message, '', 'tr');
								questionPanel.getStore().load();
								questionPanel.getSelectionModel().deselectAll();
								answerPanel.getStore().setProxy(undefined);
								answerPanel.getView().emptyText = '<div class="x-grid-empty">Please select a question.</div>';
								answerPanel.getStore().load();
								// The component panel should be refreshed
								// because the user may have toggled the last question 
								// for any given component, therefore, it should no 
								// longer be listed in the component list.
								componentPanel.getStore().load();
								Ext.getCmp('question-activateButton').disable();
							},
							failure: function (response, opts) {
								Ext.MessageBox.alert('Failed to' + what,
										"Error: Could not " + what + ' the question.');
							}
						});
				};

				var toggleAnswer = function toggleAnswer(componentId, questionId, answerId) {
					var activeStatus = Ext.getCmp('answer-activeStatus').getValue();
					if (activeStatus === 'A') {
						var method = 'DELETE';
						var url = 'api/v1/resource/components/';
						url += componentId + '/questions/' + questionId;
						url += '/responses/' + answerId;
						var what = 'deactivate';
					}
					else {
						var method = 'PUT';
						var url = 'api/v1/resource/components/';
						url += componentId + '/questions/' + questionId;
						url += '/responses/' + answerId + '/activate';
						var what = 'activate';
					}

					Ext.Ajax.request({
							url: url,
							method: method,
							success: function (response, opts) {
								var message = 'Successfully ' + what + 'd answer.';
								Ext.toast(message, '', 'tr');
								answerPanel.getStore().load();
								Ext.getCmp('answer-activateButton').disable();
							},
							failure: function (response, opts) {
								Ext.MessageBox.alert('Failed to' + what,
										"Error: Could not " + what + ' the answer.');
							}
						});

				};


			});

		</script>
	</stripes:layout-component>
</stripes:layout-render>