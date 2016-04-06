/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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

/* global Ext, CoreService, CoreUtil */

Ext.define('OSF.component.QuestionWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.QuestionWindow',
	title: 'Ask a Question',
	iconCls: 'fa fa-lg fa-comment',
	modal: true,
	width: '70%',
	height: 350,
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
		
		//Query Branding
		CoreService.brandingservice.getCurrentBanding().then(function(response, opts){
			var branding = Ext.decode(response.responseText);
			if (branding.userInputWarning) {
				questionWindow.getComponent('userInputWarning').update('<h3 class="alert-warning" style="text-align: center;">' + 
				'<i class="fa fa-warning"></i> ' + branding.userInputWarning + 
				'</h3>');
			}
		});		
		
		questionWindow.form = Ext.create('Ext.form.Panel', {	
			bodyStyle: 'padding-left: 10px;padding-right: 10px;',
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
				}
			],
			dockedItems: [
				{
					xtype: 'toolbar',
					dock: 'bottom',
					items: [
						{
							text: 'Post',
							iconCls: 'fa fa-save',
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
										url: '../api/v1/resource/components/' + questionWindow.componentId + '/questions' + urlEnd,
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
							iconCls: 'fa fa-close',
							handler: function(){
								questionWindow.close();
							}							
						}
					]
				}
			]
		});
		
		questionWindow.add(questionWindow.form);
		
		
		//Query User
		CoreService.usersevice.getCurrentUser().then(function(response){
			questionWindow.user = Ext.decode(response.responseText);
						
			//confirm that they have the required info
			questionWindow.on('show', function(){
				if (!questionWindow.user.organization || !questionWindow.user.userTypeCode) {
					var userProfileWin = Ext.create('OSF.component.UserProfileWindow', {
						alwaysOnTop: false,
						saveCallback: function(response, opts){
							CoreService.usersevice.getCurrentUser().then(function (response) {
								questionWindow.user = Ext.decode(response.responseText);
								
							});
						}
					});
					userProfileWin.show();
						
					Ext.defer(function(){	
						Ext.MessageBox.show({
							title:'Update User Profile',
							message: 'Please update you profile and fill in missing information to continue.',
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
	height: 350,
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
				
		//Query Branding
		CoreService.brandingservice.getCurrentBanding().then(function(response, opts){
			var branding = Ext.decode(response.responseText);
			if (branding.userInputWarning) {
				responseWindow.getComponent('userInputWarning').update('<h3 class="alert-warning" style="text-align: center;">' + 
				'<i class="fa fa-warning"></i> ' + branding.userInputWarning + 
				'</h3>');
			}
		});			
		
		responseWindow.form = Ext.create('Ext.form.Panel', {			
			bodyStyle: 'padding-left: 10px;padding-right: 10px;',
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
				}
			],
			dockedItems: [
				{
					xtype: 'toolbar',
					dock: 'bottom',
					items: [
						{
							text: 'Post',
							iconCls: 'fa fa-save',
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
										url: '../api/v1/resource/components/' + responseWindow.componentId + '/questions/' + responseWindow.questionId + '/responses' + urlEnd,
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
							iconCls: 'fa fa-close',
							handler: function(){
								responseWindow.close();
							}							
						}
					]
				}
			]
		});
		
		responseWindow.add(responseWindow.form);
		
		//Query User
		CoreService.usersevice.getCurrentUser().then(function(response){
			responseWindow.user = Ext.decode(response.responseText);
						
			//confirm that they have the required info
			responseWindow.on('show', function(){
				if (!responseWindow.user.organization || !responseWindow.user.userTypeCode) {
					var userProfileWin = Ext.create('OSF.component.UserProfileWindow', {
						alwaysOnTop: false,
						saveCallback: function(response, opts){
							CoreService.usersevice.getCurrentUser().then(function (response) {
								responseWindow.user = Ext.decode(response.responseText);
								
							});
						}
					});
					userProfileWin.show();
						
					Ext.defer(function(){	
						Ext.MessageBox.show({
							title:'Update User Profile',
							message: 'Please update you profile and fill in missing information to continue.',
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
		this.form.reset();
	},
	
	edit: function(reponseRecord) {
		var responseWindow = this;
		responseWindow.refresh();
		
		responseWindow.questionId = reponseRecord.get('questionId');
		responseWindow.responseId = reponseRecord.get('responseId');
		responseWindow.form.loadRecord(reponseRecord);
	}
	
});