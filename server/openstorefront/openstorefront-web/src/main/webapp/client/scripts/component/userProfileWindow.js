/* 
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
Ext.define('OSF.component.UserProfileWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.UserProfileWindow',
	title: 'User Profile',
	iconCls: 'fa fa-lg fa-user',
	layout: 'hbox',
	modal: true,
	width: '50%',
	alwaysOnTop: true,
	height: 385,
	initComponent: function () {
		this.callParent();

		var profileWindow = this;

		var profilePanel = Ext.create('OSF.component.UserProfilePanel', {
			width: '100%',
			saveCallback: profileWindow.saveCallback,
			profileWindow: profileWindow,
			extraTools: [
				{
					xtype: 'tbfill'
				},
				{
					text: 'Cancel',
					iconCls: 'fa fa-close',
					handler: function () {
						profileWindow.close();
					}
				}
			]
		});

		profileWindow.add(profilePanel);
	}

});

