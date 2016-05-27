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

Ext.define('OSF.component.VisualSearchPanel', {
	extend: 'Ext.draw.Container',
	alias: 'osf.widget.VisualSearchPanel',
	
	
	//height: 5000,
	//border: true,
	plugins: ['spriteevents'],
	style: 'cursor: pointer',
	bodyStyle: 'background: white;',
		

		
	initComponent: function () {
		this.callParent();

		var visPanel = this;
		
		visPanel.setLoading("Loading initial Relationships...");
		Ext.Ajax.request({
			url: '../api/v1/resource/componentrelationship',
			callback: function(){
				visPanel.setLoading(false);
			},
			sucess: function(response, opts) {
				var data = Ext.decode(response.responseText);
				
				var viewData = [];
				Ext.Array.each(data, function(relationship){
					viewData.push({
						type: 'component',
						key: relationship.ownerComponentId,
						label: relationship.ownerComponentName,
						relationshipLabel: relationship.relationshipTypeDescription,
						targetKey: relationship.targetComponentId,
						targetName: relationship.targetComponentName
					});
				});
				
				visPanel.drawVisual(viewData);
			}
		});
		
	},
	
	drawVisual: function(viewData) {
		var visPanel = this;
		
		//group and sort data
				
		
		
		var nodeGen = 0;
		
		Ext.Array.each(viewData, function(node){
			
			var containerHeight = visPanel.getHeight();
			var containerWidth = visPanel.getWidth();
			
			var componentNode = {
				size: 50,
				fillStyle: 'orange',
				lineWidth: 2,
				strokeStyle: '#000000'
			};
			
			
			
		});
		
	}
		
});

Ext.define('OSF.component.VisualContainerPanel', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.VisualContainerPanel',
	
	scrollable: true,
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'top',
			style: 'background: darkgrey;',
			items: [
				{
					xtype: 'tbfill'					
				},
				{
					text: 'Reset',
					iconCls: 'fa fa-reply',
					handler: function(){
						
					}
				}
			]
		}
	],		
	
	initComponent: function () {
		this.callParent();

		var containerPanel = this;
		
		containerPanel.visualPanel = Ext.create('OSF.component.VisualSearchPanel', {
			
		});
		
		containerPanel.add(containerPanel.visualPanel);
		
		Ext.defer(function(){
			containerPanel.updateLayout(true, true);
		}, 100);
		
	}, 
	afterRender: function() {
		this.callParent();
		
		var containerPanel = this;
		Ext.defer(function(){
			containerPanel.visualPanel.setWidth(containerPanel.getWidth());
			containerPanel.visualPanel.setHeight(containerPanel.getHeight()-40);
		}, 100);
	}
	
});