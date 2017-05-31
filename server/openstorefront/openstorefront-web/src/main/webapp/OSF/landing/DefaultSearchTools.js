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

Ext.define('OSF.landing.DefaultSearchTools', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-defaultsearchtools',
	requires: [
		'OSF.landing.TagCloud',
		'OSF.landing.AdvancedSearch',
		'OSF.landing.SavedSearch',
		'OSF.landing.OrganizationSearch'
	],
	
	layout: 'center',		
	items: [
		{
			xtype: 'dataview',
			itemId: 'dataview',
			store: {				
			},
			itemSelector: 'div.search-tool',
			tpl: new Ext.XTemplate(
				'<div class="action-tool-header">Search Tools</div>',	
				'<tpl for=".">',
					'<div style="margin: 15px;" class="search-tool-button-outer search-tool" data-qtip="{tip}">',
					  '<div class="search-tool-button-inner">',	
						'<tpl if="imageSrc"><img src="{imageSrc}" /></tpl>',	
						'<tpl if="icon"><i class="fa fa-4x {icon}"></i></tpl>',
						'<br/><span>{text}</span>',
					  '</div>',
					'</div>',
				'</tpl>'
			),
			listeners: {
				itemclick: function(dataView, record, item, index, e, eOpts) {	
					if (!record.tool) {
						record.tool = Ext.create(record.data.toolType, {							
						});
					}
					record.tool.handler(record, item);
				}
			}
		}
	],
	initComponent: function () {
		this.callParent();			
		var searchToolsPanel = this;
				
		var tools=[
			{
				text: 'Tags',
				tip: 'Search Tag Cloud',
				icon: 'fa-cloud',
				toolType: 'OSF.landing.TagCloud'
			},
			{
				text: 'Organizations',
				tip: 'Search by Entry Organization',
				icon: 'fa-sitemap',
				toolType: 'OSF.landing.OrganizationSearch'
			},
			{
				text: 'Relationships',
				tip: 'View relationships bewteen entries',
				icon: 'fa-share-alt',
				toolType: 'OSF.landing.SavedSearch'
			},				
			{
				text: 'Advanced',
				tip: 'Create Advanced Searches',
				icon: 'fa-search-plus',
				toolType: 'OSF.landing.AdvancedSearch'
			}					
		];
		
		searchToolsPanel.queryById('dataview').getStore().loadData(tools);
	}
	
});
