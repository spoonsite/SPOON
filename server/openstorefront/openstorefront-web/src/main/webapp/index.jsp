<%--
/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
	
	<script src="scripts/component/advancedSearch.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/component/savedSearchPanel.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/component/searchToolContentPanel.js?v=${appVersion}" type="text/javascript"></script>
	

	<div id="browserWarning" class="browser-warning" >
		 <p>You are using an <strong>unsupported</strong> browser. The website may not work as intended.  Please switch to <strong>
		 <a class="browser-warning-link" href="http://www.mozilla.org/en-US/firefox/new/">Firefox</a></strong> or <strong>
		 <a class="browser-warning-link" href="https://www.google.com/intl/en-US/chrome/browser/">Chrome</a></strong>, or <strong>
		 <a class="browser-warning-link" href="http://browsehappy.com/">upgrade your browser</a></strong> to improve your experience</p>		
	</div>
	
	<script type="text/javascript">
		//start fresh on index
		sessionStorage.clear();
				
		if (Ext.isIE10m) {
			Ext.get('browserWarning').setStyle({
				display: 'block'
			});
		} 		
		
		var homepage = {
			readToggleHighlight: function(highlightId) {
				
				Ext.Array.each(homepage.highlights, function(item){
					if (item.highlightId === highlightId) {
//						if (item.moreText === 'Read More >>') {
//							item.displayDesc = item.description;
//							item.moreText = '<< Read Less';
//						} else {
//							item.displayDesc = Ext.util.Format.ellipsis(Ext.util.Format.stripTags(item.description), 300);
//							item.moreText = 'Read More >>';
//						}						
//						item.tpl.overwrite(item.panel.body, item);	
					
						var articleWindow = Ext.create('Ext.window.Window', {
							title: item.title,
							width: '60%',
							height: '50%',
							autoScroll: true,
							modal: true,
							maximizable: true,
							closeAction: 'destroy',
							bodyStyle: 'padding: 20px;',
							html: item.description + '<br><br><span style="font-size: 10px;">Updated: ' + Ext.util.Format.date(item.updateDts, "m/d/y") + '</span>',							
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
				});
			},
			viewRecentlyAdded: function(componentId) {
				
				var searchObj = {
					"sortField": null,
					"sortDirection": "ASC",
					"startOffset": 0,
					"max": 2147483647,
					"searchElements": [{
							"searchType": "COMPONENT",
							"field": "componentId",
							"value": componentId,
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
				window.location.href = 'searchResults.jsp?showcomponent=' + componentId;				
			}
		};
				
		/* global Ext, CoreService, CoreApp */	
		Ext.onReady(function(){
		
			var notificationWin = Ext.create('OSF.component.NotificationWindow', {				
			});	

			var searchtoolsWin;
						
			var quoteBanner = Ext.create('Ext.panel.Panel', {
				width: '100%',				
				layout: 'center',
				cls: 'home-quote-banner-section',				
				items: [
					{
						id: 'quote',
						bodyCls: 'home-quote-banner-text',
						html: ''
					}
				]
		
			});

			var searchPanel = Ext.create('Ext.panel.Panel', {				
				width: '100%',	
				bodyCls: 'home-search-panel',								
				items: [
					{
							xtype: 'panel',
							width: '100%',
							layout: 'center',
							items: [
								{
									xtype: 'image',
									id: 'logoImage',
									height: 200,						
									src: ''									
								}
							]
					},					
					{
							xtype: 'panel',
							width: '100%',
							layout: 'center',
							items: [
								{
									id: 'componentStats'
								}
							]
					},
					{
							xtype: 'panel',
							width: '100%',
							layout: 'hbox',
							items: [
								{
									flex: 1
								},
								{	
									xtype: 'panel',
									width: '65%',
									layout: {
										type: 'hbox',
										align: 'stretch'
									},
									items: [
										{
											xtype: 'combobox',										
											itemId: 'searchText',
											flex: 1,
											fieldCls: 'home-search-field',
											emptyText: 'Search',
											queryMode: 'remote',
											hideTrigger: true,
											valueField: 'query',
											displayField: 'name',											
											autoSelect: false,
											store: {
												autoLoad: false,
												proxy: {
													type: 'ajax',
													url: 'api/v1/service/search/suggestions'													
												}
											},
											listeners: {
												
												expand: function(field, opts) {
												
													field.getPicker().clearHighlight();
												},
												
												specialkey: function(field, e) {
													
													var value = this.getValue();
													
													if (!Ext.isEmpty(value)) {
														
														switch (e.getKey()) {
															
															case e.ENTER:
																var query = value;
																if (query && !Ext.isEmpty(query)) {
																	var searchRequest = {
																		type: 'SIMPLE',
																		query: CoreUtil.searchQueryAdjustment(query)
																	};
																	CoreUtil.sessionStorage().setItem('searchRequest', Ext.encode(searchRequest));
																}
																window.location.href = 'searchResults.jsp';
																break;
															
															case e.HOME:
																field.setValue(field.lastQuery);
																field.selectText(0, 0);
																field.expand();
																break;
															
															case e.END:
																field.setValue(field.lastQuery);
																field.selectText(field.getValue().length, field.getValue().length);
																field.expand();
																break;
														}
													}
												}
											} 
										},
										{
											xtype: 'button',
											tooltip: 'Keyword Search',
											iconCls: 'fa fa-2x fa-search icon-search-adjustment',
											style: 'border-radius: 0px 3px 3px 0px;',																					
											width: 50,											
											handler: function(){
											
												var query = this.up('panel').getComponent('searchText').getValue();
												if (query && !Ext.isEmpty(query)) {																										
													var searchRequest = {
														type: 'SIMPLE',
														query: CoreUtil.searchQueryAdjustment(query)
													}
													CoreUtil.sessionStorage().setItem('searchRequest', Ext.encode(searchRequest));
												} else {
													delete CoreUtil.sessionStorage().searchRequest;
												}												
												window.location.href = 'searchResults.jsp';
												
											}
										},
										{
											xtype: 'button',
											text: '<span style="font-size: 12px; margin-left: 2px;">Search Tools</span>',																		
											iconCls: 'fa fa-2x fa-search-plus icon-vertical-correction-search-tools',
											margin: '0 0 0 10',
											style: 'border-radius: 3px 3px 3px 3px;',											
											width: 130,
											handler: function(){
												searchtoolsWin.show();
											}
										}
									]
								},
								{
									flex: 1
								}
							]
					}										
				
				]
				
			});		
		
			var loadedHighlightsRecently = false;		
		
			var highlights = [];
			var recently = [];
			var loadHighlightsRecently = function() {
				Ext.Ajax.request({
					url: 'api/v1/resource/highlights',
					success: function(response, opt) {
						highlights = Ext.decode(response.responseText);	
						
						Ext.Array.each(highlights, function(highlight){
							if (!highlight.link) {
								highlight.link = false;
							}
						});
						
						homepage.highlights = highlights;
						
						Ext.Ajax.request({
							url: 'api/v1/service/search/recent',
							success: function(response, opt) {
								recently = Ext.decode(response.responseText);								
								homepage.recently = recently;
								loadedHighlightsRecently = true;
								
								populateInfoPanel(infoPanel);
							}
						});
					}
				});
			};
			loadHighlightsRecently();
			
			var populateInfoPanel = function(panel) {
				
				panel.suspendEvent('resize');
				panel.removeAll();			
				panel.resumeEvent('resize');	
				
				var layout = {
					type: 'hbox',
					align: 'stretch'
				};
				var infoPanelsWidth = '50%';
				var borderSeparator = 'border-right: 1px solid lightgrey !important; padding-right: 10px;';
				if (panel.getWidth() < 1024) {
					layout = {
						type: 'vbox'
					};
					infoPanelsWidth = '100%';
					borderSeparator = '';
				}			

				var highlightPanel = Ext.create('Ext.panel.Panel', {					
				});
				
				Ext.Array.each(highlights, function(item){
					
					var highlightTemplate = new Ext.XTemplate(
						'<div class="home-highlight-item">',
						'<h2><tpl if="link"><a href="{link}" class="link" target="_blank">{title}</a></tpl><tpl if="!link">{title}</tpl></h2>',
						'<div class="home-highlight-item-desc"><tpl if="securityMarkingType">({securityMarkingType}) </tpl>{displayDesc}<br><span style="font-size: 10px;">Updated: {[Ext.util.Format.date(values.updateDts, "m/d/y")]}</span></div>',
						'<br><div style="text-align: right;"><a href="#" class="link" onclick="homepage.readToggleHighlight(\'{highlightId}\');">{moreText}</a></div>',
						'</div>'
					);
			
					var itemPanel = Ext.create('Ext.panel.Panel', {
						tpl: highlightTemplate,
						bodyStyle: 'padding: 5px',
						margin: '0 0 20 0'
					});	
					item.tpl = highlightTemplate;
					item.moreText = 'Read More >>';
					item.displayDesc = Ext.util.Format.ellipsis(Ext.util.Format.stripTags(item.description), 300);
					item.panel = itemPanel;
					itemPanel.update(item);				
										
					highlightPanel.add(itemPanel);
					item.highlightPanel = highlightPanel;
				});
				
				var recentlyPanel = Ext.create('Ext.panel.Panel', {					
				});
				
				Ext.Array.each(recently, function(item){
					
					var template = new Ext.XTemplate(
						'<div class="home-highlight-item">',
						'	<h2><a href="#" class="link" onclick="homepage.viewRecentlyAdded(\'{componentId}\');"><tpl if="securityMarkingType">({securityMarkingType}) </tpl>{name}</a></h2>',
						'	<div class="home-highlight-item-desc">{displayDesc}</div>',
						'	<div class="home-highlight-approved">Approved: {[Ext.util.Format.date(values.addedDts, "m/d/y")]}</div>',						
						'</div>'
					);					
					
					var itemPanel = Ext.create('Ext.panel.Panel', {											
						tpl: template,
						bodyStyle: 'padding: 5px',
						margin: '0 0 20 0'
					});
					item.displayDesc = Ext.util.Format.ellipsis(Ext.util.Format.stripTags(item.description), 300);
					itemPanel.update(item);
					
					recentlyPanel.add(itemPanel);
					
				});				
				


				var mainPanel = Ext.create('Ext.panel.Panel', {					
					width: '100%',
					layout: layout,					
					items: [
						{									
							bodyStyle: borderSeparator,							
							margin: '0 10 0 0',
							xtype: 'panel',
							width: infoPanelsWidth,
							items: [
								{
									html: '<h1 class="home-info-section-title">Highlights</h1><hr class="home-info-section-title-rule">'
								},
								highlightPanel
							]									
						},
						{			
							xtype: 'panel',
							width: infoPanelsWidth,
							items: [
								{
									html: '<h1 class="home-info-section-title">Most Recently Added</h1><hr class="home-info-section-title-rule">'
								},
								recentlyPanel
							]									

						}								
					]
				});
				panel.suspendEvent('resize');
				panel.add(mainPanel);
				panel.updateLayout(true, true);
				panel.resumeEvent('resize');				
			};
	
			var infoPanel = Ext.create('Ext.panel.Panel', {				
				bodyCls: 'home-info-section',	
				width: '100%',
				listeners: {
					resize: function(panel, width, height, oldWidth, oldHeight, eOpts) {
						if (loadedHighlightsRecently) {
							populateInfoPanel(panel);
						}
					}
				}
			});
			

			var footer = Ext.create('Ext.panel.Panel', {	
				bodyCls: 'home-footer',
				width: '100%',
				items: [
					{
							xtype: 'panel',
							width: '100%',
							layout: 'center',
							bodyStyle: 'padding: 20px 0px 0px 0px;',							
							items: [
								{
									id: 'customFooter',
									bodyCls: 'home-footer-contents'
								}
							]
					},
					{
							xtype: 'panel',
							width: '100%',
							layout: 'center',
							bodyStyle: 'padding: 20px 0px 20px 0px;',
							items: [
								{
									html: '<div class="home-footer-version">${appVersion}</div>'
								}
							]
					}					
				]
			});

			var mainContentPanel = Ext.create('Ext.panel.Panel', {					
				items: [
					quoteBanner,
					searchPanel,
					infoPanel,
					footer
				]
			});

			Ext.create('Ext.container.Viewport', {
				layout: 'border',
				items: [{
					xtype: 'panel',
					id: 'topNavPanel',
					region: 'north',					
					border: false,					
					cls: 'border_accent',
					dockedItems: [						
						{
							xtype: 'toolbar',
							dock: 'top',								
							cls: 'nav-back-color',
							listeners: {
								resize: function(toolbar, width, height, oldWidth, oldHeight, eOpts) {
									if (width < 1024) {
										toolbar.getComponent('spacer').setHidden(true);
										toolbar.getComponent('notificationBtn').setText('');										
									} else {
										toolbar.getComponent('spacer').setHidden(false);										
										toolbar.getComponent('notificationBtn').setText('Notifications');										
									}
								}
							},
							items: [
								{
									xtype: 'tbspacer',
									itemId: 'spacer',
									width: 250
								},								
								{
									xtype: 'tbfill'
								},
								{
									xtype: 'tbtext',
									id: 'homeTitle',
									text: '',
									cls: 'page-title'
								},
								{
									xtype: 'tbfill'
								},
								{
									xtype: 'button',									
									itemId: 'notificationBtn',
									scale   : 'large',
									ui: 'default',
									iconCls: 'fa fa-2x fa-envelope-o',
									iconAlign: 'left',
									text: 'Notifications',
									handler: function() {
										notificationWin.show();
										notificationWin.refreshData();
									}
								},								
								Ext.create('OSF.component.UserMenu', {									
									ui: 'default',
									initCallBack: function(usercontext) {
										setupServerNotifications(usercontext);	
									}
								})
							]
						}						
					]
				},
				{
					region: 'center',
					xtype: 'panel',
					scrollable: true,
					items: [
						mainContentPanel
					]
				}]
			});	
			
			CoreService.brandingservice.getCurrentBranding().then(function(branding){				
				if (branding.securityBannerText && branding.securityBannerText !== '') {
					Ext.getCmp('topNavPanel').addDocked(CoreUtil.securityBannerPanel({
						securityBannerText: branding.securityBannerText
					}), 0);
				}
			});			
			
			var loadStats = function() {
				Ext.Ajax.request({
					url: 'api/v1/service/search/stats',
					success: function(response, opts) {
						var data = Ext.decode(response.responseText);
						
						Ext.getCmp('componentStats').update('<div class="home-search-stats">Browse through ' + data.numberOfComponents + ' ' + branding.landingStatsText + '<div>');						
					}
					
				});
			};			
			
			var branding; 
			var loadBranding = function() {
				Ext.Ajax.request({
					url: 'api/v1/resource/branding/current',
					success: function(response, opts) {
						branding = Ext.decode(response.responseText);					
						
						Ext.getCmp('homeTitle').setText(branding.landingPageTitle);
						Ext.getCmp('quote').update('<blockquote style="text-align: center;">' + branding.landingPageBanner + '</blockquote>');
						Ext.getCmp('logoImage').setSrc(branding.primaryLogoUrl);
						Ext.getCmp('logoImage').getEl().on('load', function(evt, target, opts){
							searchPanel.updateLayout(true, true);							
						});
						
						Ext.getCmp('customFooter').update(branding.landingPageFooter);
											
						loadStats();
						
						searchtoolsWin = Ext.create('OSF.component.SearchToolWindow', {
							branding: branding
						});							
					}
				});
			}
			loadBranding();
			
			var checkNotifications = function(){
				Ext.Ajax.request({
					url: 'api/v1/resource/notificationevent',
					success: function(response, opts) {
						var data = Ext.decode(response.responseText);
						
						var unreadCount = 0;
						Ext.Array.each(data.data, function(item){
							if (!item.readMessage) {
								unreadCount++;
							}
						});
						
						if (unreadCount > 0) {
							Ext.toast({
								title: 'Notifications',
								html: 'You have <span style="font-size: 16px; font-weight: bold;"> ' + unreadCount + ' unread</span> notifications.',
								align: 'br',
								closable: true
							});
						}
					}
				});
				
			};
			checkNotifications();
			
			
		});
		
	</script>	
		
    </stripes:layout-component>
</stripes:layout-render>