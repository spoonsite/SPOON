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
<stripes:layout-render name="../../../../../layout/toplevelLayout.jsp">
	<stripes:layout-component name="contents">

		<stripes:layout-render name="../../../../../layout/adminheader.jsp">		
		</stripes:layout-render>	
				
		<script type="text/javascript">
			
			//////////////////
			// Begin Stores //
			//////////////////
			
			/* global Ext, CoreUtil */
			Ext.onReady(function () {

				var store_tags_remote = Ext.create('Ext.data.Store', {
					storeId: 'tagStore',
					autoLoad: true,
					fields: [
						'componentName',
						'tagId',
						'text',
						'createUser',
						'createDts',
						'securityMarkingType'
					],
					sorters: 'text',
					proxy: {
						id: 'store_tags_remoteProxy',
						type: 'ajax',
						url: 'api/v1/resource/components/tagviews'
					},
					listeners: {
						
						load: function(store, operation, opts) { // Once Data Store Has Loaded
							
							// Initialize Local Tags Data Array
							var localTags = [];
							
							// Loop Through Remote Tags
							for (var i = 0; i < store.getCount(); i++) {
								
								// Store Current Tag Name
								var currentTagName = store.getAt(i).data.text;
								
								// Check If Current Tag Has Been Seen Before
								if (localTag == null || localTag.name == null) {
									
									// Indicate Current Loop Is First Iteration
									var firstLoop = true;
								}
								else {
									
									// Indicate Current Loop Is Not First Iteration
									var firstLoop = false;
								}
				
								// Check If Current Iteration Is First Or If Tag Name Has Changed
								if (firstLoop || currentTagName != localTag.name) {
									
									// Ensure Current Iteration Is Not First
									if (!firstLoop) {
										
										// Store Current Tag
										localTags.push(localTag);
									}
									
									// Initialize Current Tag
									var localTag = {};
									
									// Store Current Tag Name
									localTag.name = currentTagName;
									
									// Indicate Tag Is NOT New
									localTag.isNew = false;
									
									// Store Current Tag Security
									localTag.security = store.getAt(i).data.securityMarkingType
									
									// Initialize Component Array
									localTag.components = [];
								}
								
								// Build Component
								var currentComponent = {

									id: store.getAt(i).data.componentId,
									name: store.getAt(i).data.componentName,
									tag: {
										
										id: store.getAt(i).data.tagId
									}
								};

								// Store Component
								localTag.components.push(currentComponent);
							}
							
							// Set Local Tag Store Data
							store_tags_local.setData(localTags);
						}
					}
				});
				
				var store_tags_local = Ext.create('Ext.data.Store', {
					storeId: 'store_tags_local',
					autoLoad: true,
					fields: [
						'name',
						'security',
						'components',
						'isNew'
					],
					sorters: 'name'
				});
				
				var store_components_remote = Ext.create('Ext.data.Store', {
					storeId: 'store_components_remote',
					autoLoad: true,
					fields: [
						{name: 'name', mapping: function(data){
							return data.component.name;	
						}},
						{name: 'componentId', mapping: function(data){
							return data.component.componentId;	
						}},	
						{name: 'componentType', mapping: function(data){
							return data.component.componentType;	
						}},		
						{name: 'componentTypeDescription', mapping: function(data){
							return data.component.componentTypeLabel;	
						}}
					],
					sorters: new Ext.util.Sorter({
						property: 'name',
						direction: 'ASC'
					}),
					proxy: {
						id: 'store_components_remoteProxy',
						type: 'ajax',
						url: 'api/v1/resource/components/filterable',
						reader: {
							type: 'json',
							rootProperty: 'components'
						}
					},
					listeners: {
						
						load: function(store, operation, opts) { // Once Data Store Has Loaded
							
							// Initialize Local Components Data Array
							var localComponents = [];
							
							// Loop Through Remote Components
							for (var i = 0; i < store.getCount(); i++) {
									
								// Initialize Current Component
								var currentComponent = {
									
									// Store Current Component ID
									id: store.getAt(i).get('componentId'),

									// Store Current Component Name
									name: store.getAt(i).get('name'),

									// Store Current Component 
									type: {
										
										name: store.getAt(i).get('componentTypeDescription'),
										code: store.getAt(i).get('componentType')
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
				
				var store_components_local = Ext.create('Ext.data.Store', {
					storeId: 'store_components_local',
					autoLoad: true,
					fields: [
						'id',
						'name',
						'type',
						'components'
					],
					sorters: 'name'
				});
				
				var store_componentTypes_remote = Ext.create('Ext.data.Store', {
					storeId: 'store_componentTypes_remote',
					proxy: {
						type: 'ajax',
						url: 'api/v1/resource/componenttypes/lookup'
					},
					autoLoad: true
				});
				
				var store_tagComponents_local = Ext.create('Ext.data.Store', {
					storeId: 'store_tagComponents_local',
					autoLoad: true,
					fields: [
						'id',
						'name',
						'type',
						'components'
					],
					sorters: 'name'
				});

				var store_securityTypes_remote = Ext.create('Ext.data.Store', {
					storeId: 'store_securityTypes_remote',
					autoLoad: true,
					fields: [
						'code',
						'description'
					],
					proxy: {
						id: 'store_securityTypes_remoteProxy',
						type: 'ajax',
						url: 'api/v1/resource/lookuptypes/SecurityMarkingType/view'
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
				            var fieldValue = this.dragData.records[0].get(this.dragTextField);
				            return Ext.String.format(this.dragText, fieldValue);
				        } else {
				            var count = this.dragData.records.length;
				            return Ext.String.format(this.dragText, count, count == 1 ? '' : 's');
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
				
				var tagGrid = Ext.create('Ext.grid.Panel', {
					id: 'tagGrid',
					store: store_tags_local,
					flex: 1,
					border: false,
					autoScroll: true,
					columns: [
						{ 
							text: 'Tags',
							dataIndex: 'name',
							flex: 1,
							renderer: function (value, metaData, record) {
								
								// Get Component Count
								var componentCount = record.get('components').length;
								
								// Ensure Count Is Valid
								if (!componentCount) {
									
									// Initialize Count To Zero
									componentCount = 0;
								}
								
								// Check Component Count
								if (componentCount === 0) {
									
									// Build "New" Tag Presentation
									var html = '<div style="color: #999; padding: 1em 0 2em 0;">';
									html += '<strong style="color: #111; float: left;">' + value.replace(/\*/, '<span style="color: #22DD22;">[NEW]</span> ') + '</strong>';
									html += '<span style="float: right"><i class="fa fa-book fa-fw"></i> ' + componentCount + '</span>';
									html += "</div>";
									return html;
								}
								else {
								
									// Build Saved Tag Presentation
									var html = '<div style="color: #999; padding: 1em 0 2em 0;">';
									html += '<strong style="color: #111; float: left;">' + value + '</strong>';
									html += '<span style="float: right"><i class="fa fa-book fa-fw icon-small-vertical-correction-book"></i> ' + componentCount + '</span>';
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
								if (!records || records.length == 0) {

									// Exit Function
									return false;
								}

								// Store Selected Record Data
								var tagData = records[0].getData();

								// Build Component Array
								var components = [];

								// Check For Components Associated With Tag
								if (tagData.components.length > 0) {

									// Loop Through Components
									for (i = 0; i < tagData.components.length; i++) {

										// Lookup Matching Components
										var matchedComponents = store_components_local.query('id', tagData.components[i].id, false, true, true);

										// Loop Through Matched Components
										for (j = 0; j < matchedComponents.items.length; j++) {

											// Store Component Data
											var componentData = matchedComponents.items[j].data;

											// Build Component
											var component = {

												id: componentData.id,
												name: componentData.name,
												type: {
													
													name: componentData.type.name,
													code: componentData.type.code
												},
												tag: {

													id: tagData.components[i].tag.id
												}
											};

											// Add Component To Array
											components.push(component);

											// Remove Component From Component Grid
											store_components_local.remove(store_components_local.createModel(component));
										}
									}
								}
								else {

									// Build Empty Component
									var component = {

										id: "EMPTY",
										name: "No Associated Entries",
										type: ""
									};

									// Add Component To Array
									components.push(component);
								}

								// Get Currently Associated Components
								var currentComponents = store_tagComponents_local.getData();

								// Loop Through Current Components
								for (i = 0; i < currentComponents.getCount(); i++) {

									// Store Component Data
									var currentComponent = currentComponents.items[i].data;

									// Ensure Current Component Is Not Empty Placeholder
									if (currentComponent.id != "EMPTY") {

										// Add Component Back To Component Store
										store_components_local.addSorted(store_components_local.createModel(currentComponent));
									}
								}

								// Add Components To Component-Tag Association Store
								store_tagComponents_local.setData(components);
								
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
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										
										// Backup Any "New" Tags
										var newTags = store_tags_local.query('isNew', true, false, true, true);
										
										// Backup Currently Selected Tag
										var selectedTags = tagGrid.getSelection();
										
										// Reload Data
										store_tags_remote.load(function(records, operation, success) {
											
											// Loop Through "New" Tags
											for (i = 0; i < newTags.items.length; i++) {

												// Reinsert "New" Tags
												store_tags_local.addSorted(store_tags_local.createModel(newTags.items[i].data));
											}
											
											// Loop Through Selected Tags
											for (i = 0; i < selectedTags.length; i++) {
												
												// Refresh Grid
												tagGrid.getView().refresh();

												// Store Tag Model
												var tagModel = selectedTags[i];

												// Query New Data Set
												var newTagQueryResults = store_tags_local.query('name', tagModel.data.name, false, true, true);

												// Check For Results
												if (newTagQueryResults.items.length > 0) {

													// Store New Tag Model
													var newTagModel = newTagQueryResults.items[0];
													
													// Store Selection Model
													var selectionModel = tagGrid.getSelectionModel();

													// Select Tag
													selectionModel.select([newTagModel], false, true);
													
													// Send Focus Temporarily Elsewhere
													componentGrid.focus();
													
													// Focus On Tag
													tagGrid.getView().focusRow(newTagModel);
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
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction',
									handler: function () {
										actionAddTagForm();
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
												var store = Ext.getCmp("tagGrid").getStore();

												// Clear Previous Filter(s)
												store.clearFilter();

												// Set Filter
												store.filterBy(function(record) {

													// Return Whether Search String Was Found
													return record.get('name').search(new RegExp(newValue, 'i')) != -1;
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
					requiredPermissions: ['ADMIN-ENTRY-TAG-MANAGEMENT'],
					permissionCheckFailure: function () { // shouldn't be able to add tags to an entry
						this.setHidden(false);
						this.getView().getPlugins('gridviewdragdrop')[0].disable();
					},
					viewConfig: {
						
						plugins: {
							
							ptype: 'gridviewdragdrop',
							dragGroup: 'tagAssociation-add-drag-drop-group',
							dropGroup: 'tagAssociation-remove-drag-drop-group',
							enableDrag: true,
							enableDrop: true,
							dragText: 'Add: {0}',
							dragTextField: 'name'
						},
						listeners: {
							
							beforeDrop: function (node, data, overModel, dropPosition, dropHandlers, eOpts) {
							
								// Store Component Data
								var component = data.records[0].getData();
								
								// Ensure Selected Tag Has Components
								if (component.id == "EMPTY") {

									// Provide An Error Message
									Ext.toast("That is not a valid Entry. Please select a Tag with Entries first.", '', 'tr');
									
									// Halt Drop Operation
									return false;
								}
							},

							drop: function (node, data, overModel, dropPosition, eOpts) {

								// Store Component Data
								var component = data.records[0];
								var componentData = component.getData();
								
								// Store Selected Tag
								var tag = tagGrid.getSelection()[0];
								var tagData = tag.getData();
								
								// Make Tag API Request
								Ext.Ajax.request({
									
									url: 'api/v1/resource/components/' + componentData.id + '/tags/' + componentData.tag.id,
									method: 'DELETE',
									success: function (response, opts) {
										
										// Define New Component Array
										var components = [];
										
										// Check If There Will Be Remaining Components
										if ((tagData.components.length - 1) > 0) {

											// Loop Through Existing Components
											for (i = 0; i < tagData.components.length; i++) {

												// Ensure Current Component Is Not The Component To Delete
												if (tagData.components[i].id != componentData.id) {

													// Store Remaining Component
													components.push(tagData.components[i]);
												}
											}

											// Reassign Tag Components
											tagData.components = components;

											// Update Component
											component.set(componentData);

											// Update Tag Data
											tag.set(tagData);
											
											// Reload Tag Store
											tagGrid.getView().refresh();
										}
										else {
											
											// Remove Tag From Store
											store_tags_local.remove(tag);
										}
									},
									failure: function (response, opts) {
										
										console.log(response);
										
										// Remove Component From Components Store
										store_components_local.remove(component);
										
										// Add Component Back To Tag Associated Components Store
										store_tagComponents_local.addSorted(store_tagComponents_local.createModel(componentData));
										
										// Provide An Error Message
										Ext.toast("An error occurred removing the Tag Association", '', 'tr');
									}
								});
							}
						}
					},
					columns: [
						{ 
							text: 'Entries',
							dataIndex: 'name',
							flex: 1,
							renderer: function (value, metaData, record) {
								
								var html = "<strong>" + value + "</strong>";
								html += '<div style="color: #999; margin: 1em 0; padding: 0 0 0.75em 0;">';
								html += '<i class="fa fa-book fa-fw icon-small-vertical-correction-book" style="float:left; margin-right: 2px;"></i> ';
								html += '<span style="float: left;">' + record.get('type').name + '</span>';
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
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										
										// Reload Remote Component Store
										store_components_remote.load(function(records, operation, success) {
											
											// Check If A Tag Is Selected
											if (tagGrid.getSelection().length > 0) {
												
												// Store All Currently Associated Components
												var associatedComponents = store_tagComponents_local.getData();
												
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
												if (filterFunction.search(/FILTER_BY_TYPE_CODE/) != -1) {

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
													return Ext.Array.contains(newValue, record.get('type').code);
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
													if (filters.items[i].getFilterFn().toString().search(/FILTER_BY_NAME/) != -1) {

														// Remove Previous Filter
														store.removeFilter(filters.items[i]);
													}
												}

												// Set Filter
												store.filterBy(function(record) {
													
													// Identify Filter
													var filterName = "FILTER_BY_NAME";

													// Return Whether Search String Was Found
													return record.get('name').search(new RegExp(newValue, 'i')) != -1;
												});
											}
										}
									}
								}
							]
						}
					]
				});
				
				var tagAssociationGrid = Ext.create('Ext.grid.Panel', {
					flex: 1,
					id: 'tagAssociationGrid',
					store: store_tagComponents_local,
					border: false,
					autoScroll: true,
					margin: '5 5 5 5',
					emptyText: 'Select a Tag to see the current associated Entries',
					requiredPermissions: ['ADMIN-ENTRY-TAG-MANAGEMENT'],
					permissionCheckFailure: function () { // shouldn't be able to remove tags from an entry
						this.setHidden(false);
						this.getView().getPlugins('gridviewdragdrop')[0].disable();
					},
					viewConfig: {
						
						plugins: {
							
							ptype: 'gridviewdragdrop',
							dragGroup: 'tagAssociation-remove-drag-drop-group',
							dropGroup: 'tagAssociation-add-drag-drop-group',
							enableDrag: true,
							enableDrop: true,
							dragText: 'Delete: {0}',
							dragTextField: 'name'
						},
						listeners: {
							
							beforeDrop: function (node, data, overModel, dropPosition, dropHandlers, eOpts) {
								
								// Ensure A Tag Is Selected
								if (tagGrid.getSelection().length == 0) {

									// Provide An Error Message
									Ext.toast("Please select a Tag first", '', 'tr');
									
									// Halt Drop Operation
									return false;
								}
							},

							drop: function (node, data, overModel, dropPosition, eOpts) {

								// Store Component Data
								var component = data.records[0];
								var componentData = component.getData();
								
								// Store Selected Tag
								var tag = tagGrid.getSelection()[0];
								var tagData = tag.getData();
								
								// Check If Tag Is New
								if (tagData.isNew) {
									
									// Set Tag Data (For API Request)
									requestData = {

										text: tagData.name.replace(/\*/, ""),
										securityMarkingType: tagData.security
									};
									
									// Store "Empty" Component Model
									var emptyComponent = store_tagComponents_local.query('name', 'No Associated Entries', false, true, true).getAt(0);
									
									// Remove "Empty" Component
									store_tagComponents_local.remove(emptyComponent);
									
									// Reset "New" Status
									tagData.isNew = false;
								}
								else {
									
									// Set Tag Data (For API Request)
									requestData = {

										text: tagData.name,
										securityMarkingType: tagData.security
									};
								}
								
								

								Ext.Ajax.request({
									
									url: 'api/v1/resource/components/' + componentData.id + '/tags',
									method: 'POST',
									jsonData: requestData,
									success: function (response, opts) {
										
										// Convert Response To Object
										var responseObject = JSON.parse(response.responseText);
										
										// Update Tag Name
										tagData.name = responseObject.text;
										
										// Update Tag ID
										componentData.tag = {
											
											id: responseObject.tagId
										};
										
										// Add Component To Tag
										tagData.components.push(componentData);
										
										// Update Component Data
										component.set(componentData);
										
										// Update Tag Data
										tag.set(tagData);
										
										// Reload Tag Store
										tagGrid.getView().refresh();
									},
									failure: function (response, opts) {
										
										// Re-Enable "New" Status
										tagData.isNew = true;
										
										// Remove Component From Tag Associated Components Store
										store_tagComponents_local.remove(store_tagComponents_local.createModel(componentData));
										
										// Add Component Back To Components Store
										store_components_local.addSorted(store_components_local.createModel(componentData));
										
										// Check For "Empty" Component
										if (typeof emptyComponent !== 'undefined' && emptyComponent !== null) {
											
											// Re-Add "Empty" Component
											store_tagComponents_local.addSorted(emptyComponent);
										}
										
										// Provide An Error Message
										Ext.toast("An error occurred saving the Tag Association", '', 'tr');
									}
								});
							}
						}
					},
					columns: [
						{ 
							text: 'Entries',
							dataIndex: 'name',
							flex: 1,
							renderer: function (value, metaData, record) {
								
								// Store Record Type
								var recordType = record.get('type');
								
								// Check If Record Type Is Empty
								if (!recordType) {
									
									// Build Component Without Record Type
									var html = '<div style="color: #999; margin: 1em 0; padding: 0 0 0.75em 0;">';
									html += "<strong>" + value + "</strong>";
									html += "</div>";
									return html;
								}
								else {
									
									// Build Component With Record Type
									var html = "<strong>" + value + "</strong>";
									html += '<div style="color: #999; margin: 1em 0; padding: 0 0 0.75em 0;">';
									html += '<i class="fa fa-book fa-fw icon-small-vertical-correction-book" style="float:left; margin-right: 2px;"></i> ';
									html += '<span style="float: left;">' + record.get('type').name + '</span>';
									html += "</div>";
									return html;
								}
							}
						}
					]
				});
				
				var tagsMainLayout = Ext.create('Ext.panel.Panel', {
					title: 'Tag Management Tool <i class="fa fa-question-circle"  data-qtip="Quickly create and relate tags with entries."></i>',
					layout: 'border',
					height: '100%',
					items: [
						{
							title: 'Select A Tag',
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
								tagGrid
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
							title: 'Tag/Entry Association',
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
									html: '<div style="width: 100%; line-height: 3em; background-color: white; text-align: center; font-weight: bold;">Select A Tag</div>',
									margin: '5 5 5 5',
									flex: 1,
									id: 'east-north-container'
								},
								{
									title: 'Unassociated Entries <i class="fa fa-question-circle"  data-qtip="Drag Entries from here to the \'Associated Entries\' column to associate the Entry with the selected Tag."></i>',
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
									title: 'Associated Entries <i class="fa fa-question-circle"  data-qtip="Drag Entries from here to the \'Unssociated Entries\' column to disassociate the Entry from the selected Tag."></i>',
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

										tagAssociationGrid
									]
								}
							]
						}
					]
				});

				var tagAddWin = Ext.create('Ext.window.Window', {
					id: 'tagAddWin',
					title: 'Add Tag',
					modal: true,
					width: '30%',
					height: 185,
					y: '10em',
					iconCls: 'fa fa-lg fa-plus icon-small-vertical-correction',
					layout: 'fit',
					items: [
						{
							xtype: 'form',
							id: 'addTagForm',
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
									fieldLabel: 'Tag<span class="field-required"></span>',
									id: 'adddTagForm-tag',
									name: 'name',
									listeners: {
										
										change: {
											
											buffer: 100,
											fn: function(field, newValue, oldValue, eOpts) {
												
												// Lookup New Value Against Existing Tags
												var matchingTags = store_tags_local.query('name', new RegExp('^[\*]?' + newValue + '$', 'i'), false, false, true);
												
												// Store Save Button
												var saveButton = Ext.getCmp('addTagForm-saveButton');
												
												// Check For Matches
												if (matchingTags.getCount() > 0) {
													
													// Indicate Tage Already Exists
													field.markInvalid("Tag already exists");
													
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
								},
								{
									xtype: 'combobox',
									fieldLabel: 'Security Type',
									id: 'addTagForm-securityType',
									displayField: 'description',
									valueField: 'code',
									emptyText: 'Select a Security Type',
									name: 'securityType',
									hidden: !${branding.allowSecurityMarkingsFlg},
									store: store_securityTypes_remote
								}
							],
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										{
											text: 'Save',
											id: 'addTagForm-saveButton',
											iconCls: 'fa fa-lg fa-save icon-button-color-save',
											formBind: true,
											handler: function () {
												
												// Retrieve Form
												var form = Ext.getCmp('addTagForm');
												
												// Ensure Data Is Valid
												// (Probably Not Necessary)
												if (form.isValid()) {
													
													// Store Form Data
													var formData = form.getValues();
													
													// Build Tag
													var tag = {
														
														isNew: true,
														name: "*" + formData.name,
														security: formData.securityType,
														components: []
													};
													
													// Create Tag Model
													var tagModel = store_tags_local.createModel(tag);
													
													// Add Tag To Store
													store_tags_local.addSorted(tagModel);
													
													// Reset Form
													Ext.getCmp('addTagForm').reset();
													
													// Close Add Window
													Ext.getCmp('tagAddWin').hide();
													
													// Select New Tag
													tagGrid.getSelectionModel().select([tagModel]);
													tagGrid.getView().focusRow(tagModel);
												}
												else {
													
													form.markInvalid("Error with Tag name");
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
												Ext.getCmp('addTagForm').reset();
												Ext.getCmp('tagAddWin').hide();
											}
										}
									]
								}
							]
						}
					]
				});

				var actionAddTagForm = function () {
					tagAddWin.show();
					Ext.getCmp('addTagForm').reset(true);
				};

				var actionDeleteTag = function (record) {
					if (record) {
						var tagId = record.data.tagId;
						var componentId = record.data.componentId;
						var method = 'DELETE';
						var url = 'api/v1/resource/components/';
						url += componentId + '/tags/' + tagId;

						Ext.Ajax.request({
							url: url,
							method: method,
							success: function (response, opts) {
								var message = 'Successfully deleted tag "' + record.data.text + '"';
								Ext.toast(message, '', 'tr');
								Ext.getCmp('tagGrid').getStore().load();
								Ext.getCmp('tagGrid').getSelectionModel().deselectAll();
								Ext.getCmp('tagGrid-tools-delete').disable();
							},
							failure: function (response, opts) {
								Ext.MessageBox.alert('Failed to delete',
										'Error: Could not delete tag "' + record.data.name + '"');
							}
						});

					} 
					else {
						Ext.MessageBox.alert('No Record Selected', 'Error: You have not selected a record.');
					}
				};

				addComponentToMainViewPort(tagsMainLayout);
				
			});

		</script>
	</stripes:layout-component>
</stripes:layout-render>