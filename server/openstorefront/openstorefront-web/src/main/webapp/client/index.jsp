<%--
Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
	
	<script src="scripts/component/searchToolContentPanel.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/component/searchToolWindow.js?v=${appVersion}" type="text/javascript"></script>
		
	<script type="text/javascript">
		
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
							html: item.description,							
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
											iconCls: 'fa fa-close',
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
			}
		};
				
		/* global Ext, CoreService, CoreApp */	
		Ext.onReady(function(){
		
			var notificationWin = Ext.create('OSF.component.NotificationWindow', {				
			});	

			var feedbackWin = Ext.create('OSF.component.FeedbackWindow', {				
			});
			
			var searchtoolsWin = Ext.create('OSF.component.SearchToolWindow', {	
			});			

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
											xtype: 'button',
											tooltip: 'Search Tools',								
											iconCls: 'fa fa-2x fa-th icon-top-padding',
											style: 'border-radius: 3px 0px 0px 3px;',
											scale   : 'large',
											width: 50,
											handler: function(){
												searchtoolsWin.show();
											}
										}, 
										{
											xtype: 'textfield',										
											itemId: 'searchText',
											flex: 1,
											fieldCls: 'home-search-field',
											emptyText: 'Search',
											listeners:{
												specialkey: function(field, e) {
													var value = this.getValue();
													if (e.getKey() === e.ENTER && !Ext.isEmpty(value)) {													
														var query = value;
														if (query && !Ext.isEmpty(query)) {
															var searchRequest = {
																type: 'SIMPLE',
																query: query
															}
															CoreUtil.sessionStorage().setItem('searchRequest', Ext.encode(searchRequest));
														}
														window.location.href = 'Router.action?page=main/searchResults.jsp';														
													}
												}
											} 
										},
										{
											xtype: 'button',
											tooltip: 'Keyword Search',
											iconCls: 'fa fa-2x fa-search icon-top-padding',
											style: 'border-radius: 0px 3px 3px 0px;',
											scale   : 'large',											
											width: 50,
											handler: function(){
											
												var query = this.up('panel').getComponent('searchText').getValue();
												if (query && !Ext.isEmpty(query)) {
													var searchRequest = {
														type: 'SIMPLE',
														query: query
													}
													CoreUtil.sessionStorage().setItem('searchRequest', Ext.encode(searchRequest));
												} else {
													delete CoreUtil.sessionStorage().searchRequest;
												}												
												window.location.href = 'Router.action?page=main/searchResults.jsp';
												
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
					url: '../api/v1/resource/highlights',
					success: function(response, opt) {
						highlights = Ext.decode(response.responseText);	
						homepage.highlights = highlights;
						
						Ext.Ajax.request({
							url: '../api/v1/service/search/recent',
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
						'<h2><tpl if="link"><a href="{link}" class="link" target="_blank">{title} <i class="fa fa-link"></i></a></tpl><tpl if="!link">{title}</tpl></h2>',
						'<div class="home-highlight-item-desc">{displayDesc}</div>',
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
						'<h2><a href="view?id={componentId}" class="link" target="_blank">{name} <i class="fa fa-link"></i></a></h2>',
						'<div class="home-highlight-item-desc">{displayDesc}</div>',						
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
									id: 'customFooter'
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
									iconCls: 'fa fa-2x fa-envelope icon-top-padding',
									iconAlign: 'left',
									text: 'Notifications',
									handler: function() {
										notificationWin.show();
										notificationWin.refreshData();
									}
								},								
								{
									xtype: 'button',
									id: 'userMenuBtn',
									scale   : 'large',
									ui: 'default',
									text: 'User Menu',
									minWidth: 150,
									maxWidth: 250,
									menu: {						
										items: [
											{
												text: 'Admin Tools',
												id: 'menuAdminTools',
												iconCls: 'fa fa-gear',
												hidden: true,
												href: 'admin.jsp'
											},											
											{
												xtype: 'menuseparator'
											},											
											{
												text: 'User Tools',
												iconCls: 'fa fa-user',
												href: 'usertools.jsp'
											},											
											{
												xtype: 'menuseparator'
											},
											{
												text: '<b>Help</b>',
												iconCls: 'fa fa-question-circle',
												href: '../help',
												hrefTarget: '_blank'
											},
											{
												text: '<b>Feedback / issues</b>',
												iconCls: 'fa fa-commenting',
												handler: function() {
													feedbackWin.show();
												}
											},
											{
												xtype: 'menuseparator'
											},
											{
												text: 'Logout',
												iconCls: 'fa fa-sign-out',
												href: '../Login.action?Logout'												
											}
										],
										listeners: {
											beforerender: function () {
											 this.setWidth(this.up('button').getWidth());
											}
										}
									}
								}
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
			
			
			CoreService.usersevice.getCurrentUser().then(function(response, opts){
				var usercontext = Ext.decode(response.responseText);
				
				var userMenuText = usercontext.username;
				if (usercontext.firstName && usercontext.lastName)
				{
					userMenuText = usercontext.firstName + ' ' + usercontext.lastName;
				}
				Ext.getCmp('userMenuBtn').setText(userMenuText);	
				if (usercontext.admin) {
					Ext.getCmp('menuAdminTools').setHidden(false);
				}				
				
				setupServerNotifications(usercontext);	
			});	
			
			
			var loadStats = function() {
				Ext.Ajax.request({
					url: '../api/v1/service/search/stats',
					success: function(response, opts) {
						var data = Ext.decode(response.responseText);
						
						Ext.getCmp('componentStats').update('<div class="home-search-stats">Browse through ' + data.numberOfComponents + ' ' + branding.landingStatsText + '<div>');						
					}
					
				});
			};			
			
			var branding; 
			var loadBranding = function() {
				Ext.Ajax.request({
					url: '../api/v1/resource/branding/current',
					success: function(response, opts) {
						branding = Ext.decode(response.responseText);
						branding = branding.branding;
						
						Ext.getCmp('homeTitle').setText(branding.landingPageTitle);
						Ext.getCmp('quote').update('<blockquote style="text-align: center;">' + branding.landingPageBanner + '</blockquote>');
						Ext.getCmp('logoImage').setSrc(branding.primaryLogoUrl);
						Ext.getCmp('customFooter').update(branding.landingPageFooter);
											
						loadStats();
					}
				});
			}
			loadBranding();
			
			var checkNotifications = function(){
				Ext.Ajax.request({
					url: '../api/v1/resource/notificationevent',
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