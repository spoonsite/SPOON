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
	requires: [
		'OSF.landing.DefaultHeader',
		'OSF.landing.DefaultFooter',
		'OSF.landing.DefaultSearch',
		'OSF.landing.DefaultVersion',
		'OSF.landing.DefaultSearchTools',
		'OSF.landing.DefaultActions',
		'OSF.landing.DefaultCategory',
		'OSF.landing.DefaultInfo'
	],
	
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
					collapsible: true,
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
					collapsible: true,
					disabled: true,
					width: '100%',
					title: 'Properties',					
					colunmLines: true,
					flex: 1,
					selModel: 'cellmodel',
					plugins: {
						ptype: 'cellediting',
						clicksToEdit: 1												
					},						
					store: {},
					columns: [
						{ text: 'Key', dataIndex: 'property', flex: 1,
							field: 'textfield'
						},
						{ text: 'Value', dataIndex: 'value', flex: 1,
							field: 'textfield'
						}
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
										var grid = this.up('grid');
										grid.getStore().add({
											property: '',
											value: ''
										});										
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									iconCls: 'fa fa-trash icon-button-color-warning',
									handler: function() {
										var grid = this.up('grid');
										var record = grid.getSelection()[0];																			
										grid.getStore().remove(record);
										grid.applyChanges(grid);
									}									
								}
							]
						},
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [
								{
									text: 'Apply',
									itemId: 'apply',									
									iconCls: 'fa fl-g fa-check icon-button-color-save',
									handler: function() {
										var grid = this.up('grid');
										grid.applyChanges(grid);	
									}
								},								
								{
									xtype: 'tbfill'
								},
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
			layout: 'fit',
			items: []
		}
	],
	initComponent: function () {
		this.callParent();		
		var designerPanel = this;
				
		designerPanel.components = [];
		
		//available layouts
		var layoutComponents = [
			{
				name: 'Layout - Fit',
				layout: true,
				description: 'Fit a single component to size of parent',
				className: 'Ext.panel.Panel',
				config: {},
				items: [],
				designerRender: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {
						layout: 'fit'
					});
					return commonDesignerRender(this, config);					
				},
				renderCode: function(){
					
				}
			},
			{
				name: 'Layout - HBox',
				layout: true,
				description: 'Horizontal layout of items',
				className: 'Ext.panel.Panel',
				config: {},
				items: [],
				designerRender: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {
						layout: 'hbox'
					});
					return commonDesignerRender(this, config, {
						flex: 1
					});					
				},
				renderCode: function(){
					
				}				
			},
			{
				name: 'Layout - VBox',
				layout: true,
				description: 'Vertical layout of items',
				className: 'Ext.panel.Panel',
				config: {},
				items: [],
				designerRender: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {
						layout: 'vbox'
					});
					return commonDesignerRender(this, config, {
						flex: 1
					});						
				},
				renderCode: function(){
					
				}				
			},
			{
				name: 'Layout - Basic',
				layout: true,
				description: 'Basic panel',
				className: 'Ext.panel.Panel',
				config: {},
				items: [],
				designerRender: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {						
					});
					return commonDesignerRender(this, config);						
				},
				renderCode: function(){
					
				}				
			},			
			{
				name: 'Layout - Tab',
				layout: true,
				description: 'Adds a tab panel',
				className: 'Ext.tab.Panel',
				designerClassName: 'Ext.tab.Panel',
				config: {},
				items: [],
				designerRender: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {						
					});
					return commonDesignerRender(this, config);						
				},
				renderCode: function(){
					
				}				
			},
			{
				name: 'Layout - Center',
				layout: true,
				description: '',
				className: 'Ext.panel.Panel',
				config: {},
				items: [],
				designerRender: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {
						layout: 'center'
					});
					return commonDesignerRender(this, config);							
				},
				renderCode: function(){
					
				}				
			},
			{
				name: 'Layout - Accordion',
				layout: true,
				description: '',
				className: 'Ext.panel.Panel',
				config: {},
				items: [],
				designerRender: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {
						layout: 'accordion'
					});
					return commonDesignerRender(this, config);						
				},
				renderCode: function(){
					
				}				
			},
			{
				name: 'Layout - Anchor',
				layout: true,
				description: '',
				className: 'Ext.panel.Panel',
				config: {},
				items: [],
				designerRender: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {
						layout: 'anchor'
					});
					return commonDesignerRender(this, config);							
				},
				renderCode: function(){
					
				}				
			},
			{
				name: 'Layout - Border',
				layout: true,
				description: '',
				className: 'Ext.panel.Panel',
				config: {},
				items: [],
				designerRender: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {
						layout: 'border'
					});
					return commonDesignerRender(this, config);							
				},
				renderCode: function(){
					
				}				
			}			
		];
		
		
		//available Components
		var availableComponents = [
			{
				name: 'Quick Launch',
				description: 'Set of quick launch buttons',
				className: 'OSF.landing.DefaultActions',
				designerClassName: 'OSF.landing.DefaultActions',
				config: {},
				items: [],
				designerRender: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {						
					});
					return commonDesignerRender(this, config);						
				},
				renderCode: function(){
					
				}				
			},
			{
				name: 'Category',
				description: 'Allow user to search based on category',
				className: 'OSF.landing.DefaultCategory',
				designerClassName: 'OSF.landing.DefaultCategory',
				config: {},
				items: [],
				designerRender: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {						
					});
					return commonDesignerRender(this, config);					
				},
				renderCode: function(){
					
				}				
			},
			{
				name: 'Footer',
				description: 'Displays a footer',
				className: 'OSF.landing.DefaultFooter',
				designerClassName: 'OSF.landing.DefaultFooter',
				config: {},
				items: [],
				designerRender: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {						
					});
					return commonDesignerRender(this, config);					
				},
				acceptCheck: function(info) {
					return false;
				},
				renderCode: function(){
					
				}				
			},			
			{
				name: 'Header',
				description: 'Displays the header',
				className: 'OSF.landing.DefaultHeader',
				designerClassName: 'OSF.landing.DefaultHeader',
				config: {},
				items: [],
				designerRender: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {						
					});
					return commonDesignerRender(this, config);					
				},
				renderCode: function(){
					
				}				
			},			
			{
				name: 'Info',
				description: 'Displays highlight and new approved items',
				className: 'OSF.landing.DefaultInfo',
				designerClassName: 'OSF.landing.DefaultInfo',
				config: {},
				items: [],
				designerRender: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {						
					});
					return commonDesignerRender(this, config);					
				},
				renderCode: function(){
					
				}				
			},
			{
				name: 'Search',
				description: 'Displays the searchbar',
				className: 'OSF.landing.DefaultSearch',
				designerClassName: 'OSF.landing.DefaultSearch',
				config: {},
				items: [],
				designerRender: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {						
					});
					return commonDesignerRender(this, config);					
				},
				renderCode: function(){
					
				}				
			},
			{
				name: 'Search Tools',
				description: 'Displays Configured search tools',
				className: 'OSF.landing.DefaultSearchTools',
				designerClassName: 'OSF.landing.DefaultSearchTools',
				config: {},
				items: [],
				designerRender: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {						
					});
					return commonDesignerRender(this, config);						
				},
				renderCode: function(){
					
				}				
			},
			{
				name: 'Version',
				description: 'Display software version',
				className: 'OSF.landing.DefaultVersion',
				designerClassName: 'OSF.landing.DefaultVersion',
				config: {},
				items: [],
				designerRender: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {						
					});
					return commonDesignerRender(this, config);					
				},
				renderCode: function(){
					
				}				
			}
		];					
		
		var allComponents = [];
		allComponents = layoutComponents.concat(availableComponents);
		allComponents.sort(function(a, b){
			return a.name.localeCompare(b.name);
		});
		
		//add rendered stub 
		var componentBlocks = [];
		Ext.Array.each(allComponents, function(item){
			
			var cpmponentPanel = Ext.create('Ext.panel.Panel', {
				xtype: 'panel',
				title: item.name + ' <i class="fa fa-question-circle" data-qtip="' + item.description + '"></i>',				
				header: {
					cls: item.layout ? 'landing-designer-layout' : 'landing-designer-comp'
				},
				componentConfig: Ext.clone(item),
				listeners: {
					afterrender: function(blockPanel, opts) {
						blockPanel.dragSource = new Ext.drag.Source(Ext.apply({
								block: item
							}, {
							element: cpmponentPanel.getEl(),
							proxy: {
								type: 'placeholder',
								cls: 'entry-template-drag-proxy',
								invalidCls: 'entry-template-drag-proxy-invalid',
								validCls: 'entry-template-drag-proxy-valid',
								html: '<b>' + item.name + '</b>'														
							}
						}));												
					},
					destroy: function(blockPanel, opts) {
						if (blockPanel.dragSource) {
							Ext.destroy(blockPanel.dragSource);
						}
					}
				}				
			});
			componentBlocks.push(cpmponentPanel);
			
		});
				
		designerPanel.queryById('uiComponents').add(componentBlocks);
		designerPanel.queryById('uiComponents').updateLayout(true, true);
		
		var configBlock = function(block) {
			var propertyGrid = designerPanel.queryById('properties');
			propertyGrid.setDisabled(false);
			propertyGrid.setTitle('Properties: ' + block.name);
			
			//load config
			var data = [];
			Ext.Object.each(block.config, function(key, value, myself) {
				data.push({
					property: key,
					value: value
				});
			});
			propertyGrid.selectedBlock = block;
			propertyGrid.getStore().loadData(data);
			propertyGrid.applyChanges = function(grid) {
				var config = {};
				grid.getStore().each(function(record){
					config[record.get('property')] = record.get('value');
				});
				grid.selectedBlock.config = config;	
				
				designerPanel.updateAll();
				grid.getStore().commitChanges();
				Ext.toast('Apply Properties');				
			};
			
			
			//check for custom handler
			
			
		};
		
		//add default viewport and body; needs happen after everything is initialized
		var commonDesignerRender = function(block, config, childConfig) {
			config = config ? config : {};
			var designerClassName = block.designerClassName ? block.designerClassName : 'Ext.panel.Panel';
			
			var container = Ext.create(designerClassName, Ext.apply(config, {
				title: block.name + ' <i class="fa fa-question-circle" data-qtip="' + block.description + '"></i>',
				titleAlign: 'center',
				border: true,
				bodyStyle: 'padding: 10px;',
				closable: block.fixed ? false : true,
				block: block,
				tools: [
					{
						type: 'up',
						hidden: block.fixed ? true : false,
						callback: function (panel) {
							//find block and it's array that it's in
							
						}
					},
					{
						type: 'gear',
						callback: function (panel) {
							configBlock(panel.block);							
						}						
					}
				],
				listeners: {
					close: function(panel, opts) {
												
						var removeBlock = function(blocks) {
							if (Ext.Array.contains(blocks, panel.block)) {
								Ext.Array.remove(blocks, panel.block);
							} else {
								Ext.Array.each(blocks, function(block) {
									removeBlock(block.items);
								});
							}
						};
						removeBlock(designerPanel.components);
						designerPanel.updateAll();
					}
				}
			}));
			setupContainerDropTarget(container, block);
			
			Ext.Array.each(block.items, function(child){
				container.add(child.designerRender(childConfig));
			});
			
			
			return container;
		};
		
		var setupContainerDropTarget = function(container, block) {
			container.on('afterrender', function(blockPanel, opts) {
				var targetElement;
				if (!blockPanel.getHeader() || blockPanel.getHeader() === false) {
					targetElement = blockPanel.tab.getEl();
				} else {
					targetElement = blockPanel.getHeader().getEl();
				}
				blockPanel.dragTarget =	new Ext.drag.Target(Ext.apply({
					block: block,
					element: targetElement
				}, {
						accepts: function(info) {
								if (block.acceptCheck) {
									return block.acceptCheck(info);
								} else {
									return true;
								}
						},
						listeners: {												
							drop: function (target, info) {
								
								var newBlock = info.source.block;
								newBlock.blockId = Ext.id().replace('-', '_');
								target.block.items.push(Ext.clone(newBlock));
								designerPanel.updateAll();
								
//								var newBlock = info.source.block;
//								var addChildBlock = function() {
//									newBlock.blockId = Ext.id().replace('-', '_');
//									target.block.blocks.push(newBlock);
//									updateTemplate();
//								};
//
//								if (newBlock.prompt){
//									newBlock.prompt(addChildBlock);
//								} else {
//									addChildBlock();
//								}
							}
						}
					}));									
			});
			container.on('destroy', function(blockPanel, opts) {
				if (blockPanel.dragTarget) {
					Ext.destroy(blockPanel.dragTarget);
				}
			});				
		};
		
		
		var commonCodeRender = function() {			
		};
		
		Ext.defer(function(){
			designerPanel.components.push(
				{
					name: 'Viewport',
					description: 'Represent the page',
					className: 'Ext.container.Viewport',					
					fixed: true,
					config: {},
					items: [
						{
							name: 'Body',
							description: 'Contents of the page',
							className: 'Ext.panel.Panel',
							fixed: true,
							config: {
								region: 'center',
								scrollable: true
							},
							items: [],
							designerRender: function(config){
								config = config ? config : {};
								config = Ext.apply(config, {
									region: 'center',
									scrollable: true
								});
								return commonDesignerRender(this, config);
							},
							renderCode: function(){

							}				
						}							
					],
					designerRender: function(config){
						config = config ? config : {};
						config = Ext.apply(config, {
							layout: 'border'
						});
						return commonDesignerRender(this, config);
					},
					renderCode: function(){

					}				
				}	
			);
			
			
			designerPanel.updateAll();
		}, 100);
				
	},
	updateAll: function() {
		var templatePanel = this;
		templatePanel.designerPanel.updateAll(templatePanel.components);
	},
	updateDesigner: function (componentBlocks) {
		var designerPanel = this;		
		designerPanel.components = componentBlocks;
		
		var visualContent = designerPanel.queryById('visualContent');
		visualContent.removeAll();
		
		var renderedItems = [];
		Ext.Array.each(componentBlocks, function(block){
			renderedItems.push(block.designerRender());
		});
		
		visualContent.add(renderedItems);
	}
	
});
