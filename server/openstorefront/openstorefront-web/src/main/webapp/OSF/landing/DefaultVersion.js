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

Ext.define('OSF.landing.DefaultVersion', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-defaultversion',
	
	width: '100%',
	layout: 'center',
	bodyCls: 'home-footer',	
	items: [
		{
			itemId: 'display',
			tpl: '<div class="home-footer-version">{applicationVersion}</div>'
		}
	],
	initComponent: function () {
		this.callParent();			
		var versionPanel = this;
				
		Ext.Ajax.request({
			url: 'api/v1/service/application/version',
			success: function(response, opts) {
				var version = response.responseText;
				console.log(versionPanel);
				versionPanel.getComponent('display').update({
					applicationVersion: version
				});
			}
		});				
	}	
	
});

