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

/* global Ext, CoreService, CoreUtil */

Ext.define('OSF.component.QuestionWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.QuestionWindow',
	title: 'Ask a Question',
	iconCls: 'fa fa-lg fa-comment',
	modal: true,
	width: '70%',
	height: 440,
	maximizable: true,
	layout: 'fit',
	dockedItems: [
		{
			xtype: 'panel',
			dock: 'top',
			itemId: 'userInputWarning',
			html: ''
		}
	],
	initComponent: function () {
		this.callParent();

		var questionWindow = this;
		
		questionWindow.form = Ext.create('Ext.form.Panel', {	
			bodyStyle: 'padding-left: 10px;padding-right: 10px;',
			scrollable: true,
			items: [
				{
					xtype: 'htmleditor',
					name: 'question',					
					labelAlign: 'top',
					fieldLabel: 'Question <span class="field-required" />',
					labelSeparator: '',
					allowBlank: false,
					fieldBodyCls: 'form-comp-htmleditor-border',					
					maxLength: 1024
				},
				Ext.create('OSF.component.SecurityComboBox', {	
					itemId: 'securityMarkings',
					hidden: true
				}),
				Ext.create('OSF.component.DataSensitivityComboBox', {												
					width: '100%'
				})				
			],
			dockedItems: [
				{
					xtype: 'toolbar',
					dock: 'bottom',
					items: [
						{
							text: 'Post',
							iconCls: 'fa fa-lg fa-save icon-button-color-save',
							handler: function(){
								var form = this.up('form');
								var data = form.getValues();
								
								if (!data.question || data.question === '') {
									Ext.MessageBox.show({
									   title:'Missing Question',
										message: 'Please enter Question.',
										buttons: Ext.Msg.OK,
										icon: Ext.Msg.ERROR										
									});
								} else {								
									data.userTypeCode = questionWindow.user.userTypeCode;
									data.organization = questionWindow.user.organization;

									var method = 'POST';
									var urlEnd = '';
									if (questionWindow.questionId) {
										method = 'PUT',
										urlEnd = '/' + questionWindow.questionId;		
									}

									CoreUtil.submitForm({
										url: 'api/v1/resource/components/' + questionWindow.componentId + '/questions' + urlEnd,
										method: method,
										data: data,
										form: form,
										success: function(response, opts) {
											Ext.toast('Posted Question');
											if (questionWindow.postHandler) {
												questionWindow.postHandler(questionWindow, response, opts);
											}
											questionWindow.close();
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
							iconCls: 'fa fa-lg fa-close icon-button-color-warning',
							handler: function(){
								questionWindow.close();
							}							
						}
					]
				}
			]
		});
		
		questionWindow.add(questionWindow.form);
		
		//query branding
		CoreService.brandingservice.getCurrentBranding().then(function(branding){		
			if (branding.userInputWarning) {
				questionWindow.getComponent('userInputWarning').update('<h3 class="alert-warning" style="text-align: center;">' + 
				'<i class="fa fa-warning"></i> ' + branding.userInputWarning + 
				'</h3>');
			}			
			if (branding.allowSecurityMarkingsFlg) {
				questionWindow.form.getComponent('securityMarkings').setHidden(false);
			}
		});	
				
		//Query User
		CoreService.userservice.getCurrentUser().then(function(user){
			questionWindow.user = user;
						
			//confirm that they have the required info
			questionWindow.on('show', function(){
				if (!questionWindow.user.organization || !questionWindow.user.userTypeCode) {
					var userProfileWin = Ext.create('OSF.component.UserProfileWindow', {
						alwaysOnTop: false,
						saveCallback: function(response, opts){
							CoreService.userservice.getCurrentUser().then(function (user) {
								questionWindow.user = user;
								
							});
						}
					});
					userProfileWin.show();
						
					Ext.defer(function(){	
						Ext.MessageBox.show({
							title:'Update User Profile',
							message: 'Please update your profile and fill in missing information to continue.',
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.ERROR										
						});						
					}, 200);
					
					questionWindow.close();
				}				
			});
			
		});		
	},
	
	refresh: function(){
		this.form.reset();
	},
	
	edit: function(questionRecord) {
		var questionWindow = this;
		questionWindow.refresh();
		
		questionWindow.questionId = questionRecord.get('questionId');
		questionWindow.form.loadRecord(questionRecord);
	}
	
});

Ext.define('OSF.component.ResponseWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.ResponseWindow',
	title: 'Answer',
	iconCls: 'fa fa-lg fa-comments-o',
	modal: true,
	maximizable: true,
	width: '70%',
	height: 440,
	layout: 'fit',
	dockedItems: [
		{
			xtype: 'panel',
			dock: 'top',
			itemId: 'userInputWarning',
			html: ''
		}
	],
	initComponent: function () {
		this.callParent();

		var responseWindow = this;
				
		responseWindow.form = Ext.create('Ext.form.Panel', {			
			bodyStyle: 'padding-left: 10px;padding-right: 10px;',
			scrollable: true,
			items: [
				{
					xtype: 'htmleditor',
					name: 'response',
					labelSeparator: '',
					labelAlign: 'top',
					fieldLabel: 'Response <span class="field-required" />',
					allowBlank: false,
					fieldBodyCls: 'form-comp-htmleditor-border',					
					maxLength: 1024
				},
				Ext.create('OSF.component.SecurityComboBox', {	
					itemId: 'securityMarkings',
					hidden: true
				}),			
				Ext.create('OSF.component.DataSensitivityComboBox', {												
					width: '100%'
				})				
			],
			dockedItems: [
				{
					xtype: 'toolbar',
					dock: 'bottom',
					items: [
						{
							text: 'Post',
							iconCls: 'fa fa-lg fa-save icon-button-color-save',
							handler: function(){
								var form = this.up('form');
								var data = form.getValues();

								if (!data.response || data.response === '') {
									Ext.MessageBox.show({
									   title:'Missing Response',
										message: 'Please enter Response.',
										buttons: Ext.Msg.OK,
										icon: Ext.Msg.ERROR										
									});
								} else {								
								
									data.questionId = responseWindow.questionId;
									data.userTypeCode = responseWindow.user.userTypeCode;
									data.organization = responseWindow.user.organization;

									var method = 'POST';
									var urlEnd = '';
									if (responseWindow.responseId) {
										method = 'PUT',
										urlEnd = '/' + responseWindow.responseId;		
									}

									CoreUtil.submitForm({
										url: 'api/v1/resource/components/' + responseWindow.componentId + '/questions/' + responseWindow.questionId + '/responses' + urlEnd,
										method: method,
										data: data,
										form: form,
										success: function(response, opts) {
											Ext.toast('Posted Answer');
											if (responseWindow.postHandler) {
												responseWindow.postHandler(responseWindow, response, opts);
											}
											responseWindow.close();
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
							iconCls: 'fa fa-lg fa-close icon-button-color-warning',
							handler: function(){
								responseWindow.close();
							}							
						}
					]
				}
			]
		});
		
		responseWindow.add(responseWindow.form);
		
		//query branding
		CoreService.brandingservice.getCurrentBranding().then(function(branding){			
			if (branding.userInputWarning) {
				responseWindow.getComponent('userInputWarning').update('<h3 class="alert-warning" style="text-align: center;">' + 
				'<i class="fa fa-warning"></i> ' + branding.userInputWarning + 
				'</h3>');
			}			
			if (branding.allowSecurityMarkingsFlg) {
				responseWindow.form.getComponent('securityMarkings').setHidden(false);
			}
		});
		
		//Query User
		CoreService.userservice.getCurrentUser().then(function(user){
			responseWindow.user = user;
						
			//confirm that they have the required info
			responseWindow.on('show', function(){
				if (!responseWindow.user.organization || !responseWindow.user.userTypeCode) {
					var userProfileWin = Ext.create('OSF.component.UserProfileWindow', {
						alwaysOnTop: false,
						saveCallback: function(response, opts){
							CoreService.userservice.getCurrentUser().then(function (user) {
								responseWindow.user = user;
								
							});
						}
					});
					userProfileWin.show();
						
					Ext.defer(function(){	
						Ext.MessageBox.show({
							title:'Update User Profile',
							message: 'Please update your profile and fill in missing information to continue.',
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.ERROR										
						});						
					}, 200);
					
					responseWindow.close();
				}				
			});
			
		});			
		
	},
	
	refresh: function(){
		var responseWindow = this;
		responseWindow.questionId = null;
		responseWindow.responseId = null;
		responseWindow.form.reset();
	},
	
	edit: function(reponseRecord) {
		var responseWindow = this;
		responseWindow.refresh();
		
		responseWindow.questionId = reponseRecord.get('questionId');
		responseWindow.responseId = reponseRecord.get('responseId');
		responseWindow.form.loadRecord(reponseRecord);
	}
	
});