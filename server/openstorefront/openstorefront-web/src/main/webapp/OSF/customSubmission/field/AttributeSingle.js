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
	requires: [
		'OSF.common.AttributeCodeSelect'
	],
	
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
		

		var checkForRequiredComment = function(value) {
			if (panel.fieldTemplate.requiredCommentOnValue &&
					panel.fieldTemplate.requiredCommentOnValue === value) {
				
				panel.queryById('comment').setHidden(false);
				panel.queryById('comment').setAllowBlank(false);
			} else if (panel.fieldTemplate.requiredCommentOnValue) {
				
				panel.queryById('comment').setHidden(true);
				panel.queryById('comment').setAllowBlank(true);				
			}
		};	
				
		var displayItems = [];
		if (panel.fieldTemplate.fieldType === 'ATTRIBUTE_SINGLE') {
			//combo box or nothing if code is set
			Ext.Ajax.request({
				url: 'api/v1/resource/attributes/attributetypes/' + encodeURIComponent(panel.fieldTemplate.attributeType),
				success: function(response, opts) {
					var attributeTypeView = Ext.decode(response.responseText);
					displayItems.push({
						xtype: 'AttributeCodeSelect',
						name: 'attributeCode',
						attributeTypeView: attributeTypeView,
						width: '100%',
						maxWidth: 800,
						listerners: {
							change: function(field, newValue, oldValue) {
								checkForRequiredComment(newValue);
							} 
						}
					});							
				}
			});

		} else if (panel.fieldTemplate.fieldType === 'ATTRIBUTE_RADIO') {

			Ext.Ajax.request({
				url: 'api/v1/resource/attributes/attributetypes/' + encodeURIComponent(panel.fieldTemplate.attributeType) + '/attributecodes',
				success: function(response, opts) {
					var attributeCodes = Ext.decode(response.responseText);

					//display all active
					Ext.Array.each(attributeCodes, function(code){
						displayItems.push({
							xtype: 'radio',
							name: 'attributeCode',
							boxLabel: code.label,
							listerners: {
								change: function(field, newValue, oldValue) {
									checkForRequiredComment(newValue);
								} 
							}							
						});
					});		
				}
			});

		} else if (panel.fieldTemplate.fieldType === 'ATTRIBUTE_MCHECKBOX') {
			//display all active	
			Ext.Ajax.request({
				url: 'api/v1/resource/attributes/attributetypes/' + encodeURIComponent(panel.fieldTemplate.attributeType) + '/attributecodes',
				success: function(response, opts) {					
					var attributeCodes = Ext.decode(response.responseText);

					Ext.Array.each(attributeCodes, function(code){
						displayItems.push({
							xtype: 'checkBox',							
							name: 'attributeCodes',
							boxLabel: code.label,
							listerners: {
								change: function(field, newValue, oldValue) {
									checkForRequiredComment(newValue);
								} 
							}							
						});
					});
				}	
			});
		}				

		//comments
		var commentType = 'textarea';
		if (panel.fieldTemplate.allowHTMLInComment) {
			commentType = 'htmleditor';
		}				
		displayItems.push({
			xtype: commentType,
			itemId: 'comment',
			name: 'comment',
			allowBlank: panel.fieldTemplate.requireComment ? false : true,
			fieldLabel: panel.fieldTemplate.commentLabel,
			hidden: panel.fieldTemplate.requireComment ? false : true	
		});

		panel.add(displayItems);	
		
	}

});