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

Ext.define('OSF.customSubmissionTool.FormBuilderItem', {	
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-formbuilderitem',

	requires: ['OSF.customSubmissionTool.FieldOptions'],
	
	height: 275,
	margin: '10 10 10 0',
	padding: '10 20 10 20',
	target: true,
	width: '100%',
	cls: 'form-builder-item',
	overCls: 'csf-hover',
	layout: {
		type: 'anchor'
	},

	isActive: false,
	
	items: [
		{
			xtype: 'container',
			layout: {
				type: 'hbox',
				align: 'center'
			},
			
			items: [
				{
					xtype: 'textfield',
					itemId: 'labelCode',
					width: '9%',
					maxLength: 3,
					enforceMaxLength: true,
					fieldStyle: 'font-size: 32px; text-align: center; padding: 0;',
					height: 50,
					listeners: {
						change: function (self, newVal) {
							// var formBuilderItem = this.up('[cls=form-builder-item]');
							// formBuilderItem.labelCode = newVal;
							// formBuilderItem.saveSection();
							this.up('[cls=form-builder-item]').saveSection('labelCode', newVal);
						}
					}
				},
				{
					xtype: 'label',
					text: '∙',
					width: '1%',
					style: 'font-size: 32px; padding-top: 32px;',
					height: 50
				},
				{
					xtype: 'textfield',
					itemId: 'question',
					emptyText: 'Untitled Question',
					fieldStyle: 'font-size: 32px; padding: 0;',
					height: 50,
					width: '90%',
					listeners: {
						change: function (self, newVal) {
							// var formBuilderItem = this.up('[cls=form-builder-item]');
							// formBuilderItem.question = newVal;
							// formBuilderItem.saveSection();
							this.up('[cls=form-builder-item]').saveSection('question', newVal);
						}
					}
				}
			]
		},
		{
			xtype: 'container',
			itemId: 'itemRenderContainer',
			//style: 'background: red;',
			items: [
				{
					xtype: 'csf-field-options'
				}
			]
		}
	],
	
	saveSection: function (itemField, newVal) {
		var formBuilderItem = this;
		if (this.getFormBuilderPanel()) {

			if (itemField && newVal) {

				if (formBuilderItem.getFormBuilderPanel().validSectionItems.indexOf(itemField) === -1) {
					console.error("There is no support for the field: ", itemField);
				}
				else {
					this[itemField] = newVal;
				}
			}

			// saveSection defined is the DisplayPanel
			this.getFormBuilderPanel().saveSection(true);
		}
	},

    getFormBuilderPanel: function () {
    	return this.formBuilderPanel || this.up('[itemId=formBuilderPanel]');
    },

    getItemContainer: function () {
    	return this.getFormBuilderPanel().itemContainer;
    },

    setActiveFormItem: function (cmp) {

    	var newItem = cmp || this;
    	var formBuilderPanel = this.getFormBuilderPanel();
		var previousActiveItem = formBuilderPanel.activeItem;

		if (previousActiveItem) {
			previousActiveItem.removeCls('csf-active');
		}
		newItem.addCls('csf-active');
		formBuilderPanel.activeItem = newItem;

		// update the menu position
		formBuilderPanel.floatingMenu.updatePosition();
    },

    listeners: {
    	afterlayout: function () {

			var fieldItem = this;

			var formBuilderPanel = fieldItem.getFormBuilderPanel();
			var itemContainer = formBuilderPanel.itemContainer;		
    	},
    	click: {
    		element: 'el',
    		fn: function () {

    			var formBuilderItem = Ext.getCmp(this.id);
    			formBuilderItem.setActiveFormItem();
    		}
    	},
    	afterrender: function () {

			var fieldContainer = this;

			// set this container as a drag source
			new Ext.drag.Source({
				// handle: '#' + fieldContainer.el.dom.id,
				element: fieldContainer.getEl(),
				draggingCls: 'csf-drag',
				constrain: {
					vertical: true
				},
				listeners: {
					dragstart: function (self, info, event, eOpts) {
						fieldContainer.disable();
					},
					dragend: function (self, info, event, eOpts) {

						// if there is a valid target container:
						//		* swap the y coords of both containers
						//		* swap the location in the parent panel's items array
						if (info.target !== null) {

							// identify the active (container being dragged,) and the target container
							var activeContainer = fieldContainer;
							var targetContainer = Ext.getCmp(info.target._element.id);
							var itemContainer = fieldContainer.getItemContainer();

							// get the index of the active and target field items in the item container
							var activeIndex = itemContainer.items.items.indexOf(activeContainer);
							var targetIndex = itemContainer.items.items.indexOf(targetContainer);

							// reset the y index of the active container, and then swap the items.
							activeContainer.setY(fieldContainer.getY() - event.delta.y);
							itemContainer.insert(targetIndex, activeContainer);
							itemContainer.insert(activeIndex, targetContainer);

							// update the floating menu
							fieldContainer.getFormBuilderPanel().floatingMenu.updatePosition();

						} else {

							fieldContainer.setY(fieldContainer.getY() - event.delta.y);
						}

						fieldContainer.enable();
					}
				}
			});

			// set this container as an eligible drag source
			fieldContainer.dragTarget = new Ext.drag.Target({
				element: fieldContainer.getEl()
			});

			// set the field type
			var fieldTypeCombo = fieldContainer.queryById('fieldTypeCombo');
			if (fieldContainer.fieldType) {
				fieldTypeCombo.setValue(fieldContainer.fieldType);
			}
    	}
    },

	initComponent: function () {

		this.callParent();
		var fieldContainer = this;

		fieldContainer.queryById('question').setValue(fieldContainer.question);
		fieldContainer.queryById('labelCode').setValue(fieldContainer.labelCode);
	}
});
