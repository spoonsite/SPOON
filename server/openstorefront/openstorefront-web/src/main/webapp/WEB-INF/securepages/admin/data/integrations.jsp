<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
    <stripes:layout-component name="contents">
	
	<script src="scripts/component/importWindow.js?v=${appVersion}" type="text/javascript"></script>	
		
	<script type="text/javascript">
		/* global Ext, CoreUtil */
		Ext.onReady(function(){	


			var componentConfigStore = Ext.create('Ext.data.Store', {
				id: 'componentConfigStore',
				autoLoad: true,
				proxy: {
					type: 'ajax',
					url: '/openstorefront/api/v1/resource/components/integration?status=ALL'
				}
			});

			var componentConfigGrid = Ext.create('Ext.grid.Panel', {
				title: 'Component Configuration',
				id: 'componentConfigGrid',
				store: componentConfigStore,
				columnLines: true,
				columns: [
					{ text: 'Component', dataIndex: 'componentName', flex: 2},
					{ 
						text: 'Start Time', 
						dataIndex: 'lastStartTime', 
						xtype: 'datecolumn',
						format: 'm/d/y H:i:s A',
						flex: 1
					},
					{ 
						text: 'End Time', 
						dataIndex: 'lastEndTime',
						xtype: 'datecolumn',
						format: 'm/d/y H:i:s A',
						flex: 1
					},
					{ 
						text: 'Status', 
						dataIndex: 'status', 
						flex: 1,
						renderer: function(value, metadata) {
							if (value === 'C') return 'Complete';
							else if (value === 'E') return 'Error';
							else if (value === 'P') return 'Pending';
							else if (value === 'W') return 'Working';
						}
					},
					{ 
						text: 'Active Status', 
						dataIndex: 'activeStatus', 
						flex: 0.5,
						renderer: function(value, metadata) {
							if (value === 'A') {
								metadata.tdCls = 'alert-success';
								return "Active";
							}
							if (value === 'I') {
								metadata.tdCls = 'alert-warning';
								return "Inactive";
							}
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
									componentConfigStore.load();
								}
							},
							{
								xtype: 'tbseparator'
							},
							{
								text: 'Add New Configuration',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-plus',
								handler: function () {
									actionAddNewConfiguration();
								}
							},
							{
								xtype: 'tbseparator'
							},
							{
								text: 'Run Job',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-bolt',
								disabled: true,
								handler: function () {
									actionRunJob(record);
								}
							},
							{
								text: 'Edit',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-edit',
								disabled: true,
								handler: function () {
									actionEditIntegration(record);
								}
							},
							{
								text: 'Deactivate',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-power-off',
								disabled: true,
								handler: function () {
									actionToggleIntegration(record);
								}
							},
							{
								text: 'Delete',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-trash',
								disabled: true,
								handler: function () {
									actionDeleteIntegration(record);
								}
							},

						]
					}
				]
			});

			var actionAddNewConfiguration = function actionAddNewConfiguration() {

			};

			var actionRunJob = function actionRunJob(record) {

			};

			var actionEditIntegration = function actionEditIntegration(record) {

			};

			var actionToggleIntegration = function actionToggleIntegration(record) {

			};

			var actionDeleteIntegration = function actionDeleteIntegration(record) {

			};

			var jiraConfigGrid = Ext.create('Ext.grid.Panel', {
				title: 'Jira Configuration'
			});


			var mainPanel = Ext.create('Ext.tab.Panel', {
				title: 'Manage Integration <i class="fa fa-question-circle"  data-qtip="Allows for the configuration of data integration with external systems such as JIRA"></i>',
				items: [
					componentConfigGrid,
					jiraConfigGrid
				]
			});


			Ext.create('Ext.container.Viewport', {
				layout: 'fit',
				items: [
					mainPanel
				]
			});
			


		});		
	</script>	
		
	</stripes:layout-component>
</stripes:layout-render>	
