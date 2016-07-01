<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
	<stripes:layout-component name="contents">
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function(){	


				var actualComponentsStore = Ext.create('Ext.data.Store', {
					storeId: 'actualComponentsStore',
					sorters: new Ext.util.Sorter({
						property: 'ownerComponentName',
						direction: 'ASC'
					})
				}); 

				var componentRelationshipStore = Ext.create('Ext.data.Store', {
					storeId: 'componentRelationshipStore',
					proxy: {
						id: 'componentRelationshipStoreProxy',
						type: 'ajax',
						url: '../api/v1/resource/componentrelationship'
					},
					listeners: {
						load: function(store, records, successful, eOpts) {
							// populate the actualComponentStore
							// with the a list of unique components
							var ac = Ext.getStore('actualComponentsStore');
							var acData = Ext.getStore('actualComponentsStore').getData();
							Ext.Array.each(records, function(record) {
								if (acData.find('ownerComponentId', record.get('ownerComponentId'))) {
									// do nothing
								} else {
									ac.add(record);
								}

							});
						}
					},
					autoLoad: true
				});

				var originGrid = Ext.create('Ext.grid.Panel', {
					store: actualComponentsStore,
					width: '50%',
					columns: [
						{ text: 'Origin Entry', dataIndex: 'ownerComponentName', flex: 1 }
					]
				});

				var targetGrid = Ext.create('Ext.grid.Panel', {
					store: actualComponentsStore,
					width: '50%',
					columns: [
						{ text: 'Target Entry', dataIndex: 'ownerComponentName', flex: 1 }
					]
				});

				var relationshipsGrid = Ext.create('Ext.grid.Panel', {
					region: 'center',
					viewConfig: {
						emptyText: 'Please select a component to view its relationships',
						deferEmptyText: false
					},
				});

				var visualizationPanel = Ext.create('Ext.panel.Panel', {
					height: '60%',
					region: 'south',
					title: 'Visualization',
					collapsible: true,
					resizable: true,
					html: 'Please select a component to view its relationships'
				});

				var relationshipCreationGridsPanel = Ext.create('Ext.panel.Panel', {
					width: '100%',
					layout: {
						type: 'hbox',
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
									width: '60%'
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
							flex: 2,
							id: 'west-container',
							autoScroll: true,
							layout: {
								type: 'hbox',
							},
							items: [
								relationshipCreationGridsPanel
							]
						},
						{
							title: 'Relationships',
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



			});
		</script>	
		
	</stripes:layout-component>
</stripes:layout-render>
