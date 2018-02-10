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

	extend: 'Ext.container.Container',

	recordItem: undefined,

	width: '85%',
	padding: 20,
	target: true,
	overCls: 'csf-hover',
	items: [
		{
	        xtype: 'textfield',
	        emptyText: 'Enter Question Here',
	        height: 50,
	        width: '50%'
    	}
    ],

    generateRandomId: function (num) {
    	var newId = [];
    	var charString = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz';

    	for (var ii = 0; ii < (num ? num : 20); ii++) {
    		newId.push(charString.charAt(Math.floor(Math.random() * charString.length)));
    	}

    	return newId.join('');
    },

    listeners: {
    	afterrender: function () {

			var fieldContainer = this;

    		// style the question input field
			fieldContainer.items.items[0].el.dom.querySelector('input').style.fontSize = '32px';

			// set this container as a drag source
			new Ext.drag.Source({
				handle: '.drag-handle',
				element: fieldContainer.getEl(),
				draggingCls: 'csf-drag',
				constrain: {
					vertical: true,
				},
				listeners: {
					dragend: function (self, info, event, eOpts) {
						console.log("this.container", fieldContainer);
						// console.log("self",self);
						// console.log("info",info);
						// console.log("event",event);
						// console.log("eOpts",eOpts);

						var getPanel = function (divId, isChecking = false) {
							var regex = /ext\-comp\-[0-9]{4}/;
							if (isChecking) {
								return regex.exec(divId) !== null;	
							}
							else {
								return Ext.getCmp(regex.exec(divId)[0]);
							}
						};

						if (info.target !== null) {

							// identify the active (container being dragged,) and the target container
							var activeContainer = getPanel(self._element.dom.id);
							var targetContainer = getPanel(info.target._element.dom.id);

							// swap positions in the items list
							var activeIndex = activeContainer.up().items.items.indexOf(activeContainer);
							var targetIndex = activeContainer.up().items.items.indexOf(targetContainer);
							activeContainer.up().items.items[activeIndex] = targetContainer;
							activeContainer.up().items.items[targetIndex] = activeContainer;

							// reposition the containers
							var prevY = fieldContainer.getY() - event.delta.y;
							activeContainer.setY(targetContainer.getY());
							targetContainer.setY(prevY);
						} else {

							fieldContainer.setY(fieldContainer.getY() - event.delta.y);
						}
					}
				}
			});

			// set this container as an eligible drag source
			new Ext.drag.Target({
				element: fieldContainer.getEl()
			});
    	}
    },

	initComponent: function () {

		this.callParent();
		var fieldContainer = this;

		fieldContainer.add(Ext.create('Ext.DataView', {
    		itemId: 'questionDataView',
    		store: {},
    		tpl: new Ext.XTemplate('<div class="field-content"><h1>QUESTION: ' + fieldContainer.generateRandomId(4) + '</h1><div class="drag-handle" style="width: 50px; height: 50px; background: blue;"></div></div>'),
    		itemSelector: '.field-template-' + fieldContainer.generateRandomId(50)
    	}));
		if (fieldContainer.recordItem === undefined) { // display the record with some default settings

		}
		else { // display the field with the predefined data

		}
	}
});
