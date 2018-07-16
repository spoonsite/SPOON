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
					wpWindow.getWorkplanConfig().steps.push({
						name: 'Untitled Step',
						description: '',
						allowedRoles: [],
						actions: []
					});
					wpWindow.setSelectedStep(wpWindow.getWorkplanConfig().steps[0]);

					// alert parent and siblings of alert
					stepManager.alert();
				}
			}
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
			width: '90%',
			padding: 10,
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
		
		if (this.getWpWindow().getWorkplanConfig().steps.length === 0) {
			this.setHtml('This workplan has <b>no</b> steps yet<br />Add one');
			firstStepButton.show();
			stepsContainer.hide();
			addRemoveContainer.hide();
		}
		else {
			this.setHtml('');
			firstStepButton.hide();
			stepsContainer.show();
			addRemoveContainer.show();
			
			this.drawSteps();
		}
	},

	addStep: function () {
		console.log("TODO: add");
	},

	insertStep: function () {
		console.log("TODO: insert");
	},

	removeStep: function () {
		console.log("TODO: remove");
	},
	
	drawSteps: function () {
		
		var stepsContainer = this.getStepsContainer();
		var elementsToAdd = [];
		for (var i = 0; i < 25; i += 1) {
			elementsToAdd.push({

				xtype: 'container',
				style: 'overflow: visible; padding: 3px 0 3px 0;' + (i === 4 ? 'border: 2px dotted #ccc;' : ''),
				layout: {
					type: 'vbox',
					pack: 'center',
					align: 'middle'
				},
				width: 70,
				height: 90,
				items: [
					{
						xtype: 'container',
						html: (i !== 2 ? 'test' : 'test test test'),
						style: 'align-text: center;'
					},
					{
						xtype: 'button',
						text: ':::',
						style: 'border-radius: 40px;' + (i === 4 ? 'background: green;' : ''),
						width: 40,
						padding: 10
					}
				]
			});

			if (i !== 24) {
				elementsToAdd.push({
					xtype: 'container',
					style: 'padding-top: 10px;',
					html: '<i class="fa fa-2x fa-long-arrow-right" aria-hidden="true"></i>'
				});
			}
		}

		stepsContainer.removeAll(true);
		stepsContainer.add(elementsToAdd);
	},

	getStepsContainer: function () {
		return this.down('[itemId=stepsContainer]');
	}
});
