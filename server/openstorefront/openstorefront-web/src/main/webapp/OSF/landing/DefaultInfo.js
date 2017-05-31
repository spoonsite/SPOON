/* 
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */

/* global Ext, CoreUtil */

Ext.define('OSF.landing.DefaultInfo', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-defaultinfo',
	
	layout: 'anchor',
	//width: '100%',
	//bodyStyle: 'padding-left: 60px; padding-right: 60px;',
	items: [
		{
			xtype: 'panel',
			width: '100%',
			minHeight: 250,			
			bodyCls: 'home-info-carousel',			
			itemId: 'carousel',
			tpl: new Ext.XTemplate(
				'<div class="new-home-highlight-item">',
				'	<div class="new-home-highlight-item-back">',
						'<div class="home-highlight-header"><tpl if="link"><a href="{link}" class="home-highlight-header" target="_blank">{titleDesc} <i class="fa fa-link"></i></a></tpl><tpl if="!link">{titleDesc}</tpl></div>',
						'<div class="new-home-highlight-item-desc"><tpl if="securityMarkingType">({securityMarkingType}) </tpl>{displayDesc}</div>',					
						'<div class="home-highlight-footer"><span style="font-size: 10px; float: left;padding-top:10px;">Updated: {[Ext.util.Format.date(values.updateDts, "m/d/y")]}</span><span style="margin-left: 20px;float: right;"><a href="#" class="home-readmore" onclick="CoreUtil.pageActions.readMoreView({index});">{moreText}</a></span></div>',
				'	</div>',										
				'</div>'
			),
			dockedItems: [
				{
					xtype: 'panel',
					dock: 'bottom',
					itemId: 'carouselIndicator',
					width: '100%',
					height: 30,
					bodyCls: 'home-info-carousel',
					tpl: new Ext.XTemplate(
						'<div class="home-highlight-footer-indicator">',						
							'<tpl for=".">',							
							'	<span class="home-highlight-footer-indicator-item" onclick="CoreUtil.pageActions.handleHightlightChange({index});">',
							'   {indicator}</span>',
							'</tpl>',
						'</div>'								
					)
				}				
			]
		}
	],	
	listeners: {
		resize: function(panel, width, height, oldWidth, oldHeight, eOpts) {
			var carousel = panel.queryById('carousel');
			if (width < 1024) {
				carousel.setWidth('100%');										
			} else {
				carousel.setWidth('50%');										
			}			
		}
	},	
	initComponent: function () {
		this.callParent();			
		var infoPanel = this;
		
				
		var carousel = infoPanel.queryById('carousel');
		var carouselIndicator = infoPanel.queryById('carouselIndicator');
		infoPanel.infoItems = {
			data: [],
			indicators: []
		};	
		carousel.currentHighlighIndex = 0;
		
		var handleHightlightChange = function(index) {
			carousel.currentHighlighIndex = index;
			updateHighlight();
			carousel.currentHighlighIndex++;
		};
		CoreUtil.pageActions.handleHightlightChange = handleHightlightChange;
		
		var readMoreView = function(index) {
			var dataItem = infoPanel.infoItems.data[index];
			
			if (dataItem.componentId) {
				//goto the search
				
				var searchObj = {
					"sortField": null,
					"sortDirection": "ASC",
					"startOffset": 0,
					"max": 2147483647,
					"searchElements": [{
							"searchType": "COMPONENT",
							"field": "componentId",
							"value": dataItem.componentId,
							"keyField": null,
							"keyValue": null,
							"startDate": null,
							"endDate": null,
							"caseInsensitive": false,
							"numberOperation": "EQUALS",
							"stringOperation": "EQUALS",
							"mergeCondition": "OR"  //OR.. NOT.. AND..
						}]
				};
				
				var searchRequest = {
					type: 'Advance',
					query: searchObj
				};
				
				CoreUtil.sessionStorage().setItem('searchRequest', Ext.encode(searchRequest));
				window.location.href = 'searchResults.jsp?showcomponent=' + dataItem.componentId;	
			} else {
				//show read more window
				var articleWindow = Ext.create('Ext.window.Window', {
					title: dataItem.title,
					width: '60%',
					height: '50%',
					autoScroll: true,
					modal: true,
					maximizable: true,
					closeAction: 'destroy',
					bodyStyle: 'padding: 20px;',
					html: dataItem.description + '<br><br><span style="font-size: 10px;">Updated: ' + Ext.util.Format.date(dataItem.updateDts, "m/d/y") + '</span>',							
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [
								{
									xtype: 'tbfill'
								},
								{
									text: 'Close',
									iconCls: 'fa fa-lg fa-close icon-button-color-warning',
									handler: function() {
										this.up('window').close();
									}
								},
								{
									xtype: 'tbfill'
								}
							]
						}
					]
				}).show();				
			}
			
		};
		CoreUtil.pageActions.readMoreView = readMoreView;
		
		var updateHighlight = function() {			
			if (carousel.currentHighlighIndex >= infoPanel.infoItems.data.length) {
				carousel.currentHighlighIndex = 0;
			} 

			if (carousel.currentHighlighIndex < 0) {
				carousel.currentHighlighIndex = infoPanel.infoItems.data.length-1;
			}
			
			infoPanel.infoItems.indicators = [];
			Ext.Array.each(infoPanel.infoItems.data, function(item, index){
				item.index = index;
				if (carousel.currentHighlighIndex === index) {
					infoPanel.infoItems.indicators.push({
						indicator: '<i class="fa fa-circle"></i>',
						index: index
					});
				} else {
					infoPanel.infoItems.indicators.push({
						indicator: '<i class="fa fa-circle-o"></i>',
						index: index
					});
				}
			});						

			carousel.update(infoPanel.infoItems.data[carousel.currentHighlighIndex]);
			carouselIndicator.update(infoPanel.infoItems.indicators);
			var textel = Ext.query('.new-home-highlight-item-desc');
			var el = Ext.get(textel[0]);
			el.slideIn();	
										
		};		
		
		Ext.Ajax.request({
			url: 'api/v1/resource/highlights',
			success: function(response, opts) {
				var highlights = Ext.decode(response.responseText);
				
				Ext.Array.each(highlights, function(highlight){
					if (!highlight.link) {
						highlight.link = false;
					}
					highlight.titleDesc = highlight.title; //Ext.util.Format.ellipsis(highlight.title, 50);
					highlight.moreText = 'Read More >>';
					highlight.displayDesc = Ext.util.Format.ellipsis(Ext.util.Format.stripTags(highlight.description), 700);

				});				
				
				Ext.Array.each(highlights, function(item){
					infoPanel.infoItems.data.push(item);
				});
				
				Ext.Ajax.request({
					url: 'api/v1/service/search/recent?max=3',
					success: function(response, opts) {
						var recent = Ext.decode(response.responseText);
						
						Ext.Array.each(recent, function(item){
							infoPanel.infoItems.data.push({
								titleDesc: Ext.util.Format.ellipsis(item.name),
								link: '',
								moreText: 'View >>',
								updateDts: item.addedDts,
								componentId: item.componentId,
								displayDesc: Ext.util.Format.ellipsis(Ext.util.Format.stripTags(item.description), 700)
							});
						});
						
						infoPanel.highlightTask = Ext.TaskManager.newTask({
							run: function() {								
								updateHighlight();								
								carousel.currentHighlighIndex++;
							},
							interval: 10000
						});
						Ext.TaskManager.start(infoPanel.highlightTask);						
						updateHighlight();
						carousel.currentHighlighIndex++;
					}
				});
			}
		});
		
		
		
	}
	
});
