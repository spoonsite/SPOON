<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
    <stripes:layout-component name="contents">
	
	<script type="text/javascript">
		/* global Ext, CoreUtil */
		Ext.onReady(function(){	
			
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
				title: 'Import History',
				columnLines: true,
				store: fileHistoryStore,
				columns: [
					{ text: 'File Type', dataIndex: 'fileTypeDescription', width: 150 },
					{ text: 'File Format', dataIndex: 'fileFormatDescription', width: 200 },
					{ text: 'Upload Date', dataIndex: 'createDts', width: 175, xtype: 'datecolumn', format:'m/d/y H:i:s'  },
					{ text: 'Start Date', dataIndex: 'startDts', width: 175, xtype: 'datecolumn', format:'m/d/y H:i:s'  },
					{ text: 'Complete Date', dataIndex: 'completeDts', width: 175, xtype: 'datecolumn', format:'m/d/y H:i:s' },
					{ text: 'Filename', dataIndex: 'originalFilename', width: 200 },
					{ text: 'Data Source', dataIndex: 'dataSource', width: 200 },
					{ text: 'Mime Type', dataIndex: 'mimeType', width: 175 },
					{ text: 'Records Stored/Process', dataIndex: 'recordsStored', width: 175 },
					{ text: 'Processed', dataIndex: 'progress', flex: 1, minWidth: 175,
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
						dock: 'top',
						items: [
							{
								text: 'Refresh',								
								scale: 'medium',								
								iconCls: 'fa fa-2x fa-refresh',
								handler: function () {
									
								}
							},
							{
								xtype: 'tbseparator'
							},
							{
								text: 'View Details',								
								scale: 'medium',								
								iconCls: 'fa fa-2x fa-binoculars',
								handler: function () {
									
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
									
								}
							},							
							{
								xtype: 'tbfill'
							},
							{
								text: 'Reprocess',								
								scale: 'medium',								
								iconCls: 'fa fa-2x fa-reply',
								handler: function () {
									
								}
							},
							{
								text: 'Rollback',								
								scale: 'medium',								
								iconCls: 'fa fa-2x fa-close',
								handler: function () {
									
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
						
					},
					selectionchange: function(grid, record, index, opts){
						
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
			
		});		
	</script>	
		
	</stripes:layout-component>
</stripes:layout-render>	