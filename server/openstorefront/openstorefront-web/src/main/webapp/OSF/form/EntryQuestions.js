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

Ext.define('OSF.form.EntryQuestions', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.form.EntryQuestions',
	
	layout: 'fit',
		
	
	initComponent: function () {			
		this.callParent();
		
		var questionPanel = this;
		
		questionPanel.questionGrid = Ext.create('Ext.grid.Panel', {		
			width: '100%',
			flex: 1,
			columnLines: true,
			bufferedRenderer: false,
			store: Ext.create('Ext.data.Store', {
				fields: [
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
				{ text: 'Security Marking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: true },
				{ text: 'Data Sensitivity',  dataIndex: 'dataSensitivity', width: 150, hidden: true }
			],
			listeners: {
				selectionchange: function(selectionModel, selectedRecords, opts){
					var fullgrid = questionPanel.questionGrid;
					var responseGrid = questionPanel.questionContainer.getComponent('questionResponseGrid');
					if (fullgrid.getSelectionModel().getCount() === 1) {
						fullgrid.down('toolbar').getComponent('statusToggleBtn').setDisabled(false);
						var componentId = questionPanel.componentId;						
						var questionId = selectedRecords[0].get('questionId');

						responseGrid.getStore().load({
							url: 'api/v1/resource/components/'+componentId+'/questions/'+questionId+'/responses'
						});								
					} else {
						fullgrid.down('toolbar').getComponent('statusToggleBtn').setDisabled(true);
						responseGrid.getStore().removeAll();
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
							store: {
								data: [												
									{ code: 'A', description: 'Active' },
									{ code: 'I', description: 'Inactive' }
								]
							},
							forceSelection: true,
							queryMode: 'local',
							displayField: 'desc',
							valueField: 'code',
							value: 'A',
							listeners: {
								change: function(combo, newValue, oldValue, opts){
									this.up('grid').getStore().load({
										url: 'api/v1/resource/components/' + questionPanel.componentId + '/questions/view',
										params: {
											status: newValue
										}
									});
								}
							}
						}, 
						{
							text: 'Refresh',
							iconCls: 'fa fa-lg fa-refresh icon-button-color-refresh',
							handler: function(){
								this.up('grid').getStore().reload();
							}
						},
						{
							xtype: 'tbseparator'
						},
						{
							text: 'ToggleStatus',
							itemId: 'statusToggleBtn',
							iconCls: 'fa fa-lg fa-power-off icon-button-color-default',
							disabled: true,
							handler: function(){
								CoreUtil.actionSubComponentToggleStatus(questionPanel.questionGrid, 'questionId', 'questions');
							}
						}
					]
				}
			]					
		});

		questionPanel.questionContainer = Ext.create('Ext.panel.Panel', {
			title: 'Question',
			tooltip: 'User questions and answers',
			layout: 'vbox',
			items: [
				questionPanel.questionGrid,
				{
					xtype: 'grid',
					itemId: 'questionResponseGrid',
					title: 'Responses',
					columnLines: true,
					width: '100%',
					flex: 1,
					store: Ext.create('Ext.data.Store', {
						fields: [
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
						{ text: 'Security Marking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: true },
						{ text: 'Data Sensitivity',  dataIndex: 'dataSensitivity', width: 150, hidden: true }
					],
					listeners: {
						selectionchange: function(selectionModel, selectedRecords, opts){
							var fullgrid = questionPanel.questionContainer.getComponent('questionResponseGrid');
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
									iconCls: 'fa fa-lg fa-power-off icon-button-color-default',
									disabled: true,
									handler: function(){
										CoreUtil.actionSubComponentToggleStatus(this.up('grid'), 'questionId', 'questions', 'responseId', 'responses');
									}
								}
							]
						}
					]
				}
			]
		});		
		questionPanel.add(questionPanel.questionContainer);
		
	},
	loadData: function(evaluationId, componentId, data, opts) {
		
		var questionPanel = this;		
		questionPanel.componentId = componentId;
		
		questionPanel.questionGrid.getStore().load({
			url: 'api/v1/resource/components/' + componentId + '/questions/view'
		});		
	
	}	
	
	
});

