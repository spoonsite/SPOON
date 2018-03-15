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
Ext.require('OSF.plugin.CellToCellDragDrop');

function makeEntryGridPanel(name, title) {
	/*
	 * Make a store of entries. This needs to be done for each grid panel,
	 * because if grids share a store they'll also share a filter.
	 */
	var entryStore = Ext.create('Ext.data.Store', {
		storeId: name + 'EntryStore',
		fields: [
			// The griddragdrop plugin needs a type in order to operate!
			{
				name: 'name',
				type: 'string'
			}
		],
		sorters: new Ext.util.Sorter({
			property: 'name',
			direction: 'ASC'
		}),
		proxy: {
			id: 'entryStoreProxy',
			type: 'ajax',
			url: 'api/v1/resource/components/lookup'
		},
		autoLoad: true,
		listeners: {
			load(store) {
				console.log(store);
				store.clearFilter(true);
				
				// Get the combobox for the current grid
				var combobox = Ext.getCmp(name + 'EntryTypeFilter');
				console.log(combobox.getRawValue());
			}
		}
	});
	
	/*
	 * Create a store full of all the entry types, which the left combobox and the
	 * right combobox can both use.
	 */
	var entryTypeStore = Ext.create('Ext.data.Store', {
		fields: [
			'componentType',
			'updateUser',							
			{
				name: 'updateDts',
				type:	'date',
				dateFormat: 'c'
			},							
			'activeStatus',
			'label',
			'description',
			'componentTypeTemplate'
		],
		autoLoad: true,
		proxy: {
			type: 'ajax',
			url: 'api/v1/resource/componenttypes/lookup',
			extraParams: {
				all: true
			}
		},
		listeners: {
			// Add the "No entry type" and "All entries" items at load time, because inserting
			// into a store after Ext factories it doesn't change it and/or breaks it.
			// Factory pattern FTW?
			load : function(store, records) {			
				var noEntryType = {
					code : "NONE",
					description : "(No entry type)",
					id : "extModel56-24",
					updateDts : null
				};
				var allEntries = {
					code : "ALL",
					description : "(Show all)",
					id : "extModel56-24",
					updateDts : null
				};
				store.insert(0, [noEntryType, allEntries]);				
			}
		}
	});

	
	return Ext.create('Ext.grid.Panel', {
		id: name,
		// TODO: Better title? Or a textbox telling the user how to use this?
		title: title,
		columnWidth: .5,
		bodyStyle: 'padding: 10px;margin: 10px; border: solid black 2px',
		
		store: entryStore,
		columnLines: true,
		
		columns: [
			{text: 'Name', dataIndex: 'description', width: 275, flex: 1},
			{text: 'Type', align: 'center', dataIndex: 'componentType', width: 125},
		],
		viewConfig: {
			plugins: {
				ptype: 'gridviewdragdrop',
				// TODO: Is this copypasta or intentional?
				dragText: 'Drag and drop to Add to template'
			}
		},
		
		dockedItems: [
			{
				xtype: 'toolbar',
				dock: 'top',
				items: [
					Ext.create('OSF.component.StandardComboBox', {
						id: name + 'EntryTypeFilter',
						emptyText: '(Show all)',
						value: 'NONE',
						fieldLabel: 'Filter by Entry Type',
						name: name + 'EntryTypeFilter',
						typeAhead: false,
						editable: false,
						width: 200,
						listeners: {
							change: function (filter, newValue, oldValue, opts) {
								// TODO: Make the grid's filter persist after the window is closed.
								// The store's filter is getting cleared somewhere, when it shouldn't be.
								console.log("From " + oldValue + " to " + newValue);
								
								// Clear any existing filter
								Ext.getCmp(name).store.clearFilter(true);
								
								if (newValue !== ('ALL')) {
									// Apply new filter
									Ext.getCmp(name).store.filter('componentType', newValue);
								}
								
								// Reload the store to apply the new filter.
								Ext.getCmp(name).store.reload();
							}
						},
						emptyOptionAdded: true,
						
						store: entryTypeStore
					})
				]
			}
		]
	});
}



var leftEntryGridPanel = makeEntryGridPanel('entryTypeLeftGrid', 'Entry _ Is');
var rightEntryGridPanel = makeEntryGridPanel('entryTypeRightGrid', 'Type Of _');


Ext.define('OSF.entryType.EntryType', {
	extend: 'Ext.form.Panel',
	alias: 'widget.entryType.EntryType',

	title: 'Entry Type',

	layout: 'column',
	bodyStyle: 'padding: 10px;margin: 10px',

	items: [		
		leftEntryGridPanel,
		rightEntryGridPanel
	],

	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'bottom',
			items: [
				{
					xtype: 'button',
					text: 'Save',
					formBind: true,
					margin: '0 20 0 0',
					iconCls: 'fa fa-lg fa-save',
					handler: function () {
						// TODO: Make this save the changes and close the window
						alert("This doesn't do anything yet.");
					}
				},
				{
					xtype: 'button',
					text: 'Cancel',
					iconCls: 'fa fa-lg fa-close',
					dock: 'right',
					handler: function () {
						// TODO: Make this cancel changes and close the window
						alert("This doesn't do anything yet.");
					}
				}
			]
		}
	]
});
