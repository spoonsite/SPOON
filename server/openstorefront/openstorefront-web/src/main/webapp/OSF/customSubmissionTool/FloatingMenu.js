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
	eight: 275,
	hidden: true,
	style: 'background: rgba(200,200,200,0.8);',
	defaultType: 'button',
	defaults: {
		width: '100%',
		style: 'border: none; background: none; color: rgb(200,200,200);'
	},
	layout: 'vbox',
	getFormBuilderPanel: function () {

		return this.formBuilderPanel || this.up('[itemId=formBuilderPanel]');
	},
	updatePosition: function () {

		var formBuilderPanel = this.getFormBuilderPanel();
		if (this.hidden) {
			this.setHidden(false);
		}
		if (formBuilderPanel.activeItem) {
			this.setY(formBuilderPanel.activeItem.getY(), true);
		}
	},
	items: [
		{
			text: '<i style="color:#5f5f5f;" class="fa fa-plus-circle fa-2x" aria-hidden="true" data-qtip="Add a field"></i>',
			flex: 1,
			handler: function() {

				// // add a field after the current and set as active
				var formBuilderPanel = this.up('[itemId=floatingMenu]').getFormBuilderPanel();
				var fieldIndex = formBuilderPanel.itemContainer.items.items.indexOf(formBuilderPanel.activeItem);
				var newFormBuilderItem = Ext.create('OSF.customSubmissionTool.FormBuilderItem', {
					formBuilderPanel: formBuilderPanel
				});

				formBuilderPanel.itemContainer.insert(fieldIndex+1, newFormBuilderItem);
				newFormBuilderItem.setActiveFormItem(newFormBuilderItem);
			}
		},
		{
			text: '<i style="color:#5f5f5f;" class="fa fa-clone fa-2x" aria-hidden="true" data-qtip="Copy a field"></i>',
			flex: 1,
			handler: function() {
				
			}					
		},
		{
			text: '<i style="color:#5f5f5f;" class="fa fa-quote-right fa-2x" aria-hidden="true" data-qtip="Add a paragraph section"></i>',
			flex: 1,
			handler: function() {
				
			}					
		},				
		{
			text: '<i style="color:#5f5f5f;" class="fa fa-minus fa-2x" aria-hidden="true" data-qtip="Add a horizontal rule"></i>',
			flex: 1,
			handler: function() {
				
			}					
		},
		{
			text: '<i style="color:#5f5f5f;" class="fa fa-picture-o fa-2x" aria-hidden="true" data-qtip="Add media"></i>',
			flex: 1,
			handler: function() {
				
			}					
		},
		{
			text: '<i style="color:#5f5f5f;" class="fa fa-trash fa-2x" aria-hidden="true" data-qtip="Delete section"></i>',
			flex: 1,
			itemId: 'deleteButton',
			handler: function() {

				// delete formBuilderItem
				var formBuilderPanel = this.up('[itemId=floatingMenu]').getFormBuilderPanel();
				var activeItem = formBuilderPanel.activeItem;

				if (activeItem) {
					activeItem.destroy();
					formBuilderPanel.activeItem = null;
				}

				// hide floating menu
				formBuilderPanel.floatingMenu.setHidden(true);

			}					
		},
		{
			text: '<i style="color:#5f5f5f;" class="fa fa-ellipsis-v fa-2x" aria-hidden="true" data-qtip="More options"></i>',
			flex: 1,
			listeners: {
				click: function () {
					var button = this;
					var disabledUp = false;
					var disabledDown = false;
					var formBuilderPanel = button.up('[itemId=floatingMenu]').getFormBuilderPanel();

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
	]
});
