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

/* global Ext */

Ext.define('OSF.customSubmission.form.Relationships', {
	extend: 'OSF.customSubmission.SubmissionBaseForm',
	xtype: 'osf-submissionform-relationships',
	
	layout: 'anchor',
	bodyStyle: 'padding: 10px',
	defaults: {
		width: '100%',
		maxWidth: 800,
		labelAlign: 'top',
		labelSeparator: ''		
	},
	
	initComponent: function () {
		this.callParent();
		
		var relationshipPanel = this;
		
		relationshipPanel.add([
			{
				xtype: 'StandardComboBox',
				name: 'relationshipType',
				allowBlank: false,
				editable: false,
				typeAhead: false,
				margin: '0 0 0 0',
				fieldLabel: 'Relationship Type <span class="field-required" />',
				storeConfig: {
					url: 'api/v1/resource/lookuptypes/RelationshipType'
				}
			},
			{
				xtype: 'StandardComboBox',
				name: 'componentType',
				colName: 'Entry Type',
				allowBlank: true,
				editable: false,
				typeAhead: false,
				emptyText: 'All',
				margin: '0 0 0 0',
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
			},											
			{
				xtype: 'StandardComboBox',
				itemId: 'relationshipTargetCB',
				name: 'relatedComponentId',
				colName: 'Target Entry',
				allowBlank: false,
				margin: '0 0 10 0',
				fieldLabel: 'Target Entry <span class="field-required" />',
				forceSelection: true,
				storeConfig: {
					url: 'api/v1/resource/components/lookup?status=A&approvalState=ALL',
					autoLoad: true
				}
			}							
		]);		
	}
});
