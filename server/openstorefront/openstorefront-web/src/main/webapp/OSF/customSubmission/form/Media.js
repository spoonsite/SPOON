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

Ext.define('OSF.customSubmission.form.Media', {
	extend: 'Ext.form.Panel',
	initComponent: function () {
		this.callParent();

		// Because ExtJS does not like to create fields in the 'items' array...
		//	we have to add them on init...
		this.add([
			Ext.create('OSF.component.StandardComboBox', {
				name: 'mediaTypeCode',
				allowBlank: false,
				margin: '0 0 15 0',
				editable: false,
				typeAhead: false,
				width: 450,
				fieldLabel: 'Media Type <span class="field-required" />',
				storeConfig: {
					url: 'api/v1/resource/lookuptypes/MediaType'
				}
			}),
			{
				xtype: 'textfield',
				fieldLabel: 'Caption <span class="field-required" />',
				allowBlank: false,
				maxLength: '255',
				width: 450,
				name: 'caption'
			},
			{
				xtype: 'button',
				text: 'Local Resource',
				width: 450,
				menu: [
					{
						text: 'Local Resource',
						handler: function () {
							var form = this.up('form');
							var button = this.up('button');
							button.setText('Local Resource');
							form.getForm().findField('file').setHidden(false);
							form.getForm().findField('originalLink').setHidden(true);
							
							form.query('[name="iconFlag"]')[0].setDisabled(false);
						}
					},
					{
						text: 'External Link',
						handler: function () {
							var form = this.up('form');
							var button = this.up('button');
							button.setText('External Link');
							form.getForm().findField('file').setHidden(true);
							form.getForm().findField('originalLink').setHidden(false);
							
							form.query('[name="iconFlag"]')[0].setDisabled(true);
						}
					}
				]
			},
			{
				xtype: 'checkbox',
				fieldLabel: 'Hide In Carousel',
				width: 450,
				name: 'hideInDisplay'
			},
			{
				xtype: 'checkbox',
				fieldLabel: 'Used Inline',
				width: 450,
				name: 'usedInline'
			},
			{
				xtype: 'checkbox',
				width: 450,
				fieldLabel: 'Icon <i class="fa fa-question-circle"  data-qtip="Designates a media item to be used as an icon. There should only be one active on a entry at a time."></i>',
				name: 'iconFlag'
			},
			{
				xtype: 'fileFieldMaxLabel',
				itemId: 'upload',
				name: 'file',
				width: 450,
				resourceLabel: 'Upload Media'
			},
			{
				xtype: 'textfield',
				fieldLabel: 'Link',
				hidden: true,
				width: 450,
				maxLength: '255',
				emptyText: 'http://www.example.com/image.png',
				name: 'originalLink'
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
