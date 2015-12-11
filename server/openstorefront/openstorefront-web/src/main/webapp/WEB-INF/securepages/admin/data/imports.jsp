<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
    <stripes:layout-component name="contents">
	
	<script src="scripts/component/importWindow.js" type="text/javascript"></script>	
		
	<script type="text/javascript">
		/* global Ext, CoreUtil */
		Ext.onReady(function(){	
			
			var importWindow = Ext.create('OSF.component.ImportWindow', {					
				fileTypeReadyOnly: false
			});
			
			var mappingPanel = Ext.create('Ext.panel.Panel', {
				title: 'Mapping'
			});
			
			var fileHistoryStore = Ext.create('Ext.data.Store', {
				pageSize: 100,
				remoteSort: true,	
				sorters: [
					new Ext.util.Sorter({
						property : 'updateDts',
						direction: 'DESC'
					})
				],
				fields: [
					"fileHistoryId",
					"fileFormatDescription",
					"fileTypeDescription",
					"createDts",
					"createUser",
					"startDts",
					"completeDts",
					"mimeType",
					"originalFilename",
					"dataSource",
					"warningsCount",
					"errorsCount",
					"fileFormat",
					"numberRecords",
					"recordsProcessed",					
					{ name: "progress", mapping: function(data){
						return (data.recordsProcessed / data.numberRecords);
					}},
					"recordsStored"
				],
				autoLoad: true,
				proxy: CoreUtil.pagingProxy({
					url: '../api/v1/resource/filehistory',					
					reader: {
					   type: 'json',
					   rootProperty: 'data',
					   totalProperty: 'totalNumber'
					}
				})				
			});
			
			var fileHistoryGrid = Ext.create('Ext.grid.Panel', {
				id: 'fileHistoryGrid',
				title: 'Import History',
				columnLines: true,
				store: fileHistoryStore,
				bodyCls: 'border_accent',
				columns: [
					{ text: 'File Type', dataIndex: 'fileTypeDescription', width: 150 },
					{ text: 'File Format', dataIndex: 'fileFormatDescription', width: 200 },
					{
						text: 'Dates',
						columns: [
							{ text: 'Upload', align: 'center', dataIndex: 'createDts', width: 175, xtype: 'datecolumn', format:'m/d/y H:i:s'  },
							{ text: 'Start', align: 'center', dataIndex: 'startDts', width: 175, xtype: 'datecolumn', format:'m/d/y H:i:s'  },
							{ text: 'Complete', align: 'center', dataIndex: 'completeDts', width: 175, xtype: 'datecolumn', format:'m/d/y H:i:s' }												
						]
					},
					{ text: 'Filename', dataIndex: 'originalFilename', width: 200 },
					{ text: 'Warning/Errors', align: 'center', dataIndex: 'warningsCount', width: 175,
						renderer: function(value, metaData, record) {
							if (record.get('errorsCount') > 0) {
								metaData.tdCls = 'alert-danger';
							} else if (record.get('warningsCount') > 0) {
								metaData.tdCls = 'alert-warning';
							}
							return record.get('warningsCount') + ' / ' + record.get('errorsCount');  
						}
					},
					{ text: 'Data Source', dataIndex: 'dataSource', width: 200, hidden: true },
					{ text: 'Mime Type', dataIndex: 'mimeType', width: 175, hidden: true },
					{
						text: 'Record Stats',
						columns: [
							{ text: 'Stored', align: 'center', dataIndex: 'recordsStored', width: 100 },
							{ text: 'Processed', align: 'center', dataIndex: 'recordsProcessed', width: 100 },
							{ text: 'Total', align: 'center', dataIndex: 'numberRecords', width: 100 }
						]
					},
					{ text: 'Progress', dataIndex: 'progress', flex: 1, minWidth: 175,
					   xtype: 'widgetcolumn',
					   widget: {
						xtype: 'progressbarwidget',
						textTpl: '{value:percent}'
					   }
					}
				],
				dockedItems: [
					{
						xtype: 'toolbar',
						itemId: 'mainToolbar',
						dock: 'top',
						items: [
							{
								text: 'Refresh',								
								scale: 'medium',								
								iconCls: 'fa fa-2x fa-refresh',
								handler: function () {
									this.up('grid').getStore().reload();
								}
							},
							{
								xtype: 'tbseparator'
							},
							{
								text: 'View Details',
								itemId: 'viewDetailsBtn',
								scale: 'medium',								
								iconCls: 'fa fa-2x fa-binoculars',
								disabled: true,
								handler: function () {
									actionViewDetails(Ext.getCmp('fileHistoryGrid').getSelectionModel().getSelection()[0]);
								}
							},
							{
								xtype: 'tbseparator'
							},							
							{					
								text: 'Import',								
								scale: 'medium',								
								iconCls: 'fa fa-2x fa-upload',
								handler: function () {
									importWindow.show();
								}
							},							
							{
								xtype: 'tbfill'
							},
							{
								text: 'Reprocess',
								itemId: 'reprocessBtn',
								scale: 'medium',								
								iconCls: 'fa fa-2x fa-reply',
								disabled: true,
								handler: function () {
									actionReprocess(Ext.getCmp('fileHistoryGrid').getSelectionModel().getSelection()[0]);
								}
							},
							{
								text: 'Rollback',
								itemId: 'rollbackBtn',
								scale: 'medium',								
								iconCls: 'fa fa-2x fa-close',
								disabled: true,
								handler: function () {
									actionRollback(Ext.getCmp('fileHistoryGrid').getSelectionModel().getSelection()[0]);
								}
							}							
						]
					},
					Ext.create('Ext.PagingToolbar', {
						store: fileHistoryStore,
						dock: 'bottom',
						displayInfo: true,
						displayMsg: 'Displaying Record {0} - {1} of {2}',
						emptyMsg: "No records to display"
					})
				],
				listeners: {
					itemdblclick: function(grid, record, item, index, e, opts){
						actionViewDetails(record);
					},
					selectionchange: function(selectionModel, selected, opt){
						
						if (selectionModel.getCount() === 1) {
							Ext.getCmp('fileHistoryGrid').getComponent('mainToolbar').getComponent('viewDetailsBtn').setDisabled(false);
							Ext.getCmp('fileHistoryGrid').getComponent('mainToolbar').getComponent('reprocessBtn').setDisabled(false);
							Ext.getCmp('fileHistoryGrid').getComponent('mainToolbar').getComponent('rollbackBtn').setDisabled(false);							
						} else {
							Ext.getCmp('fileHistoryGrid').getComponent('mainToolbar').getComponent('viewDetailsBtn').setDisabled(true);
							Ext.getCmp('fileHistoryGrid').getComponent('mainToolbar').getComponent('reprocessBtn').setDisabled(true);
							Ext.getCmp('fileHistoryGrid').getComponent('mainToolbar').getComponent('rollbackBtn').setDisabled(true);
						}
					}
				} 				
			});
			
			var mainPanel = Ext.create('Ext.tab.Panel', {
				title: 'Manage Imports <i class="fa fa-question-circle"  data-qtip="Allows for management of the data imports and their mappings."></i>',
				items: [
					fileHistoryGrid,
					mappingPanel					
				]
			});
			
			Ext.create('Ext.container.Viewport', {
				layout: 'fit',
				items: [
					mainPanel
				]
			});
			
			var detailWindow = Ext.create('Ext.window.Window', {
				title: 'Details',
				modal: true,
				layout: 'fit',
				width: '60%',
				height: '40%',
				maximizable: true,
				items: [
					{
						xtype: 'grid',
						itemId: 'detailGrid',
						columnLines: true,
						store: {
							fields: [
								"fileHistoryErrorId",
								"fileHistoryId",
								"recordNumber",
								"errorMessage",
								"fileHistoryErrorType"
							],
							autoLoad: false,
							proxy: {
								type: 'ajax',
								url: '../api/v1/resource/filehistory/'
							}
						}, 
						plugins: [
							{
								ptype: 'rowexpander',
								rowBodyTpl: '{errorMessage}'
							}
						],
						columns: [
							{ text: 'Record Number', dataIndex: 'recordNumber', width: 150 },
							{ text: 'Type', dataIndex: 'fileHistoryErrorType', width: 150 },
							{ text: 'Message', dataIndex: 'errorMessage', flex: 1, minWidth: 150, 
								renderer: function (value) {
									return Ext.String.ellipsis(value, 200);
								}
							}
						]
					}
				],
				dockedItems: [
					{
						xtype: 'toolbar',
						dock: 'bottom',
						items: [
							{
								text: 'Previous',
								id: 'detailWindow-previousBtn',
								iconCls: 'fa fa-arrow-left',									
								handler: function() {
									actionDetailsNextRecord(false);
								}									
							},
							{
								xtype: 'tbfill'
							},
							{
								text: 'Close',
								iconCls: 'fa fa-close',
								handler: function() {
									this.up('window').hide();
								}
							},
							{
								xtype: 'tbfill'
							},
							{
								text: 'Next',
								id: 'detailWindow-nextBtn',
								iconCls: 'fa fa-arrow-right',
								iconAlign: 'right',
								handler: function() {
									actionDetailsNextRecord(true);
								}									
							}
						]
					}
				]
			});
			
			var actionDetailsNextRecord = function(next) {
				if (next) {
					Ext.getCmp('fileHistoryGrid').getSelectionModel().selectNext();						
				} else {
					Ext.getCmp('fileHistoryGrid').getSelectionModel().selectPrevious();						
				}
				actionViewDetails(Ext.getCmp('fileHistoryGrid').getSelection()[0]);					
			};			
			
			var previewCheckButtons = function() {	
				if (Ext.getCmp('fileHistoryGrid').getSelectionModel().hasPrevious()) {
					Ext.getCmp('detailWindow-previousBtn').setDisabled(false);
				} else {
					Ext.getCmp('detailWindow-previousBtn').setDisabled(true);
				}

				if (Ext.getCmp('fileHistoryGrid').getSelectionModel().hasNext()) {
					Ext.getCmp('detailWindow-nextBtn').setDisabled(false);
				} else {
					Ext.getCmp('detailWindow-nextBtn').setDisabled(true);
				}					
			};			
			
			var actionViewDetails = function(record){
				detailWindow.show();
				detailWindow.setTitle('Details - ' + Ext.util.Format.date(record.get('createDts'), 'm/d/y H:i:s') + ' - ' + record.get('originalFilename'));
				detailWindow.getComponent('detailGrid').getStore().load({
					url: '../api/v1/resource/filehistory/' + record.get('fileHistoryId') + '/errors'
				});
				previewCheckButtons();
			};
			
			var actionReprocess = function(record){
				
			};
			
			var actionRollback = function(record){
				
			};			
			
		});		
	</script>	
		
	</stripes:layout-component>
</stripes:layout-render>	