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

/* global Ext, CoreUtil */

Ext.define('OSF.landing.AdvancedSearch', {
	
	handler: function(record) {
		var searchConfig = this;		
		if (!searchConfig.view) {
			searchConfig.view = Ext.create('OSF.landing.AdvancedSearchView', {				
			});
		}	
		searchConfig.view.show();
	}
	
});

Ext.define('OSF.landing.AdvancedSearchView', {
	extend: 'Ext.window.Window',
	alias: 'widget.osf-advancedsearchview',
	
	
	width: '70%',
	height: '70%',
	layout: 'fit',
	modal: true,
	title: 'Advanced Search',
	iconCls: 'fa fa-lg fa-search-plus',
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'bottom',
			items: [
				{
					text: 'Search',
					scale: 'medium',
					iconCls: 'fa fa-2x fa-search icon-button-color-refresh icon-vertical-correction',
					width: '110px',
					handler: function () {
						var searchObj = this.up('window').advanceSearch.getSearch();

						if (searchObj) {
							var win = this.up('window');
							var searchRequest = {
								type: 'Advance',
								query: searchObj
							};
							CoreUtil.sessionStorage().setItem('searchRequest', Ext.encode(searchRequest));
							window.location.href = 'searchResults.jsp';

							//close window
							win.close();
						}
					}
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Preview Results',
					scale: 'medium',
					iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction-view',
					width: '170px',
					handler: function () {
						this.up('window').advanceSearch.previewResults();
					}
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Save Search',
					scale: 'medium',
					iconCls: 'fa fa-2x fa-save icon-button-color-save icon-vertical-correction',
					width: '150px',
					handler: function () {
						this.up('window').advanceSearch.saveSearch();
					}
				}
			]
		}
	],
			
	initComponent: function () {
		this.callParent();			
		var	searchWin = this;
		
		searchWin.advanceSearch = Ext.create('OSF.component.AdvancedSearchPanel', {				
		});
		
		searchWin.add(searchWin.advanceSearch);
	}
	
});
