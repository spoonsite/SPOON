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

Ext.define('OSF.landing.designer.Code', {
	extend: 'Ext.form.Panel',
	alias: 'widget.ofs-codedesigner',
	
	bodyStyle: 'padding: 20px',
	layout: {
		type: 'vbox'
	},
	defaults: {
		width: '100%',
		labelAlign: 'top'
	},
	items: [
		{
			xtype: 'textarea',
			name: 'preStructureCode',
			flex: 1,
			fieldLabel: 'Stucture Start',
			value: '<script type="text/javascript"> \n' +			
					'Ext.require("OSF.landing.DefaultHeader"); \n' + 
					'Ext.require("OSF.landing.DefaultFooter"); \n' + 
					'Ext.require("OSF.landing.DefaultSearch"); \n' + 
					'Ext.require("OSF.landing.DefaultVersion"); \n' + 
					'Ext.require("OSF.landing.DefaultSearchTools"); \n' + 
					'Ext.require("OSF.landing.DefaultActions"); \n' + 
					'Ext.require("OSF.landing.DefaultCategory"); \n' +
					'Ext.require("OSF.landing.DefaultInfo"); \n\n' + 
					'Ext.onReady(function(){ \n'
		},
		{
			xtype: 'textarea',
			name: 'preTemplateCode',
			flex: 1,
			fieldLabel: 'Pre'
		},
		{
			xtype: 'textarea',
			itemId: 'generatedCode',
			name: 'generatedCode',
			flex: 1,
			fieldLabel: 'Generated',
			readOnly: true,
			fieldCls: 'generated-code'
		},		
		{
			xtype: 'textarea',
			name: 'postTemplateCode',
			flex: 1,
			fieldLabel: 'Post'
		},		
		{
			xtype: 'textarea',
			name: 'postStructureCode',
			flex: 1,
			fieldLabel: 'Stucture End',
			value: '});\n</script>'
		}		
		
	],
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'top',
			items: [
				{
					text: 'Update Preview',
					iconCls: 'fa fa-lg fa-refresh icon-button-color-refresh',
					handler: function() {
						var codePanel = this.up('panel');
						
						var fullCode = codePanel.getFullTemplate();						
						codePanel.designerPanel.preview.updatePreview(fullCode);
					}
				}
			]
		}
	],	
	getFullTemplate: function(){
		var codePanel = this;
		var data = codePanel.getValues();
		var fullCode = data.preStructureCode + 
			data.preTemplateCode +  
			data.generatedCode +
			data.postTemplateCode +
			data.postStructureCode;
		return fullCode;
	},
	initComponent: function () {
		this.callParent();		
		var codePanel = this;
				
		
	},	
	loadData: function (branding) {
		var codePanel = this;
		if (branding && branding.landingTemplate) {
			
			var model = Ext.create('Ext.data.Model', {				
			});
			model.set(branding.landingTemplate);
			codePanel.loadRecord(model);
		}
	},
	getTemplate: function() {
		var codePanel = this;
		return codePanel.getValues();		
	},
	updateGeneratedCode: function(componentBlocks) {
		var codePanel = this;		
				
		var generatedCode = codePanel.queryById('generatedCode');
				
		var renderedItems = '';
		Ext.Array.each(componentBlocks, function(block){
			renderedItems += block.renderCode() + '\n';
		});
		renderedItems = renderedItems.substring(0, renderedItems.length - 1);
		renderedItems += ';';
		
		generatedCode.setValue(renderedItems);		
		
		var fullCode = codePanel.getFullTemplate();	
		
		return fullCode;
	}
	
	
});

