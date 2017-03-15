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
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">

		<stripes:layout-render name="../../../../layout/adminheader.jsp">		
		</stripes:layout-render>		
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function () {


				var jobStore = Ext.create('Ext.data.Store', {
					storeId: 'jobStore',
					autoLoad: true,
					fields: [
						{
							name: 'perviousFiredTime',
							type: 'date',
							dateFormat: 'c'
						},
						{
							name: 'nextFiredTime',
							type: 'date',
							dateFormat: 'c'
						}						
					],
					proxy: {
						type: 'ajax',
						url: 'api/v1/service/jobs'
					}					
				});

				jobStore.filterBy(function(record) {
						return record.data.jobName.indexOf('ComponentJob');
				});				

				var jobGrid = Ext.create('Ext.grid.Panel', {
					title: 'Jobs',
					id: 'jobGrid',
					store: jobStore,
					columnLines: true,
					columns: [
						{
							text: 'Job Name',
							dataIndex: 'jobName',
							flex: 3,
							cellWrap: true
						},
						{
							text: 'Group Name',
							dataIndex: 'groupName',
							flex: 1
						},
						{
							text: 'Description', 
							dataIndex: 'description',
							flex: 4
						},
						{
							text: 'Status', 
							dataIndex: 'status',
							flex: 1,
							renderer: function(value, metadata, record) {
								if (value === 'NORMAL')
									metadata.tdCls = 'alert-success';
								else if (value === 'PAUSED')
									metadata.tdCls = 'alert-warning';
								return value;
							}
						},
						{
							text: 'Previous Scheduled Fire Time', 
							dataIndex: 'perviousFiredTime',
							flex: 2,
							xtype: 'datecolumn',
							format: 'm/d/y H:i:s'
						},
						{
							text: 'Next Scheduled Fire Time',
							dataIndex: 'nextFiredTime',
							flex: 2,
							xtype: 'datecolumn',
							format: 'm/d/y H:i:s'
						},
						{
							text: 'Job Data', 
							dataIndex: 'jobData',
							flex: 6,
							cellWrap: true
						}
					],
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [
								{
									text: 'Refresh',
									tooltip: 'Refresh the list of jobs',
									scale: 'medium',
									id: 'jobGrid-refresh',
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										jobStore.load();
									}
								},
								{ 
									xtype: 'tbseparator',
								},
								{
									text: 'Resume Job',
									tooltip: 'Resume the selected job',
									scale: 'medium',
									id: 'jobGrid-jobResume',
									iconCls: 'fa fa-2x fa-play-circle icon-button-color-run icon-vertical-correction',
									name: 'individualJobControl',
									disabled: true,
									handler: function () {
										var record = Ext.getCmp('jobGrid').getSelection()[0];
										resumeJob(record);
									}
								},
								{
									text: 'Pause Job',
									tooltip: 'Pause the selected job',
									name: 'individualJobControl',
									scale: 'medium',
									id: 'jobGrid-jobPause',
									iconCls: 'fa fa-2x fa-pause-circle icon-button-color-default icon-vertical-correction',
									disabled: true,
									handler: function () {
										var record = Ext.getCmp('jobGrid').getSelection()[0];
										pauseJob(record);
									}
								},
								{
									text: 'Run Job',
									tooltip: 'Execute the selected job',
									scale: 'medium',
									id: 'jobGrid-jobExecute',
									iconCls: 'fa fa-2x fa-bolt icon-button-color-run icon-vertical-correction',
									name: 'individualJobControl',
									disabled: true,
									handler: function () {
										var record = Ext.getCmp('jobGrid').getSelection()[0];
										executeJob(record);
									}
								},
								{ 
									xtype: 'tbfill'
								},
								{ 
									xtype: 'label',
									text: 'Show Integration Jobs:',
									style: {
										fontWeight: 'bold'
									}
								},
								{
									xtype: 'segmentedbutton',
									scale: 'medium',
									items: [  
										{
											enableToggle: true,
											scale: 'medium',
											toggleGroup: 'intJobs',
											id: 'jobGrid-showIntegrationBox',
											text: 'Yes',
											name: 'showIntegration',
											handler: function () {
												jobStore.clearFilter();
											}
										},
										{
											enableToggle: true,
											scale: 'medium',
											toggleGroup: 'intJobs',
											id: 'jobGrid-noshowIntegrationBox',
											text: 'No',
											pressed: true,
											name: 'showIntegration',
											handler: function () {
												jobStore.filterBy(function(record) {
													return record.data.jobName.indexOf('ComponentJob');
												});
											}
										}
									]
								},
								{ 
									xtype: 'tbseparator'
								},
								{
									xtype: 'label',
									html: '<ext title="The scheduler state is not persistent. When the web application is restarted, the scheduler state will be reset.">System Job Scheduler:',
									style: {
										fontWeight: 'bold'
									}
								},
								{
									xtype: 'label',
									id: 'schedulerStatusLabel',
									text: 'Running',
									style: {
										color: 'green',
										fontWeight: 'bold'
									}
								},
								{
									text: 'Pause Scheduler',
									tooltip: 'Toggle the scheduler status',
									scale: 'medium',
									toggleGroup: 'scheduler',
									id: 'jobGrid-schedulerToggleButton',
									iconCls: 'fa fa-2x fa-pause-circle icon-button-color-default icon-vertical-correction',
									name: 'schedulerControl',
									handler: function () {
										toggleScheduler();
									}
								}
							]
						}
					],
					listeners: {
						itemdblclick: function (grid, record, item, index, e, opts) {
						},
						selectionchange: function (grid, record, eOpts) {
							if (Ext.getCmp('jobGrid').getSelectionModel().hasSelection()) {
								Ext.getCmp('jobGrid-jobExecute').enable();
								var record = Ext.getCmp('jobGrid').getSelection()[0];
								if (record.data.status === 'NORMAL') {
									Ext.getCmp('jobGrid-jobPause').enable();
									Ext.getCmp('jobGrid-jobResume').disable();
								}
								else {
									Ext.getCmp('jobGrid-jobPause').disable();
									Ext.getCmp('jobGrid-jobResume').enable();
								}

							} else {
								Ext.getCmp('jobGrid-jobExecute').disable();
								Ext.getCmp('jobGrid-jobPause').disable();
								Ext.getCmp('jobGrid-jobResume').disable();
							}
						}
					}
				});

				var taskStore = Ext.create('Ext.data.Store', {
					storeId: 'taskStore',
					autoLoad: true,
					fields: [
						{
							name: 'submitedDts',
							type:	'date',
							dateFormat: 'c'
						},
						{	
							name: 'completedDts',
							type:	'date',
							dateFormat: 'c'
						},
						{
							name: 'expireDts',
							type:	'date',
							dateFormat: 'c'
						}						
					],					
					proxy: {
						id: 'taskStoreProxy',
						type: 'ajax',
						url: 'api/v1/service/jobs/tasks/status',
						reader: {
							type: 'json',
							rootProperty: 'tasks'
						}
					},					
					listeners: {
						load: function(store, records, successful, opts) {
							var data = Ext.decode(opts.getResponse().responseText);
							var dataArray = [];
							dataArray.push(data);
							taskStatsStore.loadData(dataArray);		
						}
					}
				});

				var taskStatsStore = Ext.create('Ext.data.Store', {
					storeId: 'taskStatsStore',
					autoLoad: false,
					proxy: {
						id: 'taskStatsStoreProxy',
						type: 'ajax',
						url: 'api/v1/service/jobs/tasks/status'
					}
				});


				var taskGrid = Ext.create('Ext.grid.Panel', {
					title: 'Tasks',
					id: 'taskGrid',
					store: taskStore,
					columnLines: true,
					columns: [
						{text: 'Task Name', dataIndex: 'taskName', flex: 1},
						{text: 'Details', dataIndex: 'details', flex: 4},
						{text: 'Status', dataIndex: 'status', align: 'center', flex: 1,
							renderer: function(value, meta, record) {
								if (value === 'FAILED') {
									meta.tdCls = 'alert-danger';
								} else if (value === 'WORKING') {									
									meta.tdCls = 'alert-warning';
								} else if (value === 'QUEUED') {	
									meta.tdCls = 'alert-info';
								} else if (value === 'DONE') {
									meta.tdCls = 'alert-success';
								} else if (value === 'CANCELLED') {
									meta.tdCls = 'alert-danger';
								}
								return value;
							}
						},
						{
							text: 'Submitted Date',
							dataIndex: 'submitedDts',
							flex: 1,
							xtype: 'datecolumn',
							format: 'm/d/y H:i:s'
						},
						{
							text: 'Completion Date',
							dataIndex: 'completedDts',
							flex: 1,
							xtype: 'datecolumn',
							format: 'm/d/y H:i:s'
						},
						{
							text: 'Expiration Date',
							dataIndex: 'expireDts',							
							flex: 1,
							xtype: 'datecolumn',
							format: 'm/d/y H:i:s'
						},
						{text: 'Allows Multiple Tasks', dataIndex: 'allowMultiple', flex: 1}

					],
					dockedItems: [
						{
							dock: 'bottom',
							title: 'Task Statistics',
							xtype: 'panel',
							items: [
								{
									xtype: 'grid',
									store: taskStatsStore,
									columns: [
										{text: 'Active Count', dataIndex: 'activeCount', flex: 1},
										{text: 'Queued Count', dataIndex: 'queuedCount', flex: 1},
										{text: 'Complete Count', dataIndex: 'completedCount', flex: 1},
										{text: 'Total Count', dataIndex: 'totalTaskCount', flex: 1},
										{text: 'Worker Pool Size', dataIndex: 'threadPoolSize', flex: 1},
										{text: 'Largest Pool Size', dataIndex: 'largestPoolSize', flex: 1},
										{text: 'Max Pool Size', dataIndex: 'maxPoolSize', flex: 1}
									]
								}
							]
						},
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [
								{
									text: 'Refresh',
									tooltip: 'Refresh the list of tasks',
									scale: 'medium',
									id: 'taskGrid-refresh',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										taskStore.load();									
									}
								},
								{
									xtype: 'tbfill'									
								},
								
								{
									text: 'Stop Task',
									tooltip: 'Attempts to cancel the task',
									scale: 'medium',
									id: 'taskGrid-tools-cancel',
									iconCls: 'fa fa-2x fa-stop-circle icon-button-color-default icon-vertical-correction',
									disabled: true,
									handler: function() {
										actionCancelTask(Ext.getCmp('taskGrid').getSelection()[0]);
									}									
								},
								{
									xtype: 'tbseparator'
								},								
								{
									text: 'Delete',
									tooltip: 'Delete the task',
									scale: 'medium',
									id: 'taskGrid-tools-delete',
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									disabled: true,
									handler: function() {
										actionDeleteTask(Ext.getCmp('taskGrid').getSelection()[0]);
									}
								}
							]
						}
					],
					listeners: {
						itemdblclick: function (grid, record, item, index, e, opts) {
						},
						selectionchange: function (selectionModel, records, eOpts) {
							if (Ext.getCmp('taskGrid').getSelectionModel().hasSelection()) {
								Ext.getCmp('taskGrid-tools-delete').disable();
								Ext.getCmp('taskGrid-tools-cancel').disable();
								
								if(records[0].get('status') === 'DONE' || 
									records[0].get('status') === 'FAILED' ||
									records[0].get('status') === 'CANCELLED') {
									Ext.getCmp('taskGrid-tools-delete').enable();
								}	
								if(records[0].get('status') === 'QUEUED' || 
									records[0].get('status') === 'WORKING') {
									Ext.getCmp('taskGrid-tools-cancel').enable();
								}
								
							} else {
								Ext.getCmp('taskGrid-tools-delete').disable();
								Ext.getCmp('taskGrid-tools-cancel').disable();
							}
						}
					}
				});

				var actionDeleteTask = function actionDeleteTask(record) {
					var title = 'Delete Task';
					var msg = 'Are you sure you want to delete "' + record.data.taskName + '"?';

					Ext.MessageBox.confirm(title, msg, function (btn) {
						if (btn === 'yes') {
							var taskId = record.data.taskId;
							var url = 'api/v1/service/jobs/tasks';
							url += '/' + taskId;
							var method = 'DELETE';
							Ext.Ajax.request({
								url: url,
								method: method,
								success: function (response, opts) {
									var message = 'Successfully deleted task: "'+ record.data.taskName + '"';
									Ext.toast(message, '', 'tr');
									Ext.getCmp('taskGrid').getStore().load();
									Ext.getCmp('taskGrid-tools-delete').disable();
								},
								failure: function (response, opts) {
									Ext.MessageBox.alert('Failed to delete',
									'Error: Could not delete task: "' + record.data.taskName + '"');
								}
							});	
						}
					});
				};
				
				var actionCancelTask = function actionDeleteTask(record) {
					var title = 'Cancel Task';
					var msg = 'Are you sure you want to cancel "' + record.data.taskName + '"?';

					Ext.MessageBox.confirm(title, msg, function (btn) {
						if (btn === 'yes') {
							var taskId = record.data.taskId;
							var url = 'api/v1/service/jobs/tasks';
							url += '/' + taskId + '/cancel';
							var method = 'POST';
							Ext.Ajax.request({
								url: url,
								method: method,
								success: function (response, opts) {
									var message = 'Successfully canceled task: "'+ record.data.taskName + '"';
									Ext.toast(message, '', 'tr');
									Ext.getCmp('taskGrid').getStore().load();
									Ext.getCmp('taskGrid-tools-delete').disable();
								},
								failure: function (response, opts) {
									Ext.MessageBox.alert('Failed to Cancel',
									'Error: Could not cancel task: "' + record.data.taskName + '"');
								}
							});	
						}
					});
				};				

				var jobsMainPanel = Ext.create('Ext.tab.Panel', {
					title: 'Manage Jobs &nbsp; <i class="fa fa-lg fa-question-circle"  data-qtip="Control and view scheduled jobs and background tasks."></i>',
					width: 400,
					height: 400,
					items: [jobGrid, taskGrid]
				});

				addComponentToMainViewPort(jobsMainPanel);

				var toggleScheduler = function toggleScheduler() {
					if (Ext.getCmp('schedulerStatusLabel').text === 'Running'){
						var what = 'pause';
						var url = 'api/v1/service/jobs/pause';
						var method = 'POST';
					}
					else {
						var what = 'resume';
						var url = 'api/v1/service/jobs/resume';
						var method = 'POST';
					}
					Ext.Ajax.request({
						url: url,
						method: method,
						success: function (response, opts) {
							var message = 'Successfully ' + what + 'd job scheduler';
							Ext.toast(message, '', 'tr');
							updateSchedulerStatus();
						},
						failure: function (response, opts) {
							Ext.MessageBox.alert( "Error: Could not " + what + ' job scheduler');
						}
					});	
				};

				var pauseJob = function pauseJob(record) {
					var url = 'api/v1/service/jobs/';
					url += record.data.jobName + '/pause';
					var method = 'POST';
					Ext.Ajax.request({
						url: url,
						method: method,
						success: function (response, opts) {
							var message = 'Successfully paused ' + record.data.jobName;
							Ext.toast(message, '', 'tr');
							jobStore.load();
						},
						failure: function (response, opts) {
							Ext.MessageBox.alert('Error','Could not pause ' + record.data.jobName);
						}
					});	
					
				};

				var resumeJob = function resumeJob(record) {
					var url = 'api/v1/service/jobs/';
					url += record.data.jobName + '/resume';
					var method = 'POST';
					Ext.Ajax.request({
						url: url,
						method: method,
						success: function (response, opts) {
							var message = 'Successfully resumed ' + record.data.jobName;
							Ext.toast(message, '', 'tr');
							jobStore.load();
						},
						failure: function (response, opts) {
							Ext.MessageBox.alert('Error','Could not resume ' + record.data.jobName);
						}
					});	
				};

				var executeJob = function executeJob(record) {
					var url = 'api/v1/service/jobs/';
					url += record.data.jobName + '/' + record.data.groupName + '/runnow';
					var method = 'POST';
					Ext.Ajax.request({
						url: url,
						method: method,
						success: function (response, opts) {
							var message = 'Successfully executed ' + record.data.jobName;
							Ext.toast(message, '', 'tr');
							jobStore.load();
						},
						failure: function (response, opts) {
							Ext.MessageBox.alert('Error','Could not execute ' + record.data.jobName);
						}
					});	
				};

				var updateSchedulerStatus = function updateSchedulerStatus() {
					Ext.Ajax.request({
						url: 'api/v1/service/jobs/status',
						success: function (response, opts) {
							var rsp = Ext.decode(response.responseText);
							var label = Ext.getCmp('schedulerStatusLabel');
							var button = Ext.getCmp('jobGrid-schedulerToggleButton');
							if (rsp.status === 'Running') {
								label.setText('Running');
								label.setStyle({color: 'green'});
								button.setText('Pause Scheduler');
								button.setIconCls('fa fa-2x fa-pause-circle icon-button-color-default icon-vertical-correction');
							}
							else {
								label.setText('Paused');
								label.setStyle({color: 'red'});
								button.setText('Resume Scheduler');
								button.setIconCls('fa fa-2x fa-play-circle icon-button-color-run icon-vertical-correction');
							}
						},
						failure: function (response, opts) {
							Ext.MessageBox.alert('Error', 'Unable to update system scheduler status.');
						}
					});		
				};
				
				updateSchedulerStatus();


			});

		</script>
    </stripes:layout-component>
</stripes:layout-render>
