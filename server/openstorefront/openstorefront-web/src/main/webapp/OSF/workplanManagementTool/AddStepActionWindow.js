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
/* global Ext, CoreUtil, CoreService, data */

Ext.define('OSF.workplanManagementTool.AddStepActionWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.osf.wp.AddStepActionWindow',

	config: {
		stepActionStore: null
	},

	title: 'Add Step Action',
	modal: true,
	padding: 10,
	scrollable: 'y',
	items: [
		{
			xtype: 'form',
			defaults: {
				labelAlign: 'top',
				width: '100%'
			},
			checkFormStatus: function () {
				if (this.down('[itemId=actionOrderCombo]').getValue() && this.down('[itemId=actionTypeCombo]').getValue()) {
					console.log("THIS: ", this);
					this.up('window').down('[itemId=saveButton]').enable();
				}
			},
			items: [
				{
					xtype: 'combo',
					fieldLabel: 'Action Order <span class="field-required" />',
					itemId: 'actionOrderCombo',
					allowBlank: false,
					queryMode: 'local',
					valueField: 'value',
					displayField: 'name',
					name: 'actionOrder',
					editable: false,
					store: {
						fields: ['name', 'value']
					},
					listeners: {
						change: function () {
							this.up('form').checkFormStatus();
						}
					}
				},
				{
					xtype: 'combo',
					fieldLabel: 'Action Type <span class="field-required" />',
					allowBlank: false,
					valueField: 'code',
					displayField: 'description',
					itemId: 'actionTypeCombo',
					name: 'workPlanStepActionType',
					autoLoad: true,
					editable: false,
					queryMode: 'remote',
					store: {
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/lookuptypes/WorkPlanStepActionType'
						}
					},
					listeners: {
						change: function (combo, newVal, oldVal) {
							
							this.up('form').checkFormStatus();
							var optionsContainer = combo.up('form').down('[itemId=actionOptionsContainer]');
							optionsContainer.removeAll(true);

							// handle assigning to groups or entries
							if (newVal === 'ASSIGN_ENTRY' || newVal === 'EMAIL') {
								optionsContainer.show();
								if (newVal === 'ASSIGN_ENTRY') {
									optionsContainer.add({
										xtype: 'fieldcontainer',
										defaultType: 'radiofield',
										defaults: {
											name: 'assignType'
										},
										items: [
											{
												boxLabel: 'Assign to User',
												inputValue: 'user',
												listeners: {
													change: function (self, newVal, oldVal) {

														var userGroupCombo = this.up('fieldset').down('[itemId=userGroupCombo]');
														console.log("COMBO: ", userGroupCombo);
														userGroupCombo.enable();
														if (newVal === true) {
															userGroupCombo.getStore().setProxy({
																type: 'ajax',
																url: 'api/v1/resource/userprofiles/lookup'
															});
															userGroupCombo.getStore().load();
															userGroupCombo.setFieldLabel('Assign to User');
															userGroupCombo.name = 'assignUser';
														}
													}
												}
											},
											{
												boxLabel: 'Assign to Group',
												inputValue: 'group',
												listeners: {
													change: function (self, newVal, oldVal) {

														var userGroupCombo = this.up('fieldset').down('[itemId=userGroupCombo]');
														userGroupCombo.enable();
														if (newVal === true) {
															userGroupCombo.getStore().setProxy({
																type: 'ajax',
																url: 'api/v1/resource/securityroles/lookup'
															});
															userGroupCombo.getStore().load();
															userGroupCombo.setFieldLabel('Assign to Group');
															userGroupCombo.name = 'assignGroup';
														}
													}
												}
											}
										]
									});
									optionsContainer.add({
										xtype: 'combo',
										itemId: 'userGroupCombo',
										valueField: 'code',
										displayField: 'description',
										forceSelection: true,
										disabled: true,
										labelAlign: 'top',
										width: 400,
										style: 'float: right; padding-right: 25%;',
										store: {}
									});
								}

								// handle emailing to user(s), and/or groups, and (if desired) the record owner
								else {
									optionsContainer.add([
										{
											xtype: 'UserCustomEmailCombo',
											labelAlign: 'top',
											fieldLabel: 'Individual Email Recipients (?)',
											colspan: 2
										},
										{
											xtype: 'checkbox',
											name: 'emailOwner',
											fieldLabel: 'Email Owner',
											colspan: 2
										},
										{
											xtype: 'checkbox',
											name: 'emailEntryTypeGroup',
											fieldLabel: 'Email Entry Type Group',
											colspan: 2
										},
										{
											xtype: 'tinymce_textarea',
											name: 'emailMessage',
											width: '100%',
											height: 350,
											colspan: 2
										}
									]);
								}
							}

							else {
								optionsContainer.hide();
							}
						}
					}
				},
				{
					xtype: 'fieldset',
					title: 'Action Options',
					itemId: 'actionOptionsContainer',
					cls: 'action-options-fieldset',
					hidden: true,
					layout: {
						type: 'table',
						columns: 2
					},
					items: []
				}
			],
		}
	],
	initComponent: function () {
		this.callParent();

		for (var i = 0; i < this.getStepActionStore().getData().items.length + 1; i += 1) {
			this.down('[itemId=actionOrderCombo]').getStore().add({ name: i + 1, value: i + 1 });
		}
	},
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'bottom',
			items: [
				{
					text: 'Save',
					itemId: 'saveButton',
					disabled: true,
					iconCls: 'fa fa-2x fa-floppy-o icon-button-color-save icon-vertical-correction',
					handler: function () {
						// TODO: SAVE
					}
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Cancel',
					iconCls: 'fa fa-2x fa-close icon-button-color-warning icon-vertical-correction',
					handler: function () {
						this.up('window').close();
					}
				}
			]
		}
	]
});
