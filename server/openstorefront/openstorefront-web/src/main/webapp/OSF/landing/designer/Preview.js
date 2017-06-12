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

Ext.define('OSF.landing.designer.Preview', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.ofs-designerPreview',
	
	bodyStyle: 'border: 10px solid lightgrey !important;',
	layout: 'fit',
	items: [		
		{
			xtype: 'osf-uxiframe',
			itemId: 'content',		
			src: ''
		}
	],	
	initComponent: function () {
		this.callParent();		
		var previewPanel = this;
		
		
	},
	loadData: function (branding) {
		
	},
	updatePreview: function (code) {
		var previewPanel = this;		
		
		previewPanel.setLoading(true);
		Ext.Ajax.request({
			url: 'Landing.action?Preview',
			params: {
				landingTemplate: code
			},
			callback: function() {
				previewPanel.setLoading(false);
			},
			success: function(response, opts){
				var contentPanel = previewPanel.queryById('content');
				contentPanel.load('Landing.action?Preview');				
			}
		});
		
		
	}
	
});

