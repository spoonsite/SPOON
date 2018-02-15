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

Ext.define('OSF.customSubmissionTool.FormBuilderPanel', {
	extend: 'Ext.panel.Panel',
	itemId: 'formBuilderPanel',
	width: '100%',
	alignTarget: 'center',
	style: 'background: #6c6c6c',
	activeItem: null,
	layout: {
		type: 'hbox',
		align: 'stretch'
	},
	items: [
		{
			flex: 0.25,
			style: 'background: #fff;',
			height: '100%',
			layout: {
				type: 'vbox'
			}
		},
		{
			flex: 0.75,
			margin: '0 0 0 10',
			scrollable: 'vertical',
			style: 'background: #fff;',
			itemId: 'fieldDisplayPanel',
			height: '100%',
			minHeight: '100%',
			maxHeight: '100%',
			dockedItems: [{
				xtype: 'toolbar',
				dock: 'bottom',
				ui: 'footer',
				fixed: true,
				layout: {
					type: 'vbox',
					align: 'center'
				},
				items: [
					{
						xtype: 'container',
						// <<, <, >, and >>
						items: [
							{
								xtype: 'button',
								text: '<i style="color:#5f5f5f;" class="fa fa-angle-double-left fa-2x" aria-hidden="true"></i>',
								overCls: 'csf-meta-menu-item',
								style: 'border: none; background: none;',
								cls: 'csf-meta-menu-item'
							},
							{
								xtype: 'button',
								text: '<i style="color:#5f5f5f;" class="fa fa-angle-left fa-2x" aria-hidden="true"></i>',
								overCls: 'csf-meta-menu-item',
								style: 'border: none; background: none;',
								cls: 'csf-meta-menu-item'
							},
							{
								xtype: 'button',
								text: '<i style="color:#5f5f5f;" class="fa fa-angle-right fa-2x" aria-hidden="true"></i>',
								overCls: 'csf-meta-menu-item',
								style: 'border: none; background: none;',
								cls: 'csf-meta-menu-item'
							},
							{
								xtype: 'button',
								text: '<i style="color:#5f5f5f;" class="fa fa-angle-double-right fa-2x" aria-hidden="true"></i>',
								overCls: 'csf-meta-menu-item',
								style: 'border: none; background: none;',
								cls: 'csf-meta-menu-item'
							}
						]
					}
				]
			}]
		}
	],

	recordItem: undefined,

	initComponent: function () {

		this.callParent();
		var formBuilderPanel = this;

		// TODO: query the template...
		// for each items in record... add FormBuilderItem...
		formBuilderPanel.items.items[1].add(Ext.create('OSF.customSubmissionTool.FormBuilderItem'));

		// add the form info items
		formBuilderPanel.items.items[0].add(
			[
				Ext.create('Ext.form.Panel', {
				 	title: 'Form Name <i class="fa fa-question-circle" data-qtip="This is what the form template is identified by"></i>',
				    bodyPadding: 5,
				    width: '100%',
				    flex: 1,
				    layout: {
				        type: 'hbox',
				        align: 'center',
				        pack: 'center'
				    }, 
					items: [
						{
							xtype: 'textfield',
							itemId: 'formNameSaveField',
							trackResetOnLoad: true,
							height: '32px',
							flex: 8,
							value: formBuilderPanel.recordItem.formName,
							listeners: {
								change: function () {
									var formPanel = this.up();
									var saveNameButton = formPanel.queryById('formNameSaveButton');
									if (saveNameButton.isDisabled()) {
										saveNameButton.setDisabled(false);
									}
								}
							}
						},
						{
							xtype: 'button',
							itemId: 'formNameSaveButton',
							iconCls: 'fa fa-floppy-o',
							height: '32px',
							layout: {
								pack: 'center'
							},
							style: 'border-left: none;',
							disabled: true,
							flex: 2,
							listeners: {
								click: function () {
									// TODO: save name!

									console.log("Save form name.");
									this.setDisabled(true);
								}
							}
						}
					]
				}),
				Ext.create('Ext.panel.Panel', {
					title: 'Navigation',
					width: '100%',
					flex: 3,
					items: [
						{
							xtype: 'treelist',
							store: {
								root: {
									expanded: true,
									children: [
										{
											text: 'Page 1',
											expanded: true,
											iconCls: 'fa fa-file-text',
											children: [
												{
													text: 'Is private',
													iconCls: 'fa fa-dot-circle-o',
													leaf: true
												},
												{
													text: 'Summary',
													iconCls: 'fa fa-minus',
													leaf: true
												},
												{
													text: 'Contacts',
													iconCls: 'fa fa-table',
													leaf: true
												},
											]
										},
										{
											text: 'Page 2',
											expanded: false,
											iconCls: 'fa fa-file-text',
											children: [
												{
													text: 'Is private',
													iconCls: 'fa fa-dot-circle-o',
													leaf: true
												},
												{
													text: 'Summary',
													iconCls: 'fa fa-minus',
													leaf: true
												},
												{
													text: 'Contacts',
													iconCls: 'fa fa-table',
													leaf: true
												},
											]
										},
										{
											text: 'Page 3',
											expanded: false,
											iconCls: 'fa fa-file-text',
											children: [
												{
													text: 'Is private',
													iconCls: 'fa fa-dot-circle-o',
													leaf: true
												},
												{
													text: 'Summary',
													iconCls: 'fa fa-minus',
													leaf: true
												},
												{
													text: 'Contacts',
													iconCls: 'fa fa-table',
													leaf: true
												},
											]
										}
									]
								}
							}
						}
					]
				}),

				Ext.create('Ext.panel.Panel', {
					title: 'Form Progress',
					width: '100%',
					flex: 6,
					layout: 'fit',
					items: [
						{
							xtype: 'grid',
							scrollable: 'y',
							columns: [
								{text: 'Entry Field', dataIndex: 'nameMapping', flex: 6},
								{
									text: 'Required',
									dataIndex: 'required',
									flex: 4,
									renderer: function (value) {
										if (value === 'Required') {
											return '<i style="color:red;" class="fa fa-check" aria-hidden="true"></i>';
										}
										return '';
									}
								},
								{
									text: 'Eligible Fields',
									dataIndex: 'fieldTypes',
									flex: 5,
									renderer: function (fieldArray) {

										// should be an array of strings - field types.
										var iconString = '';
										if (typeof fieldArray === 'object' && fieldArray.length) {
											Ext.Array.forEach(fieldArray, function (field, index) {
												switch (field) {
													case 'textfield':
														iconString += '<i class="fa fa-minus" aria-hidden="true" data-qtip="Short Answer"></i>&nbsp;&nbsp;';
														break;

													case 'textarea':
														iconString += '<i class="fa fa-align-left" aria-hidden="true" data-qtip="Long Answer"></i>&nbsp;&nbsp;';
														break;

													case 'radio':
														iconString += '<i class="fa fa-dot-circle-o" aria-hidden="true" data-qtip="Radio"></i>&nbsp;&nbsp;';
														break;

													case 'checkbox':
														iconString += '<i class="fa fa-check-square-o" aria-hidden="true" data-qtip="Checkbox"></i>&nbsp;&nbsp;';
														break;

													case 'dropdown':
														iconString += '<i class="fa fa-chevron-circle-down" aria-hidden="true" data-qtip="Dropdown"></i>&nbsp;&nbsp;';
														break;

													case 'grid':
														iconString += '<i class="fa fa-table" aria-hidden="true" data-qtip="Grid"></i>&nbsp;&nbsp;';
														break;

													default:

												}
											});
										}

										return iconString;
									}
								}
							],
							store: Ext.create('Ext.data.Store', {
								sortInfo: { field: "required", direction: "ASC" },
								data: [
									{nameMapping: 'Description', required: 'Required', fieldTypes: ['textarea','textfield']},
									{nameMapping: 'Summary', required: 'Required', fieldTypes: ['textfield','textarea']},
									{nameMapping: 'Entry Type', required: 'Required', fieldTypes: ['dropdown','radio','checkbox']},
									{nameMapping: 'Name', required: 'Required', fieldTypes: ['textarea','textfield']},
									{nameMapping: 'GUID', required: 'Not Required', fieldTypes: ['grid']},
									{nameMapping: 'Version', required: 'Not Required', fieldTypes: ['checkbox', 'radio', 'grid']},
									{nameMapping: 'Data Source', required: 'Not Required', fieldTypes: ['radio','dropdown','checkbox']},
									{nameMapping: 'Release Date', required: 'Not Required', fieldTypes: ['grid', 'radio']},
									{nameMapping: 'Cool', required: 'Not Required', fieldTypes: ['textfield','textarea']},
									{nameMapping: 'Useful', required: 'Not Required', fieldTypes: ['radio','checkbox','dropdown']},

									{nameMapping: 'Version', required: 'Required', fieldTypes: ['textfield','textarea']},
									{nameMapping: 'GUID test 1', required: 'Not Required', fieldTypes: ['textfield','textarea']},
									{nameMapping: 'Summary test 1', required: 'Not Required', fieldTypes: ['dropdown','radio','checkbox']},
									{nameMapping: 'Name test 1', required: 'Not Required', fieldTypes: ['textfield','textarea']},
									{nameMapping: 'Summary test 2', required: 'Not Required', fieldTypes: ['textfield','textarea']},
									{nameMapping: 'GUID test 2', required: 'Required', fieldTypes: ['textfield','textarea']},
									{nameMapping: 'Cool test 1', required: 'Required', fieldTypes: ['grid', 'radio']},
									{nameMapping: 'Entry Type test 1', required: 'Not Required', fieldTypes: ['textfield','textarea']},
									{nameMapping: 'Name test 2', required: 'Not Required', fieldTypes: ['textfield','textarea']},
									{nameMapping: 'GUID test 3', required: 'Not Required', fieldTypes: ['textfield','textarea']},
									{nameMapping: 'Version test 1', required: 'Not Required', fieldTypes: ['dropdown','radio','checkbox']},
									{nameMapping: 'Entry Type test 2', required: 'Not Required', fieldTypes: ['textfield','textarea']},
									{nameMapping: 'GUID test 4', required: 'Required', fieldTypes: ['grid', 'radio']},
									{nameMapping: 'Summary test 3', required: 'Not Required', fieldTypes: ['dropdown','radio','checkbox']},
									{nameMapping: 'Entry Type test 3', required: 'Not Required', fieldTypes: ['textfield','textarea']},
									{nameMapping: 'Name test 3', required: 'Not Required', fieldTypes: ['textfield','textarea']},
									{nameMapping: 'Entry Type test 4', required: 'Not Required', fieldTypes: ['textfield','textarea']}
								],
								fields: ['nameMapping', 'required']
							})
						}
					]
				})
			]
		);
	}
});