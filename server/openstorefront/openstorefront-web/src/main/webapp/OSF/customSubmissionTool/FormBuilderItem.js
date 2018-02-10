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

	// extend: 'Ext.container.Container',
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
   //  constrain: true,
   //  draggable: {
   //  	delegate: 'h1',
   //  	vertical: true,
   //  	snap: {
   //  		snap: 30
   //  	},
   //  	endDrag: function () {
   //  		console.log("END DRAG", this);
   //  		this.panelProxy.hide();
			// this.panel.saveState();
   //  	}
   //  },

    generateRandomId: function (num) {
    	var newId = [];
    	var charString = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz';

    	for (var ii = 0; ii < (num ? num : 20); ii++) {
    		newId.push(charString.charAt(Math.floor(Math.random() * charString.length)));
    	}

    	return newId.join('');
    },

	initComponent: function () {

		this.callParent();
		var fieldContainer = this;

		Ext.defer(function () {
			new Ext.drag.Source({
				handle: '.drag-handle',
				element: fieldContainer.getEl(),
				draggingCls: 'csf-drag',
				// delegate: 'h1',
				constrain: {
					vertical: true,
					// el: fieldContainer.up().getEl(),
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
						var checkFormat = function (divId) {
							return getPanel(divId, true);
						};

						if (info.target !== null) {

							var activeContainer = getPanel(self._element.dom.id);
							var targetContainer = getPanel(info.target._element.dom.id);
							var prevY = fieldContainer.getY() - event.delta.y;

							var activeIndex = activeContainer.up().items.items.indexOf(activeContainer);
							var targetIndex = activeContainer.up().items.items.indexOf(targetContainer);

							activeContainer.up().items.items[activeIndex] = targetContainer;
							activeContainer.up().items.items[targetIndex] = activeContainer;

							activeContainer.setY(targetContainer.getY());
							targetContainer.setY(prevY);
						} else {

							fieldContainer.setY(fieldContainer.getY() - event.delta.y);
						}
					}
				}
			});

			new Ext.drag.Target({
				element: fieldContainer.getEl()
			});
		},1000)

		fieldContainer.add(Ext.create('Ext.DataView', {
    		itemId: 'questionDataView',
    		store: {},
    		tpl: new Ext.XTemplate('<h1>QUESTION: ' + fieldContainer.generateRandomId(4) + '</h1><div class="drag-handle" style="width: 50px; height: 50px; background: blue;"></div>'),
    		itemSelector: '.field-template-' + fieldContainer.generateRandomId(50)
    	}));
		if (fieldContainer.recordItem === undefined) { // display the record with some default settings

		}
		else { // display the field with the predefined data

		}
	}
});
