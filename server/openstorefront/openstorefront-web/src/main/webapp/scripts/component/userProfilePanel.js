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
/* global Ext, CoreService */

Ext.define('OSF.component.UserProfilePanel', {
	extend: 'Ext.form.Panel',
	alias: 'osf.widget.UserProfilePanel',
	bodyStyle: 'padding: 20px;',
	extraTools: [],
	defaults: {
		labelAlign: 'right'
	},
	initComponent: function () {
		this.callParent();

		var profileForm = this;
		
		var formItems = [
			{
				xtype: 'displayfield',
				fieldLabel: 'Username',
				name: 'username',
				submitValue: true
			},
			{
				xtype: 'hidden',
				name: 'guid'
			},
			{
				xtype: 'textfield',
				name: 'firstName',
				fieldLabel: 'First Name <span class="field-required" />',
				labelSeparator: '',
				width: '100%',
				maxLength: 80,
				allowBlank: false
			},
			{
				xtype: 'textfield',
				name: 'lastName',
				fieldLabel: 'Last Name <span class="field-required" />',
				labelSeparator: '',
				width: '100%',
				maxLength: 80,
				allowBlank: false
			},
			{
				xtype: 'panel',
				layout: 'hbox',
				padding: '0 0 5 0',
				defaults: profileForm.defaults,
				items: [
					{
						xtype: 'textfield',
						name: 'email',
						inputType: 'email',						
						fieldLabel: 'Email <span class="field-required" />',
						labelSeparator: '',
						flex: 1,
						maxLength: 1000,
						allowBlank: false
					},
					{
						xtype: 'button',
						width: '175',
						margin: profileForm.defaults ? profileForm.defaults.labelAlign === 'top' ? '25 0 0 0' : '0 0 0 0' : '0 0 0 0',
						text: 'Send Test Message',
						iconCls: 'fa fa-envelope',
						maxWidth: 175,
						handler: function(){
							var user = this.up('form').getForm().findField('username');
							var email = this.up('form').getForm().findField('email');
							if (email.getValue()) {
								Ext.Ajax.request({
									url: 'api/v1/resource/userprofiles/' + user.getValue() + '/test-email',
									method: 'POST',									
									rawData: email.getValue(),
									success: function(){
										Ext.toast('Sent test email message to: ' + email.getValue());
									}
								});
							} else {
								Ext.Msg.show({
									title: 'Validation',
									message: 'A email address is required.',
									buttons: Ext.Msg.OK,
									icon: Ext.Msg.ERROR,
									fn: function(btn) {
									}
								});								
							}
						}
					}
				]
			},
			{
				xtype: 'textfield',
				name: 'phone',
				fieldLabel: 'Phone',
				width: '100%',
				maxLength: 80
			},
			Ext.create('OSF.component.StandardComboBox', {
				name: 'organization',
				allowBlank: false,
				margin: '0 0 5 0',
				width: '100%',
				maxLength: 120,
				fieldLabel: 'Organization <span class="field-required" />',
				forceSelection: false,
				valueField: 'description',
				storeConfig: {
					url: 'api/v1/resource/organizations/lookup'
				}
			}),
			Ext.create('OSF.component.StandardComboBox', {
				itemId: 'userTypeCodeCB',
				name: 'userTypeCode',
				allowBlank: false,
				margin: '0 0 5 0',
				width: '100%',
				fieldLabel: 'Role <span class="field-required" />',
				editable: false,
				typeAhead: false,
				storeConfig: {
					url: 'api/v1/resource/lookuptypes/UserTypeCode'
				}
			}),
			{
				xtype: 'checkboxfield',
				boxLabel: 'Receive periodic email about recent changes',
				name: 'notifyOfNew'
			},
			{
				xtype: 'displayfield',
				fieldLabel: 'Member Since',
				name: 'createDts',
				renderer: function (value, field) {
					return '<b>' + Ext.util.Format.date(value, 'F d, Y') + '</b>';
				}
			}
		];

		var toolbarItems = [
			{
				text: 'Save',
				formBind: true,
				iconCls: 'fa fa-save',
				handler: function () {
					var data = profileForm.getValues();
					data.externalGuid = data.guid;

					//update user profile  
					profileForm.setLoading("Saving...");
					Ext.Ajax.request({
						url: 'api/v1/resource/userprofiles/' + data.username,
						method: 'PUT',
						jsonData: data,
						callback: function() {
							profileForm.setLoading(false);
						},
						success: function (response, opts) {
							Ext.toast('Updated User Profile', '', 'tr');
							if (profileForm.saveCallback) {
								profileForm.saveCallback(response, opts);
							}
							if (profileForm.profileWindow) {
								profileForm.profileWindow.close();
							}
						}
					});

				}
			}
		];
		toolbarItems = toolbarItems.concat(profileForm.extraTools);

		var dockedItems = [
			{
				xtype: 'toolbar',
				dock: 'bottom',
				items: toolbarItems

			}
		];
		profileForm.add(formItems);
		profileForm.addDocked(dockedItems);

		profileForm.getComponent('userTypeCodeCB').getStore().on('load', function () {

			// Check to see if the user was set on a profileWindow.
			if (profileForm.profileWindow && profileForm.profileWindow.loadUser) {
				profileForm.loadUser = profileForm.profileWindow.loadUser;
			}

			if (profileForm.loadUser){
				Ext.Ajax.request({
					url: 'api/v1/resource/userprofiles/' + profileForm.loadUser,
					success: function(response, opts) {
						var usercontext = Ext.decode(response.responseText);
						profileForm.getForm().setValues(usercontext);						
					}
				});
			} else {
				CoreService.usersevice.getCurrentUser().then(function (response) {
					var usercontext = Ext.decode(response.responseText);
					profileForm.getForm().setValues(usercontext);
				});
			}
		});

	}

});

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

