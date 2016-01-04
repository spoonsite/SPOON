<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../client/layout/adminlayout.jsp">
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
		
		/* global Ext */
		Ext.onReady(function(){	
			
			//var approveStatusTemplate
			
			var componentTopStore = Ext.create('Ext.data.Store', {
			    fields: ['componentId', 'componentName', 'views']
			});
			
			var tplComponentStats = new Ext.XTemplate(
				'<div style="padding: 10px;" ><ul class="list-group">',	
				'<li class="list-group-item">Approved: <span class="badge">{numberOfApprovedEntries}</span></li>',
				'<li class="list-group-item">Pending: <span class="badge">{numberOfPendingEntries}</span></li>',
				'<li class="list-group-item">Not Submitted: <span class="badge">{numberOfNotSubmited}</span></li>',
				'<li class="list-group-item">Categories (Attributes): <span class="badge">{numberOfAttributesTypes}</span></li>',
				'<li class="list-group-item">Specific Categories (Attributes): <span class="badge">{numberOfAttributesCodes}</span></li>',
				'</ul><div>'
			);
			var tplComponentStatsRecentViews = new Ext.XTemplate(
				'<ol style="padding-top: 10px;"><tpl for="recentlyViewed">', 
				'<li> <b>{componentName}</b> <span style="font-size: 9px; color: grey; ">({viewDts:date("m/d/Y H:i:s")})</span>',
				'',
				'</li>',				
				'</tpl></ol>'
			);
			
			var componentStats = Ext.create('Ext.panel.Panel', {
				title: 'Entry Statistics',
				id: 'componentStats',
				iconCls: 'fa fa-lg fa-database',				
				margin: "0 20 0 0",
				border: 1,
				frame: true,
				style: 'box-shadow: 7px 7px 7px #888888;',
				float: true,
				flex: 1,
							
				tools: [
					{
						type: 'refresh',
						handler: function(event, toolEl, panelHeader) {
							actionLoadComponentData();
						}
					}
				],
				items: [					
					{
						xtype: 'panel',
						layout: {
							type: 'hbox',
							align: 'stretch'
						},
						items: [
							{
								xtype: 'panel',
								id: 'componentStatsPanel',
								title: 'Active Entries',								
								autoScroll: true,
								border: true,
								tpl: tplComponentStats,								
								bodyStyle: 'background-color: white',
								width: '50%',
								margin: '0 0 0 0'
							},
							{
								xtype: 'panel',
								id: 'componentStatsViewedPanel',
								title: 'Recently Viewed',								
								autoScroll: true,
								border: true,								
								bodyStyle: 'background-color: white',
								width: '50%',
								tpl: tplComponentStatsRecentViews
							}
						]
					}, 
					{
						xtype: 'panel',
						layout: 'fit',
						height: 300,
						items: [
							{
								xtype: 'cartesian',
								reference: 'chart',
								theme: {
									type: 'muted'
								},
								store: componentTopStore,
								insetPadding: '40 40 40 20',
								animation: Ext.isIE8 ? false : {
									easing: 'backOut',
									duration: 500
								},
								axes: [{
									type: 'numeric3d',
									position: 'left',
									fields: 'views',							
									majorTickSteps: 10,
									label: {
										textAlign: 'right'
									},
									renderer: function (axis, label, layoutContext) {
										// Custom renderer overrides the native axis label renderer.
										// Since we don't want to do anything fancy with the value
										// ourselves except adding a thousands separator, but at the same time
										// don't want to loose the formatting done by the native renderer,
										// we let the native renderer process the value first.
										return Ext.util.Format.number(layoutContext.renderer(label), '0,000');
									},
									title: 'Views',
									grid: {
										odd: {
											fillStyle: 'rgba(255, 255, 255, 0.06)'
										},
										even: {
											fillStyle: 'rgba(0, 0, 0, 0.03)'
										}
									}
								}, {
									type: 'category3d',
									position: 'bottom',
									fields: 'componentName',
									renderer: function (axis, label, layoutContext) {
										return Ext.util.Format.ellipsis(label, 18);
									},
									grid: true
								}],
								series: [{
									type: 'bar3d',
									xField: 'componentName',
									yField: 'views',
									style: {
										minGapWidth: 20
									},
									highlightCfg: {
										saturationFactor: 1.5
									},
									label: {
										field: 'views',
										display: 'insideEnd',
										renderer: function (v) {
										  return Ext.util.Format.number(v, '0,000');
										}
									},
									tooltip: {
										trackMouse: true,
										renderer: function (tooltip, record, item) {
											tooltip.setHtml(record.get('componentName') + ': ' +
											Ext.util.Format.number(record.get('views'), '0,000 views'));
										}
									}
								}],
								sprites: [{
									type: 'text',
									text: 'Top Viewed Entries',
									fontSize: 22,
									width: 100,
									height: 30,
									x: 40, // the sprite x position
									y: 20  // the sprite y position
								}]
							}
						]
					}
					
				]
					
			});
			
			var actionLoadComponentData = function() {
				componentStats.setLoading(true);
				
				Ext.Ajax.request({
					url: '../api/v1/service/statistic/component',
					success: function(response, opt){
						var data = Ext.decode(response.responseText);
						Ext.getCmp('componentStatsPanel').update(data);
						Ext.getCmp('componentStatsViewedPanel').update(data);
						
						componentTopStore.loadData(data.topViewed);
						
						componentStats.setLoading(false);
					},
					failure: function(response, opt){
						componentStats.setLoading(false);
					}
				});
		
			};			
			actionLoadComponentData();
			
			var tplUserStats = new Ext.XTemplate(
				'<div style="padding: 10px;" ><ul class="list-group">',	
				'<li class="list-group-item">Users: <span class="badge">{activeUsers}</span></li>',
				'<li class="list-group-item">Reviews: <span class="badge">{activeUserReviews}</span></li>',
				'<li class="list-group-item">Questions: <span class="badge">{activeUserQuestions}</span></li>',
				'<li class="list-group-item">Question Responses: <span class="badge">{activeUserQuestionResponses}</span></li>',
				'<li class="list-group-item">Watches: <span class="badge">{activeUserWatches}</span></li>',
				'</ul><div>'
			);
			var tplUserStatsRecentLogins = new Ext.XTemplate(
				'<ol style="padding-top: 10px;"><tpl for="recentLogins">', 
				'<li> <b>{username}<b> - {loginDts:date("m/d/Y H:i:s")}</li>',				
				'</tpl></ol>'
			);			
			
			var userStats = Ext.create('Ext.panel.Panel', {
				title: 'User Statistics',
				iconCls: 'fa fa-lg fa-users',				
				border: 1,
				margin: "0 20 0 0",
				style: 'box-shadow: 7px 7px 7px #888888;',
				float: true,
				frame: true,
				flex: 1,
				tools: [
					{
						type: 'refresh',
						handler: function(event, toolEl, panelHeader) {
							actionLoadUserData();
						}
					}
				],
				items: [
					{
						xtype: 'panel',
						layout: {
							type: 'hbox',
							align: 'stretch'
						},
						items: [
							{
								xtype: 'panel',
								id: 'userStatsPanel',
								title: 'Active User Data',
								
								autoScroll: true,
								border: true,
								tpl: tplUserStats,								
								bodyStyle: 'background-color: white',
								width: '50%',
								margin: '0 0 0 0'
							},
							{
								xtype: 'panel',
								id: 'userStatsViewedPanel',
								title: 'Recent Logins',
								
								autoScroll: true,
								border: true,								
								bodyStyle: 'background-color: white',
								width: '50%',
								tpl: tplUserStatsRecentLogins
							}
						]
					}
				]
			});
			
			var actionLoadUserData = function() {
				userStats.setLoading(true);
				
				Ext.Ajax.request({
					url: '../api/v1/service/statistic/user',
					success: function(response, opt){
						var data = Ext.decode(response.responseText);
						Ext.getCmp('userStatsPanel').update(data);
						Ext.getCmp('userStatsViewedPanel').update(data);						
						
						userStats.setLoading(false);
					},
					failure: function(response, opt){
						userStats.setLoading(false);
					}
				});
		
			};			
			actionLoadUserData();			
			
			var notPanel = Ext.create('OSF.component.NotificationPanel', {      
			});
			
			var messages = Ext.create('Ext.panel.Panel', {
				title: 'Notifications',
				iconCls: 'fa fa-lg fa-envelope',
				height: 400,
				layout: 'fit',
				style: 'box-shadow: 7px 7px 7px #888888;',
				margin: "0 10 0 0",
				border: 1,
				float: true,
				frame: true,
				flex: 1,
				tools: [
					{
						type: 'refresh',
						handler: function(event, toolEl, panelHeader) {
							notPanel.refreshData();
						}
					}
				],
				items: [
					notPanel
				]				
			});
			
			var tplSystemStats = new Ext.XTemplate(
				'<div style="padding: 10px;" ><ul class="list-group">',	
				'<li class="list-group-item">Active Alerts: <span class="badge">{alertsSetup}</span></li>',
				'<li class="list-group-item">Current Branding: <span class="badge">{currentBranding}</span></li>',
				'<li class="list-group-item">Error Tickets: <span class="badge">{errorTickets}</span></li>',
				'<li class="list-group-item">Queued Messages: <span class="badge">{queuedMessages}</span></li>',
				'<li class="list-group-item">Scheduled Reports: <span class="badge">{scheduleReports}</span></li>',
				'<li class="list-group-item">Active Tasks: <span class="badge">{tasksRunning}</span></li>',
				'</ul><div>'
			);
	
			var tplSystemDetailStats = new Ext.XTemplate(
				'<div style="padding:10px;" ><ul class="list-group">',	
				'<li class="list-group-item">Version: <span class="badge">{applicationVersion}</span></li>',
				'<li class="list-group-item">Up Time: <span class="badge">{upTime}</span></li>',
				'<li class="list-group-item">Start Time: <span class="badge">{startTime}</span></li>',
				'<li class="list-group-item">System Load: <span class="badge">{systemLoad}</span></li>',			
				'</ul><div>'
			);
			
			var systemStats = Ext.create('Ext.panel.Panel', {
				title: 'System Statistics',		
				iconCls: 'fa fa-lg fa-gear',
				height: 300,
				margin: "0 10 0 0",
				border: 1,
				style: 'box-shadow: 7px 7px 7px #888888;',
				float: true,
				frame: true,
				flex: 1,
				bodyStyle: 'background-color: white',
				tools: [
					{
						type: 'refresh',
						handler: function(event, toolEl, panelHeader) {
							actionLoadSystemData();
						}
					}
				],
				items: [
					{
						xtype: 'panel',
						layout: {
							type: 'hbox',													
							align: 'stretch'
						},
						items: [
							{
								xtype: 'panel',
								id: 'systemStatusPanel',
								bodyStyle: 'background-color: white',
								width: '50%',
								tpl: tplSystemStats
							},
							{	
								bodyStyle: 'background-color: white; border-left: 1px solid lightgrey;',
								width: '50%',
								margin: '0 10 0 0',								
								items: [
									{
										xtype: 'panel',
										id: 'detailedSystemPanel',
										tpl: tplSystemDetailStats
									}, 
									{
										padding: '0 10 0 10',
										html: '<b>Working Memory</b>'
									},
									{
										xtype: 'progressbar',
										padding: '0 10 0 10',
										id: 'detailedSystemPanel-memory',
										text: ''
									}
								]								
							}
						]
					}		
				]
			});		
			
			var actionLoadSystemData = function() {
				systemStats.setLoading(true);
				
				Ext.Ajax.request({
					url: '../api/v1/service/statistic/system',
					success: function(response, opt){
						var data = Ext.decode(response.responseText);
						Ext.getCmp('systemStatusPanel').update(data);
						
						systemStats.setLoading(false);
					},
					failure: function(response, opt){
						systemStats.setLoading(false);
					}
				});
				
				Ext.Ajax.request({
				  url: '../api/v1/service/application/status',
				  success: function(response, opt){
					  var data = Ext.decode(response.responseText);
					  Ext.getCmp('detailedSystemPanel').update(data);
					  
					  Ext.getCmp('detailedSystemPanel-memory').setValue(data.heapMemoryStatus.usedKb/data.heapMemoryStatus.maxKb);					 
				  }
				  
				});
				
		
			};			
			actionLoadSystemData();
			
			var mainPanel = Ext.create('Ext.panel.Panel', {
				scrollable: true,		
				items: [					
					{
						xtype: 'panel',
						layout: {
							type: 'hbox',						
							padding: 10,
							align: 'stretch'
						},
						items: [
							componentStats,
							messages
														
						]
					},
					{
						xtype: 'panel',
						layout: {
							type: 'hbox',							
							padding: 10,
							align: 'stretch'
						},
						items: [
							userStats,
							systemStats
						]
					}
				]
			});
			
			Ext.create('Ext.container.Viewport', {
				layout: 'fit',
				items: [
					mainPanel
				]
			});
			mainPanel.updateLayout(true, true);
			
		});
		
	</script>
	</stripes:layout-component>
</stripes:layout-render>