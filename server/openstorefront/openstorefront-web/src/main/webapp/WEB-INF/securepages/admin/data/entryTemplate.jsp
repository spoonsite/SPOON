<%-- 
	Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		 http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.

    Document   : entryTemplate
    Created on : Mar 21, 2016, 2:43:11 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../layout/adminlayout.jsp">
	<stripes:layout-component name="contents">
		
		<script src="scripts/component/templateBlocks.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/mediaViewer.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/relationshipVisualization.js?v=${appVersion}" type="text/javascript"></script>		
		
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
				
				var predefinedBlocks = [
					{
						name: 'Description',
						blockCode: function(){
							return 'var block_' + this.blockId + " = Ext.create('OSF.component.template.Description', {" +
									"margin: '0 0 20 0'" +
									"});";
						},							
						generate: function(entryData) {
							var description = Ext.create('OSF.component.template.Description', {
								margin: '0 0 20 0'
							});
							return description;
						}
					},
					{
						name: 'Media',
						blockCode: function(){
							return 'var block_' + this.blockId + " = Ext.create('OSF.component.template.Media', {" +
									"margin: '0 0 20 0'" +
									"});";
						},						
						generate: function(entryData) {
							var media = Ext.create('OSF.component.template.Media', {
								margin: '0 0 20 0'
							});
							return media;
						}
					},
					{
						name: 'Dependencies',
						blockCode: function(){
							return 'var block_' + this.blockId + " = Ext.create('OSF.component.template.Dependencies', {" +
									"margin: '0 0 20 0'" +
									"});";
						},						
						generate: function(entryData) {
							var dependencies = Ext.create('OSF.component.template.Dependencies', {	
								margin: '0 0 20 0'
							});
							return dependencies;
						}
					},
					{
						name: 'DI2E Evaluation Level',
						blockCode: function(){
							return 'var block_' + this.blockId + " = Ext.create('OSF.component.template.DI2EEvalLevel', {" +
									"margin: '0 0 20 0'" +
									"});";
						},								
						generate: function(entryData) {
							var di2elevel = Ext.create('OSF.component.template.DI2EEvalLevel', {
								margin: '0 0 20 0'
							});
							return di2elevel;
						}
					},
					{
						name: 'Evaluation Summary',
						blockCode: function(){
							return 'var block_' + this.blockId + " = Ext.create('OSF.component.template.EvaluationSummary', {" +
									"margin: '0 0 20 0'" +
									"});";
						},															
						generate: function(entryData) {
							var evaluationSummary = Ext.create('OSF.component.template.EvaluationSummary', {	
								margin: '0 0 20 0'
							});
							return evaluationSummary;
						}
					},
					{
						name: 'Resources',
						blockCode: function(){
							return 'var block_' + this.blockId + " = Ext.create('OSF.component.template.Resources', {" +
									"margin: '0 0 20 0'" +
									"});";
						},																						
						generate: function(entryData) {
							var resources = Ext.create('OSF.component.template.Resources', {
								margin: '0 0 20 0'
							});
							return resources;
						}
					},
					{
						name: 'Contacts',
						blockCode: function(){
							return 'var block_' + this.blockId + " = Ext.create('OSF.component.template.Contacts', {" +
									"margin: '0 0 20 0'" +
									"});";
						},								
						generate: function(entryData) {
							var contacts = Ext.create('OSF.component.template.Contacts', {
								margin: '0 0 20 0'
							});
							return contacts;
						}
					},
					{
						name: 'Vitals',
						blockCode: function(){
							return 'var block_' + this.blockId + " = Ext.create('OSF.component.template.Vitals', {" +
									"margin: '0 0 20 0'" +
									"});";
						},							
						generate: function(entryData) {
							var vitals = Ext.create('OSF.component.template.Vitals', {	
								margin: '0 0 20 0'
							});
							return vitals;
						}
					},
					{
						name: 'Relationship',
						blockCode: function(){
							return 'var block_' + this.blockId + " = Ext.create('OSF.component.template.Relationships', {" +
									"margin: '0 0 20 0'" +
									"});";
						},								
						generate: function(entryData) {
							var relationships = Ext.create('OSF.component.template.Relationships', {
								margin: '0 0 20 0'
							});
							return relationships;
						}
					}
				];
				
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
											iconCls: 'fa fa-save',
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
											iconCls: 'fa fa-close',
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
					iconCls: 'fa fa-file-o',
					modal: true,
					width: '80%',
					height: '80%',
					maximizable: true,
					layout: 'fit',
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
									scrollable: true,
									bodyStyle: 'padding: 10px;',
									layout: 'anchor',
									items: [
										{
											xtype: 'combobox',
											fieldLabel: 'Sample Data',
											labelAlign: 'top',
											name: 'entry',		
											width: '100%',
											queryMode: 'remote',
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
										},
										{
											xtype: 'panel',
											id: 'dataFieldPanel',
											title: 'Data Fields',
											border: true,	
											width: '100%',
											collapsible: true,
											titleCollapse: true,
											bodyStyle: 'padding: 10px;',
											margin: '0 0 10 0'											
										},
										{
											xtype: 'panel',
											id: 'blocksPanel',
											title: 'Template Blocks',											
											width: '100%',
											collapsible: true,											
											titleCollapse: true,											
											bodyStyle: 'padding: 10px;',
											layout: 'anchor',
											border: true,
											dockedItems: [
												{
													xtype: 'toolbar',
													dock: 'top',
													items: [
														{
															text: 'Add Custom',
															iconCls: 'fa fa-plus',
															handler: function() {
																addEditCustomBlock.show();
																addEditCustomBlock.getComponent('form').reset();																
															}
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
											layout: 'anchor',											
											scrollable: true,
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
											]
										},
										{
											xtype: 'panel',
											id: 'previewPanel',
											scrollable: true,
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
									iconCls: 'fa fa-2x fa-save',
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
											var blockNames = [];
											Ext.Array.each(templateBlocks, function(block){
												blockNames.push(block.name);
											});
											var visualState = blockNames.join(',');
											
											
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
									iconCls: 'fa fa-2x fa-close',
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
											tooltip: 'Remove',
											callback: function(panel, tool, event) {
												var templateBlockId = panel.block.templateBlock.templateBlockId;
												Ext.Msg.show({
													title:'Remove Template Block?',
													message: 'Are you sure you want to remove block: ' + panel.block.templateBlock.name + '?',
													buttons: Ext.Msg.YESNO,
													icon: Ext.Msg.QUESTION,
													fn: function(btn) {
														if (btn === 'yes') {
															Ext.getCmp('blocksPanel').setLoading("Removing...");
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
											newBlock.blockId = Ext.id().replace('-', '_');
											templateBlocks.push(newBlock);
											updateTemplate();
										}										
									});
									
									var panel = Ext.create('Ext.panel.Panel', {
										title: block.name,
										block: block,
										header: {
											cls: 'entry-template_block'
										},
										margin: '0 0 5 0',
										tools: tools
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
				};
				
				var updateVisual = function() {
					
					var visualPanels = [];					
					Ext.Array.each(templateBlocks, function(block, index){
						
						var templateBlockPanel = block.generate();
						if (entryData) {
							templateBlockPanel.updateTemplate(entryData);
						}
						var tools = [];
						var moveUpTool = {
							type: 'plus',
							tooltip: 'Move Up',
							callback: function(panel, tool, event) {
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
						};
						
						var moveDownTool = {
							type: 'minus',
							tooltip: 'Move Down',
							callback: function(panel, tool, event) {
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
							tooltip: 'Remove',							
							callback: function(panel, tool, event) {
								Ext.Array.remove(templateBlocks, panel.block);
								updateTemplate();
							}							
						});
						
						var wrapperPanel = Ext.create('Ext.panel.Panel', {
							title: block.name,
							collapsible: true,
							block: block,
							header: {
								cls: 'entry-template_block'
							},
							margin: '0 0 5 0',
							bodyStyle: 'padding: 5px;',
							border: true,
							tools: tools,
							items: [
								templateBlockPanel
							]
						});
						visualPanels.push(wrapperPanel);
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
					viewContent.value = Ext.getCmp('codePanel').getComponent('gencode').getValue();
					
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
									iconCls: 'fa fa-2x fa-refresh',
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
									iconCls: 'fa fa-2x fa-plus',
									handler: function () {
										actionAdd();
									}
								},
								{
									xtype: 'tbseparator'
								}, 								
								{
									text: 'Edit',
									id: 'lookupGrid-tools-edit',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-edit',
									disabled: true,
									handler: function () {
										actionEdit(Ext.getCmp('templateGrid').getSelection()[0]);
									}								
								},
								{
									xtype: 'tbfill'
								},								
								{
									text: 'Toggle Status',
									id: 'lookupGrid-tools-status',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-power-off',
									disabled: true,
									handler: function () {
										actionToggleStatus();
									}								
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Delete',
									id: 'lookupGrid-tools-delete',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-close',
									disabled: true,
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
				
				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [
						templateGrid
					]
				});				

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
					
					Ext.getCmp('codePanel').getComponent('precode').setValue(record.get('preTemplateCode'));
					Ext.getCmp('codePanel').getComponent('postcode').setValue(record.get('postTemplateCode'));					
					Ext.getCmp('templateNameField').setValue(record.get('name'));					
					
					templateBlocks = [];
					var blockList = record.get('templateBlocks');
					if (blockList) {
						var blocks = blockList.split(',');
						Ext.Array.each(blocks, function(blockName) {
							Ext.Array.each(allBasicBlocks, function(block) {
								if (blockName === block.name) {
									var newBlock = Ext.clone(block);
									newBlock.blockId = Ext.id().replace('-', '_');
									templateBlocks.push(newBlock);							
								}
							});						
						});
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
									title:'Remove Template?',
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