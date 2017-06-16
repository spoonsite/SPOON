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

    Document   : questions
    Created on : Mar 1, 2016, 11:41:25 AM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
		
		<stripes:layout-render name="../../../layout/userheader.jsp">		
		</stripes:layout-render>		
		
	   <script src="scripts/component/questionWindow.js?v=${appVersion}" type="text/javascript"></script>	
		
	   <script type="text/javascript">
			/* global Ext, CoreUtil */

			Ext.onReady(function () {

				var responseGrid = Ext.create('Ext.grid.Panel', {
					title: 'Answers',
					id: 'responseGrid',
					columnLines: true,
					store: {
						sorters: [{
							property: 'componentName',
							direction: 'ASC'
						}],	
						autoLoad: true,
						fields: [
							{
								name: 'answeredDate',
								type:	'date',
								dateFormat: 'c'
							},							
							{
								name: 'createDts',
								type:	'date',
								dateFormat: 'c'
							},
							{
								name: 'updateDts',
								type:	'date',
								dateFormat: 'c'
							}
						],
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/componentquestions/responses/${user}'
						}		
					},
					columns: [
						{ text: 'Entry', dataIndex: 'componentName', width: 275	},
						{ text: 'Answer', dataIndex: 'response', flex: 1, minWidth: 200 },
						{ text: 'Post Date', dataIndex: 'answeredDate', width: 200, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Update Date', dataIndex: 'updateDts', width: 200, xtype: 'datecolumn', format:'m/d/y H:i:s' }
					],
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',
							itemId: 'tools',
							items: [{
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
											responseGrid.getStore().load({
												url: 'api/v1/resource/componentquestions/responses/${user}?status=' + Ext.getCmp('answer-activeStatus').getSelection().getData().code 
											});
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
												code: 'P',
												description: 'Pending'
											}
										]
									})
								},
								{
									text: 'Refresh',
									scale: 'medium',
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										actionRefreshResponses();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'View Entry',
									itemId: 'view',
									scale: 'medium',
									disabled: true,
									width: '140px',
									iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction-view',
									handler: function () {
										actionViewEntry(Ext.getCmp('responseGrid').getSelectionModel().getSelection()[0].get('componentId'));										
									}									
								},								
								{
									text: 'Edit',
									itemId: 'edit',
									scale: 'medium',
									width: '100px',
									disabled: true,
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
									handler: function () {
										actionEditResponse(Ext.getCmp('responseGrid').getSelectionModel().getSelection()[0]);										
									}									
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									itemId: 'delete',
									scale: 'medium',
									disabled: true,
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									handler: function () {
										actionDeleteResponse(Ext.getCmp('responseGrid').getSelectionModel().getSelection()[0]);
									}									
								}								
							]
						}
					],
					listeners: {
						itemdblclick: function(grid, record, item, index, e, opts){
							actionEditResponse(record);
						},
						selectionchange: function(selectionModel, selected, opts){
							var tools = Ext.getCmp('responseGrid').getComponent('tools');
							
							if (selected.length > 0) {	
								tools.getComponent('view').setDisabled(false);
								tools.getComponent('edit').setDisabled(false);
								tools.getComponent('delete').setDisabled(false);
							} else {
								tools.getComponent('view').setDisabled(true);
								tools.getComponent('edit').setDisabled(true);
								tools.getComponent('delete').setDisabled(true);
							}
						}
					}					
				});
				
				var actionRefreshResponses = function() {
					Ext.getCmp('responseGrid').getStore().load();
				};
						
				var responseWindow = Ext.create('OSF.component.ResponseWindow', {
					title: 'Edit Answer',
					iconCls: 'fa fa-lg fa-edit icon-small-vertical-correction',
					postHandler: function(responseWin, response) {
						actionRefreshResponses();
					}
				});						
				var actionEditResponse = function(record) {
					responseWindow.componentId = record.get('componentId');
					responseWindow.show();
					responseWindow.edit(record);
				};
				
				var actionDeleteResponse = function(record) {
					Ext.Msg.show({
						title:'Delete Answer?',
						iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
						message: 'Are you sure you want to delete this Answer?',
						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						fn: function(btn) {
							if (btn === 'yes') {
								Ext.getCmp('responseGrid').setLoading("Deleting...");
								Ext.Ajax.request({
									url: 'api/v1/resource/components/'+record.get('componentId')+'/questions/'+record.get('questionId') + '/responses/' + record.get('responseId'),
									method: 'DELETE',
									callback: function(){
										Ext.getCmp('responseGrid').setLoading(false);
									},
									success: function(){
										actionRefreshResponses();
									}
								});
							} 
						}
					});					
				};				
		
				var questionGrid = Ext.create('Ext.grid.Panel', {
					title: 'Questions',
					id: 'questionGrid',
					columnLines: true,
					store: {
						sorters: [{
							property: 'componentName',
							direction: 'ASC'
						}],	
						autoLoad: true,
						fields: [
							{
								name: 'createDts',
								type:	'date',
								dateFormat: 'c'
							},
							{
								name: 'questionUpdateDts',
								type:	'date',
								dateFormat: 'c'
							}
						],
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/componentquestions/${user}' 
						}		
					},
					columns: [
						{ text: 'Entry', dataIndex: 'componentName', width: 275
						},
						{ text: 'Question', dataIndex: 'question', flex: 1, minWidth: 200 },
						{ text: 'Post Date', dataIndex: 'createDts', width: 200, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Update Date', dataIndex: 'questionUpdateDts', width: 200, xtype: 'datecolumn', format:'m/d/y H:i:s' }
					],
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',
							itemId: 'tools',
							items: [
								{
									xtype: 'combobox',
									id: 'question-activeStatus',									
									value: 'A',
									editable: false,
									fieldLabel: 'Active Status',
									name: 'question-activeStatus',
									displayField: 'description',
									valueField: 'code',
									listeners: {
										change: function (filter, newValue, oldValue, opts) {
											questionGrid.getStore().load({
													url: 'api/v1/resource/componentquestions/${user}?status=' + Ext.getCmp('question-activeStatus').getSelection().getData().code
												});
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
												code: 'P',
												description: 'Pending'
											}
										]
									})
								},
								{
									text: 'Refresh',
									scale: 'medium',
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										actionRefreshQuestions();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'View Entry',
									itemId: 'view',
									scale: 'medium',
									disabled: true,
									width: '140px',
									iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction-view',
									handler: function () {
										actionViewEntry(Ext.getCmp('questionGrid').getSelectionModel().getSelection()[0].get('componentId'));										
									}									
								},								
								{
									text: 'Edit',
									itemId: 'edit',
									scale: 'medium',
									disabled: true,
									width: '100px',
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
									handler: function () {
										actionEditQuestion(Ext.getCmp('questionGrid').getSelectionModel().getSelection()[0]);										
									}									
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									itemId: 'delete',
									scale: 'medium',
									disabled: true,
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									handler: function () {
										actionDeleteQuestion(Ext.getCmp('questionGrid').getSelectionModel().getSelection()[0]);
									}									
								}								
							]
						}
					],
					listeners: {
						itemdblclick: function(grid, record, item, index, e, opts){
							actionEditQuestion(record);
						},
						selectionchange: function(selectionModel, selected, opts){
							var tools = Ext.getCmp('questionGrid').getComponent('tools');
							
							if (selected.length > 0) {	
								tools.getComponent('view').setDisabled(false);
								tools.getComponent('edit').setDisabled(false);
								tools.getComponent('delete').setDisabled(false);
							} else {
								tools.getComponent('view').setDisabled(true);
								tools.getComponent('edit').setDisabled(true);
								tools.getComponent('delete').setDisabled(true);
							}
						}
					}					
				});
				
				var actionViewEntry = function(componentId){
				
					var frame = Ext.create('OSF.ux.IFrame', {							
					});
					
					var entryViewWindow = Ext.create('Ext.window.Window', {
						title: 'View Entry',
						iconCls: 'fa fa-lg fa-eye icon-button-color-view',
						maximizable: true,
						modal: true,
						closeMode: 'destroy',
						width: '70%',
						height: '70%',
						layout: 'fit',
						items: [
							frame
						]
					});					
					entryViewWindow.show();
					frame.load('view.jsp?fullPage=true&id=' + componentId);
				};
				
				var actionRefreshQuestions = function() {
					Ext.getCmp('questionGrid').getStore().load();
				};
						
				var questionWindow = Ext.create('OSF.component.QuestionWindow', {
					title: 'Edit Question',	
					iconCls: 'fa fa-lg fa-edit icon-small-vertical-correction',
					postHandler: function(questionWin, response) {
						actionRefreshQuestions();
					}
				});						
				var actionEditQuestion = function(record) {
					questionWindow.componentId = record.get('componentId');
					questionWindow.show();
					questionWindow.edit(record);
				};
				
				var actionDeleteQuestion = function(record) {
					Ext.Msg.show({
						title:'Delete Question?',
						iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
						message: 'Are you sure you want to delete this question?',
						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						fn: function(btn) {
							if (btn === 'yes') {
								Ext.getCmp('questionGrid').setLoading("Deleting...");
								Ext.Ajax.request({
									url: 'api/v1/resource/components/'+record.get('componentId')+'/questions/'+record.get('questionId'),
									method: 'DELETE',
									callback: function(){
										Ext.getCmp('questionGrid').setLoading(false);
									},
									success: function(){
										actionRefreshQuestions();
									}
								});
							} 
						}
					});					
				};					
				
				
				var mainPanel = Ext.create("Ext.tab.Panel", {
					title: 'Questions/Answers <i class="fa fa-question-circle"  data-qtip="Inspect and manage your questions and answers."></i>',
					items: [
						questionGrid,
						responseGrid
					]
				})
				
				addComponentToMainViewPort(mainPanel);
				
			});
			
		</script>
		
	</stripes:layout-component>
</stripes:layout-render>			
