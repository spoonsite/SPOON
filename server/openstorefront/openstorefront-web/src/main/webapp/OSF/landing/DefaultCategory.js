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

Ext.define('OSF.landing.DefaultCategory', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-defaultcategory',
	
	width: '100%',
	titleCollapse: true,
	collapsible: true,
	hideCollapseTool:true,
	layout: 'center',
	collapsed: true,	
	cls: 'home-category-block',
	header: {
		title: '<span class="home-category-title">Browse by Category &nbsp;<i class="fa fa-lg fa-caret-down"></i></span>',		
		cls: 'home-category-title-section',		
		padding: 20,
		titleAlign: 'center'
	},
	listeners: {
		expand: function(panel, opts) {
			panel.setTitle('<span class="home-category-title">Browse by Category &nbsp;<i class="fa fa-lg fa-caret-up"></i></span>');
		},
		collapse: function(panel, opts) {
			panel.setTitle('<span class="home-category-title">Browse by Category &nbsp;<i class="fa fa-lg fa-caret-down"></i></span>');
		}		
	},
	items: [
		{
			xtype: 'dataview',
			width: '80%',
			itemId: 'dataview',
			store: {	
				autoLoad: true,
				sorters: [{
					property: 'description',
					direction: 'ASC'
				}],				
				proxy: {
					type: 'ajax',
					url: 'api/v1/resource/attributes?important=true'
				}
			},
			itemSelector: 'div.category-tool',
			tpl: new Ext.XTemplate(				
				'<tpl for=".">',
					'<div class="home-category-section">',
					'	<div class="home-category-header">',
					'		<table style="width: 100%"><tr><td><span class="{icon}"></span></td>',
					'		<td valign="center"><span class="home-nav-item-header">{description}</span></td></tr></table></div>',
					'	<div style="padding: 10px; overflow: auto; height: 165px;" class="home-category-content">',
					'		<ul>',
					'		<tpl for="codes">',
					'			<li><a href="#" class="link" onclick="CoreUtil.pageActions.viewCategories(\'{parent.attributeType}\', \'{code}\');">{label}</a></li>',
					'		</tpl></ul></div>',
					'</div>',
				'</tpl>'
			),
			listeners: {
				itemclick: function(dataView, record, item, index, e, eOpts) {	
					if (record.handler) {
						record.handler(record, item);
					} else {
						Ext.log("Add Handler to item");
					}
				}
			}			
		}
	],	
	initComponent: function () {
		this.callParent();			
		var infoPanel = this;
		
		var viewCategories = function(attributeType, attributeCode) {
			var searchObj = {
				"sortField": null,
				"sortDirection": "ASC",
				"startOffset": 0,
				"max": 2147483647,
				"searchElements": [{
						"searchType": "ATTRIBUTE",
						"field": null,
						"value": null,
						"keyField": attributeType,
						"keyValue": attributeCode,
						"startDate": null,
						"endDate": null,
						"caseInsensitive": false,
						"numberOperation": "EQUALS",
						"stringOperation": "EQUALS",
						"mergeCondition": "OR"  //OR.. NOT.. AND..
					}]
			};

			var searchRequest = {
				type: 'Advance',
				query: searchObj
			};

			CoreUtil.sessionStorage().setItem('searchRequest', Ext.encode(searchRequest));
			window.location.href = 'searchResults.jsp';	
		};		
		CoreUtil.pageActions.viewCategories = viewCategories;
		
		
	}
	
});
