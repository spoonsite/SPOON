/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
				if (!url) { 
					store.remove(record); 
				}
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



Ext.define('OSF.component.MediaInsertWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.MediaInsertWindow',
	layout: 'border',

	title: 'Insert Media',
	closeAction: 'destroy',
	alwaysOnTop: true,
	modal: true,
	width: '70%',
	height: 700,
	mediaToShow: 'IMG',
	mediaName: 'Image',
	
	isEditor: true,
	
	mediaHandler: function(link, alt, mediaType, mimeType) {

		this.insertInlineMedia(link, alt, mediaType, mimeType);
	},
	
	initComponent: function () {
		this.callParent();

		var mediaInsertWindow = this;
		
		mediaInsertWindow.mediaSelectionUrl = (!mediaInsertWindow.isEditor ? (!mediaInsertWindow.mediaSelectionUrl ? "" : mediaInsertWindow.mediaSelectionUrl) : mediaInsertWindow.editor.settings.mediaSelectionUrl());

		mediaInsertWindow.mediaSelectionStore = Ext.create('Ext.data.Store', {			
		});

		mediaInsertWindow.mediaSelectionStorePreLoad = Ext.create('Ext.data.Store', {
			autoLoad: true,
			proxy: {
				type: 'ajax',
				url: mediaInsertWindow.mediaSelectionUrl
			},
			listeners: {
				load: function(store, records, success, eOpts) {
					if (!store.getCount()) {
						mediaInsertWindow.mediaSelection.up('panel').hide();
						mediaInsertWindow.setHeight(220);
					}
					//normalize records
					var showRecords = [];
					Ext.Array.each(records, function(media) {
						if (!media.get('link')) {
							media.set('link', media.get('mediaLink'));
						}
						if (!media.get('caption')) {
							media.set('caption', media.get('name'));
						}
						if (media.get('mimeType')) {
							if (media.get('mimeType').indexOf('image') !== -1) {
								media.set('mediaTypeCode', 'IMG');
							} else if (media.get('mimeType').indexOf('video') !== -1) {
								media.set('mediaTypeCode', 'VID');
							} else if (media.get('mimeType').indexOf('audio') !== -1) {
								media.set('mediaTypeCode', 'AUD');
							} else {
								media.set('mediaTypeCode', 'OTH');
							} 
						}
						
						//keep based on mediatype
						if (media.get('mediaTypeCode') === mediaInsertWindow.mediaToShow) {
							showRecords.push(media);
						}
						
					});
					mediaInsertWindow.mediaSelectionStore.loadRecords(showRecords);
				}
			}
		});

		mediaInsertWindow.insertInlineMedia = function(link, alt, mediaType, mimeType) {
			var content = '<img src="' + link +'" alt="' + alt + '" />';
			
			if (mediaType && mediaType === 'VID') {
				content = '<video width="340" height="240" controls><source src="' +link + '" type="' + (mimeType ? mimeType : 'video/mp4') + '" ><i class="fa fa-5x fa-file-video-o"></i></video>';
			} 			
			mediaInsertWindow.editor.execCommand('mceInsertContent', false, content);
		};

		mediaInsertWindow.mediaSelection = Ext.create('Ext.view.View', {					
			itemSelector: 'div.media-item',
			tpl: new Ext.XTemplate(
				'	<tpl for=".">',
				'		<div class="detail-media-block media-item">',
				'			<tpl if="mediaTypeCode == \'IMG\'">',
				'				<img class="x-item" src="{link}" height="150" alt="{[values.caption ? values.caption : values.filename]}">',
				'				<tpl if="caption || securityMarkingType"><p class="detail-media-caption"><tpl if="securityMarkingType">({securityMarkingType}) </tpl>{caption}</p></tpl>',
				'			</tpl>',
				'			<tpl if="mediaTypeCode == \'VID\'">',
				'				<video height="150" controls><source src="{link}#t=10" onloadedmetadata="this.currentTime=10;" type="{mimeType}" ><i class="fa fa-5x fa-file-video-o"></i></video>',
				'			</tpl>',
				'		</div>',				
				'	</tpl>'
			),			
			store: mediaInsertWindow.mediaSelectionStore,
			listeners: {
				itemclick: function(dataView, offRecord, item, index, e, eOpts) {	
					var record = dataView.getStore().getAt(index);
					if (!record) {
						record = offRecord;
					}
					mediaInsertWindow.mediaHandler(record.get('link'), record.get('caption') ? record.get('caption'): record.get('filename'), record.get('mediaTypeCode'), record.get('mimeType'));
					Ext.defer(function(){
						mediaInsertWindow.close();
					}, 250);					
				}
			}
		});

		mediaInsertWindow.uploadImagePanel = Ext.create('Ext.form.Panel', {
			region: 'north',
			layout: 'fit',
			width: '100%',
			height: 150,
			bodyStyle: 'padding: 10px;',
			title: "Upload New " + mediaInsertWindow.mediaName,
			items: [
				{
					xtype: 'form',
					layout: {
						type: 'vbox',
						align: 'stretch'
					},
					items: [
						{
							xtype: 'filefield',
							title: 'Upload New ' + mediaInsertWindow.mediaName,
							name: 'file',
							allowBlank: false,
							flex: 1,
							fieldLabel: 'Upload an ' + mediaInsertWindow.mediaName + ' <span class="field-required" />',
							labelWidth: 175,
							buttonText: 'Select ' + mediaInsertWindow.mediaName + ' File...'
						},
						{
							xtype: 'panel',
							layout: 'hbox',
							items: [
								{
									xtype: 'textfield',									
									name: 'temporaryMedia.name',
									allowBlank: false,
									flex: 9,
									labelWidth: 175,
									fieldLabel: 'Caption / Name <span class="field-required" />',
									style: 'padding-right: 3px;'
								},
								{
									xtype: 'button',
									title: 'Upload',
									flex: 1,
									iconCls: 'fa fa-lg fa-upload icon-button-color-default',
									formBind: true,
									text: 'Upload',
									handler: function() {
										var uploadForm = this.up('form');
																				
										if (mediaInsertWindow.isEditor && mediaInsertWindow.editor.settings.mediaUploadHandler) {
											mediaInsertWindow.editor.settings.mediaUploadHandler(uploadForm, mediaInsertWindow);
										} else {
											uploadForm.setLoading("Uploading...");
										
											uploadForm.submit({
												url: 'Media.action?' + (mediaInsertWindow.isEditor ? 'UploadTemporaryMedia' : 'UploadMedia'),
												method: 'POST',
												success: function(form, action) { },
												failure: function(form, action) {
													// In this case, to not up-end the
													// server side things, technically a 
													// failure is a potentially a sucess
													if (action.result && action.result.fileName) {
														// True success
														uploadForm.setLoading(false);
														var link = 'Media.action?' + (mediaInsertWindow.isEditor ? 'TemporaryMedia' : 'GeneralMedia') + '&name=';
														link += encodeURIComponent(action.result.name);
														
														var mediaTypeCode = mediaInsertWindow.mediaToShow;
														if (action.result.mimeType) {
															if (action.result.mimeType.indexOf('image') !== -1) {
																mediaTypeCode = 'IMG';
															} else if (action.result.mimeType.indexOf('video') !== -1) {
																mediaTypeCode = 'VID';
															} else if (action.result.mimeType.indexOf('audio') !== -1) {
																mediaTypeCode = 'AUD';
															} 
														}
														
														mediaInsertWindow.mediaHandler(link, action.result.name, mediaTypeCode, action.result.mimeType);
														uploadForm.up('window').close();
													} else {
														// True failure
														uploadForm.setLoading(false);
														Ext.Msg.show({
															title: 'Upload Failed',
															msg: 'The file upload was unsuccessful.',
															buttons: Ext.Msg.OK
														});		
													}
												}
											});
										}
									}
								}
							]
						}
					]
				}
			] 
		});
		
		mediaInsertWindow.add(mediaInsertWindow.uploadImagePanel);
		mediaInsertWindow.add(Ext.create('Ext.panel.Panel', {
			title: 'Pick an Existing ' + mediaInsertWindow.mediaName,
			region: 'center',
			autoScroll: true,
			bodyStyle: 'padding: 10px;',			
			items: mediaInsertWindow.mediaSelection
		}));
	}
		

	
});

