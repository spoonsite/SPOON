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

Ext.define('OSF.customSubmissionTool.FieldDisplayPanel', {
	extend: 'Ext.container.Container',
	alias: 'widget.osf-csf-displaypanel',

	scrollable: true,
	bodyStyle: 'padding: 5px;',
	style: 'background: #fff;',
	addItem: function (item) {
		this.queryById('itemContainer').add(item);
	},
	getItems: function () {

	},
	createItem: function (sectionItem) {
		return Ext.create({
			xtype: 'osf-formbuilderitem',
			question: sectionItem.question
		});
	},
	saveSection: function () {

	},
	loadSection: function (section, saveSection) {

		var displayPanel = this;
		saveSection = saveSection ? saveSection : true;

		if (saveSection) {
			displayPanel.saveSection();
		}

		var sectionForm = displayPanel.queryById('sectionContainer').getForm();
		var itemContainer = displayPanel.queryById('itemContainer');

		// reset the section form to the sections current values...
		sectionForm.setValues({
			name: section.name,
			instructions: section.instructions
		});

		// clear current item container
		itemContainer.removeAll();

		// create items and add them to the itemsContainer
		var fieldItems = [];
		for (var i = 0; i < section.fieldItems.length; i++) {

			fieldItems.push(displayPanel.createItem(section.fieldItems[i]));
		}

		itemContainer.add(fieldItems);
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
					width: '100%'					
				},
				{
					xtype: 'textarea',
					name: 'instructions',
					emptyText: 'Instructions',
					width: '100%'					
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
