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

	templateRecord: undefined,

	height: 275,
	margin: '10 10 10 0',
	padding: '10 20 10 20',
	target: true,
	width: '100%',
	cls: 'form-builder-item',
	overCls: 'csf-hover',
	isActive: false,
	layout: {
		type: 'anchor'
	},
	items: [
		{
	        xtype: 'textfield',
	        emptyText: 'Untitled Question',
			fieldStyle: 'font-size: 32px',
	        height: 50,
	        width: '100%'
		}
    ],

    getFormBuilderPanel: function () {
    	return this.formBuilderPanel || this.up('[itemId=formBuilderPanel]');
    },

    getItemContainer: function () {
    	return this.getFormBuilderPanel().itemContainer;// || this.getFormBuilderPanel().queryById('itemContainer');
    },

    setActiveFormItem: function (cmp) {

    	var newItem = cmp || this;
    	var formBuilderPanel = this.formBuilderPanel;
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

			var formBuilderPanel = this.formBuilderPanel;
			var itemContainer = formBuilderPanel.itemContainer;
			if (itemContainer.items.items.length === 1) {
				formBuilderPanel.queryById('deleteButton').setDisabled(true);
			}
			else if (itemContainer.items.items.length === 2) {
				formBuilderPanel.queryById('deleteButton').setDisabled(false);	
			}
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
					vertical: true,
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
    	}
    },

	initComponent: function () {

		this.callParent();
		var fieldContainer = this;

		//fieldContainer.items.items[0].items.items[0].add(Ext.create('OSF.customSubmissionTool.ItemMenu', {width: '100%'}));

		// fieldContainer.items.items[1].add(Ext.create('Ext.DataView', {
  //   		itemId: 'questionDataView',
  //   		store: {},
  //   		tpl: new Ext.XTemplate('<div class="field-content"><h1>QUESTION: ' + fieldContainer.generateRandomId(4)), // + '</h1><div class="drag-handle" style="width: 50px; height: 50px; background: blue;"></div></div>'
  //   		itemSelector: '.field-template-' + fieldContainer.generateRandomId(50)
  //   	}));
		// if (fieldContainer.templateRecord === undefined) { // display the record with some default settings

		// }
		// else { // display the field with the predefined data

		// }
	}
});
