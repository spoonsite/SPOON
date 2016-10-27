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
/* global Ext, CoreService */

Ext.define('OSF.component.EvaluationPanel', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.EvaluationPanel',
	layout: 'border',
	initComponent: function () {
		this.callParent();
		
		var evalPanel = this;
		
		evalPanel.navigation = Ext.create('Ext.panel.Panel', {
			title: 'Navigation',
			region: 'west',
			collapsible: true,
			animCollapse: false,
			width: 250,
			minWidth: 250,
			split: true,
			scrollable: true,			
			layout: 'anchor',
			bodyStyle: 'background: white;',
			defaults: {
				width: '100%'
			},			
			items: [
				{
					xype: 'panel',
					itemId: 'evalmenu',
					title: 'Evaluation',
					titleCollapse: true,
					collapsible: true,
					margin: '20 0 0 0',
					bodyStyle: 'padding: 10px;',
					defaultType: 'button',
					defaults: {
						width: '100%',
						cls: 'evaluation-nav-button',							
						overCls: 'evaluation-nav-button-over',
						focusCls: 'evaluation-nav-button',
						margin: '5 0 0 0'
					},
					items: [
						{							
							text: 'Info',							
							handler: function(){
								evalPanel.loadContentForm({
									form: 'EvaluationInfo',
									title: 'Evaluation Info'
								});
							}
						},
						{						
							text: 'Review',														
							handler: function(){
								
							}
						}						
					]
				},
				{
					xype: 'panel',
					itemId: 'entrymenu',
					title: 'Entry',	
					titleCollapse: true,
					collapsible: true,
					margin: '0 0 0 0',
					bodyStyle: 'padding: 10px;',
					defaultType: 'button',
					defaults: {
						width: '100%',
						cls: 'evaluation-nav-button',							
						overCls: 'evaluation-nav-button-over',
						focusCls: 'evaluation-nav-button',
						margin: '5 0 0 0'
					},
					items: [
						{							
							text: 'Summary',							
							handler: function(){
								evalPanel.loadContentForm({
									form: 'EntrySummary',
									title: 'Entry Summary'
								});						
							}							
						}
					]
				},
				{
					xype: 'panel',
					itemId: 'checklistmenu',
					title: 'Checklist',
					titleCollapse: true,
					collapsible: true,
					bodyStyle: 'padding: 10px;',
					margin: '0 0 0 0',
					defaultType: 'button',
					defaults: {
						width: '100%',
						cls: 'evaluation-nav-button',							
						overCls: 'evaluation-nav-button-over',
						focusCls: 'evaluation-nav-button',
						margin: '5 0 0 0'
					},					
					items: [
						{							
							text: 'Summary',							
							handler: function(){
								
							}							
						}
					]
				},
				{
					xype: 'panel',
					itemId: 'sectionmenu',
					title: 'Sections',
					collapsible: true,
					bodyStyle: 'padding: 10px;',
					margin: '00 0 0 0',
					defaultType: 'button',
					items: [
						
					]
				}				
				
			]
		});
		
		evalPanel.contentPanel = Ext.create('Ext.panel.Panel', {
			region: 'center',			
			layout: 'fit',
			dockedItems: [
				{
					xtype: 'toolbar',
					dock: 'top',
					itemId: 'tools',
					style: 'background: rgba(0, 0, 0, .5) !important;',
					items: [
						{
							xtype: 'tbfill'
						},
						{
							xtype: 'panel',
							itemId: 'title',							
							tpl: '<h1 style="color: white;">{title}</h1>'					
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Comment',
							iconCls: 'fa fa-2x fa-comment',
							scale: 'medium',
							handler: function(){
								
							}
						}
					]
				}
			]			
		});
		
		evalPanel.add(evalPanel.navigation);
		evalPanel.add(evalPanel.contentPanel);
		evalPanel.loadEval(evalPanel.evalutationId);
		
	},
	loadEval: function(evalutationId){
		var evalPanel = this;
		
		evalPanel.setLoading(true);
		
		//load eval
		//load component	
		var entryType = 'COMP';
		
		Ext.Ajax.request({
			url: 'api/v1/resource/componenttypes/'+ entryType,
			callback: function() {
				evalPanel.setLoading(false);	
			},
			success: function(response, opts) {
				var entryType = Ext.decode(response.responseText);
				var menuItems = [];
				if (entryType.dataEntryAttributes){
					menuItems.push({						
						text: 'Attributes',							
						handler: function(){

						}
					});
				}
				if (entryType.dataEntryRelationships){
					menuItems.push({						
						text: 'Relationships',							
						handler: function(){

						}
					});					
				}
				if (entryType.dataEntryContacts){
					menuItems.push({						
						text: 'Contacts',							
						handler: function(){
							evalPanel.loadContentForm({
								form: 'Contacts',
								title: 'Entry Contacts'
							});
						}
					});					
				}
				if (entryType.dataEntryResources){
					menuItems.push({						
						text: 'Resources',							
						handler: function(){
							evalPanel.loadContentForm({
								form: 'Resources',
								title: 'Entry Resources'
							});	
						}
					});					
				}
				if (entryType.dataEntryMedia){
					menuItems.push({						
						text: 'Media',							
						handler: function(){

						}
					});						
				}
				if (entryType.dataEntryDependencies){
					menuItems.push({						
						text: 'Dependancies',							
						handler: function(){

						}
					});					
				}				
				if (entryType.dataEntryMetadata){
					menuItems.push({						
						text: 'Metadata',							
						handler: function(){

						}
					});						
				}		
				
				evalPanel.navigation.getComponent('entrymenu').add(menuItems);
				
			}
			
		});
		
	},
	loadContentForm: function(page) {
		var evalPanel = this;
		
		evalPanel.contentPanel.removeAll(true);
		evalPanel.contentPanel.getComponent('tools').getComponent('title').update({
			title: page.title
		});
		
		evalPanel.contentPanel.add(Ext.create('OSF.form.' + page.form, Ext.apply({			
		}, page.options)
		));

		//load Form
		
	}
	
	
});

Ext.define('OSF.component.EvaluationFormWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.EvaluationFormWindow',
	title: 'Evaluation Form',
	iconCls: 'fa fa-clipboard',
	width: '75%',
	height: '75%',
	modal: true,
	maximizable: true,
	layout: 'fit',
	initComponent: function () {
		this.callParent();
		
		var evalWin = this;
		
		evalWin.evalPanel = Ext.create('OSF.component.EvaluationPanel', {			
		});
		
		evalWin.add(evalWin.evalPanel);
		
	}	
	
});