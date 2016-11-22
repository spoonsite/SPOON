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
/* global Ext, CoreService */

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
		'OSF.form.Metadata',
		'OSF.form.EntrySummary',
		'OSF.form.ChecklistSummary',
		'OSF.form.ChecklistQuestion',
		'OSF.form.ChecklistAll',
		'OSF.form.Section',
		'OSF.form.Review'
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
									title: 'Evaluation Info'
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
									title: 'Entry Summary'
								});						
							}							
						}
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
						{							
							text: 'Summary',							
							handler: function(){
								evalPanel.loadContentForm({
									form: 'ChecklistSummary',
									title: 'Checklist Summary'
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
				}				
				
			]
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
						}
					]
				}
			]			
		});
		
		evalPanel.commentPanel = Ext.create('Ext.panel.Panel', {
			title: 'Comments',
			iconCls: 'fa fa-comment',
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
											iconCls: 'fa fa-comment',
											handler: function(){
												var form = this.up('form');
												var data = form.getValues();
												data.acknowledge = false;
												
												var method = 'POST';
												var update = '';		
												if (data.commentId) {
													method = 'PUT',
													update = '/' + data.commentId 		
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
											iconCls: 'fa fa-close',
											handler: function(){										
												var form = this.up('form');
												form.reset();
												if (evalPanel.commentPanel.getComponent('comments').replyMessage) {
													evalPanel.commentPanel.getComponent('comments').removeDocked(evalPanel.commentPanel.getComponent('comments').replyMessage, true);
													evalPanel.commentPanel.getComponent('comments').replyMessage = null;
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
			
			evalPanel.commentPanel.lastLoadOpt = {
				evaluationId: evaluationId,
				entity: entity,
				entityId: entityId
			};
			
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
										})
									}, this);
									header.getEl().on('mouseout', function() {
										header.getTools().forEach(function(tool) {
											tool.hide();
										})
									}, this);
								}
							},							
							tools: [
								{
									type: 'save',									
									tooltip: 'Toggle Acknowledge',
									hidden: true,
									callback: function(panel, tool, event) {
										
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
							tpl: [
								'{comment}'
							]
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
		
		CoreService.brandingservice.getCurrentBranding().then(function(response, opts){
			var branding = Ext.decode(response.responseText);			
			evalPanel.branding = branding;
		});
		
		CoreService.usersevice.getCurrentUser().then(function(response, opts){
			var user = Ext.decode(response.responseText);
			evalPanel.user = user;	
			
			evalPanel.loadContentForm({
				form: 'EvaluationInfo',
				title: 'Evaluation Info'
			});			
		});
		
	},
	loadEval: function(evaluationId, componentId){
		var evalPanel = this;
		
		evalPanel.setLoading(true);

		
		var entryType = 'COMP';		
		Ext.Ajax.request({
			url: 'api/v1/resource/componenttypes/'+ entryType,
			callback: function() {				
			},
			success: function(response, opts) {
				var entryType = Ext.decode(response.responseText);
				var menuItems = [];
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
				if (entryType.dataEntryMetadata){
					menuItems.push({						
						text: 'Metadata',							
						handler: function(){
							evalPanel.loadContentForm({
								form: 'Metadata',
								title: 'Entry Metadata'
							});
						}
					});						
				}		
				
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
							text: 'All Questions',							
							handler: function(){
								evalPanel.loadContentForm({
									form: 'ChecklistAll',
									title: 'Checklist Questions',
									data: evaluationAll.checkListAll
								});
							}							
						});
						
						Ext.Array.each(evaluationAll.checkListAll.responses, function(chkresponse) {
							questions.push({							
								text: chkresponse.question.qid,
								tooltip: chkresponse.question.question,
								handler: function(){
									evalPanel.loadContentForm({
										form: 'ChecklistQuestion',
										title: 'Checklist Question',
										data: chkresponse
									});
								}							
							});
						});	
						evalPanel.navigation.getComponent('checklistmenu').add(questions);
						
						var sections = [];
						Ext.Array.each(evaluationAll.contentSections, function(sectionAll) {
							sections.push({							
								text: sectionAll.section.title,
								handler: function(){
									evalPanel.loadContentForm({
										form: 'Section',
										title: sectionAll.section.title,
										data: sectionAll
									});
								}							
							});							
						});
						
						evalPanel.navigation.getComponent('sectionmenu').add(sections);
					}
				});				
				
			}
			
		});
		
	},
	loadContentForm: function(page) {
		var evalPanel = this;
		
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

		if (contentForm.loadData) {
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
	initComponent: function () {
		this.callParent();
		
		var evalWin = this;
		
		evalWin.evalPanel = Ext.create('OSF.component.EvaluationPanel', {			
		});
		
		evalWin.add(evalWin.evalPanel);
		
	},
	loadEval: function(evaluationId, componentId) {
		var evalWin = this;
		
		evalWin.evalPanel.loadEval(evaluationId, componentId);
		evalWin.evalPanel.evaluationId = evaluationId;
		evalWin.evalPanel.componentId = componentId;
		
	}
	 
});