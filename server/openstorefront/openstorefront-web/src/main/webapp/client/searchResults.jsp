<%--
Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
			
	<script src="scripts/component/advanceSearch.js?v=${appVersion}" type="text/javascript"></script>		
	<script src="scripts/component/searchToolContentPanel.js?v=${appVersion}" type="text/javascript"></script>
		
	<form id="exportForm" action="../api/v1/service/search/export" method="POST">		
		<p style="display: none;" id="exportFormIds">
		</p> 
	</form>
	
	<script type="text/javascript">
		var SearchPage = {
			viewDetails: function(componentId, resultId) {
				SearchPage.detailPanel.expand();
				
				//load component
				if (!SearchPage.currentLoadedComponent ||  SearchPage.currentLoadedComponent !== componentId) { 
					Ext.defer(function(){
						var resultElm = Ext.get(resultId);
						var container = Ext.getCmp('resultsDisplayPanel').body;
						resultElm.scrollIntoView(container, null, true, true);						
					}, 1000);					
					
					SearchPage.detailContent.load('view.jsp?id=' + componentId);
					SearchPage.currentLoadedComponent = componentId;
				}
			},
			addRemoveCompare: function(chk, labelId, componentId, componentName, nameId) {
				var label = Ext.get(labelId);
				var componentNameElm = Ext.get(nameId);
				if (chk.checked) {
					label.setHtml("Remove from Compare");
					
					Ext.getCmp('compareBtn').getMenu().add({
						componentId: componentId,
						text: componentName,
						chkField: chk,
						labelElm: label,
						handler: function() {
							var container = Ext.getCmp('resultsDisplayPanel').body;
							componentNameElm.scrollIntoView(container, null, true, true);			
						}
					});
					
				} else {					
					label.setHtml("Add to Compare");
					var menuItemToRemove;
					Ext.getCmp('compareBtn').getMenu().items.each(function(item){
						if (item.componentId === componentId) {
							menuItemToRemove = item;
						}
					});
					if (menuItemToRemove) {
						menuItemToRemove.chkField.checked = false;
						menuItemToRemove.labelElm.setHtml("Add to Compare");
						Ext.getCmp('compareBtn').getMenu().remove(menuItemToRemove);
					}
				}
				
			}
		};

		/* global Ext, CoreService, CoreApp */	
		Ext.onReady(function(){	


			var savedSearchId = '${param.savedSearchId}';
			
			var maxPageSize = 300;
			
			var notificationWin = Ext.create('OSF.component.NotificationWindow', {				
			});	

			var feedbackWin = Ext.create('OSF.component.FeedbackWindow', {				
			});
			
			var searchtoolsWin = Ext.create('OSF.component.SearchToolWindow', {	
			});	
			
			var helpWin = Ext.create('OSF.component.HelpWindow', {				
			});												
			
			var compareViewTemplate = new Ext.XTemplate(						
			);
		
			Ext.Ajax.request({
				url: 'Router.action?page=shared/entryCompareTemplate.jsp',
				success: function(response, opts){
					compareViewTemplate.set(response.responseText, true);
				}
			});			
									
			var compareWin = Ext.create('Ext.window.Window', {
				title: 'Compare',
				iconCls: 'fa fa-columns',
				modal: true,
				width: '80%',
				height: '80%',
				maximizable: true,
				layout: {
					type: 'hbox',
					align: 'stretch'
				},				
				items: [	
					{
						xtype: 'panel',
						itemId: 'compareAPanel',
						width: '50%',
						split: true,
						scrollable: true,
						bodyStyle: 'padding: 10px;',
						tpl: compareViewTemplate,
						dockedItems: [
							{
								xtype: 'combobox',
								itemId: 'cb',
								fieldLabel: '',								
								queryMode: 'local',
								name: 'componentA',
								valueField: 'componentId',
								displayField: 'name',
								emptyText: 'Select Entry',
								store: {									
								},
								flex: 1,
								editable: false,
								typeAhead: false,								
								listeners: {
									change: function(cb, newValue, oldValue, opts) {
										var comparePanel = this.up('panel');
										if (newValue) {										
											//remove selection from other cb
											var otherStore = comparePanel.up('panel').getComponent('compareBPanel').getComponent("cb").getStore();
											otherStore.clearFilter();
											otherStore.filterBy(function(record){
												if (record.get('componentId') === newValue) {
													return false;
												} else {
													return true;
												}
											});

											comparePanel.setLoading(true);
											Ext.Ajax.request({
												url: '../api/v1/resource/components/' + newValue + '/detail',
												callback: function(){
													comparePanel.setLoading(false);
												}, 
												success: function(response, opts) {
													var data = Ext.decode(response.responseText);
													data = CoreUtil.processEntry(data);
													comparePanel.update(data);
												}
											});
										} else {
											comparePanel.update(null);
										}
									}
								}
							}
						]
					},
					{
						xtype: 'panel',
						itemId: 'compareBPanel',
						flex: 1,
						split: true,
						scrollable: true,
						bodyStyle: 'padding: 20px;',
						tpl: compareViewTemplate,
						dockedItems: [
							{
								xtype: 'combobox',
								itemId: 'cb',
								fieldLabel: '',								
								queryMode: 'local',
								name: 'componentB',
								valueField: 'componentId',
								displayField: 'name',
								emptyText: 'Select Entry',
								flex: 1,
								store: {									
								},								
								editable: false,
								typeAhead: false,								
								listeners: {
									change: function(cb, newValue, oldValue, opts) {
										var comparePanel = this.up('panel');
										
										if (newValue) {	
											var otherStore = comparePanel.up('panel').getComponent('compareAPanel').getComponent("cb").getStore();
											otherStore.clearFilter();
											otherStore.filterBy(function(record){
												if (record.get('componentId') === newValue) {
													return false;
												} else {
													return true;
												}
											});

											comparePanel.setLoading(true);
											Ext.Ajax.request({
												url: '../api/v1/resource/components/' + newValue + '/detail',
												callback: function(){
													comparePanel.setLoading(false);
												}, 
												success: function(response, opts) {
													var data = Ext.decode(response.responseText);
													data = CoreUtil.processEntry(data);
													comparePanel.update(data);
												}
											});	
										} else {
											comparePanel.update(null);
										}
									}
								}
							}							
						]						
					}
				]
			});
			
			var loadAttributes = function() {
				Ext.Ajax.request({
					url: '../api/v1/resource/attributes/attributetypes',
					success: function(response, opts) {
						var data = Ext.decode(response.responseText);
						
						
						var visible = [];
						var nonvisible = [];
						Ext.Array.each(data.data, function(item){
							
							var panel = Ext.create('Ext.panel.Panel', {
								title: item.description,
								collapsible: true,
								collapsed: true,
								margin: '0 0 1 0',
								titleCollapse: true,
								animCollapse: false,
								html: '&nbsp;',								
								listeners: {									
									expand: function(panel, opts){
										if (!panel.loadedCodes) {
											panel.loadedCodes = true;
											panel.setLoading(true);
											Ext.Ajax.request({
												url: '../api/v1/resource/attributes/attributetypes/' + panel.attributeItem.attributeType + '/attributecodes',
												callback: function(){
													panel.setLoading(false);
												},
												success: function(response, opts) {
													var attributeData = Ext.decode(response.responseText);
															
													var checkboxes = [];		
													Ext.Array.each(attributeData, function(attributeCode){
														var check = Ext.create('Ext.form.field.Checkbox', {
															boxLabel: attributeCode.label,
															attributeCode: attributeCode.attributeCodePk.attributeCode,
															listeners: {
																change: function(checkbox, newValue, oldValue, opts) {																	
																	if (newValue){
																		
																		var containsAttribute = false;
																		Ext.Array.each(attributeFilters, function(item) {
																			if (item.type === attributeCode.attributeCodePk.attributeType &&
																				item.code === attributeCode.attributeCodePk.attributeCode) {
																				containsAttribute = true;
																			}
																		});
																		
																		if (!containsAttribute) {
																			attributeFilters.push({
																				type: attributeCode.attributeCodePk.attributeType,
																				code: attributeCode.attributeCodePk.attributeCode,
																				typeLabel: item.description,
																				label: attributeCode.label,
																				checkbox: checkbox
																			});
																		}
																	} else {
																		attributeFilters = Ext.Array.filter(attributeFilters, function(item) {
																			var keep = true;
																			if (item.type === attributeCode.attributeCodePk.attributeType &&
																				item.code === attributeCode.attributeCodePk.attributeCode) {
																				keep = false;																																								
																			}
																			return keep;
																		});
																	}
																	filterResults();
																}
															}
														});	
														checkboxes.push(check);
													});													
													panel.add(checkboxes);
													panel.updateLayout(true, true);
												}
											});
										}
									}
								}
							});
							panel.attributeItem = item;
							if (item.visibleFlg) {
								visible.push(panel);
							} else {
								nonvisible.push(panel);
							}
						});						
						Ext.getCmp('attributeFiltersContainer').add(visible);
						Ext.getCmp('nonvisibleAttributes').add(nonvisible);						
					}
				});
			};
			loadAttributes();
		
			var attributeFilters = [];
			var filterPanel = Ext.create('Ext.panel.Panel', {
				region: 'west',
				title: 'Filters',
				minWidth: 300,
				collapsible: true,
				titleCollapse: true,
				split: true,				
				autoScroll: true,
				bodyStyle: 'padding: 10px; background: whitesmoke;',
				defaults: {
					labelAlign: 'top',
					labelSeparator: ''
				},
				layout: {
					type: 'vbox',
					align: 'stretch'
				},
				items: [
					{
						xtype: 'textfield',
						id: 'filterByName',
						fieldLabel: 'By Name',						
						name: 'filterName',								
						emptyText: 'Filter Search',
						listeners: {
							change: {
								fn: function(field, newValue, oldValue, opts) {
									filterResults();
								},
								buffer: 1000
							}
						}
					},
					{
						xtype: 'tagfield',
						id: 'filterByTag',
						fieldLabel: 'By Tag',						
						name: 'tags',
						emptyText: 'Select Tags',
						grow: false,
						store: Ext.create('Ext.data.Store', {
							autoLoad: true,
							proxy: {
								type: 'ajax',
								url: '../api/v1/resource/components/tags'
							}
						}),
						listeners: {
							change: function(field, newValue, oldValue, opts) {
								filterPanel.updateLayout(true, true);
								filterResults();
							}	
						}						
					},
					Ext.create('OSF.component.StandardComboBox', {	
						id: 'filterByType',
						fieldLabel: 'By Topic',
						name: 'componentType',						
						margin: '0 0 10 0',					
						editable: false,
						typeAhead: false,
						displayField: 'label',
						valueField: 'componentType',
						storeConfig: {
							autoLoad: false,
							url: '../api/v1/resource/componenttypes'
						},
						listeners: {
							change: function(field, newValue, oldValue, opts) {
								filterResults();
							}	
						}
					}),
					{
						xtype: 'label',
						html: '<b>By User Rating</b>'
					},
					{
						xtype: 'container',
						margin: '0 0 10 0',
						layout: {
							type: 'hbox'
						}, 
						items: [
							{
								xtype: 'button',
								iconCls: 'fa fa-close',								
								style: 'border-radius: 15px 15px 15px 15px;',
								handler: function(){
									var rating = this.up('container').getComponent('filterRating');
									rating.setValue(null);
									filterResults();
								}
							},
							{
								xtype: 'rating',
								id: 'filterByRating',
								itemId: 'filterRating',
								scale: '200%',
								overStyle: 'text-shadow: -1px -1px 0 #000, 1px -1px 0 #000,-1px 1px 0 #000, 1px 1px 0 #000;',
								selectedStyle: 'text-shadow: -1px -1px 0 #000, 1px -1px 0 #000,-1px 1px 0 #000, 1px 1px 0 #000;',
								listeners: {
									click: {
										element: 'element',
										fn: function(){
											filterResults();
										}
									}
								}
							}
						]
					},
					{
						xtype: 'label',
						html: '<b>By Vitals</b>'
					},
					{
						xtype: 'container',
						id: 'attributeFiltersContainer',
						layout: {
							type: 'vbox',							
							align: 'stretch'							
						},
						items: [
							{
								xtype: 'panel',
								hidden: true
							}
						],
						margin: '0 0 20 0'
					},
					{
						xtype: 'panel',
						id: 'nonvisibleAttributes',
						title: 'More Vitals',
						iconCls: 'fa fa-filter',
						header: {
							cls: 'searchresults-morefilter'
						},
						layout: {
							type: 'vbox',							
							align: 'stretch'							
						},						
						listeners: {
							expand: function(panel){
								panel.setTitle('Less Vitals');
							},
							collapse: function(panel){
								panel.setTitle('More Vitals');
							}
						},
						items: [
							{
								xtype: 'panel',
								hidden: true
							}
						],						
						collapsible: true,
						collapsed: true,						
						titleCollapse: true	
					}
				],
				dockedItems: [
					{
						xtype: 'toolbar',
						dock: 'bottom',
						items: [
							{
								xtype: 'tbfill'
							},
							{
								text: 'Reset Filters',
								scale: 'medium',
								width: '80%',
								handler: function(){
									Ext.getCmp('filterByName').reset();
									Ext.getCmp('filterByTag').reset();
									Ext.getCmp('filterByType').reset();
									Ext.getCmp('filterByRating').setValue(null);
								
									Ext.Array.each(attributeFilters, function(filter) {
										filter.checkbox.suspendEvents(false);
										filter.checkbox.setValue(false);
										filter.checkbox.resumeEvents(true);
									});
									attributeFilters = [];
									
									filterResults();
								}
							},
							{
								xtype: 'tbfill'
							}
						]
					}
				]
			});

			var filterMode;
			var filterResults = function() {
				
				//construct filter object
				var filter = {
					name: Ext.getCmp('filterByName').getValue(),
					tags: Ext.getCmp('filterByTag').getValue(),
					type: Ext.getCmp('filterByType').getValue(),
					rating: Ext.getCmp('filterByRating').getValue(),
					sortBy: Ext.getCmp('sortByCB').getSelection() ? Ext.getCmp('sortByCB').getSelection().data : null,
					attributes: attributeFilters
				};				
				
				//determine client side or server-side
				if (filterMode === 'CLIENT') {
					//client side
					
					var filterSet = [];
					
					Ext.Array.each(allResultsSet, function(result) {
						var keep = true;
						
						if (filter.name && filter.name !== '') {
							if (result.name.toLowerCase().indexOf(filter.name.toLowerCase()) === -1) {
								keep = false;
							}
						}
						
						if (filter.tags && filter.tags.length > 0) {
							Ext.Array.each(filter.tags, function (tag) {
								var containsTag = false;							
								Ext.Array.each(result.tags, function (resultsTag) {
									if (resultsTag.text === tag) {
										containsTag= true;
									}
								});
								if (!containsTag) {
									keep = false;
								}
							});			
						}
						
						if (filter.type) {
							if (result.componentType !== filter.type) {
								keep = false;
							}
						}
						
						if (filter.rating) {
							if (result.averageRating < filter.rating){
								keep = false;
							}
						}
						
						if (filter.attributes && filter.attributes.length > 0) {
							var containsAttribute = false;	
							Ext.Array.each(filter.attributes, function (attribute) {														
								Ext.Array.each(result.attributes, function (resultsAttribute) {
									if (resultsAttribute.type === attribute.type &&
										resultsAttribute.code === attribute.code) {
										containsAttribute = true;
									}
								});								
							});	
							if (!containsAttribute) {
									keep = false;
							}
						}
						
						if (keep) {
							filterSet.push(result);
						}
					});
					
					//sort
					if (filter.sortBy) {						
						Ext.Array.sort(filterSet, filter.sortBy.compare);
					}
					
					processResults(filterSet);
					
				} else { 
					//server side										
					var searchRequest = Ext.clone(originalSearchRequest);
					var sort;
					if (filter.sortBy) {
						sort = {
							field: filter.sortBy.field,
							dir: filter.sortBy.dir
						}
					} else {
						sort = {
							field: 'name',
							dir: 'ASC'
						}
					}
					
					//Transform Filters into search elements.
					if (filter.name && filter.name !== '') {
						searchRequest.query.searchElements.push({
							searchType: 'COMPONENT',
							field: 'name',
							value: filter.name,
							caseInsensitive: true,
							stringOperation: 'CONTAINS',
							mergeCondition: 'AND'
						});
					}
					
					if (filter.tags && filter.tags.length > 0) {
						Ext.Array.each(filter.tags, function (tag) {
							searchRequest.query.searchElements.push({
								searchType: 'TAG',								
								value: tag,
								caseInsensitive: true,
								stringOperation: 'EQUALS',
								mergeCondition: 'AND'
							});
						});
					}
					
					if (filter.type) {
						searchRequest.query.searchElements.push({
							searchType: 'COMPONENT',
							field: 'componentType',
							value: filter.type,
							caseInsensitive: false,
							stringOperation: 'EQUALS',
							mergeCondition: 'AND'
						});						
					}
					
					if (filter.rating) {
						searchRequest.query.searchElements.push({
							searchType: 'USER_RATING',							
							value: filter.rating,
							caseInsensitive: false,
							numberOperation: 'GREATERTHANEQUALS',
							mergeCondition: 'AND'
						});						
					}
					
					if (filter.attributes && filter.attributes.length > 0) {
						Ext.Array.each(filter.attributes, function (attribute) {
							searchRequest.query.searchElements.push({
								searchType: 'ATTRIBUTE',							
								keyField: attribute.type,
								keyValue: attribute.code,
								caseInsensitive: false,
								numberOperation: 'EQUALS',
								mergeCondition: 'OR'
							});	
						});
					}
					
					doSearch(searchRequest, sort);
				}
				
			};
			SearchPage.filterResults = filterResults;
		
			var searchResultsStore = Ext.create('Ext.data.Store', {
				autoLoad: false,
				pageSize: maxPageSize,
				sorters: [
						new Ext.util.Sorter({
							property : 'name',
							direction: 'DESC'
						})
				],
				proxy: CoreUtil.pagingProxy({
						url: '../api/v1/service/search',						
						reader: {
						   type: 'json',
						   rootProperty: 'data',
						   totalProperty: 'totalNumber'
						}
				}),
				listeners: {
					beforeload: function(store, operation, opts) {
						Ext.getCmp('resultsDisplayPanel').setLoading("Searching...");
					}
				}
			});
			
			var currentDataSet;
			var processResults = function(data) {
				currentDataSet = data;
				Ext.getCmp('resultsDisplayPanel').update(data);				
		
				//update Stats
				if (filterMode === 'CLIENT') {
					var statLine = 'No results Found';
					if (data.length > 0) {
						statLine = '';
						var stats = {};
						Ext.Array.each(data, function(dataItem){
							if (stats[dataItem.componentType]) {
								stats[dataItem.componentType].count += 1;
							} else {
								stats[dataItem.componentType] = {
									typeLabel: dataItem.componentTypeDescription,
									type: dataItem.componentType,
									count: 1
								}
							}
						});
						
						Ext.Object.each(stats, function(key, value, self) {
							statLine += '<span style="font-size: 14px;"><a href="#" onclick="Ext.getCmp(\'filterByType\').setValue(\'' + value.type + '\');SearchPage.filterResults();">' + value.count + '</a></span> <b>'+ value.typeLabel + '(s)</b> ';
						});
					
					}

					Ext.getCmp('searchStats').update(statLine);	
				}
				
			};
			
			
			var displaySections = [					
				{ text: 'Organization', section: 'organization', display: true },
				{ text: 'Badges', section: 'badges', display: true },
				{ text: 'Description', section: 'description', display: true },
				{ text: 'Last Update', section: 'update', display: true },	
				{ text: 'Vitals', section: 'attributes', display: false },
				{ text: 'Tags', section: 'tags', display: false },
				{ text: 'Average User Rating', section: 'rating', display: false },
				{ text: 'Approved Date', section: 'approve', display: false }										
			];			
			var allResultsSet;
			searchResultsStore.on('load', function(store, records, success, opts){
				Ext.getCmp('resultsDisplayPanel').setLoading(false);
				
				//determine: client filtering or remote
				if (!filterMode) {
					if (searchResultsStore.getTotalCount() <= maxPageSize) {
						filterMode = 'CLIENT';
					} else {
						filterMode = 'REMOTE';
					}										
				}
								
				var data = [];
				Ext.Array.each(records, function(record){
					record.data.ratingStars = [];
					var rating = record.data.averageRating;
					for (var i=0; i<5; i++){					
						record.data.ratingStars.push({						
							star: i < rating? (rating - i) > 0 && (rating - i) < 1 ? 'star-half-o' : 'star' : 'star-o'
						});
					}
					data.push(record.data);
				});
				
				if (filterMode === 'REMOTE') {
					var statLine = 'No results Found';					
					if (data.length > 0) {
						statLine = '';
						var response = opts.getResponse();
						var dataResponse = Ext.decode(response.responseText);
						
						Ext.Array.each(dataResponse.resultTypeStats, function(stat) {
							statLine += '<span style="font-size: 14px;"><a href="#" onclick="Ext.getCmp(\'filterByType\').setValue(\'' + stat.componentType + '\');SearchPage.filterResults();">' + stat.count + '</a></span> <b>'+ stat.componentTypeDescription + '(s)</b> ';
						});
						
					}
					Ext.getCmp('searchStats').update(statLine);
				}
				
				//add all topics found
				var entryTypeInResults = [];
				if (data.length > 0) {
					entryTypeInResults.push({
						label: '*All*',
						componentType: null
					});
						
					var response = opts.getResponse();
					var dataResponse = Ext.decode(response.responseText);
						
					Ext.Array.each(dataResponse.resultTypeStats, function(stat) {
						entryTypeInResults.push({
							label: stat.componentTypeDescription,
							componentType: stat.componentType
						});					
					});
				}
				Ext.getCmp('filterByType').getStore().removeAll();			
				Ext.getCmp('filterByType').getStore().add(entryTypeInResults);
				
				//sorting Attributes
				Ext.Array.each(data, function(dataItem) {
					Ext.Array.sort(dataItem.attributes, function(a, b){
						return a.typeLabel.localeCompare(b.typeLabel);	
					});
				});
				
				
				//Custom Display
				var menuItems = [];
				
				Ext.Array.each(data, function(dataItem) {
					dataItem.show = {};
					Ext.Array.each(displaySections, function(item){
						dataItem.show[item.section] = item.display;
					});	
				});			
				
				Ext.Array.each(displaySections, function(item){					
					menuItems.push({
						xtype: 'menucheckitem',
						text: item.text,
						checked: item.display,
						listeners: {
							checkchange: function(menuItem, checked, opts) {
								Ext.Array.each(data, function(dataItem) {
									dataItem.show[item.section] = checked;
								});
								item.display = checked;
								Ext.getCmp('resultsDisplayPanel').update(currentDataSet);							
								Ext.getCmp('resultsDisplayPanel').updateLayout(true, true);
							}
						}
					});
				});		
				
				Ext.getCmp('customDisplay').setMenu({
					items: menuItems
				}, true);				
								
				allResultsSet = data;
				//filterResults();
				processResults(allResultsSet);
			});
			
			var originalSearchRequest;
			var performSearch = function(){
				Ext.getCmp('resultsDisplayPanel').setLoading("Searching...");
				// First, check if we should load a savedSearch as given by an ID. If not,
				// we assume that this is not a saved search and we can retreive the searchRequest
				// from sessionStorage. If there is no searchRequest in sessionStorage, we default
				// to a search for 'ALL'
				if (savedSearchId !== '') {
					// If an ID is set, we must obtain the searchRequest from the server in order 
					// to actaully perform the search.
					var url = '/openstorefront/api/v1/resource/systemsearches/';
					url += savedSearchId;
					Ext.Ajax.request({
							url: url,
							method: 'GET',
							success: function (response, opts) {
							    var responseObj = Ext.decode(response.responseText);
								var searchRequest = Ext.decode(responseObj.searchRequest);
							
								Ext.getCmp('searchResultsPanel').setTitle('Search Results - Advanced - Saved Search - "' + responseObj.searchName + '"');
								// Adjust searchRequest to match doSearch() and API expectations
								searchRequest.query = {
									searchElements: searchRequest.searchElements
								};
								originalSearchRequest = searchRequest;
								doSearch(searchRequest, {
									field: 'name',
									dir: 'ASC'
								});
							},
							failure: function (response, opts) {
								Ext.MessageBox.alert("Not found", "The saved search you requested was not found.", function() { });
							}
						});

				}
				else {
					var searchRequest = CoreUtil.sessionStorage().getItem('searchRequest');
					if (searchRequest) {
						searchRequest = Ext.decode(searchRequest);
						if (searchRequest.type === 'SIMPLE') {
							Ext.getCmp('searchResultsPanel').setTitle('Search Results - ' + searchRequest.query);
							searchRequest.query = 	{							
								"searchElements": [{
									"searchType": "INDEX",									
									"value": searchRequest.query
								}]
							};
						} else {						
							Ext.getCmp('searchResultsPanel').setTitle('Search Results - Advanced');					
						}
					}
					else {
						//search all					
						Ext.getCmp('searchResultsPanel').setTitle('Search Results - ALL');
						searchRequest = {};					
						searchRequest.query = 	{							
							"searchElements": [{
								"searchType": "INDEX",									
								"value": '*'
							}]
						};
					}
					originalSearchRequest = searchRequest;
					doSearch(searchRequest, {
						field: 'name',
						dir: 'ASC'
					});
				}
			};
			
			var doSearch = function(searchRequest, sort) {
				searchResultsStore.getProxy().setActionMethods({create: 'POST', read: 'POST', update: 'POST', destroy: 'POST'});
				searchResultsStore.getProxy().buildRequest = function (operation) {
					var initialParams = Ext.apply({
						paging: true,
						sortField: sort ? sort.field : operation.getSorters() ? operation.getSorters()[0].getProperty() : null,
						sortOrder: sort ? sort.dir : operation.getSorters() ? operation.getSorters()[0].getDirection() : null,
						offset: operation.getStart(),
						max: operation.getLimit()
					}, operation.getParams());
					params = Ext.applyIf(initialParams, searchResultsStore.getProxy().getExtraParams() || {});

					var request = new Ext.data.Request({
						url: '/openstorefront/api/v1/service/search/advance',
						params: params,
						operation: operation,
						action: operation.getAction(),
						jsonData: Ext.util.JSON.encode(searchRequest.query)
					});
					operation.setRequest(request);

					return request;
				};
				searchResultsStore.loadPage(1);					
			};
			
			
			var badgeHeight = "";
			if (Ext.isIE) {
				badgeHeight = 'height="100%"';
			}
			
			var resultsTemplate = new Ext.XTemplate(
				'<tpl for=".">',
				' <div id="result{#}" class="searchresults-item">',
				'	<h2 id="result{#}name" title="View Details" class="searchresults-item-click" onclick="SearchPage.viewDetails(\'{componentId}\', \'result{#}\')"><tpl if="securityMarkingType">({securityMarkingType}) </tpl>{name}</h2>',
				'	<tpl if="show.organization">',
				'		<p class="searchresults-item-org">{organization}</p>',
				'	</tpl>',
				'  <tpl if="show.badges">',
				'	<tpl for="attributes">',
				'		 <tpl if="badgeUrl"><img src="{badgeUrl}" title="{label}" width="40" '+badgeHeight+' /></tpl>',
				'	 </tpl>',
				'  </tpl>',
				'	<tpl if="show.rating">',				
				'		<tpl if="averageRating"><tpl for="ratingStars"><i class="fa fa-2x fa-{star} rating-star-color" title="User Rating"></i></tpl></tpl>',
				'	</tpl>',
				'	<tpl if="show.description">',
				'		<div class="home-highlight-item-desc">{[Ext.util.Format.ellipsis(Ext.util.Format.stripTags(values.description), 300)]}</div>',
				'  </tpl>',
				'	<tpl if="show.attributes">',	
				'		<ul>',
				'		<tpl for="attributes"><li><b>{typeLabel}: </b>{label} <tpl if="securityMarkingType">({securityMarkingType}) </tpl></li></tpl>',
				'		</ul>',
				'   </tpl>',
				'	<tpl if="show.tags">',				
				'		<tpl if="tags">',
				'			<b>Tags: </b>',
				'			<tpl for="tags">',
				'				<span class="searchresults-tag">',
				'					{text} <tpl if="securityMarkingType">({securityMarkingType}) </tpl>',
				'				</span>&nbsp;',
				'			</tpl>',
				'		</tpl>',
				'	</tpl>',
				'  <br><div class="searchresults-item-update">',
				'  <tpl if="show.approve"> <b>Approved Date:</b> {[Ext.util.Format.date(values.approvedDts, "m/d/y")]}</tpl>',
				'  <tpl if="show.update"> <b>Last Updated:</b> {[Ext.util.Format.date(values.lastActivityDts, "m/d/y")]}</tpl>',
				'   ({componentTypeDescription})<span style="float: right"><input type="checkbox" onclick="SearchPage.addRemoveCompare(this, \'result{#}compare\', \'{componentId}\', \'{name}\', \'result{#}name\')"></input><span id="result{#}compare">Add to Compare</span></span></div>',
				' </div>',
				'</tpl>'		
			);			
			
			var searchResultsPanel = Ext.create('Ext.panel.Panel', {
				region: 'center',
				id: 'searchResultsPanel',
				title: 'Search Results',
				collapsible: true,
				titleCollapse: true,
				collapseDirection: 'left',
				split: true,
				layout: 'fit',
				flex: 1,
				dockedItems: [
					{
						xtype: 'panel',
						dock: 'top',
						id: 'searchStats',
						bodyStyle: 'text-align: center;',
						html: 'Loading...'
					},
					{
						xtype: 'toolbar',
						dock: 'top',
						items: [				
							{
								xtype: 'combobox',
								id: 'sortByCB',
								width: 200,
								tooltip: 'Sort By',
								emptyText: 'Sort By',
								queryMode: 'local',
								editable: false,
								typeAhead: false,
								displayField: 'label',
								valueField: 'fieldCode',
								listeners: {
									change: function(cb, newValue, oldValue, opts) {
										filterResults();
									}
								},
								store: {
									data: [
										{
											label: 'Name (A-Z)',
											field: 'name',
											fieldCode: 'name',
											dir: 'ASC',
											compare: function(a, b){
												return a.name.localeCompare(b.name);
											}
										},
										{
											label: 'Name (Z-A)',
											field: 'name',
											fieldCode: 'name-desc',
											dir: 'DESC',
											compare: function(a, b){
												return b.name.localeCompare(a.name);
											}											
										},
										{
											label: 'User Rating (High-Low)',
											field: 'averageRating',
											fieldCode: 'averageRating',
											dir: 'DESC',
											compare: function(b, a){
												if (a.averageRating > b.averageRating) {
													return 1;
												} else if (a.averageRating < b.averageRating) {
													return -1;
												} else {
													return 0;
												}												
											}											
										},
										{
											label: 'User Rating (Low-High)',
											field: 'averageRating',
											fieldCode: 'averageRating-desc',
											dir: 'ASC',
											compare: function(a, b){
												if (a.averageRating > b.averageRating) {
													return 1;
												} else if (a.averageRating < b.averageRating) {
													return -1;
												} else {
													return 0;
												}
											}											
										},										
										{
											label: 'Last Update (Newest)',
											field: 'lastActivityDts',
											fieldCode: 'lastUpdateDts',
											dir: 'DESC',
											compare: function(b, a){
												if (a.lastActivityDts > b.lastActivityDts) {
													return 1;
												} else if (a.lastActivityDts < b.lastActivityDts) {
													return -1;
												} else {
													return 0;
												}
											}											
										},
										{
											label: 'Last Update (Oldest)',
											field: 'lastActivityDts',
											fieldCode: 'lastUpdateDts-desc',
											dir: 'ASC',
											compare: function(a, b){
												if (a.lastActivityDts > b.lastActivityDts) {
													return 1;
												} else if (a.lastActivityDts < b.lastActivityDts) {
													return -1;
												} else {
													return 0;
												}
											}											
										}											
									]
								}
							},
							{
								xtype: 'splitbutton',
								id: 'compareBtn',
								text: 'Compare',
								iconCls: 'fa fa-columns',
								menu: [
									{
										text: 'Clear All Selected Entries',
										iconCls: 'fa fa-close',
										handler : function() {
											var menu = this.up('menu');
											
											var itemsToRemove = [];
											menu.items.each(function(item) {
												if (item.componentId) {
													item.chkField.checked = false;
													item.labelElm.setHtml("Add to Compare");
													itemsToRemove.push(item);
												}
											});
											Ext.Array.each(itemsToRemove, function(item) {
												menu.remove(item);
											});
										}
									},
									{
										xtype: 'menuseparator'
									}
								],
								listeners: {
									click: function(){
										var menu = this.getMenu();
										var compareAcb = compareWin.getComponent('compareAPanel').getComponent('cb');
										var compareBcb = compareWin.getComponent('compareBPanel').getComponent('cb');
										
										compareAcb.setValue(null);
										compareBcb.setValue(null);
										
										var selectedComponents = [];
										menu.items.each(function(item) {
											if (item.componentId) {
												var record = Ext.create('Ext.data.Model', {													
												});
												record.set({
													componentId: item.componentId,
													name: item.text
												});
												selectedComponents.push(record);
											}
										});
										
										
										//if nothing selected
										if(selectedComponents.length > 0) {
											if (selectedComponents.length === 1) {
												compareAcb.getStore().loadRecords(selectedComponents);
												compareAcb.setValue(selectedComponents[0].get('componentId'));
												
												var records = [];
												searchResultsStore.each(function(record) {
													records.push(record);
												});
												Ext.Array.sort(records, function(a, b){
													return a.get('name').toLowerCase().localeCompare(b.get('name').toLowerCase());
												});
												compareBcb.getStore().loadRecords(records);
			
											} else if (selectedComponents.length > 1) {
												compareAcb.getStore().loadRecords(selectedComponents);	
												compareBcb.getStore().loadRecords(selectedComponents);
												
												compareAcb.setValue(selectedComponents[0].get('componentId'));
												compareBcb.setValue(selectedComponents[1].get('componentId'));
											}											
										} else {
										
											var records = [];
											searchResultsStore.each(function(record) {
												records.push(record);
											});
											Ext.Array.sort(records, function(a, b){
												return a.get('name').toLowerCase().localeCompare(b.get('name').toLowerCase());
											});

											compareAcb.getStore().loadRecords(records);
											compareBcb.getStore().loadRecords(records);
										}
										compareWin.show();
									}
								}								
							},
							{
								xtype: 'tbfill'
							},
							{
								iconCls: 'fa fa-gear',
								tooltip: 'Customize Display',
								id: 'customDisplay'
							}
						]						
					},
					{
						xtype: 'toolbar',
						dock: 'bottom',
						items: [
							Ext.create('Ext.PagingToolbar', {
								store: searchResultsStore,
								displayInfo: true,
								displayMsg: '{0} - {1} of {2}',
								emptyMsg: "No entries to display",
								items: [
									{
										xtype: 'tbseparator'
									},
									{
										text: 'Export',					
										tooltip: 'Exports records in current view',
										iconCls: 'fa fa fa-download',																			
										handler: function () {
											var exportForm = Ext.getDom('exportForm');
											var exportFormIds = Ext.getDom('exportFormIds');
											
											var ids = '';
											searchResultsStore.each(function(record){
												ids += '<input type="hidden" name="multipleIds" value="' + record.get('componentId') + '" />'
											});
											exportFormIds.innerHTML = ids;
											exportForm.submit();
										}
									}
								]
							})
						]						
					}
				],
				items: [
					{
						xtype: 'panel',
						id: 'resultsDisplayPanel',					
						autoScroll: true,
						tpl: resultsTemplate
					}
				]
				
			});
			
			SearchPage.detailContent = Ext.create('OSF.ux.IFrame', {				
			});
			
			SearchPage.detailPanel = Ext.create('Ext.panel.Panel', {
				region: 'east',
				title: 'Details',
				width: '60%',
				collapsible: true,
				collapseDirection: 'right',
				titleCollapse: true,
				flex: 2,
				collapseMode: 'mini',
				split: true,
				collapsed: false,
				layout: 'fit',
				items: [
					SearchPage.detailContent
				]
			});
			//This corrects layout issue
			Ext.defer(function(){
				SearchPage.detailPanel.collapse();
			}, 50);			
			
			var mainContentPanel = Ext.create('Ext.panel.Panel', {
				region: 'center',
				layout: 'border',
				items: [
					filterPanel,
					{
						xtype: 'panel',
						region: 'center',
						layout: {
							type: 'hbox',
							align: 'stretch'
						},
						items: [
							searchResultsPanel,
							SearchPage.detailPanel
						]
					}
				]
			});			
			
			Ext.create('Ext.container.Viewport', {
				layout: 'border',
				items: [{
					xtype: 'panel',
					region: 'north',
					id: 'topNavPanel',
					border: false,					
					cls: 'border_accent',
					dockedItems: [						
						{
							xtype: 'toolbar',
							dock: 'top',								
							cls: 'nav-back-color',
							listeners: {
								resize: function(toolbar, width, height, oldWidth, oldHeight, eOpts) {
									if (width < 1024) {
										toolbar.getComponent('searchBar').setHidden(true);
										toolbar.getComponent('homeTitle').setHidden(false);
										
										toolbar.getComponent('notificationBtn').setText('');										
									} else {
										toolbar.getComponent('searchBar').setHidden(false);
										toolbar.getComponent('homeTitle').setHidden(true);
										
										toolbar.getComponent('notificationBtn').setText('Notifications');										
									}
								}
							},
							items: [
								{
									xtype: 'image',
									height: 53,
									cls: 'linkItem',
									title: 'Go back to Home Page',
									src: '${branding.secondaryLogoUrl}',
									alt: 'logo',
									listeners: {
										el: {
											click: function() {
												window.location.replace('index.jsp');
											}
										}
									}
								},																
								{
									xtype: 'tbfill'
								},								
								{
									xtype: 'tbtext',
									id: 'homeTitle',
									text: 'Search Results',
									hidden: true,
									cls: 'page-title'
								},
								{	
									xtype: 'panel',
									itemId: 'searchBar',
									width: '50%',
									layout: {
										type: 'hbox',
										align: 'stretch'
									},
									items: [
										{
											xtype: 'combobox',										
											itemId: 'searchText',
											flex: 1,
											fieldCls: 'home-search-field',
											emptyText: 'Search',
											queryMode: 'remote',
											hideTrigger: true,
											valueField: 'query',
											displayField: 'name',											
											autoSelect: false,
											store: {
												autoLoad: false,
												proxy: {
													type: 'ajax',
													url: '../api/v1/service/search/suggestions'													
												}
											},
											listeners:{
												specialkey: function(field, e) {
													var value = this.getValue();
													if (e.getKey() === e.ENTER && !Ext.isEmpty(value)) {													
														var query = value;
														if (query && !Ext.isEmpty(query)) {
															var searchRequest = {
																type: 'SIMPLE',
																query: CoreUtil.searchQueryAdjustment(query)
															};
															CoreUtil.sessionStorage().setItem('searchRequest', Ext.encode(searchRequest));
														}
														window.location.href = 'searchResults.jsp';														
													}
												}
											}
										},
										{
											xtype: 'button',
											tooltip: 'Keyword Search',
											iconCls: 'fa fa-2x fa-search icon-top-padding',
											style: 'border-radius: 0px 3px 3px 0px;',
											scale   : 'large',											
											width: 50,
											handler: function(){

												var query = this.up('panel').getComponent('searchText').getValue();
												if (query && !Ext.isEmpty(query)) {
													var searchRequest = {
														type: 'SIMPLE',
														query: CoreUtil.searchQueryAdjustment(query)
													}
													CoreUtil.sessionStorage().setItem('searchRequest', Ext.encode(searchRequest));
												} else {
													delete CoreUtil.sessionStorage().searchRequest;
												}												
												window.location.href = 'searchResults.jsp';

											}
										},
										{
											xtype: 'button',
											text: '<span style="font-size: 10px;">Search Tools</span>',																		
											iconCls: 'fa fa-2x fa-search-plus icon-top-padding',
											iconAlign: 'top',
											margin: '0 0 0 10',
											style: 'border-radius: 3px 0px 0px 3px;',											
											width: 100,
											handler: function(){
												searchtoolsWin.show();
											}
										}										
									]
								},														
								{
									xtype: 'tbfill'
								},
								{
									xtype: 'button',									
									itemId: 'notificationBtn',
									scale   : 'large',
									ui: 'default',
									iconCls: 'fa fa-2x fa-envelope icon-top-padding',
									iconAlign: 'left',
									text: 'Notifications',
									handler: function() {
										notificationWin.show();
										notificationWin.refreshData();
									}
								},								
								{
									xtype: 'button',
									id: 'userMenuBtn',
									scale   : 'large',
									ui: 'default',
									text: 'User Menu',
									minWidth: 150,
									maxWidth: 250,
									menu: {						
										items: [
											{
												text: 'Home',
												iconCls: 'fa fa-home',
												href: 'index.jsp'
											},
											{
												text: 'Admin Tools',
												id: 'menuAdminTools',
												iconCls: 'fa fa-gear',
												hidden: true,
												href: 'admin.jsp'
											},											
											{
												xtype: 'menuseparator'
											},											
											{
												text: 'User Tools',
												iconCls: 'fa fa-user',
												href: 'usertools.jsp'
											},											
											{
												xtype: 'menuseparator'
											},
											{
												text: '<b>Help</b>',
												iconCls: 'fa fa-question-circle',
												handler: function() {
													helpWin.show();
												}
											},
											{
												text: '<b>Feedback / issues</b>',
												iconCls: 'fa fa-commenting',
												handler: function() {
													feedbackWin.show();
												}
											},
											{
												xtype: 'menuseparator'
											},
											{
												text: 'Logout',
												iconCls: 'fa fa-sign-out',
												href: '../Login.action?Logout'												
											}
										],
										listeners: {
											beforerender: function () {
											 this.setWidth(this.up('button').getWidth());
											}
										}
									}
								}
							]
						}						
					]
				},
				mainContentPanel
				]
			});
			
			CoreService.brandingservice.getCurrentBanding().then(function(response, opts){
				var branding = Ext.decode(response.responseText);
				if (branding.securityBannerText && branding.securityBannerText !== '') {
					Ext.getCmp('topNavPanel').addDocked(CoreUtil.securityBannerPanel({
						securityBannerText: branding.securityBannerText
					}), 0);
				}
			});	
		
			//Load 
			performSearch();
		
		
			CoreService.usersevice.getCurrentUser().then(function(response, opts){
				var usercontext = Ext.decode(response.responseText);
				
				var userMenuText = usercontext.username;
				if (usercontext.firstName && usercontext.lastName)
				{
					userMenuText = usercontext.firstName + ' ' + usercontext.lastName;
				}
				Ext.getCmp('userMenuBtn').setText(userMenuText);	
				if (usercontext.admin) {
					Ext.getCmp('menuAdminTools').setHidden(false);
				}				
				
				setupServerNotifications(usercontext);	
			});	
			
		});
		
	</script>	
		
    </stripes:layout-component>
</stripes:layout-render>
