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

Ext.define('OSF.customSubmissionTool.ItemMenu', {
	extend: 'Ext.Container',

	layout: 'column',
	initComponent: function () {

		this.callParent();

		// Add field type combo...
		this.items.items[0].add(Ext.create('Ext.form.ComboBox', {
			fieldLabel: 'Field Type',
		    store: Ext.create('Ext.data.Store', {
			    fields: ['value', 'label'],
			    data : [
			        {"value":"grid", "label":"Grid"},
			        {"value":"radiobtn", "label":"Radio Button"},
			        {"value":"checkbox", "label":"Checkbox"},
			        {"value":"combo", "label":"Dropdown"},
			        {"value":"tf", "label":"Text Field"},
			        {"value":"ta", "label":"Text Area"},
			        {"value":"contactsgrid", "label":"Contacts Grid"}
			    ]
			}),
		    queryMode: 'local',
		    displayField: 'label',
		    valueField: 'value'
		}));

		// add is optional radio
		this.items.items[0].add(Ext.create('Ext.form.FieldContainer', {
            fieldLabel: 'Is Required?',
            defaultType: 'radiofield',
            defaults: {
                flex: 1
            },
            layout: 'hbox',
            items: [
            	{
            		boxLabel: 'Yes',
            		name: 'isRequired',
            		inputValue: 'yes'
            	},
            	{

            		boxLabel: 'No',
            		name: 'isRequired',
            		inputValue: 'no',
            		value: true,
            		margin: '0 0 0 10'
            	}
            ]
		}));

		// Add mapped to combo...
		this.items.items[0].add(Ext.create('Ext.form.ComboBox', {
			fieldLabel: 'Mapped To',
		    store: Ext.create('Ext.data.Store', {
			    fields: ['value', 'label'],
			    data : [
			        {"value":"name", "label":"Contact Name"},
			        {"value":"phone", "label":"Contact Phone"},
			        {"value":"email", "label":"Contact Email"},
			        {"value":"attr", "label":"Attribute"},
			        {"value":"private", "label":"Is Private"},
			        {"value":"description", "label":"Entry Description"},
			        {"value":"summary", "label":"Entry Summary"}
			    ]
			}),
		    queryMode: 'local',
		    displayField: 'label',
		    valueField: 'value'
		}));
	},
	items: [
		{
			columnWidth: 0.5,
			margin: '0 30 0 0',
			items: [
			]
		},
		{
			columnWidth: 0.5,
			items: [
				{
					xtype: 'textfield',
					style: 'float: right;',
					fieldLabel: 'Label Code'
				},
				{
					xtype: 'textfield',
					style: 'float: right;',
					fieldLabel: 'Label Tip'
				},
				{
					xtype: 'button',
					style: 'float: right;',
					text: 'Additional Settings <i style="margin-left: 5px;" class="fa fa-cog" aria-hidden="true"></i>',
					listeners: {
						click: function () {
							Ext.create('Ext.window.Window', {
								title: 'Additional Field Settings',
								modal: true,
								html: '<h1>TODO: Dynamically load in fields here for specific fields.</h1>'
							}).show();
						}
					}
				}
			]
		}
	]
});
