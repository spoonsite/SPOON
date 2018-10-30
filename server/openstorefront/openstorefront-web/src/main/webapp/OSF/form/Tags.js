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

Ext.define('OSF.form.Tags', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.form.Tags',
	requires: ['OSF.component.TagDropDownWithFamilyPanel'],
	
	requiredPermissions: ['ADMIN-ENTRY-TAG-MANAGEMENT'],
	beforePermissionsCheckFailure: function () { return false; },
	beforePermissionsCheckSuccess: function () { return false; },
	preventDefaultAction: true,
	layout: 'fit',
	initComponent: function () {			
		this.callParent();
		
		var tagPanel = this;		
		
		tagPanel.tagGrid = Ext.create('Ext.grid.Panel', {
			columnLines: true,
			viewConfig: {
				enableTextSelection: true
			},
			store: Ext.create('Ext.data.Store', {
				fields: [			
					{
						name: 'createDts',
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
				{ text: 'Tag', dataIndex: 'text', flex: 1, minWidth: 200 },
				{ text: 'Create User', align: 'center', dataIndex: 'createUser', width: 150 },
				{ text: 'Create Date', dataIndex: 'createDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
				{ text: 'Security Marking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: true },
				{ text: 'Data Sensitivity',  dataIndex: 'dataSensitivity', width: 150, hidden: true }
			],
			listeners: {
				selectionchange: function(grid, record, index, opts){
					var fullgrid = tagPanel.tagGrid;
					if (fullgrid.getSelectionModel().getCount() === 1) {
						fullgrid.down('toolbar').getComponent('removeBtn').setDisabled(false);
					} else {
						fullgrid.down('toolbar').getComponent('removeBtn').setDisabled(true);
					}
				}						
			},
			dockedItems: [
				{
					xtype: 'form',
					layout: 'anchor',
					padding: 10,
					defaults: {
						labelAlign: 'top',
						labelSeparator: ''
					},
					items: [
						{
							xtype: 'panel',
							layout: 'hbox',
							items: [
								{
									xtype: 'familyTagDropPanel',
									itemId: 'thisthing',
									componentId: tagPanel.componentId,
									width: '100%',
									refreshCallBack: function(){
										tagPanel.tagGrid.getStore().reload();
										this.up('form').reset();
									}
								}
							]
						}
					]
				},						
				{
					xtype: 'toolbar',
					items: [							
						{
							text: 'Refresh',
							iconCls: 'fa fa-lg fa-refresh icon-button-color-refresh',
							handler: function(){
								this.up('grid').getStore().reload();
							}
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Delete',
							itemId: 'removeBtn',
							iconCls: 'fa fa-lg fa-trash icon-button-color-warning',									
							disabled: true,
							handler: function(){
								CoreUtil.actionSubComponentToggleStatus(tagPanel.tagGrid, 'tagId', 'tags');
							}
						}
					]
				}
			]
		});		
		tagPanel.add(tagPanel.tagGrid);		
		
	},
	loadData: function(evaluationId, componentId, data, opts, callback) {
		
		var tagPanel = this;
		
		tagPanel.componentId = componentId;
		tagPanel.tagGrid.componentId = componentId;
		tagPanel.queryById('thisthing').componentId = componentId;
		
		tagPanel.tagGrid.getStore().load({
			url: 'api/v1/resource/components/' + componentId + '/tagsview'
		});		
		
		if (callback) {
			callback();
		}
	}
});