MediaUtil = {
	
	generalMediaUrl: function(){
		return 'api/v1/resource/generalmedia';
	},
	generalMediaUnloadHandler: function(uploadForm, mediaInsertWindow){
		//check name 
		var name = uploadForm.getValues()['temporaryMedia.name'];
		uploadForm.setLoading("Checking Name...");	
		Ext.Ajax.request({
			url: 'api/v1/resource/generalmedia/' + encodeURIComponent(name) + '/available',
			callback: function(){
				uploadForm.setLoading(false);
			},
			success: function(response, opts) {
				if (response.responseText === 'true') {
					uploadForm.setLoading("Uploading Media...");																					
					uploadForm.submit({
						url: 'Media.action?UploadGeneralMedia',
						method: 'POST',
						params: {
							'generalMedia.name': name 
						},
						callback: function() {
							uploadForm.setLoading(false);
						},
						success: function(form, action) {
							var link = "Media.action?GeneralMedia&name=";
								link += encodeURIComponent(name);

							mediaInsertWindow.insertInlineMedia(link, name, mediaInsertWindow.mediaToShow, 'video/mp4');													
							mediaInsertWindow.close();
						},
						failure: function(form, action){
							Ext.Msg.show({
								title: 'Upload Failed',
								msg: 'The file upload was not successful.',
								icon: Ext.Msg.ERROR,
								buttons: Ext.Msg.OK
							});	
						}
					});	
				} else {
					Ext.Msg.show({
						title:'Check Name',
						message: 'Name must be unique to the general Media<br>See Data Management->Media',
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.ERROR,
						fn: function(btn) {															
						}
					});
				}
			}
		});
	}
	
};

