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


				//
				//  USER TRACKING TAB-------------->
				//

				//
				//  User Tracking Store
				//
				var sortField='eventDts';
				var sortDirection ='DESC';
				
				
				var userTrackingGridStore = Ext.create('Ext.data.Store', {
					id: 'userTrackingGridStore',
					autoLoad: true,
					pageSize: 100,
					remoteSort: true,
					sorters: [
						new Ext.util.Sorter({
							property: 'eventDts',
							direction: 'DESC'
						})
					],
					fields: [
						{name: 'trackEventTypeCode', mapping: function (data) {

								if (data.trackEventTypeCode === 'SYNC') {
									return 'Component Sync';
								}
								else if (data.trackEventTypeCode === 'ELC') {
									return 'External Link Click';
								}
								else if (data.trackEventTypeCode === 'L') {
									return 'Login';
								}
								else if (data.trackEventTypeCode === 'V') {
									return 'View';
								}
								else {
									return data.trackEventTypeCode;
								}
							}},
						{
							name: 'eventDts',
							type:	'date',
							dateFormat: 'c'
						}
					],
					proxy: CoreUtil.pagingProxy({
						url: 'api/v1/resource/usertracking',
						method: 'GET',
						reader: {
							type: 'json',
							rootProperty: 'result',
							totalProperty: 'count'
						}
					}),
					listeners: {
						beforeLoad: function(store, operation, eOpts) {
							
							var name = Ext.getCmp('user_name').getValue();
							var startDate = Ext.getCmp('from_date').getValue();
							var endDate = Ext.getCmp('to_date').getValue();
							if (endDate) {
								endDate = Ext.Date.add(endDate, Ext.Date.DAY, 1);
								endDate = Ext.Date.subtract(endDate, Ext.Date.MILLI, 1);
							}
							store.getProxy().extraParams = {
								name: name,
								start: Ext.Date.format(startDate, 'Y-m-d\\TH:i:s.u'),
								end: Ext.Date.format(endDate, 'Y-m-d\\TH:i:s.u')
							};
						}
					}													
				});

				var userTrackingGrid = Ext.create('Ext.grid.Panel', {
					title: '',
					id: 'userTrackingGrid',
					store: userTrackingGridStore,
					columnLines: true,
					bodyCls: 'border_accent',									
					columns: [
						{text: 'Name', dataIndex: 'createUser', width: 125, flex: 1},
						{text: 'Organization', dataIndex: 'organization', width: 250},
						{text: 'User Type', dataIndex: 'userTypeCode', width: 150},
						{text: 'Event Date', dataIndex: 'eventDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s'},
						{text: 'Event Type', dataIndex: 'trackEventTypeCode', width: 250},
						{text: 'Client IP', dataIndex: 'clientIp', width: 150},
						{text: 'Browser', dataIndex: 'browser', width: 150},
						{text: 'Browser Version', dataIndex: 'browserVersion', width: 150},
						{text: 'OS', dataIndex: 'osPlatform', width: 200}
					],
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [{
									xtype: 'textfield',
									id: 'user_name',
									labelAlign: 'top',
									fieldLabel: 'Username',
									name: 'user_name',
									listeners: {
										change: {
											buffer: 1000,
											fn: function () {
													if (Ext.getCmp('user_name').getValue() !== null) {
														processUserDataFilter();
													}
												}
										}
									}
								},
								{
									xtype: 'datefield',
									id: 'from_date',
									labelAlign: 'top',
									fieldLabel: 'From',
									name: 'from_date',
									maxValue: new Date(), // limited to the current date or prior
									listeners: {
										change: function () {
											if (Ext.getCmp('to_date').getValue() !== null) {
												processUserDataFilter();
											}
										}
									}
								},
								{
									xtype: 'datefield',
									labelAlign: 'top',
									fieldLabel: 'To',
									id: 'to_date',
									name: 'to_date',
									maxValue: new Date(),
									listeners: {
										change: function () {
											if (Ext.getCmp('from_date').getValue() !== null) {
												processUserDataFilter();
											}
										}
									}
								}
							]
						},
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									id: 'userRefreshButton',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									tip: 'Refresh the list of records',
									handler: function () {
										userRefreshGrid();
									},
									tooltip: 'Refresh the list of records'
								},
								{
									xtype: 'tbseparator',
									requiredPermissions: ['ADMIN-TRACKING-READ']
								},
								{
									text: 'View',
									id: 'userViewButton',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction-view',
									width: '100px',
									disabled: true,
									requiredPermissions: ['ADMIN-TRACKING-READ'],
									handler: function () {
										userViewMessage();
									},
									tooltip: 'View the message data'
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Export',
									id: 'userExportButton',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-download icon-button-color-default',
									disabled: false,
									requiredPermissions: ['ADMIN-TRACKING-READ'],
									handler: function () {
										userExport();
									},
									tooltip: 'Export data and download to .csv format'

								}
							]
						},
						{
							xtype: 'pagingtoolbar',
							dock: 'bottom',
							store: 'userTrackingGridStore',
							displayInfo: true,
						}
					],
					listeners: {
						itemdblclick: function (grid, record, item, index, e, opts) {

							userViewMessage();
						},
						selectionchange: function (grid, record, index, opts) {
							userCheckNavButtons();
						},
						sortchange: function(thisGrid, col, dir, eOpts) {
							//console.log(col.dataIndex, col.sortState);
							sortField=col.dataIndex;
							sortDirection=col.sortState;
							    
						}
					}
				});


				//
				//   USER TRACKING FUNCTIONS
				//


				//
				// Check which buttons should be on and which should be off
				//
				var userCheckNavButtons = function () {

					if (userTrackingGrid.getSelectionModel().getCount() === 1) {

						Ext.getCmp('userViewButton').setDisabled(false);
					} else if (userTrackingGrid.getSelectionModel().getCount() > 1) {

						Ext.getCmp('userViewButton').setDisabled(true);
					} else {

						Ext.getCmp('userViewButton').setDisabled(true);
					}
				};

				//
				//  Refresh and reload the grid
				//
				var userRefreshGrid = function () {
					processUserDataFilter('refresh');
				};


				//
				// Perform Data Range Filter
				//
				var processUserDataFilter = function () {

					var name = null;
					var startDate = null;
					var endDate = null;

					name = Ext.getCmp('user_name').getValue();
					startDate = Ext.getCmp('from_date').getValue();
					endDate = Ext.getCmp('to_date').getValue();

					// Check For Name
					if (name === null ||
							typeof name === 'undefined' ||
							name === '') {
						
						// Indicate Name Is Blank
						var nameIsBlank = true;
					}
					
					// Check For Dates
					if (startDate === null ||
							endDate === null ||
							typeof startDate === 'undefined' ||
							typeof endDate === 'undefined' ||
							startDate === '' ||
							endDate === '') {
						
						// Indicate Dates Are Blank
						var datesAreBlank = true;
					}
					
					// Check If Both Name & Dates Are Blank
					if (nameIsBlank && datesAreBlank) {
						
						// Reload Grid Store
						Ext.getCmp('userTrackingGrid').getStore().load();
					}
					else if (!datesAreBlank) {
						
						// Check For Reversed Dates
						if (startDate > endDate) {
							
							// Provide Error Feedback
							Ext.toast(" 'FROM' date must be earlier than the 'TO' date.");
						}
						else {
							
							// Add 1 Day To End Date								
							endDate = Ext.Date.add(endDate, Ext.Date.DAY, 1);
							
							// Subtract 1 Millisecond From End Date
							// (Makes End Date Inclusive Of Itself)
							endDate = Ext.Date.subtract(endDate, Ext.Date.MILLI, 1);
							
							// See If Name Is Blank
							if (nameIsBlank) {
								
								// Build Store Options
								var storeOptions = {
									params: {
										
										start: Ext.Date.format(startDate, 'Y-m-d\\TH:i:s.u'),
										end: Ext.Date.format(endDate, 'Y-m-d\\TH:i:s.u')
									}
								};
							}
							else {
								
								// Build Store Options
								var storeOptions = {
									params: {
										
										name: name,
										start: Ext.Date.format(startDate, 'Y-m-d\\TH:i:s.u'),
										end: Ext.Date.format(endDate, 'Y-m-d\\TH:i:s.u')
									}
								};
							}
							
							// Indicate That Data Is Filtering
							//Ext.toast('Filtering data...');
							
							// Process Filtering
							Ext.getCmp('userTrackingGrid').getStore().loadPage(1, storeOptions);
						}
					}
					else {
						
						// Indicate That Data Is Filtering
						//Ext.toast('Filtering data...');

						Ext.getCmp('userTrackingGrid').getStore().loadPage(1, {
							params: {

								name: name
							}
						});
					}
				};

				//
				//  Create View String
				//
				var createHTMLViewString = function (recobj) {
					var myHTMLStr = '';

					myHTMLStr += '<b>Platform Information</b><br/>';
					myHTMLStr += '<ul><li><b>OS Platform: </b>' + recobj.osPlatform + '</li>';
					myHTMLStr += '<li><b>User Agent: </b>' + recobj.userAgent + '</li></ul>';

					myHTMLStr += '<b>Browser Information</b><br/>';
					myHTMLStr += '<ul><li><b>Device Type: </b>' + recobj.deviceType + '</li>';
					myHTMLStr += '<li><b>Browser: </b>' + recobj.browser + '</li>';
					myHTMLStr += '<li><b>Version: </b>' + recobj.browserVersion + '</li></ul>';

					myHTMLStr += '<b>Tracking Information</b><br/>';
					myHTMLStr += '<ul><li><b>Tracking Id: </b>' + recobj.trackingId + '</li>';
					myHTMLStr += '<li><b>Tracked Date: </b>' + recobj.eventDts + '</li></ul>';
					return myHTMLStr;

				};


				//
				//  Create and hide the view window
				//
				var createViewMessageWindow = function () {
					Ext.create('Ext.window.Window', {
						id: 'viewUserTrackingInfo',
						title: 'View Record Information',
						iconCls: 'fa fa-lg fa-eye',
						width: '30%',
						height: 400,
						autoScroll: true,
						bodyStyle: 'padding: 10px;',
						y: 40,
						modal: true,
						maximizable: false,
						layout: 'vbox',
						listeners: {
							show: function () {
								getContentForCurrentRecord();
								checkPreviewButtons();
							}
						},
						items: [
							{
								xtype: 'panel',
								id: 'viewUserTrackingDataPanel',
								width: '100%'
							}
						],
						dockedItems: [
							{
								xtype: 'toolbar',
								dock: 'bottom',
								items: [
									{
										xtype: 'button',
										text: 'Previous',
										id: 'previewWinTools-previousBtn',
										iconCls: 'fa fa-lg fa-arrow-left icon-button-color-default',
										handler: function () {
											actionPreviewNextRecord(false);
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										xtype: 'button',
										text: 'Close',
										iconCls: 'fa fa-lg fa-close icon-button-color-warning',
										handler: function () {
											this.up('window').hide();
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										xtype: 'button',
										text: 'Next',
										id: 'previewWinTools-nextBtn',
										iconCls: 'fa fa-lg fa-arrow-right icon-button-color-default',
										iconAlign: 'right',
										handler: function () {
											actionPreviewNextRecord(true);
										}
									}									
								]
							}
						]
					});
				};
				createViewMessageWindow();

				//
				// View Record
				//
				var userViewMessage = function (whichone) {
					Ext.getCmp('viewUserTrackingInfo').show();
				};

				//
				// User Export
				//
				var userExport = function () {

					var params = '';
					var name = null;
					var startDate = null;
					var endDate = null;

					name = Ext.getCmp('user_name').getValue().trim();
					startDate = Ext.getCmp('from_date').getValue();
					endDate = Ext.getCmp('to_date').getValue();

					// Check For Name
					if (name === null ||
							typeof name === 'undefined' ||
							name === '') {
						
						// Indicate Name Is Blank
						var nameIsBlank = true;
					}
					
					// Check For Dates
					if (startDate === null ||
							endDate === null ||
							typeof startDate === 'undefined' ||
							typeof endDate === 'undefined' ||
							startDate === '' ||
							endDate === '') {
						
						// Indicate Dates Are Blank
						var datesAreBlank = true;
					}
					
					// Check If Both Name & Dates Are Blank
					if (nameIsBlank && datesAreBlank) {
						
						// Set Parameters
						var params = {
						
							sortField: sortField,
							sortOrder: sortDirection
						};
					}
					else if (!datesAreBlank) {
						
						// Check For Reversed Dates
						if (startDate > endDate) {
							
							// Provide Error Feedback
							Ext.toast(" 'FROM' date must be earlier than the 'TO' date.");
							
							// Return With Shame
							return;
						}
						else {
							
							// Add 1 Day To End Date								
							endDate = Ext.Date.add(endDate, Ext.Date.DAY, 1);
							
							// Subtract 1 Millisecond From End Date
							// (Makes End Date Inclusive Of Itself)
							endDate = Ext.Date.subtract(endDate, Ext.Date.MILLI, 1);
							
							// See If Name Is Blank
							if (nameIsBlank) {
								
								// Set Parameters
								var params = {
								
									start: Ext.Date.format(startDate, 'Y-m-d\\TH:i:s.u'),
									end: Ext.Date.format(endDate, 'Y-m-d\\TH:i:s.u'),
									sortField: sortField,
									sortOrder: sortDirection
								};
							}
							else {
								
								// Set Parameters
								var params = {
								
									name: name,
									start: Ext.Date.format(startDate, 'Y-m-d\\TH:i:s.u'),
									end: Ext.Date.format(endDate, 'Y-m-d\\TH:i:s.u'),
									sortField: sortField,
									sortOrder: sortDirection
								};
							}
						}
					}
					else {
						
						// Indicate That Data Is Filtering
						//Ext.toast('Filtering data...');

						// Set Parameters
						var params = {
						
							name: name,
							sortField: sortField,
							sortOrder: sortDirection
						};
					}
					
					// Indicate To User That Export Is Occurring
					Ext.toast('Exporting User Tracking Data ...');
					
					// Process Export
					Ext.Ajax.request({
						url: 'api/v1/resource/usertracking/export',
						method: 'GET',
						params: params,
						success: function (response, opts) {

							CoreUtil.downloadCSVFile('userTracking.csv', response.responseText);
						}
					});
				};

				//
				//  Record Preview methods
				//
				var getContentForCurrentRecord = function () {
					var selectedObj = Ext.getCmp('userTrackingGrid').getSelection()[0].data;
					Ext.getCmp('viewUserTrackingDataPanel').update(createHTMLViewString(selectedObj));
				};

				var actionPreviewNextRecord = function (next) {
					if (next) {
						Ext.getCmp('userTrackingGrid').getSelectionModel().selectNext();
					} else {
						Ext.getCmp('userTrackingGrid').getSelectionModel().selectPrevious();
					}

					getContentForCurrentRecord();
					checkPreviewButtons();

				};

				var checkPreviewButtons = function () {
					if (Ext.getCmp('userTrackingGrid').getSelectionModel().hasPrevious()) {
						Ext.getCmp('previewWinTools-previousBtn').setDisabled(false);
					} else {
						Ext.getCmp('previewWinTools-previousBtn').setDisabled(true);
					}

					if (Ext.getCmp('userTrackingGrid').getSelectionModel().hasNext()) {
						Ext.getCmp('previewWinTools-nextBtn').setDisabled(false);
					} else {
						Ext.getCmp('previewWinTools-nextBtn').setDisabled(true);
					}
				};

				//
				// ENTRY TRACKING TAB PANEL-------------->
				//

				//
				// Entry Tracking Store
				//

                var eSortField='eventDts';
				var eSortDirection ='DESC';
				
				var entryTrackingGridStore = Ext.create('Ext.data.Store', {
					id: 'entryTrackingGridStore',
					autoLoad: false,
					pageSize: 100,
					remoteSort: true,
					sorters: [
						new Ext.util.Sorter({
							id: 'entrySorter',
							property: 'eventDts',
							direction: 'DESC'
						})
					],
					fields: [
						{name: 'componentId', mapping: function (data) {
								return data.data.componentId;
							}},
						{name: 'componentType', mapping: function (data) {
								return data.data.componentType;
							}},
						{name: 'eventDts', 
							type:	'date',
							dateFormat: 'c',
							mapping: function (data) {
								return data.data.eventDts;
							}},
						{name: 'trackEventTypeCode', mapping: function (data) {
								if (data.data.trackEventTypeCode === 'SYNC') {
									return 'Component Sync';
								}
								else if (data.data.trackEventTypeCode === 'ELC') {
									return 'External Link Click';
								}
								else if (data.data.trackEventTypeCode === 'L') {
									return 'Login';
								}
								else if (data.data.trackEventTypeCode === 'V') {
									return 'View';
								}
								else {
									return data.data.trackEventTypeCode;
								}
							}},
						{name: 'activeStatus', mapping: function (data) {
								return data.data.activeStatus;
							}},
						{name: 'updateDts', mapping: function (data) {
								return data.data.updateDts;
							}},
						{name: 'updateUser', mapping: function (data) {
								return data.data.updateUser;
							}},
						{name: 'createDts', mapping: function (data) {
								return data.data.createDts;
							}},
						{name: 'createUser', mapping: function (data) {
								return data.data.createUser;
							}},
						{name: 'clientIp', mapping: function (data) {
								return data.data.clientIp;
							}}
					],
					proxy: CoreUtil.pagingProxy({
						url: 'api/v1/resource/componenttracking',
						reader: {
							type: 'json',
							rootProperty: 'result',
							totalProperty: 'count'
						}
					}),
					listeners: {
						beforeLoad: function(store, operation, eOpts){
							
							var name = Ext.getCmp('entry_user').getValue();
							var startDate = Ext.getCmp('from_entry_date').getValue();
							var endDate = Ext.getCmp('to_entry_date').getValue();
							if (endDate) {
								endDate = Ext.Date.add(endDate, Ext.Date.DAY, 1);
								endDate = Ext.Date.subtract(endDate, Ext.Date.MILLI, 1);
							}
							store.getProxy().extraParams = {
								
								name: name,
								start: Ext.Date.format(startDate, 'Y-m-d\\TH:i:s.u'),
								end: Ext.Date.format(endDate, 'Y-m-d\\TH:i:s.u')
							};
						}
					}
				});


				//
				//  Entry Tracking Grid Panel
				//

				var entryTrackingGrid = Ext.create('Ext.grid.Panel', {
					title: '',
					id: 'entryTrackingGrid',
					store: entryTrackingGridStore,
					columnLines: true,
					bodyCls: 'border_accent',					
					listeners:{
						sortchange: function(thisGrid, col, dir, eOpts) {
							//console.log(col.dataIndex, col.sortState);
							eSortField=col.dataIndex;
							eSortDirection=col.sortState;
							    
						}
					},
					columns: [
						{text: 'Name', dataIndex: 'name', width: 125, flex: 1 },
						{text: 'Entry Type', dataIndex: 'componentType', width: 200},
						{text: 'Event Date', dataIndex: 'eventDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s'},
						{text: 'Event Type', dataIndex: 'trackEventTypeCode', width: 150},
						{text: 'Client IP', dataIndex: 'clientIp', width: 125},
						{text: 'User', dataIndex: 'createUser', width: 150}
					],
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [
								{
									xtype: 'textfield',
									id: 'entry_user',
									labelAlign: 'top',
									fieldLabel: 'User',
									name: 'entry_user',
									listeners: {
										change: {
											buffer: 1000,
											fn: function () {
													if (Ext.getCmp('entry_user').getValue() !== null) {
														processEntryDataFilter();
													}
												}
										}
									}
								},						
								{
									xtype: 'textfield',
									id: 'filter_component_name',
									labelAlign: 'top',
									fieldLabel: 'Entry Name',
									name: 'componentName',
									listeners: {
										change: {
											buffer: 1000,
											fn: function () {
													if (Ext.getCmp('filter_component_name').getValue() !== null) {
														processEntryDataFilter();
													}
												}
										}
									}
								},								
								{
									xtype: 'datefield',
									id: 'from_entry_date',
									labelAlign: 'top',
									fieldLabel: 'From',
									name: 'from_entry_date',
									maxValue: new Date(), // limited to the current date or prior
									listeners: {
										change: function () {
											if (Ext.getCmp('to_entry_date').getValue() !== null) {
												processEntryDataFilter();
											}
										}
									}
								},
								{
									xtype: 'datefield',
									labelAlign: 'top',
									fieldLabel: 'To',
									id: 'to_entry_date',
									name: 'to_entry_date',
									maxValue: new Date(),
									listeners: {
										change: function () {
											if (Ext.getCmp('from_entry_date').getValue() !== null) {
												processEntryDataFilter();
											}
										}
									}
								}
							]
						},
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									id: 'entryRefreshButton',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										entryRefreshGrid();
									},
									tooltip: 'Refresh the list of records'
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Export',
									id: 'entryExportButton',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-download icon-button-color-default',
									disabled: false,
									requiredPermissions: ['ADMIN-TRACKING-READ'],
									handler: function () {
										entryExport();
									},
									tooltip: 'Export data and download to .csv format'
								}
							]
						},
						{
							xtype: 'pagingtoolbar',
							dock: 'bottom',
							store: 'entryTrackingGridStore',
							displayInfo: true,
						}
					]
				});


				//
				//   ENTRY TRACKING FUNCTIONS
				//


				//
				//  Refresh and reload the grid
				//
				var entryRefreshGrid = function () {
					processEntryDataFilter();
				};
				//
				// Do Data Range Filter
				//
				var processEntryDataFilter = function () {

					var name = null;
					var startDate = null;
					var endDate = null;

					name = Ext.getCmp('entry_user').getValue();
					startDate = Ext.getCmp('from_entry_date').getValue();
					endDate = Ext.getCmp('to_entry_date').getValue();
					var componentName = Ext.getCmp('filter_component_name').getValue();

					// Check For Name
					if (name === null ||
							typeof name === 'undefined' ||
							name === '') {
						
						// Indicate Name Is Blank
						var nameIsBlank = true;
					}
					
					// Check For Dates
					if (startDate === null ||
							endDate === null ||
							typeof startDate === 'undefined' ||
							typeof endDate === 'undefined' ||
							startDate === '' ||
							endDate === '') {
						
						// Indicate Dates Are Blank
						var datesAreBlank = true;
					}
					
					// Check If Both Name & Dates Are Blank
					if (nameIsBlank && datesAreBlank) {
						
						// Reload Grid Store
						Ext.getCmp('entryTrackingGrid').getStore().loadPage(1, {
							params: {
								componentName: componentName
							}
						});
					}
					else if (!datesAreBlank) {
						
						// Check For Reversed Dates
						if (startDate > endDate) {
							
							// Provide Error Feedback
							Ext.toast(" 'FROM' date must be earlier than the 'TO' date.");
						}
						else {
							
							// Add 1 Day To End Date								
							endDate = Ext.Date.add(endDate, Ext.Date.DAY, 1);
							
							// Subtract 1 Millisecond From End Date
							// (Makes End Date Inclusive Of Itself)
							endDate = Ext.Date.subtract(endDate, Ext.Date.MILLI, 1);
							
							// See If Name Is Blank
							if (nameIsBlank) {
								
								// Build Store Options
								var storeOptions = {
									params: {
										componentName: componentName,
										start: Ext.Date.format(startDate, 'Y-m-d\\TH:i:s.u'),
										end: Ext.Date.format(endDate, 'Y-m-d\\TH:i:s.u')
									}
								};
							}
							else {
								
								// Build Store Options
								var storeOptions = {
									params: {
										componentName: componentName,
										name: name,
										start: Ext.Date.format(startDate, 'Y-m-d\\TH:i:s.u'),
										end: Ext.Date.format(endDate, 'Y-m-d\\TH:i:s.u')
									}
								};
							}
							
							// Indicate That Data Is Filtering
							//Ext.toast('Filtering data...');
							
							// Process Filtering
							Ext.getCmp('entryTrackingGrid').getStore().loadPage(1, storeOptions);
						}
					}
					else {
						
						// Indicate That Data Is Filtering
						//Ext.toast('Filtering data...');

						Ext.getCmp('entryTrackingGrid').getStore().loadPage(1, {
							params: {
								componentName: componentName,
								name: name
							}
						});
					}
				};


				//
				// Entry Export
				//
				//
				// User Export
				//
				var entryExport = function () {

					var params = '';
					var name = null;
					var startDate = null;
					var endDate = null;

					name = Ext.getCmp('entry_user').getValue().trim();
					startDate = Ext.getCmp('from_entry_date').getValue();
					endDate = Ext.getCmp('to_entry_date').getValue();

					// Check For Name
					if (name === null ||
							typeof name === 'undefined' ||
							name === '') {
						
						// Indicate Name Is Blank
						var nameIsBlank = true;
					}
					
					// Check For Dates
					if (startDate === null ||
							endDate === null ||
							typeof startDate === 'undefined' ||
							typeof endDate === 'undefined' ||
							startDate === '' ||
							endDate === '') {
						
						// Indicate Dates Are Blank
						var datesAreBlank = true;
					}
					
					// Check If Both Name & Dates Are Blank
					if (nameIsBlank && datesAreBlank) {
						
						// Set Parameters
						var params = {
						
							sortField: eSortField,
							sortOrder: eSortDirection
						};
					}
					else if (!datesAreBlank) {
						
						// Check For Reversed Dates
						if (startDate > endDate) {
							
							// Provide Error Feedback
							Ext.toast(" 'FROM' date must be earlier than the 'TO' date.");
							
							// Return With Shame
							return;
						}
						else {
							
							// Add 1 Day To End Date								
							endDate = Ext.Date.add(endDate, Ext.Date.DAY, 1);
							
							// Subtract 1 Millisecond From End Date
							// (Makes End Date Inclusive Of Itself)
							endDate = Ext.Date.subtract(endDate, Ext.Date.MILLI, 1);
							
							// See If Name Is Blank
							if (nameIsBlank) {
								
								// Set Parameters
								var params = {
								
									start: Ext.Date.format(startDate, 'Y-m-d\\TH:i:s.u'),
									end: Ext.Date.format(endDate, 'Y-m-d\\TH:i:s.u'),
									sortField: eSortField,
									sortOrder: eSortDirection
								};
							}
							else {
								
								// Set Parameters
								var params = {
								
									name: name,
									start: Ext.Date.format(startDate, 'Y-m-d\\TH:i:s.u'),
									end: Ext.Date.format(endDate, 'Y-m-d\\TH:i:s.u'),
									sortField: eSortField,
									sortOrder: eSortDirection
								};
							}
						}
					}
					else {
						
						// Indicate That Data Is Filtering
						//Ext.toast('Filtering data...');

						// Set Parameters
						var params = {
						
							name: name,
							sortField: eSortField,
							sortOrder: eSortDirection
						};
					}
					
					// Indicate To User That Export Is Occurring
					Ext.toast('Exporting Entry Tracking Data ...');
					
					// Process Export
					Ext.Ajax.request({
						url: 'api/v1/resource/componenttracking/export',
						method: 'GET',
						params: params,
						success: function (response, opts) {

							CoreUtil.downloadCSVFile('entryTracking.csv', response.responseText);
						}
					});
				};

				//
				//  CREATE PANELS
				//

				var userPanel = Ext.create('Ext.panel.Panel', {
					title: 'User',
					iconCls: 'fa fa-lg fa-user',
					layout: 'fit',
					items: [
						userTrackingGrid
					]
				});

				var entryPanel = Ext.create('Ext.panel.Panel', {
					title: 'Entry',
					iconCls: 'fa fa-lg fa-book',
					layout: 'fit',
					items: [
						entryTrackingGrid
					]
				});

				var msgTabPanel = Ext.create('Ext.tab.Panel', {
					title: 'Manage Tracking <i class="fa fa-lg fa-question-circle"  data-qtip="Track user, entry, and article data." ></i>',
					layout: 'fit',
					items: [
						userPanel,
						entryPanel

					],
					listeners: {
						tabchange: function (tabPanel, newTab, oldTab, index) {
							if (newTab.title === 'User') {
								if (!Ext.getCmp('userTrackingGrid').getStore().isLoaded()) {
									userRefreshGrid();
								}
							}
							else if (newTab.title === 'Entry') {
								if (!Ext.getCmp('entryTrackingGrid').getStore().isLoaded()) {
									entryRefreshGrid();
								}
							}

						}
					}
				});

				addComponentToMainViewPort(msgTabPanel);
				
			});

        </script>

    </stripes:layout-component>
</stripes:layout-render>
