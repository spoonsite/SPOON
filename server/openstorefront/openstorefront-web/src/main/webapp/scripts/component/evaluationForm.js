/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/* global Ext, CoreService, CoreUtil */

Ext.define('OSF.component.EvaluationPanel', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.EvaluationPanel',
	requires: [
		'OSF.form.EvaluationInfo',
		'OSF.form.Attributes',
		'OSF.form.Relationships',
		'OSF.form.Contacts',
		'OSF.form.Resources',
		'OSF.form.Media',
		'OSF.form.Dependencies',		
		'OSF.form.EntrySummary',
		'OSF.form.ChecklistSummary',
		'OSF.form.ChecklistQuestion',
		'OSF.form.ChecklistAll',
		'OSF.form.Section',
		'OSF.form.Review',
		'OSF.form.Tags',
		'OSF.form.ManageEvalQuestions'
	],
	
	layout: 'border',
	initComponent: function () {
		this.callParent();
		
		var evalPanel = this;
		
		evalPanel.navigation = Ext.create('Ext.panel.Panel', {
			title: 'Navigation',
			iconCls: 'fa fa-navicon',
			region: 'west',
			collapsible: true,
			animCollapse: false,
			width: 250,
			minWidth: 250,
			split: true,
			scrollable: true,			
			layout: 'anchor',
			bodyStyle: 'background: white;',
			defaults: {
				width: '100%'
			},			
			items: [
				{
					xype: 'panel',
					itemId: 'evalmenu',
					title: 'Evaluation',
					titleCollapse: true,
					collapsible: true,
					margin: '20 0 0 0',
					bodyStyle: 'padding: 10px;',
					defaultType: 'button',
					defaults: {
						width: '100%',
						cls: 'evaluation-nav-button',							
						overCls: 'evaluation-nav-button-over',
						focusCls: 'evaluation-nav-button',
						margin: '5 0 0 0'
					},
					items: [
						{							
							text: 'Info',							
							handler: function(){
								evalPanel.loadContentForm({
									form: 'EvaluationInfo',
									title: 'Evaluation Info',
									refreshCallback: evalPanel.externalRefreshCallback
								});
							}
						},
						{						
							text: 'Review',														
							handler: function(){
								evalPanel.loadContentForm({
									form: 'Review',
									title: 'Review'
								});
								evalPanel.commentPanel.setHidden(true);
							}
						}						
					]
				},
				{
					xype: 'panel',
					itemId: 'entrymenu',
					title: 'Entry',	
					titleCollapse: true,
					collapsible: true,
					margin: '0 0 0 0',
					bodyStyle: 'padding: 10px;',
					defaultType: 'button',
					defaults: {
						width: '100%',
						cls: 'evaluation-nav-button',							
						overCls: 'evaluation-nav-button-over',
						focusCls: 'evaluation-nav-button',
						margin: '5 0 0 0'
					},
					items: [
						{							
							text: 'Summary',							
							handler: function(){
								evalPanel.loadContentForm({
									form: 'EntrySummary',
									title: 'Entry Summary',
									refreshCallback: evalPanel.externalRefreshCallback
								});								
							}							
						}
					]
				},
				{
					xype: 'panel',
					itemId: 'sectionmenu',
					title: 'Sections',
					collapsible: true,
					bodyStyle: 'padding: 10px;',
					margin: '0 0 0 0',
					defaultType: 'button',
					defaults: {
						width: '100%',
						cls: 'evaluation-nav-button',							
						overCls: 'evaluation-nav-button-over',
						focusCls: 'evaluation-nav-button',
						margin: '5 0 0 0'
					},					
					items: [
					]
				},					
				{
					xype: 'panel',
					itemId: 'checklistmenu',
					title: 'Checklist',
					titleCollapse: true,
					collapsible: true,
					bodyStyle: 'padding: 10px;',
					margin: '0 0 0 0',
					defaultType: 'button',
					defaults: {
						width: '100%',
						cls: 'evaluation-nav-button',							
						overCls: 'evaluation-nav-button-over',
						focusCls: 'evaluation-nav-button',
						margin: '5 0 0 0'
					},					
					items: [																
					]
				}
			]
		});
		
		var changeHistory = Ext.create('OSF.component.ChangeLogWindow', {									
		});
		
		evalPanel.contentPanel = Ext.create('Ext.panel.Panel', {
			region: 'center',			
			layout: 'fit',
			dockedItems: [
				{
					xtype: 'toolbar',
					dock: 'top',
					itemId: 'tools',					
					cls: 'eval-form-title',
					items: [
						{
							xtype: 'tbfill'
						},
						{
							xtype: 'panel',
							itemId: 'title',							
							tpl: '<h1 style="color: white;">{title}</h1>'					
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Change History',
							itemId: 'changeHistoryBtn',
							iconCls: 'fa fa-2x fa-history icon-button-color-default icon-vertical-correction',
							scale: 'medium',
							handler: function() {									
								changeHistory.show();
								
								changeHistory.load({
									entity: 'Component',												
									entityId: evalPanel.componentId,
									includeChildren: true,
									addtionalLoad: function(data, changeWindow) {
										changeWindow.setLoading(true);
										Ext.Ajax.request({
											url: 'api/v1/resource/changelogs/Evaluation/' + evalPanel.evaluationId + '?includeChildren=true',
											callback: function() {
												changeWindow.setLoading(false);
											},
											success: function(response, opts) {
												var extraData = Ext.decode(response.responseText);												
												Ext.Array.push(data, extraData);
												data.sort(function(a, b){
													return Ext.Date.parse(b.createDts, 'C') - Ext.Date.parse(a.createDts, 'C');
												});												
												changeWindow.updateData(data);
											}
										});
									}
								});
							}
						}
					]
				}
			]			
		});
		
		evalPanel.commentPanel = Ext.create('Ext.panel.Panel', {
			title: 'Comments',
			iconCls: 'fa fa-lg fa-comment',
			region: 'east',			
			collapsed: true,
			collapsible: true,
			animCollapse: false,
			floatable: false,
			titleCollapse: true,
			width: 375,
			minWidth: 250,
			split: true,			
			bodyStyle: 'background: white;',
			layout: 'fit',
			items: [
				{
					xtype: 'panel',
					itemId: 'comments',
					bodyStyle: 'padding: 10px;',
					scrollable: true,
					items: [						
					],
					dockedItems: [
						{
							xtype: 'form',
							itemId: 'form',
							dock: 'bottom',
							layout: 'anchor',
							items: [
								{
									xtype: 'hidden',
									name: 'commentId'
								},
								{
									xtype: 'hidden',
									name: 'replyCommentId'
								},
								{
									xtype: 'htmleditor',
									name: 'comment',									
									width: '100%',
									fieldBodyCls: 'form-comp-htmleditor-border',
									maxLength: 4000
								}
							],
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										{
											text: 'Comment',
											iconCls: 'fa fa-lg fa-comment icon-button-color-save',
											handler: function(){
												var form = this.up('form');
												var data = form.getValues();
												data.acknowledge = false;
												
												var method = 'POST';
												var update = '';		
												if (data.commentId) {
													method = 'PUT',
													update = '/' + data.commentId;		
												}
												var evaluationId = evalPanel.commentPanel.lastLoadOpt.evaluationId;
												var entity = evalPanel.commentPanel.lastLoadOpt.entity;
												var entityId = evalPanel.commentPanel.lastLoadOpt.entityId;
												if (!entity) {
													data.entity = 'Evaluation';
													data.entityId = evaluationId;	
												} else {
													data.entity = entity;
													data.entityId = entityId;
												}
												
												CoreUtil.submitForm({
													url: 'api/v1/resource/evaluations/' + evaluationId + '/comments' + update,
													method: method,
													data: data,
													form: form,
													success: function(){
														evalPanel.commentPanel.loadComments(evaluationId, entity, entityId);														
														form.reset();
														
														if (evalPanel.commentPanel.getComponent('comments').replyMessage) {
															evalPanel.commentPanel.getComponent('comments').removeDocked(evalPanel.commentPanel.getComponent('comments').replyMessage, true);
															evalPanel.commentPanel.getComponent('comments').replyMessage = null;
														}
														if (evalPanel.commentPanel.getComponent('comments').editMessage) {
															evalPanel.commentPanel.getComponent('comments').removeDocked(evalPanel.commentPanel.getComponent('comments').editMessage, true);
															evalPanel.commentPanel.getComponent('comments').editMessage = null;
														}														
													}
												});												
												
											}
										},
										{
											xtype: 'tbfill'
										},
										{
											text: 'Cancel',
											itemId: 'cancel',											
											iconCls: 'fa fa-lg fa-close icon-button-color-warning',
											handler: function(){										
												var form = this.up('form');
												form.reset();
												if (evalPanel.commentPanel.getComponent('comments').replyMessage) {
													evalPanel.commentPanel.getComponent('comments').removeDocked(evalPanel.commentPanel.getComponent('comments').replyMessage, true);
													evalPanel.commentPanel.getComponent('comments').replyMessage = null;
												}
												if (evalPanel.commentPanel.getComponent('comments').editMessage) {
													evalPanel.commentPanel.getComponent('comments').removeDocked(evalPanel.commentPanel.getComponent('comments').editMessage, true);
													evalPanel.commentPanel.getComponent('comments').editMessage = null;
												}												
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
		evalPanel.commentPanel.loadComments = function(evaluationId, entity, entityId){
			
			if (evaluationId) {			
				evalPanel.commentPanel.lastLoadOpt = {
					evaluationId: evaluationId,
					entity: entity,
					entityId: entityId
				};
			} else {
				evaluationId = evalPanel.commentPanel.lastLoadOpt.evaluationId;
				entity = evalPanel.commentPanel.lastLoadOpt.entity;
				entityId = evalPanel.commentPanel.lastLoadOpt.entityId;				
			}
			
			evalPanel.commentPanel.getComponent('comments').removeAll(true);
			evalPanel.commentPanel.setLoading(true);
			Ext.Ajax.request({
				url: 'api/v1/resource/evaluations/' + evaluationId + '/comments',
				method: 'GET',
				params: {
					entity: entity,
					entityId: entityId
				},
				callback: function(){
					evalPanel.commentPanel.setLoading(false);
				},
				success: function(response, opts) {
					var data = Ext.decode(response.responseText);
					var comments = [];
					var commentMap = {					
					};
					//build hiearchy
					Ext.Array.each(data, function(comment){
						comment.replies = [];
						if (!comment.replyCommentId) {							
							commentMap[comment.commentId] = comment;							
							comments.push(comment);
						}
					});
					
					var mapReplies = function(replies) {
						var missingBucket = [];
						Ext.Array.each(replies, function(comment){
							if (comment.replyCommentId) {
								var existing = commentMap[comment.replyCommentId];
								if (existing) {
									existing.replies.push(comment);
									commentMap[comment.commentId] = comment;
								} else {
									missingBucket.push(comment);
								}
							}
						});		
						return missingBucket;
					};
					var missingBucket = data;
					do {
						missingBucket = mapReplies(missingBucket);
					} while (missingBucket.length > 0);
										
					
					comments.sort(function(a,b){
						return a.createDts > b.createDts;
					});
					
					var commentPanels = [];
					var createComments = function(comment, parent) {
						var closeable = false;
						var editHidden = true;
						if (evalPanel.user.admin || evalPanel.user.username === comment.createUser) {
							closeable = true;
							editHidden = false;
						}
						var iconCls = '';
						var headerStyle = 'background: olive;';
						if (comment.replyCommentId) {
							iconCls = 'fa fa-reply';
							headerStyle = 'background: darkolivegreen;';
						}
												
						var panel = Ext.create('Ext.panel.Panel', {	
							iconCls: iconCls,
							header: {
								style: headerStyle
							},
							title: 	comment.createUser + ' - ' + 
									Ext.Date.format(Ext.Date.parse(comment.createDts, 'c'), 'm-d-Y H:i:s'),
							listeners: {
								beforeclose: function(panel, opts) {
									if (panel.finishClose) {
										return true;
									} else {
										Ext.Msg.show({
											title:'Delete Comment',
											message: 'Are you sure you want DELETE this comment?',
											buttons: Ext.Msg.YESNO,
											icon: Ext.Msg.QUESTION,
											fn: function(btn) {
												if (btn === 'yes') {
													panel.setLoading('Deleting...');
													Ext.Ajax.request({
														url: 'api/v1/resource/evaluations/' + evaluationId + '/comments/' + comment.commentId,
														method: 'DELETE',
														callback: function() {
															panel.setLoading(false);
														},
														success: function() {
															panel.finishClose = true;
															panel.close();
														}
													});
												} 
											}
										});
									}
									return false;
								},
								afterrender: function(panel) {
									var header = panel.getHeader();
									header.getTools().forEach(function(tool) {
										tool.hide();
									});
									
									header.getEl().on('mouseover', function() {
										header.getTools().forEach(function(tool) {
											if (tool.type === 'gear') {
												if (!editHidden) {
													tool.show();
												}
											} else {
												tool.show();
											}
										});
									}, this);
									header.getEl().on('mouseout', function() {
										header.getTools().forEach(function(tool) {
											tool.hide();
										});
									}, this);
								}
							},							
							tools: [
								{
									type: 'save',									
									tooltip: 'Toggle Acknowledge',
									hidden: true,
									callback: function(panel, tool, event) {																
										panel.setLoading('Updating record...');								
										Ext.Ajax.request({
											url: 'api/v1/resource/evaluations/' + evaluationId + '/comments/' + panel.data.commentId + '/acknowlege',
											method: 'PUT',
											callback: function() {
												panel.setLoading(false);
											},
											success: function(response, opts) {
												evalPanel.commentPanel.loadComments();
											}
										});	
									}
								},
								{
									type: 'prev',									
									tooltip: 'Reply',
									hidden: true,
									callback: function(panel, tool, event) {
										var comment = this.up('panel');

										if (evalPanel.commentPanel.getComponent('comments').replyMessage) {
											evalPanel.commentPanel.getComponent('comments').removeDocked(evalPanel.commentPanel.getComponent('comments').replyMessage, true);
											evalPanel.commentPanel.getComponent('comments').replyMessage = null;
										}

										var replyMessage = Ext.create('Ext.panel.Panel', {
											dock: 'bottom',
											html: 'Replying to ' + comment.getTitle(),
											bodyStyle: 'background: #00d400; color: white; padding-left: 3px;'
										});
										evalPanel.commentPanel.getComponent('comments').addDocked(replyMessage);
										evalPanel.commentPanel.getComponent('comments').replyMessage = replyMessage;
										var form = evalPanel.commentPanel.getComponent('comments').getComponent('form');

										var record = Ext.create('Ext.data.Model', {												
										});
										record.set('replyCommentId', comment.data.commentId);
										form.loadRecord(record);										
									}
								},
								{
									type: 'gear',
									tooltip: 'Edit',
									hidden: true,									
									callback: function(panel, tool, event) {
										var form = evalPanel.commentPanel.getComponent('comments').getComponent('form');

										var record = Ext.create('Ext.data.Model', {												
										});
										record.set(comment);
										form.loadRecord(record);
																				
										if (evalPanel.commentPanel.getComponent('comments').editMessage) {
											evalPanel.commentPanel.getComponent('comments').removeDocked(evalPanel.commentPanel.getComponent('comments').editMessage, true);
											evalPanel.commentPanel.getComponent('comments').editMessage = null;
										}

										var editMessage = Ext.create('Ext.panel.Panel', {
											dock: 'bottom',
											html: 'Editing ' + panel.getTitle(),
											bodyStyle: 'background: #00d400; color: white; padding-left: 3px;'
										});
										evalPanel.commentPanel.getComponent('comments').addDocked(editMessage);
										evalPanel.commentPanel.getComponent('comments').editMessage = editMessage;										
									}
								}
							],
							collapsible: true,
							titleCollapse: true,
							closable: closeable,
							closeToolText: 'Delete',
							margin: '0 0 0 0', 
							bodyStyle: 'padding: 5px;',
							data: comment,
							tpl: new Ext.XTemplate(	
								'<tpl if="acknowledge"><span class="fa fa-lg fa-check text-success" title="acknowledged"></span></tpl>{comment}'
							)
						});	
						
						if (parent) {
							parent.add(panel);
						} else {
							commentPanels.push(panel);
						}
						return panel;
					};
					var processCommentPanel = function (comments, parent) {
						Ext.Array.each(comments, function(comment) {
							var createdPanel = createComments(comment, parent);
							processCommentPanel(comment.replies);
						});
					};
					processCommentPanel(comments);						
					
					evalPanel.commentPanel.getComponent('comments').add(commentPanels);
				}
			});		
		};
		
		evalPanel.add(evalPanel.navigation);
		evalPanel.add(evalPanel.contentPanel);
		evalPanel.add(evalPanel.commentPanel);
		
		CoreService.brandingservice.getCurrentBranding().then(function(branding){			
			evalPanel.branding = branding;
		});
		
		CoreService.userservice.getCurrentUser().then(function(user){
			evalPanel.user = user;	
			
			evalPanel.loadContentForm({
				form: 'EvaluationInfo',
				title: 'Evaluation Info',
				refreshCallback: evalPanel.externalRefreshCallback
			});			
		});
		
	},
	loadEval: function(evaluationId, componentId){
		var evalPanel = this;
		
		evalPanel.setLoading(true);
		evalPanel.evaluationId = evaluationId;
		evalPanel.componentId = componentId;
		
		var entryType = 'COMP';		
		Ext.Ajax.request({
			url: 'api/v1/resource/componenttypes/'+ entryType,
			callback: function() {				
			},
			success: function(response, opts) {
				var entryType = Ext.decode(response.responseText);
				var menuItems = [];
				menuItems.push(
					{							
						text: 'Summary',							
						handler: function(){
							evalPanel.loadContentForm({
								form: 'EntrySummary',
								title: 'Entry Summary',
								refreshCallback: evalPanel.externalRefreshCallback
							});								
						}							
					}					
				);
				if (entryType.dataEntryAttributes){
					menuItems.push({						
						text: 'Attributes',							
						handler: function(){
							evalPanel.loadContentForm({
								form: 'Attributes',
								title: 'Entry Attributes'
							});
						}
					});
				}
				if (entryType.dataEntryRelationships){
					menuItems.push({						
						text: 'Relationships',							
						handler: function(){
							evalPanel.loadContentForm({
								form: 'Relationships',
								title: 'Entry Relationships'
							});
						}
					});					
				}
				if (entryType.dataEntryContacts){
					menuItems.push({						
						text: 'Contacts',							
						handler: function(){
							evalPanel.loadContentForm({
								form: 'Contacts',
								title: 'Entry Contacts'
							});
						}
					});					
				}
				if (entryType.dataEntryResources){
					menuItems.push({						
						text: 'Resources',							
						handler: function(){
							evalPanel.loadContentForm({
								form: 'Resources',
								title: 'Entry Resources'
							});	
						}
					});					
				}
				if (entryType.dataEntryMedia){
					menuItems.push({						
						text: 'Media',							
						handler: function(){
							evalPanel.loadContentForm({
								form: 'Media',
								title: 'Entry Media'
							});
						}
					});						
				}
				if (entryType.dataEntryDependencies){
					menuItems.push({						
						text: 'Dependencies',							
						handler: function(){
							evalPanel.loadContentForm({
								form: 'Dependencies',
								title: 'Entry Dependencies'
							});
						}
					});					
				}
				menuItems.push({						
					text: 'Tags',							
					handler: function(){
						evalPanel.loadContentForm({
							form: 'Tags',
							title: 'Tags'
						});
					}
				});					
				
				evalPanel.navigation.getComponent('entrymenu').removeAll();
				evalPanel.navigation.getComponent('entrymenu').add(menuItems);
				
				Ext.Ajax.request({
					url: 'api/v1/resource/evaluations/' + evaluationId +'/details',
					callback: function() {
						evalPanel.setLoading(false);	
					},
					success: function(response, opt) {
						var evaluationAll = Ext.decode(response.responseText);

						var questions = [];
						
						questions.push({							
							text: 'Summary',							
							handler: function(){
								evalPanel.loadContentForm({
									form: 'ChecklistSummary',
									title: 'Checklist Summary',
									data: evaluationAll.checkListAll
								});
							}							
						});
						
						var allQuestionButtonType = 'button';
						var allQuestionMenu = null;
						if (evaluationAll.evaluation.allowQuestionManagement) {
							allQuestionButtonType = 'splitbutton';
							allQuestionMenu = {
								items: [
									{
										text: 'Manage Questions',
										iconCls: 'fa fa-lg fa-edit icon-small-vertical-correction',
										handler: function() {
											
											var manageWin = Ext.create('OSF.form.ManageEvalQuestions', {
												evaluationAll: evaluationAll,
												successCallback: function() {
													evalPanel.loadEval(evalPanel.evaluationId, evalPanel.componentId);
													allQuestionLoadAction();
												}
											});											
											manageWin.show();
										}
									}
								],
								listeners: {
									beforerender: function () {
									 this.setWidth(this.up('button').getWidth());
									}					
								}								
							};
						}
						
						var allQuestionLoadAction = function() {
							evalPanel.loadContentForm({
								form: 'ChecklistAll',
								title: 'Checklist Questions',
								data: evaluationAll.checkListAll,
								refreshCallback: function(updatedResponse) {
									var newStatusIcon = questionStatusIcon(updatedResponse.workflowStatus);

									var checklistMenu = evalPanel.navigation.getComponent('checklistmenu');
									Ext.Array.each(checklistMenu.items.items, function(item){
										if (item.questionId && updatedResponse.questionId === item.questionId) {
											var itemStatus = item.getComponent('status');
											itemStatus.setText(newStatusIcon);	
											itemStatus.setTooltip(updatedResponse.workflowStatusDescription);
										}
									});
								}									
							});
						};
						
						questions.push({		
							xtype: allQuestionButtonType,
							text: 'All Questions',
							menu: allQuestionMenu,
							handler: function(){
								allQuestionLoadAction();
							}							
						});
						
						var questionStatusIcon = function(workflowStatus) {
							var statusIcon = '';
							if (workflowStatus === 'COMPLETE') {
								statusIcon = '<span class="fa fa-2x fa-check text-success"></span>';
							} else if (workflowStatus === 'INPROGRESS') {
								statusIcon = '<span class="fa fa-2x fa-refresh text-info"></span> ';
							} else if (workflowStatus === 'HOLD') {
								statusIcon = '<span class="fa fa-2x fa-close text-danger"></span> ';
							} else if (workflowStatus === 'WAIT') {
								statusIcon = ' - <span class="fa fa-2x fa-minus text-warning"></span> ';
							}
							return statusIcon;
						};
						
						
						Ext.Array.each(evaluationAll.checkListAll.responses, function(chkresponse) {
														
							var statusIcon = questionStatusIcon(chkresponse.workflowStatus);
							
							var questionHandler = function(btn) {
								evalPanel.loadContentForm({
									form: 'ChecklistQuestion',
									title: 'Checklist Question',
									data: chkresponse,
									refreshCallback: function(updatedResponse) {
										var newStatusIcon = questionStatusIcon(updatedResponse.workflowStatus);
																				
										var checklistMenu = evalPanel.navigation.getComponent('checklistmenu');
										Ext.Array.each(checklistMenu.items.items, function(item){
											if (item.questionId && updatedResponse.questionId === item.questionId) {
												var itemStatus = item.getComponent('status');
												itemStatus.setText(newStatusIcon);
												itemStatus.setTooltip(updatedResponse.workflowStatusDescription);
											}
										});
									}
								});
							};
							
							questions.push({
								xtype: 'segmentedbutton',
								allowMultiple: false,
								allowToggle: false,
								allowDepress: false,
								qid: chkresponse.question.qid,
								questionId: chkresponse.question.questionId,
								items: [
									{
										text: chkresponse.question.qid,
										width: 50,
										tooltip: chkresponse.question.question,
										handler: questionHandler
									},
									{
										text: chkresponse.question.evaluationSectionDescription,
										tooltip: chkresponse.question.evaluationSectionDescription,
										handler: questionHandler
									},
									{
										text: statusIcon,
										itemId: 'status',
										tooltip: chkresponse.workflowStatusDescription,
										cls: 'evaluation-nav-question-status',
										width: 50,
										handler: questionHandler
									}
								]							
							});
						});
						evalPanel.navigation.getComponent('checklistmenu').removeAll();
						evalPanel.navigation.getComponent('checklistmenu').add(questions);
						
						var sections = [];
						Ext.Array.each(evaluationAll.contentSections, function(sectionAll) {
							
							var menu = null;
							var buttonType = 'button';
							if (evaluationAll.evaluation.allowNewSections) {
								
								buttonType = 'splitbutton';
								menu = {
									items: [
										{
											text: 'Delete Section',
											iconCls: 'fa fa-lg fa-trash-o icon-button-color-warning icon-small-vertical-correction-book',
											handler: function(){
												Ext.Msg.show({
													title:'Delete: ' + sectionAll.section.title + '?',													
													message: 'Are you sure you want to remove this section?',
													buttons: Ext.Msg.YESNO,
													icon: Ext.Msg.QUESTION,
													fn: function(btn) {
														if (btn === 'yes') {
															
															evalPanel.setLoading('Deleting Section: ' + sectionAll.section.title);
															Ext.Ajax.request({
																url: 'api/v1/resource/evaluations/' + evalPanel.evaluationId + '/sections/' + sectionAll.section.contentSectionId,
																method: 'DELETE',
																callback: function() {
																	evalPanel.setLoading(false);
																},
																success: function(response, opts) {
																	evalPanel.loadContentForm({
																		form: 'EvaluationInfo',
																		title: 'Evaluation Info'
																	});
																	evalPanel.loadEval(evalPanel.evaluationId, evalPanel.componentId);																		
																}
															});
														}
													}
												});												
											}
										}
									]
								};
							}
							
							sections.push({	
								xtype: buttonType,
								text: sectionAll.section.title,
								menu: menu,
								handler: function(){
									evalPanel.loadContentForm({
										form: 'Section',
										title: sectionAll.section.title,
										data: sectionAll										
									});
								}							
							});							
						});
						
						evalPanel.navigation.getComponent('sectionmenu').removeAll();
						evalPanel.navigation.getComponent('sectionmenu').add(sections);
						
						if (evaluationAll.evaluation.allowNewSections) {
							var dockedTools = evalPanel.navigation.getComponent('sectionmenu').getDockedComponent('tools');
							if (!dockedTools) {							
								evalPanel.navigation.getComponent('sectionmenu').addDocked({
									xtype: 'toolbar',
									itemId: 'tools',
									dock: 'top',
									items: [
										{
											iconCls: 'fa fa-lg fa-plus icon-button-color-save',
											text: 'Add Section',
											handler: function() {

												var sectionWindow = Ext.create('Ext.window.Window', {
													title: 'Add Section',
													modal: true,
													closeAction: 'destroy',
													width: 400,
													height: 175,
													layout: 'fit',
													items: [
														{
															xtype: 'form',
															bodyStyle: 'padding: 10px;',
															items: [
																{
																	xtype: 'combobox',
																	name: 'templateId',
																	fieldLabel: 'Section Template',
																	displayField: 'name',
																	valueField: 'templateId',								
																	emptyText: 'Select',
																	labelAlign: 'top',
																	width: '100%',
																	editable: false,
																	forceSelection: true,
																	allowBlank: false,
																	store: {									
																		autoLoad: true,
																		proxy: {
																			type: 'ajax',
																			url: 'api/v1/resource/contentsectiontemplates'
																		},
																		listeners: {
																			load: function(store, records, opts) {
																				store.filterBy(function(record){
																					var keep = true;
																					Ext.Array.each(evaluationAll.contentSections, function(sectionAll) {
																						if (record.get('templateId') === sectionAll.section.templateId) {
																							keep = false;
																						}
																					});
																					return keep;
																				});
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
																			text: 'Add',
																			iconCls: 'fa fa-lg fa-plus icon-button-color-save',
																			formBind: true,
																			handler: function() {
																				var win = this.up('window');
																				var form = this.up('form');
																				var sectionData = form.getValues();

																				evalPanel.setLoading('Adding Section...');
																				Ext.Ajax.request({
																					url: 'api/v1/resource/evaluations/' + evalPanel.evaluationId + '/sections/' + sectionData.templateId,
																					method: 'POST',
																					callback: function(response, opts) {
																						evalPanel.setLoading(false);
																					},
																					success: function(response, opts) {
																						evalPanel.loadEval(evalPanel.evaluationId, evalPanel.componentId);
																						win.close();
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
																			handler: function() {
																				this.up('window').close();
																			}
																		}																	
																	]
																}
															]
														}
													]
												});
												sectionWindow.show();
											}
										}
									]
								});
							}
						}
						
					}
				});				
				
			}
			
		});
		
	},
	loadContentForm: function(page) {
		var evalPanel = this;
		
		if (evalPanel.currentContentForm && evalPanel.currentContentForm.unsavedChanges) {
			evalPanel.currentContentForm.saveData();
		}
		
		evalPanel.commentPanel.setHidden(false);
		evalPanel.contentPanel.removeAll(true);
		evalPanel.contentPanel.getComponent('tools').getComponent('title').update({
			title: page.title
		});
		
		var hideSecurityMarking = true;
		if (evalPanel.branding) {
			hideSecurityMarking = !evalPanel.branding.allowSecurityMarkingsFlg;
		}
		
		var contentForm = Ext.create('OSF.form.' + page.form, Ext.apply({	
			hideSecurityMarking: hideSecurityMarking
		}, page.options)
		);
		
		evalPanel.contentPanel.add(contentForm);
		evalPanel.currentContentForm = contentForm;

		if (contentForm.loadData) {
			if (page.refreshCallback) {
				evalPanel.refreshCallback = page.refreshCallback;
			}
			
			contentForm.loadData(evalPanel.evaluationId, evalPanel.componentId, page.data, {
				commentPanel: evalPanel.commentPanel,
				user: evalPanel.user,
				mainForm: evalPanel
			});
		}
		
	}
	
	
});

Ext.define('OSF.component.EvaluationFormWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.EvaluationFormWindow',
	title: 'Evaluation Form',
	iconCls: 'fa fa-clipboard',
	width: '85%',
	height: '85%',
	modal: true,
	maximizable: true,
	layout: 'fit',
	listeners: {
		show: function() {        
			this.removeCls("x-unselectable");    
		}
	},	
	initComponent: function () {
		this.callParent();
		
		var evalWin = this;
		
		evalWin.evalPanel = Ext.create('OSF.component.EvaluationPanel', {			
		});
		
		evalWin.add(evalWin.evalPanel);
		
	},
	loadEval: function(evaluationId, componentId, refreshCallback) {
		var evalWin = this;
		
		evalWin.evalPanel.loadEval(evaluationId, componentId);
		if (refreshCallback) {
			evalWin.evalPanel.externalRefreshCallback = refreshCallback;
		}		
		evalWin.evalPanel.evaluationId = evaluationId;
		evalWin.evalPanel.componentId = componentId;
		
	}
	 
});