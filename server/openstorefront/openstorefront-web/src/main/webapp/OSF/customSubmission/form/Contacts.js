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
		
		if (!contactPanel.fieldTemplate.contactType) {
			formItems.push({
				xtype: 'StandardComboBox',
				name: 'contactType',
				itemId: 'contactType',
				allowBlank: false,
				margin: '0 0 5 0',
				editable: false,
				typeAhead: false,					
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
			storeConfig: {
				url: 'api/v1/resource/organizations/lookup'
			}
		});
		
		formItems.push({
			xtype: 'textfield',
			fieldLabel: 'First Name<span class="field-required" />',
			maxLength: '80',
			allowBlank: false,
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
			fieldLabel: 'Last Name<span class="field-required" />',
			maxLength: '80',
			allowBlank: false,
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
			maxLength: '120',					
			name: 'phone'			
		});
		
		formItems.push({
			xtype: 'SecurityComboBox'	
		});
		formItems.push({
			xtype: 'DataSensitivityComboBox'
		});		
					
		contactPanel.add([
			{
				xtype: 'panel',
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
						if(this.getSelectionModel().getCount() > 0){
							var contactType = form.getForm().findField('contactType').getValue();
							form.reset();
							form.loadRecord(this.getSelection()[0]);
							form.getForm().findField('contactType').setValue(contactType);
						}
						else{
							form.reset();
						}
					}
				}				
			}
		]);
		
		if (contactPanel.fieldTemplate.popluateContactWithUser) {
			CoreService.userservice.getCurrentUser().then(function (usercontext) {
				contactPanel.getForm().setValues(usercontext);
			});
		}
	
	}
	
});
