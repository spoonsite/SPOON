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

Ext.define('OSF.form.EntryReviews', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.form.EntryReviews',
	
	layout: 'fit',
	initComponent: function () {			
		this.callParent();
		
		var reviewPanel = this;
		
		reviewPanel.reviewGrid = Ext.create('Ext.grid.Panel', {		
			title: 'Reviews',
			tooltip: 'User Reviews',
			columnLines: true,
			bufferedRenderer: false,			
			store: Ext.create('Ext.data.Store', {
				fields: [
					{
						name: 'updateDate',
						type:	'date',
						dateFormat: 'c'
					},							
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
				{ text: 'Security Marking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: true },
				{ text: 'Data Sensitivity',  dataIndex: 'dataSensitivity', width: 150, hidden: true }
			],
			listeners: {
				selectionchange: function(grid, record, index, opts){
					var fullgrid = reviewPanel.reviewGrid;
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
										url: 'api/v1/resource/components/' + reviewPanel.componentId + '/reviews/view',
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
								CoreUtil.actionSubComponentToggleStatus(reviewPanel.reviewGrid, 'reviewId', 'reviews');
							}
						}
					]
				}
			]
		});
		
		reviewPanel.add(reviewPanel.reviewGrid);
	},
	loadData: function(evaluationId, componentId, data, opts) {
		
		var reviewPanel = this;		
		reviewPanel.componentId = componentId;
		
		reviewPanel.reviewGrid.getStore().load({
			url: 'api/v1/resource/components/' + componentId + '/reviews/view'
		});	
		
	}
	
});

