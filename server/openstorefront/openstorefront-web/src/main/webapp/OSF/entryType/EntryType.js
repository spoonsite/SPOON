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
		// Add the "No entry type" item at load time, because inserting
		// into the store after Ext factories it doesn't change it and/or breaks it.
		// Factory pattern FTW?
		load : function(store, records) {
//			console.log("loaded");
//			// Log the first entry for debugging. Then copy the values from the
//			// console into noEntry below and modify them.
//			console.log(store.getAt(1));
			
			var noEntry = {
				code : "NONE",
				description : "(No entry type)",
				id : "extModel56-24",
				updateDts : null
			};
			store.insert(0, noEntry);
		}
	}
});

// Insert a "No entry type" item into the store.
var NETText = 'No entry type';


var leftEntryGridPanel = Ext.create('Ext.grid.Panel', {
	id: 'entryTypeOrigin',
	// TODO: Better title? Or a textbox telling the user how to use this?
	title: 'Entry _ Is',
	columnWidth: .5,
	bodyStyle: 'padding: 10px;margin: 10px; border: solid black 2px',
	
	store: {
		autoLoad: false,
		sorters: [
			new Ext.util.Sorter({
				property: 'qid',
				direction: 'ASC'
			})
		],
		proxy: {
			type: 'ajax',
			url: 'api/v1/resource/components/lookup?componentType=' + 'ALL' /* TODO: Concatenate the component type in the filter here instead of ALL*/,
			reader: {
				type: 'json',
				rootProperty: 'data',
				totalProperty: 'totalNumber'
			}
		}
	},
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
					id: 'leftEntryTypeFilter',
					emptyText: '(No entry type)',
					value: 'NONE',
					fieldLabel: 'Filter by Entry Type',
					name: 'leftEntryTypeFilter',
					typeAhead: false,
					editable: false,
					width: 200,
					listeners: {
						change: function (filter, newValue, oldValue, opts) {
							// TODO: Make the grid show entries, and make this function
							// update its filter correctly.
							
							// Clear any existing filter
							Ext.getCmp('entryTypeOrigin').store.clearFilter(true);
							
							if (newValue !== ('All')) {
								// Apply new filter
								Ext.getCmp('entryTypeOrigin').store.filter('activeStatus', newValue);
							}
							
							// Reload the store to apply the new filter.
							Ext.getCmp('entryTypeOrigin').store.reload();
						}
					},
					emptyOptionAdded: true,
					
					store: entryTypeStore
				})
			]
		}
	]
});

// TODO: Make the right grid panel and use it in the definition below.
var rightEntryGridPanel = null;

Ext.define('OSF.entryType.EntryType', {
	extend: 'Ext.form.Panel',
	alias: 'widget.entryType.EntryType',

	title: 'Entry Type',

	layout: 'column',
	bodyStyle: 'padding: 10px;margin: 10px',

	items: [
		
		leftEntryGridPanel,

		//**************************
		// Target panel
		//***************************

		Ext.create('Ext.grid.Panel', {
			id: 'entryTypeTarget',
			// TODO: Better title? Or a textbox telling the user how to use this?
			title: 'Type Of _',
			columnWidth: .5,
			height: '100%',
			layout: 'fit',
			bodyStyle: 'padding: 10px;margin: 10px;',

			store: {
				autoLoad: false,
				sorters: [
					new Ext.util.Sorter({
						property: 'qid',
						direction: 'ASC'
					})
				],
				proxy: {
					type: 'ajax',
					url: 'api/v1/resource/components/lookup?componentType=' + 'ALL' /* TODO: Concatenate the component type in the filter here instead of ALL*/,
					reader: {
						type: 'json',
						rootProperty: 'data',
						totalProperty: 'totalNumber'
					}
				}
			},
			columnLines: true,

			columns: [
				{text: 'Name', dataIndex: 'description', width: 275, flex: 1},
				{text: 'Type', align: 'center', dataIndex: 'componentType', width: 125},
			],
			listeners: {
				// TODO: These are copypasta from the main Entry Types window.
				// Do we want them in the popup?
				// I don't like being able to edit things in a popup window created by a popup window.
				// It seems messy. - Michael
				//				itemdblclick: function(grid, record, item, index, e, opts){
				//					// actionEditEntry(record);
				//				},
				//				selectionchange: function(grid, record, index, opts){
				//					// checkEntryGridTools();
				//				}
			},
			viewConfig: {
				plugins: {
					ptype: 'gridviewdragdrop',
					dragText: 'Drag and drop to Add to template'
				}
			},

			dockedItems: [
				{
					xtype: 'toolbar',
					dock: 'top',
					items: [
						Ext.create('OSF.component.StandardComboBox', {
							id: 'filterEntryTypeTarget',
							emptyText: '(Show All)',
							value: 'All',
							fieldLabel: 'Filter by Entry Type',
							name: 'componentType',
							typeAhead: false,
							editable: false,
							width: 200,
							listeners: {
								change: function (filter, newValue, oldValue, opts) {
									// Clear any existing filter
									Ext.getCmp('entryTypeTarget').store.clearFilter(true);

									if (newValue !== ('All')) {
										// Apply new filter
										Ext.getCmp('entryTypeTarget').store.filter('componentType', newValue);
									}

									// Reload the store to apply the new filter.
									Ext.getCmp('entryTypeTarget').store.reload();
								}
							},
							store: Ext.create('Ext.data.Store', {
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
								}
							})
						})
					]
				}
			]
		})
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
