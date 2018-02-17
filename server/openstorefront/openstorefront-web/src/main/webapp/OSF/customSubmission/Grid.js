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
	initComponent: function () {

		var csGrid = this;
		csGrid.callParent();
		csGrid.setStore(Ext.create('Ext.data.Store'));

		var storeFields = [];
		var gridColumns = [];
		var dummyFormPanel = Ext.create('OSF.customSubmission.form.' + csGrid.formPanel);
		Ext.Array.forEach(dummyFormPanel.items.items, function (field) {

			var colName = field.colName || field.name;
			
			if (colName) {
				// add items as a field in the store
				storeFields.push(colName);

				// add a column to the grid for each field
				gridColumns.push({
					dataIndex: colName, 
					text: (colName.charAt(0).toUpperCase() + colName.slice(1)).match(/[A-Z][a-z]+/g).join(' ') // Capitalize then split on capital letter (pretty-ify col name)
				});
			}
		});

		csGrid.getStore().setFields(storeFields);
		csGrid.setColumns(gridColumns);

	},

	// return the records in a meaningful way
	getValue: function () {

		var csGrid = this;

		var columnDataIndices = [];
		Ext.Array.forEach(csGrid.getColumns(), function (el) {
			columnDataIndices.push(el.dataIndex);
		});

		var storeRecords = [];
		Ext.Array.forEach(csGrid.store.getData().items, function (el) {
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
						Ext.create('OSF.customSubmission.GridWindow', {
							title: 'Add Item to ' + grid.title,
							formPanel: [Ext.create('OSF.customSubmission.form.' + grid.formPanel)],
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
						Ext.create('OSF.customSubmission.GridWindow', {
							title: 'Edit ' + grid.title + ' Item',
							formPanel: [Ext.create('OSF.customSubmission.form.' + grid.formPanel)],
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

						var deleteButton = grid.query('[itemId=deleteBtn]')[0];
						var editButton = grid.query('[itemId=editBtn]')[0];
						editButton.setDisabled(true);
						deleteButton.setDisabled(true);
					}
				}
			]
		}
	]
});

Ext.define('OSF.customSubmission.GridWindow', {
	extend: 'Ext.window.Window',
	inEdit: false,
	minWidth: 430,
	minHeight: 300,
	maxWidth: 500,
	padding: 10,
	closeAction: 'hide',
	modal: true,
	alwaysOnTop: true,
	scrollable: true,
	gridReference: null,
	formPanel: undefined,
	initComponent: function () {
		
		this.callParent();
		var csGrid = this;

		csGrid.add(csGrid.formPanel);
		if (csGrid.inEdit) {
			csGrid.query('form')[0].loadRecord(csGrid.gridReference.getSelection()[0]);
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

						var csGrid = this.up('window');
						var form = csGrid.query('form')[0];
						var newRecord = {};
						Ext.Array.forEach(form.items.items, function (el) {
							newRecord[el.name] = el.getValue();
						});

						// remove currently selected item
						if (csGrid.inEdit) {
							csGrid.gridReference.store.remove(csGrid.gridReference.getSelection()[0]);
						}

						csGrid.gridReference.setSelection();
						csGrid.gridReference.query('[itemId=deleteBtn]')[0].setDisabled(true);
						csGrid.gridReference.query('[itemId=editBtn]')[0].setDisabled(true);
						csGrid.gridReference.getStore().add(newRecord);
						form.reset();
						csGrid.close();
					}
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Cancel',
					iconCls: 'fa fa-lg fa-close icon-button-color-warning',
					handler: function () {

						var csGrid = this.up('window');
						var form = csGrid.query('form')[0];
						form.reset();
						csGrid.close();
					}
				}
			]
		}
	]
})
