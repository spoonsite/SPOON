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
/* global Ext, CoreUtil, CoreService */

/* Author: cyearsley */

Ext.define('OSF.customSubmission.form.Contacts', {
	extend: 'OSF.customSubmission.SubmissionBaseForm',
	xtype: 'osf-submissionform-contact',
	
	layout: 'hbox',
	bodyStyle: 'padding: 10px',
	
	initComponent: function () {
		this.callParent();
		
		var contactPanel = this;

		var formItems = [
			{
				xtype: 'hidden',
				name: 'componentContactId'
			},
			{
				xtype: 'hidden',
				name: 'contactId'
			}
		];
		
		var disableField = false;		
		
		if (!contactPanel.fieldTemplate.required) {
			disableField = true;
			
			formItems.push({
				xtype: 'checkbox',
				itemId: 'fillInCB',
				name: 'use',
				boxLabel: 'Edit form',
				noDisable: true,
				listeners: {
					change: function (field, newValue, oldValue, opts) {
						
						if (newValue) {
							contactPanel.queryById('existingContactGrid').setDisabled(false);
							
							var actualForm = contactPanel.queryById('actualForm');
							Ext.Array.each(actualForm.items.items, function(formField){
								if (!formField.noDisable) {
									formField.setDisabled(false);
								}
							});
							
						} else {
							contactPanel.queryById('existingContactGrid').setDisabled(true);
							
							var actualForm = contactPanel.queryById('actualForm');
							Ext.Array.each(actualForm.items.items, function(formField){
								if (!formField.noDisable) {
									formField.setDisabled(true);
								}
							});							
						}
					}
				}			
			});
		}
		
		if (!contactPanel.fieldTemplate.contactType) {
			formItems.push({
				xtype: 'StandardComboBox',
				name: 'contactType',
				itemId: 'contactType',
				allowBlank: false,
				margin: '0 0 5 0',
				editable: false,
				typeAhead: false,
				disabled: disableField,
				fieldLabel: 'Contact Type <span class="field-required" />',
				storeConfig: {
					url: 'api/v1/resource/lookuptypes/ContactType'
				}				
			});
		}
		
		formItems.push({
			xtype: 'StandardComboBox',
			name: 'organization',
			allowBlank: false,
			margin: '0 0 5 0',					
			fieldLabel: 'Organization <span class="field-required" />',
			forceSelection: false,
			valueField: 'description',
			disabled: disableField,
			storeConfig: {
				url: 'api/v1/resource/organizations/lookup'
			}
		});
		
		formItems.push({
			xtype: 'textfield',
			fieldLabel: 'First Name <span class="field-required" />',
			maxLength: '80',
			allowBlank: false,
			disabled: disableField,
			name: 'firstName',
			listeners: {
				change: function (firstNameField, newValue, oldValue, opts) {
					var grid = this.up('form').down('grid');
					grid.store.filter('firstName', newValue);
				}
			}			
		});	
		
		formItems.push({
			xtype: 'textfield',
			fieldLabel: 'Last Name <span class="field-required" />',
			maxLength: '80',
			allowBlank: false,
			disabled: disableField,
			name: 'lastName',
			listeners: {
				change: function (lastNameField, newValue, oldValue, opts) {
					var grid = this.up('form').down('grid');
					grid.store.filter('lastName', newValue);
				}
			}
		});	
		
		formItems.push({
			xtype: 'textfield',
			fieldLabel: 'Email <span class="field-required" />',										
			maxLength: '255',
			allowBlank: false,
			disabled: disableField,
			regex: new RegExp("[a-z0-9!#$%&'*+/=?^_`{|}~.-]+@[a-z0-9-]+(\.[a-z0-9-]+)*", "i"),
			regexText: 'Must be a valid email address. Eg. xxx@xxx.xxx',
			name: 'email',
			listeners: {
				change: function (emailField, newValue, oldValue, opts) {
					var grid = this.up('form').down('grid');
					grid.store.filter('email', newValue);
				}
			}			
		});	
		
		formItems.push({
			xtype: 'textfield',
			fieldLabel: 'Phone <span class="field-required" />',
			allowBlank: false,	
			disabled: disableField,
			maxLength: '120',					
			name: 'phone'			
		});
		
		formItems.push({
			xtype: 'SecurityComboBox',
			disabled: disableField
		});
		formItems.push({
			xtype: 'DataSensitivityComboBox',
			disabled: disableField
		});		
					
		contactPanel.add([
			{
				xtype: 'panel',
				itemId: 'actualForm',
				width: '50%',
				margin: '0 10 0 0',			
				layout: 'anchor',
				defaults: {
					width: '100%',
					maxWidth: 800,
					labelAlign: contactPanel.fieldTemplate.labelAlign ? contactPanel.fieldTemplate.labelAlign : 'top',
					labelSeparator: ''		
				},				
				items: formItems
			},
			{
				xtype: 'grid',
				width: '50%',
				title: 'Existing Contacts  <i class="fa fa-question-circle" data-qtip="Selecting a contact from this grid will allow you to add an existing contact to the entry. This grid will also show the contact currently being edited."></i>',
				itemId: 'existingContactGrid',
				disabled: disableField,
				columnLines: true,
				height: '100%',
				border: true,
				frameHeader: true,
				hidden: contactPanel.fieldTemplate.hideExistingContactPicker ? contactPanel.fieldTemplate.hideExistingContactPicker : false,
				store: {
					fields: [
						{
							name: 'updateDts',
							type: 'date',
							dateFormat: 'c'
						}
					],
					autoLoad: true,
					proxy: {
						type: 'ajax',
						url: 'api/v1/resource/contacts/filtered'
					},
					listeners: {
						load: function(store, records, opt) {
							if (contactPanel.contactId) {
								var existingGrid = contactPanel.queryById('existingContactGrid');
								var index = existingGrid.getStore().find('contactId', contactPanel.contactId);
								existingGrid.getView().select(index);
							}
						}
					}
				},
				columns: [
					{ text: 'First Name', dataIndex: 'firstName', flex: 1 },
					{ text: 'Last Name', dataIndex: 'lastName', flex: 1 },
					{ text: 'Email', dataIndex: 'email', flex: 2 }
				],
				selModel: {
					selType: 'checkboxmodel',
					allowDeselect: true,
					toggleOnClick: true,
					mode: 'SINGLE'
				},
				listeners: {
					selectionchange: function (grid, record, index, opts) {
						var form = this.up('form');
						
						var fillInValue;
						if (contactPanel.queryById('fillInCB')) {
							fillInValue = contactPanel.queryById('fillInCB').getValue();
						}
						
						if(this.getSelectionModel().getCount() > 0){
							
							var contactType;
							if (form.getForm().findField('contactType')) {
								contactType = form.getForm().findField('contactType').getValue();
							}
							
							form.reset();
							form.loadRecord(this.getSelection()[0]);
							
							if (contactType) {
								form.getForm().findField('contactType').setValue(contactType);
							}
						}
						else{
							form.reset();
						}
						
						if (fillInValue) {
							form.queryById('fillInCB').setValue(fillInValue);
						}						
						
					}
				}				
			}
		]);
		
		var initialData = null;
		if (contactPanel.section) {
			initialData = contactPanel.section.submissionForm.getFieldData(contactPanel.fieldTemplate.fieldId);
			if (initialData) {
				var data = Ext.decode(initialData);
				var record = Ext.create('Ext.data.Model', {				
				});
				
				if (contactPanel.fieldTemplate.contactType) {
					var specificContact;
					Ext.Array.each(data, function(contact){
						if (contact.contactType === contactPanel.fieldTemplate.contactType) {
							specificContact = contact;
						}
					});
					if (specificContact) {
						record.set(specificContact);
					}
				} else {				
					record.set(data[0]);
				}				
				contactPanel.loadRecord(record);
				
				if (!contactPanel.fieldTemplate.hideExistingContactPicker) {	
					contactPanel.contactId = record.data.contactId;		
				}
			}			
		}	
		
		if (contactPanel.fieldTemplate.popluateContactWithUser && !initialData) {
			CoreService.userservice.getCurrentUser().then(function (usercontext) {
				contactPanel.getForm().setValues(usercontext);
			});
		}
		
		var contactTypeField = contactPanel.queryById('contactType');
		if (contactTypeField) {
			if (contactPanel.fieldTemplate.excludeContactType) {
				var filterTypes = Ext.decode(contactPanel.fieldTemplate.excludeContactType);
				
				contactTypeField.getStore().filterBy(function(record){
					var keep = true;
					Ext.Array.each(filterTypes, function (filterItem) {
						if (record.get('code') === filterItem) {
							keep = false;
						}
					});
					return keep;
				});
				
			}
		}

	},
	
	getSubmissionValue: function() {
		var contactPanel = this;
		
		var createContact = true;
		if (contactPanel.queryById('fillInCB') && !contactPanel.queryById('fillInCB').getValue()) {
			createContact = false;
		}

		if (createContact) {
			var data = contactPanel.getValues();

			if (contactPanel.fieldTemplate.contactType) {
				data.contactType = contactPanel.fieldTemplate.contactType;
			}

			var userSubmissionField = {
				templateFieldId: contactPanel.fieldTemplate.fieldId,
				rawValue: Ext.encode([
					data
				])
			};
			return userSubmissionField;
		} else {
			return null;
		}
	}	
	
});
