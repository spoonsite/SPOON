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

/* global Ext, CoreUtil */

Ext.define('OSF.form.Section', {
	extend: 'Ext.form.Panel',
	alias: 'osf.form.Section',

	layout: 'fit',	
	initComponent: function () {		
		this.callParent();
		
		var sectionForm = this;
		
	},
	loadData: function(evaluationId, componentId, data, opts) {
	
		var sectionForm = this;
		
		if (data.subsections && data.subsections.length > 0) {
			var contentPanel = Ext.create('Ext.panel.Panel', {
				scrollable: true,
				bodyStyle: 'padding: 20px;'
			});
			var items = [];			
			if (!data.section.noContent) {			
				var border = '0';
				if (data.section.privateSection) {
					border = '1px dash steelblue';
				}
				
				items.push({
					xtype: 'tinymce_textarea',											
					fieldStyle: 'font-family: Courier New; font-size: 12px;',
					style: { border: border },					
					name: 'content',			
					maxLength: 32000,
					height: 400,
					width: '100%',
					value: data.section.content,
					tinyMCEConfig: CoreUtil.tinymceConfig("osfmediaretriever")							
				});				
			}
			
			//create sub-sections
			Ext.Array.each(data.subsections, function(subsection){
				
				var extraTitleInfo = '';
				var border = '1px';
				if (subsection.hideTitle) {
					extraTitleInfo = " (Hidden on View)";
				}
				if (subsection.privateSection) {
					extraTitleInfo += " (Private)";
					border = '1px dash steelblue';
				}

				var subItems = [];	
				if (!subsection.noContent) {
					subItems.push({
						xtype: 'tinymce_textarea',												
						fieldStyle: 'font-family: Courier New; font-size: 12px;',
						style: { border: border },					
						name: 'content',			
						maxLength: 32000,
						height: 400,
						
						value: subsection.content,
						tinyMCEConfig: CoreUtil.tinymceConfig("osfmediaretriever")							
					});
				}
				
				Ext.Array.each(subsection.customFields, function(field) {
					
					if (field.fieldType === 'TEXT') {
						subItems.push({
							xtype: 'textbox',
							name: 'customValue',
							fieldLabel: field.label,
							value: field.value
						});
					} else if (field.fieldType === 'RICHTEXT') {
						subItems.push(
							{
								xtype: 'panel',						
								html: '<b>' + field.label + '</b>'
							},
							{
								xtype: 'tinymce_textarea',											
								fieldStyle: 'font-family: Courier New; font-size: 12px;',
								style: { border: border },					
								name: 'customValue',			
								maxLength: 32000,
								height: 250,
								width: '100%',
								value: field.value,
								tinyMCEConfig: CoreUtil.tinymceConfig("osfmediaretriever")	
							}
						);						
					} else if (field.fieldType === 'COMBO') {
						subItems.push({
							xtype: 'combobox',
							name: 'customValue',
							fieldLabel: field.label,
							value: field.value,
							editable: false,
							typeAhead: false,
							forceSelection: true,
							displayField: 'value',
							valueField: 'value',
							store: {
								data: field.validValues
							}
						});						
					} else if (field.fieldType === 'COMBOEDIT') {
						subItems.push({
							xtype: 'combobox',
							name: 'customValue',
							fieldLabel: field.label,
							value: field.value,
							editable: true,
							typeAhead: true,							
							displayField: 'value',
							valueField: 'value',
							store: {
								data: field.validValues
							}
						});							
					} else if (field.fieldType === 'CHECKBOX') {
						subItems.push({
							xtype: 'checkbox',
							name: 'customValue',
							boxLabel: field.label,
							value: field.value
						});
					}
					
				});
				
				var subSectionPanel = Ext.create('Ext.panel.Panel', {
					title: subsection.title + extraTitleInfo,
					border: border,
					layout: 'anchor',
					defaults: {
						labelAlign: 'right',
						width: '100%'
					},
					items: subItems
				});
				items.push(subSectionPanel);
			});
			
			contentPanel.add(items);
			sectionForm.add(contentPanel);
		} else {
			if (data.section.noContent) {
				sectionForm.update("There's no content allowed for this section. Check template.");
			} else {
				sectionForm.add({
					xtype: 'tinymce_textarea',											
					fieldStyle: 'font-family: Courier New; font-size: 12px;',
					style: { border: '0' },					
					name: 'content',			
					maxLength: 32000,
					value: data.section.content,
					tinyMCEConfig: CoreUtil.tinymceConfig("osfmediaretriever")						
				});
				
			}
		}
		
		if (data.section.privateSection) {
			sectionForm.setTitle("PRIVATE");
		}
		
		opts.commentPanel.loadComments(evaluationId, data.section.title, componentId);
	}
	
});
