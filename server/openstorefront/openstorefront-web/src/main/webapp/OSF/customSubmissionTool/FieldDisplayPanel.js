/* 
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
 /* Author: cyearsley */
/* global Ext, CoreUtil, CoreService */

/**
 * The FieldDisplayPanel is responsible for "rendering" items in the form field viewport.
 */
Ext.define('OSF.customSubmissionTool.FieldDisplayPanel', {
	extend: 'Ext.container.Container',
	xtype: 'osf-csf-displaypanel',
	requires: ['OSF.customSubmissionTool.FloatingMenu'],
	layout: 'border',

	bodyStyle: 'padding: 5px;',
	style: 'background: #fff;',
	untitledSectionName: '<b style="color: red;">Untitled Section</b> <i class="fa fa-exclamation-triangle" style="color: orange;" data-qtip="This section needs a name" aria-hidden="true"></i>',
	addItem: function (item) {
		this.queryById('itemContainer').add(item);

	},

	/**
	 * Create an formBuilderItem given the sectionItem's data
	 * @param sectionItem - data representation of a form item
	 */
	createItem: function (sectionItem) {
		var displayPanel = this;

		var formBuilderPanel = displayPanel.formBuilderPanel;
		return Ext.create(
			Ext.apply(
				formBuilderPanel.generateSectionObject(sectionItem),
				{xtype: 'osf-formbuilderitem'}
			)
		);
	},
	
	updateFieldPanels: function() {
		var displayPanel = this;	
		var itemContainer = displayPanel.queryById('itemContainer');
		Ext.Array.each(itemContainer.items.items, function(fieldItem){
			fieldItem.resyncfieldOptions();	
		});		
	},

	/**
	 * physically inserts all form items in a section onto the display panel
	 * @param section
	 * @param activeFieldId
	 */
	loadSection: function (section, activeFieldId) {

		var displayPanel = this;		
		var formBuilderPanel = displayPanel.formBuilderPanel;
		var sectionContainer = displayPanel.queryById('sectionContainer');
		var sectionForm = displayPanel.queryById('sectionContainer').getForm();
		var itemContainer = displayPanel.queryById('itemContainer');
				
		
		
		if (!section) {
			displayPanel.queryById('selectionMessage').setHidden(false);
			sectionContainer.setHidden(true);			
			return;
		} else {
			if (displayPanel.section && displayPanel.section.sectionId === section.sectionId) {
				
				if (activeFieldId) {
					displayPanel.selectQuestion(activeFieldId, itemContainer);
				}
			 
			} else {
				itemContainer.removeAll();		
				
				displayPanel.queryById('selectionMessage').setHidden(true);
				sectionContainer.setHidden(false);
				displayPanel.section = section;
				displayPanel.formBuilderPanel.activeSection = section;

				displayPanel.queryById('menu').formBuilderPanel = formBuilderPanel;
				displayPanel.queryById('menu').setHidden(false);

				//load questions
				var newFieldItems = [];
				Ext.Array.each(section.fields, function(field){
					newFieldItems.push({
						xtype: 'osf-formbuilderitem',
						formBuilderPanel: formBuilderPanel,
						floatingMenu: displayPanel.queryById('menu'),
						templateField: field
					});
				});		
				itemContainer.add(newFieldItems);
				itemContainer.updateLayout(true, true);

				if (activeFieldId) {
					displayPanel.selectQuestion(activeFieldId, itemContainer);
				} else {
					if (newFieldItems.length > 0) {
						itemContainer.items.items[0].setActiveFormItem();
					} else {
						formBuilderPanel.activeItem = null;
					}
				}			
				
				// reset the section form to the sections current values...
				sectionForm.setValues({
					name: section.name === displayPanel.untitledSectionName ? '' : section.name,
					instructions: section.instructions
				});				
			}
		}


		
	},
	selectQuestion: function(activeFieldId, itemContainer){
		var displayPanel = this;
		
		Ext.Array.each(itemContainer.items.items, function(fieldItem){
			if (fieldItem.templateField.fieldId === activeFieldId) {
				fieldItem.setActiveFormItem();
				
				var y = fieldItem.getEl().dom.offsetTop;
				displayPanel.queryById('center').scrollTo(0, y, false);												

			}
		});		
	},		
	items: [
		{
			xtype: 'panel',
			region: 'north',
			itemId: 'selectionMessage',
			html: '<h1>Add or Select a Section.</h1>'
		},	

		// display area for fieldItems and the floating menu
		{
			xtype: 'panel',
			region: 'center',
			itemId: 'center',
			scrollable: true,
			bodyStyle: 'padding: 10px;',
			layout: 'anchor',
			dockedItems: [
				{
					xtype: 'osf-csf-floatingMenu',
					dock: 'bottom',
					itemId: 'menu',
					width: '100%'						
				}
			],			
			minHeight: 50,			
			items: [
				{
					xtype: 'form',
					width: '100%',
					itemId: 'sectionContainer',
					hidden: true,					
					items: [
						{
							xtype: 'textfield',
							fieldLabel: 'Section Title',
							name: 'name',
							emptyText: 'Untitled Section',
							width: '100%',
							listeners: {
								change: function (field, newValue, oldValue, opts) {
									var displayPanel = this.up('osf-csf-displaypanel');							
									displayPanel.section.name = newValue;							
									displayPanel.formBuilderPanel.updateSection(displayPanel.section);
								}
							}
						},
						{	
							xtype: 'tinymce_textarea',
							itemId: 'description',
							fieldStyle: 'font-family: Courier New; font-size: 12px;',
							style: { border: '0' },					
							width: '100%',
							height: 250,
							name: 'instructions',			
							maxLength: 65536,
							emptyText: 'Instructions',
							tinyMCEConfig: CoreUtil.tinymceConfigNoMedia(),				
							listeners: {
								change: function (field, newValue, oldValue, opts) {
									var displayPanel = this.up('osf-csf-displaypanel');							
									displayPanel.section.instructions = newValue;							
									displayPanel.formBuilderPanel.updateSection(displayPanel.section);							
								}
							}
						}
					]
				},				
				{
					xtype: 'container',
					layout: 'anchor',
					width: '100%',
					itemId: 'itemContainer'
				}
			]
		}
	]
});
