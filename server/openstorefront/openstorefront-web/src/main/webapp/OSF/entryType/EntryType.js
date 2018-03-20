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
		autoLoad: true
	});

	/*
	 * Create a store full of all the entry types.
	 */
	var entryTypeStore = Ext.create('Ext.data.Store', {
		fields: [
			'componentType',
			'updateUser',
			{
				name: 'updateDts',
				type: 'date',
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
			// Add the "All entries" items at load time, because inserting
			// into a store after Ext factories it doesn't change it and/or breaks it.
			// Factory pattern FTW?
			load: function (store, records) {
				//Entry type is a required attribute so no "NONE" field is not necessary
//				var noEntryType = {
//					code: "NONE",
//					description: "(No entry type)",
//					id: "extModel56-999",
//					updateDts: null
//				};
				var allEntries = {
					code: "ALL",
					description: "(Show all)",
					id: "extModel56-9999",
					updateDts: null
				};
//				store.insert(0, noEntryType);
				store.insert(0, allEntries);
			}
		}
	});


	Ext.Define('Ext.grid.Panel', {

		id: 'entryTypeLeftGrid',
		height: "100%",
		flex:1,
		title: title,
		columnWidth: .5,
		margin:5,
		//border: 1,
		store: entryStore,
		columnLines: true,

		columns: [
			{text: 'Name', dataIndex: 'description', flex: 5},
			{text: 'Type', align: 'center', dataIndex: 'componentType', flex: 1}
		],
		viewConfig: {
			plugins: {
				ptype: 'gridviewdragdrop',
				dragText: 'Drag and drop to change entry type'
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
						value: 'ALL',
						fieldLabel: 'Filter by Entry Type',
						name: name + 'EntryTypeFilter',
						typeAhead: false,
						editable: false,
						width: 200,
						listeners: {
							expand: function (e, opts) {
								//Reload to catch newly created types
								entryTypeStore.reload();
							},
							change: function (filter, newValue, oldValue, opts) {

								//console.log("From " + oldValue + " to " + newValue);

								var grid = Ext.getCmp(name);

								// Clear any existing filter
								grid.store.clearFilter(true);

								if (newValue !== ('ALL')) {
									// Apply new filter
									grid.store.filter('componentType', newValue);
								}

								// Reload the store to apply the new filtering.
								grid.store.reload();

							}
						},
						//emptyOptionAdded: true,

						store: entryTypeStore
					})
				]
			}
		],

		listeners: {
			beforedrop: function (node, data, overModel, dropPosition, dropHandlers) {
				// Don't do anything if the drop isn't over a node in the grid
				if (node == null)
					dropHandlers.processDrop();

				var filterBox = Ext.getCmp(name + 'EntryTypeFilter');

				var code = filterBox.getValue();

				//console.log(data.records[0].data.componentType);

				// Don't let the user try to assign 'All' to an entry's type.
				if (code === 'ALL') {
					dropHandlers.cancelDrop();
					Ext.toast("Cannot assign entry type while filter is set to Show All");
				}
				// Cancel the drop if the dragged entry
				// is already of the entry type in the target grid
				else if (data.records[0].data.componentType === code) {
					dropHandlers.cancelDrop();
					Ext.toast("Entry is already assigned to this type")
				}


			},
			drop: function (node, overModel, dropPosisiont) {

				var filterBox = Ext.getCmp(name + 'EntryTypeFilter');

				var code = filterBox.getValue();

				var compID = overModel.records[0].data.code;

				//console.log("Changing " + compID + " to " + code);

				Ext.Ajax.request({
					url: 'api/v1/resource/components/' + compID + "/changeComponentType?newType=" + code,
					method: 'PUT',
					headers: {
						'Accept': 'application/json'
					},
					success: function (response, opts) {
						var obj = Ext.decode(response.responseText);
						//console.dir(obj);
					},

					failure: function (response, opts) {
//						console.log("Failed to change " + compID + " to type " + code);
//						console.log('server-side failure with status code ' + response.status);
					}

				});

				var grid = Ext.getCmp(name);

				grid.store.reload();
			}
		}
	});



// Use the huge function above to make 2 panels: one right, and one left.


//var leftEntryGridPanel = makeEntryGridPanel('entryTypeLeftGrid', 'Entry _ Is');
//var rightEntryGridPanel = makeEntryGridPanel('entryTypeRightGrid', 'Type Of _');
//
//
Ext.define('OSF.entryType.EntryType', {
	extend: 'Ext.form.Panel',
	alias: 'widget.entryType.EntryType',

	title: 'Entry Type',

	layout: 'column',
	bodyStyle: 'padding: 10px;margin: 10px',

	items: [
//		leftEntryGridPanel,
//		rightEntryGridPanel
	]
});
