<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
	<stripes:layout-component name="contents">

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
						var url = '../api/v1/resource/components/' + originId + '/relationships';
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
				
				var componentRelationshipsListingStore = Ext.create('Ext.data.Store', {
					storeId: 'componentRelationshipsListingStore',
					proxy: {
						type: 'ajax',
						url: '../api/v1/resource/componentrelationship'
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
						url: '../api/v1/resource/lookuptypes/RelationshipType/view'
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
						url: '../api/v1/resource/components/'
					},
					autoLoad: true,
					listeners: {
						load: function () {
							componentRelationshipsListingStore.load();
						}
					}
				}); 
				
				var typePromptWindow = Ext.create('Ext.window.Window', {
					id: 'typePromptWindow',
					title: 'Choose Relationship Type',
					modal: true,
					width: '35%',
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
							store: relationshipTypeStore,
						}
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [
								{
									text: 'Cancel',
									handler: function() {
										this.up('window').hide();
									}
								},
								{
									xtype: 'tbfill',
								},
								{
									text: 'Create Relationship',
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
										var url = '../api/v1/resource/components/' + originId + '/relationships';
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

												// If this is from the 'Create Inverse' button, we need to disable the toolbar items.
												if (typePromptWindow.openSource === 'inverse') {
													Ext.getCmp('relationshipGridAction-CreateInverse').disable();
													Ext.getCmp('relationshipGridAction-Delete').disable();
												}
											},
											failure: function (response, opts) {
												Ext.MessageBox.alert('Failed to create relationship for "' + originName + '"');
												typePromptWindow.hide();
											}
										});
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
							enableDrop: false,
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
								html += '<p style="color: #999; margin-bottom: 0.5em;">';
								html += '<i class="fa fa-book fa-fw"></i> '
								html += '<span style="padding-right: 20px">' + record.get('componentTypeDescription') + '</span>';
								html += '<span style="float: right"><i class="fa fa-share-alt"></i> ' + num + '</span>';
								html += "</p>";
								return html;
							},
						}
					],
					listeners: {
						select: function(grid, record, index, eOpts) {
							var id = record.get('componentId');
							relationshipsStore.setProxy({
								type: 'ajax',
								url: '../api/v1/resource/components/' + id + '/relationships'
							});
							relationshipsStore.load();
						}
					}
				});

				var targetGrid = Ext.create('Ext.grid.Panel', {
					id: 'targetGrid',
					store: componentsStore,
					flex: 1,
					border: true,
					selectable: false,
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
								html += '<p style="color: #999; margin-bottom: 0.5em;">';
								html += '<i class="fa fa-book fa-fw"></i> '
								html += '<span style="padding-right: 20px">' + record.get('componentTypeDescription') + '</span>';
								html += "</p>";
								return html;
							},

						}
					],
					listeners: {
						beforeselect: function(grid, record, index, eOpts) {
							originGrid.getView().select(record);
						},
						select: function(grid, record, index, eOpts) {
							return false;
						}
					}
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
						emptyText: 'You have not selected an entry or the entry you selected has no existing relationships.',
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
										var url = '../api/v1/resource/components/' + originId + '/relationships/';
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
								Ext.getCmp('relationshipGridAction-CreateInverse').enable();
								Ext.getCmp('relationshipGridAction-Delete').enable();
							} else {
								Ext.getCmp('relationshipGridAction-CreateInverse').disable();
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
									iconCls: 'fa fa-refresh',
									handler: function () {
										relationshipsStore.load()
									}
								},
								{
									text: 'Delete',
									id: 'relationshipGridAction-Delete',
									iconCls: 'fa fa-trash',
									disabled: true,
									handler: function() {
										var record = Ext.getCmp('relationshipsGrid').getSelection()[0];
										var title = 'Delete Relationship';
										var msg = 'Are you sure you want to delete this relationship?';
										Ext.MessageBox.confirm(title, msg, function (btn) {
											if (btn === 'yes') {
												var url = '/openstorefront/api/v1/resource/components/'
												url += record.get('ownerComponentId') + "/relationships/";
												url += record.get('relationshipId');
												var method = "DELETE";
												Ext.Ajax.request({
													url: url,
													method: method,
													success: function (response, opts) {
														Ext.toast('Successfully deleted relationship', '', 'tr');
														relationshipsStore.load();
														Ext.getCmp('relationshipGridAction-CreateInverse').disable();
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
								{
									text: 'Create Inverse',
									iconCls: 'fa fa-exchange',
									id: 'relationshipGridAction-CreateInverse',
									disabled: true,
									handler: function() {
										var record = Ext.getCmp('relationshipsGrid').getSelection()[0];
										// Set up reverse direction for prompt window.
										typePromptWindow.targetId = record.get('ownerComponentId');
										typePromptWindow.targetName = record.get('ownerComponentName');
										typePromptWindow.originId = record.get('targetComponentId');
										typePromptWindow.originName = record.get('targetComponentName');
										typePromptWindow.openSource = 'inverse';

										// Set up html for prompt
										var html = '<strong>Origin Entry:</strong> ';
										html += typePromptWindow.originName;
										html += '<br />';
										html += '<strong>Target Entry:</strong> ';
										html += typePromptWindow.targetName;
										Ext.getCmp('relationshipWindowSelectorDesc').update(html);

										// Show prompt
										typePromptWindow.show();

									}
								}
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
					title: 'Relationship Management Tool',
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
								align: 'stretch',
							},
							items: [
								relationshipsGrid, visualizationPanel
							]
						}
					]
				});

				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [
						relationshipsMainLayout
					]
				});

				Ext.defer(function(){
					visualPanel.setWidth(visualizationPanel.getWidth());
					visualPanel.setHeight(visualizationPanel.getHeight()-40);
				}, 400);	




			});
		</script>	
		
	</stripes:layout-component>
</stripes:layout-render>
