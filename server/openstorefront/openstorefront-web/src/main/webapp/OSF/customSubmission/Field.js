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

Ext.define('OSF.customSubmission.Field', {
	extend: 'Ext.form.Panel',
	config: {
		label: '',
		labelTip: '',
		labelCode: '',
		questionComment: '',
		isScoped: false,
		canComment: false,
		commentRich: false,
		commentLabel: 'If so, provide details below.',
		hideSecurityMarkings: false
	},
	name: '', // what is this field mapped to?
	fieldType: undefined,
	field: undefined,
	fieldConfig: {},
	items: [
		{columnWidth: 0.75, style: 'padding-right: 50px;', items: []},
		{columnWidth: 0.25, items: []}
	],
	padding: 10,
	layout: 'column',
	initComponent: function () {

		this.callParent();
		var csField = this;

		if (typeof csField.fieldType === 'undefined') {

			console.warn('WARNING: A customSubmission field was defined and does NOT have a fieldType (String)!');
		}
		else {

			// create the custom field from OSF/customSubmission/field/<field-name>.js
			csField.field = Ext.create('OSF.customSubmission.field.' + csField.fieldType, Ext.apply({name: csField.name, style: 'margin-top: 8px;'}, csField.fieldConfig));

			if (typeof csField.field.getValue === 'undefined') {
				console.warn('WARNING: A customSubmission field was defined and does NOT have a "getValue" function!' + (csField.fieldType ? ' - field type: ' + csField.fieldType : ''));
			}

			csField.updateField();
		}

	},
	getValue: function () {
		return {
			value: this.field.getValue(),
			fieldData: this.getForm().getValues(),
			name: this.name
		}
	},
	getItems: function () {
		return this.items.items;
	},
	getFullLabel: function () {
		var titleCode = this.getLabelCode() ? '<span style="font-weight: bold; margin-right: 5px;">' + this.getLabelCode() + '.</span>' : '';
		var titleLabel = this.getLabel();
		var titleTip = this.getLabelTip() ? '<i class="fa fa-question-circle"  data-qtip="' + this.getLabelTip() + '" style="margin-left: 3px;"></i>' : '';
		return titleCode + titleLabel + titleTip;
	},

	// clear all items from the current field, and replace them
	updateField: function () {
		var csField = this;
		csField.getItems()[0].removeAll(false);
		csField.getItems()[1].removeAll(false);

		csField.getItems()[0].add(Ext.create('Ext.form.Label', {
			html: csField.getFullLabel(),
			style: 'font-size: 1.5em;'
		}));
		csField.getItems()[0].add(csField.field);
		csField.getItems()[0].add(Ext.create('Ext.form.Label', {html: '<h3>' + csField.getQuestionComment() + '</h3>'}));

		if (csField.getCanComment()) {

			csField.getItems()[0].add(Ext.create(csField.getCommentRich() ? 'Ext.ux.form.TinyMCETextArea' : 'Ext.form.field.TextArea', {
				fieldLabel: csField.getCommentLabel(),
				labelAlign: 'top',
				width: '75%',
				height: csField.getCommentRich() ? 300 : 150,
				labelSeparator: '',
				name: 'comments'
			}));
		}

		if (csField.getIsScoped()) {
			csField.getItems()[1].add(Ext.create('Ext.form.ComboBox', {
				fieldLabel: 'Scope',
				queryMode: 'local',
				displayField: 'text',
				valueField: 'value',
				labelAlign: 'top',
				value: 'public',
				editable: false,
				name: 'scope',
				store: Ext.create('Ext.data.Store', {
					fields: ['value', 'text'],
					data: [
						{text: 'Public', value: 'public'},
						{text: 'Private', value: 'private'}
					]
				})
			}));
		}
	}
});