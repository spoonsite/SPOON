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
/* global Ext */

Ext.define('OSF.component.MessageWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.MessageWindow',
	
	title: 'Send Mail',
	iconCls: 'fa fa-lg fa-envelope-o',
	modal: true,	
	layout: 'fit',
	width: '60%',	
	resizable: false,
	initialToUsers: '',
	
	initComponent: function () {
		this.callParent();

		var messageWindow = this;
		
		var messageForm = Ext.create('Ext.form.Panel', {
			bodyStyle: 'padding: 10px;',
			defaults: {
				labelAlign: 'top',
				labelSeparator: ''
			},
			items: [
				{
					xtype: 'panel',
					itemId: 'toPanel',
					layout: 'hbox',
					width: '100%',
					defaults: {
						labelAlign: 'top',
						labelSeparator: ''
					},					
					items: [
						{
							xtype: 'textfield',
							itemId: 'toText',
							fieldLabel: 'To <span class="field-required" /> <span style="color: grey">(semi-colon list of email addresses)</span>',
							width: '90%',
							emptyText: 'email@mail.com; ..',
							allowBlank: false,
							name: 'emailAddresses',
							value: messageWindow.initialToUsers,
							maxLength: 2048
						},
						{
							xtype: 'button',
							text: 'Select Users',
							iconCls: 'fa fa-users',
							width: '10%',
							margin: '25 0  0  0',
							handler: function(){
								var messageFormInternal = this.up('form');								
								var selectUserWindow = Ext.create('Ext.window.Window', {
									title: 'Select Users',
									modal: true,
									width: '30%',
									height: '50%',
									layout: 'fit',
									closeAction: 'destory',
									items: [
										{
											xtype: 'grid',
											itemId: 'userGrid',
											columnLine: true,
											selModel: {
												selType: 'checkboxmodel'
											},
											store: {
												fields: [
													"firstName",
													"lastName",
													"email"
												],
												autoLoad: true,
												proxy: {
													type: 'ajax',
													url: '../api/v1/resource/userprofiles',
													reader: {
														type: 'json',
														rootProperty: 'data',
														totalProperty: 'totalNumber'
													}
												},
												listeners: {
													load: function(store, records, successfu, operation, opts) {
														store.filterBy(function(record){
															return record.get('email');
														});
													}
												}
											},
											columns: [
												{ text: 'User', dataIndex: '', flex: 1,
													renderer: function(value, metaData, record) {
														var display = '';
														if (record.get('email')) {
															if (record.get('firstName')) {
																display += record.get('firstName') + ' ';
															}
															if (record.get('lastName')) {
																display += record.get('lastName');
															}
															if (record.get('email')) {
																display += '<br><span style="color: grey;">' + 
																		record.get('email') +
																		'</span>';
															}
														}
														return display;
													}
												}
											]
										}
									],
									dockedItems: [
										{
											xtype: 'textfield',
											emptyText: 'Search',											
											width: '100%',
											listeners: {
												change: function(field, newValue, oldValue, eOpts){
													field.up('window').getComponent('userGrid').getStore().filter("firstName", newValue);
												}
											}
										},
										{
											xtype: 'toolbar',
											dock: 'bottom',
											items: [
												{
													text: 'Add',
													iconCls: 'fa fa-plus',
													handler: function(){
														var toText = messageFormInternal.getComponent('toPanel').getComponent('toText');
														if (toText.getValue()){
															toText.setValue(toText.getValue() + "; ");
														}
														
														var selected = this.up('window').getComponent('userGrid').getSelectionModel().getSelection();
														var addresses = "";
														
														Ext.Array.each(selected, function(record){
															addresses += record.get('email') + "; ";
														});
														
														toText.setValue(toText.getValue() + addresses);																												
														this.up('window').close();
													}
												},
												{
													xtype: 'tbfill'
												},
												{
													text: 'Cancel',
													iconCls: 'fa fa-close',
													handler: function(){
														this.up('window').close();
													}													
												}
											]
										}
									]
								}).show();
							}
						}
					]
				},
				{
					xtype: 'textfield',
					fieldLabel: 'Subject <span class="field-required" />',
					width: '100%',
					allowBlank: false,
					name: 'subject',
					maxLength: 255
				},
				{
					xtype: 'htmleditor',
					name: 'message',
					allowBlank: false,
					fieldLabel: 'Message <span class="field-required" />',
					width: '100%',
					height: 300,
					fieldBodyCls: 'form-comp-htmleditor-border',
					maxLength: 4000					
				}
				
			],
			dockedItems: [
				{
					xtype: 'toolbar',
					dock: 'bottom',
					items: [
						{
							text: 'Send',
							formBind: true,
							iconCls: 'fa fa-envelope',
							handler: function() {
								var mainForm = this.up('form');
								var data = mainForm.getValues();
								
								if (data.message) {								
									var message ={};
									message.subject = data.subject;
									message.message = data.message;								
									message.userToEmail = [];

									var emails = data.emailAddresses.split(';');
									Ext.Array.each(emails, function(email){
										message.userToEmail.push(email);
									});

									Ext.Ajax.request({
										url: '../api/v1/service/notification/admin-message',
										method: 'POST',
										jsonData: data,
										success: function(response, opts){
											Ext.toast('Sent message successfully<br> Individual email delivery success will depend on the email servers.');
											mainForm.reset();
											messageWindow.close();
										}
									});
							     } else {
									mainForm.getForm().markInvalid({
										message: 'Required'
									});
									Ext.Msg.show({
										title:'Validation',
										message: 'The message body is required.',
										buttons: Ext.Msg.OK,
										icon: Ext.Msg.ERROR,
										fn: function(btn) {
										}
									});									
								}
							}
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Cancel',
							iconCls: 'fa fa-close',
							handler: function() {
								messageForm.reset();
								messageWindow.close();
							}
						}						
					]
				}
			]			
			
		});
		
		messageWindow.add(messageForm);		

	}


});

