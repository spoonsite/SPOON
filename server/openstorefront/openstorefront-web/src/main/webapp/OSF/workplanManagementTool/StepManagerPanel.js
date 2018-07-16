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
	height: 180,
	layout: {
		type: 'vbox',
		pack: 'center',
		align: 'middle'
	},
	items: [
		{
			xtype: 'button',
			text: 'Create Step',
			itemId: 'createFirstStepButton',
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
			html: 'STEPS!!!!!!',
			hidden: true
		}
	],

	alertChange: function () {
		
		console.log("THIS (Step manager Panel)", this);

		if (this.getWpWindow().getWorkplanConfig().steps.length === 0) {
			this.setHtml('This workplan has <b>no</b> steps yet<br />Add one');
			this.down('[itemId=createFirstStepButton]').show();
			this.down('[itemId=stepsContainer]').hide();
		}
		else {
			this.setHtml('');
			this.down('[itemId=createFirstStepButton]').hide();
			this.down('[itemId=stepsContainer]').show();
		}
	}
});
