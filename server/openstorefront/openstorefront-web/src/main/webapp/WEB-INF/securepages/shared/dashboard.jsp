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

    Document   : dashboard
    Created on : May 3, 2016, 9:13:09 AM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../layout/toplevelLayout.jsp">	
    <stripes:layout-component name="contents">
		
	<stripes:layout-render name="../../../layout/${param.user ? 'userheader.jsp' : 'adminheader.jsp'}">		
	</stripes:layout-render>		
		
	<script src="scripts/component/userwatchPanel.js?v=${appVersion}" type="text/javascript"></script>	
		
        <script type="text/javascript">
			/* global Ext, CoreUtil */
			
			var dashboardPage = {
				selectWidget: function(widgetCode) {
					var widget = Ext.Array.findBy(dashboardPage.widgets, function(item) {
						if (item.code === widgetCode) {
							return true;
						}
					});
					if (widget.selected) {
						widget.selected = false;
					} else {
						widget.selected = true;
					}
					var pickWin = Ext.getCmp('addwidget-picklist').up('window');
					var scrollY = pickWin.getScrollY();
					var scrollX = pickWin.getScrollX();
					
					Ext.getCmp('addwidget-picklist').update(dashboardPage.widgets);
					
					pickWin.scrollTo(scrollX,scrollY);
				}
			};
			
			Ext.require('OSF.widget.SystemStats');
			Ext.require('OSF.widget.UserStats');
			Ext.require('OSF.widget.EntryStats');
			Ext.require('OSF.widget.ApprovalRequests');
			Ext.require('OSF.widget.Submissions');
			Ext.require('OSF.widget.SavedSearch');
			Ext.require('OSF.widget.Feedback');
			Ext.require('OSF.widget.Reports');
			Ext.require('OSF.widget.Questions');
			Ext.require('OSF.widget.RecentUserData');
			
												
			Ext.onReady(function () {
				var adminUser = ${admin};
				
				var widgets = [
					{
						name: 'Notifications',
						code: 'NOTIFICATIONS',
						description: 'Shows event notifications',
						iconCls: 'fa fa-envelope',
						jsClass: 'OSF.component.NotificationPanel',
						height: 400,
						allowMultiples: false,
						refresh: function(widget) {
							widget.refreshData();
						}
					},
					{
						name: 'System Status',
						code: 'SYSTEMSTATS',
						description: 'Shows system status',
						iconCls: 'fa fa-gear',
						jsClass: 'OSF.widget.SystemStats',						
						height: 320,
						adminOnly: true,
						allowMultiples: false,
						refresh: function(widget) {
							widget.refresh();
						}						
					},
					{
						name: 'User Stats',
						code: 'USERSTATS',
						description: 'Shows user stats',
						iconCls: 'fa fa-users',
						jsClass: 'OSF.widget.UserStats',						
						height: 320,
						adminOnly: true,
						allowMultiples: false,
						refresh: function(widget) {
							widget.refresh();
						}						
					},
					{
						name: 'Entry Stats',
						code: 'ENTRYSTATS',
						description: 'Shows entry stats',
						iconCls: 'fa fa-database',
						jsClass: 'OSF.widget.EntryStats',						
						height: 575,
						adminOnly: true,
						allowMultiples: false,
						refresh: function(widget) {
							widget.refresh();
						}						
					},
					{
						name: 'Pending Approval Requests',
						code: 'APPROVEREQ',
						description: 'Shows entry changes and submission that are waiting for approval',
						iconCls: 'fa fa-list',
						jsClass: 'OSF.widget.ApprovalRequests',						
						height: 400,
						adminOnly: true,
						allowMultiples: false,
						refresh: function(widget) {
							widget.refresh();
						}						
					},
					{
						name: 'Watches',
						code: 'WATCHES',
						description: 'Shows all watches on entries',
						iconCls: 'fa fa-binoculars',
						jsClass: 'OSF.component.UserWatchPanel',						
						height: 400,
						adminOnly: false,
						allowMultiples: false,
						refresh: function(widget) {
							widget.actionRefresh();
						}
					},
					{
						name: 'Submission Status',
						code: 'SUBMISSIONSTATS',
						description: 'Shows the status of your submissions',
						iconCls: 'fa fa-list',
						jsClass: 'OSF.widget.Submissions',						
						height: 400,
						adminOnly: false,
						allowMultiples: false,
						refresh: function(widget) {
							widget.refresh();
						}						
					},
					{
						name: 'Saved Search',
						code: 'SAVEDSEARCH',
						description: 'Shows the results of a saved search',
						iconCls: 'fa fa-search',
						jsClass: 'OSF.widget.SavedSearch',						
						height: 400,
						adminOnly: false,
						allowMultiples: true,
						refresh: function(widget) {
							widget.refresh();
						},
						save: function(widget) {
							return widget.saveConfig();
						},
						restore: function(widget, config) {							
							widget.restoreConfig(config);
						},						
						configChange: function() {
							saveDashboard();
						}
					},
					{
						name: 'Outstanding Feedback',
						code: 'OFEEDBACK',
						description: 'Shows outstanding feedback from users.',
						iconCls: 'fa fa-comment',
						jsClass: 'OSF.widget.Feedback',						
						height: 400,
						adminOnly: true,
						allowMultiples: false,
						refresh: function(widget) {
							widget.actionRefresh();
						}
					},
					{
						name: 'Reports',
						code: 'REPORTS',
						description: 'Shows your generated report history.',
						iconCls: 'fa fa-file-text-o',
						jsClass: 'OSF.widget.Reports',						
						height: 400,
						adminOnly: false,
						allowMultiples: false,
						refresh: function(widget) {
							widget.actionRefresh();
						}
					},
					{
						name: 'Questions',
						code: 'QUESTIONS',
						description: 'Shows your question and allows for viewing responses.',
						iconCls: 'fa fa-question',
						jsClass: 'OSF.widget.Questions',						
						height: 400,
						adminOnly: false,
						allowMultiples: false,
						refresh: function(widget) {
							widget.refresh();
						}
					},
					{
						name: 'Recent User Data',
						code: 'RECENTUDATA',
						description: 'Shows recent user data (questions, reviews, tags, contacts) added/updated in the past 30 days.',
						iconCls: 'fa fa-list-alt',
						jsClass: 'OSF.widget.RecentUserData',						
						height: 400,
						adminOnly: true,
						allowMultiples: false,
						refresh: function(widget) {
							widget.refresh();
						}						
					}
				];
				
				var getAvailableWidgets = function() {
					
					//filter out existing; allowMultiple is false
					var myAvailableWidgets = Ext.Array.clone(widgets);
					Ext.Array.each(myAvailableWidgets, function(item) {
						item.selected = false;
					});
					myAvailableWidgets = Ext.Array.filter(myAvailableWidgets, function(widget){		
						var keep = true;
						if (!widget.allowMultiples) {
							var existing = Ext.Array.findBy(widgetsOnDashBoard, function(item) {
								if (item.widgetConfig.code === widget.code) {
									return true;
								}
							});
							if (existing) {
								keep = false;
							}
						}
						if (widget.adminOnly) {
							if (!adminUser) {
								keep = false;
							}
						}
						
						return keep;
					});
					Ext.Array.sort(myAvailableWidgets, function(a, b) {
						return a.name.localeCompare(b.name);
					});
					
					return myAvailableWidgets;
				};
								
				var addWidgetsWin = Ext.create('Ext.window.Window', {
					title: 'Add Widget',
					iconCls: 'fa fa-plus',
					modal: true,
					width: '60%',
					height: '50%',
					y: 100,
					maximizable: true,
					scrollable: true,						
					bodyStyle: 'padding: 10px 20px 10px 10px;',
					items: [
						{
							xtype: 'panel',
							id: 'addwidget-picklist',
							width: '100%',
							bodyStyle: 'padding: 10px;',							
							tpl: new Ext.XTemplate(
								'<tpl for=".">',
								'  <div class="widget-picklist-item" onclick="dashboardPage.selectWidget(\'{code}\')">',								
								'	<span style="float: left; width: 100px; padding-right: 20px;">',
								'		<tpl if="selected"><i class="fa fa-5x fa-check highlight-success "></i>',
								'		</tpl><tpl if="!selected"><i class="fa-5x {iconCls}"></i></tpl>',
								'	</span>',
								'	<div class="widget-picklist-item-title">{name}</div> {description}',								
								'  </div>',
								'</tpl>'
							)
						}
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [
								{
									text: 'Add Selected Widgets',
									iconCls: 'fa fa-plus',									
									handler: function() {
										var selectedWidget = false;
										Ext.Array.each(dashboardPage.widgets, function(item) {
											if (item.selected) {
												selectedWidget = true;
												addWidgetToDashboard(item);												
											}
										});
										if (selectedWidget) {
											this.up('window').close();
											saveDashboard();
										} else {
											Ext.Msg.show({
												title:'Missing Selection?',
												message: 'Select widget(s) to add.',
												buttons: Ext.Msg.OK,
												icon: Ext.Msg.ERROR,
												fn: function(btn) {													 
												}
											});											
										}
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Cancel',
									iconCls: 'fa fa-close',
									handler: function() {
										this.up('window').close();
									}
								}								
							]
						}
					]
				});
				
				var addWidgetMessage = '<h1><i class="fa fa-2x fa-arrow-up"></i> Add widgets to dashboard</h1>';
				var rows = [];
				var updateDashboard = function() {
					Ext.getCmp('dashPanel').removeAll(false);
					var width = Ext.getCmp('dashPanel').getWidth();
					
					Ext.Array.each(rows, function(row) {
						row.removeAll(false);
						row.destroy();
					});
					rows = [];
		
					var maxColumns = 2;
					
					var layout;
					if (width && width < 1400) {
					  layout = {
						type: 'vbox',							
						padding: '10',
						align: 'stretch'
					  };
					} else {
					  layout = {
						type: 'hbox',							
						padding:  '10',
						align: 'stretch'
					  };
					}		
					
					var row;
					var widgetsInRow = 0;					
					Ext.Array.each(widgetsOnDashBoard, function(widget, index) {
						if (!row) {
							row = Ext.create('Ext.panel.Panel', {
								layout: layout
							});
							rows.push(row);
						}
						
						widget.setMargin('0 10 10 0');
						widget.dashBoardIndex = index;						
						row.add(widget);		
						widgetsInRow++;
						
						if (widgetsInRow >= maxColumns) 	{
							//create new row
							row = Ext.create('Ext.panel.Panel', {
								layout: layout
							});							
							rows.push(row);
							widgetsInRow = 0;
						}
					});
					
					if (rows.length > 0) {
						Ext.getCmp('dashPanel').update(undefined);
						Ext.getCmp('dashPanel').add(rows);		
						Ext.getCmp('dashPanel').updateLayout(true, true);
						Ext.Array.each(widgetsOnDashBoard, function(widget, index) {
							widget.updateMoveTools(widget);
						});
					} else {
						Ext.getCmp('dashPanel').update(addWidgetMessage);
					}
				};
							
				
				var dashPanel = Ext.create('Ext.panel.Panel', {
					title: 'Dashboard <i class="fa fa-question-circle"  data-qtip="Displays widgets and allows for quick mashup of data."></i>',
					id: 'dashPanel',					
					columnWidths: [
						0.5, 0.5
					],					
					scrollable: true,
					bodyStyle: 'padding: 10px;',					
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Add Widget',
									iconCls: 'fa fa-2x fa-plus',
									scale: 'medium',
									handler: function() {
										addWidgetsWin.show();
										dashboardPage.widgets = getAvailableWidgets();
										Ext.getCmp('addwidget-picklist').update(dashboardPage.widgets);										
									}
								}
							]
						}
					],
					listeners: {
					resize: function(panel, width, height, oldWidth, oldHeight, eOpts) {
							updateDashboard();
						}
					}
				});
				
				addComponentToMainViewPort(dashPanel);
				
				var dashboard;
				var loadUserWidgets = function() {
					dashPanel.setLoading(true);
					Ext.Ajax.request({
						url: 'api/v1/resource/userdashboard',
						callback: function() {
							dashPanel.setLoading(false);
						},
						success: function(response, opts) {
							dashboard = Ext.decode(response.responseText);
							
							Ext.Array.each(dashboard.widgets, function(widget){
								var config = Ext.Array.findBy(widgets, function(widgetConfig){
									if (widgetConfig.code === widget.systemWidgetCode) {
										return true;
									}
								});
								config = Ext.clone(config);								
								config.name = widget.widgetName;
								config.widgetColor = widget.widgetColor;
								
								var widgetPanel;
								if (config.adminOnly) {
									//if the user is no longer admin don't add widget
									if (adminUser) {
										widgetPanel = addWidgetToDashboard(config);										
									} 
								} else {
									widgetPanel = addWidgetToDashboard(config);				
								}
								
								//set other settings
								if (widgetPanel) {
									if (widget.widgetState) {
										config.restore(widgetPanel.getComponent('actualWidget'), Ext.decode(widget.widgetState));
									}
								}
							});							
						}
					});
					
				};
				loadUserWidgets();
				
				var widgetsOnDashBoard = [];
				var addWidgetToDashboard = function(widget) {
					
					var widgetPanel = Ext.create('Ext.panel.Panel', {
						title: widget.name,
						iconCls: widget.iconCls,					
						layout: 'fit',
						frame: true,						
						style: 'box-shadow: 7px 7px 7px #888888;',
						closable: true,												
						height: widget.height,
						widgetConfig: widget,
						flex: 1,
						tools: [
							{
								type: 'refresh',
								tooltip: 'Refresh Widget',							
								callback: function(panel, tool, event) {
									panel.widgetConfig.refresh(panel.getComponent('actualWidget'));
								}
							},							
							{
								type: 'prev',
								tooltip: 'Move previous',
								itemId: 'prevTool',
								callback: function(panel, tool, event) {
									var oldIndex = panel.dashBoardIndex;									
									var newIndex = oldIndex;
									if (oldIndex > 0) {
										newIndex--;
										
										var old = widgetsOnDashBoard[newIndex];
										widgetsOnDashBoard[newIndex] = widgetsOnDashBoard[oldIndex];
										widgetsOnDashBoard[oldIndex] = old;
										updateDashboard();
										saveDashboard();
									}									
								}
							},
							{
								type: 'next',
								tooltip: 'Move next',
								itemId: 'nextTool',
								callback: function(panel, tool, event) {
									var oldIndex = panel.dashBoardIndex;									
									var newIndex = oldIndex;
									if (oldIndex < widgetsOnDashBoard.length - 1) {
										newIndex++;
										
										var old = widgetsOnDashBoard[newIndex];
										widgetsOnDashBoard[newIndex] = widgetsOnDashBoard[oldIndex];
										widgetsOnDashBoard[oldIndex] = old;
										updateDashboard();
										saveDashboard();
									}
								}
							},
							{
								type: 'maximize',
								tooltip: 'Maximize view',
								callback: function(panel, tool, event) {
									
									var maxWindow = Ext.create('Ext.window.Window', {
										maximized: true,
										autoDestroy: false,
										closeMode: 'destroy',
										layout: 'fit',
										frame: false,
										title: panel.title,
										iconCls: panel.iconCls,
										widgetConfig: panel.widgetConfig,
										closable: false,
										tools: [
											{
												type: 'refresh',
												tooltip: 'Refresh Widget',							
												callback: function(panel, tool, event) {
													panel.widgetConfig.refresh(panel.getComponent('actualWidget'));
												}												
											},
											{
												type: 'restore',
												tooltip: 'Restore Widget',							
												callback: function(panel, tool, event) {
													tool.up('window').close();
												}												
											}											
										],
										items: [
											panel.getComponent('actualWidget')
										],
										listeners: {
											beforeclose: function(win, opts) {
												panel.add(win.getComponent('actualWidget'));
											}
										}
									});																		
									maxWindow.show();
									if (panel.headerStyle) {
										maxWindow.getHeader().setStyle(panel.headerStyle);
									}
								}
							},
							{
								type: 'gear',
								tooltip: 'Configure',
								callback: function(panel, tool, event) {
									
									var setupWin = Ext.create('Ext.window.Window', {
										title: 'Configure',										
										iconCls: 'fa fa-gear',
										closeMode: 'destory',
										modal: true,
										width: 350,
										scrollable: true,
										bodyStyle: 'padding: 10px',
										y: tool.getY(),
										x: tool.getX() - 350,
										items: [
											{
												xtype: 'textfield',
												itemId: 'title',
												width: '100%',
												fieldLabel: 'Title',
												name: 'title',
												value: panel.title,
												maxLength: 120
											},
											{
												xtype: 'colorfield',
												itemId: 'headerColor',
												width: '100%',
												format: '#hex6',
												fieldLabel: 'Header Color',
												value: panel.headerColor ? panel.headerColor : '${branding.primaryColor}',
												name: 'headerColor'
											}
										],
										dockedItems: [
											{
												xtype: 'toolbar',
												dock: 'bottom',
												items: [
													{
														text: 'Apply',
														iconCls: 'fa fa-check',
														handler: function(){
															var win = this.up('window');
															var headerColor = win.getComponent('headerColor').getValue();
															panel.headerColor = headerColor;
															panel.headerStyle = {
																'background': panel.headerColor																
															};	
															panel.getHeader().setStyle(panel.headerStyle);
															
															var title = win.getComponent('title').getValue();
															panel.setTitle(title);
															
															this.up('window').close();
															saveDashboard();
														}
													},
													{
														xtype: 'tbfill'
													},
													{
														text: 'Cancel',
														iconCls: 'fa fa-close',
														handler: function(){
															this.up('window').close();
														}														
													}
												]
											}
										]
									});
									setupWin.show();
								}								
							}
						],
						items: [
							Ext.create(widget.jsClass, {
								itemId: 'actualWidget',
								configChange: widget.configChange
							})
						],
						updateMoveTools: function(panel) {
							var prevTool = Ext.Array.findBy(panel.tools, function(item) {
								if (item.itemId === 'prevTool') {
									return item;
								}
							});
							var nextTool = Ext.Array.findBy(panel.tools, function(item) {
								if (item.itemId === 'nextTool') {
									return item;
								}
							});		
							prevTool.setHidden(true);
							nextTool.setHidden(true);
							if (widgetsOnDashBoard.length > 1) {
								if (panel.dashBoardIndex === 0) {							
									prevTool.setHidden(true);
									nextTool.setHidden(false);
								} else if (panel.dashBoardIndex === widgetsOnDashBoard.length-1) {
									prevTool.setHidden(false);
									nextTool.setHidden(true);							
								} else {
									prevTool.setHidden(false);
									nextTool.setHidden(false);
								}							
							}
						},
						listeners: {
							afterrender: function(panel, opts) {
								panel.updateMoveTools(panel);
							},
							beforeclose: function(panel, opts) {
								if (!panel.doWidgetRemove) {
									Ext.Msg.show({
										title:'Remove Widget?',
										message: 'Are you sure you want to remove widget?',
										buttons: Ext.Msg.YESNO,
										icon: Ext.Msg.QUESTION,
										fn: function(btn) {
											if (btn === 'yes') {
												panel.doWidgetRemove = true;
												panel.close();
											} 
										}
									});	
									return false;
								} 
							},
							close: function(panel, opts) {
								Ext.Array.remove(widgetsOnDashBoard, panel);
								if (widgetsOnDashBoard.length === 0) {
									Ext.getCmp('dashPanel').update(addWidgetMessage);
								}
								saveDashboard();
								updateDashboard();
							}
						}
					});
					widgetsOnDashBoard.push(widgetPanel);				
					updateDashboard();
					
					if (widget.widgetColor) {
						widgetPanel.headerColor = widget.widgetColor;
						widgetPanel.headerStyle = {
							'background': widget.widgetColor																
						};	
						widgetPanel.getHeader().setStyle(widgetPanel.headerStyle);					
					}					
					
					return widgetPanel;
				};
				
				var saveTask;
				var saveRunner = new Ext.util.TaskRunner();
				var saveDashboard = function() {
					
					if (saveTask) {
						saveTask.restart(2000);
					} else {
						saveTask = saveRunner.newTask({
							run: function() {
								
								dashboard.widgets = [];
								
								Ext.Array.each(widgetsOnDashBoard, function(widgetPanel){									
									var widgetState = null;
									if (widgetPanel.widgetConfig.save) {
										widgetState = Ext.encode(widgetPanel.widgetConfig.save(widgetPanel.getComponent('actualWidget')));
									}	
								
									dashboard.widgets.push({
										systemWidgetCode: widgetPanel.widgetConfig.code,
										widgetName: widgetPanel.getTitle(),
										widgetColor: widgetPanel.headerColor,
										widgetState: widgetState
									});									
								});
								
								Ext.Ajax.request({
									url: 'api/v1/resource/userdashboard/' + dashboard.dashboard.dashboardId,
									method: 'PUT',
									jsonData: dashboard,
									success: function(response, opts){
										Ext.toast("Saved dashboard state.");
									}
								});
								
							},
							interval: 3000,
							repeat: 1
						});
						saveTask.start(2000);
					}
					
				};
				
			});

        </script>

    </stripes:layout-component>
</stripes:layout-render>		