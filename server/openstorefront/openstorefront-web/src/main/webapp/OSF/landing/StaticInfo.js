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

Ext.define('OSF.landing.StaticInfo', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-staticinfo',
	
	listeners: {
		resize: function(infoPanel, width, height, oldWidth, oldHeight, eOpts) {

			infoPanel.suspendEvent('resize');
			infoPanel.removeAll();
			infoPanel.resumeEvent('resize');

			var containerLayout = {
				type: 'hbox',
				align: 'stretch'
			};
			var infoPanelsWidth = '50%';
			var borderSeparator = 'border-right: 1px solid lightgrey !important; padding-right: 10px; padding-left: 10px;';
			if (width < 1024) {
				infoPanelsWidth = '100%';
				containerLayout = {
					type: 'vbox'
				};	
				borderSeparator = '';
			} 			
			var container = Ext.create('Ext.panel.Panel', {	
				itemId: 'container',
				layout: containerLayout
			});

			//add display
			
			var template = new Ext.XTemplate(
				'<tpl for=".">',
					'<div class="new-home-highlight-item">',
					'	<div class="new-home-highlight-item-back">',
							'<div class="home-highlight-header"><tpl if="link"><a href="{link}" class="home-highlight-header" target="_blank">{titleDesc} <i class="fa fa-link"></i></a></tpl><tpl if="!link">{titleDesc}</tpl></div>',
							'<div class="new-home-highlight-item-desc"><tpl if="securityMarkingType">({securityMarkingType}) </tpl>{displayDesc}</div>',					
							'<div class="home-highlight-footer"><span style="font-size: 10px; float: left;padding-top:10px;">Updated: {[Ext.util.Format.date(values.updateDts, "m/d/y")]}</span><span style="margin-left: 20px;float: right;"><a href="#" class="home-readmore" onclick="CoreUtil.pageActions.readMoreView({index});">{moreText}</a></span></div>',
					'	</div>',										
					'</div>',
				'</tpl>'	
			);	

			var highlightPanel = Ext.create('Ext.panel.Panel', {
				itemId: 'highlightPanel',
				xtype: 'panel',
				width: infoPanelsWidth,						
				bodyCls: 'home-info-carousel',			
				bodyStyle: borderSeparator,
				items: [
					{
						html: '<h1 class="home-info-section-title">Highlights</h1><hr class="home-info-section-title-rule">'
					},
					{
						itemId: 'highlightTemplate',
						xtype: 'panel',							
						tpl: template					
					}
				]											
			});

			var recentlyAddedPanel = Ext.create('Ext.panel.Panel', {
				itemId: 'recentlyAddedPanel',
				xtype: 'panel',
				width: infoPanelsWidth,						
				bodyCls: 'home-info-carousel',
				bodyStyle: 'padding-right: 10px; padding-left: 10px;',			
				items: [										
					
				]							
			});				
			container.add(highlightPanel);
			container.add(recentlyAddedPanel);

			infoPanel.suspendEvent('resize');
			infoPanel.add(container);
			infoPanel.updateLayout(true, true);
			infoPanel.resumeEvent('resize');
			
			
		}
	},	
	initComponent: function () {
		this.callParent();			
		var infoPanel = this;						
		
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
		
		
	},
	afterRender: function() {
		this.callParent();			
		var infoPanel = this;
		Ext.defer(function(){
			infoPanel.loadData();
		}, 100);
		
	},
	loadData: function(){
		var infoPanel = this;
		
		if (!infoPanel.dataloaded) {
		
			var template = new Ext.XTemplate(
				'<tpl for=".">',
					'<div class="new-home-highlight-item">',
					'	<div class="new-home-highlight-item-back">',
							'<div class="home-highlight-header"><tpl if="link"><a href="{link}" class="home-highlight-header" target="_blank">{titleDesc} <i class="fa fa-link"></i></a></tpl><tpl if="!link">{titleDesc}</tpl></div>',
							'<div class="new-home-highlight-item-desc"><tpl if="securityMarkingType">({securityMarkingType}) </tpl>{displayDesc}</div>',					
							'<div class="home-highlight-footer"><span style="font-size: 10px; float: left;padding-top:10px;">Updated: {[Ext.util.Format.date(values.updateDts, "m/d/y")]}</span><span style="margin-left: 20px;float: right;"><a href="#" class="home-readmore" onclick="CoreUtil.pageActions.readMoreView({index});">{moreText}</a></span></div>',
					'	</div>',										
					'</div>',
				'</tpl>'	
			);
		
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
					var highlightItems = [];
					Ext.Array.each(highlights, function(item){
						highlightItems.push(item);
					});							
					infoPanel.queryById('highlightTemplate').update(highlightItems);

					Ext.Ajax.request({
						url: 'api/v1/service/search/recent?max=5',
						success: function(response, opts) {
							var recent = Ext.decode(response.responseText);

							var recentItems = [];
							Ext.Array.each(recent, function(item){
								recentItems.push({
									titleDesc: Ext.util.Format.ellipsis(item.name),
									link: '',
									moreText: 'View >>',
									updateDts: item.addedDts,
									componentId: item.componentId,
									displayDesc: Ext.util.Format.ellipsis(Ext.util.Format.stripTags(item.description), 700)
								});
							});			
							
							infoPanel.getComponent('container').getComponent('recentlyAddedPanel').add({								
								xtype: 'panel',
								data: recentItems,
								tpl: template
							});							
							console.log(template.apply(recentItems));
														
							infoPanel.getComponent('container').getComponent('recentlyAddedPanel').updateLayout(true, true);
							infoPanel.updateLayout(true, true);
						}
					});

				}
			});	
			
			infoPanel.dataloaded = true;
		}
	}
	
});

