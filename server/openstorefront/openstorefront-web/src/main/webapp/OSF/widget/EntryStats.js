/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* global Ext */

Ext.define('OSF.widget.EntryStats', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.EntryStats',
	
	layout: 'border',
			
	initComponent: function () {
		this.callParent();
		var statPanel = this;
		
		statPanel.componentTopStore = Ext.create('Ext.data.Store', {
			fields: ['componentId', 'componentName', 'views']
		});

		var tplComponentStats = new Ext.XTemplate(
			'<div style="padding: 10px;" ><ul class="stat-list-group">',	
			'<li class="stat-list-group-item">Approved: <span class="stat-badge">{numberOfApprovedEntries}</span></li>',
			'<li class="stat-list-group-item">Pending: <span class="stat-badge">{numberOfPendingEntries}</span></li>',
			'<li class="stat-list-group-item">Not Submitted: <span class="stat-badge">{numberOfNotSubmited}</span></li>',
			'<li class="stat-list-group-item">Categories (Attributes): <span class="stat-badge">{numberOfAttributesTypes}</span></li>',
			'<li class="stat-list-group-item">Specific Categories (Attributes): <span class="stat-badge">{numberOfAttributesCodes}</span></li>',
			'</ul><div>'
		);
		var tplComponentStatsRecentViews = new Ext.XTemplate(
			'<ol style="padding-top: 10px;"><tpl for="recentlyViewed">', 
			'<li> <b>{componentName}</b> <span style="font-size: 9px; color: grey; ">({[Ext.util.Format.date(Ext.Date.parse(values.viewDts, "c"), "m/d/Y H:i:s")]})</span>',
			'',
			'</li>',				
			'</tpl></ol>'
		);		
		
		statPanel.componentStatsPanel = Ext.create('Ext.panel.Panel', {
			title: 'Active Entries',								
			autoScroll: true,
			border: true,
			tpl: tplComponentStats,								
			bodyStyle: 'background-color: white',
			width: '50%',
			margin: '0 0 0 0'			
		});
		
		statPanel.componentStatsViewedPanel = Ext.create('Ext.panel.Panel', {
			title: 'Recently Viewed',								
			autoScroll: true,
			border: true,								
			bodyStyle: 'background-color: white',
			width: '50%',
			tpl: tplComponentStatsRecentViews			
		});		
		
		var items = [					
			{
				xtype: 'panel',
				region: 'north',
				layout: {
					type: 'hbox',
					align: 'stretch'
				},
				items: [
					statPanel.componentStatsPanel,
					statPanel.componentStatsViewedPanel
				]
			}, 
			{
				xtype: 'panel',
				layout: 'fit',
				region: 'center',
				items: [
					{
						xtype: 'cartesian',
						reference: 'chart',
						theme: {
							type: 'muted'
						},
						store: statPanel.componentTopStore,
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
		];
		
		
		statPanel.add(items);
		statPanel.refresh();
	},
	
	refresh: function() {
		var statPanel = this;
		
		statPanel.setLoading(true);
		Ext.Ajax.request({
			url: 'api/v1/service/statistic/component',
			callback: function(){
				statPanel.setLoading(false);
			},
			success: function(response, opt){
				var data = Ext.decode(response.responseText);
				statPanel.componentStatsPanel.update(data);
				statPanel.componentStatsViewedPanel.update(data);

				statPanel.componentTopStore.loadData(data.topViewed);

			}
		});

	}
	
	
});

