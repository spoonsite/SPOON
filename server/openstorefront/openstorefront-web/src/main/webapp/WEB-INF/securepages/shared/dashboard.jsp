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

    Document   : dashboard
    Created on : May 3, 2016, 9:13:09 AM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../layout/toplevelLayout.jsp">	
    <stripes:layout-component name="contents">
		
	<stripes:layout-render name="../../../layout/${actionBean.headerPage}">		
	</stripes:layout-render>		

	<link rel="stylesheet" href="css/dashboard.css">	
		
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
			Ext.require('OSF.widget.EvaluationStats');
			
												
			Ext.onReady(function () {
				var adminUser = ${admin};
				
				var currentUser;
				CoreService.userservice.getCurrentUser().then(function(user){
					currentUser = user;
					loadUserWidgets(true);
				})
				
				var widgets = [
					{
						name: 'Notifications',
						code: 'NOTIFICATIONS',
						description: 'Shows event notifications',
						iconCls: 'fa fa-envelope-o',
						jsClass: 'OSF.component.NotificationPanel',
						height: 400,
						permissions: ['DASHBOARD-WIDGET-NOTIFICATIONS'],
						allowMultiples: false,
						refresh: function(widget) {
							widget.refreshData();
						}
					},
					{
						name: 'System Status',
						code: 'SYSTEMSTATS',
						description: 'Shows system status',
						iconCls: 'fa fa-2x fa-gear',
						jsClass: 'OSF.widget.SystemStats',						
						height: 320,
						permissions: ['DASHBOARD-WIDGET-SYSTEM-STATUS', 'ADMIN-SYSTEM-MANAGEMENT-STATUS'],
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
						iconCls: 'fa fa-lg fa-bar-chart icon-small-vertical-correction',
						jsClass: 'OSF.widget.UserStats',						
						height: 320,
						permissions: ['DASHBOARD-WIDGET-USER-STATS', 'ADMIN-USER-MANAGEMENT-READ'],
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
						iconCls: 'fa fa-lg fa-bar-chart icon-small-vertical-correction',
						jsClass: 'OSF.widget.EntryStats',						
						height: 575,
						permissions: ['DASHBOARD-WIDGET-ENTRY-STATS', 'ADMIN-ENTRY-READ'],
						adminOnly: true,
						allowMultiples: false,
						refresh: function(widget) {
							widget.refresh();
						}						
					},
					{
						name: 'Evaluation Stats',
						code: 'EVALSTATS',
						description: 'Shows evaluation stats',
						iconCls: 'fa fa-lg fa-pie-chart icon-small-vertical-correction',
						jsClass: 'OSF.widget.EvaluationStats',						
						height: 575,
						permissions: ['DASHBOARD-WIDGET-EVALUATION-STATS', 'USER-EVALUATIONS-READ'],
						adminOnly: false,
						allowMultiples: false,
						refresh: function(widget) {
							widget.refresh();
						}						
					},
					{
						name: 'Questions',
						code: 'QUESTIONS',
						description: 'Shows your questions and allows for viewing responses.',
						iconCls: 'fa fa-2x fa-question',
						jsClass: 'OSF.widget.Questions',						
						height: 400,
						permissions: ['DASHBOARD-WIDGET-QUESTIONS'],
						adminOnly: false,
						allowMultiples: false,
						refresh: function(widget) {
							widget.refresh();
						}
					},
					{
						name: 'Pending Approval Requests',
						code: 'APPROVEREQ',
						description: 'Shows entry changes and submission that are waiting for approval',
						iconCls: 'fa fa-lg fa-commenting',
						jsClass: 'OSF.widget.ApprovalRequests',						
						height: 400,
						permissions: ['DASHBOARD-WIDGET-PENDING-REQUESTS', 'ADMIN-ENTRY-READ'],
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
						iconCls: 'fa fa-lg fa-binoculars icon-small-vertical-correction',
						jsClass: 'OSF.component.UserWatchPanel',						
						height: 400,
						permissions: ['DASHBOARD-WIDGET-WATCHES'],
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
						iconCls: 'fa fa-lg fa-list icon-small-vertical-correction',
						jsClass: 'OSF.widget.Submissions',						
						height: 400,
						permissions: ['DASHBOARD-WIDGET-SUBMISSION-STATUS', 'USER-SUBMISSIONS-READ'],
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
						iconCls: 'fa fa-lg fa-search',
						jsClass: 'OSF.widget.SavedSearch',						
						height: 400,
						permissions: ['DASHBOARD-WIDGET-SAVED-SEARCH'],
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
						iconCls: 'fa fa-lg fa-comment',
						jsClass: 'OSF.widget.Feedback',						
						height: 400,
						permissions: ['DASHBOARD-WIDGET-OUTSTANDING-FEEDBACK', 'ADMIN-FEEDBACK-READ'],
						adminOnly: true,
						allowMultiples: false,
						refresh: function(widget) {
							widget.refresh();
						}
					},
					{
						name: 'Reports',
						code: 'REPORTS',
						description: 'Shows your generated report history.',
						iconCls: 'fa fa-lg fa-file-text-o',
						jsClass: 'OSF.widget.Reports',						
						height: 400,
						permissions: ['DASHBOARD-WIDGET-REPORTS', 'REPORTS'],
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
						iconCls: 'fa fa-2x fa-list-alt',
						jsClass: 'OSF.widget.RecentUserData',						
						height: 400,
						permissions: ['DASHBOARD-WIDGET-USER-DATA', 'ADMIN-ENTRY-READ'],
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
						if (widget.permissions) {
							var matchedPermissions = 0;
							Ext.Array.forEach(widget.permissions, function (permission) {
								if (CoreService.userservice.userHasPermission(currentUser, permission)) {
									matchedPermissions += 1;
								}
							});
							if (matchedPermissions !== widget.permissions.length) {
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
								'	<span style="float: left; width: 100px; padding-right: 20px; text-align: center;">',
								'		<tpl if="selected"><i class="fa fa-3x fa-check highlight-success "></i>',
								'		</tpl><tpl if="!selected"><i class="fa-3x {iconCls}"></i></tpl>',
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
									iconCls: 'fa fa-plus icon-button-color-save icon-small-vertical-correction',									
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
									iconCls: 'fa fa-close icon-button-color-warning icon-small-vertical-correction',
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
									iconCls: 'fa fa-2x fa-plus icon-button-color-save',
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
				var loadUserWidgets = function(noUpdateDash) {
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

								if (config) {
									config = Ext.clone(config);								
									config.name = widget.widgetName;
									config.widgetColor = widget.widgetColor;

									var widgetPanel;
									//	Add panel regardless of permissions. If a user gets in this state... we still want them to be able
									//		to remove the widget from the dashboard. NOTE: the user will not be able to use or view the widget
									//		contents. If the user does not have the permissions they will see a message indicating they
									//		do not have privileges.
									widgetPanel = addWidgetToDashboard(config, noUpdateDash);				
									
									// if (config.permissions) {
									// 	//if the user is no longer admin don't add widget
									// 	if (CoreService.userservice.userHasPermission(currentUser, config.permissions)) {
									// 		widgetPanel = addWidgetToDashboard(config, noUpdateDash);										
									// 	} 
									// } else {
									// 	widgetPanel = addWidgetToDashboard(config, noUpdateDash);				
									// }
									//set other settings
									if (widgetPanel) {
										if (widget.widgetState) {
											config.restore(widgetPanel.getComponent('actualWidget'), Ext.decode(widget.widgetState));
										}
									}
								}
							});
							updateDashboard();
						}
					});
					
				};
				
				
				var widgetsOnDashBoard = [];
				var addWidgetToDashboard = function(widget, noUpdateDash) {
					
					var widgetPanel = Ext.create('Ext.panel.Panel', {
						title: widget.name,
						iconCls: widget.iconCls,					
						layout: 'fit',
						frame: true,						
						requiredPermissions: widget.permissions,
						permissionLogicalOperator: 'AND',
						permissionCheckFailure: function () {
							this.setHidden(false);
							this.down('panel').setHidden(true);
							this.setHtml('<div style="display: table; height: 100%; width: 100%;"><h2 style="display: table-cell; vertical-align: middle; text-align: center;">You have insufficient privileges to view this widget</h2></div>');
							this.down('[type=refresh]').setHidden(true);
							this.down('[type=prev]').setHidden(true);
							this.down('[type=next]').setHidden(true);
							this.down('[type=maximize]').setHidden(true);
							this.down('[type=gear]').setHidden(true);
						},
						header: {
							style: {
								'background': widget.widgetColor
							}
						},
						style: 'box-shadow: 7px 7px 7px #888888;',						
						closable: true,												
						height: widget.height,
						widgetConfig: widget,
						flex: 1,
						tools: [
							{
								type: 'refresh',
								itemId: 'refresh-tool',
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
								hidden: Ext.isIE9m ? true : false,
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
										iconCls: 'fa fa-lg fa-gear',
										closeMode: 'destroy',
										modal: true,
										width: 350,
										height: 200,
										minHeight: 200,
										scrollable: true,
										bodyStyle: 'padding: 10px',
										y: tool.getY(),
										x: panel.getX() + (panel.getWidth() / 2) - 175,
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
														iconCls: 'fa fa-lg fa-check icon-button-color-save',
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
														iconCls: 'fa fa-lg fa-close icon-button-color-warning',
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
										title:'Delete Widget?',
										message: 'Are you sure you want to delete this widget?',
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
					if (!noUpdateDash) {
						updateDashboard();
					}	
					
					if (widget.widgetColor) {
						widgetPanel.headerColor = widget.widgetColor;
						widgetPanel.headerStyle = {
							'background': widget.widgetColor																
						};	
						if (widgetPanel.getHeader().setStyle) {
							widgetPanel.getHeader().setStyle(widgetPanel.headerStyle);					
						}
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