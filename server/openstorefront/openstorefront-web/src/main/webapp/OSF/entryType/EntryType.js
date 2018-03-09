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

var actionRefreshOrigin = function() {
	Ext.getCmp('entryTypeOrigin').getStore().load({
		url: 'api/v1/resource/componenttypes'
	});					
};
var actionRefreshTarget = function() {
	Ext.getCmp('entryTypeTarget').getStore().load({
		url: 'api/v1/resource/componenttypes'
	});
};

//
// Creates a combobox that filters an entry type panel's grid based on active or inactive status
// owningPanel should be the panel that contains the grid to be filtered when the combobox is changed.
//
function createEntryTypeFilterComboBox(owningPanel) {
	// Make the combobox
	var cb = Ext.create('OSF.component.StandardComboBox', {
		id: 'filterActiveStatus',									
		emptyText: 'Active',
		value: 'A',
		fieldLabel: 'Entry Type',
		name: 'activeStatus',									
		typeAhead: false,
		editable: false,
		width: 200,							
		listeners: {
			change: function(filter, newValue, oldValue, opts){
				console.log(owningPanel);
			}
		},
		storeConfig: {
			customStore: {
				fields: [
					'code',
					'description'
				],
				data: [												
					{
						code: 'A',
						description: 'Active'
					},
					{
						code: 'I',
						description: 'Inactive'
					}
				]
			}
		},
		
		setChangeListener: function(){console.log("Can't get scope");}
	});
		
	console.log(cb);
	
	cb.setChangeListener();
	
	// Use JavaScript witchcraft to redefine the method we gave the combobox
	// even though it's already been constructed. Now it can reference itself.
	// It couldn't do that in the Ext.create method body, because it was contained
	// in an anonymous constructor (the curly braces that make a thing with no type),
	// and referencing "this" in anonymous constructor just references the constructor,
	// which doesn't help at all because it's meaningless once Ext.create uses
	// the anonymous constructor to make an identified object.
	var betterFunction = function(f) { cb.listeners.change = f; };
	cb.setChangeListener = betterFunction;
	
	return cb;
}

function makeTargetPanel() {
	
	// Make the combobox in advance so we have a reference to it.
	var cb = createEntryTypeFilterComboBox();
	
	var targetPanel = Ext.create('Ext.grid.Panel', {
		id: 'entryTypeTarget',
		title: 'Type Of _',
		columnWidth: .5,
		bodyStyle: 'padding: 10px;margin: 10px',
		
		//TODO Filter this store by the drop down menu value
		//See contacts.jsp for an example
		
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
				url: 'api/v1/resource/componenttypes',
				extraParams: {
					all: true
				}
			}
		}),
		columnLines: true,
		columns: [						
			{ text: 'Type Code', dataIndex: 'componentType', width: 125 },
			{ text: 'Label', dataIndex: 'label', width: 200 }
		],
		listeners: {
			itemdblclick: function(grid, record, item, index, e, opts){
				// actionEditEntry(record);
			},
			selectionchange: function(grid, record, index, opts){
				// checkEntryGridTools();
			}
		},
		viewConfig: {
			plugins: [
				Ext.create('OSF.plugin.CellToCellDragDrop', {
					id: 'celltocell',
					ddGroup: 'typeof',
					enableDrop: true,
					enableDrag: false,
					onDrop: function onDrop(target, dd, e, dragData) {
						alert('Create Relationship');
						//TODO Create the type-of relationship here
					},
					onEnter: function(target, dd, e, dragData) {
						var originName = dragData.record.data.label; 
						var targetName = target.record.data.label;
						
						var text = originName + ' ';
						text += 'is type of ';
						text += targetName;
						dd.ddel.innerText = text;
					},
					onOut: function(target, dd, e, dragData) {
						var originName = dragData.record.data.label; 
						dd.ddel.innerText = originName;
					}
				})
			]
		},			
		
		// Make an empty array, so the constructor doesn't try to read the length
		// of nothing.
//		dockedItems: [ "Placeholder" ]

			dockedItems: [
			{
				//TODO populate this with the correct values
				xtype: 'toolbar',
				dock: 'top',
				items: [
					cb
				]
			}]
	});
	
	cb = targetPanel.dockedItems.items[0];
	console.log(cb);
	
	console.log("Editing dockedItems");
	// Now that we have a pointer to targetPanel for the combobox to use
	// when it changes, replace the placeholder with a combobox that knows
	// about its owning panel.
	alert(cb.listeners);
	//cb.listeners[0] = function(filter, newValue, oldValue, opts){alert("hi");}
	cb.setChangeListener(function(filter, newValue, oldValue, opts)
	{
		alert(filter);
	});
	console.log("Returning targetPanel");
	return targetPanel;
}

Ext.define('OSF.entryType.EntryType', {
	extend: 'Ext.form.Panel',
	alias: 'widget.entryType.EntryType',
	
	title:'Entry Type',
	
	layout: 'column',
	bodyStyle: 'padding: 10px;margin: 10px',
	
	items: [
		
		//**************************
		// Origin panel
		//***************************
		
		Ext.create('Ext.grid.Panel', {
			id: 'entryTypeOrigin',
			title: 'Entry _ Is',
			columnWidth: .5,
			bodyStyle: 'padding: 10px;margin: 10px',
			
			//TODO Filter this store by the drop down menu value
			//See contacts.jsp for an example
			
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
					url: 'api/v1/resource/componenttypes',
					extraParams: {
						all: true
					}
				}
			}),
			columnLines: true,
			columns: [						
				{ text: 'Type Code', dataIndex: 'componentType', width: 125 },
				{ text: 'Label', dataIndex: 'label', width: 200 }
			],
			listeners: {
				itemdblclick: function(grid, record, item, index, e, opts){
					actionEditEntry(record);
				},
				selectionchange: function(grid, record, index, opts){
					checkEntryGridTools();
				}
			},
			viewConfig: {
				plugins: {
					ddGroup: 'typeof',
					ptype: 'celltocelldragdrop',
					enableDrop: false,
					enableDrag: true
				}
			}
		}), 
		
		//**************************
		// Target panel
		//***************************
		
		makeTargetPanel()
	]
});