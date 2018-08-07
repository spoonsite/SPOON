<%--
/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
	<stripes:layout-component name="contents">

		<stripes:layout-render name="../../../../layout/adminheader.jsp">		
		</stripes:layout-render>		
		
		<script src="scripts/component/visualSearch.js?v=${appVersion}" type="text/javascript"></script>	
		<script src="scripts/plugin/cellToCellDragDrop.js?v=${appVersion}" type="text/javascript"></script>	
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function(){	
				
				var visualPanel = Ext.create('OSF.component.VisualSearchPanel', {
					id: 'visualPanel',
					height: '100%',
					viewType: null
				});


				var actionCreateRelationship = function actionCreateRelationship(originId, originName, targetId, targetName, relationshipTypeCode) {

					if (relationshipTypeCode !== null) {
						// Create the relationship as specified
						var url = 'api/v1/resource/components/' + originId + '/relationships';
						var method = 'POST';
						var jsonData = {
							relationshipType: relationshipTypeCode,
							relatedComponentId: targetId
						};

						Ext.Ajax.request({
							url: url,
							method: method,
							jsonData: jsonData,
							success: function (response, opts) {
								var message = 'Sucessfully created relationship for "' + originName + '"';
								Ext.toast(message, '', 'tr');
								Ext.getCmp('relationshipsGrid').getStore().load();
							},
							failure: function (response, opts) {
								Ext.MessageBox.alert('Failed to create relationship for "' + originName + '"');
							}
						});

					} else {
						// Prompt for type of relationship to create
						var html = '<strong>Origin Entry:</strong> ';
						html += originName;
						html += '<br />';
						html += '<strong>Target Entry:</strong> ';
						html += targetName;
						Ext.getCmp('relationshipWindowSelectorDesc').update(html);
						typePromptWindow.originId = originId;
						typePromptWindow.originName = originName;
						typePromptWindow.targetId = targetId;
						typePromptWindow.targetName = targetName;
						typePromptWindow.openSource = 'dragdrop';
						typePromptWindow.show();
					}

				};

				var entryTypeStore = Ext.create('Ext.data.Store', {
					storeId: 'entryTypeStore',
					proxy: {
						type: 'ajax',
						url: 'api/v1/resource/componenttypes/lookup'
					},
					autoLoad: true
				});
				
				var componentRelationshipsListingStore = Ext.create('Ext.data.Store', {
					storeId: 'componentRelationshipsListingStore',
					proxy: {
						type: 'ajax',
						url: 'api/v1/resource/componentrelationship'
					},
					listeners: {
						load: function () {
							
							componentsStore.each(function(component) {
								component.set('numRelationships', parseInt(0));
							});

							componentRelationshipsListingStore.each(function (componentRelationship) {
								var id = componentRelationship.get('ownerComponentId');

								// Count up instances of item and assign a number to the component
								var record = Ext.getStore('componentsStore').findRecord('componentId', id);
								if (record) {
									if (record.get('numRelationships')) {
										record.set('numRelationships', 1 + parseInt(record.get('numRelationships')));
									} else {
										record.set('numRelationships', parseInt(1));
									}
								}

							});
						}
					}
				});
			
				var relationshipsStore = Ext.create('Ext.data.Store', {
					storeId: 'relationshipsStore',
					listeners: {
						load: function() {
							var viewData = [];
								relationshipsStore.each(function(relationship){
									viewData.push({
										type: 'component',
										nodeId: relationship.get('relationshipId'),
										key: relationship.get('ownerComponentId'),
										label: relationship.get('ownerComponentName'),
										relationshipLabel: relationship.get('relationshipTypeDescription'),
										targetKey: relationship.get('targetComponentId'),
										targetName: relationship.get('targetComponentName'),
										targetType: 'component'
									});
								});
								var visPanel = Ext.getCmp('visualPanel');
								visPanel.viewType = null;
								visPanel.reset();
								visPanel.viewData = viewData;
								visPanel.initVisual(visPanel.viewData);

								componentRelationshipsListingStore.load();
						}
					}
				});

				var relationshipTypeStore = Ext.create('Ext.data.Store', {
					storeId: 'relationshipTypeStore',
					proxy: {
						type: 'ajax',
						url: 'api/v1/resource/lookuptypes/RelationshipType/view'
					},
					autoLoad: true
				});
													   

				var componentsStore = Ext.create('Ext.data.Store', {
					storeId: 'componentsStore',
					fields: [
						// The griddragdrop plugin needs a type in order to operate!
						{
							name: 'name',
							type: 'string'
						}
					],
					sorters: new Ext.util.Sorter({
						property: 'name',
						direction: 'ASC'
					}),
					proxy: {
						id: 'componentsStoreProxy',
						type: 'ajax',
						url: 'api/v1/resource/components/'
					},
					autoLoad: true,
					listeners: {
						load: function (store, records, successful, eOpts) {
							componentRelationshipsListingStore.load();
							Ext.getStore('targetStore').add(records);
						}
					}
				}); 

				var targetStore = Ext.create('Ext.data.Store', {
					storeId: 'targetStore',
					fields: [
						{ 
							name: 'name',
							type: 'string'
						}
					],
					sorters: new Ext.util.Sorter({
						property: 'name',
						direction: 'ASC'
					})
				});

				
				var typePromptWindow = Ext.create('Ext.window.Window', {
					id: 'typePromptWindow',
					title: 'Choose Relationship Type',
					modal: true,
					width: '35%',
					height: 225,
					y: '10em',
					layout: 'vbox',
					items: [
						{ 
							xtype: 'panel',
							id: 'relationshipWindowSelectorDesc',
							style: 'padding: 20px 20px 0px'
						},
						{
							xtype: 'combobox',
							id: 'relationshipWindowSelector',
							style: 'padding: 20px;',
							displayField: 'description',
							valueField: 'code',
							width: '100%',
							emptyText: 'Select a relationship type',
							store: relationshipTypeStore
						}
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [								
								{
									text: 'Apply',
									iconCls: 'fa fa-lg fa-check icon-button-color-save',
									handler: function() {
										var originId = typePromptWindow.originId;
										var originName = typePromptWindow.originName;
										var targetName = typePromptWindow.targetName;
										var targetId = typePromptWindow.targetId;
										var record = Ext.getCmp('relationshipWindowSelector').getSelection();
										if (!record) {
											Ext.MessageBox.alert('Relationship Type Missing', 'Please choose a relationship type.');
											return;
										}
										var relationshipTypeCode = record.get('code');
										var url = 'api/v1/resource/components/' + originId + '/relationships';
										var method = 'POST';
										var jsonData = {
											relationshipType: relationshipTypeCode,
											relatedComponentId: targetId
										};

										Ext.Ajax.request({
											url: url,
											method: method,
											jsonData: jsonData,
											success: function (response, opts) {
												var message = 'Sucessfully created relationship for "' + originName + '"';
												Ext.toast(message, '', 'tr');
												Ext.getCmp('relationshipsGrid').getStore().load();
												typePromptWindow.hide();
											},
											failure: function (response, opts) {
												Ext.MessageBox.alert('Failed to create relationship for "' + originName + '"');
												typePromptWindow.hide();
											}
										});
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Cancel',
									iconCls: 'fa fa-lg fa-close icon-button-color-warning',
									handler: function() {
										this.up('window').hide();
									}
								}								
							]
						}
					]
				});


				var originGrid = Ext.create('Ext.grid.Panel', {
					id: 'originGrid',
					store: componentsStore,
					flex: 1,
					border: true,
					autoScroll: true,
					viewConfig: {
						plugins: {
							ddGroup: 'relationship',
							ptype: 'celltocelldragdrop',
							enableDrop: false
						}
					},
					columns: [
						{ 
							text: 'Origin Entry', 
							dataIndex: 'name', 
							flex: 1,
							renderer: function (value, metaData, record) {
								var num = record.get('numRelationships');
								if (!num) num = 0;
								var html = "<strong>" + value + "</strong>";
								html += '<div style="color: #999; margin: 1em 0; padding: 0 0 0.75em 0;">';
								html += '<i class="fa fa-book fa-fw icon-small-vertical-correction-book" style="float:left; margin-right: 2px;"></i> ';
								html += '<span style="float: left;">' + record.get('componentTypeDescription') + '</span>';
								html += '<span style="float: right"><i class="fa fa-share-alt"></i> ' + num + '</span>';
								html += "</div>";
								return html;
							}
						}
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									xtype: 'tagfield',
									fieldLabel: 'Entry Types',
									flex: 1,
									store: entryTypeStore,
									valueField: 'code',
									displayField: 'description',
									emptyText: 'All',
									listeners: {
										change: function (tagfield, newValue, oldValue, eOpts) {
											componentsStore.clearFilter();
											componentsStore.selectedValues = newValue;
											if (newValue.length > 0) {
												componentsStore.filterBy(filter = function multiFilter(record) {
													return Ext.Array.contains(componentsStore.selectedValues, record.get('componentType'));
												});
											}
										}
									}
								}
							]
						}
					],
					listeners: {
						select: function(grid, record, index, eOpts) {
							var id = record.get('componentId');
							relationshipsStore.setProxy({
								type: 'ajax',
								url: 'api/v1/resource/components/' + id + '/relationships'
							});
							relationshipsStore.load();
						}
					}
				});

				var targetGrid = Ext.create('Ext.grid.Panel', {
					id: 'targetGrid',
					store: targetStore,
					flex: 1,
					border: true,
					requiredPermissions: ['ADMIN-ENTRY-RELATIONSHIP-MANAGEMENT'],
					viewConfig: {
						plugins: [
							Ext.create('OSF.plugin.CellToCellDragDrop', {
								id: 'celltocell',
								ddGroup: 'relationship',
								enableDrop: true,
								enableDrag: false,
								onDrop: function onDrop(target, dd, e, dragData) {
									var originId = dragData.record.data.componentId;
									var originName = dragData.record.data.name; 
									var targetId = target.record.data.componentId;
									var targetName = target.record.data.name;
									var relationshipTypeCode = Ext.getCmp('relationshipTypeComboBox').getValue();
									if (originId === targetId) {
										Ext.Msg.alert('Relationship Not Allowed', 'Relationships from an entry to itself are not allowed.');
									} else {
										actionCreateRelationship(originId, originName, targetId, targetName, relationshipTypeCode);
									}
								},
								onEnter: function(target, dd, e, dragData) {
									var originName = dragData.record.data.name; 
									var targetName = target.record.data.name;
									var relationshipTypeCode = Ext.getCmp('relationshipTypeComboBox').getSelection();
									if (relationshipTypeCode) {
										var relationship = relationshipTypeCode.get('description');
									}
									else var relationship = ' -> ';

									var text = originName + ' ';
								    text += relationship + ' ';
									text += targetName;
									dd.ddel.innerText = text;
								},
								onOut: function(target, dd, e, dragData) {
									var originName = dragData.record.data.name; 
									dd.ddel.innerText = originName;
								}
							})
						]
					},
					columns: [
						{ 
							text: 'Target Entry',
							dataIndex: 'name',
							flex: 1,
							renderer: function (value, metaData, record) {
								var num = record.get('numRelationships');
								if (!num) num = 0;
								var html = "<strong>" + value + "</strong>";
								html += '<div style="color: #999; margin: 1em 0; padding: 0 0 0.75em 0;">';
								html += '<i class="fa fa-book fa-fw icon-small-vertical-correction-book" style="float:left; margin-right: 2px;"></i> ';
								html += '<span style="float: left;">' + record.get('componentTypeDescription') + '</span>';
								html += "</div>";
								return html;
							}

						}
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									xtype: 'tagfield',
									fieldLabel: 'Entry Types',
									flex: 1,
									store: entryTypeStore,
									valueField: 'code',
									displayField: 'description',
									emptyText: 'All',
									listeners: {
										change: function (tagfield, newValue, oldValue, eOpts) {
											targetStore.clearFilter();
											targetStore.selectedValues = newValue;
											if (newValue.length > 0) {
												targetStore.filterBy(filter = function multiFilter(record) {
													return Ext.Array.contains(targetStore.selectedValues, record.get('componentType'));
												});
											}
										}
									}
								}
							]
						}
					]
				});

				var relationshipsGrid = Ext.create('Ext.grid.Panel', {
					id: 'relationshipsGrid',
					title: 'Existing Relationships',
					region: 'center',
					flex: 2,
					width: '100%',
					store: relationshipsStore,
					autoScroll: true,
					viewConfig: {
						emptyText: 'You have not selected an origin entry or the entry you selected has no existing relationships.',
						deferEmptyText: false
					},
					columns: [
						{ text: 'Origin Entry', dataIndex: 'ownerComponentName', flex: 5 },
						{
							text: 'Relationship Type',
							flex: 2,
							xtype: 'widgetcolumn',
							dataIndex: 'relationshipType',
							widget: {
								xtype: 'combo',
								store: relationshipTypeStore,
								editable: false,
								displayField: 'description',
								valueField: 'code',
								listeners: {
									select: function(combo, newValue, oldValue, eOpts) {
										var record = combo.record;
										var originId = record.get('ownerComponentId');
										var relationId = record.get('relationshipId');
										var targetId = record.get('targetComponentId');
										var relationshipType = newValue.get('code');
										var url = 'api/v1/resource/components/' + originId + '/relationships/';
										url += relationId;
										var method = 'PUT';
										var jsonData = {
											relationshipType: relationshipType,
											relatedComponentId: targetId
										};

										Ext.Ajax.request({
											url: url,
											method: method,
											jsonData: jsonData,
											success: function (response, opts) {
												var message = 'Sucessfully updated relationship';
												Ext.toast(message, '', 'tr');
												Ext.getCmp('relationshipsGrid').getStore().load();
											},
											failure: function (response, opts) {
												Ext.MessageBox.alert('Failed to update relationship');
											}
										});

									}
								}
							},
							onWidgetAttach: function(col, widget, rec) {
								widget.record = rec;
							}
						},
						{ text: 'Target Entry', dataIndex: 'targetComponentName', flex: 5 }
					],
					listeners: {
						select: function(grid, record, index, eOpts) {
							if (relationshipsGrid.getSelectionModel().hasSelection()) {
								Ext.getCmp('relationshipGridAction-Delete').enable();
							} else {
								Ext.getCmp('relationshipGridAction-Delete').disable();
							}

						}
					},
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',
							id: 'relationshipGridToolbar',
							items: [
								{
									text: 'Refresh',
									iconCls: 'fa fa-lg fa-refresh icon-button-color-refresh',
									handler: function () {
										relationshipsStore.load();
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									id: 'relationshipGridAction-Delete',
									iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
									disabled: true,
									handler: function() {
										var record = Ext.getCmp('relationshipsGrid').getSelection()[0];
										var title = 'Delete Relationship';
										var msg = 'Are you sure you want to delete this relationship?';
										Ext.MessageBox.confirm(title, msg, function (btn) {
											if (btn === 'yes') {
												var url = 'api/v1/resource/components/';
												url += record.get('ownerComponentId') + "/relationships/";
												url += record.get('relationshipId');
												var method = "DELETE";
												Ext.Ajax.request({
													url: url,
													method: method,
													success: function (response, opts) {
														Ext.toast('Successfully deleted relationship', '', 'tr');
														relationshipsStore.load();
														Ext.getCmp('relationshipGridAction-Delete').disable();
													},
													failure: function (response, opts) {
														Ext.MessageBox.alert('Failed to delete',
																			 'Could not delete relationship.');
													}
												});
											}
										});

									}
								},
							]
						}
					]
				});

				var visualizationPanel = Ext.create('Ext.panel.Panel', {
					flex: 3,
					width: '100%',
					title: 'Visualization',
					collapsible: true,
					region: 'south',
					items: [
						visualPanel
					]
				});

				var relationshipCreationGridsPanel = Ext.create('Ext.panel.Panel', {
					height: '100%',
					width: '100%',
					layout: {
						type: 'hbox',
						align: 'stretch'	
					},
					items: [
						originGrid, targetGrid
					],
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',
							layout: {
								pack: 'center'
							},
							items: [
								Ext.create('OSF.component.StandardComboBox', {
									id: 'relationshipTypeComboBox',
									fieldLabel: 'Type of Relationship to Create',
									width: '60%',
									store: 'relationshipTypeStore',
									displayField: 'description',
									valueField: 'code',
									emptyText: 'Ask for type each time',
									allowBlank: true
								})
							]
						}
					]
				});

				var relationshipsMainLayout = Ext.create('Ext.panel.Panel', {
					title: 'Relationship Management Tool <i class="fa fa-question-circle"  data-qtip="Quickly create relationships between entries by dragging from the origin grid to the target grid."></i>',
					layout: 'border',
					height: '100%',
					items: [
						{
							title: 'Relationship Creation',
							region: 'west',
							xtype: 'panel',
							margin: '5 5 5 5',
							flex: 2,
							id: 'west-container',
							autoScroll: true,
							layout: {
								type: 'hbox'
							},
							items: [
								relationshipCreationGridsPanel
							]
						},
						{
							margin: '5 5 5 5',
							borderWidth: '5px',
							region: 'center',
							xtype: 'panel',
							flex: 3,
							id: 'center-container',
							layout: {
								type: 'border',
								align: 'stretch'
							},
							items: [
								relationshipsGrid, visualizationPanel
							]
						}
					]
				});

				addComponentToMainViewPort(relationshipsMainLayout);

				Ext.defer(function(){
					visualPanel.setWidth(visualizationPanel.getWidth());
					visualPanel.setHeight(visualizationPanel.getHeight()-40);
				}, 400);	




			});
		</script>	
		
	</stripes:layout-component>
</stripes:layout-render>
