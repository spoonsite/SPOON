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

Ext.define('OSF.landing.DefaultCategory', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-defaultcategory',
	
	width: '100%',
	titleCollapse: true,
	collapsible: true,
	hideCollapseTool:true,
	header: {
		title: 'Browse Categories',
		titleAlign: 'center'
	},
	items: [
		{
			xtype: 'dataview',
			width: '50%',
			itemId: 'dataview',
			store: {				
			},
			itemSelector: 'div.category-tool',
			tpl: new Ext.XTemplate(
				'<div',	
				'<tpl for=".">',
					'<div style="margin: 15px;" class="search-tool-button-outer category-tool">',
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
		
		
		
	}
	
});
