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
/*global Ext, CoreService, CoreUtil*/
Ext.define('OSF.component.FeedbackWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.FeedbackWindow',
	title: 'Feedback / Issues',
	iconCls: 'fa fa-exclamation-triangle',
	width: '50%',
	height: '70%',
	y: 40,
	modal: true,
	maximizable: false,
	layout: 'fit',
	initComponent: function () {
		this.callParent();

		var feedbackWin = this;
				
		
		//
		// Selection Combobox feedbackTypes items
		//
		var feedbackTypes = Ext.create('Ext.data.Store', {
			fields: ['name'],
			data: [
				{"name": "Help"},
				{"name": "Improvement"},
				{"name": "New Feature"},
				{"name": "Report Issue"}
			]
		});

		//
		//  Feedback Panel
		//  This is the panel tab for the topic search tool
		//

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
							text: 'Send',
							formBind: true,
							iconCls: 'fa fa-save',
							handler: function () {
								var feedbackForm = this.up('form');
								var method = 'POST';
								var url = '/openstorefront/api/v1/service/jira/submitticket';
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
										feedbackWin.close();
									}
								});
							}
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Cancel',
							iconCls: 'fa fa-close',
							handler: function () {
								feedbackWin.close();
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
					maxLength: 50,
					hidden: feedbackWin.hideType ? true : false,
					store: feedbackTypes,
					value: 'Help',
					displayField: 'name',
					valueField: 'name',
					editable: false
				},
				{
					xtype: 'textfield',
					name: 'summary',
					hidden: feedbackWin.hideSummary ? true : false,
					fieldLabel: 'Summary<span class="field-required" />',
					width: '100%',
					maxLength: 50,
					allowBlank: feedbackWin.hideSummary ? true : false
				},
				{
					xtype: 'textarea',
					name: 'description',
					fieldLabel: (feedbackWin.labelForDescription ? feedbackWin.labelForDescription : 'Description') + '<span class="field-required" />',
					width: '100%',
					height: 200,
					maxLength: 255,
					allowBlank: false
				},
				{
					xtype: 'fieldset',
					title: 'Contact Information',					
					collapsible: true,
					margin: '20 0 10 0',
					padding: 10,
					width: '100%',
					items: [
						{
							xtype: 'displayfield',
							name: 'firstName',
							fieldLabel: 'First Name',
							width: '100%'
						},
						{
							xtype: 'displayfield',
							name: 'lastName',
							fieldLabel: 'Last Name',
							width: '100%'
						},
						{
							xtype: 'displayfield',
							name: 'email',
							fieldLabel: 'Email',
							width: '100%'
						},
						{
							xtype: 'displayfield',
							name: 'phone',
							fieldLabel: 'Phone',
							width: '100%'
						},
						{
							xtype: 'displayfield',
							name: 'organization',
							fieldLabel: 'Organization',
							width: '100%'
						},
						{
							xtype: 'button',
							text: 'Update Profile',
							handler: function () {
								var userProfileWin = Ext.create('OSF.component.UserProfileWindow', {
									closeMethod: 'destroy',
									saveCallback: function (response, opts) {
										CoreService.usersevice.getCurrentUser().then(function (response) {
											var usercontext = Ext.decode(response.responseText);
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
		
		
		feedbackWin.resetForm = function(fbWin, opts){
			formPanel.reset();
			CoreService.usersevice.getCurrentUser().then(function (response) {
				var usercontext = Ext.decode(response.responseText);
				formPanel.getForm().setValues(usercontext);
			});			
		};
		
		feedbackWin.on('show', feedbackWin.resetForm);
	}	
	
});

