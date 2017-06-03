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

Ext.define('OSF.landing.RelationshipSearch', {
	
	handler: function(record, item) {
		var relationshipSearch = this;		
		if (!relationshipSearch.view) {
			relationshipSearch.view = Ext.create('OSF.landing.RelationshipSearchView', {				
			});
		}	
		relationshipSearch.view.show();
	}
	
});

Ext.define('OSF.landing.RelationshipSearchView', {
	extend: 'Ext.window.Window',
	alias: 'widget.osf-relationshipsearchview',
	
	width: '70%',
	height: '70%',
	scrollable: true,
	maximizable: true,	
	modal: true,
	title: 'Relationship Select',
	iconCls: 'fa fa-lg fa-share-alt',
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'top',
			items: [
				{
					text: 'Select Type',
					handler: function() {
						
					}
				}
			]
		}
	],	
	items: [
		{
			xtype: 'panel',
			layout: 'card',
			items: [
				{
					xtype: 'dataview',
					itemId: 'relationType',
					scrollable: true,
					store: {						
					},
					itemSelector: 'div.search-tool',			
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
										'<a href="#" onclick="CoreUtil.pageActions.organizationSearch(\'{name}\');" class="search-tool-org-text-name link">{name}</a><br>',
										'<span class="search-tool-org-text-desc">{description}</span>',
									'</td>',
								'</tr></table>',
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
				},
				{
					xtype: 'dataview',
					itemId: 'itemSelect',
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
					itemSelector: 'div.search-tool',			
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
										'<a href="#" onclick="CoreUtil.pageActions.organizationSearch(\'{name}\');" class="search-tool-org-text-name link">{name}</a><br>',
										'<span class="search-tool-org-text-desc">{description}</span>',
									'</td>',
								'</tr></table>',
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
			]
		}
	],
	initComponent: function () {
		this.callParent();			
		var	relationshipSearchView = this;
		
		var relationshipTypes = [
			{
				text: 'Entries',
				icon: 'fa-list'
			},
			{
				text: 'Organizations',
				icon: 'fa-sitemap'
			},
			{
				text: 'Attributes',
				icon: 'fa-list-alt'
			},
			{
				text: 'Tags',
				icon: 'fa-tags'
			}
		];
		
		var relationType = relationshipSearchView.queryById('relationType');
		relationType.getStore().loadData(relationshipTypes);
		
		
	}
	
});
