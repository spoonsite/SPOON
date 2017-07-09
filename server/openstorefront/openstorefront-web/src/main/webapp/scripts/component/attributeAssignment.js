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

/*global Ext, CoreService, CoreUtil*/
Ext.define('OSF.component.AttributeAssignment', {
	extend: 'Ext.window.Window',
	alias: 'widget.AttributeAssignment',

	title: 'Manage Assignments',
	iconCls: 'fa fa-lg fa-list-alt icon-small-vertical-correction',
	modal: true,
	width: '60%',
	height: '80%',
	maximizable: true,
	y: '2em',
	layout: 'fit',
	bodyStyle: 'padding: 5px',
	items: [
		
	],
	initComponent: function () {
		this.callParent();
		
		var assignPanel = this;
		
		var layoutPanel = Ext.create('Ext.panel.Panel', {			
			layout: {
				type: 'hbox',
				align: 'stretch'
			},
			dockedItems: [
				{
					xtype: 'panel',
					dock: 'top',
					layout: 'anchor',
					items: [
						{
							xtype: 'combobox',
							itemId: 'attributeTypeCB',
							fieldLabel: 'Attribute Type <span class="field-required" />',
							name: 'attributeType',
							forceSelection: true,	
							queryMode: 'local',
							editable: true,
							typeAhead: true,																	
							valueField: 'attributeType',
							displayField: 'description',
							width: '100%',
							labelWidth: 175,
							store: {
								autoLoad: true,
								proxy: {
									type: 'ajax',
									url: 'api/v1/resource/attributes'									
								}							
							},
							listeners: {
								change: function (field, newValue, oldValue, opts) {
									var panel = field.up('panel');
									panel.getComponent('attributeCodeCB').clearValue();

									var record = field.getSelection();		
									if (record) {
										panel.getComponent('attributeCodeCB').getStore().loadData(record.data.codes);
																																							
									} else {
										panel.getComponent('attributeCodeCB').getStore().removeAll();
									}
								}
							}
						},
						{
							xtype: 'combobox',
							itemId: 'attributeCodeCB',
							fieldLabel: 'Attribute Code <span class="field-required" />',
							name: 'attributeCode',							
							queryMode: 'local',
							editable: false,
							typeAhead: false,																	
							valueField: 'code',
							displayField: 'label',
							labelWidth: 175,
							width: '100%',
							store: Ext.create('Ext.data.Store', {
								fields: [
									"code",
									"label"
								]																							
							})									
						}
					]
				}
			]
		});
		
		assignPanel.unassignedGrid = Ext.create('Ext.grid.Panel', {
			width: '50%',
			title: 'Unassigned Entries',
			margin: '0 5 0 0',
			store: {				
			},
			columnLines: true,
			selModel: {
				selType: 'checkboxmodel'
			},
			columns: [
				{ text: 'Name', dataIndex: 'name', flex: 1, minWidth: 100 }
			],
			dockedItems: [
				{
					xtype: 'panel',
					dock: 'top',
					layout: 'anchor',
					items: [
						{
							xtype: 'combobox',
							valueField: 'code',
							displayField: 'description',
							fieldLabel: 'Entry Type',
							width: '100%',
							store: {
								autoLoad: true,
								proxy: {
									type: 'ajax',
									url: 'api/v1/resource/componenttypes/lookup'
								}
							},
							listerns: {
								change: function(field, newValue, oldValue) {
									
								}
							}
						},
						{
							xtype: 'textfield',
							name: 'filter',
							fieldLabel: 'Filter Name',
							width: '100%',
							listerns: {
								change: function(field, newValue, oldValue) {
									
								}
							}							
						}
					]
				},
				{
					xtype: 'toolbar',
					dock: 'bottom',
					items: [
						{
							text: 'Assign Entries',
							items: 'assignEntriesBtn',
							handle: function() {
								
							}
						}
					]					
				}
			]
		});
		
		assignPanel.assignedGrid = Ext.create('Ext.grid.Panel', {
			width: '50%',
			title: 'Assigned Entries',
			store: {				
			},
			columnLines: true,
			selModel: {
				selType: 'checkboxmodel'
			},			
			columns: [
				{ text: 'Name', dataIndex: 'name', flex: 1, minWidth: 100 }
			],
			dockedItems: [
				{
					xtype: 'toolbar',
					dock: 'bottom',
					items: [
						{
							text: 'Unassign Entries',							
							items: 'assignEntriesBtn',
							handle: function() {
								
							}
						}
					]					
				}
			]			
		});
		layoutPanel.add(assignPanel.unassignedGrid);
		layoutPanel.add(assignPanel.assignedGrid);
		
		
		assignPanel.add(layoutPanel);
	}
	
});

