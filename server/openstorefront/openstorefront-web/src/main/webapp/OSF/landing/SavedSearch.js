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
/* global Ext */

Ext.define('OSF.landing.SavedSearch', {
	
	handler: function(record) {
		var searchConfig = this;		
		if (!searchConfig.view) {
			searchConfig.view = Ext.create('OSF.landing.SavedSearchView', {				
			});
		}	
		searchConfig.view.show();
	}
	
});

Ext.define('OSF.landing.SavedSearchView', {
	extend: 'Ext.window.Window',
	alias: 'widget.osf-savedsearchview',
	
	width: '70%',
	height: '70%',
	layout: 'fit',
	modal: true,
	title: 'My Searches',
	iconCls: 'fa fa-lg fa-folder',
	
	initComponent: function () {
		this.callParent();			
		var	searchWin = this;
		
		var savedSearches = Ext.create('OSF.landing.SavedSearchPanel', {
			title: '',
			iconCls: ''
		});		
		
		searchWin.add(savedSearches);
		
	}
	
	
});

