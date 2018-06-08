<%-- 
/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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

    Document   : entryTemplate
    Created on : Mar 21, 2016, 2:43:11 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
	<stripes:layout-component name="contents">

		<stripes:layout-render name="../../../../layout/adminheader.jsp">		
		</stripes:layout-render>
				
		<script src="scripts/component/templateBlocks.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/mediaViewer.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/relationshipVisualization.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/reviewWindow.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/questionWindow.js?v=${appVersion}" type="text/javascript"></script>
		
		<div style="display:none; visibility: hidden;" id="templateHolder"></div>
		<form id="viewForm" action="Template.action?PreviewTemplate"  method="POST">		
			<input type="hidden" name="templateContents" id="viewContent" />
		</form>
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function(){			
				
				var entryData;
				var templateBlocks = [];				
				var allBasicBlocks = [];
				
				var commonBlockCode = function(config) {
					var block = this;							
					var code = 'var block_' + block.blockId + " = " +  block.childBlock(config) + ";";
					return code;
				};
				
				var commonChildBlock = function(childConfig) {
					var block = this;	
					
					var generated = Ext.clone(block.config);
					Ext.apply(generated, childConfig);
					
					return ' Ext.create(\'' + block.className + '\', ' + Ext.encode(generated) + ")";
				};
				
				var commonLayoutChildBlock = function(config) {
					var block = this;	
					
					var itemsToAdd = [];
					Ext.Array.each(block.blocks, function(childBlock){						
						itemsToAdd.push(childBlock.childBlock(config));
					});	
					
					var generated = Ext.clone(block.config);
					
					var configString = '';
					Ext.Object.each(generated, function(key, value, myself) {
						if (Ext.isString(value)) {
							configString += key + ": '" + value + "',\n";
						} else {
							configString += key + ": " + value + ",\n";
						}
					});
					configString += 'items: [\n' + itemsToAdd.join(',\n') + '\n]\n';
					
					return ' Ext.create(\'' + block.className + '\', {' +
							configString + "})";
				};				
				
				var commonGenerate = function(entryData, config) {
					var block = this;	
					
					var generated = Ext.clone(block.config);
					Ext.apply(generated, config);		
					
					var comp = Ext.create(block.className, generated);
					return comp;
				};
				
				var commonLayoutGenerate = function(entryData, config, childConfig) {
					var layoutBlock = this;
					var container =  commonGenerate.call(layoutBlock, entryData, config);					
					setupContainerDropTarget(container, layoutBlock);
							
					var itemsToAdd = [];
					Ext.Array.each(layoutBlock.blocks, function(childBlock){
						itemsToAdd.push(childBlock.generate(entryData, childConfig));
					});
					container.add(itemsToAdd);
					Ext.defer(function(){
						if (container.setActiveTab) {
							container.setActiveTab(0);
						}
						container.updateLayout(true, true);
					}, 200);

					return container;
				};
				
				
				var predefinedBlocks = [
					{
						name: 'Description',
						className: 'OSF.component.template.Description',
						config: {							
							margin: '0 0 20 0'
						},
						blockCode: function() {
							return commonBlockCode.call(this);
						},
						childBlock: function(config) {
							return commonChildBlock.call(this, config);
						},							
						generate: function(entryData, config) {
							var visualConfig = {
								//title: 'Description'
							};							
							Ext.apply(visualConfig, (config ? config : {}));
							return commonGenerate.call(this, entryData, visualConfig);
						}
					},
					{
						name: 'Media',
						className: 'OSF.component.template.Media',
						config: {							
							margin: '0 0 20 0'
						},												
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config) {							
							return commonChildBlock.call(this, config);
						},						
						generate: function(entryData, config) {
							return commonGenerate.call(this, entryData, config);
						}
					},
					{
						name: 'Dependencies',
						className: 'OSF.component.template.Dependencies',
						config: {							
							margin: '0 0 20 0'
						},												
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config) {							
							return commonChildBlock.call(this, config);
						},						
						generate: function(entryData, config) {
							return commonGenerate.call(this, entryData, config);
						}												
					},
					{
						name: 'DI2E Evaluation Level',
						className: 'OSF.component.template.DI2EEvalLevel',
						config: {							
							margin: '0 0 20 0'
						},												
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config) {							
							return commonChildBlock.call(this, config);
						},						
						generate: function(entryData, config) {
							return commonGenerate.call(this, entryData, config);
						}												
					},
					{
						name: 'Evaluation Summary (Old)',
						className: 'OSF.component.template.EvaluationSummary',
						config: {							
							margin: '0 0 20 0'
						},												
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config) {							
							return commonChildBlock.call(this, config);
						},						
						generate: function(entryData, config) {
							return commonGenerate.call(this, entryData, config);
						}												
					},
					{
						name: 'Resources',
						className: 'OSF.component.template.Resources',
						config: {							
							margin: '0 0 20 0'
						},												
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config) {							
							return commonChildBlock.call(this, config);
						},						
						generate: function(entryData, config) {
							return commonGenerate.call(this, entryData, config);
						}							
					},
					{
						name: 'Contacts',
						className: 'OSF.component.template.Contacts',
						config: {							
							margin: '0 0 20 0'
						},												
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config) {							
							return commonChildBlock.call(this, config);
						},						
						generate: function(entryData, config) {
							return commonGenerate.call(this, entryData, config);
						}												
					},
					{
						name: 'Vitals',
						className: 'OSF.component.template.Vitals',
						config: {							
							margin: '0 0 20 0'
						},												
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config) {							
							return commonChildBlock.call(this, config);
						},						
						generate: function(entryData, config) {
							return commonGenerate.call(this, entryData, config);
						}													
					},
					{
						name: 'Relationship',
						className: 'OSF.component.template.Relationships',
						config: {							
							margin: '0 0 20 0'
						},												
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config) {							
							return commonChildBlock.call(this, config);
						},						
						generate: function(entryData, config) {
							return commonGenerate.call(this, entryData, config);
						}												
					},
					{
						name: 'Reviews',
						className: 'OSF.component.template.Reviews',
						config: {							
							margin: '0 0 20 0'
						},												
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config) {							
							return commonChildBlock.call(this, config);
						},						
						generate: function(entryData, config) {
							return commonGenerate.call(this, entryData, config);
						}												
					},
					{
						name: 'Questions',
						className: 'OSF.component.template.Questions',
						config: {							
							margin: '0 0 20 0'
						},												
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config) {							
							return commonChildBlock.call(this, config);
						},						
						generate: function(entryData, config) {
							return commonGenerate.call(this, entryData, config);
						}															
					},
					{
						name: 'Related By Attributes',
						className: 'OSF.component.template.RelatedAttributes',
						config: {							
							margin: '0 0 20 0'
						},												
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config) {							
							return commonChildBlock.call(this, config);
						},						
						generate: function(entryData, config) {
							return commonGenerate.call(this, entryData, config);
						}												
					},
					{
						name: 'Related By Organization',
						className: 'OSF.component.template.RelatedOrganization',
						config: {							
							margin: '0 0 20 0'
						},												
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config) {							
							return commonChildBlock.call(this, config);
						},						
						generate: function(entryData, config) {
							return commonGenerate.call(this, entryData, config);
						}												
					},
					{
						name: 'Evaluation Version',
						className: 'OSF.component.template.EvaluationVersionSelect',
						config: {							
							margin: '0 0 20 0'
						},												
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config) {							
							return commonChildBlock.call(this, config);
						},						
						generate: function(entryData, config) {
							return commonGenerate.call(this, entryData, config);
						}																		
					},
					{
						name: 'Evaluation Sections - All',
						className: 'OSF.component.template.EvaluationSections',
						config: {							
							margin: '0 0 20 0'
						},												
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config) {							
							return commonChildBlock.call(this, config);
						},						
						generate: function(entryData, config) {
							return commonGenerate.call(this, entryData, config);
						}																					
					},
					{
						name: 'Evaluation Sections - By Title',
						prompt: function(successAction) {
							var sectionBlock = this;
							Ext.Msg.prompt('Section Select', 'Enter Section Title:', function(btn, text){
								if (btn == 'ok'){
									sectionBlock.config.sectionTitle = text;
									successAction();
								}
							});
						},
						className: 'OSF.component.template.EvaluationSectionByTitle',
						config: {							
							margin: '0 0 20 0',
							sectionTitle: ''
						},												
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config) {							
							return commonChildBlock.call(this, config);
						},						
						generate: function(entryData, config) {
							var visualConfig = {
								title: this.config.sectionTitle
							};							
							Ext.apply(visualConfig, (config ? config : {}));
							return commonGenerate.call(this, entryData, visualConfig);
						}													
					},
					{
						name: 'Evaluation Recommendations',
						className: 'OSF.component.template.EvaluationChecklistRecommendation',
						config: {							
							margin: '0 0 20 0'
						},												
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config) {							
							return commonChildBlock.call(this, config);
						},						
						generate: function(entryData, config) {
							return commonGenerate.call(this, entryData, config);
						}					
					},
					{
						name: 'Evaluation Checklist Summary',
						className: 'OSF.component.template.EvaluationChecklistSummary',
						config: {							
							margin: '0 0 20 0'
						},												
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config) {							
							return commonChildBlock.call(this, config);
						},						
						generate: function(entryData, config) {
							return commonGenerate.call(this, entryData, config);
						}												
					},					
					{
						name: 'Evaluation Checklist Details',
						className: 'OSF.component.template.EvaluationChecklistDetail',
						config: {							
							margin: '0 0 20 0'
						},												
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config) {							
							return commonChildBlock.call(this, config);
						},						
						generate: function(entryData, config) {
							return commonGenerate.call(this, entryData, config);
						}																		
					},
					{
						name: 'Evaluation Checklist Scores',
						className: 'OSF.component.template.EvaluationChecklistScores',
						config: {							
							margin: '0 0 20 0'
						},												
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config) {							
							return commonChildBlock.call(this, config);
						},						
						generate: function(entryData, config) {
							return commonGenerate.call(this, entryData, config);
						}																	
					},
					{
						name: 'Layout - Tabs',
						className: 'OSF.component.template.LayoutTab',
						config: {							
							margin: '0 0 20 0'
						},						
						layoutBlock: true,
						blocks: [],
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config) {	
							return commonLayoutChildBlock.call(this, config);
						},							
						generate: function(entryData, config) {
							var visualConfig = {
								title: 'Tab Container'
							};							
							Ext.apply(visualConfig, (config ? config : {}));
							return commonLayoutGenerate.call(this, entryData, visualConfig);							
						}
					},
					{
						name: 'Layout - Scroll',
						className: 'OSF.component.template.LayoutScroll',
						config: {							
							margin: '0 0 20 0'
						},							
						layoutBlock: true,						
						blocks: [],
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config, itemsToAdd) {
							return commonLayoutChildBlock.call(this, config);
						},
						generate: function(entryData, config) {
							var visualConfig = {
								title: 'Scroll Container'
							};							
							Ext.apply(visualConfig, (config ? config : {}));
							return commonLayoutGenerate.call(this, entryData, visualConfig);								
						}
					},
					{
						name: 'Layout - HBox',
						className: 'OSF.component.template.LayoutHbox',
						config: {							
							margin: '0 0 20 0'
						},									
						layoutBlock: true,
						blocks: [],
						blockCode: function(){
							return commonBlockCode.call(this, {
								flex: 1
							});
						},
						childBlock: function(config, itemsToAdd) {	
							return commonLayoutChildBlock.call(this, config);
						},
						generate: function(entryData, config) {
							var visualConfig = {
								title: 'HBox Container'
							};							
							Ext.apply(visualConfig, (config ? config : {}));
							return commonLayoutGenerate.call(this, entryData, visualConfig, {
								flex: 1
							});	
						}
					},					
					{
						name: 'Layout - VBox',
						className: 'OSF.component.template.LayoutVbox',
						config: {							
							margin: '0 0 20 0'
						},							
						layoutBlock: true,
						blocks: [],
						blockCode: function(){
							return commonBlockCode.call(this, {
								flex: 1
							});
						},
						childBlock: function(config, itemsToAdd) {	
							return commonLayoutChildBlock.call(this, config);
						},
						generate: function(entryData, config) {							
							var visualConfig = {
								title: 'VBox Container'
							};							
							Ext.apply(visualConfig, (config ? config : {}));
							return commonLayoutGenerate.call(this, entryData, visualConfig, {
								flex: 1
							});							
						}
					},				
					{
						name: 'Layout - Accordion',
						className: 'OSF.component.template.LayoutAccordion',
						config: {							
							margin: '0 0 20 0'
						},								
						layoutBlock: true,
						blocks: [],
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config, itemsToAdd) {
							return commonLayoutChildBlock.call(this, config);
						},
						generate: function(entryData, config) {
							var visualConfig = {
								title: 'Accordion Container'
							};							
							Ext.apply(visualConfig, (config ? config : {}));
							return commonLayoutGenerate.call(this, entryData, visualConfig);								
						}
					},
					{
						name: 'Layout - Fit',
						className: 'OSF.component.template.LayoutFit',
						config: {							
							margin: '0 0 20 0'
						},							
						layoutBlock: true,
						blocks: [],
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config, itemsToAdd) {
							return commonLayoutChildBlock.call(this, config);
						},
						generate: function(entryData, config) {
							var visualConfig = {
								title: 'LayoutFit Container'
							};							
							Ext.apply(visualConfig, (config ? config : {}));
							return commonLayoutGenerate.call(this, entryData, visualConfig);								
						},
						acceptCheck: function(info) {
							var layoutBlock = this;
							if (layoutBlock.blocks.length == 0) {
								return true;
							} else {
								return false;
							}
						}
					},					
					{
						name: 'Layout - Center',
						className: 'OSF.component.template.LayoutCenter',
						config: {							
							margin: '0 0 20 0'
						},						
						layoutBlock: true,
						blocks: [],
						blockCode: function(){
							return commonBlockCode.call(this);
						},
						childBlock: function(config, itemsToAdd) {
							return commonLayoutChildBlock.call(this, config);
						},
						generate: function(entryData, config) {
							var visualConfig = {
								title: 'LayoutCenter Container'
							};							
							Ext.apply(visualConfig, (config ? config : {}));
							return commonLayoutGenerate.call(this, entryData, visualConfig);								
						},
						acceptCheck: function(info) {
							var layoutBlock = this;
							if (layoutBlock.blocks.length == 0) {
								return true;
							} else {
								return false;
							}
						}
					}
				];
				
				var setupContainerDropTarget = function(container, block) {				
					container.on('afterrender', function(blockPanel, opts) {
						var targetElement;
						if (!blockPanel.getHeader() || blockPanel.getHeader() == false) {
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
										var addChildBlock = function() {
											newBlock.blockId = Ext.id().replace('-', '_');
											target.block.blocks.push(newBlock);
											updateTemplate();
										};

										if (newBlock.prompt){
											newBlock.prompt(addChildBlock);
										} else {
											addChildBlock();
										}
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
				
				var getAllDataField = function(data, output, parentLevel) {
					
					Ext.Object.each(data, function(key, value, myself) {
			
						if (Ext.isArray(value)) {
							parentLevel++;
							Ext.Array.each(value, function(item){	
								output += '<b>' + key + '</b> [ Showing 1 Item ] <br>';
								Ext.Object.each(item, function(key, value, myself) {
										output += '&nbsp; &nbsp; -> <b>' + key + '</b> : ' + Ext.String.ellipsis(value, 150) + '<br>';
								});
								return false;
							});
						} else if (Ext.isObject(value)) {
							parentLevel++;
							//output += getAllDataField(value, output, parentLevel);
						} else {
							var spacer = '';
							for (var i=0; i<parentLevel; i++) {
								spacer += ' ';
							}
													
							output += spacer + '<b>' + key + '</b> : ' + Ext.String.ellipsis(Ext.util.Format.stripTags(value), 150) + '<br>';
						}																
					});
					return output;
				};
				
				var addEditCustomBlock = Ext.create('Ext.window.Window', {
					id: 'addEditCustomBlock',
					title: 'Add/Edit Custom Template Block',
					modal: true,
					width: '60%',
					height: '80%', 
					maximizable: true,
					layout: 'fit',					
					items: [
						{
							xtype: 'form',
							itemId: 'form',
							layout: 'vbox',
							bodyStyle: 'padding: 10px;',
							defaults: {
								labelAlign: 'top',
								labelSeparator: ''
							},
							items: [
								{
									xtype: 'hiddenfield',
									name: 'templateBlockId'
								},
								{
									xtype: 'textfield',
									name: 'name',
									fieldLabel: 'Name <span class="field-required" />',
									maxLength: 255,
									width: '100%',
									allowBlank: false
								},
								{
									xtype: 'textarea',
									name: 'codeBlock',
									fieldLabel: 'Code <span class="field-required" />',
									width: '100%',
									flex: 1,
									maxLength: 65000,
									allowBlank: false,
									value: "Ext.create('OSF.component.template.BaseBlock', {\n" + 
											" margin: '0 0 20 0',\n" + 
											" tpl: '', \n" +							
											" updateHandler: function(entry){ \n" + 
											"   return entry; \n"+
											" } \n"+										
											"});\n"
								}
							],
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										{
											text: 'Save',
											formBind: true,
											iconCls: 'fa fa-lg fa-save icon-button-color-save',
											handler: function(){
												var win = this.up('window');
												var form = this.up('form');
												var data = form.getValues();
												
												var valid = true;
												if (!data.templateBlockId) {
													//check for unique name
													var unique = true;
													Ext.Array.each(allBasicBlocks, function(block) {
														if (data.name === block.name) {
															unique = false;
															return false;
														}
													});
													if (!unique) {
														valid = false;
														Ext.Msg.show({
															title:'Validation',
															message: 'Template block name needs to be unique.',
															buttons: Ext.Msg.OK,
															icon: Ext.Msg.ERROR,
															fn: function(btn) {
															}
														});															
													}
												}
												
												if (valid) {
													var method = 'POST';
													var endUrl = '';
													if (data.templateBlockId) {
														method = 'PUT';
														endUrl = '/' + data.templateBlockId
													}

													CoreUtil.submitForm({
														url: 'api/v1/resource/templateblocks' + endUrl,
														method: method,
														form: form,
														data: data,
														success: function(response, opt) {
															initToolTemplateBlocks();
															win.close();
														}
													});												
												}
											}
										},
										{
											xtype: 'tbfill'
										},
										{
											text: 'Cancel',
											iconCls: 'fa fa-lg fa-close icon-button-color-warning',
											handler: function() {
												this.up('window').close();
											}
										}
									]
								}
							]
						}
					]
				});
				
				var addEditWindow = Ext.create('Ext.window.Window', {
					id: 'addEditWindow',
					title: 'Add/Edit Template',
					iconCls: 'fa fa-edit',
					modal: true,
					width: '80%',
					height: '80%',
					maximizable: true,
					layout: 'fit',
					listeners: {
						show: function() {        
							this.removeCls("x-unselectable");    
						}
					},					
					items: [
						{
							xtype: 'panel',
							layout: {
								type: 'hbox',
								align: 'stretch'
							},
							items: [
								{
									xtype: 'panel',
									title: 'Toolbox',
									width: '30%',
									collapsible: true,
									titleCollapse: true,
									collapseDirection: 'left',
									split: true,									
									bodyStyle: 'padding: 10px;',
									layout: 'fit',
									items: [
										{
											xtype: 'tabpanel',
											minTabWidth: 100,
											items: [
												{
													xtype: 'panel',	
													title: 'Template Blocks',
													scrollable: true,
													items: [
														{
															xtype: 'panel',
															id: 'blocksPanel',																										
															width: '100%',																										
															bodyStyle: 'padding: 10px;',
															layout: 'anchor',
															border: true															
														}													
													],
													dockedItems: [
														{
															xtype: 'toolbar',
															dock: 'top',
															items: [
																{
																	text: 'Add Custom',
																	iconCls: 'fa fa-lg fa-plus icon-button-color-save',
																	handler: function() {
																		addEditCustomBlock.show();
																		addEditCustomBlock.getComponent('form').reset();																
																	}
																}
															]
														}
													]
												},
												{
													xtype: 'panel',
													title: 'Data',
													layout: 'fit',
													dockedItems: [
														{
															xtype: 'combobox',
															dock: 'top',
															fieldLabel: 'Sample Data',
															labelAlign: 'top',
															name: 'entry',		
															width: '100%',
															queryMode: 'remote',
															editable: false,
															emptyText: 'Select Entry',
															displayField: 'description',											
															valueField: 'code',
															store: {
																proxy: {
																	type: 'ajax',
																	url: 'api/v1/resource/components/lookup'
																}
															},
															listeners: {
																change: function(cb, newValue, oldValue, opts) {

																	if (newValue) {
																		Ext.getCmp('dataFieldPanel').setLoading(true);
																		Ext.Ajax.request({
																			url: 'api/v1/resource/components/' + newValue + '/detail',
																			callback: function(){
																				Ext.getCmp('dataFieldPanel').setLoading(false);
																			},
																			success: function(response, opts){
																				entryData = Ext.decode(response.responseText);

																				var fieldDisplay = getAllDataField(entryData, '', 0);
																				Ext.getCmp('dataFieldPanel').update(fieldDisplay);
																				updateTemplate();
																			}
																		});
																	}

																}
															}
														}															
														
													],
													items: [														
														{
															xtype: 'panel',
															id: 'dataFieldPanel',
															title: 'Data Fields',
															border: true,
															scrollable: true,
															width: '100%',															
															bodyStyle: 'padding: 10px;',
															margin: '0 0 10 0'											
														}														
													]
												},
												{
													xtype: 'panel',	
													itemId: 'blockConfigPanel',
													title: 'Block Config',													
													bodyStyle: 'padding: 10px',
													layout: 'fit',
													reset: function() {
														this.queryById('grid').getStore().removeAll();
														this.queryById('blockSelect').clearValue();
														this.queryById('addProperty').setDisabled(true);
														this.queryById('apply').setDisabled(true);
													},
													items: [
														{
															xtype: 'grid',
															itemId: 'grid',
															tooltip: 'Block Properties; Click to Edit',
															columnLines: true,
															frame: true,
															selModel: 'cellmodel',
															plugins: {
																ptype: 'cellediting',
																clicksToEdit: 1												
															},	
															store: {
																listeners: {
																	update: function(store, record, operation, modifiedFieldNames, details, eOpts) {																		
																		
																	},
																	remove: function(store, records, index, isMove, eOpts) {
																		
																	}
																} 																
															},
															columns: [
																{ text: 'Property', dataIndex: 'property', flex: 1,
																	field: 'textfield'
																},
																{ text: 'Value', dataIndex: 'value', flex: 1,
																	field: 'textfield'
																},
																{
																	text: 'Action', 
																	dataIndex: '',
																	sortable: false,
																	xtype: 'widgetcolumn',
																	align: 'center',
																	width: 50,               
																	widget: {
																		xtype: 'button',
																		iconCls: 'fa fa-lg fa-trash',
																		maxWidth: 25,						   
																		handler: function() {	
																			var record = this.getWidgetRecord();
																			var grid = this.up('grid');
																			grid.getStore().remove(record);
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
																			xtype: 'combo',
																			itemId: 'blockSelect',
																			emptyText: 'Select block',
																			editable: false,
																			forceSelection: true,
																			valueField: 'blockId',
																			displayField: 'name',
																			width: 225,
																			store: {																				
																			},
																			listeners: {																			
																				change: function(field, newValue, oldValue, opts) {
																					var grid = field.up('grid');
																					var selectedBlock = field.getSelection();
																					
																					if (selectedBlock && selectedBlock.data.config) {
																						var props = [];
																						Ext.Object.each(selectedBlock.data.config, function(key, value, myself) {
																							props.push({
																								property: key,
																								value: value
																							});
																						});
																						grid.getStore().loadData(props);
																						grid.selectedBlock = selectedBlock.data;
																						
																						grid.queryById('addProperty').setDisabled(false);
																						grid.queryById('apply').setDisabled(false);																						
																					} else {
																						grid.queryById('addProperty').setDisabled(true);
																						grid.queryById('apply').setDisabled(true);
																					}
																				}
																			}
																		},
																		{
																			text: 'Add Property',
																			itemId: 'addProperty',
																			disabled: true,
																			iconCls: 'fa fa-lg fa-plus icon-button-color-save',
																			handler: function() {
																				var grid = this.up('grid');
																				grid.getStore().add({
																					property: '',
																					value: ''
																				});																				
																			}
																		}
																	]
																},
																{
																	xtype: 'toolbar',
																	dock: 'bottom',
																	items: [
																		{
																			xtype: 'tbfill'
																		},
																		{
																			text: 'Apply',
																			itemId: 'apply',
																			disabled: true,
																			iconCls: 'fa fl-g fa-check icon-button-color-save',
																			handler: function() {
																				var grid = this.up('grid');
																				
																				var config = {};
																				grid.getStore().each(function(record){
																					config[record.get('property')] = record.get('value');
																				});
																				grid.selectedBlock.config = config;																				
																				updateTemplate();
																			}
																		},
																		{
																			xtype: 'tbfill'
																		}
																	]
																}
															]															
														}
													]													
												}
											]
										}										
									]
								},
								{
									xtype: 'tabpanel',
									id: 'templateTabpanel',
									flex: 1,
									border: true,
									split: true,
									tabBar: {
										style: 'padding-top: 0px;'
									},
									items: [
										{
											xtype: 'panel',
											id: 'visualPanel',
											layout: 'fit',																					
											title: 'Visual Design'											
										},
										{
											xtype: 'panel',
											id: 'codePanel',
											bodyStyle: 'padding: 10px;',
											scrollable: true,
											title: 'Code',
											layout: 'vbox',
											defaults: {
												labelAlign: 'top',
												labelSeparator: ''
											},
											items: [
												{
													xtype: 'textareafield',
													itemId: 'precode',
													name: 'precode',
													split: true,
													fieldLabel: 'Pre',
													width: '100%',
													flex: 1													
												},
												{
													xtype: 'textareafield',
													itemId: 'gencode',
													name: 'gencode',
													split: true,
													fieldLabel: 'Generated',
													readOnly: true,
													fieldCls: 'generated-code',
													width: '100%',
													flex: 1													
												},
												{
													xtype: 'textareafield',
													itemId: 'postcode',
													name: 'postcode',
													split: true,
													fieldLabel: 'Post',
													width: '100%',
													flex: 1													
												}												
											],
											dockedItems: [
												{
													xtype: 'toolbar',
													dock: 'top',
													items: [
														{
															text: 'Update Preview',
															iconCls: 'fa fa-lg fa-refresh icon-button-color-refresh',
															handler: function() {
																updatePreview();
															}
														}
													]
												}
											]
										},
										{
											xtype: 'panel',
											id: 'previewPanel',
											layout: 'fit',
											bodyStyle: 'padding: 5px;',
											title: 'Preview'											
										}										
									]
								}
							]
						}
					],
					dockedItems: [
						{
							xtype: 'textfield',							
							id: 'templateNameField',
							dock: 'top',
							name: 'name',
							fieldLabel: 'Name<span class="field-required" />',
							allowBlank: false,							
							margin: '10 10 10 10',
							maxLength: 255
						},
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [
								{
									text: 'Save/Continue',
									scale: 'medium',
									iconCls: 'fa fa-lg fa-save icon-button-color-save icon-small-vertical-correction',
									handler: function(){
										var precode = Ext.getCmp('codePanel').getComponent('precode').getValue();
										var gencode = Ext.getCmp('codePanel').getComponent('gencode').getValue();
										var postcode = Ext.getCmp('codePanel').getComponent('postcode').getValue();
										var fullCode = precode + '\n' + gencode + '\n' + postcode;
										var templateName = Ext.getCmp('templateNameField').getValue();
										
										var valid = true;
										if (!templateName) {
											valid = false;
											Ext.Msg.show({
												title:'Validation',
												message: 'Missing template name.',
												buttons: Ext.Msg.OK,
												icon: Ext.Msg.ERROR,
												fn: function(btn) {
												}
											});	
										}
										if (fullCode === '\n\n\n') {
											valid = false;
											Ext.Msg.show({
												title:'Validation',
												message: 'Add template blocks or custom code.',
												buttons: Ext.Msg.OK,
												icon: Ext.Msg.ERROR,
												fn: function(btn) {
												}
											});	
										}										
										
										if (valid) {											
											var visualState = Ext.encode(templateBlocks);
											
											
											var method = 'POST';
											var endUrl = '';
											if (addEditWindow.editTemplateId) {
												method = 'PUT';
												endUrl = '/' + addEditWindow.editTemplateId;
											}
											
											Ext.getCmp('addEditWindow').setLoading("Saving...");
											Ext.Ajax.request({
												url: 'api/v1/resource/componenttypetemplates' + endUrl,
												method: method,
												jsonData: {
													name: templateName,
													template: fullCode,
													preTemplateCode: precode,
													postTemplateCode: postcode,
													templateBlocks: visualState
												},
												callback: function(){
													Ext.getCmp('addEditWindow').setLoading(false);
												},
												success: function(response, opts) {
													var template = Ext.decode(response.responseText);
													addEditWindow.editTemplateId = template.templateId;
															
													actionRefresh();
													Ext.toast("Saved Template Successfully.");
													//Ext.getCmp('addEditWindow').close();
												}												
											});		
											
										}
										
									}
								},															
								{
									xtype: 'tbfill'
								},
								{
									text: 'Close',
									scale: 'medium',
									iconCls: 'fa fa-lg fa-close icon-button-color-warning icon-small-vertical-correction',
									handler: function(){
										var win = this.up('window');
										win.close();
									}
								}								
							]
						}
					]					
				});
				
				
				//Load Template block
				var initToolTemplateBlocks = function() {
					var toolBox = Ext.getCmp('blocksPanel');
					
					toolBox.setLoading(true);
					Ext.Ajax.request({
						url: 'api/v1/resource/templateblocks',
						callback: function(){
							toolBox.setLoading(false);
						},
						success: function(response, opts){
							var data = Ext.decode(response.responseText);
							if (data) {
								var allBlocks = [];
								allBasicBlocks = Ext.Array.clone(predefinedBlocks);
								Ext.Array.each(data, function(block){
									var newBlock ={
										name: block.name,
										templateBlock: block,										
										blockCode: function(){
											return 'var block_' + this.blockId + " = " + block.codeBlock;
										},
										childBlock: function(config) {											
										},
										generate: function(entryData) {
											var tempBlock = eval(block.codeBlock);														
											return tempBlock;
										}
									};
									allBasicBlocks.push(newBlock);
								});
								allBasicBlocks.sort(function(a, b) {
									return a.name.localeCompare(b.name);
								});
								
								Ext.Array.each(allBasicBlocks, function(block){
									
									var tools = [];
									if(block.templateBlock) {
										tools.push({
											type: 'gear',
											tooltip: 'Edit',
											callback: function(panel, tool, event) {
												var record = Ext.create('Ext.data.Model',{});
												record.set(panel.block.templateBlock);
												
												addEditCustomBlock.show();
												addEditCustomBlock.getComponent('form').reset();
												addEditCustomBlock.getComponent('form').loadRecord(record);
											}											
										});
										tools.push({
											type: 'close',
											tooltip: 'Delete',
											callback: function(panel, tool, event) {
												var templateBlockId = panel.block.templateBlock.templateBlockId;
												Ext.Msg.show({
													title:'Delete Template Block?',
													iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
													message: 'Are you sure you want to delete the block: ' + panel.block.templateBlock.name + '?',
													buttons: Ext.Msg.YESNO,
													icon: Ext.Msg.QUESTION,
													fn: function(btn) {
														if (btn === 'yes') {
															Ext.getCmp('blocksPanel').setLoading("Deleting...");
															Ext.Ajax.request({
																url: 'api/v1/resource/templateblocks/' + templateBlockId,
																method: 'DELETE',
																callback: function(){
																	Ext.getCmp('blocksPanel').setLoading(false);
																},
																success: function(){
																	initToolTemplateBlocks();
																}
															});
														} 
													}
												});	
											}
										});
									}
									
									tools.push({
										type: 'plus',
										tooltip: 'Add Block',
										callback: function(panel, tool, event) {											
											var newBlock = Ext.clone(panel.block);
											
											var addBlock = function() {
												newBlock.blockId = Ext.id().replace('-', '_');
												templateBlocks.push(newBlock);
												updateTemplate();												
											};
											
											if (newBlock.prompt){
												newBlock.prompt(addBlock);
											} else {
												addBlock();
											}
										}										
									});
									
									var panel = Ext.create('Ext.panel.Panel', {
										title: block.name,
										block: Ext.clone(block),										
										header: {
											cls: block.layoutBlock ? 'entry-template_block-layout' : 'entry-template_block' 
										},
										margin: '0 0 5 0',
										tools: tools,
										listeners: {
											afterrender: function(blockPanel, opts) {
												blockPanel.dragSource = new Ext.drag.Source(Ext.apply({
														block: block
													}, {
													element: panel.getEl(),
													proxy: {
														type: 'placeholder',
														cls: 'entry-template-drag-proxy',
														invalidCls: 'entry-template-drag-proxy-invalid',
														validCls: 'entry-template-drag-proxy-valid',
														html: '<b>' + block.name + '</b>'														
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
									
									

									
									allBlocks.push(panel);
								});

								toolBox.removeAll();
								toolBox.add(allBlocks);
								reloadTemplateBlocks();
							}
						}
					});
					

				};								
				initToolTemplateBlocks();
				
				
				var reloadTemplateBlocks = function() {
					
					var refreshedBlocks = [];					
					Ext.Array.each(templateBlocks, function(existingBlock) {
						Ext.Array.each(allBasicBlocks, function(block) {
							if (existingBlock.name === block.name) {
								var newBlock = Ext.clone(block);
								newBlock.blockId = Ext.id().replace('-', '_');
								refreshedBlocks.push(newBlock);							
							}
						});						
					});					
					templateBlocks = refreshedBlocks;
					updateTemplate();
				}
				
				var updateTemplate = function() {
					updateVisual();
					updateCode();
					updatePreview();
					updateBlockConfig();
				};
				
				var updateBlockConfig = function() {
					var allBlocks = [];
					var getBlocks = function(blocks) {
						Ext.Array.each(blocks, function(block){
							if (block.blocks) {
								getBlocks(block.blocks);
							}
							allBlocks.push(block);
						});
					};
					getBlocks(templateBlocks);
					
					var blockConfigPanel = addEditWindow.queryById('blockConfigPanel');
					var blockSelect = blockConfigPanel.queryById('blockSelect');
					var configGrid = blockConfigPanel.queryById('grid');
					
					blockSelect.getStore().loadData(allBlocks);			
					blockSelect.clearValue();
					configGrid.getStore().removeAll();					
					
				};
				
				var updateVisual = function() {
					
					var visualPanels = [];					
					Ext.Array.each(templateBlocks, function(block, index){
						if (block) {
							var templateBlockPanel = block.generate();
							
							if (entryData) {
								if (templateBlockPanel.updateTemplate) {
									templateBlockPanel.updateTemplate(entryData);
								}
							}
							var tools = [];
							var moveUpTool = {
								type: 'plus',
								tooltip: 'Move Up',
								callback: function(panel, tool, event) {
									if (templateBlocks.length > 1) {
										var blockIndex = 0; 
										Ext.Array.each(templateBlocks, function(item, index) {
											if (panel.block.blockId === item.blockId) {
												blockIndex = index;
												return false;
											}
										});
										var temp = templateBlocks[blockIndex];
										templateBlocks[blockIndex] = templateBlocks[blockIndex-1];
										templateBlocks[blockIndex-1] = temp;
										updateTemplate();
									}
								}
							};

							var moveDownTool = {
								type: 'minus',
								tooltip: 'Move Down',
								callback: function(panel, tool, event) {
									if (templateBlocks.length > 1) {
										var blockIndex = 0; 
										Ext.Array.each(templateBlocks, function(item, index) {
											if (panel.block.blockId === item.blockId) {
												blockIndex = index;
												return false;
											}
										});
										var temp = templateBlocks[blockIndex];
										templateBlocks[blockIndex] = templateBlocks[blockIndex+1];
										templateBlocks[blockIndex+1] = temp;
										updateTemplate();
									}
								}
							}

							if (index == 0) {
								tools.push(moveDownTool);							
							} else if (index === templateBlocks.length - 1){
								tools.push(moveUpTool);
							} else {
								tools.push(moveUpTool);
								tools.push(moveDownTool);	
							}

							tools.push({
								type: 'close',
								tooltip: 'Delete',							
								callback: function(panel, tool, event) {
									Ext.Array.remove(templateBlocks, panel.block);
									updateTemplate();
									addEditWindow.queryById('blockConfigPanel').reset();
								}							
							});

							var wrapperPanel = Ext.create('Ext.panel.Panel', {
								title: block.name,
								collapsible: true,
								block: block,
								header: {
									cls: block.layoutBlock ? 'entry-template_block-layout' : 'entry-template_block' 
								},
								margin: '0 0 5 0',
								bodyStyle: 'padding: 5px;',
								border: true,
								tools: tools,							
								items: [
									templateBlockPanel
								],
								listeners: {
									afterrender: function(wPanel, opts) {
										wPanel.dragTarget =	new Ext.drag.Target(Ext.apply({
											 block: block
											}, {
												accepts: function(info) {
													if (!block.layoutBlock) {
														return false;
													}
													if (block.acceptCheck) {
														return block.acceptCheck(info);
													} else {
														return true;
													}
												},												
												element: wPanel.getHeader().getEl(),
												listeners: {													
													drop: function (target, info) {
														return templateBlockPanel.dragTarget.fireEvent('drop', target, info);
													}
												}
											}
										));												
									},
									destroy: function(wPanel, opts) {
										if (wPanel.dragTarget) {
											Ext.destroy(wPanel.dragTarget);
										}
									}
								}
							});
							visualPanels.push(wrapperPanel);
						}
					});					
					Ext.getCmp('visualPanel').removeAll();
					Ext.getCmp('visualPanel').add(visualPanels);					
					
				};				
				
				var updateCode = function() {
					
					var generatedCode = '';				
					var addBlockCode = '\n';
					Ext.Array.each(templateBlocks, function(block, index){
						generatedCode += block.blockCode() + "\n";						
						addBlockCode += 'template.blocks.push(block_' + block.blockId + '); \n';
					});
					
					Ext.getCmp('codePanel').getComponent('gencode').setValue(generatedCode + addBlockCode);
				};
				
				var updatePreview = function() {
					var viewContent = Ext.getDom('viewContent');
					viewContent.value = Ext.getCmp('codePanel').getComponent('precode').getValue() + '\n' +
										Ext.getCmp('codePanel').getComponent('gencode').getValue() + '\n' +
										Ext.getCmp('codePanel').getComponent('postcode').getValue() + '\n';
					
					if (viewContent.value && 
							viewContent.value !== '' &&
							viewContent.value !== '\n') {					
						var previewPanel = Ext.getCmp('previewPanel');
						previewPanel.setLoading(true);
						Ext.Ajax.request({
							url: 'Template.action?PreviewTemplate',
							method: 'POST',
							form: 'viewForm',
							callback: function(){
								previewPanel.setLoading(false);
							},										
							success: function(response, opt) {
								var text = response.responseText;											
								Ext.dom.Element.get("templateHolder").setHtml(text, true, function(){
									try {
										if(template !== undefined && template) {
											template.refresh(previewPanel, entryData ? entryData : {});
										} 
									} catch (e){}
								});
							}
						});	
					} else {
						Ext.getCmp('previewPanel').removeAll();
					}
		
				};				
				
				var templateGrid = Ext.create('Ext.grid.Panel', {
					id: 'templateGrid',
					title: 'Entry Templates <i class="fa fa-question-circle"  data-qtip="Allows for defining custom templates for entries" ></i>',
					store: {
						fields: [
							{
								name: 'createDts',
								type:	'date',
								dateFormat: 'c'
							},								
							{
								name: 'updateDts',
								type:	'date',
								dateFormat: 'c'
							}							
						],						
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/componenttypetemplates',
							extraParams: {
								all: true
							}							
						}						
					},
					columnLines: true,
					columns: [						
						{ text: 'Name', dataIndex: 'name', flex: 1, minWidth: 200 },
						{ text: 'Active Status', align: 'center', dataIndex: 'activeStatus', width: 150 },
						{ text: 'Create User', dataIndex: 'createUser', width: 150, hidden: true },
						{ text: 'Create Date', dataIndex: 'createDts', width: 150, hidden: true, xtype: 'datecolumn', format:'m/d/y H:i:s' },						
						{ text: 'Update User', dataIndex: 'updateUser', width: 150 },
						{ text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' }
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										actionRefresh();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Add',
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction',
									requiredPermissions: ['ADMIN-ENTRY-TEMPLATES-CREATE'],
									handler: function () {
										actionAdd();
									}
								}, 								
								{
									text: 'Edit',
									id: 'lookupGrid-tools-edit',
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
									disabled: true,
									requiredPermissions: ['ADMIN-ENTRY-TEMPLATES-UPDATE'],
									handler: function () {
										actionEdit(Ext.getCmp('templateGrid').getSelection()[0]);
									}								
								},
								{
									requiredPermissions: ['ADMIN-ENTRY-TEMPLATES-UPDATE'],
									xtype: 'tbseparator'
								},								
								{
									text: 'Toggle Status',
									id: 'lookupGrid-tools-status',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-power-off icon-button-color-default icon-vertical-correction',
									disabled: true,
									requiredPermissions: ['ADMIN-ENTRY-TEMPLATES-UPDATE'],
									handler: function () {
										actionToggleStatus();
									}								
								},
								{
									requiredPermissions: ['ADMIN-ENTRY-TEMPLATES-DELETE'],
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									id: 'lookupGrid-tools-delete',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									disabled: true,
									requiredPermissions: ['ADMIN-ENTRY-TEMPLATES-DELETE'],
									handler: function () {
										actionDelete(Ext.getCmp('templateGrid').getSelection()[0]);
									}								
								}								
							]
						}
					],
					listeners: {
						itemdblclick: function(grid, record, item, index, e, opts){
							actionEdit(record);
						},
						selectionchange: function(grid, record, index, opts){
							checkEntryGridTools();
						}
					}					
				});
				
				addComponentToMainViewPort(templateGrid);				

				var checkEntryGridTools = function() {
					if (Ext.getCmp('templateGrid').getSelectionModel().getCount() === 1) {
						Ext.getCmp('lookupGrid-tools-edit').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-status').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-delete').setDisabled(false);					
					} else {
						Ext.getCmp('lookupGrid-tools-edit').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-status').setDisabled(true);	
						Ext.getCmp('lookupGrid-tools-delete').setDisabled(true);
					}
				};
				
				var actionRefresh = function() {
					Ext.getCmp('templateGrid').getStore().reload();
				};

				var actionAdd = function() {
					addEditWindow.show();	
					addEditWindow.queryById('blockConfigPanel').reset();
					
					Ext.getCmp('templateTabpanel').getLayout().setActiveItem(0);
					addEditWindow.editTemplateId = null;
					Ext.getCmp('codePanel').getComponent('precode').reset();
					Ext.getCmp('codePanel').getComponent('postcode').reset();					
					Ext.getCmp('templateNameField').reset();						
					templateBlocks = [];
					updateTemplate();
				};

				var actionEdit = function(record) {
					addEditWindow.show();
					
					addEditWindow.editTemplateId = record.get('templateId');
					addEditWindow.queryById('blockConfigPanel').reset();
					
					Ext.getCmp('codePanel').getComponent('precode').setValue(record.get('preTemplateCode'));
					Ext.getCmp('codePanel').getComponent('postcode').setValue(record.get('postTemplateCode'));					
					Ext.getCmp('templateNameField').setValue(record.get('name'));					
					
					templateBlocks = [];
					var blockList = record.get('templateBlocks');
					if (blockList) {
						try {
							//restore state and function							
							var blocks = Ext.decode(blockList);
							
							var restoreBlocks = function(blocks, childblock) {
								Ext.Array.each(blocks, function(blockConfig) {
									if (blockConfig.blocks) {
										//childblocks
										restoreBlocks(blockConfig.blocks, true);
									}
									Ext.Array.each(allBasicBlocks, function(block) {
										if (blockConfig.name === block.name) {

											Ext.applyIf(blockConfig, block);				

											//make sure ids are still good
											blockConfig.blockId = Ext.id().replace('-', '_');																			
											if (!childblock) {
												templateBlocks.push(blockConfig);
											}
										}
									});						
								});
							};
							restoreBlocks(blocks, false);
							
						} catch(e) {
							Ext.log(e.message);
							Ext.log(e);
						}
					}					
					updateTemplate();					
				
				 };				
				
				var actionToggleStatus = function() {
					Ext.getCmp('templateGrid').setLoading("Updating Status...");
					var type = Ext.getCmp('templateGrid').getSelection()[0].get('templateId');
					var currentStatus = Ext.getCmp('templateGrid').getSelection()[0].get('activeStatus');

					var method = 'PUT';
					var urlEnd = '/activate';
					if (currentStatus === 'A') {
						method = 'DELETE';
						urlEnd = '';
					}					
					Ext.Ajax.request({
						url: 'api/v1/resource/componenttypetemplates/' + type + urlEnd,
						method: method,
						callback: function(){
							Ext.getCmp('templateGrid').setLoading(false);
						},
						success: function(response, opts){
							actionRefresh();
						}
					});					
				};				
				
				var actionDelete = function(record){
					
					//check if delete is possible
					Ext.getCmp('templateGrid').setLoading("Checking delete...");
					Ext.Ajax.request({
						url: 'api/v1/resource/componenttypetemplates/' + record.get('templateId') + '/attached',
						method: 'GET',
						callback: function(){
							Ext.getCmp('templateGrid').setLoading(false);
						},
						success: function(response, opts){
							var attachedRecords = Ext.decode(response.responseText);
							if (attachedRecords.length > 0) {
								var entryTypeNames = [];
								Ext.Array.each(attachedRecords, function(entryType) {
									entryTypeNames.push(entryType.label);
								});
								Ext.Msg.alert('Template in Use', 'The template is being use by the following entry type(s):<br> ' + entryTypeNames.join());								
								
							} else {							
								Ext.Msg.show({
									title:'Delete Template?',
									iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
									message: 'Are you sure you want to delete ' + Ext.util.Format.ellipsis(record.get('name'), 20) + '?',
									buttons: Ext.Msg.YESNOCANCEL,
									icon: Ext.Msg.QUESTION,
									fn: function(btn) {
										if (btn === 'yes') {
											Ext.getCmp('templateGrid').setLoading("Deleting...");
											Ext.Ajax.request({
												url: 'api/v1/resource/componenttypetemplates/' + record.get('templateId') + '/force',
												method: 'DELETE',
												callback: function(){
													Ext.getCmp('templateGrid').setLoading(false);
												},
												success: function(response, opts){
													actionRefresh();
												}
											});
										}
									}
								});
							}
						}
					});
					
				};

			});
			
		</script>	
		
	</stripes:layout-component>
</stripes:layout-render>