/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/* global Ext, CoreService */

Ext.define('OSF.component.SavedSearchLinkInsertWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.SavedSearchLinkInsertWindow',
	layout: 'fit',

	title: 'Insert Link to Saved Search',
	modal: true,
	width: '40%',
	height: '50%',

	items: [
		{
			xtype: 'grid',
			id: 'savedSearchGrid',
			columns: [
				{text: 'Name', dataIndex: 'searchName', flex: 2},
				{
					text: 'Last Updated', 
					dataIndex: 'updateDts',
					flex: 1,
					xtype: 'datecolumn',
					format: 'm/d/y H:i:s'
				}
			],
			store: Ext.create('Ext.data.Store', {
				id: 'savedSearchStore',
				autoLoad: true,
				proxy: {
					type: 'ajax',
					url: '/openstorefront/api/v1/resource/usersavedsearches',
					reader: {
						type: 'json',
						rootProperty: 'data',
						totalProperty: 'totalNumber'
					}
				}
			}),
			listeners: {
				selectionchange: function (grid, record, eOpts) {
					if (Ext.getCmp('savedSearchGrid').getSelectionModel().hasSelection()) {
						Ext.getCmp('insertButton').enable();
					}
				}
			}
		}
	],

	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'bottom',
			items: [
				{
					text: 'Cancel'
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Insert Link',
					id: 'insertButton',
					disabled: true,
					handler: function(button) {
						var window = button.up('window');
						var editor = window.editor;
						var record = Ext.getCmp('savedSearchGrid').getSelection()[0];
						var link = '<a href="#">';
						link += record.getData().searchName;
						link +=	'</a>';
						editor.execCommand('mceInsertContent', false, link);
						window.close();
					}
				}
			]
		}
	],
	
	initComponent: function () {
		this.callParent();

		var savedSearchLinkInsertWindow = this;

	}
		

	
});


