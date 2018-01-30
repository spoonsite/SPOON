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
/* global Ext, CoreUtil, CoreService */

/* Author: cyearsley */

Ext.define('OSF.customSubmission.Grid', {
	extend: 'Ext.grid.Panel',
	formPanel: undefined,
	title: '&nbsp',
	forceFit: true,
	frame: true,
	store: Ext.create('Ext.data.Store'),
	initComponent: function () {

		this.callParent();

		var storeFields = [];
		var gridColumns = [];
		Ext.Array.forEach(this.formPanel.items.items, function (field) {

			// add items as a field in the store
			storeFields.push(field.name);

			// add a column to the grid for each field
			gridColumns.push({
				dataIndex: field.name, 
				text: (field.name.charAt(0).toUpperCase() + field.name.slice(1)).match(/[A-Z][a-z]+/g).join(' ') // Capitalize then split on capital letter (pretty-ify col name)
			});
		});

		this.getStore().setFields(storeFields);
		this.setColumns(gridColumns);

	},

	// return the records in a meaningful way
	getValue: function () {

		var columnDataIndices = [];
		Ext.Array.forEach(this.getColumns(), function (el) {
			columnDataIndices.push(el.dataIndex);
		});

		var storeRecords = [];
		Ext.Array.forEach(this.store.getData().items, function (el) {
			storeRecords.push(el.data);
		});

		var primedRecords = [];
		Ext.Array.forEach(storeRecords, function (sRec) {

			var primedRecord = {};
			Ext.Array.forEach(columnDataIndices, function (dataIndex) {

				primedRecord[dataIndex] = sRec[dataIndex];
			});
			primedRecords.push(primedRecord);
		});

		return primedRecords;
	},
	listeners: {
		itemclick: function (self, record) {

			var deleteButton = this.query('[itemId=deleteBtn]')[0];
			var editButton = this.query('[itemId=editBtn]')[0];

			editButton.setDisabled(false);
			deleteButton.setDisabled(false);
		}
	},
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'bottom',
			items: [
				{
					text: 'Add',
					iconCls: 'fa fa-lg fa-plus icon-button-color-save',
					handler: function () {

						var grid = this.up('grid');
						var newWindow = Ext.create('OSF.customSubmission.GridWindow', {
							title: 'Add Item to ' + grid.title,
							items: [grid.formPanel],
							gridReference: grid
						}).show();
					}
				},
				{
					text: 'Edit',
					iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
					disabled: true,
					itemId: 'editBtn',
					handler: function () {

						var grid = this.up('grid');
						var newWindow = Ext.create('OSF.customSubmission.GridWindow', {
							title: 'Edit ' + grid.title + ' Item',
							items: [grid.formPanel],
							gridReference: grid,
							inEdit: true
						}).show();
					}
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Delete',
					iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
					disabled: true,
					itemId: 'deleteBtn',
					handler: function () {

						var grid = this.up('grid');
						grid.store.remove(grid.getSelection()[0]);
					}
				}
			]
		}
	]
});

Ext.define('OSF.customSubmission.GridWindow', {
	extend: 'Ext.window.Window',
	inEdit: false,
	minWidth: 700,
	minHeight: 400,
	padding: 10,
	closeAction: 'destory',
	modal: true,
	alwaysOnTop: true,
	scrollable: true,
	inEdit: false,
	gridReference: null,
	initComponent: function () {
		this.callParent();

		if (this.inEdit) {
			this.gridReference.formPanel.loadRecord(this.gridReference.getSelection()[0]);
		}
	},
	listeners: {
		close: function () {
			
			this.gridReference.formPanel.reset();
		}
	},
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'bottom',
			items: [
				{
					text: 'Save',
					iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
					handler: function () {

						var self = this.up('window');
						var form = self.query('form')[0];
						var newRecord = {};
						Ext.Array.forEach(form.items.items, function (el) {
							newRecord[el.name] = el.getValue();
						});

						// remove currently selected item
						if (self.inEdit) {
							self.gridReference.store.remove(self.gridReference.getSelection()[0]);
						}

						self.gridReference.setSelection();
						self.gridReference.query('[itemId=deleteBtn]')[0].setDisabled(true);
						self.gridReference.query('[itemId=editBtn]')[0].setDisabled(true);
						self.gridReference.getStore().add(newRecord);
						form.reset();
						self.close();
					}
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Cancel',
					iconCls: 'fa fa-lg fa-close icon-button-color-warning',
					handler: function () {

						var self = this.up('window');
						var form = self.query('form')[0];
						form.reset();
						self.close();
					}
				}
			]
		}
	]
})
