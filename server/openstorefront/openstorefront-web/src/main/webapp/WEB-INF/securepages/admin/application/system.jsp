<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
    <stripes:layout-component name="contents">
<style>
			.list-group-item:first-child {
				border-top-right-radius: 4px;
				border-top-left-radius: 4px;
			}
			.list-group-item {
				position: relative;
				display: block;
				padding: 10px 15px;
				margin-bottom: -1px;
				background-color: #8A8A8A;
				border: 1px solid #464545;
				font-size: 14px;
			}
			.list-group {
				/* margin-bottom: 20px; */
				padding-left: 0;
				color: white;
			}
			ul, ol {
				margin-top: 0;
				margin-bottom: 10.5px;
			}
			.badge {
				display: inline-block;
				min-width: 10px;
				padding: 3px 7px;
				font-size: 13px;
				font-weight: bold;
				color: #ffffff;
				line-height: 1;
				vertical-align: middle;
				white-space: nowrap;
				text-align: center;
				float: right;
				background-color: rgba(68,30,90,1);
				border-radius: 10px;
			}
			.green-highlight{
				color: lightgreen
			}
		</style>	

		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function () {

				var tplSystemDetailStats = new Ext.XTemplate(
					'<div>',
					'<div style="padding:10px; float: left;" ><ul class="list-group">',	
					'<li class="list-group-item">Application Version: <span class="badge">{applicationVersion}</span></li>',
					'<li class="list-group-item">Up Time: <span class="badge">{upTime}</span></li>',
					'<li class="list-group-item">Start Time: <span class="badge">{startTime}</span></li>',
					'<li class="list-group-item">System Load: <span class="badge">{systemLoad}</span></li>',			
					'</ul><div>',
					'<div style="padding:10px;" ><ul class="list-group">',	
					'<li class="list-group-item">Application Version: <span class="badge">{applicationVersion}</span></li>',
					'<li class="list-group-item">Up Time: <span class="badge">{upTime}</span></li>',
					'<li class="list-group-item">Start Time: <span class="badge">{startTime}</span></li>',
					'<li class="list-group-item">System Load: <span class="badge">{systemLoad}</span></li>',			

					'</div>'
				);	

				var statusStats = Ext.create('Ext.panel.Panel', {
					title: 'Statistics',
					id: 'statusStats',
					tpl: tplSystemDetailStats,
					data: {
						'applicationVersion': '1.1.1.1.1.'
					}
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
