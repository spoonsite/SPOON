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

Ext.define('OSF.widget.SystemStats', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.SystemStats',
			
	initComponent: function () {
		this.callParent();
		
		var statPanel = this;
		
		var tplSystemStats = new Ext.XTemplate(
			'<div ><ul class="list-group">',	
			'<li class="stat-list-group-item">Active Alerts: <span class="stat-badge">{alertsSetup}</span></li>',
			'<li class="stat-list-group-item">Current Branding: <span class="stat-badge">{currentBranding}</span></li>',
			'<li class="stat-list-group-item">Error Tickets: <span class="stat-badge">{errorTickets}</span></li>',
			'<li class="stat-list-group-item">Queued Messages: <span class="stat-badge">{queuedMessages}</span></li>',
			'<li class="stat-list-group-item">Scheduled Reports: <span class="stat-badge">{scheduleReports}</span></li>',
			'<li class="stat-list-group-item">Active Tasks: <span class="stat-badge">{tasksRunning}</span></li>',
			'</ul><div>'
		);

		var tplSystemDetailStats = new Ext.XTemplate(
			'<div><ul class="list-group">',	
			'<li class="stat-list-group-item">Version: <span class="stat-badge">{applicationVersion}</span></li>',
			'<li class="stat-list-group-item">Up Time: <span class="stat-badge">{upTime}</span></li>',
			'<li class="stat-list-group-item">Start Time: <span class="stat-badge">{startTime}</span></li>',
			'<li class="stat-list-group-item">System Load: <span class="stat-badge">{systemLoad}</span></li>',
			'<li class="stat-list-group-item">Disk Space: <span class="stat-badge">{freeDiskSpace} MB / {totalDiskSpace} MB</span></li>',
			'<li class="stat-list-group-item">Working Memory: <span class="stat-badge">{heapMemoryStatus.usedKb} kb / {heapMemoryStatus.maxKb} kb</span></li>',				
			'</ul><div>'
		);

		
		statPanel.systemStatusPanel = Ext.create('Ext.panel.Panel', {
			bodyStyle: 'background-color: white',
			width: '50%',
			tpl: tplSystemStats
		});
		
		statPanel.detailedSystemPanel = Ext.create('Ext.panel.Panel', {
			tpl: tplSystemDetailStats
		});

		statPanel.detailedSystemPanelMemory = Ext.create('Ext.ProgressBar', {			
			text: ''
		});

		var items = {
			xtype: 'panel',
			layout: {
				type: 'hbox',													
				align: 'stretch'
			},
			items: [
				statPanel.systemStatusPanel,
				{	
					bodyStyle: 'background-color: white; border-left: 1px solid lightgrey;',
					width: '50%',
					margin: '0 10 0 0',								
					items: [
						statPanel.detailedSystemPanel
					]
				}
			]
		}		
		statPanel.add(items);
		statPanel.refresh();
		
	},
	
	refresh: function() {
		var statPanel = this;
		
		statPanel.setLoading(true);

		Ext.Ajax.request({
			url: '../api/v1/service/statistic/system',			
			success: function(response, opt){
				var data = Ext.decode(response.responseText);
				statPanel.systemStatusPanel.update(data);
				
				Ext.Ajax.request({
				  url: '../api/v1/service/application/status',
				  callback: function() {
					  statPanel.setLoading(false);
				  },
				  success: function(response, opt){
					  var data = Ext.decode(response.responseText);
					  statPanel.detailedSystemPanel.update(data);
				  }
				});
			}
		});		
		
	}
	
});


