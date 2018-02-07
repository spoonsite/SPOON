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

Ext.define('OSF.customSubmissionTool.Window', {
	extend: 'Ext.window.Window',

	record: undefined,
	formBuilderPanel: undefined,

	maximizable: true,
	maximized: true,
	minWidth: 1000,
	minHeight: 800,
	modal: true,
	title: 'Form Builder Tool',

	initComponent: function () {
		
		this.callParent();
		
		if (typeof this.fromBuilderPanel === 'undefined') {
			this.formBuilderPanel = Ext.create('OSF.customSubmissionTool.FormBuilderPanel');
		}

		this.add(this.formBuilderPanel);
	}
});