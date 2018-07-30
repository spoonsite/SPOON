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

Ext.define('OSF.workplanManagementTool.Window', {
	extend: 'Ext.window.Window',
	requires: [
		'OSF.workplanManagementTool.StepManagerPanel',
		'OSF.workplanManagementTool.WPFormPanel',
		'OSF.workplanManagementTool.StepFormPanel'
	],

	config: {
		workplanConfig: {},
		selectedStep: null,
		migrationsToPerform: []
	},
	layout: 'border',
	resizable: false,
	defaults: {
		split: true
	},
	items: [
		{
			xtype: 'osf.wp.stepManager',
			itemId: 'stepManager',
			region: 'north'
		},
		{
			xtype: 'osf.wp.form',
			itemId: 'workplanForm',
			region: 'west',
			collapsible: true,
			titleCollapse: true,
			scrollable: true
		},
		{
			xtype: 'osf.wp.stepForm',
			itemId: 'stepForm',
			region: 'center',
			width: '100%'
		}
	],
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'bottom',
			items: [
				{
					text: 'Save Workplan',
					itemId: 'saveWorkplanButton',
					iconCls: 'fa fa-2x fa-floppy-o icon-button-color-save icon-vertical-correction',
					handler: function () {
						var wpWindow = this.up('window');
						
						// set step order
						Ext.Array.forEach(wpWindow.getWorkplanConfig().steps, function (item, index) {
							wpWindow.getWorkplanConfig().steps[index].stepOrder = index + 1;
						});
						
						// Configure componentType for save
						Ext.Array.forEach(wpWindow.getWorkplanConfig().componentTypes, function (componentType, index) {
							wpWindow.getWorkplanConfig().componentTypes[index] = { componentType: componentType };
						});
						
						Ext.Array.forEach(wpWindow.getWorkplanConfig().steps, function (step, index) {
							// Configure stepRoles for save
							if (step.stepRole) {
								Ext.Array.forEach(step.stepRole, function (role, index) {
									step.stepRole[index] = { securityRole: role };
								});
							}
							else {
								step.stepRole = [];
							}
							
							// Configure triigerEvents for save
							if (step.triggerEvents) {
								Ext.Array.forEach(step.triggerEvents, function (event, index) {
									step.triggerEvents[index] = { entityEventType: event };
								});
							}
							else {
								step.triggerEvents = [];
							}

							// Configure emails in action (if there are any)
							if (step.actions) {
								Ext.Array.forEach(step.actions, function (action) {
									if (action.actionOption.fixedEmails) {
										Ext.Array.forEach(action.actionOption.fixedEmails, function (item, index) {
											action.actionOption.fixedEmails[index] = { email: item };
										});
									}
								});
							}
						});

						Ext.Ajax.request({
							method: wpWindow.getWorkplanConfig().workPlanId ? 'POST' : 'PUT',
							url: 'api/v1/resource/workplans',
							jsonData: wpWindow.getWorkplanConfig(),
							success: function (res) {
								wpWindow.close();
							}
						});
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
	],
	
	listeners: {
		show: function () {

			var saveButton = this.down('[itemId=saveWorkplanButton]').enable();
			if (this.getWorkplanConfig().steps.length > 0) {
				saveButton.enable();
			}
			else {
				saveButton.disable();
			}
		},
		resize: function () {

			this.down('[itemId=stepsContainer]').relativeWindowResize();
		},
		close: function () {
			this.stepForm.clearForm();
			Ext.getCmp('workplanGrid').getStore().load();
		}
	},
	
	initComponent: function () {
		
		this.callParent();

		this.stepManager = this.down('[itemId=stepManager]');
		this.workplanForm = this.down('[itemId=workplanForm]');
		this.stepForm = this.down('[itemId=stepForm]');
	},
	
	loadWorkplan: function (record) {

		var defaultConfig = {
			name: 'Untitled',
			workPlanType: 'COMPONENT',
			active: false,
			pendingColor: '#cccccc',
			inProgressColor: '777aea',
			completeColor: '84d053',
			subStatusColor: 'ff0000',
			componentTypes: [],
			steps: [],
			roleLogicCondition: 'OR',
			appliesToChildComponentTypes: true,
			defaultWorkPlan: false
		};

		// reset the last selected step (if re-opening the tool)
		this.setSelectedStep(null);
		
		// If no record was provided, us the defaultConfig
		record = Ext.apply(defaultConfig, record);
		this.unPackFieldsForLoading(record)

		this.setConfig(record);
		
		return this;
	},

	unPackFieldsForLoading: function (record) {

		// unpack component types
		Ext.Array.forEach(record.componentTypes, function (componentTypeObj, index) {
			record.componentTypes[index] = componentTypeObj.componentType;
		});

		// sort the record's steps
		record.steps.sort(function (a, b) {
			return a.stepOrder - b.stepOrder;
		});

		// unpack steps
		Ext.Array.forEach(record.steps, function (step, index) {
			if (step.stepRole) {
				step.stepRole = step.stepRole.map(function (item) {
					return item.securityRole;
				});
			}
			if (step.triggerEvents) {
				step.triggerEvents = step.triggerEvents.map(function (item) {
					return item.entityEventType;
				});
			}

			if (step.actions) {
				Ext.Array.forEach(step.actions, function (action) {
					if (action.actionOption.fixedEmails) {
						Ext.Array.forEach(action.actionOption.fixedEmails, function (item, index) {
							action.actionOption.fixedEmails[index] = item.email;
						});
					}
				});
			}
		});
	},

	setConfig: function () {

		var toggleAlert = true;

		if (arguments.length === 1) {
			this.workplanConfig = arguments[0];
		}
		else if (arguments.length === 2) {
			if (typeof this[arguments[0]] !== undefined) {
				this[arguments[0]] = arguments[1];
			}
			else {
				toggleAlert = false;
				console.error('"' + arguments[0] + '" is an invalid property of the object: ', this);
			}
		}
		else {
			toggleAlert = false;
			console.error('You have supplied an invalid number of parameters to the method: "setConfig"');
		}

		if (toggleAlert) {
			this.alertChildrenComponents();
		}
	},

	alertChildrenComponents: function () {
		this.stepManager.alertChange();
		this.workplanForm.alertChange();
		this.stepForm.alertChange();
	}
});
