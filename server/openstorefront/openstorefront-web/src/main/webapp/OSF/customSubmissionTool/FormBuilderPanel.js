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

Ext.define('OSF.customSubmissionTool.FormBuilderPanel', {
	extend: 'Ext.panel.Panel',
	requires: [
		'OSF.customSubmissionTool.FormBuilderItem',
		'OSF.customSubmissionTool.FormTemplateInfoPanel',
		'OSF.customSubmissionTool.SectionNavPanel',
		'OSF.customSubmissionTool.TemplateProgressPanel',
		'OSF.customSubmissionTool.FloatingMenu'
	],

	width: '100%',		
	style: 'background: #6c6c6c',
	itemId: 'formBuilderPanel',
	activeItem: null,
	layout: {
		type: 'border'
	},
	items: [
		{
			flex: 0.25,
			region: 'west',
			title: 'Template Information',
			itemId: 'tools',
			titleCollapse: true,
			collapsible: true,		
			split: true,
			style: 'background: #fff;',			
			layout: {
				type: 'vbox'				
			}
		},
		{
			flex: 0.75,			
			region: 'center',
			scrollable: true,
			style: 'background: #fff;',
			itemId: 'fieldDisplayPanel',
			bodyStyle: 'padding: 5px;',
			addItem: function (item) {
				this.queryById('itemContainer').add(item);
			},
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
		}
	],

	templateRecord: undefined,

	initComponent: function () {

		this.callParent();
		var formBuilderPanel = this;

		formBuilderPanel.queryById('tools').add(
			[
				{
					xtype: 'osf-form-templateinfo-panel',
					width: '100%',
					templateRecord: formBuilderPanel.templateRecord
				},
				{
					xtype: 'osf-csf-sectionnavpanel',					
					width: '100%',					
					templateRecord: formBuilderPanel.templateRecord,
					flex: 2
				},
				{
					xtype: 'osf-csf-templateprogresspanel',
					width: '100%',
					templateRecord: formBuilderPanel.templateRecord,
					flex: 2
				}
			]
		);

		// TODO: query the template...
		// for each items in record... add FormBuilderItem...
		formBuilderPanel.displayPanel = formBuilderPanel.queryById('fieldDisplayPanel');
		formBuilderPanel.itemContainer = formBuilderPanel.queryById('itemContainer');
		formBuilderPanel.floatingMenu = formBuilderPanel.queryById('floatingMenu');

		formBuilderPanel.displayPanel.addItem({
			xtype: 'osf-formbuilderitem',
			formBuilderPanel: formBuilderPanel,
			templateRecord: formBuilderPanel.templateRecord,
			isActive: true
		});
		// for (var i = 0; i < 5; i++) {
		// 	formBuilderPanel.displayPanel.addItem(Ext.create('OSF.customSubmissionTool.FormBuilderItem', {
		// 		formBuilderPanel: formBuilderPanel,
		// 		templateRecord: formBuilderPanel.templateRecord
		// 	}));
		// }
	}
});
