<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
    <stripes:layout-component name="contents">
       
        <script type="text/javascript">
            /* global Ext, CoreUtil */
            
            Ext.onReady(function () {
                
                
                //
                //
                //  Generate CSV to DataGrid
                //
                //
                var csvToHTML = function(csvData){
                    
                    var lines = csvData.split("\n");
                    var rows =[];
                    var maxCols = 0;
                    var htmlData='';
                    var foundHeaderFlag=0;
                    var lengthCheck = 3;
                    var lengthCheckFlag = 0;
                    
                    for(ctr=0;ctr<lines.length; ctr++){
                        var tmpRow = lines[ctr].split('",');
                        for(ctr2=0; ctr2<tmpRow.length; ctr2++){
                            tmpRow[ctr2]=tmpRow[ctr2].replace(/["]+/g, '');
                        }
                        if(tmpRow.length>maxCols){
                            maxCols = tmpRow.length;
                        }
                        rows[ctr]=tmpRow;
                    }
                    console.log("Max Cols:"+maxCols);
                    console.log("rows",rows);
                    
                    htmlData+='<html><head><style> table{border-collapse: collapse; border: 2px black solid; font: 12px sans-serif} td{border: 1px black solid; padding:5px;} th{padding:5px;}</style></head>';
                    
                    for(ctr=0; ctr<rows.length; ctr++){
                        for (ctr2=0; ctr2<rows[ctr].length; ctr2++){
                           if(foundHeaderFlag === 0){
                               if(rows[ctr].length===maxCols) //This is the header
                               {
                                  if(ctr2===0){ //Start Header
                                      htmlData+='<table><tr><th>'+rows[ctr][ctr2]+'</th>';
                                  }  
                                  
                                  if ((ctr2+1)=== maxCols){ //End Row
                                      foundHeaderFlag =1;
                                      if(ctr2!==0){
                                          htmlData+='<th>'+rows[ctr][ctr2]+'</th></tr>';
                                      }
                                      else {
                                          htmlData+='</tr>';
                                      }
                                      
                                  }
                                  else if(ctr2!==0){//Normal Header Col
                                      htmlData+='<th>'+rows[ctr][ctr2]+'</th>';
                                  }
                               }
                               else  //Data before the header
                               {
                                 
                                 if(ctr2 === 0){
                                    if(rows[ctr].length>lengthCheck){
                                        
                                        if(!lengthCheckFlag){
                                            htmlData+='<table>';
                                            lengthCheckFlag=1;
                                        }
                                        htmlData+='<tr><td>'+rows[ctr][ctr2]+'</td>';
                                        
                                    } 
                                    else{
                                        if(lengthCheckFlag){
                                           htmlData+='</table>';
                                           lengthCheckFlag=0;
                                        }
                                        htmlData+='<div>'+rows[ctr][ctr2];
                                    }
                                     
                                 }
                                 
                                 if((ctr2+1) === rows[ctr].length){ //Last Col
                                     
                                    if(rows[ctr].length>lengthCheck){
                                       if(ctr2 !== 0){
                                           htmlData+='<td>'+rows[ctr][ctr2]+'</td></tr>';
                                       }
                                       else {
                                           htmlData+='</tr>';
                                       }
                                    } 
                                    else{
                                       if(ctr2 !== 0){
                                           htmlData+=rows[ctr][ctr2]+'</div>'; 
                                       }
                                       else
                                       {
                                           htmlData+='</div>'; 
                                       }
                                       
                                    }
                                 }
                                 else if(ctr2!==0){ //Middle Col
                                     if(rows[ctr].length>lengthCheck){
                                         
                                        htmlData+='<td>'+rows[ctr][ctr2]+'</td>';
                                    } 
                                    else{
                                     htmlData+=rows[ctr][ctr2];
                                    }
                                   
                               }
                             }
                           }
                           else { //Regular table data
                               
                              if(ctr2 === 0){ //Start new row
                                  htmlData+='<tr><td>'+rows[ctr][ctr2]+'</td>';
                              } 
                              
                              if((ctr2+1) === rows[ctr].length){ //End row
                                  if(ctr2!==0){
                                      htmlData+='<td>'+rows[ctr][ctr2]+'</td></tr>'; 
                                  }
                                  else{
                                      htmlData+='</tr>';
                                  }
                                      
                                 
                              }
                              else if (ctr2 !==0){ //Normal Data
                                  
                                  htmlData+='<td>'+rows[ctr][ctr2]+'</td>';
                              }
                               
                           }
                        }
                    }
                    htmlData+='</table></html>';
                    htmlData=htmlData.replace(/<td><\/td>/g, '<td>&nbsp</td>');
                   
                    console.log("HTML:", htmlData);
                    return htmlData;                  
                };
                
                
                //
                //
                //  History View Window
                //
                //
                var historyViewWin = function(data, data_type){
                     var theItems=[];
                     var contentData = '';
                     if(data_type === 'text-html'){
                        
                        contentData = data;
                     }
                     else if(data_type === 'text-csv'){
                        
                        contentData = csvToHTML(data); 
                     }
                     else
                     {
                        
                        contentData = data;
                     }
                     
                     Ext.create('Ext.window.Window', {
                        title: 'View Report Data',
                        id: 'viewHistoryData',
                        iconCls: 'fa fa-eye',
                        width: '50%',
                        height: '60%',
                        y: 40,
                        closeAction: 'destroy',
                        modal: true,
                        maximizable: true,
                        layout: 'fit',
                        items:theItems,
                        html:contentData,
                        bodyStyle: 'padding: 10px;',
                        scrollable: true
                    }).show();
                };
                
                
                //
                //  History Tab-------------->
                //
                
                //
                //  History Store
                //                
                var historyGridStore = Ext.create('Ext.data.Store', {
                    id: 'historyGridStore',
                    autoLoad: true,
                    pageSize: 100,
                    remoteSort: true,
                    sorters: [
                                new Ext.util.Sorter({
                                     id: 'hsorter',
                                     property: 'createDts',
                                     direction: 'DESC'
                                })
                            ],
                    fields: [
                                {name: 'storageVersion', mapping: function (data) {
                                        //console.log("Data:",data);
                                        return data.storageVersion;
                                    }},
                                {name: 'securityMarkingType', mapping: function (data) {
                                        return data.securityMarkingType;
                                    }},
                                {name: 'activeStatus', mapping: function (data) {
                                        return data.activeStatus;
                                    }},
                                {name: 'createUser', mapping: function (data) {
                                        return data.createUser;
                                    }},
                                {name: 'createDts', mapping: function (data) {
                                        return data.createDts;
                                    }},
                                {name: 'updateUser', mapping: function (data) {
                                        return data.updateUser;
                                    }},
                                {name: 'updateDts', mapping: function (data) {
                                        return data.updateDts;
                                    }},
                                {name: 'adminModified', mapping: function (data) {
                                        return data.adminModified;
                                    }},
                                {name: 'reportId', mapping: function (data) {
                                        return data.reportId;
                                    }},
                                {name: 'reportType', mapping: function (data) {
                                        return data.reportType;
                                    }},
                                {name: 'reportFormat', mapping: function (data) {
                                        return data.reportFormat;
                                    }},
                                {name: 'runStatus', mapping: function (data) {
                                        return data.runStatus;
                                    }},
                                {name: 'ids', mapping: function (data) {
                                        return data.ids;
                                    }},
                                {name: 'reportOption', mapping: function (data) {
                                        return data.reportOption;
                                    }},
                                {name: 'scheduled', mapping: function (data) {
                                        return data.scheduled;
                                    }},
                                {name: 'reportTypeDescription', mapping: function (data) {
                                        return data.reportTypeDescription;
                                    }},
                                {name: 'reportFormatDescription', mapping: function (data) {
                                        return data.reportFormatDescription;
                                    }},
                                {name: 'runStatusDescription', mapping: function (data) {
                                        return data.scheduled;
                                    }}                              
                            ],
                    proxy: CoreUtil.pagingProxy({
                                url: '../api/v1/resource/reports',
                                method: 'GET',
                                reader: {
                                    type: 'json',
                                    rootProperty: 'data',
                                    totalProperty: 'totalNumber'
                                }
                            })
                });

                var historyGrid = Ext.create('Ext.grid.Panel', {
                    title: '',
                    id: 'historyGrid',
                    store: historyGridStore,
                    columnLines: true,
                    bodyCls: 'border_accent',
                    selModel: {
                        selType: 'checkboxmodel'
                    },
                    plugins: 'gridfilters',
                    enableLocking: true,
                    columns: [
                        {text: 'Report Type', dataIndex: 'reportTypeDescription', width: 200, flex:1, lockable: true,
                            filter: {
                                type: 'string'
                            }
                        },
                        {text: 'Format', dataIndex: 'reportFormatDescription', width: 250,
                            filter: {
                                type: 'string'
                            }
                        },
                        {text: 'Run Status', dataIndex: 'runStatusDescription', width: 150,
                            filter: {
                                type: 'string'
                            },
                            renderer: function(value, meta){
                                if(value === 'Error'){
                                    meta.style = "background-color:#ce0000;color:#FFFFFF;";
                                   
                                }
                                return value;
                                
                            }
                        },
                        {text: 'Create Date', dataIndex: 'createDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s'},
                        {text: 'Create User', dataIndex: 'createUser', width: 150},
                        {text: 'Scheduled', dataIndex: 'scheduled', width: 100},
                        {text: 'Options', dataIndex: 'reportOption', width: 300, flex:1,
                            filter: {
                                type:'string'
                            },
                            renderer: function(v,meta){
                                if(v){
                                    //console.log("V:",v);
                                    if(v.category){
                                       return 'Category:'+v.category;
                                    }
                                    else if(v.startDts){
                                        return 'Start Date:'+v.startDts+'<br/>End Date:'+v.endDts+'<br/>Previous Days:'+v.previousDays;
                                    }
                                    else if(v.maxWaitSeconds){
                                        return 'Max Wait Seconds:'+v.maxWaitSeconds;
                                    }
                                }
                                else {
                                    return '';
                                }      
                            }
                            
                        },
                        {text: 'Active Status', dataIndex: 'activeStatus', width: 125,
                            filter: {
                                type:'string'
                            }
                        }
                    ],
                    dockedItems: [
//                        {
//                            dock: 'top',
//                            xtype: 'toolbar',
//                            items: [
//                                  Ext.create('OSF.component.StandardComboBox', {
//                                            id: 'userTrackingFilter-ActiveStatus',
//                                            emptyText: 'All',
//                                            fieldLabel: 'Active Status',
//                                            name: 'activeStatus',	
//                                            listeners: {
//                                                change: function(filter, newValue, oldValue, opts){
//                                                    userRefreshGrid();
//                                                }
//                                            },
//                                            storeConfig: {
//										customStore: {
//											fields: [
//												'code',
//												'description'
//											],
//											data: [												
//												{
//													code: 'A',
//													description: 'Active'
//												},
//												{
//													code: 'I',
//													description: 'Inactive'
//												},
//												{
//													code: 'All',
//													description: 'All'
//												}
//											]
//										}
//									}
//                            })]
//                       },
                       {
                            dock: 'top',
                            xtype: 'toolbar',
                            items: [
                            {
                                text: 'Refresh',
                                scale: 'medium',
                                id: 'historyRefreshButton',
                                iconCls: 'fa fa-2x fa-refresh',
                                tip: 'Refresh the list of records',
                                handler: function () {
                                   historyRefreshGrid();
                                },
                                tooltip: 'Refresh the list of records'
                            },
                            {
                                text: 'View',
                                scale: 'medium',
                                id: 'historyViewButton',
                                iconCls: 'fa fa-2x fa-eye',
                                disabled: true,
                                handler: function () {
                                    
                                   viewHistory(); 
                                  
                                },
                                tooltip: 'View Record'
                            },
                            {
                                text: 'Delete',
                                id: 'historyDeleteButton',
                                scale: 'medium',
                                iconCls: 'fa fa-2x fa-trash',
                                disabled: true,
                                handler: function () {
                                    historyDelete();
                                },
                                tooltip: 'Delete the record'
                            },
                            
                            {
                                text: 'Export',
                                id: 'historyExportButton',
                                scale: 'medium',
                                iconCls: 'fa fa-2x fa-download',
                                disabled: true,
                                handler: function () {
                                   historyExport();         
                                },
                                tooltip:'Export data and download to .csv format'

                            },
                            {
                                xtype: 'tbfill'
                            }]
                        },
                        { 
                            xtype: 'pagingtoolbar',
                            dock: 'bottom',
                            store: 'historyGridStore',
                            displayInfo: true   
                        }
                    ],
                    listeners: {
                        
                        selectionchange: function (grid, record, index, opts) {
                           historyCheckNavButtons(); 
                        }
                    }
                });
                
                
                //
                //   history FUNCTIONS
                //
                
                //
                // Check which nav buttons should be on and which should be off
                //
                var historyCheckNavButtons = function() {
                    
                    if (historyGrid.getSelectionModel().getCount() === 1) {
                        
                        Ext.getCmp('historyDeleteButton').setDisabled(false);
                        Ext.getCmp('historyViewButton').setDisabled(false);
                        Ext.getCmp('historyExportButton').setDisabled(false);
                    } else if (historyGrid.getSelectionModel().getCount() > 1) {
                        
                        Ext.getCmp('historyDeleteButton').setDisabled(false);
                        Ext.getCmp('historyViewButton').setDisabled(true);
                        Ext.getCmp('historyExportButton').setDisabled(true); 
                    } else {
                        Ext.getCmp('historyViewButton').setDisabled(true);
                        Ext.getCmp('historyDeleteButton').setDisabled(true);
                        Ext.getCmp('historyExportButton').setDisabled(true);     
                    }
                };
                
                //
                //  Refresh and reload the grid
                //
                var historyRefreshGrid = function(){ 
                     Ext.getCmp('historyGrid').getStore().load({
//						params: {
//							status: Ext.getCmp('userTrackingFilter-ActiveStatus').getValue() ? Ext.getCmp('userTrackingFilter-ActiveStatus').getValue() : 'ALL',
//						    all: (Ext.getCmp('userTrackingFilter-ActiveStatus').getValue() === 'All')
//                             
//                        }
				    });
                };
                
               
                //
                // viewHistory
                //
                var viewHistory = function(){
                    
                    var selectedObj = Ext.getCmp('historyGrid').getSelection()[0];
                    
                     Ext.Ajax.request({
                        url: '../api/v1/resource/reports/'+selectedObj.data.reportId+'/report',
                        method: 'GET',
                        success: function (response, opts) {
                            console.log("View Report:",response);
                            
                            historyViewWin(response.responseText,selectedObj.data.reportFormat); 
                        }
                    });
                };

                //
                // Delete Record
                //
                var  historyDelete = function (){
                    
                    var selectedObj = Ext.getCmp('historyGrid').getSelection();
                    
                    if(selectedObj.length>1){
                        var ids=[];
                    
                        for(ctr=0; ctr<selectedObj.length; ctr++){
                            //console.log("selectedObj Id:",selectedObj[ctr].data.reportId);
                            ids.push(selectedObj[ctr].data.reportId);

                        }
                        console.log("ids:",ids);
                        
                        Ext.Msg.show({
                            title: 'Delete Reports?',
                            message: 'Are you sure you want to delete the selected reports?',
                            buttons: Ext.Msg.YESNO,
                            icon: Ext.Msg.QUESTION,
                            fn: function (btn) {
                                if (btn === 'yes') {
                                    Ext.getCmp('historyGrid').setLoading(true);
                                    
                                    //console.log("Report ID:",reportId);
                                    Ext.Ajax.request({
                                        url: '../api/v1/resource/reports/delete',
                                        method: 'DELETE',
                                        jsonData: {entity: ids},
                                        headers: {'Accept': 'application/json, text/plain, */*', 'Content-Type': 'application/json;charset=UTF-8'},
                                        success: function (response, opts) {
                                            Ext.getCmp('historyGrid').setLoading(false);
                                            historyRefreshGrid();
                                        },
                                        failure: function (response, opts) {
                                            Ext.getCmp('historyGrid').setLoading(false);
                                        }
                                    });
                                }
                            }
                        });   
                    }
                    else {
                        Ext.Msg.show({
                            title: 'Delete Report?',
                            message: 'Are you sure you want to delete the selected report?',
                            buttons: Ext.Msg.YESNO,
                            icon: Ext.Msg.QUESTION,
                            fn: function (btn) {
                                if (btn === 'yes') {
                                    Ext.getCmp('historyGrid').setLoading(true);
                                    var reportId = selectedObj[0].data.reportId;
                                    //console.log("Report ID:",reportId);
                                    Ext.Ajax.request({
                                        url: '../api/v1/resource/reports/' + reportId,
                                        method: 'DELETE',
                                        success: function (response, opts) {
                                            Ext.getCmp('historyGrid').setLoading(false);
                                            historyRefreshGrid();
                                        },
                                        failure: function (response, opts) {
                                            Ext.getCmp('historyGrid').setLoading(false);
                                        }
                                    });
                                }
                            }
                        });  
                    }
                                     
                };
                
                //
                //  Export Record
                //
                var  historyExport = function (){
                    Ext.toast('Exporting Report Data ...');
                     var selectedObj = Ext.getCmp('historyGrid').getSelection()[0].data;
                    //console.log("Selected Obj:",selectedObj);
                    window.location.href='../api/v1/resource/reports/'+selectedObj.reportId+'/report';
//                    Ext.Ajax.request(
//                        url: '../api/v1/resource/reports/'+selectedObj.reportId+'/report',
//                        method: 'GET',
//                        success: function (response, opts) {
//                           // console.log("Export Report:",response);
//                            if(selectedObj.)
//                            CoreUtil.downloadCSVFile('report.csv', response.responseText );
//                        }
//                    });
                };
                
                
                //
                //  Schedule Report Tab-------------->
                //
                
                //
                //  Schedule Report Store
                //                
                var scheduleReportsGridStore = Ext.create('Ext.data.Store', {
                    id: 'scheduleReportsGridStore',
                    autoLoad: true,
                    pageSize: 100,
                    remoteSort: true,
                    sorters: [
                                new Ext.util.Sorter({
                                     id: 'sorter',
                                     property: 'createDts',
                                     direction: 'DESC'
                                })
                            ],
                    fields: [
                                {name: 'storageVersion', mapping: function (data) {
                                        return data.storageVersion;
                                    }},
                                {name: 'securityMarkingType', mapping: function (data) {
                                        return data.securityMarkingType;
                                    }},
                                {name: 'activeStatus', mapping: function (data) {
                                        return data.activeStatus;
                                    }},
                                {name: 'createUser', mapping: function (data) {
                                        return data.createUser;
                                    }},
                                {name: 'createDts', mapping: function (data) {
                                        return data.createDts;
                                    }},
                                {name: 'updateUser', mapping: function (data) {
                                        return data.updateUser;
                                    }},
                                {name: 'updateDts', mapping: function (data) {
                                        return data.updateDts;
                                    }},
                                {name: 'adminModified', mapping: function (data) {
                                        return data.adminModified;
                                    }},
                                {name: 'scheduleReportId', mapping: function (data) {
                                        return data.scheduleReportId;
                                    }},
                                {name: 'reportType', mapping: function (data) {
                                        return data.reportType;
                                    }},
                                {name: 'reportFormat', mapping: function (data) {
                                        return data.reportFormat;
                                    }},
                                {name: 'reportOption', mapping: function (data) {
                                        return data.reportOption;
                                    }},
                                {name: 'emailAddresses', mapping: function (data) {
                                        return data.emailAddresses;
                                    }},
                                {name: 'scheduleIntervalDays', mapping: function (data) {
                                        return data.scheduleIntervalDays;
                                    }},
                                {name: 'lastRanDts', mapping: function (data) {
                                        return data.lastRanDts;
                                    }},
                                {name: 'reportTypeDescription', mapping: function (data) {
                                        return data.reportTypeDescription;
                                    }},
                                {name: 'reportFormatDescription', mapping: function (data) {
                                        return data.reportFormatDescription;
                                    }}                                
                            ],
                    proxy: CoreUtil.pagingProxy({
                                url: '../api/v1/resource/scheduledreports',
                                method: 'GET',
                                reader: {
                                    type: 'json',
                                    rootProperty: '',
                                    totalProperty: ''
                                }
                            })
                });

                var scheduleReportsGrid = Ext.create('Ext.grid.Panel', {
                    title: '',
                    id: 'scheduleReportsGrid',
                    store: scheduleReportsGridStore,
                    columnLines: true,
                    bodyCls: 'border_accent',
                    selModel: {
                        selType: 'checkboxmodel'
                    },
                    plugins: 'gridfilters',
                    enableLocking: true,
                    columns: [
                        {text: 'Report Type', dataIndex: 'reportTypeDescription', width: 300, flex:1, lockable: true,
                            filter: {
                                type: 'string'
                            }
                            
                        },
                        {text: 'Format', dataIndex: 'reportFormatDescription', width: 300,
                            filter: {
                                type: 'string'
                            }
                        },
                        {text: 'Create User', dataIndex: 'createUser', width: 150},
                        {text: 'Scheduled Interval', dataIndex: 'scheduleIntervalDays', width: 200,
                            renderer: function(v,meta){
                                if(v === 1){
                                    return 'Daily';
                                }
                                else if(v > 1 && v <8){
                                    return 'Weekly';
                                }
                                else if(v > 8){
                                    return 'Monthly'
                                }
                                    
                            }
                        },
                        
                        {text: 'Last Run Date', dataIndex: 'lastRanDts', width: 200, xtype: 'datecolumn', format: 'm/d/y H:i:s'},
                        
                        {text: 'Email Addresses', dataIndex: 'emailAddresses', width: 150, flex:1,
                             renderer: function(v,meta){
                              var emailStr='';
                              for (index=0; index<v.length; ++index){
                                  
                                  emailStr+=v[index].email+'<br/>';
                              }
                              return emailStr;
                            }
                        },
                        {text: 'Options', dataIndex: 'reportOption', width: 200, flex:1,
                            filter: {
                                type:'string'
                            },
                            renderer: function(v,meta){
                                if(v){
                                    //console.log("V:",v);
                                    if(v.category){
                                       return 'Category:'+v.category;
                                    }
                                    else if(v.startDts){
                                        return 'Start Date:'+v.startDts+'<br/>End Date:'+v.endDts+'<br/>Previous Days:'+v.previousDays;
                                    }
                                    else if(v.maxWaitSeconds){
                                        return 'Max Wait Seconds:'+v.maxWaitSeconds;
                                    }
                                }
                                else {
                                    return '';
                                }     
                            }
                            
                        },
                        {text: 'Active Status', dataIndex: 'activeStatus', width: 125,
                            filter: {
                                type:'string'
                            }
                        }
                        
                    ],
                    dockedItems: [
                        {
                            dock: 'top',
                            xtype: 'toolbar',
                            items: [
                                  Ext.create('OSF.component.StandardComboBox', {
                                            id: 'scheduleReportFilter-ActiveStatus',
                                            fieldLabel: 'Active Status',
                                            name: 'activeStatus',
                                            displayField: 'description',
                                            valueField: 'code',
                                            value:'A',
                                            listeners: {
                                                change: function(filter, newValue, oldValue, opts){
                                                    scheduleReportRefreshGrid();
                                                }
                                            },
                                            storeConfig: {
										customStore: {
											fields: [
												'code',
												'description'
											],
											data: [												
												{
													code: 'A',
													description: 'Active'
												},
												{
													code: 'I',
													description: 'Inactive'
												}
											]
										}
									}
                            })]
                       },
                       {
                            dock: 'top',
                            xtype: 'toolbar',
                            items: [
                            {
                                text: 'Refresh',
                                scale: 'medium',
                                id: 'reportRefreshButton',
                                iconCls: 'fa fa-2x fa-refresh',
                                tip: 'Refresh the list of records',
                                handler: function () {
                                   scheduleReportRefreshGrid();
                                },
                                tooltip: 'Refresh the list of records'
                            },
                            {
                                text: 'Add',
                                id: 'reportAddButton',
                                scale: 'medium',
                                iconCls: 'fa fa-2x fa-plus',
                                disabled: false,
                                handler: function () {
                                    scheduleReportAdd();
                                },
                                tooltip: 'Add a record'
                            },
                            {
                                text: 'Edit',
                                id: 'reportEditButton',
                                scale: 'medium',
                                iconCls: 'fa fa-2x fa-edit',
                                disabled: true,
                                handler: function () {
                                    scheduleReportEdit();
                                },
                                tooltip: 'Edit a record'
                            },
                            {
                                text: 'Toggle Active',
                                id: 'reportActivateButton',
                                scale: 'medium',
                                iconCls: 'fa fa-2x fa-power-off',
                                disabled: true,
                                handler: function () {
                                   scheduleReportActivate();         
                                },
                                tooltip: 'Toggle activation of a record'
                            },         
                            {
                                text: 'Delete',
                                id: 'reportDeleteButton',
                                scale: 'medium',
                                iconCls: 'fa fa-2x fa-trash',
                                disabled: true,
                                handler: function () {
                                    scheduleReportDelete();
                                },
                                tooltip: 'Delete the record'
                            },
                            {
                                xtype: 'tbfill'
                            }]
                        },
                        { 
                            xtype: 'pagingtoolbar',
                            dock: 'bottom',
                            store: 'scheduleReportsGridStore',
                            displayInfo: true   
                        }
                    ],
                    listeners: {
                        
                        selectionchange: function (grid, record, index, opts) {
                           scheduledReportCheckNavButtons(); 
                        }
                    }
                });
                
           
                //
                //   Scheduled Report FUNCTIONS
                //
                
                //
                // Check which nav buttons should be on and which should be off
                //
              
                
                var scheduledReportCheckNavButtons = function() {
                    
                    if (scheduleReportsGrid.getSelectionModel().getCount() === 1) {
                        
                        Ext.getCmp('reportEditButton').setDisabled(false);
                        Ext.getCmp('reportActivateButton').setDisabled(false);
                        Ext.getCmp('reportDeleteButton').setDisabled(false);
                        
                    } else if (scheduleReportsGrid.getSelectionModel().getCount() > 1) {
                        
                        Ext.getCmp('reportEditButton').setDisabled(true);
                        Ext.getCmp('reportActivateButton').setDisabled(true);
                        Ext.getCmp('reportDeleteButton').setDisabled(true); 
                    } else {
                        
                        Ext.getCmp('reportEditButton').setDisabled(true);
                        Ext.getCmp('reportActivateButton').setDisabled(true);
                        Ext.getCmp('reportDeleteButton').setDisabled(true);     
                    }
                };
                
                //
                //  Refresh and reload the grid
                //
                var scheduleReportRefreshGrid = function(){ 
                     Ext.getCmp('scheduleReportsGrid').getStore().load({
						params: {
							status: Ext.getCmp('scheduleReportFilter-ActiveStatus').getValue() ? Ext.getCmp('scheduleReportFilter-ActiveStatus').getValue() : ''       
                        }
				    });
                };
                
                //
                // Add Record
                //
                var scheduleReportAdd = function(){
                    scheduleReportWin(null); 
                };
                
                //
                // Edit Record
                //
                var scheduleReportEdit = function(){
                    scheduleReportWin(Ext.getCmp('scheduleReportsGrid').getSelection()[0]); 
                };
  
                //
                //  Activate Record
                //
                var scheduleReportActivate = function(){
                    var selectedObj = Ext.getCmp('scheduleReportsGrid').getSelection()[0];
                    var reportId = selectedObj.data.scheduleReportId;
                    var newUrl='';
                    var newMethod='';
                    if(selectedObj.data.activeStatus === 'A'){
                        newUrl = '../api/v1/resource/scheduledreports/' + reportId;
                        newMethod='DELETE';
                    }
                    else {
                        newUrl = '../api/v1/resource/scheduledreports/' + reportId +'/activate';
                        newMethod = 'POST';
                    }
                    Ext.getCmp('scheduleReportsGrid').setLoading(true);
                    Ext.Ajax.request({
                        url: newUrl,
                        method: newMethod,
                        success: function (response, opts) {
                            Ext.getCmp('scheduleReportsGrid').setLoading(false);
                            scheduleReportRefreshGrid();
                        },
                        failure: function (response, opts) {
                            Ext.getCmp('scheduleReportsGrid').setLoading(false);
                        }
                    });
                };
                 
                //
                // Delete Record
                //
                var  scheduleReportDelete = function (){
                    
                    var selectedObj = Ext.getCmp('scheduleReportsGrid').getSelection()[0];
                    
                    Ext.Msg.show({
                        title: 'Delete Scheduled Report?',
                        message: 'Are you sure you want to delete the scheduled report?',
                        buttons: Ext.Msg.YESNO,
                        icon: Ext.Msg.QUESTION,
                        fn: function (btn) {
                            if (btn === 'yes') {
                                Ext.getCmp('scheduleReportsGrid').setLoading(true);
                                var reportId = selectedObj.data.scheduleReportId;
                                
                                Ext.Ajax.request({
                                    url: '../api/v1/resource/scheduledreports/' + reportId+'/force',
                                    method: 'DELETE',
                                    success: function (response, opts) {
                                        Ext.getCmp('scheduleReportsGrid').setLoading(false);
                                        scheduleReportRefreshGrid();
                                    },
                                    failure: function (response, opts) {
                                        Ext.getCmp('scheduleReportsGrid').setLoading(false);
                                    }
                                });
                            }
                        }
                    });                   
                };
                
                
                
                
                
                
                
                
                
                //
                //
                //  Schedule Reports Window
                //
                //
                 var scheduleReportWin = function(scheduleData){
                    var scheduleReportId=null;
                    //
                    //This is for editing schedule report
                    //
                    if(scheduleData !== null){
                        console.log("Schedule Data:",scheduleData);
                        scheduleReportId  = scheduleData.data.scheduleReportId;
                    }
                    
                    //
                    //  This formats the emails separated by ';' strng into an array 
                    //
                    var createEmailAddressesList = function(emailStr){
                        if(emailStr === '' || typeof emailStr === 'undefined'){
                            return null;
                        }
                        var emailArr =[];
                        var eArr= String(emailStr).trim().split(';');
                        for(ctr=0;ctr<eArr.length;ctr++){
                            var tmpStr = String(eArr[ctr]).trim();
                            if(tmpStr!==''){
                                emailArr.push({email: tmpStr});
                            }
                        }
                        return emailArr;
                    };
                    
                    var emailsArrayToString = function(emailArr){
                        emailStr='';
                        for(ctr=0;ctr<emailArr.length;ctr++){
                            emailStr+=emailArr[ctr].email+"; ";
                        }
                        
                        return emailStr;
                    };
                    
                    //
                    //  This is for the one time report run
                    //
                    var generateReport = function(data){
                        
                        Ext.getCmp('scheduleReportForm').setLoading(true);
                        
                        CoreUtil.submitForm({
                            url: '../api/v1/resource/reports',
                            method: 'POST',
                            data: data,
                            removeBlankDataItems: true,
                            form: Ext.getCmp('scheduleReportForm'),
                            success: function (response, opts) {
                                Ext.toast('Saved Successfully', '', 'tr');
                                
                                Ext.getCmp('scheduleReportForm').setLoading(false);
                                Ext.getCmp('scheduleReportForm').destroy();

                                Ext.getCmp('scheduleReportWin').destroy();
                                Ext.getCmp('scheduleReportsGrid').getStore().load();
                                
                                Ext.getCmp('reportTabPanel').setActiveTab(1);                      
                                
                                
                            },
                            failure: function(response, opts){

                                Ext.toast('Failed to Save', '', 'tr');
                                Ext.getCmp('scheduleReportForm').setLoading(false);
                            }
                        });
                    };
                    //
                    //   This is to schedule a report to run daily, monthly etc.
                    //
                    var scheduleReport = function(data){
                        
                        Ext.getCmp('scheduleReportForm').setLoading(true);
                        var url ='';
                        var method ='';
                        if(scheduleReportId){
                            url = '../api/v1/resource/scheduledreports/'+scheduleReportId;
                            method ='PUT'
                        }
                        else{
                            url = '../api/v1/resource/scheduledreports';
                            method = 'POST'
                        }
                        
                        CoreUtil.submitForm({
                            url: url,
                            method: method,
                            data: data,
                            removeBlankDataItems: true,
                            form: Ext.getCmp('scheduleReportForm'),
                            success: function (response, opts) {
                                Ext.toast('Saved Successfully', '', 'tr');

                                Ext.getCmp('scheduleReportForm').setLoading(false);
                                Ext.getCmp('scheduleReportForm').destroy();

                                Ext.getCmp('scheduleReportWin').destroy();
                                Ext.getCmp('scheduleReportsGrid').getStore().load();
                            },
                            failure: function(response, opts){

                                Ext.toast('Failed to Save', '', 'tr');
                                Ext.getCmp('scheduleReportForm').setLoading(false);
                            }
                        });
                    };
                    
                    
                    
                    
                    //
                    //  This is the store list for the Report Types
                    //
                    var reportTypesStore = Ext.create('Ext.data.Store', {
                        id: 'reportTypesStore',
                        autoLoad: true,
                        pageSize: 100,
                        remoteSort: true,
                        listeners:{
                            endupdate: function(opts){
                               console.log("Report Types Loaded");
                               if(scheduleReportId!==null){
                                   //Edit data
                                   console.log("Editing Record");
                                   Ext.getCmp('reportType').setValue(scheduleData.data.reportType);
                                   Ext.getCmp('reportFormat').setValue(scheduleData.data.reportFormat);
                                   Ext.getCmp('scheduledHours').setValue(String(scheduleData.data.scheduleIntervalDays));
                                   Ext.getCmp('emailAddresses').setValue(emailsArrayToString(scheduleData.data.emailAddresses));
                                   
                                   if(scheduleData.data.reportOption.category){
                                       Ext.getCmp('categorySelect').setValue(scheduleData.data.reportOption.category);
                                   }
                                   if(scheduleData.data.reportOption.maxWaitSeconds){
                                       Ext.getCmp('waitSeconds').setValue(scheduleData.data.reportOption.maxWaitSeconds);
                                   }
                                   if(scheduleData.data.reportOption.startDts){
                                       Ext.getCmp('startDate').setValue(new Date(scheduleData.data.reportOption.startDts));
                                       Ext.getCmp('endDate').setValue(new Date(scheduleData.data.reportOption.endDts));
                                       Ext.getCmp('previousDaysSelect').setValue(scheduleData.data.reportOption.previousDays);
                                   }
                                   
                                   
                                }
                            }
                        },
                        sorters: [
                                    new Ext.util.Sorter({
                                         id: 'sorter',
                                         property: 'description',
                                         direction: 'DESC'
                                    })
                                ],
                        fields: [
                                    {name: 'storageVersion', mapping: function (data) {
                                            //console.log("Data:",data);
                                            return data.storageVersion;
                                        }},
                                    {name: 'securityMarkingType', mapping: function (data) {
                                            return data.securityMarkingType;
                                        }},
                                    {name: 'activeStatus', mapping: function (data) {
                                            return data.activeStatus;
                                        }},
                                    {name: 'createUser', mapping: function (data) {
                                            return data.createUser;
                                        }},
                                    {name: 'createDts', mapping: function (data) {
                                            return data.createDts;
                                        }},
                                    {name: 'updateUser', mapping: function (data) {
                                            return data.updateUser;
                                        }},
                                    {name: 'updateDts', mapping: function (data) {
                                            return data.updateDts;
                                        }},
                                    {name: 'adminModified', mapping: function (data) {
                                            return data.adminModified;
                                        }},
                                    {name: 'code', mapping: function (data) {
                                            return data.code;
                                        }},
                                    {name: 'description', mapping: function (data) {
                                            return data.description;
                                        }},
                                    {name: 'detailedDescription', mapping: function (data) {
                                            return data.reportFormat;
                                        }},
                                    {name: 'sortOrder', mapping: function (data) {
                                            return data.sortOrder;
                                        }},
                                    {name: 'highlightStyle', mapping: function (data) {
                                            return data.highlightStyle;
                                        }},
                                    {name: 'adminOnly', mapping: function (data) {
                                            return data.adminOnly;
                                        }},
                                    {name: 'componentReport', mapping: function (data) {
                                            return data.componentReport;
                                        }},
                                    {name: 'supportedFormats', mapping: function (data) {
                                            return data.supportedFormats;
                                        }}                              
                                ],
                        proxy: CoreUtil.pagingProxy({
                                    url: '../api/v1/resource/reports/reporttypes',
                                    method: 'GET',
                                    reader: {
                                        type: 'json',
                                        rootProperty: '',
                                        totalProperty: ''
                                    }
                                })
                    });
                    
                    //
                    //  This is the store list for the Report Formats
                    //
                    var reportFormatsStore = Ext.create('Ext.data.Store', {
                        id: 'reportFormatsStore',
                        autoLoad: false,
                        pageSize: 100,
                        remoteSort: true,
                        sorters: [
                                    new Ext.util.Sorter({
                                         id: 'sorter',
                                         property: 'description',
                                         direction: 'DESC'
                                    })
                                ],
                        fields: [
                                    {name: 'code', mapping: function (data) {
                                            //("Formats",data);
                                            return data.code;
                                        }},
                                    {name: 'decription', mapping: function (data) {
                                            return data.description;
                                        }}                       
                                ],
                        proxy: CoreUtil.pagingProxy({
                                    url: '',
                                    method: 'GET',
                                    reader: {
                                        type: 'json',
                                        rootProperty: '',
                                        totalProperty: ''
                                    }
                                })
                    });
                    
                    //
                    //  This is the store list for the Report Options
                    //
                    var scheduleOptionsStore = Ext.create('Ext.data.Store', {
                        id: 'scheduleOptionsStore',
                        autoLoad: true,
                        pageSize: 100,
                        remoteSort: true,
                        sorters: [
                                    new Ext.util.Sorter({
                                         id: 'sorter',
                                         property: 'name',
                                         direction: 'DESC'
                                    })
                                ],
                        fields: [
                                    {name: 'name', mapping: function (data) {
                                            //console.log("Components",data);
                                            return data.name;
                                    }}                       
                                ],
                        proxy: CoreUtil.pagingProxy({
                                    url: '../api/v1/resource/components',
                                    method: 'GET',
                                    reader: {
                                        type: 'json',
                                        rootProperty: '',
                                        totalProperty: ''
                                    }
                                })
                    });
                    
                    
                    //
                    //  This is the store list for the catagories 
                    //
                    var scheduleCategoryStore = Ext.create('Ext.data.Store', {
                        id: 'scheduleCategoryStore',
                        autoLoad: true,
                        pageSize: 100,
                        remoteSort: true,
                        sorters: [
                                    new Ext.util.Sorter({
                                         id: 'sorter',
                                         property: 'description',
                                         direction: 'ASC'
                                    })
                                ],
                        fields: [
                                    {name: 'description', mapping: function (data) {
                                            //console.log("Categories",data);
                                            return data.description;
                                    }},
                                   {name: 'attributeType', mapping: function (data) {
                                            //console.log("Categories",data);
                                            return data.attributeType;
                                    }} 
                                ],
                        proxy: CoreUtil.pagingProxy({
                                    url: '../api/v1/resource/attributes/attributetypes',
                                    method: 'GET',
                                    reader: {
                                        type: 'json',
                                        rootProperty: 'data',
                                        totalProperty: ''
                                    }
                                })
                    });
                    
                    //
                    //  This is the store list for the how often to schedule report combo
                    //
                    var scheduleHowOften = Ext.create('Ext.data.Store', {
                        fields: ['code', 'description'],
                        data : [
                            {"code":"0", "description":"Now (One Time Only)"},
                            {"code":"1", "description":"Daily"},
                            {"code":"7", "description":"Weekly"},
                            {"code":"28", "description":"Monthly"}
                        ]
                    });
                    
                    
                    //
                    //  This is the store list for the Previous Days combo
                    //
                    var previousDaysStore = Ext.create('Ext.data.Store', {
                        id: 'previousDaysStore',
                        fields: ['code', 'days'],
                        data : [
                            {"days":"1"},
                            {"days":"2"},
                            {"days":"3"},
                            {"days":"4"},
                            {"days":"5"},
                            {"days":"6"},
                            {"days":"7"},
                            {"days":"8"},
                            {"days":"9"},
                            {"days":"10"},
                            {"days":"11"},
                            {"days":"12"},
                            {"days":"13"},
                            {"days":"14"},
                            {"days":"15"},
                            {"days":"16"},
                            {"days":"17"},
                            {"days":"18"},
                            {"days":"19"},
                            {"days":"20"},
                            {"days":"21"},
                            {"days":"22"},
                            {"days":"23"},
                            {"days":"24"},
                            {"days":"25"},
                            {"days":"26"},
                            {"days":"27"},
                            {"days":"28"}
                            
                        ]
                    });
                    
                    
                    //
                    //  scheduleReportWin
                    //  The popup window to schedule are report to run now or on a set repeating schedule
                    // 
                    //
                    Ext.create('Ext.window.Window', {
                        title: 'Schedule Report',
                        id: 'scheduleReportWin',
                        iconCls: 'fa fa-calendar',
                        width: '30%',
                        y: 40,
                        closeAction: 'destroy',
                        modal: true,
                        maximizable: false,
                        layout: 'fit',
                        items:[{
                                xtype: 'form',
                                id: 'scheduleReportForm',
                                layout: 'vbox',
                                scrollable: true,
                                bodyStyle: 'padding: 10px;',
                                submitEmptyText: false,
                                defaults: {
                                    labelAlign: 'top'
                                },
                                dockedItems: [
                                    {
                                        dock: 'bottom',
                                        xtype: 'toolbar',
                                        items: [
                                            {
                                                text: 'Save',
                                                formBind: true,
                                                iconCls: 'fa fa-save',
                                                handler: function () {
                                                    
                                                    var data={};
                                                    var reportOpt={};
                                                    
                                                    data.reportType=Ext.getCmp('reportType').getValue();
                                                    data.reportFormat=Ext.getCmp('reportFormat').getValue();
                                                    data.reportOption=null;
                                                    data.emailAddresses=createEmailAddressesList(Ext.getCmp('emailAddresses').getValue());
                                                    data.scheduleIntervalDays=Ext.getCmp('scheduledHours').getValue();
                                                    
                                                    if(data.scheduleIntervalDays === '0')
                                                    {
                                                       data.scheduleIntervalDays=null;    
                                                    }
                                                    
                                                    if(Ext.getCmp('categorySelect').isVisible()){
                                                        
                                                     // console.log("Cat:",Ext.getCmp('categorySelect'));
                                                      reportOpt.category=Ext.getCmp('categorySelect').getValue();
                                                        
                                                    }
                                                    
                                                    if(Ext.getCmp('startDate').isVisible()){
                                                        
                                                        reportOpt.startDts=Ext.Date.format(Ext.getCmp('startDate').getValue(),'Y-m-d\\TH:i:s.u');
                                                        reportOpt.endDts=Ext.Date.format(Ext.getCmp('endDate').getValue(),'Y-m-d\\TH:i:s.u');
                                                        reportOpt.previousDays=Ext.getCmp('previousDaysSelect').getValue();
                                                    }
                                                    
                                                    if(Ext.getCmp('waitSeconds').isVisible()){
                                                        reportOpt.maxWaitSeconds=Ext.getCmp('waitSeconds').getValue();
                                                    }
                                                    data.reportOption = reportOpt;
                                                    
                                                    if(data.scheduleIntervalDays === null)
                                                    {
                                                        var theData={};
                                                        if(Ext.getCmp('scheduleOptionsGrid').isVisible()){
                                                           var gridSelections=Ext.getCmp('scheduleOptionsGrid').getSelection();
                                                           

                                                           theData.report=data;
                                                           theData.reportDataId=[];
                                                           for(ctr=0;ctr<gridSelections.length; ctr++)
                                                           {
                                                               theData.reportDataId.push({id:gridSelections[ctr].data.componentId});
                                                           }

                                                           console.log("TheData",theData);
                                                           generateReport(theData);

                                                        }
                                                        else
                                                        {
                                                           theData.report=data;
                                                           theData.reportDataId=[];
                                                           console.log("TheData",theData);
                                                           generateReport(theData);
                                                        }
                                                    }
                                                    else {
                                                       console.log("Data",data);
                                                       scheduleReport(data);
 
                                                    }
                                                       
                                                }
                                            },
                                            {
                                                xtype: 'tbfill'
                                            },
                                            {
                                                text: 'Cancel',
                                                iconCls: 'fa fa-close',
                                                handler: function () {
                                                    Ext.getCmp('scheduleReportWin').destroy();
                                                }
                                            }
                                        ]
                                    }
                                ],
                                items: [
                                    {
                                        xtype: 'combobox',
                                        name: 'reportType',
                                        id: 'reportType',
                                        fieldLabel: 'Choose Report Type<span class="field-required" />',
                                        width: '100%',
                                        maxLength: 50,
                                        store: reportTypesStore,
                                        displayField:'description',
                                        valueField:'code',
                                        editable:false,
                                        allowBlank:false,
                                        listeners:{
                                            change: function(cb, newVal, oldVal, opts){
                                                //console.log("Changed Report Type:"+newVal);
                                                Ext.getCmp('reportFormat').getStore().removeAll();
                                                Ext.getCmp('reportFormat').clearValue();
                                                Ext.getCmp('reportFormat').getStore().getProxy().setUrl('../api/v1/resource/reports/'+encodeURIComponent(newVal)+'/formats');
                                                Ext.getCmp('reportFormat').getStore().load({
                                                    callback: function(records, operation, success){
                                                        //console.log('Records:',records);
                                                        if(records.length==1){
                                                            Ext.getCmp('reportFormat').setValue(records[0].data.code);
                                                        }
                                                    }
                                                });
                                                Ext.getCmp('scheduledHours').setValue('0');
                                                Ext.getCmp('reportFormat').setHidden(false);
                                                Ext.getCmp('scheduledHours').setHidden(false);
                                                Ext.getCmp('emailAddresses').setHidden(false);
                                                
                                                
                                                //Hide and clear data from all the form elements at first and turn them on based on what rtype is selected
                                                Ext.getCmp('filterForEntries').setHidden(true);
                                                Ext.getCmp('scheduleOptionsGrid').setHidden(true);
                                                Ext.getCmp('categorySelect').setHidden(true);
                                                Ext.getCmp('waitSeconds').setHidden(true);
                                                Ext.getCmp('startDate').setHidden(true);
                                                Ext.getCmp('endDate').setHidden(true);
                                                Ext.getCmp('previousDaysSelect').setHidden(true);
                                                Ext.getCmp('waitSeconds').setValue('');
                                                Ext.getCmp('filterForEntries').setValue('');
                                                Ext.getCmp('scheduleOptionsGrid').getSelectionModel().clearSelections();
                                                var dt = new Date();
                                                Ext.getCmp('startDate').setValue(dt);
                                                Ext.getCmp('endDate').setValue(dt);
                                                Ext.getCmp('previousDaysSelect').clearValue();
                                                
                                                var rType = Ext.getCmp('reportType').value;
                                                if(rType === "COMPONENT" || rType === 'CMPORG'|| rType === 'TYPECOMP'){
                                                    
                                                    Ext.getCmp('filterForEntries').setHidden(false);
                                                    Ext.getCmp('scheduleOptionsGrid').setHidden(false);
                                                }
                                                else if(rType === 'CATCOMP'){
                                                    
                                                    Ext.getCmp('categorySelect').setHidden(false);
                                                    Ext.getCmp('filterForEntries').setHidden(false);
                                                    Ext.getCmp('scheduleOptionsGrid').setHidden(false);
                                                }
                                                else if(rType === 'LINKVALID'){
                                                    
                                                    Ext.getCmp('waitSeconds').setHidden(false); 
                                                }
                                                else if(rType === 'SUBMISSION' || rType === 'USAGE'){
                                                    
                                                    Ext.getCmp('startDate').setHidden(false);
                                                    Ext.getCmp('endDate').setHidden(false);
                                                    Ext.getCmp('previousDaysSelect').setHidden(false);
                                                }
                                                else if(rType === 'USER' || rType === 'ORGANIZATION'){
                                                    //Do nothing just the base form which is already active.
                                                }
                                               
                                                
                                            }
                                        }
                                        
                                        
                                    },
                                    {
                                        xtype: 'combobox',
                                        name: 'reportFormat',
                                        id: 'reportFormat',
                                        fieldLabel: 'Choose Report Format<span class="field-required" />',
                                        width: '100%',
                                        maxLength: 50,
                                        store: reportFormatsStore,
                                        displayField:'description',
                                        valueField:'code',
                                        editable:false,
                                        hidden:true,
                                        allowBlank:false
                                    },
                                    {
                                        xtype: 'combobox',
                                        name: 'scheduledHours',
                                        id: 'scheduledHours',
                                        fieldLabel: 'How often to run the report?<span class="field-required" />',
                                        width: '100%',
                                        maxLength: 50,
                                        store: scheduleHowOften,
                                        displayField:'description',
                                        valueField:'code',
                                        editable:false,
                                        hidden:true,
                                        allowBlank:false,
                                        listeners:{
                                            change: function(cb, newVal, oldVal, opts){
                                                
                                                if(oldVal!==null && newVal === '0' && scheduleReportId){
                                                    Ext.toast('You cannot run that report now, you are editing a scheduled report. Click the Add + button to run a report now.');
                                                    Ext.getCmp('scheduledHours').setValue(String(scheduleData.data.scheduleIntervalDays));
                                                    return;
                                                } else if(newVal !== '0'){
                                                    
                                                    Ext.getCmp('filterForEntries').setHidden(true);
                                                    Ext.getCmp('scheduleOptionsGrid').setHidden(true); 
                                                }
                                                else{          
                                                    Ext.getCmp('filterForEntries').setHidden(false);
                                                    Ext.getCmp('scheduleOptionsGrid').setHidden(false); 
                                                }     
                                            }
                                        }
                                    },
                                    {
                                        xtype: 'textarea',
                                        name: 'emailAddresses',
                                        id: 'emailAddresses',
                                        fieldLabel: 'Enter email addresses separated by semi-colons.<span class="field-required" />',
                                        width: '100%',
                                        maxLength: 300,
                                        editable:true,
                                        hidden:true,
                                        allowBlank:false
                                    },
                                    {
                                        xtype: 'combobox',
                                        name: 'categorySelect',
                                        id: 'categorySelect',
                                        fieldLabel: 'Select Category<span class="field-required" />',
                                        width: '100%',
                                        maxLength: 50,
                                        store: scheduleCategoryStore,
                                        displayField:'description',
                                        valueField:'attributeType',
                                        editable:false,
                                        hidden:true,
                                        allowBlank:true
                                    },
                                    {
                                        xtype: 'textfield',
                                        name: 'waitSeconds',
                                        id: 'waitSeconds',
                                        fieldLabel: 'Enter how many seconds to wait (default: 5 sec, min 1 second up to max 300 seconds)',
                                        width: '100%',
                                        maxLength: 30,
                                        value: '5',
                                        editable:true,
                                        hidden:true,
                                        allowBlank: true,
                                        style:{
                                            marginTop:'20px'
                                        }
                                    },
                                    {
                                        xtype: 'datefield',
                                        name: 'startDate',
                                        id: 'startDate',
                                        fieldLabel: 'Start Date',
                                        width: '100%',
                                        format: 'm/d/Y',
                                        submitFormat: 'Y-m-d\\TH:i:s.u',
                                        editable:true,
                                        hidden:true,
                                        allowBlank: true,
                                        style:{
                                            marginTop:'20px'
                                        }
                                    },
                                    {
                                        xtype: 'datefield',
                                        name: 'endDate',
                                        id: 'endDate',
                                        fieldLabel: 'End Date',
                                        width: '100%',
                                        format: 'm/d/Y',
                                        editable:true,
                                        hidden:true,
                                        allowBlank: true
                                    },
                                    {
                                        xtype: 'combobox',
                                        name: 'previousDaysSelect',
                                        id: 'previousDaysSelect',
                                        fieldLabel: 'Previous Days',
                                        width: '100%',
                                        maxLength: 50,
                                        store: previousDaysStore,
                                        displayField:'days',
                                        valueField:'days',
                                        editable:false,
                                        hidden:true,
                                        allowBlank:true
                                    },
                                    {
                                        xtype: 'textfield',
                                        name: 'filterForEntries',
                                        id: 'filterForEntries',
                                        fieldLabel: '',
                                        emptyText:'Filter entries by name',
                                        width: '100%',
                                        maxLength: 30,
                                        editable:true,
                                        hidden:true,
                                        allowBlank:true,
                                        style:{
                                            marginTop:'20px'
                                        },
                                        listeners:{
                                            change: function(tb, newVal, oldVal, opts){
                                               Ext.getCmp('scheduleOptionsGrid').getStore().filter([
                                                   {
                                                       property: 'name',
                                                       value: tb.value
                                                   }
                                               ]);
                                            }
                                        }
                                    },
                                    {
                                        xtype: 'gridpanel',
                                        title: 'Report Options',
                                        id: 'scheduleOptionsGrid',
                                        store: 'scheduleOptionsStore',
                                        width: '100%',
                                        height: 200,
                                        columnLines: true,
                                        bodyCls: 'border_accent',
                                        selModel: {
                                            selType: 'checkboxmodel'
                                        },
                                        plugins: 'gridfilters',
                                        enableLocking: true,
                                        columns: [
                                            {text: 'Entry Name', dataIndex: 'name', flex:1, lockable: true,
                                                filter: {
                                                    type: 'string'
                                                }
                                            }
                                        ],
                                        hidden:true
                                    }

                                ]
                        }]                      
                    }).show(); 
                    
                    
                };
                
                
                
                //
                //
                //  TABS SETUP WITH PANELS
                //
                //
                
                
                var historyPanel = Ext.create('Ext.panel.Panel', {
                    title: 'History',
                    iconCls: 'fa fa-clock-o',
                    layout: 'fit',
                    items: [
                             historyGrid
                           ]
                });
                
                var scheduleReportsPanel = Ext.create('Ext.panel.Panel', {
                    title: 'Schedule',
                    iconCls: 'fa fa-calendar-plus-o',
                    layout: 'fit',
                    items: [
                             scheduleReportsGrid
                           ]
                });
                
               
                
                
                var reportTabPanel = Ext.create('Ext.tab.Panel', {
                    id: 'reportTabPanel',
                    title:'Manage Reports <i class="fa fa-question-circle"  data-qtip="System scheduled and hard reports" ></i>',
                    layout: 'fit',
                    items: [
                        scheduleReportsPanel,
                        historyPanel
                    ],
                    listeners: {
                        tabchange: function(tabPanel, newTab, oldTab, index){
                            
                            if(newTab.title === 'History'){
                               historyRefreshGrid(); 
                            }
                            else if(newTab.title === 'Schedule'){
                               scheduleReportRefreshGrid();
                            }
                        }
                    }
                });
                
                Ext.create('Ext.container.Viewport', {
                    layout: 'fit',
                    items: [
                        reportTabPanel
                    ]
                });
        });

        </script>

    </stripes:layout-component>
</stripes:layout-render>
