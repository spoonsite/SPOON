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
			// title: 'Form Info',
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
			height: '100%',
			minHeight: '100%',
			maxHeight: '100%'
		}
	],

	recordItem: undefined,

	initComponent: function () {

		this.callParent();
		var formBuilderPanel = this;

		// TODO: query the template...
		// for each items in record... add FormBuilderItem...
		for (var i = 0; i < 10; i++) {
			formBuilderPanel.items.items[1].add(Ext.create('OSF.customSubmissionTool.FormBuilderItem'));
		}

		// add the form info items
		formBuilderPanel.items.items[0].add(
			[
				Ext.create('Ext.form.Panel', {
				 	title: 'Form Name <i class="fa fa-question-circle"  data-qtip="This is what the form template is identified by"></i>',
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
									// can use these when we actually save the form...
									// var formPanel = this.up();
									// var nameField = formPanel.queryById('formNameSaveField');
									this.setDisabled(true);
								}
							}
						}
					]
				}),
				Ext.create('Ext.panel.Panel', {
					title: 'Tools',
					width: '100%',
					flex: 9,
					items: [
						{
							xtype: 'container',
							layout: {
								type: 'vbox',
								pack: 'center'
							},
							height: 50,
							items: [
								{
									xtype: 'container',
									layout: {
										type: 'hbox',
										align: 'center',
										pack: 'center'
									},
									padding: 15,
									items: [
										{
											xtype: 'label',
											style: 'font-size: 1.25em; font-weight: bold;',
											text: 'Focus on active item ',
										},
										{
											xtype: 'button',
											text: '<i class="fa fa-eye fa-2x" aria-hidden="true"></i>',
											margin: '0 0 0 10',
											padding: 0,
											listeners: {
												click: function () {
													if (formBuilderPanel.activeItem) {
														formBuilderPanel.items.items[1].scrollTo(formBuilderPanel.activeItem.getX(), formBuilderPanel.activeItem.getY(), true);
													}
												}
											}
										}
									]
								},

								// other tools
							]
						}
					]
				})
			]
		);
	}
});