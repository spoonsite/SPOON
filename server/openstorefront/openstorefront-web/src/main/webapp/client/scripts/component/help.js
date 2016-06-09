/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/* global Ext */



Ext.define('OSF.component.HelpPanel', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.HelpPanel',
	layout: 'border',
	autoLoad: false,
	initComponent: function () {
		this.callParent();
		
		var helpPanel = this;
		helpPanel.helpFlat = [];
		
		helpPanel.navPanel = Ext.create('Ext.panel.Panel', {
			region: 'west',
			title: 'Sections',
			minWidth: 250,
			collapsible: true,
			split: true,
			layout: 'fit',
			bodyStyle: 'background: white;',
			scollable: true,
			items: [
				{
					xtype: 'treepanel',
					itemId: 'tree',
					width: 250,
					store: Ext.create('Ext.data.TreeStore', {		
						rootVisible: false,
						expand: true,
						autoLoad: helpPanel.autoLoad,
						fields: [
							{
                                name: 'text',
                                mapping: function(data){
									return data.helpSection.title;
								}
                            }
						],
						defaultRootText: 'Sections',
						defaultRootProperty: 'childSections',
						proxy: {
							type: 'ajax',
							url: '../api/v1/resource/help'
						},
						listeners: {
							load: function(store, records, successful, operation, node, eOpt) {											
								if (records && records.length > 0) {
									helpPanel.navPanel.getComponent('tree').expandAll();
									helpPanel.navPanel.getComponent('tree').collapseAll();
									helpPanel.contentPanel.update(records[0].get('helpSection'));
								}								
								var response = operation.getResponse();
								if (response && response.responseText){
									var allHelp = Ext.decode(response.responseText);
									var flattenHelp = function(allHelp) {
									  helpPanel.helpFlat.push(allHelp.helpSection);
									   Ext.Array.each(allHelp.childSections, function(section) {
										  flattenHelp(section);
									   });
									};
									flattenHelp(allHelp);
								}
							}							
						}
					}),
					useArrows: true,
					rootVisible: false,
					listeners: {
						beforeselect: function (thetree, therecord, theindex, theOpts) {
							helpPanel.contentPanel.update(therecord.get('helpSection'));
						}
					}					
				}
			],
			dockedItems: [
				{
					xtype: 'toolbar',
					dock: 'top',
					items: [
						{
							text: 'Print',
							iconCls: 'fa fa-print',
							handler: function(){
								var frame = Ext.getDom(helpPanel.getId() + '-frame');
																
								var template = new Ext.XTemplate(
									'<style>',
									'	.help-content{',
									'		background: white;',
									'		font-family: "Helvetica Neue", Helvetica, "Segoe UI", Arial, freesans, sans-serif;',
									'		font-size: 16px;',
									'		line-height: 1.6;',			
									'	}',
									'	@media print {',
									'		.pageBreak {',
									'			page-break-after: always;',
									'		}',
									'	}',
									'</style>',										
									'<tpl for=".">',
									'	<div class="help-content pageBreak">',
									'		<h2 style="text-align: center">{title}</h2>',
									'		<p>{content}</p>',
									'	</div>',
									'</tpl>'
								);
						
								var html = template.apply(helpPanel.helpFlat);
								
								if (Ext.isIE) {
									var helpPrintWin = window.open('about:blank', 'helpPrintPin', 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, width=800, height=600');
									if (!helpPrintWin) {
										Ext.toast('Unable to open help. Check popup blocker.');
									} else {
										helpPrintWin.document.open();
										helpPrintWin.document.write(html);
										helpPrintWin.document.close();
										helpPrintWin.print();
									}
								} else {								
									frame.contentWindow.document.open();
									frame.contentWindow.document.write(html);
									frame.contentWindow.document.close();
									frame.contentWindow.print();
								}
							}
						},
						{
							xtype: 'tbfill'
						},
						{
							iconCls: 'fa fa-expand',
							tooltip: 'Expand All',
							handler: function(){
								helpPanel.navPanel.getComponent('tree').expandAll();
							}							
						},
						{
							iconCls: 'fa fa-compress',
							tooltip: 'Collapse All',
							handler: function(){
								helpPanel.navPanel.getComponent('tree').collapseAll();
							}							
						}						
					]
				},
				{
					xtype: 'panel',					
					items: [
						{
							xtype: 'textfield',
							width: '100%',
							emptyText: 'Search',
							triggers: {
								reset: {
									cls: 'x-form-clear-trigger',
									handler: function() {
										this.reset();
									}
								}
							},							
							listeners: {								
								change: function() {
									var tree = helpPanel.navPanel.getComponent('tree'),
										v,
										matches = 0;

									try {
										tree.store.clearFilter();
										if (this.getValue() && this.getValue() !== ''){
											var rawValue = this.getValue().toLowerCase();	
											
											v = new RegExp(this.getValue(), 'i');
											Ext.suspendLayouts();
											tree.store.filter({
												filterFn: function(node) {
													var children = node.childNodes,
														len = children && children.length;
													 	

														// Visibility of leaf nodes is whether they pass the test.
														// Visibility of branch nodes depends on them having visible children.
														if (!node.get('helpSection').content) {
															node.get('helpSection').content = '';
														}
														var searchText = node.get('text').toLowerCase() + " " +  node.get('helpSection').content.toLowerCase();
														var visible = searchText.indexOf(rawValue) !== -1 ? true : false ;
														var i;

													// We're visible if one of our child nodes is visible.
													// No loop body here. We are looping only while the visible flag remains false.
													// Child nodes are filtered before parents, so we can check them here.
													// As soon as we find a visible child, this branch node must be visible.
													for (i = 0; i < len && !(visible = children[i].get('visible')); i++);

													if (visible && node.isLeaf()) {
														matches++;
													}
													return visible;
												},
												id: 'titleFilter'
											});
											Ext.resumeLayouts(true);
										}
									} catch (e) {
										console.log(e);
										//this.markInvalid('Invalid regular expression');
									}							
								}
							},
							buffer: 250
						}
					]
				}
			]			
		});
		
		helpPanel.contentPanel = Ext.create('Ext.panel.Panel', {
			region: 'center',
			scrollable: true,
			bodyStyle: 'background: white; padding: 10px;',
			tpl: new Ext.XTemplate(
				'<h2 style="text-align: center">{title}</h2>',
				'<p>{content}</p>'
			)
			
		});
		
		helpPanel.add(helpPanel.contentPanel);
		helpPanel.add(helpPanel.navPanel);
		
	},
	
	afterRender: function(){
		this.callParent();
		
		var helpPanel = this;	
		//Add frame for print
		Ext.dom.Helper.append(helpPanel.getEl(), [
			{
				tag:'iframe', 
				id: helpPanel.getId() + '-frame',
				style: 'display: none; visiblity: hidden;'
			}
		]);		
	},
	
	
	loadSections: function(){
		var helpPanel = this;		
		helpPanel.navPanel.getComponent('tree').getStore().load();		
	}
	
});

Ext.define('OSF.component.HelpWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.HelpWindow',
	layout: 'fit',
	title: 'Help',
	iconCls: 'fa fa-lg fa-question-circle',
	
	x: 50,
	y: 70,
	width: '50%',
	height: 500,
	maximizable: true,
	collapsible: true,
	items: [
		Ext.create('OSF.component.HelpPanel', {	
			itemId: 'helpPanel'
		})
	],
	tools: [
		{
			type: 'toggle',
			tooltip: 'Open in new window',
			callback: function(panel, tool, event){
				var helpWin = window.open('help.jsp', 'helpwin');
				if (!helpWin) {
					Ext.toast('Unable to open help. Check popup blocker.');
				} else {
					this.up('window').close();
				}
			}
		}
	],
	listeners: {
		beforeShow: function(helpWin){
			if (!helpWin.loaded) {
				helpWin.getComponent('helpPanel').loadSections();
				helpWin.loaded = true;
			}
		}
	},
	
	initComponent: function () {
		this.callParent();
	}
		
	
});
