/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/* global Ext, CoreUtil */

Ext.define('OSF.form.Relationships', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.form.Relationships',

	layout: 'fit',
	initComponent: function () {		
		this.callParent();
		
		var relationshipPanel = this;
		
		relationshipPanel.relationshipsGrid = Ext.create('Ext.grid.Panel', {
			columnLines: true,
			viewConfig: {
				enableTextSelection: true
			},
			store: Ext.create('Ext.data.Store', {
				fields: [
					"relationshipId",
					"ownerComponentId",
					"ownerComponentName",
					"ownerApproved",
					"targetComponentId",
					"targetComponentName",
					"targetApproved",
					"relationshipType",
					"relationshipTypeDescription",
					{
						name: 'updateDts',
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
				{ text: 'Relationship Owner', dataIndex: 'ownerComponentName',  width: 200 },
				{ text: 'Owner Approved', dataIndex: 'ownerApproved',  width: 150 },
				{ text: 'Type',  dataIndex: 'relationshipTypeDescription', width: 200 },
				{ text: 'Target',  dataIndex: 'targetComponentName', flex: 1, minWidth: 200 },						
				{ text: 'Target Approved',  dataIndex: 'targetApproved', width: 150 },		
				{ text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s' }
			],
			listeners: {
				selectionchange: function(grid, record, index, opts){
					var fullgrid = relationshipPanel.relationshipsGrid;
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
					title: 'Add/Edit Relationship',
					collapsible: true,
					titleCollapse: true,
					border: true,
					layout: 'vbox',
					bodyStyle: 'padding: 10px;',
					margin: '0 0 5 0', 
					defaults: {
						labelAlign: 'right',
						labelWidth: 200,
						width: '100%'
					},
					buttonAlign: 'center',
					buttons: [
						{
							xtype: 'button',
							text: 'Save',
							formBind: true,
							margin: '0 20 0 0',
							iconCls: 'fa fa-lg fa-save',
							handler: function(){	
								var form = this.up('form');
								var data = form.getValues();
								var componentId = relationshipPanel.componentId;

								var method = 'POST';
								var update = '';
								if (componentId === data.relatedComponentId) {
									Ext.Msg.alert('Relationship Not Allowed', 'Relationships from an entry to itself are not allowed.');
									return;
								}

								CoreUtil.submitForm({
									url: 'api/v1/resource/components/' + componentId + '/relationships' + update,
									method: method,
									data: data,
									form: form,
									success: function(){
										relationshipPanel.relationshipsGrid.getStore().reload();
										form.reset();
									}
								});
							}
						},
						{
							xtype: 'button',
							text: 'Cancel',										
							iconCls: 'fa fa-lg fa-close',
							handler: function(){
								this.up('form').reset();
							}									
						}								
					],
					items: [								
						Ext.create('OSF.component.StandardComboBox', {
							name: 'relationshipType',									
							allowBlank: false,
							editable: false,
							typeAhead: false,
							margin: '0 0 5 0',
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
							margin: '0 0 5 0',
							width: '100%',
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
								change: function(cb, newValue, oldValue) {
									var componentType = '';
									if (newValue) {
										componentType = '&componentType=' + newValue;
									}
									var target = this.up('form').getComponent('relationshipTargetCB');
									target.reset();
									target.getStore().load({
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
							width: '100%',
							fieldLabel: 'Target Entry <span class="field-required" />',
							forceSelection: false,
							storeConfig: {
								url: 'api/v1/resource/components/lookup?status=A&approvalState=ALL'
							}
						})				
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
								CoreUtil.actionSubComponentToggleStatus(relationshipPanel.relationshipsGrid, 'relationshipId', 'relationships');
							}
						}
					]
				}
			]																
		});
		
		
		relationshipPanel.add(relationshipPanel.relationshipsGrid);
	},
	loadData: function(evaluationId, componentId, data, opts, callback) {
		//just load option (filter out required)
		var relationshipPanel = this;
		
		relationshipPanel.componentId = componentId;
		relationshipPanel.relationshipsGrid.componentId = componentId;
		
		relationshipPanel.relationshipsGrid.getStore().load({
			url: 'api/v1/resource/components/' + componentId + '/relationships'
		});
		
		if (opts && opts.commentPanel) {
			opts.commentPanel.loadComments(evaluationId, "Relationships", componentId);
		}

		if (callback) {
			callback();
		}
	}	
	
});


