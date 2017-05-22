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

Ext.define('OSF.landing.SavedSearchPanel', {
	extend: 'Ext.grid.Panel',
	alias: 'widget.osf-savedsearchpanel',
	
	title: 'Saved Searches',
	iconCls: 'fa fa-lg fa-save icon-small-vertical-correction',
	columnLines: true,
	store: {
		autoLoad: true,
		proxy: {
			type: 'ajax',
			url: 'api/v1/resource/usersavedsearches/user/current'
		}
	},
	columns: [
		{text: 'Select Search', dataIndex: 'searchName', flex: 1, minWidth: 200,
			renderer: function (value, meta) {
				meta.tdStyle = 'font-size: 16px';
				return value;
			}
		}
	],
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'top',
			items: [
				{
					xtype: 'tbfill'
				},
				{
					text: 'Manage Searches',
					iconCls: 'fa fa-lg fa-gear icon-button-color-default',
					href: 'UserTool.action?load=Searches',
					hrefTarget: ''
				}
			]
		},
		{
			xtype: 'toolbar',
			itemId: 'btools',
			dock: 'bottom',
			items: [
				{
					text: 'Search',
					scale: 'medium',
					iconCls: 'fa fa-2x fa-search icon-button-color-refresh icon-vertical-correction',
					width: '110px',
					itemId: 'search',
					disabled: true,
					handler: function () {

						var grid = this.up('grid');
						var record = grid.getSelectionModel().getSelection()[0];
						grid.searchAction(record);
					}
				}
			]
		}
	],	
	
	initComponent: function () {
		this.callParent();

		var searchPanel = this;			
		
		searchPanel.searchAction = function(record) {
			var searchObj = Ext.decode(record.get('searchRequest'));

			if (searchObj) {
				var searchRequest = {
					type: 'Advance',
					query: searchObj
				};
				CoreUtil.sessionStorage().setItem('searchRequest', Ext.encode(searchRequest));
				window.location.href = 'searchResults.jsp';

				if (searchPanel.searchCallback) {
					searchPanel.searchCallback();
				}
			}			
		};
		
		searchPanel.on('selectionchange', function (selModel, selected, opts) {
			var tools = searchPanel.getComponent('btools');
			if (selModel.getCount() >= 1) {
				tools.getComponent('search').setDisabled(false);
			} else {
				tools.getComponent('search').setDisabled(true);
			}			
		});	
		
		searchPanel.on('itemdblclick', function (view, record, item, index, e, opts) {
			searchPanel.searchAction(record);			
		});
		
	}	
	
});
