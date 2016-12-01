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
/* global Ext, CoreService, top, CoreUtil */

Ext.define('OSF.component.SavedSearchLinkInsertWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.SavedSearchLinkInsertWindow',
	layout: 'fit',

	title: 'Insert Link to Saved Search',
	closeMode: 'destroy',
	alwaysOnTop: true,
	modal: true,
	width: '40%',
	height: '50%',
	
	initComponent: function () {
		this.callParent();

		var savedSearchLinkInsertWindow = this;
		
		savedSearchLinkInsertWindow.store = Ext.create('Ext.data.Store', {
			autoLoad: true,
			proxy: {
				type: 'ajax',
				url: 'api/v1/resource/systemsearches',
				reader: {
					type: 'json',
					rootProperty: 'data',
					totalProperty: 'totalNumber'
				}
			}
		});
		
		savedSearchLinkInsertWindow.grid = Ext.create('Ext.grid.Panel', {
			columnLines: true,
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
			store: savedSearchLinkInsertWindow.store,
			listeners: {
				selectionchange: function (grid, record, eOpts) {
					if (savedSearchLinkInsertWindow.grid.getSelectionModel().hasSelection()) {
						var tools = savedSearchLinkInsertWindow.grid.getComponent('tools');
						tools.getComponent('insert').enable();						
					}
				}
			},
			dockedItems: [
				{
					xtype: 'toolbar',
					itemId: 'tools',
					dock: 'bottom',
					items: [
						{
							text: 'Insert Link',
							itemId: 'insert',
							iconCls: 'fa fa-link',
							disabled: true,
							handler: function(button) {
								var window = button.up('window');
								var editor = window.editor;
								var record = savedSearchLinkInsertWindow.grid.getSelection()[0];
								var link = '<a href="/openstorefront/searchResults.jsp?savedSearchId=';
								link += record.getData().searchId;
								link +=	'">';
								link += record.getData().searchName;
								link += '</a>';
								editor.execCommand('mceInsertContent', false, link);
								window.close();
							}
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Cancel',
							handler: function(button) {
								var window = button.up('window');
								window.close();
							}
						}
					]
				}
			]			
		});		
		savedSearchLinkInsertWindow.add(savedSearchLinkInsertWindow.grid);
	}
		

	
});

