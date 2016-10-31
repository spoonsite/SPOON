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

Ext.define('OSF.form.ChecklistSummary', {
	extend: 'Ext.form.Panel',
	alias: 'osf.form.ChecklistSummary',

	layout: 'border',

	initComponent: function () {		
		this.callParent();
		
		var summaryForm = this;
				
		var topPanel = 	Ext.create('Ext.panel.Panel', {
			region: 'north',
			items: [
				{
					xtype: 'panel',	
					dock: 'top',
					html: '<b>Summary</b>'
				},
				{
					xtype: 'tinymce_textarea',						
					dock: 'top',
					fieldStyle: 'font-family: Courier New; font-size: 12px;',
					style: { border: '0' },
					height: 300,
					width: '100%',
					name: 'summary',			
					maxLength: 32000,
					tinyMCEConfig: CoreUtil.tinymceConfig("osfmediaretriever")			
				}
			]
		});	
				
		var recommendations = Ext.create('Ext.panel.Panel', {
			title: 'Recommendations',
			region: 'center',
			scrollable: true,
			dockedItems: [	
				{
					xtype: 'toolbar',
					items: [
						{
							text: 'Add',
							iconCls: 'fa fa-2x fa-plus',
							scale: 'medium',
							handler: function() {

							}
						}
					]
				}
			],
			tpl: new Ext.XTemplate(
				'<table>',
				'	<th>Type</th><th>Recommedation</th><th>Reason</th>',					
				'</table>'
			)			
		});
		summaryForm.add(topPanel);
		summaryForm.add(recommendations);
	}
	
});
