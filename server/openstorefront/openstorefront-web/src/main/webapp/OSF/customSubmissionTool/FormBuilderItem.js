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
	
	minHeight: 275,
	margin: '10 10 10 0',
	padding: '10 20 10 20',
	target: true,
	width: '100%',
	cls: 'form-builder-item',
	overCls: 'csf-hover',
	layout: {
		type: 'anchor'
	},
	isActive: false,
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
					var formBuilderItem = this.up('panel');														
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
					var formBuilderItem = this.up('panel');														
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
					var formBuilderItem = this.up('panel');														
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
					var formBuilderItem = this.up('panel');														
					formBuilderItem.syncTemplateField(true);						
				}
			}			
		},		
		{
			xtype: 'combo',
			fieldLabel: 'Field Type <i class="fa fa-question-circle" data-qtip="Radio buttons, checkboxes, grids, etc."></i>',
			itemId: 'fieldTypeCombo',
			name: 'fieldType',
			queryMode: 'local',
			displayField: 'description',
			valueField: 'code',
			editable: false,							
			listeners: {
				change: function (combo, newVal, oldValue, opts) {
					var formBuilderItem = this.up('panel');	
					
					formBuilderItem.fieldType = newVal;
					formBuilderItem.resyncfieldOptions();
				}
			},			
			store: {
				autoLoad: true,
				proxy: {
					type: 'ajax',
					url: 'api/v1/resource/lookuptypes/SubmissionFormFieldType'
				}
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
					var formBuilderItem = this.up('panel');	
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
			listeners: {
				change: function (combo, newVal, oldValue, opts) {
					var formBuilderItem = this.up('panel');	
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
			queryMode: 'remote',
			listeners: {
				change: function (combo, newVal, oldValue, opts) {
					var formBuilderItem = this.up('panel');	
					
					var record = combo.getSelection();
					if (record) {
						var codeField = formBuilderItem.queryById('attributeCode');
						var requireValueField = formBuilderItem.queryById('requiredCommentOnValue');
						codeField.clearValue();
						requireValueField.clearValue();					

						codeField.getStore().loadData(record.data.codes);
						requireValueField.getStore().loadData(record.data.codes);

						formBuilderItem.syncTemplateField(false);					
					}
				}
			},			
			store: {
				proxy: {
					type: 'ajax',
					url: 'api/v1/resource/attributes'
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
			listeners: {
				change: function (combo, newVal, oldValue, opts) {
					var formBuilderItem = this.up('panel');	
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
			listeners: {
				change: function (combo, newVal, oldValue, opts) {
					var formBuilderItem = this.up('panel');
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
			queryMode: 'remote',
			listeners: {
				change: function (combo, newVal, oldValue, opts) {
					var formBuilderItem = this.up('panel');							
					formBuilderItem.syncTemplateField(false);					
				}
			},
			store: {
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
					var formBuilderItem = this.up('panel');						
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
					var formBuilderItem = this.up('panel');	
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
					var formBuilderItem = this.up('panel');	
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
					var formBuilderItem = this.up('panel');						
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
					var formBuilderItem = this.up('panel');						
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
					var formBuilderItem = this.up('panel');														
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
					var formBuilderItem = this.up('panel');														
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
					var formBuilderItem = this.up('panel');														
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
					var formBuilderItem = this.up('panel');														
					formBuilderItem.syncTemplateField(true);						
				}
			}			
		},
		{
			xtype: 'checkbox',
			hidden: true,
			optionField: true,
			name: 'allowPrivateResource',
			boxLabel: '<b>Alliow Private Resource</b>',
			margin: '0 0 0 155',
			listeners: {
				change: function (self, newVal) {
					var formBuilderItem = this.up('panel');														
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
					var formBuilderItem = this.up('panel');														
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
					var formBuilderItem = this.up('panel');														
					formBuilderItem.syncTemplateField(true);						
				}
			}			
		}
		
	],
	resyncfieldOptions: function() {
		var formBuilderItem = this;
		
		//hide all option fields
		Ext.Array.each(formBuilderItem.items.items, function(optionField) {
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
			break;
			case 'ATTRIBUTE_MCHECKBOX':
				formBuilderItem.templateField.mappingType = 'COMPLEX';

				formBuilderItem.getForm().findField('attributeType').setHidden(false);					
				formBuilderItem.getForm().findField('commentLabel').setHidden(false);
				formBuilderItem.getForm().findField('requireComment').setHidden(false);
				formBuilderItem.getForm().findField('showComment').setHidden(false);
				formBuilderItem.getForm().findField('allowHTMLInComment').setHidden(false);
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
			break;
			case 'ATTRIBUTE_MULTI':
				formBuilderItem.templateField.mappingType = 'COMPLEX';

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
			break;
			case 'EXT_DEPEND_MULTI':
				formBuilderItem.templateField.mappingType = 'COMPLEX';

			break;
			case 'MEDIA_MULTI':
				formBuilderItem.templateField.mappingType = 'COMPLEX';

			break;
			case 'RESOURCE_MULTI':
				formBuilderItem.templateField.mappingType = 'COMPLEX';

			break;	
			case 'RELATIONSHIPS_MULTI':
				formBuilderItem.templateField.mappingType = 'COMPLEX';

			break;
			case 'TAG_MULTI':
				formBuilderItem.templateField.mappingType = 'COMPLEX';
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
				console.error('Field type note supported: ' + formBuilderItem.fieldType);
			break;	
		}										

		formBuilderItem.syncTemplateField(false);		
		
	},
	syncTemplateField: function(updateQuestion) {
		var formBuilderItem = this;
		var formData = formBuilderItem.getValues();
	
		if (formData.contactType === ''){
			delete formData.contactType;
		}	
		
		if (formData.resourceType === ''){
			delete formData.resourceType;
		}		
		
		Ext.apply(formBuilderItem.templateField, formData);
			
		if (updateQuestion) {
			formBuilderItem.updateQuestion();
		}
		
	},
	
	updateQuestion: function () {
		var formBuilderItem = this;
		formBuilderItem.formBuilderPanel.sectionPanel.updateField(formBuilderItem.templateField);
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
		} else {
			previousActiveItem = null;
		}
		newItem.addCls('csf-active');
		formBuilderPanel.activeItem = newItem;

		// update the menu position
		newItem.floatingMenu.updatePosition();
    },

    listeners: {    	
    	click: {
    		element: 'el',
    		fn: function () {

    			var formBuilderItem = Ext.getCmp(this.id);
    			formBuilderItem.setActiveFormItem();
    		}
    	},
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
		fieldContainer.loadRecord(record);
	}
});
