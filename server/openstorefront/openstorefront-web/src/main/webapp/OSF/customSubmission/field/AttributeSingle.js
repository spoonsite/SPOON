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
	extend: 'Ext.form.Panel',	
	xtype: 'osf-submissionform-attributesingle',
	requires: [
		'OSF.common.AttributeCodeSelect'
	],
	
	width: '100%',
	layout: 'anchor',
	
	initComponent: function () {
		var panel = this;
		panel.callParent();
			
		panel.label = Ext.create('Ext.panel.Panel', {
			layout: 'hbox',
			items: [
				{
					xtype: 'panel',
					flex: 1,
					maxWidth: 800,
					html: panel.createQuestionLabel()
				},
				{
					xtype: 'checkbox',
					itemId: 'privateField',
					name: 'privateFlag',
					hidden: panel.fieldTemplate.hidePrivateAttributeFlag ? true : false,
					boxLabel: 'Private'
				}
			]
		});	
		
		panel.add([
			panel.label
		]);			
		
		var addCommentField = function(displayItems) {
			//comments
			var commentType = 'textarea';
			if (panel.fieldTemplate.allowHTMLInComment) {
				commentType = 'htmleditor';
			}				
			displayItems.push({
				xtype: commentType,
				itemId: 'comment',
				name: 'comment',
				width: '100%',
				maxWidth: 800,
				height: panel.fieldTemplate.allowHTMLInComment ? 200 : 150,
				allowBlank: panel.fieldTemplate.requireComment ? false : true,
				fieldLabel: panel.fieldTemplate.commentLabel,
				labelAlign: 'top',
				hidden: panel.fieldTemplate.showComment ? false : true	
			});
		};		

		var checkForRequiredComment = function(value) {
			if (panel.fieldTemplate.requiredCommentOnValue &&
					panel.fieldTemplate.requiredCommentOnValue === value) {
				
				panel.queryById('comment').setHidden(false);
				panel.queryById('comment').setConfig({
					allowBlank: false					
				});
			} else if (panel.fieldTemplate.requiredCommentOnValue) {
				
				panel.queryById('comment').setHidden(true);
				panel.queryById('comment').setConfig({
					allowBlank: true
				});				
			}
		};	
		
		var initialData = panel.section.submissionForm.getFieldData(panel.fieldTemplate.fieldId);
		var decodedData = null;
		if (initialData) {
			decodedData = Ext.decode(initialData);
		}			
		
				
		var displayItems = [];
		panel.selectedValue = {};
		if (panel.fieldTemplate.fieldType === 'ATTRIBUTE_SINGLE') {
			//combo box or nothing if code is set
			if (panel.fieldTemplate.attributeCode) {
				addCommentField(displayItems);
				panel.add(displayItems);
				
				//init
				if (decodedData) {
					var record = Ext.create('Ext.data.Model', {						
					});				
					record.set(decodedData[0]);	
					panel.loadRecord(record);
				}
				
			} else {
				Ext.Ajax.request({
					url: 'api/v1/resource/attributes/attributetypes/' + encodeURIComponent(panel.fieldTemplate.attributeType) + '?view=true',
					success: function(response, opts) {
						var attributeTypeView = Ext.decode(response.responseText);
						displayItems.push({
							xtype: 'AttributeCodeSelect',
							name: 'attributeCode',
							itemId: 'attributeCodeSingle',
							showLabel: false,
							attributeTypeView: attributeTypeView,
							required: panel.fieldTemplate.required,
							width: '100%',
							maxWidth: 800,
							fieldListeners: {
								change: function(field, newValue, oldValue) {
									
									var processValue = '';
									var record = field.getSelection();
									if (record) {
										processValue = record.get('label');
									} else {
										processValue = newValue;
									}
																	
									panel.selectedValue = {
										label: processValue,
										value: field.getValue()
									};									
									checkForRequiredComment(newValue);
								} 
							}
						});	
						addCommentField(displayItems);
						panel.add(displayItems);
						
						//init
						if (decodedData) {
							var record = Ext.create('Ext.data.Model', {						
							});			
							//set comment and private
							record.set(decodedData[0]);
							panel.loadRecord(record);
							
							
							//group values by type
							var typeGroup = {};
							Ext.Array.each(decodedData, function(item) {
								if (typeGroup[item.componentAttributePk.attributeType]) {
									typeGroup[item.componentAttributePk.attributeType].push(item.componentAttributePk.attributeCode);
								} else {
									typeGroup[item.componentAttributePk.attributeType] = [];
									typeGroup[item.componentAttributePk.attributeType].push(item.componentAttributePk.attributeCode);
								}
							});

							panel.queryById('attributeCodeSingle').setValue(typeGroup[attributeTypeView.attributeType]);							
						}
						
					}
				});
			}
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
							attributeCode: code.attributeCodePk.attributeCode,
							boxLabel: code.label,							
							submitValue: code.attributeCodePk.attributeCode,
							listeners: {
								change: function(field, newValue, oldValue, optd) {									
									if (newValue) {
										
										panel.selectedValue = {
											label: field.boxLabel,
											value: field.submitValue
										};
										
										checkForRequiredComment(field.submitValue);
									}
								} 
							}							
						});
					});	
					addCommentField(displayItems);
					panel.add(displayItems);	
					
					//init
					if (decodedData) {
						var record = Ext.create('Ext.data.Model', {						
						});				
						//set comment and private
						record.set(decodedData[0]);
						record.set('attributeCode',decodedData[0].componentAttributePk.attributeCode);						
						panel.loadRecord(record);
						
						Ext.Array.each(panel.items.items, function(formItem){
							if (formItem.attributeCode && formItem.attributeCode === decodedData[0].componentAttributePk.attributeCode) {
								formItem.setValue(true);
							}
						});
						
					}
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
							xtype: 'checkbox',							
							name: code.attributeCodePk.attributeCode,
							submitValue: code.attributeCodePk.attributeCode,
							boxLabel: code.label,
							listeners: {
								change: function(field, newValue, oldValue) {
									if (newValue) {
										panel.selectedValue[field.submitValue] = field.submitValue;
										checkForRequiredComment(field.submitValue);
									} else {
										delete panel.selectedValue[field.submitValue];
									}
								} 
							}							
						});
					});
					addCommentField(displayItems);
					panel.add(displayItems);	
					
					//init
					if (decodedData) {
						var record = Ext.create('Ext.data.Model', {						
						});		
						//set comment and private
						record.set(decodedData[0]);
						panel.loadRecord(record);
						
						Ext.Array.each(decodedData, function(loadAttribute) {
							var attributeCode = loadAttribute.componentAttributePk.attributeCode;
							
							Ext.Array.each(panel.items.items, function(formItem){
								if (formItem.name && formItem.name === attributeCode) {
									formItem.setValue(true);
								}
							});
							
						});
						
					}
				}	
			});
		}

	},
	
	isValid : function() {
		var panel = this;
		var valid = panel.callParent();
		if (valid && panel.fieldTemplate.required) {
			if (panel.fieldTemplate.fieldType === 'ATTRIBUTE_RADIO') {
				valid = panel.selectedValue && panel.selectedValue.value;
			} else if (panel.fieldTemplate.fieldType === 'ATTRIBUTE_MCHECKBOX') {
				valid = panel.selectedValue && Ext.Object.getSize(panel.selectedValue) > 0;
			}
		}	
		return valid;	
	},
	
	reviewDisplayValue: function() {
		var panel = this;
		
		var values = panel.getValues();
		
		
		var template = new Ext.XTemplate(
			'<table class="submission-review-table">' + 
			'<tbody>' + 
			'	<tpl for=".">'+
			'		<tr class="submission-review-row">' +
			'			<td class="submission-review-label">'+
			'				{label}' +
			'			</td>' +
			'			<td class="submission-review-data" style="min-width: 150px">' +
			'				{value}' +
			'			</td>' +			
			'		</tr>' +
			'	</tpl>'+
			'</tbody>' +
			'</table>'
		);

		var data = [];
		
		//values 
		var responseValue = 'No Data Entered';
		
		if (panel.fieldTemplate.fieldType === 'ATTRIBUTE_SINGLE' || panel.fieldTemplate.fieldType === 'ATTRIBUTE_RADIO') {
			if (panel.selectedValue) {
				responseValue = panel.selectedValue.label;
			}
		} else if (panel.fieldTemplate.fieldType === 'ATTRIBUTE_MCHECKBOX') {			
			var allValues = [];
			Ext.Object.each(panel.selectedValue, function(key, value, myself) {
				allValues.push(value);
			});
			responseValue = allValues.join(',<br>');
		}
		
		if (responseValue) {
			data.push({
				label: 'Response',
				value: responseValue
			});		
		}
		
		if (values.comment && values.comment  !== '') {
			data.push({
				label: 'Comment',
				value: values.comment
			});		
		}

		if (values.privateFlag) {
			data.push({
				label: 'Private',
				value: 'True'
			});		
		}
		
		if (data.length === 0) {
			return '(No Data Entered)';
		}
		
		return template.apply(data);
	},
	getUserData: function() {
		var panel = this;
		
		var values = panel.getValues();
		
		var data = [];
		
		if (panel.fieldTemplate.fieldType === 'ATTRIBUTE_SINGLE') {
			if (panel.selectedValue.value) {
				
				//place comment on all attrbitues?
				Ext.Array.each(panel.selectedValue.value, function(value) {
					var componentAttribute = {
						componentAttributePk: {
							attributeType: panel.fieldTemplate.attributeType,
							attributeCode: value
						},
						comment: values.comment,
						privateFlag: values.privateFlag					
					};
					if (componentAttribute.comment === '') {
						delete componentAttribute.comment;
					}

					data.push(componentAttribute);
				});
							
			} else {
				//comment only
				var componentAttribute = {
					componentAttributePk: {
						attributeType: panel.fieldTemplate.attributeType,
						attributeCode: panel.fieldTemplate.attributeCode
					},
					comment: values.comment,
					privateFlag: values.privateFlag					
				};
				if (componentAttribute.comment === '') {
					delete componentAttribute.comment;
				}

				data.push(componentAttribute);												
			}
			
		} else if (panel.fieldTemplate.fieldType === 'ATTRIBUTE_RADIO') {
			if (panel.selectedValue) {
				
				var componentAttribute = {
					componentAttributePk: {
						attributeType: panel.fieldTemplate.attributeType,
						attributeCode: panel.selectedValue.value
					},
					comment: values.comment,
					privateFlag: values.privateFlag					
				};
				if (componentAttribute.comment === '') {
					delete componentAttribute.comment;
				}
				
				data.push(componentAttribute);			
			}
		} else if (panel.fieldTemplate.fieldType === 'ATTRIBUTE_MCHECKBOX') {			
			
			//place comment on all attrbitues?
			Ext.Object.each(panel.selectedValue, function(key, value, myself) {
				
				var componentAttribute = {
					componentAttributePk: {
						attributeType: panel.fieldTemplate.attributeType,
						attributeCode: value
					},
					comment: values.comment,
					privateFlag: values.privateFlag					
				};
				if (componentAttribute.comment === '') {
					delete componentAttribute.comment;
				}				
				
				data.push(componentAttribute);				
				
			});			
		}	
		
		var userSubmissionField = null;
		var cleanUpData = [];
		Ext.Array.each(data, function(dataItem){
			if (dataItem.componentAttributePk.attributeCode && dataItem.componentAttributePk.attributeCode !== '') {
				cleanUpData.push(dataItem);
			}
		});
		
		if (cleanUpData.length > 0) {
			userSubmissionField = {			
				templateFieldId: panel.fieldTemplate.fieldId,
				rawValue: Ext.encode(cleanUpData)
			};		
		}
		return userSubmissionField;			
	}
	

});