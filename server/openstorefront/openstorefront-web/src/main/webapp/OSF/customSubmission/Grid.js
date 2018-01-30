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
				text: (field.name.charAt(0).toUpperCase() + field.name.slice(1)).match(/[A-Z][a-z]+/g).join(' ')
			});
		});

		this.getStore().setFields(storeFields);
		this.setColumns(gridColumns);
	},
	getValue: function () {

		console.log("TODO: get the grid's value (probably all it's rows!)");
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
						var addEditWindow = Ext.create('Ext.window.Window', {
							minWidth: 700,
							minHeight: 400,
							padding: 10,
							closeAction: 'destory',
							modal: true,
							alwaysOnTop: true,
							scrollable: true,
							title: 'Add Item to ' + grid.title,
							items: [grid.formPanel],
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'bottom',
									frame: true,
									items: [
										{
											text: 'Save',
											iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
											handler: function () {

											}
										},
										{
											xtype: 'tbfill'
										},
										{
											text: 'Cancel',
											iconCls: 'fa fa-lg fa-close icon-button-color-warning',
											handler: function () {
												addEditWindow.close();
											}
										}
									]
								}
							]
						}).show();
					}
				},
				{
					text: 'Edit',
					iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
					disabled: true,
					handler: function () {

					}
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Delete',
					iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
					disabled: true,
					handler: function () {

					}
				}
			]
		}
	]
});