Ext.define('OSF.component.SearchPopupResultsWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.SearchPopupResultsWindow',

	title: 'Saved Search Results',
	modal: true,
	closeMode: 'destroy',
	alwaysOnTop: true,
	width: '70%',
	height: '50%',
	maximizable: true,
	layout: 'fit',
	
	initComponent: function () {
		this.callParent();
		var resultsWindow = this;
		
		resultsWindow.searchResultsStore = Ext.create('Ext.data.Store', {
			pageSize: 50,
			autoLoad: false,
			remoteSort: true,
			sorters: [
				new Ext.util.Sorter({
					property: 'name',
					direction: 'ASC'
				})
			],
			proxy: CoreUtil.pagingProxy({
				actionMethods: {create: 'POST', read: 'POST', update: 'POST', destroy: 'POST'},
				reader: {
					type: 'json',
					rootProperty: 'data',
					totalProperty: 'totalNumber'
				}
			})			
		});
		
		resultsWindow.searchResultsGrid = Ext.create('Ext.grid.Panel', {
			columnLines: true,
			store: resultsWindow.searchResultsStore,
			columns: [
				{
					text: 'Name',
					cellWrap: true,
					dataIndex: 'name',
					flex: 1,
					autoSizeColumn: false,
					renderer: function (value, metaData, record) {
						var url = '<a style="text-decoration: none" href="/openstorefront/view.jsp?id=';
						url += record.getData().componentId;
						url += '">';
						url += '<span class="search-tools-column-orange-text">' + value + '</span></a>';
						return url;
					}
				},
				{
					text: 'Description',
					dataIndex: 'description',
					flex: 3,
					cellWrap: true,
					autoSizeColumn: true,
					renderer: function (value) {
						value = Ext.util.Format.stripTags(value);
						var str = value.substring(0, 500);
						if (str === value) {
							return str;
						} else {
							str = str.substr(0, Math.min(str.length, str.lastIndexOf(' ')));
							return str += ' ... <br/>';
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
							xtype: 'pagingtoolbar',
							store: resultsWindow.searchResultsStore,
							displayInfo: true
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'View Full Results Page',
							iconCls: 'fa fa-2x fa-search',
							scale: 'medium',
							handler: function() {
								var url = "/openstorefront/searchResults.jsp?savedSearchId=";
								url += this.up('window').searchId;
								if (top) {
									top.window.location.href = url;
								} else {
									window.location.href = url;
								}
							}
						}
					]
				}
			]
		});		
		resultsWindow.add(resultsWindow.searchResultsGrid);
		

		resultsWindow.showResults = function(savedSearchId) {
			this.searchId = savedSearchId;
			
			resultsWindow.show();
			
			var url = 'api/v1/resource/systemsearches/';
			url += savedSearchId;
			Ext.Ajax.request({
				url: url,
				method: 'GET',
				success: function (response, opts) {
					var responseObj = Ext.decode(response.responseText);
					var searchRequest = Ext.decode(responseObj.searchRequest);

					searchRequest.query = {
						searchElements: searchRequest.searchElements
					};
					resultsWindow.searchResultsGrid.getStore().getProxy().buildRequest = function buildRequest(operation) {
						var initialParams = Ext.apply({
							paging: true,
							sortField: operation.getSorters()[0].getProperty(),
							sortOrder: operation.getSorters()[0].getDirection(),
							offset: operation.getStart(),
							max: operation.getLimit()
						}, operation.getParams());
						params = Ext.applyIf(initialParams, resultsWindow.searchResultsGrid .getStore().getProxy().getExtraParams() || {});

						var request = new Ext.data.Request({
							url: 'api/v1/service/search/advance',
							params: params,
							operation: operation,
							action: operation.getAction(),
							jsonData: Ext.util.JSON.encode(searchRequest.query)
						});
						operation.setRequest(request);

						return request;
					};
					resultsWindow.searchResultsGrid.getStore().loadPage(1);
				},
				failure: function (response, opts) {
					Ext.MessageBox.alert("Not found", "The saved search you requested was not found.", function() { });
				}
			});



		};
	}
		
});

