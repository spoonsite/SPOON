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

Ext.define('OSF.workplanManagementTool.StepFormPanel', {
	extend: 'OSF.workplanManagementTool.WPDefaultPanel',
	alias: 'widget.osf.wp.stepForm',

	style: 'background: #fff; text-align: center;',
	title: 'Step Configuration',
	layout: {
		type: 'vbox',
		pack: 'center',
		align: 'middle'
	},
	items: [
		{
			xtype: 'container',
			itemId: 'defaultContainer',
			style: 'border: 1px solid #ccc; border-radius: 5px; padding: 15px;',
			html: '<span style="font-size: 20px;">You can modify step configurations here<br />Select or create a step <b>above</b> to continue</span>',
			hidden: true
		},
		{
			xtype: 'container',
			itemId: 'stepFormContainer',
			hidden: true
		}
	],

	alertChange: function () {

		var defaultContainer = this.down('[itemId=defaultContainer]');
		var stepFormContainer = this.down('[itemId=stepFormContainer]');
		if (this.getWpWindow().getSelectedStep() === null) {
			defaultContainer.show();
			stepFormContainer.hide();
		}
		else {
			defaultContainer.hide();
			stepFormContainer.show();
			stepFormContainer.setHtml(this.getWpWindow().getSelectedStep().name);
		}
	}
});
