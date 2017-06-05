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

Ext.define('OSF.landing.OrganizationSearch', {
	
	handler: function(record) {
		var searchConfig = this;		
		if (!searchConfig.view) {
			searchConfig.view = Ext.create('OSF.landing.OrganizationSearchView', {				
			});
		}	
		searchConfig.view.show();
	}
	
});

Ext.define('OSF.landing.OrganizationSearchView', {
	extend: 'Ext.window.Window',
	alias: 'widget.osf-organizationview',
		
	width: '70%',
	height: '70%',	
	modal: true,
	maximizable: true,
	title: 'Organizations',
	iconCls: 'fa fa-lg fa-sitemap',
	layout: 'fit',
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'top',
			items: [
				{
					xtype: 'textfield',
					name: 'filter',
					fieldLabel: 'Filter',
					width: '100%',					
					listeners: {
						change: function(field, newValue, oldValue, opts) {
							var orgWin = field.up('window');
							var store = orgWin.queryById('dataview').getStore();
							store.clearFilter();
							store.filterBy(function(record) {
								return Ext.String.startsWith(record.get('name'), newValue, true);
							});
						}
					}
				}
			]
		}
	],
	items: [
		{
			xtype: 'dataview',
			itemId: 'dataview',
			scrollable: true,
			store: {
				autoLoad: true,
				sorters: [{
					property: 'name',
					direction: 'ASC',
					transform: function(value) {
						return value.toLowerCase();
					}
				}],
				fields: [
					{ name: 'nologo', mapping: function(data) {
						return data.logoOriginalFileName ? false : true;
					}}						
				],
				proxy: {
					type: 'ajax',
					url: 'api/v1/resource/organizations?componentOnly=true',
					reader: {
						type: 'json',
						rootProperty: 'data',
						totalProperty: 'totalNumber'
					}
				}
			},
			itemSelector: 'div.search-tool-org',			
			tpl: new Ext.XTemplate(
				'<tpl for=".">',					
					'<div class="search-tool-org">',
						'<table width="100%"><tr>',
							'<td class="search-tool-org-logo" width="120">',							
								'<tpl if="logoOriginalFileName">',
								'		<img src="Media.action?OrganizationLogo&organizationId={organizationId}" width=100 />',
								'</tpl>',
								'<tpl if="nologo">',
								'		<div class="search-tool-org-logo-text">{[Ext.String.capitalize(values.name).substring(0, 1)]}</div>',
								'</tpl>',								
							'</td>',						
							'<td class="search-tool-org-text">',										
								'<span class="search-tool-org-text-name">{name}</span><br>',
								'<span class="search-tool-org-text-desc">{description}</span>',
							'</td>',
						'</tr></table>',
					'</div>',
				'</tpl>'
			),
			listeners: {
				itemclick: function(dataView, record, item, index, e, eOpts) {	
				
					var searchObj = {
						"sortField": null,
						"sortDirection": "ASC",
						"startOffset": 0,
						"max": 2147483647,
						"searchElements": [{
								"searchType": "COMPONENT",
								"field": "organization",
								"value": record.get('name'),
								"keyField": null,
								"keyValue": null,
								"startDate": null,
								"endDate": null,
								"caseInsensitive": false,
								"numberOperation": "EQUALS",
								"stringOperation": "EQUALS",
								"mergeCondition": "OR"
							}]
					};

					var searchRequest = {
						type: 'Advance',
						query: searchObj
					};

					CoreUtil.sessionStorage().setItem('searchRequest', Ext.encode(searchRequest));
					window.location.href = 'searchResults.jsp';						
					
				}
			}
		}
	],
	initComponent: function () {
		this.callParent();			
		var	orgView = this;
		
	}
	
});

