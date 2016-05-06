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

Ext.define('OSF.widget.UserStats', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.UserStats',
			
	initComponent: function () {
		this.callParent();
		var statPanel = this;
					
		var tplUserStats = new Ext.XTemplate(
			'<div style="padding: 10px;" ><ul class="stat-list-group">',	
			'<li class="stat-list-group-item">Users: <span class="stat-badge">{activeUsers}</span></li>',
			'<li class="stat-list-group-item">Reviews: <span class="stat-badge">{activeUserReviews}</span></li>',
			'<li class="stat-list-group-item">Questions: <span class="stat-badge">{activeUserQuestions}</span></li>',
			'<li class="stat-list-group-item">Question Responses: <span class="stat-badge">{activeUserQuestionResponses}</span></li>',
			'<li class="stat-list-group-item">Watches: <span class="stat-badge">{activeUserWatches}</span></li>',
			'</ul><div>'
		);

		var tplUserStatsRecentLogins = new Ext.XTemplate(
			'<ol style="padding-top: 10px;"><tpl for="recentLogins">', 
			'<li> <b>{username}<b> - {loginDts:date("m/d/Y H:i:s")}</li>',				
			'</tpl></ol>'
		);		

		statPanel.userStatsPanel = Ext.create('Ext.panel.Panel', {
			title: 'Active User Data',
			autoScroll: true,
			border: true,
			tpl: tplUserStats,								
			bodyStyle: 'background-color: white',
			width: '50%',
			margin: '0 0 0 0'			
		});
		
		statPanel.userStatsViewedPanel = Ext.create('Ext.panel.Panel', {
			title: 'Recent Logins',
			autoScroll: true,
			border: true,								
			bodyStyle: 'background-color: white',
			width: '50%',
			tpl: tplUserStatsRecentLogins			
		});

		var items = {
			xtype: 'panel',
			layout: {
				type: 'hbox',
				align: 'stretch'
			},
			items: [
				statPanel.userStatsPanel,
				statPanel.userStatsViewedPanel
			]
		}
		
		statPanel.add(items);
		statPanel.refresh();		
		
	},
	
	refresh: function() {
		var statPanel = this;
		
		statPanel.setLoading(true);
		Ext.Ajax.request({
			url: '../api/v1/service/statistic/user',
			callback: function(response, opt) {
				statPanel.setLoading(false);
			},
			success: function(response, opt){
				var data = Ext.decode(response.responseText);
				statPanel.userStatsPanel.update(data);
				statPanel.userStatsViewedPanel.update(data);						
			}
		});

	}
	
});