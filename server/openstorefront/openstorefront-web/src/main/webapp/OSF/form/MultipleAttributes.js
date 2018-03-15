/* 
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
/* global Ext, CoreUtil */

/*
 *
 * @param componentId
 * @param componentType
 * @param viewOptional (optional)
 *
 */

Ext.define('OSF.form.MultipleAttributes', {
	alias: 'widget.multipleAttributesForm',
	extend: 'Ext.form.Panel',

	layout: 'anchor',
	scrollable: true,
	viewHiddenAttributes: false,

	//	Override submit method
	submit: function (cb) {
		var postData = [];
		var attrForm = this;
		var valid = attrForm.getForm().isValid();

		if (valid)
		{
			var rawData = attrForm.getValues();
			var componentId = attrForm.componentId;
			Ext.Object.each(rawData, function (key, value) {
				if (value)
				{
					if (this.valueTypes[key] === 'NUMBER' && Ext.String.endsWith(value, ".")) { //check percision; this will enforce max allowed
						try {
							var valueNumber = new Number(value);
							if (isNaN(valueNumber))
								throw "Bad Format";
							value = valueNumber.toString();
						} catch (e) {
							valid = false;
							var dataError = {};
							dataError[key] = 'Number must not have a decimal point or have at least one digit after the decimal point.';
							attrForm.getForm().markInvalid(dataError);
						}
					}
					if (valid)
					{
						postData.push({
							attributeType: key,
							attributeCode: value,
							componentAttributePk: {
								attributeType: key,
								attributeCode: value
							}
						});
					}
				}
			}, attrForm);
		}

		if (valid) {
			CoreUtil.submitForm({
				url: 'api/v1/resource/components/' + componentId + '/attributeList',
				method: 'POST',
				data: postData,
				form: attrForm,
				success: function () {
					attrForm.reset();
					cb(attrForm);
				}
			});
		} else {
			Ext.Msg.show({
				title: 'Form Validation Error',
				message: 'There are errors in the attributes submitted',
				buttons: Ext.Msg.OK,
				icon: Ext.Msg.ERROR
			});
		}
	},

	cancel: function (cb) {
		var attrForm = this;
		var rawData = attrForm.getValues();
		var unSavedData = false;

		Ext.Object.each(rawData, function (key, value) {
			if (value)
			{
				unSavedData = true;
				return false;
			}
		});
		if (unSavedData)
		{
			Ext.Msg.show({
				title: 'Unsaved data',
				message: 'Warning unsaved data will be lost.',
				buttons: Ext.Msg.YESNO,
				buttonText: {
					yes: "OK",
					no: "Cancel"
				},
				icon: Ext.Msg.WARNING,
				fn: function (btn) {
					if (btn === 'yes') {
						cb(attrForm);
					}
				}
			});

		} else
		{
			cb(attrForm);
		}
	},

	initComponent: function () {
		var attrForm = this;
		attrForm.callParent();

		if (!attrForm.componentId) {
			console.error("OSF.form.MultipleAttributes : You must specific a component id (componentId)!");
		}
		if (!attrForm.componentType) {
			console.error("OSF.form.MultipleAttributes : You must specific a component type (componentType)!");
		}

		attrForm.setLoading(true);

		Ext.Ajax.request({
			url: 'api/v1/resource/attributes/optional?componentType=' + attrForm.componentType,
			callback: function () {
				attrForm.setLoading(false);
			},
			success: function (response, opts) {
				var items = new Array();
				var attributes = Ext.decode(response.responseText);
				var valueTypes = [];
				Ext.Array.forEach(attributes, function (attribute, key) {
					var label = attribute.description;
					if (attribute.detailedDescription !== undefined)
					{
						label = Ext.String.format('{0} <i class="fa fa-question-circle"  data-qtip="{1}"></i>', attribute.description, attribute.detailedDescription.replace(/"/g, '&quot;'));
					}
					var vtype = undefined;
					if (attribute.attributeValueType === 'NUMBER')
					{
						vtype = 'AttributeNumber';
						valueTypes[attribute.attributeType] = attribute.attributeValueType;
					}
					var item = {
						name: attribute.attributeType,
						itemId: 'multiAttributeCode_' + attribute.attributeType,
						width: '98%',
						labelStyle: 'width:300px',
						labelWidth: '100%',
						xtype: 'combobox',
						margin: '10 0 10 10',
						fieldLabel: label,
						queryMode: 'local',
						editable: attribute.allowUserGeneratedCodes,
						typeAhead: false,
						allowBlank: true,
						valueField: 'code',
						displayField: 'label',
						vtype: vtype,
						store: Ext.create('Ext.data.Store', {
							fields: [
								"code",
								"label"
							],
							data: attribute.codes
						}),
						listConfig: {
							getInnerTpl: function () {
								return '{label} <tpl if="description"><i class="fa fa-question-circle" data-qtip=\'{description}\'></i></tpl>';
							}
						}
					};
					if (attrForm.viewHiddenAttributes || !attribute.hideOnSubmission) {
						items.push(item);
					}
				});
				attrForm.add(items);
				attrForm.valueTypes = valueTypes;
			}
		});
	}
});
