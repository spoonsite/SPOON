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
/* global Ext, CoreService */

Ext.define('OSF.landing.DefaultActions', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-defaultactions',
	
	layout: 'center',
	bodyStyle: 'padding-bottom: 40px;',
	toolSize: 'large', //small, medium, large	
	actionTools: [
		{
			text: 'Dashboard',
			//icon: 'fa-th-large',
			tip: 'Access your dashboard',
			imageSrc: 'images/dash.png',
			toolBackground: '',
			link: 'UserTool.action?load=Dashboard'
		},
		{
			text: 'Submissions',
			//icon: 'fa-file-text-o',
			imageSrc: 'images/submission.png',
			tip: 'Add or update entries to the registry',
			permission: 'USER-SUBMISSIONS-PAGE',
			toolBackground: '',
			link: 'UserTool.action?load=Submissions'
		},
		{
			text: 'My Searches',
			//icon: 'fa-share-alt',
			tip: 'View and manage your saved searches',
			imageSrc: 'images/savedsearch.png',
			toolBackground: '',
			link: 'UserTool.action?load=Searches'
		},		
		{
			text: 'Feedback',
			//icon: 'fa-comments',
			tip: 'Provide feedback about the site',
			imageSrc: 'images/feedback.png',
			toolBackground: '',
			link: 'feedback.jsp'
		}		
	],
	initComponent: function () {
		this.callParent();			
		var actionToolsPanel = this;
				
		var iconSize = 'fa-4x';
		var toolCSS = 'action-tool-button-outer-large';
		var toolCSSInner = 'action-tool-button-inner-large';
		var iconWidth = 200;
		if (actionToolsPanel.toolSize === 'small') {
			iconSize = 'fa-3x';
			toolCSS = 'action-tool-button-outer-small';
			toolCSSInner = 'action-tool-button-inner-small';
			iconWidth = 100;
		} else if (actionToolsPanel.toolSize === 'medium') {
			iconSize = 'fa-4x';
			toolCSS = 'action-tool-button-outer-medium';
			toolCSSInner = 'action-tool-button-inner-medium';
			iconWidth = 150;
		}		
			
		if (actionToolsPanel.textCSSOverride) {
			toolCSSInner = actionToolsPanel.textCSSOverride;
		}
		
		var dataView = Ext.create('Ext.DataView', {
			xtype: 'dataview',
			itemId: 'dataview',
			store: {				
			},
			itemSelector: 'div.search-tool',
			tpl: new Ext.XTemplate(
				'<div class="action-tool-header">Quick Launch</div>',	
				'<tpl for=".">',
					'<div style="margin: 15px;" class="' + toolCSS + ' {toolBackground} search-tool" data-qtip="{tip}">',
					  '<div class="' + toolCSSInner + '" >',	
						'<tpl if="imageSrc"><img src="{imageSrc}" width=' + iconWidth + '/></tpl>',	
						'<tpl if="icon"><i class="fa ' + iconSize + '  {icon}"></i></tpl>',
						'<br/><span>{text}</span>',
					  '</div>',
					'</div>',
				'</tpl>'
			),
			listeners: {
				itemclick: function(dataView, record, item, index, e, eOpts) {	
					if (record.data.link) {
						window.location.href = record.data.link;
					} else {
						Ext.log("Add link to item");
					}
				}
			}
		});		
		actionToolsPanel.add(dataView);
		
				
		//check permission
		actionToolsPanel.loadActions = function(actions) {			
			CoreService.userservice.getCurrentUser().then(function(user){
				var availableActions = [];
				Ext.Array.each(actions, function(action){
					var add = true;
					if (action.permission) {
						if (!CoreService.userservice.userHasPermission(user, action.permission)) {
							add = false;
						}
					}
					if (add) {
						availableActions.push(action);
					}
				});
				actionToolsPanel.queryById('dataview').getStore().loadData(availableActions);
			});
		};	
		actionToolsPanel.loadActions(actionToolsPanel.actionTools);
		
		
	},
	loadData: function(actions) {
		var actionToolsPanel = this;
		actionToolsPanel.actionTools = actions;
		actionToolsPanel.loadActions(actions);
	}
	
});

