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

Ext.define('OSF.customSubmission.form.Relationships', {
	extend: 'Ext.form.Panel',
	initComponent: function () {
		this.callParent();
		
		this.add([
			Ext.create('OSF.component.StandardComboBox',{
				name: 'relationshipType',
				allowBlank: false,
				editable: false,
				typeAhead: false,
				width: 400,
				margin: '0 0 0 0',
				width: '100%',
				fieldLabel: 'Relationship Type <span class="field-required" />',
				storeConfig: {
					url: 'api/v1/resource/lookuptypes/RelationshipType'
				}
				}),
			Ext.create('OSF.component.StandardComboBox', {
				name: 'componentType',
				allowBlank: true,
				editable: false,
				typeAhead: false,
				emptyText: 'All',
				margin: '0 0 0 0',
				width:400,
				//width: '100%',
				fieldLabel: 'Entry Type',
				storeConfig: {
					url: 'api/v1/resource/componenttypes/lookup',
					addRecords: [
						{
							code: null,
							description: 'All'
						}
					]
				},
				listeners: {
					change: function (cb, newValue, oldValue) {
						var form = cb.up('form');
						var componentType = '';
						if (newValue) {
							componentType = '&componentType=' + newValue;
						}
						form.getComponent('relationshipTargetCB').reset();
						form.getComponent('relationshipTargetCB').getStore().load({
							url: 'api/v1/resource/components/lookup?status=A&approvalState=ALL' + componentType
						});
					}
				}
				}),											
			Ext.create('OSF.component.StandardComboBox', {
				itemId: 'relationshipTargetCB',
				name: 'relatedComponentId',
				allowBlank: false,
				margin: '0 0 0 0',
				//width: '100%',
				width: 400,
				fieldLabel: 'Target Entry <span class="field-required" />',
				forceSelection: true,
				storeConfig: {
					url: 'api/v1/resource/components/lookup?status=A&approvalState=ALL',
					autoLoad: true
				}
			})							
		]);		
	}
});
