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
	alias: 'widget.osf-csf-displaypanel',
	requires: ['OSF.customSubmissionTool.FloatingMenu'],

	scrollable: true,
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

		var formBuilderPanel = this.up('[itemId=formBuilderPanel]');
		return Ext.create(
			Ext.apply(
				formBuilderPanel.generateSectionObject(sectionItem),
				{xtype: 'osf-formbuilderitem'}
			)
		);
	},

	/**
	 * physically inserts all form items in a section onto the display panel
	 */
	loadSection: function (section, saveSection) {

		var displayPanel = this;
		var formBuilderPanel = displayPanel.up('[itemId=formBuilderPanel]');
		var sectionForm = displayPanel.queryById('sectionContainer').getForm();
		var itemContainer = displayPanel.queryById('itemContainer');

		saveSection = typeof saveSection !== 'undefined' ? saveSection : true;

		if (saveSection) {
			formBuilderPanel.saveSection();
		}
		
		// clear current item container
		itemContainer.removeAll();
		
		// create items and add them to the itemsContainer
		var fieldItems = [];
		for (var i = 0; i < section.fieldItems.length; i++) {
			
			fieldItems.push(displayPanel.createItem(section.fieldItems[i]));
		}
		
		itemContainer.add(fieldItems);
		
		// set the first item as active (need to push this back on the stack though...)
		Ext.defer(function () {
			itemContainer.query('[cls=form-builder-item]')[0].setActiveFormItem();
		},1);
		
		formBuilderPanel.activeSection = section;

		// reset the section form to the sections current values...
		sectionForm.setValues({
			name: section.name === displayPanel.untitledSectionName ? '' : section.name,
			instructions: section.instructions
		});
	},
	items: [
		{
			xtype: 'form',
			itemId: 'sectionContainer',
			padding: 10,
			items: [
				{
					xtype: 'textfield',
					name: 'name',
					emptyText: 'Untitled Section',
					width: '100%',
					listeners: {
						change: function (self, newVal) {

							this.up('[itemId=formBuilderPanel]').saveSection();
						}
					}
				},
				{
					xtype: 'textarea',
					name: 'instructions',
					emptyText: 'Instructions',
					width: '100%',
					listeners: {
						change: function () {
							
							this.up('[itemId=formBuilderPanel]').saveSection();
						}
					}
				}
			]
		},

		// display area for fieldItems and the floating menu
		{
			xtype: 'container',
			layout: 'column',
			items: [
				{
					xtype: 'container',
					itemId: 'itemContainer',
					columnWidth: 0.9,
				},
				{
					xtype: 'container',
					height: '100%',
					padding: '10 40 0 20',
					columnWidth: 0.1,
					items: [
						{
							xtype: 'osf-csf-floatingMenu',
							formBuilderPanel: Ext.getCmp('formBuilderPanel')
						}
					]
				}
			]
		}
	]
});
