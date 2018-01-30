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

Ext.define('OSF.customSubmission.form.Resources', {
	extend: 'Ext.form.Panel',
	initComponent: function () {
		this.callParent();

		// Because ExtJS does not like to create fields in the 'items' array...
		//	we have to add them on init...
		this.add([
			Ext.create('OSF.component.StandardComboBox', {
				name: 'resourceType',
				allowBlank: false,
				margin: '0 0 15 0',
				editable: false,
				typeAhead: false,
				width: 450,
				fieldLabel: 'Resource Type <span class="field-required" />',
				storeConfig: {
					url: 'api/v1/resource/lookuptypes/ResourceType'
				}
			}),
			{
				xtype: 'textfield',
				labelAlign: 'top',
				fieldLabel: 'Description',
				maxLength: '255',
				width: 450,
				name: 'description'
			},
			{
				xtype: 'checkbox',
				name: 'restricted',
				width: 450,
				boxLabel: 'Restricted'
			},
			{
				xtype: 'button',
				width: 450,
				margin: '0 0 15 0',
				text: 'External Link',
				menu: [
					{
						text: 'External Link',
						handler: function () {
							var form = this.up('form');
							var button = this.up('button');
							button.setText('External Link');
							form.getForm().findField('file').setHidden(true);
							form.getForm().findField('originalLink').setHidden(false);
						}
					},
					{
						text: 'Local Resource',
						handler: function (btn) {
							var form = this.up('form');
							var button = this.up('button');
							button.setText('Local Resource');
							form.getForm().findField('file').setHidden(false);
							form.getForm().findField('originalLink').setHidden(true);
						}
					}
				]
			},
			{
				xtype: 'textfield',
				fieldLabel: 'Link',
				maxLength: '255',
				emptyText: 'http://www.example.com/resource',
				width: 450,
				labelAlign: 'top',
				name: 'originalLink'
			},
			{
				xtype: 'fileFieldMaxLabel',
				itemId: 'upload',
				name: 'file',
				width: 450,
				labelAlign: 'top',
				hidden: true
			},
			// Ext.create('OSF.component.SecurityComboBox', {
			// 	itemId: 'securityMarkings',
			// 	hidden: submissionPanel.hideSecurityMarkings
			// }),
			Ext.create('OSF.component.DataSensitivityComboBox', {
				width: 450
			})
		]);
	}
});
