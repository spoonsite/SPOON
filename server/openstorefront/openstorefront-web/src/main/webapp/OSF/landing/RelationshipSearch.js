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
								'<span class="fa fa-3x {icon}"></span>',
								'<span class="search-tool-relation-text">{text}</span>',
							'</div>',
						'</tpl>'
					),
					listeners: {
						itemclick: function(dataView, record, item, index, e, eOpts) {	
							
							
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
									text: lookup.description,
									handler: function(record) {
										
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
									logo: org.logoOriginalFileName ? 'Media.action?OrganizationLogo&organizationId={organizationId}' : null,
									handler: function(record) {
										
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
									handler: function(record) {
										
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
									handler: function(record) {
										
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
