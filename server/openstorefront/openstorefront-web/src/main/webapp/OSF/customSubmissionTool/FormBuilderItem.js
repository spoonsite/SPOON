/* 
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
 /* Author: cyearsley */
/* global Ext, CoreUtil, CoreService, MediaUtil */

Ext.define('OSF.customSubmissionTool.FormBuilderItem', {	
	extend: 'Ext.form.Panel',
	alias: 'widget.osf-formbuilderitem',
	
	minHeight: 50,
	margin: '10 10 10 0',
	padding: '10 20 10 20',
	target: true,
	width: '100%',
	cls: 'form-builder-item',
	overCls: 'csf-hover',
	layout: {
		type: 'card'
	},
	isActive: false,

	items: [
		{
			xtype: 'panel',
			itemId: 'collapsedSide',
			tpl: new Ext.XTemplate(
				'<tpl if="questionNumber">',
					'<b> {questionNumber} </b>',
				'</tpl>',
				'<tpl if="label">',
					'<b> {label} </b>',
				'</tpl>',
				'<tpl if="required">',
					'<i style="color:#e81717"><b>*</b></i>',
				'</tpl>',
				'<div>{[this.iconFinder(values.fieldType)]}</div>' ,
				'<div style="border-bottom: 1px solid grey"></div>',
				{
					disableFormats: true,
					iconFinder: function(fieldType){
						if(fieldType === 'TEXT'){
							return '<i style="color:#5179ba;" class="fa fa-minus fa-2x" aria-hidden="true" data-qtip="Short Answer Mapped Text"></i>';
						}
						if(fieldType === 'NUMBER'){
							return '<i style="color:#5179ba;" class="fa fa-minus fa-2x" aria-hidden="true" data-qtip="Short Answer Mapped Number"></i>';
						}
						if(fieldType === 'DATE'){
							return '<i style="color:#5179ba;" class="fa fa-minus fa-2x" aria-hidden="true" data-qtip="Short Answer Mapped Date"></i>';
						}
						if(fieldType === 'TEXTAREA'){
							return '<i style="color:#5179ba;" class="fa fa-align-left fa-2x" aria-hidden="true" data-qtip="Paragraph Mapped Text Area"></i>';
						}
						if(fieldType === 'RICHTEXT'){
							return '<i style="color:#5179ba;" class="fa fa-align-left fa-2x" aria-hidden="true" data-qtip="Paragraph Mapped Rich Text"></i>';
						}
						if(fieldType === 'ATTRIBUTE_RADIO'){
							return '<i style="color:#5179ba;" class="fa fa-dot-circle-o fa-2x" aria-hidden="true" data-qtip="Multiple Choice"></i>';
						}
						if(fieldType === 'ATTRIBUTE_MCHECKBOX'){
							return '<i style="color:#5179ba;" class="fa fa-check-square-o fa-2x" aria-hidden="true" data-qtip="Checkboxes"></i>';
						}
						if(fieldType === 'ATTRIBUTE_SINGLE'){
							return '<i style="color:#5179ba;" class="fa fa-chevron-circle-down fa-2x" aria-hidden="true" data-qtip="Dropdown Attribute Select"></i>';
						}
						if(fieldType === 'ORGANIZATION'){
							return '<i style="color:#5179ba;" class="fa fa-chevron-circle-down fa-2x" aria-hidden="true" data-qtip="Dropdown Entry Organization"></i>';
						}
						if(fieldType === 'ATTRIBUTE_REQUIRED'){
							return '<i style="color:#5179ba;" class="fa fa-table fa-2x" aria-hidden="true" data-qtip="Grid Attributes(Required)"></i>';
						}
						if(fieldType === 'ATTRIBUTE_MULTI'){
							return '<i style="color:#5179ba;" class="fa fa-table fa-2x" aria-hidden="true" data-qtip="Grid Attributes(Optional)"></i>';
						}
						if(fieldType === 'CONTACT_MULTI'){
							return '<i style="color:#5179ba;" class="fa fa-table fa-2x" aria-hidden="true" data-qtip="Grid Contacts"></i>';
						}
						if(fieldType === 'EXT_DEPEND_MULTI'){
							return '<i style="color:#5179ba;" class="fa fa-table fa-2x" aria-hidden="true" data-qtip="Grid External Dependency"></i>';
						}
						if(fieldType === 'MEDIA_MULTI'){
							return '<i style="color:#5179ba;" class="fa fa-table fa-2x" aria-hidden="true" data-qtip="Grid Media"></i>';
						}
						if(fieldType === 'RESOURCE_MULTI'){
							return '<i style="color:#5179ba;" class="fa fa-table fa-2x" aria-hidden="true" data-qtip="Grid Resources"></i>';
						}
						if(fieldType === 'RELATIONSHIPS_MULTI'){
							return '<i style="color:#5179ba;" class="fa fa-table fa-2x" aria-hidden="true" data-qtip="Grid Relationships"></i>';
						}
						if(fieldType === 'TAG_MULTI'){
							return '<i style="color:#5179ba;" class="fa fa-table fa-2x" aria-hidden="true" data-qtip="Grid Tags"></i>';
						}
						if(fieldType === 'SUBMISSIONS'){
							return '<i style="color:#5179ba;" class="fa fa-table fa-2x" aria-hidden="true" data-qtip="Grid Child Submissions"></i>';
						}
						if(fieldType === 'CONTACT'){
							return '<i style="color:#5179ba;" class="fa fa-file-text-o fa-2x" aria-hidden="true" data-qtip="Form Single Contact"></i>';
						}
						if(fieldType === 'RESOURCE_SIMPLE'){
							return '<i style="color:#5179ba;" class="fa fa-file-text-o fa-2x" aria-hidden="true" data-qtip="Form Single Resource"></i>';
						}
						return '<i style="color:#5179ba;" class="fa fa-info-circle fa-2x" aria-hidden="true" data-qtip=""></i>';
					}
				}
			),
			listeners: {
				click: {
					element: 'el',
					fn: function () {
						var formBuilderItem = this.component.up();
						formBuilderItem.setActiveFormItem();
					}
				}
			}
		},
		{
			xtype: 'panel',
			itemId: 'infoSide',
			layout: 'anchor',
			defaults: {
				labelAlign: 'right',
				labelWidth: 150,
				width: '100%'		
			},
			items: [
				{
					xtype: 'textfield',
					itemId: 'questionNumber',
					name: 'questionNumber',
					fieldLabel: 'Question Number',
					maxLength: 80,
					enforceMaxLength: true,					
					listeners: {
						change: function (self, newVal) {
							var formBuilderItem = this.up().up('panel');														
							formBuilderItem.syncTemplateField(true);					
						}
					}
				},
				{
					xtype: 'textarea',
					itemId: 'label',
					name: 'label',
					fieldLabel: 'Label',				
					maxLength: 4096,
					enforceMaxLength: true,					
					listeners: {
						change: function (self, newVal) {
							var formBuilderItem = this.up().up('panel');														
							formBuilderItem.syncTemplateField(true);
						}
					}
				},
				{
					xtype: 'textfield',
					name: 'labelTooltip',
					fieldLabel: 'Label Tooltip',
					itemId: 'labelTooltip',						
					listeners: {
						change: function (self, newVal) {
							var formBuilderItem = this.up().up('panel');														
							formBuilderItem.syncTemplateField(true);						
						}
					}			
				},	
				{
					xtype: 'checkbox',
					name: 'required',
					boxLabel: '<b>Required</b>',
					margin: '0 0 0 155',					
					listeners: {
						change: function (self, newVal) {
							var formBuilderItem = this.up().up('panel');														
							formBuilderItem.syncTemplateField(true);						
						}
					}			
				},		
				{
					xtype: 'combo',
					fieldLabel: '(Read Only) Type <i class="fa fa-question-circle" data-qtip="Radio buttons, checkboxes, grids, etc."></i>',
					itemId: 'fieldTypeCombo',						
					name: 'fieldType',
					readOnly: true,
					queryMode: 'local',
					displayField: 'description',
					valueField: 'code',
					editable: false,											
					listeners: {
						change: function (combo, newVal, oldValue, opts) {
							var formBuilderItem = this.up().up('panel');	
							
							formBuilderItem.fieldType = newVal;
							formBuilderItem.resyncfieldOptions();
						}
					},			
					store: {
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/lookuptypes/SubmissionFormFieldType'
						},
						filters: [
							function(fieldType){
								var subFieldType = fieldType.get('code');
								if(subFieldType === 'ATTRIBUTE'){
									return false;
								}
								if(subFieldType === 'EXT_DEPEND'){
									return false;
								}
								if(subFieldType === 'MEDIA'){
									return false;
								}
								if(subFieldType === 'RELATIONSHIPS'){
									return false;
								}
								if(subFieldType === 'RESOURCE'){
									return false;
								}
								if(subFieldType === 'TAG'){
									return false;
								}
								return true;
							}
						]
					}
				},
				//<-----------Control Fields ------>
				
				{
					xtype: 'combo',
					hidden: true,
					optionField: true,
					fieldLabel: 'Label Align',	
					name: 'labelAlign',
					queryMode: 'local',
					displayField: 'description',
					valueField: 'code',
					editable: false,
					emptyText: 'Default',					
					listeners: {
						change: function (combo, newVal, oldValue, opts) {
							var formBuilderItem = this.up().up('panel');	
							formBuilderItem.syncTemplateField(false);					
						}
					},			
					store: {
						data: [
							{code: 'top', description: 'Top'},
							{code: 'left', description: 'Left'},
							{code: 'right', description: 'Right'},
							{code: null, description: 'Default'}
						]
					}
				},		
				{
					xtype: 'combo',
					hidden: true,
					optionField: true,			
					fieldLabel: 'Map To Field',
					name: 'fieldName',
					displayField: 'field',
					valueField: 'field',
					editable: false,					
					checkRequiredMapping: function(formBuilderItem, combo) {				
						var selectedRecord = combo.getSelection();
						if (!selectedRecord && combo.getValue()) {
							selectedRecord = formBuilderItem.formBuilderPanel.templateProgressPanel.getMappableFieldForName(combo.getValue());
						}

						if (selectedRecord && selectedRecord.get('required')) {
							combo.originalRequiredValue = formBuilderItem.getForm().findField('required').getValue();
							formBuilderItem.getForm().findField('required').setValue(true);
							formBuilderItem.getForm().findField('required').setReadOnly(true);
						} else {
							formBuilderItem.getForm().findField('required').setReadOnly(false);
							formBuilderItem.getForm().findField('required').setValue(combo.originalRequiredValue || false);						
						}									
					},
					listeners: {
						change: function (combo, newVal, oldValue, opts) {
							var formBuilderItem = this.up().up('panel');											
							combo.checkRequiredMapping(formBuilderItem, combo);

							formBuilderItem.syncTemplateField(false);	

							formBuilderItem.formBuilderPanel.templateProgressPanel.updateTemplateProgress();
							formBuilderItem.formBuilderPanel.displayPanel.updateFieldPanels();
						}
					},
					store: {				
					}
				},
				{
					xtype: 'combo',
					hidden: true,
					optionField: true,			
					fieldLabel: 'Attribute Type',
					name: 'attributeType',
					displayField: 'description',
					valueField: 'attributeType',
					editable: false,
					queryMode: 'local',					
					listeners: {
						change: function (combo, newVal, oldValue, opts) {
							var formBuilderItem = this.up().up('panel');	
							
							var record = combo.getSelection();
							if (record && record.data.codes) {
								var codeField = formBuilderItem.queryById('attributeCode');
								var requireValueField = formBuilderItem.queryById('requiredCommentOnValue');
								codeField.suspendEvents(false);
								requireValueField.suspendEvents(false);
																
								codeField.clearValue();
								requireValueField.clearValue();					
		
								codeField.getStore().loadData(Ext.Array.clone(record.data.codes));
								requireValueField.getStore().loadData(Ext.Array.clone(record.data.codes));
								
								
								codeField.setValue(formBuilderItem.templateField.attributeCode);								
								requireValueField.setValue(formBuilderItem.templateField.requiredCommentOnValue);
								
								codeField.resumeEvents(true);
								requireValueField.resumeEvents(true);
										
								formBuilderItem.syncTemplateField(false);					
							}
						}
					},			
					store: {
						autoLoad: false,
						sorters: [
							{
								property: 'description',
								direction: 'ASC',
								transform: function (item) {
									if (item) {
										item = item.toLowerCase();
									}
									return item;
								}
							}
						],
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/attributes'
						},
						listeners:{
							load: function() {
								this.data.items.forEach(item => {
									item.data.codes.push({
										code: null,
										label: 'Select (null)'
									});
								});
							}
						}
					}
				},
				{
					xtype: 'combo',
					hidden: true,
					optionField: true,
					fieldLabel: 'Attribute Code',
					itemId: 'attributeCode',
					name: 'attributeCode',
					displayField: 'label',
					valueField: 'code',
					editable: false,
					queryMode: 'local',
					listeners: {
						change: function (combo, newVal, oldValue, opts) {
							var formBuilderItem = this.up().up('panel');
							formBuilderItem.syncTemplateField(false);
						}
					},
					store: {
					}
				},
				{
					xtype: 'combo',
					hidden: true,
					optionField: true,			
					itemId: 'requiredCommentOnValue',
					fieldLabel: 'Require Comment on Value',
					name: 'requiredCommentOnValue',
					displayField: 'label',
					valueField: 'code',
					editable: false,
					queryMode: 'local',
					listeners: {
						change: function (combo, newVal, oldValue, opts) {
							var formBuilderItem = this.up().up('panel');
							formBuilderItem.syncTemplateField(false);					
						}
					},
					store: {				
					}
				},		
				{
					xtype: 'combo',
					hidden: true,
					optionField: true,			
					fieldLabel: 'Contact Type',
					name: 'contactType',
					displayField: 'description',
					valueField: 'code',
					editable: false,
					queryMode: 'local',					
					listeners: {
						change: function (combo, newVal, oldValue, opts) {
							var formBuilderItem = this.up().up('panel');							
							formBuilderItem.syncTemplateField(false);					
						}
					},
					store: {
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/lookuptypes/ContactType?addSelect=true'
						}				
					}
				},		
				{
					xtype: 'tagfield',
					hidden: true,
					optionField: true,			
					fieldLabel: 'Exclude Contact Types',
					name: 'excludeContactType',
					displayField: 'description',
					valueField: 'code',
					editable: true,
					queryMode: 'local',					
					listeners: {
						change: function (combo, newVal, oldValue, opts) {
							var formBuilderItem = this.up().up('panel');							
							formBuilderItem.syncTemplateField(false);					
						}
					},
					store: {
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/lookuptypes/ContactType'
						}				
					}
				},		
				{
					xtype: 'combo',
					hidden: true,
					optionField: true,			
					fieldLabel: 'Resource Type',
					name: 'resourceType',
					displayField: 'description',
					valueField: 'code',
					editable: false,
					queryMode: 'remote',					
					listeners: {
						change: function (combo, newVal, oldValue, opts) {
							var formBuilderItem = this.up().up('panel');						
							formBuilderItem.syncTemplateField(false);					
						}
					},
					store: {
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/lookuptypes/ResourceType'
						}
					}
				},
				{
					xtype: 'combo',
					hidden: true,
					optionField: true,			
					fieldLabel: 'Submission Template',
					name: 'subSubmissionTemplateId',
					displayField: 'name',
					valueField: 'submissionTemplateId',
					editable: false,
					queryMode: 'remote',					
					listeners: {
						change: function (combo, newVal, oldValue, opts) {
							var formBuilderItem = this.up().up('panel');	
							formBuilderItem.syncTemplateField(false);					
						}
					},
					store: {				
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/submissiontemplates'
						}				
					}
				},		
				{			
					xtype: 'tinymce_textarea',
					hidden: true,
					optionField: true,			
					fieldStyle: 'font-family: Courier New; font-size: 12px;',
					style: {border: '0'},
					fieldLabel: 'Static Content',
					name: 'staticContent',
					height: 200,
					maxLength: 4096,					
					tinyMCEConfig: Ext.apply(CoreUtil.tinymceSearchEntryConfig(), {
						mediaSelectionUrl: MediaUtil.generalMediaUrl,
						mediaUploadHandler: MediaUtil.generalMediaUnloadHandler
					}),
					listeners: {
						change: function (combo, newVal, oldValue, opts) {
							var formBuilderItem = this.up().up('panel');	
							formBuilderItem.syncTemplateField(false);					
						}
					}			
				},
				{
					xtype: 'combo',
					hidden: true,
					optionField: true,			
					fieldLabel: 'Child Entry Type',
					name: 'childEntryType',
					displayField: 'description',
					valueField: 'code',
					editable: false,
					queryMode: 'remote',					
					listeners: {
						change: function (combo, newVal, oldValue, opts) {
							var formBuilderItem = this.up().up('panel');						
							formBuilderItem.syncTemplateField(false);					
						}
					},
					store: {
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/componenttypes/lookup'
						}
					}
				},
				{
					xtype: 'textfield',
					hidden: true,
					optionField: true,
					fieldLabel: 'Comment Label',
					name: 'commentLabel',
					maxLength: 255,					
					listeners: {
						change: function (combo, newVal, oldValue, opts) {
							var formBuilderItem = this.up().up('panel');						
							formBuilderItem.syncTemplateField(false);					
						}
					}			
				},		
				{
					xtype: 'checkbox',
					hidden: true,
					optionField: true,			
					name: 'requireComment',
					boxLabel: '<b>Require Comment</b>',
					margin: '0 0 0 155',					
					listeners: {
						change: function (self, newVal) {
							var formBuilderItem = this.up().up('panel');														
							formBuilderItem.syncTemplateField(true);						
						}
					}			
				},
				{
					xtype: 'checkbox',
					hidden: true,
					optionField: true,			
					name: 'showComment',
					boxLabel: '<b>Show Comment</b>',
					margin: '0 0 0 155',					
					listeners: {
						change: function (self, newVal) {
							var formBuilderItem = this.up().up('panel');														
							formBuilderItem.syncTemplateField(true);						
						}
					}			
				},		
				{
					xtype: 'checkbox',
					hidden: true,
					optionField: true,			
					name: 'hidePrivateAttributeFlag',
					boxLabel: '<b>Hide Private Attribute Flag</b>',
					margin: '0 0 0 155',					
					listeners: {
						change: function (self, newVal) {
							var formBuilderItem = this.up().up('panel');														
							formBuilderItem.syncTemplateField(true);						
						}
					}			
				},
				{
					xtype: 'checkbox',
					hidden: true,
					optionField: true,			
					name: 'hideExistingContactPicker',
					boxLabel: '<b>Hide Existing Contact Picker</b>',
					margin: '0 0 0 155',					
					listeners: {
						change: function (self, newVal) {
							var formBuilderItem = this.up().up('panel');														
							formBuilderItem.syncTemplateField(true);						
						}
					}			
				},
				{
					xtype: 'checkbox',
					hidden: true,
					optionField: true,
					name: 'allowPrivateResource',
					boxLabel: '<b>Allow Private Resource</b>',
					margin: '0 0 0 155',					
					listeners: {
						change: function (self, newVal) {
							var formBuilderItem = this.up().up('panel');														
							formBuilderItem.syncTemplateField(true);						
						}
					}			
				},
				{
					xtype: 'checkbox',
					hidden: true,
					optionField: true,			
					name: 'popluateContactWithUser',
					boxLabel: '<b>Populate Contact With User</b>',
					margin: '0 0 0 155',					
					listeners: {
						change: function (self, newVal) {
							var formBuilderItem = this.up().up('panel');														
							formBuilderItem.syncTemplateField(true);						
						}
					}			
				},
				{
					xtype: 'checkbox',
					hidden: true,
					optionField: true,			
					name: 'allowHTMLInComment',
					boxLabel: '<b>Allow HTML in Comment</b>',
					margin: '0 0 0 155',					
					listeners: {
						change: function (self, newVal) {
							var formBuilderItem = this.up().up('panel');														
							formBuilderItem.syncTemplateField(true);						
						}
					}			
				},
				{
					xtype: 'checkbox',
					hidden: true,
					optionField: true,			
					name: 'alwaysShowDetailGrid',
					boxLabel: '<b>Always Show Details Grid</b> <i class="fa fa-question-circle" data-qtip="Default behavior is to hide if the entry type does not support this information."></i>',
					margin: '0 0 0 155',					
					listeners: {
						change: function (self, newVal) {
							var formBuilderItem = this.up().up('panel');														
							formBuilderItem.syncTemplateField(true);						
						}
					}			
				}		
			]
		}
	],

	resyncfieldOptions: function() {
		var formBuilderItem = this;
		
		//hide all option fields
		var infoSidePanel = formBuilderItem.queryById('infoSide');
		Ext.Array.each(infoSidePanel.items.items, function(optionField) {
			if (optionField.optionField) {
				optionField.setHidden(true);
			}
		});

		//update Mapping Types 
		var availableMappedFields = formBuilderItem.formBuilderPanel.templateProgressPanel.getAvaliableMappableFields();					

		var filterMappedFilelds = function(typeAccepted) {
			var mappableFields = Ext.Array.filter(availableMappedFields, function (fieldRecord) {
				var keep = false;
				Ext.Array.forEach(fieldRecord.get('fieldTypes'), function (fieldType) {
					if (fieldType === typeAccepted) {
						keep = true;
					}
				});
				return keep;
			});	
			return mappableFields;
		};

		var mappableFields;	
		switch (formBuilderItem.fieldType) {
			case 'TEXT':
				formBuilderItem.templateField.mappingType = 'COMPONENT';
				formBuilderItem.getForm().findField('fieldName').setHidden(false);	
				formBuilderItem.getForm().findField('labelAlign').setHidden(false);					

				mappableFields = filterMappedFilelds('textfield');							
				formBuilderItem.getForm().findField('fieldName').getStore().loadData(mappableFields);				
			break;	
			case 'NUMBER':
				formBuilderItem.templateField.mappingType = 'COMPONENT';
				formBuilderItem.getForm().findField('fieldName').setHidden(false);
				formBuilderItem.getForm().findField('labelAlign').setHidden(false);	

				mappableFields = filterMappedFilelds('textfield');							
				formBuilderItem.getForm().findField('fieldName').getStore().loadData(mappableFields);
			break;	
			case 'DATE':
				formBuilderItem.templateField.mappingType = 'COMPONENT';
				formBuilderItem.getForm().findField('fieldName').setHidden(false);
				formBuilderItem.getForm().findField('labelAlign').setHidden(false);	

				mappableFields = filterMappedFilelds('date');			
				formBuilderItem.getForm().findField('fieldName').getStore().loadData(mappableFields);							
			break;	
			case 'TEXTAREA':
				formBuilderItem.templateField.mappingType = 'COMPONENT';
				formBuilderItem.getForm().findField('fieldName').setHidden(false);
				formBuilderItem.getForm().findField('labelAlign').setHidden(false);	

				mappableFields = filterMappedFilelds('textarea');		
				formBuilderItem.getForm().findField('fieldName').getStore().loadData(mappableFields);
			break;	
			case 'RICHTEXT':
				formBuilderItem.templateField.mappingType = 'COMPONENT';
				formBuilderItem.getForm().findField('fieldName').setHidden(false);

				mappableFields = filterMappedFilelds('textarea');	
				formBuilderItem.getForm().findField('fieldName').getStore().loadData(mappableFields);
			break;	
			case 'ATTRIBUTE_RADIO':
				formBuilderItem.templateField.mappingType = 'COMPLEX';
			
				formBuilderItem.getForm().findField('attributeType').setHidden(false);
				formBuilderItem.getForm().findField('requiredCommentOnValue').setHidden(false);				
				formBuilderItem.getForm().findField('commentLabel').setHidden(false);
				formBuilderItem.getForm().findField('requireComment').setHidden(false);
				formBuilderItem.getForm().findField('showComment').setHidden(false);
				formBuilderItem.getForm().findField('allowHTMLInComment').setHidden(false);
				formBuilderItem.getForm().findField('hidePrivateAttributeFlag').setHidden(false);
			break;
			case 'ATTRIBUTE_MCHECKBOX':
				formBuilderItem.templateField.mappingType = 'COMPLEX';

				formBuilderItem.getForm().findField('attributeType').setHidden(false);					
				formBuilderItem.getForm().findField('commentLabel').setHidden(false);
				formBuilderItem.getForm().findField('requireComment').setHidden(false);
				formBuilderItem.getForm().findField('showComment').setHidden(false);
				formBuilderItem.getForm().findField('allowHTMLInComment').setHidden(false);
				formBuilderItem.getForm().findField('hidePrivateAttributeFlag').setHidden(false);
			break;
			case 'ATTRIBUTE_SINGLE':
				formBuilderItem.templateField.mappingType = 'COMPLEX';

				formBuilderItem.getForm().findField('labelAlign').setHidden(false);	
				formBuilderItem.getForm().findField('attributeType').setHidden(false);
				formBuilderItem.getForm().findField('attributeCode').setHidden(false);
				formBuilderItem.getForm().findField('requiredCommentOnValue').setHidden(false);
				formBuilderItem.getForm().findField('commentLabel').setHidden(false);
				formBuilderItem.getForm().findField('requireComment').setHidden(false);
				formBuilderItem.getForm().findField('showComment').setHidden(false);
				formBuilderItem.getForm().findField('allowHTMLInComment').setHidden(false);
				formBuilderItem.getForm().findField('hidePrivateAttributeFlag').setHidden(false);
			break;
			case 'ATTRIBUTE_REQUIRED':
				formBuilderItem.templateField.mappingType = 'COMPLEX';
			break;			
			case 'ATTRIBUTE_MULTI':
				formBuilderItem.templateField.mappingType = 'COMPLEX';

				formBuilderItem.getForm().findField('alwaysShowDetailGrid').setHidden(false);	
			break;						
			case 'ORGANIZATION':
				formBuilderItem.templateField.mappingType = 'COMPONENT';
				formBuilderItem.getForm().findField('fieldName').setHidden(false);
				formBuilderItem.getForm().findField('labelAlign').setHidden(false);	

				mappableFields = Ext.Array.filter(availableMappedFields, function (fieldRecord) {
					return fieldRecord.get('field') === 'organization';
				});							
				formBuilderItem.getForm().findField('fieldName').getStore().loadData(mappableFields);	
			break;	
			case 'CONTACT_MULTI':
				formBuilderItem.templateField.mappingType = 'COMPLEX';
				formBuilderItem.getForm().findField('hideExistingContactPicker').setHidden(false);
				formBuilderItem.getForm().findField('alwaysShowDetailGrid').setHidden(false);	
				formBuilderItem.getForm().findField('excludeContactType').setHidden(false);
			break;
			case 'EXT_DEPEND_MULTI':
				formBuilderItem.templateField.mappingType = 'COMPLEX';
				formBuilderItem.getForm().findField('alwaysShowDetailGrid').setHidden(false);	
			break;
			case 'MEDIA_MULTI':
				formBuilderItem.templateField.mappingType = 'COMPLEX';
				formBuilderItem.getForm().findField('alwaysShowDetailGrid').setHidden(false);	
			break;
			case 'RESOURCE_MULTI':
				formBuilderItem.templateField.mappingType = 'COMPLEX';
				formBuilderItem.getForm().findField('alwaysShowDetailGrid').setHidden(false);	
			break;	
			case 'RELATIONSHIPS_MULTI':
				formBuilderItem.templateField.mappingType = 'COMPLEX';
				formBuilderItem.getForm().findField('alwaysShowDetailGrid').setHidden(false);	
			break;
			case 'TAG_MULTI':
				formBuilderItem.templateField.mappingType = 'COMPLEX';
				formBuilderItem.getForm().findField('alwaysShowDetailGrid').setHidden(false);
			break;					
			case 'SUBMISSIONS':
				formBuilderItem.templateField.mappingType = 'SUBMISSION';

				formBuilderItem.getForm().findField('childEntryType').setHidden(false);
				formBuilderItem.getForm().findField('subSubmissionTemplateId').setHidden(false);
			break;						
			case 'RESOURCE_SIMPLE':
				formBuilderItem.templateField.mappingType = 'COMPLEX';

				formBuilderItem.getForm().findField('resourceType').setHidden(false);							
				formBuilderItem.getForm().findField('allowPrivateResource').setHidden(false);
			break;	
			case 'CONTACT':
				formBuilderItem.templateField.mappingType = 'COMPLEX';

				formBuilderItem.getForm().findField('contactType').setHidden(false);
				formBuilderItem.getForm().findField('popluateContactWithUser').setHidden(false);
				formBuilderItem.getForm().findField('hideExistingContactPicker').setHidden(false);
			break;
			case 'CONTENT':
				formBuilderItem.templateField.mappingType = 'NONE';
				formBuilderItem.getForm().findField('staticContent').setHidden(false);			
			break;					

			default: 
				console.error('Field type not supported: ' + formBuilderItem.fieldType);
			break;	
		}										
		
		if (!formBuilderItem.getForm().findField('fieldName').isHidden()) {
			var fieldNameCb = formBuilderItem.getForm().findField('fieldName');
			fieldNameCb.checkRequiredMapping(formBuilderItem, fieldNameCb);			
		}

		formBuilderItem.syncTemplateField(false);		
		
	},
	syncTemplateField: function(updateQuestion) {
		var formBuilderItem = this;
		var formData = formBuilderItem.getValues();
	
		if (formData.contactType === ''){
			delete formData.contactType;
		}	
		
		if (formData.excludeContactType === ''){
			delete formData.excludeContactType;
		}		
		
		if (formData.resourceType === ''){
			delete formData.resourceType;
		}		
		
		Ext.apply(formBuilderItem.templateField, formData);
		
		//remove fields that don't exist
		Ext.Object.each(formBuilderItem.templateField, function(key, value){
			var existingInForm = false;
			if (key === 'mappingType' ||
				key === 'fieldId' ||
				key === 'sectionId' ||
				key === 'fieldOrder' ||
				key === 'fieldType'
				) {
				existingInForm = true;
			} else {	
				Ext.Object.each(formData, function(formDataKey, formDataValue){
					if (key === formDataKey) {
						existingInForm = true;
					}
				});
			}
			
			if (!existingInForm) {
				delete formBuilderItem.templateField[key];
			}
		});
		
		
		if (formBuilderItem.templateField.excludeContactType) {
			formBuilderItem.templateField.excludeContactType = Ext.encode(formBuilderItem.templateField.excludeContactType);
		}
			
		if (updateQuestion) {
			formBuilderItem.updateQuestion();
		}
		
	},
	
	updateQuestion: function (skipMarkedChange) {
		var formBuilderItem = this;
		formBuilderItem.formBuilderPanel.sectionPanel.updateField(formBuilderItem.templateField, skipMarkedChange);
		formBuilderItem.queryById('collapsedSide').update(formBuilderItem.templateField);
	},

    getFormBuilderPanel: function () {
    	return this.formBuilderPanel || this.up('[itemId=formBuilderPanel]');
    },

    getItemContainer: function () {
    	return this.getFormBuilderPanel().itemContainer;
    },

    setActiveFormItem: function (cmp) {
    	var newItem = cmp || this;
    	var formBuilderPanel = this.getFormBuilderPanel();
		var previousActiveItem = formBuilderPanel.activeItem;


		if (previousActiveItem && !previousActiveItem.isDestroyed) {
			previousActiveItem.removeCls('csf-active');
			previousActiveItem.setActiveItem(previousActiveItem.queryById('collapsedSide'));
			var skipMarkedChange = true;
			previousActiveItem.updateQuestion(skipMarkedChange);
			
		} else {
			previousActiveItem = null;
		}
		newItem.addCls('csf-active');


		formBuilderPanel.activeItem = newItem;
		newItem.setActiveItem(newItem.queryById('infoSide'));

    },

    listeners: {    	

    	afterrender: function () {

			var fieldContainer = this;

			// set this container as a drag source
//			new Ext.drag.Source({
//				// handle: '#' + fieldContainer.el.dom.id,
//				element: fieldContainer.getEl(),
//				draggingCls: 'csf-drag',
//				constrain: {
//					vertical: true
//				},
//				listeners: {
//					dragstart: function (self, info, event, eOpts) {
//						fieldContainer.disable();
//					},
//					dragend: function (self, info, event, eOpts) {
//
//						// if there is a valid target container:
//						//		* swap the y coords of both containers
//						//		* swap the location in the parent panel's items array
//						if (info.target !== null) {
//
//							// identify the active (container being dragged,) and the target container
//							var activeContainer = fieldContainer;
//							var targetContainer = Ext.getCmp(info.target._element.id);
//							var itemContainer = fieldContainer.getItemContainer();
//
//							// get the index of the active and target field items in the item container
//							var activeIndex = itemContainer.items.items.indexOf(activeContainer);
//							var targetIndex = itemContainer.items.items.indexOf(targetContainer);
//
//							// reset the y index of the active container, and then swap the items.
//							activeContainer.setY(fieldContainer.getY() - event.delta.y);
//							itemContainer.insert(targetIndex, activeContainer);
//							itemContainer.insert(activeIndex, targetContainer);
//
//							// update the floating menu
// 							UPDATE POSITION HAS BEEN REMOVED
//							fieldContainer.getFormBuilderPanel().floatingMenu.updatePosition();
//
//						} else {
//
//							fieldContainer.setY(fieldContainer.getY() - event.delta.y);
//						}
//
//						fieldContainer.enable();
//					}
//				}
//			});
//
//			// set this container as an eligible drag source
//			fieldContainer.dragTarget = new Ext.drag.Target({
//				element: fieldContainer.getEl()
//			});

			// set the field type
			var fieldTypeCombo = fieldContainer.queryById('fieldTypeCombo');
			if (fieldContainer.fieldType) {
				fieldTypeCombo.setValue(fieldContainer.fieldType);
			}
    	}
    },

	initComponent: function () {
		this.callParent();
		var fieldContainer = this;

		var record = Ext.create('Ext.data.Model', {			
		});
		record.set(fieldContainer.templateField);

		if (fieldContainer.templateField.excludeContactType) {		
			try {
				record.set('excludeContactType', Ext.decode(fieldContainer.templateField.excludeContactType));
			}catch(e){				
			}
		}
		
		fieldContainer.loadRecord(record);	
	
		var componentTypeUrl = '';
		if (fieldContainer.formBuilderPanel.templateRecord.entryType) {
			componentTypeUrl = '?componentType=' + fieldContainer.formBuilderPanel.templateRecord.entryType;
		}
	
		fieldContainer.getForm().findField('attributeType').getStore().load({
			url: 'api/v1/resource/attributes' + componentTypeUrl,
			success: function() {
				fieldContainer.getForm().findField('attributeType').setValue(record.get('attributeType'));
			}
		});			
			

		fieldContainer.queryById('collapsedSide').update(record.data);
	}
});
