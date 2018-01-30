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

Ext.define('OSF.customSubmission.form.Attributes', {
	extend: 'Ext.form.Panel',
	initCompontent: function(){
		this.callParent();
		this.add([
			
			Ext.create('Ext.field.ComboBox',{
				//xtype: 'combobox',
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
					],
					fields: [
						"attributeType",
						"description"
					],
					data: submissionPanel.optionalAttributes
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
			}),
			Ext.create('Ext.field.ComboBox',{
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
			})
		]);
	}
});





