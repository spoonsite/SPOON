
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../../layout/toplevelLayout.jsp">
	<stripes:layout-component name="contents">

		<stripes:layout-render name="../../../../../layout/adminheader.jsp">		
		</stripes:layout-render>
		
		<script src="scripts/plugin/cellToCellDragDrop.js?v=${appVersion}" type="text/javascript"></script>	
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function () {

				var remoteTagStore = Ext.create('Ext.data.Store', {
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
						id: 'tagStoreProxy',
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
									
									// Store Current Tag ID
									localTag.id = store.getAt(i).data.tagId;
									
									// Store Current Tag Name
									localTag.name = currentTagName;
									
									// Initialize Component Array
									localTag.components = [];
								}
								
								// Build Component
								var currentComponent = {

									id: store.getAt(i).data.componentId,
									name: store.getAt(i).data.componentName
								};

								// Store Component
								localTag.components.push(currentComponent);
							}
							
							// Set Local Tag Store Data
							localTagStore.setData(localTags);
						}
					}
				});


				var securityTypeStore = Ext.create('Ext.data.Store', {
					storeId: 'securityTypeStore',
					autoLoad: true,
					fields: [
						'code',
						'description'
					],
					proxy: {
						id: 'securityTypeStoreProxy',
						type: 'ajax',
						url: 'api/v1/resource/lookuptypes/SecurityMarkingType/view'
					}
				});
				
				var localTagStore = Ext.create('Ext.data.Store', {
					storeId: 'localTagStore',
					autoLoad: true,
					fields: [
						'id',
						'name',
						'count'
					],
					sorters: 'name'
				});
				
				var componentTypeStore = Ext.create('Ext.data.Store', {
					storeId: 'componentTypeStore',
					proxy: {
						type: 'ajax',
						url: 'api/v1/resource/componenttypes/lookup'
					},
					autoLoad: true
				});
				
				var componentStore = Ext.create('Ext.data.Store', {
					storeId: 'componentStore',
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
						id: 'componentStoreProxy',
						type: 'ajax',
						url: 'api/v1/resource/components/'
					},
					autoLoad: true
				});
				
				var localTagComponentStore = Ext.create('Ext.data.Store', {
					storeId: 'localTagComponentStore',
					autoLoad: true,
					fields: [
						'id',
						'name',
						'type'
					],
					sorters: 'name'
				});
				
				var tagGrid = Ext.create('Ext.grid.Panel', {
					id: 'tagGrid',
					store: localTagStore,
					flex: 1,
					border: false,
					autoScroll: true,
					columns: [
						{ 
							text: 'Tags',
							dataIndex: 'name',
							flex: 1,
							renderer: function (value, metaData, record) {
								var num = record.get('components').length;
								if (!num) num = 0;
								
								if (value == 'discovery') {
									console.log(record.data);
								}
								
								var html = '<div style="color: #999; padding: 1em 0 2em 0;">';
								html += "<strong style=\"color: #111; float: left;\">" + value + "</strong>";
								html += '<span style="float: right"><i class="fa fa-book fa-fw"></i> ' + num + '</span>';
								html += "</div>";
								return html;
							}
						}
					],
					listeners: {
								
						selectionChange: function(grid, records, eOpts) {

							// Store Selected Record Data
							var tagData = records[0].getData();
							
							// Build Component Array
							var components = [];

							// Loop Through Components
							for (i = 0; i < tagData.components.length; i++) {
							
								// Lookup Matching Components
								var matchedComponents = componentStore.query('componentId', tagData.components[i].id, false, true, true);
								
								// Loop Through Matched Components
								for (j = 0; j < matchedComponents.items.length; j++) {
									
									// Store Component Data
									var componentData = matchedComponents.items[j].data;
									
									// Build Component
									var component = {
										
										id: componentData.componentId,
										name: componentData.name,
										type: componentData.componentTypeDescription
									};
									
									// Add Component To Array
									components.push(component);
								}
							}

							// Add Components To Component-Tag Association Store
							localTagComponentStore.setData(components);
							console.log(components);
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
									iconCls: 'fa fa-2x fa-refresh',
									handler: function () {
										
										// TODO: Backup Any New Tags
										
										// Reload Data
										remoteTagStore.load();
										localTagStore.load();
										
										// TODO: Reinsert Backed Up Tags
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Add',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-plus',
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
											
											buffer: 750,
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
					store: componentStore,
					flex: 1,
					border: false,
					autoScroll: true,
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
							text: 'Entries',
							dataIndex: 'name',
							flex: 1,
							renderer: function (value, metaData, record) {
								//console.log(record.data.tags);
								var num = record.get('numRelationships');
								if (!num) num = 0;
								var html = "<strong>" + value + "</strong>";
								html += '<div style="color: #999; margin: 1em 0; padding: 0 0 0.75em 0;">';
								html += '<i class="fa fa-book fa-fw" style="float:left; margin-right: 2px;"></i> ';
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
									text: 'Refresh',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-refresh',
									handler: function () {
										Ext.getCmp('componentGrid').getStore().load();
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
									store: componentTypeStore,
									valueField: 'code',
									displayField: 'description',
									emptyText: 'All',
									listeners: {
										change: function (tagfield, newValue, oldValue, eOpts) {
											componentStore.clearFilter();
											componentStore.selectedValues = newValue;
											if (newValue.length > 0) {
												componentStore.filterBy(filter = function multiFilter(record) {
													return Ext.Array.contains(componentStore.selectedValues, record.get('componentType'));
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
											
											buffer: 750,
											fn: function (field, newValue, oldValue, eOpts) {

												// Get Field's Store
												var store = Ext.getCmp("componentGrid").getStore();

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
				
				var tagRelationshipsGrid = Ext.create('Ext.grid.Panel', {
					title: 'Tag Association',
					flex: 1,
					id: 'tagRelationshipsGrid',
					store: localTagComponentStore,
					border: false,
					autoScroll: true,
					margin: '5 5 5 5',
					emptyText: 'Select a Tag to see the currently associated Entries',
					columns: [
						{ 
							text: 'Tags',
							dataIndex: 'name',
							flex: 1,
							renderer: function (value, metaData, record) {
								
								var html = "<strong>" + value + "</strong>";
								html += '<div style="color: #999; margin: 1em 0; padding: 0 0 0.75em 0;">';
								html += '<i class="fa fa-book fa-fw" style="float:left; margin-right: 2px;"></i> ';
								html += '<span style="float: left;">' + record.get('type') + '</span>';
								html += "</div>";
								return html;
							}
						}
					]
				});
				
				var tagCloudPanel = Ext.create('Ext.grid.Panel', {
					title: 'Tag Cloud',
					flex: 1,
					id: 'tagCloudPanel',
					store: localTagStore,
					border: false,
					autoScroll: true,
					margin: '5 5 5 5',
					columns: [
						{ 
							text: 'Tags',
							dataIndex: 'name',
							flex: 1,
							renderer: function (value, metaData, record) {
								var num = record.get('components').length;
								if (!num) num = 0;
								
								var html = '<div style="color: #999; padding: 1em 0 2em 0;">';
								html += "<strong style=\"color: #111; float: left;\">" + value + "</strong>";
								html += '<span style="float: right"><i class="fa fa-book fa-fw"></i> ' + num + '</span>';
								html += "</div>";
								return html;
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
							title: 'Tags',
							region: 'west',
							xtype: 'panel',
							margin: '5 5 5 5',
							flex: 2,
							id: 'west-container',
							layout: {
								type: 'vbox',
								align: 'stretch'
							},
							items: [
								tagGrid
							]
						},
						{
							title: 'Entries',
							region: 'center',
							xtype: 'panel',
							margin: '5 5 5 5',
							flex: 3,
							id: 'center-container',
							layout: {
								type: 'vbox',
								align: 'stretch'
							},
							items: [
								
								componentGrid
							]
						},
						{
							region: 'east',
							xtype: 'panel',
							flex: 3,
							id: 'east-container',
							layout: {
								type: 'vbox',
								align: 'stretch'
							},
							items: [
								
								tagRelationshipsGrid,
								tagCloudPanel
							]
						}
					]
				});

				var tagAddWin = Ext.create('Ext.window.Window', {
					id: 'tagAddWin',
					title: 'Add Tag',
					modal: true,
					width: '30%',
					y: '10em',
					iconCls: 'fa fa-lg fa-plus',
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
									xtype: 'combobox',
									fieldLabel: 'Component<span class="field-required"></span>',
									id: 'addTagForm-component',
									forceSelection: true,
									displayField: 'description',
									valueField: 'code',
									emptyText: 'Select a Component',
									typeAhead: true,
									minChars: 3,
									allowBlank: false,
									name: 'component',
									store: Ext.create('Ext.data.Store', {
										storeId: 'componentStore',
										autoLoad: true,
										sorters: "description",
										fields: [
											'code',
											'description'
										],
										proxy: {
											id: 'componentStoreProxy',
											type: 'ajax',
											url: 'api/v1/resource/components/lookup'
										}
									})
								},								
								Ext.create('OSF.component.StandardComboBox', {
									name: 'text',	
									id: 'adddTagForm-tag',										
									margin: '0 0 0 0',
									width: '100%',
									fieldLabel: 'Tag<span class="field-required"></span>',
									forceSelection: false,
									allowBlank: false,
									valueField: 'text',
									displayField: 'text',
									margin: '0 0 10 0',
									maxLength: 120,
									storeConfig: {
										url: 'api/v1/resource/components/tags'
									}
								}),
								{
									xtype: 'combobox',
									fieldLabel: 'Security Type',
									id: 'addTagForm-securityType',
									displayField: 'description',
									valueField: 'code',
									emptyText: 'Select a Security Type',
									name: 'securityType',
									hidden: !${branding.allowSecurityMarkingsFlg},
									store: Ext.data.StoreManager.lookup('securityTypeStore')
								}
							],
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										{
											text: 'Save',
											iconCls: 'fa fa-save',
											formBind: true,
											handler: function () {
												var form = Ext.getCmp('addTagForm');
												// Submit Data
												if (form.isValid()) {
													var formData = form.getValues();
													var url = 'api/v1/resource/components/';
													url += formData.component + "/tags";
													var method = 'POST';

													// Compile properly formatted data
													data = {};
													data.text = formData.text;
													data.securityMarkingType = formData.securityType;

													CoreUtil.submitForm({
														url: url,
														method: method,
														data: data,
														removeBlankDataItems: true,
														form: Ext.getCmp('addTagForm'),
														success: function (response, opts) {
															// Server responded OK
															var errorResponse = Ext.decode(response.responseText);

															// Confusingly, you will only see the "success"
															// property in the response if the success
															// is success = false. Therefore
															// the appearance of the success property actually
															// means there was a failure.

															if (!errorResponse.hasOwnProperty('success')) {
																// Validation succeeded.
																Ext.toast('Saved Successfully', '', 'tr');
																Ext.getCmp('addTagForm').setLoading(false);
																Ext.getCmp('addTagForm').reset();
																Ext.getCmp('tagAddWin').hide();
																Ext.getCmp('tagGrid').getStore().load();
																Ext.getCmp('tagGrid').getSelectionModel().deselectAll();
																Ext.getCmp('tagGrid-tools-delete').setDisabled(true);
															} else {
																// Validation failed

																// Compile an object to pass to ExtJS Form
																// that allows validation messages
																// using the markInvalid() method.
																var errorObj = {};
																Ext.Array.each(errorResponse.errors.entry, function (item, index, entry) {
																	errorObj[item.key] = item.value;
																});
																var form = Ext.getCmp('addTagForm').getForm();
																form.markInvalid(errorObj);
															}
														},
														failure: function (response, opts) {
															// The same failure procedure as seen above
															var errorResponse = Ext.decode(response.responseText);
															var errorObj = {};
															Ext.Array.each(errorResponse.errors.entry, function (item, index, entry) {
																errorObj[item.key] = item.value;
															});
															var form = Ext.getCmp('addTagForm').getForm();
															form.markInvalid(errorObj);
														}
													});
												}
											}
										},
										{
											xtype: 'tbfill'
										},
										{
											text: 'Cancel',
											iconCls: 'fa fa-close',
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