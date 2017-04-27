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
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
	<stripes:layout-component name="contents">

		<stripes:layout-render name="../../../../layout/adminheader.jsp">		
		</stripes:layout-render>	
				
		<script type="text/javascript">
			
			//////////////////
			// Begin Stores //
			//////////////////
			
			/* global Ext, CoreUtil */
			Ext.onReady(function () {

				var store_labels_remote = Ext.create('Ext.data.Store', {
					storeId: 'labelStore',
					autoLoad: true,
					fields: [
						'metadataId',
						'componentId',
						'label',
						'value',
						'createUser',
						'createDts',
						'securityMarkingType'
					],
					sorters: 'text',
					proxy: {
						id: 'store_labels_remoteProxy',
						type: 'ajax',
						url: 'api/v1/resource/components/metadata'
					},
					listeners: {
						
						load: function(store, operation, opts) { // Once Data Store Has Loaded
							
							// Initialize Local Labels Data Array
							var localLabels = [];
							
							// Initialize Local Variables
							var localLabel = null;
							var localComponent = null;
							var localMetadata = null;
							
							// Get Record Count
							var recordCount = store.getCount();
							
							// Loop Through Remote Labels
							for (var i = 0; i < recordCount; i++) {
								
								// Store Record Data
								var recordData = store.getAt(i).data;
								
								// Store Current Record Information
								var record_label = recordData.label;
								var record_componentId = recordData.componentId;
								var record_metadataId = recordData.metadataId;
								var record_value = recordData.value;
								
								// Check If Local Label Has Been Initialized
								if (localLabel !== null) {
								
									// Check For Same Label
									if (localLabel.name !== record_label) {
										
										// Push Metadata Onto Local Component
										localComponent.metadata.push(localMetadata);

										// Push Component Onto Local Label
										localLabel.components.push(localComponent);

										// Push Label Onto Local Labels
										localLabels.push(localLabel);

										// Reinitialize Local Label Object
										var localLabel = {};
										localLabel.components = [];
										
										// Reinitialize Local Component Object
										var localComponent = {};
										localComponent.metadata = [];

										// Reinitialize Local Metadata Object
										var localMetadata = {};
									}
									else {
										
										// Check For Same Component
										if (localComponent.id !== record_componentId) {
											
											// Push Metadata Onto Local Component
											localComponent.metadata.push(localMetadata);

											// Push Component Onto Local Label
											localLabel.components.push(localComponent);

											// Reinitialize Local Component Object
											var localComponent = {};
											localComponent.metadata = [];
											
											// Reinitialize Local Metadata Object
											var localMetadata = {};
										}
										else {
											
											// Push Metadata Onto Local Component
											localComponent.metadata.push(localMetadata);

											// Reinitialize Local Metadata Object
											var localMetadata = {};
										}
									}
								}
								else {
									
									// Initialize Local Label Object
									var localLabel = {};
									localLabel.components = [];
									
									// Initialize Local Component Object
									var localComponent = {};
									localComponent.metadata = [];
									
									// Initialize Local Metadata Object
									var localMetadata = {};
								}
								
								// Store Record Label Data
								localLabel.name = record_label;
								
								// Store Record Component Data
								localComponent.id = record_componentId;
								
								// Store Record Metadata Data
								localMetadata.id = record_metadataId;
								localMetadata.value = record_value;
								
								// Check If Current Iteration Is The Last
								if ((i + 1) === recordCount) {
									
									// Push Component Onto Local Label
									localComponent.metadata.push(localMetadata);
									
									// Push Component Onto Local Label
									localLabel.components.push(localComponent);
									
									// Push Label Onto Local Labels
									localLabels.push(localLabel);
								}
							}
							
							// Set Local Label Store Data
							store_labels_local.setData(localLabels);
						}
					}
				});
				
				var store_labels_local = Ext.create('Ext.data.Store', {
					storeId: 'store_labels_local',
					autoLoad: true,
					fields: [
						'name',
						'components',
						'isNew'
					],
					sorters: 'name'
				});
				
				var store_components_remote = Ext.create('Ext.data.Store', {
					storeId: 'store_components_remote',
					autoLoad: true,
					fields: [
						'name',
						'componentId',
						'componentType',
						'componentTypeDescription'
					],
					sorters: new Ext.util.Sorter({
						property: 'name',
						direction: 'ASC'
					}),
					proxy: {
						id: 'store_components_remoteProxy',
						type: 'ajax',
						url: 'api/v1/resource/components/'
					},
					listeners: {
						
						load: function(store, operation, opts) { // Once Data Store Has Loaded
							
							// Initialize Local Components Data Array
							var localComponents = [];
							
							// Loop Through Remote Components
							for (var i = 0; i < store.getCount(); i++) {
									
								// Initialize Current Component
								var currentComponent = {
									
									component: {
									
										// Store Current Component ID
										id: store.getAt(i).data.componentId,

										// Store Current Component Name
										name: store.getAt(i).data.name,

										// Store Current Component Security Level
										type: {

											name: store.getAt(i).data.componentTypeDescription,
											code: store.getAt(i).data.componentType
										}
									}
								};

								// Store Component
								localComponents.push(currentComponent);
							}
							
							// Set Local Component Store Data
							store_components_local.setData(localComponents);
						}
					}					
				});
				
				var component_sorter_ascending = new Ext.util.Sorter({
					
					sorterFn: function (one, two) {

						// Sort Records
						return (one.data.component.name > two.data.component.name) ? 1 : (one.data.component.name === two.data.component.name ? 0 : -1);
					}
				});
				
				var component_sorter_descending = new Ext.util.Sorter({
					
					sorterFn: function (one, two) {

						// Sort Records
						return (two.data.component.name > one.data.component.name) ? 1 : (two.data.component.name === one.data.component.name ? 0 : -1);
					}
				});
				
				var store_components_local = Ext.create('Ext.data.Store', {
					storeId: 'store_components_local',
					autoLoad: true,
					fields: [
						'component'
					],
					sorters: component_sorter_ascending
				});
				
				var store_componentTypes_remote = Ext.create('Ext.data.Store', {
					storeId: 'store_componentTypes_remote',
					proxy: {
						type: 'ajax',
						url: 'api/v1/resource/componenttypes/lookup'
					},
					autoLoad: true
				});
				
				var store_metadataValues_local = Ext.create('Ext.data.Store', {
					storeId: 'store_metadataValues_local',
					autoLoad: true,
					fields: [
						'value'
					]
				});
				
				var transformAssociatedEntryComboBox = function (component) {
					
					// Get Component Data
					var componentData = component.getData();
					
					// Check If Component Data Has Metadata
					if (!componentData.component.metadata) {
						
						// Initialize Metadata
						componentData.component.metadata = {};
						
						// Nullify Metadata ID
						componentData.component.metadata.id = null;
						
						// Empty Out Metadata Value
						componentData.component.metadata.value = "";
					}
					
					// Build Combo Box
					Ext.create('Ext.form.ComboBox', {
						store: Ext.create('Ext.data.Store', {
							
							listeners: {
								
								load: function(store, operation, opts) {
									
									// Store Existing Data
									var data = store_metadataValues_local.getData();
									
									// Load Data Into Store
									store.loadData(data.items);
								}
							}
						}),
						queryMode: 'local',
						displayField: 'value',
						valueField: 'value',
						value: componentData.component.metadata.value,
						fieldLabel: 'Value',
						emptyText: 'Please add value to save',
						labelWidth: new Ext.util.TextMetrics().getWidth("Value:"),
						width: '100%',
						transform: 'select_' + componentData.component.name.replace(/ /g, '_'),
						listeners: {
							
							focus: {
								
								fn: function(field, event, opts) {
								
									// Reload Store
									field.getStore().load();
								}
								
							},
							
							change: {

								buffer: 1000,
								fn: function (field, newValue, oldValue, opts) {
									
									// Check For Empty String
									if (newValue === null || newValue === '') {
										
										return false;
									}

									// Store Selected Label
									var label = labelGrid.getSelection()[0];
									var labelData = label.getData();

									// Check If Label Is New
									if (labelData.isNew) {

										// Set Label Data (For API Request)
										requestData = {

											label: labelData.name.replace(/\*/, ""),
											value: newValue
										};

										// Store "Empty" Component Model
										var emptyComponent = store_labelComponents_local.query('name', 'No Associated Entries', false, true, true).getAt(0);

										// Remove "Empty" Component
										store_labelComponents_local.remove(emptyComponent);
									}
									else {

										// Set Label Data (For API Request)
										requestData = {

											label: labelData.name,
											value: newValue
										};
									}
									
									// Check For Metadata ID
									if (componentData.component.metadata.id !== null && componentData.component.metadata.id !== "") {

										// Add Metadata ID To Request Data
										requestData.metadataId = componentData.component.metadata.id;
									}


									Ext.Ajax.request({

										url: 'api/v1/resource/components/' + componentData.component.id + '/metadata',
										method: 'POST',
										jsonData: requestData,
										success: function (response, opts) {

											// Convert Response To Object
											var responseObject = JSON.parse(response.responseText);

											// Update Label Name
											labelData.name = responseObject.label;

											// Store New Metadata
											var metadata = {

												id: responseObject.metadataId,
												value: responseObject.value
											};
											
											// Lookup Existing Value In Dropdown Store
											var matchedValues = store_metadataValues_local.queryBy(function (record, id) {

												// Store Record Component
												var recordValue = record.get('value');

												// See If Record Matches
												if (recordValue === metadata.value) {

													// Return TRUE
													return true;
												}
												else {

													// Return FALSE
													return false;
												}
											});
											
											// Check For Existing Value In Dropdown Store
											if (matchedValues.items.length === 0) {
											
												// Build Value For Store
												var value = {
													
													value: metadata.value
												};
											
												// Add Metadata Value To Dropdown Store
												store_metadataValues_local.add(store_metadataValues_local.createModel(value));
											}
											
											// Loop Through Label Components
											for (i = 0; i < labelData.components.length; i++) {

												// Check If Label Already Has Component
												if (labelData.components[i].id === componentData.component.id) {

													// Indicate Component Found
													var componentFound = true;
													
													// Initialize Metadata Replaced
													var metadataReplaced = false;
													
													// Loop Through Metadata
													for (j = 0; j < labelData.components[i].metadata.length; j++) {
														
														// Check If Component Already Has Metadata
														if (labelData.components[i].metadata[j].id === metadata.id) {
															
															// Indicate Metadata Replaced
															metadataReplaced = true;
															
															// Replace Metadata
															labelData.components[i].metadata[j] = metadata;
														}
													}
													
													// Check If Metadata Was Replaced
													if (!metadataReplaced) {

														// Add Metadata To Component
														labelData.components[i].metadata.push(metadata);
													}
												}
											}

											// Check If Component Is New Component
											if (!componentFound) {
												
												// Initialize New Component Data
												var newComponentData = {
													
													id: componentData.component.id,
													name: componentData.component.name,
													type: {
														
														code: componentData.component.type.code,
														name: componentData.component.type.name
													},
													metadata: [
														
														metadata
													]
												};

												// Add New Component To Label
												labelData.components.push(newComponentData);
											}
											
											// Push Metadata Onto Component
											componentData.component.metadata = metadata;

											// Update Component Data
											component.set(componentData);

											// Modify "New" Status
											labelData.isNew = false;

											// Update Label Data
											label.set(labelData);

											// Reload Label Store
											labelGrid.getView().refresh();
											
											// Reload Dropdown Data
											field.getStore().load();
											
											// Refresh Label Grid
//											refreshLabelGrid();
											
											// Select Next Label
											// (In Order To "Change Selection")
//											labelGrid.getSelectionModel().deselectAll();
											
											// Re-Select Label
//											labelGrid.getSelectionModel().select([label], false, false);

											// Provide An Notification
											Ext.toast("New Metadata Value Saved Successfully", '', 'tr');
										},
										failure: function (response, opts) {

											// Remove Component From Label Associated Components Store
											store_labelComponents_local.remove(store_labelComponents_local.createModel(componentData));

											// Add Component Back To Components Store
											store_components_local.addSorted(store_components_local.createModel(componentData));

											// Check For "Empty" Component
											if (typeof emptyComponent !== 'undefined' && emptyComponent !== null) {

												// Re-Add "Empty" Component
												store_labelComponents_local.addSorted(emptyComponent);
											}

											// Provide An Error Message
											Ext.toast("An error occurred saving the Label Association", '', 'tr');
										}
									});
								}
							}
						}
					});
				};
				
				var store_labelComponents_local = Ext.create('Ext.data.Store', {
					storeId: 'store_labelComponents_local',
					autoLoad: true,
					fields: [
						'component'
					],
					sorters: component_sorter_ascending,
					listeners: {
						
						refresh: {
							
							buffer: 100,
							fn: function (store, opts) {
							
								// Store Data
								var storeData = store.getData();
								
								// Loop Over Records
								for (i = 0; i < storeData.items.length; i++) {

									// Store Component
									var component = storeData.items[i];

									// TRANSFORM!
									transformAssociatedEntryComboBox(component);
								}
							}
						},
						add: {
							
							buffer: 100,
							fn: function (store, records, index, opts) {
								
								// Loop Over Added Records
								for (i = 0; i < records.length; i++) {
									
									// Store Component
									var component = records[i];
									
									// TRANSFORM!
									transformAssociatedEntryComboBox(component);
								}
							}
						},
						remove: {
							
							buffer: 100,
							fn: function (store, records, index, isMove, opts) {
								
								// Store Data
								var storeData = store.getData();
								
								// Loop Over Records
								for (i = 0; i < storeData.items.length; i++) {

									// Store Component
									var component = storeData.items[i];

									// TRANSFORM!
									transformAssociatedEntryComboBox(component);
								}
							}
						},
						update: {
							
							buffer: 100,
							fn: function (store, record, operation, modifiedFieldNames, details, opts) {
								
								// TRANSFORM!
								transformAssociatedEntryComboBox(record);
							}
						}
					}
				});
				
				////////////////
				// End Stores //
				////////////////
				
				///////////////
				// Overrides //
				///////////////
				
				Ext.override(Ext.view.DragZone, {
					
				    getDragText: function() {
					
				        if (this.dragTextField) {
					
							// Check For Dot In Text Field
							if (this.dragTextField.indexOf('.') !== -1) {
								
								// Get Text Field Split
								var dragTextFieldSplit = this.dragTextField.split('.')
								
								// Get Parent (Permits Only A Single Parent)
								var fieldParent = this.dragData.records[0].get(dragTextFieldSplit[0]);
								
								// Set Field Value
								var fieldValue = fieldParent[dragTextFieldSplit[1]];
							}
							else {
							
								var fieldValue = this.dragData.records[0].get(this.dragTextField);
							}
							
				            return Ext.String.format(this.dragText, fieldValue);
				        }
						else {
							
				            var count = this.dragData.records.length;
				            return Ext.String.format(this.dragText, count, count === 1 ? '' : 's');
				        }
				    }
				});


				Ext.override(Ext.grid.plugin.DragDrop, {
				    onViewRender : function(view) {
				        var me = this;

				        if (me.enableDrag) {
				            me.dragZone = Ext.create('Ext.view.DragZone', {
				                view: view,
				                ddGroup: me.dragGroup || me.ddGroup,
				                dragText: me.dragText,
				                dragTextField: me.dragTextField
				            });
				        }

				        if (me.enableDrop) {
				            me.dropZone = Ext.create('Ext.grid.ViewDropZone', {
				                view: view,
				                ddGroup: me.dropGroup || me.ddGroup
				            });
				        }
				    }
				});
				
				///////////////////
				// End Overrides //
				///////////////////
				
				var labelGrid = Ext.create('Ext.grid.Panel', {
					id: 'labelGrid',
					store: store_labels_local,
					flex: 1,
					border: false,
					autoScroll: true,
					columns: [
						{ 
							text: 'Labels',
							dataIndex: 'name',
							flex: 1,
							renderer: function (value, metaData, record) {
								
								// Get Component Count
								var componentCount = record.get('components').length;
								
								// Initialize Metadata Count
								var metadataCount = 0;
								
								// Loop Through Components
								for (i = 0; i < componentCount; i++) {
									
									// Increment Metadata Count
									metadataCount += record.get('components')[i].metadata.length;
								}
								
								// Ensure Count Is Valid
								if (!metadataCount) {
									
									// Initialize Count To Zero
									metadataCount = 0;
								}
								
								// Check Component Count
								if (metadataCount === 0) {
									
									// Build "New" Label Presentation
									var html = '<div style="color: #999; padding: 1em 0 2em 0;">';
									html += '<strong style="color: #111; float: left;">' + value.replace(/\*/, '<span class="text-danger">[NOT Saved]</span> ') + '</strong>';
									html += '<span style="float: right"><i class="fa fa-book fa-fw icon-small-vertical-correction"></i> ' + metadataCount + '</span>';
									html += "</div>";
									return html;
								}
								else {
								
									// Build Saved Label Presentation
									var html = '<div style="color: #999; padding: 1em 0 2em 0;">';
									html += '<strong style="color: #111; float: left;">' + value + '</strong>';
									html += '<span style="float: right"><i class="fa fa-book fa-fw icon-small-vertical-correction"></i> ' + metadataCount + '</span>';
									html += "</div>";
									return html;
								}
							}
						}
					],
					listeners: {
								
						selectionchange: {
						
							buffer: 100,
							fn: function(grid, records, eOpts) {

								// Ensure Record(s) Were Sent
								if (!records || records.length === 0) {

									// Exit Function
									return false;
								}

								// Store Selected Record Data
								var labelData = records[0].getData();
								
								// Change Panel Title
								Ext.getCmp('east-container').setTitle(Ext.getCmp('east-container').getTitle().replace(/ - .*/, '') + ' - ' + labelData.name);

								// Build Component Array
								var components = [];
								
								// Build Values Array
								var values = [];
								
								// Clear Stores
								store_labelComponents_local.removeAll();
								store_metadataValues_local.removeAll();

								// Check For Components Associated With Label
								if (labelData.components.length > 0) {

									// Loop Through Components
									for (i = 0; i < labelData.components.length; i++) {

										// Lookup Matching Components
										var matchedComponents = store_components_local.queryBy(function (record, id) {
											
											// Store Record Component
											var recordComponent = record.get('component');
											
											// See If Record Matches
											if (recordComponent.id === labelData.components[i].id) {
												
												// Return TRUE
												return true;
											}
											else {
												
												// Return FALSE
												return false;
											}
										});

										// Loop Through Matched Components
										for (j = 0; j < matchedComponents.items.length; j++) {

											// Store Component Data
											var componentData = matchedComponents.items[j].get('component');
											
											// Loop Through Metadata
											for (k = 0; k < labelData.components[i].metadata.length; k++) {

												// Build Component
												var component = {
													
													component: {

														id: componentData.id,
														name: componentData.name,
														type: {

															name: componentData.type.name,
															code: componentData.type.code
														},
														metadata: {

															id: labelData.components[i].metadata[k].id,
															value: labelData.components[i].metadata[k].value
														}
													}
												};
												
												// Add Component To Array
												components.push(component);
												
												// Create Function To Check Existing Values
												var checkValue = function (value) {
													
													return value.value === this.toString();
												};
												
												// See If Value Is Already In Values Array
												if (values.filter(checkValue, labelData.components[i].metadata[k].value).length === 0) {
													
													// Assign Metadata Value
													var value = {

														value: labelData.components[i].metadata[k].value
													};

													// Add Value To Values Array
													values.push(value);
												}
											}
										}
									}
									
									// Add Values To Values Store
									store_metadataValues_local.setData(values);
								}
								else {

									// Build Empty Component
									var component = {
										
										component: {

											id: "EMPTY",
											name: "No Associated Entries",
											type: ""
										}
									};

									// Add Component To Array
									components.push(component);
								}

								// Get Currently Associated Components
								var currentComponents = store_labelComponents_local.getData();

								// Loop Through Current Components
								for (i = 0; i < currentComponents.getCount(); i++) {

									// Store Component Data
									var currentComponent = currentComponents.items[i].data;

									// Ensure Current Component Is Not Empty Placeholder
									if (currentComponent.component.id !== "EMPTY") {

										// Add Component Back To Component Store
										store_components_local.addSorted(store_components_local.createModel(currentComponent));
									}
								}

								// Add Components To Component-Label Association Store
								store_labelComponents_local.setData(components);
								
								// Hide Entry Text
								Ext.getCmp('east-north-container').hide();
								
								// Display Grids
								Ext.getCmp('east-west-container').show();
								Ext.getCmp('east-east-container').show();
							}
						}
					},
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									width: '120px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										
										// Send Focus Temporarily Elsewhere
										Ext.getCmp('componentGrid_FilterField').focus();

										// Backup Any "New" Labels
										var newLabels = store_labels_local.query('isNew', true, false, true, true);

										// Backup Currently Selected Label
										var selectedLabels = labelGrid.getSelection();

										// Clear Local Store
										store_labels_local.removeAll();

										// Reload Data
										store_labels_remote.load(function(records, operation, success) {

											// Loop Through "New" Labels
											for (i = 0; i < newLabels.items.length; i++) {

												// Reinsert "New" Labels
												store_labels_local.addSorted(store_labels_local.createModel(newLabels.items[i].data));
											}

											// Loop Through Selected Labels
											for (i = 0; i < selectedLabels.length; i++) {

												// Store Label Model
												var labelModel = selectedLabels[i];

												// Query New Data Set
												var newLabelQueryResults = store_labels_local.query('name', labelModel.data.name, false, true, true);

												// Check For Results
												if (newLabelQueryResults.items.length > 0) {

													// Store New Label Model
													var newLabelModel = newLabelQueryResults.items[0];

													// Store Selection Model
													var selectionModel = labelGrid.getSelectionModel();

													// Deselect All
													selectionModel.deselectAll();

													// Select Label
													selectionModel.select([newLabelModel], false, false);

													// Focus On Label
													labelGrid.getView().focusRow(newLabelModel);
												}
											}
										});
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Add',
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction-edit',
									handler: function () {
										actionAddLabelForm();
									}
								}
							]
						},
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									xtype: 'textfield',
									flex: 1,
									fieldLabel: 'Filter',
									labelWidth: new Ext.util.TextMetrics().getWidth("Filter:"),
									listeners: {
										change: {
											
											buffer: 500,
											fn: function (field, newValue, oldValue, eOpts) {

												// Get Field's Store
												var store = Ext.getCmp("labelGrid").getStore();

												// Clear Previous Filter(s)
												store.clearFilter();

												// Set Filter
												store.filterBy(function(record) {

													// Return Whether Search String Was Found
													return record.get('name').search(new RegExp(newValue, 'i')) !== -1;
												});
											}
										}
									}
								}
							]
						}
					]
				});
				
				var componentGrid = Ext.create('Ext.grid.Panel', {
					id: 'componentGrid',
					store: store_components_local,
					flex: 1,
					border: false,
					autoScroll: true,
					viewConfig: {
						
						plugins: {
							
							ptype: 'gridviewdragdrop',
							dragGroup: 'labelAssociation-add-drag-drop-group',
							dropGroup: 'labelAssociation-remove-drag-drop-group',
							enableDrag: true,
							enableDrop: true,
							dragText: 'Add: {0}',
							dragTextField: 'component.name'
						},
						copy: true,
						listeners: {
							
							beforeDrop: function (node, data, overModel, dropPosition, dropHandlers, eOpts) {
							
								// Store Component Data
								var component = data.records[0];
								var componentData = component.getData();
								
								// Ensure Selected Label Has Components
								if (componentData.component.id === "EMPTY") {

									// Provide An Error Message
									Ext.toast("That is not a valid Entry. Please select a Label with Entries first.", '', 'tr');
									
									// Halt Drop Operation
									return false;
								}
							},

							drop: function (node, data, overModel, dropPosition, eOpts) {
								
								// Loop Over Dropped Records
								for (i = 0; i < data.records.length; i++) {

									// Store Component Data
									var component = data.records[i];
									var componentData = component.getData();

									// Store Selected Label
									var label = labelGrid.getSelection()[0];
									var labelData = label.getData();

									// Ensure Metadata Is Set
									if (componentData.component.metadata.id !== null) {

										// Make Label API Request
										Ext.Ajax.request({

											url: 'api/v1/resource/components/' + componentData.component.id + '/metadata/' + componentData.component.metadata.id,
											method: 'DELETE',
											success: function (response, opts) {

												// Define New Component Array
												var components = [];

												// Loop Through Existing Components
												for (j = 0; j < labelData.components.length; j++) {

													// Ensure Current Component Is Not The Component To Delete
													if (labelData.components[j].id !== componentData.component.id) {

														// Store Remaining Component
														components.push(labelData.components[j]);
													}
													else {

														// Initialize Temporary Metadata
														var metadata = [];

														// Loop Through Component Data
														for (k = 0; k < labelData.components[j].metadata.length; k++) {

															// Ensure Current Metadata Is Not The Metadata To Delete
															if (labelData.components[j].metadata[k].id !== componentData.component.metadata.id) {

																// Store Remaining Metadata
																metadata.push(labelData.components[j].metadata[k]);
															}
														}

														// Ensure There Is Metadata Remaining
														if (metadata.length > 0) {

															// Push Metadata Onto Component
															labelData.components[j].metadata = metadata;

															// Store Temporary Component
															components.push(labelData.components[j]);
														}
													}
												}

												// Check If There Are Remaining Components
												if (components.length > 0) {

													// Reassign Label Components
													labelData.components = components;

													// Update Component
													component.set(componentData);

													// Update Label Data
													label.set(labelData);

													// Reload Label Grid
													labelGrid.getView().refresh();
												}
												else {

													// Remove Entire Label
													store_labels_local.remove(label);
												}
											},
											failure: function (response, opts) {

												// Add Component Back To Label Associated Components Store
												store_labelComponents_local.addSorted(store_labelComponents_local.createModel(componentData));

												// Provide An Error Message
												Ext.toast("An error occurred removing the Metadata Association", '', 'tr');
											}
										});
									}
									else {

										// Check For "New" Label
										if (labelData.isNew) {

											// Check If Other Unsaved Components Are Still In The Store
											if (store_labelComponents_local.count() === 0) {

												// Build "Empty" Component
												var emptyComponent = {

													component: {

														id: "EMPTY",
														name: "No Associated Entries",
														type: ""
													}
												};

												// Add "Empty" Component To Store
												store_labelComponents_local.addSorted(store_labelComponents_local.createModel(emptyComponent));
											}
										}
									}

									// Remove Component From Component Store
									// (Results In Duplicates If Left In - Different ExtJS IDs)
									store_components_local.remove(component);

									// Search For Actual Component
									var matchedComponents = store_components_local.queryBy(function (record, id) {

										// Store Record Component
										var recordComponent = record.get('component');

										// See If Record Matches
										return recordComponent.id === componentData.component.id;
									});

									// Loop Over Matched Components
									// (Should Only Be One)
									for (i = 0; i < matchedComponents.items.length; i++) {

										// Store New Label Model
										var matchedComponent = matchedComponents.items[i];

										// Store Selection Model
										var selectionModel = componentGrid.getSelectionModel();

										// Deselect Any Previous Selection
										selectionModel.deselectAll();

										// Select Label
										selectionModel.select([matchedComponent], false, true);
										
										// Send Focus Temporarily Elsewhere
										Ext.getCmp('componentGrid_FilterField').focus();

										// Focus On Label
										componentGrid.getView().focusRow(matchedComponent);
									}
								}
							}
						}
					},
					listeners: {

						sortchange: function (grid, column, direction, eOpts) {

							// Check Descending
							switch (direction) {
								
								case 'DESC':

									// Sort Descending
									store_components_local.setSorters(component_sorter_descending);

									// Exit Switch
									break;

								case 'ASC':

									// Sort Ascending
									store_components_local.setSorters(component_sorter_ascending);

									// Exit Switch
									break;
							}
						}
					},
					columns: [
						{ 
							text: 'Entries',
							dataIndex: 'component',
							flex: 1,
							renderer: function (component, metaData, record) {
								
								var html = "<strong>" + component.name + "</strong>";
								html += '<div style="color: #999; margin: 1em 0; padding: 0 0 0.75em 0;">';
								html += '<i class="fa fa-book fa-fw icon-small-vertical-correction-book" style="float:left; margin-right: 2px;"></i> ';
								html += '<span style="float: left;">' + component.type.name + '</span>';
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
									text: 'Refresh',
									scale: 'medium',
									width: '120px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										
										// Reload Remote Component Store
										store_components_remote.load(function(records, operation, success) {
											
											// Check If A Label Is Selected
											if (labelGrid.getSelection().length > 0) {
												
												// Store All Currently Associated Components
												var associatedComponents = store_labelComponents_local.getData();
												
												// Loop Through Associated Components
												for (i = 0; i < associatedComponents.items.length; i++) {
													
													// Remove Associated Component From Local Component Store
													// (Still Exists In Associated Component Store)
													store_components_local.remove(associatedComponents.items[i]);
												}
											}
										});
									}
								}
							]
						},
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									xtype: 'tagfield',
									fieldLabel: 'Entry Types',
									labelWidth: new Ext.util.TextMetrics().getWidth("Entry Types:"),
									flex: 1,
									store: store_componentTypes_remote,
									valueField: 'code',
									displayField: 'description',
									emptyText: 'All',
									listeners: {
										change: function (tagfield, newValue, oldValue, eOpts) {
											
											// Get Current Filters On Store
											var filters = store_components_local.getFilters();
											
											// Loop Through Filters
											for (i = 0; i < filters.length; i++) {
												
												// Store Filter Function
												var filterFunction = filters.items[i].getFilterFn().toString();
												
												// Check If Current Filter Contains A String Which Itentifies This Filter
												if (filterFunction.search(/FILTER_BY_TYPE_CODE/) !== -1) {

													// Remove Previous Filter
													store_components_local.removeFilter(filters.items[i]);
												}
											}
											
											// Check If We Should Create A Filter
											if (newValue.length > 0) {
												
												// Create A Filter
												store_components_local.filterBy(filter = function multiFilter(record) {
													
													// Identify Filter
													var filterName = "FILTER_BY_TYPE_CODE";
													
													// Locate Matching Records
													return Ext.Array.contains(newValue, record.get('component').type.code);
												});
											}
										}
									}
								}
							]
						},
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									xtype: 'textfield',
									id: 'componentGrid_FilterField',
									flex: 1,
									fieldLabel: 'Filter',
									labelWidth: new Ext.util.TextMetrics().getWidth("Filter:"),
									listeners: {
										change: {
											
											buffer: 500,
											fn: function (field, newValue, oldValue, eOpts) {

												// Get Field's Store
												var store = Ext.getCmp("componentGrid").getStore();
												
												// Get Current Filters On Store
												var filters = store.getFilters();

												// Loop Through Filters
												for (i = 0; i < filters.length; i++) {

													// Check If Current Filter Contains A String Which Itentifies This Filter
													if (filters.items[i].getFilterFn().toString().search(/FILTER_BY_NAME/) !== -1) {

														// Remove Previous Filter
														store.removeFilter(filters.items[i]);
													}
												}

												// Set Filter
												store.filterBy(function(record) {
													
													// Identify Filter
													var filterName = "FILTER_BY_NAME";

													// Return Whether Search String Was Found
													return record.get('component').name.search(new RegExp(newValue, 'i')) !== -1;
												});
											}
										}
									}
								}
							]
						}
					]
				});
				
				var labelAssociationGrid = Ext.create('Ext.grid.Panel', {
					flex: 1,
					id: 'labelAssociationGrid',
					store: store_labelComponents_local,
					border: false,
					autoScroll: true,
					margin: '5 5 5 5',
					emptyText: 'Select a Label to see the currently associated Entries',
					viewConfig: {
						
						plugins: {
							
							ptype: 'gridviewdragdrop',
							dragGroup: 'labelAssociation-remove-drag-drop-group',
							dropGroup: 'labelAssociation-add-drag-drop-group',
							enableDrag: true,
							enableDrop: true,
							dragText: 'Delete: {0}',
							dragTextField: 'component.name'
						},
						listeners: {
							
							beforeDrop: function (node, data, overModel, dropPosition, dropHandlers, eOpts) {
								
								// Ensure A Label Is Selected
								if (labelGrid.getSelection().length === 0) {

									// Provide An Error Message
									Ext.toast("Please select a Label first", '', 'tr');
									
									// Halt Drop Operation
									return false;
								}
								
								// Loop Over Dropped Records
								for (i = 0; i < data.records.length; i++) {
									
									// Initialize New Component Data Object
									var componentData = {
										
										component: {
											
											id: data.records[i].data.component.id,
											name: data.records[i].data.component.name,
											type: {
												
												code: data.records[i].data.component.type.code,
												name: data.records[i].data.component.type.name
											}
										}
									};
									
									// Overwrite Dropped Component's Data
									data.records[i].set(componentData);
								}
							},
							
							drop: function (node, data, overModel, dropPosition, eOpts) {
								
								// Search For "Empty" Component
								var emptyComponents = store_labelComponents_local.queryBy(function (record, id) {

									// Store Record Component
									var recordComponent = record.get('component');

									// See If Record Matches
									return recordComponent.id === "EMPTY";
								});
								
								// Loop Through "Empty" Components
								for (i = 0; i < emptyComponents.items.length; i++) {
									
									// Remove "Empty" Component
									store_labelComponents_local.remove(emptyComponents.items[i]);
								}
								
								// Loop Over Dropped Records
								for (i = 0; i < data.records.length; i++) {
									
									// Search For Actual Component In Component Grid
									var matchedComponents = store_components_local.queryBy(function (record, id) {

										// Store Record Component
										var recordComponent = record.get('component');

										// See If Record Matches
										return recordComponent.id === data.records[i].getData().component.id;
									});

									// Loop Over Matched Components
									// (Should Only Be One)
									for (j = 0; j < matchedComponents.items.length; j++) {

										// Store New Label Model
										var componentModel = matchedComponents.items[j];

										// Store Selection Model
										var selectionModel = componentGrid.getSelectionModel();
										
										// Deselect Any Previous Selection
										selectionModel.deselectAll();

										// Select Label
										selectionModel.select([componentModel], false, true);
										
										// Send Focus Temporarily Elsewhere
										Ext.getCmp('componentGrid_FilterField').focus();

										// Focus On Label
										componentGrid.getView().focusRow(componentModel);
									}
									
									// Remove Newly Dragged-In Component
									store_labelComponents_local.remove(data.records[i]);
									
									// Copy Component
									var copiedComponent = data.records[i].copy(null);
									
									// Add A New Copy Of The Component
									store_labelComponents_local.addSorted(copiedComponent);
									
									// Store Selection Model
									var selectionModel = labelAssociationGrid.getSelectionModel();
									
									// Deselect Any Previous Selection
									selectionModel.deselectAll();

									// Select Label
									selectionModel.select([copiedComponent], false, true);
									
									// Send Focus Temporarily Elsewhere
									Ext.getCmp('componentGrid_FilterField').focus();

									// Focus On Label
									labelAssociationGrid.getView().focusRow(copiedComponent);
								}
							}
						}
					},
					listeners: {

						sortchange: function (grid, column, direction, eOpts) {

							// Check Descending
							switch (direction) {
								
								case 'DESC':

									// Sort Descending
									store_labelComponents_local.setSorters(component_sorter_descending);

									// Exit Switch
									break;

								case 'ASC':

									// Sort Ascending
									store_labelComponents_local.setSorters(component_sorter_ascending);

									// Exit Switch
									break;
							}
						}
					},
					columns: [
						{ 
							text: 'Entries',
							dataIndex: 'component',
							flex: 1,
//							renderer: Ext.ux.comboBoxRenderer(metadataValueCombobox)
							renderer: function (component, metaData, record) {
								
								// Check If Record Type Is Empty
								if (!component.type) {
									
									// Build Component Without Record Type
									var html = '<div style="color: #999; margin: 1em 0; padding: 0 0 0.75em 0;">';
									html += "<strong>" + component.name + "</strong>";
								}
								else {
									
									// Build Component With Record Type
									var html = "<strong>" + component.name + "</strong>";
									html += '<div style="color: #999; margin: 1em 0; padding: 0 0 0.75em 0;">';
									html += '<i class="fa fa-book fa-fw icon-small-vertical-correction-book" style="float:left; margin-right: 2px;"></i> ';
									html += '<span style="float: left;">' + component.type.name + '</span>';
								}
								
								// When Is A Component Not A Component
								if (component.id !== "EMPTY") {
								
									// Add Spot For ComboBox
									html += '<div style="float: left; margin: 1em 0;">';
									html += '<select id=select_' + component.name.replace(/ /g, "_") + '></select>';
									html += '</div>';
								}
								
								// Close HTML
								html += "</div>";
								
								// Return HTML For Grid
								return html;
							}
						}
					]
				});
				
				
				var labelsMainLayout = Ext.create('Ext.panel.Panel', {
					title: 'Metadata Management Tool <i class="fa fa-question-circle"  data-qtip="Quickly create and relate labels with entries."></i>',
					layout: 'border',
					height: '100%',
					items: [
						{
							title: 'Select A Label',
							region: 'west',
							xtype: 'panel',
							margin: '5 5 5 5',
							flex: 2,
							id: 'west-container',
							layout: {
								type: 'hbox',
								align: 'stretch'
							},
							items: [
								labelGrid
							]
						},
						{
							region: 'center',
							xtype: 'panel',
							margin: '0 10 0 0',
							minWidth: 10,
							maxWidth: 10,
							flex: 0,
							id: 'center-container',
							cls: 'x-panel-header-default',
							style: 'border: none;'
						},
						{
							title: 'Label/Entry Association',
							region: 'east',
							xtype: 'panel',
							margin: '5 5 5 5',
							flex: 6,
							id: 'east-container',
							layout: 'border',
							items: [
								{
									region: 'north',
									xtype: 'panel',
									html: '<div style="width: 100%; line-height: 3em; background-color: white; text-align: center; font-weight: bold;">Select A Label</div>',
									margin: '5 5 5 5',
									flex: 1,
									id: 'east-north-container'
								},
								{
									title: 'All Entries <i class="fa fa-question-circle"  data-qtip="Drag Entries from here to the \'Associated Entries\' column to associate the Entry with the selected Label."></i>',
									region: 'center',
									xtype: 'panel',
									margin: '5 5 5 0',
									flex: 2,
									id: 'east-west-container',
									hidden: true,
									layout: {
										type: 'hbox',
										align: 'stretch'
									},
									items: [

										componentGrid
									]
								},
								{
									title: 'Associated Entries <i class="fa fa-question-circle"  data-qtip="Drag Entries from here to the \'All Entries\' column to disassociate the Entry with the selected Label."></i>',
									region: 'east',
									xtype: 'panel',
									margin: '5 0 5 5',
									flex: 2,
									id: 'east-east-container',
									hidden: true,
									layout: {
										type: 'hbox',
										align: 'stretch'
									},
									items: [

										labelAssociationGrid
									]
								}
							]
						}
					]
				});

				var labelAddWin = Ext.create('Ext.window.Window', {
					id: 'labelAddWin',
					title: 'Add Label',
					modal: true,
					width: '30%',
					height: 185,
					y: '10em',
					iconCls: 'fa fa-lg fa-plus icon-small-vertical-correction',
					layout: 'fit',
					items: [
						{
							xtype: 'form',
							id: 'addLabelForm',
							layout: 'vbox',
							scrollable: true,
							bodyStyle: 'padding: 10px;',
							defaults: {
								labelAlign: 'top',
								labelSeparator: '',
								width: '100%'
							},
							items: [
								{
									xtype: 'textfield',
									fieldLabel: 'Label<span class="field-required"></span>',
									id: 'adddLabelForm-label',
									allowBlank: false,
									name: 'name',
									listeners: {
										
										change: {
											
											buffer: 100,
											fn: function(field, newValue, oldValue, eOpts) {
												
												// Lookup New Value Against Existing Labels
												var matchingLabels = store_labels_local.query('name', newValue, false, false, true);
												
												// Store Save Button
												var saveButton = Ext.getCmp('addLabelForm-saveButton');
												
												// Check For Matches
												if (matchingLabels.getCount() > 0) {
													
													// Indicate Labele Already Exists
													field.markInvalid("Label already exists");
													
													// Disable Save Button
													saveButton.disable();
												}
												else {
													
													// Check If Save Button Is Disabled
													if (saveButton.isDisabled()) {
													
														// Enable Save Button
														saveButton.enable();
													}
												}
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
											text: 'Create',
											id: 'addLabelForm-saveButton',
											iconCls: 'fa fa-lg fa-save icon-button-color-save',
											formBind: true,
											handler: function () {
												
												// Retrieve Form
												var form = Ext.getCmp('addLabelForm');
												
												// Ensure Data Is Valid
												// (Probably Not Necessary)
												if (form.isValid()) {
													
													// Store Form Data
													var formData = form.getValues();
													
													// Build Label
													var label = {
														
														isNew: true,
														name: "*" + formData.name,
														security: formData.securityType,
														components: []
													};
													
													// Create Label Model
													var labelModel = store_labels_local.createModel(label);
													
													// Add Label To Store
													store_labels_local.addSorted(labelModel);
													
													// Reset Form
													Ext.getCmp('addLabelForm').reset();
													
													// Close Add Window
													Ext.getCmp('labelAddWin').hide();
													
													// Select New Label
													labelGrid.getSelectionModel().select([labelModel]);
													labelGrid.getView().focusRow(labelModel);
												}
												else {
													
													form.markInvalid("Error with Label name");
												}
											}
										},
										{
											xtype: 'tbfill'
										},
										{
											text: 'Cancel',
											iconCls: 'fa fa-lg fa-close icon-button-color-warning',
											handler: function () {
												Ext.getCmp('addLabelForm').reset();
												Ext.getCmp('labelAddWin').hide();
											}
										}
									]
								}
							]
						}
					]
				});

				var actionAddLabelForm = function () {
					labelAddWin.show();
					Ext.getCmp('addLabelForm').reset(true);
				};

				var actionDeleteLabel = function (record) {
					if (record) {
						var labelId = record.data.labelId;
						var componentId = record.data.componentId;
						var method = 'DELETE';
						var url = 'api/v1/resource/components/';
						url += componentId + '/labels/' + labelId;

						Ext.Ajax.request({
							url: url,
							method: method,
							success: function (response, opts) {
								var message = 'Successfully deleted label "' + record.data.text + '"';
								Ext.toast(message, '', 'tr');
								Ext.getCmp('labelGrid').getStore().load();
								Ext.getCmp('labelGrid').getSelectionModel().deselectAll();
								Ext.getCmp('labelGrid-tools-delete').disable();
							},
							failure: function (response, opts) {
								Ext.MessageBox.alert('Failed to delete',
										'Error: Could not delete label "' + record.data.name + '"');
							}
						});

					} 
					else {
						Ext.MessageBox.alert('No Record Selected', 'Error: You have not selected a record.');
					}
				};

				addComponentToMainViewPort(labelsMainLayout);
				
			});

		</script>
	</stripes:layout-component>
</stripes:layout-render>