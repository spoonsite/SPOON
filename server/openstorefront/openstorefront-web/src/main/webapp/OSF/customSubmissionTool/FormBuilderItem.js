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
	// dockedItems: [
	// 	{
	// 		xtype: 'toolbar',
	// 		margin: '0 0 0 10',
	// 		dock: 'right',
	// 		style: 'background: transparent;',
	// 		//ui: 'footer',
	// 		items: [
				// {
				// 	scale: 'medium',
				// 	iconCls: 'fa fa-plus-circle fa-2x',
				// 	handler: function() {
						
				// 	}
				// },
				// {
				// 	scale: 'medium',
				// 	padding: '10 0 0 0',
				// 	iconCls: 'fa fa-quote-right',
				// 	handler: function() {
						
				// 	}					
				// },				
				// {
				// 	scale: 'medium',
				// 	iconCls: 'fa fa-minus fa-2x',
				// 	handler: function() {
						
				// 	}					
				// },
				// {
				// 	scale: 'medium',
				// 	iconCls: 'fa fa-image fa-2x',
				// 	handler: function() {
						
				// 	}					
				// },				
				// {
				// 	scale: 'medium',
				// 	iconCls: 'fa fa-trash fa-2x',
				// 	handler: function() {
						
				// 	}					
				// }			
	// 		]
	// 	}
	// ],
	items: [
		{
	        xtype: 'textfield',
	        emptyText: 'Untitled Question',
			fieldStyle: 'font-size: 32px',
	        height: 50,
	        width: '100%'
		}
				
//				{
//					xtype: 'container',
//					width: '100%',
//					height: '30%',
//					items: [
//						{
//							xtype: 'button',
//							text: '<i style="color:#5f5f5f;" class="fa fa-ellipsis-v fa-2x" aria-hidden="true"></i>',
//							padding: 0,
//							width: 40,
//							height: 35,
//							margin: '10 0 0 0',
//							overCls: 'csf-meta-menu-item',
//							style: 'border: none; background: none; float: right;',
//							listeners: {
//								click: function () {
//									var button = this;
//									var disabledUp = false;
//									var disabledDown = false;
//									var fieldDisplayPanel = button.up('[itemId=fieldDisplayPanel]');
//									var itemIndex = fieldDisplayPanel.items.items.indexOf(button.up('[cls=form-builder-item]'));
//									if (itemIndex === 0) {
//										disabledUp = true;
//									}
//									if (itemIndex === fieldDisplayPanel.items.items.length -1) {
//										disabledDown = true;
//									}
//									var popupMenu = Ext.create('Ext.menu.Menu', {
//										floating: true,
//										items: [
//											{text: 'Move up', iconCls: 'fa fa-angle-up fa-2x', disabled: disabledUp},
//											{text: 'Move down', iconCls: 'fa fa-angle-down fa-2x', disabled: disabledDown},
//											{text: 'Select & swap', iconCls: 'fa fa-retweet fa-2x'}, //TODO
//											{text: 'Move to page', iconCls: 'fa fa-external-link-square fa-2x'} //TODO
//										]
//									});
//									popupMenu.showAt(this.getXY());
//								}
//							}
//						},
//						{
//							xtype: 'button',
//							text: '<i style="color:#5f5f5f;" class="fa fa-trash fa-2x" aria-hidden="true" data-qtip="Delete this field"></i>',
//							padding: 0,
//							width: 40,
//							height: 35,
//							margin: '10 0 0 0',
//							overCls: 'csf-meta-menu-item',
//							itemId: 'deleteButton',
//							style: 'border: none; background: none; float: right;',
//							listeners: {
//								click: function () {
//									this.up('FormBuilderItem').displayPanel.activeItem = null;
//									this.up('[cls=form-builder-item]').destroy();
//								}
//							}
//						},
//						{
//							xtype: 'button',
//							text: '<i style="color:#5f5f5f;" class="fa fa-clone fa-2x" aria-hidden="true" data-qtip="Copy this field"></i>',
//							padding: 0,
//							width: 40,
//							height: 35,
//							margin: '10 0 0 0',
//							overCls: 'csf-meta-menu-item',
//							style: 'border: none; background: none; float: right;',
//							listeners: {
//								click: function () {
//									console.log("CLONE THE FIELD!");
//								}
//							}
//						},
//						{
//							xtype: 'button',
//							text: '<i style="color:#5f5f5f;" class="fa fa-plus-circle fa-2x" aria-hidden="true" data-qtip="Add a new field here"></i>',
//							padding: 0,
//							width: 40,
//							height: 35,
//							margin: '10 0 0 0',
//							overCls: 'csf-meta-menu-item',
//							style: 'border: none; background: none; float: right;',
//							listeners: {
//								click: function () {
									// // add a field after the current
									// var displayPanel = this.up('osf-formbuilderitem').displayPanel;
									
									// //This need be re-examined 
									// //perhaps a maintain a separate array and render on change 
									
									// var fieldIndex = displayPanel.items.items.indexOf(this.up('[cls=form-builder-item]'));
									// this.up('[itemId=formBuilderPanel]').items.items[1].insert(fieldIndex+1, Ext.create('OSF.customSubmissionTool.FormBuilderItem'));
//								}
//							}
//						}
//					]
//				}
//			]
//    	}
    ],

    setActiveFormItem: function (cmp) {
    	var newItem = cmp || this;
    	// var formBuilderPanel = newItem.formBuilderPanel.displayPanel;
    	var formBuilderPanel = newItem.up('[itemId=formBuilderPanel]');
		var previousActiveItem = formBuilderPanel.activeItem;

		if (previousActiveItem) {
			previousActiveItem.removeCls('csf-active');
		}
		newItem.addCls('csf-active');
		formBuilderPanel.activeItem = newItem;

		// update the menu position
		formBuilderPanel.queryById('floatingItemMenu').updatePosition();
    },

    listeners: {
    	afterlayout: function () {
			var itemPanel = this;

    		// disabled/enable delete button
    		// var fieldDisplayPanel = itemPanel.formBuilderPanel.displayPanel;
    		// if (fieldDisplayPanel.items.items.length === 1) {
    			
    		// 	this.queryById('deleteButton').setDisabled(true);
    		// }
    		// else if (fieldDisplayPanel.items.items.length === 2) {
    		// 	this.queryById('deleteButton').setDisabled(false);
    		// }
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

    		// style the question input field
			//fieldContainer.items.items[0].items.items[0].el.dom.querySelector('input').style.fontSize = '32px';

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
						console.log("this.container", fieldContainer);

						var getPanel = function (divId, isChecking) {
							var regex = /ext\-comp\-[0-9]{4}/;
							if (isChecking) {
								return regex.exec(divId) !== null;	
							}
							else {
								return Ext.getCmp(regex.exec(divId)[0]);
							}
						};

						// if there is a valid target container:
						//		* swap the y coords of both containers
						//		* swap the location in the parent panel's items array
						if (info.target !== null) {

							// identify the active (container being dragged,) and the target container
							var activeContainer = getPanel(self._element.dom.id);
							var targetContainer = getPanel(info.target._element.dom.id);
							var formBuilderPanel = fieldContainer.up('[itemId=formBuilderPanel]');

							// swap positions in the items list
							var activeIndex = activeContainer.up().items.items.indexOf(activeContainer);
							var targetIndex = activeContainer.up().items.items.indexOf(targetContainer);

							// reset the y index of the active container, then swap the items.
							activeContainer.setY(fieldContainer.getY() - event.delta.y);
							formBuilderPanel.items.items[1].insert(targetIndex, activeContainer);
							formBuilderPanel.items.items[1].insert(activeIndex, targetContainer);

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
