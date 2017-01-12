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

/* global Ext, CoreUtil */

Ext.define('OSF.form.EntrySummary', {
	extend: 'Ext.form.Panel',
	alias: 'osf.form.EntrySummary',

	layout: 'fit',
	bodyStyle: 'padding: 20px',
	dockedItems: [
		{
			xtype: 'panel',
			itemId: 'topform',
			dock: 'top',
			layout: 'anchor',
			bodyStyle: 'padding: 10px 20px 0px 20px;',
			defaults: {
				width: '100%',
				labelAlign: 'right'
			},				
			items: [
				{
					xtype: 'textfield',
					itemId: 'name',
					name: 'name',
					fieldLabel: 'Name <span class="field-required" />',
					allowBlank: false,
					maxLength: 255														
				},		
				{
					xtype: 'combo',
					itemId: 'organization',
					name: 'organization',									
					allowBlank: false,															
					fieldLabel: 'Organization <span class="field-required" />',
					forceSelection: false,
					valueField: 'description',
					displayField: 'description',
					editable: true,
					queryMode: 'remote',
					store: {				
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/organizations/lookup'
						},
						sorters: [{
							property: 'description',
							direction: 'ASC'
						}]
					}
				},	
				{
					xtype: 'datefield',
					itemId: 'releaseDate',
					fieldLabel: 'Release Date',
					name: 'releaseDate'									
				},
				{
					xtype: 'textfield',
					itemId: 'version',
					fieldLabel: 'Version',
					name: 'version'																		
				},
				{
					xtype: 'panel',
					html: '<b>Description</b> <span class="field-required" />'
				}				
			]
		}
	],	
	items: [
		{
			xtype: 'tinymce_textarea',
			itemId: 'description',
			fieldStyle: 'font-family: Courier New; font-size: 12px;',
			style: { border: '0' },
			margin: '-20 0 0 0',
			name: 'description',			
			maxLength: 65536,
			tinyMCEConfig: CoreUtil.tinymceSearchEntryConfig("osfmediaretriever")
		}		
	],
	initComponent: function () {		
		this.callParent();
		
		var entryForm = this;
	},
	
	loadData: function(evaluationId, componentId, data, opts) {
		var entryForm = this;
		
		entryForm.setLoading(true);
		Ext.Ajax.request({
			url: 'api/v1/resource/components/' + componentId,
			callback: function() {
				entryForm.setLoading(false);
			},
			success: function(response, opt) {
				var component = Ext.decode(response.responseText);
				var record = Ext.create('Ext.data.Model',{					
				});
				record.set(component);				
				entryForm.loadRecord(record);
				entryForm.componentId = componentId;
				entryForm.componentData = component;
				
				if (opts && opts.mainForm) {
					entryForm.refreshCallback = opts.mainForm.refreshCallback;
				}
				
				//set change event
				entryForm.getComponent('description').on('change', function(){
					entryForm.saveData();
				}, undefined, {
					buffer: 2000
				});
				entryForm.getComponent('topform').getComponent('name').on('change', function(){
					entryForm.saveData();
				}, undefined, {
					buffer: 2000
				});
				entryForm.getComponent('topform').getComponent('organization').on('change', function(){
					entryForm.saveData();
				}, undefined, {
					buffer: 2000
				});				
				entryForm.getComponent('topform').getComponent('releaseDate').on('change', function(){
					entryForm.saveData();
				}, undefined, {
					buffer: 2000
				});	
				entryForm.getComponent('topform').getComponent('version').on('change', function(){
					entryForm.saveData();
				}, undefined, {
					buffer: 2000
				});					
				
			}
		});	
		
		opts.commentPanel.loadComments(evaluationId, "Entry Summmary", componentId);
	},
	saveData: function() {		
		var entryForm = this;
		
		//make sure it's valid		
		var data = entryForm.getValues();
	
		
		if (!entryForm.saving &&
			entryForm.isValid() &&
			data.description &&
			data.description !== '') {
		
			entryForm.saving = true;
			Ext.Ajax.request({
				url: 'api/v1/resource/components/' + entryForm.componentId + '/attributes',
				success: function(response, opts) {
					var attributes = Ext.decode(response.responseText);
					
					data.componentType = entryForm.componentData.componentType;
					data.approvalState = entryForm.componentData.approvalState;
					
					var requiredForComponent = {
						component: data,
						attributes: []						
					};
					Ext.Array.each(attributes, function(attribute){
						requiredForComponent.attributes.push({
							componentAttributePk: {
								attributeType: attribute.componentAttributePk.attributeType,
								attributeCode: attribute.componentAttributePk.attributeCode
							}
						});
					});
					
					CoreUtil.submitForm({
						url: 'api/v1/resource/components/' + 
							entryForm.componentId,
						method: 'PUT',
						data: requiredForComponent,
						form: entryForm,
						callback: function() {
							entryForm.saving = false;
						},
						success: function(action, opts) {							

							Ext.toast('Saved Entry Summary');
							if (entryForm.refreshCallback) {
								entryForm.refreshCallback();
							}
						}	
					});	
				},
				failure: function(response, opt) {
					entryForm.saving = false;
				}
			});
			
		}
		
	}
	
});
