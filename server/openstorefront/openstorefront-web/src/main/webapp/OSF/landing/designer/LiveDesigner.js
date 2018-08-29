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
		'OSF.landing.*'
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
					columnLines: true,
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
					listeners: {
						selectionchange: function(selectionModel, records, opts ) {	
							var grid = this;
							if (records.length > 0) {
								grid.queryById('delete').setDisabled(false);
							} else {
								grid.queryById('delete').setDisabled(true);								
							}
						}
					},
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
									itemId: 'delete',
									disabled: true,
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
									itemId: 'custom',
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
		
		var icons = [];
		CoreService.iconservice.getAllIcons().then(function(iconClasses){
			icons = iconClasses;
		});
		
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
				renderCode: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {
						layout: 'fit'
					});					
					return commonCodeRender(this, config);							
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
				renderCode: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {
						layout: 'hbox'
					});					
					return commonCodeRender(this, config, {
						flex: 1
					});							
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
				renderCode: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {
						layout: 'vbox'
					});					
					return commonCodeRender(this, config, {
						flex: 1
					});							
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
				renderCode: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {						
					});					
					return commonCodeRender(this, config);							
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
				renderCode: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {						
					});					
					return commonCodeRender(this, config);							
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
				renderCode: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {
						layout: 'center'
					});					
					return commonCodeRender(this, config);							
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
				renderCode: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {
						layout: 'accordion'
					});					
					return commonCodeRender(this, config);							
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
				renderCode: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {
						layout: 'anchor'
					});					
					return commonCodeRender(this, config);							
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
				renderCode: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {
						layout: 'border'
					});					
					return commonCodeRender(this, config);							
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
				data: [
					{
						text: 'Dashboard',
						icon: null,
						tip: 'Access your dashboard',
						imageSrc: 'images/dash.png',
						link: 'UserTool.action?load=Dashboard'
					},
					{
						text: 'Submissions',
						icon: null,
						imageSrc: 'images/submission.png',
						tip: 'Add or update entries to the registry',
						permission: 'USER-SUBMISSIONS-PAGE',
						link: 'UserTool.action?load=Submissions'
					},
					{
						text: 'My Searches',
						icon: null,
						tip: 'View and manage your saved searches',
						imageSrc: 'images/savedsearch.png',
						link: 'UserTool.action?load=Searches'
					},
					{
						text: 'Tools',
						icon: null,
						tip: 'Access user tools to update profile and manage your data.',
						imageSrc: 'images/tools.png',
						link: 'UserTool.action'
					},
					{
						text: 'Feedback',
						icon: null,
						tip: 'Provide feedback about the site',
						imageSrc: 'images/feedback.png',
						link: 'feedback.jsp'
					}									
				],
				customConfigHandler: function(block) {					
					var editWin = Ext.create('Ext.window.Window', {
						title: 'Action Tool Config',
						iconCls: 'fa fa-edit',
						closeAction: 'destroy',
						modal: true,
						width: '80%',
						height: 500,
						layout: 'fit',						
						items: [
							{
								xtype: 'grid',
								itemId: 'grid',
								store: {},
								columnLines: true,
								plugins: [{
									ptype: 'cellediting',
									clicksToEdit: 1,
									id: 'editor'
								}],
								viewConfig: {
									plugins: {
										ptype: 'gridviewdragdrop',
										dragText: 'Drag and drop to reorder'												
									},
									listeners: {
										drop: function(node, data, overModel, dropPostition, opts){													
										}
									}
								},							
								columns: [
									{ text: 'Label', dataIndex: 'text', width: 150,
										editor: 'textfield'
									},
									{ text: 'Tip', dataIndex: 'tip', minWidth: 150, flex: 1,
										editor: 'textfield'
									},
									{ text: 'Icon', dataIndex: 'icon', width: 200,
										editor: {
											xtype: 'combobox',
											valueField: 'cls',
											displayField: 'cls',
											listConfig: {
												getInnerTpl: function(displayField) {
													return '{view}';
												}
											},
											store: {
												data: icons
											}
										}
									},
									{ text: 'Image', dataIndex: 'imageSrc', minWidth: 150, flex: 1,
										editor: 'textfield'
									},
									{
										text: 'Background Css', dataIndex: 'toolBackground', minWidth: 150, flex: 1,
										editor: 'textfield'										
									},
									{ text: 'Link', dataIndex: 'link', minWidth: 150, flex: 1,
										editor: 'textfield'
									}
								]
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
											var grid = editWin.queryById('grid');
											grid.getStore().add({
											});												
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Delete',
										itemId: 'delete',
										iconCls: 'fa fa-trash icon-button-color-warning',
										handler: function() {											
											var grid = editWin.queryById('grid');
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
										iconCls: 'fa fa-check icon-button-color-save',
										handler: function() {
											var win = this.up('window');
											block.data = [];
											editWin.queryById('grid').getStore().each(function(record){
												var data = Ext.clone(record.data);
												delete data.id;
												block.data.push(data);
											});
											win.close();											
											designerPanel.updateAll();
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Cancel',
										iconCls: 'fa fa-close icon-button-color-warning',
										handler: function() {
											this.up('window').close();
										}										
									}
								]
							}
						]
						
					});
					editWin.show();		
					editWin.queryById('grid').getStore().loadRawData(Ext.clone(block.data));
					
				},				
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
				renderCode: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {	
						actionTools: this.data
					});					
					return commonCodeRender(this, config);
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
				acceptCheck: function(info) {
					return false;
				},
				renderCode: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {						
					});					
					return commonCodeRender(this, config);					
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
				renderCode: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {						
					});					
					return commonCodeRender(this, config);					
				}				
			},			
			{
				name: 'Header',
				description: 'Displays the header',
				className: 'OSF.landing.DefaultHeader',
				designerClassName: 'OSF.landing.DefaultHeader',
				config: {},
				data: [],
				customConfigHandler: function(block) {					
					var editWin = Ext.create('Ext.window.Window', {
						title: 'Custom Menu Config',
						iconCls: 'fa fa-edit',
						closeAction: 'destroy',
						modal: true,
						width: '80%',
						height: 500,
						layout: 'fit',						
						items: [
							{
								xtype: 'grid',
								itemId: 'grid',
								store: {},
								columnLines: true,
								plugins: [{
									ptype: 'cellediting',
									clicksToEdit: 1,
									id: 'editor'
								}],
								viewConfig: {
									plugins: {
										ptype: 'gridviewdragdrop',
										dragText: 'Drag and drop to reorder'												
									},
									listeners: {
										drop: function(node, data, overModel, dropPostition, opts){													
										}
									}
								},							
								columns: [
									{ text: 'Text', dataIndex: 'text', width: 150,
										editor: 'textfield'
									},
									{ text: 'Tip', dataIndex: 'tooltip', minWidth: 150, flex: 1,
										editor: 'textfield'
									},
									{ text: 'Icon', dataIndex: 'iconCls', width: 200,
										editor: {
											xtype: 'combobox',
											valueField: 'cls',
											displayField: 'cls',
											listConfig: {
												getInnerTpl: function(displayField) {
													return '{view}';
												}
											},
											store: {
												data: icons
											}
										}
									},									
									{ text: 'Link', dataIndex: 'href', minWidth: 150, flex: 1,
										editor: 'textfield'
									},
									{ text: 'Target', dataIndex: 'hrefTarget', minWidth: 150, flex: 1,
										editor: {
											xtype: 'combobox',
											valueField: 'target',
											displayField: 'target',
											store: {
												data: [
													{
														target: '_blank'
													},
													{
														target: '_self'
													},
													{
														target: '_parent'
													},
													{
														target: '_top'
													}													
												]
											}											
										}
												
									}
								]
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
											var grid = editWin.queryById('grid');
											grid.getStore().add({
											});												
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Delete',
										itemId: 'delete',
										iconCls: 'fa fa-trash icon-button-color-warning',
										handler: function() {											
											var grid = editWin.queryById('grid');
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
										iconCls: 'fa fa-check icon-button-color-save',
										handler: function() {
											var win = this.up('window');
											block.data = [];
											editWin.queryById('grid').getStore().each(function(record){
												var data = Ext.clone(record.data);
												delete data.id;
												block.data.push(data);
											});
											win.close();											
											designerPanel.updateAll();
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Cancel',
										iconCls: 'fa fa-close icon-button-color-warning',
										handler: function() {
											this.up('window').close();
										}										
									}
								]
							}
						]
						
					});
					editWin.show();		
					editWin.queryById('grid').getStore().loadRawData(Ext.clone(block.data));
					
				},	
				items: [],				
				designerRender: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {
						renderLogo: false
					});
					return commonDesignerRender(this, config);					
				},
				acceptCheck: function(info) {
					return false;
				},
				renderCode: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {
						customMenuItems: this.data
					});					
					return commonCodeRender(this, config);					
				}				
			},			
			{
				name: 'Info',
				description: 'Displays highlight and new approved items (Carousel)',
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
				acceptCheck: function(info) {
					return false;
				},
				renderCode: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {						
					});					
					return commonCodeRender(this, config);					
				}				
			},
			{
				name: 'Static Info',
				description: 'Displays highlight and new approved items (Static view)',
				className: 'OSF.landing.StaticInfo',
				designerClassName: 'OSF.landing.StaticInfo',
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
				renderCode: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {						
					});					
					return commonCodeRender(this, config);					
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
				acceptCheck: function(info) {
					return false;
				},
				renderCode: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {						
					});					
					return commonCodeRender(this, config);					
				}				
			},
			{
				name: 'Search Tools',
				description: 'Displays Configured search tools',
				className: 'OSF.landing.DefaultSearchTools',
				designerClassName: 'OSF.landing.DefaultSearchTools',
				config: {},
				data: [
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
				customConfigHandler: function(block) {					
					var editWin = Ext.create('Ext.window.Window', {
						title: 'Search Tool Config',
						iconCls: 'fa fa-edit',
						closeAction: 'destroy',
						modal: true,
						width: '80%',
						height: 500,
						layout: 'fit',						
						items: [
							{
								xtype: 'grid',
								itemId: 'grid',
								store: {},
								columnLines: true,
								plugins: [{
									ptype: 'cellediting',
									clicksToEdit: 1
								}],
								viewConfig: {
									plugins: {
										ptype: 'gridviewdragdrop',
										dragText: 'Drag and drop to reorder'												
									},
									listeners: {
										drop: function(node, data, overModel, dropPostition, opts){													
										}
									}
								},							
								columns: [
									{ text: 'label', dataIndex: 'text', width: 150,
										editor: 'textfield'
									},
									{ text: 'Tip', dataIndex: 'tip', minWidth: 150, flex: 1,
										editor: 'textfield'
									},
									{ text: 'Icon', dataIndex: 'icon', width: 200,
										editor: {
											xtype: 'combobox',
											valueField: 'cls',
											displayField: 'cls',
											listConfig: {
												getInnerTpl: function(displayField) {
													return '{view}';
												}
											},
											store: {
												data: icons
											}
										}
									},
									{ text: 'Tool Type', dataIndex: 'toolType', width: 250,
										editor: {
											xtype: 'combobox',
											valueField: 'classType',
											displayField: 'classType',
											store: {
												data: [
													{
														classType: 'OSF.landing.TagCloud'
													},
													{
														classType: 'OSF.landing.OrganizationSearch'
													},
													{
														classType: 'OSF.landing.RelationshipSearch'
													},
													{
														classType: 'OSF.landing.AdvancedSearch'
													}													
												]
											}
										}
									}
								]
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
											var grid = editWin.queryById('grid');
											grid.getStore().add({
											});												
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Delete',
										itemId: 'delete',
										iconCls: 'fa fa-trash icon-button-color-warning',
										handler: function() {											
											var grid = editWin.queryById('grid');
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
										iconCls: 'fa fa-check icon-button-color-save',
										handler: function() {
											var win = this.up('window');
											block.data = [];
											editWin.queryById('grid').getStore().each(function(record){
												var data = Ext.clone(record.data);
												delete data.id;
												block.data.push(data);
											});
											win.close();											
											designerPanel.updateAll();
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Cancel',
										iconCls: 'fa fa-close icon-button-color-warning',
										handler: function() {
											this.up('window').close();
										}										
									}
								]
							}
						]
						
					});
					editWin.show();		
					editWin.queryById('grid').getStore().loadRawData(Ext.clone(block.data));
					
				},
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
				renderCode: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {
						searchTools: this.data
					});					
					return commonCodeRender(this, config);					
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
				acceptCheck: function(info) {
					return false;
				},
				renderCode: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {						
					});					
					return commonCodeRender(this, config);					
				}				
			},
			{
				name: 'Viewport',
				description: 'Represent the page',
				className: 'Ext.container.Viewport',					
				fixed: true,
				config: {},
				items: [],
				designerRender: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {
						layout: 'border'
					});
					return commonDesignerRender(this, config);
				},
				renderCode: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {
						layout: 'border'
					});					
					return commonCodeRender(this, config);	
				}
			},
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
				renderCode: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {						
					});					
					return commonCodeRender(this, config);	
				}					
			}
			
		];					
		
		designerPanel.allComponents = layoutComponents.concat(availableComponents);
		designerPanel.allComponents.sort(function(a, b){
			return a.name.localeCompare(b.name);
		});
		
		//add rendered stub 
		var componentBlocks = [];
		Ext.Array.each(designerPanel.allComponents, function(item){
			
			if (!item.fixed) {
				var componentPanel = Ext.create('Ext.panel.Panel', {
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
								element: componentPanel.getEl(),
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
				componentBlocks.push(componentPanel);
			}
			
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
				Ext.toast('Applied Properties');				
			};		
						
			if (block.customConfigHandler) {
				propertyGrid.queryById('custom').setDisabled(false);
				propertyGrid.queryById('custom').handler = function(){
					block.customConfigHandler(block);
				};
			} else {
				propertyGrid.queryById('custom').setDisabled(true);
			}
			
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
				closeToolText: 'Delete Section',
				tools: [
					{
						type: 'up',
						tooltip: 'Move this section up (wraps around)',
						hidden: block.fixed ? true : false,
						callback: function (panel) {
							
							//wrap
							var moveBlock = function(blocks) {
								if (Ext.Array.contains(blocks, panel.block)) {
									var index = Ext.Array.indexOf(blocks, panel.block, 0);
									Ext.Array.removeAt(blocks, index);
									var newIndex = index - 1;
									if (newIndex < 0) {
										blocks.push(panel.block);
									} else {
										var newArray = [];
										var originalLength = blocks.length;
										for (var i=0; i<originalLength + 1; i++) {
											if (i === newIndex) {
												newArray.push(panel.block);
											} else {
												newArray.push(blocks.pop());
											}
										}
										//can't reassign as it loses reference; fill the original reference
										Ext.Array.each(newArray, function(item){
											blocks.push(item);
										});
									}
								} else {
									Ext.Array.each(blocks, function(block) {
										moveBlock(block.items);
									});
								}
							};
							moveBlock(designerPanel.components);
							designerPanel.updateAll();
						}
					},
					{
						type: 'gear',
						tooltip: 'configure',
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
			if (block.data) {
				container.loadData(Ext.clone(block.data));
			}
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
		
		
		var commonCodeRender = function(block, config, childConfig) {	
			var itemsToAdd = [];
			Ext.Array.each(block.items, function(childBlock){						
				itemsToAdd.push(childBlock.renderCode(childConfig));
			});	

			var generated = Ext.clone(Ext.apply(config, block.config));

			var configString = '';
			
			var renderConfig = function(configObj) {
				Ext.Object.each(configObj, function(key, value, myself) {					
					if (Ext.isString(value)) {
						configString += key + ": '" + value + "',\n";
					} else if (Ext.isArray(value)) {
						configString += key + ": " + Ext.encode(value)+ ",\n";						
					} else if (Ext.isObject(value))	{				
						configString += key + ": " + Ext.encode(value)+ ",\n";	
					} else {
						configString += key + ": " + value + ",\n";
					}
				});
			};
			renderConfig(generated);			
	
			if (itemsToAdd.length > 0) {
				configString += '\nitems: [\n' + itemsToAdd.join(',\n') + '\n]\n';
			}

			return 'Ext.create(\'' + block.className + '\', {\n' +
					configString + "\n})";
		};
		
		
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
						renderCode: function(config){
							config = config ? config : {};
							config = Ext.apply(config, {						
							});					
							return commonCodeRender(this, config);	
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
				renderCode: function(config){
					config = config ? config : {};
					config = Ext.apply(config, {
						layout: 'border'
					});					
					return commonCodeRender(this, config);	
				}				
			}	
		);			
		
				
	},
	initialize: function() {
		var designerPanel = this;		
		designerPanel.updateAll();
	},
	restoreBlocks: function(loadedConfigBlocks) {
		var designerPanel = this;
		
		if (loadedConfigBlocks) {
			try {
				//restore state and function				
				var restoreBlocks = function(blocks, childblock, parentBlock) {
					Ext.Array.each(blocks, function(blockConfig) {					
					
						Ext.Array.each(designerPanel.allComponents, function(block) {
							if (blockConfig.name === block.name) {

								Ext.applyIf(blockConfig, block);
								
								//make sure ids are still good
								blockConfig.blockId = Ext.id().replace('-', '_');																											
								
							}
						});						
						if (blockConfig.items && blockConfig.items.length > 0) {
							//childblocks
							restoreBlocks(blockConfig.items, true, blockConfig);
						}
					});
				};
				restoreBlocks(loadedConfigBlocks, false);

			} catch(e) {
				Ext.log(e.message);
				Ext.log(e);
			}			
			designerPanel.components = loadedConfigBlocks;
			designerPanel.updateAll();
		}	
		
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
