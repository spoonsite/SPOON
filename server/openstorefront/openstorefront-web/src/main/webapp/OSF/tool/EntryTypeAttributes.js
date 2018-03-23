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

Ext.define('OSF.tool.EntryTypeAttributes', {
	extend: 'Ext.window.Window',
	alias: 'widget.EntryTypeAttributes',
	
	title: 'Entry Type to Attribute Assignment',
	iconCls: 'fa fa-list-alt',
	width: '80%',
	height: '80%',
	closeAction: 'destroy',
	maximizable: true,
	modal: true,	
	layout: {
		type: 'hbox',
		align: 'stretch'
	},
		
	initComponent: function () {	
		this.callParent();
		var attributePanel = this;
		
		
		
		
		var leftPanel = {
			xtype: 'panel',
			width: '50%',
			margin: '0 20 0 0',
			layout: {
				type: 'vbox'				
			},
			items: [
				{
					xtype: 'grid',
					itemId: 'entryTypeGrid',
					title: 'Entry Types',
					columnLines: true,
					border: true,
					width: '100%',
					height: '50%',
					store: {							
					},
					listeners: {
						selectionChange: function(selModel, records) {
							if (records.length > 0) {
								loadAttributes(records[0].get('code'));
							} else {
								//clear 
								attributePanel.queryById('availableTypesGrid').getStore().removeAll();
								attributePanel.queryById('requiredTypesGrid').getStore().removeAll();	
								attributePanel.queryById('optionalTypesGrid').getStore().removeAll();
							}
						}
					},
					columns: [
						{ text: 'Type', dataIndex: 'description', flex: 1 }
					],
					dockedItems: [
						{
							xtype: 'textfield',
							emptyText: 'Search...',
							width: '100%',
							dock: 'top',
							listeners: {
								change: function(filter, newValue, oldValue, opts){
									var grid = filter.up('grid');
									grid.getStore().clearFilter();
									if (newValue) {
										grid.getStore().filterBy(function(record){											
											return record.get('description').toLowerCase().includes(newValue.toLowerCase());
										});
									}
								}
							}
						}
					]
				},
				{
					xtype: 'grid',
					title: 'Unassociated Attributes',
					itemId: 'availableTypesGrid',
					columnLines: true,
					width: '100%',
					height: '50%',
					border: true,
					selModel: {
						selType: 'rowmodel', 
						mode: 'MULTI'
					},					
					store: {						
					},
					viewConfig: {
						plugins: {
							ptype: 'gridviewdragdrop',
							dragText: 'Drag and drop to Add to template'
						}
					},						
					columns: [
						{ text: 'Type', dataIndex: 'description', flex: 1 }
					],
					listeners: {
						drop: function(node, data, overModel, dropPosition, eOpts) {
							if (data.view === attributePanel.queryById('availableTypesGrid').getView()) {
								//internal move
								return;
							} else {
								var attributesToSave = [];
								Ext.Array.each(data.records, function(record) {
									var saveAttribute = {
										attributeType: record.data,
										requiredComponentType: [										
										],
										optionalComponentTypes: [										
										]
									};
									delete saveAttribute.attributeType.type;
									
									//just remove required and optional
									if (saveAttribute.attributeType.requiredRestrictions) {
										Ext.Array.each(saveAttribute.attributeType.requiredRestrictions, function(existing) {
											if (existing.componentType !== attributePanel.selectedComponentType) {												
												saveAttribute.requiredComponentType.push({
													componentType: existing.componentType
												});
											}
										});	
									}
									
									if (saveAttribute.attributeType.optionalRestrictions) {
										Ext.Array.each(saveAttribute.attributeType.optionalRestrictions, function(existing) {
											if (existing.componentType !== attributePanel.selectedComponentType) {												
												saveAttribute.optionalComponentTypes.push({
													componentType: existing.componentType
												});
											}
										});	
									}
									
									attributesToSave.push(saveAttribute);
								});
								saveAttributes(attributesToSave);							
							}
						}
					},					
					dockedItems: [
						{
							xtype: 'textfield',
							itemId: 'availableTypesFilter',
							emptyText: 'Search...',
							width: '100%',
							dock: 'top',
							listeners: {
								change: function(filter, newValue, oldValue, opts){
									var grid = filter.up('grid');
									grid.getStore().clearFilter();
									if (newValue) {
										grid.getStore().filterBy(function(record){											
											return record.get('description').toLowerCase().includes(newValue.toLowerCase());
										});
									}
								}
							}							
						}
					]
				}				
			]
		};

		var rightPanel = {
			xtype: 'panel',
			width: '50%',			
			layout: {
				type: 'vbox'				
			},
			items: [
				{
					xtype: 'grid',
					title: 'Required Attributes',
					itemId: 'requiredTypesGrid',
					columnLines: true,
					border: true,
					width: '100%',
					height: '50%',
					selModel: {
						selType: 'rowmodel', 
						mode: 'MULTI'
					},
					store: {						
					},
					viewConfig: {
						plugins: {
							ptype: 'gridviewdragdrop',
							dragText: 'Drag and drop to Add to template'
						}
					},						
					columns: [
						{ text: 'Type', dataIndex: 'description', flex: 1 }
					],
					listeners: {
						drop: function(node, data, overModel, dropPosition, eOpts) {
							
							if (data.view === attributePanel.queryById('requiredTypesGrid').getView()) {
								//internal move
								return;
							} else {
								var attributesToSave = [];
								Ext.Array.each(data.records, function(record) {
									var saveAttribute = {
										attributeType: record.data,
										requiredComponentType: [										
										],
										optionalComponentTypes: [										
										]
									};
									delete saveAttribute.attributeType.type;									
									
									//add to required
									if (saveAttribute.attributeType.requiredRestrictions) {
										var foundExisting = Ext.Array.findBy(saveAttribute.attributeType.requiredRestrictions, function(existing) {
											if (existing.componentType === attributePanel.selectedComponentType) {
												return existing;
											}
										});	
										if (!foundExisting) {
											saveAttribute.attributeType.requiredRestrictions.push({
												componentType: attributePanel.selectedComponentType
											});
										}
										saveAttribute.requiredComponentType = saveAttribute.requiredComponentType.concat(saveAttribute.attributeType.requiredRestrictions);
									} else {
										saveAttribute.requiredComponentType.push({
												componentType: attributePanel.selectedComponentType
										});
									}
									
									//remove from optional
									if (saveAttribute.attributeType.optionalRestrictions) {
										Ext.Array.each(saveAttribute.attributeType.optionalRestrictions, function(existing) {
											if (existing.componentType !== attributePanel.selectedComponentType) {												
												saveAttribute.optionalComponentTypes.push({
													componentType: existing.componentType
												});
											}
										});	
									}									
									
									attributesToSave.push(saveAttribute);
								});
								saveAttributes(attributesToSave);							
							}
						}
					},
					dockedItems: [
						{
							xtype: 'textfield',
							itemId: 'requiredTypesFilter',
							emptyText: 'Search...',
							width: '100%',
							dock: 'top',
							listeners: {
								change: function(filter, newValue, oldValue, opts){
									var grid = filter.up('grid');
									grid.getStore().clearFilter();
									if (newValue) {
										grid.getStore().filterBy(function(record){											
											return record.get('description').toLowerCase().includes(newValue.toLowerCase());
										});
									}
								}
							}
						}
					]
				},
				{
					xtype: 'grid',
					title: 'Optional Attributes',
					itemId: 'optionalTypesGrid',
					columnLines: true,
					border: true,
					width: '100%',
					height: '50%',
					selModel: {
						selType: 'rowmodel', 
						mode: 'MULTI'
					},					
					store: {						
					},
					viewConfig: {
						plugins: {
							ptype: 'gridviewdragdrop',
							dragText: 'Drag and drop to Add to template'
						}
					},					
					columns: [
						{ text: 'Type', dataIndex: 'description', flex: 1 }
					],
					listeners: {
						drop: function(node, data, overModel, dropPosition, eOpts) {
							
							if (data.view === attributePanel.queryById('optionalTypesGrid').getView()) {
								//internal move
								return;
							} else {
								var attributesToSave = [];
								Ext.Array.each(data.records, function(record) {
									var saveAttribute = {
										attributeType: record.data,
										requiredComponentType: [										
										],
										optionalComponentTypes: [										
										]
									};
									delete saveAttribute.attributeType.type;
									
									//add to optional
									if (saveAttribute.attributeType.optionalRestrictions) {
										var foundExisting = Ext.Array.findBy(saveAttribute.attributeType.optionalRestrictions, function(existing) {
											if (existing.componentType === attributePanel.selectedComponentType) {
												return existing;
											}
										});	
										if (!foundExisting) {
											saveAttribute.attributeType.optionalRestrictions.push({
												componentType: attributePanel.selectedComponentType
											});
										}
										saveAttribute.optionalComponentTypes = saveAttribute.optionalComponentTypes.concat(saveAttribute.attributeType.optionalRestrictions);
									} else {
										saveAttribute.optionalComponentTypes.push({
											componentType: attributePanel.selectedComponentType
										});
									}
									
									//remove from required
									if (saveAttribute.attributeType.requiredRestrictions) {
										Ext.Array.each(saveAttribute.attributeType.requiredRestrictions, function(existing) {
											if (existing.componentType !== attributePanel.selectedComponentType) {												
												saveAttribute.requiredComponentType.push({
													componentType: existing.componentType
												});
											}
										});	
									}
									
									attributesToSave.push(saveAttribute);
								});
								saveAttributes(attributesToSave);							
							}
						
						}
					},					
					dockedItems: [
						{
							xtype: 'textfield',
							itemId: 'optionalTypesFilter',
							emptyText: 'Search...',
							width: '100%',
							dock: 'top',
							listeners: {
								change: function(filter, newValue, oldValue, opts){
									var grid = filter.up('grid');
									grid.getStore().clearFilter();
									if (newValue) {
										grid.getStore().filterBy(function(record){											
											return record.get('description').toLowerCase().includes(newValue.toLowerCase());
										});
									}
								}
							}
						}
					]
				}				
			]			
		};

		attributePanel.add(leftPanel);
		attributePanel.add(rightPanel);		
		
			
		var allAttributeTypes = [];
		var allComponentTypes = [];
		attributePanel.setLoading('Loading Attributes...');
		Ext.Ajax.request({
			url: 'api/v1/resource/attributes/attributetypes',
			callback: function() {
				attributePanel.setLoading(false);
			},
			success: function(response, opts) {
				var data = Ext.decode(response.responseText);
				allAttributeTypes = data.data;
				
				attributePanel.setLoading('Loading Entry Types...');
				Ext.Ajax.request({
					url: 'api/v1/resource/componenttypes/lookup?all=true',
					callback: function() {
						attributePanel.setLoading(false);
					},
					success: function(responseCT, opts) {
						var componentTypeData = Ext.decode(responseCT.responseText);
						
						Ext.Array.sort(componentTypeData, function(a, b) {
							return a.description.localeCompare(b.description);								
						});
						allComponentTypes = componentTypeData;
						attributePanel.queryById('entryTypeGrid').getStore().loadData(allComponentTypes);
					}
				});
				
			}
		});
			
		
		
		var loadAttributes = function(componentType) {
			attributePanel.selectedComponentType = componentType;
			
			attributePanel.setLoading('Loading Required...');
			Ext.Ajax.request({
				url: 'api/v1/resource/attributes/required?componentType=' + componentType,
				callback: function(response, opts) {
					attributePanel.setLoading(false);
				},
				success: function(response, opt) {				
					var requiredAttributes = Ext.decode(response.responseText);

					Ext.Array.sort(requiredAttributes, function(a, b) {
						return a.description.localeCompare(b.description);								
					});

					attributePanel.queryById('requiredTypesFilter').setValue(null);					
					attributePanel.queryById('requiredTypesGrid').getStore().loadData(requiredAttributes);					
					attributePanel.setLoading('Loading Optional...');
					Ext.Ajax.request({
						url: 'api/v1/resource/attributes/optional?componentType=' + componentType,
						callback: function(response, opts) {
							attributePanel.setLoading(false);
						},
						success: function(response, opt) {				
							var optionalAttributes = Ext.decode(response.responseText);
							
							Ext.Array.sort(optionalAttributes, function(a, b) {
								return a.description.localeCompare(b.description);								
							});

							attributePanel.queryById('optionalTypesFilter').setValue(null);
							attributePanel.queryById('optionalTypesGrid').getStore().loadData(optionalAttributes);
							
							var unassociated = [];
							Ext.Array.each(allAttributeTypes, function(type){
								
								var foundRequired = Ext.Array.findBy(requiredAttributes, function(required){
									if (type.attributeType === required.attributeType) {
										return required;
									}	
								});
								var foundOptional = Ext.Array.findBy(optionalAttributes, function(optional){
									if (type.attributeType === optional.attributeType) {
										return optional;
									}	
								});
								if (!foundRequired && !foundOptional) {
									unassociated.push(type);
								}
								
							});	
							Ext.Array.sort(unassociated, function(a, b) {
								return a.description.localeCompare(b.description);								
							});
							attributePanel.queryById('availableTypesFilter').setValue(null);
							attributePanel.queryById('availableTypesGrid').getStore().loadData(unassociated);
							
						}
					});					
				}
			});				
		};
		
		var saveAttributes = function(attributeTypes) {
			if (attributeTypes.length > 0) {				
						
				attributePanel.setLoading('Saving Changes...');
				Ext.Ajax.request({
					url: 'api/v1/resource/attributes/attributetypes/types',
					method: 'PUT',
					jsonData: attributeTypes,
					callback: function() {
						attributePanel.setLoading(false);
					},
					success: function(response, opts) {
						Ext.toast('Saved changes');
					}
				});
				
			}
		};
		
	}
	
});