Ext.define('OSF.component.FullScreenEditor', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.FullScreenEditor',
	layout: 'fit',
	closeAction: 'destroy',
	maximizable: true, 
	maximized: true,
	alwaysOnTop: true,
	modal: true,
	title: 'Editor',
	iconCls: 'fa fa-edit',
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'bottom',
			items: [
				{
					xtype: 'tbfill'
				},
				{
					text: 'Close',
					iconCls: 'fa fa-2x fa-close',
					scale: 'medium',
					handler: function() {
						this.up('window').close();
					}
				},
				{
					xtype: 'tbfill'
				}				
			]
		}
	],
	initComponent: function () {
		this.callParent();
		
		var fswin = this;
		
		var plugins = '';
		if (fswin.editor.settings.plugins) {
			var pluginGroup = fswin.editor.settings.plugins.split(' ');
			var restOfPlugins = [];
			Ext.Array.each(pluginGroup, function(plugin){
				if (plugin !== 'osffullscreen') {
					restOfPlugins.push(plugin);
				}
			});
			
			plugins = restOfPlugins.join(' ');
		}
		
		fswin.fsEditor = Ext.create('Ext.ux.form.TinyMCETextArea', {
			value: fswin.editor.getContent(),
			tinyMCEConfig: {
				plugins: plugins,
				toolbar1: fswin.editor.settings.toolbar1,
				statusbar: fswin.editor.settings.statusbar,
				menubar: fswin.editor.settings.menubar,
				skin: fswin.editor.settings.skin,
				toolbar_items_size: fswin.editor.settings.toolbar_items_size,
				extended_valid_elements: fswin.editor.settings.extended_valid_elements,
				table_default_styles: fswin.editor.settings.table_default_styles,
				mediaSelectionUrl: fswin.editor.settings.mediaSelectionUrl,
				mediaUploadHandler: fswin.editor.settings.mediaUploadHandler
			}
		});
		
		fswin.on('beforeclose', function(panel, eOts) {
			fswin.editor.setContent(fswin.fsEditor.getValue());
		});
		
		fswin.fsEditor.on('change', function(panel, newValue, oldValue) {
			fswin.editor.setContent(fswin.fsEditor.getValue());
		});		
		
		fswin.add(fswin.fsEditor);
	}
	
});
