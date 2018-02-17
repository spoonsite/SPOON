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
	requires: ['OSF.customSubmissionTool.FormBuilderPanel'],

	config: {
		recordItem: undefined
	},
	formBuilderPanel: undefined,

	maximizable: true,	
	width: '90%',
	height: '90%',
	
	modal: true,
	title: 'Form Builder Tool',
	iconCls: 'fa fa-edit',
	layout: 'fit',
	closeAction: 'destroy',
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'bottom',
			items: [
				{
					text: 'Save',
					scale: 'medium',
					iconCls: 'fa fa-2x fa-save icon-button-color-save icon-vertical-correction',
					handler: function() {
						
					}
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Preview',
					scale: 'medium',
					iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction',
					handler: function() {
						
					}					
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Close',
					scale: 'medium',
					iconCls: 'fa fa-2x fa-close icon-button-color-warning icon-vertical-correction',
					handler: function() {
						
						this.up('window').close();
					}
				}				
			]
		}
	],
	initComponent: function () {
		this.callParent();
		var csfWindow = this;

		if (!csfWindow.formBuilderPanel) {
			csfWindow.formBuilderPanel = Ext.create('OSF.customSubmissionTool.FormBuilderPanel', {
				templateRecord: csfWindow.recordItem
			});
		}

		csfWindow.add(csfWindow.formBuilderPanel);
	}

});
