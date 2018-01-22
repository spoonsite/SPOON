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
	
	maximizable: true,	
	modal: true,
	title: 'Relationship Select',
	iconCls: 'fa fa-lg fa-share-alt',
	layout: 'fit',
	listeners: {
		resize: function(view, width, height, oldWidth, oldHeight, eOpts) {
			if (width < 568) {
				this.maximize(false);
			} else {
				this.restore();
			}		
		}
	},	
	items: [
		{
			xtype: 'panel',
			itemId: 'cardPanel',
			layout: 'card',
			dockedItems: [
				{
					xtype: 'toolbar',
					dock: 'top',				
					items: [
						{
							text: 'Select Type',
							iconCls: 'fa fa-lg fa-arrow-circle-left',
							handler: function() {
								var relationshipWin = this.up('window');
								var cardPanel = relationshipWin.queryById('cardPanel');
								cardPanel = cardPanel.setActiveItem('relationType');
							}
						},
						{
							xtype: 'textfield',
							name: 'filter',
							fieldLabel: 'Filter',
							flex: 1,					
							listeners: {
								change: function(field, newValue, oldValue, opts) {
									var relationshipWin = field.up('window');							
									var itemSelect = relationshipWin.queryById('itemSelect');
									var store = itemSelect.getStore();
									store.clearFilter();
									store.filterBy(function(record) {
										return Ext.String.startsWith(record.get('text'), newValue, true);
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
					itemId: 'relationType',
					scrollable: true,
					width: '100%',
					store: {						
					},
					itemSelector: 'div.search-tool-relation',			
					tpl: new Ext.XTemplate(
						'<tpl for=".">',					
							'<div class="search-tool-relation">',
								'<span class="fa fa-3x {icon}"></span>',
								'<span class="search-tool-relation-text">{text}</span>',
							'</div>',
						'</tpl>'
					),
					listeners: {
						itemclick: function(dataView, record, item, index, e, eOpts) {	
							
							var cardPanel = dataView.up('panel');
							cardPanel.setActiveItem(cardPanel.queryById('itemSelect'));														
							record.data.loadData();							
						}
					}
				},
				{
					xtype: 'dataview',
					itemId: 'itemSelect',
					scrollable: true,
					width: '100%',
					store: {
						sorters: [{
							property: 'text',
							direction: 'ASC',
							transform: function(value) {
								return value.toLowerCase();
							}
						}]
					},
					itemSelector: 'div.search-tool-relation',			
					tpl: new Ext.XTemplate(
						'<tpl for=".">',					
							'<div class="search-tool-relation">',
								'<table width="100%"><tr>',
									'<td class="search-tool-org-logo" width="120">',							
										'<tpl if="logo">{logo}</tpl>',								
									'</td>',						
									'<td class="search-tool-org-text">',										
										'<span class="search-tool-relation-text">{text}</span>',
									'</td>',
								'</tr></table>',															
							'</div>',
						'</tpl>'
					),
					listeners: {
						itemclick: function(dataView, record, item, index, e, eOpts) {	
							record.data.handler(record);
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
				icon: 'fa-list',
				loadData: function() {
					relationshipSearchView.setLoading(true);
					Ext.Ajax.request({
						url: 'api/v1/resource/components/lookup',
						callback: function(){
							relationshipSearchView.setLoading(false);
						},
						success: function(response, opt){
							var data = Ext.decode(response.responseText);
							
							var itemSelect = relationshipSearchView.queryById('itemSelect');
							
							var views = [];
							Ext.Array.each(data, function(lookup){
								views.push({
									itemId: lookup.code,
									logo: '<div class="search-tool-org-logo-text">'+ Ext.String.capitalize(lookup.description).substring(0, 1) + '</div>',
									text: lookup.description,
									handler: function(record) {
										var params = 'entityId=' + record.get('itemId') + '&entityName=' + encodeURIComponent(record.get('text'));
										window.location.href='UserTool.action?load=Relationships&viewType=RELATION&'+ params;
									}
								});	
							});							
							itemSelect.getStore().loadRawData(views);
						}
					});					
				}
			},
			{
				text: 'Organizations',				
				icon: 'fa-sitemap',
				loadData: function() {
					relationshipSearchView.setLoading(true);
					Ext.Ajax.request({
						url: 'api/v1/resource/organizations?componentOnly=true',
						callback: function(){
							relationshipSearchView.setLoading(false);
						},
						success: function(response, opt){
							var data = Ext.decode(response.responseText);
							
							var itemSelect = relationshipSearchView.queryById('itemSelect');
							
							var views = [];
							Ext.Array.each(data.data, function(org){
								views.push({
									itemId: org.organizationId,
									text: org.name,
									logo: org.logoOriginalFileName ? '<img src="Media.action?OrganizationLogo&organizationId=' + org.organizationId + '" width=100 />' : '<div class="search-tool-org-logo-text">'+ Ext.String.capitalize(org.name).substring(0, 1) + '</div>',
									handler: function(record) {
										var params = 'entityId=' + encodeURIComponent(record.get('itemId')) + '&entityName=' + encodeURIComponent(record.get('text'));
										window.location.href='UserTool.action?load=Relationships&viewType=ORG&'+ params;										
									}
								});	
							});							
							itemSelect.getStore().loadRawData(views);							
						}
					});	
				}				
			},
			{
				text: 'Attributes',
				icon: 'fa-list-alt',
				loadData: function() {
					relationshipSearchView.setLoading(true);
					Ext.Ajax.request({
						url: 'api/v1/resource/attributes/attributetypes',
						callback: function(){
							relationshipSearchView.setLoading(false);
						},
						success: function(response, opt){
							var data = Ext.decode(response.responseText);
							
							var itemSelect = relationshipSearchView.queryById('itemSelect');
							
							var views = [];
							Ext.Array.each(data.data, function(attributeType){
								views.push({
									itemId: attributeType.attributeType,
									text: attributeType.description,
									logo: '<div class="search-tool-org-logo-text">'+ Ext.String.capitalize(attributeType.description).substring(0, 1) + '</div>',
									handler: function(record) {
										var params = 'entityId=' + record.get('itemId') + '&entityName=' + encodeURIComponent(record.get('text'));
										window.location.href='UserTool.action?load=Relationships&viewType=ATT&'+ params;											
									}
								});	
							});							
							itemSelect.getStore().loadRawData(views);							
						}
					});						
				}				
			},
			{
				text: 'Tags',
				icon: 'fa-tags',
				loadData: function() {
					relationshipSearchView.setLoading(true);
					Ext.Ajax.request({
						url: 'api/v1/resource/components/tags',
						callback: function(){
							relationshipSearchView.setLoading(false);
						},
						success: function(response, opt){
							var data = Ext.decode(response.responseText);
							
							var itemSelect = relationshipSearchView.queryById('itemSelect');
							
							var views = [];
							Ext.Array.each(data, function(tag){
								views.push({
									itemId: tag.tagId,
									text: tag.text,
									logo: '<div class="search-tool-org-logo-text">'+ Ext.String.capitalize(tag.text).substring(0, 1) + '</div>',
									handler: function(record) {
										var params = 'entityId=' + record.get('text') + '&entityName=' + encodeURIComponent(record.get('text'));
										window.location.href='UserTool.action?load=Relationships&viewType=TAGS&'+ params;											
									}
								});	
							});							
							itemSelect.getStore().loadRawData(views);								
						}
					});					
				}				
			}
		];
		
		var relationType = relationshipSearchView.queryById('relationType');
		relationType.getStore().loadData(relationshipTypes);
		
		
	}
	
});
