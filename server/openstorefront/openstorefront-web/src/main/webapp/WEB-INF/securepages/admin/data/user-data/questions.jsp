
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../../client/layout/adminlayout.jsp">
	<stripes:layout-component name="contents">

		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function () {
				var componentStore = Ext.create('Ext.data.Store', {
					// This store only grabs components which have Questions.
					storeId: 'componentStore',
					autoLoad: true,
					sorters: 'componentName',
					fields: [
						'componentId',
						'componentName'
					],
					proxy: {
						id: 'componentStoreProxy',
						type: 'ajax',
						url: '../api/v1/resource/components/questionviews'
					},
					listeners: {
						load: function (theStore) {
							// Since the API returns multiple listings,
							// we must remove duplicate entries of components
							theStore.each(function (i) {
								theStore.each(function (j) {
									// check first if i and j exist
									// then if they are different entries
									// and then if they have the same componentId
									if (i && j && i.internalId !== j.internalId && i.data.componentId === j.data.componentId) {
										theStore.remove(j);
									}
								});
							});
						}
					}
				});

				var questionStore = Ext.create('Ext.data.Store', {
					// This store gets modified heavily when actionSelectedComponent() invokes.
					storeId: 'questionStore'
				});

				var componentPanel = Ext.create('Ext.grid.Panel', {
					flex: 2,
					store: componentStore,
					layout: 'fit',
					columns: [
						{
							text: 'Components',
							dataIndex: 'componentName',
							flex: 1
						}
					],
					listeners: {
						select: function(rowModel, record) {
							actionSelectedComponent(record.data.componentId);
						}
					}
				});

				var questionPanel = Ext.create('Ext.grid.Panel', {
					flex: 3,
					layout: 'fit',
					store: questionStore,
					viewConfig: {
						emptyText: 'Please select a component.',
						deferEmptyText: false
					},
					columns: [
						{
							text: 'Questions',
							dataIndex: 'question',
							flex: 1
						}
					]
				});


				var answerPanel = Ext.create('Ext.grid.Panel', {
					flex: 3,
					layout: 'fit',
					viewConfig: {
						emptyText: 'Please select a component.',
						deferEmptyText: false
					},
					columns: [
						{
							text: 'Answers',
							dataIndex: 'componentName',
							flex: 1
						}
					]
				});


				var mainPanel = Ext.create('Ext.panel.Panel', {
					title: 'Manage Questions <i class="fa fa-question-circle"  data-qtip="User questions and answers about a component."></i>',
					bodyPadding: '6em',
					layout: {
						type: 'hbox',
						align: 'stretch',
						pack: 'start',
						fit: 'fit'
					},
					defaults: {
					},
					items: [
						componentPanel,
						{
							xtype: 'splitter'
						},
						questionPanel,
						{
							xtype: 'splitter'
						},
						answerPanel
					]
				});
				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [
						mainPanel
					]
				});

				var actionSelectedComponent = function actionSelectedComponent(componentId) {
					// Set Proxy and Load Questions
					questionStore.setProxy({
						id: 'questionStoreProxy',
						url: '/openstorefront/api/v1/resource/components/' + componentId + '/questions',
						type: 'ajax'
					});
					questionStore.load();
				};


			});

		</script>
	</stripes:layout-component>
</stripes:layout-render>