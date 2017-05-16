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

/* global Ext */

Ext.define('OSF.landing.DefaultSearch', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-defaultsearch',
	
	width: '100%',	
	layout: 'hbox',	
	
	items: [
		{
			flex: 1
		},
		{	
			xtype: 'panel',
			width: '75%',		
			layout: {
				type: 'hbox',
				align: 'stretch'
			},
			items: [
				{
					xtype: 'combo',
					name: 'entryType',
					width: 325,
					valueField: 'code',
					displayField: 'description',
					editable: false,
					typeAhead: false,
					forceSelection: true,
					emptyText: 'All',
					fieldCls: 'home-search-field-cat',
					triggerCls: 'home-search-field-cat',
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
					}
				},
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
							url: 'api/v1/service/search/suggestions'													
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
										if (query && !Ext.isEmpty(query)) {
											var searchRequest = {
												type: 'SIMPLE',
												query: CoreUtil.searchQueryAdjustment(query)
											};
											CoreUtil.sessionStorage().setItem('searchRequest', Ext.encode(searchRequest));
										}
										window.location.href = 'searchResults.jsp';
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
