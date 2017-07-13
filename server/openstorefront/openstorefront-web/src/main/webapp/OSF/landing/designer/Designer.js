/* 
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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


/* global Ext, CoreService */

Ext.define('OSF.landing.designer.Designer', {
	extend: 'Ext.tab.Panel',
	alias: 'widget.ofs-landingPageDesigner',
	requires: [
		'OSF.landing.designer.LiveDesigner',
		'OSF.landing.designer.Code',
		'OSF.landing.designer.Preview'
	],

	title: 'Landing Page Design',	
	tabPosition: 'left',
	tabBar: {
		cls: 'landing-designer-tabbar'
	},
	saveHandler: function(landingPageTemplate) {		
	},
	cancelHandler: function(){		
	},
	activeItem: 0,
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'bottom',
			items: [
				{
					text: 'Save',
					iconCls: 'fa fa-lg fa-save icon-button-color-save icon-small-vertical-correction',
					scale: 'medium',				
					handler: function () {
						var designer = this.up('panel');												
						var landingTemplate = designer.getTemplate();
						designer.saveHandler(landingTemplate);
					}
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Close',
					iconCls: 'fa fa-lg fa-close icon-button-color-warning icon-small-vertical-correction',
					scale: 'medium',
					handler: function () {					
						var designer = this.up('panel');
						designer.cancelHandler();
					}
				}
			]
		}
	],
	isDirty: function() {
		var designerPanel = this;
		if (designerPanel.code.getFullTemplate()) {
			return !(designerPanel.loadedTemplate === designerPanel.code.getFullTemplate());
		} else {
			return false;
		}
	},
	getTemplate: function() {
		var designerPanel = this;
		var template = designerPanel.code.getTemplate();
		template.templateBlocks = Ext.encode(designerPanel.liveDesigner.components);
		return template;
	},
	initComponent: function () {
		this.callParent();
		
		var designerPanel = this;
		
		designerPanel.liveDesigner = Ext.create('OSF.landing.designer.LiveDesigner', {
			title: 'Designer',
			iconCls: 'fa fa-rotate-90 fa-file-image-o',
			designerPanel: designerPanel
		});
		
		designerPanel.code = Ext.create('OSF.landing.designer.Code', {
			title: 'Code',
			iconCls: 'fa fa-rotate-90 fa-cogs',
			designerPanel: designerPanel
		});
		
		designerPanel.preview = Ext.create('OSF.landing.designer.Preview', {
			title: 'Preview',
			iconCls: 'fa fa-rotate-90 fa-eye',
			designerPanel: designerPanel
		});
		
		designerPanel.add(designerPanel.liveDesigner);
		designerPanel.add(designerPanel.code);
		designerPanel.add(designerPanel.preview);
		
	},
	afterRender: function() {
		this.callParent();		
		var designerPanel = this;			
				
		//cycle through tab so every thing get rendered
		designerPanel.setActiveItem(designerPanel.code);
		designerPanel.setActiveItem(designerPanel.preview);
		designerPanel.setActiveItem(designerPanel.liveDesigner);
		
		designerPanel.initialize();
		
	},
	initialize: function() {
		var designerPanel = this;		
		designerPanel.liveDesigner.initialize();
		if (designerPanel.initializeCallback) {
			designerPanel.initializeCallback();
		}
	},
	loadData: function (branding) {
		var designerPanel = this;
		
		designerPanel.code.loadData(branding);
		
		if (branding.landingTemplate && branding.landingTemplate.templateBlocks) {
			try{
				var decodedBlocks = Ext.decode(branding.landingTemplate.templateBlocks);
				designerPanel.liveDesigner.restoreBlocks(decodedBlocks);
			} catch (e) {
				Ext.log(e.message);
				Ext.log(e);
			}
		}		
		designerPanel.loadedTemplate = designerPanel.code.getFullTemplate();
		
	},
	updateAll: function(componentBlocks) {
		var designerPanel = this;
		
		if (designerPanel.liveDesigner) {
			designerPanel.liveDesigner.updateDesigner(componentBlocks);
			var fullCode = designerPanel.code.updateGeneratedCode(componentBlocks);
			designerPanel.preview.updatePreview(fullCode);
		}
	}
	
});