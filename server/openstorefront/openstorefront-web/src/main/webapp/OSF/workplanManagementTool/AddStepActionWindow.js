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
		stepActionGrid: null,
		recordToLoad: null
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

				// enable save button if action order and action type fields are populated
				if (this.down('[itemId=actionOrderCombo]').getValue() && this.down('[itemId=actionTypeCombo]').getValue()) {
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
					editable: false,
					queryMode: 'local',
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
											name: 'fixedEmails',
											colspan: 2
										},
										{
											xtype: 'checkboxfield',
											name: 'emailOwner',
											fieldLabel: 'Email Owner',
											colspan: 2
										},
										{
											xtype: 'checkboxfield',
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
		var actionWindow = this;
		actionWindow.stepActionStore = actionWindow.getStepActionGrid().getStore();

		for (var i = 0; i < actionWindow.stepActionStore.getData().items.length + 1; i += 1) {
			actionWindow.down('[itemId=actionOrderCombo]').getStore().add({ name: i + 1, value: i + 1 });
		}

		actionWindow.down('[itemId=actionTypeCombo]').getStore().load();

		if (actionWindow.recordToLoad) {
			var form = actionWindow.down('form');

			for (key in actionWindow.recordToLoad.actionOption) {
				actionWindow.recordToLoad[key] = actionWindow.recordToLoad.actionOption[key];
			}

			var actionTypeCombo = form.getForm().getFields().items.filter(function (field) {
				if (field.name === 'workPlanStepActionType') {
					return field;
				}
			})[0];

			// apply the value to the action type combo, in case there are action options to be set
			actionTypeCombo.setValue(actionWindow.recordToLoad.workPlanStepActionType);

			// defer to allow time for the rest of the from to render
			Ext.defer(function () {
				form.getForm().setValues(actionWindow.recordToLoad);
			}, 500)
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
						var actionWindow = this.up('window');
						var form = actionWindow.down('form');
						var formValues = form.getValues();

						var recordToSave = {};
						recordToSave.actionOrder = formValues.actionOrder;
						recordToSave.workPlanStepActionType = formValues.workPlanStepActionType;
						
						delete formValues.actionOrder;
						delete formValues.workPlanStepActionType;
						recordToSave.actionOption = formValues;

						// Format emails
						Ext.Array.forEach(recordToSave.actionOption.fixedEmails, function (item, index) {
							recordToSave.actionOption.fixedEmails[index] = { email: item };
						});

						// if we are editing a record, update it
						if (actionWindow.recordToLoad) {
							var indexToInsert = -1;
							Ext.Array.forEach(actionWindow.stepActionStore.getData().items, function (record, index) {
								if (record == actionWindow.getStepActionGrid().getSelection()[0]) {
									indexToInsert = index;
								}
							});
							actionWindow.stepActionStore.removeAt(indexToInsert);
							actionWindow.stepActionStore.insert(indexToInsert, [recordToSave]);
						}
						// else add a new record
						else {
							actionWindow.stepActionStore.add(recordToSave);
						}

						actionWindow.close();
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
