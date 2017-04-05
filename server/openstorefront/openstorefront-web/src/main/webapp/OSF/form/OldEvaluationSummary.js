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

Ext.define('OSF.form.OldEvaluationSummary', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.form.OldEvaluationSummary',
	
	layout: 'fit',
	initComponent: function () {			
		this.callParent();
		
		var evalPanel = this;
		
		evalPanel.evaluationGrid = Ext.create('Ext.grid.Panel', {	
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
					url: 'api/v1/resource/lookuptypes/EvaluationSection'
				}
			}),
			columns: [
				{ text: 'Section', dataIndex: 'description', flex: 1, minWidth: 200 },
				{ text: 'Not Available', align: 'center', dataIndex: 'notAvailable', width: 150, 
					xtype: 'checkcolumn',
					listeners: {
						checkchange: function(checkColumn, rowIndex, checked, opt) {
							var record = evalPanel.evaluationGrid.getStore().getAt(rowIndex);
							if (checked) {										
								record.actualScoreField.setDisabled(true);
							} else {
								record.actualScoreField.setDisabled(false);
							}
						}
					}
				},
				{ text: 'Score', dataIndex: 'actualScore', width: 100, align: 'center', 
					xtype: 'widgetcolumn',
					widget: {
						xtype: 'numberfield',
						minValue: 1,
						maxValue: 5,
						step: .5,
						fieldStyle: 'text-align: left;',
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
							return "<i class='fa fa-lg fa-check'></i>";
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
				   iconCls: 'fa fa-lg fa-trash',
				   maxWidth: 25,						   
				   handler: function() {
					 var record = this.getWidgetRecord();
					 var componentId = evalPanel.evaluationGrid.componentRecord.get('componentId');
						evalPanel.evaluationGrid.setLoading('Clearing Section...');
						Ext.Ajax.request({
							url: 'api/v1/resource/components/' + componentId + '/sections/'+ record.get('code'),
							method: 'DELETE',
							callback: function(){
								evalPanel.evaluationGrid.setLoading(false);
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
							iconCls: 'fa fa-lg fa-save icon-button-color-save',
							handler: function(){
								var componentId = evalPanel.componentId;
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
								evalPanel.evaluationGrid.setLoading('Saving...');
								Ext.Ajax.request({
									url: 'api/v1/resource/components/' + componentId + '/sections/all',
									method: 'POST',
									jsonData: sectionsToPost,
									callback: function(){
										evalPanel.evaluationGrid.setLoading(false);
									},
									success: function(response, opts) {
										Ext.toast('Saved Records', 'Success');
										evalPanel.loadEvalationData(componentId);
									}
								});


							}
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Clear All Sections',
							iconCls: 'fa fa-lg fa-trash icon-button-color-warning',									
							handler: function(){			
								Ext.Msg.show({
									title:'Clear All Sections?',
									message: 'Are you sure you would like to clear all sections?',
									buttons: Ext.Msg.YESNO,
									icon: Ext.Msg.QUESTION,
									fn: function(btn) {
										if (btn === 'yes') {
											var componentId = evalPanel.componentId;
											evalPanel.evaluationGrid.setLoading('Clearing All Sections...');
											Ext.Ajax.request({
												url: 'api/v1/resource/components/' + componentId + '/sections',
												method: 'DELETE',
												callback: function(){
													evalPanel.evaluationGrid.setLoading(false);
												},
												success: function(response, opts) {
													Ext.toast('Cleared evaluation records', 'Success');
													evalPanel.loadEvalationData(componentId);
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

		evalPanel.loadEvalationData = function(componentId){
			evalPanel.evaluationGrid.setLoading(true);

			//clear data
			evalPanel.evaluationGrid.getStore().each(function(record){
				record.set('notAvailable', null, { dirty: false });
				record.set('actualScore', null, { dirty: false });
				record.set('existing', null, { dirty: false });
				if (record.actualScoreField)
				{
					record.actualScoreField.setDisabled(false);
				}
			});

			Ext.Ajax.request({
				url: 'api/v1/resource/components/' + componentId + '/sections',
				callback: function(){
					evalPanel.evaluationGrid.setLoading(false);
				},
				success: function(response, opt){
					var data = Ext.decode(response.responseText);

					evalPanel.evaluationGrid.getStore().each(function(record){
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
		
		evalPanel.add(evalPanel.evaluationGrid);
		
	},	
	loadData: function(evaluationId, componentId, data, opts) {
		
		var evalPanel = this;
		
		evalPanel.componentId = componentId;
		evalPanel.loadEvalationData(componentId);
	}	
	
	
});

