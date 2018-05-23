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

Ext.define('OSF.customSubmissionTool.EntryTypeSelectWindow', {
	extend: 'Ext.window.Window',
	xtype: 'osf-entrySelectWindow',
	
	title: 'Select Entry type',
	modal: true,
	width: 500,
	height: 200,
	layout: 'fit',	
	closeAction: 'destroy',
	items: [
		{
			xtype: 'form',
			itemId: 'form',
			bodyStyle: 'padding: 10px;',
			layout: 'anchor',
			defaults: {
				labelAlign: 'top',
				width: '100%'
			},
			items: [
				{
					xtype: 'combobox',
					fieldLabel: 'Entry Type <span class="field-required" />',
					name: 'entryType',
					allowBlank: false,
					displayField: 'description',
					valueField: 'code',
					editable: false,
					typeAhead: false,					
					store: {
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/componenttypes/lookup'
						},
						listeners: {
							load: function(store, records, successful, operation, opts) {
								
							}
						}
					}
				}
			],
			dockedItems: [
				{
					xtype: 'toolbar',
					dock: 'bottom',
					items: [
						{
							text: 'Select',
							formBind: true,
							iconCls: 'fa fa-lg fa-check icon-button-color-save',
							handler: function() {
								var selectWin = this.up('window');
								var form = selectWin.queryById('form');
								
								selectWin.selectCallBack(form.getValues().entryType);
								selectWin.close();
							}
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Cancel',
							iconCls: 'fa fa-lg fa-close icon-button-color-warning',
							handler: function() {
								this.up('window').close();
							}							
						}
					]
				}
			]
		}
	],
	initComponent: function () {
		this.callParent();
		var entryTypeWindow = this;
		
		
			
		
	}
	
	
});	

