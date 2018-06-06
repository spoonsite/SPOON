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

 /* Author: cyearsley */

/* global Ext, CoreUtil, CoreService */

Ext.define('OSF.customSubmission.form.Attributes', {
	extend: 'OSF.customSubmission.SubmissionBaseForm',
	alias: 'widget.osf-submissionform-attribute',
	
	layout: 'anchor',
	bodyStyle: 'padding: 10px',
	
	defaults: {
		width: '100%',
		maxWidth: 800,
		labelAlign: 'right',
		labelSeparator: ''		
	},	
	
	initComponent: function(){
		this.callParent();
		var attributePanel = this;
		
		
		
		var items = [
			{
				xtype: 'panel',
				layout: 'hbox',
				margin: '0 0 10 0',
				items: [
					{				
						xtype: 'combobox',
						itemId: 'attributeTypeCB',
						fieldLabel: 'Attribute Type <span class="field-required" />',
						name: 'type',
						flex: 1,
						labelWidth: 150,
						labelAlign: 'right',
						labelSeparator: '',
						forceSelection: true,
						queryMode: 'local',
						editable: true,
						typeAhead: true,
						allowBlank: false,
						valueField: 'attributeType',
						displayField: 'description',
						store: Ext.create('Ext.data.Store', {
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
							]
						}),
						listConfig: {
							getInnerTpl: function () {
								return '{description} <tpl if="detailedDescription"><i class="fa fa-question-circle" data-qtip=\'{detailedDescription}\'></i></tpl>';
							}
						},
						listeners: {

							change: function (field, newValue, oldValue, opts) {

								// Clear Previously Selected Code
								var codeField = field.up('form').getComponent('attributeCodeCB');
								codeField.clearValue();

								codeField.setLoading(true);
								Ext.Ajax.request({
									url: 'api/v1/resource/attributes/attributetypes/' + newValue + '/attributecodes',
									callback: function () {
										codeField.setLoading(false);
									},
									success: function (response, opts) {
										var attributeCodes = Ext.decode(response.responseText);

										var lookUpCodes = [];
										Ext.Array.each(attributeCodes, function (attributeCode) {
											lookUpCodes.push({
												code: attributeCode.attributeCodePk.attributeCode,
												label: attributeCode.label,
												description: attributeCode.description
											});
										});
										codeField.getStore().loadData(lookUpCodes);
									}
								});

								var record = field.getSelection();

								// Check For Selected Type
								if (record) {
									// Allow or Disallow Editing Of ComboBox based on if User-Generated Codes Being Enabled
									codeField.setEditable(record.get("allowUserGeneratedCodes"));
									codeField.vtype = (record.data.attributeValueType === 'NUMBER') ? 'AttributeNumber' : undefined;
								} else {

									// Nothing Selcted, Remove All Codes
									codeField.getStore().removeAll();
									codeField.vtype = undefined;
								}
							}
						}						
					},
					{
						xtype: 'button',
						itemId: 'addAttributeType',
						text: 'Add',
						iconCls: 'fa fa-lg fa-plus icon-button-color-save',
						minWidth: 100,
						hidden: true,
						handler: function () {
							var attributeTypeCb = attributePanel.queryById('attributeTypeCB');


							var addTypeWin = Ext.create('Ext.window.Window', {
								title: 'Add Type',
								iconCls: 'fa fa-plus',
								closeAction: 'destroy',
								alwaysOnTop: true,
								modal: true,
								width: 400,
								height: 380,
								layout: 'fit',
								items: [
									{
										xtype: 'form',
										scrollable: true,
										layout: 'anchor',
										bodyStyle: 'padding: 10px',
										defaults: {
											labelAlign: 'top',
											labelSeparator: '',
											width: '100%'
										},
										items: [
											{
												xtype: 'textfield',
												name: 'label',
												fieldLabel: 'Label <span class="field-required" />',
												allowBlank: false,
												maxLength: 255
											},
											{
												xtype: 'textarea',
												name: 'detailedDescription',
												fieldLabel: 'Description',
												maxLength: 255
											},
											{
												xtype: 'combobox',
												fieldLabel: 'Code Label Value Type <span class="field-required" />',
												displayField: 'description',
												valueField: 'code',
												typeAhead: false,
												editable: false,
												allowBlank: false,
												name: 'attributeValueType',
												store: {
													autoLoad: true,
													proxy: {
														type: 'ajax',
														url: 'api/v1/resource/lookuptypes/AttributeValueType'
													}
												}
											}
										],
										dockedItems: [
											{
												xtype: 'toolbar',
												dock: 'bottom',
												items: [
													{
														text: 'Save',
														formBind: true,
														iconCls: 'fa fa-lg fa-save icon-button-color-save',
														handler: function () {
															var form = this.up('form');
															var data = form.getValues();
															var addTypeWin = this.up('window');

															var componentType = attributePanel.componentType.componentType;
															if (componentType) {
																componentType = encodeURIComponent(componentType);
															} else {
																componentType = '';
															}

															CoreUtil.submitForm({
																url: 'api/v1/resource/attributes/attributetypes/metadata?componentType=' + componentType,
																method: 'POST',
																data: data,
																form: form,
																success: function (response, opts) {
																	
																	var newAttribute = Ext.decode(response.responseText);
																	attributeTypeCb.getStore().add(newAttribute);
																	addTypeWin.close();
																}
															});

														}
													},
													{
														xtype: 'tbfill'
													},
													{
														text: 'Cancel',
														iconCls: 'fa fa-lg fa-close icon-button-color-warning',
														handler: function () {
															this.up('window').close();
														}
													}
												]
											}
										]
									}
								]
							});
							addTypeWin.show();

						}
					}
				]
			},
			{
				xtype: 'combobox',
				itemId: 'attributeCodeCB',
				fieldLabel: 'Attribute Code <span class="field-required" />',
				name: 'code',
				queryMode: 'local',
				labelWidth: 150,
				editable: false,
				typeAhead: false,
				allowBlank: false,
				valueField: 'code',
				displayField: 'label',
				listConfig: {
					getInnerTpl: function () {
						return '{label} <tpl if="description"><i class="fa fa-question-circle" data-qtip=\'{description}\'></i></tpl>';
					}
				},
				store: Ext.create('Ext.data.Store', {
					sorters: [
						{
							property: 'label',
							direction: 'ASC',
							transform: function (item) {
								if (item) {
									item = item.toLowerCase();
								}
								return item;
							}
						}
					],
					fields: [
						"code",
						"label"
					]
				})				
			}
			
		];
		
		//load optional types
		attributePanel.setLoading(true);
		Ext.Ajax.request({
			url: 'api/v1/resource/attributes/optional?componentType=' + attributePanel.componentType.componentType + '&submissionOnly=true',
			callback: function() {
				attributePanel.setLoading(false);
			},
			success: function(response, opts) {
				var attributeTypes = Ext.decode(response.responseText);				
				attributePanel.queryById('attributeTypeCB').getStore().loadData(attributeTypes);				
			}
		});
		
		attributePanel.add(items);
		
		CoreService.userservice.getCurrentUser().then(function (user) {

			if (CoreService.userservice.userHasPermisson(user, 'ALLOW-USER-ATTRIBUTE-TYPE-CREATION')) {
				attributePanel.queryById('addAttributeType').setHidden(false);
			}

		});		

	},
	
	getSubmissionValue: function() {
		var attributePanel = this;
		
		var data = attributePanel.getValues();
		
		var userSubmissionField = {			
			templateFieldId: attributePanel.fieldTemplate.fieldId,
			rawValue: Ext.encode([
				data
			])
		};		
		return userSubmissionField;
	},
	
	reviewDisplayValue: function() {
		var attributePanel = this;
		
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
		var value = '';
		var typeRecord = attributePanel.queryById('attributeTypeCB').getSelection();
		if (typeRecord) {
			value = typeRecord.get('description');
		}
		
		data.push({
			label: attributePanel.queryById('attributeTypeCB').fieldLabel,
			value: value
		});
				
		typeRecord = attributePanel.queryById('attributeCodeCB').getSelection();
		if (typeRecord) {
			value = typeRecord.get('label');
		} else {
			value = attributePanel.queryById('attributeCodeCB').getValue();
		}
		
		data.push({
			label: attributePanel.queryById('attributeCodeCB').fieldLabel,
			value: value
		});		
		
		return template.apply(data);
	}
	
	
});





