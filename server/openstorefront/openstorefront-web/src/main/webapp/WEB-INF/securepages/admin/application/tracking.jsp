<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
    <stripes:layout-component name="contents">

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
							}}
					],
					proxy: CoreUtil.pagingProxy({
						url: '../api/v1/resource/usertracking',
						method: 'GET',
						reader: {
							type: 'json',
							rootProperty: 'result',
							totalProperty: 'count'
						}
					})
				});

				var userTrackingGrid = Ext.create('Ext.grid.Panel', {
					title: '',
					id: 'userTrackingGrid',
					store: userTrackingGridStore,
					columnLines: true,
					bodyCls: 'border_accent',
					plugins: 'gridfilters',
					enableLocking: true,
					columns: [
						{text: 'Name', dataIndex: 'createUser', width: 125, flex: 1, lockable: true},
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
									xtype: 'datefield',
									id: 'from_date',
									labelAlign: 'top',
									fieldLabel: 'From',
									name: 'from_date',
									maxValue: new Date(), // limited to the current date or prior
									listeners: {
										change: function () {
											if (Ext.getCmp('to_date').getValue() !== null) {
												processUserDateFilter();
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
												processUserDateFilter();
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
									iconCls: 'fa fa-2x fa-refresh',
									tip: 'Refresh the list of records',
									handler: function () {
										userRefreshGrid();
									},
									tooltip: 'Refresh the list of records'
								},
								{
									text: 'View',
									id: 'userViewButton',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-eye',
									disabled: true,
									handler: function () {
										userViewMessage();
									},
									tooltip: 'View the message data',
								},
								{
									xtype: 'tbfill'
								}]
						},
						{
							xtype: 'pagingtoolbar',
							dock: 'bottom',
							store: 'userTrackingGridStore',
							displayInfo: true,
							items: [
								{
									text: 'Export',
									id: 'userExportButton',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-download',
									disabled: false,
									handler: function () {
										userExport();
									},
									tooltip: 'Export data and download to .csv format'

								}
							]
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
					processUserDateFilter('refresh');
				};


				//
				// Perform Data Range Filter
				//
				var processUserDateFilter = function () {

					var startDate = null;
					var endDate = null;

					startDate = Ext.getCmp('from_date').getValue();
					endDate = Ext.getCmp('to_date').getValue();

					if (startDate === null ||
							endDate === null ||
							typeof startDate === 'undefined' ||
							typeof endDate === 'undefined' ||
							startDate === '' ||
							endDate === '')
					{
					    Ext.getCmp('userTrackingGrid').getStore().load();
					}
					else if (startDate > endDate) {
						Ext.toast(" 'FROM' date must be earlier than the 'TO' date.");
					}
					else {
						//Ext.toast('Filtering data by date range...');
						Ext.getCmp('userTrackingGrid').getStore().loadPage(1, {
							params: {
								start: Ext.Date.format(startDate, 'Y-m-d\\TH:i:s.u'),
								end: Ext.Date.format(endDate, 'Y-m-d\\TH:i:s.u')

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
						iconCls: 'fa fa-info-circle',
						width: '30%',
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
							},
							{
								xtype: 'tbfill',
								bodyStyle: 'background-color:000;'
							},
							{
								xtype: 'panel',
								width: '100%',
								layout: 'hbox',
								items: [
									{
										xtype: 'button',
										text: 'Previous',
										id: 'previewWinTools-previousBtn',
										iconCls: 'fa fa-arrow-left',
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
										iconCls: 'fa fa-close',
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
										iconCls: 'fa fa-arrow-right',
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
					
					var params='';
					var startDate = null;
					var endDate = null;

					startDate = Ext.getCmp('from_date').getValue();
					endDate = Ext.getCmp('to_date').getValue();

					if (startDate === null ||
							endDate === null ||
							typeof startDate === 'undefined' ||
							typeof endDate === 'undefined' ||
							startDate === '' ||
							endDate === '')
					{
					    params = {
							sortField: sortField,
							sortOrder: sortDirection
						};
					}
					else if (startDate > endDate) {
						Ext.toast(" 'FROM' date must be earlier than the 'TO' date.");
						return;
					}
					else {
						params={
						    start: Ext.Date.format(startDate, 'Y-m-d\\TH:i:s.u'),
						    end: Ext.Date.format(endDate, 'Y-m-d\\TH:i:s.u'),
							sortField: sortField,
							sortOrder: sortDirection
						};
					}
					
					console.log("Grid Sorting", Ext.getCmp('userTrackingGrid').sortInfo);
					
					Ext.toast('Exporting User Tracking Data ...');
					Ext.Ajax.request({
						url: '../api/v1/resource/usertracking/export',
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
						{name: 'eventDts', mapping: function (data) {
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
						url: '../api/v1/resource/componenttracking',
						reader: {
							type: 'json',
							rootProperty: 'result',
							totalProperty: 'count'
						}
					})
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
					plugins: 'gridfilters',
					enableLocking: true,
					listeners:{
						sortchange: function(thisGrid, col, dir, eOpts) {
							//console.log(col.dataIndex, col.sortState);
							eSortField=col.dataIndex;
							eSortDirection=col.sortState;
							    
						}
					},
					columns: [
						{text: 'Name', dataIndex: 'name', width: 125, flex: 1, lockable: true},
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
							items: [{
									xtype: 'datefield',
									id: 'from_entry_date',
									labelAlign: 'top',
									fieldLabel: 'From',
									name: 'from_entry_date',
									maxValue: new Date(), // limited to the current date or prior
									listeners: {
										change: function () {
											if (Ext.getCmp('to_entry_date').getValue() !== null) {
												processEntryDateFilter();
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
												processEntryDateFilter();
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
									iconCls: 'fa fa-2x fa-refresh',
									handler: function () {
										entryRefreshGrid();
									},
									tooltip: 'Refresh the list of records'
								},
								{
									xtype: 'tbfill'
								}]
						},
						{
							xtype: 'pagingtoolbar',
							dock: 'bottom',
							store: 'entryTrackingGridStore',
							displayInfo: true,
							items: [
								{
									text: 'Export',
									id: 'entryExportButton',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-download',
									disabled: false,
									handler: function () {
										entryExport();
									},
									tooltip: 'Export data and download to .csv format'
								}
							]
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
					processEntryDateFilter();
				};
				//
				// Do Data Range Filter
				//
				var processEntryDateFilter = function () {

					var startDate = null;
					var endDate = null;

					startDate = Ext.getCmp('from_entry_date').getValue();
					endDate = Ext.getCmp('to_entry_date').getValue();

					if (startDate === null ||
							endDate === null ||
							typeof startDate === 'undefined' ||
							typeof endDate === 'undefined' ||
							startDate === '' ||
							endDate === '')
					{
						Ext.getCmp('entryTrackingGrid').getStore().load();
					}
					else if (startDate > endDate) {
						Ext.toast(" 'FROM' date must be earlier than the 'TO' date.");
					}
					else {
						Ext.getCmp('entryTrackingGrid').getStore().loadPage(1, {
							params: {
								start: Ext.Date.format(startDate, 'Y-m-d\\TH:i:s.u'),
								end: Ext.Date.format(endDate, 'Y-m-d\\TH:i:s.u')

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
					
					var params='';
					var startDate = null;
					var endDate = null;

					startDate = Ext.getCmp('from_entry_date').getValue();
					endDate = Ext.getCmp('to_entry_date').getValue();

					if (startDate === null ||
							endDate === null ||
							typeof startDate === 'undefined' ||
							typeof endDate === 'undefined' ||
							startDate === '' ||
							endDate === '')
					{
					    params = {
							sortField: eSortField,
							sortOrder: eSortDirection
						};
					}
					else if (startDate > endDate) {
						Ext.toast(" 'FROM' date must be earlier than the 'TO' date.");
						return;
					}
					else {
						params={
						    start: Ext.Date.format(startDate, 'Y-m-d\\TH:i:s.u'),
						    end: Ext.Date.format(endDate, 'Y-m-d\\TH:i:s.u'),
							sortField: eSortField,
							sortOrder: eSortDirection
						};
					}
					
					console.log("Grid Sorting", Ext.getCmp('userTrackingGrid').sortInfo);
					
					Ext.toast('Exporting Entry Tracking Data ...');
					Ext.Ajax.request({
						url: '../api/v1/resource/componenttracking/export',
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
					iconCls: 'fa fa-user',
					layout: 'fit',
					items: [
						userTrackingGrid
					]
				});

				var entryPanel = Ext.create('Ext.panel.Panel', {
					title: 'Entry',
					iconCls: 'fa fa-book',
					layout: 'fit',
					items: [
						entryTrackingGrid
					]
				});

				var msgTabPanel = Ext.create('Ext.tab.Panel', {
					title: 'Manage Tracking <i class="fa fa-question-circle"  data-qtip="Track user, entry, and article data." ></i>',
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

				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [
						msgTabPanel
					]
				});
			});

        </script>

    </stripes:layout-component>
</stripes:layout-render>
