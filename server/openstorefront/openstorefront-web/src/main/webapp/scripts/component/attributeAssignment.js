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
	closeAction: 'destroy',
	
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
							}),
							listeners: {
								change: function(field, newValue, oldValue, opts) {
									assignPanel.actionRefreshData();
								}
							}
						}
					]
				}
			]
		});
		
		assignPanel.actionPullEntries = function(callback) {
			
			if (!assignPanel.allEntries) {
				layoutPanel.setLoading(true);
				Ext.Ajax.request({
					url: 'api/v1/resource/components/filterable',
					callback: function() {
						layoutPanel.setLoading(false);
					},
					success: function(response, opts) {
						assignPanel.allEntries = Ext.decode(response.responseText).components;
						if (callback) {
							callback();
						}
					}

				});			
			} else {
				if (callback) {
					callback();
				}
			}	
		};
		assignPanel.actionPullEntries();
		
		assignPanel.actionRefreshData = function() {
				
			var attributeType = layoutPanel.queryById('attributeTypeCB').getValue();	
			var attributeCode = layoutPanel.queryById('attributeCodeCB').getValue();	
			layoutPanel.setLoading(true);	
			Ext.Ajax.request({
				url: 'api/v1/resource/componentattributes?attributeType=' + attributeType + '&attributeCode=' + attributeCode,
				callback: function() {
					layoutPanel.setLoading(false);
				},
				success: function(response, opt) {
					var assignedRaw = Ext.decode(response.responseText);
					assignedRaw = assignedRaw.data;
					
					var unassigned = [];
					var assigned = [];
					Ext.Array.each(assignPanel.allEntries, function(entry){
						
						var found = Ext.Array.findBy(assignedRaw, function(item) {
							if (entry.component.componentId === item.componentId) {
								return true;
							}							
						});
						
						if (found) {
							assigned.push(entry);
						} else {
							unassigned.push(entry);
						}
						
					});
					
					assignPanel.unassignedGrid.getStore().loadRawData(unassigned);
					assignPanel.assignedGrid.getStore().loadRawData(assigned);
					
					assignPanel.actionApplyFilter();
				}
			});
			
		};
		
		assignPanel.actionApplyFilter = function() {
			assignPanel.unassignedGrid.getStore().clearFilter();					

			var componentTypes = assignPanel.unassignedGrid.queryById('filterType').getValue();
			var filterName = assignPanel.unassignedGrid.queryById('filterName').getValue();
			var filters = [];
			if (componentTypes && componentTypes.length > 0) {
				
				filters.push({
					property: 'componentType',
					value: componentTypes,
					operator: 'in'
				});					
			}
			
			if (filterName) {
				filters.push({
					property: 'name',
					value: filterName,
					operator: 'like'
				});
			}
			assignPanel.unassignedGrid.getStore().filter(filters);				
			
		};
		
		assignPanel.actionAssign = function() {
			
		};
		
		assignPanel.actionUnassign = function() {
			
		};
		
		
		assignPanel.unassignedGrid = Ext.create('Ext.grid.Panel', {
			width: '50%',
			title: 'Unassigned Entries',
			margin: '0 5 0 0',
			frame: true,
			store: {	
				fields: [
					{ name: 'componentId', mapping: function(data) {
						return data.component.componentId;
					}},					
					{ name: 'name', mapping: function(data) {
						return data.component.name;
					}},
					{ name: 'componentType', mapping: function(data) {
						return data.component.componentType;
					}}
				],
				sorters: [
					new Ext.util.Sorter({
						property: 'name',
						direction: 'ASC'
					})
				]
			},
			columnLines: true,
			selModel: {
				selType: 'checkboxmodel'
			},
			columns: [
				{ text: 'Name', dataIndex: 'name', flex: 1, minWidth: 100 }
			],
			listeners: {
				selectionchange: function(grid, records) {
					if (records.length > 0) {
						assignPanel.unassignedGrid.queryById('assignEntriesBtn').setDisabled(false);
					} else {
						assignPanel.unassignedGrid.queryById('assignEntriesBtn').setDisabled(true);
					}
				}
			},
			dockedItems: [
				{
					xtype: 'panel',
					dock: 'top',
					layout: 'anchor',
					bodyStyle: 'padding: 5px',
					items: [
						{
							xtype: 'tagfield',
							itemId: 'filterType',
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
							listeners: {
								change: function(field, newValue, oldValue) {
									assignPanel.actionApplyFilter();
								}
							}
						},
						{
							xtype: 'textfield',
							itemId: 'filterName',
							name: 'filter',
							fieldLabel: 'Filter Name',
							width: '100%',
							listeners: {
								change: function(field, newValue, oldValue) {
									assignPanel.actionApplyFilter();
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
							itemId: 'assignEntriesBtn',
							iconCls: 'fa fa-lg fa-arrow-right',							
							iconAlign: 'right',
							disabled: true,
							handler: function() {
								var records = assignPanel.unassignedGrid.getSelection();								
								
								var ids = [];
								Ext.Array.each(records, function(record){
									ids.push(record.get('componentId'));
								});
								
								var data = {
									ids: ids
								};								
								
								var attributeType = layoutPanel.queryById('attributeTypeCB').getValue();	
								var attributeCode = layoutPanel.queryById('attributeCodeCB').getValue();
								
								layoutPanel.setLoading('Assigning Entries...');
								Ext.Ajax.request({
									url: 'api/v1/resource/componentattributes/' + attributeType + '/' + attributeCode,
									method: 'POST',
									jsonData: data,
									callback: function() {										
										layoutPanel.setLoading(false);
									},
									success: function(response, opt) {
										Ext.toast('Assigned Attributes Successfully');
										assignPanel.actionRefreshData();										
									}
								});
								
							}
						}
					]					
				}
			]
		});
		
		assignPanel.assignedGrid = Ext.create('Ext.grid.Panel', {
			width: '50%',
			title: 'Assigned Entries',
			frame: true,
			store: {	
				fields: [
					{ name: 'componentId', mapping: function(data) {
						return data.component.componentId;
					}},					
					{ name: 'name', mapping: function(data) {
						return data.component.name;
					}}
				],
				sorters: [
					new Ext.util.Sorter({
						property: 'name',
						direction: 'ASC'
					})
				]
			},
			columnLines: true,
			selModel: {
				selType: 'checkboxmodel'
			},			
			columns: [
				{ text: 'Name', dataIndex: 'name', flex: 1, minWidth: 100 }
			],
			listeners: {
				selectionchange: function(grid, records) {
					if (records.length > 0) {
						assignPanel.assignedGrid.queryById('unassignEntriesBtn').setDisabled(false);
					} else {
						assignPanel.assignedGrid.queryById('unassignEntriesBtn').setDisabled(true);
					}
				}
			},			
			dockedItems: [
				{
					xtype: 'toolbar',
					dock: 'bottom',
					items: [
						{
							text: 'Unassign Entries',		
							iconCls: 'fa fa-lg fa-arrow-left',							
							itemId: 'unassignEntriesBtn',
							disabled: true,
							handler: function() {
								var records = assignPanel.assignedGrid.getSelection();
								
								var ids = [];
								Ext.Array.each(records, function(record){
									ids.push(record.get('componentId'));
								});
								
								var data = {
									ids: ids
								};
								
								
								var attributeType = layoutPanel.queryById('attributeTypeCB').getValue();	
								var attributeCode = layoutPanel.queryById('attributeCodeCB').getValue();
								
								layoutPanel.setLoading('Removing attributes from entries...');
								Ext.Ajax.request({
									url: 'api/v1/resource/componentattributes/' + attributeType + '/' + attributeCode,
									method: 'DELETE',
									jsonData: data,
									callback: function() {										
										layoutPanel.setLoading(false);
									},
									success: function(response, opt) {
										Ext.toast('Unassigned Attributes Successfully');
										assignPanel.actionRefreshData();
									}
								});								
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

