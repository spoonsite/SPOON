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
						if (!infoPanel.initialLoad) {
							infoPanel.formBuilderPanel.markAsChanged();
						}
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
						if (!infoPanel.initialLoad) {
							infoPanel.formBuilderPanel.markAsChanged();
						}
					}
				}	
			},
			{
				xtype: 'combobox',
				fieldLabel: 'Entry Type <span class="field-required" />',
				labelAlign: 'top',
				itemId: 'entryType',
				name: 'entryType',
				displayField: 'description',
				valueField: 'code',
				editable: false,
				typeAhead: false,
				allowBlank: false,
				hidden: infoPanel.templateRecord.defaultTemplate || false,
				store: {
					autoLoad: true,
					proxy: {
						type: 'ajax',
						url: 'api/v1/resource/componenttypes/lookup'
					}
				},
				listeners: {
					change: function (field, newValue, oldValue) {
						infoPanel.templateRecord.entryType = newValue;
						if (!infoPanel.initialLoad) {
							infoPanel.formBuilderPanel.markAsChanged();							
							Ext.Msg.show({
								title:'Check Attributes',
								message: 'Check all attribute questions to verify they are still valid.',
								buttons: Ext.Msg.OK,
								icon: Ext.Msg.INFO,
								fn: function(btn) {
								}
							});
							infoPanel.formBuilderPanel.displayPanel.reloadCurrentSection();		
						}
						infoPanel.initialLoad = false;
						
						infoPanel.formBuilderPanel.reloadAttributes(newValue);							
						
						
					}
				}
			}			
			
		];		
		
		infoPanel.add(items);
		
		var record = Ext.create('Ext.data.Model', {			
		});
		record.set(infoPanel.templateRecord);
		
		infoPanel.initialLoad = true;
		infoPanel.loadRecord(record);
		
		if (record.get('entryType')) {
			//make sure the grid have finish rendering
			Ext.defer(function(){
				infoPanel.formBuilderPanel.requiredAttrProgressPanel.loadGridStore(record.get('entryType'));
				infoPanel.formBuilderPanel.optionalAttrProgressPanel.loadGridStore(record.get('entryType'));
			}, 100);			
		} else {
			infoPanel.initialLoad = false;
		}
		
	}	
	
});
	

