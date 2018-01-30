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

Ext.define('OSF.customSubmission.form.Tags', {
	extend: 'Ext.form.Panel',
	initComponent: function () {
		this.callParent();
		// Because ExtJS does not like to create fields in the 'items' array...
		//	we have to add them on init...
		this.add(Ext.create('OSF.component.StandardComboBox', {
			name: 'text',
			allowBlank: false,
			margin: '0 0 0 0',
			width: 400,
			fieldLabel: 'Tag<span class="field-required" />',
			forceSelection: false,
			valueField: 'text',
			displayField: 'text',
			maxLength: 120,
			storeConfig: {
				url: 'api/v1/resource/components/tags'
			}
		}));

	}
});