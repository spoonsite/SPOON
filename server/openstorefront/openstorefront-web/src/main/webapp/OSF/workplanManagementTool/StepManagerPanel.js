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

Ext.define('OSF.workplanManagementTool.StepManagerPanel', {
	extend: 'OSF.workplanManagementTool.WPDefaultPanel',
	alias: 'widget.osf.wp.stepManager',
	requires: [
		'OSF.workplanManagementTool.StepMigrationWindow'
	],

	itemId: 'stepManager',
	style: 'background: #fff; text-align: center;',
	title: 'Step Manager',
	height: 160,
	layout: {
		type: 'hbox',
		pack: 'center',
		align: 'stretch'
	},
	items: [
		{
			xtype: 'button',
			text: 'Create Step',
			itemId: 'createFirstStepButton',
			style: 'margin: 3.5em 0 2em 0;',
			hidden: true,
			listeners: {
				click: function () {

					var stepManager = this.up('[itemId=stepManager]');
					var wpWindow = stepManager.getWpWindow();
					
					stepManager.addStep();
					wpWindow.setSelectedStep(wpWindow.getWorkplanConfig().steps[0]);

					// alert parent and siblings of alert
					stepManager.alert();
				}
			}
		},
		{
			xtype: 'container',
			itemId: 'stepsLegendContainer',
			hidden: true,
			width: '5%',
			padding: '0 0 0 10',
			style: 'border-right: 1px solid #ccc',
			layout: {
				type: 'vbox',
				pack: 'center'
			},
			defaults: {
				margin: '0 0 10 0'
			},
			items: [
				{
					xtype: 'container',
					html: '<div style="width: 100%;"><div class="wp-step-lengend wp-step-new"></div>&nbsp;<b>New</b></div>'
				},
				{
					xtype: 'container',
					html: '<div style="width: 100%;"><div class="wp-step-lengend wp-step-existing"></div>&nbsp;<b>Existing</b></div>'
				},
				{
					xtype: 'container',
					html: '<div style="width: 100%;"><div class="wp-step-lengend wp-step-migrated"></div>&nbsp;<b>Migrated</b></div>'
				},
				{
					xtype: 'container',
					html: '<div style="width: 100%;"><div class="wp-step-lengend wp-step-active"></div>&nbsp;<b>Active</b></div>'
				}
			]
		},
		{
			xtype: 'container',
			itemId: 'stepsContainer',
			layout: {
				type: 'hbox',
				pack: 'center',
				align: 'middle'
			},
			hidden: true,
			scrollable: 'x',
			height: '100%',
			width: '85%',
			padding: '10 0 10 50',
			cls: 'step-container',
			listeners: {
				render: function () {
					var stepsContainer = this;

					// set up horizontal scrolling
					stepsContainer.el.on('mousewheel', function (e) {
						e.preventDefault();

						if (e.getWheelDeltas().y < 0) {
							stepsContainer.el.scrollTo('left', stepsContainer.el.getScroll().left + 20)
						}
						else {
							stepsContainer.el.scrollTo('left', stepsContainer.el.getScroll().left - 20)
						}
					});
				}
			}
		},
		{
			itemId: 'addRemoveStepContainer',
			hidden: true,
			xtype: 'container',
			width: '10%',
			style: 'border-left: 1px solid #ccc;',
			padding: 5,
			defaults: {
				listeners: {
					click: function () {

						var stepManager = this.up('[itemId=stepManager]');
						switch (this.clickAction) {
							case 'add':
								stepManager.addStep();
								break;
							case 'insert':
								stepManager.insertStep();
								break;

							case 'remove':
								stepManager.removeStep();
								break;
							default:
						}
					}
				}
			},
			items: [
				{
					xtype: 'button',
					text: 'Add Step',
					iconCls: 'fa fa-2x fa-plus icon-vertical-correction',
					clickAction: 'add'
				},
				{
					xtype: 'button',
					text: 'Insert Step',
					iconCls: 'fa fa-2x fa-level-down icon-vertical-correction',
					margin: 5,
					clickAction: 'insert'
				},
				{
					xtype: 'button',
					text: 'Remove Step',
					iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
					clickAction: 'remove'
				}
			]
		}
	],
	
	alertChange: function () {
		
		console.log("THIS (Step manager Panel)", this);
		var firstStepButton = this.down('[itemId=createFirstStepButton]');
		var stepsContainer = this.getStepsContainer();
		var addRemoveContainer = this.down('[itemId=addRemoveStepContainer]');
		var stepsLegendContainer = this.down('[itemId=stepsLegendContainer]');
		
		if (this.getWpWindow().getWorkplanConfig().steps.length === 0) {
			this.setHtml('This workplan has <b>no</b> steps yet<br />Add one');
			firstStepButton.show();
			stepsContainer.hide();
			addRemoveContainer.hide();
			stepsLegendContainer.hide();
		}
		else {
			this.setHtml('');
			firstStepButton.hide();
			stepsContainer.show();
			addRemoveContainer.show();
			stepsLegendContainer.show();
			
			this.drawSteps();
		}
	},

	addStep: function () {

		this.getWpWindow().getWorkplanConfig().steps.push(this.getDefaultStep());
		this.alert('stepManager');
	},

	insertStep: function () {

		var wpWindow = this.getWpWindow();
		var selectedStepIndex = wpWindow.getWorkplanConfig().steps.indexOf(wpWindow.getSelectedStep());

		// insert an empty step AFTER the currently selected step
		wpWindow.getWorkplanConfig().steps.splice(selectedStepIndex + 1, 0, this.getDefaultStep());
		this.alert('stepManager');
	},

	removeStep: function () {

		var wpWindow = this.getWpWindow();
		var selectedStepIndex = wpWindow.getWorkplanConfig().steps.indexOf(wpWindow.getSelectedStep());
		var stepManager = this;

		var remove = function () {
			wpWindow.setSelectedStep(null);
			wpWindow.getWorkplanConfig().steps.splice(selectedStepIndex, 1);
			stepManager.alert();
		};

		if (wpWindow.getSelectedStep().isNewStep) {
			remove();
		}
		else {
			Ext.create({
				xtype: 'osf.wp.migrationwindow',
				wpWindow: wpWindow,
				onMigrate: function () {
					remove();
				}
			}).show();
		}
		
	},

	getDefaultStep: function () {
		return {
			name: 'Untitled Step - ' + (this.getWpWindow().getWorkplanConfig().steps.length + 1),
			description: '',
			allowedRoles: [],
			actions: [],
			isNewStep: true,
			isMigratedTo: false,
			stepId: CoreUtil.uuidv4()
		}
	},
	
	drawSteps: function () {
		
		var stepsContainer = this.getStepsContainer();
		var elementsToAdd = [];
		var wpWindow = this.getWpWindow();

		Ext.Array.forEach(wpWindow.getWorkplanConfig().steps, function (item, index) {
			elementsToAdd.push({
				xtype: 'dataview',
				itemSelector: 'div.step-view',
				stepRecord: item,
				store: {
					fields: ['name'],
					data: [ { name: item.name } ]
				},
				listeners: {
					itemmousedown: function (dataView) {
						
						var wpWindow = dataView.up('window');
						wpWindow.setSelectedStep(dataView.stepRecord);
					},
					itemmouseup: function (dataview) {

						var wpWindow = this.up('window');
						wpWindow.alertChildrenComponents();
					}
				},
				itemTpl:'<div class="step-view-container">' +
							'<span>{name}</span>' +
							'<div ' +
								'class="step-view ' + (index === wpWindow.getWorkplanConfig().steps.length - 1 ? 'last-step ' : ' ') +
								(item === wpWindow.getSelectedStep() ? 'wp-step-active ' : ' ') +
								(item.isNewStep && item !== wpWindow.getSelectedStep() ? 'wp-step-new ' : ' ') +
								(!item.isNewStep && !item.isMigratedTo && item !== wpWindow.getSelectedStep() ? 'wp-step-existing ' : ' ') +
								(!item.isNewStep && item.isMigratedTo && item !== wpWindow.getSelectedStep() ? 'wp-step-migrated ' : ' ') +
							'"></div>' +
						'</div>'
			});
		});

		stepsContainer.removeAll(true);
		stepsContainer.add(elementsToAdd);
	},

	getStepsContainer: function () {
		return this.down('[itemId=stepsContainer]');
	}
});
