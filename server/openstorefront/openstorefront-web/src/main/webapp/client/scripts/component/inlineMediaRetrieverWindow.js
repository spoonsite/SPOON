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

Ext.define('OSF.component.InlineMediaRetrieverWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.InlineMediaRetrieverWindow',
	layout: 'fit',

	title: 'Retrieving External Media',
	modal: true,
	width: '40%',
	height: '50%',

	items: [
		{
			xtype: 'grid',
			id: 'mediaGrid',
			columns: [
				{text: 'URL', dataIndex: 'url', flex: 2},
				{
					text: 'Status', 
					dataIndex: 'result',
					flex: 1
				}
			],
			store: Ext.create('Ext.data.Store', {id: 'mediaStore'}),
			listeners: {
				selectionchange: function (grid, record, eOpts) {
					if (Ext.getCmp('savedSearchGrid').getSelectionModel().hasSelection()) {
						Ext.getCmp('insertButton').enable();
					}
				}
			}
		}
	],

	initComponent: function () {
		this.callParent();
		var savedSearchLinkInsertWindow = this;

	},

	processMedia: function processMedia(editor) {
		var store = Ext.getStore('mediaStore');
		var data = Ext.getStore('mediaStore').getData();
		var total_count = Ext.getStore('mediaStore').getCount();
		var retrieve_count = 0;

		// Remove items that are already stored (src contains 'Media.action?')
		store.each(function(record, id){
			if (record) {
				var url = record.get('url');
				if (url.indexOf('Media.action?') > -1) { store.remove(record); }
			}
		});

		// Show the Retrival Window
		this.show();

		
		
	}
		

	
});


