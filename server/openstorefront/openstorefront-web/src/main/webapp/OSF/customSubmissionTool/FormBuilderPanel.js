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
		'OSF.customSubmissionTool.FieldDisplayPanel'
	],

	width: '100%',		
	style: 'background: #6c6c6c',
	itemId: 'formBuilderPanel',
	activeItem: null,
	activeSection: null,
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
			xtype: 'osf-csf-displaypanel',
			itemId: 'fieldDisplayPanel',
			flex: 0.75,			
			region: 'center'
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
					itemId: 'sectionPanel',
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

		formBuilderPanel.displayPanel = formBuilderPanel.queryById('fieldDisplayPanel');
		formBuilderPanel.itemContainer = formBuilderPanel.queryById('itemContainer');
		formBuilderPanel.floatingMenu = formBuilderPanel.queryById('floatingMenu');
		formBuilderPanel.sectionPanel = formBuilderPanel.queryById('sectionPanel');

		// if no template record section has been defined, define it
		if (typeof formBuilderPanel.templateRecord.sections === 'undefined') {
			formBuilderPanel.templateRecord.sections = [
				{
					name: 'Untitled',
					instructions: '',
					sectionId: Math.random().toString(36).substr(2, 10),
					fieldItems: [
						{
							question: 'Question #1',
							formBuilderPanel: formBuilderPanel
						},
						{
							question: 'Question #2',
							formBuilderPanel: formBuilderPanel
						}
					]
				},
				{
					name: 'A brand new section!',
					instructions: 'This is some dummy data to fill out the "instructions" textfield :)',
					sectionId: Math.random().toString(36).substr(2, 10),
					fieldItems: [
						{
							question: 'Super cool question #1',
							formBuilderPanel: formBuilderPanel
						},
						{
							question: 'Super cool question #2',
							formBuilderPanel: formBuilderPanel
						}
					]
				}
			];
		}

		// utils for easily adding/removing sections to the templateRecord
		formBuilderPanel.templateRecord.sections.add = function (newSection) {
			formBuilderPanel.templateRecord.sections.push(newSection);
			formBuilderPanel.sectionPanel.updateNavList();
			formBuilderPanel.activeItem = null;
			formBuilderPanel.displayPanel.loadSection(formBuilderPanel.templateRecord.sections[formBuilderPanel.templateRecord.sections.length - 1]);
		};
		formBuilderPanel.templateRecord.sections.remove = function (section) {
			var sectionId = section.sectionId || section;
			Ext.Array.forEach(formBuilderPanel.templateRecord.sections, function (el, index) {
				if (el.sectionId === sectionId) {
					formBuilderPanel.templateRecord.sections.splice(index, 1);
				}
			});
			formBuilderPanel.sectionPanel.updateNavList();
			formBuilderPanel.activeItem = null;
			formBuilderPanel.displayPanel.loadSection(formBuilderPanel.templateRecord.sections[0]);
		};

		// loads the first section in the tempateRecord
		formBuilderPanel.displayPanel.loadSection(formBuilderPanel.templateRecord.sections[0]);

		formBuilderPanel.sectionPanel.updateNavList();
	}
});
