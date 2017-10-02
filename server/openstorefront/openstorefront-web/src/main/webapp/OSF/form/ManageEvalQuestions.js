/* 
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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

/* global Ext, CoreUtil */

Ext.define('OSF.form.ManageEvalQuestions', {
	extend: 'Ext.window.Window',
	alias: 'osf.form.ManageEvalQuestions',
	
	title: 'Manage Questions',
	iconCls: 'fa fa-lg fa-edit',
	modal: true,
	width: '80%',
	height: '80%',
	alwaysOnTop: true,
	maximizable: true,
	closeAction: 'destroy',
		
	layout: 'fit',
	initComponent: function () {			
		this.callParent();
		
		var manageEvalWin = this;
		
		
		var availableGrid = Ext.create('Ext.grid.Panel', {
			title: 'Available Questions - <span class="alert-warning"> drag to add <i class="fa fa-lg fa-arrow-right"></i> </span>',
			width: '50%',
			columnLines: true,
			border: true,
			margin: '0 10 0 0',
			selModel: {
				selType: 'rowmodel',
				mode: 'MULTI'
			},			
			store: {				
			},
			viewConfig: {
				plugins: {
					ptype: 'gridviewdragdrop',
					dragText: 'Drag and drop to Add to Checklist'
				}
			},
			columns: [
				{ text: 'QID', dataIndex: 'qid', align: 'center', width: 125 },
				{ text: 'Section', dataIndex: 'evaluationSection', align: 'center', width: 200,
					renderer: function(value, metadata, record) {
						return record.get('evaluationSectionDescription');
					}
				},
				{ text: 'Tags', dataIndex: 'tags', width: 175, sortable: false, 
					renderer: function(value, meta, record) {
						var viewHtml = '';							
						var tags = record.get('tags');
						Ext.Array.each(tags, function(tag){
							viewHtml += '<span class="alerts-option-items">' + tag.tag + '</span>';
						});							
						return viewHtml;
					}
				},
				{ text: 'Question', dataIndex: 'question',  flex: 1,
					renderer: function(value, metadata, record) {
						return Ext.util.Format.stripTags(value);
					}												
				}				
			],
			dockedItems: [
				{
					xtype: 'tagfield',												
					fieldLabel: 'Filter By Tags',												
					name: 'tags',
					emptyText: 'Select Tags',
					grow: true,
					width: 300,	
					forceSelection: true,
					valueField: 'tag',
					displayField: 'tag',
					createNewOnEnter: true,
					createNewOnBlur: false,
					filterPickList: true,
					queryMode: 'local',
					publishes: 'tag',								
					store: Ext.create('Ext.data.Store', {
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/checklistquestions/tags'
						},
						sorters: [{
							property: 'text',
							direction: 'ASC'
						}]
					}),
					listeners: {
						change: function(filter, newValue, oldValue, opts){
							var grid = filter.up('grid');
							grid.getStore().clearFilter();
							if (newValue && newValue.length > 0) {
								grid.getStore().filterBy(function(record){
									var containsAny = false;
									Ext.Array.each(newValue, function(value){
										if (record.get('tags')) {
											Ext.Array.each(record.get('tags'), function(tag) {
												if (tag.tag === value) {
													containsAny = true;
												}	
											});
										}
									});
									return containsAny;
								});
							}
						}
					}
				}
			]			
		});

		var inChecklistGrid = Ext.create('Ext.grid.Panel', {
			title: 'In Checklist Questions - <span class="alert-warning"><i class="fa fa-lg fa-arrow-left"></i> drag to remove </span>',
			width: '50%',
			columnLines: true,
			border: true,
			selModel: {
			   selType: 'rowmodel',
			   mode: 'MULTI'
			},										
			store: {											
			},
			viewConfig: {
				plugins: {
					ptype: 'gridviewdragdrop',
					dragText: 'Drag and drop to delete from checklist'												
				},
				listeners: {
					drop: function(node, data, overModel, dropPostition, opts){													
					}
				}
			},	
			columns: [
				{ text: 'QID', dataIndex: 'qid', align: 'center', width: 125 },
				{ text: 'Section', dataIndex: 'evaluationSection', align: 'center', width: 200,
					renderer: function(value, metadata, record) {
						return record.get('evaluationSectionDescription');
					}
				},
				{ text: 'Tags', dataIndex: 'tags', width: 175, 
					renderer: function(value, meta, record) {
						var viewHtml = '';							
						var tags = record.get('tags');
						Ext.Array.each(tags, function(tag){
							viewHtml += '<span class="alerts-option-items">' + tag.tag + '</span>';
						});							
						return viewHtml;
					}
				},											
				{ text: 'Question', dataIndex: 'question',  flex: 1,
					renderer: function(value, metadata, record) {
						return Ext.util.Format.stripTags(value);
					}												
				}
			],
			dockedItems: [
				{
					xtype: 'tagfield',												
					fieldLabel: 'Filter By Tags',												
					name: 'tags',
					emptyText: 'Select Tags',
					grow: true,
					width: 300,	
					forceSelection: true,
					valueField: 'tag',
					displayField: 'tag',
					createNewOnEnter: true,
					createNewOnBlur: false,
					filterPickList: true,
					queryMode: 'local',
					publishes: 'tag',								
					store: Ext.create('Ext.data.Store', {
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/checklistquestions/tags'
						},
						sorters: [{
							property: 'text',
							direction: 'ASC'
						}]
					}),
					listeners: {
						change: function(filter, newValue, oldValue, opts){
							var grid = filter.up('grid');
							grid.getStore().clearFilter();
							if (newValue && newValue.length > 0) {
								grid.getStore().filterBy(function(record){
									var containsAny = false;
									Ext.Array.each(newValue, function(value){
										if (record.get('tags')) {
											Ext.Array.each(record.get('tags'), function(tag) {
												if (tag.tag === value) {
													containsAny = true;
												}	
											});
										}
									});
									return containsAny;
								});
							}
						}
					}
				}
			]				
		});
		
		var containerPanel = Ext.create('Ext.panel.Panel', {
			layout: {
				type: 'hbox',									
				align: 'stretch'
			},
			items: [
				availableGrid,
				inChecklistGrid
			],
			dockedItems: [
				{
					xtype: 'toolbar',
					dock: 'bottom',
					items: [
						{
							text: 'Apply Changes',
							iconCls: 'fa fa-2x fa-check icon-button-color-save',
							scale: 'medium',
							handler: function(){
								
								var questionIds = [];
								inChecklistGrid.getStore().each(function(record) {
									questionIds.push(record.get('questionId'));
								});
								
								var evalationId = manageEvalWin.evaluationAll.evaluation.evaluationId;
								var checklistId = manageEvalWin.evaluationAll.checkListAll.evaluationChecklist.checklistId;
								
								//save
								containerPanel.setLoading(true);
								Ext.Ajax.request({
									url: 'api/v1/resource/evaluations/' + evalationId + '/checklist/' + checklistId + '/syncquestions',
									method: 'PUT',
									jsonData: questionIds,
									callback: function() {
										containerPanel.setLoading(false);
									},
									success: function(response, opts) {
										Ext.toast('Updated Checklist Questions');
										
										if (manageEvalWin.successCallback) {
											manageEvalWin.successCallback();
										}
										manageEvalWin.close();
									}
								});
								
							}
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Cancel',
							iconCls: 'fa fa-2x fa-close icon-button-color-warning icon-vertical-correction',
							scale: 'medium',
							handler: function(){
								manageEvalWin.close();
							}							
						}
					]
				}
			]
		});
		manageEvalWin.add(containerPanel);
		
		//load available question
		containerPanel.setLoading(true);
		Ext.Ajax.request({
			url: 'api/v1/resource/checklistquestions',
			callback: function() {
				containerPanel.setLoading(false);
			},
			success: function(response, opts) {
				var questions = Ext.decode(response.responseText);
				
				var checklistResponses = manageEvalWin.evaluationAll.checkListAll.responses;
				var available = [];
				var inChecklist = [];
				Ext.Array.each(questions.data, function(question){
					var exists = false;
					Ext.Array.each(checklistResponses, function(response){
						if (question.questionId === response.questionId) {
							exists = true;
						}
					});
					
					if (exists) {
						inChecklist.push(question);
					} else {
						available.push(question);
					}
				});
				availableGrid.getStore().loadRawData(available);
				inChecklistGrid.getStore().loadRawData(inChecklist);
			}
		});

		
	}
		
});
