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
<stripes:layout-render name="../../../client/layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
			
	<script src="scripts/component/searchToolContentPanel.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/component/searchToolWindow.js?v=${appVersion}" type="text/javascript"></script>		
		
	<script type="text/javascript">
		var SearchPage = {
			viewDetails: function(componentId) {
				SearchPage.detailPanel.expand();
				
				//load component
				SearchPage.detailContent.load('view.jsp?id=' + componentId);
			}
		};

		/* global Ext, CoreService, CoreApp */	
		Ext.onReady(function(){	
			
			var notificationWin = Ext.create('OSF.component.NotificationWindow', {				
			});	

			var feedbackWin = Ext.create('OSF.component.FeedbackWindow', {				
			});
			
			var searchtoolsWin = Ext.create('OSF.component.SearchToolWindow', {	
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
															attributeCode: attributeCode.attributeCodePk.attributeCode															
														});	
														checkboxes.push(check);
													});													
													panel.add(checkboxes);													
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
						visible.reverse();
						nonvisible.reverse();
						Ext.getCmp('attributeFiltersContainer').add(visible);
						Ext.getCmp('nonvisibleAttributes').add(nonvisible);						
					}
				});
			};
			loadAttributes();
			
			var filterPanel = Ext.create('Ext.panel.Panel', {
				region: 'west',
				title: 'Filters',
				minWidth: 250,
				collapsible: true,
				titleCollapse: true,
				split: true,
				autoScroll: true,
				bodyStyle: 'padding: 10px; background: whitesmoke;',
				defaults: {
					labelAlign: 'top'
				},
				layout: {
					type: 'vbox',
					align: 'stretch'
				},
				items: [
					{
						xtype: 'textfield',
						fieldLabel: 'By Name',						
						name: 'filterName',
						emptyText: 'Filter Search'
					},
					{
						xtype: 'tagfield',
						fieldLabel: 'By Tag',						
						name: 'tags',
						emptyText: 'Select Tags'						
					},
					{
						xtype: 'label',
						html: '<b>User Rating:</b>'
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
								}
							},
							{
								xtype: 'rating',
								itemId: 'filterRating',
								scale: '200%',
								overStyle: 'text-shadow: -1px -1px 0 #000, 1px -1px 0 #000,-1px 1px 0 #000, 1px 1px 0 #000;',
								selectedStyle: 'text-shadow: -1px -1px 0 #000, 1px -1px 0 #000,-1px 1px 0 #000, 1px 1px 0 #000;'
							}
						]
					},
					{
						xtype: 'label',
						html: '<b>Attributes:</b>'
					},
					{
						xtype: 'container',
						id: 'attributeFiltersContainer',
						layout: {
							type: 'accordion',							
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
						title: 'More Filters',
						iconCls: 'fa fa-filter',
						header: {
							cls: 'searchresults-morefilter'
						},
						layout: {
							type: 'accordion',							
							align: 'stretch'							
						},						
						listeners: {
							expand: function(panel){
								panel.setTitle('Less Filters');
							},
							collapse: function(panel){
								panel.setTitle('More Filters');
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
								width: '80%'
							},
							{
								xtype: 'tbfill'
							}
						]
					}
				]
			});
			
			var searchResultsStore = Ext.create('Ext.data.Store', {
				autoLoad: false,
				pageSize: 300,
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
				})
			});
			
			searchResultsStore.on('load', function(store, records, success, opts){
				Ext.getCmp('resultsDisplayPanel').setLoading(false);
				var data = [];
				Ext.Array.each(records, function(record){
					data.push(record.data);
				});
				Ext.getCmp('resultsDisplayPanel').update(data);
				
				//update Stats
				var statLine = 'No results Found';
				if (records.length > 0) {
					statLine = '';
					var stats = {};
					Ext.Array.each(records, function(record){
						if (stats[record.get('componentType')]) {
							stats[record.get('componentType')].count += 1;
						} else {
							stats[record.get('componentType')] = {
								typeLabel: record.get('componentTypeDescription'),
								count: 1
							}
						}
					});
					Ext.Object.each(stats, function(key, value, self) {
						statLine += '<span style="font-size: 14px;">' + value.count + '</span> <b>'+ value.typeLabel + '(s)</b> '
					});
				}
				
				Ext.getCmp('searchStats').update(statLine);				
				
			});
			
			var performSearch = function(){
				//pull session storage
				Ext.getCmp('resultsDisplayPanel').setLoading("Searching...");
				var searchRequest = CoreUtil.sessionStorage().getItem('searchRequest');
				if (searchRequest) {
					searchRequest = Ext.decode(searchRequest);
					if (searchRequest.type === 'SIMPLE') {
						Ext.getCmp('searchResultsPanel').setTitle('Search Results - ' + searchRequest.query);
						searchResultsStore.load({
						url: '../api/v1/service/search',
						params: {
							paging: true,
							query: searchRequest.query
						}
					});
					} else {
						//advanced 
						Ext.getCmp('searchResultsPanel').setTitle('Search Results - Advance');
						searchResultsStore.getProxy().buildRequest = function (operation) {
							var initialParams = Ext.apply({
								paging: true,
								sortField: operation.getSorters()[0].getProperty(),
								sortOrder: operation.getSorters()[0].getDirection(),
								offset: operation.getStart(),
								max: operation.getLimit()
							}, operation.getParams());
							params = Ext.applyIf(initialParams, store.getProxy().getExtraParams() || {});

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
						searchResultsStore.loadPage(1);						
					}
				} else {
					//search all
					Ext.getCmp('searchResultsPanel').setTitle('Search Results - ALL');
					searchResultsStore.load({
						url: '../api/v1/service/search',
						params: {
							paging: true,
							query: '*'
						}
					});
				}			
				
			};
			
			var resultsTemplate = new Ext.XTemplate(
				'<tpl for=".">',
				' <div class="searchresults-item" onclick="SearchPage.viewDetails(\'{componentId}\')">',
				'  <h2>{name}</h2>',
				'  <tpl for="attributes">',
				'    <tpl if="badgeUrl"><img src="{badgeUrl}" title="{label}" width="40" /></tpl>',
				'  </tpl>',
				'  <div class="home-highlight-item-desc">{[Ext.util.Format.ellipsis(Ext.util.Format.stripTags(values.description), 300)]}</div>',
				'  <br><div class="searchresults-item-update">Last Updated: {[Ext.util.Format.date(values.lastActivityDts, "m/d/y")]} ({componentTypeDescription})</div>',
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
								width: 150,
								tooltip: 'Sort By',
								emptyText: 'Sort By'
							},
							{
								text: 'Compare',
								iconCls: 'fa fa-columns',
								handler: function(){
									
								}
							},
							{
								xtype: 'tbfill'
							},
							{
								iconCls: 'fa fa-gear',
								handler: function(){
									
								}
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
										iconCls: 'fa fa fa-download',																			
										handler: function () {
											
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
									width: 200,
									height: 53,
									title: 'Go back to Home Page',
									src: 'images/di2elogo-sm.png',
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
											xtype: 'button',
											tooltip: 'Search Tools',								
											iconCls: 'fa fa-2x fa-th icon-top-padding',
											style: 'border-radius: 3px 0px 0px 3px;',
											scale   : 'large',
											width: 50,
											handler: function(){
												searchtoolsWin.show();
											}
										}, 
										{
											xtype: 'textfield',
											itemId: 'searchText',
											flex: 1,
											fieldCls: 'home-search-field',
											emptyText: 'Search',
											listeners:{
												specialkey: function(field, e) {
													var value = this.getValue();
													if (e.getKey() === e.ENTER && !Ext.isEmpty(value)) {													
														var query = value;
														if (query && !Ext.isEmpty(query)) {
															var searchRequest = {
																type: 'SIMPLE',
																query: CoreUtil.searchQueryAdjustment(query)																
															}
															CoreUtil.sessionStorage().setItem('searchRequest', Ext.encode(searchRequest));
														}
														window.location.href = 'Router.action?page=main/searchResults.jsp';														
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
												window.location.href = 'Router.action?page=main/searchResults.jsp';

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
												href: '../help',
												hrefTarget: '_blank'
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
