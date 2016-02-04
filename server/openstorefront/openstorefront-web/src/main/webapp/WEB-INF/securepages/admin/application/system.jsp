<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
    <stripes:layout-component name="contents">

		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function () {
				
				var statusStats = Ext.create('Ext.grid.Panel', {
					title: 'Statistics',
					id: 'statusStats'
				});
				
				var threadStatus = Ext.create('Ext.grid.Panel', {
					title: 'Threads Status',
					id: 'threadStatus'
				});

				var systemProperties = Ext.create('Ext.grid.Panel', {
					title: 'System Properties',
					id: 'systemProperties'
				});

				var statusPanel = Ext.create('Ext.tab.Panel', {
					title: 'Status',
					id: 'statusPanel',
					tabPosition: 'left',
					tabRotation: 0,
					tabBar: {
						border: false
					},
					items: [statusStats, threadStatus, systemProperties]
				});

				var errorTicketsGrid = Ext.create('Ext.grid.Panel', {
					title: 'Error Tickets',
					id: 'errorTicketsGrid'
				});

				var appStatePropGrid = Ext.create('Ext.grid.Panel', {
					title: 'Application State Properties',
					id: 'appStatePropGrid'
				});	

				var appInitPropGrid = Ext.create('Ext.grid.Panel', {
					title: 'Application Initilization Properties',
					id: 'appInitPropGrid'
				});

				var logGrid = Ext.create('Ext.grid.Panel', {
					title: 'Logs and Logging',
					id: 'logGrid'
				});	

				var pluginGrid = Ext.create('Ext.grid.Panel', {
					title: 'Plugins',
					id: 'pluginGrid'
				});

				var searchControlPanel = Ext.create('Ext.grid.Panel', {
					title: 'Search Control',
					id: 'searchControlPanel'
				});

				var recentChangesPanel = Ext.create('Ext.grid.Panel', {
					title: 'Recent Changes E-mail',
					id: 'recentChangesPanel'
				});



				var systemMainPanel = Ext.create('Ext.tab.Panel', {
					title: 'System Management <i class="fa fa-question-circle"  data-qtip="View the system status and manage system properties"></i>',
					width: 400,
					height: 400,
					items: [
						statusPanel,
						errorTicketsGrid,
						appStatePropGrid,
						appInitPropGrid,
						logGrid,
						pluginGrid,
						searchControlPanel,
						recentChangesPanel
					]
				});

				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [systemMainPanel]
				});


			});

		</script>
    </stripes:layout-component>
</stripes:layout-render>
