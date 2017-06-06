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

/* global Ext, CoreService */

Ext.define('OSF.landing.designer.LiveDesigner', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.ofs-liveDesigner',
	
	layout: 'border',
	items: [
		{
			region: 'west',
			width: 300,
			split: true,
			layout: {
				type: 'vbox'
			},
			items: [
				{
					xtype: 'panel',
					itemId: 'uiComponents',
					title: 'UI Components',
					width: '100%',
					flex: 1,
					scrollable: true,
					layout: 'anchor',
					items: [						
					]
				},
				{
					xtype: 'grid',
					itemId: 'properties',
					width: '100%',
					title: 'Properties',					
					colunmLines: true,
					flex: 1,
					store: {},
					columns: [
						{ text: 'Key', dataIndex: '', width: 150},
						{ text: 'Value', dataIndex: '', flex: 1}
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Add',
									iconCls: 'fa fa-plus icon-button-color-save',
									handler: function() {
										
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									iconCls: 'fa fa-trash icon-button-color-warning',
									handler: function() {
										
									}									
								}
							]
						},
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [
								{
									text: 'Custom',
									iconCls: 'fa fa-cog icon-button-color-default',
									disabled: true,
									handler: function() {
										
									}
								}
							]
						}
					]
				}
			]
		},
		{
			region: 'center',
			itemId: 'visualContent',
			split: true,
			items: [
				{
					html: 'center'
				}
			]
		}
	],
	initComponent: function () {
		this.callParent();		
		var designerPanel = this;
		
		//available layouts
		designerPanel.components = [];
		
		var layoutComponents = [
			{
				name: 'Layout - Fit',
				description: 'Test',
				className: 'Ext.panel.Panel',
				config: {},
				items: [],
				designerRender: function(){
					
				},
				renderCode: function(){
					
				}
			},
			{
				name: 'Layout - HBox',
				description: '',
				className: 'Ext.panel.Panel',
				config: {},
				items: [],
				designerRender: function(){
					
				},
				renderCode: function(){
					
				}				
			}			
		];
		
		
		//available Components
		var availableComponents = [
		];					
		
		var allComponents = [];
		allComponents = layoutComponents.concat(availableComponents);
		allComponents.sort(function(a, b){
			return a.name.localeCompare(b.name);
		});
		
		//add rendered stub 
		var componentBlocks = [];
		Ext.Array.each(allComponents, function(item){
			componentBlocks.push({
				xtype: 'panel',
				title: item.name,
				toolTip: item.description,
				header: {
					cls: 'landing-designer-comp'
				},
				componentConfig: item				
			});
		});
		console.log(componentBlocks);
		
		designerPanel.queryById('uiComponents').add(componentBlocks);
		designerPanel.queryById('uiComponents').updateLayout(true, true);
		
	},
	loadData: function (branding) {
		
		
	}
	
});
