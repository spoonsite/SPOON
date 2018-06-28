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

// @author cyearsley

Ext.define('OSF.customSubmissionTool.AttributeProgressPanel', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-csf-attributeprogresspanel',

	layout: 'fit',
	config: {
		displayRequiredAttributes: false,
		title: null,
		templateRecord: null
	},

	initComponent: function () {

		this.callParent();

		var progressPanel = this;
		if (progressPanel.getDisplayRequiredAttributes()) {
			progressPanel.setTitle('Required Attributes');
		}
		else {
			progressPanel.setTitle('Optional Attributes');
		}

		progressPanel.loadGridStore();
	},

	loadGridStore: function (entryType) {

		var progressPanel = this;
		var entryType = entryType || progressPanel.templateRecord.entryTypeCode;

		var gridStore = Ext.create('Ext.data.Store', {
			fields: ['description'],
			proxy: {
				type: 'ajax',
				url: 'api/v1/resource/attributes/' + (progressPanel.displayRequiredAttributes ? 'required' : 'optional') + 
					'?componentType=' + entryType
			}
		});

		progressPanel.queryById('attributeProgressGrid').setStore(gridStore);
		gridStore.reload();
	},

	items: [
		{
			xtype: 'gridpanel',
			itemId: 'attributeProgressGrid',
			scrollable: true,
			columns: [
				{ text: 'Attribute Name', dataIndex: 'description', flex: 1 }
			],
			store: Ext.create('Ext.data.Store')
		}
	]
});
