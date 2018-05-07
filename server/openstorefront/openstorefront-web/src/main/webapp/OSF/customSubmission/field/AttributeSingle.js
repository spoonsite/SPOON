/* 
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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


/* global Ext, fieldPanel */

Ext.define('OSF.customSubmission.field.AttributeSingle', {
	extend: 'Ext.panel.Panel',	
	
	width: '100%',
	layout: 'anchor',
	
	initComponent: function () {
		var panel = this;
		panel.callParent();
			
		fieldPanel.label = Ext.create('Ext.panel.Panel', {
			layout: 'hbox',
			items: [
				{
					xtype: 'panel',
					flex: 1,
					maxWidth: 800,
					html: fieldPanel.createQuestionLabel()
				},
				{
					xtype: 'checkbox',
					boxLabel: 'Private'
				}
			]
		});	
		
		panel.add([
			fieldPanel.label
		]);			
		
		Ext.Ajax.request({
			url: 'api/v1/resource/attributes/attributetypes/' + encodeURIComponent(panel.fieldTemplate.attributeType) + '/attributecodes',
			success: function(response, opts) {
				var attributeCodes = Ext.decode(response.responseText);
				
				var displayItems = [];
				if (panel.fieldTemplate.fieldType === 'ATTRIBUTE_SINGLE') {
					//combo box or nothing if code is set
					displayItems.push({
							xtype: 'combobox',
							name: 'attributeCode',
							width: '100%',
							maxWidth: 800,
							editable: false,
							typeAhead: false,
							displayField: 'label',
							valueField: 'code',
							store: {
								fields: [
									{ name: 'code', mapping: function(data){ 
										return data.attributeCodePk.attributeCode;
									}}										
								],
								data: attributeCodes
							}
						});
				} else if (panel.fieldTemplate.fieldType === 'ATTRIBUTE_RADIO') {
					//display all active
					Ext.Array.each(attributeCodes, function(code){
						displayItems.push({
							xtype: 'radio',
							name: 'attributeCode',
							boxLabel: code.label
						});
					});					

				} else if (panel.fieldTemplate.fieldType === 'ATTRIBUTE_MCHECKBOX') {
					//display all active	
					Ext.Array.each(attributeCodes, function(code){
						displayItems.push({
							xtype: 'checkBox',							
							name: 'attributeCodes',
							boxLabel: code.label
						});
					});						
				}				
								
				//comments
				var commentType = 'textarea';
				if (panel.fieldTemplate.allowHTMLInComment) {
					commentType = '';
				}				
				displayItems.push({
					xtype: commentType
				});
				
				panel.add(displayItems);	
				
			}
		});			

						
	}

});