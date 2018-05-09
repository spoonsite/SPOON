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

Ext.define('OSF.customSubmission.form.Dependencies', {
	extend: 'Ext.form.Panel',
	xtype: 'osf-submissionform-dependency',
		
	layout: 'anchor',
	bodyStyle: 'padding: 10px;',
	fieldType: 'EXT_DEPEND',

	defaults: {
		width: '100%',
		maxWidth: 800,
		labelAlign: 'top',
		labelSeparator: ''		
	},
	
	initComponent: function () {
		this.callParent();
		var dependancyPanel = this;		

		dependancyPanel.add([
			{
				xtype: 'textfield',
				fieldLabel: 'Name <span class="field-required" />',									
				allowBlank: false,	
				maxLength: '255',
				name: 'dependencyName',
				colName: 'name'
			},
			{
				xtype: 'textfield',
				fieldLabel: 'Version <span class="field-required" />',									
				allowBlank: false,								
				maxLength: '255',
				name: 'version'
			},
			{
				xtype: 'textfield',
				fieldLabel: 'External Link',															
				emptyText: 'http://dependency.com/download',									
				maxLength: '255',
				name: 'dependancyReferenceLink',
				colName: 'externalLink'
			},
			{
				xtype: 'textfield',
				fieldLabel: 'Comment',																											
				maxLength: '255',
				name: 'comment'
			},
			{
				xtype: 'SecurityComboBox'
			},
			{			
				xtype: 'DataSensitivityComboBox'
			}
		]);
	}
});
