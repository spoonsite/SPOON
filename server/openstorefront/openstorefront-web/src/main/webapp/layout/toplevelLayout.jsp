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

<%@page import="edu.usu.sdl.openstorefront.security.RedirectUtil"%>
<%@page import="edu.usu.sdl.openstorefront.web.init.ShiroAdjustedFilter"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="edu.usu.sdl.openstorefront.core.entity.Branding"%>
<%@page import="edu.usu.sdl.openstorefront.service.ServiceProxy"%>
<%@page import="edu.usu.sdl.openstorefront.security.SecurityUtil"%>
<%@page import="edu.usu.sdl.openstorefront.common.manager.PropertiesManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-definition>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=10, user-scalable=yes">
		
	<%
		String appVersion = PropertiesManager.getApplicationVersion();		
		request.setAttribute("appVersion", appVersion);
		
		Branding brandingView = ServiceProxy.getProxy().getBrandingService().getCurrentBrandingView();
		
		request.setAttribute("appTitle", brandingView.getApplicationName());		
		request.setAttribute("branding", brandingView);
		request.setAttribute("user", SecurityUtil.getCurrentUserName());
		request.setAttribute("usercontext", SecurityUtil.getUserContext());
		request.setAttribute("admin", SecurityUtil.isEntryAdminUser());
		
		request.setAttribute("idleTimeoutMinutes", PropertiesManager.getValue(PropertiesManager.KEY_UI_IDLETIMEOUT_MINUTES, "-1"));
		request.setAttribute("idlegraceperiod", PropertiesManager.getValue(PropertiesManager.KEY_UI_IDLETIMEGRACE_MINUTES, "1"));
		
	%>	

	<link href="webjars/extjs/6.2.0/build/classic/theme-triton/resources/theme-triton-all-debug.css" rel="stylesheet" type="text/css"/>
	<link href="webjars/extjs/6.2.0/build/packages/ux/classic/triton/resources/ux-all-debug.css" rel="stylesheet" type="text/css"/>
	<link href="webjars/extjs/6.2.0/build/packages/charts/classic/triton/resources/charts-all-debug.css" rel="stylesheet" type="text/css"/>
	<link href="webjars/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
	<link href="Branding.action?CSS&template=extTritonTheme.jsp&v=${appVersion}" rel="stylesheet" type="text/css"/>	
	<link href="Branding.action?CSS&template=apptemplate.jsp&v=${appVersion}" rel="stylesheet" type="text/css"/>
	<link href="Branding.action?Override&v=${appVersion}" rel="stylesheet" type="text/css"/>	
	
	<link rel="shortcut icon" href="${pageContext.request.contextPath}/appicon.png" type="image/x-icon">	

	<script src="webjars/extjs/6.2.0/ext-bootstrap.js" type="text/javascript"></script>
	<script src="webjars/extjs/6.2.0/build/classic/theme-triton/theme-triton.js" type="text/javascript"></script>
	<script src="webjars/extjs/6.2.0/build/packages/ux/classic/ux-debug.js" type="text/javascript"></script>
	<script src="webjars/extjs/6.2.0/build/packages/charts/classic/charts-debug.js" type="text/javascript"></script>
	<script src="scripts/socket.io.js" type="text/javascript"></script>
		
	<%-- Core Utils --%>	
	<script src="scripts/global/override.js?v=${appVersion}" type="text/javascript"></script>		
	<script src="scripts/util/coreUtil.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/util/dateUtil.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/global/coreService.js?v=${appVersion}" type="text/javascript"></script>	
	
	<%-- Custom Components --%>		
	<script src="scripts/component/notificationPanel.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/component/framePanel.js?v=${appVersion}" type="text/javascript"></script>	
	<script src="scripts/component/userProfilePanel.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/component/feedbackWindow.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/component/help.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/component/standardComponents.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/extension/tinymceExtensions.js?v=${appVersion}" type="text/javascript"></script>
	<script src="webjars/tinymcetextarea/5.1/tinymce/tinymce.min.js" type="text/javascript"></script>
	<script src="webjars/tinymcetextarea/5.1/TinyMCETextArea.js" type="text/javascript"></script>	
	
	<title>${appTitle}</title>
        <stripes:layout-component name="html_head"/>
    </head>
    <body>
	<stripes:layout-component name="body_header" />	
	
	
          <stripes:layout-component name="contents"/>
		  
	
	<stripes:layout-component name="body_footer" />	
	
	<script type="text/javascript">
		
		//Call this only after geting the usercontext
		//Global (specialcase) beware
		var setupServerNotifications = function(usercontext) {
			//FIX ME: Websockets 
//				io.set('transports', [
//					'websocket',
//					'xhr-polling'					
//				]);
			var pollingHandler = function(){
				
				var eventState = {
					eventIds: [],
					initalPoll: true
				};
				Ext.util.TaskManager.start({
					run: function(){
						//poll events
						Ext.Ajax.request({
							url: 'api/v1/resource/notificationevent',
							method: 'GET',
							params: {
								max: 5, 
								sortField: 'createDts'
							},
							success: function(response, opts) {
								var data = Ext.decode(response.responseText);
								
								if (eventState.initalPoll) {									
									Ext.Array.each(data.data, function(event){
										eventState.eventIds.push(event.eventId);
									});
									eventState.initalPoll = false;
								} else {
									Ext.Array.each(data.data, function(event){
										if (Ext.Array.contains(eventState.eventIds, event.eventId) === false) {
											eventState.eventIds.push(event.eventId);
											
											handleAlert({
												eventId: event.eventId,
												msg: event.message,
												type: event.entityMetaDataStatus ? alertStatus(event.entityMetaDataStatus) : event.eventTypeDescription
											}, {
												eventType: event.eventType
											});
										} 
									});
								}
							}
						});	
					},
					interval: 20000,
					fireOnStart: true
				});				
			};
			
			if (!Ext.isIE9m || Ext.isIE9m === false) {			
				var contextPath = "${pageContext.request.contextPath}";
				contextPath = contextPath.replace('/', '');
				var socket = io.connect('', {
				  'resource': contextPath + '/event', 
				   query: 'id=' + usercontext.username,
				   transports: [
					'websocket',
					'xhr-polling'
				   ]
				});


				  socket.on('connect', function () {
					// console.warn(this.socket.transport.name + ' contected');
				  });
				  socket.on('error', function (e) {
					//activate polling
					pollingHandler();
				  });
				  socket.on('WATCH', function (args) {

					var alert = {'type': args.entityMetaDataStatus ? alertStatus(args.entityMetaDataStatus): 'watch', 'msg': args.message + '<i>View the changes <a href="view.jsp?fullPage=true&id='+args.entityId+'"><strong>here</strong></a>.</i>', 'id': 'watch_'+ args.eventId};
					handleAlert(alert, args);
				  });
				  socket.on('IMPORT', function (args) {					
					var alert = {'type': args.entityMetaDataStatus ? alertStatus(args.entityMetaDataStatus): 'import', 'msg': args.message, 'id': 'import_'+ args.eventId};					
					handleAlert(alert, args);
				  });
				  socket.on('TASK', function (args) {				
					var alert = {'type': args.entityMetaDataStatus ? alertStatus(args.entityMetaDataStatus): 'task', 'msg': args.message, 'id': 'task_'+ args.eventId};
					handleAlert(alert, args);
				  });
				  socket.on('REPORT', function (args) {					
					var alert = {'type': args.entityMetaDataStatus ? alertStatus(args.entityMetaDataStatus): 'report', 'msg': args.message + '<i>View/Download the report <a href="UserTool.action?load=Reports"><strong>here</strong></a></i>.', 'id': 'report_'+ args.eventId};					
					handleAlert(alert, args);
				  });
				  socket.on('ADMIN', function (args) {					
					var alert = {'type': args.entityMetaDataStatus ? alertStatus(args.entityMetaDataStatus): 'admin', 'msg': '<i class="fa fa-warning"></i>&nbsp;' + args.message, 'id': 'admin_'+ args.eventId};
					handleAlert(alert, args);
				  });	
		     } else {
				pollingHandler(); 
			}
			  
			var alertStatus = function(status) {
				switch(status) {
				  case 'DONE':
				  return 'success';
				  break;
				  case 'CANCELLED':
				  return 'warning';
				  break;
				  case 'FAILED':
				  return 'danger';
				  break;
				  case 'QUEUED':
				  return 'warning';
				  break;
				  case 'WORKING':
				  return 'info';
				  break;
				  default:
				  return 'default';
				  break;
				}				
			};  
			  
			//Debounce....avoid dups (Coming from two instances; old admin page and the new)
			var lastNotificationEventId; 
			var handleAlert = function(alert, args) {
				var showMessage = true;
				if (lastNotificationEventId) {
					if (lastNotificationEventId === alert.id) {
						showMessage = false;
					}
				}
				if (showMessage) {					
					Ext.toast({
						html: alert.msg,
						title: 'Notification - ' + args.eventType,
						bodyCls: 'alert-' + alert.type,
						bodyPadding: 'padding: 40px;',
						closable: true,						
						minWidth: 200,						
						minHeight: 60,						
						align: 'br'
					});
					lastNotificationEventId = alert.id;
				}
			};			  
			  
		};
		
		/* global Ext */
		Ext.onReady(function(){
			//Idle timeout check (Note: this probably as it doesn't cover all cases.)
			Ext.ns('CoreApp');

			CoreApp.BTN_OK = 'ok';
			CoreApp.BTN_YES = 'yes';

			// before notifying the user session will expire. Change this to a reasonable interval.
			if (${idleTimeoutMinutes} === -1) {
				CoreApp.USE_IDLE_WARNING = false;
				CoreApp.SESSION_ABOUT_TO_TIMEOUT_PROMT_INTERVAL_IN_MIN = 25;
			} else {
				CoreApp.USE_IDLE_WARNING = true;
				CoreApp.SESSION_ABOUT_TO_TIMEOUT_PROMT_INTERVAL_IN_MIN = ${idleTimeoutMinutes};
			}			
			
			// 1 min. to kill the session after the user is notified.
			CoreApp.GRACE_PERIOD_BEFORE_EXPIRING_SESSION_IN_MIN = ${idlegraceperiod};
			// The page that kills the server-side session variables.
			CoreApp.SESSION_KILL_URL = '${pageContext.request.contextPath}/Login.action?Logout';
			CoreApp.toMilliseconds = function (minutes) {
			  return minutes * 60 * 1000;
			};

			CoreApp.simulateAjaxRequest = function () {
				Ext.Ajax.request({
					url: 'api/v1/resource/userprofiles/currentuser'
				});
			};

			CoreApp.sessionAboutToTimeoutPromptTask = new Ext.util.DelayedTask(function () {
				if (CoreApp.USE_IDLE_WARNING === false) {
					CoreApp.simulateAjaxRequest();
				} else {
					Ext.Msg.confirm(
						'Your Session is About to Expire',
						Ext.String.format('Your session will expire in {0} minute(s). Would you like to continue your session?',
							CoreApp.GRACE_PERIOD_BEFORE_EXPIRING_SESSION_IN_MIN),
						function (btn, text) {
							if (btn == CoreApp.BTN_YES) {
								// Simulate resetting the server-side session timeout timer
								// by sending an AJAX request.
								CoreApp.simulateAjaxRequest();
								CoreApp.sessionAboutToTimeoutPromptTask.delay(CoreApp.toMilliseconds(CoreApp.SESSION_ABOUT_TO_TIMEOUT_PROMT_INTERVAL_IN_MIN));
								CoreApp.killSessionTask.cancel();
							} else {
								// Send request to kill server-side session.
								window.location.replace(CoreApp.SESSION_KILL_URL);
							}
						}
					);

					CoreApp.killSessionTask.delay(CoreApp.toMilliseconds(
					CoreApp.GRACE_PERIOD_BEFORE_EXPIRING_SESSION_IN_MIN));	
				}	
			});
			CoreApp.killSessionTask = new Ext.util.DelayedTask(function () {        
				window.location.replace(CoreApp.SESSION_KILL_URL);
			});			
			
			Ext.Ajax.on('requestcomplete', function(conn, response, options){
			   if (options.url !== CoreApp.SESSION_KILL_URL) {				  		   
					// Reset the client-side session timeout timers.
					// Note that you must not reset if the request was to kill the server-side session.
					CoreApp.sessionAboutToTimeoutPromptTask.delay(CoreApp.toMilliseconds(CoreApp.SESSION_ABOUT_TO_TIMEOUT_PROMT_INTERVAL_IN_MIN));
					CoreApp.killSessionTask.cancel();				
				}
/*	

//this doesn't run because already jumps back to the logout. If this changes in the future this could be useful
 				else {
					var currentlocation = window.parent.location.pathname.replace('/openstorefront', '');
					currentlocation = currentlocation + window.parent.location.search;						
					
					// Notify user their session timed out.
					Ext.Msg.alert(
						'Session Expired',
						'Your session expired. Please login to start a new session.',
						function (btn, text) {
							if (btn === CoreApp.BTN_OK) {
								window.location.replace('${pageContext.request.contextPath}/Login.action?gotoPage=' + encodeURIComponent(currentlocation));
							}
						}
					);
				}
*/				
			});
	
		});
		
	</script>
	
	<%
		if (StringUtils.isNotBlank(brandingView.getAnalyticsTrackingCode())) {
			out.print(brandingView.getAnalyticsTrackingCode());			
		}
	%>	
	
	
    </body>
</html>
</stripes:layout-definition>