Ext.define('OSF.component.InlineMediaRetrieverWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.InlineMediaRetrieverWindow',
	layout: 'fit',

	title: 'Retrieving External Media',
	modal: true,
	closeMode: 'destroy',
	alwaysOnTop: true,
	width: '40%',
	height: '50%',
	closable: false,

	initComponent: function () {
		this.callParent();
		var inlineMediaRetrieverWindow = this;
		
		
		inlineMediaRetrieverWindow.grid = Ext.create('Ext.grid.Panel', {	
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
			store: {},			
			dockedItems: [
				{
					xtype: 'panel',
					padding: '10px',
					html: 'Please wait while external media is retrieved to be stored in the Storefront. The status of the operation can be seen below.'
				},
				{
					xtype: 'toolbar',
					itemId: 'tools',
					dock: 'bottom',
					items: [
						{
							xtype: 'tbfill'
						},
						{
							xtype: 'button',
							itemId: 'close',
							text: 'Close',
							disabled: true,
							handler: function() {
								inlineMediaRetrieverWindow.grid.getStore().removeAll();
								this.up('window').close();
							}
						},
						{
							xtype: 'tbfill'
						}
					]
				}
			]			
		});		
		inlineMediaRetrieverWindow.add(inlineMediaRetrieverWindow.grid);

	},
	
	processMedia: function(editor) {
		var mediaWindow = this;
		
		var store = mediaWindow.grid.getStore();
		
		// Set up some helper functions

		var setIgnoreLinks = function(originalURL) {
			// Add an html attribute to ignore these links.
			var elem = document.createElement('html');
			elem.innerHTML = editor.getContent();
			// For now, we are only grabbing media from img tags.
			var images = elem.getElementsByTagName('img');
			Ext.Array.each(images, function(image) {
				if (image.src === originalURL) {
					image.setAttribute('data-storefront-ignore', true);
				}
			});
			mediaWindow.programmaticUpdate = true;
			editor.setContent(elem.innerHTML);
			mediaWindow.programmaticUpdate = false;			
		};

		var checkIfDone = function() {		
			var total_count = store.getCount();
			var success_count = 0;
			var failure_count = 0;
			store.each(function(record, id) {
				if (record) {
					if (record.get('status') === 'OK') success_count++;
					else if (record.get('status') === 'FAIL') failure_count++;
				}
				else failure_count++;
			});

			if (success_count === total_count) {
				setTimeout(function() { 
					store.removeAll();
					mediaWindow.close();
					Ext.toast("Successfully retrieved external media");
				}, 1000);
			}
			else if (success_count + failure_count === total_count) {
				// Some failures. We must notify the user and ignore the links from now on.
				store.each(function(record, id) {
					if (record) {
						if (record.get('status') === 'FAIL') {
							setIgnoreLinks(record.get('url'));
						}
					}
				});
				setTimeout(function() { 
					var msg = "Some of the external media you inserted was not able to be saved ";
					msg += "to the Storefront. This could be because whatever media link was inserted ";
					msg += "is not available publicly or the media was not in an expected format. <br /><br />";
					msg += "To ensure that this media displays properly in the Storefront, you should ";
					msg += "take note of which media failed and upload the media using the 'Media' tab on your entry.";

					Ext.Msg.alert('External media failure', msg, function() {
						var tools = mediaWindow.grid.getComponent('tools');
						tools.getComponent('close').enable();
					});
				}, 1000);
			}
		};

		var replaceLinks = function(originalUrl, temporaryId) {
			var replacement = "/openstorefront/Media.action?TemporaryMedia&name=" + temporaryId;
			var content = editor.getContent();
			// Because TinyMCE sends back HTML encoded entities, we need to decode to replace.
		
			var temp = document.createElement('textarea');
			temp.innerHTML = content;
			content = temp.value;
			content = content.split(originalUrl).join(replacement); //replacement without regex
			mediaWindow.programmaticUpdate = true;
			editor.setContent(content);
			mediaWindow.programmaticUpdate = false;			
		};

		

		// Now begin processing media

		// Remove items that are already stored (src contains 'Media.action?')
		// Also don't send back src urls that are blank.


		store.each(function(record, id){
			if (record) {
				var url = record.get('url');
				if (url.indexOf('Media.action?') > -1) { store.remove(record); }
				if (!url) { store.remove(record); }
			}
			else {
				store.remove(record);
			}

		});

		// If there's nothing left, we're done here.
		if (store.getCount() === 0) {
			return;
		}

		// Show the Retrieval Window
		mediaWindow.show();


		
		// Send API requests, get back temporaryIDs.
		store.each(function(record, id){
			if (record) {
				var data = { URL: record.get('url') };
				var url = 'api/v1/service/application/retrievemedia';
				var method = 'POST';
				Ext.Ajax.request({
					url: url,
					method: method,
					jsonData: data,
					success: function (response, opts) {
						var result = Ext.decode(response.responseText);
						replaceLinks(data.URL, result.fileName); 
						record.set('status', 'OK');
						record.set('result', 'SUCCESS');
						store.commitChanges();
						checkIfDone();
					},
					failure: function (response, opts) {
						record.set('status', 'FAIL');
						record.set('result', 'FAILED');
						store.commitChanges();
						checkIfDone();
					}
				});
			}

		});
	}

	
});