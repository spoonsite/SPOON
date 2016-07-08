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
						typePromptWindow.originId = originId;
						typePromptWindow.originName = originName;
						typePromptWindow.targetId = targetId;
						typePromptWindow.targetName = targetName;
						typePromptWindow.show();
					}

				};
				
			
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
							name: 'description',
							type: 'string'
						}
					],
					sorters: new Ext.util.Sorter({
						property: 'description',
						direction: 'ASC'
					}),
					proxy: {
						id: 'componentsStoreProxy',
						type: 'ajax',
						url: '../api/v1/resource/components/lookup'
					},
					autoLoad: true
				}); 
				
				var typePromptWindow = Ext.create('Ext.window.Window', {
					id: 'typePromptWindow',
					title: 'Choose Relationship Type',
					modal: true,
					width: '35%',
					y: '10em',
					layout: 'fit',
					items: [
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
						{ text: 'Origin Entry', dataIndex: 'description', flex: 1 }
					],
					listeners: {
						select: function(grid, record, index, eOpts) {
							var id = record.get('code');
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
									var originId = dragData.record.data.code;
									var originName = dragData.record.data.description; 
									var targetId = target.record.data.code;
									var targetName = target.record.data.description;
									var relationshipTypeCode = Ext.getCmp('relationshipTypeComboBox').getValue();
									actionCreateRelationship(originId, originName, targetId, targetName, relationshipTypeCode);
								}
							})
						]
					},
					columns: [
						{ text: 'Target Entry', dataIndex: 'description', flex: 1 }
					],
					listeners: {
						beforeselect: function(grid, record, index, eOpts) {
							originGrid.getView().select(record);
							grid.getSelectionModel().deselectAll();
						},
						select: function(grid, record, index, eOpts) {
							grid.getSelectionModel().deselectAll();
							originGrid.focus();
						}
					}
				});

				var relationshipsGrid = Ext.create('Ext.grid.Panel', {
					id: 'relationshipsGrid',
					region: 'center',
					store: relationshipsStore,
					viewConfig: {
						emptyText: 'Please select an entry to view its relationships',
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
								displayField: 'description',
								valueField: 'code'
							}
						},
						{ text: 'Target Entry', dataIndex: 'targetComponentName', flex: 5 }
					]
				});

				var visualizationPanel = Ext.create('Ext.panel.Panel', {
					height: '60%',
					region: 'south',
					title: 'Visualization',
					collapsible: true,
					resizable: true,
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
							title: 'Existing Relationships',
							margin: '5 5 5 5',
							borderWidth: '5px',
							region: 'center',
							xtype: 'panel',
							flex: 3,
							id: 'center-container',
							layout: 'border',
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
