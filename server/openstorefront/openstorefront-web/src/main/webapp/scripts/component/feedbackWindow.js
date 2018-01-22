/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */
/*global Ext, CoreService, CoreUtil*/

Ext.define('OSF.component.FeedbackWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.FeedbackWindow',
	title: 'Contact Us',
	iconCls: 'fa fa-exclamation-triangle',
	scrollable: true,
	width: '30%',
	minWidth: 150,
	height: '70%',
	minHeight: 200,
	fieldType: 'textfield',
	isLoggedIn: false,
	y: 40,
	modal: true,
	maximizable: false,
	layout: 'fit',
	buttonSize: 'small',
	initComponent: function () {
		this.callParent();

		var feedbackWin = this;

		//
		// Selection Combobox feedbackTypes items
		//
		var feedbackTypes = Ext.create('Ext.data.Store', {
			fields: ['name'],
			data: [
				{"name": "Report Issue"},
				{"name": "Help"},
				{"name": "Improvement"},
				{"name": "New Feature"}
			],
		});

		//
		//  Feedback Panel
		//  This is the panel tab for the topic search tool
		//
		var iconSize = 'fa-lg';
		if (feedbackWin.buttonSize === 'medium') {
			iconSize = 'fa-2x';
		} else if (feedbackWin.buttonSize === 'large') {
			iconSize = 'fa-2x';
		}

		var formPanel = Ext.create('Ext.form.Panel', {
			layout: 'vbox',
			scrollable: true,
			bodyStyle: 'padding: 10px;',
			defaults: {
				labelAlign: 'top'
			},
			dockedItems: [
				{
					dock: 'bottom',
					xtype: 'toolbar',
					items: [
						{
							text: 'Submit',
							formBind: true,
							iconCls: 'fa ' + iconSize + ' fa-envelope-o icon-button-color-save',
							scale: feedbackWin.buttonSize,
							minWidth: 110,
							handler: function () {
								var feedbackForm = this.up('form');
								var method = 'POST';
								var url = 'api/v1/resource/feedbacktickets';
								var data = feedbackForm.getValues();
								data.webInformation = {
									location: window.location.href,
									userAgent: navigator.userAgent,
									referrer: document.referrer,
									screenResolution: window.screen.availWidth + 'x' + window.screen.availHeight
								};

								if (feedbackWin.extraDescription) {
									data.description = feedbackWin.extraDescription + '\n\n' + data.description;
								}

								if (feedbackWin.hideType) {
									data.ticketType = feedbackWin.hideType;
								}

								if (feedbackWin.hideSummary) {
									data.summary = feedbackWin.hideSummary;
								}

								//submit ticket
								CoreUtil.submitForm({
									loadingText: 'Submitting...',
									url: url,
									method: method,
									data: data,
									removeBlankDataItems: true,
									form: feedbackForm,
									success: function (response, opts) {
										Ext.toast('Sent Successfully', 'Thanks', 'br');
										feedbackForm.setLoading(false);
										feedbackForm.reset();
										if (feedbackWin.successHandler) {
											feedbackWin.successHandler(feedbackWin);
										} else {
											feedbackWin.close();
										}
									}
								});
							}
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Cancel',
							iconCls: 'fa ' + iconSize + ' fa-close icon-button-color-warning',
							scale: feedbackWin.buttonSize,
							handler: function () {
								if (feedbackWin.closeHandler) {
									feedbackWin.closeHandler(feedbackWin);
								} else {
									feedbackWin.close();
								}
							}
						}
					]
				}
			],
			items: [
				{
					xtype: 'combobox',
					name: 'ticketType',
					fieldLabel: 'Choose Type<span class="field-required" />',
					width: '100%',
					hidden: feedbackWin.hideType ? true : false,
					store: feedbackTypes,
					value: 'Report Issue',
					displayField: 'name',
					valueField: 'name',
					editable: false
				},
				{
					xtype: 'textfield',
					name: 'summary',
					hidden: feedbackWin.hideSummary ? true : false,
					fieldLabel: 'Subject<span class="field-required" />',
					width: '100%',
					maxLength: 200,
					allowBlank: feedbackWin.hideSummary ? true : false
				},
				{
					xtype: 'textarea',
					name: 'description',
					fieldLabel: (feedbackWin.labelForDescription ? feedbackWin.labelForDescription : 'Description') + '<span class="field-required" />',
					width: '100%',
					height: 145,
					maxLength: 4096,
					allowBlank: false
				},
				{
					xtype: 'fieldset',
					title: 'Contact Information',
					collapsible: false,
					margin: '10 0 10 0',
					padding: '10 15 10 15',
					width: '100%',
					items: [
						{
							xtype: this.fieldType,
							name: 'fullname',
							fieldLabel: 'Name<span class="field-required" />',
							width: '100%',
							allowBlank: false
						},
						{
							xtype: this.fieldType,
							name: 'email',
							fieldLabel: 'Email<span class="field-required" />',
							width: '100%',
							allowBlank: false,
							vtype: 'email',
							inputType: 'email'
						},
						{
							xtype: this.fieldType,
							name: 'phone',
							fieldLabel: 'Phone',
							width: '100%',
							inputType: 'phone'
						},
						{
							xtype: this.fieldType,
							name: 'organization',
							fieldLabel: 'Organization',
							width: '100%'
						},
						{
							xtype: 'button',
							itemId: 'updateProfile',
							text: 'Update Profile',
							margin: '10 0 15 0',
							hidden: feedbackWin.isLoggedIn ? false : true,
							handler: function () {
								var userProfileWin = Ext.create('OSF.component.UserProfileWindow', {
									closeMethod: 'destroy',
									width: 650,
									saveCallback: function (response, opts) {
										CoreService.userservice.getCurrentUser(true).then(function (usercontext) {
											usercontext.fullname = usercontext.firstName + " " + usercontext.lastName;
											formPanel.getForm().setValues(usercontext);
										});
									}
								}).show();
							}
						}
					]
				}
			]
		});

		feedbackWin.add(formPanel);


		feedbackWin.resetForm = function (fbWin, opts) {
			formPanel.reset();

			if (fbWin.isLoggedIn) {

				CoreService.userservice.getCurrentUser().then(function (usercontext) {
					usercontext.fullname = usercontext.firstName + " " + usercontext.lastName;
					formPanel.getForm().setValues(usercontext);
				});
			}

			//Correct random scrollbar; bump the layout
			feedbackWin.setWidth(feedbackWin.getWidth() + 1);
		};

		feedbackWin.on('show', feedbackWin.resetForm);

		if (feedbackWin.isLoggedIn) {
			CoreService.systemservice.getSecurityPolicy().then(function (policy) {
				if (policy.disableUserInfoEdit) {
					formPanel.queryById('updateProfile').setHidden(true);
				}
			});
		}
	}

});

