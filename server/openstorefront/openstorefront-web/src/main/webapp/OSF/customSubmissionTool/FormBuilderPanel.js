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
	width: '100%',
	alignTarget: 'center',
	style: 'background: #6c6c6c',
	layout: {
		type: 'hbox',
		align: 'stretch'
	},
	items: [
		{
			title: 'Form Info',
			flex: 0.15,
			style: 'background: #fff; border-bottom: 8px solid #6c6c6c;',
			height: '100%'
		},
		{
			flex: 0.85,
			margin: '0 0 0 10',
			scrollable: 'vertical',
			style: 'background: #fff;',
			height: '100%',
			minHeight: '100%',
			maxHeight: '100%'
		}
	],

	record: undefined,

	initComponent: function () {

		this.callParent();
		console.log("FORM BUILDER PANEL: ", this);

		// TODO query the template...

		// for each items in record... add FormBuilderItem...
		for (var i = 0; i < 100; i++) {
			this.items.items[1].add(Ext.create('OSF.customSubmissionTool.FormBuilderItem'));
		}

	}
});