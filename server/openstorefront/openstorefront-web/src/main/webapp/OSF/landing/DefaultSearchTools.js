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
		'OSF.landing.OrganizationSearch',
		'OSF.landing.RelationshipSearch'
	],
	
	layout: 'center',
	toolSize: 'large', //small, medium, large
	searchTools: [
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
			tip: 'View relationships between entries',
			icon: 'fa-share-alt',
			toolType: 'OSF.landing.RelationshipSearch'
		},				
		{
			text: 'Advanced',
			tip: 'Create Advanced Searches',
			icon: 'fa-search-plus',
			toolType: 'OSF.landing.AdvancedSearch'
		}			
	],
	initComponent: function () {
		this.callParent();			
		var searchToolsPanel = this;	
		
		var iconSize = 'fa-4x';
		var toolCSS = 'search-tool-button-outer-large';
		var toolCSSInner = 'search-tool-button-inner-large';
		if (searchToolsPanel.toolSize === 'small') {
			iconSize = 'fa-3x';
			toolCSS = 'search-tool-button-outer-small';
			toolCSSInner = 'search-tool-button-inner-small';
		} else if (searchToolsPanel.toolSize === 'medium') {
			iconSize = 'fa-4x';
			toolCSS = 'search-tool-button-outer-medium';
			toolCSSInner = 'search-tool-button-inner-medium';
		} 
		
		var dataView = 	Ext.create('Ext.DataView', {			
			itemId: 'dataview',
			store: {				
			},
			itemSelector: 'div.search-tool',
			tpl: new Ext.XTemplate(
				'<div class="action-tool-header">Search Tools</div>',	
				'<tpl for=".">',
					'<div style="margin: 15px;" class="' + toolCSS + ' search-tool" data-qtip="{tip}">',
					  '<div class="' + toolCSSInner + '">',	
						'<tpl if="imageSrc"><img src="{imageSrc}" /></tpl>',	
						'<tpl if="icon"><i class="fa ' + iconSize + ' {icon}"></i></tpl>',
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
		});
		searchToolsPanel.add(dataView);
		
		
		dataView.getStore().loadData(searchToolsPanel.searchTools);
	},
	loadData: function(tools) {
		var searchToolsPanel = this;
		searchToolsPanel.searchTools = tools;
		searchToolsPanel.queryById('dataview').getStore().loadData(tools);
	}
	
});
