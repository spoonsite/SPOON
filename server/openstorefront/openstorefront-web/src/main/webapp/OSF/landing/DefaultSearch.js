/* 
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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

/* global Ext, CoreUtil */

Ext.define('OSF.landing.DefaultSearch', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-defaultsearch',
	
	width: '100%',	
	layout: 'hbox',	
	listeners: {
		resize: function(mainPanel, width, height, oldWidth, oldHeight, eOpts) {
			var searchBar = mainPanel.queryById('searchBar');
			if (width < 1024) {
				searchBar.setWidth('100%');
			} else {
				searchBar.setWidth('75%');
			}
		}
	},
	items: [
		{
			flex: 1
		},
		{	
			xtype: 'panel',
			itemId: 'searchBar',
			width: '75%',		
			layout: {
				type: 'hbox',
				align: 'stretch'
			},
			performSearch: function(query) {
				if (!query || Ext.isEmpty(query)) {																										
					query = '*';		
				}
				
				var containerPanel = this;
				var entryTypeCB = containerPanel.getComponent('entryType');
				var entryType = entryTypeCB.getValue();

				var searchRequest;
				if (entryType) {
					var searchObj = {
						"sortField": null,
						"sortDirection": "ASC",
						"startOffset": 0,
						"max": 2147483647,
						"searchElements": [
							{
								"searchType": "COMPONENT",
								"field": 'componentType',
								"value": entryType,
								"caseInsensitive": false,
								"numberOperation": "EQUALS",
								"stringOperation": "EQUALS",
								"mergeCondition": "AND"
							},
							{
								"searchType": 'INDEX',
								"field": null,
								"value": query,
								"mergeCondition": "AND"
							}
						]
					};

					searchRequest = {
						type: 'Advance',
						query: searchObj
					};						
				} else {												
					searchRequest = {
						type: 'SIMPLE',
						query: CoreUtil.searchQueryAdjustment(query)
					};
				}
				CoreUtil.sessionStorage().setItem('searchRequest', Ext.encode(searchRequest));
				
				window.location.href = 'searchResults.jsp';				
			},
			items: [
				{
					xtype: 'combo',
					itemId: 'entryType',
					name: 'entryType',
					width: 80,
					matchFieldWidth: false,
					valueField: 'code',
					displayField: 'description',
					//grow: true,
					editable: false,
					typeAhead: false,
					forceSelection: true,
					emptyText: 'All',
					fieldCls: 'home-search-field-cat',
					triggerCls: 'home-search-field-cat',
					emptyCls: 'home-search-field-cat',
					store: {						
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/componenttypes/lookup'
						},
						listeners: {
							load: function(store, records, successful, operations, opts) {
								store.add({
									code: null,
									description: 'All'
								});								
							}
						}
					},
					listeners: {
						change: function(field, newValue, oldValue, opts) {
							CoreUtil.tempComponentType = newValue;
							var record = field.getSelection();
							var textDimensions = Ext.util.TextMetrics.measure(field.inputEl, record.get('description'));
							var triggerSize = 60;
							field.setWidth(textDimensions.width + triggerSize);							
						}
					}
				},
				{
					xtype: 'combobox',										
					itemId: 'searchText',
					flex: 1,
					fieldCls: 'home-search-field-new',
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
							url: 'api/v1/service/search/suggestions'													
						},
						listeners: {
							beforeload: function(store, operation, opts) {
								store.getProxy().extraParams = {
									componentType: CoreUtil.tempComponentType ? CoreUtil.tempComponentType : null
								};
							}
						}
					},
					listeners: {

						expand: function(field, opts) {

							field.getPicker().clearHighlight();
						},

						specialkey: function(field, e) {

							var value = this.getValue();

							if (!Ext.isEmpty(value)) {

								switch (e.getKey()) {

									case e.ENTER:
										var query = value;
										field.up('panel').performSearch(query);
										break;

									case e.HOME:
										field.setValue(field.lastQuery);
										field.selectText(0, 0);
										field.expand();
										break;

									case e.END:
										field.setValue(field.lastQuery);
										field.selectText(field.getValue().length, field.getValue().length);
										field.expand();
										break;
								}
							}
						}
					} 
				},
				{
					xtype: 'button',
					tooltip: 'Keyword Search',
					iconCls: 'fa fa-2x fa-search icon-search-adjustment',
					style: 'border-radius: 0px 3px 3px 0px;',																					
					width: 50,											
					handler: function(){
						var query = this.up('panel').getComponent('searchText').getValue();
						this.up('panel').performSearch(query);
					}
				}
			]
		},
		{
			flex: 1
		}
	],	
	
	initComponent: function () {
		this.callParent();	
		
		
	}
	
	
	
});
