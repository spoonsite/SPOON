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

Ext.define('OSF.customSubmissionTool.FormTemplateInfoPanel', {
	extend: 'Ext.form.Panel',
	alias: 'widget.osf-form-templateinfo-panel',
	
	layout: 'anchor',
	scrollable: true,
	bodyStyle: 'padding: 10px;',
	defaults: {
		labelAlign: 'top',
		labelSeparator: '',
		width: '100%'
	},

	initComponent: function () {
		this.callParent();
		var infoPanel = this;
		
		
		var items = [
			{
				xtype: 'textfield',
				fieldLabel: 'Name <span class="field-required" />',
				name: 'name',
				maxLength: 255,
				allowBlank: false,
				listeners: {
					change: function(field, newValue, oldValue, opts) {
						infoPanel.templateRecord.name = newValue;
						infoPanel.formBuilderPanel.markAsChanged();
					}
				}
			},
			{
				xtype: 'textfield',
				fieldLabel: 'Description <span class="field-required" />',
				labelAlign: 'top',
				name: 'description',
				maxLength: 255,
				allowBlank: false,
				listeners: {
					change: function(field, newValue, oldValue, opts) {
						infoPanel.templateRecord.description = newValue;
						infoPanel.formBuilderPanel.markAsChanged();
					}
				}	
			},
			{
				xtype: 'panel',
				itemId: 'lastSaved',
				data: infoPanel.templateRecord,
				tpl: '<b>Last Saved: </b> {[Ext.Date.format(values.updateDts, "F j, Y, g:i a")]}'+
					 ' <tpl if="unsavedChanges"><span class="text-danger"><i class="fa fa-lg fa-exclamation-triangle"></i> Unsaved Changes</span></tpl> '	
			}
			
		];		
		
		infoPanel.add(items);
		
		var record = Ext.create('Ext.data.Model', {			
		});
		record.set(infoPanel.templateRecord);
		
		infoPanel.loadRecord(record);
	},
	
	updateInfo: function(unsavedChanges) {		
		var infoPanel = this;
		
		infoPanel.queryById('lastSaved').update(Ext.apply({
			unsavedChanges: unsavedChanges
		}, infoPanel.templateRecord));		
		
	}
	
	
});
	

