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

Ext.define('OSF.landing.DefaultActions', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-defaultactions',
	
	layout: 'center',	
	items: [
		{
			xtype: 'dataview',
			itemId: 'dataview',
			store: {				
			},
			itemSelector: 'div.search-tool',
			tpl: new Ext.XTemplate(
				'<tpl for=".">',
					'<div style="margin: 15px;" class="action-tool-button-outer search-tool">',
					  '<div class="action-tool-button-inner">',	
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
		var searchToolsPanel = this;
				
		var tools=[
			{
				text: 'Dashboard',
				icon: 'fa-th-large',
				handler: function(record, item) {
					
				}
			},
			{
				text: 'Submissions',
				icon: 'fa-file-text-o',
				permissions: [''],
				handler: function(record, item) {
					
				}
			},
			{
				text: 'Relationships',
				icon: 'fa-share-alt',
				handler: function(record, item) {
					
				}
			},
			{
				text: 'Tools',
				icon: 'fa-gears',
				handler: function(record, item) {
					
				}
			}			
		];
		
		searchToolsPanel.queryById('dataview').getStore().loadData(tools);
	}
	
});

