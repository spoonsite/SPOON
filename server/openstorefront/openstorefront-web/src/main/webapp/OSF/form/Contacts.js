/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
/* global Ext, CoreUtil */

Ext.define('OSF.form.Contacts', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.form.Contacts',

	requiredPermissions: ['ADMIN-ENTRY-CONTACT-MANAGEMENT'],
	beforePermissionsCheckFailure: function () { return false; },
	beforePermissionsCheckSuccess: function () { return false; },
	preventDefaultAction: true,
	layout: 'fit',
	hideSecurityMarking: false,

	//bodyStyle: 'padding: 20px',
	initComponent: function () {
		this.callParent();

		var contactPanel = this;

		contactPanel.contactGrid = Ext.create('Ext.grid.Panel', {
			columnLines: true,
			viewConfig: {
				enableTextSelection: true
			},
			store: Ext.create('Ext.data.Store', {
				fields: [
					"contactId",
					"positionDescription",
					"contactType",
					"name",
					"firstName",
					"lastName",
					"email",
					"phone",
					"organization",
					{
						name: 'updateDts',
						type: 'date',
						dateFormat: 'c'
					},
					"activeStatus"
				],
				autoLoad: false,
				proxy: {
					type: 'ajax'
				}
			}),
			columns: [
				{ text: 'Contact Type', dataIndex: 'positionDescription', width: 200 },
				{ text: 'First Name', dataIndex: 'firstName', width: 200 },
				{ text: 'Last Name', dataIndex: 'lastName', width: 200 },
				{ text: 'Email', dataIndex: 'email', flex: 1, minWidth: 200 },
				{ text: 'Phone', dataIndex: 'phone', width: 150 },
				{ text: 'Organization', dataIndex: 'organization', width: 200 },
				{ text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s' },
				{ text: 'Entry Contact Id', dataIndex: 'componentContactId', width: 200, hidden: true },
				{ text: 'Contact Id', dataIndex: 'contactId', width: 200, hidden: true },
				{ text: 'Data Sensitivity', dataIndex: 'dataSensitivity', width: 200, hidden: true },
				{ text: 'Security Marking', dataIndex: 'securityMarkingDescription', width: 150, hidden: contactPanel.hideSecurityMarking }
			],
			listeners: {
				itemdblclick: function (grid, record, item, index, e, opts) {
					this.down('form').reset();
					this.down('form').loadRecord(record);
				},
				selectionchange: function (grid, record, index, opts) {
					var fullgrid = contactPanel.contactGrid;
					if (fullgrid.getSelectionModel().getCount() === 1) {
						fullgrid.down('toolbar').getComponent('editBtn').setDisabled(false);
						fullgrid.down('toolbar').getComponent('delete').setDisabled(false);
						fullgrid.down('toolbar').getComponent('toggleStatusBtn').setDisabled(false);
					} else {
						fullgrid.down('toolbar').getComponent('editBtn').setDisabled(true);
						fullgrid.down('toolbar').getComponent('delete').setDisabled(true);
						fullgrid.down('toolbar').getComponent('toggleStatusBtn').setDisabled(true);
					}
				}
			},
			dockedItems: [
				{
					xtype: 'form',
					title: 'Add/Edit Contact <i class="fa fa-question-circle" data-qtip="This form allows you add contacts to the entry or edit the contacts already connected to the entry."></i>',
					collapsible: true,
					titleCollapse: true,
					animCollapse: false,
					border: true,
					layout: 'column',
					bodyStyle: 'padding: 10px;',
					margin: '0 0 5 0',
					buttonAlign: 'center',
					buttons: [
						{
							xtype: 'button',
							text: 'Save',
							formBind: true,
							margin: '0 20 0 0',
							iconCls: 'fa fa-lg fa-save',
							handler: function () {
								var form = this.up('form');
								var data = form.getValues();
								var componentId = contactPanel.contactGrid.componentId;
								var existingStore = this.up('form').down('grid').getStore();
								if (data.componentContactId) {
									//Update an existing contact
									CoreUtil.submitForm({
										url: 'api/v1/resource/components/' + componentId + '/contacts/' + data.componentContactId,
										method: 'PUT',
										data: data,
										form: form,
										success: function () {
											contactPanel.contactGrid.getStore().reload();
											existingStore.reload();
											form.reset();
										}
									});
								}
								else {
									//Create a new contact
									CoreUtil.submitForm({
										url: 'api/v1/resource/components/' + componentId + '/contacts',
										method: 'POST',
										data: data,
										form: form,
										success: function () {
											contactPanel.contactGrid.getStore().reload();
											existingStore.reload();
											form.reset();
										}
									});
								}
							}
						},
						{
							xtype: 'button',
							text: 'Cancel',
							iconCls: 'fa fa-lg fa-close',
							handler: function () {
								this.up('form').reset();
								this.up('form').down('grid').getSelectionModel().deselectAll();
							}
						}
					],
					items: [
						{
							columnWidth: 0.5,
							layout: 'vbox',
							bodyStyle: 'padding: 10px;',
							defaults: {
								labelAlign: 'right',
								width: '100%'
							},
							items: [{
								xtype: 'hidden',
								name: 'componentContactId'
							},
							{
								xtype: 'hidden',
								name: 'contactId'
							},
							Ext.create('OSF.component.StandardComboBox', {
								name: 'contactType',
								itemId: 'contactType',
								margin: '0 0 5 0',
								allowBlank: false,
								editable: false,
								typeAhead: false,
								width: '100%',
								fieldLabel: 'Contact Type:<span class="field-required" />',
								storeConfig: {
									url: 'api/v1/resource/lookuptypes/ContactType'
								}
							}),
							Ext.create('OSF.component.StandardComboBox', {
								name: 'organization',
								allowBlank: false,
								margin: '0 0 5 0',
								width: '100%',
								fieldLabel: 'Organization:<span class="field-required" />',
								forceSelection: false,
								valueField: 'description',
								storeConfig: {
									url: 'api/v1/resource/organizations/lookup',
									sorters: [{
										property: 'description',
										direction: 'ASC'
									}]
								}
							}),
							{
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
							},
							{
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
							},
							{
								xtype: 'textfield',
								fieldLabel: 'Email',
								maxLength: '255',
								regex: new RegExp("[a-z0-9!#$%&'*+/=?^_`{|}~.-]+@[a-z0-9-]+(\.[a-z0-9-]+)*", "i"),
								regexText: 'Must be a valid email address. Eg. xxx@xxx.xxx',
								name: 'email',
								listeners: {
									change: function (emailField, newValue, oldValue, opts) {
										var grid = this.up('form').down('grid');
										grid.store.filter('email', newValue);
									}
								}
							},
							{
								xtype: 'textfield',
								fieldLabel: 'Phone',
								maxLength: '120',
								name: 'phone'
							},
							Ext.create('OSF.component.SecurityComboBox', {
							}),
							Ext.create('OSF.component.DataSensitivityComboBox', {
							})
							]

						},
						{
							title: 'Existing Contacts  <i class="fa fa-question-circle" data-qtip="Selecting a contact from this grid will allow you to add an existing contact to the entry. This grid will also show the contact currently being edited."></i>',
							columnWidth: 0.5,
							items: [
								{
									xtype: 'grid',
									itemId: 'existingContactGrid',
									columnLines: true,
									height: 250,
									border: true,
									frameHeader: true,
									store: Ext.create('Ext.data.Store', {
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
									}),
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
							]
						}]
				},
				{
					xtype: 'toolbar',
					items: [
						{
							xtype: 'combobox',
							fieldLabel: 'Filter Status',
							store: {
								data: [
									{ code: 'A', description: 'Active' },
									{ code: 'I', description: 'Inactive' }
								]
							},
							forceSelection: true,
							queryMode: 'local',
							displayField: 'description',
							valueField: 'code',
							value: 'A',
							listeners: {
								change: function (combo, newValue, oldValue, opts) {
									this.up('grid').getStore().load({
										url: 'api/v1/resource/components/' + contactPanel.contactGrid.componentId + '/contacts/view',
										params: {
											status: newValue
										}
									});
								}
							}
						},
						{
							text: 'Refresh',
							iconCls: 'fa fa-lg fa-refresh icon-button-color-refresh',
							handler: function () {
								this.up('grid').getStore().reload();
							}
						},
						{
							xtype: 'tbseparator'
						},
						{
							text: 'Edit',
							itemId: 'editBtn',
							iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
							handler: function () {
								var record = contactPanel.contactGrid.getSelection()[0];
								var grid = this.up('grid').queryById('existingContactGrid');
								var index = grid.store.find('contactId', record.data.contactId);
								grid.getView().select(index);
								this.up('grid').down('form').reset();
								this.up('grid').down('form').loadRecord(record);	
							}
						},
						{
							xtype: 'tbseparator',
							hidden: contactPanel.hideToggleStatus || false
						},
						{
							text: 'Toggle Status',
							itemId: 'toggleStatusBtn',
							iconCls: 'fa fa-lg fa-power-off icon-button-color-default',
							disabled: true,
							hidden: contactPanel.hideToggleStatus || false,
							handler: function () {
								CoreUtil.actionSubComponentToggleStatus(contactPanel.contactGrid, 'componentContactId', 'contacts');
							}
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Delete',
							itemId: 'delete',
							iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
							disabled: true,
							handler: function () {

								Ext.Msg.show({
									title: 'Delete Contact?',
									message: 'Are you sure you want to delete the contact from this entry?',
									buttons: Ext.Msg.YESNO,
									icon: Ext.Msg.QUESTION,
									fn: function (btn) {
										if (btn === 'yes') {
											CoreUtil.actionSubComponentToggleStatus(contactPanel.contactGrid, 'componentContactId', 'contacts', undefined, undefined, true);
										}
									}
								});
							}
						}
					]
				}
			]
		});

		contactPanel.add(contactPanel.contactGrid);

	},
	loadData: function (evaluationId, componentId, data, opts, callback) {
		var contactPanel = this;

		contactPanel.componentId = componentId;
		contactPanel.contactGrid.componentId = componentId;

		contactPanel.contactGrid.getStore().load({
			url: 'api/v1/resource/components/' + componentId + '/contacts/view'
		});

		if (opts && opts.commentPanel) {
			opts.commentPanel.loadComments(evaluationId, "Contacts", componentId);
		}

		if (callback) {
			callback();
		}
	}

});


