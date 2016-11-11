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

/* global Ext */

Ext.define('OSF.form.EntrySummary', {
	extend: 'Ext.form.Panel',
	alias: 'osf.form.EntrySummary',

	layout: 'fit',
	bodyStyle: 'padding: 20px',
	dockedItems: [
		{
			xtype: 'panel',
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
					name: 'name',
					fieldLabel: 'Name <span class="field-required" />',
					allowBlank: false,
					maxLength: 255														
				},		
				{
					xtype: 'combo',
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
					fieldLabel: 'Release Date',
					name: 'releaseDate'									
				},
				{
					xtype: 'textfield',
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
	
	loadData: function(evalationId, componentId) {
		var entryForm = this;
		
		entryForm.setLoading(true);
		Ext.Ajax.request({
			url: 'api/v1/resource/components/' + componentId,
			callback: function() {
				entryForm.setLoading(false);
			},
			success: function(response, opt) {
				var evaluation = Ext.decode(response.responseText);
				var record = Ext.create('Ext.data.Model',{					
				});
				record.set(evaluation);				
				entryForm.loadRecord(record);
			}
		});		
	}
	
});
