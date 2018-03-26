/* 
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
/* global Ext, code */

Ext.define('OSF.tool.EntryTypeEntryAssignment', {
	extend: 'Ext.window.Window',
	alias: 'widget.EntryTypeEntryAssignment',

	title: 'Entry Assignment',
	iconCls: 'fa fa-exchange',
	width: '80%',
	height: '80%',	
	modal: true,
	closeAction: 'destroy',
	bodyStyle: 'padding: 10px;',
	layout: {
		type: 'hbox',
		align: 'stretch'
	},	
	initComponent: function () {	
		this.callParent();
		var entryAssignmentPanel = this;
		
		var leftGrid = {
			xtype: 'grid',
			itemId: 'leftGrid',
			flex: 1,
			margin: '0 20 0 0',
			border: 1,
			store: {	
				proxy: {
					type: 'ajax',
					url: 'api/v1/resource/components/lookup'
				}
			},
			columnLines: true,
			columns: [
				{text: 'Name', dataIndex: 'description', flex: 5}
			],
			selModel: {
				selType: 'rowmodel', 
				mode: 'MULTI'
			},			
			viewConfig: {
				plugins: {
					ptype: 'gridviewdragdrop',
					dragText: 'Drag and drop to change entry type'
				}
			},
			listeners: {
				drop: function(node, data, overModel, dropPosition, eOpts) {			
					if (data.view === entryAssignmentPanel.queryById('leftGrid').getView()) {
						//internal move
						return;
					} else {
						
						if (data.records.length > 0 ) {
							saveChanges(data, entryAssignmentPanel.leftGridEntryType);
						}
					}
				}
			},
			dockedItems: [
				{
					xtype: 'combobox',					
					dock: 'top',
					width: '100%',				
					fieldLabel: 'Entry Type',
					emptyText: 'Select Type',
					typeAhead: false,
					editable: false,
					forceSelection: true,
					queryMode: 'local',
					displayField: 'description',
					valueField: 'code',					
					store: {
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/componenttypes/lookup'
						}
					},
					listeners: {
						change: function (filter, newValue, oldValue, opts) {
							entryAssignmentPanel.leftGridEntryType = newValue;
							if (newValue) {										
								filter.up('grid').getStore().load({
									url: 'api/v1/resource/components/lookup?componentType=' +  encodeURIComponent(newValue)
								});
							}
						}
					}
				},
				{
					xtype: 'textfield',
					width: '100%',
					dock: 'top',
					emptyText: 'Search',
					listeners: {
						change: function (filter, newValue, oldValue, opts) {
							var grid = filter.up('grid');
							grid.getStore().clearFilter();
							if (newValue) {
								grid.getStore().filterBy(function(record){											
									return record.get('description').toLowerCase().includes(newValue.toLowerCase());
								});
							}						
						}
					}
				}
			]
		};
		
		var saveChanges = function(data, newType) {
			
			entryAssignmentPanel.setLoading('Updating records...');
			var multipleIds = {
				ids: []
			};

			Ext.Array.each(data.records, function(record) {
				multipleIds.ids.push(record.get('code'));
			});
			Ext.Ajax.request({
				url: 'api/v1/resource/components/componenttype/' + newType,
				method: 'PUT',
				jsonData: multipleIds,
				callback: function() {
					entryAssignmentPanel.setLoading(false);
				},
				success: function() {
					Ext.toast('Saved Changes'); 
				}
			});	
		};
		
		
		var rightGrid = {
			xtype: 'grid',
			itemId: 'rightGrid',
			width: '50%',
			store: {				
				proxy: {
					type: 'ajax',
					url: 'api/v1/resource/components/lookup'
				}				
			},
			border: 1,
			columnLines: true,
			columns: [
				{text: 'Name', dataIndex: 'description', flex: 5}
			],
			selModel: {
				selType: 'rowmodel', 
				mode: 'MULTI'
			},			
			viewConfig: {
				plugins: {
					ptype: 'gridviewdragdrop',
					dragText: 'Drag and drop to change entry type'
				}
			},
			listeners: {
				drop: function(node, data, overModel, dropPosition, eOpts) {			
					if (data.view === entryAssignmentPanel.queryById('rightGrid').getView()) {
						//internal move
						return;
					} else {
						
						if (data.records.length > 0 ) {
							saveChanges(data, entryAssignmentPanel.leftGridEntryType);
						}
					}
				}
			},			
			dockedItems: [
				{
					xtype: 'combobox',					
					dock: 'top',
					width: '100%',
					fieldLabel: 'Entry Type',
					emptyText: 'Select Type',
					typeAhead: false,
					editable: false,
					forceSelection: true,
					queryMode: 'local',
					displayField: 'description',
					valueField: 'code',	
					store: {
						autoLoad: true,					
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/componenttypes/lookup'
						}
					},
					listeners: {
						change: function (filter, newValue, oldValue, opts) {
							entryAssignmentPanel.rightGridEntryType = newValue;
							filter.up('grid').getStore().load({
								url: 'api/v1/resource/components/lookup?componentType=' +  encodeURIComponent(newValue)
							});						
						}
					}
				},
				{
					xtype: 'textfield',
					width: '100%',
					dock: 'top',
					emptyText: 'Search',
					listeners: {
						change: function (filter, newValue, oldValue, opts) {
							var grid = filter.up('grid');
							grid.getStore().clearFilter();
							if (newValue) {
								grid.getStore().filterBy(function(record){											
									return record.get('description').toLowerCase().includes(newValue.toLowerCase());
								});
							}						
						}
					}	
				}
			]
		};
			
		entryAssignmentPanel.add(leftGrid);
		entryAssignmentPanel.add(rightGrid);
		
	}
	
});

