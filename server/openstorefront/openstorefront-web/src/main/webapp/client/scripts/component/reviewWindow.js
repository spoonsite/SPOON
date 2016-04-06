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
/* global CoreService */

Ext.define('OSF.component.ReviewWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.ReviewWindow',
	title: 'Write a Review',
	iconCls: 'fa fa-lg fa-comment',
	modal: true,
	maximizable: true,
	width: '80%',
	height: 725,
	layout: 'fit',	
	dockedItems:[
		{
			xtype: 'panel',
			itemId: 'userInputWarning',
			html: ''
		}
	],	
	initComponent: function () {
		this.callParent();

		var reviewWindow = this;
		
		//Query Branding
		CoreService.brandingservice.getCurrentBanding().then(function(response, opts){
			var branding = Ext.decode(response.responseText);
			if (branding.userInputWarning) {
				reviewWindow.getComponent('userInputWarning').update('<h3 class="alert-warning" style="text-align: center;">' + 
				'<i class="fa fa-warning"></i> ' + branding.userInputWarning + 
				'</h3>');
			}
		});
		
		reviewWindow.formPanel = Ext.create('Ext.form.Panel', {
			bodyStyle: 'padding: 10px;',
			scrollable: true,
			dockedItems:[				
				{
					xtype: 'toolbar',
					dock: 'bottom',
					items: [
						{
							text: 'Post Review',
							formBind: true,
							iconCls: 'fa fa-save',
							handler: function(){
								var form = this.up('form');
								var data = form.getValues();
								
								var value = form.getComponent('rating').getValue();
								if (!value || value === 0) {
									Ext.MessageBox.show({
									   title:'Missing Rating',
										message: 'Please select a Rating.',
										buttons: Ext.Msg.OK,
										icon: Ext.Msg.ERROR										
									});
								} else if (!data.comment || data.comment === '') {
									Ext.MessageBox.show({
									   title:'Missing Comment',
										message: 'Please enter comment.',
										buttons: Ext.Msg.OK,
										icon: Ext.Msg.ERROR										
									});
								} else {
									data.rating = value;
								
									var method = 'POST';
									var urlEnd = 'detail';
									if (data.reviewId && data.reviewId !== '') {
										method = 'PUT';
										urlEnd = data.reviewId + '/detail';
									}
									data.lastUsed += '-01';
									data.lastUsed = Ext.Date.parse(data.lastUsed,'m-Y-d');
									if (data.prosRaw) {
										data.pros = [];
										Ext.Array.each(data.prosRaw, function(pro){
											data.pros.push({
												text: pro
											});
										});
									}

									if (data.consRaw) {
										data.cons = [];
										Ext.Array.each(data.consRaw, function(con){
											data.cons.push({
												text: con
											});
										});
									}

									data.userTypeCode = reviewWindow.user.userTypeCode;
									data.organization = reviewWindow.user.organization;

									CoreUtil.submitForm({
										url: '../api/v1/resource/components/' + reviewWindow.componentId + '/reviews/' + urlEnd,
										method: method,
										data: data,
										form: form,
										success: function(response, opts) {
											Ext.toast('Posted Review');
											if (reviewWindow.postHandler) {
												reviewWindow.postHandler(reviewWindow, response, opts);
											}
											reviewWindow.close();
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
								this.up('window').close();
							}
						}
					]
				}
			],
			layout: 'anchor',
			defaults: {
				labelAlign: 'top',
				width: '100%',
				labelSeparator: ''
			},
			items: [
				{
					xtype: 'hidden',
					name: 'reviewId'
				},
				{
					xtype: 'textfield',
					name: 'title',
					fieldLabel: 'Title <span class="field-required" />',
					allowBlank: false,
					maxLength: 255,
					emptyText: 'Enter Review Title Here'					
				},
				{
					xtype: 'label',
					html: '<b>Rating</b><span class="field-required" />'
				},				
				{
					xtype: 'rating',
					itemId: 'rating',
					name: 'rating',
					scale: '200%',
					tooltip: 'Rate: 1 poor, 3 Average, 5 Excellent',
					overStyle: 'text-shadow: -1px -1px 0 #000, 1px -1px 0 #000,-1px 1px 0 #000, 1px 1px 0 #000;',
					selectedStyle: 'text-shadow: -1px -1px 0 #000, 1px -1px 0 #000,-1px 1px 0 #000, 1px 1px 0 #000;'
				},
				{
					xtype: 'checkbox',
					name: 'recommend',
					boxLabel: 'Recommend'
				},
				{
					xtype: 'datefield',
					itemId: 'lastUsed',
					name: 'lastUsed',
					fieldLabel: 'Last Used <span class="field-required" />',
					width: 200,
					format: 'm-Y'					
				},
				Ext.create('OSF.component.StandardComboBox', {
					name: 'userTimeCode',									
					allowBlank: false,								
					margin: '0 0 5 0',
					editable: false,
					typeAhead: false,
					width: 200,
					fieldLabel: 'How long have you used it <span class="field-required" />',
					storeConfig: {
						url: '../api/v1/resource/lookuptypes/ExperienceTimeType',						
					}
				}),
				{	
					xtype: 'tagfield',
					itemId: 'pros',
					fieldLabel: 'Pros',					
					name: 'prosRaw',	
					displayField: 'description',
					valueField: 'code',
					queryMode: 'local',
					filterPickList: true,
					store: Ext.create('Ext.data.Store', {
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: '../api/v1/resource/lookuptypes/ReviewPro'
						}
					})
				},
				{	
					xtype: 'tagfield',
					itemId: 'cons',
					fieldLabel: 'Cons',
					name: 'consRaw',
					displayField: 'description',
					valueField: 'code',
					queryMode: 'local',
					filterPickList: true,
					store: Ext.create('Ext.data.Store', {
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: '../api/v1/resource/lookuptypes/ReviewCon'
						}
					})
				},
				{
					xtype: 'htmleditor',
					name: 'comment',
					fieldLabel: 'Comment <span class="field-required" />',
					allowBlank: false,
					fieldBodyCls: 'form-comp-htmleditor-border',					
					maxLength: 4096
				}

			]
		});
		
		reviewWindow.add(reviewWindow.formPanel);
		
		//Query User
		CoreService.usersevice.getCurrentUser().then(function(response){
			reviewWindow.user = Ext.decode(response.responseText);
						
			//confirm that they have the required info
			reviewWindow.on('show', function(){
				if (!reviewWindow.user.organization || !reviewWindow.user.userTypeCode) {
					var userProfileWin = Ext.create('OSF.component.UserProfileWindow', {
						alwaysOnTop: false,
						saveCallback: function(response, opts){
							CoreService.usersevice.getCurrentUser().then(function (response) {
								reviewWindow.user = Ext.decode(response.responseText);
								
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
					
					reviewWindow.close();
				}				
			});
			
		});
		
	},
	
	refresh: function() {
		var reviewWindow = this;
		reviewWindow.formPanel.reset();
		
		reviewWindow.formPanel.getComponent('rating').setValue(null);
	},
	
	editReview: function(reviewRecord) {
		var reviewWindow = this;
		
		reviewWindow.refresh();
		reviewWindow.componentId = reviewRecord.get('componentId');		
		reviewWindow.formPanel.loadRecord(reviewRecord);
		
		reviewWindow.formPanel.getComponent('rating').setValue(reviewRecord.get('rating'));
		var allPros = [];
		Ext.Array.each(reviewRecord.get('pros'), function(item){
			allPros.push(item.code);
		});
		reviewWindow.formPanel.getComponent('pros').setValue(allPros);
		
		var allCons = [];
		Ext.Array.each(reviewRecord.get('cons'), function(item){
			allCons.push(item.code);
		});	
		reviewWindow.formPanel.getComponent('cons').setValue(allCons);
		
		reviewWindow.formPanel.getComponent('lastUsed').setValue(Ext.util.Format.date(reviewRecord.get('lastUsed'), 'm-Y'));
		
	}
		
});

