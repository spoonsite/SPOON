/* 
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
/*global Ext*/

Ext.define('OSF.component.SearchToolContentPanel', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.SearchToolContentPanel',
	layout: 'border',
	defaults: {
		bodyStyle: 'padding:0px'
	},
	initComponent: function () {
		this.callParent();

		var searchContentPanel = this;

		//
		//  This is the navPanel on the left 
		//
		searchContentPanel.navPanel = Ext.create('Ext.panel.Panel', {
			region: 'west',
			width: '30%',
			autoScroll: true,
			layout: 'vbox',
			bodyStyle: 'background-color: white',
			split: true
		});

		//
		//  This is the infoPanel on the top right
		//
		searchContentPanel.infoPanel = Ext.create('Ext.panel.Panel', {
			region: 'north',
			height: '30%',
			title: 'Description',
			autoScroll: true,
			bodyPadding: 10,
			split: true
		});

		//
		//  This allows the search results to be loaded and paged in the results grid panel
		//
		searchContentPanel.loadGrid = function (searchObj) {

			var theStore = gStore;
			theStore.getProxy().buildRequest = function (operation) {
				var initialParams = Ext.apply({
					paging: true,
					sortField: operation.getSorters()[0].getProperty(),
					sortOrder: operation.getSorters()[0].getDirection(),
					offset: operation.getStart(),
					max: operation.getLimit()
				}, operation.getParams());
				params = Ext.applyIf(initialParams, theStore.getProxy().getExtraParams() || {});

				var request = new Ext.data.Request({
					url: '/openstorefront/api/v1/service/search/advance',
					params: params,
					operation: operation,
					action: operation.getAction(),
					jsonData: Ext.util.JSON.encode(searchObj)
				});
				operation.setRequest(request);

				return request;
			};

			theStore.loadPage(1);

		};
		//
		//  This is the results grid panel data store
		//
		var gStore = Ext.create('Ext.data.Store', {
			fields: ['name', 'description'],
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
		//
		// This is the results Grid panel in the bottom right of the layout.
		//
		searchContentPanel.resultsGridPanel = Ext.create('Ext.grid.Panel', {
			region: 'center',
			title: 'Search Results',
			split: true,
			store: gStore,
			columnLines: true,
			header: {
				items: [{
						xtype: 'button',
						text: '<i style="color:white;" class="fa fa-external-link"></i>',
						tooltip: 'Show results',
						handler: function () {
							////console.log("load all clicked");
							if (!searchContentPanel.resultsGridPanel.hidden)
							{
								//perform search
								var win = this.up('window');
								var searchRequest = {
									type: 'Advance',
									query: win.searchObj
								}
								CoreUtil.sessionStorage().setItem('searchRequest', Ext.encode(searchRequest));
								window.location.href = 'searchResults.jsp';

								//close window
								win.close();

							}
						}
					}]
			},
			columns: [
				{text: 'Name',
					cellWrap: true,
					dataIndex: 'name',
					width: 150,
					autoSizeColumn: false,
					renderer: function (value) {
						return '<span class="search-tools-column-orange-text">' + value + '</span>';
					}
				},
				{text: 'Description',
					dataIndex: 'description',
					flex: 1,
					autoSizeColumn: true,
					cellWrap: true,
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
				},
				{xtype: 'actioncolumn',
					width: 25,
					resizable: false,
					sortable: false,
					menuDisabled: true,
					items: [{
							iconCls: 'fa fa-link action-icon',
							tooltip: 'Link to page',
							handler: function (grid, rowIndex, colIndex) {
								var theStore = grid.getStore();
								var newUrl = 'view.jsp?fullPage=true&id=' + theStore.getAt(rowIndex).data.componentId;
								window.location.href = newUrl;

								var win = this.up('window');
								win.close();

							}
						}]
				}
			],
			dockedItems: [{
					xtype: 'pagingtoolbar',
					store: gStore,
					dock: 'bottom',
					displayInfo: true
				}]

		});

		//
		//  This is the center panel which splits the right top and bottom panels.
		//
		var centerPanel = Ext.create('Ext.panel.Panel', {
			layout: 'border',
			region: 'center',
			items: [
				searchContentPanel.infoPanel,
				searchContentPanel.resultsGridPanel
			]
		});



		searchContentPanel.add(centerPanel);
		searchContentPanel.add(searchContentPanel.navPanel);
		searchContentPanel.updateLayout(true, true);

	}

});

Ext.define('OSF.component.SearchToolWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.SearchToolWindow',
	title: 'Search Tools',
	iconCls: 'fa fa-lg fa-search-plus',
	width: '70%',
	height: '70%',
	minHeight: 600,
	minWidth: 800,
	y: 40,
	modal: true,
	maximizable: true,
	layout: 'fit',
	initComponent: function () {
		this.callParent();

		var searchToolWin = this;

		//
		//  topicSearchPanel Tab
		//  This is the panel tab for the topic search tool
		//
		var topicSearchPanel = Ext.create('Ext.panel.Panel', {
			title: 'Topic',
			iconCls: 'fa fa-book',
			layout: 'fit',
			items: [
				Ext.create('OSF.component.SearchToolContentPanel', {
					searchToolType: "topic",
					itemId: 'topicPanel'
				})
			]
		});

		//
		//  categorySearchPanel Tab
		//  This is the panel tab for the category search tool
		//
		var categorySearchPanel = Ext.create('Ext.panel.Panel', {
			title: 'Category',
			iconCls: 'fa fa-list-ul',
			layout: 'fit',
			items: [
				Ext.create('OSF.component.SearchToolContentPanel', {
					searchToolType: "category",
					itemId: 'contentPanel'
				})
			]
		});

		//
		//  archSearchPanel Tab
		//  This is the panel tab for the architecture search tool
		//
		var archSearchPanel = Ext.create('Ext.panel.Panel', {
			title: 'Architecture',
			iconCls: 'fa fa-sitemap',
			layout: 'fit',
			items: [
				Ext.create('OSF.component.SearchToolContentPanel', {
					searchToolType: "architecture",
					architectureType: searchToolWin.branding ?  searchToolWin.branding.architectureSearchType : null,
					itemId: 'archPanel'
				})
			]
		});

		//
		//  tagSearchPanel Tab
		//  This is the panel tab for the tag search tool
		//
		var tagSearchPanel = Ext.create('Ext.panel.Panel', {
			title: 'Tag',
			iconCls: 'fa fa-tag',
			layout: 'fit',
			items: [
				Ext.create('OSF.component.SearchToolContentPanel', {
					searchToolType: "tag",
					architectureType: searchToolWin.branding ?  searchToolWin.branding.tagSearchType : null,
					itemId: 'tagPanel'
				})
			]
		});

		var advanceSearch = Ext.create('OSF.component.AdvanceSearchPanel', {
			title: 'Advanced',
			iconCls: 'fa fa-search-plus',
			saveHook: function (response, opts) {
				savedSearches.getStore().reload();
			},
			dockedItems: [
				{
					xtype: 'toolbar',
					dock: 'bottom',
					items: [
						{
							text: 'Search',
							scale: 'medium',
							iconCls: 'fa fa-2x fa-search',
							handler: function () {
								var searchObj = this.up('panel').getSearch();

								if (searchObj) {
									var win = this.up('window');
									var searchRequest = {
										type: 'Advance',
										query: searchObj
									}
									CoreUtil.sessionStorage().setItem('searchRequest', Ext.encode(searchRequest));
									window.location.href = 'searchResults.jsp';

									//close window
									win.close();
								}
							}
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Preview Results',
							scale: 'medium',
							iconCls: 'fa fa-2x fa-eye',
							handler: function () {
								this.up('panel').previewResults();
							}
						},
						{
							xtype: 'tbseparator'
						},
						{
							text: 'Save',
							scale: 'medium',
							iconCls: 'fa fa-2x fa-save',
							handler: function () {
								this.up('panel').saveSearch();
							}
						}
					]
				}
			]
		});

		var savedSearches = Ext.create('Ext.grid.Panel', {
			title: 'Saved Searches',
			iconCls: 'fa fa-save',
			itemId: 'savedGrid',
			columnLines: true,
			store: {
				autoLoad: true,
				proxy: {
					type: 'ajax',
					url: '../api/v1/resource/usersavedsearches/user/current'
				}
			},
			columns: [
				{text: 'Select Search', dataIndex: 'searchName', flex: 1, minWidth: 200,
					renderer: function (value, meta) {
						meta.tdStyle = 'font-size: 16px';
						return value;
					}
				}
			],
			listeners: {
				selectionchange: function (selModel, selected, opts) {
					var tools = savedSearches.getComponent('btools');
					if (selModel.getCount() >= 1) {
						tools.getComponent('search').setDisabled(false);
					} else {
						tools.getComponent('search').setDisabled(true);
					}
				}
			},
			dockedItems: [
				{
					xtype: 'toolbar',
					dock: 'top',
					items: [
						{
							xtype: 'tbfill'
						},
						{
							text: 'Manage Searches',
							iconCls: 'fa fa-gear',
							href: 'usertools.jsp#Searches',
							hrefTarget: ''
						}
					]
				},
				{
					xtype: 'toolbar',
					itemId: 'btools',
					dock: 'bottom',
					items: [
						{
							text: 'Search',
							itemId: 'search',
							scale: 'medium',
							disabled: true,
							iconCls: 'fa fa-2x fa-search',
							handler: function () {

								var grid = this.up('grid');
								var record = grid.getSelectionModel().getSelection()[0];
								var searchObj = Ext.decode(record.get('searchRequest'));

								if (searchObj) {
									var win = this.up('window');
									var searchRequest = {
										type: 'Advance',
										query: searchObj
									}
									CoreUtil.sessionStorage().setItem('searchRequest', Ext.encode(searchRequest));
									window.location.href = 'searchResults.jsp';

									//close window
									win.close();
								}
							}
						}
					]
				}
			]
		});

		//
		//  tabPanel
		//  This is the panel to hold all the other tab panels
		//

		var searchToolPanels = [];
		searchToolPanels.push(topicSearchPanel);
		searchToolPanels.push(categorySearchPanel);
		searchToolPanels.push(tagSearchPanel);

		if (searchToolWin.branding) {
			if (!searchToolWin.branding.hideArchitectureSearchFlg) {
				searchToolPanels.push(archSearchPanel);
			}
		} else {
			searchToolPanels.push(archSearchPanel);
		}

		searchToolPanels.push(advanceSearch);
		searchToolPanels.push(savedSearches);

		var tabPanel = Ext.create('Ext.tab.Panel', {
			items: searchToolPanels
		});

		//***************************
		//  Utility Methods 
		//***************************
		var sortList = function (theArray, fieldname, direction) {

			if (direction === 'DESC') {
				var tData = theArray.sort(function (a, b) {
					if (a[fieldname] > b[fieldname])
						return -1;
					if (a[fieldname] < b[fieldname])
						return 1;
					return 0;

				});
			} else if (direction === 'ASC') {
				var tData = theArray.sort(function (a, b) {
					if (a[fieldname] < b[fieldname])
						return -1;
					if (a[fieldname] > b[fieldname])
						return 1;
					return 0;

				});
			}


			return tData;
		};




		//***************************
		//  Topic Methods 
		//***************************
		//

		//
		//  Topic Load Search
		//
		var topicButtonHandler = function (newTab, item) {

			var desc = item.description;
			var descriptionText = '<h3>' + item.label + '</h3>';
			if (desc)
			{
				descriptionText += '<hr>' + desc;
			}
			newTab.getComponent('topicPanel').infoPanel.update(descriptionText);


			//Do the search on the category attribute
			searchToolWin.searchObj = {
				"sortField": null,
				"sortDirection": "ASC",
				"startOffset": 0,
				"max": 2147483647,
				"searchElements": [{
						"searchType": "COMPONENT",
						"field": "componentType",
						"value": item.componentType,
						"keyField": null,
						"keyValue": null,
						"startDate": null,
						"endDate": null,
						"caseInsensitive": false,
						"numberOperation": "EQUALS",
						"stringOperation": "EQUALS",
						"mergeCondition": "OR"  //OR.. NOT.. AND..
					}]
			};
			newTab.getComponent('topicPanel').loadGrid(searchToolWin.searchObj);
		};



		//
		//  This method calls an API call to get all the topics and creates a list of buttons inside the navPanel.
		//
		//
		var loadTopicNav = function (newTab) {
			//console.log("Loading Topic Nav");
			newTab.setLoading(true);
			Ext.Ajax.request({
				url: '/openstorefront/api/v1/resource/componenttypes',
				success: function (response, opts) {
					newTab.setLoading(false);
					var data = Ext.decode(response.responseText);
					var tData = sortList(data, 'label', 'ASC');
					////console.log("tData",tData);
					Ext.Array.each(tData, function (item) {
						////console.log("newTab",newTab);

						newTab.getComponent('topicPanel').navPanel.add({
							xtype: 'button',
							cls: 'list-button',
							height: 50,
							text: item.label,
							desc: item.description,
							width: '100%',
							handler: function () {
								//console.log("Comp clicked", this.text);
								topicButtonHandler(newTab, item);
							}
						});
					});
				},
				failure: function (response, opts) {
					newTab.setLoading(false);
				}
			});
			newTab.doneLoad = true;
		};


		//
		//  Topic Tab Processing
		//
		var topicTabProcessing = function (tabpanel, newTab, oldtab, opts) {
			if (!newTab.doneLoad) {
				loadTopicNav(newTab);
			}
		};

		//***************************
		//*** Tag Methods ***
		//***************************

		var tagButtonHandler = function (newTab, tag, count) {

			var descriptionText = '<h3>Tag: "' + tag + '"</h3>';
			descriptionText += '<hr /><p>' + count + ' occurrence' + (count > 1 ? 's' : '') + '</p>';
			newTab.getComponent('tagPanel').infoPanel.update(descriptionText);


			//Do the search on the category attribute
			searchToolWin.searchObj = {
				"sortField": null,
				"sortDirection": "ASC",
				"startOffset": 0,
				"max": 2147483647,
				"searchElements": [{
						"searchType": "TAG",
						"value": tag,
						"field": null,
						"keyField": null,
						"keyValue": null,
						"startDate": null,
						"endDate": null,
						"caseInsensitive": false,
						"numberOperation": "EQUALS",
						"stringOperation": "EQUALS",
						"mergeCondition": "OR"  //OR.. NOT.. AND..
					}]
			};
			newTab.getComponent('tagPanel').loadGrid(searchToolWin.searchObj);
		};


		var loadTagNav = function (newTab) {
			newTab.setLoading(true);
			Ext.Ajax.request({
				url: '/openstorefront/api/v1/resource/components/tagviews',
				success: function (response, opts) {
					newTab.setLoading(false);
					var data = Ext.decode(response.responseText);
					var tData = sortList(data, 'text', 'ASC');

					// Count the number of occurences for each tag
					var tags = {};
					Ext.Array.each(tData, function (item) {
						if (tags.hasOwnProperty(item.text)) {
							tags[item.text]++;
						} else {
							tags[item.text] = 1;
						}
					});

					// Loop through tags and create buttons with tag name and count
					for (var key in tags) { 
						if (!tags.hasOwnProperty(key)) continue;

						var buttonCfg = {
							xtype: 'button',
							cls: 'list-button',
							height: 30,
							text: key + ' (' + tags[key] +')',
							desc: key + ' (' + tags[key] +')',
							width: '100%',
							tag: key,
							count: tags[key],
							handler: function() {
								tagButtonHandler(newTab, this.tag, this.count);
							}
						};

						newTab.getComponent('tagPanel').navPanel.add(buttonCfg);
					}

				},
				failure: function (response, opts) {
					newTab.setLoading(false);
				}
			});
			newTab.doneLoad = true;
		};


		//
		//  Tag Tab Processing
		//
		var tagTabProcessing = function (tabpanel, newTab, oldtab, opts) {
			if (!newTab.doneLoad) {
				loadTagNav(newTab);
			}
		};


		//***************************
		//  Category Methods 
		//***************************

		//
		// This is the button handler for the category list button
		// This does an advanced search on the category list button clicked. 
		//
		var categoryButtonHandler = function (newTab, item, item2) {

			var desc = item2.description;
			var descriptionText = '<h3>' + item2.label + '</h3>';
			if (desc)
			{
				descriptionText += '<hr>' + desc;
			}
			newTab.getComponent('contentPanel').infoPanel.update(descriptionText);

			//Do the search on the category attribute
			searchToolWin.searchObj = {
				"sortField": null,
				"sortDirection": "ASC",
				"startOffset": 0,
				"max": 2147483647,
				"searchElements": [{
						"searchType": "ATTRIBUTE",
						"field": null,
						"value": null,
						"keyField": item.attributeType,
						"keyValue": item2.code,
						"startDate": null,
						"endDate": null,
						"caseInsensitive": false,
						"numberOperation": "EQUALS",
						"stringOperation": "EQUALS",
						"mergeCondition": "OR"  //OR.. NOT.. AND..
					}]
			};
			// newTab.setLoading(true);
			newTab.getComponent('contentPanel').loadGrid(searchToolWin.searchObj);
		};

		//
		// This is the loader handler when a category is selected. The category list buttons load below and the panel expands.
		// The list buttons are loaded from the api call if they have not been loaded before.
		//

		var categoryBeforePanelExpandHandler = function (p, animate, eOpts, newTab, item) {
			//Add description when selected
			newTab.getComponent('contentPanel').infoPanel.update(item.description + '<br/> Item Code: ' + item.attributeType);
			if (p.loadedUp === undefined) {
				newTab.setLoading(true);
				Ext.Ajax.request({
					url: '/openstorefront/api/v1/resource/attributes/attributetypes/' + encodeURIComponent(item.attributeType) + '/attributecodeviews',
					success: function (response, opts) {
						newTab.setLoading(false);
						var aData = Ext.decode(response.responseText);

						//Sort the aData
						var dataArray = aData.data;
						var tData = sortList(dataArray, 'label', 'ASC');

						Ext.Array.each(tData, function (item2) {
							p.add({
								xtype: 'button',
								text: item2.label,
								width: '100%',
								textAlign: 'left',
								cls: 'list-button',
								handler: function () {
									categoryButtonHandler(newTab, item, item2);
								}
							});
						});

						p.setHeight(null); //This is required to make the panel collapse/expand work.
						p.loadedUp = true;

					},
					failure: function (response, opts) {
						newTab.setLoading(false);
						p.update("Failed to load data.");
						//console.log("Failed to load data:" + response);
						p.setHeight(null);

					}

				});
			}
		};

		//
		//  This method calls an API call to get all the categories and creates a list of collapsible panels inside the navPanel.
		//
		//
		var loadCategoryNav = function (newTab) {

			newTab.setLoading(true);
			Ext.Ajax.request({
				url: '/openstorefront/api/v1/resource/attributes/attributetypes',
				success: function (response, opts) {
					newTab.setLoading(false);
					var data = Ext.decode(response.responseText);
					data = data.data;
					tData = sortList(data, 'description', 'ASC');

					Ext.Array.each(tData, function (item) {

						if (item.visibleFlg) {
							newTab.getComponent('contentPanel').navPanel.add({
								xtype: 'panel',
								collapsible: true,
								collapsed: true,
								titleCollapse: true,
								height: 100,
								header: {
									cls: 'panel-header',
									title: item.description
								},
								bodyCls: 'search-tools-nav-body-panel-item',
								width: '100%',
								listeners: {
									beforeexpand: function (p, animate, eOpts) {
										categoryBeforePanelExpandHandler(p, animate, eOpts, newTab, item);
									}
								}
							});
						}
					});
				},
				failure: function (response, opts) {
					newTab.setLoading(false);
				}
			});
			newTab.doneLoad = true;
		};

		//
		//  Category Tab Processing
		//
		var categoryTabProcessing = function (tabpanel, newTab, oldtab, opts) {
			if (!newTab.doneLoad) {
				loadCategoryNav(newTab);
			}
		};

		//***************************
		//  Architecture Methods 
		//***************************

		//
		// This is the button handler for the category list button
		// This does an advanced search on the category list button clicked. 
		//
		var archSelectHandler = function (newTab, item) {

			var desc = item.get('description');
			var descriptionText = '<h3>' + item.get('name') + '</h3>';
			if (desc)
			{
				descriptionText += '<hr>' + desc;
			}
			newTab.getComponent('archPanel').infoPanel.update(descriptionText);

			//Do the search on the category attribute
			searchToolWin.searchObj = {
				"sortField": null,
				"sortDirection": "ASC",
				"startOffset": 0,
				"max": 2147483647,
				"searchElements": [{
						"searchType": "ARCHITECTURE",
						"keyField": item.get('attributeType'),
						"keyValue": item.get('attributeCode'),
						"startDate": null,
						"endDate": null,
						"caseInsensitive": false,
						"numberOperation": "EQUALS",
						"stringOperation": "STARTS_LIKE",
						"mergeCondition": "OR"  //OR.. NOT.. AND..
					}]
			};
			newTab.getComponent('archPanel').loadGrid(searchToolWin.searchObj);
		};




		//
		//  This method calls an API call to get all the SVC-V4-A tree arcg and creates a tree inside the navPanel.
		//
		//
		var loadArchNav = function (newTab) {
			
			var architectureType = 'DI2E-SVCV4-A'
			if  (searchToolWin.branding && searchToolWin.branding.architectureSearchType) {
				architectureType = searchToolWin.branding.architectureSearchType;
			}

			newTab.setLoading(true);
			Ext.Ajax.request({
				url: '/openstorefront/api/v1/resource/attributes/attributetypes/' + encodeURIComponent(architectureType) + '/architecture',
				success: function (response, opts) {
					newTab.setLoading(false);
					var data = Ext.decode(response.responseText);
					var tStore = Ext.create('Ext.data.TreeStore', {
						storeId: 'archStore',
						rootVisible: true,
						fields: [{
								name: 'text',
								mapping: 'name'
							},
							"description",
							"attributeType",
							"attributeCode",
							"originalAttributeCode",
							"architectureCode",
							"sortOrder"

						],
						data: data
					});

					newTab.getComponent('archPanel').navPanel.addDocked({
						xtype: 'toolbar',
						dock: 'top',
						items: [
							{
								text: 'Expand All',
								iconCls: 'fa fa-expand',
								handler: function () {
									this.up('panel').down('treepanel').expandAll();
								}
							},
							{
								xtype: 'tbfill'
							},
							{
								text: 'Collapse All',
								iconCls: 'fa fa-compress',
								handler: function () {
									this.up('panel').down('treepanel').collapseAll();
								}
							}
						]
					});

					newTab.getComponent('archPanel').navPanel.add({
						xtype: 'treepanel',
						store: tStore,
						width: '100%',
						rootVisible: false,
						listeners: {
							beforeselect: function (thetree, therecord, theindex, theOpts) {
								archSelectHandler(newTab, therecord);
							}
						}
					});

				},
				failure: function (response, opts) {
					newTab.setLoading(false);
				}
			});
			newTab.doneLoad = true;
		};



		//
		//  Architecture Tab Processing
		//
		var archTabProcessing = function (tabpanel, newTab, oldtab, opts) {
			if (!newTab.doneLoad) {
				loadArchNav(newTab);
			}
		};


		//***************************
		//  Tab Panel  Methods 
		//***************************
		//
		//  This is the tab panel tab change handler
		//


		tabPanel.on('tabchange', function (tabpanel, newTab, oldtab, opts) {

			if (newTab.getTitle() === 'Topic') {
				topicTabProcessing(tabpanel, newTab, oldtab, opts);
			} else if (newTab.getTitle() === 'Tag') {
				tagTabProcessing(tabpanel, newTab, oldtab, opts);
			} else if (newTab.getTitle() === 'Category') {
				categoryTabProcessing(tabpanel, newTab, oldtab, opts);
			} else if (newTab.getTitle() === 'Architecture') {
				archTabProcessing(tabpanel, newTab, oldtab, opts);
			}
		});
		searchToolWin.add(tabPanel);
		searchToolWin.setHeight(600);

		var setActiveTabByTitle = function (tabTitle) {

			//console.log('Setting Active Tab to:' + tabTitle);
			var tabs = tabPanel.items.findIndex('title', tabTitle);
			tabPanel.setActiveTab(tabs);
		};
		setActiveTabByTitle("Category");
		setActiveTabByTitle("Topic");

	} //End Init Component

});
