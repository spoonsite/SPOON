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

Ext.define('OSF.customSubmissionTool.FloatingMenu', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-csf-floatingMenu',

	itemId: 'floatingMenu',
	height: 275,
	hidden: true,
	cls: 'floating-menu-container',
	style: 'background: rgba(200,200,200,0.8); box-shadow: 2px 2px 10px; border-radius: 5px;',
	defaultType: 'button',
	defaults: {
		width: '100%',
		style: 'border: none; background: none; color: rgb(200,200,200);'
	},
	layout: 'vbox',

	updatePosition: function () {
		var floatingMenu = this;
		
		var formBuilderPanel = floatingMenu.formBuilderPanel;
		if (floatingMenu.hidden) {
				floatingMenu.setHidden(false);
		}
					
		if (formBuilderPanel.activeItem && formBuilderPanel.activeItem.el) {		
			floatingMenu.setY(formBuilderPanel.activeItem.getY(), true);								
		}
	},
	items: [
		{
			text: '<i style="color:#5f5f5f;" class="fa fa-plus-circle fa-2x" aria-hidden="true" data-qtip="Add a field"></i>',
			flex: 1,
			cls: 'floating-menu-button',
			handler: function() {

				var button = this;
				var disabledUp = false;
				var disabledDown = false;
				var floatingMenuPanel = button.up('panel');
				var formBuilderPanel = button.up('panel').formBuilderPanel;
				
				var popupMenu = Ext.create('Ext.menu.Menu', {
					floating: true,
					defaults: {
						listeners: {
							click: function (menuItem, e, opts) {
								floatingMenuPanel.addNewItem(menuItem, menuItem.fieldType, menuItem.mappingType, menuItem.options);
							}
						}
					},
					items: [
						{
							text: 'Short Answer',
							iconCls: 'fa fa-minus fa-2x',
							defaults: {
								listeners: {
									click: function (menuItem, e, opts) {
										floatingMenuPanel.addNewItem(menuItem, menuItem.fieldType, menuItem.mappingType, menuItem.options);
									}
								}
							},							
							menu: [
								{
									text: 'Mapped Text',
									fieldType: 'TEXT',
									mappingType: 'COMPONENT'
								},
								{
									text: 'Mapped Number',
									fieldType: 'NUMBER',
									mappingType: 'COMPONENT'
								},
								{
									text: 'Mapped Date',
									fieldType: 'DATE',
									mappingType: 'COMPONENT'
								}
							]
						},
						{
							text: 'Paragraph',
							iconCls: 'fa fa-align-left fa-2x',
							defaults: {
								listeners: {
									click: function (menuItem, e, opts) {
										floatingMenuPanel.addNewItem(menuItem, menuItem.fieldType, menuItem.mappingType, menuItem.options);
									}
								}
							},								
							menu: [
								{
									text: 'Mapped TextArea',
									fieldType: 'TEXTAREA',
									mappingType: 'COMPONENT'
								},
								{
									text: 'Mapped RichText',
									fieldType: 'RICHTEXT',
									mappingType: 'COMPONENT'
								}
							]
						},
						{
							xtype: 'menuseparator'
						},
						{
							text: 'Multiple Choice',
							iconCls: 'fa fa-dot-circle-o fa-2x',
							fieldType: 'ATTRIBUTE_RADIO',
							mappingType: 'COMPLEX'
						},
						{
							text: 'Checkboxes',
							iconCls: 'fa fa-check-square-o fa-2x',
							fieldType: 'ATTRIBUTE_MCHECKBOX',
							mappingType: 'COMPLEX'
						},
						{
							text: 'Dropdown',
							iconCls: 'fa fa-chevron-circle-down fa-2x',
							defaults: {
								listeners: {
									click: function (menuItem, e, opts) {
										floatingMenuPanel.addNewItem(menuItem, menuItem.fieldType, menuItem.mappingType, menuItem.options);
									}
								}
							},								
							menu: [
								{
									text: 'Attribute Select',
									fieldType: 'ATTRIBUTE_SINGLE',
									mappingType: 'COMPLEX'									
								},
								{
									text: 'Entry Organization',
									fieldType: 'ORGANIZATION',
									mappingType: 'COMPONENT'																		
								}
							]
						},
						{
							xtype: 'menuseparator'
						},
						{
							text: 'Grid',
							iconCls: 'fa fa-table fa-2x',
							defaults: {
								listeners: {
									click: function (menuItem, e, opts) {
										floatingMenuPanel.addNewItem(menuItem, menuItem.fieldType, menuItem.mappingType, menuItem.options);
									}
								}
							},							
							menu: [
								{
									text: 'Attributes',
									fieldType: 'ATTRIBUTE_MULTI',
									mappingType: 'COMPLEX'									
								},
								{
									text: 'Contacts',
									fieldType: 'CONTACT_MULTI',
									mappingType: 'COMPLEX'																		
								},
								{
									text: 'External Dependency',
									fieldType: 'EXT_DEPEND_MULTI',
									mappingType: 'COMPLEX'																			
								},								
								{
									text: 'Media',
									fieldType: 'MEDIA_MULTI',
									mappingType: 'COMPLEX'																			
								},
								{
									text: 'Resources',
									fieldType: 'RESOURCE_MULTI',
									mappingType: 'COMPLEX'																			
								},
								{
									text: 'Relationships',
									fieldType: 'RELATIONSHIPS_MULTI',
									mappingType: 'COMPLEX'																			
								},								
								{
									text: 'Tags',
									fieldType: 'TAG_MULTI',
									mappingType: 'COMPLEX'																			
								},															
								{
									text: 'Child Submissions',
									fieldType: 'SUBMISSIONS',
									mappingType: 'SUBMISSION'																			
								}	
							]
						},
						{
							text: 'Form',
							iconCls: 'fa fa-file-text-o fa-2x',
							defaults: {
								listeners: {
									click: function (menuItem, e, opts) {
										floatingMenuPanel.addNewItem(menuItem, menuItem.fieldType, menuItem.mappingType, menuItem.options);
									}
								}
							},							
							menu: [
								{
									text: 'Single Contact',
									fieldType: 'CONTACT',
									mappingType: 'COMPLEX'									
								},
								{
									text: 'Single Resource',
									fieldType: 'RESOURCE_SIMPLE',
									mappingType: 'COMPLEX'																		
								}
							]
						}
					]
				});
				popupMenu.showAt(button.getXY());
			}
		},
		{
			text: '<i style="color:#5f5f5f;" class="fa fa-clone fa-2x" aria-hidden="true" data-qtip="Copy a field"></i>',
			flex: 1,
			cls: 'floating-menu-button',
			handler: function() {
				//copy active item
				
			}					
		},
		{
			text: '<i style="color:#5f5f5f;" class="fa fa-quote-right fa-2x" aria-hidden="true" data-qtip="Add a paragraph section"></i>',
			flex: 1,
			cls: 'floating-menu-button',
			handler: function() {
				var floatingMenuPanel = this.up('panel');
				floatingMenuPanel.addNewItem(null, 'CONTENT', 'NONE', {
					staticContent: ''					
				});
			}					
		},				
		{
			text: '<i style="color:#5f5f5f;" class="fa fa-minus fa-2x" aria-hidden="true" data-qtip="Add a horizontal rule"></i>',
			flex: 1,
			cls: 'floating-menu-button',
			handler: function() {
				var floatingMenuPanel = this.up('panel');
				floatingMenuPanel.addNewItem(null, 'CONTENT', 'NONE', {
					staticContent: '<hr>'
				});				
			}					
		},
		{
			text: '<i style="color:#5f5f5f;" class="fa fa-picture-o fa-2x" aria-hidden="true" data-qtip="Add media"></i>',
			flex: 1,
			cls: 'floating-menu-button',
			handler: function() {
				var floatingMenuPanel = this.up('panel');
				floatingMenuPanel.addNewItem(null, 'CONTENT', 'NONE', {
					staticContent: '<img src="GeneralMedia?name="></img>'
				});				
			}					
		},
		{
			text: '<i style="color:#5f5f5f;" class="fa fa-trash fa-2x" aria-hidden="true" data-qtip="Delete Field"></i>',
			flex: 1,
			cls: 'floating-menu-button',
			itemId: 'deleteButton',
			handler: function() {
				var floatingMenuPanel = this.up('panel');
				// delete formBuilderItem
				var formBuilderPanel = floatingMenuPanel.formBuilderPanel;
				var activeItem = formBuilderPanel.activeItem;

				if (activeItem) {
					formBuilderPanel.sectionPanel.deleteField(activeItem.templateField);
				
					activeItem.destroy();
					formBuilderPanel.activeItem = null;
					
					// hide floating menu
					floatingMenuPanel.setHidden(true);
				}

			}					
		},
		{
			text: '<i style="color:#5f5f5f;" class="fa fa-ellipsis-v fa-2x" aria-hidden="true" data-qtip="More options"></i>',
			flex: 1,
			cls: 'floating-menu-button',
			listeners: {
				click: function () {
					var button = this;
					var disabledUp = false;
					var disabledDown = false;
					var formBuilderPanel = button.up('panel').formBuilderPanel;

					var itemIndex = formBuilderPanel.itemContainer.items.items.indexOf(formBuilderPanel.activeItem);
					if (itemIndex === 0) {
						disabledUp = true;
					}
					if (itemIndex === formBuilderPanel.itemContainer.items.items.length -1) {
						disabledDown = true;
					}
					var popupMenu = Ext.create('Ext.menu.Menu', {
						floating: true,
						items: [
							{text: 'Move up', iconCls: 'fa fa-angle-up fa-2x', disabled: disabledUp}, //TODO
							{text: 'Move down', iconCls: 'fa fa-angle-down fa-2x', disabled: disabledDown}, //TODO
							{text: 'Select & swap', iconCls: 'fa fa-retweet fa-2x'}, //TODO
							{text: 'Move to Section', iconCls: 'fa fa-external-link-square fa-2x'} //TODO
						]
					});
					popupMenu.showAt(button.getXY());
				}
			},
			handler: function() {
				
			}					
		}
	],
	addNewItem: function(menuItem, fieldType, mappingType, fieldOptions) {
		var floatingMenu = this;		
		var formBuilderPanel = floatingMenu.formBuilderPanel;
		
		fieldOptions = fieldOptions || {};
		
		var newTemplateField = Ext.apply({
			fieldId: CoreUtil.uuidv4(),
			sectionId: formBuilderPanel.activeSection.sectionId,
			fieldType: fieldType,
			mappingType: mappingType ? mappingType : 'COMPLEX',
			label: 'Untitled',
			questionNumber: ''
		},fieldOptions);
		
		// add a field after the current and set as active
		var fieldIndex = formBuilderPanel.itemContainer.items.items.indexOf(formBuilderPanel.activeItem);
		var newFormBuilderItem = Ext.create({
			xtype: 'osf-formbuilderitem',
			formBuilderPanel: formBuilderPanel,
			floatingMenu: floatingMenu,
			templateField: newTemplateField,
			fieldType: fieldType
		});

		var insertIndex = fieldIndex + 1;
		newTemplateField.fieldOrder = insertIndex;
		
		formBuilderPanel.itemContainer.insert(insertIndex, newFormBuilderItem);
		newFormBuilderItem.setActiveFormItem(newFormBuilderItem);	
		
		if (!formBuilderPanel.activeSection.fields) {
			formBuilderPanel.activeSection.fields = [];
		}		
		Ext.Array.insert(formBuilderPanel.activeSection.fields, insertIndex, [newTemplateField]); 
		formBuilderPanel.sectionPanel.addField(formBuilderPanel.activeSection, newTemplateField, insertIndex);

	}
	
});
