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
/* global Ext, CoreUtil */

Ext.define('OSF.customSubmissionTool.SectionNavPanel', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-csf-sectionnavpanel',
	
	title: 'Sections',
	scrollable: true,
	layout: 'anchor',
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'top',			
			items: [
				{
					text: 'Add',
					iconCls: 'fa fa-lg fa-plus icon-button-color-save',
					handler: function() {
						var navPanel = this.up('panel');
						
					//	var formBuilderPanel = navPanel.formBuilderPanel;
						
						navPanel.addSection({
							name: 'Untitled',
							instructions: '',
							sectionId: CoreUtil.uuidv4(),
							fields: []
						});
						
					}
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Delete',
					itemId: 'delete',
					disabled: true,
					iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
					handler: function() {
						var navPanel = this.up('panel');
						
						var formBuilderPanel = navPanel.formBuilderPanel;
						
						var navList = navPanel.queryById('navList');
						navPanel.deleteSection(navList.getSelection()[0]);
						
						
						//formBuilderPanel.removeSection(formBuilderPanel.activeSection);

//						//set the selection to the first tree list section
//						var navList = formBuilderPanel.queryById('navList');
//						avList.setSelection(navList.getStore().getRoot().childNodes[0]);
						
					}					
				}
			]
		}
	],	
			
	initComponent: function () {
		var navPanel = this;
		navPanel.callParent();
		
		navPanel.treePanel = Ext.create('Ext.tree.Panel', {
			itemId: 'navList',			
			rootVisible: false,
			width: '100%',
			viewConfig: {
				plugins: {
					ptype: 'treeviewdragdrop',
					containerScroll: true,
					allowParentInserts: true
				}
			},			
			listeners: {
				beforedrop: function(node, data, overModel, dropPosition, dropHandlers, eOpts) {
				
					if (overModel && overModel.get('sectionId') 
							&& dropPosition === 'append') {
						//don't allow nesting sections
						dropHandlers.cancelDrop();
					}					
				
				},
				drop: function(node, data, overModel, dropPosition, opts) {
					//reorder sections and move questions
					if (overModel && overModel.get('sectionId')) {					
						var root = navPanel.treePanel.getStore().getRoot();
						
						var sections = [];
						root.eachChild(function(node){
							Ext.Array.filter(navPanel.templateRecord.sections, function(section){
								if (node.get('sectionId') === section.sectionId) {
									sections.push(section);
								}
							});					
						});
						navPanel.templateRecord.sections = sections;						
					}
					
				},
				selectionchange: function (selectionModel, records) {
					
					if (records.length > 0) {
						if (records[0].get('sectionId')) {
							navPanel.queryById('delete').setDisabled(false);

							// loads selection if ids match
							Ext.Array.forEach(navPanel.templateRecord.sections, function (el, index) {
								if (el.sectionId === records[0].get('sectionId')) {
									navPanel.formBuilderPanel.saveSection();
									navPanel.formBuilderPanel.displayPanel.loadSection(el);
								}
							});
							navPanel.formBuilderPanel.activeItem = null;
						} else {
							navPanel.queryById('delete').setDisabled(true);
						}
					} else {
						navPanel.queryById('delete').setDisabled(true);
					}
				}
				
			}
		});
		navPanel.add(navPanel.treePanel);
						
	},
	
	addSection: function(section) {		
		var navPanel = this;
				
		navPanel.templateRecord.sections.push(section);
		
		var navList = navPanel.queryById('navList');		
		var record = Ext.create('Ext.data.TreeModel', {			
		});
		
		record.set({
			expanded: false,
			leaf: false,			
			text: section.name,
			sectionId: section.sectionId,
			children: []			
		});
		
		var root = navList.getStore().getRoot();
		root.appendChild(record);
		
		navList.setSelection(record);		
		
	},
		
	updateSection: function(section) {
		var navPanel = this;
		
		var navList = navPanel.queryById('navList');
		
		navList.getStore().each(function(sectionRecord) {
			if (sectionRecord.get('sectionId') && sectionRecord.get('sectionId') === section.sectionId) {
				sectionRecord.set('name', section.name);
				sectionRecord.set('text', section.name);
				sectionRecord.set('instructions', section.instructions);
			}
		});
		
	},
	
	deleteSection: function(sectionNavRecord) {
		var navPanel = this;
		var navList = navPanel.queryById('navList');
		
		//remove from model
		var newSections = Ext.Array.filter(navPanel.templateRecord.sections, function(section){
			return section.sectionId !== sectionNavRecord.get('sectionId');
		});
		navPanel.templateRecord.sections = newSections;
		
		//remove node from tree
		var root = navList.getStore().getRoot();
		root.removeChild(sectionNavRecord);
	},	
	
	addField: function(section, field, index) {
		var navPanel = this;
		var navList = navPanel.queryById('navList');
		var root = navList.getStore().getRoot();
		
		var record = Ext.create('Ext.data.TreeModel', {			
		});
		
		record.set({
			expanded: false,
			leaf: true,			
			text: field.questionNumber + ' ' + field.label,
			fieldId: field.fieldId,
			children: []			
		});		
		
		root.eachChild(function(child) {
			if (child.get('sectionId') === section.sectionId) {
				child.insertChild(index,  record);
			}	
		});
	},	
	
	updateField: function(field) {
		var navPanel = this;
		var navList = navPanel.queryById('navList');
	},
	
	deleteField: function(field) {
		var navPanel = this;
		var navList = navPanel.queryById('navList');
	},	
	
	updateTemplate: function() {
		var navPanel = this;		
		var store = navPanel.treePanel.getStore();
		var navList = navPanel.queryById('navList');
		
		var root = navList.getStore().getRoot();
		
		var selectionSet = false;
		Ext.Array.each(navPanel.templateRecord.sections, function(section) {
			
			var sectionItem = {
				expanded: false,
				leaf: false,		
				text: section.name,
				sectionId: section.sectionId,
				children: []
			};
			
			Ext.Array.each(section.fields, function(field) {
				var fieldType = field.fieldType ? ' (' + field.fieldType + ')' : '';
				
				sectionItem.children.push({
					leaf: true,
					text: (field.questionNumber ? field.questionNumber : '')  + field.label + fieldType,
					fieldId: field.fieldId
				});
			});
			var nodeInserted = root.appendChild(sectionItem);
			if (!selectionSet) {
				navList.setSelection(nodeInserted);
				selectionSet = true;
			}
		});
				
	}	
	
	
});
