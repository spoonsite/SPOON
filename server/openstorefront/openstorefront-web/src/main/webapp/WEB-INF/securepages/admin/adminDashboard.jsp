<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../client/layout/adminlayout.jsp">
	<stripes:layout-component name="contents">
		
	<script type="text/javascript">
		
		/* global Ext */
		Ext.onReady(function(){	
			
			//var approveStatusTemplate
			
			var componentTopStore = Ext.create('Ext.data.Store', {
			    fields: ['name', 'viewCount'],
				data: [
					{ name: 'Calvin', viewCount: 150 },
					{ name: 'DIB',   viewCount: 500},
					{ name: 'GVS',   viewCount: 250},
					{ name: 'DDF',      viewCount: 375},
					{ name: 'Arcsight',  viewCount: 75}
				]
			});
			
			var componentStats = Ext.create('Ext.panel.Panel', {
				title: 'Entry Statistics',
				iconCls: 'fa fa-lg fa-database',
				height: 300,
				margin: "0 20 0 0",
				border: 1,
				float: true,
				flex: 1,
				padding: 5,
				layout: 'fit',
				tools: [
					{
						type: 'refresh',
						handler: function(event, toolEl, panelHeader) {
						
						}
					}
				],
				items: [
					/**
					{
						xtype: 'panel',
						layout: 'hbox',
						items: [
							{
								xtype: 'panel',
								tpl: 
							},
							{
								xtype: 'panel',
								tpl: 
							}
						]
					}, **/
				
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
							fields: 'viewCount',
							//maximum: 4000000,
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
							fields: 'name',
							grid: true
						}],
						series: [{
							type: 'bar3d',
							xField: 'name',
							yField: 'viewCount',
							style: {
								minGapWidth: 20
							},
							highlightCfg: {
								saturationFactor: 1.5
							},
							label: {
								field: 'viewCount',
								display: 'insideEnd',
								renderer: function (v) {
								  return Ext.util.Format.number(v, '0,000');
								}
							},
							tooltip: {
								trackMouse: true,
								renderer: function (tooltip, record, item) {
									tooltip.setHtml(record.get('name') + ': ' +
									Ext.util.Format.number(record.get('viewCount'), '0,000 views'));
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
					
			});
			
			var userStats = Ext.create('Ext.panel.Panel', {
				title: 'User Statistics',
				iconCls: 'fa fa-lg fa-users',
				height: 300,
				border: 1,
				float: true,
				flex: 1,
				tools: [
					{
						type: 'refresh',
						handler: function(event, toolEl, panelHeader) {
						
						}
					}
				]
			});
			
			var messages = Ext.create('Ext.panel.Panel', {
				title: 'Messages',
				iconCls: 'fa fa-lg fa-envelope',
				height: 300,
				margin: "0 20 0 0",
				border: 1,
				float: true,
				flex: 1,
				tools: [
					{
						type: 'refresh',
						handler: function(event, toolEl, panelHeader) {
						
						}
					}
				]
			});
			
			var systemStats = Ext.create('Ext.panel.Panel', {
				title: 'System Statistics',		
				iconCls: 'fa fa-lg fa-gear',
				height: 300,
				border: 1,
				float: true,
				flex: 1,
				tools: [
					{
						type: 'refresh',
						handler: function(event, toolEl, panelHeader) {
						
						}
					}
				]
			});			
			
			var mainPanel = Ext.create('Ext.panel.Panel', {
				scrollable: true,				
				items: [
					{
						xtype: 'panel',
						layout: {
							type: 'hbox',						
							padding: 10
						},
						items: [
							componentStats,
							userStats
						]
					},
					{
						xtype: 'panel',
						layout: {
							type: 'hbox',							
							padding: 10
						},
						items: [
							messages,
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
			
		});
		
	</script>
	</stripes:layout-component>
</stripes:layout-render>