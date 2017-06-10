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
			name: 'structureStart',
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
			name: 'preCode',
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
			name: 'postCode',
			flex: 1,
			fieldLabel: 'Post'
		},		
		{
			xtype: 'textarea',
			name: 'structureEnd',
			flex: 1,
			fieldLabel: 'Stucture End',
			value: '});'
		}		
		
	],	
	initComponent: function () {
		this.callParent();		
		var codePanel = this;
				
		
	},	
	loadData: function (branding) {
		
	},
	updateGeneratedCode: function(componentBlocks) {
		var codePanel = this;		
				
		var generatedCode = codePanel.queryById('generatedCode');
				
		var renderedItems = '';
		Ext.Array.each(componentBlocks, function(block){
			renderedItems += block.renderCode() + '\n';
		});
		renderedItems += ';';
		
		generatedCode.setValue(renderedItems);		
		
		var data = codePanel.getValues();
		var fullCode = data.structureStart + 
						data.preCode +  
						data.generatedCode +
						data.postCode +
						data.structureEnd;
		
		
		return fullCode;
	}
	
	
});

