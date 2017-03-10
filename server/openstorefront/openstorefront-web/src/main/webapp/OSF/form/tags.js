/* 
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
/* global Ext, CoreUtil */

Ext.define('OSF.form.Tags', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.form.Tags',
	
	layout: 'fit',
		
	//bodyStyle: 'padding: 20px',
	initComponent: function () {			
		this.callParent();
		
		var tagPanel = this;		
		
		tagPanel.tagGrid = Ext.create('Ext.grid.Panel', {
			columnLines: true,
			store: Ext.create('Ext.data.Store', {
				fields: [
					"text",
					"tagId",
					"activeStatus",
					"createUser",
					{
						name: 'createDts',
						type:	'date',
						dateFormat: 'c'
					}														
				],
				autoLoad: false,
				proxy: {
					type: 'ajax'							
				}
			}),
			columns: [
				{ text: 'Tag', dataIndex: 'text', flex: 1, minWidth: 200 },
				{ text: 'Create User', align: 'center', dataIndex: 'createUser', width: 150 },
				{ text: 'Create Date', dataIndex: 'createDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
				{ text: 'Security Marking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: !${branding.allowSecurityMarkingsFlg} }
			],
			listeners: {
				selectionchange: function(grid, record, index, opts){
					var fullgrid = Ext.getCmp('tagGrid');
					if (fullgrid.getSelectionModel().getCount() === 1) {
						fullgrid.down('toolbar').getComponent('removeBtn').setDisabled(false);
					} else {
						fullgrid.down('toolbar').getComponent('removeBtn').setDisabled(true);
					}
				}						
			},
			dockedItems: [
				{
					xtype: 'form',
					layout: 'anchor',
					padding: 10,
					defaults: {
						labelAlign: 'top',
						labelSeparator: ''
					},
					items: [
						{
							xtype: 'textfield',
							fieldLabel: 'Tag<span class="field-required" />',
							allowBlank: false,
							margin: '0 20 0 0',
							width: '100%',
							maxLength: 120,
							name: 'text',
							listeners: {
								specialkey: function(field, e){
									if (e.getKey() === e.ENTER) {
									   actionAddTag(this.up('form'), Ext.getCmp('tagGrid'));
									}
								}
							}
						},
						Ext.create('OSF.component.SecurityComboBox', {							
						}),								
						{
							xtype: 'button',
							text: 'Add',
							formBind: true,
							iconCls: 'fa fa-lg fa-plus',
							handler: function(){
								actionAddTag(this.up('form'), Ext.getCmp('tagGrid'));
							}
						}
					]
				},						
				{
					xtype: 'toolbar',
					items: [							
						{
							text: 'Refresh',
							iconCls: 'fa fa-lg fa-refresh icon-button-color-refresh',
							handler: function(){
								this.up('grid').getStore().reload();
							}
						},
						{
							xtype: 'tbfill'
						},
						{
							text: 'Delete',
							itemId: 'removeBtn',
							iconCls: 'fa fa-lg fa-trash icon-button-color-warning',									
							disabled: true,
							handler: function(){
								actionSubComponentToggleStatus(Ext.getCmp('tagGrid'), 'tagId', 'tags');
							}
						}
					]
				}
			]);		
		
	},
	
	loadData: function(evaluationId, componentId, data, opts) {
		
	}
	
	
});

