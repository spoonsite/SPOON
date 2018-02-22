/* 
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
Ext.define('OSF.customSubmissionTool.SectionNavPanel', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-csf-sectionnavpanel',
	
	title: 'Sections',
	scrollable: true,
	
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'top',
			
			items: [
				{
					text: 'Add',
					iconCls: 'fa fa-lg fa-plus icon-button-color-save',
					handler: function() {
						var formBuilderPanel = this.up('[itemId=formBuilderPanel]');
						formBuilderPanel.templateRecord.sections.add({
							name: 'Untitled',
							instructions: '',
							sectionId: Math.random().toString(36).substr(2, 10),
							fieldItems: [
								{
									question: '',
									formBuilderPanel: formBuilderPanel
								}
							]
						});

						// set the selection to the last tree list section
						var navList = formBuilderPanel.queryById('navList');
						var navRoot = navList.getStore().getRoot();
						navList.setSelection(navRoot.childNodes[navRoot.childNodes.length - 1]);
					}
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Delete',
					iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
					handler: function() {
						var formBuilderPanel = this.up('[itemId=formBuilderPanel]');
						if (formBuilderPanel.templateRecord.sections.length > 1) {
							formBuilderPanel.templateRecord.sections.remove(formBuilderPanel.activeSection);

							// set the selection to the first tree list section
							var navList = formBuilderPanel.queryById('navList');
							navList.setSelection(navList.getStore().getRoot().childNodes[0]);
						}
					}					
				}
			]
		}
	],	

	updateNavList: function (templateRecord) {
		var sectionPanel = this;
		var formBuilderPanel = sectionPanel.up('[itemId=formBuilderPanel]');
		var templateRecord = templateRecord || formBuilderPanel.templateRecord;

		// clear the current tree list, and build a new one
		formBuilderPanel.sectionPanel.removeAll();
		formBuilderPanel.sectionPanel.add({

			xtype: 'treelist',				
			itemId: 'navList',
			listeners: {
				selectionchange: function (self, record) {

					if (!record.data.leaf) {
						
						// loads selection if ids match
						Ext.Array.forEach(templateRecord.sections, function (el, index) {
							if (el.sectionId === record.data.sectionId) {
								formBuilderPanel.displayPanel.loadSection(el);
							}
						});
						formBuilderPanel.activeItem = null;
					}
				}
			},
			store: {
				root: {
					expanded: true,
					children: (function () {

						// dynamically create section list in regards to what is in templateRecord.sections
						var childrenItems = [];
						Ext.Array.forEach(templateRecord.sections, function (el, index) {

							childrenItems.push({});
							Ext.apply(childrenItems[index], {
								text: el.name,
								expanded: false,
								iconCls: 'fa fa-file-text',
								children: [],
								sectionId: el.sectionId,
							});

							Ext.Array.forEach(el.fieldItems, function (el) {
								childrenItems[index].children.push({
									text: el.question,
									leaf: true
								});
							});
						});

						return childrenItems;
					}())
				}
			}
		});
	},
});
