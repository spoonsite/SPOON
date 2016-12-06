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
			url: 'api/v1/service/statistic/user',
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