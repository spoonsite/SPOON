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
				{text: 'URL', dataIndex: 'url', flex: 4},
				{
					text: 'Status', 
					dataIndex: 'result',
					flex: 1,
					renderer: function (value, metadata, record) {
						if (value === 'SUCCESS') 
							metadata.tdCls = 'alert-success';
						else if (value === 'FAILED')
							metadata.tdCls = 'alert-danger';
						return value;
					}
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

		// Show the Retrieval Window
		this.show();

		
		// Send API requests, get back temporaryIDs.
		store.each(function(record, id){

			if (record) {
				var data = { URL: record.get('url') };
				var url = '/openstorefront/api/v1/service/application/retrievemedia';
				var method = 'POST';
				Ext.Ajax.request({
					url: url,
					method: method,
					jsonData: data,
					success: function (response, opts) {
						var result = Ext.decode(response.responseText);
						record.set('status', 'OK');
						record.set('result', 'SUCCESS');
						store.commitChanges();
					},
					failure: function (response, opts) {
						record.set('status', 'FAIL');
						record.set('result', 'FAILED');
						store.commitChanges();
					}
				});
			}

		});

		
	}
		

	
});


