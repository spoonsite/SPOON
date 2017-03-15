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

/* global Ext, CoreUtil */

Ext.define('OSF.widget.Reports', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.Reports',

	layout: 'fit',
	
	initComponent: function () {
		this.callParent();
		
		var reportPanel = this;
		
		var historyGridStore = Ext.create('Ext.data.Store', {
			autoLoad: true,
			pageSize: 100,
			remoteSort: true,
			fields: [
				{
					name: 'createDts',
					type: 'date',
					dateFormat: 'c'
				}
			],
			sorters: [
				new Ext.util.Sorter({
					property: 'createDts',
					direction: 'DESC'
				})
			],
			proxy: CoreUtil.pagingProxy({
				url: 'api/v1/resource/reports',
				method: 'GET',
				reader: {
					type: 'json',
					rootProperty: 'data',
					totalProperty: 'totalNumber'
				}
			})
		});		
		
		reportPanel.grid = Ext.create('Ext.grid.Panel', {
			columnLines: true,
			store: historyGridStore,
			columns: [
				{text: 'Report Type', dataIndex: 'reportType', flex: 1, minWidth: 200, 
					renderer: function (value, meta, record) {
						return record.get('reportTypeDescription');
					}
				},			
				{text: 'Format', dataIndex: 'reportFormat', width: 250,
					renderer: function (value, meta, record) {
						return record.get('reportFormatDescription');
					}
				},				
				{text: 'Run Status', dataIndex: 'runStatus', width: 150,
					renderer: function (value, meta, record) {
						if (value === 'E') {									
							meta.tdCls = 'alert-danger';
						} else if (value === 'W') {
							meta.tdCls = 'alert-warning';
						} else {
							meta.tdCls = '';
						}
						return record.get('runStatusDescription');
					}
				},
				{text: 'Create Date', dataIndex: 'createDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s'}										
			],
			dockedItems: [
				{
					xtype: 'pagingtoolbar',
					dock: 'bottom',
					store: historyGridStore,
					displayInfo: true
				},				
				{
					dock: 'top',
					xtype: 'toolbar',
					itemId: 'tools',
					items: [
						{
							text: 'View',
							scale: 'medium',
							itemId: 'historyViewButton',
							width: '100px',
							iconCls: 'fa fa-2x fa-eye icon-vertical-correction-view icon-button-color-view',
							disabled: true,
							handler: function () {
								var grid = this.up('grid');
								var record = grid.getSelection()[0];								
								viewHistory(grid, record);
							},
							tooltip: 'Preview Report'
						},
						{
							xtype: 'tbseparator'
						},
						{
							text: 'Download',
							itemId: 'historyExportButton',
							scale: 'medium',
							iconCls: 'fa fa-2x fa-download icon-button-color-default',
							disabled: true,
							handler: function () {
								var grid = this.up('grid');
								var record = grid.getSelection()[0];
								reportPanel.exportReport(record);
							},
							tooltip: 'Export report'
						}			
					]
				}
			],
			listeners: {
				itemdblclick: function (grid, record, item, index, e, opts) {
					viewHistory(grid, record);
				},
				selectionchange: function (selectionModel, record, index, opts) {
					historyCheckNavButtons(this);							
				}				
			}
		});
		
		var viewHistory = function (grid, record) {			
			if (record.get('runStatus') === 'C') {
				reportPanel.previewReport(grid);
			}
		};		
		
		var historyCheckNavButtons = function (historyGrid) {
			var cnt = historyGrid.getSelectionModel().getCount();
			var tools  = historyGrid.getComponent('tools');
			if (cnt === 1) {
				var record = historyGrid.getSelectionModel().getSelection()[0];
				if (record.get('runStatus') !== 'C') {
					tools.getComponent('historyViewButton').setDisabled(true);
					tools.getComponent('historyExportButton').setDisabled(true);	
				} else {
					tools.getComponent('historyViewButton').setDisabled(false);
					tools.getComponent('historyExportButton').setDisabled(false);							
				}						
			} else {
				tools.getComponent('historyViewButton').setDisabled(true);
				tools.getComponent('historyExportButton').setDisabled(true);
			}
		};		
		
		reportPanel.add(reportPanel.grid);
		
	},
	refresh: function() {
		var reportPanel = this;
		reportPanel.grid.getStore().reload();
	},
	exportReport: function(record) {
		Ext.toast('Exporting Report Data...');
		window.location.href = 'api/v1/resource/reports/' + record.get('reportId') + '/report';
	},
	previewReport: function(grid) {
		var reportPanel = this;
	
		var record = grid.getSelection()[0];
	
		var previewWin = Ext.create('Ext.window.Window', {
			title: 'Report:  ' + record.get('reportTypeDescription') + ' - ' + Ext.util.Format.date(record.get('createDts'), 'm/d/y H:i:s'),			
			iconCls: 'fa fa-eye',
			width: '50%',
			height: '60%',
			y: 40,
			closeAction: 'destroy',
			modal: true,
			maximizable: true,
			scrollable: true,
			layout: 'fit',
			bodyStyle: 'padding: 10px;',			
			dockedItems: [
			{
				dock: 'bottom',
				xtype: 'toolbar',
				itemId: 'tools',
				items: [
					{
						xtype: 'button',
						text: 'Previous',
						itemId: 'previewWinTools-previousBtn',
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
						itemId: 'previewWinTools-download',
						text: 'Download',
						iconCls: 'fa fa-download',
						handler: function () {
							reportPanel.exportReport(record);
						}
					},
					{
						xtype: 'tbfill'
					},
					{
						xtype: 'button',
						text: 'Next',
						itemId: 'previewWinTools-nextBtn',
						iconCls: 'fa fa-arrow-right',
						iconAlign: 'right',
						handler: function () {
							actionPreviewNextRecord(true);
						}
					}
				]
			}]
		});
	
		
		var setHistoryContentData = function(){					
			Ext.Ajax.request({
				url: 'api/v1/resource/reports/' + record.data.reportId + '/report',
				method: 'GET',
				success: function (response, opts) {
					var reportData = response.responseText;
					var reportFormat = record.data.reportFormat;
					
					var contentData;
					if (reportFormat === 'text-html') {
						contentData = reportData;
					}
					else if (reportFormat === 'text-csv') {
						contentData = CoreUtil.csvToHTML(reportData);
					}
					else{
						contentData = reportData;
					}
					previewWin.update(contentData);
				}
			});
		};
		setHistoryContentData();

		var actionPreviewNextRecord = function (next) {
			var tools = previewWin.getComponent('tools');
			
			if (next) {
				grid.getSelectionModel().selectNext();
			} else {
				grid.getSelectionModel().selectPrevious();
			}

			record = grid.getSelectionModel().getSelection()[0]; 
			tools.getComponent('previewWinTools-download').setDisabled(true);
			var formattedDate = Ext.util.Format.date(record.get('createDts'),'m/d/y H:i:s');
			previewWin.setTitle("Report: "+record.get('reportTypeDescription') +' - '+formattedDate);

			if (record.get('runStatus') === 'C') {
				tools.getComponent('previewWinTools-download').setDisabled(false);
				setHistoryContentData();
			} else if (record.get('runStatus') === 'W') {						
				previewWin.update("Generating...");						
			} else if (record.get('runStatus') === 'E') {
				previewWin.update("Failed to generate.");						
			}
			checkPreviewButtons();
		};

		var checkPreviewButtons = function () {
			var tools = previewWin.getComponent('tools');
			
			if (grid.getSelectionModel().hasPrevious()) {
				tools.getComponent('previewWinTools-previousBtn').setDisabled(false);
			} else {
				tools.getComponent('previewWinTools-previousBtn').setDisabled(true);
			}

			if (grid.getSelectionModel().hasNext()) {
				tools.getComponent('previewWinTools-nextBtn').setDisabled(false);
			} else {
				tools.getComponent('previewWinTools-nextBtn').setDisabled(true);
			}
		};
		
		previewWin.show();
	}
	
});
