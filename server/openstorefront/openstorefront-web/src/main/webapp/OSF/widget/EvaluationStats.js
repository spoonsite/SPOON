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

/* global Ext */

Ext.define('OSF.widget.EvaluationStats', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.EvaluationStats',
	
	layout: 'fit',
	
	initComponent: function () {
		this.callParent();	
		var statPanel = this;
		
		//dock the filters
		statPanel.addDocked(
			{
				xtype: 'toolbar',
				dock: 'top',
				items: [
					{
						xtype: 'combobox',
						itemId: 'filterAssignedUser',
						name: 'assignedUser',
						fieldLabel: 'Assigned User',
						displayField: 'description',
						valueField: 'code',								
						emptyText: 'All',						
						width: 350,
						typeAhead: true,
						editable: true,
						forceSelection: true,
						store: {									
							autoLoad: true,
							proxy: {
								type: 'ajax',
								url: 'api/v1/resource/userprofiles/lookup'
							},
							listeners: {
								load: function(store, records, opts) {
									store.add({
										code: null,
										description: 'All'
									});
								}
							}									
						},
						listeners: {
							change: function(filter, newValue, oldValue, opts){
								statPanel.refresh();
							}
						}										
					},
					{
						xtype: 'combobox',
						itemId: 'filterAssignedGroup',
						name: 'assignedGroup',
						fieldLabel: 'Assigned Group',
						displayField: 'description',
						valueField: 'code',								
						emptyText: 'All',					
						width: 370,	
						labelWidth: 120,
						editable: false,
						forceSelection: true,
						store: {									
							autoLoad: true,
							proxy: {
								type: 'ajax',
								url: 'api/v1/resource/securityroles/lookup'
							},
							listeners: {
								load: function(store, records, opts) {
									store.add({
										code: null,
										description: 'All'
									});
								}
							}
						},
						listeners: {
							change: function(filter, newValue, oldValue, opts){
								statPanel.refresh();
							}
						}									
					}					
				]
			}	
		);
		
		//two graphs horizonal	
		statPanel.publishStore = Ext.create('Ext.data.Store', {			
		});
		
		statPanel.statusStore = Ext.create('Ext.data.Store', {			
		});
				
		var container = Ext.create('Ext.panel.Panel', {
			layout: {
				type: 'hbox',
				align: 'stretch'
			},
			items: [
				{
					xtype: 'polar',
					itemId: 'publishedChart',
					titleAlign: 'center',
					reference: 'chart',
					innerPadding: 40,
					width: '50%',					
					store: statPanel.publishStore,
					//theme: 'default',
					interactions: ['itemhighlight', 'rotatePie3d'],
					legend: {
						type: 'sprite',
						docked: 'bottom'
					},
					series: [
						{
							type: 'pie3d',
							angleField: 'percent',
							donut: 30,
							distortion: 0.6,
							highlight: {
								margin: 40
							},
							label: {
								field: 'label'
							},
							tooltip: {
								trackMouse: true,
								renderer: function (toolTip, record, ctx) {
									 toolTip.setHtml(record.get('label') + ': ' + record.get('count'));
								}
							}
						}
					]					
				},
				{
					xtype: 'cartesian',
					reference: 'chart',
					//theme: 'midnight',
					itemId: 'statusChart',
					width: '50%',
					store: statPanel.statusStore,
					insetPadding: {
						top: 40,
						bottom: 40,
						left: 20,
						right: 40
					},
					interactions: ['itemhighlight'],
					axes: [{
						type: 'numeric',
						position: 'left',
						minimum: 0,
						titleMargin: 20,
						title: {
							text: 'Count'
						}
					}, {
						type: 'category',
						position: 'bottom'
					}],
					animation: Ext.isIE8 ? false : true,
					series: {
						type: 'bar',
						xField: 'statusLabel',
						yField: 'count',
						style: {
							minGapWidth: 20
						},
						highlight: {
							strokeStyle: 'black',
							fillStyle: 'gold'
						},
						label: {
							field: 'count',
							display: 'insideEnd'
						},
						tooltip: {
							trackMouse: true,
							renderer: function (toolTip, record, ctx) {
								 toolTip.setHtml(record.get('statusLabel') + ': ' + record.get('count'));
							}
						}						
					},
					sprites: {
						type: 'text',
						text: 'Unpublished per Status',
						fontSize: 22,
						width: 100,
						height: 30,
						x: 40, 
						y: 20  
					}					
				}
			]
		});
		
		statPanel.add(container);
		statPanel.refresh();
	},	
	
	refresh: function() {
		var statPanel = this;
		
		statPanel.setLoading(true);
		Ext.Ajax.request({
			url: 'api/v1/resource/evaluations/statistics',
			method: 'GET',
			params: {
				assignedUser: statPanel.queryById('filterAssignedUser').getValue(),
				assignedGroup: statPanel.queryById('filterAssignedGroup').getValue()
			},
			callback: function(){
				statPanel.setLoading(false);
			},
			success: function(response, opt){
				var data = Ext.decode(response.responseText);
								
				var totalRecords =  (data.published + data.unpublished);				
								
				var publishData = [
					{
						id: 1,
						label: 'Published',
						count: data.published,
						percent: totalRecords > 0 ?  (data.published / totalRecords) : 0						
					},
					{
						id: 2,
						label: 'Unpublished',
						count: data.unpublished,
						percent: totalRecords > 0 ?  (data.unpublished / totalRecords) : 0						
					}
				];				
				statPanel.publishStore.loadData(publishData);
				
				if (totalRecords === 0) {
					statPanel.queryById('publishedChart').setTitle("No Evaluations Found");
				} else {
					statPanel.queryById('publishedChart').setTitle(totalRecords + " Evaluation(s)");
				}
				
				statPanel.statusStore.loadData(data.statusStats);
			}
		});

	}	
	
});
